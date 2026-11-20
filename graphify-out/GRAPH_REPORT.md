# Graph Report - RafasConquest  (2026-11-20)

## Corpus Check
- 651 files · ~554,359 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 8460 nodes · 27531 edges · 415 communities (144 shown, 271 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3494 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `88409741`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- .measure
- .fire
- net.minecraft.nbt.CompoundTag
- org.junit.jupiter.api.Test
- org.joml.Vector3d
- BattleScreen
- Front
- net.minecraft.core.BlockPos
- PoliticsTest
- net.minecraft.world.level.block.state.BlockState
- ConquestEvents
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- War
- Point
- PveCommands.java
- BattleMap.java
- Morale
- RafasConquest Project Site
- Person
- RecruitsBattle
- net.neoforged.bus.api.SubscribeEvent
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- list
- .view
- CityHud
- Tier
- net.minecraft.core.Vec3i
- RoadNetwork
- GateBlockEntity
- PlaceMusic.java
- .allSitesInBox
- BattleShips
- PveCommands
- .map
- CityPlan
- BaseMapGenerator.java
- Ranks
- WaterSwell
- .findGenerationPoint
- rafasconquest.faction.Faction
- net.minecraft.client.gui.GuiGraphics
- ConquestServer
- SiegeWorks
- ArrowVolleys
- rafasconquest.battle.FormationSnapshot
- Personality
- SeaLanes
- .sendToServer
- ConquestServer.java
- BattlePlan
- Kit
- DistantOcclusion
- .render
- CityStyles
- MarkerRenderer.java
- CityTerrain
- ConquestSavedData
- HorizontalStretch
- WorldSettings
- TitleMap
- Bodies
- StyleLibrary
- .roadStart
- MapPreview.java
- Row
- School
- DistantShips
- NobleTitle
- MapLands
- WarCouncil
- .step
- net.minecraft.world.item.ItemStack
- RecruitFigures
- BaseMap
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- Candidate
- RoadLayer
- Recruiter
- CityTerrainPiece
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- CityVariant
- DialogueScreen
- OrganizationScreen
- CourtSet
- FarFlags
- .of
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- GatePlanksBlock.java
- FactionInfoScreen
- Person
- Hull
- .enlist
- .mouseClicked
- FaceOffScreen
- How Total War calculates battles between units
- LabelLayer
- .map
- CityPanel
- NationScreen
- .encode
- Faction
- Arm
- .block
- .onClientExtensions
- CameraView
- GrandStrategyScreen
- Cloth
- CityManagerScreen
- TechTreeScreen
- Roads
- DayClockTest
- MapContext
- FigureGear
- Terrain
- TitleOwnersScreen
- Lands
- .shift
- org.jetbrains.annotations.Nullable
- MediumCannonEntity
- net.minecraft.world.level.block.entity.BlockEntity
- MarkerSpec
- SablePhysics
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .choose
- .onLogout
- .view
- MarkerScreen
- Barracks
- CLAUDE.md
- Cited Findings
- com.google.gson.JsonObject
- Builder
- Call
- net.minecraft.sounds.SoundEvent
- org.spongepowered.asm.mixin.Mixin
- .aWoodNearAWingHidesAnAmbushThatStaysPutUntilItStrikes
- net.minecraft.world.phys.Vec3
- BattleWallBannerBlock
- DistantHorizonsLink
- /pve debug commands
- net.minecraft.server.level.ServerPlayer
- com.mojang.blaze3d.vertex.PoseStack
- UnitOrder
- HornCall
- .noble
- .say
- ArmySpawnScreen
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Settlement
- RecruitFigures.java
- FieldRules
- .wallSegments
- ClientPolitics
- .rightClick
- GrappleHook
- DistantArmyRenderer
- Body
- CapeCurio.java
- CombatMode
- Trait
- ClientModEvents
- Doctrine
- WeaponSkills
- ProvinceMap
- HorizontalStretch
- DistantHorizonsArmies
- Profiling run sheet (baseline)
- Duty
- .boundsAt
- GateScreen
- BattleSnapshot
- .findGenerationPoint
- ConquestWorldScreen
- City
- .measure
- net.minecraft.client.Minecraft
- .touches
- Kind
- BackBannerLayer.java
- CityGridBuilder
- ClientScreens
- .generate
- TitleLayer
- CannonShots
- .aNewCardsFileAddsACardThatDeploysByItsKind
- FactionMapScreen
- BoardScreen
- Phase
- UnitMode
- Machine
- .draw
- Missile
- .accept
- .onLevelLoad
- Build
- net.minecraft.core.BlockPos
- WallTemplates
- OarFigures
- Deflects
- Succession
- BattleKeepInventory
- BlockGrid
- ShareLiveTest
- PoliticsData
- FormationSnapshot
- FieldBlock
- FleetRole
- Kind
- RafasConquest.java
- ConquestConfig
- ShipKind
- SwingModes
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- ClientBarks
- Role
- net.minecraft.core.Direction

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 323 edges
2. `FieldBlock` - 245 edges
3. `FieldBattle` - 238 edges
4. `ConquestNetwork` - 143 edges
5. `FieldArmy` - 140 edges
6. `Point` - 125 edges
7. `Lands` - 94 edges
8. `BattleScreen` - 88 edges
9. `Siege` - 86 edges
10. `Politics` - 84 edges

## Surprising Connections (you probably didn't know these)
- `Stage 3: client frame time (2–3 jars, only where stage 0 showed a cost)` --references--> `BaseMapLayer`  [INFERRED]
  reports/RafasConquest optimization plan 2026-11-18.md → src/main/java/rafasconquest/client/map/BaseMapLayer.java
- `Cited Findings` --references--> `MapView`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/MapView.java
- `Cited Findings` --references--> `ChunkMapAccess`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/mixin/ChunkMapAccess.java
- `Cited Findings` --references--> `ChunkMapCameraMixin`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/mixin/ChunkMapCameraMixin.java
- `Stage 3: client frame time (2–3 jars, only where stage 0 showed a cost)` --references--> `RoadLayer`  [INFERRED]
  reports/RafasConquest optimization plan 2026-11-18.md → src/main/java/rafasconquest/client/map/RoadLayer.java

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Daily economy tick flow** — recruits_pve_addon___design_doc_economy_loop, recruits_pve_addon___design_doc_daily_tick, recruits_pve_addon___design_doc_day_clock, recruits_pve_addon___design_doc_offline_catchup, recruits_pve_addon___design_doc_poi, recruits_pve_addon___design_doc_strength_pool [EXTRACTED 1.00]
- **Milestone 1 SavedData model** — recruits_pve_addon___design_doc_saved_data, recruits_pve_addon___design_doc_world_settings, recruits_pve_addon___design_doc_faction_model, recruits_pve_addon___design_doc_poi_model, recruits_pve_addon___design_doc_enums, recruits_pve_addon___design_doc_day_clock [EXTRACTED 1.00]
- **Battle map poll-render-draw loop** — src_main_resources_assets_rafasconquest_web_battle_map_poll, src_main_resources_assets_rafasconquest_web_battle_map_render, src_main_resources_assets_rafasconquest_web_battle_map_showselection, src_main_resources_assets_rafasconquest_web_battle_map_draw, src_main_resources_assets_rafasconquest_web_battle_map_http_api [EXTRACTED 1.00]
- **World map planning: provinces to titles to cities, nations and roads** — readme_provinces, readme_land_titles, readme_seat_cities, readme_countryside, readme_nations, readme_roads [EXTRACTED 1.00]
- **Styled Palace and Barracks staffed by Character Markers** — src_main_resources_rafasconquest_structures_readme_palace, src_main_resources_rafasconquest_structures_readme_small_barracks, src_main_resources_rafasconquest_structures_readme_character_marker, src_main_resources_rafasconquest_structures_readme_building_style [EXTRACTED 1.00]
- **Battle frame coordinate transforms** — src_main_resources_assets_rafasconquest_web_battle_map_frame, src_main_resources_assets_rafasconquest_web_battle_map_world, src_main_resources_assets_rafasconquest_web_battle_map_toscreen, src_main_resources_assets_rafasconquest_web_battle_map_dirframe, src_main_resources_assets_rafasconquest_web_battle_map_corners [INFERRED 0.85]
- **Muster to battle flow** — rafasconquest_commands_pve_politics_war, rafasconquest_commands_pve_muster, rafasconquest_commands_pve_politics_muster_finish, rafasconquest_commands_pve_politics_muster_field, rafasconquest_commands_muster_bar, rafasconquest_commands_war_camp_option [INFERRED 0.85]
- **Hybrid live/numeric battle with distant rendering** — readme_battles_as_numbers, readme_live_area, readme_calibration, readme_distant_figures, readme_arrow_volleys, readme_battle_sounds [INFERRED 0.85]
- **Player military career: recruitment, ranks, peerage, battle roles** — readme_recruitment_posts, readme_ranks, readme_peerage, readme_enlisted_players, readme_jobs, readme_roles, readme_land_grants [INFERRED 0.85]

## Communities (415 total, 271 thin omitted)

### Community 0 - ".measure"
Cohesion: 0.19
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 2 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.06
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - "org.junit.jupiter.api.Test"
Cohesion: 0.06
Nodes (3): DoctrineTest, FieldBattleBodiesTest, FieldBattleTest

### Community 6 - "Front"
Cohesion: 0.10
Nodes (8): Clash, Feud, Kind, DUEL, SKIRMISH, Lord, Feuds, Front

### Community 7 - "net.minecraft.core.BlockPos"
Cohesion: 0.07
Nodes (6): PlannedTent, Saved, WarCamp, CannonControl, CityGates, Done

### Community 9 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (7): GrandStrategyBlock, MediumCannonBlock, OarBlock, QuestBoardBlock, SailBlock, StrategyBoardBlock, WorldMapItem

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "War"
Cohesion: 0.10
Nodes (3): Goal, PoliticsSync, War

### Community 13 - "Point"
Cohesion: 0.04
Nodes (20): Point, Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate (+12 more)

### Community 14 - "PveCommands.java"
Cohesion: 0.10
Nodes (30): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+22 more)

