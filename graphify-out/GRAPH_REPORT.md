# Graph Report - RafasConquest  (2026-11-12)

## Corpus Check
- 510 files · ~455,673 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7299 nodes · 23310 edges · 366 communities (153 shown, 213 thin omitted)
- Extraction: 86% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3145 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `598706c2`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FieldBlock
- net.minecraft.world.level.block.state.BlockState
- net.minecraft.world.level.ChunkPos
- .army
- MapContext
- BattleScreen
- DistantHorizonsArmies.java
- CityGridBuilder
- Politics
- Lands
- .roadStart
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- net.minecraft.core.BlockPos
- BaseMap
- UnitOrder
- PoliticsTest
- Point
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.GuiGraphics
- PveCommands.java
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- org.jetbrains.annotations.Nullable
- HorizontalStretch
- CityManagerScreen
- .plan
- org.spongepowered.asm.mixin.Mixin
- .shift
- WarCouncil
- .get
- .send
- net.minecraft.client.renderer.MultiBufferSource
- PveCommands Registration
- School
- CityPlan
- ConquestServer
- ConquestNetwork
- PoiPrecomputer.java
- .map
- rafasconquest.faction.Faction
- .feudFight
- Lands
- Capture
- net.minecraft.nbt.CompoundTag
- Front
- Personalities
- SeaLanes
- net.minecraft.network.FriendlyByteBuf
- rafasconquest.battle.FormationSnapshot
- Rank
- MapPreview.java
- Centre
- Barracks
- Body
- CityStyles
- SeatCities
- Faction Map View
- .step
- ClientScreens
- Quest
- TitleMapTest
- Bodies
- StyleLibrary
- .step
- .choose
- RafasConquest.java
- Row
- net.minecraft.server.level.ServerLevel
- Ride
- BattlePlan
- MapLands
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- .world
- .board
- Faction
- ClothRenderer
- Terrain
- Ownership & Territory
- CityLayout
- Work
- Land
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- ThrownFigures
- Kind
- .enlist
- .generate
- net.minecraft.core.Vec3i
- .place
- FactionInfoScreen
- Siege
- CityTerrainPiece
- Cloth
- FigureBatches
- Peerage
- .onLogout
- net.neoforged.neoforge.network.handling.IPayloadContext
- Override
- net.minecraft.sounds.SoundEvent
- FigureGear
- City
- .inCity
- WaterSwell
- Gate
- ConquestSavedData
- .of
- Cited Findings
- .build
- GrandStrategyScreen
- CityTerrain
- WorldSettings
- .siege
- .view
- MarkerSpec
- BattleSounds
- BattleShips
- GrappleHook
- MapView
- Roads
- .findGenerationPoint
- SeaCalm
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .onLevelLoad
- Homeland
- .renderByItem
- net.minecraft.resources.ResourceLocation
- net.minecraft.client.gui.GuiGraphics
- RoadNetwork
- CLAUDE.md
- Override
- SeatCitiesTest
- .plan
- ShipHulls
- org.joml.Vector3d
- TechTreeScreen
- CityLayout
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- How do world generation and structure placement work, and what do they cost?
- net.minecraft.core.Vec3i
- BoardScreen
- SoldierFigure
- RafasConquest.java
- Plan
- .noble
- .passOnOrders
- .view
- Recruiter
- OrganizationScreen
- .findGenerationPoint
- RecruitFigures.java
- DistantOcclusion
- .readPlacements
- .stat
- BattleSnapshot
- Townsperson
- ConquestEvents
- com.mojang.blaze3d.vertex.VertexConsumer
- Kind
- DistantArmyRenderer
- GatePlanksBlock.java
- WallTemplates
- ConquestGameRules
- .boundsAt
- GateBlockEntity
- Trait
- .bark
- RafasConquest (PvE addon for Villager Recruits)
- Ranks
- Cities (grid, plots, vanilla village buildings)
- Profiling run sheet (baseline)
- .measure
- RecruitsBattle
- ClothItemRenderer
- GateScreen
- BattleKeepInventory
- ClientPolitics
- .at
- CourtView
- WoolTest
- Realm
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- Corners
- Build
- Kind
- WarCamp.java
- How Total War calculates battles between units
- Role
- NobleAnnex
- Task
- ShipKind
- ConquestConfig
- Wind.java
- .postChunk
- Call
- Striker
- Phase
- .rightClick
- ConquestWorldScreen
- UnitMode
- rafasconquest.character.Rank
- arraylist
- ClientBarks
- 6. Sodium (and Iris) compatibility
- UnitsState
- HornCall
- BaseMapPart
- Kind
- .sendPeerage
- ShareLiveTest
- Cited Findings
- WarCamp
- State
- BattleSite.Water
- .ships
- SetSetting
- NobleDistrictTest

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 296 edges
2. `FieldBlock` - 256 edges
3. `FieldBattle` - 221 edges
4. `FieldArmy` - 127 edges
5. `ConquestNetwork` - 119 edges
6. `Lands` - 89 edges
7. `Siege` - 84 edges
8. `BattleScreen` - 82 edges
9. `Politics` - 79 edges
10. `BaseMap` - 77 edges

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

