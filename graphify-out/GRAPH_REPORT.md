# Graph Report - RafasConquest  (2026-11-14)

## Corpus Check
- 544 files · ~471,441 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7498 nodes · 24110 edges · 381 communities (154 shown, 227 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3171 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `f5ae1d75`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- .measure
- Politics
- net.minecraft.world.level.levelgen.structure.BoundingBox
- .army
- .allSitesInBox
- BattleScreen
- DistantHorizonsArmies.java
- CityGridBuilder
- PoliticsTest
- ClothRenderer
- Peerage
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- arraylist
- BaseMap
- rafasconquest.map.BaseMap
- SailBlock.java
- ConquestSavedData
- Battle Map Web Page & Site
- Person
- net.minecraft.world.level.block.state.BlockState
- .fire
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- net.minecraft.server.level.ServerPlayer
- HorizontalStretch
- CityManagerScreen
- .plan
- org.spongepowered.asm.mixin.Mixin
- Arm
- WarCouncil
- City
- Point
- .passOnOrders
- Lands
- PveCommands Registration
- School
- CityPlan
- net.minecraft.core.Vec3i
- Machine
- CityVegetationMixin.java
- SiegeWorks
- rafasconquest.faction.Faction
- com.talhanation.recruits.entities.AbstractRecruitEntity
- Front
- GateBlockEntity
- FieldBlock
- Personalities
- SeaLanes
- Characters (holders, skills, prowess, traits)
- Enlisted
- Side
- ProvinceMap
- Faction
- Barracks
- .onLogout
- CityStyles
- City
- Faction Map View
- .step
- Cities (grid, plots, vanilla village buildings)
- MapContext
- TitleMapTest
- Bodies
- StyleLibrary
- FigureGear
- CapitalLayout
- BattleMap.java
- Row
- .generate
- net.minecraft.world.item.DyeColor
- .say
- MapLands
- Kit
- .rightClick
- Quests
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Duty
- net.minecraft.server.level.ServerLevel
- Ownership & Territory
- .wallSegments
- FormationSnapshot
- Land
- Grade
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- .render
- .postChunk
- .groundCoversEveryTent
- .bark
- Grade
- Kind
- FactionInfoScreen
- ClientEvents
- com.mojang.blaze3d.vertex.PoseStack
- Cloth
- FigureBatches
- net.minecraft.world.phys.Vec3
- .block
- How Total War calculates battles between units
- Override
- Homeland
- net.minecraft.network.RegistryFriendlyByteBuf
- Hull
- .view
- WaterSwell
- Ride
- Body
- Trait
- Portraits.java
- HarbourCut
- GrandStrategyScreen
- CityTerrain
- DistantOcclusion
- CityTerrainPiece
- .tick
- GoldChurch.java
- BattleShips
- Terrain
- .touches
- .drawEmpire
- .shift
- .choose
- SeaCalm
- ChunkMapCameraMixin.java
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- ClientScreens
- TechTreeScreen
- ArtilleryMount.java
- NationScreen
- Organizations
- CLAUDE.md
- CourtSet
- BattleType
- .world
- ShipHulls
- RoadNetwork
- net.minecraft.core.BlockPos
- RecruitFigures
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- RafasConquest (PvE addon for Villager Recruits)
- RecruitsBattle
- BattleSnapshot
- SableSeen
- Politics.Front
- Phase
- .noble
- CityGridBuilder.java
- .render
- WallTemplates
- ClientBarks
- rafasconquest.battle.FormationSnapshot
- RecruitFigures.java
- .at
- .view
- UnitOrder
- .render
- net.neoforged.bus.api.SubscribeEvent
- BattleKeepInventory
- Kind
- net.minecraft.client.Minecraft
- CameraView
- .boundsAt
- ConquestGameRules
- OrganizationScreen
- .build
- .hinge
- BattleRole
- BattlePlan
- Roads
- Settlement
- Profiling run sheet (baseline)
- .measure
- net.minecraft.sounds.SoundEvent
- ClientPolitics
- Sail
- Clash
- net.minecraft.client.gui.GuiGraphics
- Realm
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- .onClientExtensions
- World map (CK3-style political map)
- .of
- RankKits
- Kind
- ArtilleryScreen
- .walks
- .onLevelLoad
- .plan
- ConquestConfig
- BoardScreen
- .borders
- Call
- Recruiter
- ConquestNetwork
- Override
- Point
- .onlySoldiersWhoReachTheEnemyFight
- PveCommands.java
- assertequals
- net.minecraft.server.level.ServerPlayer
- .load
- .findGenerationPoint
- UnitMode
- Task
- ClientModEvents.java
- OarFigures
- ArrowSaveMixin.java
- ThrownFigures
- ChunkMapAccess
- 8. Lessons from performance mods that a content mod can apply
- 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel)
- Shape
- Kind
- State
- Build
- 6. Sodium (and Iris) compatibility
- Morale
- ShipKind

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 305 edges
2. `FieldBlock` - 222 edges
3. `FieldBattle` - 209 edges
4. `FieldArmy` - 127 edges
5. `ConquestNetwork` - 126 edges
6. `Lands` - 89 edges
7. `BattleScreen` - 86 edges
8. `Siege` - 82 edges
9. `Politics` - 79 edges
10. `BaseMap` - 77 edges

