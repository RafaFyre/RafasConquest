# Graph Report - RafasConquest  (2026-11-15)

## Corpus Check
- 557 files · ~484,939 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7678 nodes · 24761 edges · 377 communities (143 shown, 234 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3220 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `b9d5b5a0`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- .measure
- Lands
- net.minecraft.nbt.CompoundTag
- .army
- Lands
- BattleScreen
- DistantHorizonsArmies.java
- CityGridBuilder
- .enlist
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- net.minecraft.server.level.ServerPlayer
- BaseMap
- rafasconquest.map.BaseMap
- .fire
- net.minecraft.world.level.block.Block
- Battle Map Web Page & Site
- Person
- CityLayout
- Homeland
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- Organization
- HorizontalStretch
- CityManagerScreen
- .plan
- .step
- PoliticsTest
- WarCouncil
- City
- Point
- Roads
- net.minecraft.world.entity.LivingEntity
- PveCommands Registration
- CameraView
- CityPlan
- net.minecraft.core.Vec3i
- Machine
- Ride
- org.jetbrains.annotations.Nullable
- rafasconquest.faction.Faction
- Faction
- GateBlockEntity
- rafasconquest.map.BaseMap
- net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
- Personalities
- SeaLanes
- net.minecraft.server.level.ServerPlayer
- net.minecraft.client.renderer.MultiBufferSource
- BattlePlan
- .choose
- PoiPrecomputer
- Barracks
- .render
- CityStyles
- net.minecraft.world.item.ItemStack
- .build
- ClientTownsfolk
- rafasconquest.battle.FormationSnapshot
- TechTreeScreen
- TitleMapTest
- Bodies
- StyleLibrary
- RecruitsBattle
- WorldSettings
- BattleMap.java
- Row
- .generate
- net.minecraft.world.item.DyeColor
- ClientEvents
- MapLands
- .findGenerationPoint
- Arm
- Realm
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Duty
- net.minecraft.server.level.ServerLevel
- Ownership & Territory
- .render
- .layout
- org.jetbrains.annotations.Nullable
- CityManagerScreen
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- .drawEmpire
- .postChunk
- net.minecraft.network.RegistryFriendlyByteBuf
- SiegeWorks
- Cited Findings
- org.spongepowered.asm.mixin.Mixin
- FactionInfoScreen
- OrganizationScreen
- .view
- Cloth
- FigureBatches
- net.minecraft.core.BlockPos
- .block
- How Total War calculates battles between units
- .tick
- WorldFeatures
- Chunks
- DayClockTest
- .plan
- WaterSwell
- MapView
- Body
- com.mojang.blaze3d.vertex.PoseStack
- RafasConquest.java
- CityManagerScreen
- GrandStrategyScreen
- Retinues
- Settlement
- CityTerrainPiece
- SeatCitiesTest
- RoadNetwork
- Layout
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- .onLogout
- TitleOwnersScreen
- ProvinceMap
- .shift
- .rightClick
- SeaCalm
- ConquestSavedData
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .board
- net.minecraft.nbt.CompoundTag
- NationScreen
- MapView
- CLAUDE.md
- MapContext
- .encode
- .soundHorn
- RafasConquest (PvE addon for Villager Recruits)
- ClientPolitics
- Job
- RecruitFigures
- Kind
- Small Cluster 170
- Terrain
- .of
- net.neoforged.bus.api.SubscribeEvent
- BattleSnapshot
- Call
- Peerage
- .noble
- CourtSet
- .roadStart
- CityTerrain
- .view
- StrategyRelief
- RecruitFigures.java
- PlaceMusic.java
- CityLayout
- UnitOrder
- Cited Findings
- GatePlanksBlock.java
- Phase
- Piece
- net.minecraft.client.Minecraft
- Task
- ChunkWait
- ConquestGameRules
- .hinge
- .load
- ClientModEvents
- NobleAnnex
- .build
- PoliticsData
- BattleType
- Profiling run sheet (baseline)
- .measure
- net.minecraft.sounds.SoundEvent
- BlockGrid
- Morale
- ShipKind
- Type
- .world
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- ConquestWorldScreen
- GrappleHook
- Shape
- com.talhanation.recruits.entities.AbstractRecruitEntity
- Kind
- CityManager
- net.minecraft.server.level.ServerLevel
- .onLevelLoad
- .touches
- Doctrine
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- net.minecraft.world.level.Level
- Sea
- rafasconquest.battle.BattlePlan.Point
- PveCommands.java
- ThrownFigures
- list
- UnitMode
- .findGenerationPoint
- RecruitsBattle.java
- .boundsAt
- WallTemplates
- OarFigures
- Trait
- RecruitsBattle
- BattleKeepInventory
- BoardScreen
- Recruiter
- ClientBarks

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 308 edges
2. `FieldBlock` - 222 edges
3. `FieldBattle` - 209 edges
4. `FieldArmy` - 127 edges
5. `ConquestNetwork` - 122 edges
6. `Lands` - 89 edges
7. `BattleScreen` - 86 edges
8. `Siege` - 82 edges
9. `Politics` - 79 edges
10. `BaseMap` - 76 edges

## Surprising Connections (you probably didn't know these)
- `A tick budget and staggered schedules make the fixes stick` --references--> `BaseMap`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/map/BaseMap.java
- `Cited Findings` --references--> `MapView`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/MapView.java
- `Cited Findings` --references--> `MapView`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/MapView.java

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

## Communities (377 total, 234 thin omitted)

### Community 0 - ".measure"
Cohesion: 0.14
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 1 - "Lands"
Cohesion: 0.08
Nodes (9): Clash, Feud, Kind, DUEL, SKIRMISH, Lord, FeudDuel, Feuds (+1 more)

### Community 2 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.07
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - ".army"
Cohesion: 0.06
Nodes (6): DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, SwingModesTest, GrandStrategyDateTest, OarRowingTest

### Community 4 - "Lands"
Cohesion: 0.07
Nodes (14): Cited Findings, Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Lands, Stronghold (+6 more)

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.09
Nodes (3): DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "CityGridBuilder"
Cohesion: 0.06
Nodes (11): CityGridBuilder, Ground, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA (+3 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.15
Nodes (4): Camera, Card, Enlisted, Unit

### Community 13 - "BaseMap"
Cohesion: 0.07
Nodes (12): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, ClientMapCache, BaseMap, Nation (+4 more)

### Community 14 - "rafasconquest.map.BaseMap"
Cohesion: 0.08
Nodes (6): Army, Goal, MusterBars, PoliticsSync, War, Wars

### Community 16 - "net.minecraft.world.level.block.Block"
Cohesion: 0.14
Nodes (4): GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, Builder

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.15
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 19 - "CityLayout"
Cohesion: 0.10
Nodes (9): CityLayout, Side, EAST, NORTH, SOUTH, WEST, Site, WallSegment (+1 more)

### Community 20 - "Homeland"
Cohesion: 0.09
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 22 - "Organization"
Cohesion: 0.07
Nodes (27): Base map generated from seed, Characters (holders, skills, prowess, traits), Command Table block, Countryside (hamlets, villages, watchtowers, castles), Cultures (seven homelands), Demographics (population by culture and race), Epic Knights optional armour (Armoury), Faction Info screen (+19 more)

### Community 24 - "CityManagerScreen"
Cohesion: 0.09
Nodes (3): CityHud, Spot, CityHudPreviewTest

### Community 25 - ".plan"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - ".step"
Cohesion: 0.12
Nodes (6): FigureMotion, Ground, Match, Placed, State, FigureMotionTest

### Community 27 - "PoliticsTest"
Cohesion: 0.10
Nodes (6): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, PoliticsTest

### Community 28 - "WarCouncil"
Cohesion: 0.12
Nodes (8): Facts, Officer, Said, Talk, WarCouncil, SaveGuard, Option, WarCouncilPagesTest

### Community 29 - "City"
Cohesion: 0.10
Nodes (8): Countryside, Plan, City, Grid, SeatCities, CityIndex, CountrysideTest, World

### Community 30 - "Point"
Cohesion: 0.09
Nodes (4): Point, Ground, GroundPlan, SiegeTest

### Community 31 - "Roads"
Cohesion: 0.07
Nodes (19): Grade, CITY, DOJO, HIGH, SEA, TOWN, Heap, Junction (+11 more)

### Community 34 - "CameraView"
Cohesion: 0.09
Nodes (12): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway, Cited Findings, Cited Findings, CameraView (+4 more)

### Community 35 - "CityPlan"
Cohesion: 0.15
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 37 - "Machine"
Cohesion: 0.06
Nodes (14): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+6 more)

### Community 38 - "Ride"
Cohesion: 0.15
Nodes (8): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Ride, Swell, SwellTest

### Community 39 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.04
Nodes (8): The core change: morale becomes a sum, FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 42 - "Faction"
Cohesion: 0.13
Nodes (4): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction

### Community 43 - "GateBlockEntity"
Cohesion: 0.10
Nodes (9): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+1 more)

