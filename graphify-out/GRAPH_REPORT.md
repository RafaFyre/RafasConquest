# Graph Report - RafasConquest  (2026-11-11)

## Corpus Check
- 464 files · ~400,314 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 6530 nodes · 20682 edges · 343 communities (140 shown, 203 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2742 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `e01de794`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FieldBlock
- org.jetbrains.annotations.Nullable
- net.minecraft.world.level.levelgen.structure.BoundingBox
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsLink
- net.minecraft.server.MinecraftServer
- SiegeWorks
- Arm
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- Roads
- BaseMap
- net.minecraft.server.level.ServerPlayer
- Peerage
- BattlePlan
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.screens.Screen
- RafasConquest Commands (guide v0.0.2)
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- list
- HorizontalStretch
- CityManagerScreen
- .plan
- City
- .shift
- WarCouncil
- Quests
- .second
- Lands
- CapitalLayout
- PveCommands Registration
- School
- CityPlan
- Land
- Override
- FormationSnapshot
- net.minecraft.core.Direction
- rafasconquest.faction.Faction
- Faction
- War
- ConquestSavedData
- .view
- Personalities
- SeaLanes
- org.spongepowered.asm.mixin.Mixin
- .drawEmpire
- .roadStart
- TechTreeScreen
- Centre
- .postChunk
- Row
- Retinues
- SeatCities
- Faction Map View
- .syncGates
- net.minecraft.world.level.block.state.BlockState
- TitleMapTest
- SableLink
- StyleLibrary
- .render
- net.minecraft.world.item.ItemStack
- RafasConquest.java
- .render
- RecruitsBattle
- Cited Findings
- CityGridBuilder
- MapLands
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- PlaceMusicClient
- GateBlockEntity
- org.joml.Matrix4f
- .build
- SeaCalm
- Ownership & Territory
- .choose
- .view
- PoliticsTest
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- CourtView
- Barracks
- .generate
- PoiPrecomputer
- WaterSwell
- FactionInfoScreen
- ConquestEvents.java
- .regroup
- CityStyles
- net.minecraft.resources.ResourceLocation
- Realm
- MouthLayer.java
- rafasconquest.battle.FormationSnapshot
- RoadFeature
- Lands
- .readPlacements
- Door
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- MarkerScreen
- ClientModEvents.java
- RecruitsBattle.java
- GateBlockEntity.java
- RoadLayer
- .allSitesInBox
- GrandStrategyScreen
- Terrain
- ClientPolitics
- Countryside
- UnitOrder
- Settlement
- CityLayout
- RoadNetwork
- net.minecraft.world.item.DyeColor
- BattleShips
- MapContext
- net.minecraft.core.BlockPos
- BoardScreen
- .player
- .world
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- OrganizationScreen
- WallTemplates
- .step
- .bark
- CameraView
- Townsperson
- CLAUDE.md
- .get
- GroundPlan
- .plan
- Ranks
- GateScreen
- ChunkMapCameraMixin.java
- RecruitsArrowCleanerMixin.java
- Tier
- Small Cluster 170
- ClientEvents
- NobleAnnex
- .boundsAt
- ConquestWorldScreen
- NobleDistrictTest
- SimpleChannel.java
- .pack
- CityPlacement.java
- .onLevelLoad
- Siege
- Cited Findings
- DistantArmyRenderer.java
- CityTerrainPiece
- .touches
- Kind
- Cited Findings
- net.neoforged.bus.api.SubscribeEvent
- Striker
- ShipKind
- Phase
- Builder
- ConquestGameRules
- Recruiter
- Trait
- ClientBarks
- Call
- Ramming
- Task
- Profiling run sheet (baseline)
- .measure
- Race
- Type
- Organizations
- CourtSet
- How do world generation and structure placement work, and what do they cost?
- ChunkMapAccess
- 3. GUI and map screens (strategy map, faction map, battle overlays)
- BattleKeepInventory
- 6. Sodium (and Iris) compatibility
- Kind
- WarCamp
- CityManager
- Swell
- ProvinceNames
- Characters (holders, skills, prowess, traits)
- Build
- map
- Job
- UnitMode
- Shape
- GatePlanksBlock.java
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- net.minecraft.server.level.ServerPlayer
- HarbourCut
- rafasconquest.battle.FieldBlock
- 7. Client tick, stutter and sound
- .buildCell
- Kind
- PoliticsData
- ConquestConfig

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 279 edges
2. `FieldBattle` - 169 edges
3. `FieldBlock` - 163 edges
4. `ConquestNetwork` - 117 edges
5. `FieldArmy` - 111 edges
6. `Lands` - 89 edges
7. `Point` - 85 edges
8. `Politics` - 79 edges
9. `BaseMap` - 77 edges
10. `BattleScreen` - 70 edges

## Surprising Connections (you probably didn't know these)
- `Inferences` --references--> `ChunkMapCameraMixin`  [INFERRED]
  research_notes/RafasConquest performance gameplan/client_techniques.md → src/main/java/rafasconquest/mixin/ChunkMapCameraMixin.java
- `Cited Findings` --references--> `ChunkMapCameraMixin`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/mixin/ChunkMapCameraMixin.java
- `Cited Findings` --references--> `ChunkMapCameraMixin`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/mixin/ChunkMapCameraMixin.java
- `Cited Findings` --references--> `FormationSnapshot`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/battle/FormationSnapshot.java
- `Conclusion` --references--> `RoadLayer`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/client/map/RoadLayer.java

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

## Communities (343 total, 203 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.06
Nodes (7): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 1 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.06
Nodes (13): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoliticalMap (+5 more)

### Community 2 - "net.minecraft.world.level.levelgen.structure.BoundingBox"
Cohesion: 0.07
Nodes (8): CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece, PlaceholderPiece

### Community 3 - ".army"
Cohesion: 0.08
Nodes (3): DoctrineTest, FieldBattleTest, GrandStrategyDateTest

### Community 4 - "CityTerrain"
Cohesion: 0.07
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.08
Nodes (4): BattleScreen, Entry, Menu, HornOrders

### Community 6 - "DistantHorizonsLink"
Cohesion: 0.07
Nodes (9): 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), Cited Findings, Gaps, Inferences, Takeaway, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+1 more)

