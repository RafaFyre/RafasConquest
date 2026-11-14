# Graph Report - RafasConquest  (2026-11-13)

## Corpus Check
- 543 files · ~468,414 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7472 nodes · 24011 edges · 380 communities (148 shown, 232 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3161 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `0d3b4edc`
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
- CharacterMarkers.java
- BaseMap
- rafasconquest.map.BaseMap
- Faction
- ConquestSavedData
- Battle Map Web Page & Site
- Person
- MarkerBlock.java
- RecruitsBattle.java
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- HorizontalStretch
- CityManagerScreen
- .plan
- org.spongepowered.asm.mixin.Mixin
- RafasConquest.java
- WarCouncil
- net.minecraft.core.BlockPos
- .enlist
- net.minecraft.network.RegistryFriendlyByteBuf
- Grid
- PveCommands Registration
- School
- CityPlan
- Quest
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
- .say
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
- CityVegetationMixin.java
- .hash
- .board
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
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
- .world
- .postChunk
- Terrain
- net.minecraft.world.entity.LivingEntity
- Grade
- WarCamp
- FactionInfoScreen
- Point
- .findGenerationPoint
- Cloth
- FigureBatches
- net.minecraft.core.Vec3i
- Cited Findings
- Morale
- Override
- CourtSet
- net.neoforged.bus.api.SubscribeEvent
- SiegeWorks
- Retinues
- WaterSwell
- Hull
- Body
- Trait
- .view
- HarbourCut
- GrandStrategyScreen
- CityTerrain
- CapitalLayout
- CityTerrainPiece
- com.mojang.blaze3d.vertex.PoseStack
- ConquestEvents.java
- City
- BattleShips
- MapView
- net.minecraft.resources.ResourceLocation
- .changed
- .shift
- .choose
- SeaCalm
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- Kit
- Organizations
- Phase
- .build
- Townsperson
- .readPlacements
- CLAUDE.md
- How do world generation and structure placement work, and what do they cost?
- .feudFight
- .place
- .roadStart
- .place
- Cited Findings
- RecruitFigures
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- Race
- UnitOrder
- ClientPolitics
- CityPlacement
- BattleSnapshot
- SableSeen
- .noble
- .findGenerationPoint
- .passOnOrders
- .touches
- CourtView
- .onClientExtensions
- ChunkMapAccess
- Ranks
- .render
- Sea
- ConquestEvents
- WallTemplates
- Kind
- DistantArmyRenderer
- .boundsAt
- ConquestGameRules
- OrganizationScreen
- net.minecraft.core.BlockPos
- RecruitsBattle
- Ranks
- BattleType
- Roads
- Settlement
- Profiling run sheet (baseline)
- .measure
- net.minecraft.sounds.SoundEvent
- ChunkMapCameraMixin.java
- Recruiter
- GatePlanksBlock.java
- .at
- net.minecraft.client.gui.GuiGraphics
- net.minecraft.server.level.ServerPlayer
- Kind
- Realm
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- .onLogout
- ArtilleryScreen
- PoiPrecomputer
- .inCity
- Kind
- net.minecraft.world.level.block.entity.BlockEntity
- MarkerSpec
- Task
- CityLayout
- .onLevelLoad
- .nationOf
- NobleDistrictTest
- .load
- BattleKeepInventory
- Call
- ThrownFigures
- BoardScreen
- ConquestWorldScreen.java
- 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling
- .deploy
- Rank
- PoiPrecomputer.java
- 4. Depth buffers and post effects (DH depth, occlusion, GPU sync)
- assertequals
- WarCampTest.java
- SiegeWorks.java
- BlockGrid
- ClientBarks
- .eachSpeakersRunIsAPageWithTheChoicesOnTheLast
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
- `Cited Findings` --references--> `ChunkMapAccess`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/mixin/ChunkMapAccess.java

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

## Communities (380 total, 232 thin omitted)

### Community 0 - "ConquestNetwork"
Cohesion: 0.03
Nodes (37): ArtillerySet, BackBanner, Bark, BaseMapPart, BattleSpot, BoardAction, BoardView, CameraAt (+29 more)

### Community 2 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.06
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - ".army"
Cohesion: 0.07
Nodes (7): DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, SiegeSurveyTest, GrandStrategyDateTest, OarRowingTest, ArmouryTest

### Community 4 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.10
Nodes (12): HornCall, ATTACKING, MOVING, RALLYING, RETREATING, Role, BEARER, GENERAL (+4 more)

### Community 5 - "BattleScreen"
Cohesion: 0.08
Nodes (3): BattleScreen, Entry, Menu

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.05
Nodes (12): Arrows in flight (figure volleys), Battle sounds, Recruits PvE Addon Design Doc (PDF), Seeing the whole field (distant flat figures), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits (+4 more)

### Community 7 - "CityGridBuilder"
Cohesion: 0.09
Nodes (9): CityGridBuilder, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY (+1 more)

### Community 9 - "arraylist"
Cohesion: 0.07
Nodes (14): Base map generated from seed, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Faction Info screen, Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, World map layers, Nations by zone (+6 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "CharacterMarkers.java"
Cohesion: 0.11
Nodes (7): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), TickWeave experiment

### Community 13 - "BaseMap"
Cohesion: 0.07
Nodes (12): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, MapPreview, BaseMap, Nation (+4 more)

### Community 14 - "rafasconquest.map.BaseMap"
Cohesion: 0.08
Nodes (7): Cited Findings, Army, Goal, MusterBars, PoliticsSync, War, Wars

### Community 15 - "Faction"
Cohesion: 0.11
Nodes (3): RecruitsBridge, Faction, FactionInfoTest

### Community 16 - "ConquestSavedData"
Cohesion: 0.08
Nodes (6): Cited Findings, ConquestSavedData, Poi, StructureLink, EconomyTick, ConquestSavedDataTest

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.25
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "MarkerBlock.java"
Cohesion: 0.08
Nodes (3): BattleWallBannerBlock, GateBlock, MarkerBlock

### Community 20 - "RecruitsBattle.java"
Cohesion: 0.04
Nodes (34): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+26 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.17
Nodes (7): Cited Findings, ClientChunkWindowMixin, DhShaderLoaderMixin, DhTerrainShaderMixin, RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 27 - "RafasConquest.java"
Cohesion: 0.05
Nodes (5): ClientModEvents, GrappleHooks, StreetWalks, ConquestEntities, RafasConquest

### Community 28 - "WarCouncil"
Cohesion: 0.16
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 29 - "net.minecraft.core.BlockPos"
Cohesion: 0.05
Nodes (13): Saved, ArtilleryBlock, CommandTableBlock, GrandStrategyBlock, OarBlock, QuestBoardBlock, SailBlock, StrategyBoardBlock (+5 more)

### Community 30 - ".enlist"
Cohesion: 0.05
Nodes (12): Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, GeneralTask, BattleMap, GroundPlanTest (+4 more)

### Community 31 - "net.minecraft.network.RegistryFriendlyByteBuf"
Cohesion: 0.15
Nodes (3): Entry, Msg, SimpleChannel

### Community 32 - "Grid"
Cohesion: 0.17
Nodes (4): Countryside, Plan, CountrysideTest, World

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.14
Nodes (3): CityPlan, Piece, Plan

### Community 36 - "Quest"
Cohesion: 0.13
Nodes (9): Boards, Entry, View, Taken, Quest, Type, CLEAR, DELIVER (+1 more)

### Community 37 - "Machine"
Cohesion: 0.09
Nodes (12): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+4 more)

