# Graph Report - RafasConquest  (2026-11-13)

## Corpus Check
- 511 files · ~458,414 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7320 nodes · 23400 edges · 374 communities (143 shown, 231 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3150 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `fa70878e`
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
- Lands
- net.minecraft.server.level.ServerPlayer
- .findGenerationPoint
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- Recruiter
- BaseMap
- UnitOrder
- PoliticsTest
- ConquestSavedData
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.GuiGraphics
- PveCommands.java
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- RafasConquest.java
- HorizontalStretch
- CityManagerScreen
- .plan
- org.spongepowered.asm.mixin.Mixin
- CityVariant
- WarCouncil
- GatePlanksBlock.java
- .view
- TechTreeScreen
- CapeCurio.java
- PveCommands Registration
- School
- CityPlan
- PoiPrecomputer
- ChunkMapCameraMixin.java
- Arm
- .map
- rafasconquest.faction.Faction
- net.minecraft.client.renderer.MultiBufferSource
- Lands
- RecruitFigures
- .enlist
- FieldArmy
- Personalities
- SeaLanes
- ConquestServer
- Cited Findings
- BattlePlan
- PoiPrecomputer.java
- .plan
- Barracks
- .roadStart
- CityStyles
- SeatCities
- Faction Map View
- .step
- ClientScreens
- net.minecraft.core.Vec3i
- TitleMapTest
- Bodies
- StyleLibrary
- .step
- .choose
- BattleMap.java
- Row
- Kit
- DistantShips
- org.junit.jupiter.api.Test
- MapLands
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- NobleAnnex
- .board
- ClientEvents
- org.jetbrains.annotations.Nullable
- net.minecraft.core.Direction
- Ownership & Territory
- CityLayout
- Politics
- Land
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- Faction
- .postChunk
- FieldBattle
- Terrain
- PlaceMusic.java
- WarCamp
- FactionInfoScreen
- Point
- CityTerrainPiece
- Cloth
- FigureBatches
- .view
- .view
- ClothRenderer
- Override
- .of
- CourtSet
- NobleTitle
- Cited Findings
- WaterSwell
- SiegeWorks
- .generate
- WallTemplates
- Cited Findings
- .build
- GrandStrategyScreen
- CityTerrain
- WorldSettings
- .accept
- City
- com.mojang.blaze3d.vertex.PoseStack
- Settlement
- BattleShips
- GrappleHook
- .render
- Roads
- .findGenerationPoint
- SeaCalm
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .onLevelLoad
- net.minecraft.world.level.block.Rotation
- .build
- net.minecraft.resources.ResourceLocation
- MapLayer
- RoadNetwork
- CLAUDE.md
- War
- .get
- .touches
- RoadNetwork
- Machine
- FigureGear
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- RecruitsBattle
- net.minecraft.world.item.DyeColor
- BoardScreen
- SoldierFigure
- Build
- SeatCitiesTest
- .noble
- Duty
- ConquestNetwork
- .view
- OrganizationScreen
- RecruitFigures.java
- Cited Findings
- CityVegetationMixin.java
- .render
- .onRegisterClientCommands
- Townsperson
- net.neoforged.bus.api.SubscribeEvent
- GateScreen
- Piece
- DistantArmyRenderer
- BattleSounds
- Body
- ConquestGameRules
- .boundsAt
- GateBlockEntity
- .officers
- Politics.Front
- Ride
- .plan
- Phase
- Profiling run sheet (baseline)
- .measure
- net.minecraft.sounds.SoundEvent
- SableSeen
- .offTheBack
- BattleKeepInventory
- ClientPolitics
- rafasconquest.core.WorldSettings
- CourtView
- .fire
- Realm
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- CityLayout
- .nationOf
- BackBanners
- Kind
- ItemFlags.java
- How Total War calculates battles between units
- Wind.java
- UnitMode
- net.minecraft.server.level.ServerLevel
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- .sendToServer
- .render
- Corners
- Debug
- 4. Depth buffers and post effects (DH depth, occlusion, GPU sync)
- Call
- 6. Sodium (and Iris) compatibility
- .borders
- CityManager
- Override
- .at
- Ranks
- arraylist
- ClientBarks
- assertequals
- .rout
- Characters (holders, skills, prowess, traits)
- ShareLiveTest
- Kind
- WarHorn
- .block
- ArmouryTest
- .siege
- Registration
- .groundCoversEveryTent
- net.neoforged.fml.common.EventBusSubscriber
- .order

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 299 edges
2. `FieldBlock` - 222 edges
3. `FieldBattle` - 212 edges
4. `Point` - 153 edges
5. `FieldArmy` - 127 edges
6. `ConquestNetwork` - 119 edges
7. `Lands` - 89 edges
8. `Siege` - 84 edges
9. `BattleScreen` - 82 edges
10. `Politics` - 79 edges

## Surprising Connections (you probably didn't know these)
- `Cited Findings` --references--> `ChunkMapAccess`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/mixin/ChunkMapAccess.java
- `Cited Findings` --references--> `RecruitsArrowCleanerMixin`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/mixin/RecruitsArrowCleanerMixin.java
- `Inferences` --references--> `TrackedEntityCameraMixin`  [INFERRED]
  research_notes/RafasConquest performance gameplan/client_techniques.md → src/main/java/rafasconquest/mixin/TrackedEntityCameraMixin.java
- `Cited Findings` --references--> `TrackedEntityCameraMixin`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/mixin/TrackedEntityCameraMixin.java
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

## Communities (374 total, 231 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.05
Nodes (15): BlockGrid, Volley, FieldBlock, ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN (+7 more)

### Community 1 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.05
Nodes (9): BattleBannerBlock, BattleWallBannerBlock, CommandTableBlock, GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, Builder (+1 more)

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.06
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 4 - "MapContext"
Cohesion: 0.15
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.06
Nodes (9): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+1 more)