## Communities (366 total, 213 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.04
Nodes (8): The core change: morale becomes a sum, FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 1 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.05
Nodes (9): BattleBannerBlock, BattleWallBannerBlock, GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, WorldMapItem, Builder (+1 more)

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.07
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - ".army"
Cohesion: 0.07
Nodes (3): DoctrineTest, FieldBattleBodiesTest, FieldBattleTest

### Community 4 - "MapContext"
Cohesion: 0.17
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.07
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "CityGridBuilder"
Cohesion: 0.09
Nodes (10): CityGridBuilder, Ground, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA (+2 more)

### Community 8 - "Politics"
Cohesion: 0.10
Nodes (9): Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Muster, CEREMONIAL, OFFENSIVE, Politics (+1 more)

### Community 9 - "Lands"
Cohesion: 0.09
Nodes (4): Cited Findings, Lands, Stronghold, Claim

### Community 10 - ".roadStart"
Cohesion: 0.08
Nodes (5): DistrictKey, Townsfolk, CityLayoutTest, CitySizesTest, StreetWalksTest

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "net.minecraft.core.BlockPos"
Cohesion: 0.08
Nodes (5): CommandTableBlock, GateGeometry, CharacterMarkers, Placed, GateGeometryTest

### Community 13 - "BaseMap"
Cohesion: 0.06
Nodes (11): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, ClientMapCache, BaseMap, Nation (+3 more)

### Community 14 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 16 - "Point"
Cohesion: 0.06
Nodes (10): Point, BlockGrid, Ground, GroundPlan, GroundPlanTest, Land, Land, Land (+2 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.14
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 19 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.04
Nodes (12): Character page, Characters (holders, skills, prowess, traits), Court 3D scene (CourtView), Cultures (seven homelands), Epic Knights optional armour (Armoury), Faiths and cults, Martial titles (Sword Saint, Sword King, Sword God), Nation page (+4 more)

### Community 20 - "PveCommands.java"
Cohesion: 0.08
Nodes (30): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+22 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.10
Nodes (12): Cited Findings, Cited Findings, ChunkMapAccess, ChunkMapCameraMixin, CityVegetationMixin, ClientChunkWindowMixin, DhShaderLoaderMixin, DhTerrainShaderMixin (+4 more)

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.16
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 30 - ".get"
Cohesion: 0.15
Nodes (4): PlayerStats, View, Retinues, RetinuesTest

### Community 31 - ".send"
Cohesion: 0.08
Nodes (11): BackBanner, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel, Type (+3 more)

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.16
Nodes (3): CityPlan, Piece, Building

### Community 36 - "ConquestServer"
Cohesion: 0.13
Nodes (4): CityBanners, ConquestServer, StructureLink, PoiPrecomputer

### Community 37 - "ConquestNetwork"
Cohesion: 0.08
Nodes (14): Bark, CameraAt, ChargeHits, CityClose, ConquestNetwork, OpenBattleScreen, Place, PoliticsView (+6 more)

### Community 38 - "PoiPrecomputer.java"
Cohesion: 0.05
Nodes (3): CityStructure, ConquestWorldgen, FarmStructure

### Community 39 - ".map"
Cohesion: 0.08
Nodes (7): Cited Findings, Army, Goal, MusterBars, PoliticsSync, War, Wars

### Community 41 - ".feudFight"
Cohesion: 0.13
Nodes (5): Clash, Kind, DUEL, SKIRMISH, FeudDuel

### Community 42 - "Lands"
Cohesion: 0.13
Nodes (4): Feud, Lord, Feuds, Front

### Community 43 - "Capture"
Cohesion: 0.13
Nodes (7): Capture, Film, Layer, Look, Mesh, RecruitFigures, RecruitFiguresTest

### Community 44 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.08
Nodes (4): MarkerRenderer, BattleBannerBlockEntity, MarkerBlockEntity, MarkerSpec

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "SeaLanes"
Cohesion: 0.14
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.06
Nodes (9): BattleSpot, BoardView, FigureStrike, GrandStrategyView, StatsView, TalkChoice, TalkView, TeleportTo (+1 more)

### Community 50 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.14
Nodes (6): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, DistantArmies

### Community 51 - "Rank"
Cohesion: 0.06
Nodes (21): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+13 more)

