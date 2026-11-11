# Graph Report - RafasConquest  (2026-11-11)

## Corpus Check
- 463 files · ~399,588 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 6514 nodes · 20633 edges · 358 communities (149 shown, 209 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2741 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `ebe86639`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FieldBlock
- PoiPrecomputer.java
- net.minecraft.world.level.levelgen.structure.BoundingBox
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsLink
- net.minecraft.server.MinecraftServer
- SiegeWorks
- Cited Findings
- Arm
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- Roads
- BaseMap
- ConquestNetwork
- Poi
- BattlePlan
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.screens.Screen
- RafasConquest Commands (guide v0.0.2)
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- HorizontalStretch
- CityManagerScreen
- .plan
- RecruitsBattle
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
- .block
- FormationSnapshot
- net.minecraft.core.Direction
- rafasconquest.faction.Faction
- Faction
- War
- ConquestSavedData
- .view
- Personalities
- SeaLanes
- ConquestWorldgen
- .drawEmpire
- .buildCell
- TechTreeScreen
- Centre
- .postChunk
- Row
- Peerage
- SeatCities
- Faction Map View
- net.minecraft.world.level.Level
- net.minecraft.world.level.block.state.BlockState
- TitleMapTest
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- StyleLibrary
- .render
- net.minecraft.world.item.ItemStack
- BattleMap.java
- .render
- com.talhanation.recruits.entities.AbstractRecruitEntity
- DistantOcclusion
- CityGridBuilder
- MapLands
- ChunkMapCameraMixin.java
- PlaceMusicClient
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- .render
- PoliticalStyle
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
- OrganizationScreen
- Barracks
- .generate
- Override
- WaterSwell
- FactionInfoScreen
- ConquestWorldgen
- BattleType
- CityStyles
- net.minecraft.resources.ResourceLocation
- Realm
- MouthLayer.java
- rafasconquest.battle.FormationSnapshot
- RoadFeature
- Lands
- net.minecraft.network.FriendlyByteBuf
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- MarkerScreen
- ClientModEvents.java
- RecruitsBattle.java
- net.minecraft.nbt.CompoundTag
- RoadLayer
- CityLayout
- GrandStrategyScreen
- Terrain
- ClientPolitics
- Countryside
- UnitOrder
- Settlement
- .wallSegments
- RoadNetwork
- net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
- BattleShips
- MapContext
- net.minecraft.core.Vec3i
- BoardScreen
- .player
- MapLayer
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- ClientScreens
- DojoPlacement
- .step
- Feud
- .view
- Townsperson
- CLAUDE.md
- .get
- GroundPlan
- .plan
- CourtView
- Rank.java
- View
- CityVegetationMixin.java
- Kit
- Small Cluster 170
- ClientEvents
- BlockPos
- .boundsAt
- ConquestWorldScreen
- Duty
- .send
- .pack
- CityPlacement
- .onLevelLoad
- Siege
- Cited Findings
- DistantArmyRenderer.java
- CityTerrainPiece
- .touches
- Piece
- BattleSounds
- net.neoforged.bus.api.SubscribeEvent
- .bark
- SeatCitiesTest
- Phase
- net.neoforged.neoforge.network.handling.IPayloadContext
- ConquestGameRules
- Recruiter
- Trait
- .readPlacements
- Call
- Ramming
- Task
- Profiling run sheet (baseline)
- .measure
- Race
- WallTemplates
- .of
- CourtSet
- How do world generation and structure placement work, and what do they cost?
- Strategy
- GateScreen
- BattleKeepInventory
- .handleUnitsRequest
- Kind
- WarCamp
- Swell
- Ranks (Soldier to Grand Marshal)
- BattlePlan.java
- CountryPiece
- map
- Job
- UnitMode
- Shape
- GatePlanksBlock.java
- FarmPlacement
- net.minecraft.server.level.ServerPlayer
- Settlement
- .officers
- Role
- Kind
- Cited Findings
- HarbourCut
- rafasconquest.battle.FieldBlock
- Realm
- ClientBarks
- .people
- 3. GUI and map screens (strategy map, faction map, battle overlays)
- Distribution
- RankKits
- NobleDistrictTest
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- PoliticsData
- ConquestConfig
- BlockGrid
- ShipKind
- .walks
- TalkChoice
- How do world generation and structure placement work, and what do they cost?
- How do the simulation and daily-decision systems schedule work? Do they spike?

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
- `Conclusion` --references--> `RoadLayer`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Cited Findings` --references--> `RoadLayer`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Takeaway` --references--> `RoadLayer`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `A tick budget and staggered schedules make the fixes stick` --references--> `BaseMap`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/map/BaseMap.java

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

## Communities (358 total, 209 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.05
Nodes (7): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 1 - "PoiPrecomputer.java"
Cohesion: 0.06
Nodes (7): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), SeedHash

