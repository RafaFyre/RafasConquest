# Graph Report - RafasConquest  (2026-11-13)

## Corpus Check
- 511 files · ~458,712 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7324 nodes · 23400 edges · 372 communities (149 shown, 223 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3150 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `d56e7eb6`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- .enlist
- net.minecraft.world.level.block.state.BlockState
- net.minecraft.world.level.ChunkPos
- .army
- BattleScreen
- DistantHorizonsArmies.java
- CityGridBuilder
- Lands
- BattlePlan
- .findGenerationPoint
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- .view
- BaseMap
- UnitOrder
- PoliticsTest
- ConquestSavedData
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.GuiGraphics
- PveCommands.java
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- map
- HorizontalStretch
- CityManagerScreen
- .plan
- org.spongepowered.asm.mixin.Mixin
- rafasconquest.battle.FieldBlock
- WarCouncil
- net.minecraft.core.BlockPos
- net.minecraft.server.level.ServerPlayer
- SimpleChannel.java
- BackBannerLayer.java
- PveCommands Registration
- School
- CityPlan
- Front
- .of
- Arm
- .map
- rafasconquest.faction.Faction
- net.minecraft.client.renderer.MultiBufferSource
- Lands
- GateBlockEntity.java
- RecruitsBattle
- FieldBlock
- Personalities
- SeaLanes
- Recruiter
- Cited Findings
- .deployment
- ProvinceMap
- .plan
- Barracks
- .roadStart
- CityStyles
- SeatCities
- Faction Map View
- .step
- ClientScreens
- net.minecraft.world.level.block.Rotation
- TitleMapTest
- Bodies
- StyleLibrary
- .step
- TechTreeScreen
- BattleMap.java
- Row
- .generate
- net.minecraft.world.item.DyeColor
- .view
- MapLands
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- NobleAnnex
- Quests
- ClientEvents
- Kit
- net.minecraft.server.level.ServerLevel
- Ownership & Territory
- CityLayout
- PoliticsData
- Land
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- Faction
- .postChunk
- BattleSounds
- Terrain
- CityLayout
- WarCamp
- FactionInfoScreen
- Point
- CityTerrainPiece
- Cloth
- FigureBatches
- Peerage
- Body
- ClothRenderer
- Override
- DayClockTest
- NationsTest
- SiegeWorks
- Hull
- WaterSwell
- Gate
- OrganizationScreen
- CapeCurio.java
- Cited Findings
- HarbourCut
- GrandStrategyScreen
- CityTerrain
- WorldSettings
- Grade
- .world
- SeatCitiesTest
- CountryStructure
- BattleShips
- GrappleHook
- MapContext
- .roadStart
- .shift
- .findGenerationPoint
- SeaCalm
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .onLevelLoad
- BattleStandardItem.java
- .height
- .drawEmpire
- MapLayer
- RoadNetwork
- CLAUDE.md
- War
- Ranks
- net.minecraft.resources.ResourceLocation
- .touches
- RoadNetwork
- Machine
- RecruitFigures
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- ShipHulls
- BlockPos
- Body
- DataFiles
- .onRegisterClientCommands
- .noble
- Duty
- ConquestNetwork
- net.minecraft.network.FriendlyByteBuf
- CourtView
- BattleSnapshot
- RecruitFigures.java
- Cited Findings
- .nearest
- .render
- Dialogue
- Townsperson
- net.neoforged.bus.api.SubscribeEvent
- .load
- Piece
- DistantArmyRenderer
- Cited Findings
- Kind
- ConquestGameRules
- .boundsAt
- GateBlockEntity
- WarPlan
- Politics.Front
- Mixins on server paths: what they hook, how hot, added cost
- Roads
- WarHorn
- Profiling run sheet (baseline)
- .measure
- net.minecraft.sounds.SoundEvent
- SableSeen
- WallTemplates
- BattleKeepInventory
- ClientPolitics
- GateScreen
- CourtView
- .at
- Realm
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- CityLayout
- 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound
- Kind
- CityPlacement
- How Total War calculates battles between units
- Wind.java
- RecruitsBattle.java
- CameraView
- .lineUp
- .render
- .order
- BattleType
- Call
- .bark
- Override
- Feud
- RankKits
- Rank
- uuid
- ClientBarks
- State
- locale
- Sea
- FieldRules
- .plus
- .startAt
- Kind
- net.neoforged.fml.common.EventBusSubscriber
- BlockGrid
- Siege
- ShipKind
- State

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 300 edges
2. `FieldBlock` - 222 edges
3. `FieldBattle` - 212 edges
4. `FieldArmy` - 127 edges
5. `ConquestNetwork` - 119 edges
6. `Point` - 108 edges
7. `Lands` - 89 edges
8. `Siege` - 84 edges
9. `BattleScreen` - 82 edges
10. `Politics` - 79 edges

## Surprising Connections (you probably didn't know these)
- `Inferences` --references--> `ChunkMapCameraMixin`  [INFERRED]
  research_notes/RafasConquest performance gameplan/client_techniques.md → src/main/java/rafasconquest/mixin/ChunkMapCameraMixin.java
- `Conclusion` --references--> `RoadLayer`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Cited Findings` --references--> `RoadLayer`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Takeaway` --references--> `RoadLayer`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `A tick budget and staggered schedules make the fixes stick` --references--> `BaseMap`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/map/BaseMap.java

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

## Communities (372 total, 223 thin omitted)

### Community 0 - ".enlist"
Cohesion: 0.05
Nodes (18): Outcome, Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT (+10 more)

### Community 1 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.05
Nodes (9): BattleBannerBlock, BattleWallBannerBlock, CommandTableBlock, GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, Builder (+1 more)

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.06
Nodes (15): BarracksPiece, CityRoadPiece, Build, CASTLE, HAMLET, RUIN, TOWER, WELL (+7 more)

### Community 3 - ".army"
Cohesion: 0.06
Nodes (5): DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, SiegeTest, GrandStrategyDateTest

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.06
Nodes (9): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+1 more)

