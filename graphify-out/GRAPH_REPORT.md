# Graph Report - RafasConquest  (2026-11-10)

## Corpus Check
- 453 files · ~388,192 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 115 file(s) not represented in the graph (top: .nbt 84, .toml 14, (none) 3)

## Summary
- 6347 nodes · 20090 edges · 339 communities (146 shown, 193 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2680 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `63cd2e93`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FieldBlock
- WarCamp.java
- net.minecraft.world.level.levelgen.structure.BoundingBox
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsLink
- net.minecraft.server.MinecraftServer
- SiegeWorks
- .passOnOrders
- Recruiter
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- Roads
- BaseMap
- ConquestNetwork
- PoiPrecomputer.java
- BattlePlan
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.screens.Screen
- PveCommands.java
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- HorizontalStretch
- CityManagerScreen
- .plan
- CityLayout
- .shift
- WarCouncil
- Quests
- RecruitsBattle
- War
- CapitalLayout
- PveCommands Registration
- School
- CityPlan
- Land
- .plus
- FormationSnapshot
- net.minecraft.core.Direction
- rafasconquest.faction.Faction
- net.minecraft.server.level.ServerLevel
- .block
- net.minecraft.core.Vec3i
- ConquestSavedData
- net.minecraft.world.phys.Vec3
- Personalities
- SeaLanes
- CityVariant
- .drawEmpire
- .cellMax
- BattleSounds
- Centre
- Homeland
- Row
- Peerage
- SeatCities
- Faction Map View
- .of
- Cited Findings
- net.minecraft.world.level.block.state.BlockState
- TitleMapTest
- .measure
- StyleLibrary
- .render
- .view
- RafasConquest.java
- .render
- .postChunk
- DistantOcclusion
- CityGridBuilder
- MapLands
- net.minecraft.server.level.ServerPlayer
- PlaceMusicClient
- Tier
- .onLevelLoad
- net.neoforged.neoforge.network.handling.IPayloadContext
- SeaCalm
- Ownership & Territory
- .choose
- .player
- PoliticsTest
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- ConquestEvents.java
- DialogueScreen
- .send
- OrganizationScreen
- net.minecraft.world.item.ItemStack
- Barracks
- .generate
- PoiPrecomputer
- WaterSwell
- FactionInfoScreen
- ClientScreens
- Cited Findings
- CityStyles
- GateBlockEntity.java
- Realm
- com.mojang.blaze3d.vertex.PoseStack
- rafasconquest.battle.FormationSnapshot
- Override
- RoadFeature
- com.talhanation.recruits.entities.AbstractRecruitEntity
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- MarkerScreen
- NobleAnnex
- RecruitsBattle.java
- GateBlockEntity
- RoadLayer
- CityLayout
- GrandStrategyScreen
- Terrain
- ClientPolitics
- DistantArmyRenderer
- UnitOrder
- Settlement
- Placed
- RoadNetwork
- net.minecraft.world.item.DyeColor
- BattleShips
- MapContext
- RafasConquest (PvE addon for Villager Recruits)
- Role
- .build
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- GroundPlan.java
- net.minecraft.world.level.block.Rotation
- net.minecraft.resources.ResourceLocation
- Lands
- .view
- Townsperson
- CLAUDE.md
- BattleRole
- PoliticsData
- .plan
- CourtView
- Cited Findings
- Front
- SableLink
- City
- Small Cluster 170
- Countryside
- .inCity
- .simulate
- CityTerrainPiece
- BoardScreen
- .pack
- com.talhanation.recruits.entities.AbstractRecruitEntity
- rafasconquest.battle.BattlePlan.Point
- .render
- CourtView.java
- ConquestEntities.java
- .touches
- .onLogout
- net.neoforged.bus.api.SubscribeEvent
- Organizations
- UnitInfo
- .bark
- ConquestGameRules
- Quest
- Trait
- .view
- Call
- Ramming
- Duty
- Kind
- net.minecraft.network.FriendlyByteBuf
- ItemFlags.java
- DistantGeneration
- ConquestServer
- WallTemplates
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- Realm
- Kind
- CourtSet
- NobleDistrictTest
- Clash
- BattleType
- arraylist
- Phase
- org.jetbrains.annotations.Nullable
- Job
- UnitMode
- 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound
- GatePlanksBlock.java
- Task
- .people
- Piece
- .sendVolleys
- 4. Depth buffers and post effects (DH depth, occlusion, GPU sync)
- BlockGrid
- ShipKind
- .place
- 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel)
- 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling
- CountryPiece
- ClientBarks
- 8. Lessons from performance mods that a content mod can apply
- SetSetting
- .walks
- Recruitment posts and recruiter
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- net.minecraft.server.level.ServerPlayer

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 266 edges
2. `FieldBlock` - 192 edges
3. `FieldBattle` - 166 edges
4. `ConquestNetwork` - 117 edges
5. `FieldArmy` - 111 edges
6. `Lands` - 89 edges
7. `Politics` - 79 edges
8. `BaseMap` - 77 edges
9. `BattleScreen` - 63 edges
10. `Realm` - 56 edges

