# Graph Report - RafasConquest  (2026-11-11)

## Corpus Check
- 464 files · ~403,029 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 6568 nodes · 20794 edges · 343 communities (136 shown, 207 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2760 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `551242df`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FieldBlock
- .world
- net.minecraft.world.level.levelgen.structure.BoundingBox
- .army
- CityTerrain
- BattleScreen
- .onLogout
- SableLink
- SiegeWorks
- .render
- Arm
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- City
- BaseMap
- ConquestNetwork
- Peerage
- .deployment
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.screens.Screen
- RafasConquest Commands (guide v0.0.2)
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- HorizontalStretch
- CityManagerScreen
- .plan
- org.jetbrains.annotations.Nullable
- .shift
- WarCouncil
- TechTreeScreen
- ConquestWorldgen
- Type
- CapitalLayout
- PveCommands Registration
- School
- CityPlan
- Land
- Recruiter
- .step
- net.minecraft.core.BlockPos
- rafasconquest.faction.Faction
- net.minecraft.server.level.ServerLevel
- Swell
- War
- Faction
- BattlePlan
- Personalities
- SeaLanes
- org.spongepowered.asm.mixin.Mixin
- Realm
- View
- RecruitsBattle.java
- Centre
- .postChunk
- Row
- .view
- City
- Faction Map View
- .wallSegments
- CityVariant
- net.minecraft.world.level.block.state.BlockState
- TitleMapTest
- .saveFormation
- StyleLibrary
- Lands
- net.minecraft.world.item.ItemStack
- RafasConquest.java
- CityTerrainPiece
- com.mojang.blaze3d.vertex.PoseStack
- DistantOcclusion
- Countryside
- MapLands
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- PlaceMusicClient
- GateBlockEntity
- Quests
- .sail
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
- How do world generation and structure placement work, and what do they cost?
- Retinues
- Barracks
- .generate
- ConquestSavedData
- RecruitsArrowCleanerMixin.java
- FactionInfoScreen
- CityStyles
- .layout
- Realm
- MouthLayer.java
- rafasconquest.battle.FormationSnapshot
- MarkerScreen
- BattleSounds
- .player
- .of
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- WaterSwell
- Phase
- .fire
- RoadLayer
- Organizations
- GrandStrategyScreen
- Terrain
- ClientPolitics
- Kind
- UnitOrder
- .tick
- CityLayout
- .place
- net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
- BattleShips
- MapView
- net.minecraft.core.Vec3i
- RafasConquest (PvE addon for Villager Recruits)
- Plan
- BoardScreen
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- ConquestWorldScreen
- CourtSet
- Engine
- Homeland
- Ranks
- .walks
- CLAUDE.md
- Feud
- .startAt
- .plan
- Override
- .order
- .onLogout
- Override
- net.minecraft.network.FriendlyByteBuf
- Small Cluster 170
- ShipKind
- NobleAnnex
- Duty
- net.neoforged.neoforge.network.handling.IPayloadContext
- NobleDistrictTest
- SimpleChannel
- .pack
- .sender
- .mapWater
- Point
- org.joml.Matrix4f
- DistantArmyRenderer.java
- net.minecraft.world.level.biome.Biome
- Cited Findings
- net.neoforged.bus.api.SubscribeEvent
- ConquestServer
- DistantArmyRenderer
- net.minecraft.resources.ResourceLocation
- ConquestGameRules
- DayClockTest
- .simulate
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- Call
- Ramming
- ClientEvents
- Profiling run sheet (baseline)
- .measure
- .view
- ConquestContent.java
- OrganizationScreen
- CourtView
- .boundsAt
- CameraView.java
- Characters (holders, skills, prowess, traits)
- Kind
- WarCamp
- ClientScreens
- PoliticsData
- GoldChurch.java
- Build
- .handleUnitsRequest
- RecruitsBattle
- BattleKeepInventory
- GatePlanksBlock.java
- .onLevelLoad
- .second
- WallTemplates
- .build
- rafasconquest.battle.FieldBlock
- .roadStart
- Kind
- ClientModEvents.java
- ClientBarks
- HornCall
- PoliticsView
- CityManager

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 282 edges
2. `FieldBlock` - 164 edges
3. `FieldBattle` - 160 edges
4. `ConquestNetwork` - 119 edges
5. `FieldArmy` - 111 edges
6. `Lands` - 89 edges
7. `Point` - 85 edges
8. `Politics` - 79 edges
9. `BattleScreen` - 78 edges
10. `BaseMap` - 77 edges

## Surprising Connections (you probably didn't know these)
- `Cited Findings` --references--> `Sprite`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/battle/FormationSnapshot.java
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

## Communities (343 total, 207 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.05
Nodes (9): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock, Rates (+1 more)

### Community 2 - "net.minecraft.world.level.levelgen.structure.BoundingBox"
Cohesion: 0.07
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - ".army"
Cohesion: 0.07
Nodes (5): DoctrineTest, FieldBattleTest, GroundPlanTest, Land, GrandStrategyDateTest

### Community 4 - "CityTerrain"
Cohesion: 0.08
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.09
Nodes (3): BattleScreen, Entry, Menu

### Community 6 - ".onLogout"
Cohesion: 0.07
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "SableLink"
Cohesion: 0.24
Nodes (3): Frame, SableLink, SiegeFrames

### Community 8 - "SiegeWorks"
Cohesion: 0.12
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 9 - ".render"
Cohesion: 0.08
Nodes (8): CharacterScreen, Retainer, Spot, ClientPeerage, Banner, Portraits, Honour, Honours

### Community 10 - "Arm"
Cohesion: 0.09
Nodes (13): Arm, ARCHERS, CAVALRY, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, SKIRMISHERS, ZWEIHANDLERS (+5 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "City"
Cohesion: 0.09
Nodes (15): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+7 more)

### Community 13 - "BaseMap"
Cohesion: 0.06
Nodes (13): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, ClientMapCache, MapPreview, BaseMap (+5 more)

### Community 14 - "ConquestNetwork"
Cohesion: 0.08
Nodes (15): Bark, BaseMapPart, CameraAt, ChargeHits, CityClose, ConquestNetwork, GateSet, GateView (+7 more)

### Community 15 - "Peerage"
Cohesion: 0.08
Nodes (11): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+3 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.22
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 20 - "RafasConquest Commands (guide v0.0.2)"
Cohesion: 0.08
Nodes (31): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+23 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.07
Nodes (12): FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoliticalMap, FactionInfo (+4 more)

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.18
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 29 - "TechTreeScreen"
Cohesion: 0.18
Nodes (4): TechTreeScreen, Tech, TechTree, TechTreeTest

### Community 30 - "ConquestWorldgen"
Cohesion: 0.05
Nodes (8): CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 31 - "Type"
Cohesion: 0.33
Nodes (4): Type, CLEAR, DELIVER, SCOUT

### Community 32 - "CapitalLayout"
Cohesion: 0.09
Nodes (10): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, SeedHash (+2 more)

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.15
Nodes (3): CityPlan, Piece, Building

### Community 36 - "Land"
Cohesion: 0.17
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 37 - "Recruiter"
Cohesion: 0.08
Nodes (5): Walk, Barks, Ear, Recruiter, Townsperson

### Community 38 - ".step"
Cohesion: 0.18
Nodes (5): FigureMotion, Match, Placed, State, FigureMotionTest

### Community 39 - "net.minecraft.core.BlockPos"
Cohesion: 0.09
Nodes (7): Land, Result, SiegeSurvey, Wall, GateGeometry, SiegeSurveyTest, GateGeometryTest

### Community 43 - "War"
Cohesion: 0.08
Nodes (7): Cited Findings, Army, Goal, MusterBars, PoliticsSync, War, Wars

### Community 44 - "Faction"
Cohesion: 0.12
Nodes (4): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.20
Nodes (4): CityVegetationMixin, ClientChunkWindowMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 50 - "Realm"
Cohesion: 0.20
Nodes (4): Kind, FRIEND, RIVAL, Realm

### Community 51 - "View"
Cohesion: 0.15
Nodes (3): BoardView, GrandStrategyView, TalkView

### Community 53 - "Centre"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 54 - ".postChunk"
Cohesion: 0.15
Nodes (3): Cited Findings, BarracksPeople, CityGuards

### Community 56 - ".view"
Cohesion: 0.08
Nodes (14): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+6 more)