### Community 44 - "rafasconquest.map.BaseMap"
Cohesion: 0.04
Nodes (5): Character page, Court 3D scene (CourtView), Nation page, GateScreen, InventoryStatsButton

### Community 45 - "net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate"
Cohesion: 0.20
Nodes (4): BattleKind, Part, ShipHulls, Loaded

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (13): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+5 more)

### Community 48 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.03
Nodes (38): ArtillerySet, Bark, BaseMapPart, BattleSpot, BoardAction, BoardView, CameraAt, ChargeHits (+30 more)

### Community 50 - "net.minecraft.client.renderer.MultiBufferSource"
Cohesion: 0.07
Nodes (6): BackBannerLayer, BattleBannerRenderer, CapeCurio, GateRenderer, MarkerRenderer, MouthLayer

### Community 51 - "BattlePlan"
Cohesion: 0.05
Nodes (9): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, WarPlan (+1 more)

### Community 52 - ".choose"
Cohesion: 0.14
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 53 - "PoiPrecomputer"
Cohesion: 0.10
Nodes (3): Poi, CityBanners, PoiPrecomputer

### Community 55 - ".render"
Cohesion: 0.08
Nodes (7): CharacterScreen, Retainer, Spot, Banner, Portraits, Honour, Honours

### Community 57 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.05
Nodes (28): SwingModes, WarHorn, BattleStandardItem, Armoury, Kit, ARCHER, ARCHER_GUARD, CAPTAIN (+20 more)