### Community 7 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.16
Nodes (4): Cited Findings, FeudDuel, MusterBars, Wars

### Community 8 - "SiegeWorks"
Cohesion: 0.12
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 10 - "Arm"
Cohesion: 0.06
Nodes (27): Arm, ARCHERS, CAVALRY, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, SKIRMISHERS, ZWEIHANDLERS (+19 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "Roads"
Cohesion: 0.11
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 13 - "BaseMap"
Cohesion: 0.07
Nodes (11): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, MapPreview, BaseMap, Nation (+3 more)

### Community 14 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.03
Nodes (36): BackBanner, Bark, BaseMapPart, BattleSnapshot, BattleSpot, BoardAction, BoardView, CameraAt (+28 more)

### Community 15 - "Peerage"
Cohesion: 0.09
Nodes (10): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+2 more)

### Community 16 - "BattlePlan"
Cohesion: 0.06
Nodes (9): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, WarPlan (+1 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.24
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "net.minecraft.client.gui.screens.Screen"
Cohesion: 0.04
Nodes (3): Inferences, InventoryStatsButton, Portraits

### Community 20 - "RafasConquest Commands (guide v0.0.2)"
Cohesion: 0.06
Nodes (38): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+30 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 22 - "list"
Cohesion: 0.06
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 25 - ".plan"
Cohesion: 0.18
Nodes (6): Nation, Nations, Plan, Seats, NationsTest, World

### Community 26 - "City"
Cohesion: 0.16
Nodes (4): City, CityGates, Done, CityIndex

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.16
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 29 - "Quests"
Cohesion: 0.11
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 31 - "Lands"
Cohesion: 0.10
Nodes (8): Clash, Feud, Kind, DUEL, SKIRMISH, Lord, Feuds, Front

### Community 32 - "CapitalLayout"
Cohesion: 0.10
Nodes (10): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, WorldSettings (+2 more)

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.14
Nodes (3): CityPlan, Piece, Plan

### Community 36 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 37 - "Override"
Cohesion: 0.13
Nodes (3): BattleSite.Water, SettingsScreen, Politics.Front

### Community 38 - "FormationSnapshot"
Cohesion: 0.09
Nodes (16): Cited Findings, FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, HEAVY_CAVALRY (+8 more)

### Community 39 - "net.minecraft.core.Direction"
Cohesion: 0.22
Nodes (5): Land, Result, SiegeSurvey, Wall, SiegeSurveyTest

### Community 40 - "rafasconquest.faction.Faction"
Cohesion: 0.11
Nodes (4): RecruitsFactions, FactionBanner, Layer, FactionBannerTest

### Community 42 - "Faction"
Cohesion: 0.13
Nodes (3): RecruitsBridge, Faction, EconomyTick

### Community 43 - "War"
Cohesion: 0.09
Nodes (4): Army, Goal, PoliticsSync, War

### Community 44 - "ConquestSavedData"
Cohesion: 0.07
Nodes (6): Cited Findings, ConquestSavedData, Poi, StructureLink, ConquestSavedDataTest, FactionInfoTest

### Community 45 - ".view"
Cohesion: 0.24
Nodes (7): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, GrandStrategy, View

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.29
Nodes (3): ClientChunkWindowMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 50 - ".drawEmpire"
Cohesion: 0.29
Nodes (3): Hit, LabelLayer, LabelShape

### Community 52 - "TechTreeScreen"
Cohesion: 0.18
Nodes (4): TechTreeScreen, Tech, TechTree, TechTreeTest

### Community 53 - "Centre"
Cohesion: 0.17
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 54 - ".postChunk"
Cohesion: 0.09
Nodes (4): Cited Findings, BarracksPeople, CityGuards, ConquestServer

### Community 57 - "SeatCities"
Cohesion: 0.12
Nodes (3): SeatCities, SeatCitiesTest, World

### Community 58 - "Faction Map View"
Cohesion: 0.16
Nodes (3): Cited Findings, FactionMapScreen, MapView

### Community 60 - ".syncGates"
Cohesion: 0.19
Nodes (5): Swing, CLOSED, CLOSING, OPEN, OPENING

### Community 61 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (8): Saved, CommandTableBlock, GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, WorldMapItem, MarkerBlock

### Community 63 - "TitleMapTest"
Cohesion: 0.17
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "SableLink"
Cohesion: 0.22
Nodes (3): Frame, SableLink, SiegeFrames

### Community 66 - ".render"
Cohesion: 0.07
Nodes (13): CharacterScreen, Spot, ClientPeerage, Banner, Honour, Honours, Stat, DIPLOMACY (+5 more)

### Community 67 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.06
Nodes (26): WarHorn, BackBanners, Armoury, Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY (+18 more)

### Community 69 - "RafasConquest.java"
Cohesion: 0.05
Nodes (3): Battle map developer web page, Morale, rout and rally, RafasConquest

### Community 71 - "RecruitsBattle"
Cohesion: 0.05
Nodes (11): Cited Findings, Figure, Officers, RecruitsBattle, Role, BEARER, GENERAL, GUARD (+3 more)

### Community 72 - "Cited Findings"
Cohesion: 0.07
Nodes (13): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, BattleSpotRing, DistantOcclusion, Mode (+5 more)

### Community 73 - "CityGridBuilder"
Cohesion: 0.07
Nodes (11): CityGridBuilder, Ground, PlacedWall, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY (+3 more)

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.06
Nodes (32): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+24 more)

