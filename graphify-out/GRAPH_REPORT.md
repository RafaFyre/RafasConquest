# Graph Report - RafasConquest  (2026-11-11)

## Corpus Check
- 465 files · ~409,175 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 6637 nodes · 21102 edges · 344 communities (146 shown, 198 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2831 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `502ee3db`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FieldBlock
- PoliticsData
- net.minecraft.world.level.levelgen.structure.BoundingBox
- .army
- CityTerrain
- BattleScreen
- .onLogout
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- SiegeWorks
- ConquestNetwork
- net.minecraft.world.entity.Entity
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- City
- BaseMap
- net.minecraft.world.phys.Vec3
- Peerage
- BattlePlan
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.screens.Screen
- PveCommands.java
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- HorizontalStretch
- CityManagerScreen
- .plan
- org.jetbrains.annotations.Nullable
- .shift
- WarCouncil
- TechTreeScreen
- ConquestEvents.java
- net.minecraft.network.FriendlyByteBuf
- CapitalLayout
- PveCommands Registration
- School
- CityPlan
- Land
- Townsperson
- .render
- net.minecraft.core.Direction
- rafasconquest.faction.Faction
- Swell
- .stat
- net.minecraft.server.MinecraftServer
- Faction
- War
- Personalities
- SeaLanes
- org.spongepowered.asm.mixin.Mixin
- .feudFight
- CityVariant
- org.jetbrains.annotations.Nullable
- Centre
- Barracks
- Row
- .accept
- City
- Faction Map View
- .wallSegments
- Cited Findings
- net.minecraft.world.level.block.state.BlockState
- TitleMapTest
- .saveFormation
- StyleLibrary
- .step
- net.minecraft.world.level.block.Rotation
- RafasConquest.java
- CityTerrainPiece
- Cited Findings
- DistantOcclusion
- Countryside
- MapLands
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- PlaceMusicClient
- GateBlockEntity
- Quests
- BattleType
- Realm
- Ownership & Territory
- .choose
- .view
- PoliticsTest
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- How do world generation and structure placement work, and what do they cost?
- .view
- MapLayer
- .generate
- ConquestSavedData
- .walks
- FactionInfoScreen
- RoadFeature.java
- ConquestWorldgen
- CityStyles
- .layout
- Realm
- MouthLayer.java
- rafasconquest.battle.FormationSnapshot
- MarkerScreen
- .send
- Peerage
- .of
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- WaterSwell
- Phase
- NobleTitle
- WallTemplates
- RoadLayer
- .fire
- GrandStrategyScreen
- Terrain
- ClientPolitics
- Kind
- UnitOrder
- BlockPos
- CityLayout
- RoadNetwork
- .hash
- BattleShips
- .render
- net.minecraft.core.Vec3i
- .sendToServer
- Plan
- .readPlacements
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- ConquestWorldScreen
- CourtSet
- CityGridBuilder
- Race
- Ranks
- .build
- CLAUDE.md
- Feud
- .place
- .plan
- .startAt
- How Total War calculates battles between units
- net.minecraft.network.RegistryFriendlyByteBuf
- Override
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- Override
- MapContext
- BoardScreen
- Build
- NobleDistrictTest
- net.neoforged.neoforge.network.handling.IPayloadContext
- .pack
- GatePlanksBlock.java
- .drawEmpire
- Point
- Morale
- DistantArmyRenderer.java
- Chunks
- .touches
- GateScreen
- BattleSounds
- net.neoforged.bus.api.SubscribeEvent
- BlockGrid
- SetSetting
- DistantArmyRenderer
- net.minecraft.resources.ResourceLocation
- ConquestGameRules
- ShipKind
- Trait
- ThrownFigures
- Call
- Ramming
- ClientEvents
- Profiling run sheet (baseline)
- .measure
- .view
- RafasConquest (PvE addon for Villager Recruits)
- OrganizationScreen
- CourtView
- .boundsAt
- CameraView
- 7. Client tick, stutter and sound
- Kind
- WarCamp
- ClientScreens
- ShipHulls
- How do the simulation and daily-decision systems schedule work? Do they spike?
- Cited Findings
- .postChunk
- RecruitsBattle
- BattleKeepInventory
- org.joml.Matrix4f
- Task
- .onLevelLoad
- Role
- HarbourCut
- Duty
- Recruiter
- .roadStart
- ClientModEvents.java
- ClientBarks
- ConquestConfig
- CityManager

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 283 edges
2. `FieldBlock` - 216 edges
3. `FieldBattle` - 194 edges
4. `FieldArmy` - 136 edges
5. `Point` - 128 edges
6. `ConquestNetwork` - 121 edges
7. `Lands` - 89 edges
8. `BattleScreen` - 82 edges
9. `Politics` - 79 edges
10. `BaseMap` - 77 edges

## Surprising Connections (you probably didn't know these)
- `Cited Findings` --references--> `FormationSnapshot`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/battle/FormationSnapshot.java
- `Cited Findings` --references--> `Sprite`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/battle/FormationSnapshot.java
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

## Communities (344 total, 198 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.05
Nodes (9): The core change: morale becomes a sum, FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock (+1 more)

### Community 2 - "net.minecraft.world.level.levelgen.structure.BoundingBox"
Cohesion: 0.06
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - ".army"
Cohesion: 0.07
Nodes (4): DoctrineTest, FieldBattleTest, GrandStrategyDateTest, ArmouryTest

### Community 4 - "CityTerrain"
Cohesion: 0.07
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.07
Nodes (4): BattleScreen, Entry, Menu, UnitInfo

### Community 6 - ".onLogout"
Cohesion: 0.07
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.13
Nodes (4): GateDoors, Frame, SableLink, SiegeFrames

### Community 8 - "SiegeWorks"
Cohesion: 0.11
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 9 - "ConquestNetwork"
Cohesion: 0.07
Nodes (14): Bark, ChargeHits, CityClose, ConquestNetwork, GrandStrategyView, OpenBattleScreen, PeerageView, Place (+6 more)

### Community 10 - "net.minecraft.world.entity.Entity"
Cohesion: 0.10
Nodes (6): FieldRules, Rates, Side, FLANK, FRONT, REAR

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "City"
Cohesion: 0.08
Nodes (17): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+9 more)

