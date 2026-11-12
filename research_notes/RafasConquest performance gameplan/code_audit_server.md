# RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots

Scope: the mod's own server-side and shared (non-rendering) code on branch port-1.21.1-neoforge, read on 2026-11-10. Read-only; nothing was built or run. Code citations are `path:line`, relative to `src/main/java/rafasconquest/`. "Log" citations are the user's own game logs: the CurseForge instance `RafasConquest1.21Neo/logs/latest.log` (08 Nov 2026) and `logs/2026-11-05-*.log.gz` (05–06 Nov 2026). Save sizes come from `RafasConquest1.21Neo/saves/*/data/`. The cost estimates under Inferences are worked out from the code. None of them was profiled.

## Overall: which server/common hotspots matter most (ranked)?

### Takeaway
The mod's own battle code is cheap per tick. The logs measure it at 0.1–1.7 ms of a 38–70 ms server tick with about 200 real soldiers. The real risks are **one-tick spikes and main-thread blocking**: synchronous chunk loading or generation (siege survey, camps, battle ground), up to 160 soldier spawns in a single tick, an unbudgeted chunk-load queue that loops over every character in the realm per chunk, one ~40 ms POI region per tick while exploring, and up to 40 slow world-generator height reads in one tick. A few loops that are fine today grow badly (O(n²)) with very large battles, fleets or populations.

### Cited Findings
Ranked from most to least likely to matter. Each line gives where, how often, what it scales with, and a candidate fix.
1. **Siege survey loads or generates chunks on the server thread.** It runs inside `Wars.tick` (every 100 ticks) when armies meet near a fort with a player within 400 blocks. `SiegeSurvey.survey` is called synchronously ([politics/Wars.java:319-327]). It calls `level.getChunk` for every chunk in a 32-block ring around all four walls ([bridge/SiegeSurvey.java:253-273]), and again for every height sample ([bridge/SiegeSurvey.java:325-328]; samples every 2 blocks along each wall, [bridge/SiegeSurvey.java:134-148]). It scales with the fort's perimeter, so a capital is far worse than a castle. Fix: run the survey from a chunk-ticket plus `CompletableFuture` that waits for the chunks off-thread, or read unloaded columns from the generator as `Terrain` already does, then resume on the server thread.
2. **Battle start does heavy work on the server thread.** `begin()` runs on the server thread through `whenCompleteAsync(..., level.getServer())` ([bridge/RecruitsBattle.java:649-666]). It runs `GroundPlan.deploy`, then `predict()`: up to 20 full numeric battles when there are 200 blocks or fewer ([bridge/RecruitsBattle.java:675, 5424-5443]; `PREDICT_UP_TO_BLOCKS=200` at :157). It then runs `buildCamps()` ([bridge/RecruitsBattle.java:688, 4321-4356]), which calls `loadGround` → a synchronous `level.getChunk` on 3×3 chunks per camp, up to 6 camps ([bridge/RecruitsBattle.java:5497-5506]). Log: "camps pitched in 532 ms", and "Can't keep up! … 4773ms or 95 ticks behind" in the second the 10022-vs-5022 "Siege of Quarormark Woods" began (latest.log 08Nov2026 08:24:56.884–08:24:58.143). Fix: move `predict` off-thread (it is pure numbers), make the camp chunks' ticket wait asynchronous, and build camps over several ticks.
3. **Up to 160 soldiers spawn in one tick.** `SPAWNS_PER_SECOND = 160` ([bridge/RecruitsBattle.java:144]) is used inside `switchLive()`, which runs once a second from `second()` ([bridge/RecruitsBattle.java:1795-1797, 2081, 1166-1181]). Each spawn is a Recruits entity plus a horse for cavalry, gets dressed with items, and calls `addFreshEntity`. The first soldier of a block also triggers a synchronous 3×3 `getChunk` (`liveOf`→`loadGround`, [bridge/RecruitsBattle.java:996-1004]). Log: "[battle 10s] … 164 real soldiers … 69.7 ms/tick average, 347.1 ms worst"; sea battle "1502.7 ms worst" in its first 10 s (2026-11-05 logs, 23:02:46 and 23:32:27). Fix: spread spawns over the 20 ticks (about 8 per tick), and preload the ground through tickets a second ahead.
4. **The chunk-load queue is drained with no budget.** `drainBannerChunks()` empties the whole queue in one tick ([sim/ConquestServer.java:204-214]). Every city or keep chunk loaded runs three passes:
   - `CityBanners.paintChunk`, which copies the block entities and calls `wallAt` plus owner lookups for each banner ([sim/CityBanners.java:34-66]).
   - `CityGuards.postChunk`, which rebuilds `wallSegments` for every nearby city ([sim/CityGuards.java:116-147]).
   - `BarracksPeople.postChunk` → `nobles()`, which loops over **every person in the realm** and runs `cityCells()`, a linear scan of all ~100 seat cities, for each ([sim/BarracksPeople.java:103-163, 236-243]). Cost is O(people × cities) per chunk.

   `drainStructureLinks()` is also unbudgeted ([sim/ConquestServer.java:186-202]). Fix: cap work per tick (a time budget), precompute a person→city-cells array once per map (as `holdInChunk` already does for keeps, [sim/BarracksPeople.java:76-92]), and index the nobles to place by chunk.
