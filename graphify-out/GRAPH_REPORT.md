# Graph Report - RafasConquest  (2026-11-12)

## Corpus Check
- 508 files · ~454,713 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7290 nodes · 23266 edges · 369 communities (149 shown, 220 thin omitted)
- Extraction: 86% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3139 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `96744faa`
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
- RafasConquest.java
- Lands
- com.mojang.blaze3d.vertex.PoseStack
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- RecruitsBattle
- BaseMap
- UnitOrder
- PoliticsTest
- .begin
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.GuiGraphics
- PveCommands.java
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- RecruitsBattle.java
- HorizontalStretch
- CityManagerScreen
- .plan
- org.spongepowered.asm.mixin.Mixin
- .shift
- WarCouncil
- .view
- net.minecraft.network.FriendlyByteBuf
- .findGenerationPoint
- PveCommands Registration
- School
- CityPlan
- .render
- .send
- Quest
- net.minecraft.server.MinecraftServer
- rafasconquest.faction.Faction
- Faction
- Lands
- Capture
- net.minecraft.nbt.CompoundTag
- ConquestEvents.java
- Personalities
- SeaLanes
- .broadcast
- rafasconquest.battle.FormationSnapshot
- RecruitFigures
- PoiPrecomputer.java
- Centre
- Barracks
- Wind.java
- CityStyles
- City
- Faction Map View
- .step
- ClientScreens
- Peerage
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
- Phase
- .board
- Swell
- net.minecraft.client.renderer.MultiBufferSource
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
- CityLayout
- com.mojang.blaze3d.vertex.PoseStack
- .enlist
- .generate
- net.minecraft.world.level.block.Rotation
- .place
- FactionInfoScreen
- Gate
- CityTerrainPiece
- Cloth
- FigureBatches
- NobleTitle
- net.neoforged.bus.api.SubscribeEvent
- .roadStart
- Override
- net.minecraft.sounds.SoundEvent
- FigureGear
- City
- BattleShips
- WaterSwell
- Arm
- ConquestSavedData
- ConquestNetwork
- Cited Findings
- .findGenerationPoint
- GrandStrategyScreen
- CityTerrain
- CapitalLayout
- net.minecraft.resources.ResourceLocation
- Kit
- Rank
- BackBannerLayer.java
- BattleShips
- GrappleHook
- MapView
- Roads
- .moraleOf
- SeaCalm
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .onLevelLoad
- Homeland
- .walks
- .shoot
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- RoadNetwork
- CLAUDE.md
- Plan
- How do world generation and structure placement work, and what do they cost?
- .plan
- ShipHulls
- CityGuards.java
- Machine
- .world
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- .general
- Strategy
- BoardScreen
- SoldierFigure
- Cities (grid, plots, vanilla village buildings)
- Task
- .noble
- Duty
- CapeCurio.java
- Recruiter
- Siege
- HarbourCut
- RecruitFigures.java
- CityPlacement
- Ranks
- .stat
- .deployDefenders
- net.minecraft.resources.ResourceLocation
- ConquestEvents
- .findGenerationPoint
- Kind
- DistantArmyRenderer
- GatePlanksBlock.java
- .touches
- ConquestGameRules
- Distribution
- GateBlockEntity
- Trait
- CourtView
- Call
- Ramming
- GateScreen
- Profiling run sheet (baseline)
- .measure
- .passOnOrders
- ClientModEvents.java
- BlockGrid
- BattleKeepInventory
- ClientPolitics
- War
- NationScreen
- Realm
- Building
- Build
- Kind
- Characters (holders, skills, prowess, traits)
- How Total War calculates battles between units
- SeatCitiesTest
- .hash
- BattleSnapshot
- UnitMode
- Job
- .inCity
- .postChunk
- .bark
- Front
- Ranks (Soldier to Grand Marshal)
- ConquestWorldScreen
- PoliticsData
- arraylist
- Placed
- Where are full scans, nested loops, repeated lookups and per-tick allocations?
- UnitsState
- org.spongepowered.asm.mixin.Mixin
- Kind
- 6. Sodium (and Iris) compatibility
- ShareLiveTest
- ClientBarks
- Networking: what is sent, how big, how often, to whom?
- Cited Findings
- WarCamp
- ShipKind
- .officers
- .offTheBack
- BaseMapPart
- .sendBattleSpot
- Volleys
- WorldStretch
- NobleDistrictTest
- State
- BattleSite.Water
- .ships

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 296 edges
2. `FieldBlock` - 256 edges
3. `FieldBattle` - 222 edges
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
- `Inferences` --references--> `RoadLayer`  [INFERRED]
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

