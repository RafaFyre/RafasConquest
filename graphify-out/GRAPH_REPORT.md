# Graph Report - RafasConquest  (2026-11-13)

## Corpus Check
- 542 files · ~468,244 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7467 nodes · 23991 edges · 374 communities (152 shown, 222 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3161 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `9a8c9f05`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- ConquestNetwork
- TechTreeScreen
- net.minecraft.nbt.CompoundTag
- .army
- com.talhanation.recruits.entities.AbstractRecruitEntity
- BattleScreen
- DistantHorizonsArmies.java
- CityGridBuilder
- PoliticsTest
- arraylist
- GateBlockEntity
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- heightmap
- BaseMap
- rafasconquest.map.BaseMap
- Faction
- ConquestSavedData
- Battle Map Web Page & Site
- Person
- net.minecraft.world.level.block.state.BlockState
- PveCommands.java
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- org.jetbrains.annotations.Nullable
- HorizontalStretch
- CityManagerScreen
- .plan
- org.spongepowered.asm.mixin.Mixin
- CityTerrain
- WarCouncil
- net.minecraft.core.BlockPos
- .state
- net.minecraft.network.RegistryFriendlyByteBuf
- Grid
- PveCommands Registration
- School
- CityPlan
- Peerage
- Machine
- Arm
- Politics.Front
- rafasconquest.faction.Faction
- Build
- Front
- GateDoors
- NobleTitle
- FieldBlock
- Personalities
- SeaLanes
- BattleSounds
- BattlePlan
- ProvinceMap
- .plan
- Barracks
- Cited Findings
- CityStyles
- SeatCities
- Faction Map View
- .step
- ClientScreens
- MapContext
- TitleMapTest
- Bodies
- StyleLibrary
- FormationSnapshot
- .siege
- BattleMap.java
- Row
- .generate
- net.minecraft.world.item.DyeColor
- .view
- MapLands
- RecruitsArrowCleanerMixin.java
- CityLayoutTest
- .board
- Plan
- Duty
- net.minecraft.server.level.ServerLevel
- Ownership & Territory
- .wallSegments
- .layout
- Land
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- GoldChurch.java
- .postChunk
- Terrain
- Grade
- net.minecraft.core.BlockPos
- FactionInfoScreen
- Point
- Quest
- Cloth
- FigureBatches
- net.minecraft.core.Vec3i
- Cited Findings
- Morale
- Override
- rafasconquest.battle.FieldBlock
- net.neoforged.bus.api.SubscribeEvent
- SiegeWorks
- Retinues
- WaterSwell
- GrappleHook
- Body
- Trait
- .view
- .build
- GrandStrategyScreen
- .column
- CapitalLayout
- CityTerrainPiece
- com.mojang.blaze3d.vertex.PoseStack
- ConquestEvents.java
- CityGates.java
- BattleShips
- net.minecraft.client.gui.GuiGraphics
- net.minecraft.resources.ResourceLocation
- .shift
- .choose
- SeaCalm
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- Kit
- .offTheBack
- Phase
- Building
- Townsperson
- 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound
- CLAUDE.md
- How do world generation and structure placement work, and what do they cost?
- Clash
- .place
- .roadStart
- .place
- .platform
- RecruitFigures
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- .moraleOf
- UnitOrder
- ClientPolitics
- NobleAnnex
- BattleSnapshot
- MapView
- .noble
- .passOnOrders
- .touches
- CourtView
- .onClientExtensions
- RecruitFigures.java
- ChunkMapAccess
- ConquestConfig
- .render
- Sea
- ConquestEvents
- WallTemplates
- Kind
- DistantArmyRenderer
- .boundsAt
- ConquestGameRules
- OrganizationScreen
- Placed
- RecruitsBattle
- Ranks
- BattleType
- Roads
- Settlement
- Profiling run sheet (baseline)
- .measure
- net.minecraft.sounds.SoundEvent
- ChunkMapCameraMixin
- Recruiter
- GatePlanksBlock.java
- Kind
- net.minecraft.client.gui.GuiGraphics
- net.minecraft.server.level.ServerPlayer
- Kind
- Realm
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- .bark
- 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel)
- ConquestServer
- Ride
- Kind
- net.minecraft.world.level.block.entity.BlockEntity
- MarkerSpec
- Task
- CityLayout
- .onLevelLoad
- BattleKeepInventory
- Call
- ThrownFigures
- BoardScreen
- ConquestWorldScreen.java
- .deploy
- Rank
- PoiPrecomputer.java
- RankKits
- assertequals
- SiegeWorks.java
- BlockGrid
- UnitMode
- ClientBarks
- OarFigures
- Realm
- ClientModEvents
- State
- Lands
- ShipKind

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 305 edges
2. `FieldBlock` - 222 edges
3. `FieldBattle` - 211 edges
4. `FieldArmy` - 127 edges
5. `ConquestNetwork` - 123 edges
6. `Point` - 108 edges
7. `Lands` - 89 edges
8. `Siege` - 84 edges
9. `BattleScreen` - 82 edges
10. `Politics` - 79 edges

