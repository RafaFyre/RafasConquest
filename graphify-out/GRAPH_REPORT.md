# Graph Report - RafasConquest  (2026-11-13)

## Corpus Check
- 543 files · ~468,802 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7481 nodes · 24021 edges · 402 communities (159 shown, 243 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3161 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `9a2c5123`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- net.minecraft.network.FriendlyByteBuf
- TechTreeScreen
- net.minecraft.nbt.CompoundTag
- .army
- Siege
- BattleScreen
- DistantHorizonsArmies.java
- CityGridBuilder
- PoliticsTest
- Ride
- net.minecraft.server.level.ServerPlayer
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- RecruitsBattle.java
- BaseMap
- rafasconquest.map.BaseMap
- Faction
- ConquestSavedData
- Battle Map Web Page & Site
- Person
- net.minecraft.world.level.block.state.BlockState
- PveCommands.java
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- HorizontalStretch
- CityManagerScreen
- .plan
- org.spongepowered.asm.mixin.Mixin
- Arm
- WarCouncil
- GatePlanksBlock.java
- .enlist
- ClothRenderer
- Clash
- PveCommands Registration
- School
- CityPlan
- .send
- Machine
- SailFigures
- Gate
- rafasconquest.faction.Faction
- Build
- Front
- GateBlockEntity
- .rightClick
- FieldBlock
- Personalities
- SeaLanes
- arraylist
- RecruitsBattle
- BattlePlan
- ProvinceMap
- SeatCitiesTest
- Barracks
- Cited Findings
- CityStyles
- City
- Faction Map View
- .step
- ClientScreens
- MapContext
- TitleMapTest
- Bodies
- StyleLibrary
- FormationSnapshot
- .startAt
- BattleMap.java
- Row
- .generate
- net.minecraft.world.item.DyeColor
- .readPlacements
- MapLands
- Recruiter
- RafasConquest (PvE addon for Villager Recruits)
- Quests
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Duty
- net.minecraft.server.level.ServerLevel
- Ownership & Territory
- .wallSegments
- .layout
- Land
- .end
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- .stat
- .postChunk
- Terrain
- .bark
- Grade
- WarCamp
- FactionInfoScreen
- Front
- .findGenerationPoint
- Cloth
- FigureBatches
- net.minecraft.core.Vec3i
- Cited Findings
- How Total War calculates battles between units
- Override
- Race
- ClientEvents
- SiegeWorks
- Rank
- WaterSwell
- Hull
- Body
- Trait
- Cited Findings
- HarbourCut
- GrandStrategyScreen
- CityTerrain
- CapitalLayout
- CityTerrainPiece
- com.mojang.blaze3d.vertex.PoseStack
- ConquestEvents.java
- Quest
- BattleShips
- .drawEmpire
- net.minecraft.resources.ResourceLocation
- .renderByItem
- .shift
- Peerage
- SeaCalm
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- Kit
- Organizations
- GateDoors
- ClientPolitics
- Townsperson
- .hash
- CLAUDE.md
- How do world generation and structure placement work, and what do they cost?
- War
- .place
- .roadStart
- .place
- CameraView.java
- RecruitFigures
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- Override
- UnitOrder
- .view
- .tick
- BattleSnapshot
- .noble
- .findGenerationPoint
- .inCity
- .render
- NationScreen
- ClothItemRenderer
- RecruitFigures.java
- Strategy
- .view
- .render
- Sea
- ConquestEvents
- WallTemplates
- Kind
- DistantArmyRenderer
- .boundsAt
- ConquestGameRules
- OrganizationScreen
- net.minecraft.core.BlockPos
- Size
- Ranks
- .deployment
- Roads
- Settlement
- Profiling run sheet (baseline)
- .measure
- net.minecraft.sounds.SoundEvent
- ConquestConfig
- .onLogout
- .collect
- .at
- net.minecraft.client.gui.GuiGraphics
- WoolTest
- Kind
- Realm
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- net.minecraft.world.level.Level
- 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound
- .of
- BattleSounds
- Kind
- net.minecraft.world.level.block.entity.BlockEntity
- .fire
- Kind
- .order
- CityLayout
- .onLevelLoad
- .plan
- Grade
- .load
- BattleKeepInventory
- Call
- BannerArt.java
- BlockPos
- ConquestWorldScreen
- .plus
- Role
- PoiPrecomputer.java
- .place
- Politics.Front
- Characters (holders, skills, prowess, traits)
- Distribution
- GrappleHook
- ClientBarks
- FieldRules
- OarFigures
- MapView
- ArtilleryScreen
- .nearest
- .nationOf
- NobleDistrictTest
- State
- Lands
- ShipKind
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- Where are full scans, nested loops, repeated lookups and per-tick allocations?
- BlockGrid
- ShareLiveTest
- PoliticsData
- Saved data: how big, how often written, is everything re-serialized?
- ConquestNetwork
- ItemFlags.java
- UnitMode
- .general
- SetSetting
- .offTheBack
- .onRegisterRenderers
- WarHorn
- WarCampTest.java
- GrandStrategyDateTest.java
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 305 edges
2. `FieldBlock` - 222 edges
3. `FieldBattle` - 211 edges
4. `FieldArmy` - 127 edges
5. `ConquestNetwork` - 123 edges
6. `Point` - 108 edges
7. `Lands` - 89 edges
8. `Siege` - 84 edges
9. `BattleScreen` - 82 edges
10. `Politics` - 79 edges

## Surprising Connections (you probably didn't know these)
- `A tick budget and staggered schedules make the fixes stick` --references--> `BaseMap`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/map/BaseMap.java
- `Cited Findings` --references--> `ChunkMapAccess`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/mixin/ChunkMapAccess.java
- `Cited Findings` --references--> `ChunkMapCameraMixin`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/mixin/ChunkMapCameraMixin.java

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

## Communities (402 total, 243 thin omitted)

### Community 0 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.05
Nodes (15): BaseMapPart, BattleSpot, BoardView, FactionInfoReply, FigureStrike, GrandStrategyView, PeerageView, PoliticsView (+7 more)

### Community 2 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.06
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - ".army"
Cohesion: 0.06
Nodes (7): Point, WallTower, DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, OarRowingTest, ArmouryTest

### Community 4 - "Siege"
Cohesion: 0.10
Nodes (7): Face, GateAction, Region, IN, OUT, WALL, Siege

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.07
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "CityGridBuilder"
Cohesion: 0.09
Nodes (10): CityGridBuilder, Ground, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA (+2 more)

### Community 9 - "Ride"
Cohesion: 0.13
Nodes (3): Ride, Swell, SwellTest

### Community 10 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.11
Nodes (8): Boards, Entry, View, CityManager, GrandStrategy, View, Taken, QuestProgress

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "RecruitsBattle.java"
Cohesion: 0.03
Nodes (8): /council <topic>, /pve debug recruits, Battle phases and days, TickWeave experiment, War camps, War council, BiomePalette, Entry

### Community 13 - "BaseMap"
Cohesion: 0.08
Nodes (11): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, BaseMap, Nation, BaseMapStore (+3 more)

### Community 14 - "rafasconquest.map.BaseMap"
Cohesion: 0.14
Nodes (4): Cited Findings, FeudDuel, MusterBars, Wars

### Community 16 - "ConquestSavedData"
Cohesion: 0.06
Nodes (8): Cited Findings, ConquestSavedData, CityBanners, ConquestServer, StructureLink, EconomyTick, PoiPrecomputer, ConquestSavedDataTest

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.26
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.07
Nodes (6): BattleBannerBlock, BattleWallBannerBlock, GateBlock, HelmBlock, Builder, MarkerBlock

### Community 20 - "PveCommands.java"
Cohesion: 0.06
Nodes (33): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+25 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.14
Nodes (9): Cited Findings, ArrowSaveMixin, CityVegetationMixin, ClientChunkWindowMixin, DhShaderLoaderMixin, DhTerrainShaderMixin, RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin (+1 more)

### Community 27 - "Arm"
Cohesion: 0.10
Nodes (14): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+6 more)

