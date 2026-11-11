# Graph Report - RafasConquest  (2026-11-11)

## Corpus Check
- 461 files · ~395,306 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 6454 nodes · 20463 edges · 336 communities (140 shown, 196 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2708 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `4a4eab52`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FieldBlock
- org.jetbrains.annotations.Nullable
- net.minecraft.world.level.levelgen.structure.BoundingBox
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsArmies.java
- net.minecraft.server.MinecraftServer
- SiegeWorks
- FieldBlock
- Arm
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- Roads
- BaseMap
- ConquestNetwork
- RafasConquest.java
- BattlePlan
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.screens.Screen
- RafasConquest Commands (guide v0.0.2)
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- HorizontalStretch
- CityManagerScreen
- .plan
- rafasconquest.battle.BattlePlan.Point
- .shift
- WarCouncil
- Quests
- RecruitsBattle
- Lands
- CapitalLayout
- PveCommands Registration
- School
- CityPlan
- Land
- .begin
- FormationSnapshot
- net.minecraft.core.Direction
- rafasconquest.faction.Faction
- PoiPrecomputer.java
- Faction
- ConquestSavedData
- net.minecraft.server.level.ServerPlayer
- Personalities
- SeaLanes
- Armoury
- .drawEmpire
- .roadStart
- Cited Findings
- Centre
- PoliticalStyle
- Row
- .view
- SeatCities
- Faction Map View
- GameCharacter
- net.minecraft.world.level.block.state.BlockState
- TitleMapTest
- .measure
- StyleLibrary
- .render
- .onLogout
- BattleMap.java
- .render
- .postChunk
- DistantOcclusion
- CityGridBuilder
- MapLands
- org.spongepowered.asm.mixin.Mixin
- PlaceMusicClient
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- .rightClick
- net.neoforged.neoforge.network.handling.IPayloadContext
- SeaCalm
- Ownership & Territory
- .choose
- PoliticsTest
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- OrganizationScreen
- net.minecraft.world.item.ItemStack
- Barracks
- .generate
- Override
- WaterSwell
- FactionInfoScreen
- ClientScreens
- .world
- CityStyles
- MapLayer
- Realm
- MouthLayer.java
- rafasconquest.battle.FormationSnapshot
- RoadFeature
- .cellMax
- .of
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- MarkerScreen
- ClientModEvents.java
- net.minecraft.nbt.CompoundTag
- Battle code costs 0.2 ms; the multi-second freezes come from blocking
- CityLayout
- GrandStrategyScreen
- Terrain
- ClientPolitics
- net.minecraft.world.item.DyeColor
- UnitOrder
- Settlement
- .wallSegments
- RoadNetwork
- Ship
- BattleShips
- MapContext
- RafasConquest (PvE addon for Villager Recruits)
- BoardScreen
- Swell
- .build
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- ChunkMapCameraMixin.java
- net.minecraft.core.Vec3i
- net.minecraft.resources.ResourceLocation
- Lands
- .view
- Townsperson
- CLAUDE.md
- Ranks
- Kit
- .plan
- CourtView
- net.minecraft.world.level.block.Rotation
- Clash
- SableLink
- CourtSet
- Small Cluster 170
- ClientEvents
- .hash
- .boundsAt
- RoadLayer
- .pack
- Gate
- .onLevelLoad
- Siege
- Cited Findings
- DistantArmyRenderer.java
- UnitInfo
- .touches
- FormationSnapshot
- net.neoforged.bus.api.SubscribeEvent
- RankKits
- Strategy
- ConquestGameRules
- Recruiter
- Trait
- PoliticsData
- Call
- Ramming
- Task
- Profiling run sheet (baseline)
- Race
- Role
- DistantGeneration
- .named
- BlockGrid
- BattleKeepInventory
- Kind
- WarCamp
- Distribution
- .of
- .second
- Cities (grid, plots, vanilla village buildings)
- Memory: unbounded static caches, leaked Level/Entity references, large arrays
- Build
- org.jetbrains.annotations.Nullable
- Rank
- UnitMode
- Shape
- GatePlanksBlock.java
- BattleSite.Water
- CameraView
- Piece
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- Type
- State
- ConquestConfig
- ClientBarks
- Kind
- Kind
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 269 edges
2. `FieldBlock` - 196 edges
3. `FieldBattle` - 166 edges
4. `Point` - 126 edges
5. `ConquestNetwork` - 120 edges
6. `FieldArmy` - 111 edges
7. `Lands` - 89 edges
8. `Politics` - 79 edges
9. `BaseMap` - 77 edges
10. `BattleScreen` - 70 edges

## Surprising Connections (you probably didn't know these)
- `Conclusion` --references--> `RoadLayer`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/map/BaseMap.java
- `A tick budget and staggered schedules make the fixes stick` --references--> `RoadLayer`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Every frame pays for disk listings, per-rectangle flushes and unculled soldiers` --references--> `RoadLayer`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Cited Findings` --references--> `RoadLayer`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/RoadLayer.java

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

## Communities (336 total, 196 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.04
Nodes (11): Point, FieldArmy, Event, FieldBattle, Order, Target, Volley, Rates (+3 more)

### Community 1 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.05
Nodes (8): CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 2 - "net.minecraft.world.level.levelgen.structure.BoundingBox"
Cohesion: 0.06
Nodes (10): BarracksPiece, CityRoadPiece, CityTerrainPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece (+2 more)

### Community 3 - ".army"
Cohesion: 0.09
Nodes (3): DoctrineTest, FieldBattleTest, SiegeTest

### Community 4 - "CityTerrain"
Cohesion: 0.07
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.06
Nodes (9): 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), Cited Findings, Gaps, Inferences, Takeaway, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+1 more)