## Communities (369 total, 220 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.03
Nodes (8): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock, Landing

### Community 1 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.05
Nodes (8): BattleBannerBlock, BattleWallBannerBlock, CommandTableBlock, GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, MarkerBlock

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.06
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - ".army"
Cohesion: 0.06
Nodes (5): Point, DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, SiegeTest

### Community 4 - "MapContext"
Cohesion: 0.14
Nodes (5): MapContext, Place, PlaceMenuLayer, PoiIconLayer, LabelShape

### Community 5 - "BattleScreen"
Cohesion: 0.07
Nodes (4): BattleScreen, Entry, Menu, HornOrders

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.05
Nodes (9): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+1 more)

### Community 7 - "CityGridBuilder"
Cohesion: 0.07
Nodes (10): CityGridBuilder, Ground, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA (+2 more)

### Community 8 - "RafasConquest.java"
Cohesion: 0.05
Nodes (4): ConquestContent, GrappleHooks, ConquestEntities, RafasConquest

### Community 9 - "Lands"
Cohesion: 0.07
Nodes (13): Cited Findings, Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Lands, Stronghold (+5 more)

### Community 10 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.19
Nodes (3): BannerArt, ClothItemRenderer, Layer

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "RecruitsBattle"
Cohesion: 0.03
Nodes (24): Cited Findings, Card, Enlisted, Figure, GeneralTask, HornCall, ATTACKING, MOVING (+16 more)

### Community 13 - "BaseMap"
Cohesion: 0.07
Nodes (13): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, ClientMapCache, Junction, MapPreview (+5 more)

### Community 14 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 16 - ".begin"
Cohesion: 0.10
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.14
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 19 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.05
Nodes (7): /council <topic>, Character page, Court 3D scene (CourtView), Demographics (population by culture and race), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), InventoryStatsButton

### Community 20 - "PveCommands.java"
Cohesion: 0.09
Nodes (30): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+22 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.15
Nodes (7): Cited Findings, CityVegetationMixin, ClientChunkWindowMixin, RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin, TrackedEntityCameraMixin

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.16
Nodes (7): Facts, Officer, Said, Talk, WarCouncil, Option, WarCouncilPagesTest

### Community 30 - ".view"
Cohesion: 0.15
Nodes (4): PlayerStats, View, Retinues, RetinuesTest

### Community 31 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.05
Nodes (11): BoardView, FigureStrike, GrandStrategyView, PeerageView, PoliticsView, SetSetting, StatsView, TalkChoice (+3 more)

### Community 34 - "School"
Cohesion: 0.09
Nodes (14): Dojos, Ground, School, BLOOD, FIRE, ICE, IRON, LIGHT (+6 more)

### Community 35 - "CityPlan"
Cohesion: 0.13
Nodes (3): CityPlan, Piece, Plan

### Community 36 - ".render"
Cohesion: 0.14
Nodes (5): CharacterScreen, Retainer, Spot, Banner, Portraits

### Community 37 - ".send"
Cohesion: 0.09
Nodes (8): BackBanner, Builder, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel

### Community 38 - "Quest"
Cohesion: 0.14
Nodes (9): Ladder, MILITARY, NOBLE, NONE, Quest, Type, CLEAR, DELIVER (+1 more)

### Community 39 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.08
Nodes (6): GrandStrategy, View, MusterBars, PoliticsSync, Wars, Politics.Front

### Community 41 - "Faction"
Cohesion: 0.08
Nodes (4): RecruitsBridge, Faction, PoiPrecomputer, FactionInfoTest

### Community 42 - "Lands"
Cohesion: 0.09
Nodes (9): Clash, Feud, Kind, DUEL, SKIRMISH, Lord, FeudDuel, Feuds (+1 more)

### Community 44 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.07
Nodes (4): MarkerRenderer, BattleBannerBlockEntity, MarkerBlockEntity, MarkerSpec

### Community 45 - "ConquestEvents.java"
Cohesion: 0.05
Nodes (5): FactionBannerNbt, ConquestWorldgen, DojoPlacement, FarmPlacement, FarmStructure

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (13): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+5 more)

### Community 50 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.09
Nodes (12): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, BattleSounds, ChargeSound, DinSound (+4 more)

