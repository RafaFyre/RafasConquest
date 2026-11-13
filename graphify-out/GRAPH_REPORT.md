# Graph Report - RafasConquest  (2026-11-12)

## Corpus Check
- 473 files · ~436,863 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 6999 nodes · 22234 edges · 368 communities (153 shown, 215 thin omitted)
- Extraction: 86% EXTRACTED · 14% INFERRED · 0% AMBIGUOUS · INFERRED: 3010 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `6c0aef44`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FieldBlock
- Townsperson
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsArmies.java
- CityGridBuilder
- SiegeWorks
- ShipHulls
- rafasconquest.battle.BattlePlan.Arm
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- Land
- BaseMap
- UnitOrder
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.GuiGraphics
- PveCommands.java
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- list
- HorizontalStretch
- CityManagerScreen
- .plan
- net.minecraft.core.BlockPos
- .shift
- WarCouncil
- .stat
- .view
- .findGenerationPoint
- CityLayout
- PveCommands Registration
- School
- CityPlan
- .of
- net.neoforged.neoforge.network.handling.IPayloadContext
- net.minecraft.world.phys.Vec3
- CharacterMarkers.java
- rafasconquest.faction.Faction
- SeaCalm
- Feud
- net.minecraft.server.MinecraftServer
- net.minecraft.nbt.CompoundTag
- net.minecraft.server.level.ServerLevel
- Personalities
- SeaLanes
- BattleType
- Cited Findings
- Capture
- Faction
- Centre
- Barracks
- Row
- .rightClick
- City
- Faction Map View
- .step
- ClientScreens
- .render
- TitleMapTest
- .enlist
- StyleLibrary
- .step
- .choose
- RafasConquest.java
- RecruitsBattle
- net.minecraft.server.level.ServerLevel
- Cited Findings
- BattlePlan
- MapLands
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Phase
- .board
- net.minecraft.world.level.block.state.BlockState
- PoliticsTest
- Terrain
- Ownership & Territory
- .findGenerationPoint
- com.talhanation.recruits.entities.AbstractRecruitEntity
- City
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- CityLayout
- PoliticsData
- Point
- .generate
- net.minecraft.core.Vec3i
- DistantOcclusion
- FactionInfoScreen
- MapContext
- .place
- CityStyles
- FigureBatches
- .view
- MouthLayer.java
- ConquestNetwork
- MarkerScreen
- net.minecraft.sounds.SoundEvent
- UnitMode
- City
- BattleShips
- WaterSwell
- Poi
- ConquestSavedData
- ConquestServer
- Cited Findings
- org.jetbrains.annotations.Nullable
- GrandStrategyScreen
- MapLayer
- CapitalLayout
- MarkerRenderer.java
- Ranks
- Swell
- BattleShips
- Kit
- SeatCitiesTest
- BattleSounds
- PoliticalStyle
- .render
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .onLevelLoad
- CourtSet
- .cellMax
- GoldChurch.java
- Job
- RoadNetwork
- CLAUDE.md
- .world
- Chunks
- .plan
- Organizations
- .build
- Machine
- TechTreeScreen
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- .drawEmpire
- Override
- BoardScreen
- SoldierFigure
- .touches
- Morale
- .pack
- net.minecraft.server.level.ServerPlayer
- .view
- rafasconquest.battle.BattlePlan.Point
- RecruitFigures.java
- .hash
- net.neoforged.bus.api.SubscribeEvent
- ClientPolitics
- Race
- net.minecraft.resources.ResourceLocation
- net.neoforged.bus.api.SubscribeEvent
- .at
- ItemFlags.java
- DistantArmyRenderer
- CityVariant
- ConquestGameRules
- net.minecraft.network.FriendlyByteBuf
- Trait
- OrganizationScreen
- Call
- Ramming
- .block
- Profiling run sheet (baseline)
- .measure
- .passOnOrders
- GateBlockEntity
- BattleKeepInventory.java
- .onLogout
- Lands
- CourtView
- CityTerrainPiece
- Realm
- .view
- NobleTitle
- Kind
- Characters (holders, skills, prowess, traits)
- How Total War calculates battles between units
- .readPlacements
- .collect
- .boundsAt
- BattleSnapshot
- .findGenerationPoint
- Task
- .inCity
- .postChunk
- .soundHorn
- .feudFight
- DistantGeneration
- Build
- ConquestWorldScreen
- Mixins on server paths: what they hook, how hot, added cost
- GatePlanksBlock
- RecruitsBattle.java
- .issue
- BackBanners
- HarbourCut
- Recruiter
- Kind
- ShareLiveTest
- PlayerStats
- 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound
- ClientBarks
- PoliticsView
- WarHorn
- ShipKind
- Plan
- NobleDistrictTest
- net.minecraft.core.BlockPos
- 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?
- How do the simulation and daily-decision systems schedule work? Do they spike?
- net.neoforged.fml.common.EventBusSubscriber

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 295 edges
2. `FieldBattle` - 200 edges
3. `FieldBlock` - 197 edges
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
- `Cited Findings` --references--> `RoadLayer`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Takeaway` --references--> `RoadLayer`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Inferences` --references--> `PoliticalMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/map/PoliticalMap.java
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

## Communities (368 total, 215 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.04
Nodes (7): The core change: morale becomes a sum, FieldArmy, Event, FieldBattle, Order, Target, FieldBlock

### Community 1 - "Townsperson"
Cohesion: 0.12
Nodes (4): Walk, DistrictKey, Townsfolk, Townsperson

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.07
Nodes (8): CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece, PlaceholderPiece

### Community 3 - ".army"
Cohesion: 0.07
Nodes (4): DoctrineTest, FieldBattleTest, SiegeTest, GrandStrategyDateTest

### Community 4 - "CityTerrain"
Cohesion: 0.06
Nodes (19): Ground, CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind (+11 more)

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.06
Nodes (9): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+1 more)

### Community 7 - "CityGridBuilder"
Cohesion: 0.10
Nodes (5): Houses, CityGridBuilder, Ground, PlacedWall, Platform

### Community 8 - "SiegeWorks"
Cohesion: 0.09
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 9 - "ShipHulls"
Cohesion: 0.42
Nodes (3): BattleKind, Part, ShipHulls

### Community 10 - "rafasconquest.battle.BattlePlan.Arm"
Cohesion: 0.05
Nodes (30): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+22 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "Land"
Cohesion: 0.17
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 13 - "BaseMap"
Cohesion: 0.08
Nodes (11): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, ClientMapCache, MapPreview, BaseMap (+3 more)

### Community 14 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.23
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.04
Nodes (7): Character page, Court 3D scene (CourtView), Demographics (population by culture and race), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), GateScreen, InventoryStatsButton

### Community 20 - "PveCommands.java"
Cohesion: 0.08
Nodes (31): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+23 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 22 - "list"
Cohesion: 0.05
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 25 - ".plan"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "net.minecraft.core.BlockPos"
Cohesion: 0.10
Nodes (5): Saved, GateGeometry, CharacterMarkers, Placed, GateGeometryTest

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.15
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 29 - ".stat"
Cohesion: 0.08
Nodes (13): ClientPeerage, Honour, Honours, Kind, FRIEND, RIVAL, Stat, DIPLOMACY (+5 more)

### Community 30 - ".view"
Cohesion: 0.11
Nodes (11): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+3 more)

### Community 32 - "CityLayout"
Cohesion: 0.08
Nodes (8): CityLayout, District, Site, SlopeTile, ChunkCoord, FarmLayout, WorldSettings, CityLayoutTest

### Community 34 - "School"
Cohesion: 0.09
Nodes (14): Dojos, Ground, School, BLOOD, FIRE, ICE, IRON, LIGHT (+6 more)

### Community 35 - "CityPlan"
Cohesion: 0.14
Nodes (3): CityPlan, Piece, Plan

### Community 36 - ".of"
Cohesion: 0.20
Nodes (3): FactionBanner, Layer, FactionBannerTest

### Community 37 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.06
Nodes (16): BackBanner, Bark, ChargeHits, CityClose, OpenBattleScreen, SeaSwell, Entry, Msg (+8 more)

### Community 38 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.12
Nodes (8): Card, Enlisted, Shape, LINE, SQUARE, V, WEDGE, Unit

### Community 41 - "SeaCalm"
Cohesion: 0.24
Nodes (3): Sea, Depths, SeaCalm

### Community 42 - "Feud"
Cohesion: 0.12
Nodes (5): Feud, Lord, Feuds, Front, Politics

### Community 43 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.09
Nodes (6): Cited Findings, Goal, MusterBars, PoliticsSync, War, Wars

### Community 44 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.08
Nodes (4): Palette, WallTemplates, MarkerBlockEntity, MarkerSpec

### Community 45 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.04
Nodes (6): FactionBannerNbt, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "SeaLanes"
Cohesion: 0.15
Nodes (11): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+3 more)

### Community 48 - "BattleType"
Cohesion: 0.12
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 50 - "Cited Findings"
Cohesion: 0.16
Nodes (6): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, Arrow

### Community 51 - "Capture"
Cohesion: 0.08
Nodes (9): FigureGear, Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures (+1 more)

### Community 52 - "Faction"
Cohesion: 0.11
Nodes (4): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction

### Community 53 - "Centre"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 54 - "Barracks"
Cohesion: 0.24
Nodes (3): Barracks, BarracksPiece, BarracksTest

### Community 56 - ".rightClick"
Cohesion: 0.23
Nodes (3): Entry, Menu, HornOrders

### Community 57 - "City"
Cohesion: 0.10
Nodes (8): Countryside, Plan, City, Grid, SeatCities, CityIndex, CountrysideTest, World

### Community 58 - "Faction Map View"
Cohesion: 0.16
Nodes (3): Cited Findings, FactionMapScreen, MapView

### Community 59 - ".step"
Cohesion: 0.14
Nodes (4): FigureMotion, Match, State, FigureMotionTest

### Community 61 - ".render"
Cohesion: 0.12
Nodes (5): CharacterScreen, Retainer, Spot, Banner, Portraits

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 66 - ".step"
Cohesion: 0.08
Nodes (19): Cited Findings, Engine, FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, ENGINES (+11 more)

### Community 67 - ".choose"
Cohesion: 0.15
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 69 - "RafasConquest.java"
Cohesion: 0.05
Nodes (4): Battle map developer web page, Morale, rout and rally, StreetWalks, RafasConquest

### Community 70 - "RecruitsBattle"
Cohesion: 0.08
Nodes (3): Cited Findings, Landing, RecruitsBattle

### Community 71 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.12
Nodes (7): Land, Result, SiegeSurvey, Wall, CityGates, Done, SiegeSurveyTest

### Community 72 - "Cited Findings"
Cohesion: 0.08
Nodes (11): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Cited Findings, Gaps, Inferences, Takeaway, ArrowVolleys, BattleSpotRing (+3 more)

### Community 73 - "BattlePlan"
Cohesion: 0.06
Nodes (8): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, BattlePlanTest

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.06
Nodes (32): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps (+24 more)

### Community 76 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 77 - ".board"
Cohesion: 0.11
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 78 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (7): CommandTableBlock, GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, Builder, MarkerBlock

### Community 79 - "PoliticsTest"
Cohesion: 0.09
Nodes (3): Army, Event, PoliticsTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 84 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.13
Nodes (7): Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER, Side

### Community 85 - "City"
Cohesion: 0.11
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 86 - "Calibration"
Cohesion: 0.14
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.05
Nodes (31): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+23 more)

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - "CityLayout"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 94 - "Point"
Cohesion: 0.10
Nodes (9): Point, BlockGrid, Ground, GroundPlan, GroundPlanTest, Land, Land, Land (+1 more)

### Community 95 - ".generate"
Cohesion: 0.07
Nodes (14): Dojo, ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry, Homeland, DESERT (+6 more)

### Community 98 - "DistantOcclusion"
Cohesion: 0.11
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 100 - "MapContext"
Cohesion: 0.15
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 103 - "FigureBatches"
Cohesion: 0.09
Nodes (10): Batch, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE (+2 more)

### Community 105 - ".view"
Cohesion: 0.08
Nodes (11): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+3 more)

### Community 107 - "ConquestNetwork"
Cohesion: 0.07
Nodes (12): BoardAction, CameraAt, CityView, ConquestNetwork, FactionInfoReply, GateSet, GateView, MarkerSet (+4 more)

### Community 109 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 110 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 111 - "City"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 112 - "BattleShips"
Cohesion: 0.19
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 113 - "WaterSwell"
Cohesion: 0.15
Nodes (7): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, WaterSwell, SeaFoamTest

### Community 114 - "Poi"
Cohesion: 0.15
Nodes (7): ResourceType, FOOD, Poi, PoiType, CAPITAL, CITY, FARM

### Community 115 - "ConquestSavedData"
Cohesion: 0.09
Nodes (4): Cited Findings, ConquestSavedData, EconomyTick, ConquestSavedDataTest

### Community 116 - "ConquestServer"
Cohesion: 0.15
Nodes (4): CityBanners, ConquestServer, StructureLink, PoiPrecomputer

### Community 117 - "Cited Findings"
Cohesion: 0.17
Nodes (11): A tick budget and staggered schedules make the fixes stick, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom?, Gaps (+3 more)

### Community 118 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.08
Nodes (7): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, PoliticalMap, FactionInfo

### Community 121 - "MapLayer"
Cohesion: 0.18
Nodes (5): LegendEntry, MapLayer, Symbol, TerritoryLayer, UnitLayer

### Community 122 - "CapitalLayout"
Cohesion: 0.19
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 125 - "Ranks"
Cohesion: 0.16
Nodes (3): Ranks, Offer, Recruitment

### Community 126 - "Swell"
Cohesion: 0.15
Nodes (3): Ride, Swell, SwellTest

### Community 128 - "BattleShips"
Cohesion: 0.21
Nodes (4): BattleShips, Helm, Ram, SableLink

### Community 129 - "Kit"
Cohesion: 0.10
Nodes (19): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+11 more)

### Community 132 - "BattleSounds"
Cohesion: 0.17
Nodes (5): BattleSounds, ChargeSound, DinSound, Source, DistantArmies

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.10
Nodes (19): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, Cited Findings, Cited Findings, Gaps, Gaps (+11 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (41): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 6. Sodium (and Iris) compatibility, 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply (+33 more)

### Community 142 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 143 - ".cellMax"
Cohesion: 0.10
Nodes (3): CitySizesTest, StreetWalksTest, PlaceMusicTest

### Community 146 - "GoldChurch.java"
Cohesion: 0.13
Nodes (10): Battles (prototype), Blocks of 20 soldiers, Gold church, Land grants (seven ways), Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter (+2 more)

### Community 148 - "Job"
Cohesion: 0.06
Nodes (20): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Enlisted players, The general and guard, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Battle roles (frontline, bait, support, flanker, kill squad), Victory, pursuit and surrender, The war horn (+12 more)

### Community 149 - "RoadNetwork"
Cohesion: 0.15
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 156 - ".plan"
Cohesion: 0.13
Nodes (7): Centre, Demographics, Share, Faith, Bond, RealmPlanner, Seat

### Community 157 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 163 - "Machine"
Cohesion: 0.11
Nodes (12): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+4 more)

### Community 164 - "TechTreeScreen"
Cohesion: 0.18
Nodes (4): TechTreeScreen, Tech, TechTree, TechTreeTest

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.29
Nodes (3): Armoury, Outfit, Style

### Community 174 - ".drawEmpire"
Cohesion: 0.17
Nodes (4): BaseMapLayer, Hit, LabelLayer, LabelShape

### Community 178 - "Override"
Cohesion: 0.16
Nodes (3): BattleSite.Water, SettingsScreen, Politics.Front

### Community 186 - "SoldierFigure"
Cohesion: 0.07
Nodes (32): Weapon, BOW, GREAT, LONG, NONE, SWORD, Body, BALLISTA (+24 more)

### Community 205 - "Morale"
Cohesion: 0.40
Nodes (5): State, RALLIED, ROUTED, SHATTERED, STEADY

### Community 217 - ".pack"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 218 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.08
Nodes (13): Battle code costs 0.2 ms; the multi-second freezes come from blocking, Cited Findings, Cited Findings, CameraView, ChunkMapAccess, ChunkMapCameraMixin, CityVegetationMixin, ClientChunkWindowMixin (+5 more)

### Community 219 - ".view"
Cohesion: 0.18
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 221 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.05
Nodes (18): Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate, GateAction (+10 more)

### Community 224 - "RecruitFigures.java"
Cohesion: 0.05
Nodes (5): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), DistantDepth, Figure

### Community 225 - ".hash"
Cohesion: 0.17
Nodes (3): NobleAnnex, OriginRoller, Roll

### Community 231 - "Race"
Cohesion: 0.20
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.09
Nodes (4): DistantArmyRenderer, Hull, Rects, Placed

### Community 242 - "CityVariant"
Cohesion: 0.17
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 249 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.05
Nodes (14): Morale, BaseMapPart, BattleSpot, FigureStrike, PeerageView, SetSetting, StatsView, TalkChoice (+6 more)

### Community 252 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 267 - "GateBlockEntity"
Cohesion: 0.08
Nodes (9): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+1 more)

### Community 274 - "Lands"
Cohesion: 0.08
Nodes (7): Cited Findings, Lands, Stronghold, Claim, Muster, CEREMONIAL, OFFENSIVE

### Community 275 - "CourtView"
Cohesion: 0.09
Nodes (4): Camera, CourtView, Figure, NationScreen

### Community 277 - "Realm"
Cohesion: 0.12
Nodes (4): Hook, Realm, Succession, Taking

### Community 283 - ".view"
Cohesion: 0.14
Nodes (5): BoardView, GrandStrategyView, TalkView, GrandStrategy, View

### Community 288 - "NobleTitle"
Cohesion: 0.14
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.06
Nodes (31): Ambushes from woods, Base map generated from seed, Battle phases and days, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Command Table block, Countryside (hamlets, villages, watchtowers, castles) (+23 more)

### Community 292 - "How Total War calculates battles between units"
Cohesion: 0.11
Nodes (17): 1. Combat is per soldier, not per unit, 1. Flank and rear shock, 2. Does the swing land?, 2. Nearby rout (fright), 3. Army-wide losses, 3. How much damage the hit does, 4. Charges, 4. Shattering (+9 more)

### Community 293 - ".readPlacements"
Cohesion: 0.27
Nodes (3): Offsets, Placement, PlacementOffsetsTest

### Community 295 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 301 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 303 - ".postChunk"
Cohesion: 0.15
Nodes (3): Cited Findings, BarracksPeople, CityGuards

### Community 305 - ".soundHorn"
Cohesion: 0.33
Nodes (5): HornCall, ATTACKING, MOVING, RALLYING, RETREATING

### Community 306 - ".feudFight"
Cohesion: 0.17
Nodes (5): Clash, Kind, DUEL, SKIRMISH, FeudDuel

### Community 308 - "DistantGeneration"
Cohesion: 0.32
Nodes (3): Inferences, Api, DistantGeneration

### Community 309 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 310 - "ConquestWorldScreen"
Cohesion: 0.20
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 311 - "Mixins on server paths: what they hook, how hot, added cost"
Cohesion: 0.50
Nodes (4): Gaps, Inferences, Mixins on server paths: what they hook, how hot, added cost, Takeaway

### Community 318 - "RecruitsBattle.java"
Cohesion: 0.05
Nodes (3): /council <topic>, TickWeave experiment, Officers

### Community 324 - "Recruiter"
Cohesion: 0.09
Nodes (4): WorldMapItem, Barks, Ear, Recruiter

### Community 327 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 330 - "9. Memory: client caches, textures and NativeImages that are never freed or grow without bound"
Cohesion: 0.40
Nodes (5): 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Gaps, Inferences, Takeaway

### Community 344 - "ShipKind"
Cohesion: 0.33
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 347 - "Plan"
Cohesion: 0.22
Nodes (7): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL

### Community 349 - "net.minecraft.core.BlockPos"
Cohesion: 0.12
Nodes (3): Camera, PlannedTent, WarCamp

### Community 351 - "5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?"
Cohesion: 0.50
Nodes (4): 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, Gaps, Inferences, Takeaway

### Community 352 - "How do the simulation and daily-decision systems schedule work? Do they spike?"
Cohesion: 0.50
Nodes (4): Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **411 isolated node(s):** `ARCHERS`, `CAVALRY`, `ENGINES`, `HEAVY_CAVALRY`, `HEAVY_INFANTRY` (+406 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1343 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **215 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `.block`, `net.minecraft.world.level.ChunkPos`, `.army`, `.passOnOrders`, `SiegeWorks`, `UnitOrder`, `.pitch`, `PveCommands.java`, `WarCouncil`, `PveCommands Registration`, `net.neoforged.neoforge.network.handling.IPayloadContext`, `net.minecraft.world.phys.Vec3`, `net.minecraft.world.item.ItemStack`, `rafasconquest.faction.Faction`, `net.minecraft.server.level.ServerLevel`, `.inCity`, `.soundHorn`, `.feudFight`, `RecruitsBattle.java`, `.enlist`, `RafasConquest.java`, `CameraView.java`, `net.minecraft.server.level.ServerLevel`, `Phase`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `Calibration`, `Doctrine`, `net.minecraft.server.level.ServerPlayer`, `rafasconquest.battle.BattlePlan.Point`, `net.minecraft.core.BlockPos`, `.isOurs`, `net.neoforged.bus.api.SubscribeEvent`, `UnitMode`, `Trait`?**
  _High betweenness centrality (0.079) - this node is a cross-community bridge._
- **What connects `ARCHERS`, `CAVALRY`, `ENGINES` to the rest of the system?**
  _411 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.04078817733990148 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `Kit`, `PoliticalStyle`, `CityGridBuilder`, `.onLevelLoad`, `net.minecraft.client.gui.GuiGraphics`, `CourtView`, `Realm`, `list`, `.plan`, `.plan`, `WarCouncil`, `net.minecraft.server.MinecraftServer`, `net.minecraft.server.level.ServerLevel`, `.postChunk`, `City`, `.render`, `TitleMapTest`, `.issue`, `RafasConquest.java`, `Terrain`, `.generate`, `MapContext`, `.place`, `City`, `ConquestServer`, `Cited Findings`, `org.jetbrains.annotations.Nullable`, `GrandStrategyScreen`, `OrganizationScreen`?**
  _High betweenness centrality (0.036) - this node is a cross-community bridge._
- **Should `Townsperson` be split into smaller, more focused modules?**
  _Cohesion score 0.12413793103448276 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._