### Community 7 - "CityGridBuilder"
Cohesion: 0.09
Nodes (10): CityGridBuilder, Ground, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA (+2 more)

### Community 8 - "Lands"
Cohesion: 0.11
Nodes (4): Cited Findings, Lands, Stronghold, Claim

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - ".view"
Cohesion: 0.12
Nodes (4): PlayerStats, View, Retinues, RetinuesTest

### Community 13 - "BaseMap"
Cohesion: 0.07
Nodes (11): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, MapPreview, BaseMap, Nation (+3 more)

### Community 14 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 16 - "ConquestSavedData"
Cohesion: 0.08
Nodes (5): Cited Findings, ConquestSavedData, Poi, EconomyTick, ConquestSavedDataTest

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.15
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 19 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.05
Nodes (5): Character page, Court 3D scene (CourtView), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), InventoryStatsButton

### Community 20 - "PveCommands.java"
Cohesion: 0.10
Nodes (26): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+18 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 22 - "map"
Cohesion: 0.04
Nodes (6): Ambushes from woods, Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), Deploying to the land (GroundPlan), StreetWalks

### Community 25 - ".plan"
Cohesion: 0.21
Nodes (9): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+1 more)

### Community 26 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.09
Nodes (17): Battle code costs 0.2 ms; the multi-second freezes come from blocking, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, Cited Findings, Gaps, Inferences, Takeaway, Cited Findings, ChunkMapAccess (+9 more)

### Community 27 - "rafasconquest.battle.FieldBlock"
Cohesion: 0.04
Nodes (9): Cited Findings, Landing, Live, Shape, LINE, SQUARE, V, WEDGE (+1 more)

### Community 28 - "WarCouncil"
Cohesion: 0.14
Nodes (6): Facts, Officer, Said, Talk, WarCouncil, WarCouncilPagesTest