### Community 51 - "RecruitFigures"
Cohesion: 0.23
Nodes (4): Layer, Look, Mesh, RecruitFigures

### Community 52 - "PoiPrecomputer.java"
Cohesion: 0.08
Nodes (14): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoliticalMap (+6 more)

### Community 53 - "Centre"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 57 - "City"
Cohesion: 0.09
Nodes (8): Countryside, Plan, City, Grid, SeatCities, CityIndex, CountrysideTest, World

### Community 58 - "Faction Map View"
Cohesion: 0.14
Nodes (6): 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Gaps, Inferences, Takeaway, FactionMapScreen

### Community 59 - ".step"
Cohesion: 0.12
Nodes (6): FigureMotion, Ground, Match, Placed, State, FigureMotionTest

### Community 61 - "Peerage"
Cohesion: 0.09
Nodes (10): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+2 more)

### Community 63 - "TitleMapTest"
Cohesion: 0.17
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.10
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.11
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - ".step"
Cohesion: 0.07
Nodes (23): Cited Findings, Engine, FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, ENGINES (+15 more)

### Community 67 - ".choose"
Cohesion: 0.17
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 71 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.10
Nodes (7): Land, Result, SiegeSurvey, Wall, CityGates, Done, SiegeSurveyTest

### Community 72 - "Ride"
Cohesion: 0.17
Nodes (4): DistantShips, Far, Riding, Ride

### Community 73 - "BattlePlan"
Cohesion: 0.05
Nodes (9): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, WarPlan (+1 more)

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.10
Nodes (19): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps, Inferences (+11 more)

### Community 76 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 77 - ".board"
Cohesion: 0.11
Nodes (6): DataFiles, Dialogue, Kind, Quests, Target, DialogueTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "CityLayout"
Cohesion: 0.08
Nodes (7): CityLayout, District, Site, ChunkCoord, FarmLayout, WorldSettings, CityLayoutTest

### Community 85 - "Land"
Cohesion: 0.19
Nodes (4): Heap, Land, Search, ShippingRoutes

### Community 86 - "Calibration"
Cohesion: 0.18
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.18
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "AgeTest.java"
Cohesion: 0.12
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - "CityLayout"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 92 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.28
Nodes (4): ClothRenderer, Fabric, Kind, Live

### Community 95 - ".generate"
Cohesion: 0.11
Nodes (6): Dojo, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 96 - "net.minecraft.world.level.block.Rotation"
Cohesion: 0.11
Nodes (3): StylePiece, GoldChurch, Layout

### Community 99 - "FactionInfoScreen"
Cohesion: 0.12
Nodes (5): FactionInfoScreen, Line, FactionBanner, Layer, FactionBannerTest

### Community 102 - "Cloth"
Cohesion: 0.12
Nodes (4): Cloth, Solid, ClothTest, WoolTest

### Community 103 - "FigureBatches"
Cohesion: 0.10
Nodes (9): Batch, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE, FigureBatches (+1 more)

### Community 105 - "NobleTitle"
Cohesion: 0.18
Nodes (5): NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 109 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 110 - "FigureGear"
Cohesion: 0.18
Nodes (7): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 111 - "City"
Cohesion: 0.09
Nodes (10): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+2 more)

### Community 112 - "BattleShips"
Cohesion: 0.20
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 113 - "WaterSwell"
Cohesion: 0.15
Nodes (7): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, WaterSwell, SeaFoamTest

### Community 114 - "Arm"
Cohesion: 0.07
Nodes (16): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+8 more)

### Community 115 - "ConquestSavedData"
Cohesion: 0.07
Nodes (6): Cited Findings, ConquestSavedData, ConquestServer, StructureLink, EconomyTick, ConquestSavedDataTest

### Community 116 - "ConquestNetwork"
Cohesion: 0.08
Nodes (16): Bark, BoardAction, CameraAt, ChargeHits, CityClose, ConquestNetwork, FactionInfoReply, GateSet (+8 more)

### Community 117 - "Cited Findings"
Cohesion: 0.11
Nodes (12): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+4 more)

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.14
Nodes (4): Inferences, GrandStrategyScreen, Spot, GrandStrategyDateTest

### Community 121 - "CityTerrain"
Cohesion: 0.07
Nodes (18): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+10 more)

### Community 122 - "CapitalLayout"
Cohesion: 0.18
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 124 - "Kit"
Cohesion: 0.11
Nodes (19): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+11 more)