5. **Lazy POI precompute costs one ~40 ms region per tick.** It runs one region per tick ([sim/ConquestServer.java:31, 144-148]); a region is 2048×2048 blocks ([core/WorldSettings.java:38]). Each farm candidate runs `Structure.findValidGenerationPoint` (biome and height sampling) on the server thread ([sim/PoiPrecomputer.java:94-120, 185-198]). Log: 60 regions in 2197 ms and 2754 ms at first start, about 37–46 ms per region (2026-11-05/06 logs). It recurs whenever a player crosses into new regions. Fix: a time budget per tick, or compute candidates off-thread (worldgen code is already thread-safe for this) and apply the results on the server thread.
6. **Distant battle blocks read the world generator on the server thread.** `snapshots()` → `groundY()` calls `getBaseHeight` for unloaded chunks, up to `GROUND_LOOKUPS_PER_SECOND = 40` per snapshot ([bridge/RecruitsBattle.java:336, 5522-5544]), and snapshots go out every 20 ticks ([bridge/RecruitsBattle.java:1811-1816]). The mod's own comment says each generator column "costs ~10 ms" with heavy terrain mods ([bridge/Terrain.java:201]). Fix: take heights from the survey `Terrain` already read for the battle area, or the BaseMap ground; move the remaining lookups to a background future.
7. **SeaCalm rescans unloaded chunks every tick near loaded edges.** `SeaCalm.own()` never caches a chunk that isn't loaded: it allocates `byte[256]` and calls `depth()` 256 times on every call ([core/SeaCalm.java:138-158]). `column()` checks a diamond of radius `REACH=33`, about 2,245 cells, per uncached column ([core/SeaCalm.java:15, 74-95]), and `guessed` stops caching ([core/SeaCalm.java:91-93]). Every sea-lane ship samples 5 heights × 4 columns per tick (`Swell.Ride.tick`, [core/Swell.java:184-200]; via `SableShips.ride`, [bridge/SableShips.java:157-161]). Ships are kept out to view distance + 32 blocks ([bridge/SableShips.java:43, 79-95]), so ships near the edge of loaded land sample unloaded chunks. Fix: check `hasChunk` once per chunk before the 256-column loop; cache "unknown" chunks for ~20 ticks.
8. **The naval AI and fleet spacing are O(n²) per second.** `captains()` → `claim()` copies and **sorts every enemy ship for each own ship**, with `Math.hypot` inside the comparator ([battle/FieldBattle.java:236-262, 375-389]). `keepShipsApart()` checks every pair in a fleet and allocates two `footprint` hulls per pair ([battle/FieldBattle.java:415-440]). `MAX_SOLDIERS = 100_000` ([battle/BattlePlan.java:19]), so up to about 5000 blocks a side. Fix: use the existing `BlockGrid` (`army.grid.nearest/within`, [battle/FieldBattle.java:2543-2574]) for side-claiming and spacing, and compute footprints once per second.
9. **All sea-lane ships are recomputed every tick.** `SableShips.tick` calls `SeaLanes.at(...)` every tick ([bridge/SableShips.java:62-98]). That builds a `Ship` record for every ship on every lane in the world, with 2–3 `String` concatenations each (`id()`, [core/SeaLanes.java:257-259]; `Lane.key()`, [core/SeaLanes.java:85-87]; `voyage()` builds `id` twice, [core/SeaLanes.java:188-196]). It then runs `nearestPlayer` over all players for each ship ([bridge/SableShips.java:101-107]). Log: 41–63 ports and 67–107 shipping routes per world. At 3–9 moored ships per port plus 4 per lane end ([core/SeaLanes.java:41, 153-154]), that is roughly 800–1,200 ships. The client calls the same method per frame ([client/DistantShips.java:73]), and both sides take the same `synchronized(port)` locks ([core/SeaLanes.java:112-121]). Fix: only compute ports or lanes whose bounds come within range of a player; cache the id strings per port.
10. **The strategy camera loads a second player's worth of ticking chunks.** Each camera update adds `addRegionTicket(TICKET, centre, viewDistance+1, …)` with a 200-tick timeout ([bridge/CameraView.java:28, 56-60]), and calls `map.move(player)`, which visits every tracked entity ([bridge/CameraView.java:61-63]). The mod's own comment says a region ticket at distance 2 makes the chunk "entity-ticking, as /forceload does" ([bridge/RecruitsBattle.java:5490-5492]). By that rule, distance 13 entity-ticks roughly a 23×23 chunk area around each camera spot, and old spots linger for 10 s. Fix: use a smaller ticking radius (or tickets at level 33 = loaded but not ticking) and send chunks only.

### Inferences
- The battle report's "ms/tick average" is measured from `ServerTickEvent.Pre` to the end of `Post` ([bridge/RecruitsBattle.java:1783-1823]), so it covers the whole server tick. With 160–230 real soldiers, the whole tick averaged 38–70 ms while the mod's own battle code took 0.1–1.7 ms (2026-11-05 logs). Most steady-state cost is therefore entity ticking (Recruits AI, vanilla movement and collision, Sable), not the mod's loops. The mod's main levers are the live cap (`MAX_LIVE_SOLDIERS = 300`, [bridge/RecruitsBattle.java:139]), the chunks it keeps ticking, and how spawns are paced.
- Items 1–6 are spikes, not steady load. They explain the "Can't keep up" bursts better than per-tick averages do.

