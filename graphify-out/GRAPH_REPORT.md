# Graph Report - RafasConquest  (2026-11-12)

## Corpus Check
- 473 files · ~435,543 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 6987 nodes · 22205 edges · 370 communities (150 shown, 220 thin omitted)
- Extraction: 86% EXTRACTED · 14% INFERRED · 0% AMBIGUOUS · INFERRED: 3002 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `b9453275`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FieldBlock
- Townsperson
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsArmies.java
- CityGridBuilder
- SiegeWorks
- net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
- rafasconquest.battle.BattlePlan.Arm
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- Land
- BaseMap
- UnitOrder
- GateDoors.java
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.GuiGraphics
- PveCommands.java
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- HorizontalStretch
- CityManagerScreen
- .plan
- Siege
- .shift
- WarCouncil
- War
- Retinues
- net.minecraft.world.level.levelgen.structure.Structure
- CapitalLayout
- PveCommands Registration
- School
- CityPlan
- .of
- net.neoforged.neoforge.network.handling.IPayloadContext
- ConquestWorldgen
- rafasconquest.faction.Faction
- SeaCalm
- Feud
- net.minecraft.server.MinecraftServer
- net.minecraft.nbt.CompoundTag
- ConquestEvents.java
- Personalities
- SeaLanes
- BattleType
- Cited Findings
- Capture
- Faction
- Centre
- Barracks
- Row
- .rightClick
- SeatCities
- Faction Map View
- .step
- ClientScreens
- .render
- TitleMapTest
- .enlist
- StyleLibrary
- .step
- .choose
- RafasConquest.java
- RecruitsBattle
- net.minecraft.server.level.ServerLevel
- com.mojang.blaze3d.vertex.PoseStack
- .deployment
- MapLands
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Phase
- Quests
- net.minecraft.world.level.block.state.BlockState
- .roadStart
- Terrain
- Ownership & Territory
- CityLayout
- Gate
- Roads
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- CityLayout
- .thePoliticsAreKept
- .place
- .generate
- net.minecraft.core.Vec3i
- BattlePlan
- FactionInfoScreen
- .view
- CityStyles
- FigureBatches
- net.minecraft.server.level.ServerPlayer
- MouthLayer.java
- ConquestNetwork
- MarkerScreen
- net.minecraft.sounds.SoundEvent
- UnitMode
- City
- BattleShips
- WaterSwell
- .render
- ConquestSavedData
- FigureGear
- Cited Findings
- PoiPrecomputer.java
- GrandStrategyScreen
- MapLayer
- .siege
- Shape
- .nearest
- Rank
- Swell
- BattleShips
- Kit
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- BattleSounds
- MapContext
- NobleTitle
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .onLevelLoad
- CourtSet
- .cellMax
- net.minecraft.core.Vec3i
- Job
- .place
- CLAUDE.md
- .plan
- .place
- .plan
- .view
- .rout
- WarPlan
- Machine
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- MapView
- How do world generation and structure placement work, and what do they cost?
- BoardScreen
- SoldierFigure
- .touches
- DayClockTest
- MarkerRenderer.java
- .pack
- org.spongepowered.asm.mixin.injection.Inject
- Duty
- Front
- .hash
- RecruitFigures.java
- .boundsAt
- ClientEvents
- ClientPolitics
- .model
- net.minecraft.resources.ResourceLocation
- net.neoforged.bus.api.SubscribeEvent
- SeatCitiesTest
- Job
- DistantArmyRenderer
- ConquestGameRules
- BlockPos
- Trait
- OrganizationScreen
- Call
- Ramming
- Profiling run sheet (baseline)
- .measure
- .passOnOrders
- GateBlockEntity
- BattleKeepInventory.java
- ThrownFigures
- Lands
- CourtView
- Realm
- net.minecraft.network.FriendlyByteBuf
- Kind
- BattlePlan.java
- How Total War calculates battles between units
- Plan
- CityPlacement
- .order
- ShipHulls
- ShareLiveTest
- .postChunk
- .at
- Clash
- ItemFlags.java
- Role
- ConquestWorldScreen
- net.minecraft.server.level.ServerPlayer
- GatePlanksBlock
- org.jetbrains.annotations.Nullable
- CountryStructure
- BackBanners
- .build
- Recruiter
- ConquestConfig
- Kind
- .borders
- .findGenerationPoint
- PoliticsView
- ClientBarks
- Where are full scans, nested loops, repeated lookups and per-tick allocations?
- BlockGrid
- ShipKind
- Piece
- .report
- net.minecraft.core.BlockPos
- Type
- Cited Findings
- Kind
- State
- WarHorn
- net.neoforged.fml.common.EventBusSubscriber
- ClientMapCache

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 295 edges
2. `FieldBattle` - 200 edges
3. `FieldBlock` - 197 edges
4. `FieldArmy` - 127 edges
5. `ConquestNetwork` - 120 edges
6. `Lands` - 89 edges
7. `Siege` - 83 edges
8. `BattleScreen` - 82 edges
9. `Politics` - 79 edges
10. `BaseMap` - 77 edges

