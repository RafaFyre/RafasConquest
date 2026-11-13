# Graph Report - RafasConquest  (2026-11-12)

## Corpus Check
- 511 files · ~456,246 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7305 nodes · 23338 edges · 362 communities (153 shown, 209 thin omitted)
- Extraction: 86% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3150 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `8e7cddac`
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
- CityLayoutTest
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- net.minecraft.world.level.Level
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
- MouthLayer.java
- PveCommands Registration
- School
- CityPlan
- ConquestServer
- net.minecraft.world.phys.Vec3
- Arm
- .map
- rafasconquest.faction.Faction
- .feudFight
- Lands
- Capture
- MarkerSpec
- net.minecraft.world.entity.Entity
- Personalities
- SeaLanes
- net.minecraft.server.level.ServerPlayer
- rafasconquest.battle.FormationSnapshot
- Rank
- PoiPrecomputer.java
- Centre
- Barracks
- Body
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
- RafasConquest.java
- Row
- net.minecraft.server.level.ServerLevel
- Ride
- BattlePlan
- MapLands
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Ride
- .board
- ClientEvents
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
- .block
- .generate
- ConquestWorldgen
- .inBox
- FactionInfoScreen
- rafasconquest.battle.BattlePlan.Point
- CityTerrainPiece
- Cloth
- FigureBatches
- Peerage
- .onLogout
- .moraleOf
- Override
- net.minecraft.sounds.SoundEvent
- FigureGear
- City
- .inCity
- WaterSwell
- SiegeWorks
- ConquestSavedData
- .of
- Cited Findings
- .build
- GrandStrategyScreen
- CityTerrain
- WorldSettings
- PoliticalStyle
- .view
- NobleTitle
- BattleSounds
- BattleShips
- GrappleHook
- MapView
- Roads
- CityVariant
- SeaCalm
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .onLevelLoad
- Homeland
- com.mojang.blaze3d.vertex.PoseStack
- net.minecraft.resources.ResourceLocation
- MapLayer
- RoadNetwork
- CLAUDE.md
- Override
- org.joml.Matrix4f
- .plan
- ShipHulls
- .sail
- TechTreeScreen
- .render
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- How do world generation and structure placement work, and what do they cost?
- GoldChurch.java
- BoardScreen
- SoldierFigure
- Recruiter
- Organizations
- .noble
- Duty
- .view
- net.minecraft.world.level.block.Block
- OrganizationScreen
- .findGenerationPoint
- RecruitFigures.java
- DistantOcclusion
- .readPlacements
- .render
- BattleSnapshot
- Townsperson
- ConquestEvents
- Capture
- Kind
- DistantArmyRenderer
- GatePlanksBlock.java
- WallTemplates
- ConquestGameRules
- .boundsAt
- net.minecraft.core.BlockPos
- Trait
- .bark
- RafasConquest (PvE addon for Villager Recruits)
- BattleRole
- Profiling run sheet (baseline)
- .measure
- RecruitsBattle
- ClothItemRenderer
- .touches
- BattleKeepInventory
- ClientPolitics
- CourtView
- Placed
- Realm
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound
- Outcome
- Build
- Kind
- WarCamp.java
- How Total War calculates battles between units
- Role
- .embody
- Task
- .nearest
- .named
- .postChunk
- Call
- Shape
- .rightClick
- ConquestWorldScreen
- UnitMode
- Ranks
- arraylist
- ClientBarks
- assertequals
- .walks
- State
- com.talhanation.recruits.entities.AbstractRecruitEntity
- CityManager
- Kind
- ProvinceNames
- ShareLiveTest
- Cited Findings
- WarCamp
- State
- net.neoforged.fml.common.EventBusSubscriber
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

## Communities (362 total, 209 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.04
Nodes (7): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 1 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.07
Nodes (5): Saved, BattleBannerBlock, BattleWallBannerBlock, GateBlock, MarkerBlock

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.06
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - ".army"
Cohesion: 0.07
Nodes (4): DoctrineTest, FieldBattleTest, SiegeTest, GrandStrategyDateTest

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.07
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "CityGridBuilder"
Cohesion: 0.11
Nodes (3): CityGridBuilder, Ground, Platform