### Community 16 - "Morale"
Cohesion: 0.28
Nodes (6): Morale, State, RALLIED, ROUTED, SHATTERED, STEADY

### Community 17 - "RafasConquest Project Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.11
Nodes (5): TitleStyle, Faith, Bond, RealmPlanner, Seat

### Community 19 - "RecruitsBattle"
Cohesion: 0.03
Nodes (22): Cited Findings, Card, Enlisted, Figure, GunMark, Landing, Live, Officers (+14 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 22 - "list"
Cohesion: 0.03
Nodes (16): SiegeSurveyTest, SwingModesTest, WarCampTest, CityHudPreviewTest, ClientTownsfolkTest, GrandStrategyDateTest, OarRowingTest, GateGeometryTest (+8 more)

### Community 23 - ".view"
Cohesion: 0.07
Nodes (15): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+7 more)

### Community 25 - "Tier"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 27 - "RoadNetwork"
Cohesion: 0.09
Nodes (6): Cited Findings, RoadFeature, Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 28 - "GateBlockEntity"
Cohesion: 0.08
Nodes (10): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+2 more)

### Community 29 - "PlaceMusic.java"
Cohesion: 0.04
Nodes (10): FactionType, BANDIT, CITY_STATE, EMPIRE, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement (+2 more)

### Community 30 - ".allSitesInBox"
Cohesion: 0.08
Nodes (9): CityLayout, District, Side, EAST, NORTH, SOUTH, WEST, Site (+1 more)