### Community 2 - "net.minecraft.world.level.levelgen.structure.BoundingBox"
Cohesion: 0.08
Nodes (8): BarracksPiece, CityRoadPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece, PlaceholderPiece

### Community 3 - ".army"
Cohesion: 0.06
Nodes (4): DoctrineTest, FieldBattleTest, GroundPlanTest, Land

### Community 4 - "CityTerrain"
Cohesion: 0.06
Nodes (21): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+13 more)

### Community 5 - "BattleScreen"
Cohesion: 0.08
Nodes (4): BattleScreen, Entry, Menu, HornOrders

### Community 6 - "DistantHorizonsLink"
Cohesion: 0.06
Nodes (9): 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), Cited Findings, Gaps, Inferences, Takeaway, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+1 more)

### Community 7 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.16
Nodes (3): Cited Findings, MusterBars, Wars

### Community 8 - "SiegeWorks"
Cohesion: 0.11
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 10 - "Arm"
Cohesion: 0.09
Nodes (14): Arm, ARCHERS, CAVALRY, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, SKIRMISHERS, ZWEIHANDLERS (+6 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "Roads"
Cohesion: 0.10
Nodes (16): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Land (+8 more)

### Community 13 - "BaseMap"
Cohesion: 0.14
Nodes (9): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, BaseMap, Nation, BaseMapStore (+1 more)

### Community 14 - "ConquestNetwork"
Cohesion: 0.08
Nodes (14): Bark, CameraAt, ChargeHits, CityClose, CityView, ConquestNetwork, FigureStrike, Place (+6 more)

### Community 15 - "Poi"
Cohesion: 0.10
Nodes (11): FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, Poi, PoiType (+3 more)

### Community 16 - "BattlePlan"
Cohesion: 0.05
Nodes (14): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, WarPlan (+6 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.14
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 19 - "net.minecraft.client.gui.screens.Screen"
Cohesion: 0.04
Nodes (15): Base map generated from seed, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Faction Info screen, Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, World map layers, Nations by zone (+7 more)

### Community 20 - "RafasConquest Commands (guide v0.0.2)"
Cohesion: 0.08
Nodes (31): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+23 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "RecruitsBattle"
Cohesion: 0.07
Nodes (3): Officers, RecruitsBattle, Side

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.15
Nodes (6): Facts, Officer, Said, Talk, WarCouncil, WarCouncilPagesTest

### Community 29 - "Quests"
Cohesion: 0.13
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 31 - "Lands"
Cohesion: 0.12
Nodes (5): Inferences, Lord, Feuds, Front, Politics

### Community 32 - "CapitalLayout"
Cohesion: 0.10
Nodes (9): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, CapitalRelocationTest (+1 more)

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 36 - "Land"
Cohesion: 0.26
Nodes (3): Heap, Search, ShippingRoutes

### Community 38 - "FormationSnapshot"
Cohesion: 0.09
Nodes (16): Cited Findings, FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, HEAVY_CAVALRY (+8 more)

### Community 39 - "net.minecraft.core.Direction"
Cohesion: 0.13
Nodes (7): Land, Result, SiegeSurvey, Wall, CityGates, Done, SiegeSurveyTest

### Community 43 - "War"
Cohesion: 0.09
Nodes (7): Army, Goal, Muster, CEREMONIAL, OFFENSIVE, PoliticsSync, War

### Community 44 - "ConquestSavedData"
Cohesion: 0.09
Nodes (6): Cited Findings, ConquestSavedData, MapPreview, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 45 - ".view"
Cohesion: 0.24
Nodes (7): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, GrandStrategy, View

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 50 - ".drawEmpire"
Cohesion: 0.27
Nodes (3): Hit, LabelLayer, LabelShape

### Community 51 - ".buildCell"
Cohesion: 0.08
Nodes (3): PlaceMusic, CitySizesTest, PlaceMusicTest

### Community 52 - "TechTreeScreen"
Cohesion: 0.17
Nodes (4): TechTreeScreen, Tech, TechTree, TechTreeTest

### Community 53 - "Centre"
Cohesion: 0.17
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 54 - ".postChunk"
Cohesion: 0.08
Nodes (7): Cited Findings, BarracksPeople, CityBanners, CityGuards, ConquestServer, StructureLink, PoiPrecomputer

### Community 56 - "Peerage"
Cohesion: 0.10
Nodes (9): Boards, Entry, View, Merit, Peer, Peerage, Taken, Quest (+1 more)

### Community 57 - "SeatCities"
Cohesion: 0.12
Nodes (3): NobleAnnex, City, CityIndex

### Community 58 - "Faction Map View"
Cohesion: 0.16
Nodes (3): Cited Findings, FactionMapScreen, MapView

### Community 60 - "net.minecraft.world.level.Level"
Cohesion: 0.11
Nodes (4): GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, WorldMapItem

### Community 61 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.09
Nodes (5): Saved, CommandTableBlock, GateBlock, Builder, MarkerBlock

### Community 63 - "TitleMapTest"
Cohesion: 0.18
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.13
Nodes (4): GateDoors, Frame, SableLink, SiegeFrames

### Community 66 - ".render"
Cohesion: 0.08
Nodes (8): CharacterScreen, Spot, ClientPeerage, Banner, Portraits, Names, Honour, Honours

### Community 67 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.11
Nodes (6): WarHorn, BackBanners, Armoury, Outfit, Style, ItemFlags

### Community 71 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.16
Nodes (6): Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER

### Community 72 - "DistantOcclusion"
Cohesion: 0.09
Nodes (8): Cited Findings, BattleSpotRing, DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 73 - "CityGridBuilder"
Cohesion: 0.08
Nodes (10): CityGridBuilder, Ground, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA (+2 more)

### Community 74 - "MapLands"
Cohesion: 0.05
Nodes (25): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+17 more)

### Community 75 - "ChunkMapCameraMixin.java"
Cohesion: 0.18
Nodes (7): Cited Findings, Gaps, Inferences, Mixins on server paths: what they hook, how hot, added cost, Takeaway, ChunkMapAccess, ChunkMapCameraMixin

### Community 76 - "PlaceMusicClient"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 77 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.10
Nodes (10): Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING, GateGeometry (+2 more)

### Community 78 - ".render"
Cohesion: 0.21
Nodes (3): DistantDepth, Arrow, ArrowVolleys

### Community 81 - "SeaCalm"
Cohesion: 0.16
Nodes (4): Sea, Depths, SeaCalm, SwellTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.17
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 84 - ".view"
Cohesion: 0.20
Nodes (4): Option, Talk, View, DialogueTest

### Community 86 - "Calibration"
Cohesion: 0.14
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.16
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 89 - "AgeTest.java"
Cohesion: 0.16
Nodes (7): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, AgeTest

### Community 95 - ".generate"
Cohesion: 0.06
Nodes (14): ProvinceNames, WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry, Homeland, DESERT (+6 more)

### Community 96 - "Override"
Cohesion: 0.15
Nodes (3): BattleSite.Water, SettingsScreen, Politics.Front

### Community 99 - "FactionInfoScreen"
Cohesion: 0.10
Nodes (5): FactionInfoScreen, Line, FactionBanner, Layer, FactionBannerNbt

### Community 100 - "ConquestWorldgen"
Cohesion: 0.09
Nodes (5): CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, FarmStructure

### Community 101 - "BattleType"
Cohesion: 0.15
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 105 - "Realm"
Cohesion: 0.08
Nodes (8): Hook, Kind, FAVOUR, SECRET, Succession, Taking, RealmPlannerTest, World

### Community 106 - "MouthLayer.java"
Cohesion: 0.11
Nodes (5): Cited Findings, BackBannerLayer, GateRenderer, MarkerRenderer, MouthLayer

### Community 107 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.31
Nodes (6): Boxes, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 108 - "RoadFeature"
Cohesion: 0.14
Nodes (3): Cited Findings, RoadFeature, Profile

### Community 109 - "Lands"
Cohesion: 0.09
Nodes (4): Cited Findings, Lands, Stronghold, Claim

### Community 110 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.07
Nodes (7): BattleSnapshot, BattleSpot, FactionInfoReply, PeerageView, PoliticsView, Volleys, WorldStretch

### Community 112 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.17
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips, Ride

### Community 116 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.06
Nodes (4): CharacterMarkers, Placed, MarkerBlockEntity, MarkerSpec

### Community 117 - "RoadLayer"
Cohesion: 0.11
Nodes (13): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+5 more)