### Community 38 - "Arm"
Cohesion: 0.08
Nodes (18): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+10 more)

### Community 41 - "Build"
Cohesion: 0.29
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 42 - "Front"
Cohesion: 0.10
Nodes (11): Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Feud, Lord, Feuds, Event (+3 more)

### Community 43 - "GateDoors"
Cohesion: 0.10
Nodes (5): GateDoors, Ram, SableLink, Door, Hinge

### Community 44 - "NobleTitle"
Cohesion: 0.14
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageSync

### Community 45 - "FieldBlock"
Cohesion: 0.04
Nodes (7): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.14
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 51 - "BattlePlan"
Cohesion: 0.05
Nodes (9): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, WarPlan (+1 more)

### Community 52 - "ProvinceMap"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 53 - ".plan"
Cohesion: 0.16
Nodes (4): Faith, Bond, RealmPlanner, Seat

### Community 55 - "Cited Findings"
Cohesion: 0.05
Nodes (15): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Cited Findings, Gaps, Inferences, Takeaway, Arrow, ArrowVolleys (+7 more)

### Community 56 - "CityStyles"
Cohesion: 0.22
Nodes (3): Building, CityStyles, Houses

### Community 57 - "SeatCities"
Cohesion: 0.12
Nodes (4): Grid, SeatCities, SeatCitiesTest, World

