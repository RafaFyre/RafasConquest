# Graph Report - RafasConquest  (2026-11-12)

## Corpus Check
- 475 files · ~441,495 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7055 nodes · 22419 edges · 360 communities (142 shown, 218 thin omitted)
- Extraction: 86% EXTRACTED · 14% INFERRED · 0% AMBIGUOUS · INFERRED: 3084 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `5de9b9b9`
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
- FieldRules
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- Land
- BaseMap
- UnitOrder
- .enlist
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.GuiGraphics
- PveCommands.java
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- list
- HorizontalStretch
- CityManagerScreen
- .plan
- Placed
- .shift
- WarCouncil
- org.jetbrains.annotations.Nullable
- .view
- .findGenerationPoint
- CityLayout
- PveCommands Registration
- School
- CityPlan
- .of
- net.minecraft.network.RegistryFriendlyByteBuf
- RecruitsBattle
- rafasconquest.faction.Faction
- arraylist
- Feud
- net.minecraft.server.MinecraftServer
- GateBlockEntity.java
- CityGridBuilder.java
- Personalities
- SeaLanes
- BattleType
- Cited Findings
- Capture
- Faction
- Centre
- Barracks
- Row
- map
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
- net.minecraft.server.level.ServerLevel
- DistantShips
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
- Work
- City
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- CityLayout
- Politics
- .holder
- .generate
- net.minecraft.core.Vec3i
- DistantOcclusion
- FactionInfoScreen
- .icon
- Front
- Quest
- FigureBatches
- .get
- MouthLayer.java
- ConquestNetwork
- MarkerScreen
- net.minecraft.sounds.SoundEvent
- Gate
- City
- BattleShips
- WaterSwell
- Arm
- ConquestSavedData
- Kind
- Cited Findings
- uuid
- GrandStrategyScreen
- MapLayer
- CapitalLayout
- .tick
- Duty
- Ranks
- Plan
- BattleShips
- Kit
- .complete
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
- Rank
- RoadNetwork
- CLAUDE.md
- Job
- .plan
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
- Morale
- .bark
- WarPlan
- net.minecraft.server.level.ServerPlayer
- .view
- Siege
- RecruitFigures.java
- .hash
- net.neoforged.bus.api.SubscribeEvent
- ClientPolitics
- Homeland
- net.minecraft.resources.ResourceLocation
- net.neoforged.bus.api.SubscribeEvent
- .order
- ItemFlags.java
- DistantArmyRenderer
- .hinge
- .load
- ConquestGameRules
- net.minecraft.network.FriendlyByteBuf
- Trait
- OrganizationScreen
- Call
- Ramming
- .nearest
- Profiling run sheet (baseline)
- .measure
- .passOnOrders
- BlockGrid
- GateBlockEntity
- BattleKeepInventory.java
- .onLogout
- Lands
- CourtView
- CityTerrainPiece
- Realm
- .view
- Peerage
- Kind
- BattlePlan.java
- How Total War calculates battles between units
- .readPlacements
- NetworkDirection
- .boundsAt
- BattleSnapshot
- .findGenerationPoint
- .inCity
- .postChunk
- Slot
- .feudFight
- State
- CountryPiece
- ConquestWorldScreen
- GatePlanksBlock
- RecruitsBattle.java
- .issue
- BackBanners
- .build
- Recruiter
- org.spongepowered.asm.mixin.Mixin
- .named
- ShareLiveTest
- .eachSpeakersRunIsAPageWithTheChoicesOnTheLast
- 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound
- ClientBarks
- WarHorn
- ShipKind
- net.minecraft.core.BlockPos
- net.neoforged.fml.common.EventBusSubscriber

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 295 edges
2. `FieldBlock` - 246 edges
3. `FieldBattle` - 211 edges
4. `FieldArmy` - 127 edges
5. `ConquestNetwork` - 121 edges
6. `Lands` - 89 edges
7. `Siege` - 84 edges
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

## Communities (360 total, 218 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.04
Nodes (10): The core change: morale becomes a sum, FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock (+2 more)