## Surprising Connections (you probably didn't know these)
- `Every frame pays for disk listings, per-rectangle flushes and unculled soldiers` --references--> `TitleLayer`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/client/map/TitleLayer.java
- `A tick budget and staggered schedules make the fixes stick` --references--> `BaseMap`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/map/BaseMap.java
- `Cited Findings` --references--> `FormationSnapshot`  [INFERRED]
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

## Communities (374 total, 222 thin omitted)

### Community 0 - "ConquestNetwork"
Cohesion: 0.03
Nodes (37): ArtillerySet, Bark, BaseMapPart, BattleSpot, BoardAction, BoardView, CameraAt, ChargeHits (+29 more)

### Community 1 - "TechTreeScreen"
Cohesion: 0.18
Nodes (4): TechTreeScreen, Tech, TechTree, TechTreeTest

### Community 2 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.05
Nodes (10): StylePiece, BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece (+2 more)

### Community 3 - ".army"
Cohesion: 0.07
Nodes (4): DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, OarRowingTest

### Community 4 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.10
Nodes (12): HornCall, ATTACKING, MOVING, RALLYING, RETREATING, Role, BEARER, GENERAL (+4 more)

### Community 5 - "BattleScreen"
Cohesion: 0.08
Nodes (3): BattleScreen, Entry, Menu

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.06
Nodes (9): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), Distant Horizons (recommended mod), Cited Findings, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+1 more)

### Community 7 - "CityGridBuilder"
Cohesion: 0.09
Nodes (9): CityGridBuilder, Ground, Platform, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY (+1 more)

