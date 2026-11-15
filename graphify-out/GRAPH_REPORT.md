# Graph Report - RafasConquest  (2026-11-14)

## Corpus Check
- 554 files · ~479,336 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7596 nodes · 24473 edges · 394 communities (161 shown, 233 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3201 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `f2fe10c2`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- .measure
- War
- net.minecraft.world.level.ChunkPos
- .army
- Lands
- BattleScreen
- DistantHorizonsArmies.java
- CityGridBuilder
- Politics
- rafasconquest.battle.FieldBlock
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- PoiPrecomputer.java
- BaseMap
- net.minecraft.server.MinecraftServer
- net.minecraft.world.level.Level
- Battle Map Web Page & Site
- Person
- Sea.java
- .fire
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- net.minecraft.world.level.block.Block
- HorizontalStretch
- net.minecraft.client.gui.GuiGraphics
- .plan
- CameraView
- FieldRules
- WarCouncil
- City
- .deploy
- City
- .view
- PveCommands Registration
- PoliticalStyle
- CityPlan
- net.minecraft.core.Vec3i
- Machine
- MarkerBlock.java
- SiegeWorks
- rafasconquest.faction.Faction
- Rank
- Front
- GateBlockEntity
- rafasconquest.map.BaseMap
- FieldBattle
- Personalities
- SeaLanes
- Enlisted players
- RafasConquest.java
- .enlist
- .choose
- Faction
- Barracks
- BattlePlan
- CityStyles
- net.minecraft.world.item.ItemStack
- net.minecraft.world.phys.Vec3
- .deploy
- CapeCurio.java
- Peerage
- TitleMapTest
- Bodies
- StyleLibrary
- org.jetbrains.annotations.Nullable
- CapitalLayout
- BattleMap.java
- Row
- .generate
- net.minecraft.world.item.DyeColor
- RecruitsBattle
- MapLands
- GoldChurch.java
- Arm
- Centre
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Duty
- net.minecraft.server.level.ServerLevel
- Ownership & Territory
- .wallSegments
- FormationSnapshot
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- .render
- .postChunk
- SimpleChannel.java
- net.minecraft.client.renderer.MultiBufferSource
- Grade
- org.spongepowered.asm.mixin.Mixin
- FactionInfoScreen
- OrganizationScreen
- GrappleHook
- Cloth
- FigureBatches
- net.minecraft.world.phys.Vec3
- .state
- How Total War calculates battles between units
- Override
- .generate
- net.minecraft.server.level.ServerPlayer
- org.spongepowered.asm.mixin.injection.Inject
- .plan
- WaterSwell
- Ride
- Body
- .deployment
- Swell
- Poi
- GrandStrategyScreen
- CityTerrain
- Grid
- CityTerrainPiece
- ClothRenderer
- .encode
- MapContext
- BattleShips
- Terrain
- TitleOwnersScreen
- MapView
- .shift
- net.minecraft.world.level.levelgen.structure.BoundingBox
- SeaCalm
- ConquestSavedData
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .world
- .board
- GateBlockEntity
- NationScreen
- MapView
- CLAUDE.md
- ChunkMapCameraMixin.java
- MapLayer
- .render
- .place
- ClientPolitics
- BattleBannerBlock
- RecruitFigures
- Kit
- Small Cluster 170
- .drawEmpire
- ConquestEvents
- BattleSnapshot
- .siege
- Strategy
- .noble
- ConquestEvents.java
- .of
- TrackedEntityCameraMixin.java
- BattleSounds
- RecruitFigures.java
- WallTemplates
- RafasConquest (PvE addon for Villager Recruits)
- UnitOrder
- Cited Findings
- net.neoforged.bus.api.SubscribeEvent
- ItemFlags.java
- Kind
- net.minecraft.client.Minecraft
- .order
- .hash
- ConquestGameRules
- Characters (holders, skills, prowess, traits)
- Hull
- net.neoforged.fml.common.EventBusSubscriber
- .build
- Land
- Settlement
- Profiling run sheet (baseline)
- .measure
- net.minecraft.sounds.SoundEvent
- Ranks (Soldier to Grand Marshal)
- .moraleOf
- ClientScreens
- Distribution
- .world
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- ClientModEvents.java
- .touches
- .postProcess
- Cities (grid, plots, vanilla village buildings)
- Kind
- TechTreeScreen
- BoardScreen
- .roadStart
- .onLevelLoad
- Wars (daily nation decisions)
- .officers
- Race
- CityLayout
- Call
- Recruiter
- ConquestNetwork
- Override
- rafasconquest.battle.BattlePlan.Point
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- ShareLiveTest
- RecruitsBattle.java
- SableSeen
- list
- .onLogout
- Building
- .findGenerationPoint
- Phase
- .boundsAt
- Cited Findings
- .offTheBack
- OarFigures
- ShipHulls
- BattleType
- ShipKind
- Trait
- NetworkDirection
- net.minecraft.nbt.CompoundTag
- .load
- Build
- SwingModes
- .bark
- NobleDistrictTest
- BackBanners
- ClientBarks
- Kind
- FieldBlock
- RoadNetwork
- .inventoryTick
- ArrowSaveMixin.java
- 3. GUI and map screens (strategy map, faction map, battle overlays)
- 8. Lessons from performance mods that a content mod can apply
- Politics
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 309 edges
2. `FieldBlock` - 222 edges
3. `FieldBattle` - 209 edges
4. `FieldArmy` - 127 edges
5. `ConquestNetwork` - 122 edges
6. `Lands` - 89 edges
7. `BattleScreen` - 86 edges
8. `Siege` - 82 edges
9. `BaseMap` - 80 edges
10. `Politics` - 79 edges

## Surprising Connections (you probably didn't know these)
- `Cited Findings` --references--> `CityVegetationMixin`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/mixin/CityVegetationMixin.java
- `Cited Findings` --references--> `CityVegetationMixin`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/mixin/CityVegetationMixin.java
- `A tick budget and staggered schedules make the fixes stick` --references--> `BaseMap`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/map/BaseMap.java

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

## Communities (394 total, 233 thin omitted)

### Community 0 - ".measure"
Cohesion: 0.14
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 1 - "War"
Cohesion: 0.09
Nodes (7): Army, Goal, Muster, CEREMONIAL, OFFENSIVE, PoliticsSync, War

### Community 3 - ".army"
Cohesion: 0.07
Nodes (8): Point, DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, SwingModesTest, GrandStrategyDateTest, OarRowingTest, ArmouryTest

### Community 4 - "Lands"
Cohesion: 0.10
Nodes (4): Cited Findings, Lands, Stronghold, Claim

### Community 5 - "BattleScreen"
Cohesion: 0.07
Nodes (4): BattleScreen, Entry, Menu, HornOrders

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.07
Nodes (5): Cited Findings, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "CityGridBuilder"
Cohesion: 0.09
Nodes (10): CityGridBuilder, Ground, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA (+2 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "PoiPrecomputer.java"
Cohesion: 0.09
Nodes (11): Base map generated from seed, Command Table block, Faction Info screen, World map layers, Province size setting, Provinces (ProvinceMap), World map (CK3-style political map), FactionType (+3 more)

### Community 13 - "BaseMap"
Cohesion: 0.14
Nodes (10): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, Junction, BaseMap, Nation (+2 more)

### Community 14 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.11
Nodes (7): Clash, Kind, DUEL, SKIRMISH, FeudDuel, MusterBars, Wars

### Community 16 - "net.minecraft.world.level.Level"
Cohesion: 0.07
Nodes (6): ArtilleryBlock, GrandStrategyBlock, OarBlock, SailBlock, StrategyBoardBlock, WorldMapItem

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.15
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 20 - ".fire"
Cohesion: 0.11
Nodes (4): CharacterMarkers, Placed, MarkerBlockEntity, MarkerSpec

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 22 - "net.minecraft.world.level.block.Block"
Cohesion: 0.14
Nodes (3): GatePlanksBlock, QuestBoardBlock, Builder

### Community 25 - ".plan"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "CameraView"
Cohesion: 0.17
Nodes (4): Cited Findings, Camera, CameraView, ChunkMapAccess

### Community 27 - "FieldRules"
Cohesion: 0.18
Nodes (5): FieldRules, Side, FLANK, FRONT, REAR

### Community 28 - "WarCouncil"
Cohesion: 0.15
Nodes (6): Facts, Officer, Said, Talk, WarCouncil, WarCouncilPagesTest

### Community 29 - "City"
Cohesion: 0.09
Nodes (5): SeatCities, TitleMap, TitleStyle, SeatCitiesTest, World

### Community 30 - ".deploy"
Cohesion: 0.12
Nodes (9): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT, Ground (+1 more)

### Community 31 - "City"
Cohesion: 0.08
Nodes (15): Grade, CITY, DOJO, HIGH, SEA, TOWN, Link, Place (+7 more)

### Community 32 - ".view"
Cohesion: 0.10
Nodes (13): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+5 more)

### Community 34 - "PoliticalStyle"
Cohesion: 0.13
Nodes (3): MapColors, LabelShape, PoliticalStyle

### Community 35 - "CityPlan"
Cohesion: 0.14
Nodes (3): CityPlan, Piece, Plan

### Community 37 - "Machine"
Cohesion: 0.08
Nodes (13): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+5 more)