### Community 7 - "CityGridBuilder"
Cohesion: 0.13
Nodes (4): CityGridBuilder, Ground, PlacedWall, Platform

### Community 8 - "Lands"
Cohesion: 0.10
Nodes (4): Cited Findings, Lands, Stronghold, Claim

### Community 9 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.08
Nodes (12): Cited Findings, Camera, CameraView, Card, Enlisted, Shape, LINE, SQUARE (+4 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "Recruiter"
Cohesion: 0.08
Nodes (4): WorldMapItem, Barks, Ear, Recruiter

### Community 13 - "BaseMap"
Cohesion: 0.07
Nodes (11): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, MapPreview, BaseMap, Nation (+3 more)

### Community 14 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 16 - "ConquestSavedData"
Cohesion: 0.11
Nodes (4): Cited Findings, ConquestSavedData, ConquestSavedDataTest, FactionInfoTest

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.15
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 20 - "PveCommands.java"
Cohesion: 0.10
Nodes (30): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+22 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.24
Nodes (5): ClientChunkWindowMixin, DhShaderLoaderMixin, DhTerrainShaderMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 27 - "CityVariant"
Cohesion: 0.15
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 28 - "WarCouncil"
Cohesion: 0.17
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 30 - ".view"
Cohesion: 0.11
Nodes (12): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+4 more)

### Community 31 - "TechTreeScreen"
Cohesion: 0.18
Nodes (4): TechTreeScreen, Tech, TechTree, TechTreeTest

### Community 32 - "CapeCurio.java"
Cohesion: 0.10
Nodes (4): BackBannerLayer, CapeCurio, Carried, MouthLayer

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.15
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 36 - "PoiPrecomputer"
Cohesion: 0.12
Nodes (3): Poi, CityBanners, PoiPrecomputer

### Community 38 - "Arm"
Cohesion: 0.08
Nodes (15): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+7 more)

### Community 39 - ".map"
Cohesion: 0.10
Nodes (8): Clash, Kind, DUEL, SKIRMISH, FeudDuel, MusterBars, PoliticsSync, Wars

### Community 41 - "net.minecraft.client.renderer.MultiBufferSource"
Cohesion: 0.15
Nodes (3): BattleBannerRenderer, GateRenderer, MarkerRenderer

### Community 42 - "Lands"
Cohesion: 0.13
Nodes (4): Feud, Lord, Feuds, Front

### Community 43 - "RecruitFigures"
Cohesion: 0.10
Nodes (8): Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures, RecruitFiguresTest

### Community 44 - ".enlist"
Cohesion: 0.08
Nodes (3): Outcome, GeneralTask, TickTimes

### Community 45 - "FieldArmy"
Cohesion: 0.07
Nodes (3): FieldArmy, Event, Order

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.14
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "ConquestServer"
Cohesion: 0.16
Nodes (3): ConquestServer, StructureLink, EconomyTick

### Community 50 - "Cited Findings"
Cohesion: 0.12
Nodes (8): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, DistantArmies, Throw, ThrownFigures

### Community 52 - "PoiPrecomputer.java"
Cohesion: 0.08
Nodes (7): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, PoliticalMap, FactionInfo

### Community 53 - ".plan"
Cohesion: 0.09
Nodes (8): Centre, ProvinceMap, Terrain, Faith, Bond, RealmPlanner, Seat, ProvinceMapTest

