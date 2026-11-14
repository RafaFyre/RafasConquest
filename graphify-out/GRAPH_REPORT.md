# Graph Report - RafasConquest  (2026-11-13)

## Corpus Check
- 542 files · ~467,470 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7463 nodes · 23982 edges · 382 communities (151 shown, 231 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3160 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `5963d9b0`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- net.minecraft.network.FriendlyByteBuf
- org.jetbrains.annotations.Nullable
- net.minecraft.nbt.CompoundTag
- .army
- GateState
- BattleScreen
- DistantHorizonsArmies.java
- CityGridBuilder
- PoliticsTest
- arraylist
- GateBlockEntity
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- .world
- BaseMap
- rafasconquest.map.BaseMap
- Faction
- ConquestSavedData
- Battle Map Web Page & Site
- Person
- MarkerBlock.java
- PveCommands.java
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- org.jetbrains.annotations.Nullable
- HorizontalStretch
- CityManagerScreen
- .plan
- org.spongepowered.asm.mixin.Mixin
- net.minecraft.world.entity.LivingEntity
- WarCouncil
- registries
- net.minecraft.server.level.ServerPlayer
- net.minecraft.network.RegistryFriendlyByteBuf
- CameraView
- PveCommands Registration
- School
- CityPlan
- Peerage
- Machine
- Arm
- Politics.Front
- rafasconquest.faction.Faction
- SeatCitiesTest
- Front
- CityVariant
- FieldBlock
- Personalities
- SeaLanes
- rafasconquest.battle.FormationSnapshot
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
- Homeland
- TitleMapTest
- Bodies
- StyleLibrary
- FormationSnapshot
- Point
- BattleMap.java
- Row
- .generate
- net.minecraft.world.item.DyeColor
- .view
- MapLands
- CourtSet
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- .board
- ConquestEvents.java
- .send
- net.minecraft.server.level.ServerLevel
- Ownership & Territory
- CityLayout
- .layout
- Land
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- Front
- .postChunk
- Terrain
- net.neoforged.neoforge.network.handling.IPayloadContext
- Grade
- net.minecraft.core.BlockPos
- FactionInfoScreen
- Siege
- Quest
- Cloth
- FigureBatches
- net.minecraft.core.Vec3i
- .onLogout
- Morale
- Override
- rafasconquest.battle.FieldBlock
- net.neoforged.bus.api.SubscribeEvent
- SiegeWorks
- Retinues
- WaterSwell
- CountryStructure
- Body
- MapView
- .view
- .build
- GrandStrategyScreen
- CityTerrain
- WorldSettings
- CityTerrainPiece
- com.mojang.blaze3d.vertex.PoseStack
- optional
- .at
- BattleShips
- net.minecraft.client.gui.GuiGraphics
- net.minecraft.resources.ResourceLocation
- .walks
- .shift
- .choose
- SeaCalm
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- Kit
- Tier
- .rightClick
- View
- Townsperson
- Design: adding the four morale shocks (built 2026-11-11, untested in game)
- CLAUDE.md
- How do world generation and structure placement work, and what do they cost?
- .place
- CityVegetationMixin.java
- .place
- .world
- RecruitFigures
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- .moraleOf
- UnitOrder
- ClientPolitics
- BattleSnapshot
- MapView
- .noble
- Duty
- Task
- .touches
- CourtView
- BlockPos
- Cited Findings
- Trait
- .render
- 4. Depth buffers and post effects (DH depth, occlusion, GPU sync)
- ConquestEvents
- RafasConquest (PvE addon for Villager Recruits)
- Kind
- DistantArmyRenderer
- .boundsAt
- ConquestGameRules
- OrganizationScreen
- .say
- GateScreen
- Role
- Roads
- Settlement
- Profiling run sheet (baseline)
- .measure
- net.minecraft.sounds.SoundEvent
- ChunkMapCameraMixin.java
- Recruiter
- GatePlanksBlock.java
- SableSeen
- ConquestNetwork.java
- RecruitsBattle
- Kind
- Realm
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- .bark
- Politics
- PoiPrecomputer
- SwellTest
- Kind
- net.minecraft.world.level.block.entity.BlockEntity
- WallTemplates
- .deployment
- 5. Water/ocean waves (sea swell, foam, boats on waves)
- .findGenerationPoint
- .onLevelLoad
- NobleDistrictTest
- TeleportTo
- Feud
- ShipKind
- BattleKeepInventory
- Call
- BattleType
- BoardScreen
- ConquestWorldScreen.java
- .order
- .plus
- Ranks
- uuid
- .enlist
- assertequals
- .onClientExtensions
- SiegeWorks.java
- Build
- ClientBarks
- .groundCoversEveryTent
- OarFigures
- Sea
- BlockGrid
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- ClientModEvents
- ShareLiveTest
- WorldStretch
- Lands
- State

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 302 edges
2. `FieldBlock` - 222 edges
3. `FieldBattle` - 212 edges
4. `FieldArmy` - 127 edges
5. `ConquestNetwork` - 124 edges
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
- `Cited Findings` --references--> `CityVegetationMixin`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/mixin/CityVegetationMixin.java

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

## Communities (382 total, 231 thin omitted)

### Community 0 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.06
Nodes (9): BaseMapPart, BattleSpot, FigureStrike, HornOrders, PeerageView, PoliticsView, TalkChoice, UnitsRequest (+1 more)

### Community 2 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.06
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - ".army"
Cohesion: 0.05
Nodes (8): DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, SiegeTest, GrandStrategyDateTest, OarRowingTest, ArmouryTest, RetinuesTest

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.07
Nodes (5): Cited Findings, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "CityGridBuilder"
Cohesion: 0.09
Nodes (6): Houses, CityGridBuilder, Ground, PlacedWall, Platform, PlaceMusicTest

### Community 9 - "arraylist"
Cohesion: 0.08
Nodes (3): Mantra schools and dojos, Roads (high, city, town roads; bridges; signposts), Seat cities

### Community 10 - "GateBlockEntity"
Cohesion: 0.08
Nodes (9): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+1 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - ".world"
Cohesion: 0.15
Nodes (4): Honour, Honours, RealmPlannerTest, World

### Community 13 - "BaseMap"
Cohesion: 0.08
Nodes (12): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, Junction, MapPreview, BaseMap (+4 more)

### Community 14 - "rafasconquest.map.BaseMap"
Cohesion: 0.08
Nodes (6): Army, Goal, MusterBars, PoliticsSync, War, Wars

### Community 15 - "Faction"
Cohesion: 0.14
Nodes (3): Faction, ResourceType, FOOD

### Community 16 - "ConquestSavedData"
Cohesion: 0.10
Nodes (4): Cited Findings, ConquestSavedData, EconomyTick, ConquestSavedDataTest

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.15
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 19 - "MarkerBlock.java"
Cohesion: 0.04
Nodes (12): ArtilleryBlock, CommandTableBlock, GateBlock, GrandStrategyBlock, HelmBlock, OarBlock, QuestBoardBlock, SailBlock (+4 more)

### Community 20 - "PveCommands.java"
Cohesion: 0.10
Nodes (30): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+22 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.16
Nodes (6): Nation, Nations, Plan, Seats, NationsTest, World

### Community 26 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.23
Nodes (5): ClientChunkWindowMixin, DhShaderLoaderMixin, DhTerrainShaderMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 28 - "WarCouncil"
Cohesion: 0.18
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 34 - "School"
Cohesion: 0.09
Nodes (14): Dojos, Ground, School, BLOOD, FIRE, ICE, IRON, LIGHT (+6 more)

### Community 35 - "CityPlan"
Cohesion: 0.13
Nodes (3): CityPlan, Piece, Plan

### Community 36 - "Peerage"
Cohesion: 0.10
Nodes (7): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageSync, PeerageTest

### Community 37 - "Machine"
Cohesion: 0.08
Nodes (14): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+6 more)