### Community 125 - "Rank"
Cohesion: 0.11
Nodes (11): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+3 more)

### Community 128 - "BattleShips"
Cohesion: 0.20
Nodes (4): BattleShips, Helm, Ram, SableLink

### Community 129 - "GrappleHook"
Cohesion: 0.15
Nodes (3): GrappleHookRenderer, Registration, GrappleHook

### Community 130 - "MapView"
Cohesion: 0.08
Nodes (11): Cited Findings, BaseMapLayer, Hit, LabelLayer, LegendEntry, MapLayer, Symbol, MapView (+3 more)

### Community 132 - "Roads"
Cohesion: 0.10
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Link, Place (+6 more)

### Community 133 - ".moraleOf"
Cohesion: 0.16
Nodes (7): 1. Flank and rear shock, 2. Nearby rout (fright), 3. Army-wide losses, 4. Shattering, Design: adding the four morale shocks (built 2026-11-11, untested in game), Tests (FieldBattleTest), The core change: morale becomes a sum

### Community 134 - "SeaCalm"
Cohesion: 0.24
Nodes (3): Sea, Depths, SeaCalm

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (23): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, Cited Findings, Cited Findings, Gaps (+15 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings (+28 more)

### Community 142 - "Homeland"
Cohesion: 0.10
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 148 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.17
Nodes (8): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Enlisted players, The general and guard, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Battle roles (frontline, bait, support, flanker, kill squad), Victory, pursuit and surrender, The war horn

### Community 149 - "RoadNetwork"
Cohesion: 0.15
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 152 - "Plan"
Cohesion: 0.23
Nodes (6): Engine, LADDERS, RAMS, TOWERS, Plan, WallTower

### Community 155 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.20
Nodes (6): Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Takeaway, Api, DistantGeneration

### Community 156 - ".plan"
Cohesion: 0.14
Nodes (5): Centre, Faith, Bond, RealmPlanner, Seat

### Community 157 - "ShipHulls"
Cohesion: 0.38
Nodes (3): BattleKind, Part, ShipHulls

### Community 163 - "Machine"
Cohesion: 0.06
Nodes (21): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Machine, ARCBALLISTA, BATTERING_RAM (+13 more)

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.09
Nodes (8): WarHorn, BattleStandardItem, Armoury, Outfit, Style, ItemFlags, RankKits, ArmouryTest

### Community 174 - ".general"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 178 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 186 - "SoldierFigure"
Cohesion: 0.08
Nodes (26): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+18 more)

### Community 204 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.08
Nodes (21): Ambushes from woods, Base map generated from seed, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Faction Info screen, Deploying to the land (GroundPlan) (+13 more)

### Community 205 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 217 - "Duty"
Cohesion: 0.11
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 221 - "Siege"
Cohesion: 0.14
Nodes (6): GateAction, Region, IN, OUT, WALL, Siege

### Community 223 - "HarbourCut"
Cohesion: 0.12
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 224 - "RecruitFigures.java"
Cohesion: 0.04
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 228 - ".stat"
Cohesion: 0.08
Nodes (14): ClientPeerage, Names, Honour, Honours, Kind, FRIEND, RIVAL, Stat (+6 more)

### Community 232 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.14
Nodes (4): Walk, DistrictKey, Townsfolk, Townsperson

### Community 236 - "Kind"
Cohesion: 0.22
Nodes (7): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.06
Nodes (7): Cited Findings, DistantDepth, DistantArmyRenderer, Figure, Rects, ShipSprite, Corners

### Community 248 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 249 - "GateBlockEntity"
Cohesion: 0.06
Nodes (11): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+3 more)

### Community 252 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 254 - "CourtView"
Cohesion: 0.10
Nodes (5): Camera, CourtView, Figure, OrganizationScreen, Organization

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 274 - "War"
Cohesion: 0.08
Nodes (8): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, Army, Goal, War

### Community 275 - "NationScreen"
Cohesion: 0.14
Nodes (3): NationScreen, Demographics, Share

### Community 277 - "Realm"
Cohesion: 0.10
Nodes (5): Hook, Realm, Succession, Taking, CharacterMarkers

### Community 288 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.12
Nodes (16): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Characters (holders, skills, prowess, traits), Cultures (seven homelands), Envelopment, Epic Knights optional armour (Armoury), Faiths and cults (+8 more)