### Community 57 - "SeatCities"
Cohesion: 0.14
Nodes (3): City, SeatCities, CityIndex

### Community 58 - "Faction Map View"
Cohesion: 0.16
Nodes (3): Cited Findings, FactionMapScreen, MapView

### Community 59 - ".step"
Cohesion: 0.12
Nodes (6): FigureMotion, Ground, Match, Placed, State, FigureMotionTest

### Community 63 - "TitleMapTest"
Cohesion: 0.17
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.08
Nodes (6): Bodies, Kind, FOOT, HORSE, BodiesTest, FieldBattleBodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.10
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - ".step"
Cohesion: 0.08
Nodes (19): Cited Findings, Engine, FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, ENGINES (+11 more)

### Community 67 - ".choose"
Cohesion: 0.11
Nodes (11): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+3 more)

### Community 71 - "Kit"
Cohesion: 0.11
Nodes (19): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+11 more)

### Community 72 - "DistantShips"
Cohesion: 0.19
Nodes (3): DistantShips, Far, Riding

### Community 73 - "org.junit.jupiter.api.Test"
Cohesion: 0.06
Nodes (9): BattlePlanTest, SiegeSurveyTest, DistantOcclusionTest, GrandStrategyDateTest, CitySizesTest, NobleAnnexTest, StreetWalksTest, DialogueTest (+1 more)

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.05
Nodes (34): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+26 more)

### Community 77 - ".board"
Cohesion: 0.11
Nodes (13): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Type, CLEAR (+5 more)

### Community 79 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.05
Nodes (6): Ambushes from woods, Battle phases and days, Envelopment, Deploying to the land (GroundPlan), War camps, War council

### Community 81 - "net.minecraft.core.Direction"
Cohesion: 0.09
Nodes (6): Land, Result, SiegeSurvey, Wall, CityGates, Done

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "CityLayout"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 84 - "Politics"
Cohesion: 0.09
Nodes (11): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Event, Muster, CEREMONIAL (+3 more)

### Community 85 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 86 - "Calibration"
Cohesion: 0.11
Nodes (12): BattleSite, Part, Water, Calibration, Fit, Striker, ARCHER_MELEE, ARROWS (+4 more)

### Community 87 - "Doctrine"
Cohesion: 0.06
Nodes (25): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender, Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT (+17 more)

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - "Faction"
Cohesion: 0.08
Nodes (10): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction, ResourceType, FOOD, PoiType, CAPITAL (+2 more)

### Community 94 - "FieldBattle"
Cohesion: 0.05
Nodes (4): FieldBattle, Target, Rates, WarPlan

### Community 96 - "PlaceMusic.java"
Cohesion: 0.04
Nodes (11): Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, ConquestContent, GrappleHooks, FactionBannerNbt, ConquestEntities, ConquestWorldgen, CountryPlacement (+3 more)

### Community 100 - "Point"
Cohesion: 0.04
Nodes (20): Point, Ground, GroundPlan, Engine, LADDERS, RAMS, TOWERS, Face (+12 more)

### Community 102 - "Cloth"
Cohesion: 0.13
Nodes (4): Cloth, Solid, ClothTest, WoolTest

### Community 105 - ".view"
Cohesion: 0.10
Nodes (9): Boards, Entry, View, Merit, Peer, Peerage, Taken, Quest (+1 more)

### Community 106 - ".view"
Cohesion: 0.25
Nodes (4): Option, Talk, View, WarCouncilPagesTest

### Community 107 - "ClothRenderer"
Cohesion: 0.27
Nodes (5): ClothRenderer, Fabric, Kind, Layer, Live

### Community 109 - ".of"
Cohesion: 0.20
Nodes (3): FactionBanner, Layer, FactionBannerTest

### Community 110 - "CourtSet"
Cohesion: 0.25
Nodes (3): CourtSet, Seat, Style

### Community 111 - "NobleTitle"
Cohesion: 0.14
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageSync

### Community 112 - "Cited Findings"
Cohesion: 0.18
Nodes (8): Battle code costs 0.2 ms; the multi-second freezes come from blocking, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, Cited Findings, Gaps, Inferences, Takeaway, ChunkMapAccess, RecruitsArrowCleanerMixin

### Community 113 - "WaterSwell"
Cohesion: 0.17
Nodes (7): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Made, WaterSwell

### Community 114 - "SiegeWorks"
Cohesion: 0.09
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 115 - ".generate"
Cohesion: 0.05
Nodes (22): ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors, Homeland, DESERT (+14 more)

### Community 117 - "Cited Findings"
Cohesion: 0.17
Nodes (11): A tick budget and staggered schedules make the fixes stick, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom?, Gaps (+3 more)