### Community 76 - "PlaceMusicClient"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 77 - "GateBlockEntity"
Cohesion: 0.17
Nodes (4): GateDoors, GateBlockEntity, GateGeometry, Hinge

### Community 78 - "org.joml.Matrix4f"
Cohesion: 0.19
Nodes (3): DistantDepth, Arrow, ArrowVolleys

### Community 81 - "SeaCalm"
Cohesion: 0.22
Nodes (3): Sea, Depths, SeaCalm

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.17
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 84 - ".view"
Cohesion: 0.19
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 86 - "Calibration"
Cohesion: 0.29
Nodes (3): Calibration, Fit, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.05
Nodes (30): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+22 more)

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 92 - "CourtView"
Cohesion: 0.10
Nodes (5): Retainer, Camera, CourtView, Figure, NationScreen

### Community 94 - "Barracks"
Cohesion: 0.21
Nodes (3): Barracks, BarracksPiece, BarracksTest

### Community 95 - ".generate"
Cohesion: 0.07
Nodes (13): BaseMapGenerator, Heights, BiomePalette, Entry, MapColors, Homeland, DESERT, JUNGLE (+5 more)

### Community 98 - "WaterSwell"
Cohesion: 0.18
Nodes (6): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, WaterSwell

### Community 99 - "FactionInfoScreen"
Cohesion: 0.10
Nodes (3): ClientScreens, FactionInfoScreen, Line