### Community 1 - "Townsperson"
Cohesion: 0.12
Nodes (4): Walk, DistrictKey, Townsfolk, Townsperson

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.07
Nodes (8): BarracksPiece, CityRoadPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece, PlaceholderPiece

### Community 3 - ".army"
Cohesion: 0.06
Nodes (4): Point, DoctrineTest, FieldBattleTest, SiegeTest

### Community 4 - "CityTerrain"
Cohesion: 0.07
Nodes (17): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+9 more)

### Community 5 - "BattleScreen"
Cohesion: 0.08
Nodes (3): BattleScreen, Entry, Menu

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.05
Nodes (9): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+1 more)

### Community 7 - "CityGridBuilder"
Cohesion: 0.07
Nodes (9): CityGridBuilder, Ground, Platform, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY (+1 more)

### Community 9 - "ShipHulls"
Cohesion: 0.38
Nodes (3): BattleKind, Part, ShipHulls

### Community 10 - "FieldRules"
Cohesion: 0.18
Nodes (5): FieldRules, Side, FLANK, FRONT, REAR

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 13 - "BaseMap"
Cohesion: 0.08
Nodes (10): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, ClientMapCache, BaseMap, Nation (+2 more)

### Community 14 - "UnitOrder"
Cohesion: 0.09
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 16 - ".enlist"
Cohesion: 0.05
Nodes (14): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT, GeneralTask (+6 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.24
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.04
Nodes (3): Races (Sunborn, Vinekin, Meadowfolk, ...), GateScreen, InventoryStatsButton

### Community 20 - "PveCommands.java"
Cohesion: 0.09
Nodes (30): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+22 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 22 - "list"
Cohesion: 0.06
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 25 - ".plan"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.16
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 30 - ".view"
Cohesion: 0.14
Nodes (4): PlayerStats, View, Retinues, RetinuesTest

### Community 32 - "CityLayout"
Cohesion: 0.10
Nodes (5): CityLayout, District, Site, WorldSettings, CityLayoutTest

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.06
Nodes (19): BlockPacker, Placed, Rect, Size, CityPlan, Kind, BARRACKS, CHURCH (+11 more)

### Community 36 - ".of"
Cohesion: 0.22
Nodes (4): FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 38 - "RecruitsBattle"
Cohesion: 0.03
Nodes (27): Card, Enlisted, HornCall, ATTACKING, MOVING, RALLYING, RETREATING, RecruitsBattle (+19 more)

### Community 41 - "arraylist"
Cohesion: 0.06
Nodes (15): Base map generated from seed, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Demographics (population by culture and race), Faction Info screen, Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, World map layers (+7 more)

### Community 42 - "Feud"
Cohesion: 0.13
Nodes (4): Feud, Lord, Feuds, Front

### Community 43 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.09
Nodes (6): Cited Findings, Goal, MusterBars, PoliticsSync, War, Wars

### Community 45 - "CityGridBuilder.java"
Cohesion: 0.06
Nodes (4): ConquestContent, ConquestEntities, ConquestWorldgen, FarmStructure

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (13): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+5 more)

### Community 48 - "BattleType"
Cohesion: 0.10
Nodes (7): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome, TickTimes

### Community 50 - "Cited Findings"
Cohesion: 0.13
Nodes (7): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Inferences, Takeaway, DistantArmies, Throw, ThrownFigures

### Community 51 - "Capture"
Cohesion: 0.07
Nodes (15): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD, Capture (+7 more)

### Community 52 - "Faction"
Cohesion: 0.07
Nodes (13): RecruitsBridge, Faction, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD (+5 more)

### Community 53 - "Centre"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 57 - "SeatCities"
Cohesion: 0.13
Nodes (4): Grid, SeatCities, SeatCitiesTest, World

### Community 58 - "Faction Map View"
Cohesion: 0.16
Nodes (3): Cited Findings, FactionMapScreen, MapView

### Community 59 - ".step"
Cohesion: 0.12
Nodes (5): FigureMotion, Ground, Match, State, FigureMotionTest