### Community 7 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.10
Nodes (5): FeudDuel, Goal, MusterBars, PoliticsSync, Wars

### Community 9 - "FieldBlock"
Cohesion: 0.05
Nodes (8): FieldBlock, ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN, Figure, Live

### Community 10 - "Arm"
Cohesion: 0.06
Nodes (23): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, Duty, BEARER, FLEEING (+15 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "Roads"
Cohesion: 0.09
Nodes (16): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+8 more)

### Community 13 - "BaseMap"
Cohesion: 0.17
Nodes (4): BaseMap, Nation, BaseMapStore, Ready

### Community 14 - "ConquestNetwork"
Cohesion: 0.04
Nodes (28): BackBanner, BaseMapPart, BattleSnapshot, BattleSpot, BoardAction, BoardView, CameraAt, CityView (+20 more)

### Community 15 - "RafasConquest.java"
Cohesion: 0.06
Nodes (4): ConquestContent, StreetWalks, ConquestEntities, RafasConquest

### Community 16 - "BattlePlan"
Cohesion: 0.06
Nodes (9): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, WarPlan (+1 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.15
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 19 - "net.minecraft.client.gui.screens.Screen"
Cohesion: 0.03
Nodes (9): Character page, Characters (holders, skills, prowess, traits), Court 3D scene (CourtView), Cultures (seven homelands), Faiths and cults, Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), GateScreen (+1 more)

### Community 20 - "RafasConquest Commands (guide v0.0.2)"
Cohesion: 0.05
Nodes (48): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+40 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.18
Nodes (5): Engine, LADDERS, RAMS, TOWERS, Plan

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.14
Nodes (6): Facts, Officer, Said, Talk, WarCouncil, WarCouncilPagesTest

### Community 29 - "Quests"
Cohesion: 0.08
Nodes (13): TechTreeScreen, DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind (+5 more)

### Community 30 - "RecruitsBattle"
Cohesion: 0.04
Nodes (15): Cited Findings, Camera, Card, Enlisted, GeneralTask, Officers, RecruitsBattle, Role (+7 more)

### Community 31 - "Lands"
Cohesion: 0.08
Nodes (13): Cited Findings, Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Lands, Stronghold (+5 more)

### Community 32 - "CapitalLayout"
Cohesion: 0.18
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.15
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 36 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 37 - ".begin"
Cohesion: 0.07
Nodes (6): BattleMap, GroundPlanTest, Land, Land, Land, Land