### Community 32 - "BattleShips"
Cohesion: 0.11
Nodes (5): BattleShips, Afloat, Helm, Ram, SableLink

### Community 34 - ".map"
Cohesion: 0.09
Nodes (5): Army, FeudDuel, MusterBars, Wars, Politics.Front

### Community 35 - "CityPlan"
Cohesion: 0.09
Nodes (11): CityPlan, Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL (+3 more)

### Community 36 - "BaseMapGenerator.java"
Cohesion: 0.03
Nodes (61): Ambushes from woods, Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Character page, Characters (holders, skills, prowess, traits) (+53 more)

### Community 37 - "Ranks"
Cohesion: 0.06
Nodes (21): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+13 more)

### Community 38 - "WaterSwell"
Cohesion: 0.06
Nodes (15): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Sea, Made, WaterSwell (+7 more)

### Community 39 - ".findGenerationPoint"
Cohesion: 0.08
Nodes (6): SlopeTile, HarbourCut, CitySlopeStructure, Frame, Harbour, HarbourCutTest

### Community 43 - "SiegeWorks"
Cohesion: 0.10
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 44 - "ArrowVolleys"
Cohesion: 0.11
Nodes (13): Arrow, ArrowVolleys, Kind, ARROW, BALL, BIG_BALL, BIG_ROCK, BOLT (+5 more)