### Community 10 - "GateBlockEntity"
Cohesion: 0.09
Nodes (9): GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING, GateGeometry, Hinge (+1 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 13 - "BaseMap"
Cohesion: 0.07
Nodes (12): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, ClientMapCache, BaseMap, Nation (+4 more)

### Community 14 - "rafasconquest.map.BaseMap"
Cohesion: 0.08
Nodes (6): FeudDuel, Goal, MusterBars, PoliticsSync, War, Wars

### Community 15 - "Faction"
Cohesion: 0.08
Nodes (8): Recruits PvE Addon Design Doc (PDF), Embeddium (recommended mod), Package layout (core, data, worldgen, sim, bridge), RafasConquest (PvE addon for Villager Recruits), Recruits faction registration (bridge package), Villager Recruits, RecruitsBridge, Faction

### Community 16 - "ConquestSavedData"
Cohesion: 0.08
Nodes (6): Cited Findings, ConquestSavedData, MapPreview, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.15
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 19 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.05
Nodes (9): BattleBannerBlock, BattleWallBannerBlock, CommandTableBlock, GateBlock, HelmBlock, OarBlock, QuestBoardBlock, Builder (+1 more)

### Community 20 - "PveCommands.java"
Cohesion: 0.07
Nodes (28): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+20 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 22 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.04
Nodes (3): /council <topic>, /pve politics war <attacker> <defender> [generals], Officers

### Community 25 - ".plan"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.22
Nodes (5): ClientChunkWindowMixin, DhShaderLoaderMixin, DhTerrainShaderMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 27 - "CityTerrain"
Cohesion: 0.18
Nodes (7): CityTerrain, Heights, Side, EAST, NORTH, SOUTH, WEST

### Community 28 - "WarCouncil"
Cohesion: 0.16
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 29 - "net.minecraft.core.BlockPos"
Cohesion: 0.07
Nodes (5): ArtilleryBlock, GrandStrategyBlock, SailBlock, StrategyBoardBlock, WorldMapItem

### Community 31 - "net.minecraft.network.RegistryFriendlyByteBuf"
Cohesion: 0.11
Nodes (7): BackBanner, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel

### Community 32 - "Grid"
Cohesion: 0.20
Nodes (5): Countryside, Plan, Grid, CountrysideTest, World

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.12
Nodes (4): CityPlan, Piece, Plan, NobleDistrictTest

### Community 36 - "Peerage"
Cohesion: 0.09
Nodes (10): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+2 more)

### Community 37 - "Machine"
Cohesion: 0.08
Nodes (13): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+5 more)

### Community 38 - "Arm"
Cohesion: 0.08
Nodes (18): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+10 more)

### Community 40 - "rafasconquest.faction.Faction"
Cohesion: 0.11
Nodes (4): RecruitsFactions, FactionBanner, Layer, FactionBannerTest

### Community 41 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 42 - "Front"
Cohesion: 0.12
Nodes (4): Feud, Lord, Feuds, Front

### Community 43 - "GateDoors"
Cohesion: 0.11
Nodes (3): GateDoors, SableLink, Door

### Community 44 - "NobleTitle"
Cohesion: 0.14
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 45 - "FieldBlock"
Cohesion: 0.04
Nodes (7): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (13): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+5 more)

### Community 50 - "BattleSounds"
Cohesion: 0.25
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 51 - "BattlePlan"
Cohesion: 0.05
Nodes (9): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, WarPlan (+1 more)

### Community 52 - "ProvinceMap"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 53 - ".plan"
Cohesion: 0.16
Nodes (5): Centre, Faith, Bond, RealmPlanner, Seat

### Community 55 - "Cited Findings"
Cohesion: 0.07
Nodes (13): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, ArrowVolleys, BattleSpotRing, DistantOcclusion (+5 more)

### Community 57 - "SeatCities"
Cohesion: 0.12
Nodes (3): SeatCities, SeatCitiesTest, World

### Community 58 - "Faction Map View"
Cohesion: 0.12
Nodes (4): Cited Findings, BaseMapLayer, FactionMapScreen, MapView

### Community 59 - ".step"
Cohesion: 0.12
Nodes (6): FigureMotion, Ground, Match, Placed, State, FigureMotionTest

### Community 61 - "MapContext"
Cohesion: 0.10
Nodes (9): MapContext, LegendEntry, MapLayer, Symbol, Place, PlaceMenuLayer, PoiIconLayer, TerritoryLayer (+1 more)

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.10
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - "FormationSnapshot"
Cohesion: 0.13
Nodes (12): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY (+4 more)

### Community 69 - "BattleMap.java"
Cohesion: 0.05
Nodes (4): Battle map developer web page, Morale, rout and rally, StreetWalks, RafasConquest

### Community 71 - ".generate"
Cohesion: 0.06
Nodes (19): ProvinceNames, BaseMapGenerator, Heights, Homeland, DESERT, JUNGLE, PLAINS, SAVANNA (+11 more)

### Community 72 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.14
Nodes (8): DistantShips, Far, Riding, Want, BattleKind, Part, ShipHulls, DistantShipsTest