### Community 29 - "net.minecraft.core.BlockPos"
Cohesion: 0.10
Nodes (5): Saved, GateGeometry, CharacterMarkers, Placed, GateGeometryTest

### Community 30 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.11
Nodes (8): Advancement, Boards, Entry, View, CityManager, GrandStrategy, View, QuestProgress

### Community 31 - "SimpleChannel.java"
Cohesion: 0.08
Nodes (4): BackBanner, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.15
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 36 - "Front"
Cohesion: 0.12
Nodes (6): Engine, LADDERS, RAMS, TOWERS, Front, Rows

### Community 37 - ".of"
Cohesion: 0.18
Nodes (4): FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 38 - "Arm"
Cohesion: 0.10
Nodes (14): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+6 more)

### Community 39 - ".map"
Cohesion: 0.11
Nodes (5): Cited Findings, Goal, MusterBars, PoliticsSync, Wars

### Community 41 - "net.minecraft.client.renderer.MultiBufferSource"
Cohesion: 0.12
Nodes (3): BattleBannerRenderer, GateRenderer, MarkerRenderer

### Community 42 - "Lands"
Cohesion: 0.11
Nodes (8): Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Lord, Feuds, Front, Politics

### Community 43 - "GateBlockEntity.java"
Cohesion: 0.08
Nodes (3): BattleBannerBlockEntity, MarkerBlockEntity, MarkerSpec

### Community 44 - "RecruitsBattle"
Cohesion: 0.04
Nodes (23): Cited Findings, Card, Enlisted, Figure, HornCall, ATTACKING, MOVING, RALLYING (+15 more)

### Community 45 - "FieldBlock"
Cohesion: 0.04
Nodes (9): The core change: morale becomes a sum, FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock (+1 more)

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.14
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "Recruiter"
Cohesion: 0.06
Nodes (5): ConquestContent, GrappleHooks, WorldMapItem, ConquestEntities, Recruiter

### Community 50 - "Cited Findings"
Cohesion: 0.09
Nodes (9): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, DistantArmies, ClientMapCache, Throw (+1 more)

### Community 52 - "ProvinceMap"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 53 - ".plan"
Cohesion: 0.12
Nodes (8): Centre, Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 54 - "Barracks"
Cohesion: 0.13
Nodes (3): Barracks, Site, BarracksTest

### Community 57 - "SeatCities"
Cohesion: 0.14
Nodes (4): City, Grid, SeatCities, CityIndex

### Community 58 - "Faction Map View"
Cohesion: 0.16
Nodes (3): Cited Findings, FactionMapScreen, MapView

### Community 59 - ".step"
Cohesion: 0.12
Nodes (6): FigureMotion, Ground, Match, Placed, State, FigureMotionTest

### Community 61 - "net.minecraft.world.level.block.Rotation"
Cohesion: 0.09
Nodes (6): Gold church, Quest board and quests, Recruitment posts and recruiter, StylePiece, GoldChurch, Layout

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.11
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - ".step"
Cohesion: 0.08
Nodes (19): Cited Findings, Engine, FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, ENGINES (+11 more)

### Community 69 - "BattleMap.java"
Cohesion: 0.04
Nodes (3): Battle map developer web page, Morale, rout and rally, RafasConquest

### Community 71 - ".generate"
Cohesion: 0.06
Nodes (20): ProvinceNames, BaseMapGenerator, Heights, MapColors, Homeland, DESERT, JUNGLE, PLAINS (+12 more)

### Community 72 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.18
Nodes (4): DistantShips, Far, Riding, Ride

### Community 73 - ".view"
Cohesion: 0.11
Nodes (12): Merit, Peer, Taken, Quest, Type, CLEAR, DELIVER, SCOUT (+4 more)

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.07
Nodes (27): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+19 more)

### Community 77 - "Quests"
Cohesion: 0.23
Nodes (7): Ladder, MILITARY, NOBLE, NONE, Kind, Quests, Target

### Community 78 - "ClientEvents"
Cohesion: 0.09
Nodes (3): Post, BackBanners, ClientEvents