### Community 38 - "MarkerBlock.java"
Cohesion: 0.09
Nodes (4): CommandTableBlock, GateBlock, HelmBlock, MarkerBlock

### Community 39 - "SiegeWorks"
Cohesion: 0.09
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 41 - "Rank"
Cohesion: 0.08
Nodes (13): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+5 more)

### Community 42 - "Front"
Cohesion: 0.12
Nodes (4): Feud, Lord, Feuds, Front

### Community 43 - "GateBlockEntity"
Cohesion: 0.08
Nodes (9): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+1 more)

### Community 44 - "rafasconquest.map.BaseMap"
Cohesion: 0.04
Nodes (7): Character page, Court 3D scene (CourtView), Demographics (population by culture and race), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), GateScreen, InventoryStatsButton

### Community 45 - "FieldBattle"
Cohesion: 0.07
Nodes (4): FieldArmy, Event, FieldBattle, Order

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (13): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+5 more)

### Community 48 - "Enlisted players"
Cohesion: 0.18
Nodes (8): Battle phases and days, Enlisted players, Envelopment, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Battle roles (frontline, bait, support, flanker, kill squad), War camps, War council, The war horn

### Community 50 - "RafasConquest.java"
Cohesion: 0.09
Nodes (3): GrappleHooks, ConquestEntities, RafasConquest