## Surprising Connections (you probably didn't know these)
- `Every frame pays for disk listings, per-rectangle flushes and unculled soldiers` --references--> `TitleLayer`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/client/map/TitleLayer.java
- `Conclusion` --references--> `RoadLayer`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Cited Findings` --references--> `RoadLayer`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Takeaway` --references--> `RoadLayer`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Inferences` --references--> `PoliticalMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/map/PoliticalMap.java

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

## Communities (370 total, 220 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.05
Nodes (7): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 1 - "Townsperson"
Cohesion: 0.10
Nodes (5): Walk, DistrictKey, Townsfolk, Townsperson, StreetWalksTest

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.05
Nodes (10): BarracksPiece, CityRoadPiece, CityTerrainPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece (+2 more)

### Community 3 - ".army"
Cohesion: 0.07
Nodes (7): Point, DoctrineTest, FieldBattleTest, GroundPlanTest, Land, GrandStrategyDateTest, ArmouryTest

### Community 4 - "CityTerrain"
Cohesion: 0.08
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.05
Nodes (9): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+1 more)

### Community 7 - "CityGridBuilder"
Cohesion: 0.09
Nodes (9): CityGridBuilder, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY (+1 more)

### Community 9 - "net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate"
Cohesion: 0.19
Nodes (3): DistantShips, Far, SableSeen

### Community 10 - "rafasconquest.battle.BattlePlan.Arm"
Cohesion: 0.08
Nodes (16): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+8 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 13 - "BaseMap"
Cohesion: 0.08
Nodes (10): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, BaseMap, Nation, BaseMapStore (+2 more)

### Community 14 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.14
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 19 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.04
Nodes (3): Races (Sunborn, Vinekin, Meadowfolk, ...), GateScreen, InventoryStatsButton

### Community 20 - "PveCommands.java"
Cohesion: 0.09
Nodes (31): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+23 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "Siege"
Cohesion: 0.17
Nodes (6): GateAction, Region, IN, OUT, WALL, Siege

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.16
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 29 - "War"
Cohesion: 0.10
Nodes (4): Army, Goal, War, Politics.Front

### Community 30 - "Retinues"
Cohesion: 0.14
Nodes (4): PlayerStats, View, Retinues, RetinuesTest

### Community 31 - "net.minecraft.world.level.levelgen.structure.Structure"
Cohesion: 0.10
Nodes (3): CityDistrictStructure, CityStructure, FarmStructure

### Community 32 - "CapitalLayout"
Cohesion: 0.18
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 34 - "School"
Cohesion: 0.09
Nodes (14): Dojos, Ground, School, BLOOD, FIRE, ICE, IRON, LIGHT (+6 more)

### Community 35 - "CityPlan"
Cohesion: 0.15
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 36 - ".of"
Cohesion: 0.18
Nodes (4): FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 37 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.05
Nodes (15): BackBanner, CameraAt, CityView, FigureStrike, RequestBaseMap, RequestFactionInfo, SetSetting, TalkChoice (+7 more)

### Community 39 - "ConquestWorldgen"
Cohesion: 0.13
Nodes (3): ConquestContent, ConquestEntities, ConquestWorldgen

### Community 42 - "Feud"
Cohesion: 0.11
Nodes (4): Feud, Lord, Feuds, Front

### Community 43 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.12
Nodes (4): FeudDuel, MusterBars, PoliticsSync, Wars

### Community 44 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.04
Nodes (12): Palette, WallTemplates, CharacterMarkers, Placed, MarkerBlockEntity, MarkerSpec, Build, CASTLE (+4 more)

### Community 45 - "ConquestEvents.java"
Cohesion: 0.05
Nodes (5): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), TickWeave experiment

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.15
Nodes (11): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+3 more)

### Community 48 - "BattleType"
Cohesion: 0.14
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 50 - "Cited Findings"
Cohesion: 0.16
Nodes (6): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, DistantArmies

### Community 51 - "Capture"
Cohesion: 0.10
Nodes (8): Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures, RecruitFiguresTest