### Community 45 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.12
Nodes (6): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, DistantArmies

### Community 46 - "Personality"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.08
Nodes (9): Lane, Leg, Port, Post, SeaLanes, Ship, Size, Voyage (+1 more)

### Community 48 - ".sendToServer"
Cohesion: 0.05
Nodes (20): BackBanner, Bark, CannonShot, ChargeHits, CityClose, OpenBattleScreen, SeaSwell, Streets (+12 more)

### Community 50 - "ConquestServer.java"
Cohesion: 0.16
Nodes (3): Cited Findings, BarracksPeople, CityGuards

### Community 51 - "BattlePlan"
Cohesion: 0.06
Nodes (8): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, BattlePlanTest

### Community 52 - "Kit"
Cohesion: 0.10
Nodes (20): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+12 more)

### Community 53 - "DistantOcclusion"
Cohesion: 0.08
Nodes (11): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Cited Findings, Gaps, Inferences, DistantOcclusion, Mode, OFF (+3 more)

### Community 55 - ".render"
Cohesion: 0.10
Nodes (6): CharacterScreen, Spot, Banner, Portraits, Honour, Honours

### Community 56 - "CityStyles"
Cohesion: 0.20
Nodes (3): Building, CityStyles, Houses

### Community 58 - "MarkerRenderer.java"
Cohesion: 0.11
Nodes (3): BattleBannerRenderer, GateRenderer, MarkerRenderer

### Community 59 - "CityTerrain"
Cohesion: 0.06
Nodes (17): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+9 more)

### Community 60 - "ConquestSavedData"
Cohesion: 0.08
Nodes (7): Cited Findings, ConquestSavedData, MapPreview, Poi, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 61 - "HorizontalStretch"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 62 - "WorldSettings"
Cohesion: 0.07
Nodes (12): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, NobleAnnex, OriginRoller, Roll (+4 more)

### Community 63 - "TitleMap"
Cohesion: 0.16
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.11
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 67 - ".roadStart"
Cohesion: 0.12
Nodes (4): DistrictKey, Townsfolk, StreetWalksTest, PlaceMusicTest

### Community 69 - "MapPreview.java"
Cohesion: 0.14
Nodes (8): SeedHash, ResourceType, FOOD, FactionInfo, PoiType, CAPITAL, CITY, FARM

### Community 71 - "School"
Cohesion: 0.11
Nodes (14): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+6 more)

### Community 72 - "DistantShips"
Cohesion: 0.10
Nodes (9): Takeaway, DistantShips, Far, HullKey, Riding, Want, Placed, DeckLiftTest (+1 more)

### Community 73 - "NobleTitle"
Cohesion: 0.12
Nodes (9): Names, NobleTitle, COUNT, DUKE, EMPEROR, KING, KNIGHT, NONE (+1 more)

### Community 74 - "MapLands"
Cohesion: 0.09
Nodes (5): Gaps, Inferences, Takeaway, Where are full scans, nested loops, repeated lookups and per-tick allocations?, MapLands

### Community 75 - "WarCouncil"
Cohesion: 0.10
Nodes (8): Facts, Officer, Said, Talk, WarCouncil, Ranks, SaveGuard, WarCouncilPagesTest

### Community 76 - ".step"
Cohesion: 0.07
Nodes (17): 1. How we avoid breaking anything, 2. Where the 10 November gameplan stands, 3. New costs since 10 November, 4. The stages, 5. Order and size, Core-function check after every stage (about 10 minutes in game), RafasConquest optimization plan (18 November 2026), Stage 0: measure (one jar, no behaviour change) (+9 more)

