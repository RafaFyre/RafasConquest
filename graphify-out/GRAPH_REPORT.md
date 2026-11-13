# Graph Report - RafasConquest  (2026-11-12)

## Corpus Check
- 476 files · ~443,157 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7073 nodes · 22469 edges · 361 communities (153 shown, 208 thin omitted)
- Extraction: 86% EXTRACTED · 14% INFERRED · 0% AMBIGUOUS · INFERRED: 3099 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `a5a6bb5b`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FieldBlock
- Recruiter
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsArmies.java
- CityGridBuilder
- RafasConquest.java
- ShipHulls
- CityGridBuilder
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- Land
- BaseMap
- UnitOrder
- .stat
- .startAt
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.GuiGraphics
- PveCommands.java
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- list
- HorizontalStretch
- CityManagerScreen
- .plan
- .named
- .shift
- WarCouncil
- map
- .view
- .findGenerationPoint
- CityLayout
- PveCommands Registration
- School
- CityPlan
- .of
- net.minecraft.network.RegistryFriendlyByteBuf
- RecruitsBattle
- net.minecraft.server.MinecraftServer
- rafasconquest.faction.Faction
- Faction
- Feud
- War
- GateBlockEntity.java
- ConquestEvents.java
- Personalities
- SeaLanes
- Outcome
- Cited Findings
- Capture
- org.jetbrains.annotations.Nullable
- Centre
- Barracks
- Politics
- CityStyles
- SeatCities
- Faction Map View
- .step
- ClientScreens
- .render
- TitleMapTest
- Bodies
- StyleLibrary
- .step
- .choose
- RafasConquest.java
- Row
- net.minecraft.core.BlockPos
- DistantShips
- BattlePlan
- MapLands
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Phase
- .board
- net.minecraft.world.level.block.state.BlockState
- ChunkMapCameraMixin
- Terrain
- Ownership & Territory
- .findGenerationPoint
- Work
- Roads
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- CityLayout
- PoliticsData
- .holder
- .generate
- net.minecraft.core.Vec3i
- DistantOcclusion
- FactionInfoScreen
- Rank
- Kit
- Type
- FigureBatches
- Peerage
- MouthLayer.java
- ConquestNetwork
- MarkerScreen
- net.minecraft.sounds.SoundEvent
- .onLogout
- City
- BattleShips
- WaterSwell
- Arm
- ConquestSavedData
- Kind
- Cited Findings
- ConquestConfig
- GrandStrategyScreen
- SeaCalm
- CapitalLayout
- Grade
- Organizations
- rafasconquest.character.Rank
- City
- BattleShips
- .findGenerationPoint
- .at
- BattleSounds
- .rightClick
- .render
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .onLevelLoad
- CourtSet
- .cellMax
- GoldChurch.java
- .world
- RoadNetwork
- CLAUDE.md
- Job
- .place
- .plan
- Where are full scans, nested loops, repeated lookups and per-tick allocations?
- WallTemplates
- Machine
- TechTreeScreen
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- .drawEmpire
- Override
- BoardScreen
- SoldierFigure
- .touches
- State
- .noble
- .enlist
- .view
- .view
- rafasconquest.battle.BattlePlan.Point
- HarbourCut
- RecruitFigures.java
- NobleAnnex
- net.neoforged.bus.api.SubscribeEvent
- ClientPolitics
- Homeland
- net.minecraft.resources.ResourceLocation
- net.neoforged.bus.api.SubscribeEvent
- .world
- Kind
- DistantArmyRenderer
- Task
- 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel)
- ConquestGameRules
- Saved data: how big, how often written, is everything re-serialized?
- Trait
- OrganizationScreen
- Call
- Ramming
- Shape
- Profiling run sheet (baseline)
- .measure
- .passOnOrders
- CityManager
- GateBlockEntity
- BattleKeepInventory.java
- NobleDistrictTest
- Lands
- CourtView
- net.minecraft.nbt.CompoundTag
- Realm
- CameraView.java
- Peerage
- Kind
- BattlePlan.java
- How Total War calculates battles between units
- .readPlacements
- NetworkDirection
- .boundsAt
- BattleSnapshot
- UnitMode
- .inCity
- .postChunk
- Clash
- State
- Build
- ConquestWorldScreen
- .siege
- GatePlanksBlock
- Morale
- assertequals
- .build
- org.spongepowered.asm.mixin.Mixin
- Kind
- ClientBarks
- com.talhanation.recruits.entities.AbstractRecruitEntity
- WarCamp
- net.neoforged.fml.common.EventBusSubscriber

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 294 edges
2. `FieldBlock` - 252 edges
3. `FieldBattle` - 219 edges
4. `FieldArmy` - 127 edges
5. `ConquestNetwork` - 120 edges
6. `Lands` - 89 edges
7. `Siege` - 84 edges
8. `BattleScreen` - 82 edges
9. `Politics` - 79 edges
10. `BaseMap` - 77 edges