### Community 8 - "Politics"
Cohesion: 0.08
Nodes (13): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Goal, Event, Muster (+5 more)

### Community 9 - "Lands"
Cohesion: 0.11
Nodes (4): Cited Findings, Lands, Stronghold, Claim

### Community 10 - "CityLayoutTest"
Cohesion: 0.10
Nodes (4): District, CityStructure, CityLayoutTest, CitySizesTest

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "net.minecraft.world.level.Level"
Cohesion: 0.08
Nodes (6): Wind, CommandTableBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, WorldMapItem

### Community 13 - "BaseMap"
Cohesion: 0.11
Nodes (10): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, MapPreview, BaseMap, Nation (+2 more)

### Community 14 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 16 - "Point"
Cohesion: 0.07
Nodes (8): Point, Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.23
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.05
Nodes (6): Character page, Court 3D scene (CourtView), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), GateScreen, InventoryStatsButton

### Community 20 - "PveCommands.java"
Cohesion: 0.09
Nodes (31): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+23 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 22 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.04
Nodes (3): The general and guard, Victory, pursuit and surrender, StreetWalks

### Community 25 - ".plan"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.10
Nodes (13): Inferences, Cited Findings, Cited Findings, ChunkMapAccess, ChunkMapCameraMixin, CityVegetationMixin, ClientChunkWindowMixin, DhShaderLoaderMixin (+5 more)

### Community 27 - ".shift"
Cohesion: 0.20
Nodes (5): BattleSite, Part, Water, BattleSite.Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.16
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 30 - ".get"
Cohesion: 0.15
Nodes (4): PlayerStats, View, Retinues, RetinuesTest

### Community 31 - ".send"
Cohesion: 0.14
Nodes (4): BackBanner, Entry, Msg, SimpleChannel

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 37 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.11
Nodes (5): Camera, CameraView, Card, Enlisted, Unit

### Community 38 - "Arm"
Cohesion: 0.08
Nodes (16): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+8 more)

### Community 39 - ".map"
Cohesion: 0.10
Nodes (3): MusterBars, PoliticsSync, Wars

### Community 41 - ".feudFight"
Cohesion: 0.19
Nodes (5): Clash, Kind, DUEL, SKIRMISH, FeudDuel

### Community 42 - "Lands"
Cohesion: 0.12
Nodes (4): Feud, Lord, Feuds, Front

### Community 43 - "Capture"
Cohesion: 0.22
Nodes (4): Layer, Look, Mesh, RecruitFigures

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (13): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+5 more)

### Community 48 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.03
Nodes (35): Bark, BaseMapPart, BattleSpot, BoardAction, BoardView, CameraAt, ChargeHits, CityClose (+27 more)

### Community 50 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.18
Nodes (7): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Cited Findings, Gaps, Inferences, Takeaway, DistantArmies

### Community 51 - "Rank"
Cohesion: 0.08
Nodes (15): Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY, SHIELDMAN, Rank, CAPTAIN (+7 more)

### Community 52 - "PoiPrecomputer.java"
Cohesion: 0.04
Nodes (12): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), TickWeave experiment, SeedHash, FactionType, BANDIT (+4 more)

### Community 53 - "Centre"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 55 - "Body"
Cohesion: 0.11
Nodes (18): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+10 more)

### Community 57 - "SeatCities"
Cohesion: 0.07
Nodes (8): Countryside, Plan, Grid, SeatCities, CountrysideTest, World, SeatCitiesTest, World

### Community 58 - "Faction Map View"
Cohesion: 0.16
Nodes (3): Cited Findings, FactionMapScreen, MapView

### Community 59 - ".step"
Cohesion: 0.12
Nodes (6): FigureMotion, Ground, Match, Placed, State, FigureMotionTest

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.10
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.13
Nodes (6): 10. Overall ranking, and areas checked that look fine, Gaps, Inferences, Takeaway, Loaded, StyleLibrary

### Community 66 - ".step"
Cohesion: 0.13
Nodes (6): Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 67 - ".choose"
Cohesion: 0.15
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 69 - "RafasConquest.java"
Cohesion: 0.03
Nodes (3): Battle map developer web page, Morale, rout and rally, RafasConquest