### Community 57 - "City"
Cohesion: 0.13
Nodes (4): Grid, SeatCities, SeatCitiesTest, World

### Community 59 - ".wallSegments"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 60 - "CityVariant"
Cohesion: 0.07
Nodes (10): CityGridBuilder, Ground, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA (+2 more)

### Community 61 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.05
Nodes (9): Sea, Saved, CommandTableBlock, GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, WorldMapItem (+1 more)

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 65 - "StyleLibrary"
Cohesion: 0.10
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - "Lands"
Cohesion: 0.07
Nodes (17): Cited Findings, Gaps, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Inferences, Takeaway, Takeaway (+9 more)

### Community 67 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.06
Nodes (26): WarHorn, BackBanners, Armoury, Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY (+18 more)

### Community 69 - "RafasConquest.java"
Cohesion: 0.05
Nodes (4): Battle map developer web page, Morale, rout and rally, StreetWalks, RafasConquest

### Community 72 - "DistantOcclusion"
Cohesion: 0.13
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 73 - "Countryside"
Cohesion: 0.08
Nodes (13): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+5 more)

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.08
Nodes (24): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+16 more)

### Community 76 - "PlaceMusicClient"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 77 - "GateBlockEntity"
Cohesion: 0.09
Nodes (8): Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING, Hinge