### Community 61 - ".render"
Cohesion: 0.08
Nodes (8): CharacterScreen, Retainer, Spot, ClientPeerage, Banner, Portraits, Honour, Honours

### Community 63 - "TitleMapTest"
Cohesion: 0.13
Nodes (6): Gaps, Gaps, TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.12
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 66 - ".step"
Cohesion: 0.13
Nodes (7): Cited Findings, Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 67 - ".choose"
Cohesion: 0.17
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 69 - "RafasConquest.java"
Cohesion: 0.04
Nodes (3): Battle map developer web page, Morale, rout and rally, BattleMap

### Community 71 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.17
Nodes (5): Land, Result, SiegeSurvey, Wall, SiegeSurveyTest

### Community 72 - "DistantShips"
Cohesion: 0.13
Nodes (7): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Gaps, Inferences, Takeaway, DistantShips, Far, SableSeen

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.05
Nodes (34): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+26 more)

### Community 76 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 77 - ".board"
Cohesion: 0.16
Nodes (5): DataFiles, Dialogue, Kind, Quests, Target

### Community 78 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (8): Saved, CommandTableBlock, GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, Builder, MarkerBlock

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 85 - "City"
Cohesion: 0.08
Nodes (17): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+9 more)

### Community 86 - "Calibration"
Cohesion: 0.19
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

### Community 90 - "DialogueScreen"
Cohesion: 0.11
Nodes (3): DialogueScreen, BoardView, TalkView

### Community 91 - "CityLayout"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 92 - "Politics"
Cohesion: 0.08
Nodes (7): Army, Event, Muster, CEREMONIAL, OFFENSIVE, Politics, PoliticsData

### Community 95 - ".generate"
Cohesion: 0.09
Nodes (5): ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry

### Community 96 - "net.minecraft.core.Vec3i"
Cohesion: 0.07
Nodes (5): StylePiece, CityPlacement, CountryPlacement, DojoPlacement, FarmPlacement

### Community 98 - "DistantOcclusion"
Cohesion: 0.13
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 99 - "FactionInfoScreen"
Cohesion: 0.14
Nodes (3): FactionInfoScreen, Line, StructureLink

### Community 102 - "Quest"
Cohesion: 0.14
Nodes (10): Taken, Ladder, MILITARY, NOBLE, NONE, Quest, Type, CLEAR (+2 more)

### Community 103 - "FigureBatches"
Cohesion: 0.09
Nodes (10): Batch, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE (+2 more)

### Community 105 - ".get"
Cohesion: 0.21
Nodes (4): Boards, Entry, View, QuestProgress

### Community 106 - "MouthLayer.java"
Cohesion: 0.09
Nodes (4): BackBannerLayer, GateRenderer, MarkerRenderer, MouthLayer

### Community 107 - "ConquestNetwork"
Cohesion: 0.04
Nodes (26): Bark, BoardAction, CameraAt, ChargeHits, CityClose, CityView, ConquestNetwork, FactionInfoReply (+18 more)

### Community 109 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 110 - "Gate"
Cohesion: 0.15
Nodes (3): Face, Gate, GateState

### Community 111 - "City"
Cohesion: 0.08
Nodes (13): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+5 more)

### Community 112 - "BattleShips"
Cohesion: 0.16
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips, Ride

### Community 113 - "WaterSwell"
Cohesion: 0.07
Nodes (12): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Sea, WaterSwell, Depths (+4 more)

### Community 114 - "Arm"
Cohesion: 0.13
Nodes (10): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+2 more)

### Community 115 - "ConquestSavedData"
Cohesion: 0.07
Nodes (6): Cited Findings, ConquestSavedData, MapPreview, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 116 - "Kind"
Cohesion: 0.13
Nodes (12): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY (+4 more)

### Community 117 - "Cited Findings"
Cohesion: 0.16
Nodes (11): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+3 more)

### Community 118 - "uuid"
Cohesion: 0.07
Nodes (3): ConquestConfig, PoliticalMap, FactionInfo

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.15
Nodes (3): GrandStrategyScreen, Spot, GrandStrategyDateTest