### Community 52 - "Faction"
Cohesion: 0.08
Nodes (7): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction, CityBanners, CityGuards, PoiPrecomputer

### Community 53 - "Centre"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 56 - ".rightClick"
Cohesion: 0.30
Nodes (3): Entry, Menu, HornOrders

### Community 57 - "SeatCities"
Cohesion: 0.14
Nodes (4): City, Grid, SeatCities, CityIndex

### Community 59 - ".step"
Cohesion: 0.15
Nodes (5): FigureMotion, Match, Placed, State, FigureMotionTest

### Community 61 - ".render"
Cohesion: 0.08
Nodes (10): CharacterScreen, Spot, ClientPeerage, Banner, Portraits, Honour, Honours, Kind (+2 more)

### Community 63 - "TitleMapTest"
Cohesion: 0.17
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - ".enlist"
Cohesion: 0.05
Nodes (10): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT, GeneralTask (+2 more)

### Community 65 - "StyleLibrary"
Cohesion: 0.11
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - ".step"
Cohesion: 0.08
Nodes (19): Cited Findings, Engine, FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, ENGINES (+11 more)

### Community 67 - ".choose"
Cohesion: 0.22
Nodes (8): Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY, RUINS, SPARE_COUNTY_VASSAL

### Community 69 - "RafasConquest.java"
Cohesion: 0.04
Nodes (4): Battle map developer web page, Morale, rout and rally, StreetWalks, RafasConquest

### Community 70 - "RecruitsBattle"
Cohesion: 0.03
Nodes (20): Cited Findings, Card, Enlisted, Figure, HornCall, ATTACKING, MOVING, RALLYING (+12 more)

### Community 71 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.08
Nodes (8): Land, Result, SiegeSurvey, Wall, Door, CityGates, Done, SiegeSurveyTest

### Community 72 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.06
Nodes (13): Cited Findings, Cited Findings, Inferences, Arrow, ArrowVolleys, BattleSpotRing, Figure, DistantOcclusion (+5 more)

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.10
Nodes (20): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+12 more)

### Community 76 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 77 - "Quests"
Cohesion: 0.13
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 78 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (8): Sea, CommandTableBlock, GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, Builder, MarkerBlock

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "CityLayout"
Cohesion: 0.09
Nodes (5): CityLayout, District, Site, WorldSettings, CityLayoutTest

### Community 85 - "Roads"
Cohesion: 0.14
Nodes (10): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Plan (+2 more)

### Community 86 - "Calibration"
Cohesion: 0.19
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.10
Nodes (16): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR, Strategy (+8 more)

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - "CityLayout"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 94 - ".place"
Cohesion: 0.12
Nodes (5): Ground, GroundPlan, Land, Land, Land

### Community 95 - ".generate"
Cohesion: 0.06
Nodes (15): Dojo, ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors, Homeland (+7 more)

### Community 96 - "net.minecraft.core.Vec3i"
Cohesion: 0.09
Nodes (3): CountryPlacement, DojoPlacement, FarmPlacement

### Community 100 - ".view"
Cohesion: 0.20
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 103 - "FigureBatches"
Cohesion: 0.08
Nodes (9): Batch, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE, FigureBatches (+1 more)

### Community 105 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.09
Nodes (11): Boards, Entry, View, Grant, LandGrants, Merit, Peer, Peerage (+3 more)

### Community 107 - "ConquestNetwork"
Cohesion: 0.06
Nodes (17): Bark, BaseMapPart, BoardView, ChargeHits, CityClose, ConquestNetwork, GrandStrategyView, MarkerSet (+9 more)

### Community 109 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 110 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 111 - "City"
Cohesion: 0.11
Nodes (12): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+4 more)

### Community 112 - "BattleShips"
Cohesion: 0.15
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips, Ride

### Community 113 - "WaterSwell"
Cohesion: 0.18
Nodes (6): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, WaterSwell

### Community 115 - "ConquestSavedData"
Cohesion: 0.06
Nodes (7): Cited Findings, ConquestSavedData, MapPreview, Poi, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 116 - "FigureGear"
Cohesion: 0.18
Nodes (7): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 117 - "Cited Findings"
Cohesion: 0.15
Nodes (10): A tick budget and staggered schedules make the fixes stick, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom?, Inferences (+2 more)

### Community 118 - "PoiPrecomputer.java"
Cohesion: 0.06
Nodes (13): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoliticalMap (+5 more)

### Community 121 - "MapLayer"
Cohesion: 0.16
Nodes (6): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer, UnitLayer

