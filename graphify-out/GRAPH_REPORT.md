# Graph Report - RafasConquest  (2026-11-16)

## Corpus Check
- 578 files · ~507,235 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7963 nodes · 25735 edges · 418 communities (149 shown, 269 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3283 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `fdffa366`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Calibration
- PoiPrecomputer.java
- net.minecraft.nbt.CompoundTag
- org.junit.jupiter.api.Test
- Feud
- BattleScreen
- ConquestSavedData
- .rightClick
- net.minecraft.world.level.block.entity.BlockEntity
- RecruitsBattle
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- PoliticsTest
- BaseMap
- RecruitsBattle.java
- net.minecraft.world.entity.LivingEntity
- RafasConquest Project Site
- Person
- CityLayout
- ConquestServer
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- .encode
- net.minecraft.server.level.ServerLevel
- CityHud
- Tier
- net.minecraft.world.phys.Vec3
- ConquestEvents
- .enlist
- net.neoforged.bus.api.SubscribeEvent
- .map
- MapLayer
- BattleShips
- PveCommands
- Poi
- CityPlan
- RecruitsBattle.java
- WaterSwell
- .send
- rafasconquest.faction.Faction
- Succession
- Arm
- .deploy
- net.minecraft.client.gui.GuiGraphics
- .view
- Personality
- SeaLanes
- BattlePlan
- .bark
- net.minecraft.world.level.block.state.BlockState
- net.minecraft.server.level.ServerLevel
- Barracks
- .render
- CityStyles
- .siege
- SailFigures
- CityTerrain
- net.minecraft.world.phys.Vec3
- HorizontalStretch
- TitleMap
- Bodies
- StyleLibrary
- Kind
- WorldSettings
- BattleMap.java
- Row
- School
- DistantShips
- org.spongepowered.asm.mixin.Mixin
- MapLands
- WarCouncil
- Roads
- RecruitFigures
- .plan
- net.minecraft.core.Direction
- Candidate
- Faction
- SailBlock.java
- PlacedBanners
- net.minecraft.core.BlockPos
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- ClientScreens
- DialogueScreen
- Recruiter
- NobleAnnex
- CityPlacement
- MapLayer
- CityGridBuilder
- FactionInfoScreen
- NationScreen
- .general
- net.minecraft.world.item.Item
- FigureBatches
- rafasconquest.battle.BattlePlan.Point
- WarCamp
- How Total War calculates battles between units
- MapContext
- net.minecraft.server.level.ServerPlayer
- CityPanel
- .step
- Realm
- RoadNetwork
- .choose
- SoldierFigure
- .view
- .generate
- net.minecraft.server.level.ServerPlayer
- GrandStrategyScreen
- net.minecraft.world.level.Level
- DistantHorizonsLink
- .readPlacements
- .make
- PoliticalStyle
- FormationSnapshot
- Quests
- CityTerrainPiece
- Terrain
- TitleOwnersScreen
- ProvinceMap
- .shift
- ClothRenderer
- Cloth
- .accept
- ClothItemRenderer
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- net.minecraft.resources.ResourceLocation
- DataFiles
- CityManagerScreen
- MarkerSpec
- .tick
- CLAUDE.md
- MarkerScreen
- ThrownFigures
- BackBannerLayer.java
- NobleDistrictTest
- net.minecraft.sounds.SoundEvent
- ChunkMapCameraMixin.java
- UnitOrder
- Kit
- StrategyRelief
- /pve debug commands
- Settlement
- Site
- ClientPolitics
- Call
- ConquestConfig
- .noble
- net.minecraft.world.level.block.Rotation
- ArmySpawnScreen
- Job
- net.minecraft.client.Minecraft
- SiegeWorks
- .settings
- Chunks
- TechTreeScreen
- Duty
- CityVegetationMixin.java
- ClientTownsfolk
- Kind
- DistantArmyRenderer
- FieldArmy
- .issue
- ConquestGameRules
- .onLevelLoad
- TitleStyle
- ClientModEvents.java
- .boundsAt
- HarbourCut
- 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling
- 5. Water/ocean waves (sea swell, foam, boats on waves)
- Profiling run sheet (baseline)
- .measure
- How do world generation and structure placement work, and what do they cost?
- SeaCalm
- .roadStart
- .second
- CityManager
- DayClockTest
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- BattleKeepInventory
- .register
- BackBanners
- Kind
- FaceOffScreen
- CourtView
- GatePlanksBlock.java
- .moraleOf
- PoliticsData
- CameraView
- Doctrine
- ItemFlags.java
- net.minecraft.world.item.ItemStack
- FactionMapScreen
- Phase
- Sea
- OrganizationScreen
- GateBlockEntity
- Machine
- list
- BattleSnapshot
- Lands
- BlockGrid
- WallTemplates
- OarFigures
- DistantHorizonsArmies
- .inventoryTick
- RafasConquest (PvE addon for Villager Recruits)
- WarHorn
- Politics
- Outcome
- DistantGeneration
- org.joml.Matrix4f
- BoardScreen
- Trait
- BattleBannerBlockEntity
- Build
- ClientBarks
- ArtilleryScreen
- .inCity
- ChunkWait
- DistantArmyRenderer.java
- .touches
- BattleType
- HornCall
- SwingModes
- .load
- Task
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Characters (holders, skills, prowess, traits)
- Cited Findings
- ShipKind

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 310 edges
2. `FieldBlock` - 222 edges
3. `FieldBattle` - 210 edges
4. `ConquestNetwork` - 129 edges
5. `FieldArmy` - 128 edges
6. `Point` - 116 edges
7. `Lands` - 89 edges
8. `Siege` - 86 edges
9. `BattleScreen` - 86 edges
10. `Politics` - 79 edges

## Surprising Connections (you probably didn't know these)
- `Cited Findings` --references--> `MapView`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/MapView.java
- `Cited Findings` --references--> `FormationSnapshot`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/battle/FormationSnapshot.java
- `Cited Findings` --references--> `Sprite`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/battle/FormationSnapshot.java
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

## Communities (418 total, 269 thin omitted)

### Community 0 - "Calibration"
Cohesion: 0.17
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 1 - "PoiPrecomputer.java"
Cohesion: 0.07
Nodes (13): Base map generated from seed, Command Table block, Faction Info screen, World map layers, Province size setting, Provinces (ProvinceMap), World map (CK3-style political map), SeedHash (+5 more)

### Community 2 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.07
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - "org.junit.jupiter.api.Test"
Cohesion: 0.07
Nodes (7): DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, SwingModesTest, GrandStrategyDateTest, OarRowingTest, ArmouryTest

### Community 4 - "Feud"
Cohesion: 0.09
Nodes (7): Clash, Feud, Kind, DUEL, SKIRMISH, FeudDuel, Feuds

### Community 6 - "ConquestSavedData"
Cohesion: 0.08
Nodes (6): Cited Findings, ConquestSavedData, MapPreview, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 7 - ".rightClick"
Cohesion: 0.21
Nodes (3): Entry, Menu, HornOrders

### Community 8 - "net.minecraft.world.level.block.entity.BlockEntity"
Cohesion: 0.08
Nodes (4): ArtilleryMount, ConquestContent, Oar, Sail

### Community 9 - "RecruitsBattle"
Cohesion: 0.04
Nodes (10): Cited Findings, Live, RecruitsBattle, Role, BEARER, GENERAL, GUARD, LEADER (+2 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 13 - "BaseMap"
Cohesion: 0.11
Nodes (10): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, ClientMapCache, BaseMap, Nation (+2 more)

### Community 14 - "RecruitsBattle.java"
Cohesion: 0.05
Nodes (50): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+42 more)

### Community 15 - "net.minecraft.world.entity.LivingEntity"
Cohesion: 0.07
Nodes (7): BattleBannerBlock, FlagBlock, Mount, BARS, BLOCK, FENCE, WALL

### Community 17 - "RafasConquest Project Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.14
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 19 - "CityLayout"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 20 - "ConquestServer"
Cohesion: 0.09
Nodes (7): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, CityBanners, ConquestServer, StructureLink, PoiPrecomputer

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 22 - ".encode"
Cohesion: 0.13
Nodes (6): Hook, Kind, FAVOUR, SECRET, Succession, SuccessionFormatTest

### Community 23 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.04
Nodes (9): GrappleHooks, BiomePalette, Entry, ConquestEntities, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement (+1 more)

### Community 24 - "CityHud"
Cohesion: 0.09
Nodes (3): CityHud, Spot, CityHudPreviewTest

### Community 25 - "Tier"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.09
Nodes (16): Camera, Card, Enlisted, Figure, Shape, LINE, SQUARE, V (+8 more)

### Community 28 - ".enlist"
Cohesion: 0.06
Nodes (6): BattleMap, GroundPlanTest, Land, Land, Land, Land

### Community 30 - ".map"
Cohesion: 0.08
Nodes (7): Cited Findings, Army, Goal, MusterBars, PoliticsSync, War, Wars

### Community 31 - "MapLayer"
Cohesion: 0.16
Nodes (8): LegendEntry, MapLayer, Symbol, Place, PlaceMenuLayer, Row, TerritoryLayer, UnitLayer

### Community 32 - "BattleShips"
Cohesion: 0.10
Nodes (7): Hull, Ramming, BattleShips, Helm, Ram, SableLink, RammingTest

### Community 34 - "Poi"
Cohesion: 0.14
Nodes (7): ResourceType, FOOD, Poi, PoiType, CAPITAL, CITY, FARM

### Community 35 - "CityPlan"
Cohesion: 0.14
Nodes (3): CityPlan, Piece, Plan

### Community 38 - "WaterSwell"
Cohesion: 0.10
Nodes (5): Columns, SableSeen, Made, WaterSwell, SeaFoamTest

### Community 39 - ".send"
Cohesion: 0.09
Nodes (11): BackBanner, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel, Type (+3 more)

### Community 40 - "rafasconquest.faction.Faction"
Cohesion: 0.10
Nodes (4): RecruitsFactions, FactionBanner, Layer, FactionBannerTest

### Community 41 - "Succession"
Cohesion: 0.14
Nodes (3): Taking, RealmPlannerTest, World

### Community 42 - "Arm"
Cohesion: 0.04
Nodes (28): Arm, ARCBALLISTAS, ARCHERS, CAVALRY, CULVERINS, ENGINES, HALBERDIERS, HEAVY_CAVALRY (+20 more)

### Community 44 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.04
Nodes (3): Races (Sunborn, Vinekin, Meadowfolk, ...), GateScreen, InventoryStatsButton

### Community 45 - ".view"
Cohesion: 0.05
Nodes (23): Advancement, Boards, Entry, View, Names, NobleTitle, COUNT, DUKE (+15 more)

### Community 46 - "Personality"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (13): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+5 more)

### Community 48 - "BattlePlan"
Cohesion: 0.05
Nodes (8): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, BattlePlanTest

### Community 51 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.08
Nodes (5): Saved, BattleWallBannerBlock, CommandTableBlock, HelmBlock, MarkerBlock

### Community 53 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.16
Nodes (3): Cited Findings, BarracksPeople, CityGuards

### Community 55 - ".render"
Cohesion: 0.14
Nodes (5): CharacterScreen, Retainer, Spot, Honour, Honours

### Community 59 - "CityTerrain"
Cohesion: 0.07
Nodes (17): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+9 more)