### Community 118 - "CityLayout"
Cohesion: 0.07
Nodes (6): CityLayout, District, Site, SlopeTile, WorldSettings, CityLayoutTest

### Community 123 - "Countryside"
Cohesion: 0.19
Nodes (5): Countryside, Plan, Grid, CountrysideTest, World

### Community 124 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 126 - ".wallSegments"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 128 - "RoadNetwork"
Cohesion: 0.21
Nodes (3): RoadNetwork, Stretch, RoadNetworkTest

### Community 129 - "net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate"
Cohesion: 0.16
Nodes (5): DistantShips, Far, BattleKind, Part, ShipHulls

### Community 130 - "BattleShips"
Cohesion: 0.25
Nodes (3): BattleShips, Helm, Ram

### Community 132 - "MapContext"
Cohesion: 0.14
Nodes (5): MapContext, Place, PlaceMenuLayer, PoiIconLayer, FactionInfo

### Community 133 - "net.minecraft.core.Vec3i"
Cohesion: 0.11
Nodes (3): StylePiece, GoldChurch, Layout

### Community 136 - ".player"
Cohesion: 0.09
Nodes (7): Advancement, NobleTitle, COUNT, KNIGHT, NONE, PeerageSync, PeerageTest

### Community 137 - "MapLayer"
Cohesion: 0.15
Nodes (6): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer, UnitLayer

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.07
Nodes (28): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings (+20 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.06
Nodes (31): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 5. Water/ocean waves (sea swell, foam, boats on waves), 6. Sodium (and Iris) compatibility, 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings, Cited Findings (+23 more)

### Community 143 - ".step"
Cohesion: 0.20
Nodes (4): FigureMotion, Placed, State, FigureMotionTest

### Community 146 - "Feud"
Cohesion: 0.13
Nodes (6): Clash, Feud, Kind, DUEL, SKIRMISH, FeudDuel

### Community 148 - ".view"
Cohesion: 0.20
Nodes (3): PlayerStats, View, Retinues

### Community 152 - ".get"
Cohesion: 0.08
Nodes (12): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+4 more)