### Community 78 - "Quests"
Cohesion: 0.11
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 79 - ".sail"
Cohesion: 0.13
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.15
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 84 - ".view"
Cohesion: 0.19
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 86 - "Calibration"
Cohesion: 0.18
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.07
Nodes (21): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+13 more)

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.20
Nodes (6): Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Takeaway, Api, DistantGeneration

### Community 95 - ".generate"
Cohesion: 0.09
Nodes (11): ProvinceNames, BaseMapGenerator, Heights, Homeland, DESERT, JUNGLE, PLAINS, SAVANNA (+3 more)

### Community 96 - "ConquestSavedData"
Cohesion: 0.06
Nodes (7): Cited Findings, ConquestConfig, ConquestSavedData, Poi, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 98 - "RecruitsArrowCleanerMixin.java"
Cohesion: 0.16
Nodes (8): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway, Cited Findings, RecruitsArrowCleanerMixin, TrackedEntityCameraMixin

### Community 103 - ".layout"
Cohesion: 0.20
Nodes (4): Cited Findings, Positions, Sprite, FormationSnapshotTest

### Community 105 - "Realm"
Cohesion: 0.17
Nodes (3): Hook, Succession, Taking

### Community 106 - "MouthLayer.java"
Cohesion: 0.11
Nodes (4): BackBannerLayer, GateRenderer, MarkerRenderer, MouthLayer

### Community 107 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.27
Nodes (6): Boxes, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 109 - "BattleSounds"
Cohesion: 0.30
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 110 - ".player"
Cohesion: 0.14
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 111 - ".of"
Cohesion: 0.16
Nodes (5): FactionBanner, Layer, FactionBannerNbt, CityBanners, FactionBannerTest

### Community 112 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.17
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips, Ride

### Community 113 - "WaterSwell"
Cohesion: 0.18
Nodes (6): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, WaterSwell

### Community 114 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 116 - ".fire"
Cohesion: 0.07
Nodes (4): CharacterMarkers, Placed, MarkerBlockEntity, MarkerSpec