### Community 121 - "CityTerrain"
Cohesion: 0.07
Nodes (17): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+9 more)

### Community 122 - "WorldSettings"
Cohesion: 0.10
Nodes (10): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, WorldSettings (+2 more)

### Community 124 - "City"
Cohesion: 0.17
Nodes (5): Countryside, Plan, Grid, CountrysideTest, World

### Community 126 - "Settlement"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 128 - "BattleShips"
Cohesion: 0.13
Nodes (7): Hull, Ramming, BattleShips, Helm, Ram, SableLink, RammingTest

### Community 130 - ".render"
Cohesion: 0.23
Nodes (3): Hit, LabelLayer, LabelShape

### Community 132 - "Roads"
Cohesion: 0.16
Nodes (9): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Plan (+1 more)

### Community 134 - "SeaCalm"
Cohesion: 0.19
Nodes (5): Sea, Depths, SeaCalm, Strengths, SeaCalmTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (24): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings, Cited Findings (+16 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.06
Nodes (31): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings, Cited Findings (+23 more)

### Community 142 - "net.minecraft.world.level.block.Rotation"
Cohesion: 0.14
Nodes (10): Battles (prototype), Blocks of 20 soldiers, Gold church, Land grants (seven ways), Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter (+2 more)

### Community 148 - "MapLayer"
Cohesion: 0.15
Nodes (6): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer, UnitLayer

### Community 152 - "War"
Cohesion: 0.09
Nodes (3): Army, Goal, War

### Community 155 - ".get"
Cohesion: 0.09
Nodes (8): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Ranks, Advancement

### Community 160 - "RoadNetwork"
Cohesion: 0.14
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 163 - "Machine"
Cohesion: 0.07
Nodes (17): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Machine, ARCBALLISTA, BATTERING_RAM (+9 more)

### Community 164 - "FigureGear"
Cohesion: 0.23
Nodes (7): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.16
Nodes (4): Armoury, Outfit, Style, RankKits

### Community 174 - "RecruitsBattle"
Cohesion: 0.03
Nodes (18): Cited Findings, Cited Findings, Figure, HornCall, ATTACKING, MOVING, RALLYING, RETREATING (+10 more)

### Community 178 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.33
Nodes (3): BattleKind, Part, ShipHulls

### Community 186 - "SoldierFigure"
Cohesion: 0.19
Nodes (8): Boxes, Palette, Shape, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 204 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 206 - ".noble"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 217 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 218 - "ConquestNetwork"
Cohesion: 0.02
Nodes (45): BackBanner, Bark, BaseMapPart, BattleSnapshot, BattleSpot, BoardAction, BoardView, CameraAt (+37 more)

### Community 219 - ".view"
Cohesion: 0.24
Nodes (7): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, GrandStrategy, View

### Community 224 - "RecruitFigures.java"
Cohesion: 0.04
Nodes (4): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), DistantOcclusion

### Community 225 - "Cited Findings"
Cohesion: 0.07
Nodes (10): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, DistantDepth, Arrow, ArrowVolleys (+2 more)

### Community 228 - ".render"
Cohesion: 0.09
Nodes (7): CharacterScreen, Spot, ClientPeerage, Banner, Portraits, Honour, Honours

### Community 231 - ".onRegisterClientCommands"
Cohesion: 0.25
Nodes (4): Mode, OFF, ON, VIEW

### Community 232 - "Townsperson"
Cohesion: 0.14
Nodes (4): Walk, DistrictKey, Townsfolk, Townsperson

### Community 236 - "Piece"
Cohesion: 0.13
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.12
Nodes (4): Cited Findings, DistantArmyRenderer, Figure, Rects

### Community 240 - "BattleSounds"
Cohesion: 0.25
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 242 - "Body"
Cohesion: 0.11
Nodes (18): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+10 more)

### Community 248 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 249 - "GateBlockEntity"
Cohesion: 0.06
Nodes (10): GateDoors, BattleBannerBlockEntity, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN (+2 more)

### Community 252 - ".officers"
Cohesion: 0.09
Nodes (14): GameCharacter, Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN (+6 more)

### Community 256 - ".plan"
Cohesion: 0.21
Nodes (5): Link, Place, Roads, Spur, RoadsTest

### Community 257 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 275 - "CourtView"
Cohesion: 0.08
Nodes (7): Retainer, Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 276 - ".fire"
Cohesion: 0.07
Nodes (7): Saved, GateGeometry, CharacterMarkers, Placed, MarkerBlockEntity, MarkerSpec, GateGeometryTest