### Community 28 - "WarCouncil"
Cohesion: 0.18
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 30 - ".enlist"
Cohesion: 0.04
Nodes (10): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT, WarPlan (+2 more)

### Community 31 - "ClothRenderer"
Cohesion: 0.31
Nodes (4): ClothRenderer, Fabric, Kind, Live

### Community 32 - "Clash"
Cohesion: 0.27
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.14
Nodes (3): CityPlan, Piece, Plan

### Community 36 - ".send"
Cohesion: 0.08
Nodes (11): BackBanner, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel, Type (+3 more)

### Community 37 - "Machine"
Cohesion: 0.08
Nodes (13): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+5 more)

### Community 39 - "Gate"
Cohesion: 0.12
Nodes (3): Gate, GateState, Plan

### Community 41 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 42 - "Front"
Cohesion: 0.11
Nodes (4): Feud, Lord, Feuds, Front

### Community 43 - "GateBlockEntity"
Cohesion: 0.09
Nodes (9): GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING, GateGeometry, Hinge (+1 more)

### Community 44 - ".rightClick"
Cohesion: 0.23
Nodes (3): Entry, Menu, HornOrders

### Community 45 - "FieldBlock"
Cohesion: 0.04
Nodes (9): The core change: morale becomes a sum, FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock (+1 more)

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "SeaLanes"
Cohesion: 0.22
Nodes (6): Lane, Leg, Port, SeaLanes, Ship, Voyage