### Community 117 - "RoadLayer"
Cohesion: 0.15
Nodes (12): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+4 more)

### Community 118 - "Organizations"
Cohesion: 0.27
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 123 - "Kind"
Cohesion: 0.15
Nodes (10): Kind, ARCHERS, BEARER, CAVALRY, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY (+2 more)

### Community 124 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 126 - "CityLayout"
Cohesion: 0.08
Nodes (6): CityLayout, District, Site, SlopeTile, WorldSettings, CityLayoutTest

### Community 128 - ".place"
Cohesion: 0.08
Nodes (7): Cited Findings, CityIndex, RoadFeature, Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 129 - "net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate"
Cohesion: 0.12
Nodes (9): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Gaps, Takeaway, DistantShips, Far, SableSeen, BattleKind, Part (+1 more)

### Community 130 - "BattleShips"
Cohesion: 0.27
Nodes (3): BattleShips, Helm, Ram

### Community 132 - "MapView"
Cohesion: 0.07
Nodes (15): Cited Findings, BaseMapLayer, Hit, LabelLayer, MapContext, LegendEntry, MapLayer, Symbol (+7 more)

### Community 134 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.17
Nodes (8): Arrows in flight (figure volleys), Battle sounds, Recruits PvE Addon Design Doc (PDF), Seeing the whole field (distant flat figures), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 136 - "Plan"
Cohesion: 0.16
Nodes (8): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Plan

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.07
Nodes (28): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings (+20 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings (+28 more)

### Community 141 - "ConquestWorldScreen"
Cohesion: 0.20
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 142 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 143 - "Engine"
Cohesion: 0.24
Nodes (4): Engine, LADDERS, RAMS, TOWERS

### Community 146 - "Homeland"
Cohesion: 0.17
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 148 - "Ranks"
Cohesion: 0.10
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 149 - ".walks"
Cohesion: 0.10
Nodes (4): DistrictKey, Townsfolk, CitySizesTest, StreetWalksTest

### Community 152 - "Feud"
Cohesion: 0.08
Nodes (8): Clash, Feud, Kind, DUEL, SKIRMISH, Lord, FeudDuel, Feuds

### Community 156 - ".plan"
Cohesion: 0.14
Nodes (7): Centre, Demographics, Share, Faith, Bond, RealmPlanner, Seat

### Community 157 - "Override"
Cohesion: 0.24
Nodes (3): Land, Land, Land

### Community 160 - ".order"
Cohesion: 0.25
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

### Community 167 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.07
Nodes (7): BattleSnapshot, BattleSpot, FactionInfoReply, FigureStrike, StatsView, Volleys, WorldStretch

### Community 174 - "ShipKind"
Cohesion: 0.33
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 181 - "Duty"
Cohesion: 0.13
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 204 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.13
Nodes (6): BackBanner, Builder, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 206 - "SimpleChannel"
Cohesion: 0.16
Nodes (4): RequestFactionInfo, StatsRequest, Msg, SimpleChannel

### Community 217 - ".pack"
Cohesion: 0.29
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 218 - ".sender"
Cohesion: 0.09
Nodes (4): HornOrders, SetSetting, TalkChoice, TeleportTo

### Community 221 - "Point"
Cohesion: 0.07
Nodes (16): Point, BlockGrid, Gate, GateAction, GateState, Kind, CLOSE, OPEN (+8 more)

### Community 223 - "org.joml.Matrix4f"
Cohesion: 0.15
Nodes (4): Inferences, Arrow, ArrowVolleys, Figure

### Community 228 - "net.minecraft.world.level.biome.Biome"
Cohesion: 0.14
Nodes (3): BiomePalette, Entry, SettlementBiomes

### Community 232 - "Cited Findings"
Cohesion: 0.09
Nodes (8): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, FormationSnapshot, Ship, DistantArmies

### Community 236 - "ConquestServer"
Cohesion: 0.11
Nodes (3): ConquestServer, StructureLink, PoiPrecomputer

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.15
Nodes (3): Cited Findings, DistantArmyRenderer, Hull

### Community 238 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.23
Nodes (3): UnitLayer, RecruiterRenderer, TownspersonRenderer

### Community 249 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 252 - ".simulate"
Cohesion: 0.09
Nodes (14): GameCharacter, Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN (+6 more)

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - ".view"
Cohesion: 0.24
Nodes (7): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, GrandStrategy, View

### Community 275 - "CourtView"
Cohesion: 0.09
Nodes (4): Camera, CourtView, Figure, NationScreen

### Community 277 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 283 - "CameraView.java"
Cohesion: 0.11
Nodes (5): Cited Findings, CameraView, Chunks, ChunkMapAccess, ChunkMapCameraMixin

### Community 288 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.04
Nodes (53): Ambushes from woods, Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Character page, Characters (holders, skills, prowess, traits) (+45 more)

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 302 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 303 - ".handleUnitsRequest"
Cohesion: 0.25
Nodes (3): UnitInfo, UnitsRequest, UnitsState

### Community 306 - "RecruitsBattle"
Cohesion: 0.04
Nodes (21): Cited Findings, Camera, Card, Enlisted, Figure, Officers, RecruitsBattle, Role (+13 more)

### Community 315 - ".second"
Cohesion: 0.06
Nodes (9): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT, GeneralTask (+1 more)

### Community 322 - ".build"
Cohesion: 0.11
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 323 - "rafasconquest.battle.FieldBlock"
Cohesion: 0.08
Nodes (7): Landing, Live, Shape, LINE, SQUARE, V, WEDGE

### Community 327 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 340 - "HornCall"
Cohesion: 0.40
Nodes (5): HornCall, ATTACKING, MOVING, RALLYING, RETREATING

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **367 isolated node(s):** `ARCHERS`, `CAVALRY`, `HEAVY_CAVALRY`, `HEAVY_INFANTRY`, `LEVY` (+362 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1253 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **207 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate`, `net.minecraft.world.level.levelgen.structure.BoundingBox`, `.army`, `SiegeWorks`, `RafasConquest Commands (guide v0.0.2)`, `Feud`, `.startAt`, `CameraView.java`, `WarCouncil`, `ConquestWorldgen`, `PveCommands Registration`, `WarCamp`, `net.minecraft.core.BlockPos`, `net.minecraft.server.level.ServerLevel`, `RecruitsBattle.java`, `.second`, `.saveFormation`, `net.minecraft.world.item.ItemStack`, `rafasconquest.battle.FieldBlock`, `RafasConquest.java`, `HornCall`, `PoliticsTest`, `Calibration`, `net.minecraft.world.level.biome.Biome`, `.onServerTick`, `UnitOrder`, `Phase`, `ConquestEvents.java`, `.simulate`?**
  _High betweenness centrality (0.091) - this node is a cross-community bridge._
- **What connects `ARCHERS`, `CAVALRY`, `HEAVY_CAVALRY` to the rest of the system?**
  _367 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.05069425030286087 - nodes in this community are weakly interconnected._
- **Why does `ConquestNetwork` connect `ConquestNetwork` to `arraylist`, `ClientScreens`, `BattleScreen`, `net.minecraft.network.FriendlyByteBuf`, `BoardScreen`, `net.neoforged.neoforge.network.handling.IPayloadContext`, `SimpleChannel`, `.handleUnitsRequest`, `RecruitsBattle`, `net.minecraft.client.gui.screens.Screen`, `View`, `RecruitsBattle.java`, `CityManagerScreen`, `.sender`, `PoliticsView`, `net.minecraft.world.level.block.state.BlockState`, `map`?**
  _High betweenness centrality (0.031) - this node is a cross-community bridge._
- **Should `.world` be split into smaller, more focused modules?**
  _Cohesion score 0.14761904761904762 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._