### Community 73 - ".view"
Cohesion: 0.18
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 74 - "MapLands"
Cohesion: 0.09
Nodes (5): Gaps, Inferences, Takeaway, Where are full scans, nested loops, repeated lookups and per-tick allocations?, MapLands

### Community 75 - "RecruitsArrowCleanerMixin.java"
Cohesion: 0.18
Nodes (5): Cited Findings, Cited Findings, CityVegetationMixin, RecruitsArrowCleanerMixin, TrackedEntityCameraMixin

### Community 76 - "CityLayoutTest"
Cohesion: 0.20
Nodes (3): ChunkCoord, FarmLayout, CityLayoutTest

### Community 77 - ".board"
Cohesion: 0.15
Nodes (5): DataFiles, Dialogue, Kind, Quests, Target

### Community 78 - "Plan"
Cohesion: 0.23
Nodes (4): Edge, CARVE, FILL, Plan

### Community 79 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 81 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.15
Nodes (5): Land, Result, SiegeSurvey, Wall, SiegeSurveyTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".wallSegments"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 84 - ".layout"
Cohesion: 0.14
Nodes (7): Cited Findings, Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 85 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 86 - "Calibration"
Cohesion: 0.15
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.05
Nodes (30): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+22 more)

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "AgeTest.java"
Cohesion: 0.14
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - "GoldChurch.java"
Cohesion: 0.27
Nodes (3): PlacedWall, GoldChurch, Layout

### Community 92 - ".postChunk"
Cohesion: 0.12
Nodes (4): Cited Findings, BarracksPeople, CityGuards, CharacterMarkers

### Community 96 - "Grade"
Cohesion: 0.15
Nodes (12): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+4 more)

### Community 98 - "net.minecraft.core.BlockPos"
Cohesion: 0.10
Nodes (6): PlannedTent, Saved, WarCamp, CourtSet, Seat, Style

### Community 100 - "Point"
Cohesion: 0.05
Nodes (18): Point, Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate (+10 more)

### Community 101 - "Quest"
Cohesion: 0.16
Nodes (9): Ladder, MILITARY, NOBLE, NONE, Quest, Type, CLEAR, DELIVER (+1 more)

### Community 102 - "Cloth"
Cohesion: 0.13
Nodes (4): Cloth, Solid, ClothTest, WoolTest

### Community 103 - "FigureBatches"
Cohesion: 0.06
Nodes (12): ShipSprite, Batch, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED (+4 more)

### Community 106 - "Cited Findings"
Cohesion: 0.14
Nodes (7): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, Arrow, DistantArmies

### Community 109 - "rafasconquest.battle.FieldBlock"
Cohesion: 0.10
Nodes (7): Figure, Live, Shape, LINE, SQUARE, V, WEDGE

### Community 111 - "SiegeWorks"
Cohesion: 0.06
Nodes (33): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+25 more)

### Community 112 - "Retinues"
Cohesion: 0.15
Nodes (4): PlayerStats, View, Retinues, RetinuesTest

### Community 113 - "WaterSwell"
Cohesion: 0.10
Nodes (9): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Made, WaterSwell, Swell (+1 more)

### Community 115 - "Body"
Cohesion: 0.07
Nodes (32): Weapon, BOW, GREAT, LONG, NONE, SWORD, Body, BALLISTA (+24 more)

### Community 116 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 117 - ".view"
Cohesion: 0.24
Nodes (7): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, GrandStrategy, View

### Community 118 - ".build"
Cohesion: 0.12
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.15
Nodes (3): GrandStrategyScreen, Spot, GrandStrategyDateTest

### Community 122 - "CapitalLayout"
Cohesion: 0.12
Nodes (8): CapitalLayout, Land, Site, OriginRoller, Roll, WorldSettings, CapitalRelocationTest, LayoutTest

### Community 124 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.05
Nodes (13): BackBannerLayer, BannerArt, BattleBannerRenderer, CapeCurio, ClothItemRenderer, Carried, ClothRenderer, Fabric (+5 more)