### Community 155 - "GroundPlan"
Cohesion: 0.12
Nodes (5): Ground, GroundPlan, Land, Land, Land

### Community 156 - ".plan"
Cohesion: 0.14
Nodes (6): Centre, Demographics, Share, Faith, RealmPlanner, Seat

### Community 157 - "CourtView"
Cohesion: 0.08
Nodes (5): Retainer, Camera, CourtView, Figure, NationScreen

### Community 160 - "Rank.java"
Cohesion: 0.17
Nodes (8): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Enlisted players, The general and guard, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Battle roles (frontline, bait, support, flanker, kill squad), Victory, pursuit and surrender, The war horn

### Community 163 - "View"
Cohesion: 0.12
Nodes (4): BoardView, GrandStrategyView, StatsView, TalkView

### Community 164 - "CityVegetationMixin.java"
Cohesion: 0.16
Nodes (6): Cited Findings, CityVegetationMixin, RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin, TrackedEntityCameraMixin

### Community 167 - "Kit"
Cohesion: 0.11
Nodes (18): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+10 more)

### Community 178 - "BlockPos"
Cohesion: 0.16
Nodes (4): GateSet, GateView, MarkerSet, MarkerView

### Community 181 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 204 - "ConquestWorldScreen"
Cohesion: 0.20
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 205 - "Duty"
Cohesion: 0.13
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 206 - ".send"
Cohesion: 0.12
Nodes (9): Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel, Type, CLEAR, DELIVER (+1 more)

### Community 217 - ".pack"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 221 - "Siege"
Cohesion: 0.08
Nodes (15): Point, Engine, LADDERS, RAMS, TOWERS, Gate, GateAction, GateState (+7 more)

### Community 223 - "Cited Findings"
Cohesion: 0.11
Nodes (7): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Gaps, Inferences, Takeaway, DistantArmyRenderer, Figure, Hull

### Community 224 - "DistantArmyRenderer.java"
Cohesion: 0.05
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 231 - "Piece"
Cohesion: 0.12
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 232 - "BattleSounds"
Cohesion: 0.20
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 236 - "SeatCitiesTest"
Cohesion: 0.15
Nodes (3): SeatCities, SeatCitiesTest, World

### Community 237 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 252 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 254 - ".readPlacements"
Cohesion: 0.24
Nodes (3): Offsets, Placement, PlacementOffsetsTest

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

### Community 275 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 277 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.32
Nodes (3): Inferences, Api, DistantGeneration