### Community 13 - "BaseMap"
Cohesion: 0.08
Nodes (12): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, ClientMapCache, MapPreview, BaseMap (+4 more)

### Community 14 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.06
Nodes (21): Cited Findings, Card, Enlisted, Role, BEARER, GENERAL, GUARD, LEADER (+13 more)

### Community 15 - "Peerage"
Cohesion: 0.15
Nodes (8): Boards, Entry, View, Quest, Type, CLEAR, DELIVER, SCOUT

### Community 16 - "BattlePlan"
Cohesion: 0.04
Nodes (22): 1. Flank and rear shock, 2. Nearby rout (fright), 3. Army-wide losses, 4. Shattering, Design: adding the four morale shocks (built 2026-11-11, untested in game), Tests (FieldBattleTest), Arm, ARCHERS (+14 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.15
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 20 - "PveCommands.java"
Cohesion: 0.08
Nodes (31): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+23 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.09
Nodes (13): TerritoryMap, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoliticalMap (+5 more)

### Community 27 - ".shift"
Cohesion: 0.20
Nodes (5): BattleSite, Part, Water, BattleSite.Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.13
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 29 - "TechTreeScreen"
Cohesion: 0.18
Nodes (4): TechTreeScreen, Tech, TechTree, TechTreeTest

### Community 30 - "ConquestEvents.java"
Cohesion: 0.04
Nodes (8): CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 31 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.07
Nodes (8): BattleSnapshot, BoardView, FactionInfoReply, PoliticsView, TalkView, UnitsRequest, UnitsState, Volleys

### Community 32 - "CapitalLayout"
Cohesion: 0.17
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.14
Nodes (3): CityPlan, Piece, Plan

### Community 36 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 38 - ".render"
Cohesion: 0.12
Nodes (5): CharacterScreen, Retainer, Spot, Banner, Portraits

### Community 39 - "net.minecraft.core.Direction"
Cohesion: 0.13
Nodes (7): Land, Result, SiegeSurvey, Wall, CityGates, Done, SiegeSurveyTest

### Community 41 - "Swell"
Cohesion: 0.13
Nodes (4): Depths, SeaCalm, Swell, SwellTest

### Community 42 - ".stat"
Cohesion: 0.09
Nodes (10): ClientPeerage, Honour, Honours, Stat, DIPLOMACY, INTRIGUE, LEARNING, MARTIAL (+2 more)

### Community 43 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.11
Nodes (5): Cited Findings, Goal, MusterBars, PoliticsSync, Wars

### Community 44 - "Faction"
Cohesion: 0.09
Nodes (5): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction, FactionInfoTest

### Community 45 - "War"
Cohesion: 0.10
Nodes (5): Army, Muster, CEREMONIAL, OFFENSIVE, War

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.16
Nodes (5): ChunkMapCameraMixin, CityVegetationMixin, ClientChunkWindowMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 50 - ".feudFight"
Cohesion: 0.14
Nodes (6): Clash, Feud, Kind, DUEL, SKIRMISH, FeudDuel

### Community 51 - "CityVariant"
Cohesion: 0.16
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 52 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.03
Nodes (4): Battle phases and days, Envelopment, War camps, War council

### Community 53 - "Centre"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 55 - "Row"
Cohesion: 0.12
Nodes (3): Fort, Stronghold, Row

### Community 57 - "City"
Cohesion: 0.10
Nodes (6): Grid, SeatCities, CountrysideTest, World, SeatCitiesTest, World

### Community 58 - "Faction Map View"
Cohesion: 0.14
Nodes (3): Cited Findings, FactionMapScreen, MapView

### Community 59 - ".wallSegments"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 60 - "Cited Findings"
Cohesion: 0.15
Nodes (6): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, DistantArmies

### Community 61 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (8): Sea, CommandTableBlock, GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, Builder, MarkerBlock

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - ".saveFormation"
Cohesion: 0.08
Nodes (3): BattleMap, GroundPlanTest, Land

### Community 66 - ".step"
Cohesion: 0.18
Nodes (5): FigureMotion, Match, Placed, State, FigureMotionTest

### Community 67 - "net.minecraft.world.level.block.Rotation"
Cohesion: 0.14
Nodes (10): Battles (prototype), Blocks of 20 soldiers, Gold church, Land grants (seven ways), Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter (+2 more)

### Community 69 - "RafasConquest.java"
Cohesion: 0.03
Nodes (4): Battle map developer web page, Morale, rout and rally, StreetWalks, RafasConquest

### Community 71 - "Cited Findings"
Cohesion: 0.13
Nodes (7): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Cited Findings, Gaps, Inferences, Takeaway, BattleSpotRing

### Community 72 - "DistantOcclusion"
Cohesion: 0.13
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 73 - "Countryside"
Cohesion: 0.09
Nodes (11): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+3 more)

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.08
Nodes (24): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+16 more)