### Community 121 - "MapLayer"
Cohesion: 0.18
Nodes (5): LegendEntry, MapLayer, Symbol, TerritoryLayer, UnitLayer

### Community 122 - "CapitalLayout"
Cohesion: 0.19
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 124 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 125 - "Ranks"
Cohesion: 0.17
Nodes (3): Ranks, Offer, Recruitment

### Community 126 - "Plan"
Cohesion: 0.21
Nodes (6): Engine, LADDERS, RAMS, TOWERS, Plan, WallTower

### Community 129 - "Kit"
Cohesion: 0.11
Nodes (19): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+11 more)

### Community 132 - "BattleSounds"
Cohesion: 0.23
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (23): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, Cited Findings, Cited Findings, Gaps (+15 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings (+28 more)

### Community 142 - "CourtSet"
Cohesion: 0.25
Nodes (3): CourtSet, Seat, Style

### Community 146 - "GoldChurch.java"
Cohesion: 0.23
Nodes (3): PlacedWall, GoldChurch, Layout

### Community 148 - "Rank"
Cohesion: 0.09
Nodes (15): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Rank, CAPTAIN (+7 more)

### Community 149 - "RoadNetwork"
Cohesion: 0.09
Nodes (6): Cited Findings, RoadFeature, Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 152 - "Job"
Cohesion: 0.20
Nodes (6): Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY, SHIELDMAN

### Community 156 - ".plan"
Cohesion: 0.11
Nodes (9): Demographics, Share, Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner (+1 more)

### Community 163 - "Machine"
Cohesion: 0.10
Nodes (12): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+4 more)

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.19
Nodes (3): Armoury, Outfit, Style

### Community 174 - ".drawEmpire"
Cohesion: 0.17
Nodes (4): Hit, LabelLayer, MapContext, LabelShape

### Community 186 - "SoldierFigure"
Cohesion: 0.08
Nodes (26): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+18 more)

### Community 205 - "Morale"
Cohesion: 0.17
Nodes (8): Morale, State, RALLIED, ROUTED, SHATTERED, STEADY, UnitInfo, UnitsState

### Community 218 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.07
Nodes (13): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway, Cited Findings, Camera, CameraView (+5 more)

### Community 219 - ".view"
Cohesion: 0.18
Nodes (6): Merit, Peer, Option, Talk, View, DialogueTest

### Community 221 - "Siege"
Cohesion: 0.16
Nodes (6): GateAction, Region, IN, OUT, WALL, Siege

### Community 224 - "RecruitFigures.java"
Cohesion: 0.05
Nodes (5): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), DistantDepth, Figure

### Community 225 - ".hash"
Cohesion: 0.12
Nodes (6): ChunkCoord, FarmLayout, NobleAnnex, OriginRoller, Roll, SeedHash

### Community 231 - "Homeland"
Cohesion: 0.09
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 235 - ".order"
Cohesion: 0.25
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.08
Nodes (7): Cited Findings, Cited Findings, BattleSpotRing, DistantArmyRenderer, Hull, Rects, Placed

### Community 249 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.06
Nodes (8): BaseMapPart, BattleSpot, GrandStrategyView, PeerageView, PoliticsView, StatsView, Volleys, WorldStretch

### Community 252 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 254 - "OrganizationScreen"
Cohesion: 0.13
Nodes (5): OrganizationScreen, Organization, Organizations, Sanctum, OrganizationsTest

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
Cohesion: 0.09
Nodes (11): GateDoors, Ram, SableLink, Door, GateBlockEntity, Swing, CLOSED, CLOSING (+3 more)

### Community 274 - "Lands"
Cohesion: 0.08
Nodes (9): Cited Findings, Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Lands, Stronghold (+1 more)

### Community 275 - "CourtView"
Cohesion: 0.09
Nodes (4): Camera, CourtView, Figure, NationScreen

### Community 277 - "Realm"
Cohesion: 0.10
Nodes (6): Hook, Realm, Succession, Taking, RealmPlannerTest, World

### Community 283 - ".view"
Cohesion: 0.24
Nodes (7): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, GrandStrategy, View