### Community 50 - "RecruitsBattle"
Cohesion: 0.03
Nodes (26): Cited Findings, Card, Enlisted, Figure, HornCall, ATTACKING, MOVING, RALLYING (+18 more)

### Community 52 - "ProvinceMap"
Cohesion: 0.17
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 55 - "Cited Findings"
Cohesion: 0.06
Nodes (14): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, Arrow, ArrowVolleys, BattleSpotRing (+6 more)

### Community 56 - "CityStyles"
Cohesion: 0.22
Nodes (3): Building, CityStyles, Houses

### Community 57 - "City"
Cohesion: 0.10
Nodes (7): Countryside, Plan, City, Grid, SeatCities, CountrysideTest, World

### Community 58 - "Faction Map View"
Cohesion: 0.16
Nodes (3): Cited Findings, FactionMapScreen, MapView

### Community 59 - ".step"
Cohesion: 0.15
Nodes (4): FigureMotion, Match, State, FigureMotionTest

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 66 - "FormationSnapshot"
Cohesion: 0.11
Nodes (15): Engine, FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL (+7 more)

### Community 69 - "BattleMap.java"
Cohesion: 0.04
Nodes (4): Battle map developer web page, Morale, rout and rally, StreetWalks, RafasConquest

### Community 71 - ".generate"
Cohesion: 0.10
Nodes (10): BaseMapGenerator, Heights, Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW (+2 more)

### Community 72 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.11
Nodes (9): DistantShips, Far, HullKey, Riding, Want, BattleKind, Part, ShipHulls (+1 more)

### Community 73 - ".readPlacements"
Cohesion: 0.27
Nodes (3): Offsets, Placement, PlacementOffsetsTest

### Community 76 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.10
Nodes (17): Battles (prototype), Blocks of 20 soldiers, Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), Epic Knights optional armour (Armoury), Gold church, Land grants (seven ways) (+9 more)

### Community 77 - "Quests"
Cohesion: 0.16
Nodes (5): DataFiles, Dialogue, Kind, Quests, Target

### Community 78 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.10
Nodes (19): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps, Inferences (+11 more)

### Community 79 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 81 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.14
Nodes (5): Land, Result, SiegeSurvey, Wall, SiegeSurveyTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".wallSegments"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 84 - ".layout"
Cohesion: 0.22
Nodes (4): Cited Findings, Positions, Sprite, FormationSnapshotTest

### Community 85 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 86 - ".end"
Cohesion: 0.05
Nodes (18): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, Outcome (+10 more)