### Community 58 - ".build"
Cohesion: 0.19
Nodes (3): Frame, Harbour, CitySizesTest

### Community 59 - "ClientTownsfolk"
Cohesion: 0.18
Nodes (5): ClientTownsfolk, Folk, StreetWalks, Walk, ClientTownsfolkTest

### Community 60 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.11
Nodes (10): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, BattleSounds, ChargeSound, DinSound (+2 more)

### Community 63 - "TitleMapTest"
Cohesion: 0.13
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.10
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.14
Nodes (4): Inferences, Offsets, Placement, StyleLibrary

### Community 67 - "WorldSettings"
Cohesion: 0.09
Nodes (11): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, SeedHash (+3 more)

### Community 71 - ".generate"
Cohesion: 0.06
Nodes (18): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+10 more)

### Community 72 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.14
Nodes (6): DistantShips, Far, HullKey, Riding, Want, DistantShipsTest

### Community 76 - "Arm"
Cohesion: 0.08
Nodes (18): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+10 more)

### Community 77 - "Realm"
Cohesion: 0.10
Nodes (8): Hook, Kind, FAVOUR, SECRET, Kind, FRIEND, RIVAL, Realm

### Community 78 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.05
Nodes (34): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+26 more)

### Community 79 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 81 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.19
Nodes (5): Land, Result, SiegeSurvey, Wall, SiegeSurveyTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 84 - ".layout"
Cohesion: 0.15
Nodes (7): Cited Findings, Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 85 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.07
Nodes (12): FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoliticalMap, FactionInfo (+4 more)