### Community 58 - "Faction Map View"
Cohesion: 0.10
Nodes (6): 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Gaps, Inferences, Takeaway, FactionMapScreen

### Community 59 - ".step"
Cohesion: 0.12
Nodes (6): FigureMotion, Ground, Match, Placed, State, FigureMotionTest

### Community 61 - "MapContext"
Cohesion: 0.15
Nodes (5): MapContext, Place, PlaceMenuLayer, PoiIconLayer, LabelShape

### Community 63 - "TitleMapTest"
Cohesion: 0.17
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 66 - "FormationSnapshot"
Cohesion: 0.13
Nodes (12): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY (+4 more)

### Community 71 - ".generate"
Cohesion: 0.07
Nodes (13): ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry, Homeland, DESERT, JUNGLE (+5 more)

### Community 72 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.14
Nodes (8): DistantShips, Far, Riding, Want, BattleKind, Part, ShipHulls, DistantShipsTest

### Community 73 - ".view"
Cohesion: 0.15
Nodes (7): Merit, Peer, Peerage, Option, Talk, View, DialogueTest

### Community 76 - ".hash"
Cohesion: 0.11
Nodes (5): ChunkCoord, FarmLayout, NobleAnnex, OriginRoller, Roll

### Community 77 - ".board"
Cohesion: 0.13
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 78 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.08
Nodes (24): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+16 more)

### Community 79 - "Duty"
Cohesion: 0.07
Nodes (24): 1. Combat is per soldier, not per unit, 2. Does the swing land?, 3. How much damage the hit does, 4. Charges, 5. Missiles, 6. Leadership (morale): what actually wins battles, 7. Autoresolve, 8. Older games (Shogun 1, Medieval 1, Rome 1) (+16 more)

### Community 81 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.20
Nodes (4): Land, Result, SiegeSurvey, Wall

### Community 82 - "Ownership & Territory"
Cohesion: 0.20
Nodes (5): Candidate, OwnershipResolver, Claim, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".wallSegments"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 84 - ".layout"
Cohesion: 0.13
Nodes (7): Cited Findings, Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 85 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 86 - "Calibration"
Cohesion: 0.33
Nodes (3): Calibration, Fit, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.05
Nodes (30): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+22 more)

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 92 - ".postChunk"
Cohesion: 0.11
Nodes (4): Cited Findings, BarracksPeople, CityGuards, ConquestServer

### Community 96 - "Grade"
Cohesion: 0.12
Nodes (12): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+4 more)

### Community 99 - "FactionInfoScreen"
Cohesion: 0.11
Nodes (5): FactionInfoScreen, Line, FactionBanner, Layer, FactionBannerTest

### Community 100 - "Point"
Cohesion: 0.05
Nodes (18): Point, Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate (+10 more)

### Community 102 - "Cloth"
Cohesion: 0.08
Nodes (6): Cloth, Solid, Wind, ClothTest, OffTheBackTest, WoolTest

### Community 103 - "FigureBatches"
Cohesion: 0.10
Nodes (10): Batch, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE, FigureBatches (+2 more)

### Community 105 - "net.minecraft.core.Vec3i"
Cohesion: 0.10
Nodes (3): StylePiece, GoldChurch, Layout

### Community 106 - "Cited Findings"
Cohesion: 0.10
Nodes (10): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, BattleSounds, ChargeSound, DinSound (+2 more)

### Community 107 - "Morale"
Cohesion: 0.15
Nodes (8): 1. Flank and rear shock, 2. Nearby rout (fright), 3. Army-wide losses, 4. Shattering, Design: adding the four morale shocks (built 2026-11-11, untested in game), Tests (FieldBattleTest), The core change: morale becomes a sum, Morale

### Community 109 - "CourtSet"
Cohesion: 0.25
Nodes (3): CourtSet, Seat, Style

### Community 111 - "SiegeWorks"
Cohesion: 0.07
Nodes (11): State, CARRIED, RAISING, UP, Work, SiegeWorks, Swing, CLOSED (+3 more)

### Community 112 - "Retinues"
Cohesion: 0.11
Nodes (5): CityManager, PlayerStats, View, Retinues, RetinuesTest