### Community 38 - "Arm"
Cohesion: 0.08
Nodes (15): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+7 more)

### Community 40 - "rafasconquest.faction.Faction"
Cohesion: 0.10
Nodes (5): RecruitsFactions, FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 42 - "Front"
Cohesion: 0.13
Nodes (4): Feud, Lord, Feuds, Front

### Community 43 - "CityVariant"
Cohesion: 0.15
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 45 - "FieldBlock"
Cohesion: 0.04
Nodes (11): The core change: morale becomes a sum, FieldArmy, Event, FieldBattle, Order, Outcome, Target, Volley (+3 more)

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.14
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 50 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.10
Nodes (10): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, BattleSounds, ChargeSound, DinSound (+2 more)

### Community 52 - "ProvinceMap"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 53 - ".plan"
Cohesion: 0.13
Nodes (7): Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 55 - "Cited Findings"
Cohesion: 0.06
Nodes (14): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, Arrow, ArrowVolleys, BattleSpotRing (+6 more)

### Community 57 - "SeatCities"
Cohesion: 0.13
Nodes (4): City, Grid, SeatCities, CityIndex

### Community 59 - ".step"
Cohesion: 0.14
Nodes (5): FigureMotion, Match, Placed, State, FigureMotionTest

### Community 61 - "Homeland"
Cohesion: 0.10
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 63 - "TitleMapTest"
Cohesion: 0.13
Nodes (5): Gaps, TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.10
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.11
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - "FormationSnapshot"
Cohesion: 0.13
Nodes (12): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY (+4 more)