### Community 77 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.08
Nodes (8): WarHorn, BattleStandardItem, AddonPieces, Armoury, Outfit, Style, ItemFlags, RankKits

### Community 78 - "RecruitFigures"
Cohesion: 0.09
Nodes (8): Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures, RecruitFiguresTest

### Community 79 - "BaseMap"
Cohesion: 0.07
Nodes (11): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, ClientMapCache, BaseMap, Nation (+3 more)

### Community 81 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.18
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 82 - "Candidate"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "RoadLayer"
Cohesion: 0.14
Nodes (13): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+5 more)

### Community 89 - "CityVariant"
Cohesion: 0.18
Nodes (7): PlacedWall, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 92 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 95 - ".of"
Cohesion: 0.19
Nodes (4): FactionBanner, Layer, FactionBannerNbt, CityBanners

### Community 96 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.07
Nodes (31): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings (+23 more)

### Community 99 - "FactionInfoScreen"
Cohesion: 0.14
Nodes (3): FactionInfoScreen, Line, StructureLink

### Community 100 - "Person"
Cohesion: 0.16
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 102 - "Hull"
Cohesion: 0.23
Nodes (3): Hull, Ramming, RammingTest

### Community 103 - ".enlist"
Cohesion: 0.03
Nodes (15): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome, Task, AMBUSH (+7 more)

### Community 105 - ".mouseClicked"
Cohesion: 0.44
Nodes (3): Place, PlaceMenuLayer, Row

### Community 107 - "How Total War calculates battles between units"
Cohesion: 0.11
Nodes (17): 1. Combat is per soldier, not per unit, 1. Flank and rear shock, 2. Does the swing land?, 2. Nearby rout (fright), 3. Army-wide losses, 3. How much damage the hit does, 4. Charges, 4. Shattering (+9 more)

### Community 108 - "LabelLayer"
Cohesion: 0.13
Nodes (4): Hit, LabelLayer, StrategyRelief, LabelShape

### Community 109 - ".map"
Cohesion: 0.09
Nodes (5): Garrison, Army, GrandStrategy, View, GarrisonTest

### Community 110 - "CityPanel"
Cohesion: 0.17
Nodes (6): CityPanel, Mode, DISMISS, MARCH, MUSTER, NONE

### Community 111 - "NationScreen"
Cohesion: 0.07
Nodes (7): Retainer, Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 114 - "Arm"
Cohesion: 0.07
Nodes (6): Arm, Kind, ENGINE, FOOT, MOUNTED, RANGED

### Community 117 - "CameraView"
Cohesion: 0.10
Nodes (8): Inferences, Cited Findings, Camera, CameraView, Chunks, ChunkMapAccess, ChunkMapCameraMixin, TrackedEntityCameraMixin

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.08
Nodes (3): GrandStrategyScreen, Marcher, Spot

### Community 121 - "Cloth"
Cohesion: 0.09
Nodes (6): Cloth, Solid, ClothTest, OffTheBackTest, StretchTest, WoolTest

### Community 124 - "TechTreeScreen"
Cohesion: 0.08
Nodes (12): TechTreeScreen, DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind (+4 more)

### Community 125 - "Roads"
Cohesion: 0.07
Nodes (19): Grade, CITY, DOJO, HIGH, SEA, TOWN, Heap, Junction (+11 more)

### Community 126 - "DayClockTest"
Cohesion: 0.28
Nodes (3): DayClock, Step, DayClockTest

### Community 127 - "MapContext"
Cohesion: 0.10
Nodes (8): BaseMapLayer, MapContext, LegendEntry, MapLayer, Symbol, PoiIconLayer, TerritoryLayer, UnitLayer

### Community 128 - "FigureGear"
Cohesion: 0.20
Nodes (7): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 130 - "TitleOwnersScreen"
Cohesion: 0.19
Nodes (3): ClientPeerage, Owner, TitleOwnersScreen

### Community 131 - "Lands"
Cohesion: 0.06
Nodes (13): Cited Findings, Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Lands, Stronghold (+5 more)