### Community 38 - "FormationSnapshot"
Cohesion: 0.18
Nodes (4): Cited Findings, Positions, Sprite, FormationSnapshotTest

### Community 39 - "net.minecraft.core.Direction"
Cohesion: 0.11
Nodes (7): Land, Result, SiegeSurvey, Wall, CityGates, Done, SiegeSurveyTest

### Community 42 - "PoiPrecomputer.java"
Cohesion: 0.10
Nodes (12): FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoliticalMap, FactionInfo (+4 more)

### Community 43 - "Faction"
Cohesion: 0.09
Nodes (6): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, MapPreview, Faction, FactionInfoTest

### Community 44 - "ConquestSavedData"
Cohesion: 0.08
Nodes (5): Cited Findings, ConquestSavedData, Poi, EconomyTick, ConquestSavedDataTest

### Community 45 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.06
Nodes (15): Bark, ChargeHits, CityClose, GrandStrategyView, Place, SeaSwell, TalkView, Msg (+7 more)

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "Armoury"
Cohesion: 0.25
Nodes (3): Armoury, Outfit, Style

### Community 50 - ".drawEmpire"
Cohesion: 0.29
Nodes (3): Hit, LabelLayer, LabelShape

### Community 52 - "Cited Findings"
Cohesion: 0.28
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 53 - "Centre"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 56 - ".view"
Cohesion: 0.06
Nodes (17): Advancement, Names, NobleTitle, COUNT, KNIGHT, NONE, Merit, Peer (+9 more)

### Community 57 - "SeatCities"
Cohesion: 0.12
Nodes (4): Grid, SeatCities, SeatCitiesTest, World

### Community 58 - "Faction Map View"
Cohesion: 0.13
Nodes (3): Cited Findings, FactionMapScreen, MapView

### Community 61 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (7): Sea, CommandTableBlock, GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, MarkerBlock

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 65 - "StyleLibrary"
Cohesion: 0.11
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - ".render"
Cohesion: 0.09
Nodes (6): CharacterScreen, Retainer, Spot, ClientPeerage, Banner, Portraits

### Community 67 - ".onLogout"
Cohesion: 0.15
Nodes (3): BackBanners, Throw, ThrownFigures

### Community 71 - ".postChunk"
Cohesion: 0.09
Nodes (7): Cited Findings, BarracksPeople, CityBanners, CityGuards, ConquestServer, StructureLink, PoiPrecomputer

### Community 72 - "DistantOcclusion"
Cohesion: 0.10
Nodes (7): Cited Findings, DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 73 - "CityGridBuilder"
Cohesion: 0.07
Nodes (10): CityGridBuilder, Ground, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA (+2 more)

### Community 75 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.14
Nodes (7): Cited Findings, Cited Findings, CityVegetationMixin, RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin, TrackedEntityCameraMixin

### Community 76 - "PlaceMusicClient"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 77 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.07
Nodes (11): GateDoors, SiegeFrames, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN (+3 more)

### Community 78 - ".rightClick"
Cohesion: 0.23
Nodes (3): Entry, Menu, HornOrders

### Community 79 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.15
Nodes (5): Builder, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 81 - "SeaCalm"
Cohesion: 0.31
Nodes (3): Depths, SeaCalm, SwellTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.20
Nodes (5): Candidate, OwnershipResolver, Claim, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.14
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 85 - "PoliticsTest"
Cohesion: 0.07
Nodes (4): Army, Event, War, PoliticsTest

### Community 86 - "Calibration"
Cohesion: 0.14
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.16
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "AgeTest.java"
Cohesion: 0.14
Nodes (7): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, AgeTest

### Community 95 - ".generate"
Cohesion: 0.05
Nodes (15): ProvinceNames, WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors, Homeland (+7 more)

### Community 96 - "Override"
Cohesion: 0.10
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, SettingsScreen, Politics.Front

### Community 100 - "ClientScreens"
Cohesion: 0.11
Nodes (6): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Gaps, Inferences, Takeaway, ClientScreens, ClientMapCache

### Community 101 - ".world"
Cohesion: 0.14
Nodes (4): Honour, Honours, RealmPlannerTest, World