### Community 76 - "PlaceMusicClient"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 77 - "GateBlockEntity"
Cohesion: 0.07
Nodes (10): Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING, GateGeometry (+2 more)

### Community 78 - "Quests"
Cohesion: 0.12
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 79 - "BattleType"
Cohesion: 0.14
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 81 - "Realm"
Cohesion: 0.22
Nodes (4): Kind, FRIEND, RIVAL, Realm

### Community 82 - "Ownership & Territory"
Cohesion: 0.20
Nodes (5): Candidate, OwnershipResolver, Claim, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.17
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 84 - ".view"
Cohesion: 0.19
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 86 - "Calibration"
Cohesion: 0.17
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.06
Nodes (25): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender, Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT (+17 more)

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.40
Nodes (4): Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Takeaway

### Community 92 - ".view"
Cohesion: 0.08
Nodes (14): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+6 more)

### Community 94 - "MapLayer"
Cohesion: 0.18
Nodes (5): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer

### Community 95 - ".generate"
Cohesion: 0.09
Nodes (6): ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 96 - "ConquestSavedData"
Cohesion: 0.06
Nodes (8): Cited Findings, ConquestSavedData, Poi, CityBanners, ConquestServer, EconomyTick, PoiPrecomputer, ConquestSavedDataTest

### Community 98 - ".walks"
Cohesion: 0.10
Nodes (4): DistrictKey, Townsfolk, CitySizesTest, StreetWalksTest

### Community 99 - "FactionInfoScreen"
Cohesion: 0.15
Nodes (3): FactionInfoScreen, Line, StructureLink

### Community 100 - "RoadFeature.java"
Cohesion: 0.05
Nodes (34): Ambushes from woods, Base map generated from seed, Character page, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Command Table block, Countryside (hamlets, villages, watchtowers, castles) (+26 more)

### Community 103 - ".layout"
Cohesion: 0.20
Nodes (4): Cited Findings, Positions, Sprite, FormationSnapshotTest

### Community 105 - "Realm"
Cohesion: 0.09
Nodes (8): Hook, Kind, FAVOUR, SECRET, Succession, Taking, RealmPlannerTest, World

### Community 106 - "MouthLayer.java"
Cohesion: 0.11
Nodes (4): BackBannerLayer, GateRenderer, MarkerRenderer, MouthLayer

### Community 107 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.15
Nodes (8): FormationSnapshot, Ship, Boxes, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 109 - ".send"
Cohesion: 0.11
Nodes (6): BattleSpot, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel

### Community 110 - "Peerage"
Cohesion: 0.12
Nodes (6): Merit, Peer, Peerage, Taken, PeerageSync, QuestProgress

### Community 111 - ".of"
Cohesion: 0.20
Nodes (4): FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 112 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.15
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips, Ride

### Community 113 - "WaterSwell"
Cohesion: 0.13
Nodes (8): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, SableSeen, WaterSwell, SeaFoamTest

### Community 114 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 115 - "NobleTitle"
Cohesion: 0.14
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 117 - "RoadLayer"
Cohesion: 0.16
Nodes (11): A tick budget and staggered schedules make the fixes stick, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom?, Gaps (+3 more)

### Community 123 - "Kind"
Cohesion: 0.15
Nodes (10): Kind, ARCHERS, BEARER, CAVALRY, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY (+2 more)

### Community 124 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 125 - "BlockPos"
Cohesion: 0.15
Nodes (4): BoardAction, GateView, MarkerSet, MarkerView

### Community 126 - "CityLayout"
Cohesion: 0.08
Nodes (6): CityLayout, District, Site, SlopeTile, WorldSettings, CityLayoutTest

### Community 128 - "RoadNetwork"
Cohesion: 0.14
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 129 - ".hash"
Cohesion: 0.14
Nodes (6): ChunkCoord, FarmLayout, NobleAnnex, OriginRoller, Roll, SeedHash

### Community 130 - "BattleShips"
Cohesion: 0.27
Nodes (3): BattleShips, Helm, Ram

### Community 136 - "Plan"
Cohesion: 0.22
Nodes (7): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL

### Community 137 - ".readPlacements"
Cohesion: 0.27
Nodes (3): Offsets, Placement, PlacementOffsetsTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (24): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings, Cited Findings (+16 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.06
Nodes (35): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 6. Sodium (and Iris) compatibility, 8. Lessons from performance mods that a content mod can apply, Cited Findings (+27 more)

### Community 141 - "ConquestWorldScreen"
Cohesion: 0.22
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 142 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 143 - "CityGridBuilder"
Cohesion: 0.09
Nodes (5): Houses, CityGridBuilder, Ground, PlacedWall, Platform

### Community 146 - "Race"
Cohesion: 0.20
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 148 - "Ranks"
Cohesion: 0.06
Nodes (18): Enlisted players, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Battle roles (frontline, bait, support, flanker, kill squad), The war horn, GeneralTask, BattleRole, BAIT, FLANKER (+10 more)

### Community 152 - "Feud"
Cohesion: 0.08
Nodes (7): Cited Findings, Lord, Feuds, Lands, Claim, Front, Politics

### Community 156 - ".plan"
Cohesion: 0.09
Nodes (13): Centre, Faith, Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW (+5 more)

### Community 157 - ".startAt"
Cohesion: 0.14
Nodes (4): Ground, GroundPlan, Api, DistantGeneration

### Community 160 - "How Total War calculates battles between units"
Cohesion: 0.17
Nodes (11): 1. Combat is per soldier, not per unit, 2. Does the swing land?, 3. How much damage the hit does, 4. Charges, 5. Missiles, 6. Leadership (morale): what actually wins battles, 7. Autoresolve, 8. Older games (Shogun 1, Medieval 1, Rome 1) (+3 more)

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.06
Nodes (25): WarHorn, BackBanners, Armoury, Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY (+17 more)

### Community 174 - "Override"
Cohesion: 0.24
Nodes (3): Land, Land, Land

### Community 178 - "MapContext"
Cohesion: 0.18
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 204 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 206 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.10
Nodes (6): CameraAt, CityView, GateSet, TalkChoice, TeleportTo, Entry

### Community 217 - ".pack"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 219 - ".drawEmpire"
Cohesion: 0.27
Nodes (3): Hit, LabelLayer, LabelShape

### Community 221 - "Point"
Cohesion: 0.06
Nodes (18): Engine, LADDERS, RAMS, TOWERS, Gate, GateAction, GateState, Kind (+10 more)

### Community 223 - "Morale"
Cohesion: 0.28
Nodes (6): Morale, State, RALLIED, ROUTED, SHATTERED, STEADY

### Community 232 - "BattleSounds"
Cohesion: 0.19
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.15
Nodes (3): DistantArmyRenderer, Figure, Hull

### Community 238 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.18
Nodes (3): UnitLayer, RecruiterRenderer, TownspersonRenderer

### Community 249 - "ShipKind"
Cohesion: 0.33
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

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

### Community 263 - ".view"
Cohesion: 0.24
Nodes (7): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, GrandStrategy, View

### Community 267 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.17
Nodes (8): Arrows in flight (figure volleys), Battle sounds, Recruits PvE Addon Design Doc (PDF), Seeing the whole field (distant flat figures), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 275 - "CourtView"
Cohesion: 0.08
Nodes (6): Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 277 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 283 - "CameraView"
Cohesion: 0.11
Nodes (11): Battle code costs 0.2 ms; the multi-second freezes come from blocking, Inferences, Cited Findings, Gaps, Inferences, Mixins on server paths: what they hook, how hot, added cost, Takeaway, Camera (+3 more)

### Community 288 - "7. Client tick, stutter and sound"
Cohesion: 0.40
Nodes (5): 7. Client tick, stutter and sound, Cited Findings, Gaps, Inferences, Takeaway

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "WarCamp"
Cohesion: 0.09
Nodes (4): Saved, WarCamp, CharacterMarkers, Placed

### Community 295 - "ShipHulls"
Cohesion: 0.17
Nodes (5): DistantShips, Far, BattleKind, Part, ShipHulls

### Community 298 - "How do the simulation and daily-decision systems schedule work? Do they spike?"
Cohesion: 0.50
Nodes (4): Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway

### Community 302 - "Cited Findings"
Cohesion: 0.24
Nodes (6): 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, Cited Findings, Gaps, Inferences, Takeaway, ChunkMapAccess

### Community 303 - ".postChunk"
Cohesion: 0.15
Nodes (3): Cited Findings, BarracksPeople, CityGuards

### Community 306 - "RecruitsBattle"
Cohesion: 0.03
Nodes (13): Point, Figure, HornCall, ATTACKING, MOVING, RALLYING, RETREATING, Landing (+5 more)

### Community 311 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 320 - "Role"
Cohesion: 0.16
Nodes (9): GameCharacter, Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN (+1 more)

### Community 323 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 324 - "Recruiter"
Cohesion: 0.08
Nodes (4): WorldMapItem, Barks, Ear, Recruiter

## Ambiguous Edges - Review These
- `Roads (high, city, town roads; bridges; signposts)` → `Mustering (offensive and ceremonial)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **371 isolated node(s):** `ADVANCE`, `CAVALRY_CHARGE`, `CAVALRY_RECALL`, `WALL`, `GENERAL_CHARGES` (+366 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1273 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **198 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `net.minecraft.world.level.levelgen.structure.BoundingBox`, `.army`, `SiegeWorks`, `net.minecraft.world.entity.Entity`, `net.minecraft.world.phys.Vec3`, `Ranks`, `PveCommands.java`, `.shift`, `WarCouncil`, `.startAt`, `ConquestEvents.java`, `PveCommands Registration`, `WarCamp`, `net.minecraft.network.RegistryFriendlyByteBuf`, `net.minecraft.world.item.ItemStack`, `rafasconquest.faction.Faction`, `ShipHulls`, `net.minecraft.core.Direction`, `.feudFight`, `org.jetbrains.annotations.Nullable`, `.saveFormation`, `Role`, `RafasConquest.java`, `.roadStart`, `Calibration`, `.generate`, `net.neoforged.bus.api.SubscribeEvent`, `.send`, `Peerage`, `UnitOrder`, `Phase`, `Trait`?**
  _High betweenness centrality (0.066) - this node is a cross-community bridge._
- **What connects `ADVANCE`, `CAVALRY_CHARGE`, `CAVALRY_RECALL` to the rest of the system?**
  _371 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.04610834789622261 - nodes in this community are weakly interconnected._
- **Why does `ConquestNetwork` connect `ConquestNetwork` to `PveCommands Registration`, `net.minecraft.network.RegistryFriendlyByteBuf`, `ClientScreens`, `BattleScreen`, `.sendToServer`, `GateScreen`, `net.minecraft.server.MinecraftServer`, `SetSetting`, `.send`, `net.neoforged.neoforge.network.handling.IPayloadContext`, `net.minecraft.client.gui.screens.Screen`, `org.jetbrains.annotations.Nullable`, `BlockPos`, `list`, `CityManagerScreen`, `Cited Findings`, `net.minecraft.world.level.block.state.BlockState`, `net.minecraft.network.FriendlyByteBuf`?**
  _High betweenness centrality (0.040) - this node is a cross-community bridge._
- **Should `net.minecraft.world.level.levelgen.structure.BoundingBox` be split into smaller, more focused modules?**
  _Cohesion score 0.06336996336996337 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Roads (high, city, town roads; bridges; signposts)` and `Mustering (offensive and ceremonial)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._