### Community 89 - "AgeTest.java"
Cohesion: 0.18
Nodes (7): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, AgeTest

### Community 91 - ".drawEmpire"
Cohesion: 0.17
Nodes (3): Hit, LabelLayer, LabelShape

### Community 92 - ".postChunk"
Cohesion: 0.12
Nodes (4): Cited Findings, BarracksPeople, CityGuards, ConquestServer

### Community 94 - "net.minecraft.network.RegistryFriendlyByteBuf"
Cohesion: 0.16
Nodes (5): BackBanner, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 95 - "SiegeWorks"
Cohesion: 0.12
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 96 - "Cited Findings"
Cohesion: 0.14
Nodes (13): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+5 more)

### Community 98 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.15
Nodes (7): ArrowSaveMixin, CityVegetationMixin, ClientChunkWindowMixin, DhShaderLoaderMixin, DhTerrainShaderMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 99 - "FactionInfoScreen"
Cohesion: 0.16
Nodes (3): FactionInfoScreen, Line, StructureLink

### Community 102 - "Cloth"
Cohesion: 0.09
Nodes (5): Cloth, Solid, ClothTest, OffTheBackTest, WoolTest

### Community 103 - "FigureBatches"
Cohesion: 0.07
Nodes (12): ShipSprite, Batch, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED (+4 more)

### Community 106 - ".block"
Cohesion: 0.05
Nodes (6): BattleMap, GroundPlanTest, Land, Land, Land, Land

### Community 107 - "How Total War calculates battles between units"
Cohesion: 0.11
Nodes (17): 1. Combat is per soldier, not per unit, 1. Flank and rear shock, 2. Does the swing land?, 2. Nearby rout (fright), 3. Army-wide losses, 3. How much damage the hit does, 4. Charges, 4. Shattering (+9 more)

### Community 111 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 112 - ".plan"
Cohesion: 0.14
Nodes (6): Demographics, Share, Faith, Bond, RealmPlanner, Seat

### Community 113 - "WaterSwell"
Cohesion: 0.15
Nodes (4): Columns, SableSeen, Made, WaterSwell

### Community 114 - "MapView"
Cohesion: 0.26
Nodes (4): MapView, Place, PlaceMenuLayer, Row

### Community 115 - "Body"
Cohesion: 0.06
Nodes (32): Weapon, BOW, GREAT, LONG, NONE, SWORD, Body, BALLISTA (+24 more)

### Community 116 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.11
Nodes (8): BannerArt, ClothItemRenderer, Carried, ClothRenderer, Fabric, Kind, Layer, Live

### Community 117 - "RafasConquest.java"
Cohesion: 0.05
Nodes (3): GrappleHooks, ConquestEntities, RafasConquest

### Community 118 - "CityManagerScreen"
Cohesion: 0.09
Nodes (5): BattleSite.Water, BattleSite.Water, Drop, MarkerScreen, Politics.Front

### Community 122 - "Settlement"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 126 - "RoadNetwork"
Cohesion: 0.09
Nodes (6): Cited Findings, RoadFeature, Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 127 - "Layout"
Cohesion: 0.20
Nodes (5): Gold church, Quest board and quests, Recruitment posts and recruiter, GoldChurch, Layout

### Community 128 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.12
Nodes (7): Hull, Ramming, BattleShips, Helm, Ram, SableLink, RammingTest