### Gaps
- Nothing was profiled (no spark or JFR run; forbidden by the read-only constraint). The 10 ms-per-column generator cost is the mod's own comment, not a measurement. The SeaCalm and SeaLanes per-tick costs are estimates.
- No battle-progress report exists in the logs for the 10022-vs-5022 siege, so the steady numeric cost of a ~750-block battle is unmeasured.

## What runs every server tick or level tick, and how much does each do?

### Takeaway
Seven server-tick and four level-tick entry points run every tick. Most are gated to every 20–100 ticks and are cheap. The always-on per-tick costs are `SableShips.tick` (all lane ships), the battle tick while one runs, Gate Block entities, and the two unbudgeted chunk queues.

### Cited Findings
- `LevelTickEvent.Post` (overworld) → `ConquestServer.tick`, `Swell.weather`, `SableLink.tick` and `CityGates.tick` ([ConquestEvents.java:118-129]).
  - `ConquestServer.tick`: drain links, drain banner chunks, player region scan every 20 ticks, one region precompute per tick, catch-up, `dayStep` ([sim/ConquestServer.java:135-151]).
  - `SableLink.tick` → `SableShips.tick` every tick, plus `BattleShips`/`SiegeFrames` cleanup every 100 ticks when no battle runs ([bridge/SableLink.java:49-63]).
  - `CityGates.tick`: every 100 ticks; per player, cities in 96 blocks × 4 gates ([worldgen/CityGates.java:37-56]).
- `ServerTickEvent.Pre/Post` → `RecruitsBattle` start/end, `BattleMap.tick`, `Townsfolk.tick`, `Barks.tick`, `QuestProgress.tick`, `Wars.tick` and `PlaceMusic.tick` ([ConquestEvents.java:132-146]).
  - `RecruitsBattle.onServerTickEnd` with a battle: `second()` every 20 ticks; `sail()`, `works.tick()`, `council.tick()`, `turnToListeners()` and `fadeSome()` every tick; `driveUnits()` every 2 ticks; `trample()` every 4; snapshot and `holdChunks()` every 20 ([bridge/RecruitsBattle.java:1789-1823]).
  - `BattleMap.tick`: drains edits; JSON state only while the dev HTTP map is open ([dev/BattleMap.java:90-98]).
  - `Townsfolk.tick`: every 20 ticks, at most 2 districts (about 40 people each) ([npc/Townsfolk.java:26-30, 52-150]). Log: 0.004–0.087 ms/tick (latest.log 08Nov2026 08:24–08:40).
  - `Barks.tick`: every 20 ticks per player, `getEntitiesOfClass(Mob, 14-block box)`, then `ear.met.removeIf(... near.stream().noneMatch ...)`, which is O(met × near) ([npc/Barks.java:43-73]).
  - `QuestProgress.tick`: every 20 ticks, per player, per taken quest ([peerage/QuestProgress.java:21-46]).
  - `Wars.tick`: `MusterBars.update` every 20 ticks ([politics/Wars.java:106-108]; [politics/MusterBars.java:23, 33-81]), `FeudDuel.tick` every tick ([politics/FeudDuel.java:117-142]), `politics.advance` + `setDirty` + `PoliticsSync.toAll` every 100 ticks while any war or clash exists ([politics/Wars.java:110-125]).
  - `PlaceMusic.tick`: every 20 ticks per player, two structure-piece walks plus one packet ([PlaceMusic.java:47-54, 88-127]).