### Community 113 - "WaterSwell"
Cohesion: 0.09
Nodes (9): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Made, WaterSwell, Swell (+1 more)

### Community 114 - "Hull"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 115 - "Body"
Cohesion: 0.07
Nodes (32): Weapon, BOW, GREAT, LONG, NONE, SWORD, Body, BALLISTA (+24 more)

### Community 116 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 117 - ".view"
Cohesion: 0.24
Nodes (7): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, GrandStrategy, View

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.16
Nodes (3): Inferences, GrandStrategyScreen, Spot

### Community 121 - "CityTerrain"
Cohesion: 0.06
Nodes (18): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+10 more)

### Community 122 - "CapitalLayout"
Cohesion: 0.18
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 124 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.05
Nodes (15): BackBannerLayer, BannerArt, BattleBannerRenderer, CapeCurio, ClothItemRenderer, Carried, ClothRenderer, Fabric (+7 more)

### Community 125 - "ConquestEvents.java"
Cohesion: 0.05
Nodes (7): ConquestConfig, FactionBannerNbt, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 126 - "City"
Cohesion: 0.12
Nodes (8): Cited Findings, Gaps, How do world generation and structure placement work, and what do they cost?, Takeaway, City, CityGates, Done, CityIndex

### Community 129 - "MapView"
Cohesion: 0.09
Nodes (11): Cited Findings, BaseMapLayer, Hit, LabelLayer, LegendEntry, MapLayer, Symbol, MapView (+3 more)

### Community 130 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.14
Nodes (3): RecruiterRenderer, TownspersonRenderer, GrappleHook

### Community 132 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 133 - ".choose"
Cohesion: 0.11
Nodes (11): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+3 more)

### Community 134 - "SeaCalm"
Cohesion: 0.22
Nodes (5): Depths, SeaCalm, Strengths, SeaCalmTest, SwellTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (23): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, Cited Findings, Cited Findings, Gaps (+15 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.07
Nodes (26): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 3. GUI and map screens (strategy map, faction map, battle overlays), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings, Cited Findings, Cited Findings (+18 more)

### Community 141 - "Kit"
Cohesion: 0.11
Nodes (19): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+11 more)

### Community 142 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 143 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 148 - "Townsperson"
Cohesion: 0.10
Nodes (6): Walk, Barks, Ear, DistrictKey, Townsfolk, Townsperson

### Community 149 - ".readPlacements"
Cohesion: 0.27
Nodes (3): Offsets, Placement, PlacementOffsetsTest

### Community 152 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.32
Nodes (3): Inferences, Api, DistantGeneration

### Community 155 - ".feudFight"
Cohesion: 0.14
Nodes (5): Clash, Kind, DUEL, SKIRMISH, FeudDuel

### Community 157 - ".roadStart"
Cohesion: 0.10
Nodes (3): CitySizesTest, StreetWalksTest, PlaceMusicTest

### Community 160 - ".place"
Cohesion: 0.14
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 163 - "Cited Findings"
Cohesion: 0.18
Nodes (11): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway, Cited Findings, Gaps, Inferences (+3 more)

### Community 164 - "RecruitFigures"
Cohesion: 0.08
Nodes (9): FigureGear, Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures (+1 more)

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.10
Nodes (6): WarHorn, BackBanners, Armoury, Outfit, Style, ItemFlags

### Community 174 - "Race"
Cohesion: 0.18
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 178 - "UnitOrder"
Cohesion: 0.09
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 217 - ".findGenerationPoint"
Cohesion: 0.11
Nodes (3): CityDistrictStructure, Ground, CityStructure

### Community 221 - "CourtView"
Cohesion: 0.07
Nodes (7): Retainer, Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 228 - ".render"
Cohesion: 0.09
Nodes (7): CharacterScreen, Spot, ClientPeerage, Banner, Portraits, Honour, Honours

### Community 236 - "Kind"
Cohesion: 0.22
Nodes (7): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.07
Nodes (6): DistantDepth, DistantArmyRenderer, Figure, Rects, ShipSprite, Corners

### Community 242 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 249 - "net.minecraft.core.BlockPos"
Cohesion: 0.09
Nodes (4): GateGeometry, HelmBlock, Placed, GateGeometryTest

### Community 252 - "RecruitsBattle"
Cohesion: 0.06
Nodes (4): Cited Findings, Figure, Landing, RecruitsBattle