### Community 87 - "Doctrine"
Cohesion: 0.18
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - ".stat"
Cohesion: 0.09
Nodes (11): ClientPeerage, Names, Honour, Honours, Stat, DIPLOMACY, INTRIGUE, LEARNING (+3 more)

### Community 92 - ".postChunk"
Cohesion: 0.15
Nodes (3): Cited Findings, BarracksPeople, CityGuards

### Community 95 - ".bark"
Cohesion: 0.24
Nodes (3): Barks, Ear, DialogueTest

### Community 96 - "Grade"
Cohesion: 0.15
Nodes (11): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+3 more)

### Community 98 - "WarCamp"
Cohesion: 0.10
Nodes (6): PlannedTent, Saved, WarCamp, CourtSet, Seat, Style

### Community 100 - "Front"
Cohesion: 0.10
Nodes (6): Engine, LADDERS, RAMS, TOWERS, Front, Rows

### Community 103 - "FigureBatches"
Cohesion: 0.07
Nodes (11): ShipSprite, Batch, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED (+3 more)

### Community 105 - "net.minecraft.core.Vec3i"
Cohesion: 0.09
Nodes (4): StylePiece, CityPlacement, GoldChurch, Layout

### Community 106 - "Cited Findings"
Cohesion: 0.18
Nodes (6): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, DistantArmies

### Community 107 - "How Total War calculates battles between units"
Cohesion: 0.11
Nodes (17): 1. Combat is per soldier, not per unit, 1. Flank and rear shock, 2. Does the swing land?, 2. Nearby rout (fright), 3. Army-wide losses, 3. How much damage the hit does, 4. Charges, 4. Shattering (+9 more)

### Community 109 - "Race"
Cohesion: 0.18
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 111 - "SiegeWorks"
Cohesion: 0.14
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 113 - "WaterSwell"
Cohesion: 0.16
Nodes (7): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Made, WaterSwell

### Community 114 - "Hull"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 115 - "Body"
Cohesion: 0.08
Nodes (26): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+18 more)

### Community 116 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 117 - "Cited Findings"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?

### Community 118 - "HarbourCut"
Cohesion: 0.11
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 121 - "CityTerrain"
Cohesion: 0.05
Nodes (19): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+11 more)

### Community 122 - "CapitalLayout"
Cohesion: 0.18
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 124 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.16
Nodes (3): BattleBannerRenderer, GateRenderer, MarkerRenderer

### Community 125 - "ConquestEvents.java"
Cohesion: 0.04
Nodes (15): /pve debug citystats [samples], /pve debug cityterrain, Ambushes from woods, Cities (grid, plots, vanilla village buildings), Deploying to the land (GroundPlan), Mantra schools and dojos, Roads (high, city, town roads; bridges; signposts), Seat cities (+7 more)

### Community 126 - "Quest"
Cohesion: 0.19
Nodes (6): Advancement, Ladder, MILITARY, NOBLE, NONE, Quest

### Community 128 - "BattleShips"
Cohesion: 0.16
Nodes (4): BattleShips, Helm, Ram, SableLink

### Community 129 - ".drawEmpire"
Cohesion: 0.23
Nodes (3): Hit, LabelLayer, LabelShape

### Community 131 - ".renderByItem"
Cohesion: 0.16
Nodes (3): BannerArt, Carried, Layer

### Community 132 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 133 - "Peerage"
Cohesion: 0.06
Nodes (19): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+11 more)

### Community 134 - "SeaCalm"
Cohesion: 0.19
Nodes (5): Ground, Depths, SeaCalm, Strengths, SeaCalmTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (23): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, Cited Findings, Cited Findings, Gaps (+15 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings (+28 more)

### Community 141 - "Kit"
Cohesion: 0.11
Nodes (19): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+11 more)

### Community 142 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 148 - "Townsperson"
Cohesion: 0.12
Nodes (4): Walk, DistrictKey, Townsfolk, Townsperson

### Community 149 - ".hash"
Cohesion: 0.09
Nodes (6): ChunkCoord, FarmLayout, NobleAnnex, OriginRoller, Roll, ProvinceNames