### Community 52 - "MapPreview.java"
Cohesion: 0.07
Nodes (14): TerritoryMap, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoliticalMap (+6 more)

### Community 53 - "Centre"
Cohesion: 0.17
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 55 - "Body"
Cohesion: 0.11
Nodes (18): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+10 more)

### Community 57 - "SeatCities"
Cohesion: 0.14
Nodes (4): City, Grid, SeatCities, CityIndex

### Community 58 - "Faction Map View"
Cohesion: 0.14
Nodes (3): Cited Findings, FactionMapScreen, MapView

### Community 59 - ".step"
Cohesion: 0.12
Nodes (6): FigureMotion, Ground, Match, Placed, State, FigureMotionTest

### Community 61 - "Quest"
Cohesion: 0.19
Nodes (5): Boards, Entry, View, Taken, Quest

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 66 - ".step"
Cohesion: 0.14
Nodes (7): Cited Findings, Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 67 - ".choose"
Cohesion: 0.13
Nodes (11): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+3 more)

### Community 71 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.11
Nodes (7): Land, Result, SiegeSurvey, Wall, CityGates, Done, SiegeSurveyTest

### Community 72 - "Ride"
Cohesion: 0.16
Nodes (4): DistantShips, Far, Riding, Ride

### Community 73 - "BattlePlan"
Cohesion: 0.03
Nodes (26): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+18 more)

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.07
Nodes (28): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+20 more)

### Community 77 - ".board"
Cohesion: 0.12
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 78 - "Faction"
Cohesion: 0.11
Nodes (4): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction

### Community 79 - "ClothRenderer"
Cohesion: 0.31
Nodes (4): ClothRenderer, Fabric, Kind, Live

### Community 82 - "Ownership & Territory"
Cohesion: 0.20
Nodes (5): Candidate, OwnershipResolver, Claim, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "CityLayout"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 85 - "Land"
Cohesion: 0.14
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 86 - "Calibration"
Cohesion: 0.29
Nodes (3): Calibration, Fit, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.07
Nodes (21): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+13 more)

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 92 - "Kind"
Cohesion: 0.09
Nodes (16): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY (+8 more)

### Community 94 - ".enlist"
Cohesion: 0.08
Nodes (3): Officers, GameCharacter, BattleMap

### Community 95 - ".generate"
Cohesion: 0.08
Nodes (6): ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 96 - "net.minecraft.core.Vec3i"
Cohesion: 0.08
Nodes (4): CityPlacement, CountryPlacement, DojoPlacement, FarmPlacement

### Community 100 - "Siege"
Cohesion: 0.10
Nodes (7): Face, GateAction, Region, IN, OUT, WALL, Siege

### Community 102 - "Cloth"
Cohesion: 0.19
Nodes (3): Cloth, Solid, ClothTest

### Community 103 - "FigureBatches"
Cohesion: 0.09
Nodes (9): Batch, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE, FigureBatches (+1 more)

### Community 105 - "Peerage"
Cohesion: 0.08
Nodes (10): CityManager, NobleTitle, COUNT, KNIGHT, NONE, Merit, Peer, Peerage (+2 more)

### Community 109 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 110 - "FigureGear"
Cohesion: 0.18
Nodes (7): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 111 - "City"
Cohesion: 0.10
Nodes (11): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+3 more)

### Community 113 - "WaterSwell"
Cohesion: 0.09
Nodes (8): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, WaterSwell, Swell, SeaFoamTest

### Community 115 - "ConquestSavedData"
Cohesion: 0.08
Nodes (4): Cited Findings, ConquestSavedData, EconomyTick, ConquestSavedDataTest

### Community 116 - ".of"
Cohesion: 0.20
Nodes (4): FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 117 - "Cited Findings"
Cohesion: 0.16
Nodes (12): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+4 more)

### Community 118 - ".build"
Cohesion: 0.11
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.15
Nodes (3): GrandStrategyScreen, Spot, GrandStrategyDateTest

### Community 121 - "CityTerrain"
Cohesion: 0.05
Nodes (19): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+11 more)