## Surprising Connections (you probably didn't know these)
- `Conclusion` --references--> `RoadLayer`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Every frame pays for disk listings, per-rectangle flushes and unculled soldiers` --references--> `RoadLayer`  [INFERRED]
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

## Communities (361 total, 208 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.04
Nodes (8): The core change: morale becomes a sum, FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 1 - "Recruiter"
Cohesion: 0.09
Nodes (4): WorldMapItem, Barks, Ear, Recruiter

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.09
Nodes (6): CityRoadPiece, CountryPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece

### Community 3 - ".army"
Cohesion: 0.06
Nodes (5): DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, GrandStrategyDateTest, ArmouryTest

### Community 4 - "CityTerrain"
Cohesion: 0.07
Nodes (17): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+9 more)

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.05
Nodes (9): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+1 more)

### Community 7 - "CityGridBuilder"
Cohesion: 0.13
Nodes (7): PlacedWall, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 8 - "RafasConquest.java"
Cohesion: 0.08
Nodes (4): ConquestContent, StreetWalks, ConquestEntities, RafasConquest

### Community 9 - "ShipHulls"
Cohesion: 0.36
Nodes (3): BattleKind, Part, ShipHulls

### Community 10 - "CityGridBuilder"
Cohesion: 0.15
Nodes (3): CityGridBuilder, Ground, Platform

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "Land"
Cohesion: 0.17
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 13 - "BaseMap"
Cohesion: 0.09
Nodes (10): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, BaseMap, Nation, BaseMapStore (+2 more)

### Community 14 - "UnitOrder"
Cohesion: 0.09
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 15 - ".stat"
Cohesion: 0.09
Nodes (10): ClientPeerage, Honour, Honours, Stat, DIPLOMACY, INTRIGUE, LEARNING, MARTIAL (+2 more)

### Community 16 - ".startAt"
Cohesion: 0.07
Nodes (10): Inferences, Api, DistantGeneration, HornCall, ATTACKING, MOVING, RALLYING, RETREATING (+2 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.23
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 20 - "PveCommands.java"
Cohesion: 0.09
Nodes (30): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+22 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - ".named"
Cohesion: 0.19
Nodes (4): Kind, FRIEND, RIVAL, CharacterMarkers

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.16
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 30 - ".view"
Cohesion: 0.15
Nodes (4): PlayerStats, View, Retinues, RetinuesTest

### Community 32 - "CityLayout"
Cohesion: 0.10
Nodes (5): CityLayout, District, Site, WorldSettings, CityLayoutTest

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.14
Nodes (3): CityPlan, Piece, Plan

### Community 36 - ".of"
Cohesion: 0.16
Nodes (5): FactionBanner, Layer, FactionBannerNbt, CityBanners, FactionBannerTest

### Community 38 - "RecruitsBattle"
Cohesion: 0.06
Nodes (6): Camera, Card, Enlisted, Figure, RecruitsBattle, Unit

### Community 39 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.12
Nodes (4): Cited Findings, FeudDuel, MusterBars, Wars

### Community 41 - "Faction"
Cohesion: 0.09
Nodes (4): RecruitsBridge, MapPreview, Faction, FactionInfoTest

### Community 42 - "Feud"
Cohesion: 0.13
Nodes (4): Feud, Lord, Feuds, Front

### Community 43 - "War"
Cohesion: 0.06
Nodes (12): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Army, Goal, Muster (+4 more)

### Community 44 - "GateBlockEntity.java"
Cohesion: 0.16
Nodes (3): MarkerRenderer, MarkerBlockEntity, MarkerSpec

### Community 45 - "ConquestEvents.java"
Cohesion: 0.04
Nodes (5): ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "SeaLanes"
Cohesion: 0.14
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 50 - "Cited Findings"
Cohesion: 0.13
Nodes (7): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, Arrow, DistantArmies

### Community 51 - "Capture"
Cohesion: 0.08
Nodes (9): FigureGear, Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures (+1 more)

### Community 52 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.09
Nodes (13): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoliticalMap (+5 more)

### Community 53 - "Centre"
Cohesion: 0.17
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 54 - "Barracks"
Cohesion: 0.23
Nodes (3): Barracks, BarracksPiece, BarracksTest

### Community 55 - "Politics"
Cohesion: 0.08
Nodes (8): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Event, Politics, PoliticsTest

### Community 56 - "CityStyles"
Cohesion: 0.22
Nodes (3): Building, CityStyles, Houses

### Community 57 - "SeatCities"
Cohesion: 0.11
Nodes (4): Grid, SeatCities, SeatCitiesTest, World

### Community 59 - ".step"
Cohesion: 0.14
Nodes (4): FigureMotion, Match, State, FigureMotionTest

### Community 61 - ".render"
Cohesion: 0.10
Nodes (10): 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Gaps, Inferences, Takeaway, CharacterScreen, Retainer, Spot (+2 more)

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 66 - ".step"
Cohesion: 0.13
Nodes (7): Cited Findings, Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 67 - ".choose"
Cohesion: 0.12
Nodes (11): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+3 more)

### Community 69 - "RafasConquest.java"
Cohesion: 0.04
Nodes (9): Base map generated from seed, Battle map developer web page, Command Table block, Faction Info screen, World map layers, Morale, rout and rally, Province size setting, Provinces (ProvinceMap) (+1 more)

### Community 71 - "net.minecraft.core.BlockPos"
Cohesion: 0.06
Nodes (11): Land, Result, SiegeSurvey, Wall, Saved, GateGeometry, Placed, CityGates (+3 more)

### Community 72 - "DistantShips"
Cohesion: 0.18
Nodes (3): DistantShips, Far, SableSeen

### Community 73 - "BattlePlan"
Cohesion: 0.05
Nodes (9): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, WarPlan (+1 more)

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.08
Nodes (22): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps, Gaps (+14 more)

### Community 76 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 77 - ".board"
Cohesion: 0.12
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 78 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (8): Sea, CommandTableBlock, GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, Builder, MarkerBlock

### Community 79 - "ChunkMapCameraMixin"
Cohesion: 0.21
Nodes (8): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway, Cited Findings, ChunkMapCameraMixin, TrackedEntityCameraMixin

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 85 - "Roads"
Cohesion: 0.21
Nodes (5): Link, Place, Roads, Spur, RoadsTest

### Community 86 - "Calibration"
Cohesion: 0.17
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.05
Nodes (30): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+22 more)

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - "CityLayout"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 94 - ".holder"
Cohesion: 0.08
Nodes (9): Point, BlockGrid, Ground, GroundPlan, GroundPlanTest, Land, Land, Land (+1 more)

### Community 95 - ".generate"
Cohesion: 0.08
Nodes (6): ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 98 - "DistantOcclusion"
Cohesion: 0.13
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 99 - "FactionInfoScreen"
Cohesion: 0.16
Nodes (3): FactionInfoScreen, Line, StructureLink

### Community 100 - "Rank"
Cohesion: 0.09
Nodes (15): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Rank, CAPTAIN (+7 more)

### Community 101 - "Kit"
Cohesion: 0.11
Nodes (19): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+11 more)

### Community 102 - "Type"
Cohesion: 0.33
Nodes (4): Type, CLEAR, DELIVER, SCOUT

### Community 103 - "FigureBatches"
Cohesion: 0.08
Nodes (9): Batch, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE, FigureBatches (+1 more)

### Community 105 - "Peerage"
Cohesion: 0.10
Nodes (10): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+2 more)

### Community 106 - "MouthLayer.java"
Cohesion: 0.11
Nodes (3): BackBannerLayer, GateRenderer, MouthLayer

### Community 107 - "ConquestNetwork"
Cohesion: 0.03
Nodes (38): Bark, BaseMapPart, BattleSpot, BoardAction, BoardView, CameraAt, ChargeHits, CityClose (+30 more)

### Community 109 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 110 - ".onLogout"
Cohesion: 0.13
Nodes (3): ClientMapCache, Throw, ThrownFigures

### Community 111 - "City"
Cohesion: 0.09
Nodes (11): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+3 more)

### Community 112 - "BattleShips"
Cohesion: 0.15
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips, Ride

### Community 113 - "WaterSwell"
Cohesion: 0.10
Nodes (8): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, WaterSwell, Swell, SeaFoamTest

### Community 114 - "Arm"
Cohesion: 0.07
Nodes (18): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+10 more)

### Community 115 - "ConquestSavedData"
Cohesion: 0.07
Nodes (6): Cited Findings, ConquestSavedData, Poi, EconomyTick, PoiPrecomputer, ConquestSavedDataTest

### Community 116 - "Kind"
Cohesion: 0.13
Nodes (12): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY (+4 more)

### Community 117 - "Cited Findings"
Cohesion: 0.17
Nodes (11): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom?, Gaps (+3 more)

### Community 121 - "SeaCalm"
Cohesion: 0.19
Nodes (4): Ground, Depths, SeaCalm, SwellTest

### Community 122 - "CapitalLayout"
Cohesion: 0.10
Nodes (9): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, CapitalRelocationTest (+1 more)

### Community 123 - "Grade"
Cohesion: 0.16
Nodes (9): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Plan (+1 more)

### Community 124 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 125 - "rafasconquest.character.Rank"
Cohesion: 0.12
Nodes (4): Ranks, RankKits, Offer, Recruitment

### Community 128 - "BattleShips"
Cohesion: 0.20
Nodes (4): BattleShips, Helm, Ram, SableLink

### Community 132 - "BattleSounds"
Cohesion: 0.25
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 134 - ".render"
Cohesion: 0.08
Nodes (13): Cited Findings, BaseMapLayer, MapContext, LegendEntry, MapLayer, Symbol, MapView, Place (+5 more)

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.06
Nodes (28): Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, 10. Overall ranking, and areas checked that look fine, 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, Cited Findings (+20 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.06
Nodes (31): 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings, Cited Findings (+23 more)

### Community 142 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 143 - ".cellMax"
Cohesion: 0.12
Nodes (4): DistrictKey, Townsfolk, CitySizesTest, StreetWalksTest

### Community 146 - "GoldChurch.java"
Cohesion: 0.18
Nodes (7): Gold church, Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter, GoldChurch, Layout

### Community 149 - "RoadNetwork"
Cohesion: 0.15
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 152 - "Job"
Cohesion: 0.15
Nodes (9): Enlisted players, Jobs (cavalry, footman, archer, shieldman, heavy infantry), The war horn, Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY (+1 more)

### Community 156 - ".plan"
Cohesion: 0.14
Nodes (6): Demographics, Share, Faith, Bond, RealmPlanner, Seat

### Community 157 - "Where are full scans, nested loops, repeated lookups and per-tick allocations?"
Cohesion: 0.33
Nodes (4): Gaps, Inferences, Takeaway, Where are full scans, nested loops, repeated lookups and per-tick allocations?

### Community 163 - "Machine"
Cohesion: 0.10
Nodes (12): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+4 more)

### Community 164 - "TechTreeScreen"
Cohesion: 0.18
Nodes (4): TechTreeScreen, Tech, TechTree, TechTreeTest

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.10
Nodes (6): WarHorn, BackBanners, Armoury, Outfit, Style, ItemFlags

### Community 174 - ".drawEmpire"
Cohesion: 0.29
Nodes (3): Hit, LabelLayer, LabelShape

### Community 178 - "Override"
Cohesion: 0.15
Nodes (3): BattleSite.Water, SettingsScreen, Politics.Front

### Community 186 - "SoldierFigure"
Cohesion: 0.06
Nodes (33): Corners, Weapon, BOW, GREAT, LONG, NONE, SWORD, Body (+25 more)

### Community 205 - "State"
Cohesion: 0.40
Nodes (5): State, RALLIED, ROUTED, SHATTERED, STEADY

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 218 - ".view"
Cohesion: 0.24
Nodes (7): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, GrandStrategy, View

### Community 219 - ".view"
Cohesion: 0.17
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 221 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.04
Nodes (19): Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate, GateAction (+11 more)

### Community 224 - "RecruitFigures.java"
Cohesion: 0.05
Nodes (5): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), DistantDepth, Figure

### Community 231 - "Homeland"
Cohesion: 0.10
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 232 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.11
Nodes (4): RecruiterRenderer, TownspersonRenderer, Walk, Townsperson

### Community 236 - "Kind"
Cohesion: 0.22
Nodes (7): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.07
Nodes (8): Cited Findings, Cited Findings, ArrowVolleys, BattleSpotRing, DistantArmyRenderer, Hull, Rects, Placed

### Community 240 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 242 - "1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel)"
Cohesion: 0.40
Nodes (5): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), Cited Findings, Gaps, Inferences, Takeaway

### Community 249 - "Saved data: how big, how often written, is everything re-serialized?"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Saved data: how big, how often written, is everything re-serialized?, Takeaway

### Community 252 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 257 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - ".passOnOrders"
Cohesion: 0.04
Nodes (20): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+12 more)

### Community 267 - "GateBlockEntity"
Cohesion: 0.09
Nodes (9): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+1 more)

### Community 274 - "Lands"
Cohesion: 0.11
Nodes (4): Cited Findings, Lands, Stronghold, Claim

### Community 275 - "CourtView"
Cohesion: 0.09
Nodes (4): Camera, CourtView, Figure, NationScreen

### Community 276 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.06
Nodes (3): CityTerrainPiece, FarmPiece, PlaceholderPiece

### Community 277 - "Realm"
Cohesion: 0.13
Nodes (4): Hook, Realm, Succession, Taking

### Community 283 - "CameraView.java"
Cohesion: 0.12
Nodes (3): CameraView, Chunks, ChunkMapAccess

### Community 288 - "Peerage"
Cohesion: 0.14
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageSync

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "BattlePlan.java"
Cohesion: 0.04
Nodes (39): Ambushes from woods, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Character page, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings) (+31 more)

### Community 292 - "How Total War calculates battles between units"
Cohesion: 0.11
Nodes (17): 1. Combat is per soldier, not per unit, 1. Flank and rear shock, 2. Does the swing land?, 2. Nearby rout (fright), 3. Army-wide losses, 3. How much damage the hit does, 4. Charges, 4. Shattering (+9 more)

### Community 293 - ".readPlacements"
Cohesion: 0.27
Nodes (3): Offsets, Placement, PlacementOffsetsTest

### Community 294 - "NetworkDirection"
Cohesion: 0.50
Nodes (3): NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 295 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 300 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 303 - ".postChunk"
Cohesion: 0.13
Nodes (4): Cited Findings, BarracksPeople, CityGuards, ConquestServer

### Community 306 - "Clash"
Cohesion: 0.28
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 308 - "State"
Cohesion: 0.50
Nodes (4): State, CARRIED, RAISING, UP

### Community 309 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 310 - "ConquestWorldScreen"
Cohesion: 0.20
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 321 - "assertequals"
Cohesion: 0.07
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 326 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.15
Nodes (6): Cited Findings, CityVegetationMixin, ClientChunkWindowMixin, RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 327 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 344 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.10
Nodes (6): Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **414 isolated node(s):** `FOOT`, `HORSE`, `ADVANCE`, `CAVALRY_CHARGE`, `CAVALRY_RECALL` (+409 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1352 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **208 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `Shape`, `net.minecraft.world.level.ChunkPos`, `.army`, `.passOnOrders`, `UnitOrder`, `.startAt`, `PveCommands.java`, `CameraView.java`, `WarCouncil`, `PveCommands Registration`, `net.minecraft.network.RegistryFriendlyByteBuf`, `net.minecraft.world.item.ItemStack`, `net.minecraft.server.MinecraftServer`, `BattleSnapshot`, `UnitMode`, `ConquestEvents.java`, `.onServerTick`, `RecruitsBattle.java`, `RafasConquest.java`, `net.minecraft.core.BlockPos`, `Phase`, `Work`, `Calibration`, `Doctrine`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `.enlist`, `WarCamp`, `.holder`, `rafasconquest.battle.BattlePlan.Point`, `net.neoforged.bus.api.SubscribeEvent`, `Arm`?**
  _High betweenness centrality (0.076) - this node is a cross-community bridge._
- **What connects `FOOT`, `HORSE`, `ADVANCE` to the rest of the system?**
  _414 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.04335260115606936 - nodes in this community are weakly interconnected._
- **Why does `FieldBlock` connect `FieldBlock` to `RecruitsBattle.java`, `.army`, `Machine`, `RecruitsBattle`, `.passOnOrders`, `ShipHulls`, `net.neoforged.bus.api.SubscribeEvent`, `Task`, `Work`, `.enlist`, `rafasconquest.battle.BattlePlan.Point`, `.holder`?**
  _High betweenness centrality (0.036) - this node is a cross-community bridge._
- **Should `Recruiter` be split into smaller, more focused modules?**
  _Cohesion score 0.0858974358974359 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._