### Community 152 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.20
Nodes (6): Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Takeaway, Api, DistantGeneration

### Community 155 - "War"
Cohesion: 0.09
Nodes (4): Army, Goal, PoliticsSync, War

### Community 160 - ".place"
Cohesion: 0.14
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 163 - "CameraView.java"
Cohesion: 0.08
Nodes (12): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway, Cited Findings, Camera, CameraView (+4 more)

### Community 164 - "RecruitFigures"
Cohesion: 0.07
Nodes (15): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD, Capture (+7 more)

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.29
Nodes (3): Armoury, Outfit, Style

### Community 174 - "Override"
Cohesion: 0.17
Nodes (3): BattleSite.Water, BattleSite.Water, SettingsScreen

### Community 178 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 181 - ".view"
Cohesion: 0.29
Nodes (4): Option, Talk, View, WarCouncilPagesTest

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 221 - "NationScreen"
Cohesion: 0.13
Nodes (3): NationScreen, Demographics, Share

### Community 224 - "RecruitFigures.java"
Cohesion: 0.06
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 225 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 226 - ".view"
Cohesion: 0.08
Nodes (14): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+6 more)

### Community 228 - ".render"
Cohesion: 0.09
Nodes (7): CharacterScreen, Retainer, Spot, Camera, CourtView, Figure, Banner

### Community 236 - "Kind"
Cohesion: 0.22
Nodes (7): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.08
Nodes (6): Cited Findings, DistantDepth, DistantArmyRenderer, Figure, Rects, Placed

### Community 242 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 252 - "Size"
Cohesion: 0.22
Nodes (6): Size, LARGE, MEDIUM, SMALL, XLARGE, XXLARGE

### Community 254 - "Ranks"
Cohesion: 0.08
Nodes (16): Enlisted players, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Battle roles (frontline, bait, support, flanker, kill squad), The war horn, BattleRole, BAIT, FLANKER, FRONTLINE (+8 more)

### Community 256 - "Roads"
Cohesion: 0.19
Nodes (5): Link, Place, Roads, Spur, RoadsTest

### Community 257 - "Settlement"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 267 - ".onLogout"
Cohesion: 0.14
Nodes (3): ClientMapCache, Throw, ThrownFigures

### Community 274 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.04
Nodes (5): Inferences, GateScreen, InventoryStatsButton, Portraits, RankKits

### Community 276 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 277 - "Realm"
Cohesion: 0.10
Nodes (6): Hook, Realm, Succession, Taking, RealmPlannerTest, World

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.20
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 282 - "net.minecraft.world.level.Level"
Cohesion: 0.06
Nodes (7): ArtilleryBlock, GrandStrategyBlock, OarBlock, QuestBoardBlock, SailBlock, StrategyBoardBlock, WorldMapItem

### Community 283 - "9. Memory: client caches, textures and NativeImages that are never freed or grow without bound"
Cohesion: 0.25
Nodes (5): 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Gaps, Inferences, Takeaway

### Community 285 - ".of"
Cohesion: 0.18
Nodes (4): FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 288 - "BattleSounds"
Cohesion: 0.23
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "net.minecraft.world.level.block.entity.BlockEntity"
Cohesion: 0.06
Nodes (5): ArtilleryMount, BattleBannerBlockEntity, ConquestContent, Oar, Sail

### Community 292 - ".fire"
Cohesion: 0.08
Nodes (4): CharacterMarkers, Placed, MarkerBlockEntity, MarkerSpec

### Community 293 - "Kind"
Cohesion: 0.50
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 294 - ".order"
Cohesion: 0.25
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

### Community 295 - "CityLayout"
Cohesion: 0.10
Nodes (5): CityLayout, District, Site, WorldSettings, CityLayoutTest

### Community 298 - ".plan"
Cohesion: 0.13
Nodes (7): Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 301 - "Grade"
Cohesion: 0.16
Nodes (9): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Plan (+1 more)