### Community 60 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.11
Nodes (8): Cited Findings, BattleSounds, ChargeSound, DinSound, Queued, Shot, Source, DistantArmies

### Community 61 - "HorizontalStretch"
Cohesion: 0.06
Nodes (13): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Age, BRONZE, GUNPOWDER, STEAM, STEEL (+5 more)

### Community 63 - "TitleMap"
Cohesion: 0.15
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 66 - "Kind"
Cohesion: 0.13
Nodes (13): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HALBERDIERS, HEAVY_CAVALRY (+5 more)

### Community 67 - "WorldSettings"
Cohesion: 0.09
Nodes (10): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, WorldSettings (+2 more)

### Community 69 - "BattleMap.java"
Cohesion: 0.04
Nodes (3): Battle map developer web page, Morale, rout and rally, RafasConquest

### Community 71 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 72 - "DistantShips"
Cohesion: 0.11
Nodes (9): DistantShips, Far, HullKey, Riding, Want, BattleKind, Part, ShipHulls (+1 more)

### Community 73 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.19
Nodes (6): ArrowSaveMixin, ClientChunkWindowMixin, DhShaderLoaderMixin, DhTerrainShaderMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 75 - "WarCouncil"
Cohesion: 0.08
Nodes (8): GeneralTask, Facts, Officer, Said, Talk, WarCouncil, Ranks, WarCouncilPagesTest