## Surprising Connections (you probably didn't know these)
- `Inferences` --references--> `PoliticalMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/map/PoliticalMap.java
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

## Communities (381 total, 227 thin omitted)

### Community 0 - ".measure"
Cohesion: 0.10
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 1 - "Politics"
Cohesion: 0.07
Nodes (12): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Army, Muster, CEREMONIAL (+4 more)

### Community 2 - "net.minecraft.world.level.levelgen.structure.BoundingBox"
Cohesion: 0.06
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - ".army"
Cohesion: 0.07
Nodes (4): DoctrineTest, FieldBattleTest, GrandStrategyDateTest, OarRowingTest

### Community 4 - ".allSitesInBox"
Cohesion: 0.09
Nodes (7): CityLayout, District, Site, ChunkCoord, FarmLayout, WorldSettings, CityLayoutTest

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.07
Nodes (5): Cited Findings, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "CityGridBuilder"
Cohesion: 0.08
Nodes (10): Houses, CityGridBuilder, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA (+2 more)

### Community 9 - "ClothRenderer"
Cohesion: 0.10
Nodes (8): BannerArt, Carried, ClothRenderer, Fabric, Kind, Layer, Live, OffTheBackTest

### Community 10 - "Peerage"
Cohesion: 0.05
Nodes (19): Advancement, Boards, Entry, View, Names, NobleTitle, COUNT, KNIGHT (+11 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "arraylist"
Cohesion: 0.07
Nodes (8): Countryside (hamlets, villages, watchtowers, castles), Demographics (population by culture and race), Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, Nations by zone, Roads (high, city, town roads; bridges; signposts), Seat cities, Storm wall and war zone

### Community 13 - "BaseMap"
Cohesion: 0.07
Nodes (12): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, MapPreview, BaseMap, Nation (+4 more)

### Community 14 - "rafasconquest.map.BaseMap"
Cohesion: 0.10
Nodes (5): FeudDuel, Goal, MusterBars, PoliticsSync, Wars

### Community 15 - "SailBlock.java"
Cohesion: 0.07
Nodes (6): ArtilleryBlock, CommandTableBlock, OarBlock, QuestBoardBlock, SailBlock, Builder

### Community 16 - "ConquestSavedData"
Cohesion: 0.07
Nodes (5): Cited Findings, ConquestSavedData, Poi, ConquestServer, EconomyTick

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.25
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (6): Saved, BattleBannerBlock, BattleWallBannerBlock, GateBlock, HelmBlock, MarkerBlock

### Community 20 - ".fire"
Cohesion: 0.08
Nodes (5): MarkerRenderer, CharacterMarkers, Placed, MarkerBlockEntity, MarkerSpec

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.20
Nodes (5): ClientChunkWindowMixin, DhShaderLoaderMixin, DhTerrainShaderMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 27 - "Arm"
Cohesion: 0.08
Nodes (18): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+10 more)

### Community 28 - "WarCouncil"
Cohesion: 0.16
Nodes (7): Facts, Officer, Said, Talk, WarCouncil, Option, WarCouncilPagesTest

### Community 29 - "City"
Cohesion: 0.14
Nodes (6): Countryside, Plan, City, CityIndex, CountrysideTest, World

### Community 30 - "Point"
Cohesion: 0.09
Nodes (4): Point, BlockGrid, Ground, GroundPlan

### Community 31 - ".passOnOrders"
Cohesion: 0.08
Nodes (4): Cited Findings, Figure, Landing, Live

### Community 32 - "Lands"
Cohesion: 0.09
Nodes (8): Cited Findings, Gaps, Inferences, Takeaway, Where are full scans, nested loops, repeated lookups and per-tick allocations?, Lands, Stronghold, Claim

### Community 34 - "School"
Cohesion: 0.09
Nodes (14): Dojos, Ground, School, BLOOD, FIRE, ICE, IRON, LIGHT (+6 more)

### Community 35 - "CityPlan"
Cohesion: 0.13
Nodes (4): CityPlan, Piece, Plan, NobleDistrictTest

### Community 37 - "Machine"
Cohesion: 0.09
Nodes (12): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+4 more)

### Community 38 - "CityVegetationMixin.java"
Cohesion: 0.11
Nodes (14): Battle code costs 0.2 ms; the multi-second freezes come from blocking, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, Cited Findings, Gaps, Inferences, Takeaway, Cited Findings, Gaps (+6 more)

### Community 41 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.11
Nodes (6): Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER

### Community 42 - "Front"
Cohesion: 0.12
Nodes (4): Feud, Lord, Feuds, Front

### Community 43 - "GateBlockEntity"
Cohesion: 0.10
Nodes (9): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+1 more)

### Community 45 - "FieldBlock"
Cohesion: 0.04
Nodes (8): The core change: morale becomes a sum, FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (38): Honour, Honours, Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS (+30 more)

### Community 47 - "SeaLanes"
Cohesion: 0.14
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.04
Nodes (34): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Character page, Characters (holders, skills, prowess, traits), Court 3D scene (CourtView), Cultures (seven homelands) (+26 more)

### Community 50 - "Enlisted"
Cohesion: 0.19
Nodes (3): Card, Enlisted, Unit

### Community 51 - "Side"
Cohesion: 0.11
Nodes (7): HornCall, ATTACKING, MOVING, RALLYING, RETREATING, Officers, Side

### Community 52 - "ProvinceMap"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 53 - "Faction"
Cohesion: 0.07
Nodes (5): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), Faction, ConquestSavedDataTest, FactionInfoTest

### Community 57 - "City"
Cohesion: 0.12
Nodes (4): Grid, SeatCities, SeatCitiesTest, World

### Community 58 - "Faction Map View"
Cohesion: 0.11
Nodes (8): 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings, Gaps, Inferences, Takeaway, FactionMapScreen, MapView

### Community 59 - ".step"
Cohesion: 0.12
Nodes (6): FigureMotion, Ground, Match, Placed, State, FigureMotionTest

### Community 60 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.22
Nodes (5): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), TickWeave experiment

### Community 61 - "MapContext"
Cohesion: 0.09
Nodes (10): BaseMapLayer, MapContext, LegendEntry, MapLayer, Symbol, Place, PlaceMenuLayer, PoiIconLayer (+2 more)

### Community 63 - "TitleMapTest"
Cohesion: 0.17
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.10
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.11
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - "FigureGear"
Cohesion: 0.17
Nodes (7): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 67 - "CapitalLayout"
Cohesion: 0.09
Nodes (8): CapitalLayout, Land, Site, NobleAnnex, OriginRoller, Roll, CapitalRelocationTest, LayoutTest

### Community 69 - "BattleMap.java"
Cohesion: 0.03
Nodes (4): Battle map developer web page, Morale, rout and rally, Wind, RafasConquest

### Community 71 - ".generate"
Cohesion: 0.10
Nodes (6): Dojo, ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry

### Community 72 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.15
Nodes (5): DistantShips, Far, HullKey, Want, DistantShipsTest

### Community 75 - "Kit"
Cohesion: 0.11
Nodes (19): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+11 more)

### Community 76 - ".rightClick"
Cohesion: 0.21
Nodes (3): Entry, Menu, HornOrders

### Community 77 - "Quests"
Cohesion: 0.09
Nodes (13): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Type, CLEAR (+5 more)

### Community 78 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.06
Nodes (31): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps (+23 more)

### Community 79 - "Duty"
Cohesion: 0.13
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 81 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.10
Nodes (7): ChunkWait, Wait, Land, Result, SiegeSurvey, Wall, SiegeSurveyTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".wallSegments"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 84 - "FormationSnapshot"
Cohesion: 0.14
Nodes (7): Cited Findings, Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 85 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 86 - "Grade"
Cohesion: 0.16
Nodes (9): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Plan (+1 more)

### Community 87 - "Doctrine"
Cohesion: 0.05
Nodes (30): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+22 more)

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - ".render"
Cohesion: 0.10
Nodes (6): CharacterScreen, Retainer, Spot, ClientPeerage, Banner, Portraits

### Community 92 - ".postChunk"
Cohesion: 0.10
Nodes (5): RecruitsBridge, BarracksPeople, CityBanners, CityGuards, PoiPrecomputer

### Community 96 - "Grade"
Cohesion: 0.16
Nodes (10): A tick budget and staggered schedules make the fixes stick, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom?, Inferences (+2 more)

### Community 98 - "Kind"
Cohesion: 0.13
Nodes (12): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY (+4 more)

### Community 99 - "FactionInfoScreen"
Cohesion: 0.14
Nodes (3): FactionInfoScreen, Line, StructureLink

### Community 101 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.05
Nodes (9): BackBannerLayer, BattleBannerRenderer, CapeCurio, ClothItemRenderer, GateRenderer, GrappleHookRenderer, MouthLayer, RecruiterRenderer (+1 more)

### Community 102 - "Cloth"
Cohesion: 0.09
Nodes (5): GatePlanksBlock, Cloth, Solid, ClothTest, WoolTest

### Community 103 - "FigureBatches"
Cohesion: 0.07
Nodes (11): ShipSprite, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE (+3 more)

### Community 105 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.09
Nodes (3): Camera, PlannedTent, WarCamp

### Community 106 - ".block"
Cohesion: 0.06
Nodes (6): BattleMap, GroundPlanTest, Land, Land, Land, Land

### Community 107 - "How Total War calculates battles between units"
Cohesion: 0.11
Nodes (17): 1. Combat is per soldier, not per unit, 1. Flank and rear shock, 2. Does the swing land?, 2. Nearby rout (fright), 3. Army-wide losses, 3. How much damage the hit does, 4. Charges, 4. Shattering (+9 more)

### Community 109 - "Homeland"
Cohesion: 0.10
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 110 - "net.minecraft.network.RegistryFriendlyByteBuf"
Cohesion: 0.17
Nodes (5): BackBanner, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 111 - "Hull"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 113 - "WaterSwell"
Cohesion: 0.09
Nodes (9): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Made, WaterSwell, Swell (+1 more)

### Community 115 - "Body"
Cohesion: 0.08
Nodes (26): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+18 more)

### Community 116 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 117 - "Portraits.java"
Cohesion: 0.06
Nodes (4): SeedHash, FactionBannerNbt, PoliticalMap, FactionInfo

### Community 121 - "CityTerrain"
Cohesion: 0.06
Nodes (18): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+10 more)

### Community 122 - "DistantOcclusion"
Cohesion: 0.06
Nodes (14): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, Arrow, ArrowVolleys, BattleSpotRing (+6 more)

### Community 128 - "BattleShips"
Cohesion: 0.17
Nodes (4): BattleShips, Helm, Ram, SableLink

### Community 131 - ".drawEmpire"
Cohesion: 0.27
Nodes (3): Hit, LabelLayer, LabelShape

### Community 132 - ".shift"
Cohesion: 0.16
Nodes (6): BattleSite, Part, Water, BattleSite.Water, BattleSite.Water, BattleSiteTest

### Community 133 - ".choose"
Cohesion: 0.12
Nodes (11): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+3 more)

### Community 134 - "SeaCalm"
Cohesion: 0.18
Nodes (5): Depths, SeaCalm, Strengths, SeaCalmTest, SwellTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (23): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, Cited Findings, Cited Findings, Gaps (+15 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.07
Nodes (26): 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, Cited Findings, Cited Findings, Cited Findings (+18 more)

### Community 146 - "NationScreen"
Cohesion: 0.09
Nodes (4): Camera, CourtView, Figure, NationScreen

### Community 149 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 152 - "CourtSet"
Cohesion: 0.25
Nodes (3): CourtSet, Seat, Style

### Community 155 - "BattleType"
Cohesion: 0.20
Nodes (5): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE

### Community 157 - "ShipHulls"
Cohesion: 0.42
Nodes (3): BattleKind, Part, ShipHulls

### Community 160 - "RoadNetwork"
Cohesion: 0.09
Nodes (6): Cited Findings, RoadFeature, Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 163 - "net.minecraft.core.BlockPos"
Cohesion: 0.13
Nodes (3): GrandStrategyBlock, StrategyBoardBlock, WorldMapItem

### Community 164 - "RecruitFigures"
Cohesion: 0.09
Nodes (8): Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures, RecruitFiguresTest

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.10
Nodes (6): WarHorn, BattleStandardItem, Armoury, Outfit, Style, ItemFlags

### Community 174 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.17
Nodes (8): Arrows in flight (figure volleys), Battle sounds, Recruits PvE Addon Design Doc (PDF), Seeing the whole field (distant flat figures), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 205 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 217 - "CityGridBuilder.java"
Cohesion: 0.04
Nodes (16): FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoiType, CAPITAL (+8 more)

### Community 223 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.12
Nodes (6): Cited Findings, BattleSounds, ChargeSound, DinSound, Source, DistantArmies

### Community 224 - "RecruitFigures.java"
Cohesion: 0.05
Nodes (3): Sea, Figure, Batch

### Community 226 - ".view"
Cohesion: 0.06
Nodes (18): Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY, SHIELDMAN, Rank, CAPTAIN (+10 more)

### Community 228 - "UnitOrder"
Cohesion: 0.11
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 232 - ".render"
Cohesion: 0.19
Nodes (5): ClientTownsfolk, Folk, StreetWalks, Walk, ClientTownsfolkTest

### Community 236 - "Kind"
Cohesion: 0.22
Nodes (7): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL

### Community 237 - "net.minecraft.client.Minecraft"
Cohesion: 0.10
Nodes (3): DistantDepth, DistantArmyRenderer, Rects

### Community 242 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 249 - ".build"
Cohesion: 0.14
Nodes (3): Frame, Harbour, HarbourCutTest

### Community 254 - "BattleRole"
Cohesion: 0.25
Nodes (6): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT

### Community 255 - "BattlePlan"
Cohesion: 0.04
Nodes (10): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, Outcome (+2 more)

### Community 256 - "Roads"
Cohesion: 0.21
Nodes (5): Link, Place, Roads, Spur, RoadsTest

### Community 257 - "Settlement"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 271 - "Clash"
Cohesion: 0.32
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 277 - "Realm"
Cohesion: 0.11
Nodes (4): Hook, Realm, Succession, Taking

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.19
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 283 - "World map (CK3-style political map)"
Cohesion: 0.33
Nodes (7): Base map generated from seed, Command Table block, Faction Info screen, World map layers, Province size setting, Provinces (ProvinceMap), World map (CK3-style political map)

### Community 285 - ".of"
Cohesion: 0.20
Nodes (3): FactionBanner, Layer, FactionBannerTest

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 295 - ".walks"
Cohesion: 0.08
Nodes (5): DistrictKey, Townsfolk, CitySizesTest, StreetWalksTest, PlaceMusicTest

### Community 298 - ".plan"
Cohesion: 0.11
Nodes (10): Centre, Demographics, Share, Faith, Bond, Kind, FRIEND, RIVAL (+2 more)

### Community 305 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 306 - "Recruiter"
Cohesion: 0.06
Nodes (5): ConquestContent, GrappleHooks, Oar, ConquestEntities, Recruiter

### Community 308 - "ConquestNetwork"
Cohesion: 0.03
Nodes (36): ArtillerySet, Bark, BaseMapPart, BattleSpot, BoardAction, BoardView, CameraAt, ChargeHits (+28 more)

### Community 310 - "Override"
Cohesion: 0.13
Nodes (4): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, SettingsScreen

### Community 313 - "Point"
Cohesion: 0.04
Nodes (19): Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate, GateAction (+11 more)

### Community 318 - "PveCommands.java"
Cohesion: 0.09
Nodes (30): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+22 more)

### Community 321 - "assertequals"
Cohesion: 0.06
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 322 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.10
Nodes (7): Place, CityManager, GrandStrategy, View, PlayerStats, View, PlaceMusic

### Community 324 - ".findGenerationPoint"
Cohesion: 0.09
Nodes (4): SlopeTile, CityDistrictStructure, Ground, CitySlopeStructure

### Community 326 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 327 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 330 - "OarFigures"
Cohesion: 0.17
Nodes (3): Model, OarFigures, Rowing

### Community 344 - "8. Lessons from performance mods that a content mod can apply"
Cohesion: 0.40
Nodes (5): 8. Lessons from performance mods that a content mod can apply, Cited Findings, Gaps, Inferences, Takeaway

### Community 345 - "1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel)"
Cohesion: 0.40
Nodes (5): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), Cited Findings, Gaps, Inferences, Takeaway

### Community 347 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 350 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 351 - "State"
Cohesion: 0.50
Nodes (4): State, CARRIED, RAISING, UP

### Community 352 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 357 - "6. Sodium (and Iris) compatibility"
Cohesion: 0.40
Nodes (5): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway

### Community 367 - "Morale"
Cohesion: 0.20
Nodes (8): Morale, State, RALLIED, ROUTED, SHATTERED, STEADY, UnitInfo, UnitsState

### Community 388 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

## Ambiguous Edges - Review These
- `Roads (high, city, town roads; bridges; signposts)` → `Mustering (offensive and ceremonial)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **415 isolated node(s):** `SOLDIER`, `LEADER`, `GENERAL`, `BEARER`, `GUARD` (+410 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1378 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **227 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `.measure`, `net.minecraft.world.level.levelgen.structure.BoundingBox`, `.army`, `.shift`, `Peerage`, `rafasconquest.map.BaseMap`, `net.minecraft.server.level.ServerPlayer`, `WarCouncil`, `.passOnOrders`, `PveCommands Registration`, `net.minecraft.world.item.ItemStack`, `rafasconquest.faction.Faction`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `SiegeWorks`, `Enlisted`, `Side`, `Point`, `PveCommands.java`, `net.minecraft.server.level.ServerPlayer`, `BattleMap.java`, `Row`, `UnitMode`, `.say`, `Phase`, `net.minecraft.server.level.ServerLevel`, `Doctrine`, `CityGridBuilder.java`, `Shape`, `ClientEvents`, `UnitOrder`, `com.mojang.blaze3d.vertex.PoseStack`, `net.minecraft.world.phys.Vec3`, `.block`?**
  _High betweenness centrality (0.072) - this node is a cross-community bridge._
- **What connects `SOLDIER`, `LEADER`, `GENERAL` to the rest of the system?**
  _415 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `.measure` be split into smaller, more focused modules?**
  _Cohesion score 0.09982174688057041 - nodes in this community are weakly interconnected._
- **Why does `ConquestNetwork` connect `ConquestNetwork` to `net.minecraft.server.level.ServerPlayer`, `arraylist`, `.rightClick`, `net.minecraft.network.RegistryFriendlyByteBuf`, `Morale`, `net.minecraft.client.gui.GuiGraphics`, `net.minecraft.world.level.block.state.BlockState`, `BattleSnapshot`, `net.minecraft.server.level.ServerPlayer`, `CityManagerScreen`?**
  _High betweenness centrality (0.027) - this node is a cross-community bridge._
- **Should `Politics` be split into smaller, more focused modules?**
  _Cohesion score 0.06603346901854365 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Roads (high, city, town roads; bridges; signposts)` and `Mustering (offensive and ceremonial)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._