### Community 52 - ".choose"
Cohesion: 0.11
Nodes (11): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+3 more)

### Community 53 - "Faction"
Cohesion: 0.10
Nodes (6): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction, ResourceType, FOOD

### Community 54 - "Barracks"
Cohesion: 0.20
Nodes (3): Barracks, BarracksPiece, BarracksTest

### Community 57 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.16
Nodes (4): WarHorn, Armoury, Outfit, Style

### Community 58 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.09
Nodes (14): Card, Enlisted, Shape, LINE, SQUARE, V, WEDGE, Unit (+6 more)

### Community 60 - "CapeCurio.java"
Cohesion: 0.11
Nodes (4): BackBannerLayer, CapeCurio, Carried, MouthLayer

### Community 61 - "Peerage"
Cohesion: 0.05
Nodes (23): Advancement, Boards, Entry, View, Names, NobleTitle, COUNT, DUKE (+15 more)

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.12
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 67 - "CapitalLayout"
Cohesion: 0.18
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 71 - ".generate"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 72 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.14
Nodes (6): DistantShips, Far, HullKey, Riding, Want, DistantShipsTest

### Community 73 - "RecruitsBattle"
Cohesion: 0.04
Nodes (17): Cited Findings, Figure, HornCall, ATTACKING, MOVING, RALLYING, RETREATING, RecruitsBattle (+9 more)

### Community 75 - "GoldChurch.java"
Cohesion: 0.16
Nodes (3): GoldChurch, Layout, GoldChurchPiece

### Community 76 - "Arm"
Cohesion: 0.12
Nodes (10): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+2 more)

### Community 77 - "Centre"
Cohesion: 0.17
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 78 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.05
Nodes (34): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+26 more)

### Community 79 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 81 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.09
Nodes (8): ChunkWait, Wait, Land, SiegeSurvey, Wall, CityGates, Done, SiegeSurveyTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".wallSegments"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 84 - "FormationSnapshot"
Cohesion: 0.05
Nodes (25): Cited Findings, Engine, FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, ENGINES (+17 more)

### Community 87 - "Doctrine"
Cohesion: 0.18
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - ".render"
Cohesion: 0.10
Nodes (5): CharacterScreen, Retainer, Spot, Banner, Portraits

### Community 92 - ".postChunk"
Cohesion: 0.09
Nodes (6): Cited Findings, BarracksPeople, CityBanners, CityGuards, ConquestServer, PoiPrecomputer

