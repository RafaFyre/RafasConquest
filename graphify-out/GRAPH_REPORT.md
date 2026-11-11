# Graph Report - RafasConquest  (2026-11-11)

## Corpus Check
- 464 files · ~401,953 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 6547 nodes · 20744 edges · 348 communities (153 shown, 195 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2756 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `b855d72d`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FieldBlock
- PoiPrecomputer.java
- net.minecraft.world.level.levelgen.structure.BoundingBox
- .army
- CityTerrain
- BattleScreen
- net.minecraft.network.FriendlyByteBuf
- SiegeWorks
- .render
- Arm
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- Roads
- BaseMap
- ConquestNetwork
- Peerage
- org.junit.jupiter.api.Test
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.screens.Screen
- RafasConquest Commands (guide v0.0.2)
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- list
- HorizontalStretch
- CityManagerScreen
- .plan
- .say
- .shift
- WarCouncil
- TechTreeScreen
- ConquestEvents.java
- Feud
- CapitalLayout
- PveCommands Registration
- School
- CityPlan
- Land
- Recruiter
- .step
- net.minecraft.core.Direction
- rafasconquest.faction.Faction
- .place
- War
- Faction
- CameraView
- Personalities
- SeaLanes
- org.spongepowered.asm.mixin.Mixin
- .drawEmpire
- .roadStart
- com.talhanation.recruits.entities.AbstractRecruitEntity
- Centre
- .postChunk
- Row
- Retinues
- City
- Faction Map View
- .wallSegments
- CityVariant
- net.minecraft.world.level.block.state.BlockState
- TitleMapTest
- .layout
- StyleLibrary
- .render
- net.minecraft.world.item.ItemStack
- RafasConquest.java
- .render
- RecruitsBattle
- DistantOcclusion
- .findGenerationPoint
- MapLands
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- PlaceMusicClient
- net.minecraft.server.level.ServerLevel
- Quests
- .startAt
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
- .build
- CourtView
- Barracks
- .generate
- ConquestSavedData
- WaterSwell
- FactionInfoScreen
- arraylist
- rafasconquest.battle.FieldBlock
- CityStyles
- Chunks
- Realm
- MouthLayer.java
- rafasconquest.battle.FormationSnapshot
- MarkerScreen
- Lands
- .player
- .of
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- Swell
- net.minecraft.world.level.block.Rotation
- net.minecraft.nbt.CompoundTag
- RoadLayer
- .view
- GrandStrategyScreen
- Terrain
- ClientPolitics
- Strategy
- UnitOrder
- Settlement
- CityLayout
- RoadNetwork
- net.minecraft.world.item.DyeColor
- BattleShips
- MapContext
- StylePiece
- BlockPos
- Plan
- .requestUnits
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- OrganizationScreen
- .lineUp
- Characters (holders, skills, prowess, traits)
- Race
- Ranks
- Townsperson
- CLAUDE.md
- net.minecraft.core.BlockPos
- Point
- .plan
- PoliticalStyle
- War
- .at
- BattleSnapshot
- Small Cluster 170
- 4. Depth buffers and post effects (DH depth, occlusion, GPU sync)
- .hash
- .boundsAt
- ConquestWorldScreen
- NobleDistrictTest
- net.neoforged.neoforge.network.handling.IPayloadContext
- .pack
- Outcome
- BoardScreen
- Siege
- Cited Findings
- DistantArmyRenderer.java
- DistantHorizonsLink
- .touches
- .nearest
- Cited Findings
- net.neoforged.bus.api.SubscribeEvent
- FormationSnapshot
- ShipKind
- Phase
- SableLink
- ConquestGameRules
- .report
- Trait
- UnitMode
- Call
- Ramming
- Task
- Profiling run sheet (baseline)
- .measure
- Distribution
- DistantHorizonsArmies
- Organizations
- CourtSet
- .world
- Kind
- .hinge
- 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- Kind
- WarCamp
- ClientScreens
- SwellTest
- 3. GUI and map screens (strategy map, faction map, battle overlays)
- 7. Client tick, stutter and sound
- RafasConquest (PvE addon for Villager Recruits)
- CountryPiece
- Type
- .view
- net.minecraft.world.phys.Vec3
- Shape
- GatePlanksBlock.java
- Role
- .general
- 8. Lessons from performance mods that a content mod can apply
- RecruitsBattle
- 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound
- BlockGrid
- HarbourCut
- .block
- Realm
- Kind
- net.neoforged.fml.common.EventBusSubscriber
- Ranks (Soldier to Grand Marshal)
- PoliticsData
- ConquestConfig
- How do the simulation and daily-decision systems schedule work? Do they spike?

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 279 edges
2. `FieldBattle` - 170 edges
3. `FieldBlock` - 164 edges
4. `ConquestNetwork` - 120 edges
5. `FieldArmy` - 111 edges
6. `Lands` - 89 edges
7. `Point` - 85 edges
8. `Politics` - 79 edges
9. `BaseMap` - 77 edges
10. `BattleScreen` - 75 edges

## Surprising Connections (you probably didn't know these)
- `Inferences` --references--> `PoliticalMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/map/PoliticalMap.java
- `Inferences` --references--> `Politics`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/politics/Politics.java
- `Conclusion` --references--> `RoadLayer`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Cited Findings` --references--> `RoadLayer`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Takeaway` --references--> `RoadLayer`  [INFERRED]
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

## Communities (348 total, 195 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.05
Nodes (7): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 1 - "PoiPrecomputer.java"
Cohesion: 0.06
Nodes (16): City wall, banners and wall guards, Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE (+8 more)

### Community 2 - "net.minecraft.world.level.levelgen.structure.BoundingBox"
Cohesion: 0.06
Nodes (9): BarracksPiece, CityRoadPiece, CityTerrainPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 4 - "CityTerrain"
Cohesion: 0.07
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.08
Nodes (3): BattleScreen, Entry, Menu

### Community 7 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.06
Nodes (9): BattleSpot, BoardView, FactionInfoReply, PeerageView, PoliticsView, StatsView, TalkView, Volleys (+1 more)

### Community 8 - "SiegeWorks"
Cohesion: 0.12
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 9 - ".render"
Cohesion: 0.15
Nodes (4): CharacterScreen, Spot, Banner, Portraits

### Community 10 - "Arm"
Cohesion: 0.05
Nodes (27): Arm, ARCHERS, CAVALRY, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, SKIRMISHERS, ZWEIHANDLERS (+19 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "Roads"
Cohesion: 0.12
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 13 - "BaseMap"
Cohesion: 0.09
Nodes (10): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, MapPreview, BaseMap, Nation (+2 more)

### Community 14 - "ConquestNetwork"
Cohesion: 0.05
Nodes (20): Bark, CameraAt, ChargeHits, CityClose, CityView, ConquestNetwork, FigureStrike, GrandStrategyView (+12 more)

### Community 15 - "Peerage"
Cohesion: 0.08
Nodes (10): Boards, Entry, View, Merit, Peer, Peerage, Taken, PeerageSync (+2 more)

### Community 16 - "org.junit.jupiter.api.Test"
Cohesion: 0.05
Nodes (12): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, WarPlan (+4 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.24
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 20 - "RafasConquest Commands (guide v0.0.2)"
Cohesion: 0.07
Nodes (36): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+28 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 22 - "list"
Cohesion: 0.07
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 25 - ".plan"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - ".say"
Cohesion: 0.13
Nodes (8): Dialogue, Barks, Ear, Ladder, MILITARY, NOBLE, NONE, DialogueTest

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.15
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 29 - "TechTreeScreen"
Cohesion: 0.18
Nodes (4): TechTreeScreen, Tech, TechTree, TechTreeTest

### Community 30 - "ConquestEvents.java"
Cohesion: 0.03
Nodes (16): Ambushes from woods, Cities (grid, plots, vanilla village buildings), Deploying to the land (GroundPlan), TickWeave experiment, ConquestContent, CityLayout, StreetWalks, ConquestEntities (+8 more)

### Community 31 - "Feud"
Cohesion: 0.14
Nodes (6): Clash, Feud, Kind, DUEL, SKIRMISH, FeudDuel

### Community 32 - "CapitalLayout"
Cohesion: 0.12
Nodes (8): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, WorldSettings, CapitalRelocationTest, LayoutTest

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 36 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 38 - ".step"
Cohesion: 0.18
Nodes (5): FigureMotion, Match, Placed, State, FigureMotionTest

### Community 39 - "net.minecraft.core.Direction"
Cohesion: 0.11
Nodes (7): Land, Result, SiegeSurvey, Wall, CityGates, Done, SiegeSurveyTest

### Community 42 - ".place"
Cohesion: 0.16
Nodes (5): Cited Findings, Gaps, How do world generation and structure placement work, and what do they cost?, Takeaway, RoadFeature

### Community 43 - "War"
Cohesion: 0.10
Nodes (4): Goal, MusterBars, PoliticsSync, Wars

### Community 44 - "Faction"
Cohesion: 0.07
Nodes (5): Cited Findings, Faction, Poi, ConquestSavedDataTest, FactionInfoTest

### Community 45 - "CameraView"
Cohesion: 0.09
Nodes (14): Battle code costs 0.2 ms; the multi-second freezes come from blocking, 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway, Cited Findings, Gaps (+6 more)

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "SeaLanes"
Cohesion: 0.15
Nodes (11): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+3 more)

### Community 48 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.13
Nodes (7): Cited Findings, ChunkMapAccess, ChunkMapCameraMixin, CityVegetationMixin, ClientChunkWindowMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 50 - ".drawEmpire"
Cohesion: 0.31
Nodes (3): Hit, LabelLayer, LabelShape

### Community 52 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.18
Nodes (6): Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER

### Community 53 - "Centre"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 54 - ".postChunk"
Cohesion: 0.13
Nodes (4): Cited Findings, RecruitsBridge, BarracksPeople, CityGuards

### Community 56 - "Retinues"
Cohesion: 0.20
Nodes (3): PlayerStats, View, Retinues

### Community 57 - "City"
Cohesion: 0.11
Nodes (6): City, Grid, SeatCities, CityIndex, SeatCitiesTest, World

### Community 58 - "Faction Map View"
Cohesion: 0.15
Nodes (4): Cited Findings, Cited Findings, FactionMapScreen, MapView

### Community 59 - ".wallSegments"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 60 - "CityVariant"
Cohesion: 0.09
Nodes (9): CityGridBuilder, Ground, Platform, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY (+1 more)

### Community 61 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (7): CommandTableBlock, GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, Builder, MarkerBlock

### Community 63 - "TitleMapTest"
Cohesion: 0.12
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - ".layout"
Cohesion: 0.20
Nodes (4): Cited Findings, Positions, Sprite, FormationSnapshotTest

### Community 65 - "StyleLibrary"
Cohesion: 0.10
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - ".render"
Cohesion: 0.09
Nodes (11): ClientPeerage, Names, Honour, Honours, Stat, DIPLOMACY, INTRIGUE, LEARNING (+3 more)

### Community 67 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.07
Nodes (24): WarHorn, Armoury, Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL (+16 more)

### Community 69 - "RafasConquest.java"
Cohesion: 0.05
Nodes (3): Battle map developer web page, Morale, rout and rally, RafasConquest

### Community 72 - "DistantOcclusion"
Cohesion: 0.11
Nodes (7): Cited Findings, DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.08
Nodes (24): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+16 more)

### Community 76 - "PlaceMusicClient"
Cohesion: 0.15
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 77 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.10
Nodes (9): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+1 more)

### Community 78 - "Quests"
Cohesion: 0.22
Nodes (4): DataFiles, Kind, Quests, Target

### Community 79 - ".startAt"
Cohesion: 0.08
Nodes (10): Inferences, BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Api, DistantGeneration (+2 more)

### Community 81 - "SeaCalm"
Cohesion: 0.24
Nodes (3): Sea, Depths, SeaCalm

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.17
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 84 - ".view"
Cohesion: 0.29
Nodes (4): Option, Talk, View, WarCouncilPagesTest

### Community 86 - "Calibration"
Cohesion: 0.11
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.18
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - ".build"
Cohesion: 0.22
Nodes (3): Frame, Harbour, CitySizesTest

### Community 92 - "CourtView"
Cohesion: 0.07
Nodes (7): Retainer, Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 95 - ".generate"
Cohesion: 0.07
Nodes (13): ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry, Homeland, DESERT, JUNGLE (+5 more)

### Community 96 - "ConquestSavedData"
Cohesion: 0.08
Nodes (6): ConquestSavedData, CityBanners, ConquestServer, StructureLink, EconomyTick, PoiPrecomputer

### Community 100 - "arraylist"
Cohesion: 0.06
Nodes (15): Base map generated from seed, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Demographics (population by culture and race), Faction Info screen, Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, World map layers (+7 more)

### Community 102 - "CityStyles"
Cohesion: 0.22
Nodes (3): Building, CityStyles, Houses

### Community 105 - "Realm"
Cohesion: 0.10
Nodes (5): Hook, Succession, Taking, RealmPlannerTest, World

### Community 106 - "MouthLayer.java"
Cohesion: 0.10
Nodes (4): BackBannerLayer, GateRenderer, MarkerRenderer, MouthLayer

### Community 107 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.27
Nodes (6): Boxes, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 109 - "Lands"
Cohesion: 0.08
Nodes (7): Cited Findings, Lord, Feuds, Lands, Claim, Front, Politics

### Community 110 - ".player"
Cohesion: 0.18
Nodes (5): NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 111 - ".of"
Cohesion: 0.17
Nodes (4): FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 112 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.17
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips, Ride

### Community 113 - "Swell"
Cohesion: 0.24
Nodes (6): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Swell

### Community 114 - "net.minecraft.world.level.block.Rotation"
Cohesion: 0.23
Nodes (3): PlacedWall, GoldChurch, Layout

### Community 116 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.07
Nodes (4): Palette, WallTemplates, MarkerBlockEntity, MarkerSpec

### Community 117 - "RoadLayer"
Cohesion: 0.17
Nodes (11): A tick budget and staggered schedules make the fixes stick, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom?, Gaps (+3 more)

### Community 118 - ".view"
Cohesion: 0.14
Nodes (8): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, CityManager, GrandStrategy, View

### Community 123 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 124 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 125 - "Settlement"
Cohesion: 0.09
Nodes (11): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+3 more)

### Community 126 - "CityLayout"
Cohesion: 0.11
Nodes (3): District, Site, CityLayoutTest

### Community 128 - "RoadNetwork"
Cohesion: 0.14
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 129 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.19
Nodes (5): DistantShips, Far, BattleKind, Part, ShipHulls

### Community 130 - "BattleShips"
Cohesion: 0.27
Nodes (3): BattleShips, Helm, Ram

### Community 132 - "MapContext"
Cohesion: 0.10
Nodes (7): MapContext, LegendEntry, MapLayer, Symbol, PoiIconLayer, TerritoryLayer, UnitLayer

### Community 134 - "BlockPos"
Cohesion: 0.14
Nodes (4): BoardAction, GateSet, GateView, MarkerView

### Community 136 - "Plan"
Cohesion: 0.18
Nodes (8): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Plan

### Community 137 - ".requestUnits"
Cohesion: 0.18
Nodes (3): UnitInfo, UnitsRequest, UnitsState

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.09
Nodes (22): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, Cited Findings, Gaps, Gaps (+14 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.17
Nodes (11): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 5. Water/ocean waves (sea swell, foam, boats on waves), Cited Findings, Cited Findings, Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest), Gaps, Gaps, Inferences (+3 more)

### Community 143 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.18
Nodes (10): Character page, Characters (holders, skills, prowess, traits), Court 3D scene (CourtView), Cultures (seven homelands), Faiths and cults, Heralds (daily news), Mustering (offensive and ceremonial), Nation page (+2 more)

### Community 146 - "Race"
Cohesion: 0.18
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 148 - "Ranks"
Cohesion: 0.08
Nodes (13): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+5 more)

### Community 149 - "Townsperson"
Cohesion: 0.08
Nodes (7): RecruiterRenderer, TownspersonRenderer, Walk, DistrictKey, Townsfolk, Townsperson, StreetWalksTest

### Community 152 - "net.minecraft.core.BlockPos"
Cohesion: 0.12
Nodes (3): Saved, CharacterMarkers, Placed

### Community 155 - "Point"
Cohesion: 0.07
Nodes (13): Point, Ground, GroundPlan, Engine, LADDERS, RAMS, TOWERS, Plan (+5 more)

### Community 156 - ".plan"
Cohesion: 0.16
Nodes (4): Faith, Bond, RealmPlanner, Seat

### Community 160 - "War"
Cohesion: 0.08
Nodes (7): Army, Fort, Event, Muster, CEREMONIAL, OFFENSIVE, War

### Community 174 - "4. Depth buffers and post effects (DH depth, occlusion, GPU sync)"
Cohesion: 0.25
Nodes (6): 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), Cited Findings, Gaps, Inferences, Takeaway, DhApiBeforeApplyShaderRenderEvent

### Community 178 - ".hash"
Cohesion: 0.16
Nodes (4): NobleAnnex, OriginRoller, Roll, NobleAnnexTest

### Community 181 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 204 - "ConquestWorldScreen"
Cohesion: 0.14
Nodes (4): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Politics.Front

### Community 206 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.11
Nodes (5): BackBanner, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 217 - ".pack"
Cohesion: 0.29
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 221 - "Siege"
Cohesion: 0.09
Nodes (9): Gate, GateAction, GateState, Region, IN, OUT, WALL, Siege (+1 more)

### Community 223 - "Cited Findings"
Cohesion: 0.06
Nodes (14): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Cited Findings, Gaps, Inferences, Takeaway, DistantDepth, Arrow (+6 more)

### Community 224 - "DistantArmyRenderer.java"
Cohesion: 0.06
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 232 - "Cited Findings"
Cohesion: 0.09
Nodes (8): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Gaps, Inferences, Takeaway, BackBanners, ClientMapCache, Throw, ThrownFigures

### Community 234 - "net.neoforged.bus.api.SubscribeEvent"
Cohesion: 0.04
Nodes (4): Post, ClientEvents, ConquestEvents, ConquestRuntime

### Community 235 - "FormationSnapshot"
Cohesion: 0.09
Nodes (8): Cited Findings, FormationSnapshot, Ship, BattleSounds, ChargeSound, DinSound, Source, DistantArmies

### Community 236 - "ShipKind"
Cohesion: 0.33
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 237 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 238 - "SableLink"
Cohesion: 0.18
Nodes (3): Frame, SableLink, SiegeFrames

### Community 252 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 254 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

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

### Community 263 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 274 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 275 - "CourtSet"
Cohesion: 0.25
Nodes (3): CourtSet, Seat, Style

### Community 278 - "Kind"
Cohesion: 0.15
Nodes (10): Kind, ARCHERS, BEARER, CAVALRY, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY (+2 more)

### Community 287 - "2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling"
Cohesion: 0.40
Nodes (5): 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, Cited Findings, Gaps, Inferences, Takeaway

### Community 288 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.33
Nodes (4): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 298 - "3. GUI and map screens (strategy map, faction map, battle overlays)"
Cohesion: 0.40
Nodes (5): 3. GUI and map screens (strategy map, faction map, battle overlays), Cited Findings, Gaps, Inferences, Takeaway

### Community 300 - "7. Client tick, stutter and sound"
Cohesion: 0.40
Nodes (5): 7. Client tick, stutter and sound, Cited Findings, Gaps, Inferences, Takeaway

### Community 301 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.15
Nodes (9): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), Epic Knights optional armour (Armoury), Martial titles (Sword Saint, Sword King, Sword God), Organizations, RafasConquest (PvE addon for Villager Recruits), Sword sanctums (+1 more)

### Community 302 - "CountryPiece"
Cohesion: 0.16
Nodes (7): Build, CASTLE, HAMLET, RUIN, TOWER, WELL, CountryPiece

### Community 303 - "Type"
Cohesion: 0.33
Nodes (4): Type, CLEAR, DELIVER, SCOUT

### Community 305 - ".view"
Cohesion: 0.10
Nodes (12): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+4 more)

### Community 306 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.16
Nodes (4): Camera, Card, Enlisted, Unit

### Community 308 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 310 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 312 - "8. Lessons from performance mods that a content mod can apply"
Cohesion: 0.40
Nodes (5): 8. Lessons from performance mods that a content mod can apply, Cited Findings, Gaps, Inferences, Takeaway

### Community 315 - "RecruitsBattle"
Cohesion: 0.08
Nodes (4): Cited Findings, Landing, Officers, RecruitsBattle

### Community 316 - "9. Memory: client caches, textures and NativeImages that are never freed or grow without bound"
Cohesion: 0.50
Nodes (4): 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Gaps, Inferences, Takeaway

### Community 324 - "Realm"
Cohesion: 0.16
Nodes (5): Organization, Kind, FRIEND, RIVAL, Realm

### Community 327 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 328 - "net.neoforged.fml.common.EventBusSubscriber"
Cohesion: 0.08
Nodes (4): BattleKeepInventory, ClientBarks, Line, ClientModEvents

### Community 332 - "Ranks (Soldier to Grand Marshal)"
Cohesion: 0.17
Nodes (11): Battles (prototype), Blocks of 20 soldiers, Enlisted players, Gold church, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Land grants (seven ways), Peerage (military and noble ladders), Quest board and quests (+3 more)

### Community 344 - "How do the simulation and daily-decision systems schedule work? Do they spike?"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway

## Ambiguous Edges - Review These
- `Roads (high, city, town roads; bridges; signposts)` → `Mustering (offensive and ceremonial)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **363 isolated node(s):** `FOOD`, `CAPITAL`, `CITY`, `FARM`, `ARCHERS` (+358 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1242 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **195 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `net.minecraft.world.item.DyeColor`, `net.minecraft.world.level.levelgen.structure.BoundingBox`, `.army`, `SiegeWorks`, `WarCouncil`, `ConquestEvents.java`, `PveCommands Registration`, `WarCamp`, `net.minecraft.core.Direction`, `rafasconquest.faction.Faction`, `War`, `net.minecraft.world.phys.Vec3`, `.roadStart`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `Shape`, `.general`, `net.minecraft.world.item.ItemStack`, `.block`, `RafasConquest.java`, `RecruitsBattle`, `net.neoforged.neoforge.network.handling.IPayloadContext`, `.startAt`, `PoliticsTest`, `Calibration`, `rafasconquest.battle.FieldBlock`, `net.neoforged.bus.api.SubscribeEvent`, `Phase`, `.of`, `UnitOrder`, `RecruitsBattle.java`, `.view`, `.report`, `Trait`, `UnitMode`?**
  _High betweenness centrality (0.072) - this node is a cross-community bridge._
- **What connects `FOOD`, `CAPITAL`, `CITY` to the rest of the system?**
  _363 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.050191570881226055 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `PoiPrecomputer.java`, `MapContext`, `.render`, `OrganizationScreen`, `net.minecraft.client.gui.screens.Screen`, `.plan`, `WarCouncil`, `PoliticalStyle`, `School`, `.place`, `War`, `Centre`, `.postChunk`, `City`, `TitleMapTest`, `net.minecraft.world.item.ItemStack`, `Realm`, `RafasConquest.java`, `PoliticsTest`, `CourtView`, `.generate`, `ConquestSavedData`, `arraylist`, `Cited Findings`, `net.neoforged.bus.api.SubscribeEvent`, `RoadLayer`, `GrandStrategyScreen`, `Terrain`, `ClientPolitics`, `Settlement`?**
  _High betweenness centrality (0.042) - this node is a cross-community bridge._
- **Should `PoiPrecomputer.java` be split into smaller, more focused modules?**
  _Cohesion score 0.060408163265306125 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Roads (high, city, town roads; bridges; signposts)` and `Mustering (offensive and ceremonial)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._