### Community 100 - "ConquestEvents.java"
Cohesion: 0.04
Nodes (6): FactionBannerNbt, CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, FarmStructure

### Community 101 - ".regroup"
Cohesion: 0.05
Nodes (8): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome, Landing, TickTimes

### Community 103 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.22
Nodes (3): UnitLayer, RecruiterRenderer, TownspersonRenderer

### Community 105 - "Realm"
Cohesion: 0.11
Nodes (4): Hook, Realm, Succession, Taking

### Community 106 - "MouthLayer.java"
Cohesion: 0.10
Nodes (5): Cited Findings, BackBannerLayer, GateRenderer, MarkerRenderer, MouthLayer

### Community 107 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.31
Nodes (6): Boxes, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 109 - "Lands"
Cohesion: 0.07
Nodes (12): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Lands, Stronghold, Claim (+4 more)

### Community 110 - ".readPlacements"
Cohesion: 0.27
Nodes (3): Offsets, Placement, PlacementOffsetsTest

### Community 112 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.14
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips, Ride

### Community 117 - "RoadLayer"
Cohesion: 0.12
Nodes (12): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+4 more)

### Community 123 - "Countryside"
Cohesion: 0.19
Nodes (5): Countryside, Plan, Grid, CountrysideTest, World

### Community 124 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 125 - "Settlement"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 126 - "CityLayout"
Cohesion: 0.08
Nodes (10): CityLayout, Side, EAST, NORTH, SOUTH, WEST, Site, SlopeTile (+2 more)

### Community 128 - "RoadNetwork"
Cohesion: 0.15
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 129 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.36
Nodes (3): BattleKind, Part, ShipHulls

### Community 130 - "BattleShips"
Cohesion: 0.27
Nodes (3): BattleShips, Helm, Ram

### Community 132 - "MapContext"
Cohesion: 0.10
Nodes (8): MapContext, LegendEntry, MapLayer, Symbol, Place, PlaceMenuLayer, PoiIconLayer, TerritoryLayer

### Community 133 - "net.minecraft.core.BlockPos"
Cohesion: 0.09
Nodes (4): CharacterMarkers, Placed, StylePiece, GateGeometryTest

### Community 136 - ".player"
Cohesion: 0.10
Nodes (7): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageSync, PeerageTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.07
Nodes (28): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings (+20 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.09
Nodes (21): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 5. Water/ocean waves (sea swell, foam, boats on waves), 8. Lessons from performance mods that a content mod can apply, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+13 more)

### Community 143 - ".step"
Cohesion: 0.09
Nodes (7): FigureMotion, Placed, State, ClientMapCache, Throw, ThrownFigures, FigureMotionTest

### Community 149 - "Townsperson"
Cohesion: 0.12
Nodes (4): Walk, DistrictKey, Townsfolk, Townsperson

### Community 152 - ".get"
Cohesion: 0.10
Nodes (13): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+5 more)

### Community 155 - "GroundPlan"
Cohesion: 0.11
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 156 - ".plan"
Cohesion: 0.12
Nodes (8): Centre, Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 164 - "RecruitsArrowCleanerMixin.java"
Cohesion: 0.16
Nodes (5): Cited Findings, Cited Findings, CityVegetationMixin, RecruitsArrowCleanerMixin, TrackedEntityCameraMixin

### Community 167 - "Tier"
Cohesion: 0.29
Nodes (5): Tier, COUNTY, DUCHY, EMPIRE, KINGDOM

### Community 181 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 204 - "ConquestWorldScreen"
Cohesion: 0.22
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 217 - ".pack"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 218 - "CityPlacement.java"
Cohesion: 0.08
Nodes (4): CityPlacement, CountryPlacement, DojoPlacement, FarmPlacement