### Community 67 - "Point"
Cohesion: 0.12
Nodes (5): Point, Face, Gate, Plan, WallTower

### Community 69 - "BattleMap.java"
Cohesion: 0.04
Nodes (4): Battle map developer web page, Morale, rout and rally, StreetWalks, RafasConquest

### Community 71 - ".generate"
Cohesion: 0.09
Nodes (7): Dojo, ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 72 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.14
Nodes (8): DistantShips, Far, Riding, Want, BattleKind, Part, ShipHulls, DistantShipsTest

### Community 73 - ".view"
Cohesion: 0.18
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 75 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 76 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.07
Nodes (27): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+19 more)

### Community 77 - ".board"
Cohesion: 0.08
Nodes (13): TechTreeScreen, DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind (+5 more)

### Community 78 - "ConquestEvents.java"
Cohesion: 0.04
Nodes (6): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), TickWeave experiment, Wind

### Community 79 - ".send"
Cohesion: 0.09
Nodes (12): RequestBaseMap, SetSetting, StatsRequest, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel (+4 more)

### Community 81 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.09
Nodes (8): Land, Result, SiegeSurvey, Wall, PlaceMusic, CityGates, Done, SiegeSurveyTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.20
Nodes (5): Candidate, OwnershipResolver, Claim, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "CityLayout"
Cohesion: 0.07
Nodes (10): CityLayout, Side, EAST, NORTH, SOUTH, WEST, Site, SlopeTile (+2 more)

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
Cohesion: 0.07
Nodes (21): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+13 more)

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - "Front"
Cohesion: 0.11
Nodes (6): Engine, LADDERS, RAMS, TOWERS, Front, Rows

### Community 92 - ".postChunk"
Cohesion: 0.12
Nodes (3): BarracksPeople, CityGuards, ConquestServer

### Community 95 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.13
Nodes (3): CameraAt, CityView, StatsView

### Community 96 - "Grade"
Cohesion: 0.15
Nodes (11): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+3 more)

### Community 98 - "net.minecraft.core.BlockPos"
Cohesion: 0.07
Nodes (7): PlannedTent, Saved, WarCamp, GateGeometry, CharacterMarkers, Placed, GateGeometryTest

### Community 99 - "FactionInfoScreen"
Cohesion: 0.16
Nodes (3): FactionInfoScreen, Line, StructureLink

### Community 100 - "Siege"
Cohesion: 0.14
Nodes (6): GateAction, Region, IN, OUT, WALL, Siege

### Community 101 - "Quest"
Cohesion: 0.10
Nodes (10): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+2 more)