### Community 288 - "Peerage"
Cohesion: 0.10
Nodes (7): Names, NobleTitle, COUNT, KNIGHT, NONE, Peerage, PeerageSync

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "BattlePlan.java"
Cohesion: 0.05
Nodes (33): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Character page, Characters (holders, skills, prowess, traits), Court 3D scene (CourtView), Cultures (seven homelands) (+25 more)

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

### Community 303 - ".postChunk"
Cohesion: 0.09
Nodes (6): Cited Findings, BarracksPeople, CityBanners, CityGuards, ConquestServer, PoiPrecomputer

### Community 306 - ".feudFight"
Cohesion: 0.18
Nodes (5): Clash, Kind, DUEL, SKIRMISH, FeudDuel

### Community 308 - "State"
Cohesion: 0.50
Nodes (4): State, CARRIED, RAISING, UP

### Community 309 - "CountryPiece"
Cohesion: 0.16
Nodes (7): Build, CASTLE, HAMLET, RUIN, TOWER, WELL, CountryPiece

### Community 310 - "ConquestWorldScreen"
Cohesion: 0.20
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 318 - "RecruitsBattle.java"
Cohesion: 0.05
Nodes (8): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), TickWeave experiment, Officers

### Community 322 - ".build"
Cohesion: 0.11
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 326 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.16
Nodes (6): Cited Findings, CityVegetationMixin, ClientChunkWindowMixin, RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 327 - ".named"
Cohesion: 0.20
Nodes (4): Kind, FAVOUR, SECRET, CharacterMarkers

### Community 330 - "9. Memory: client caches, textures and NativeImages that are never freed or grow without bound"
Cohesion: 0.40
Nodes (5): 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Gaps, Inferences, Takeaway

### Community 344 - "ShipKind"
Cohesion: 0.33
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **414 isolated node(s):** `FOOT`, `HORSE`, `ADVANCE`, `CAVALRY_CHARGE`, `CAVALRY_RECALL` (+409 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1350 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **218 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `net.minecraft.world.level.ChunkPos`, `.army`, `.passOnOrders`, `SiegeWorks`, `UnitOrder`, `.enlist`, `PveCommands.java`, `WarCouncil`, `org.jetbrains.annotations.Nullable`, `PveCommands Registration`, `net.minecraft.network.RegistryFriendlyByteBuf`, `net.minecraft.world.item.ItemStack`, `rafasconquest.faction.Faction`, `net.minecraft.server.MinecraftServer`, `.onServerTick`, `.inCity`, `BattleType`, `Override`, `map`, `RecruitsBattle.java`, `RafasConquest.java`, `net.minecraft.world.entity.Entity`, `net.minecraft.server.level.ServerLevel`, `Phase`, `Work`, `Doctrine`, `net.minecraft.server.level.ServerPlayer`, `net.minecraft.core.BlockPos`, `.generate`, `.isOurs`, `net.neoforged.bus.api.SubscribeEvent`, `net.neoforged.bus.api.SubscribeEvent`, `Trait`?**
  _High betweenness centrality (0.078) - this node is a cross-community bridge._
- **What connects `FOOT`, `HORSE`, `ADVANCE` to the rest of the system?**
  _414 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.04175576146353053 - nodes in this community are weakly interconnected._
- **Why does `ConquestNetwork` connect `ConquestNetwork` to `net.minecraft.network.RegistryFriendlyByteBuf`, `net.minecraft.server.level.ServerPlayer`, `BattleSnapshot`, `arraylist`, `Morale`, `net.minecraft.world.level.block.state.BlockState`, `UnitOrder`, `net.minecraft.client.gui.GuiGraphics`, `CityManagerScreen`, `map`, `net.minecraft.network.FriendlyByteBuf`, `DialogueScreen`, `org.jetbrains.annotations.Nullable`, `RecruitsBattle.java`?**
  _High betweenness centrality (0.051) - this node is a cross-community bridge._
- **Should `Townsperson` be split into smaller, more focused modules?**
  _Cohesion score 0.12413793103448276 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._