### Community 130 - "TitleOwnersScreen"
Cohesion: 0.21
Nodes (3): ClientPeerage, Owner, TitleOwnersScreen

### Community 131 - "ProvinceMap"
Cohesion: 0.16
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 132 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 133 - ".rightClick"
Cohesion: 0.21
Nodes (3): Entry, Menu, HornOrders

### Community 134 - "SeaCalm"
Cohesion: 0.22
Nodes (4): Depths, SeaCalm, Strengths, SeaCalmTest

### Community 136 - "ConquestSavedData"
Cohesion: 0.08
Nodes (6): Cited Findings, ConquestSavedData, MapPreview, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.07
Nodes (27): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings (+19 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings (+28 more)

### Community 142 - ".board"
Cohesion: 0.13
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 143 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.04
Nodes (12): BattleBannerBlockEntity, ConquestContent, Oar, Sail, MarkerBlockEntity, MarkerSpec, Build, CASTLE (+4 more)

### Community 146 - "NationScreen"
Cohesion: 0.09
Nodes (4): Camera, CourtView, Figure, NationScreen

### Community 149 - "MapView"
Cohesion: 0.10
Nodes (3): FactionMapScreen, PetitionScreen, AbstractSliderButton

### Community 152 - "MapContext"
Cohesion: 0.09
Nodes (8): BaseMapLayer, MapContext, LegendEntry, MapLayer, Symbol, PoiIconLayer, TerritoryLayer, UnitLayer

### Community 156 - ".soundHorn"
Cohesion: 0.33
Nodes (5): HornCall, ATTACKING, MOVING, RALLYING, RETREATING

### Community 158 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 163 - "Job"
Cohesion: 0.11
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 164 - "RecruitFigures"
Cohesion: 0.08
Nodes (9): FigureGear, Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures (+1 more)

### Community 167 - "Kind"
Cohesion: 0.13
Nodes (12): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY (+4 more)

### Community 175 - ".of"
Cohesion: 0.23
Nodes (3): FactionBanner, Layer, FactionBannerNbt

### Community 186 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 205 - "Peerage"
Cohesion: 0.06
Nodes (20): Advancement, Boards, Entry, View, Names, NobleTitle, COUNT, DUKE (+12 more)

### Community 206 - ".noble"
Cohesion: 0.25
Nodes (6): BlockPacker, Placed, Rect, Size, BlockPackerTest, PlacementOffsetsTest

### Community 217 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 219 - "CityTerrain"
Cohesion: 0.07
Nodes (18): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+10 more)

### Community 221 - ".view"
Cohesion: 0.06
Nodes (17): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+9 more)

### Community 224 - "RecruitFigures.java"
Cohesion: 0.07
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 225 - "PlaceMusic.java"
Cohesion: 0.04
Nodes (11): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), BiomePalette, Entry, ConquestWorldgen, CountryPlacement (+3 more)

### Community 226 - "CityLayout"
Cohesion: 0.08
Nodes (5): District, DistrictKey, Townsfolk, CityStructure, CityLayoutTest

### Community 228 - "UnitOrder"
Cohesion: 0.11
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 232 - "Cited Findings"
Cohesion: 0.08
Nodes (7): DistantHorizonsArmies, DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 235 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 236 - "Piece"
Cohesion: 0.12
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 237 - "net.minecraft.client.Minecraft"
Cohesion: 0.05
Nodes (13): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Cited Findings, Gaps, Inferences, Takeaway, DistantDepth, Arrow (+5 more)

### Community 238 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 252 - "ClientModEvents"
Cohesion: 0.10
Nodes (3): ClientModEvents, net.neoforged.neoforge.client.extensions.common.IClientItemExtensions, Registration

### Community 257 - "BattleType"
Cohesion: 0.13
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 267 - "Morale"
Cohesion: 0.18
Nodes (8): Morale, State, RALLIED, ROUTED, SHATTERED, STEADY, UnitInfo, UnitsState