### Community 71 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.07
Nodes (10): GateDoors, Land, Result, SiegeSurvey, Wall, Door, GateGeometry, Hinge (+2 more)

### Community 72 - "Ride"
Cohesion: 0.19
Nodes (4): DistantShips, Far, Riding, SableSeen

### Community 73 - "BattlePlan"
Cohesion: 0.05
Nodes (10): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, WarPlan (+2 more)

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.07
Nodes (28): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+20 more)

### Community 76 - "Ride"
Cohesion: 0.22
Nodes (3): Ride, Swell, SwellTest

### Community 77 - ".board"
Cohesion: 0.08
Nodes (13): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Type, CLEAR (+5 more)

### Community 79 - "ClothRenderer"
Cohesion: 0.31
Nodes (4): ClothRenderer, Fabric, Kind, Live

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "CityLayout"
Cohesion: 0.10
Nodes (9): CityLayout, Side, EAST, NORTH, SOUTH, WEST, Site, WallSegment (+1 more)

### Community 85 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 86 - "Calibration"
Cohesion: 0.19
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.07
Nodes (21): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+13 more)

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "AgeTest.java"
Cohesion: 0.14
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 92 - "Kind"
Cohesion: 0.13
Nodes (12): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY (+4 more)

### Community 94 - ".block"
Cohesion: 0.07
Nodes (3): Cited Findings, BattleMap, WarCampTest

### Community 95 - ".generate"
Cohesion: 0.10
Nodes (5): BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 96 - "ConquestWorldgen"
Cohesion: 0.06
Nodes (7): ConquestConfig, CityDistrictStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 98 - ".inBox"
Cohesion: 0.16
Nodes (4): Cited Findings, CityGates, Done, CityIndex

### Community 99 - "FactionInfoScreen"
Cohesion: 0.16
Nodes (3): FactionInfoScreen, Line, StructureLink

### Community 100 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.05
Nodes (17): Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate, GateAction (+9 more)

### Community 102 - "Cloth"
Cohesion: 0.10
Nodes (5): Cloth, Solid, ClothTest, OffTheBackTest, WoolTest

### Community 103 - "FigureBatches"
Cohesion: 0.07
Nodes (11): ShipSprite, Batch, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED (+3 more)

### Community 105 - "Peerage"
Cohesion: 0.09
Nodes (11): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+3 more)

### Community 107 - ".moraleOf"
Cohesion: 0.15
Nodes (8): 1. Flank and rear shock, 2. Nearby rout (fright), 3. Army-wide losses, 4. Shattering, Design: adding the four morale shocks (built 2026-11-11, untested in game), Tests (FieldBattleTest), The core change: morale becomes a sum, Morale

### Community 109 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 110 - "FigureGear"
Cohesion: 0.17
Nodes (7): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 111 - "City"
Cohesion: 0.14
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 113 - "WaterSwell"
Cohesion: 0.16
Nodes (8): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Made, WaterSwell, SeaFoamTest

### Community 115 - "ConquestSavedData"
Cohesion: 0.05
Nodes (13): Cited Findings, ConquestSavedData, Faction, ResourceType, FOOD, Poi, PoiType, CAPITAL (+5 more)

### Community 116 - ".of"
Cohesion: 0.16
Nodes (5): FactionBanner, Layer, FactionBannerNbt, CityBanners, FactionBannerTest

### Community 117 - "Cited Findings"
Cohesion: 0.16
Nodes (12): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+4 more)

### Community 118 - ".build"
Cohesion: 0.13
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 121 - "CityTerrain"
Cohesion: 0.06
Nodes (18): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+10 more)

### Community 122 - "WorldSettings"
Cohesion: 0.09
Nodes (11): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, NobleAnnex, OriginRoller, Roll (+3 more)

### Community 124 - ".view"
Cohesion: 0.18
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 125 - "NobleTitle"
Cohesion: 0.13
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 126 - "BattleSounds"
Cohesion: 0.23
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 128 - "BattleShips"
Cohesion: 0.13
Nodes (7): Hull, Ramming, BattleShips, Helm, Ram, SableLink, RammingTest

### Community 130 - "MapView"
Cohesion: 0.27
Nodes (3): Hit, LabelLayer, LabelShape

