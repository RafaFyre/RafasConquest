# Graph Report - RafasConquest  (2026-11-11)

## Corpus Check
- 464 files · ~401,163 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 6538 nodes · 20702 edges · 355 communities (146 shown, 209 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2744 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `bd6afabd`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FieldBlock
- map
- net.minecraft.world.level.levelgen.structure.BoundingBox
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsLink
- net.minecraft.network.FriendlyByteBuf
- SiegeWorks
- Arm
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- Roads
- BaseMap
- ConquestNetwork
- Peerage
- BattlePlan
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.screens.Screen
- RafasConquest Commands (guide v0.0.2)
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- HorizontalStretch
- CityManagerScreen
- .plan
- CityGates.java
- .shift
- WarCouncil
- Quests
- ConquestEvents.java
- Feud
- CapitalLayout
- PveCommands Registration
- School
- CityPlan
- Land
- net.minecraft.world.level.Level
- .layout
- net.minecraft.core.Direction
- rafasconquest.faction.Faction
- SiegeWorks.java
- Faction
- War
- ConquestSavedData
- net.minecraft.server.level.ServerPlayer
- Personalities
- SeaLanes
- org.spongepowered.asm.mixin.Mixin
- .drawEmpire
- .roadStart
- TechTreeScreen
- Centre
- .postChunk
- Row
- .view
- City
- Faction Map View
- CityVariant
- net.minecraft.world.level.block.state.BlockState
- TitleMapTest
- .deployment
- StyleLibrary
- .render
- net.minecraft.world.item.ItemStack
- RafasConquest.java
- .render
- RecruitsBattle
- Cited Findings
- CityGridBuilder
- MapLands
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- PlaceMusicClient
- net.minecraft.server.level.ServerLevel
- org.joml.Matrix4f
- .pitch
- SeaCalm
- Ownership & Territory
- .choose
- .view
- PoliticsTest
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- Override
- CourtView
- Barracks
- .generate
- ConquestServer
- WaterSwell
- FactionInfoScreen
- arraylist
- .passOnOrders
- CityStyles
- net.minecraft.resources.ResourceLocation
- Realm
- MouthLayer.java
- rafasconquest.battle.FormationSnapshot
- RoadFeature
- Lands
- .readPlacements
- .of
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- CommandTableBlock
- RecruitsBattle.java
- GateBlockEntity.java
- RoadLayer
- .allSitesInBox
- GrandStrategyScreen
- Terrain
- ClientPolitics
- .onLogout
- UnitOrder
- Settlement
- CityLayout
- RoadNetwork
- net.minecraft.world.item.DyeColor
- BattleShips
- MapContext
- net.minecraft.core.Vec3i
- BoardScreen
- Kit
- .world
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- OrganizationScreen
- MapLayer
- .step
- Builder
- DayClockTest
- Townsperson
- CLAUDE.md
- Ranks
- GroundPlan
- .plan
- PoliticalStyle
- GateScreen
- Cited Findings
- View
- Duty
- Small Cluster 170
- ClientEvents
- NobleAnnex
- .boundsAt
- ConquestWorldScreen
- NobleDistrictTest
- .send
- .pack
- CityPlacement.java
- .onLevelLoad
- Siege
- Cited Findings
- DistantArmyRenderer.java
- CityTerrainPiece
- .touches
- Kind
- Cited Findings
- net.neoforged.bus.api.SubscribeEvent
- BattleSounds
- ShipKind
- Phase
- Builder
- ConquestGameRules
- Recruiter
- Trait
- ClientBarks
- Call
- Ramming
- Task
- Profiling run sheet (baseline)
- .measure
- Homeland
- SeatCitiesTest
- Organizations
- CourtSet
- How do world generation and structure placement work, and what do they cost?
- Kind
- net.minecraft.world.level.block.Rotation
- BattleKeepInventory
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- Kind
- net.minecraft.core.BlockPos
- ClientScreens
- Swell
- .issue
- Characters (holders, skills, prowess, traits)
- RafasConquest (PvE addon for Villager Recruits)
- CountryPiece
- .at
- Job
- UnitMode
- .order
- GatePlanksBlock.java
- Role
- .load
- ItemFlags.java
- BattleSnapshot
- WorldMapItem.java
- .nearest
- BlockGrid
- .build
- .block
- .raise
- CityLayoutTest
- Kind
- Ranks (Soldier to Grand Marshal)
- WarHorn
- PoliticsData
- ConquestConfig
- How do the simulation and daily-decision systems schedule work? Do they spike?

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 279 edges
2. `FieldBattle` - 169 edges
3. `FieldBlock` - 163 edges
4. `ConquestNetwork` - 117 edges
5. `FieldArmy` - 111 edges
6. `Lands` - 89 edges
7. `Point` - 85 edges
8. `Politics` - 79 edges
9. `BaseMap` - 77 edges
10. `BattleScreen` - 70 edges

## Surprising Connections (you probably didn't know these)
- `Cited Findings` --references--> `Sprite`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/battle/FormationSnapshot.java
- `Inferences` --references--> `PoliticalMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/map/PoliticalMap.java
- `Conclusion` --references--> `RoadLayer`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Cited Findings` --references--> `RoadLayer`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Takeaway` --references--> `RoadLayer`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/RoadLayer.java

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

## Communities (355 total, 209 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.06
Nodes (8): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock, Rates

### Community 1 - "map"
Cohesion: 0.07
Nodes (7): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, PoliticalMap, FactionInfo

### Community 2 - "net.minecraft.world.level.levelgen.structure.BoundingBox"
Cohesion: 0.08
Nodes (7): CityRoadPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece, PlaceholderPiece

### Community 4 - "CityTerrain"
Cohesion: 0.07
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.08
Nodes (3): BattleScreen, Entry, Menu

### Community 6 - "DistantHorizonsLink"
Cohesion: 0.06
Nodes (9): 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), Cited Findings, Gaps, Inferences, Takeaway, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+1 more)

### Community 7 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.05
Nodes (14): BaseMapPart, BattleSpot, BoardAction, FactionInfoReply, FigureStrike, GateView, PeerageView, PoliticsView (+6 more)

### Community 8 - "SiegeWorks"
Cohesion: 0.10
Nodes (7): State, CARRIED, RAISING, UP, Work, SiegeWorks, SableSeen

### Community 10 - "Arm"
Cohesion: 0.09
Nodes (13): Arm, ARCHERS, CAVALRY, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, SKIRMISHERS, ZWEIHANDLERS (+5 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "Roads"
Cohesion: 0.12
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 13 - "BaseMap"
Cohesion: 0.12
Nodes (9): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, BaseMap, Nation, BaseMapStore (+1 more)

### Community 14 - "ConquestNetwork"
Cohesion: 0.06
Nodes (14): Bark, CameraAt, CityClose, CityView, ConquestNetwork, GateSet, MarkerSet, Place (+6 more)

### Community 15 - "Peerage"
Cohesion: 0.10
Nodes (8): Boards, Entry, View, Merit, Peer, Peerage, Taken, Quest

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.22
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "net.minecraft.client.gui.screens.Screen"
Cohesion: 0.06
Nodes (6): Character page, Court 3D scene (CourtView), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), InventoryStatsButton, Portraits

### Community 20 - "RafasConquest Commands (guide v0.0.2)"
Cohesion: 0.08
Nodes (31): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+23 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.12
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.15
Nodes (6): Facts, Officer, Said, Talk, WarCouncil, WarCouncilPagesTest

### Community 29 - "Quests"
Cohesion: 0.13
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 30 - "ConquestEvents.java"
Cohesion: 0.06
Nodes (4): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan)

### Community 31 - "Feud"
Cohesion: 0.12
Nodes (6): Clash, Feud, Kind, DUEL, SKIRMISH, FeudDuel

### Community 32 - "CapitalLayout"
Cohesion: 0.09
Nodes (10): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, WorldSettings (+2 more)

### Community 34 - "School"
Cohesion: 0.09
Nodes (14): Dojos, Ground, School, BLOOD, FIRE, ICE, IRON, LIGHT (+6 more)

### Community 35 - "CityPlan"
Cohesion: 0.14
Nodes (3): CityPlan, Piece, Plan

### Community 36 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 38 - ".layout"
Cohesion: 0.19
Nodes (4): Cited Findings, Positions, Sprite, FormationSnapshotTest

### Community 39 - "net.minecraft.core.Direction"
Cohesion: 0.22
Nodes (5): Land, Result, SiegeSurvey, Wall, SiegeSurveyTest

### Community 42 - "Faction"
Cohesion: 0.09
Nodes (10): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction, ResourceType, FOOD, PoiType, CAPITAL (+2 more)

### Community 43 - "War"
Cohesion: 0.08
Nodes (7): Cited Findings, Army, Goal, MusterBars, PoliticsSync, War, Wars

### Community 44 - "ConquestSavedData"
Cohesion: 0.07
Nodes (7): Cited Findings, ConquestSavedData, MapPreview, Poi, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 45 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.08
Nodes (12): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, Camera, CameraView, CityManager (+4 more)

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.15
Nodes (11): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+3 more)

### Community 48 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.09
Nodes (16): Battle code costs 0.2 ms; the multi-second freezes come from blocking, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, Cited Findings, Gaps, Inferences, Takeaway, Cited Findings, Chunks (+8 more)

### Community 50 - ".drawEmpire"
Cohesion: 0.29
Nodes (3): Hit, LabelLayer, LabelShape

### Community 53 - "Centre"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 54 - ".postChunk"
Cohesion: 0.16
Nodes (3): Cited Findings, BarracksPeople, CityGuards

### Community 56 - ".view"
Cohesion: 0.14
Nodes (3): QuestProgress, Retinues, RetinuesTest

### Community 57 - "City"
Cohesion: 0.12
Nodes (7): Cited Findings, City, Grid, SeatCities, CityIndex, CountrysideTest, World

### Community 58 - "Faction Map View"
Cohesion: 0.14
Nodes (3): Cited Findings, FactionMapScreen, MapView

### Community 60 - "CityVariant"
Cohesion: 0.16
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 66 - ".render"
Cohesion: 0.11
Nodes (6): CharacterScreen, Spot, ClientPeerage, Names, Honour, Honours

### Community 67 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.21
Nodes (3): Armoury, Outfit, Style

### Community 71 - "RecruitsBattle"
Cohesion: 0.03
Nodes (19): Cited Findings, Card, Enlisted, Figure, GeneralTask, RecruitsBattle, Role, BEARER (+11 more)

### Community 72 - "Cited Findings"
Cohesion: 0.13
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 73 - "CityGridBuilder"
Cohesion: 0.11
Nodes (4): CityGridBuilder, Ground, PlacedWall, Platform

### Community 74 - "MapLands"
Cohesion: 0.09
Nodes (5): Gaps, Inferences, Takeaway, Where are full scans, nested loops, repeated lookups and per-tick allocations?, MapLands

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.08
Nodes (24): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+16 more)

### Community 76 - "PlaceMusicClient"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 77 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.06
Nodes (14): GateDoors, Frame, SableLink, SiegeFrames, Door, GateBlockEntity, Swing, CLOSED (+6 more)

### Community 78 - "org.joml.Matrix4f"
Cohesion: 0.21
Nodes (3): DistantDepth, Arrow, ArrowVolleys

### Community 79 - ".pitch"
Cohesion: 0.10
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 81 - "SeaCalm"
Cohesion: 0.19
Nodes (3): Sea, Depths, SeaCalm

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.12
Nodes (11): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+3 more)

### Community 84 - ".view"
Cohesion: 0.11
Nodes (9): NobleTitle, COUNT, KNIGHT, NONE, PeerageSync, Option, Talk, View (+1 more)

### Community 86 - "Calibration"
Cohesion: 0.14
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.07
Nodes (22): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+14 more)

### Community 89 - "AgeTest.java"
Cohesion: 0.14
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 90 - "Override"
Cohesion: 0.08
Nodes (5): BattleSite.Water, DialogueScreen, Drop, MarkerScreen, Politics.Front

### Community 92 - "CourtView"
Cohesion: 0.07
Nodes (7): Camera, CourtView, Figure, NationScreen, Banner, Demographics, Share

### Community 94 - "Barracks"
Cohesion: 0.20
Nodes (3): Barracks, BarracksPiece, BarracksTest

### Community 95 - ".generate"
Cohesion: 0.09
Nodes (6): Dojo, ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry

### Community 96 - "ConquestServer"
Cohesion: 0.14
Nodes (3): ConquestServer, StructureLink, PoiPrecomputer

### Community 98 - "WaterSwell"
Cohesion: 0.15
Nodes (7): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, WaterSwell, SeaFoamTest

### Community 100 - "arraylist"
Cohesion: 0.05
Nodes (18): Base map generated from seed, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Demographics (population by culture and race), Faction Info screen, Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, World map layers (+10 more)

### Community 101 - ".passOnOrders"
Cohesion: 0.07
Nodes (3): Landing, Live, TickTimes

### Community 102 - "CityStyles"
Cohesion: 0.22
Nodes (3): Building, CityStyles, Houses

### Community 103 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.22
Nodes (3): UnitLayer, RecruiterRenderer, TownspersonRenderer

### Community 105 - "Realm"
Cohesion: 0.12
Nodes (4): Hook, Realm, Succession, Taking

### Community 106 - "MouthLayer.java"
Cohesion: 0.10
Nodes (5): Cited Findings, BackBannerLayer, GateRenderer, MarkerRenderer, MouthLayer

### Community 107 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.31
Nodes (6): Boxes, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 109 - "Lands"
Cohesion: 0.07
Nodes (7): Inferences, Lord, Feuds, Lands, Claim, Front, Politics

### Community 110 - ".readPlacements"
Cohesion: 0.27
Nodes (3): Offsets, Placement, PlacementOffsetsTest

### Community 111 - ".of"
Cohesion: 0.16
Nodes (5): FactionBanner, Layer, FactionBannerNbt, CityBanners, FactionBannerTest

### Community 112 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.16
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips, Ride

### Community 115 - "RecruitsBattle.java"
Cohesion: 0.04
Nodes (5): Battle phases and days, Envelopment, TickWeave experiment, War camps, War council

### Community 116 - "GateBlockEntity.java"
Cohesion: 0.09
Nodes (4): Palette, WallTemplates, MarkerBlockEntity, MarkerSpec

### Community 117 - "RoadLayer"
Cohesion: 0.16
Nodes (11): A tick budget and staggered schedules make the fixes stick, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom?, Gaps (+3 more)

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.15
Nodes (3): GrandStrategyScreen, Spot, GrandStrategyDateTest

### Community 123 - ".onLogout"
Cohesion: 0.12
Nodes (4): BackBanners, ClientMapCache, Throw, ThrownFigures

### Community 124 - "UnitOrder"
Cohesion: 0.09
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 125 - "Settlement"
Cohesion: 0.10
Nodes (11): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+3 more)

### Community 126 - "CityLayout"
Cohesion: 0.08
Nodes (10): CityLayout, Side, EAST, NORTH, SOUTH, WEST, Site, SlopeTile (+2 more)

### Community 128 - "RoadNetwork"
Cohesion: 0.15
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 129 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.16
Nodes (5): DistantShips, Far, BattleKind, Part, ShipHulls

### Community 130 - "BattleShips"
Cohesion: 0.26
Nodes (3): BattleShips, Helm, Ram

### Community 136 - "Kit"
Cohesion: 0.11
Nodes (18): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+10 more)

### Community 137 - ".world"
Cohesion: 0.15
Nodes (3): Retainer, RealmPlannerTest, World

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (24): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings, Cited Findings (+16 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 5. Water/ocean waves (sea swell, foam, boats on waves), 6. Sodium (and Iris) compatibility, 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings (+28 more)

### Community 142 - "MapLayer"
Cohesion: 0.18
Nodes (5): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer

### Community 143 - ".step"
Cohesion: 0.19
Nodes (5): FigureMotion, Match, Placed, State, FigureMotionTest

### Community 146 - "Builder"
Cohesion: 0.19
Nodes (5): QuestBoardBlock, Builder, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 148 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 149 - "Townsperson"
Cohesion: 0.10
Nodes (5): Walk, DistrictKey, Townsfolk, Townsperson, StreetWalksTest

### Community 152 - "Ranks"
Cohesion: 0.17
Nodes (3): Ranks, Offer, Recruitment

### Community 155 - "GroundPlan"
Cohesion: 0.11
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 156 - ".plan"
Cohesion: 0.12
Nodes (8): Centre, Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 163 - "Cited Findings"
Cohesion: 0.17
Nodes (6): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, BattleSpotRing

### Community 164 - "View"
Cohesion: 0.15
Nodes (4): BoardView, GrandStrategyView, StatsView, TalkView

### Community 167 - "Duty"
Cohesion: 0.13
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 181 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 204 - "ConquestWorldScreen"
Cohesion: 0.20
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 206 - ".send"
Cohesion: 0.07
Nodes (10): BackBanner, ChargeHits, MarkerView, Entry, Msg, SimpleChannel, Type, CLEAR (+2 more)

### Community 217 - ".pack"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 218 - "CityPlacement.java"
Cohesion: 0.12
Nodes (4): ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement

### Community 221 - "Siege"
Cohesion: 0.06
Nodes (19): Point, Engine, LADDERS, RAMS, TOWERS, Gate, GateAction, GateState (+11 more)

### Community 223 - "Cited Findings"
Cohesion: 0.16
Nodes (3): DistantArmyRenderer, Figure, Hull

### Community 224 - "DistantArmyRenderer.java"
Cohesion: 0.04
Nodes (4): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), ClientModEvents

### Community 231 - "Kind"
Cohesion: 0.22
Nodes (7): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL

### Community 232 - "Cited Findings"
Cohesion: 0.09
Nodes (8): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, FormationSnapshot, Ship, DistantArmies

### Community 235 - "BattleSounds"
Cohesion: 0.30
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 236 - "ShipKind"
Cohesion: 0.33
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 237 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 249 - "Recruiter"
Cohesion: 0.12
Nodes (3): Barks, Ear, Recruiter

### Community 252 - "Trait"
Cohesion: 0.13
Nodes (7): Officers, GameCharacter, Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 257 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "Homeland"
Cohesion: 0.10
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 274 - "Organizations"
Cohesion: 0.20
Nodes (4): Organization, Organizations, Sanctum, OrganizationsTest

### Community 275 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 277 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.20
Nodes (6): Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Takeaway, Api, DistantGeneration

### Community 278 - "Kind"
Cohesion: 0.15
Nodes (10): Kind, ARCHERS, BEARER, CAVALRY, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY (+2 more)

### Community 288 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.17
Nodes (8): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Enlisted players, The general and guard, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Battle roles (frontline, bait, support, flanker, kill squad), Victory, pursuit and surrender, The war horn

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "net.minecraft.core.BlockPos"
Cohesion: 0.09
Nodes (4): Saved, WarCamp, CharacterMarkers, Placed

### Community 300 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.13
Nodes (13): Battles (prototype), Blocks of 20 soldiers, Characters (holders, skills, prowess, traits), Cultures (seven homelands), Epic Knights optional armour (Armoury), Faiths and cults, Heralds (daily news), Land grants (seven ways) (+5 more)

### Community 301 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 302 - "CountryPiece"
Cohesion: 0.16
Nodes (7): Build, CASTLE, HAMLET, RUIN, TOWER, WELL, CountryPiece

### Community 305 - "Job"
Cohesion: 0.06
Nodes (22): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+14 more)

### Community 306 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 308 - ".order"
Cohesion: 0.25
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

### Community 310 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 322 - ".build"
Cohesion: 0.12
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 325 - ".raise"
Cohesion: 0.33
Nodes (4): Stronghold, Muster, CEREMONIAL, OFFENSIVE

### Community 327 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 332 - "Ranks (Soldier to Grand Marshal)"
Cohesion: 0.50
Nodes (5): Gold church, Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter

### Community 344 - "How do the simulation and daily-decision systems schedule work? Do they spike?"
Cohesion: 0.67
Nodes (3): Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Takeaway

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **363 isolated node(s):** `LEVY`, `HEAVY_INFANTRY`, `SKIRMISHERS`, `ZWEIHANDLERS`, `ARCHERS` (+358 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1245 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **209 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `net.minecraft.world.item.DyeColor`, `net.minecraft.world.level.levelgen.structure.BoundingBox`, `.army`, `SiegeWorks`, `.isOurs`, `Peerage`, `WarCouncil`, `ConquestEvents.java`, `Feud`, `PveCommands Registration`, `net.minecraft.core.BlockPos`, `net.minecraft.core.Direction`, `rafasconquest.faction.Faction`, `net.minecraft.server.level.ServerPlayer`, `UnitMode`, `.roadStart`, `net.minecraft.world.item.ItemStack`, `.block`, `RafasConquest.java`, `net.minecraft.server.level.ServerLevel`, `.pitch`, `.thrown`, `Calibration`, `.generate`, `.passOnOrders`, `net.neoforged.bus.api.SubscribeEvent`, `Phase`, `UnitOrder`, `RecruitsBattle.java`, `Trait`?**
  _High betweenness centrality (0.085) - this node is a cross-community bridge._
- **What connects `LEVY`, `HEAVY_INFANTRY`, `SKIRMISHERS` to the rest of the system?**
  _363 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.05543213794562573 - nodes in this community are weakly interconnected._
- **Why does `ConquestNetwork` connect `ConquestNetwork` to `PveCommands Registration`, `map`, `arraylist`, `net.minecraft.world.level.Level`, `View`, `net.minecraft.network.FriendlyByteBuf`, `Cited Findings`, `RecruitsBattle`, `.send`, `net.minecraft.client.gui.screens.Screen`, `RecruitsBattle.java`, `CityManagerScreen`, `BattleSnapshot`, `UnitOrder`?**
  _High betweenness centrality (0.031) - this node is a cross-community bridge._
- **Should `map` be split into smaller, more focused modules?**
  _Cohesion score 0.06663141195134849 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._