### Community 122 - "WorldSettings"
Cohesion: 0.09
Nodes (11): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, SeedHash (+3 more)

### Community 124 - ".view"
Cohesion: 0.20
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 125 - "MarkerSpec"
Cohesion: 0.14
Nodes (5): BoardAction, GateSet, GateView, MarkerSet, MarkerView

### Community 126 - "BattleSounds"
Cohesion: 0.25
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 128 - "BattleShips"
Cohesion: 0.12
Nodes (7): Hull, Ramming, BattleShips, Helm, Ram, SableLink, RammingTest

### Community 129 - "GrappleHook"
Cohesion: 0.14
Nodes (3): GrappleHookRenderer, Registration, GrappleHook

### Community 130 - "MapView"
Cohesion: 0.23
Nodes (3): Hit, LabelLayer, LabelShape

### Community 132 - "Roads"
Cohesion: 0.12
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 134 - "SeaCalm"
Cohesion: 0.22
Nodes (4): Sea, Depths, SeaCalm, SwellTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (24): 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings, Cited Findings (+16 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings (+28 more)

### Community 142 - "Homeland"
Cohesion: 0.10
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 143 - ".renderByItem"
Cohesion: 0.16
Nodes (3): BannerArt, Carried, Layer

### Community 146 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.18
Nodes (3): UnitLayer, RecruiterRenderer, TownspersonRenderer

### Community 148 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.11
Nodes (6): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer, TitleLayer

### Community 149 - "RoadNetwork"
Cohesion: 0.14
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 152 - "Override"
Cohesion: 0.14
Nodes (3): Chunks, SettingsScreen, Politics.Front

### Community 156 - ".plan"
Cohesion: 0.13
Nodes (7): Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 157 - "ShipHulls"
Cohesion: 0.24
Nodes (7): 10. Overall ranking, and areas checked that look fine, Gaps, Inferences, Takeaway, BattleKind, Part, ShipHulls

### Community 163 - "TechTreeScreen"
Cohesion: 0.06
Nodes (21): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Machine, ARCBALLISTA, BATTERING_RAM (+13 more)

### Community 164 - "CityLayout"
Cohesion: 0.13
Nodes (3): CityLayout, District, Site

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.06
Nodes (26): WarHorn, BattleStandardItem, Armoury, Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY (+18 more)

### Community 174 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.20
Nodes (6): Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Takeaway, Api, DistantGeneration

### Community 178 - "net.minecraft.core.Vec3i"
Cohesion: 0.11
Nodes (6): Gold church, Quest board and quests, Recruitment posts and recruiter, StylePiece, GoldChurch, Layout

### Community 186 - "SoldierFigure"
Cohesion: 0.19
Nodes (8): Boxes, Palette, Shape, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 204 - "RafasConquest.java"
Cohesion: 0.06
Nodes (6): ClientModEvents, ConquestContent, GrappleHooks, StreetWalks, ConquestEntities, RafasConquest

### Community 205 - "Plan"
Cohesion: 0.23
Nodes (6): Engine, LADDERS, RAMS, TOWERS, Plan, WallTower

### Community 206 - ".noble"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 217 - ".passOnOrders"
Cohesion: 0.05
Nodes (21): Cited Findings, Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE (+13 more)

### Community 218 - ".view"
Cohesion: 0.13
Nodes (9): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, MapPreview, GrandStrategy, View (+1 more)

### Community 224 - "RecruitFigures.java"
Cohesion: 0.03
Nodes (6): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), BackBannerLayer, CapeCurio, MouthLayer

### Community 225 - "DistantOcclusion"
Cohesion: 0.11
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 226 - ".readPlacements"
Cohesion: 0.24
Nodes (3): Offsets, Placement, PlacementOffsetsTest

### Community 228 - ".stat"
Cohesion: 0.09
Nodes (11): ClientPeerage, Names, Honour, Honours, Stat, DIPLOMACY, INTRIGUE, LEARNING (+3 more)

### Community 234 - "ConquestEvents"
Cohesion: 0.05
Nodes (3): Post, ClientEvents, ConquestEvents

### Community 236 - "Kind"
Cohesion: 0.16
Nodes (8): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Plan

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.07
Nodes (6): Cited Findings, DistantDepth, DistantArmyRenderer, Figure, Rects, ShipSprite

### Community 248 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 249 - "GateBlockEntity"
Cohesion: 0.08
Nodes (9): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+1 more)