### Community 103 - "MapLayer"
Cohesion: 0.19
Nodes (5): LegendEntry, MapLayer, Symbol, TerritoryLayer, UnitLayer

### Community 105 - "Realm"
Cohesion: 0.12
Nodes (4): Hook, Realm, Succession, Taking

### Community 106 - "MouthLayer.java"
Cohesion: 0.11
Nodes (4): BackBannerLayer, GateRenderer, MarkerRenderer, MouthLayer

### Community 107 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.27
Nodes (6): Boxes, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 109 - ".cellMax"
Cohesion: 0.08
Nodes (4): DistrictKey, Townsfolk, CitySizesTest, StreetWalksTest

### Community 110 - ".of"
Cohesion: 0.23
Nodes (3): FactionBanner, Layer, FactionBannerNbt

### Community 111 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.05
Nodes (36): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps (+28 more)

### Community 112 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.18
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips, Ride

### Community 116 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.07
Nodes (5): Palette, WallTemplates, Placed, MarkerBlockEntity, MarkerSpec

### Community 117 - "Battle code costs 0.2 ms; the multi-second freezes come from blocking"
Cohesion: 0.22
Nodes (7): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes

### Community 118 - "CityLayout"
Cohesion: 0.07
Nodes (6): CityLayout, District, Site, SlopeTile, WorldSettings, CityLayoutTest

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.15
Nodes (3): GrandStrategyScreen, Spot, GrandStrategyDateTest

### Community 123 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.36
Nodes (3): BattleKind, Part, ShipHulls

### Community 124 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 125 - "Settlement"
Cohesion: 0.08
Nodes (13): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+5 more)

### Community 126 - ".wallSegments"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 128 - "RoadNetwork"
Cohesion: 0.12
Nodes (5): Cited Findings, Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 130 - "BattleShips"
Cohesion: 0.25
Nodes (3): BattleShips, Helm, Ram

### Community 132 - "MapContext"
Cohesion: 0.17
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 133 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 136 - "Swell"
Cohesion: 0.24
Nodes (6): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Swell

### Community 137 - ".build"
Cohesion: 0.13
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.07
Nodes (27): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings (+19 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.06
Nodes (31): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings, Cited Findings (+23 more)

### Community 141 - "ChunkMapCameraMixin.java"
Cohesion: 0.21
Nodes (7): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway, ChunkMapAccess, ChunkMapCameraMixin

### Community 146 - "Lands"
Cohesion: 0.11
Nodes (4): Feud, Lord, Feuds, Front

### Community 148 - ".view"
Cohesion: 0.11
Nodes (5): PlayerStats, View, QuestProgress, Retinues, RetinuesTest

### Community 152 - "Ranks"
Cohesion: 0.17
Nodes (3): Ranks, Offer, Recruitment

### Community 155 - "Kit"
Cohesion: 0.15
Nodes (13): Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD, HEAVY_INFANTRY (+5 more)

### Community 156 - ".plan"
Cohesion: 0.12
Nodes (7): Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 157 - "CourtView"
Cohesion: 0.09
Nodes (6): Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 160 - "net.minecraft.world.level.block.Rotation"
Cohesion: 0.20
Nodes (5): Gold church, Quest board and quests, Recruitment posts and recruiter, GoldChurch, Layout

### Community 163 - "Clash"
Cohesion: 0.28
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 167 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 178 - ".hash"
Cohesion: 0.11
Nodes (7): ChunkCoord, FarmLayout, NobleAnnex, OriginRoller, Roll, SeedHash, NobleAnnexTest

### Community 181 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 206 - "RoadLayer"
Cohesion: 0.36
Nodes (5): 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom?, Gaps, Inferences, Takeaway, RoadLayer

### Community 217 - ".pack"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 221 - "Siege"
Cohesion: 0.15
Nodes (6): GateAction, Region, IN, OUT, WALL, Siege

### Community 223 - "Cited Findings"
Cohesion: 0.07
Nodes (11): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Cited Findings, Gaps, Inferences, Takeaway, Arrow, BattleSpotRing (+3 more)

### Community 224 - "DistantArmyRenderer.java"
Cohesion: 0.07
Nodes (5): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), DistantDepth, ArrowVolleys