### Community 271 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 274 - "Type"
Cohesion: 0.33
Nodes (4): Type, CLEAR, DELIVER, SCOUT

### Community 277 - ".world"
Cohesion: 0.12
Nodes (4): Succession, Taking, RealmPlannerTest, World

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.19
Nodes (6): Cited Findings, Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 282 - "ConquestWorldScreen"
Cohesion: 0.22
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 285 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 288 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.14
Nodes (7): Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER, Side

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 300 - "Doctrine"
Cohesion: 0.05
Nodes (30): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+22 more)

### Community 306 - "net.minecraft.world.level.Level"
Cohesion: 0.05
Nodes (8): Saved, ArtilleryBlock, CommandTableBlock, GateBlock, HelmBlock, OarBlock, SailBlock, MarkerBlock

### Community 313 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.04
Nodes (19): Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate, GateAction (+11 more)

### Community 318 - "PveCommands.java"
Cohesion: 0.05
Nodes (47): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+39 more)

### Community 321 - "list"
Cohesion: 0.07
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 323 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 325 - "RecruitsBattle.java"
Cohesion: 0.03
Nodes (3): /council <topic>, TickWeave experiment, Wind

### Community 326 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 330 - "OarFigures"
Cohesion: 0.17
Nodes (3): Model, OarFigures, Rowing

### Community 344 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 393 - "Recruiter"
Cohesion: 0.08
Nodes (5): RecruiterRenderer, WorldMapItem, Barks, Ear, Recruiter

## Ambiguous Edges - Review These
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **418 isolated node(s):** `ARCHER_MELEE`, `ARROWS`, `CAVALRY`, `INFANTRY`, `NO_LIGHTMAP` (+413 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1398 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **234 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `.measure`, `Lands`, `net.minecraft.nbt.CompoundTag`, `.army`, `.enlist`, `net.minecraft.server.level.ServerPlayer`, `GrappleHook`, `WarCouncil`, `.soundHorn`, `Point`, `.isOurs`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `PveCommands Registration`, `Shape`, `net.minecraft.server.level.ServerLevel`, `rafasconquest.faction.Faction`, `Doctrine`, `net.minecraft.server.level.ServerPlayer`, `rafasconquest.battle.BattlePlan.Point`, `net.minecraft.world.item.ItemStack`, `UnitMode`, `RecruitsBattle.java`, `BattleMap.java`, `ClientEvents`, `Peerage`, `net.minecraft.server.level.ServerLevel`, `Trait`, `RecruitsBattle`, `SiegeWorks`, `UnitOrder`, `net.minecraft.core.BlockPos`, `.block`, `Phase`, `RafasConquest.java`, `CityManagerScreen`?**
  _High betweenness centrality (0.059) - this node is a cross-community bridge._
- **What connects `ARCHER_MELEE`, `ARROWS`, `CAVALRY` to the rest of the system?**
  _418 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `.measure` be split into smaller, more focused modules?**
  _Cohesion score 0.13846153846153847 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `ProvinceMap`, `arraylist`, `rafasconquest.map.BaseMap`, `NationScreen`, `Homeland`, `MapView`, `MapContext`, `WarCouncil`, `City`, `.onLevelLoad`, `rafasconquest.map.BaseMap`, `PoiPrecomputer`, `.render`, `net.minecraft.world.item.ItemStack`, `TitleMapTest`, `.generate`, `Realm`, `org.jetbrains.annotations.Nullable`, `.drawEmpire`, `.postChunk`, `StrategyRelief`, `Cited Findings`, `PlaceMusic.java`, `OrganizationScreen`, `RafasConquest.java`, `GrandStrategyScreen`, `Settlement`, `RoadNetwork`?**
  _High betweenness centrality (0.048) - this node is a cross-community bridge._
- **Should `Lands` be split into smaller, more focused modules?**
  _Cohesion score 0.07876712328767123 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._