### Community 254 - "Ranks"
Cohesion: 0.10
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 255 - "BattleType"
Cohesion: 0.13
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 256 - "Roads"
Cohesion: 0.12
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 257 - "Settlement"
Cohesion: 0.12
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 266 - "ChunkMapCameraMixin.java"
Cohesion: 0.17
Nodes (3): Chunks, ArrowSaveMixin, ChunkMapCameraMixin

### Community 274 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.04
Nodes (7): Character page, Court 3D scene (CourtView), Demographics (population by culture and race), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), GateScreen, InventoryStatsButton

### Community 275 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.08
Nodes (16): Camera, CameraView, Card, Enlisted, Shape, LINE, SQUARE, V (+8 more)

### Community 276 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 277 - "Realm"
Cohesion: 0.14
Nodes (3): Hook, Succession, Taking

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.14
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips, Ride

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "net.minecraft.world.level.block.entity.BlockEntity"
Cohesion: 0.07
Nodes (4): ArtilleryMount, ConquestContent, Oar, Sail

### Community 294 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 295 - "CityLayout"
Cohesion: 0.10
Nodes (5): CityLayout, District, Site, WorldSettings, CityLayoutTest

### Community 305 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 310 - "ConquestWorldScreen.java"
Cohesion: 0.11
Nodes (5): BattleSite.Water, BattleSite.Water, ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 311 - "2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling"
Cohesion: 0.40
Nodes (5): 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, Cited Findings, Gaps, Inferences, Takeaway

### Community 315 - "Rank"
Cohesion: 0.11
Nodes (12): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+4 more)

### Community 318 - "PoiPrecomputer.java"
Cohesion: 0.09
Nodes (14): SeedHash, TerritoryMap, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD (+6 more)

### Community 320 - "4. Depth buffers and post effects (DH depth, occlusion, GPU sync)"
Cohesion: 0.40
Nodes (5): 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), Cited Findings, Gaps, Inferences, Takeaway

### Community 321 - "assertequals"
Cohesion: 0.05
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 324 - "SiegeWorks.java"
Cohesion: 0.06
Nodes (28): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Characters (holders, skills, prowess, traits), Cultures (seven homelands), Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Enlisted players (+20 more)

### Community 336 - "Realm"
Cohesion: 0.14
Nodes (5): Kind, FRIEND, RIVAL, Realm, CharacterMarkers

### Community 367 - "State"
Cohesion: 0.40
Nodes (5): State, RALLIED, ROUTED, SHATTERED, STEADY

### Community 369 - "Lands"
Cohesion: 0.07
Nodes (7): Cited Findings, Lands, Stronghold, Claim, Muster, CEREMONIAL, OFFENSIVE

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
- **232 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `ConquestNetwork`, `net.minecraft.nbt.CompoundTag`, `.army`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `net.minecraft.resources.ResourceLocation`, `PoliticsTest`, `Phase`, `net.minecraft.server.level.ServerPlayer`, `RecruitsBattle.java`, `.feudFight`, `WarCouncil`, `.enlist`, `PveCommands Registration`, `.onServerTick`, `net.minecraft.world.item.ItemStack`, `rafasconquest.faction.Faction`, `GateDoors`, `FieldBlock`, `.say`, `UnitOrder`, `ConquestWorldScreen.java`, `BattleMap.java`, `net.minecraft.server.level.ServerLevel`, `Doctrine`, `.passOnOrders`, `WarCamp`, `SiegeWorks`, `Trait`, `ConquestEvents.java`?**
  _High betweenness centrality (0.081) - this node is a cross-community bridge._
- **What connects `CLOSED`, `CLOSING`, `OPEN` to the rest of the system?**
  _414 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `ConquestNetwork` be split into smaller, more focused modules?**
  _Cohesion score 0.030867221950024497 - nodes in this community are weakly interconnected._
- **Why does `FieldBattle` connect `FieldBlock` to `Bodies`, `assertequals`, `PveCommands Registration`, `.siege`, `.army`, `Machine`, `Arm`, `Point`, `Morale`, `Call`, `.say`, `BattlePlan`, `RecruitsBattle.java`, `.deploy`, `.enlist`, `BattleType`?**
  _High betweenness centrality (0.025) - this node is a cross-community bridge._
- **Should `net.minecraft.nbt.CompoundTag` be split into smaller, more focused modules?**
  _Cohesion score 0.06227106227106227 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._