### Community 125 - "ConquestEvents.java"
Cohesion: 0.03
Nodes (12): /pve debug citystats [samples], GrappleHooks, FactionBannerNbt, ConquestEntities, CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen (+4 more)

### Community 128 - "BattleShips"
Cohesion: 0.16
Nodes (6): Hull, Ramming, BattleShips, Helm, Ram, RammingTest

### Community 132 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 133 - ".choose"
Cohesion: 0.17
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 134 - "SeaCalm"
Cohesion: 0.24
Nodes (4): Depths, SeaCalm, Strengths, SeaCalmTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (23): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, Cited Findings, Cited Findings, Gaps (+15 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.06
Nodes (31): 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings, Cited Findings (+23 more)

### Community 141 - "Kit"
Cohesion: 0.10
Nodes (19): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+11 more)

### Community 143 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 148 - "Townsperson"
Cohesion: 0.12
Nodes (4): Walk, DistrictKey, Townsfolk, Townsperson

### Community 149 - "9. Memory: client caches, textures and NativeImages that are never freed or grow without bound"
Cohesion: 0.25
Nodes (5): 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Gaps, Inferences, Takeaway

### Community 152 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.32
Nodes (3): Inferences, Api, DistantGeneration

### Community 155 - "Clash"
Cohesion: 0.32
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 157 - ".roadStart"
Cohesion: 0.08
Nodes (4): PlaceMusic, CitySizesTest, StreetWalksTest, PlaceMusicTest

### Community 160 - ".place"
Cohesion: 0.14
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 164 - "RecruitFigures"
Cohesion: 0.08
Nodes (9): FigureGear, Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures (+1 more)

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.11
Nodes (6): WarHorn, BattleStandardItem, Armoury, Outfit, Style, ItemFlags

### Community 174 - ".moraleOf"
Cohesion: 0.11
Nodes (17): 1. Combat is per soldier, not per unit, 1. Flank and rear shock, 2. Does the swing land?, 2. Nearby rout (fright), 3. Army-wide losses, 3. How much damage the hit does, 4. Charges, 4. Shattering (+9 more)

### Community 178 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 205 - "MapView"
Cohesion: 0.29
Nodes (3): Hit, LabelLayer, LabelShape

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 221 - "CourtView"
Cohesion: 0.07
Nodes (7): Retainer, Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 228 - ".render"
Cohesion: 0.10
Nodes (7): CharacterScreen, Spot, ClientPeerage, Banner, Portraits, Honour, Honours

### Community 236 - "Kind"
Cohesion: 0.22
Nodes (7): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.09
Nodes (4): DistantDepth, DistantArmyRenderer, Figure, Rects

### Community 242 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 252 - "RecruitsBattle"
Cohesion: 0.04
Nodes (3): Cited Findings, GeneralTask, RecruitsBattle

### Community 254 - "Ranks"
Cohesion: 0.08
Nodes (13): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+5 more)

### Community 255 - "BattleType"
Cohesion: 0.13
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 256 - "Roads"
Cohesion: 0.08
Nodes (17): Cited Findings, Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction (+9 more)

### Community 257 - "Settlement"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 266 - "ChunkMapCameraMixin"
Cohesion: 0.27
Nodes (6): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway, ChunkMapCameraMixin

### Community 272 - "Kind"
Cohesion: 0.40
Nodes (5): Column, Kind, NONE, PLATE, SLOPE

### Community 274 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.03
Nodes (7): Character page, Court 3D scene (CourtView), Demographics (population by culture and race), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), GateScreen, InventoryStatsButton

### Community 275 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.09
Nodes (6): Camera, CameraView, Card, Enlisted, Unit, CityManager

### Community 276 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 277 - "Realm"
Cohesion: 0.10
Nodes (5): Hook, Succession, Taking, RealmPlannerTest, World

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.21
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 283 - "1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel)"
Cohesion: 0.40
Nodes (5): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), Cited Findings, Gaps, Inferences, Takeaway