### Community 221 - "Siege"
Cohesion: 0.06
Nodes (16): Point, BlockGrid, Engine, LADDERS, RAMS, TOWERS, Gate, GateAction (+8 more)

### Community 223 - "Cited Findings"
Cohesion: 0.10
Nodes (5): DistantArmyRenderer, Figure, Hull, DistantShips, Far

### Community 224 - "DistantArmyRenderer.java"
Cohesion: 0.04
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 231 - "Kind"
Cohesion: 0.22
Nodes (7): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL

### Community 232 - "Cited Findings"
Cohesion: 0.10
Nodes (10): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, BattleSounds, ChargeSound, DinSound (+2 more)

### Community 235 - "Striker"
Cohesion: 0.33
Nodes (5): Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY

### Community 236 - "ShipKind"
Cohesion: 0.33
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 237 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 238 - "Builder"
Cohesion: 0.08
Nodes (7): ConquestContent, Builder, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, ConquestEntities

### Community 249 - "Recruiter"
Cohesion: 0.13
Nodes (6): Gold church, Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter, Recruiter

### Community 252 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 257 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "Race"
Cohesion: 0.10
Nodes (10): Demographics, Share, Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN (+2 more)

### Community 267 - "Type"
Cohesion: 0.33
Nodes (4): Type, CLEAR, DELIVER, SCOUT

### Community 274 - "Organizations"
Cohesion: 0.21
Nodes (4): Organization, Organizations, Sanctum, OrganizationsTest

### Community 275 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 283 - "3. GUI and map screens (strategy map, faction map, battle overlays)"
Cohesion: 0.40
Nodes (5): 3. GUI and map screens (strategy map, faction map, battle overlays), Cited Findings, Gaps, Inferences, Takeaway

### Community 288 - "6. Sodium (and Iris) compatibility"
Cohesion: 0.40
Nodes (5): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 300 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.04
Nodes (47): Ambushes from woods, Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Character page, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings) (+39 more)

### Community 302 - "Build"
Cohesion: 0.29
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 305 - "Job"
Cohesion: 0.10
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 306 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 308 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 311 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.17
Nodes (4): Camera, Card, Enlisted, Unit

### Community 325 - "7. Client tick, stutter and sound"
Cohesion: 0.40
Nodes (5): 7. Client tick, stutter and sound, Cited Findings, Gaps, Inferences, Takeaway

### Community 326 - ".buildCell"
Cohesion: 0.08
Nodes (3): Houses, CitySizesTest, StreetWalksTest

### Community 327 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **363 isolated node(s):** `BANDIT`, `CITY_STATE`, `EMPIRE`, `ARCHERS`, `CAVALRY` (+358 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1242 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **203 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `net.minecraft.world.item.DyeColor`, `net.minecraft.world.level.levelgen.structure.BoundingBox`, `.army`, `net.minecraft.server.MinecraftServer`, `SiegeWorks`, `net.minecraft.world.entity.Entity`, `net.minecraft.server.level.ServerPlayer`, `Peerage`, `WarCouncil`, `.second`, `PveCommands Registration`, `WarCamp`, `Override`, `net.minecraft.core.Direction`, `rafasconquest.faction.Faction`, `SiegeWorks.java`, `UnitMode`, `Shape`, `net.minecraft.server.level.ServerPlayer`, `net.minecraft.world.item.ItemStack`, `rafasconquest.battle.FieldBlock`, `RafasConquest.java`, `Calibration`, `Doctrine`, `Siege`, `ConquestEvents.java`, `.regroup`, `net.neoforged.bus.api.SubscribeEvent`, `Striker`, `Phase`, `UnitOrder`, `RecruitsBattle.java`, `Trait`?**
  _High betweenness centrality (0.062) - this node is a cross-community bridge._
- **What connects `BANDIT`, `CITY_STATE`, `EMPIRE` to the rest of the system?**
  _363 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.05748322923895443 - nodes in this community are weakly interconnected._
- **Why does `FieldBattle` connect `FieldBlock` to `.army`, `rafasconquest.battle.FieldBlock`, `.regroup`, `RecruitsBattle`, `Arm`, `BattlePlan`, `RecruitsBattle.java`, `GroundPlan`, `Siege`, `Call`?**
  _High betweenness centrality (0.037) - this node is a cross-community bridge._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.05672926447574335 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._