### Community 252 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 255 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 257 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.22
Nodes (5): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), TickWeave experiment

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "RecruitsBattle"
Cohesion: 0.03
Nodes (17): Cited Findings, Camera, CameraView, Card, Enlisted, Figure, GeneralTask, RecruitsBattle (+9 more)

### Community 275 - "CourtView"
Cohesion: 0.06
Nodes (11): CharacterScreen, Retainer, Spot, Camera, CourtView, Figure, NationScreen, Banner (+3 more)

### Community 277 - "Realm"
Cohesion: 0.09
Nodes (6): Hook, Realm, Succession, Taking, RealmPlannerTest, World

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.20
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 288 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "WarCamp.java"
Cohesion: 0.05
Nodes (20): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Enlisted players, Envelopment, The general and guard (+12 more)

### Community 292 - "How Total War calculates battles between units"
Cohesion: 0.11
Nodes (17): 1. Combat is per soldier, not per unit, 1. Flank and rear shock, 2. Does the swing land?, 2. Nearby rout (fright), 3. Army-wide losses, 3. How much damage the hit does, 4. Charges, 4. Shattering (+9 more)

### Community 294 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 298 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 300 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 305 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 306 - "Striker"
Cohesion: 0.33
Nodes (5): Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY

### Community 308 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 309 - ".rightClick"
Cohesion: 0.21
Nodes (3): Entry, Menu, HornOrders

### Community 310 - "ConquestWorldScreen"
Cohesion: 0.20
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 311 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 315 - "rafasconquest.character.Rank"
Cohesion: 0.16
Nodes (3): Offer, Recruitment, Advancement

### Community 318 - "arraylist"
Cohesion: 0.06
Nodes (15): Base map generated from seed, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Demographics (population by culture and race), Faction Info screen, Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, World map layers (+7 more)

### Community 322 - "6. Sodium (and Iris) compatibility"
Cohesion: 0.40
Nodes (5): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway

### Community 323 - "UnitsState"
Cohesion: 0.17
Nodes (8): Morale, State, RALLIED, ROUTED, SHATTERED, STEADY, UnitInfo, UnitsState

### Community 324 - "HornCall"
Cohesion: 0.40
Nodes (5): HornCall, ATTACKING, MOVING, RALLYING, RETREATING

### Community 327 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 339 - "Cited Findings"
Cohesion: 0.13
Nodes (8): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, Arrow, ArrowVolleys, BattleSpotRing

### Community 349 - "WarCamp"
Cohesion: 0.10
Nodes (6): PlannedTent, Saved, WarCamp, CourtSet, Seat, Style

### Community 352 - "State"
Cohesion: 0.50
Nodes (4): State, CARRIED, RAISING, UP

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **414 isolated node(s):** `LADDERS`, `RAMS`, `TOWERS`, `IN`, `OUT` (+409 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1368 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **213 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `GrappleHook`, `net.minecraft.world.level.ChunkPos`, `.army`, `UnitOrder`, `Point`, `PveCommands.java`, `org.jetbrains.annotations.Nullable`, `WarCouncil`, `PveCommands Registration`, `PoiPrecomputer.java`, `net.minecraft.world.item.ItemStack`, `rafasconquest.faction.Faction`, `.feudFight`, `Striker`, `Phase`, `UnitMode`, `HornCall`, `RafasConquest.java`, `net.minecraft.server.level.ServerLevel`, `Work`, `Calibration`, `.passOnOrders`, `WarCamp`, `.enlist`, `.generate`, `Peerage`, `ConquestEvents`, `.siege`?**
  _High betweenness centrality (0.068) - this node is a cross-community bridge._
- **What connects `LADDERS`, `RAMS`, `TOWERS` to the rest of the system?**
  _414 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.04021164021164021 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `MapContext`, `Politics`, `.onLevelLoad`, `ClientPolitics`, `CourtView`, `net.minecraft.client.gui.GuiGraphics`, `Realm`, `.plan`, `WarCouncil`, `School`, `ConquestServer`, `PoiPrecomputer.java`, `net.minecraft.world.item.ItemStack`, `.map`, `.postChunk`, `MapPreview.java`, `Centre`, `SeatCities`, `arraylist`, `TitleMapTest`, `RafasConquest.java`, `Terrain`, `OrganizationScreen`, `.generate`, `.place`, `City`, `Cited Findings`, `GrandStrategyScreen`?**
  _High betweenness centrality (0.042) - this node is a cross-community bridge._
- **Should `net.minecraft.world.level.block.state.BlockState` be split into smaller, more focused modules?**
  _Cohesion score 0.0499047619047619 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._