### Community 123 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 125 - "Rank"
Cohesion: 0.10
Nodes (12): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+4 more)

### Community 128 - "BattleShips"
Cohesion: 0.21
Nodes (4): BattleShips, Helm, Ram, SableLink

### Community 129 - "Kit"
Cohesion: 0.11
Nodes (19): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+11 more)

### Community 132 - "BattleSounds"
Cohesion: 0.25
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 133 - "MapContext"
Cohesion: 0.19
Nodes (3): MapContext, PoiIconLayer, LabelShape

### Community 134 - "NobleTitle"
Cohesion: 0.10
Nodes (7): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageSync, PeerageTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.07
Nodes (26): 10. Overall ranking, and areas checked that look fine, 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings (+18 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings (+28 more)

### Community 142 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 146 - "net.minecraft.core.Vec3i"
Cohesion: 0.11
Nodes (8): Gold church, Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter, StylePiece, GoldChurch, Layout

### Community 148 - "Job"
Cohesion: 0.12
Nodes (7): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Ranks

### Community 149 - ".place"
Cohesion: 0.14
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 152 - ".plan"
Cohesion: 0.28
Nodes (4): Link, Place, Spur, RoadsTest

### Community 156 - ".plan"
Cohesion: 0.16
Nodes (4): Faith, Bond, RealmPlanner, Seat

### Community 160 - ".rout"
Cohesion: 0.12
Nodes (13): 1. Flank and rear shock, 2. Nearby rout (fright), 3. Army-wide losses, 4. Shattering, Design: adding the four morale shocks (built 2026-11-11, untested in game), Tests (FieldBattleTest), The core change: morale becomes a sum, Morale (+5 more)

### Community 163 - "WarPlan"
Cohesion: 0.15
Nodes (6): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, WarPlan

### Community 164 - "Machine"
Cohesion: 0.08
Nodes (16): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+8 more)

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.29
Nodes (3): Armoury, Outfit, Style

### Community 174 - "MapView"
Cohesion: 0.25
Nodes (4): Cited Findings, Hit, LabelLayer, MapView

### Community 178 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.20
Nodes (6): Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Takeaway, Api, DistantGeneration

### Community 186 - "SoldierFigure"
Cohesion: 0.07
Nodes (27): Corners, Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL (+19 more)

### Community 205 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 217 - ".pack"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 218 - "org.spongepowered.asm.mixin.injection.Inject"
Cohesion: 0.13
Nodes (11): Battle code costs 0.2 ms; the multi-second freezes come from blocking, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, Cited Findings, Gaps, Inferences, Takeaway, CityVegetationMixin, ClientChunkWindowMixin (+3 more)

### Community 219 - "Duty"
Cohesion: 0.13
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 221 - "Front"
Cohesion: 0.09
Nodes (3): Face, Front, Rows

### Community 223 - ".hash"
Cohesion: 0.11
Nodes (6): ChunkCoord, FarmLayout, NobleAnnex, OriginRoller, Roll, NobleAnnexTest

### Community 224 - "RecruitFigures.java"
Cohesion: 0.05
Nodes (4): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), DistantDepth

### Community 225 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 231 - ".model"
Cohesion: 0.12
Nodes (9): Cited Findings, Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN (+1 more)

### Community 236 - "Job"
Cohesion: 0.15
Nodes (9): Enlisted players, Jobs (cavalry, footman, archer, shieldman, heavy infantry), The war horn, Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY (+1 more)

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.11
Nodes (3): DistantArmyRenderer, Hull, Rects

### Community 249 - "BlockPos"
Cohesion: 0.14
Nodes (4): BoardAction, GateSet, GateView, MarkerView

### Community 252 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - ".passOnOrders"
Cohesion: 0.04
Nodes (3): Cited Findings, Landing, Live

### Community 267 - "GateBlockEntity"
Cohesion: 0.10
Nodes (9): GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING, GateGeometry, Hinge (+1 more)

### Community 274 - "Lands"
Cohesion: 0.06
Nodes (15): Cited Findings, Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Fort, Lands (+7 more)

### Community 275 - "CourtView"
Cohesion: 0.08
Nodes (7): Retainer, Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 277 - "Realm"
Cohesion: 0.10
Nodes (6): Hook, Realm, Succession, Taking, RealmPlannerTest, World

### Community 288 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.08
Nodes (7): BattleSnapshot, BattleSpot, FactionInfoReply, UnitInfo, UnitsRequest, UnitsState, BattleSnapshotGearTest

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "BattlePlan.java"
Cohesion: 0.05
Nodes (40): Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Character page, Characters (holders, skills, prowess, traits), Command Table block (+32 more)