### Community 79 - "Kit"
Cohesion: 0.11
Nodes (19): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+11 more)

### Community 81 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.09
Nodes (8): GateDoors, Land, Result, SiegeSurvey, Wall, CityGates, Done, SiegeSurveyTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "CityLayout"
Cohesion: 0.15
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 85 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 86 - "Calibration"
Cohesion: 0.14
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.07
Nodes (21): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+13 more)

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - "Faction"
Cohesion: 0.07
Nodes (11): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction, ResourceType, FOOD, PoiType, CAPITAL (+3 more)

### Community 92 - ".postChunk"
Cohesion: 0.10
Nodes (5): BarracksPeople, CityBanners, CityGuards, ConquestServer, PoiPrecomputer

### Community 94 - "BattleSounds"
Cohesion: 0.23
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 96 - "CityLayout"
Cohesion: 0.04
Nodes (13): /pve debug citystats [samples], /pve debug cityterrain, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, CityLayout, BiomePalette, Entry, CityStructure (+5 more)

### Community 99 - "FactionInfoScreen"
Cohesion: 0.16
Nodes (3): FactionInfoScreen, Line, StructureLink

### Community 100 - "Point"
Cohesion: 0.13
Nodes (5): Point, Face, Gate, Plan, WallTower

### Community 102 - "Cloth"
Cohesion: 0.08
Nodes (6): GatePlanksBlock, Cloth, Solid, ClothTest, OffTheBackTest, WoolTest

### Community 103 - "FigureBatches"
Cohesion: 0.07
Nodes (11): ShipSprite, Batch, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED (+3 more)

### Community 105 - "Peerage"
Cohesion: 0.07
Nodes (17): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+9 more)

### Community 106 - "Body"
Cohesion: 0.11
Nodes (18): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+10 more)

### Community 107 - "ClothRenderer"
Cohesion: 0.16
Nodes (7): BannerArt, Carried, ClothRenderer, Fabric, Kind, Layer, Live

### Community 109 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 112 - "Hull"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 113 - "WaterSwell"
Cohesion: 0.14
Nodes (3): Made, WaterSwell, SeaFoamTest

### Community 117 - "Cited Findings"
Cohesion: 0.17
Nodes (11): A tick budget and staggered schedules make the fixes stick, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom?, Gaps (+3 more)

### Community 121 - "CityTerrain"
Cohesion: 0.06
Nodes (17): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+9 more)

### Community 122 - "WorldSettings"
Cohesion: 0.10
Nodes (10): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, WorldSettings (+2 more)

### Community 123 - "Grade"
Cohesion: 0.16
Nodes (9): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Plan (+1 more)

### Community 126 - "CountryStructure"
Cohesion: 0.10
Nodes (11): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+3 more)

### Community 128 - "BattleShips"
Cohesion: 0.16
Nodes (4): BattleShips, Helm, Ram, SableLink

### Community 129 - "GrappleHook"
Cohesion: 0.16
Nodes (3): GrappleHookRenderer, Registration, GrappleHook

### Community 131 - ".roadStart"
Cohesion: 0.09
Nodes (5): Frame, Harbour, CityLayoutTest, CitySizesTest, StreetWalksTest

### Community 132 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 134 - "SeaCalm"
Cohesion: 0.18
Nodes (5): Depths, SeaCalm, Strengths, SeaCalmTest, SwellTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.10
Nodes (19): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, Cited Findings, Cited Findings, Gaps, Gaps (+11 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (41): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 6. Sodium (and Iris) compatibility, 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply (+33 more)

### Community 143 - ".height"
Cohesion: 0.24
Nodes (6): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Swell

### Community 146 - ".drawEmpire"
Cohesion: 0.27
Nodes (3): Hit, LabelLayer, LabelShape

### Community 148 - "MapLayer"
Cohesion: 0.18
Nodes (5): LegendEntry, MapLayer, Symbol, TerritoryLayer, UnitLayer

### Community 152 - "War"
Cohesion: 0.08
Nodes (5): Army, Muster, CEREMONIAL, OFFENSIVE, War

### Community 155 - "Ranks"
Cohesion: 0.13
Nodes (3): Ranks, Offer, Recruitment

### Community 160 - "RoadNetwork"
Cohesion: 0.14
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 163 - "Machine"
Cohesion: 0.10
Nodes (12): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+4 more)