## Surprising Connections (you probably didn't know these)
- `Battle code costs 0.2 ms; the multi-second freezes come from blocking` --references--> `RecruitsArrowCleanerMixin`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/mixin/RecruitsArrowCleanerMixin.java
- `Every frame pays for disk listings, per-rectangle flushes and unculled soldiers` --references--> `TitleLayer`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/client/map/TitleLayer.java
- `A tick budget and staggered schedules make the fixes stick` --references--> `BaseMap`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/map/BaseMap.java
- `Conclusion` --references--> `RoadLayer`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Cited Findings` --references--> `Sprite`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/battle/FormationSnapshot.java

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

## Communities (339 total, 193 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.05
Nodes (7): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 1 - "WarCamp.java"
Cohesion: 0.09
Nodes (22): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Characters (holders, skills, prowess, traits), Cultures (seven homelands), Enlisted players, Envelopment, Epic Knights optional armour (Armoury) (+14 more)

### Community 2 - "net.minecraft.world.level.levelgen.structure.BoundingBox"
Cohesion: 0.08
Nodes (8): BarracksPiece, CityRoadPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece, PlaceholderPiece

### Community 3 - ".army"
Cohesion: 0.08
Nodes (3): Point, DoctrineTest, FieldBattleTest

### Community 4 - "CityTerrain"
Cohesion: 0.08
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.09
Nodes (3): BattleScreen, Entry, Menu

### Community 6 - "DistantHorizonsLink"
Cohesion: 0.15
Nodes (3): DistantHorizonsArmies, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.12
Nodes (3): FeudDuel, MusterBars, Wars

### Community 8 - "SiegeWorks"
Cohesion: 0.12
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 9 - ".passOnOrders"
Cohesion: 0.08
Nodes (7): Landing, Live, Shape, LINE, SQUARE, V, WEDGE

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "Roads"
Cohesion: 0.12
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 13 - "BaseMap"
Cohesion: 0.07
Nodes (12): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, MapPreview, BaseMap, Nation (+4 more)

### Community 14 - "ConquestNetwork"
Cohesion: 0.07
Nodes (13): Bark, CameraAt, CityClose, CityView, ConquestNetwork, FactionInfoReply, GateSet, Place (+5 more)

### Community 15 - "PoiPrecomputer.java"
Cohesion: 0.05
Nodes (16): SeedHash, TerritoryMap, Faction, FactionBannerNbt, FactionType, BANDIT, CITY_STATE, EMPIRE (+8 more)

### Community 16 - "BattlePlan"
Cohesion: 0.04
Nodes (22): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, BattlePlan, Order, CHARGE (+14 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.22
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "net.minecraft.client.gui.screens.Screen"
Cohesion: 0.05
Nodes (7): Character page, Court 3D scene (CourtView), Demographics (population by culture and race), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), GateScreen, InventoryStatsButton

### Community 20 - "PveCommands.java"
Cohesion: 0.08
Nodes (31): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+23 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 23 - "HorizontalStretch"
Cohesion: 0.12
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 25 - ".plan"
Cohesion: 0.17
Nodes (6): Nation, Nations, Plan, Seats, NationsTest, World

### Community 26 - "CityLayout"
Cohesion: 0.09
Nodes (6): CityLayout, District, Site, SlopeTile, WorldSettings, CityLayoutTest

### Community 27 - ".shift"
Cohesion: 0.20
Nodes (5): BattleSite, Part, Water, BattleSite.Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.15
Nodes (6): Facts, Officer, Said, Talk, WarCouncil, WarCouncilPagesTest

### Community 29 - "Quests"
Cohesion: 0.14
Nodes (5): DataFiles, Dialogue, Kind, Quests, Target

### Community 30 - "RecruitsBattle"
Cohesion: 0.06
Nodes (3): GeneralTask, RecruitsBattle, TickTimes

### Community 31 - "War"
Cohesion: 0.09
Nodes (4): Army, Goal, PoliticsSync, War

### Community 32 - "CapitalLayout"
Cohesion: 0.08
Nodes (10): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, ProvinceNames (+2 more)

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.16
Nodes (3): CityPlan, Piece, Building

### Community 36 - "Land"
Cohesion: 0.14
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 37 - ".plus"
Cohesion: 0.11
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 38 - "FormationSnapshot"
Cohesion: 0.11
Nodes (13): Cited Findings, FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL (+5 more)

### Community 39 - "net.minecraft.core.Direction"
Cohesion: 0.13
Nodes (7): Land, Result, SiegeSurvey, Wall, CityGates, Done, SiegeSurveyTest

### Community 44 - "ConquestSavedData"
Cohesion: 0.08
Nodes (5): Cited Findings, ConquestSavedData, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 45 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.16
Nodes (4): Camera, Card, Enlisted, Unit

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "CityVariant"
Cohesion: 0.15
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 50 - ".drawEmpire"
Cohesion: 0.29
Nodes (3): Hit, LabelLayer, LabelShape

### Community 52 - "BattleSounds"
Cohesion: 0.30
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 53 - "Centre"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 54 - "Homeland"
Cohesion: 0.10
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 56 - "Peerage"
Cohesion: 0.08
Nodes (10): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+2 more)

### Community 57 - "SeatCities"
Cohesion: 0.12
Nodes (4): Grid, SeatCities, SeatCitiesTest, World

### Community 58 - "Faction Map View"
Cohesion: 0.17
Nodes (3): Cited Findings, FactionMapScreen, MapView

### Community 59 - ".of"
Cohesion: 0.17
Nodes (4): FactionBanner, Layer, CityBanners, FactionBannerTest

### Community 61 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.07
Nodes (8): Saved, CommandTableBlock, GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, Builder, MarkerBlock

### Community 63 - "TitleMapTest"
Cohesion: 0.17
Nodes (3): TitleMap, Land, TitleMapTest

### Community 65 - "StyleLibrary"
Cohesion: 0.10
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - ".render"
Cohesion: 0.09
Nodes (7): CharacterScreen, Spot, ClientPeerage, Banner, Portraits, Honour, Honours

### Community 67 - ".view"
Cohesion: 0.21
Nodes (4): Option, Talk, View, DialogueTest

### Community 69 - "RafasConquest.java"
Cohesion: 0.05
Nodes (3): Battle map developer web page, Morale, rout and rally, RafasConquest

### Community 71 - ".postChunk"
Cohesion: 0.14
Nodes (3): Cited Findings, BarracksPeople, CityGuards

### Community 72 - "DistantOcclusion"
Cohesion: 0.10
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 73 - "CityGridBuilder"
Cohesion: 0.11
Nodes (3): CityGridBuilder, Ground, Platform

### Community 75 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.11
Nodes (11): Inferences, Cited Findings, Cited Findings, CameraView, ChunkMapAccess, ChunkMapCameraMixin, CityVegetationMixin, RecruitsArrowCleanerMixin (+3 more)

### Community 76 - "PlaceMusicClient"
Cohesion: 0.19
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 77 - "Tier"
Cohesion: 0.29
Nodes (5): Tier, COUNTY, DUCHY, EMPIRE, KINGDOM

### Community 79 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.10
Nodes (7): BackBanner, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel

### Community 81 - "SeaCalm"
Cohesion: 0.16
Nodes (4): Sea, Depths, SeaCalm, SwellTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.20
Nodes (5): Candidate, OwnershipResolver, Claim, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.17
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 84 - ".player"
Cohesion: 0.14
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 86 - "Calibration"
Cohesion: 0.14
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.09
Nodes (16): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR, Strategy (+8 more)

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.06
Nodes (7): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, Bounds, DistanceScaling, DayClockTest, DistanceScalingTest

### Community 93 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.10
Nodes (17): WarHorn, Armoury, Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE (+9 more)

### Community 95 - ".generate"
Cohesion: 0.10
Nodes (5): WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry

### Community 96 - "PoiPrecomputer"
Cohesion: 0.10
Nodes (3): RecruitsBridge, Poi, PoiPrecomputer

### Community 98 - "WaterSwell"
Cohesion: 0.11
Nodes (7): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, WaterSwell, Swell

### Community 101 - "Cited Findings"
Cohesion: 0.11
Nodes (6): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, DistantArmies

### Community 103 - "GateBlockEntity.java"
Cohesion: 0.08
Nodes (4): ConquestContent, WorldMapItem, MarkerBlockEntity, MarkerSpec

### Community 105 - "Realm"
Cohesion: 0.10
Nodes (5): Hook, Succession, Taking, RealmPlannerTest, World

### Community 106 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.10
Nodes (4): BackBannerLayer, GateRenderer, MarkerRenderer, MouthLayer

### Community 107 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.28
Nodes (6): Boxes, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 111 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.07
Nodes (28): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+20 more)

### Community 112 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.17
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips, Ride

### Community 116 - "GateBlockEntity"
Cohesion: 0.07
Nodes (10): Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING, GateGeometry (+2 more)

### Community 117 - "RoadLayer"
Cohesion: 0.11
Nodes (13): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+5 more)

### Community 118 - "CityLayout"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 123 - "DistantArmyRenderer"
Cohesion: 0.14
Nodes (4): Cited Findings, DistantArmyRenderer, Figure, Hull

### Community 124 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 125 - "Settlement"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 128 - "RoadNetwork"
Cohesion: 0.15
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 129 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.15
Nodes (5): DistantShips, Far, BattleKind, Part, ShipHulls

### Community 130 - "BattleShips"
Cohesion: 0.27
Nodes (3): BattleShips, Helm, Ram

### Community 132 - "MapContext"
Cohesion: 0.10
Nodes (9): BaseMapLayer, MapContext, LegendEntry, MapLayer, Symbol, Place, PlaceMenuLayer, PoiIconLayer (+1 more)

### Community 133 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 134 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 137 - ".build"
Cohesion: 0.12
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (23): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, Cited Findings, Cited Findings, Gaps (+15 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.10
Nodes (20): 3. GUI and map screens (strategy map, faction map, battle overlays), 5. Water/ocean waves (sea swell, foam, boats on waves), 6. Sodium (and Iris) compatibility, 7. Client tick, stutter and sound, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+12 more)

### Community 141 - "GroundPlan.java"
Cohesion: 0.13
Nodes (7): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), TickWeave experiment

### Community 142 - "net.minecraft.world.level.block.Rotation"
Cohesion: 0.24
Nodes (3): PlacedWall, GoldChurch, Layout

### Community 143 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.22
Nodes (3): UnitLayer, RecruiterRenderer, TownspersonRenderer

### Community 146 - "Lands"
Cohesion: 0.07
Nodes (13): Cited Findings, Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Lands, Stronghold (+5 more)

### Community 148 - ".view"
Cohesion: 0.07
Nodes (14): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+6 more)

### Community 152 - "BattleRole"
Cohesion: 0.18
Nodes (6): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT

### Community 156 - ".plan"
Cohesion: 0.13
Nodes (7): Centre, Demographics, Share, Faith, Bond, RealmPlanner, Seat

### Community 157 - "CourtView"
Cohesion: 0.08
Nodes (5): Retainer, Camera, CourtView, Figure, NationScreen

### Community 160 - "Cited Findings"
Cohesion: 0.13
Nodes (7): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, BattleSpotRing, SableSeen

### Community 163 - "Front"
Cohesion: 0.13
Nodes (4): Feud, Lord, Feuds, Front

### Community 164 - "SableLink"
Cohesion: 0.24
Nodes (3): Frame, SableLink, SiegeFrames

### Community 174 - "Countryside"
Cohesion: 0.21
Nodes (4): Countryside, Plan, CountrysideTest, World

### Community 181 - ".simulate"
Cohesion: 0.17
Nodes (6): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, GameCharacter

### Community 206 - "BoardScreen"
Cohesion: 0.20
Nodes (3): BoardScreen, BoardAction, MarkerSet

### Community 217 - ".pack"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 219 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.12
Nodes (8): Officers, Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER, Side

### Community 221 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.06
Nodes (20): Engine, LADDERS, RAMS, TOWERS, Gate, GateAction, GateState, Kind (+12 more)

### Community 223 - ".render"
Cohesion: 0.20
Nodes (3): DistantDepth, Arrow, ArrowVolleys

### Community 224 - "CourtView.java"
Cohesion: 0.04
Nodes (4): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), ClientModEvents

### Community 234 - "net.neoforged.bus.api.SubscribeEvent"
Cohesion: 0.05
Nodes (3): BattleKeepInventory, ClientEvents, ConquestEvents

### Community 235 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 249 - "Quest"
Cohesion: 0.19
Nodes (9): Ladder, MILITARY, NOBLE, NONE, Quest, Type, CLEAR, DELIVER (+1 more)

### Community 252 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 254 - ".view"
Cohesion: 0.15
Nodes (8): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, GrandStrategy, View, Retinues

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 257 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 259 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 263 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.05
Nodes (14): BaseMapPart, BattleSnapshot, BattleSpot, BoardView, FigureStrike, GateView, GrandStrategyView, MarkerView (+6 more)

### Community 277 - "DistantGeneration"
Cohesion: 0.20
Nodes (6): Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Takeaway, Api, DistantGeneration

### Community 287 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.33
Nodes (4): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender

### Community 288 - "Realm"
Cohesion: 0.14
Nodes (5): Kind, FRIEND, RIVAL, Realm, CharacterMarkers

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "CourtSet"
Cohesion: 0.25
Nodes (3): CourtSet, Seat, Style

### Community 295 - "Clash"
Cohesion: 0.27
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 298 - "BattleType"
Cohesion: 0.12
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 300 - "arraylist"
Cohesion: 0.05
Nodes (8): CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 301 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 303 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.06
Nodes (14): Base map generated from seed, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Faction Info screen, Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, World map layers, Nations by zone (+6 more)

### Community 305 - "Job"
Cohesion: 0.22
Nodes (6): Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY, SHIELDMAN

### Community 306 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 308 - "9. Memory: client caches, textures and NativeImages that are never freed or grow without bound"
Cohesion: 0.25
Nodes (5): 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Gaps, Inferences, Takeaway

### Community 310 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 312 - "Piece"
Cohesion: 0.11
Nodes (8): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Plan

### Community 318 - "4. Depth buffers and post effects (DH depth, occlusion, GPU sync)"
Cohesion: 0.33
Nodes (6): 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), Cited Findings, Gaps, Inferences, Takeaway, DhApiBeforeApplyShaderRenderEvent

### Community 320 - "ShipKind"
Cohesion: 0.33
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 322 - "1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel)"
Cohesion: 0.40
Nodes (5): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), Cited Findings, Gaps, Inferences, Takeaway

### Community 323 - "2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling"
Cohesion: 0.40
Nodes (5): 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, Cited Findings, Gaps, Inferences, Takeaway

### Community 324 - "CountryPiece"
Cohesion: 0.18
Nodes (7): Build, CASTLE, HAMLET, RUIN, TOWER, WELL, CountryPiece

### Community 326 - "8. Lessons from performance mods that a content mod can apply"
Cohesion: 0.40
Nodes (5): 8. Lessons from performance mods that a content mod can apply, Cited Findings, Gaps, Inferences, Takeaway

### Community 332 - "Recruitment posts and recruiter"
Cohesion: 0.67
Nodes (3): Gold church, Quest board and quests, Recruitment posts and recruiter

## Ambiguous Edges - Review These
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **352 isolated node(s):** `ADVANCE`, `CAVALRY_CHARGE`, `CAVALRY_RECALL`, `WALL`, `GENERAL_CHARGES` (+347 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1202 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **193 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `net.minecraft.world.item.DyeColor`, `net.minecraft.world.level.levelgen.structure.BoundingBox`, `.army`, `net.minecraft.server.MinecraftServer`, `SiegeWorks`, `.passOnOrders`, `.shift`, `WarCouncil`, `PveCommands Registration`, `net.minecraft.core.Direction`, `rafasconquest.faction.Faction`, `net.minecraft.server.level.ServerLevel`, `.block`, `BattleType`, `arraylist`, `net.minecraft.world.phys.Vec3`, `Phase`, `org.jetbrains.annotations.Nullable`, `UnitMode`, `.simulate`, `Cited Findings`, `.sendVolleys`, `RafasConquest.java`, `net.minecraft.server.level.ServerPlayer`, `net.minecraft.server.level.ServerPlayer`, `Calibration`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `rafasconquest.battle.BattlePlan.Point`, `net.minecraft.world.item.ItemStack`, `net.neoforged.bus.api.SubscribeEvent`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `UnitOrder`, `RecruitsBattle.java`, `Trait`?**
  _High betweenness centrality (0.070) - this node is a cross-community bridge._
- **What connects `ADVANCE`, `CAVALRY_CHARGE`, `CAVALRY_RECALL` to the rest of the system?**
  _352 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.0501511834011052 - nodes in this community are weakly interconnected._
- **Why does `ConquestNetwork` connect `ConquestNetwork` to `BattleScreen`, `net.minecraft.network.FriendlyByteBuf`, `map`, `net.minecraft.client.gui.screens.Screen`, `CityManagerScreen`, `War`, `PveCommands Registration`, `org.jetbrains.annotations.Nullable`, `.sendVolleys`, `net.minecraft.world.level.block.state.BlockState`, `RafasConquest.java`, `SetSetting`, `BoardScreen`, `net.neoforged.neoforge.network.handling.IPayloadContext`, `net.minecraft.server.level.ServerPlayer`, `.send`, `Cited Findings`, `UnitInfo`, `RecruitsBattle.java`?**
  _High betweenness centrality (0.046) - this node is a cross-community bridge._
- **Should `WarCamp.java` be split into smaller, more focused modules?**
  _Cohesion score 0.08923076923076922 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._