### Community 132 - "Roads"
Cohesion: 0.11
Nodes (15): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+7 more)

### Community 133 - "CityVariant"
Cohesion: 0.16
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 134 - "SeaCalm"
Cohesion: 0.17
Nodes (5): Sea, Depths, SeaCalm, Strengths, SeaCalmTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.10
Nodes (19): 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, Cited Findings, Cited Findings, Gaps, Gaps (+11 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (40): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 6. Sodium (and Iris) compatibility, 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply (+32 more)

### Community 142 - "Homeland"
Cohesion: 0.09
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 143 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.07
Nodes (8): BannerArt, BattleBannerRenderer, CapeCurio, ClothItemRenderer, Carried, Layer, GateRenderer, MarkerRenderer

### Community 148 - "MapLayer"
Cohesion: 0.14
Nodes (6): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer, UnitLayer

### Community 149 - "RoadNetwork"
Cohesion: 0.10
Nodes (5): RoadFeature, Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 152 - "Override"
Cohesion: 0.13
Nodes (3): Chunks, SettingsScreen, Politics.Front

### Community 155 - "org.joml.Matrix4f"
Cohesion: 0.21
Nodes (3): Arrow, ArrowVolleys, Figure

### Community 156 - ".plan"
Cohesion: 0.13
Nodes (7): Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 157 - "ShipHulls"
Cohesion: 0.42
Nodes (3): BattleKind, Part, ShipHulls

### Community 163 - "TechTreeScreen"
Cohesion: 0.06
Nodes (21): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Machine, ARCBALLISTA, BATTERING_RAM (+13 more)

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.06
Nodes (27): WarHorn, BattleStandardItem, Armoury, Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY (+19 more)

### Community 174 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.20
Nodes (6): Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Takeaway, Api, DistantGeneration

### Community 178 - "GoldChurch.java"
Cohesion: 0.19
Nodes (5): Gold church, Quest board and quests, Recruitment posts and recruiter, GoldChurch, Layout

### Community 186 - "SoldierFigure"
Cohesion: 0.18
Nodes (8): Boxes, Palette, Shape, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 204 - "Recruiter"
Cohesion: 0.08
Nodes (4): ConquestContent, GrappleHooks, ConquestEntities, Recruiter

### Community 205 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 217 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 218 - ".view"
Cohesion: 0.24
Nodes (7): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, GrandStrategy, View

### Community 219 - "net.minecraft.world.level.block.Block"
Cohesion: 0.10
Nodes (7): CourtSet, Seat, Style, Builder, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 224 - "RecruitFigures.java"
Cohesion: 0.04
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 225 - "DistantOcclusion"
Cohesion: 0.15
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 226 - ".readPlacements"
Cohesion: 0.27
Nodes (3): Offsets, Placement, PlacementOffsetsTest

### Community 228 - ".render"
Cohesion: 0.08
Nodes (8): CharacterScreen, Retainer, Spot, ClientPeerage, Banner, Portraits, Honour, Honours

### Community 232 - "Townsperson"
Cohesion: 0.14
Nodes (4): Walk, DistrictKey, Townsfolk, Townsperson

### Community 235 - "Capture"
Cohesion: 0.13
Nodes (4): Capture, Film, Horse, RecruitFiguresTest

### Community 236 - "Kind"
Cohesion: 0.13
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.11
Nodes (3): DistantDepth, DistantArmyRenderer, Rects

### Community 248 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 249 - "net.minecraft.core.BlockPos"
Cohesion: 0.06
Nodes (8): BattleBannerBlockEntity, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING, PlacedWall

### Community 252 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 255 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 256 - "BattleRole"
Cohesion: 0.18
Nodes (6): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "RecruitsBattle"
Cohesion: 0.04
Nodes (12): GeneralTask, Live, Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT (+4 more)

### Community 275 - "CourtView"
Cohesion: 0.08
Nodes (7): Camera, CourtView, Figure, NationScreen, Demographics, Share, Organization

### Community 277 - "Realm"
Cohesion: 0.09
Nodes (6): Hook, Realm, Succession, Taking, RealmPlannerTest, World

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.18
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 282 - "9. Memory: client caches, textures and NativeImages that are never freed or grow without bound"
Cohesion: 0.25
Nodes (5): 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Gaps, Inferences, Takeaway

### Community 288 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "WarCamp.java"
Cohesion: 0.07
Nodes (25): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Characters (holders, skills, prowess, traits), Cultures (seven homelands), Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Enlisted players (+17 more)

### Community 292 - "How Total War calculates battles between units"
Cohesion: 0.17
Nodes (11): 1. Combat is per soldier, not per unit, 2. Does the swing land?, 3. How much damage the hit does, 4. Charges, 5. Missiles, 6. Leadership (morale): what actually wins battles, 7. Autoresolve, 8. Older games (Shogun 1, Medieval 1, Rome 1) (+3 more)

### Community 294 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 298 - "Task"
Cohesion: 0.13
Nodes (13): BlockGrid, ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN, Task, AMBUSH (+5 more)

### Community 303 - ".postChunk"
Cohesion: 0.10
Nodes (5): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, BarracksPeople, CityGuards

### Community 305 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 308 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 309 - ".rightClick"
Cohesion: 0.22
Nodes (3): Entry, Menu, HornOrders

### Community 310 - "ConquestWorldScreen"
Cohesion: 0.22
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 311 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 315 - "Ranks"
Cohesion: 0.18
Nodes (3): Ranks, Offer, Recruitment

### Community 318 - "arraylist"
Cohesion: 0.05
Nodes (15): Base map generated from seed, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Demographics (population by culture and race), Faction Info screen, Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, World map layers (+7 more)

### Community 321 - "assertequals"
Cohesion: 0.06
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 323 - "State"
Cohesion: 0.40
Nodes (5): State, RALLIED, ROUTED, SHATTERED, STEADY

### Community 324 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.08
Nodes (12): HornCall, ATTACKING, MOVING, RALLYING, RETREATING, Role, BEARER, GENERAL (+4 more)

### Community 327 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 339 - "Cited Findings"
Cohesion: 0.14
Nodes (7): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Cited Findings, Gaps, Inferences, Takeaway, BattleSpotRing

### Community 352 - "State"
Cohesion: 0.50
Nodes (4): State, CARRIED, RAISING, UP

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **414 isolated node(s):** `IN`, `OUT`, `WALL`, `NO_LIGHTMAP`, `NO_SHADER_COLOUR` (+409 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1368 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **209 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `GrappleHook`, `net.minecraft.world.level.ChunkPos`, `.army`, `UnitOrder`, `Point`, `PveCommands.java`, `org.jetbrains.annotations.Nullable`, `.shift`, `WarCouncil`, `.sail`, `PveCommands Registration`, `net.minecraft.world.phys.Vec3`, `net.minecraft.world.item.ItemStack`, `rafasconquest.faction.Faction`, `.map`, `net.minecraft.world.entity.Entity`, `net.minecraft.server.level.ServerPlayer`, `Shape`, `PoiPrecomputer.java`, `UnitMode`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `RafasConquest.java`, `net.minecraft.server.level.ServerLevel`, `ClientEvents`, `Work`, `WarCamp`, `.block`, `rafasconquest.battle.BattlePlan.Point`, `SiegeWorks`?**
  _High betweenness centrality (0.079) - this node is a cross-community bridge._
- **What connects `IN`, `OUT`, `WALL` to the rest of the system?**
  _414 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.042617638771484924 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `MapContext`, `Roads`, `.onLevelLoad`, `ClientPolitics`, `net.minecraft.client.gui.GuiGraphics`, `CourtView`, `Realm`, `RoadNetwork`, `.plan`, `WarCouncil`, `School`, `net.minecraft.world.item.ItemStack`, `.map`, `Lands`, `.postChunk`, `PoiPrecomputer.java`, `Centre`, `SeatCities`, `arraylist`, `TitleMapTest`, `RafasConquest.java`, `.board`, `Terrain`, `OrganizationScreen`, `.generate`, `.render`, `.onLogout`, `City`, `ConquestSavedData`, `Cited Findings`, `GrandStrategyScreen`, `PoliticalStyle`?**
  _High betweenness centrality (0.037) - this node is a cross-community bridge._
- **Should `net.minecraft.world.level.block.state.BlockState` be split into smaller, more focused modules?**
  _Cohesion score 0.07081807081807082 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._