### Community 277 - "Realm"
Cohesion: 0.08
Nodes (9): Hook, Kind, FRIEND, RIVAL, Realm, Succession, Taking, RealmPlannerTest (+1 more)

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.14
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips, Ride

### Community 282 - "CityLayout"
Cohesion: 0.09
Nodes (4): District, Site, CityStructure, CityLayoutTest

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 292 - "How Total War calculates battles between units"
Cohesion: 0.17
Nodes (11): 1. Combat is per soldier, not per unit, 2. Does the swing land?, 3. How much damage the hit does, 4. Charges, 5. Missiles, 6. Leadership (morale): what actually wins battles, 7. Autoresolve, 8. Older games (Shogun 1, Medieval 1, Rome 1) (+3 more)

### Community 294 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 295 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.04
Nodes (3): /council <topic>, TickWeave experiment, Officers

### Community 301 - "Corners"
Cohesion: 0.16
Nodes (3): ShipSprite, Corners, FigureBatchesTest

### Community 302 - "Debug"
Cohesion: 0.33
Nodes (6): Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE

### Community 303 - "4. Depth buffers and post effects (DH depth, occlusion, GPU sync)"
Cohesion: 0.40
Nodes (5): 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), Cited Findings, Gaps, Inferences, Takeaway

### Community 305 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 306 - "6. Sodium (and Iris) compatibility"
Cohesion: 0.40
Nodes (5): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway

### Community 310 - "Override"
Cohesion: 0.14
Nodes (4): BattleSite.Water, ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 315 - "Ranks"
Cohesion: 0.13
Nodes (10): Enlisted players, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Battle roles (frontline, bait, support, flanker, kill squad), The war horn, Job, ARCHER, CAVALRY, FOOTMAN (+2 more)

### Community 321 - "assertequals"
Cohesion: 0.06
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 323 - ".rout"
Cohesion: 0.11
Nodes (13): 1. Flank and rear shock, 2. Nearby rout (fright), 3. Army-wide losses, 4. Shattering, Design: adding the four morale shocks (built 2026-11-11, untested in game), Tests (FieldBattleTest), The core change: morale becomes a sum, Morale (+5 more)

### Community 324 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.07
Nodes (29): Base map generated from seed, Character page, Characters (holders, skills, prowess, traits), Command Table block, Countryside (hamlets, villages, watchtowers, castles), Court 3D scene (CourtView), Cultures (seven homelands), Demographics (population by culture and race) (+21 more)

### Community 327 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 330 - ".block"
Cohesion: 0.06
Nodes (6): BattleMap, GroundPlanTest, Land, Land, Land, Land

### Community 364 - ".order"
Cohesion: 0.40
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Roads (high, city, town roads; bridges; signposts)` → `Mustering (offensive and ceremonial)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **414 isolated node(s):** `SOLDIER`, `LEADER`, `GENERAL`, `BEARER`, `GUARD` (+409 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1372 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **231 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `Phase`, `net.minecraft.world.level.ChunkPos`, `.army`, `GrappleHook`, `net.minecraft.server.level.ServerPlayer`, `UnitOrder`, `PoliticsTest`, `PveCommands.java`, `RafasConquest.java`, `WarCouncil`, `PveCommands Registration`, `UnitMode`, `net.minecraft.world.item.ItemStack`, `net.minecraft.server.level.ServerLevel`, `rafasconquest.faction.Faction`, `.map`, `.enlist`, `.onServerTick`, `BattleMap.java`, `.block`, `org.jetbrains.annotations.Nullable`, `.siege`, `net.minecraft.core.Direction`, `ConquestNetwork`, `PlaceMusic.java`, `WarCamp`, `SiegeWorks`, `.generate`, `.officers`?**
  _High betweenness centrality (0.060) - this node is a cross-community bridge._
- **What connects `SOLDIER`, `LEADER`, `GENERAL` to the rest of the system?**
  _414 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.050724637681159424 - nodes in this community are weakly interconnected._
- **Why does `FieldBattle` connect `FieldBattle` to `Bodies`, `FieldBlock`, `PveCommands Registration`, `Machine`, `Point`, `.army`, `.rout`, `.block`, `.enlist`, `FieldArmy`, `RecruitsBattle`, `.siege`, `Call`, `PveCommands.java`, `RafasConquest.java`, `arraylist`?**
  _High betweenness centrality (0.052) - this node is a cross-community bridge._
- **Should `net.minecraft.world.level.block.state.BlockState` be split into smaller, more focused modules?**
  _Cohesion score 0.04869148159733083 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Roads (high, city, town roads; bridges; signposts)` and `Mustering (offensive and ceremonial)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._