### Community 95 - "net.minecraft.client.renderer.MultiBufferSource"
Cohesion: 0.13
Nodes (3): BattleBannerRenderer, GateRenderer, MarkerRenderer

### Community 96 - "Grade"
Cohesion: 0.15
Nodes (12): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+4 more)

### Community 98 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.26
Nodes (5): Cited Findings, DhTerrainShaderMixin, RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 99 - "FactionInfoScreen"
Cohesion: 0.16
Nodes (3): FactionInfoScreen, Line, StructureLink

### Community 102 - "Cloth"
Cohesion: 0.11
Nodes (4): Cloth, Solid, ClothTest, WoolTest

### Community 103 - "FigureBatches"
Cohesion: 0.08
Nodes (11): Batch, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE (+3 more)

### Community 105 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.08
Nodes (8): PlannedTent, Saved, WarCamp, CourtSet, Seat, Style, GateGeometry, GateGeometryTest

### Community 106 - ".state"
Cohesion: 0.07
Nodes (6): BattleMap, GroundPlanTest, Land, Land, Land, Land

### Community 107 - "How Total War calculates battles between units"
Cohesion: 0.17
Nodes (11): 1. Combat is per soldier, not per unit, 2. Does the swing land?, 3. How much damage the hit does, 4. Charges, 5. Missiles, 6. Leadership (morale): what actually wins battles, 7. Autoresolve, 8. Older games (Shogun 1, Medieval 1, Rome 1) (+3 more)

### Community 108 - "Override"
Cohesion: 0.10
Nodes (5): BattleSite.Water, BattleSite.Water, Drop, MarkerScreen, Politics.Front

### Community 109 - ".generate"
Cohesion: 0.07
Nodes (13): ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry, Homeland, DESERT, JUNGLE (+5 more)

### Community 110 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.08
Nodes (8): RankKits, Place, Offer, Recruitment, CityManager, GrandStrategy, View, PlaceMusic

### Community 111 - "org.spongepowered.asm.mixin.injection.Inject"
Cohesion: 0.23
Nodes (3): CityVegetationMixin, ClientChunkWindowMixin, DhShaderLoaderMixin

### Community 112 - ".plan"
Cohesion: 0.13
Nodes (6): Demographics, Share, Faith, Bond, RealmPlanner, Seat

### Community 115 - "Body"
Cohesion: 0.06
Nodes (33): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD, Body (+25 more)

### Community 117 - "Swell"
Cohesion: 0.20
Nodes (6): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Swell

### Community 118 - "Poi"
Cohesion: 0.16
Nodes (5): Poi, PoiType, CAPITAL, CITY, FARM

### Community 121 - "CityTerrain"
Cohesion: 0.07
Nodes (18): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+10 more)

### Community 122 - "Grid"
Cohesion: 0.19
Nodes (5): Countryside, Plan, Grid, CountrysideTest, World

### Community 124 - "ClothRenderer"
Cohesion: 0.13
Nodes (7): BannerArt, ClothItemRenderer, ClothRenderer, Fabric, Kind, Layer, Live

### Community 128 - "BattleShips"
Cohesion: 0.14
Nodes (4): BattleShips, Helm, Ram, SableLink

### Community 130 - "TitleOwnersScreen"
Cohesion: 0.21
Nodes (3): ClientPeerage, Owner, TitleOwnersScreen

### Community 131 - "MapView"
Cohesion: 0.17
Nodes (6): Cited Findings, BaseMapLayer, MapView, Place, PlaceMenuLayer, Row

### Community 132 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 133 - "net.minecraft.world.level.levelgen.structure.BoundingBox"
Cohesion: 0.14
Nodes (3): CountryPiece, FarmPiece, PlaceholderPiece

### Community 134 - "SeaCalm"
Cohesion: 0.22
Nodes (4): Depths, SeaCalm, Strengths, SeaCalmTest