### Community 278 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 288 - ".handleUnitsRequest"
Cohesion: 0.25
Nodes (3): UnitInfo, UnitsRequest, UnitsState

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 295 - "Swell"
Cohesion: 0.24
Nodes (6): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Swell

### Community 298 - "Ranks (Soldier to Grand Marshal)"
Cohesion: 0.29
Nodes (8): Battles (prototype), Blocks of 20 soldiers, Gold church, Land grants (seven ways), Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter

### Community 300 - "BattlePlan.java"
Cohesion: 0.06
Nodes (24): Battle phases and days, Character page, Characters (holders, skills, prowess, traits), Court 3D scene (CourtView), Cultures (seven homelands), Demographics (population by culture and race), Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod) (+16 more)

### Community 302 - "CountryPiece"
Cohesion: 0.16
Nodes (7): Build, CASTLE, HAMLET, RUIN, TOWER, WELL, CountryPiece

### Community 303 - "map"
Cohesion: 0.05
Nodes (7): Kind, CLOSE, OPEN, RAM, StreetWalks, PoliticalMap, RafasConquest

### Community 305 - "Job"
Cohesion: 0.11
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 306 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 308 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 311 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.10
Nodes (6): Camera, CameraView, Card, Enlisted, Unit, CityManager

### Community 312 - "Settlement"
Cohesion: 0.20
Nodes (3): Settlement, CountryIndex, CountryPlacement

### Community 316 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 319 - "Kind"
Cohesion: 0.33
Nodes (6): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER

### Community 321 - "Cited Findings"
Cohesion: 0.08
Nodes (9): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, DistantArmies, ClientMapCache, Throw (+1 more)

### Community 322 - "HarbourCut"
Cohesion: 0.13
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 324 - "Realm"
Cohesion: 0.20
Nodes (5): Bond, Kind, FRIEND, RIVAL, Realm

### Community 327 - "3. GUI and map screens (strategy map, faction map, battle overlays)"
Cohesion: 0.40
Nodes (5): 3. GUI and map screens (strategy map, faction map, battle overlays), Cited Findings, Gaps, Inferences, Takeaway

### Community 328 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 345 - "ShipKind"
Cohesion: 0.33
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 351 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.50
Nodes (3): Gaps, How do world generation and structure placement work, and what do they cost?, Takeaway

### Community 352 - "How do the simulation and daily-decision systems schedule work? Do they spike?"
Cohesion: 0.67
Nodes (3): Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Takeaway

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **363 isolated node(s):** `ADVANCE`, `CAVALRY_CHARGE`, `CAVALRY_RECALL`, `WALL`, `GENERAL_CHARGES` (+358 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1242 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **209 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `PoiPrecomputer.java`, `net.minecraft.world.level.levelgen.structure.BoundingBox`, `SiegeWorks`, `Cited Findings`, `.of`, `Feud`, `WarCouncil`, `.second`, `PveCommands Registration`, `WarCamp`, `.block`, `net.minecraft.core.Direction`, `map`, `UnitMode`, `Shape`, `net.minecraft.server.level.ServerPlayer`, `Peerage`, `.officers`, `net.minecraft.world.item.ItemStack`, `rafasconquest.battle.FieldBlock`, `BattleMap.java`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `Calibration`, `net.neoforged.bus.api.SubscribeEvent`, `Phase`, `ConquestEvents.java`, `UnitOrder`, `RecruitsBattle.java`, `Trait`?**
  _High betweenness centrality (0.076) - this node is a cross-community bridge._
- **What connects `ADVANCE`, `CAVALRY_CHARGE`, `CAVALRY_RECALL` to the rest of the system?**
  _363 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.05457516339869281 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `PoiPrecomputer.java`, `MapContext`, `net.minecraft.server.MinecraftServer`, `net.minecraft.client.gui.screens.Screen`, `.plan`, `.plan`, `WarCouncil`, `CourtView`, `School`, `map`, `.postChunk`, `Settlement`, `SeatCities`, `Cited Findings`, `.render`, `net.minecraft.world.item.ItemStack`, `Realm`, `BattleMap.java`, `PoliticalStyle`, `.onLevelLoad`, `OrganizationScreen`, `.generate`, `RoadFeature`, `RecruitsBattle.java`, `RoadLayer`, `GrandStrategyScreen`, `Terrain`, `Countryside`, `Settlement`?**
  _High betweenness centrality (0.033) - this node is a cross-community bridge._
- **Should `PoiPrecomputer.java` be split into smaller, more focused modules?**
  _Cohesion score 0.062003968253968256 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._