### Community 102 - "Cloth"
Cohesion: 0.09
Nodes (5): Cloth, Solid, ClothTest, OffTheBackTest, WoolTest

### Community 103 - "FigureBatches"
Cohesion: 0.08
Nodes (10): Batch, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE, FigureBatches (+2 more)

### Community 105 - "net.minecraft.core.Vec3i"
Cohesion: 0.15
Nodes (3): StylePiece, GoldChurch, Layout

### Community 106 - ".onLogout"
Cohesion: 0.12
Nodes (4): BackBanners, ClientMapCache, Throw, ThrownFigures

### Community 107 - "Morale"
Cohesion: 0.19
Nodes (8): Morale, State, RALLIED, ROUTED, SHATTERED, STEADY, UnitInfo, UnitsState

### Community 109 - "rafasconquest.battle.FieldBlock"
Cohesion: 0.06
Nodes (5): Cited Findings, Figure, Landing, Live, ChargeHits

### Community 112 - "Retinues"
Cohesion: 0.20
Nodes (3): PlayerStats, View, Retinues

### Community 113 - "WaterSwell"
Cohesion: 0.16
Nodes (7): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Made, WaterSwell

### Community 115 - "Body"
Cohesion: 0.07
Nodes (32): Weapon, BOW, GREAT, LONG, NONE, SWORD, Body, BALLISTA (+24 more)

### Community 116 - "MapView"
Cohesion: 0.17
Nodes (6): LegendEntry, MapLayer, Symbol, PoiIconLayer, TerritoryLayer, UnitLayer

### Community 117 - ".view"
Cohesion: 0.24
Nodes (7): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, GrandStrategy, View

### Community 118 - ".build"
Cohesion: 0.14
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 121 - "CityTerrain"
Cohesion: 0.07
Nodes (18): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+10 more)

### Community 122 - "WorldSettings"
Cohesion: 0.07
Nodes (12): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, NobleAnnex, OriginRoller, Roll (+4 more)

### Community 124 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.05
Nodes (14): BackBannerLayer, BannerArt, BattleBannerRenderer, CapeCurio, ClothItemRenderer, Carried, ClothRenderer, Fabric (+6 more)

### Community 125 - "optional"
Cohesion: 0.04
Nodes (8): ConquestConfig, CityDistrictStructure, CitySlopeStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 128 - "BattleShips"
Cohesion: 0.13
Nodes (7): Hull, Ramming, BattleShips, Helm, Ram, SableLink, RammingTest

### Community 130 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.13
Nodes (4): GrappleHookRenderer, RecruiterRenderer, TownspersonRenderer, GrappleHook

### Community 132 - ".shift"
Cohesion: 0.16
Nodes (6): BattleSite, Part, Water, BattleSite.Water, BattleSite.Water, BattleSiteTest

### Community 133 - ".choose"
Cohesion: 0.14
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 134 - "SeaCalm"
Cohesion: 0.19
Nodes (5): Ground, Depths, SeaCalm, Strengths, SeaCalmTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.07
Nodes (28): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings (+20 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.07
Nodes (26): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings, Cited Findings, Cited Findings (+18 more)

### Community 141 - "Kit"
Cohesion: 0.11
Nodes (19): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+11 more)

### Community 142 - "Tier"
Cohesion: 0.29
Nodes (5): Tier, COUNTY, DUCHY, EMPIRE, KINGDOM

### Community 146 - "View"
Cohesion: 0.14
Nodes (3): BoardView, GrandStrategyView, TalkView

### Community 148 - "Townsperson"
Cohesion: 0.12
Nodes (4): Walk, DistrictKey, Townsfolk, Townsperson

### Community 149 - "Design: adding the four morale shocks (built 2026-11-11, untested in game)"
Cohesion: 0.29
Nodes (6): 1. Flank and rear shock, 2. Nearby rout (fright), 3. Army-wide losses, 4. Shattering, Design: adding the four morale shocks (built 2026-11-11, untested in game), Tests (FieldBattleTest)

### Community 152 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.33
Nodes (3): Inferences, Api, DistantGeneration