### Community 77 - "Roads"
Cohesion: 0.07
Nodes (19): Grade, CITY, DOJO, HIGH, SEA, TOWN, Heap, Junction (+11 more)

### Community 78 - "RecruitFigures"
Cohesion: 0.09
Nodes (8): Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures, RecruitFiguresTest

### Community 79 - ".plan"
Cohesion: 0.10
Nodes (6): City, Grid, SeatCities, CityIndex, SeatCitiesTest, World

### Community 81 - "net.minecraft.core.Direction"
Cohesion: 0.20
Nodes (5): Land, Result, SiegeSurvey, Wall, SiegeSurveyTest

### Community 82 - "Candidate"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 84 - "SailBlock.java"
Cohesion: 0.08
Nodes (5): ArtilleryBlock, OarBlock, QuestBoardBlock, SailBlock, Builder

### Community 85 - "PlacedBanners"
Cohesion: 0.21
Nodes (3): Banner, PlacedBanners, SaveGuard

### Community 87 - "net.minecraft.core.BlockPos"
Cohesion: 0.11
Nodes (4): GateGeometry, CharacterMarkers, Placed, GateGeometryTest

### Community 96 - "MapLayer"
Cohesion: 0.13
Nodes (13): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+5 more)

### Community 98 - "CityGridBuilder"
Cohesion: 0.08
Nodes (11): Houses, CityGridBuilder, Ground, PlacedWall, Platform, CityVariant, DESERT, PLAINS (+3 more)