### Community 305 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 306 - "BannerArt.java"
Cohesion: 0.05
Nodes (5): BackBannerLayer, ClientModEvents, CapeCurio, Wind, MouthLayer

### Community 308 - "BlockPos"
Cohesion: 0.12
Nodes (5): BoardScreen, BoardAction, GateView, MarkerSet, MarkerView

### Community 310 - "ConquestWorldScreen"
Cohesion: 0.22
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 313 - ".plus"
Cohesion: 0.11
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 315 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 318 - "PoiPrecomputer.java"
Cohesion: 0.07
Nodes (14): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoliticalMap (+6 more)

### Community 324 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.08
Nodes (25): Base map generated from seed, Character page, Characters (holders, skills, prowess, traits), City wall, banners and wall guards, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Court 3D scene (CourtView), Cultures (seven homelands) (+17 more)

### Community 325 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 330 - "OarFigures"
Cohesion: 0.14
Nodes (4): OarFigures, Rowing, Columns, SableSeen

### Community 333 - "MapView"
Cohesion: 0.15
Nodes (6): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer, UnitLayer

### Community 367 - "State"
Cohesion: 0.20
Nodes (8): Morale, State, RALLIED, ROUTED, SHATTERED, STEADY, UnitInfo, UnitsState

### Community 369 - "Lands"
Cohesion: 0.08
Nodes (12): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Lands, Stronghold, Claim (+4 more)

### Community 370 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 371 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.33
Nodes (4): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender

### Community 376 - "Where are full scans, nested loops, repeated lookups and per-tick allocations?"
Cohesion: 0.33
Nodes (4): Gaps, Inferences, Takeaway, Where are full scans, nested loops, repeated lookups and per-tick allocations?

### Community 389 - "Saved data: how big, how often written, is everything re-serialized?"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Saved data: how big, how often written, is everything re-serialized?, Takeaway

### Community 390 - "ConquestNetwork"
Cohesion: 0.06
Nodes (13): Bark, CameraAt, ChargeHits, CityClose, CityView, ConquestNetwork, GateSet, OpenBattleScreen (+5 more)

### Community 392 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **414 isolated node(s):** `COUNT`, `KNIGHT`, `NONE`, `NO_LIGHTMAP`, `NO_SHADER_COLOUR` (+409 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1378 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **243 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `BattleShips`, `net.minecraft.nbt.CompoundTag`, `.army`, `UnitMode`, `.general`, `net.minecraft.server.level.ServerPlayer`, `RecruitsBattle.java`, `rafasconquest.map.BaseMap`, `PveCommands.java`, `org.jetbrains.annotations.Nullable`, `WarCouncil`, `.enlist`, `PveCommands Registration`, `CameraView.java`, `.send`, `net.minecraft.world.item.ItemStack`, `rafasconquest.faction.Faction`, `FieldBlock`, `UnitOrder`, `.startAt`, `BattleMap.java`, `GrappleHook`, `net.minecraft.server.level.ServerLevel`, `.end`, `.inCity`, `WarCamp`, `ClientEvents`, `SiegeWorks`, `ConquestEvents.java`?**
  _High betweenness centrality (0.063) - this node is a cross-community bridge._
- **What connects `COUNT`, `KNIGHT`, `NONE` to the rest of the system?**
  _414 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `net.minecraft.network.FriendlyByteBuf` be split into smaller, more focused modules?**
  _Cohesion score 0.04576271186440678 - nodes in this community are weakly interconnected._
- **Why does `CityLayout` connect `CityLayout` to `assertequals`, `net.minecraft.nbt.CompoundTag`, `.findGenerationPoint`, `BattleMap.java`, `.onLevelLoad`, `arraylist`, `.wallSegments`, `Barracks`, `HarbourCut`, `.roadStart`, `City`, `.postChunk`, `ConquestEvents.java`, `PoiPrecomputer.java`?**
  _High betweenness centrality (0.033) - this node is a cross-community bridge._
- **Should `net.minecraft.nbt.CompoundTag` be split into smaller, more focused modules?**
  _Cohesion score 0.05749385749385749 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._