### Community 160 - ".place"
Cohesion: 0.16
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 164 - "RecruitFigures"
Cohesion: 0.08
Nodes (9): FigureGear, Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures (+1 more)

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.10
Nodes (7): WarHorn, BattleStandardItem, Armoury, Outfit, Style, ItemFlags, RankKits

### Community 174 - ".moraleOf"
Cohesion: 0.17
Nodes (11): 1. Combat is per soldier, not per unit, 2. Does the swing land?, 3. How much damage the hit does, 4. Charges, 5. Missiles, 6. Leadership (morale): what actually wins battles, 7. Autoresolve, 8. Older games (Shogun 1, Medieval 1, Rome 1) (+3 more)

### Community 178 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 205 - "MapView"
Cohesion: 0.11
Nodes (9): Cited Findings, BaseMapLayer, Hit, LabelLayer, MapContext, MapView, Place, PlaceMenuLayer (+1 more)

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 217 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 218 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 221 - "CourtView"
Cohesion: 0.08
Nodes (6): Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 223 - "BlockPos"
Cohesion: 0.19
Nodes (4): GateSet, GateView, MarkerSet, MarkerView

### Community 225 - "Cited Findings"
Cohesion: 0.14
Nodes (14): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway, Cited Findings, Cited Findings, Gaps (+6 more)

### Community 226 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 228 - ".render"
Cohesion: 0.10
Nodes (6): CharacterScreen, Retainer, Spot, ClientPeerage, Banner, Portraits

### Community 232 - "4. Depth buffers and post effects (DH depth, occlusion, GPU sync)"
Cohesion: 0.40
Nodes (5): 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), Cited Findings, Gaps, Inferences, Takeaway

### Community 235 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.17
Nodes (8): Arrows in flight (figure volleys), Battle sounds, Recruits PvE Addon Design Doc (PDF), Seeing the whole field (distant flat figures), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 236 - "Kind"
Cohesion: 0.25
Nodes (7): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.06
Nodes (6): DistantDepth, DistantArmyRenderer, Figure, Rects, ShipSprite, Corners

### Community 242 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 252 - ".say"
Cohesion: 0.06
Nodes (9): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT (+1 more)

### Community 255 - "Role"
Cohesion: 0.17
Nodes (9): GameCharacter, Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN (+1 more)

### Community 256 - "Roads"
Cohesion: 0.12
Nodes (13): Grade, CITY, DOJO, HIGH, SEA, TOWN, Link, Place (+5 more)

### Community 257 - "Settlement"
Cohesion: 0.15
Nodes (10): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+2 more)

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 274 - "ConquestNetwork.java"
Cohesion: 0.04
Nodes (8): InventoryStatsButton, CityClose, ConquestNetwork, FactionInfoReply, OpenBattleScreen, Place, RequestFactionInfo, SeaSwell

### Community 275 - "RecruitsBattle"
Cohesion: 0.03
Nodes (30): Cited Findings, Camera, Card, Enlisted, HornCall, ATTACKING, MOVING, RALLYING (+22 more)

### Community 276 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 277 - "Realm"
Cohesion: 0.12
Nodes (4): Hook, Realm, Succession, Taking

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.14
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips, Ride

### Community 282 - ".bark"
Cohesion: 0.33
Nodes (3): Bark, Barks, Ear

### Community 283 - "Politics"
Cohesion: 0.10
Nodes (11): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Event, Muster, CEREMONIAL (+3 more)

### Community 285 - "PoiPrecomputer"
Cohesion: 0.06
Nodes (11): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Poi, PoiType, CAPITAL, CITY, FARM (+3 more)

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "net.minecraft.world.level.block.entity.BlockEntity"
Cohesion: 0.05
Nodes (7): ArtilleryMount, BattleBannerBlockEntity, ConquestContent, Oar, Sail, MarkerBlockEntity, MarkerSpec

### Community 294 - "5. Water/ocean waves (sea swell, foam, boats on waves)"
Cohesion: 0.40
Nodes (5): 5. Water/ocean waves (sea swell, foam, boats on waves), Cited Findings, Gaps, Inferences, Takeaway