### Community 292 - "How Total War calculates battles between units"
Cohesion: 0.17
Nodes (11): 1. Combat is per soldier, not per unit, 2. Does the swing land?, 3. How much damage the hit does, 4. Charges, 5. Missiles, 6. Leadership (morale): what actually wins battles, 7. Autoresolve, 8. Older games (Shogun 1, Medieval 1, Rome 1) (+3 more)

### Community 295 - ".hash"
Cohesion: 0.10
Nodes (7): Bounds, DistanceScaling, NobleAnnex, OriginRoller, Roll, ProvinceNames, DistanceScalingTest

### Community 300 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 301 - "Job"
Cohesion: 0.10
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 303 - ".postChunk"
Cohesion: 0.13
Nodes (4): Cited Findings, BarracksPeople, CityBanners, CityGuards

### Community 305 - ".bark"
Cohesion: 0.21
Nodes (4): Barks, Ear, Talk, View

### Community 309 - "Ranks (Soldier to Grand Marshal)"
Cohesion: 0.33
Nodes (6): Gold church, Land grants (seven ways), Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter

### Community 310 - "ConquestWorldScreen"
Cohesion: 0.13
Nodes (4): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, SettingsScreen

### Community 322 - "Where are full scans, nested loops, repeated lookups and per-tick allocations?"
Cohesion: 0.33
Nodes (4): Gaps, Inferences, Takeaway, Where are full scans, nested loops, repeated lookups and per-tick allocations?

### Community 323 - "UnitsState"
Cohesion: 0.17
Nodes (8): Morale, State, RALLIED, ROUTED, SHATTERED, STEADY, UnitInfo, UnitsState

### Community 326 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.09
Nodes (7): Cited Findings, Camera, CameraView, Chunks, ChunkMapAccess, ChunkMapCameraMixin, CityManager

### Community 327 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 328 - "6. Sodium (and Iris) compatibility"
Cohesion: 0.40
Nodes (5): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway

### Community 334 - "Networking: what is sent, how big, how often, to whom?"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Networking: what is sent, how big, how often, to whom?, Takeaway

### Community 339 - "Cited Findings"
Cohesion: 0.07
Nodes (14): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, Arrow, ArrowVolleys, BattleSpotRing (+6 more)

### Community 349 - "WarCamp"
Cohesion: 0.09
Nodes (6): PlannedTent, Saved, WarCamp, CourtSet, Seat, Style

### Community 351 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 361 - "State"
Cohesion: 0.50
Nodes (4): State, CARRIED, RAISING, UP

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **414 isolated node(s):** `NORMAL`, `RED`, `NO_LIGHTMAP`, `NO_SHADER_COLOUR`, `WHITE` (+409 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1369 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **220 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `GrappleHook`, `net.minecraft.world.level.ChunkPos`, `.army`, `.passOnOrders`, `UnitOrder`, `PveCommands.java`, `RecruitsBattle.java`, `WarCouncil`, `PveCommands Registration`, `.send`, `net.minecraft.world.item.ItemStack`, `rafasconquest.faction.Faction`, `net.minecraft.server.MinecraftServer`, `UnitMode`, `ConquestEvents.java`, `.inCity`, `.broadcast`, `Peerage`, `RafasConquest.java`, `org.spongepowered.asm.mixin.Mixin`, `net.minecraft.server.level.ServerLevel`, `Phase`, `Work`, `Calibration`, `Duty`, `WarCamp`, `.enlist`, `.officers`, `ConquestEvents`, `net.neoforged.bus.api.SubscribeEvent`, `Arm`, `Trait`?**
  _High betweenness centrality (0.065) - this node is a cross-community bridge._
- **What connects `NORMAL`, `RED`, `NO_LIGHTMAP` to the rest of the system?**
  _414 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.03440419229892914 - nodes in this community are weakly interconnected._
- **Why does `FieldBattle` connect `FieldBlock` to `Bodies`, `assertequals`, `Machine`, `.army`, `.moraleOf`, `UnitsState`, `BattlePlan`, `RecruitsBattle`, `.broadcast`, `.begin`, `Arm`, `RafasConquest.java`, `PveCommands.java`, `RecruitsBattle.java`, `Siege`, `.enlist`, `Call`?**
  _High betweenness centrality (0.044) - this node is a cross-community bridge._
- **Should `net.minecraft.world.level.block.state.BlockState` be split into smaller, more focused modules?**
  _Cohesion score 0.048450227441024014 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._