### Community 285 - "ConquestServer"
Cohesion: 0.12
Nodes (4): CityBanners, ConquestServer, StructureLink, PoiPrecomputer

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "net.minecraft.world.level.block.entity.BlockEntity"
Cohesion: 0.05
Nodes (5): ArtilleryMount, BattleBannerBlockEntity, ConquestContent, Oar, Sail

### Community 292 - "MarkerSpec"
Cohesion: 0.15
Nodes (3): MarkerRenderer, MarkerBlockEntity, MarkerSpec

### Community 294 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 295 - "CityLayout"
Cohesion: 0.09
Nodes (4): CityLayout, District, Site, SlopeTile

### Community 305 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 310 - "ConquestWorldScreen.java"
Cohesion: 0.10
Nodes (6): Chunks, BattleSite.Water, BattleSite.Water, ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 313 - ".deploy"
Cohesion: 0.11
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 315 - "Rank"
Cohesion: 0.12
Nodes (11): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+3 more)

### Community 318 - "PoiPrecomputer.java"
Cohesion: 0.07
Nodes (14): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoliticalMap (+6 more)

### Community 321 - "assertequals"
Cohesion: 0.05
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 324 - "SiegeWorks.java"
Cohesion: 0.03
Nodes (47): Ambushes from woods, Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings) (+39 more)

### Community 326 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 330 - "OarFigures"
Cohesion: 0.14
Nodes (4): OarFigures, Rowing, Columns, SableSeen

### Community 336 - "Realm"
Cohesion: 0.14
Nodes (4): Kind, FRIEND, RIVAL, Realm

### Community 367 - "State"
Cohesion: 0.40
Nodes (5): State, RALLIED, ROUTED, SHATTERED, STEADY

### Community 369 - "Lands"
Cohesion: 0.06
Nodes (15): Cited Findings, Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Lands, Stronghold (+7 more)

### Community 370 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **414 isolated node(s):** `CLOSED`, `CLOSING`, `OPEN`, `OPENING`, `LADDERS` (+409 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1377 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **222 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `net.minecraft.nbt.CompoundTag`, `.army`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `rafasconquest.map.BaseMap`, `Phase`, `net.minecraft.server.level.ServerPlayer`, `PveCommands.java`, `org.jetbrains.annotations.Nullable`, `WarCouncil`, `.state`, `PveCommands Registration`, `Peerage`, `Machine`, `.onServerTick`, `net.minecraft.world.item.ItemStack`, `rafasconquest.faction.Faction`, `GateDoors`, `FieldBlock`, `UnitOrder`, `ConquestWorldScreen.java`, `.siege`, `BattleMap.java`, `Row`, `UnitMode`, `BattleSnapshot`, `net.minecraft.server.level.ServerLevel`, `Calibration`, `Doctrine`, `.passOnOrders`, `net.minecraft.core.BlockPos`, `rafasconquest.battle.FieldBlock`, `SiegeWorks`, `GrappleHook`, `Trait`, `ConquestEvents.java`?**
  _High betweenness centrality (0.073) - this node is a cross-community bridge._
- **What connects `CLOSED`, `CLOSING`, `OPEN` to the rest of the system?**
  _414 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `ConquestNetwork` be split into smaller, more focused modules?**
  _Cohesion score 0.028622107554623882 - nodes in this community are weakly interconnected._
- **Why does `FieldBattle` connect `FieldBlock` to `Bodies`, `assertequals`, `PveCommands Registration`, `.army`, `.siege`, `Point`, `Arm`, `Machine`, `Morale`, `Call`, `BattlePlan`, `PveCommands.java`, `.deploy`, `.passOnOrders`, `.state`, `BattleType`?**
  _High betweenness centrality (0.031) - this node is a cross-community bridge._
- **Should `net.minecraft.nbt.CompoundTag` be split into smaller, more focused modules?**
  _Cohesion score 0.053268765133171914 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._