### Community 295 - ".findGenerationPoint"
Cohesion: 0.14
Nodes (3): District, CityPlacement, CityStructure

### Community 301 - "Feud"
Cohesion: 0.18
Nodes (5): Clash, Kind, DUEL, SKIRMISH, FeudDuel

### Community 302 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 305 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 306 - "BattleType"
Cohesion: 0.18
Nodes (5): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE

### Community 310 - "ConquestWorldScreen.java"
Cohesion: 0.15
Nodes (4): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, SettingsScreen

### Community 311 - ".order"
Cohesion: 0.22
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

### Community 313 - ".plus"
Cohesion: 0.10
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 315 - "Ranks"
Cohesion: 0.06
Nodes (21): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+13 more)

### Community 318 - "uuid"
Cohesion: 0.05
Nodes (8): SeedHash, TerritoryMap, FactionType, BANDIT, CITY_STATE, EMPIRE, PoliticalMap, FactionInfo

### Community 320 - ".enlist"
Cohesion: 0.11
Nodes (4): GeneralTask, Ranks, Offer, Recruitment

### Community 321 - "assertequals"
Cohesion: 0.06
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 324 - "SiegeWorks.java"
Cohesion: 0.04
Nodes (44): Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Character page, Characters (holders, skills, prowess, traits), Command Table block (+36 more)

### Community 325 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 369 - "Lands"
Cohesion: 0.09
Nodes (4): Cited Findings, Lands, Stronghold, Claim

### Community 376 - "State"
Cohesion: 0.50
Nodes (4): State, CARRIED, RAISING, UP

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **414 isolated node(s):** `IN`, `OUT`, `WALL`, `NO_LIGHTMAP`, `NO_SHADER_COLOUR` (+409 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1377 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **231 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `BattleShips`, `org.jetbrains.annotations.Nullable`, `net.minecraft.nbt.CompoundTag`, `.army`, `.shift`, `net.minecraft.resources.ResourceLocation`, `PveCommands.java`, `org.jetbrains.annotations.Nullable`, `WarCouncil`, `net.minecraft.server.level.ServerPlayer`, `PveCommands Registration`, `net.minecraft.world.item.ItemStack`, `rafasconquest.faction.Faction`, `.isOurs`, `FieldBlock`, `Feud`, `UnitOrder`, `.enlist`, `BattleMap.java`, `Row`, `ConquestEvents.java`, `net.minecraft.server.level.ServerLevel`, `Calibration`, `net.minecraft.core.BlockPos`, `Trait`, `rafasconquest.battle.FieldBlock`, `net.neoforged.bus.api.SubscribeEvent`, `SiegeWorks`, `.sendBackBanner`, `.say`, `Role`?**
  _High betweenness centrality (0.069) - this node is a cross-community bridge._
- **What connects `IN`, `OUT`, `WALL` to the rest of the system?**
  _414 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `net.minecraft.network.FriendlyByteBuf` be split into smaller, more focused modules?**
  _Cohesion score 0.058693244739756366 - nodes in this community are weakly interconnected._
- **Why does `ConquestNetwork` connect `ConquestNetwork.java` to `net.minecraft.network.FriendlyByteBuf`, `org.jetbrains.annotations.Nullable`, `BattleScreen`, `arraylist`, `rafasconquest.map.BaseMap`, `.rightClick`, `View`, `MarkerBlock.java`, `RecruitsBattle`, `org.jetbrains.annotations.Nullable`, `CityManagerScreen`, `.bark`, `net.minecraft.network.RegistryFriendlyByteBuf`, `PveCommands Registration`, `Machine`, `TeleportTo`, `BoardScreen`, `ClientScreens`, `BattleSnapshot`, `.send`, `BlockPos`, `net.neoforged.neoforge.network.handling.IPayloadContext`, `Morale`, `rafasconquest.battle.FieldBlock`, `WorldStretch`, `.sendBackBanner`, `.say`?**
  _High betweenness centrality (0.037) - this node is a cross-community bridge._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.04982456140350877 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._