### Community 132 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 134 - "MediumCannonEntity"
Cohesion: 0.10
Nodes (4): MediumCannonEntity, Order, Shot, ROUNDSHOT

### Community 136 - "net.minecraft.world.level.block.entity.BlockEntity"
Cohesion: 0.05
Nodes (7): BattleBannerBlockEntity, ConquestContent, Oar, Banner, PlacedBanners, Sail, MarkerBlockEntity

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (40): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 6. Sodium (and Iris) compatibility, 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply (+32 more)

### Community 141 - ".choose"
Cohesion: 0.16
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 142 - ".onLogout"
Cohesion: 0.09
Nodes (6): ClientTownsfolk, Folk, Throw, ThrownFigures, StreetWalks, Walk

### Community 143 - ".view"
Cohesion: 0.09
Nodes (6): Place, CityManager, PlayerStats, View, Retinues, PlaceMusic

### Community 152 - "Cited Findings"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?

### Community 156 - "Builder"
Cohesion: 0.09
Nodes (7): BattleBannerBlock, FlagBlock, Mount, BARS, BLOCK, FENCE, WALL

### Community 157 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 158 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 160 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.07
Nodes (17): Cited Findings, ArmorLegsMixin, ArrowSaveMixin, BetterCombatOneHandMixin, CityVegetationMixin, ClientChunkWindowMixin, DhShaderLoaderMixin, DhTerrainShaderMixin (+9 more)

### Community 162 - ".aWoodNearAWingHidesAnAmbushThatStaysPutUntilItStrikes"
Cohesion: 0.11
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 163 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.20
Nodes (6): BattleSounds, ChargeSound, DinSound, Queued, Shot, Source

### Community 164 - "BattleWallBannerBlock"
Cohesion: 0.13
Nodes (3): BattleWallBannerBlock, GoldChurch, Layout

### Community 175 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.03
Nodes (38): BaseMapPart, BattleSpot, BoardAction, BoardView, CameraAt, CannonFire, CannonLay, CannonLeave (+30 more)

### Community 178 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.09
Nodes (9): BannerArt, Carried, ClothRenderer, Fabric, Far, Kind, Layer, Live (+1 more)

### Community 186 - "UnitOrder"
Cohesion: 0.11
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 205 - "HornCall"
Cohesion: 0.40
Nodes (5): HornCall, ATTACKING, MOVING, RALLYING, RETREATING

### Community 206 - ".noble"
Cohesion: 0.32
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 219 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.06
Nodes (30): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+22 more)

### Community 223 - "Settlement"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 224 - "RecruitFigures.java"
Cohesion: 0.06
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 226 - "FieldRules"
Cohesion: 0.16
Nodes (5): FieldRules, Side, FLANK, FRONT, REAR

### Community 228 - ".wallSegments"
Cohesion: 0.17
Nodes (3): WallSegment, CitySizesTest, CityWallTest

### Community 234 - ".rightClick"
Cohesion: 0.16
Nodes (4): Entry, Menu, BattleSpotRing, HornOrders

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.07
Nodes (3): DistantDepth, DistantArmyRenderer, Rects

### Community 238 - "Body"
Cohesion: 0.09
Nodes (26): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+18 more)

### Community 248 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 252 - "Doctrine"
Cohesion: 0.05
Nodes (30): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+22 more)

### Community 255 - "ProvinceMap"
Cohesion: 0.14
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 262 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 266 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 282 - "ConquestWorldScreen"
Cohesion: 0.14
Nodes (6): BattleSite.Water, BattleSite.Water, ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, SettingsScreen

### Community 283 - "City"
Cohesion: 0.09
Nodes (10): Countryside, Plan, City, Grid, SeatCities, CityIndex, CountrysideTest, World (+2 more)

### Community 285 - "net.minecraft.client.Minecraft"
Cohesion: 0.11
Nodes (6): FarGun, Model, OarFigures, Rowing, Columns, SableSeen

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 292 - "BackBannerLayer.java"
Cohesion: 0.17
Nodes (3): BackBannerLayer, BackBanners, MouthLayer