### Community 232 - "FormationSnapshot"
Cohesion: 0.09
Nodes (11): Cited Findings, FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL (+3 more)

### Community 238 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 249 - "Recruiter"
Cohesion: 0.10
Nodes (4): WorldMapItem, Barks, Ear, Recruiter

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
Cohesion: 0.17
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 267 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 277 - "DistantGeneration"
Cohesion: 0.33
Nodes (3): Inferences, Api, DistantGeneration

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "WarCamp"
Cohesion: 0.12
Nodes (3): Saved, WarCamp, GateGeometryTest

### Community 292 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 298 - ".second"
Cohesion: 0.05
Nodes (15): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome, Phase, AFTERMATH (+7 more)

### Community 300 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.07
Nodes (23): Ambushes from woods, Base map generated from seed, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Demographics (population by culture and race), Faction Info screen (+15 more)

### Community 301 - "Memory: unbounded static caches, leaked Level/Entity references, large arrays"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway

### Community 302 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 305 - "Rank"
Cohesion: 0.06
Nodes (21): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+13 more)

### Community 306 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 308 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 312 - "Piece"
Cohesion: 0.13
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 316 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.33
Nodes (4): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender

### Community 319 - "Type"
Cohesion: 0.33
Nodes (4): Type, CLEAR, DELIVER, SCOUT

### Community 323 - "State"
Cohesion: 0.40
Nodes (4): State, CARRIED, RAISING, UP

### Community 327 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 332 - "Kind"
Cohesion: 0.50
Nodes (4): Kind, CLOSE, OPEN, RAM

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Roads (high, city, town roads; bridges; signposts)` → `Mustering (offensive and ceremonial)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **352 isolated node(s):** `ARCHERS`, `CAVALRY`, `SHIELDWALL`, `SWORDSMEN`, `FLANK` (+347 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1220 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **196 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `org.jetbrains.annotations.Nullable`, `net.minecraft.world.level.levelgen.structure.BoundingBox`, `.army`, `net.minecraft.server.MinecraftServer`, `SiegeWorks`, `FieldBlock`, `Arm`, `WarCouncil`, `PveCommands Registration`, `WarCamp`, `.begin`, `.of`, `net.minecraft.core.Direction`, `.second`, `net.minecraft.server.level.ServerPlayer`, `UnitMode`, `.roadStart`, `Shape`, `Row`, `GameCharacter`, `BattleMap.java`, `.postChunk`, `Calibration`, `net.minecraft.world.item.ItemStack`, `.generate`, `net.neoforged.bus.api.SubscribeEvent`, `UnitOrder`, `org.jetbrains.annotations.Nullable`, `net.minecraft.world.item.DyeColor`, `Trait`?**
  _High betweenness centrality (0.084) - this node is a cross-community bridge._
- **What connects `ARCHERS`, `CAVALRY`, `SHIELDWALL` to the rest of the system?**
  _352 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.04429530201342282 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `MapContext`, `net.minecraft.server.MinecraftServer`, `Roads`, `net.minecraft.client.gui.screens.Screen`, `list`, `.plan`, `.plan`, `WarCouncil`, `CourtView`, `School`, `PoiPrecomputer.java`, `Memory: unbounded static caches, leaked Level/Entity references, large arrays`, `org.jetbrains.annotations.Nullable`, `Armoury`, `Centre`, `PoliticalStyle`, `Row`, `SeatCities`, `TitleMapTest`, `.render`, `BattleMap.java`, `.postChunk`, `RoadLayer`, `.onLevelLoad`, `OrganizationScreen`, `.generate`, `ClientScreens`, `Realm`, `RoadFeature`, `Battle code costs 0.2 ms; the multi-second freezes come from blocking`, `GrandStrategyScreen`, `Terrain`, `Settlement`?**
  _High betweenness centrality (0.047) - this node is a cross-community bridge._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.04598490048043926 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Roads (high, city, town roads; bridges; signposts)` and `Mustering (offensive and ceremonial)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._