### Community 100 - "NationScreen"
Cohesion: 0.10
Nodes (3): NationScreen, Banner, Portraits

### Community 101 - ".general"
Cohesion: 0.17
Nodes (9): GameCharacter, Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN (+1 more)

### Community 102 - "net.minecraft.world.item.Item"
Cohesion: 0.16
Nodes (7): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 103 - "FigureBatches"
Cohesion: 0.09
Nodes (10): Batch, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE (+2 more)

### Community 105 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.05
Nodes (18): Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate, GateAction (+10 more)

### Community 106 - "WarCamp"
Cohesion: 0.09
Nodes (3): PlannedTent, WarCamp, WarCampTest

### Community 107 - "How Total War calculates battles between units"
Cohesion: 0.17
Nodes (11): 1. Combat is per soldier, not per unit, 2. Does the swing land?, 3. How much damage the hit does, 4. Charges, 5. Missiles, 6. Leadership (morale): what actually wins battles, 7. Autoresolve, 8. Older games (Shogun 1, Medieval 1, Rome 1) (+3 more)

### Community 108 - "MapContext"
Cohesion: 0.08
Nodes (9): Cited Findings, BaseMapLayer, Hit, LabelLayer, MapContext, MapView, PoiIconLayer, TitleLayer (+1 more)

### Community 109 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.12
Nodes (5): Garrison, Army, GrandStrategy, View, GarrisonTest

### Community 110 - "CityPanel"
Cohesion: 0.14
Nodes (6): CityPanel, Mode, DISMISS, MARCH, MUSTER, NONE

### Community 111 - ".step"
Cohesion: 0.14
Nodes (5): FigureMotion, Match, Placed, State, FigureMotionTest

### Community 112 - "Realm"
Cohesion: 0.09
Nodes (10): Demographics, Share, Faith, Bond, Kind, FRIEND, RIVAL, Realm (+2 more)

### Community 113 - "RoadNetwork"
Cohesion: 0.09
Nodes (6): Cited Findings, RoadFeature, Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 114 - ".choose"
Cohesion: 0.11
Nodes (11): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+3 more)

### Community 115 - "SoldierFigure"
Cohesion: 0.09
Nodes (26): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+18 more)

### Community 116 - ".view"
Cohesion: 0.10
Nodes (13): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+5 more)

### Community 117 - ".generate"
Cohesion: 0.06
Nodes (19): ProvinceNames, BaseMapGenerator, Heights, Homeland, DESERT, JUNGLE, PLAINS, SAVANNA (+11 more)

### Community 118 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.03
Nodes (40): ArtillerySet, Bark, BaseMapPart, BattleSpot, BoardAction, BoardView, CameraAt, ChargeHits (+32 more)

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.09
Nodes (4): GrandStrategyScreen, Marcher, Spot, GarrisonOrder

### Community 121 - "net.minecraft.world.level.Level"
Cohesion: 0.09
Nodes (4): GateBlock, GrandStrategyBlock, StrategyBoardBlock, WorldMapItem

### Community 123 - ".readPlacements"
Cohesion: 0.27
Nodes (3): Offsets, Placement, PlacementOffsetsTest

### Community 124 - ".make"
Cohesion: 0.25
Nodes (5): Dialogue, Ladder, MILITARY, NOBLE, NONE

### Community 126 - "FormationSnapshot"
Cohesion: 0.14
Nodes (7): Cited Findings, Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 127 - "Quests"
Cohesion: 0.38
Nodes (3): Kind, Quests, Target