### Community 164 - "RecruitFigures"
Cohesion: 0.10
Nodes (8): Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures, RecruitFiguresTest

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.18
Nodes (3): Armoury, Outfit, Style

### Community 178 - "ShipHulls"
Cohesion: 0.33
Nodes (3): BattleKind, Part, ShipHulls

### Community 181 - "BlockPos"
Cohesion: 0.10
Nodes (5): BoardScreen, BoardAction, GateView, MarkerSet, MarkerView

### Community 186 - "Body"
Cohesion: 0.10
Nodes (15): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD, Boxes (+7 more)

### Community 205 - ".onRegisterClientCommands"
Cohesion: 0.25
Nodes (4): Mode, OFF, ON, VIEW

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 217 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 218 - "ConquestNetwork"
Cohesion: 0.04
Nodes (25): Bark, CameraAt, ChargeHits, CityClose, CityView, ConquestNetwork, FigureStrike, GateSet (+17 more)

### Community 219 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.07
Nodes (9): Morale, BattleSpot, BoardView, FactionInfoReply, PoliticsView, StatsView, UnitInfo, UnitsRequest (+1 more)

### Community 221 - "CourtView"
Cohesion: 0.12
Nodes (6): CourtSet, Seat, Style, Camera, CourtView, Figure

### Community 225 - "Cited Findings"
Cohesion: 0.13
Nodes (8): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, Arrow, ArrowVolleys, BattleSpotRing

### Community 228 - ".render"
Cohesion: 0.07
Nodes (9): CharacterScreen, Retainer, Spot, ClientPeerage, Banner, Portraits, Names, Honour (+1 more)

### Community 232 - "Townsperson"
Cohesion: 0.13
Nodes (4): Walk, DistrictKey, Townsfolk, Townsperson

### Community 236 - "Piece"
Cohesion: 0.13
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.08
Nodes (5): Cited Findings, DistantDepth, DistantArmyRenderer, Figure, Rects

### Community 240 - "Cited Findings"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?

### Community 242 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 248 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 249 - "GateBlockEntity"
Cohesion: 0.09
Nodes (8): Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING, Hinge

### Community 252 - "WarPlan"
Cohesion: 0.07
Nodes (15): WarPlan, GameCharacter, Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN (+7 more)

### Community 255 - "Mixins on server paths: what they hook, how hot, added cost"
Cohesion: 0.50
Nodes (4): Gaps, Inferences, Mixins on server paths: what they hook, how hot, added cost, Takeaway

### Community 256 - "Roads"
Cohesion: 0.21
Nodes (5): Link, Place, Roads, Spur, RoadsTest

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 275 - "CourtView"
Cohesion: 0.13
Nodes (3): NationScreen, Demographics, Share

### Community 277 - "Realm"
Cohesion: 0.10
Nodes (6): Hook, Realm, Succession, Taking, RealmPlannerTest, World

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.18
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 288 - "9. Memory: client caches, textures and NativeImages that are never freed or grow without bound"
Cohesion: 0.25
Nodes (5): 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Gaps, Inferences, Takeaway

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 292 - "How Total War calculates battles between units"
Cohesion: 0.11
Nodes (17): 1. Combat is per soldier, not per unit, 1. Flank and rear shock, 2. Does the swing land?, 2. Nearby rout (fright), 3. Army-wide losses, 3. How much damage the hit does, 4. Charges, 4. Shattering (+9 more)

### Community 295 - "RecruitsBattle.java"
Cohesion: 0.03
Nodes (17): /pve debug recruits, /pve politics, /pve politics war <attacker> <defender> [generals], Battle phases and days, Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Enlisted players, Envelopment (+9 more)