- `PlayerTickEvent.Post` → `Retinues.onPlayerTick`: every 40 ticks per player, per retinue member × every level `getEntity(UUID)` ([peerage/Retinues.java:44, 194-220]).
- Block entity tickers:
  - `GateBlockEntity.serverTick` runs every tick per loaded Gate Block → `GateDoors.tick` (gets each door's sub-level; builds a door at most once per 20 ticks) plus a button check every 200 ticks ([content/GateBlockEntity.java:115-129]; [bridge/GateDoors.java:58-80]).
  - `MarkerBlockEntity.serverTick` fires `CharacterMarkers.fire` every 40 ticks per armed marker ([style/MarkerBlockEntity.java:17, 40-46]). Markers of not-yet-supported types, or in unheld provinces, never remove themselves and refire forever ([style/CharacterMarkers.java:69-130]).
- Per-entity event hooks: `LivingIncomingDamageEvent` → `RecruitsBattle.adjustDamage`, O(1) map lookups ([ConquestEvents.java:165-168]; [bridge/RecruitsBattle.java:4075-4109]). `LivingDeathEvent` → `onDeath`, `QuestProgress.killed`, and `Retinues.onDeath`, which loops over all peers ([peerage/Retinues.java:223-234]). `ProjectileImpactEvent` and `ExplosionEvent.Detonate` → `SableLink.hitDoor` → `GateDoors.doorAt`, an O(1) plot lookup per block ([ConquestEvents.java:272-286]; [bridge/GateDoors.java:150-161]).

### Inferences
- `SableShips.tick` is the only always-on per-tick loop that scales with the whole world (all lane ships) rather than with what is near players.
- `Barks.tick` with a player inside a battle: the 14-block box can hold dozens of Recruits; `kind(e)` and the stream filters run once a second. This is small.

### Gaps
- How often Recruits' own `onWorldTickArrowCleaner` runs (see Mixins) was not confirmed. Its class has a `tickCounter` field and an `AllowArrowCleaning` option (strings in `recruits-1.21.1-1.15.2.jar`, `RecruitEvents.class`), but the bytecode was not decompiled.

## Where are full scans, nested loops, repeated lookups and per-tick allocations?

### Takeaway
The worst are O(people × cities) per city chunk loaded, the yearly O(P²)-style passes over characters, O(provinces) lookups repeated inside per-nation loops, and per-tick string and record churn in sea lanes. Battle code mostly uses a spatial grid; the exceptions are noted.

### Cited Findings
- `BarracksPeople.nobles`: for each queued chunk, every realm person × `cityCells()` (a linear scan of `map.cities()`) ([sim/BarracksPeople.java:103-163, 236-243]). `sanctumMembers` runs per chunk too, over all organizations ([sim/BarracksPeople.java:180-215]).
- `Politics.decide` → `claims()` runs for each county and each duchy, and each `Goal.provinces()` scans every province ([politics/Politics.java:294-349, 406-426]; [politics/Goal.java:9-32]). `claim()` uses `List.contains` inside the neighbour loop ([politics/Politics.java:430-466]). `canWage`/`levy` → `held()` scan all province holders on every call ([politics/MapLands.java:58-60, 201-215]). `neighbourNations` scans all provinces ([politics/Politics.java:502-516]). `aim()` is counties × provinces ([politics/Politics.java:555-589]). This runs once per in-game day for each nation not at war ([politics/Politics.java:264-273]). Log map size: 587–621 provinces, 150–154 counties, 30–34 duchies, 15 nations ("Drew the world map", "Nations: 15").
- Yearly character passes (`YEAR_DAYS = 24`, [people/Succession.java:26]; at each year turn, [people/Succession.java:341-350]):
  - `Marriages.year` scans every person for a match, for about 25% of people ([people/Marriages.java:18, 61-110]).
  - `Succession.uncover` scans all people within `HOOK_REACH = 3000` blocks for each schemer and calls `Realm.bond(i, j)`, a **linear scan of all bonds**, for each near pair ([people/Succession.java:230-233, 282-300]; [people/Realm.java:68-75]).
  - The people list never shrinks: "the dead stay in the list as DEAD" ([people/Succession.java:18, 373]).
- `SeaLanes.at` rebuilds every ship each tick (see ranked item 9). `SableShips.avoid()` is O(live ships) for each live ship, plus up to ~6 block-collision probes each tick ([bridge/SableShips.java:209-243]).
- Battle:
  - `measure()` builds maps and calls `centre()` repeatedly (O(blocks²) plus O(soldiers) per call) once a second ([bridge/RecruitsBattle.java:5281-5366]).
  - `frontRowFights()` is O(soldiers × nearby foes) with streams, once a second ([bridge/RecruitsBattle.java:1513-1596]).
  - `holdChunks()` boxes 9 `Long`s per real soldier once a second ([bridge/RecruitsBattle.java:5459-5485]).
  - `Live.living()` allocates a list on each call and is called many times a second.
  - The numeric battle rebuilds `BlockGrid` twice a second ([battle/FieldBattle.java:959, 986, 1015-1019]) and uses it for nearest-enemy queries ([battle/FieldBattle.java:2543-2574]). The envelopment loop is still front × all enemy blocks ([battle/FieldBattle.java:2034-2041]); `general()` scans both armies ([battle/FieldBattle.java:1532-1550]).
- `PlaceMusic.of` and `inLandmark`: `getAllStructuresAt` + `getStructureWithPieceAt`, then a walk over every piece of the start, twice per player per second ([PlaceMusic.java:88-127]).
- `CityLayout` lookups allocate a new `ArrayList` and a `Site` per call (`allSitesInBox`, [core/CityLayout.java:423-431]; `CityIndex.inBox` allocates a list plus an `int[]` per city through `bounds()`, [worldgen/CityIndex.java:34-59]). They are called from worldgen per chunk, per tree (mixin), and per banner.

### Inferences
- Per day: about 15 nations × (150 counties × ~600 provinces + 30 duchies × ~750) plus `held()` scans comes to roughly 3–5 million simple operations. That is a few ms once per 20 real minutes, so it is a low priority. Caching county→provinces (rebuilt in `MapLands.regroup`) and a holder count per nation (updated in `transfer`, [politics/MapLands.java:83-86]) would remove most of it.
- Yearly `uncover` cost grows with schemers × nearby people × bonds. With low-thousands of people and bonds it could be a single-tick spike of hundreds of ms every 8 real hours (24 days × 20 min). A `HashMap<Long, Bond.Kind>` for `Realm.bond`, plus a spatial bucket of people, makes it near-linear.

### Gaps
- The realm's population (people, bonds) was not found in any log, so the yearly-pass estimate is unquantified.

## How do world generation and structure placement work, and what do they cost?

### Takeaway
City structures are planned once per city on a dedicated quarter-of-cores pool and cached. Pieces stay inside the chunk box and use `setBlock` flag 2 (no neighbour updates), which is the right pattern. The costs are a one-time ~40 s world-map draw on the server thread, multi-second city terrain plans (up to 18.5 s) held inside `ConcurrentHashMap.computeIfAbsent`, per-chunk rebuilding of slope-tile lists, and a filesystem `stat` on every template read.

### Cited Findings
- **World map:** drawn on the server thread at the first `LevelEvent.Load` (`loadOrDrawNow`, [ConquestEvents.java:54]; [map/BaseMapStore.java:95-105]) using half the CPU's threads (`drawGently`, [map/BaseMapStore.java:130-145]). Log: "Drew the world map: 1016 x 1016 pixels of 63 blocks … in 40340 ms (1829 KB)" on [Server thread] (2026-11-05). Other runs took 36931–39243 ms. It is saved and reloaded afterwards ([map/BaseMapStore.java:145-175]).
- **City terrain plan:** reads ground heights over the city box on a `ForkJoinPool(cores/4)` ([worldgen/CityGridBuilder.java:129-159]). It is cached up to 256 plans and then cleared ([worldgen/CityGridBuilder.java:76-102]), but computed **inside `PLANS.computeIfAbsent`** ([worldgen/CityGridBuilder.java:101]). Log: 93 plans; median 83 ms, p90 436 ms, max 18534 ms. Threads: 62 "DH Turbo Structure Prefetch", 24 "DH Turbo Worker", 5 Worker-Main, 2 DH-World Gen (instance logs). The Javadoc says the mapping function "should be short and simple" because other threads' updates to the same bin may block ([ConcurrentHashMap Javadoc](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html#computeIfAbsent(K,java.util.function.Function))). The same pattern appears in `RoadNetwork.reading`, which makes 128 `getBaseHeight` calls inside `readings.computeIfAbsent` ([worldgen/RoadNetwork.java:181-200]), in `stretches.computeIfAbsent` ([worldgen/RoadNetwork.java:80-83]), and in `CityPlan.PLANS` ([style/CityPlan.java:152-168]).
- **Structure placement checks run per chunk:** `CityPlacement.isPlacementChunk` → `slopeTilesInChunk` rebuilds the **full slope, annex and harbour tile list of every city within ~MAX_HALF_EXTENT+128+reach** for each chunk asked ([worldgen/CityPlacement.java:44-56]; [core/CityLayout.java:515-557]). `districtInChunk` builds every city's district list per chunk ([core/CityLayout.java:468-493]). `DojoPlacement` scans all dojos per chunk ([worldgen/DojoPlacement.java:33-44]). The country and farm checks are O(1) or hash-based ([worldgen/CountryIndex.java:28-30]; [core/FarmLayout.java:32-35]).
- **Terrain pieces** are bounded to the chunk box with flag 2 ([worldgen/CityTerrainPiece.java:152-212]). But `CityPlan.Plan.top(x, z)` loops over all styled pieces for each column ([style/CityPlan.java:106-114]), called per column at [worldgen/CityTerrainPiece.java:176].
- **Roads:** `RoadFeature.place` runs per chunk ([worldgen/RoadFeature.java:55-123]): stretches per column, `weedCities` per chunk ([worldgen/RoadFeature.java:259-276]), and `keeps()`, which scans every settlement and dojo on the map per road chunk instead of using `CountryIndex` ([worldgen/RoadFeature.java:291-305]).
- **Vegetation mixin:** every `TreeFeature`/`VinesFeature.place` runs `RoadFeature.inCity` → `CityIndex.inBox`, which allocates ([mixin/CityVegetationMixin.java:51-58]; [worldgen/RoadFeature.java:279-288]).
- **Templates:** `StyleLibrary.read` calls `Files.getLastModifiedTime` on **every** tile or template request. It uses a non-atomic get-then-put, so two worldgen threads can both parse and datafix the same large NBT file ([style/StyleLibrary.java:437-466]). There are 81 `.nbt` files totalling 8.9 MB in `mods/RafasConquest/structures` (instance folder).
- **Synchronous chunk loads outside worldgen:** `CityGates.fitAll` (an admin command; [worldgen/CityGates.java:60-77]; [command/PveCommands.java:421]); `Wars.toSurface` for muster teleports and the client teleport request ([politics/Wars.java:406-410]; [net/ConquestNetwork.java:680]); `FeudDuel` ([politics/FeudDuel.java:92]); `CityManager` ([peerage/CityManager.java:50]). These are one-off and player-triggered.
- `HorizontalStretch` rescales the noise router once at server start, with no per-sample hook ([worldgen/HorizontalStretch.java:37-46]).

### Inferences
- Most city planning runs on Distant Horizons' threads, so DH distant generation is a major consumer of the mod's worldgen code. The mod already pauses DH generation during battles (`DistantGeneration.pause`, [bridge/RecruitsBattle.java:648]).
- Caching `slopeTiles(city)` per `Site`, a bounded map like `CityPlan.OUTSETS` ([style/CityPlan.java:539-542]), would remove most per-chunk allocation in placement checks.
- Moving the plan computation out of `computeIfAbsent` (a future stored in the map, or a per-key lock) removes cross-city stalls between worldgen threads.

### Gaps
- `StylePiece.postProcess` and `CountryPiece`, `HarbourCutPiece` and `FarmPiece` were not read line by line. Only their dependencies (`StyleLibrary`, `CityPlan.top`) were.

## How do the simulation and daily-decision systems schedule work? Do they spike?

### Takeaway
Work is keyed to the day counter and to fixed tick moduli, so it lands on single ticks. The day rollover runs the economy, all nations' decisions, feuds and succession in one tick (up to 3 days at once after a clock jump). The year turn adds marriages and schemes. Wars move every 100 ticks. Nothing is spread across ticks.

### Cited Findings
- `dayStep`: `EconomyTick.runDay` (O(POIs), [sim/EconomyTick.java:16-32]) for each missed day, then `Wars.day` for at most `POLITICAL_DAYS = 3` days in the same tick ([sim/ConquestServer.java:170-184]). Offline catch-up runs 50 days per tick (`CATCH_UP_DAYS_PER_TICK`, [core/DayClock.java:8]; [sim/ConquestServer.java:154-168]).
- `Wars.day` → `Politics.day` (truces, then each nation's `decide` in shuffled order, feuds, succession deaths and weddings) → `Peerage.inherited` → `setDirty` → `claimCities` → `heralds` → `PoliticsSync.toAll` ([politics/Wars.java:76-101]; [politics/Politics.java:249-287]; [politics/Feuds.java:160-193]).
- Year turn inside `Succession.day`: `apply(realm)`, `Marriages.year` and `uncover` ([people/Succession.java:341-350]).
- Wars march every `MARCH_TICKS = 100` ([politics/Wars.java:42, 110-124]). When armies meet near a player, `fight` → siege survey and battle start run in that same tick ([politics/Wars.java:286-373]).
- Log: 624 POIs and 15 factions (dev run log 06Nov2026), so the economy pass is trivial.

### Inferences
- Spreading per-nation `decide` over the first N ticks of a day (one nation per tick) and moving the yearly passes to a background copy would flatten the day and year spikes. `Politics` is plain data, so a background job that applies results on the server thread is feasible.

### Gaps
- How long `Politics.day` and the year turn take was not measured; no log line times them.

## Saved data: how big, how often written, is everything re-serialized?

### Takeaway
Saved data is tiny and written only at vanilla autosave when dirty, so it is not a concern. The one large file, the 1.8 MB world map, is written once per world.

### Cited Findings
- `ConquestSavedData.save` rewrites settings, factions, POIs, precomputed regions and spawned guards on each save ([data/ConquestSavedData.java:173-187]). On disk, `rafasconquest_world.dat` is 3,241–7,293 bytes, `rafasconquest_politics.dat` 530–1,552 bytes, `rafasconquest_peerage.dat` 223 bytes, `rafasconquest_ranks.dat` 110 bytes, and `rafasconquest_city_gates.dat` 69–87 bytes (instance saves).
- `rafasconquest_basemap.bin` is 1.77–1.98 MB, Deflater-compressed ([map/BaseMap.java:96-99]), written once after drawing ([map/BaseMapStore.java:165-170]).
- `setDirty`/`changed()` appear at 36 call sites. `Wars.tick` sets politics dirty every 100 ticks during wars ([politics/Wars.java:120]), which only flags it for the next autosave.

### Inferences
- `spawnedGuards` and `precomputedRegions` grow with exploration ([data/ConquestSavedData.java:184-185]) but stay small (one `long` each).

### Gaps
- None material.

## Networking: what is sent, how big, how often, to whom?

### Takeaway
The steady senders are the battle snapshot (every second, to every player in the level, ~50 bytes per formation), politics (every 5 s during wars and on events, to all players, including a growing succession blob), and the place/music packet (every second, per player, even when nothing changed). The world map is 1.8 MB, sent on request.

### Cited Findings
- `BattleSnapshot`: every `SNAPSHOT_TICKS = 20` to `level.players()` ([bridge/RecruitsBattle.java:127, 1811-1816]). Each formation carries varint id, byte, 2×int colours, varint alive, **double x**, float y, **double z**, 4 floats, flags, a soldier-id varint array and a varint leader ([net/ConquestNetwork.java:917-946]). Every standing block is included, real or not ([bridge/RecruitsBattle.java:5552-5593]).
- `Volleys` every second to `level.players()` ([bridge/RecruitsBattle.java:4044]; [net/ConquestNetwork.java:893-910]).
- `PoliticsView`: wars, armies, alliances, the `conquered` map, 12 news strings, clashes, and `succession().encode()` ([politics/PoliticsSync.java:18-68]; [net/ConquestNetwork.java:410-430]). `encode` writes every character changed since the map was drawn, plus deaths and hooks ([people/Succession.java:446-471]). It is sent to all players every 100 ticks during wars ([politics/Wars.java:124]) and from 10 other call sites ([politics/Wars.java:100, 430, 454, 559, 592, 631, 765, 807, 849]; [politics/FeudDuel.java:184]).
- `PlaceMusic`: one packet per player per second, unconditionally ([PlaceMusic.java:47-54]).
- World map: 256 KB parts ([net/ConquestNetwork.java:30, 1018-1024]), all queued in one call per request ([map/BaseMapStore.java:113-124]).
- Each message goes through `PacketDistributor.sendToPlayer` per player, wrapped once ([net/SimpleChannel.java:82-84]).

### Inferences
- About 750 blocks (the 15k-soldier siege) is roughly 38–40 KB per snapshot per player per second. A 100k-soldier battle (≈5000 blocks a side) would be about 0.5 MB/s per player. Quantizing x/z to floats or shorts, sending deltas, and sending only to players within the battle's reach would cut this 3–10×.
- The succession blob grows without bound over play. Sending it only when its version changes would remove most of the politics traffic.

### Gaps
- Whether the client caches the world map across sessions (so the 1.8 MB is not re-requested) is client code and was not checked.

## Mixins on server paths: what they hook, how hot, added cost

### Takeaway
The two camera redirects sit on very hot vanilla paths but add only a `HashMap.isEmpty()` check when nobody uses the strategy view. That is negligible. The real cost of the camera feature is the chunk tickets it adds (ranked item 10). The arrow-cleaner redirect swaps a bounded box query for a full scan of the level's entities.

### Cited Findings
- `ChunkMapCameraMixin` redirects `ServerPlayer.chunkPosition()` inside `ChunkMap.updateChunkTracking` ([mixin/ChunkMapCameraMixin.java:29-34]). `TrackedEntityCameraMixin` redirects `ServerPlayer.position()` inside `ChunkMap$TrackedEntity.updatePlayer` ([mixin/TrackedEntityCameraMixin.java:72-77]). Both call `CameraView.of`, which returns at once when `CAMERAS.isEmpty()` and otherwise does a UUID hash lookup plus a dimension check ([bridge/CameraView.java:39-42]).
- `ChunkMapAccess` is an invoker only ([mixin/ChunkMapAccess.java:9-13]).
- `CityVegetationMixin`: HEAD inject on `TreeFeature`/`VinesFeature.place` (worldgen threads) → a `CityIndex` box query per placement ([mixin/CityVegetationMixin.java:51-58]).
- `RecruitsArrowCleanerMixin` replaces Recruits' `getEntitiesOfClass(AbstractArrow, worldBorderBox)` with `server.getEntities(EntityTypeTest.forClass(type), e -> true)` copied into a new `ArrayList`. That visits every entity in the level ([mixin/RecruitsArrowCleanerMixin.java:102-109]).
- The client mixins (`SodiumShader*`) are out of scope ([resources/rafasconquest.mixins.json]).

### Inferences
- `TrackedEntity.updatePlayer` runs for each (moving entity × player) pair. With 300 soldiers changing chunk sections it is called thousands of times a second, but one `isEmpty()` per call is nanoseconds.
- How much the arrow-cleaner redirect costs depends on how often Recruits runs it. If it runs every tick, the full-level scan becomes O(all entities) per tick during battles.

### Gaps
- The cadence of Recruits' `onWorldTickArrowCleaner` was not confirmed (see the tick-inventory gaps).

## Memory: unbounded static caches, leaked Level/Entity references, large arrays

### Takeaway
There is one clear leak pattern (a `WeakHashMap` whose value captures its own key), several static maps that keep Sable sub-levels or entities after the server stops, and about 15 MB of world-map arrays held for the session. The other caches are bounded or tied to the session.

### Cited Findings
- **Leak:** `Sea.calms` is a `WeakHashMap<Level, SeaCalm>`. Its value's lambda `(x, z) -> depth(l, x, z)` captures the key `Level` ([bridge/Sea.java:18-25]). The Javadoc warns that values must not "strongly refer to their own keys, either directly or indirectly, since that will prevent the keys from being discarded" ([WeakHashMap Javadoc](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/WeakHashMap.html)). It is used on the server through `SableShips.ride` ([bridge/SableShips.java:157-161]) and on the client too, so each world joined (server and client levels) stays reachable for the JVM's lifetime.
- **Static references not cleared on stop:**
  - `SableShips.live` (`HashMap<String, ServerSubLevel>`, [bridge/SableShips.java:55]), `BattleShips.ships` ([bridge/BattleShips.java:47]) and `SiegeFrames.frames` ([bridge/SiegeFrames.java:45]) hold sub-levels, and through them their level.
  - `FeudDuel.current` holds two entities and is only cleared when a duel ends ([politics/FeudDuel.java:76, 127, 163]).
  - `onServerStopping` clears only `RecruitsBattle`, `BattleMap` and `Townsfolk` ([ConquestEvents.java:148-153]).
  - `GateDoors.joints` is a `WeakHashMap<ServerSubLevel, RotaryConstraintHandle>` ([bridge/GateDoors.java:50]). Whether the handle refers back to the sub-level is unverified.
- **Large arrays:** `BaseMap` holds `pixels`, `provinces` and `ground` as `int[]` and `wet` as `boolean[]` at 1016×1016 ([map/BaseMap.java:32-36]; log size 1016×1016), plus the encoded 1.8 MB copy kept for resends ([map/BaseMapStore.java:41]).
- **Template cache:** every parsed structure template stays in `StyleLibrary.TEMPLATES` for the session ([style/StyleLibrary.java:63, 459]); 81 files, 8.9 MB on disk.
- **Bounded or swept caches** (fine): `CityGridBuilder.PLANS`/`PLATFORMS` ≤256 ([worldgen/CityGridBuilder.java:76-77, 97-99, 118-120]); `Succession.DRAWN` ≤8 ([people/Succession.java:34, 330-336]); `TitleMap.DRAWN` ≤8 ([core/TitleMap.java:131-146]); `SeaCalm` swept every 600 ticks ([core/SeaCalm.java:18, 46-53]); `RoadNetwork` stretches/readings per map ([worldgen/RoadNetwork.java:94-99]); `SableShips.rides` is weak and its values don't hold the key ([bridge/SableShips.java:57]).
- **Growing per session or per world:**
  - `Barks.EARS[player].heard` keeps every entity id ever heard until logout ([npc/Barks.java:32-38, 91]).
  - `CapitalLayout.MOVED` is never cleared ([core/CapitalLayout.java:32, 118]).
  - The realm's people list only grows ([people/Succession.java:18]).
  - `Terrain.cells` and `groundCache` grow per battle and are freed with it ([bridge/Terrain.java:40]).

### Inferences
- `BaseMap` resident size is ≈ 3×4.1 MB + 1 MB + 1.8 MB ≈ 15 MB on the server, plus a decoded copy on the client in singleplayer. Storing `ground` as `short[]` and `provinces` as `short[]` (there are fewer than 32k provinces) would save about 4 MB.
- Leaked `ServerLevel`s matter most for the user's "leave and rejoin" testing loop on a memory-limited PC.

### Gaps
- The heap size of the parsed templates and of Sable sub-levels was not measured (no heap dump).

## Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI

### Takeaway
At today's sizes (12–16 real blocks, ~200 real soldiers) the battle bridge costs about 0.2 ms/tick. Its risks are the 1 Hz bursts (spawns, generator reads, chunk tickets), the start-of-battle main-thread work, and O(n²) naval and fleet loops that only bite with very large fleets. Pathfinding is mostly left to Recruits. The mod issues `moveTo` only for player-led units (every 10 ticks per soldier when more than 8 blocks off) and for hunters.

### Cited Findings
- Log (2026-11-05): "204 real soldiers in 14 blocks … 50.3 ms/tick average … of which battle 0.2 ms/tick (numbers 0.0, switching 0.0, snapshots 0.0)" and "231 real soldiers in 16 blocks … 38.6 ms/tick average … battle 0.2 ms/tick".
- Live cap 300 (riders count as 2); blocks turn real within 96 blocks and back to numbers beyond 128 ([bridge/RecruitsBattle.java:133-144]).
- `switchLive` once a second: all blocks × watchers, a candidate list, a sort ([bridge/RecruitsBattle.java:1088-1197]).
- Chunk tickets: a 3×3 ticket at distance 2 with forced ticks around every real soldier, refreshed each second ([bridge/RecruitsBattle.java:123-125, 5459-5494]).
- Sea: `sail()` every tick builds helms for formations afloat near watchers ([bridge/RecruitsBattle.java:1843-1922]). `BattleShips.tick` every tick scans **all** sub-levels' user tags and pairs ships for rams ([bridge/BattleShips.java:56-133]). `keepDeck` runs every 5 ticks per live ship ([bridge/RecruitsBattle.java:1915-1917, 1958-1986]); `board()` runs every tick ([bridge/RecruitsBattle.java:1993-2004]).
- Sieges: `SiegeWorks.tick` every tick (frames and climbers, [bridge/SiegeWorks.java:145-181]); `second` once a second ([bridge/SiegeWorks.java:110-142]).
- Player units: `driveUnits` every 2 ticks reads `getHeight` and may call `getNavigation().moveTo` every 10 ticks per soldier ([bridge/RecruitsBattle.java:3056-3130]).
- Numeric engine: grid-indexed nearest queries ([battle/FieldBattle.java:2098-2116, 2543-2574]). The O(n²) exceptions are `captains/claim` ([battle/FieldBattle.java:236-262, 375-389]), `keepShipsApart` ([battle/FieldBattle.java:415-440]) and envelopment ([battle/FieldBattle.java:2034-2041]).

### Inferences
- With 750 blocks the remaining O(front × enemy) loops cost about 40k distance checks a second. That is fine. At `MAX_SOLDIERS = 100_000` they become millions per second, and naval `claim()` sorting becomes the dominant per-second spike.
- The numeric engine's measured "0.0 ms/tick" comes from 12–16-block battles only.

### Gaps
- No timing exists for a 500+-block land battle or a large fleet battle.

## Which areas were checked and look fine?

### Takeaway
Leave these alone. Their per-call cost is O(1), they are gated by tick moduli, or they are bounded and measured cheap.

### Cited Findings
- Townsfolk: at most 2 districts, every 20 ticks, measured 0.004–0.087 ms/tick ([npc/Townsfolk.java:26-30]; latest.log).
- Saved data sizes and save path ([data/ConquestSavedData.java:173-187]; instance saves).
- Gate door hit and lookup, O(1) ([bridge/GateDoors.java:150-187]). Damage, death and interact hooks, O(1) ([bridge/RecruitsBattle.java:523-530, 4075-4109, 1290-1295]).
- Camera mixins when unused ([bridge/CameraView.java:39-42]).
- `CityIndex`/`CountryIndex` spatial indexes ([worldgen/CityIndex.java:14-59]; [worldgen/CountryIndex.java:28-30]); `RoadNetwork.byChunk` index ([worldgen/RoadNetwork.java:101-124]).
- Terrain pieces use chunk-box bounds and flag 2 ([worldgen/CityTerrainPiece.java:152-212]). City plans run on a capped pool ([worldgen/CityGridBuilder.java:129-148]). The world map draws on half the cores ([map/BaseMapStore.java:130-145]). The battle survey runs off-thread across cores ([bridge/Terrain.java:88-117]).
- `HorizontalStretch` costs nothing per sample ([worldgen/HorizontalStretch.java:37-46]).
- `EconomyTick` (624 POIs a day), `MusterBars`, `QuestProgress`, `Retinues`, `FeudDuel.tick`, `WarCouncil.tick` ([bridge/WarCouncil.java:342-358]) and `CityGates.tick` (every 100 ticks).
- `SiegeWorks`, `GateBlockEntity` and `MarkerBlockEntity` tickers (light work per tick; markers fire every 40 ticks).
- `Politics.advance` (march maths per war, [politics/Politics.java:717-758]).

### Inferences
- `RecruitsBattle.measure`, `frontRowFights` and `holdChunks` allocate more than they need to, but with ≤300 real soldiers they stay within the measured 0.2–1.7 ms/tick. Rewriting them is not worth it before the spike fixes.

### Gaps
- `GrandStrategy.view`, `Boards` and `FactionInfo.collect` run only on request and were not costed.