### Community 292 - "How Total War calculates battles between units"
Cohesion: 0.17
Nodes (11): 1. Combat is per soldier, not per unit, 2. Does the swing land?, 3. How much damage the hit does, 4. Charges, 5. Missiles, 6. Leadership (morale): what actually wins battles, 7. Autoresolve, 8. Older games (Shogun 1, Medieval 1, Rome 1) (+3 more)

### Community 295 - "Plan"
Cohesion: 0.23
Nodes (6): Engine, LADDERS, RAMS, TOWERS, Plan, WallTower

### Community 300 - ".order"
Cohesion: 0.25
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

### Community 301 - "ShipHulls"
Cohesion: 0.42
Nodes (3): BattleKind, Part, ShipHulls

### Community 303 - ".postChunk"
Cohesion: 0.14
Nodes (3): BarracksPeople, ConquestServer, StructureLink

### Community 306 - "Clash"
Cohesion: 0.28
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 309 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 310 - "ConquestWorldScreen"
Cohesion: 0.12
Nodes (5): BattleSite.Water, ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, SettingsScreen

### Community 311 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.07
Nodes (17): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway, Cited Findings, Gaps, Inferences (+9 more)

### Community 322 - ".build"
Cohesion: 0.12
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 324 - "Recruiter"
Cohesion: 0.08
Nodes (4): WorldMapItem, Barks, Ear, Recruiter

### Community 327 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 329 - ".findGenerationPoint"
Cohesion: 0.11
Nodes (3): SlopeTile, Ground, CitySlopeStructure

### Community 334 - "Where are full scans, nested loops, repeated lookups and per-tick allocations?"
Cohesion: 0.33
Nodes (4): Gaps, Inferences, Takeaway, Where are full scans, nested loops, repeated lookups and per-tick allocations?

### Community 344 - "ShipKind"
Cohesion: 0.33
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 347 - "Piece"
Cohesion: 0.13
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 349 - "net.minecraft.core.BlockPos"
Cohesion: 0.13
Nodes (4): PlannedTent, Saved, WarCamp, PlaceMusic

### Community 351 - "Type"
Cohesion: 0.33
Nodes (4): Type, CLEAR, DELIVER, SCOUT

### Community 352 - "Cited Findings"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?

### Community 353 - "Kind"
Cohesion: 0.50
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 354 - "State"
Cohesion: 0.50
Nodes (4): State, CARRIED, RAISING, UP

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **414 isolated node(s):** `LADDERS`, `TOWERS`, `RAMS`, `OUT`, `WALL` (+409 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1345 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **220 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `net.minecraft.world.level.ChunkPos`, `.army`, `.passOnOrders`, `SiegeWorks`, `UnitOrder`, `arraylist`, `PveCommands.java`, `WarCouncil`, `PveCommands Registration`, `net.minecraft.world.item.ItemStack`, `rafasconquest.faction.Faction`, `net.minecraft.server.MinecraftServer`, `ConquestEvents.java`, `net.minecraft.server.level.ServerPlayer`, `org.jetbrains.annotations.Nullable`, `.enlist`, `RafasConquest.java`, `net.minecraft.server.level.ServerLevel`, `Phase`, `.report`, `net.minecraft.core.BlockPos`, `.generate`, `ClientEvents`, `net.neoforged.bus.api.SubscribeEvent`, `UnitMode`, `.siege`, `Shape`?**
  _High betweenness centrality (0.049) - this node is a cross-community bridge._
- **What connects `LADDERS`, `TOWERS`, `RAMS` to the rest of the system?**
  _414 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.04876926616057051 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `.issue`, `MapContext`, `.onLevelLoad`, `arraylist`, `net.minecraft.client.gui.GuiGraphics`, `CourtView`, `Realm`, `.place`, `.plan`, `WarCouncil`, `net.minecraft.server.MinecraftServer`, `.postChunk`, `Faction`, `Centre`, `SeatCities`, `.render`, `.borders`, `Terrain`, `.generate`, `ClientPolitics`, `CityLayout.java`, `ClientMapCache`, `.model`, `net.minecraft.server.level.ServerPlayer`, `City`, `ConquestSavedData`, `Cited Findings`, `PoiPrecomputer.java`, `GrandStrategyScreen`, `OrganizationScreen`?**
  _High betweenness centrality (0.048) - this node is a cross-community bridge._
- **Should `Townsperson` be split into smaller, more focused modules?**
  _Cohesion score 0.10420168067226891 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._