### Community 136 - "ConquestSavedData"
Cohesion: 0.09
Nodes (6): Cited Findings, ConquestSavedData, MapPreview, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.07
Nodes (27): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings (+19 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.07
Nodes (26): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, Cited Findings, Cited Findings, Cited Findings (+18 more)

### Community 141 - ".world"
Cohesion: 0.15
Nodes (4): Honour, Honours, RealmPlannerTest, World

### Community 142 - ".board"
Cohesion: 0.10
Nodes (13): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Type, CLEAR (+5 more)

### Community 143 - "GateBlockEntity"
Cohesion: 0.07
Nodes (5): ArtilleryMount, BattleBannerBlockEntity, ConquestContent, Oar, Sail

### Community 146 - "NationScreen"
Cohesion: 0.09
Nodes (4): Camera, CourtView, Figure, NationScreen

### Community 149 - "MapView"
Cohesion: 0.10
Nodes (3): FactionMapScreen, PetitionScreen, AbstractSliderButton

### Community 155 - "MapLayer"
Cohesion: 0.20
Nodes (5): LegendEntry, MapLayer, Symbol, TerritoryLayer, UnitLayer

### Community 164 - "RecruitFigures"
Cohesion: 0.08
Nodes (9): RecruiterRenderer, Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures (+1 more)

### Community 167 - "Kit"
Cohesion: 0.11
Nodes (19): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+11 more)

### Community 205 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 217 - "ConquestEvents.java"
Cohesion: 0.04
Nodes (10): /pve debug citystats [samples], /pve debug cityterrain, ConquestConfig, CityDistrictStructure, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement (+2 more)

### Community 218 - ".of"
Cohesion: 0.18
Nodes (4): FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 221 - "TrackedEntityCameraMixin.java"
Cohesion: 0.22
Nodes (6): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway, TrackedEntityCameraMixin

### Community 223 - "BattleSounds"
Cohesion: 0.06
Nodes (14): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, Arrow, BattleSounds, ChargeSound (+6 more)

### Community 224 - "RecruitFigures.java"
Cohesion: 0.06
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 226 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 228 - "UnitOrder"
Cohesion: 0.11
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 232 - "Cited Findings"
Cohesion: 0.07
Nodes (14): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Cited Findings, Gaps, Inferences, Takeaway, ArrowVolleys, BattleSpotRing (+6 more)

### Community 236 - "Kind"
Cohesion: 0.22
Nodes (7): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL

### Community 237 - "net.minecraft.client.Minecraft"
Cohesion: 0.07
Nodes (5): DistantDepth, DistantArmyRenderer, Figure, Rects, ShipSprite

### Community 238 - ".order"
Cohesion: 0.25
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

### Community 242 - ".hash"
Cohesion: 0.13
Nodes (6): ChunkCoord, FarmLayout, NobleAnnex, OriginRoller, Roll, SeedHash

### Community 248 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.33
Nodes (7): Characters (holders, skills, prowess, traits), Cultures (seven homelands), Epic Knights optional armour (Armoury), Faiths and cults, Martial titles (Sword Saint, Sword King, Sword God), Organizations, Sword sanctums

### Community 249 - "Hull"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 255 - ".build"
Cohesion: 0.12
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 256 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 257 - "Settlement"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 266 - "Ranks (Soldier to Grand Marshal)"
Cohesion: 0.33
Nodes (6): Gold church, Land grants (seven ways), Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter

### Community 267 - ".moraleOf"
Cohesion: 0.11
Nodes (13): 1. Flank and rear shock, 2. Nearby rout (fright), 3. Army-wide losses, 4. Shattering, Design: adding the four morale shocks (built 2026-11-11, untested in game), Tests (FieldBattleTest), The core change: morale becomes a sum, Morale (+5 more)

### Community 274 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 277 - ".world"
Cohesion: 0.12
Nodes (7): Hook, Kind, FRIEND, RIVAL, Realm, Succession, Taking

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.19
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 282 - "ClientModEvents.java"
Cohesion: 0.09
Nodes (3): ClientModEvents, net.neoforged.neoforge.client.extensions.common.IClientItemExtensions, Registration

### Community 288 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.12
Nodes (7): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), TickWeave experiment, PoliticalMap, FactionInfo

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 295 - ".roadStart"
Cohesion: 0.07
Nodes (5): DistrictKey, Townsfolk, CitySizesTest, StreetWalksTest, PlaceMusicTest

### Community 298 - "Wars (daily nation decisions)"
Cohesion: 0.33
Nodes (5): Battles (prototype), Blocks of 20 soldiers, Heralds (daily news), Mustering (offensive and ceremonial), Wars (daily nation decisions)

### Community 300 - ".officers"
Cohesion: 0.15
Nodes (9): GameCharacter, Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN (+1 more)

### Community 302 - "Race"
Cohesion: 0.20
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 303 - "CityLayout"
Cohesion: 0.08
Nodes (5): CityLayout, District, Site, WorldSettings, CityLayoutTest