### Community 297 - ".generate"
Cohesion: 0.06
Nodes (21): BaseMapGenerator, Heights, BiomePalette, Entry, MapColors, Homeland, DESERT, JUNGLE (+13 more)

### Community 305 - "FactionMapScreen"
Cohesion: 0.08
Nodes (5): Cited Findings, FactionMapScreen, MapView, PetitionScreen, AbstractSliderButton

### Community 309 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 310 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 318 - "Missile"
Cohesion: 0.29
Nodes (4): Missile, ARROWS, NONE, SHOT

### Community 324 - "Build"
Cohesion: 0.29
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 326 - "net.minecraft.core.BlockPos"
Cohesion: 0.08
Nodes (5): CommandTableBlock, GateBlock, HelmBlock, Builder, MarkerBlock

### Community 330 - "OarFigures"
Cohesion: 0.06
Nodes (13): Wind, ShipSprite, Batch, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL (+5 more)

### Community 336 - "Succession"
Cohesion: 0.09
Nodes (9): Hook, Kind, FRIEND, RIVAL, Realm, Succession, Taking, RealmPlannerTest (+1 more)

### Community 350 - "FormationSnapshot"
Cohesion: 0.07
Nodes (20): Cited Findings, Engine, FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, ENGINES (+12 more)

### Community 355 - "FieldBlock"
Cohesion: 0.04
Nodes (9): The core change: morale becomes a sum, FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock (+1 more)

### Community 357 - "FleetRole"
Cohesion: 0.29
Nodes (6): FleetRole, FRONT, LINE, RESERVE, SCREEN, SUPPORT

### Community 364 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 369 - "RafasConquest.java"
Cohesion: 0.03
Nodes (7): Kind, CLOSE, OPEN, RAM, GrappleHooks, ConquestEntities, RafasConquest

### Community 380 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, GUNSHIP, SHOCK, SWORDSMEN

### Community 418 - "Role"
Cohesion: 0.40
Nodes (5): Role, MANTLET, RAM, SHOOTER, TOWER

### Community 431 - "net.minecraft.core.Direction"
Cohesion: 0.10
Nodes (6): ChunkWait, Wait, Land, Result, SiegeSurvey, Wall

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **428 isolated node(s):** `SOLDIER`, `LEADER`, `GENERAL`, `BEARER`, `GUARD` (+423 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1509 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **271 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `net.minecraft.nbt.CompoundTag`, `org.junit.jupiter.api.Test`, `org.jetbrains.annotations.Nullable`, `net.minecraft.core.BlockPos`, `ConquestEvents`, `Point`, `PveCommands.java`, `.view`, `BattleMap.java`, `org.spongepowered.asm.mixin.Mixin`, `BattleShips`, `PveCommands`, `.map`, `rafasconquest.faction.Faction`, `net.minecraft.client.gui.GuiGraphics`, `SiegeWorks`, `net.minecraft.core.Direction`, `Phase`, `UnitMode`, `RecruitsBattle.java`, `UnitOrder`, `WarCouncil`, `net.minecraft.world.item.ItemStack`, `HornCall`, `RecruitFigures.java`, `FieldBlock`, `.enlist`, `GrappleHook`, `.map`, `.block`, `Doctrine`?**
  _High betweenness centrality (0.051) - this node is a cross-community bridge._
- **What connects `SOLDIER`, `LEADER`, `GENERAL` to the rest of the system?**
  _428 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `net.minecraft.nbt.CompoundTag` be split into smaller, more focused modules?**
  _Cohesion score 0.06310679611650485 - nodes in this community are weakly interconnected._
- **Why does `FieldBattle` connect `FieldBlock` to `Bodies`, `PveCommands`, `.aWoodNearAWingHidesAnAmbushThatStaysPutUntilItStrikes`, `org.junit.jupiter.api.Test`, `org.jetbrains.annotations.Nullable`, `.enlist`, `Point`, `PveCommands.java`, `Morale`, `.block`, `Machine`, `Call`?**
  _High betweenness centrality (0.025) - this node is a cross-community bridge._
- **Should `org.junit.jupiter.api.Test` be split into smaller, more focused modules?**
  _Cohesion score 0.06269270298047276 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._