### Community 130 - "TitleOwnersScreen"
Cohesion: 0.21
Nodes (3): ClientPeerage, Owner, TitleOwnersScreen

### Community 131 - "ProvinceMap"
Cohesion: 0.17
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 132 - ".shift"
Cohesion: 0.16
Nodes (6): BattleSite, Part, Water, BattleSite.Water, BattleSite.Water, BattleSiteTest

### Community 133 - "ClothRenderer"
Cohesion: 0.06
Nodes (14): BannerArt, BattleBannerRenderer, Carried, ClothRenderer, Fabric, Far, Kind, Layer (+6 more)

### Community 134 - "Cloth"
Cohesion: 0.10
Nodes (6): Cloth, Solid, Ground, ClothTest, StretchTest, WoolTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.06
Nodes (32): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings (+24 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.07
Nodes (26): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings, Cited Findings, Cited Findings (+18 more)

### Community 141 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.10
Nodes (3): GrappleHookRenderer, RecruiterRenderer, GrappleHook

### Community 158 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 160 - "ChunkMapCameraMixin.java"
Cohesion: 0.25
Nodes (3): Cited Findings, ChunkMapAccess, ChunkMapCameraMixin

### Community 163 - "UnitOrder"
Cohesion: 0.11
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 164 - "Kit"
Cohesion: 0.10
Nodes (20): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+12 more)

### Community 174 - "Settlement"
Cohesion: 0.08
Nodes (13): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+5 more)

### Community 175 - "Site"
Cohesion: 0.09
Nodes (4): CityLayout, Site, SlopeTile, CitySlopeStructure

### Community 186 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 217 - "net.minecraft.world.level.block.Rotation"
Cohesion: 0.10
Nodes (3): StylePiece, GoldChurch, Layout

### Community 219 - "Job"
Cohesion: 0.12
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 223 - "SiegeWorks"
Cohesion: 0.09
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 225 - ".settings"
Cohesion: 0.11
Nodes (3): DistrictKey, Townsfolk, CityDistrictStructure

### Community 232 - "Duty"
Cohesion: 0.13
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 235 - "ClientTownsfolk"
Cohesion: 0.14
Nodes (6): ClientTownsfolk, Folk, StreetWalks, Walk, ClientTownsfolkTest, StreetWalksTest

### Community 236 - "Kind"
Cohesion: 0.22
Nodes (7): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.07
Nodes (4): Cited Findings, DistantArmyRenderer, Rects, ShipSprite

### Community 238 - "FieldArmy"
Cohesion: 0.05
Nodes (7): Point, FieldArmy, Event, FieldBattle, Order, Target, FieldBlock

### Community 254 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 255 - "HarbourCut"
Cohesion: 0.12
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 256 - "2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling"
Cohesion: 0.40
Nodes (5): 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, Cited Findings, Gaps, Inferences, Takeaway

### Community 257 - "5. Water/ocean waves (sea swell, foam, boats on waves)"
Cohesion: 0.40
Nodes (5): 5. Water/ocean waves (sea swell, foam, boats on waves), Cited Findings, Gaps, Inferences, Takeaway

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.40
Nodes (4): Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Takeaway

### Community 267 - "SeaCalm"
Cohesion: 0.10
Nodes (12): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Depths, SeaCalm, Strengths (+4 more)

### Community 271 - ".roadStart"
Cohesion: 0.07
Nodes (5): District, CityStructure, CityLayoutTest, CitySizesTest, PlaceMusicTest

### Community 278 - "DayClockTest"
Cohesion: 0.25
Nodes (3): DayClock, Step, DayClockTest

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.13
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, GateDoors, SableShips

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 292 - "CourtView"
Cohesion: 0.13
Nodes (5): CourtSet, Seat, Style, CourtView, Figure

### Community 295 - ".moraleOf"
Cohesion: 0.11
Nodes (13): 1. Flank and rear shock, 2. Nearby rout (fright), 3. Army-wide losses, 4. Shattering, Design: adding the four morale shocks (built 2026-11-11, untested in game), Tests (FieldBattleTest), The core change: morale becomes a sum, Morale (+5 more)

### Community 298 - "CameraView"
Cohesion: 0.13
Nodes (9): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway, Cited Findings, CameraView, RecruitsArrowCleanerMixin (+1 more)

### Community 300 - "Doctrine"
Cohesion: 0.07
Nodes (22): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+14 more)