### Community 297 - "CameraView"
Cohesion: 0.16
Nodes (3): Camera, CameraView, Chunks

### Community 298 - ".lineUp"
Cohesion: 0.19
Nodes (3): Entry, Menu, HornOrders

### Community 301 - ".order"
Cohesion: 0.25
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

### Community 302 - "BattleType"
Cohesion: 0.22
Nodes (5): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE

### Community 305 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 310 - "Override"
Cohesion: 0.15
Nodes (5): BattleSite.Water, BattleSite.Water, ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 311 - "Feud"
Cohesion: 0.14
Nodes (6): Clash, Feud, Kind, DUEL, SKIRMISH, FeudDuel

### Community 315 - "Rank"
Cohesion: 0.06
Nodes (21): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+13 more)

### Community 318 - "uuid"
Cohesion: 0.08
Nodes (7): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, PoliticalMap, FactionInfo

### Community 323 - "State"
Cohesion: 0.40
Nodes (5): State, RALLIED, ROUTED, SHATTERED, STEADY

### Community 324 - "locale"
Cohesion: 0.07
Nodes (30): Base map generated from seed, Battles (prototype), Blocks of 20 soldiers, Characters (holders, skills, prowess, traits), Command Table block, Countryside (hamlets, villages, watchtowers, castles), Cultures (seven homelands), Demographics (population by culture and race) (+22 more)

### Community 329 - "FieldRules"
Cohesion: 0.27
Nodes (3): FieldRules, ShareLiveTest, W

### Community 330 - ".plus"
Cohesion: 0.08
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 336 - ".startAt"
Cohesion: 0.07
Nodes (5): Inferences, Api, DistantGeneration, Side, WarCampTest

### Community 337 - "Kind"
Cohesion: 0.50
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 363 - "Siege"
Cohesion: 0.12
Nodes (6): GateAction, Region, IN, OUT, WALL, Siege

### Community 370 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 373 - "State"
Cohesion: 0.50
Nodes (4): State, CARRIED, RAISING, UP

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **414 isolated node(s):** `SOLDIER`, `LEADER`, `GENERAL`, `BEARER`, `GUARD` (+409 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1371 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **223 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `.enlist`, `GrappleHook`, `net.minecraft.world.level.ChunkPos`, `.army`, `UnitOrder`, `map`, `rafasconquest.battle.FieldBlock`, `WarCouncil`, `net.minecraft.server.level.ServerPlayer`, `SimpleChannel.java`, `PveCommands Registration`, `Machine`, `net.minecraft.world.item.ItemStack`, `RecruitsBattle.java`, `rafasconquest.faction.Faction`, `.map`, `FieldBlock`, `Recruiter`, `.roadStart`, `BattleMap.java`, `Row`, `.startAt`, `net.minecraft.server.level.ServerLevel`, `Calibration`, `CityLayout`, `WarCamp`, `net.neoforged.bus.api.SubscribeEvent`, `SiegeWorks`, `WarPlan`?**
  _High betweenness centrality (0.062) - this node is a cross-community bridge._
- **What connects `SOLDIER`, `LEADER`, `GENERAL` to the rest of the system?**
  _414 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `.enlist` be split into smaller, more focused modules?**
  _Cohesion score 0.047082494969818915 - nodes in this community are weakly interconnected._
- **Why does `FieldBattle` connect `FieldBlock` to `Bodies`, `.enlist`, `PveCommands Registration`, `.army`, `Machine`, `RecruitsBattle.java`, `.plus`, `Siege`, `RecruitsBattle`, `BattleType`, `net.minecraft.network.FriendlyByteBuf`, `.startAt`, `Call`, `map`, `rafasconquest.battle.FieldBlock`, `WarPlan`?**
  _High betweenness centrality (0.039) - this node is a cross-community bridge._
- **Should `net.minecraft.world.level.block.state.BlockState` be split into smaller, more focused modules?**
  _Cohesion score 0.04858721718277552 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._