### Community 305 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 308 - "ConquestNetwork"
Cohesion: 0.03
Nodes (40): ArtillerySet, Bark, BaseMapPart, BattleSpot, BoardAction, BoardView, CameraAt, ChargeHits (+32 more)

### Community 310 - "Override"
Cohesion: 0.24
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 313 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.05
Nodes (17): Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate, GateAction (+9 more)

### Community 314 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.33
Nodes (4): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender

### Community 318 - "RecruitsBattle.java"
Cohesion: 0.04
Nodes (30): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+22 more)

### Community 321 - "list"
Cohesion: 0.05
Nodes (11): Countryside (hamlets, villages, watchtowers, castles), Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, Nations by zone, Roads (high, city, town roads; bridges; signposts), Seat cities, Storm wall and war zone, Kind (+3 more)

### Community 322 - ".onLogout"
Cohesion: 0.18
Nodes (5): ClientTownsfolk, Folk, StreetWalks, Walk, ClientTownsfolkTest

### Community 325 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 326 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 327 - "Cited Findings"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?

### Community 330 - "OarFigures"
Cohesion: 0.17
Nodes (3): Model, OarFigures, Rowing

### Community 336 - "ShipHulls"
Cohesion: 0.42
Nodes (3): BattleKind, Part, ShipHulls

### Community 337 - "BattleType"
Cohesion: 0.20
Nodes (5): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE

### Community 339 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 344 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 345 - "NetworkDirection"
Cohesion: 0.50
Nodes (3): NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 352 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 371 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 376 - "FieldBlock"
Cohesion: 0.05
Nodes (3): Target, Volley, FieldBlock

### Community 379 - "RoadNetwork"
Cohesion: 0.14
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 392 - "3. GUI and map screens (strategy map, faction map, battle overlays)"
Cohesion: 0.40
Nodes (5): 3. GUI and map screens (strategy map, faction map, battle overlays), Cited Findings, Gaps, Inferences, Takeaway

### Community 393 - "8. Lessons from performance mods that a content mod can apply"
Cohesion: 0.40
Nodes (5): 8. Lessons from performance mods that a content mod can apply, Cited Findings, Gaps, Inferences, Takeaway

### Community 398 - "Politics"
Cohesion: 0.11
Nodes (8): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Event, Politics, PoliticsData

## Ambiguous Edges - Review These
- `Roads (high, city, town roads; bridges; signposts)` → `Mustering (offensive and ceremonial)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **418 isolated node(s):** `SOLDIER`, `LEADER`, `GENERAL`, `BEARER`, `GUARD` (+413 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1393 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **233 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `.measure`, `BattleShips`, `net.minecraft.world.level.ChunkPos`, `.army`, `Politics`, `rafasconquest.battle.FieldBlock`, `net.minecraft.server.MinecraftServer`, `WarCouncil`, `PveCommands Registration`, `SiegeWorks`, `rafasconquest.faction.Faction`, `.officers`, `ConquestEvents`, `.enlist`, `rafasconquest.battle.BattlePlan.Point`, `.siege`, `net.minecraft.world.item.ItemStack`, `net.minecraft.world.phys.Vec3`, `Peerage`, `RecruitsBattle.java`, `Phase`, `BattleMap.java`, `net.minecraft.server.level.ServerLevel`, `Trait`, `ConquestEvents.java`, `ConquestEvents.java`, `BattleSounds`, `UnitOrder`, `GrappleHook`, `net.minecraft.world.phys.Vec3`, `.state`, `net.neoforged.bus.api.SubscribeEvent`, `.generate`, `net.minecraft.server.level.ServerPlayer`?**
  _High betweenness centrality (0.068) - this node is a cross-community bridge._
- **What connects `SOLDIER`, `LEADER`, `GENERAL` to the rest of the system?**
  _418 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `.measure` be split into smaller, more focused modules?**
  _Cohesion score 0.14333333333333334 - nodes in this community are weakly interconnected._
- **Why does `ConquestNetwork` connect `ConquestNetwork` to `BattleScreen`, `rafasconquest.map.BaseMap`, `net.minecraft.server.level.ServerPlayer`, `net.minecraft.world.level.Level`, `BattleSnapshot`, `arraylist`, `SimpleChannel.java`?**
  _High betweenness centrality (0.039) - this node is a cross-community bridge._
- **Should `War` be split into smaller, more focused modules?**
  _Cohesion score 0.08665269042627533 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Roads (high, city, town roads; bridges; signposts)` and `Mustering (offensive and ceremonial)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._