### Community 302 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.24
Nodes (3): Armoury, Outfit, Style

### Community 305 - "FactionMapScreen"
Cohesion: 0.11
Nodes (3): FactionMapScreen, PetitionScreen, AbstractSliderButton

### Community 306 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 311 - "GateBlockEntity"
Cohesion: 0.09
Nodes (8): Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING, Hinge

### Community 313 - "Machine"
Cohesion: 0.08
Nodes (12): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+4 more)

### Community 321 - "list"
Cohesion: 0.06
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 323 - "Lands"
Cohesion: 0.09
Nodes (4): Cited Findings, Lord, Lands, Stronghold

### Community 330 - "OarFigures"
Cohesion: 0.17
Nodes (3): Model, OarFigures, Rowing

### Community 347 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 352 - "Politics"
Cohesion: 0.11
Nodes (10): Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Event, Front, Muster, CEREMONIAL (+2 more)

### Community 365 - "org.joml.Matrix4f"
Cohesion: 0.10
Nodes (14): Arrow, ArrowVolleys, Kind, ARROW, BALL, BIG_BALL, BIG_ROCK, BOLT (+6 more)

### Community 379 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 393 - "Build"
Cohesion: 0.29
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 407 - "DistantArmyRenderer.java"
Cohesion: 0.05
Nodes (16): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway (+8 more)

### Community 410 - "BattleType"
Cohesion: 0.22
Nodes (5): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE

### Community 411 - "HornCall"
Cohesion: 0.33
Nodes (5): HornCall, ATTACKING, MOVING, RALLYING, RETREATING

### Community 414 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 415 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.07
Nodes (28): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+20 more)

### Community 419 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.07
Nodes (26): Ambushes from woods, Character page, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Countryside (hamlets, villages, watchtowers, castles), Court 3D scene (CourtView), Cultures (seven homelands) (+18 more)

### Community 432 - "Cited Findings"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?

### Community 433 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

## Ambiguous Edges - Review These
- `Roads (high, city, town roads; bridges; signposts)` → `Mustering (offensive and ceremonial)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **445 isolated node(s):** `ARCHER_MELEE`, `ARROWS`, `CAVALRY`, `INFANTRY`, `BANDIT` (+440 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1454 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **269 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `Calibration`, `net.minecraft.nbt.CompoundTag`, `org.junit.jupiter.api.Test`, `.shift`, `net.minecraft.resources.ResourceLocation`, `RecruitsBattle.java`, `.second`, `.inCity`, `ChunkWait`, `net.minecraft.server.level.ServerLevel`, `net.minecraft.world.phys.Vec3`, `HornCall`, `.enlist`, `net.neoforged.bus.api.SubscribeEvent`, `.map`, `ConquestEvents`, `BattleShips`, `PveCommands`, `UnitOrder`, `RecruitsBattle.java`, `rafasconquest.faction.Faction`, `.view`, `net.minecraft.world.item.ItemStack`, `Phase`, `.siege`, `BattleMap.java`, `Row`, `WarCouncil`, `net.minecraft.core.Direction`, `SiegeWorks`, `Outcome`, `.general`, `rafasconquest.battle.BattlePlan.Point`, `WarCamp`, `net.minecraft.server.level.ServerPlayer`, `Trait`?**
  _High betweenness centrality (0.060) - this node is a cross-community bridge._
- **What connects `ARCHER_MELEE`, `ARROWS`, `CAVALRY` to the rest of the system?**
  _445 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `PoiPrecomputer.java` be split into smaller, more focused modules?**
  _Cohesion score 0.07030527289546716 - nodes in this community are weakly interconnected._
- **Why does `FieldBlock` connect `FieldArmy` to `Bodies`, `org.junit.jupiter.api.Test`, `BlockGrid`, `.moraleOf`, `Duty`, `rafasconquest.battle.BattlePlan.Point`, `Arm`, `RecruitsBattle`, `.deploy`, `BattlePlan`, `.second`, `ShipKind`, `Machine`, `net.minecraft.world.phys.Vec3`, `ConquestEvents`, `.enlist`, `Task`, `SiegeWorks`?**
  _High betweenness centrality (0.028) - this node is a cross-community bridge._
- **Should `net.minecraft.nbt.CompoundTag` be split into smaller, more focused modules?**
  _Cohesion score 0.06594059405940594 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Roads (high, city, town roads; bridges; signposts)` and `Mustering (offensive and ceremonial)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._