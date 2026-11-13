# Graph Report - RafasConquest  (2026-11-12)

## Corpus Check
- 475 files · ~441,579 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7056 nodes · 22423 edges · 358 communities (147 shown, 211 thin omitted)
- Extraction: 86% EXTRACTED · 14% INFERRED · 0% AMBIGUOUS · INFERRED: 3084 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `f6b1b091`
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
- ShipHulls
- FieldRules
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- Land
- BaseMap
- UnitOrder
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- .block
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.GuiGraphics
- PveCommands.java
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- HorizontalStretch
- CityManagerScreen
- .plan
- .fire
- .shift
- WarCouncil
- RafasConquest.java
- .view
- .findGenerationPoint
- CityLayout
- PveCommands Registration
- School
- CityPlan
- .of
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
- BattleType
- Cited Findings
- Capture
- map
- Centre
- Barracks
- Row
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
- Swell
- net.minecraft.server.level.ServerLevel
- DistantShips
- BattlePlan
- MapLands
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Phase
- .board
- net.minecraft.world.level.block.state.BlockState
- Cited Findings
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
- .holder
- .generate
- net.minecraft.core.Vec3i
- Cited Findings
- FactionInfoScreen
- MapContext
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
- ConquestConfig
- GrandStrategyScreen
- SeaCalm
- CapitalLayout
- .tick
- Duty
- .view
- Gate
- BattleShips
- Body
- BlockPos
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
- .world
- RoadNetwork
- CLAUDE.md
- Job
- .place
- .plan
- BattlePlan
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
- .noble
- .simulate
- net.minecraft.server.level.ServerPlayer
- .view
- Siege
- HarbourCut
- RecruitFigures.java
- .hash
- net.neoforged.bus.api.SubscribeEvent
- ClientPolitics
- Homeland
- net.minecraft.resources.ResourceLocation
- net.neoforged.bus.api.SubscribeEvent
- .order
- Kind
- DistantArmyRenderer
- Task
- .load
- ConquestGameRules
- net.minecraft.network.FriendlyByteBuf
- Trait
- OrganizationScreen
- Call
- Ramming
- Role
- Profiling run sheet (baseline)
- .measure
- .passOnOrders
- BlockGrid
- GateBlockEntity
- BattleKeepInventory.java
- NobleDistrictTest
- Lands
- CourtView
- net.minecraft.nbt.CompoundTag
- Realm
- Chunks
- Peerage
- Kind
- BattlePlan.java
- How Total War calculates battles between units
- .readPlacements
- .send
- .boundsAt
- BattleSnapshot
- UnitMode
- .inCity
- .postChunk
- 7. Client tick, stutter and sound
- Clash
- State
- Build
- ConquestWorldScreen
- GatePlanksBlock
- RecruitsBattle.java
- BoardView
- Kind
- .build
- WorldMapItem.java
- org.spongepowered.asm.mixin.Mixin
- Kind
- ShareLiveTest
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- ClientBarks
- com.talhanation.recruits.entities.AbstractRecruitEntity
- net.minecraft.core.BlockPos
- net.neoforged.fml.common.EventBusSubscriber

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 295 edges
2. `FieldBlock` - 247 edges
3. `FieldBattle` - 212 edges
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
- `Every frame pays for disk listings, per-rectangle flushes and unculled soldiers` --references--> `RoadLayer`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Cited Findings` --references--> `RoadLayer`  [INFERRED]
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

## Communities (358 total, 211 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.04
Nodes (9): The core change: morale becomes a sum, FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock (+1 more)

### Community 1 - "Townsperson"
Cohesion: 0.06
Nodes (8): Walk, Barks, Ear, Recruiter, DistrictKey, Townsfolk, Townsperson, StreetWalksTest

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.09
Nodes (7): BarracksPiece, CityRoadPiece, CountryPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece

### Community 3 - ".army"
Cohesion: 0.07
Nodes (4): Point, DoctrineTest, FieldBattleTest, SiegeTest

### Community 4 - "CityTerrain"
Cohesion: 0.06
Nodes (18): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+10 more)

### Community 5 - "BattleScreen"
Cohesion: 0.08
Nodes (3): BattleScreen, Entry, Menu

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.06
Nodes (9): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+1 more)

### Community 7 - "CityGridBuilder"
Cohesion: 0.09
Nodes (10): CityGridBuilder, Ground, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA (+2 more)

### Community 9 - "ShipHulls"
Cohesion: 0.42
Nodes (3): BattleKind, Part, ShipHulls

### Community 10 - "FieldRules"
Cohesion: 0.18
Nodes (5): FieldRules, Side, FLANK, FRONT, REAR

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "Land"
Cohesion: 0.26
Nodes (3): Heap, Search, ShippingRoutes

### Community 13 - "BaseMap"
Cohesion: 0.14
Nodes (9): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, BaseMap, Nation, BaseMapStore (+1 more)

### Community 14 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 15 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.14
Nodes (3): GateDoors, Ram, SableLink

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.15
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 19 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.03
Nodes (5): Races (Sunborn, Vinekin, Meadowfolk, ...), GateScreen, InventoryStatsButton, PoliticalMap, FactionInfo

### Community 20 - "PveCommands.java"
Cohesion: 0.08
Nodes (31): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+23 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.15
Nodes (6): Facts, Officer, Said, Talk, WarCouncil, WarCouncilPagesTest

### Community 29 - "RafasConquest.java"
Cohesion: 0.03
Nodes (4): ConquestContent, StreetWalks, ConquestEntities, RafasConquest

### Community 32 - "CityLayout"
Cohesion: 0.09
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
Cohesion: 0.03
Nodes (12): Cited Findings, Camera, Card, Enlisted, GeneralTask, HornCall, ATTACKING, MOVING (+4 more)

### Community 39 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.16
Nodes (4): Cited Findings, FeudDuel, MusterBars, Wars

### Community 41 - "Faction"
Cohesion: 0.09
Nodes (5): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction, FactionInfoTest

### Community 42 - "Feud"
Cohesion: 0.12
Nodes (4): Feud, Lord, Feuds, Front

### Community 43 - "War"
Cohesion: 0.08
Nodes (7): Army, Goal, Muster, CEREMONIAL, OFFENSIVE, PoliticsSync, War

### Community 45 - "ConquestEvents.java"
Cohesion: 0.04
Nodes (5): ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (38): Honour, Honours, Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS (+30 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (13): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+5 more)

### Community 48 - "BattleType"
Cohesion: 0.14
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 50 - "Cited Findings"
Cohesion: 0.08
Nodes (10): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, BackBanners, DistantArmies, ClientMapCache (+2 more)

### Community 51 - "Capture"
Cohesion: 0.07
Nodes (15): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD, Capture (+7 more)

### Community 52 - "map"
Cohesion: 0.06
Nodes (13): SeedHash, TerritoryMap, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD (+5 more)

### Community 53 - "Centre"
Cohesion: 0.16
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 55 - "Row"
Cohesion: 0.07
Nodes (3): Event, PoliticsTest, Row

### Community 56 - "CityStyles"
Cohesion: 0.22
Nodes (3): Building, CityStyles, Houses

### Community 57 - "SeatCities"
Cohesion: 0.08
Nodes (9): Countryside, Plan, City, Grid, SeatCities, CountrysideTest, World, SeatCitiesTest (+1 more)

### Community 59 - ".step"
Cohesion: 0.12
Nodes (5): FigureMotion, Ground, Match, State, FigureMotionTest

### Community 61 - ".render"
Cohesion: 0.09
Nodes (6): CharacterScreen, Retainer, Spot, ClientPeerage, Banner, Portraits

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.12
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 66 - ".step"
Cohesion: 0.14
Nodes (7): Cited Findings, Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 67 - ".choose"
Cohesion: 0.15
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 70 - "Swell"
Cohesion: 0.16
Nodes (7): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Ride, Swell

### Community 71 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.16
Nodes (5): Land, Result, SiegeSurvey, Wall, SiegeSurveyTest

### Community 72 - "DistantShips"
Cohesion: 0.19
Nodes (3): DistantShips, Far, SableSeen

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.06
Nodes (30): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+22 more)

### Community 76 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 77 - ".board"
Cohesion: 0.16
Nodes (5): DataFiles, Dialogue, Kind, Quests, Target

### Community 78 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.07
Nodes (7): CommandTableBlock, GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, Builder, MarkerBlock

### Community 79 - "Cited Findings"
Cohesion: 0.12
Nodes (14): Battle code costs 0.2 ms; the multi-second freezes come from blocking, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, Cited Findings, Gaps, Inferences, Takeaway, Cited Findings, Gaps (+6 more)

### Community 82 - "Ownership & Territory"
Cohesion: 0.20
Nodes (5): Candidate, OwnershipResolver, Claim, OwnershipResolverTest, TerritoryMapTest

### Community 85 - "City"
Cohesion: 0.09
Nodes (16): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Land (+8 more)

### Community 86 - "Calibration"
Cohesion: 0.19
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

### Community 91 - "CityLayout"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 94 - ".holder"
Cohesion: 0.10
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 95 - ".generate"
Cohesion: 0.09
Nodes (6): ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 98 - "Cited Findings"
Cohesion: 0.06
Nodes (14): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, Arrow, ArrowVolleys, BattleSpotRing (+6 more)

### Community 99 - "FactionInfoScreen"
Cohesion: 0.16
Nodes (3): FactionInfoScreen, Line, StructureLink

### Community 100 - "MapContext"
Cohesion: 0.15
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 102 - "Quest"
Cohesion: 0.16
Nodes (9): Ladder, MILITARY, NOBLE, NONE, Quest, Type, CLEAR, DELIVER (+1 more)

### Community 103 - "FigureBatches"
Cohesion: 0.11
Nodes (9): Batch, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE, FigureBatches (+1 more)

### Community 105 - ".get"
Cohesion: 0.17
Nodes (5): Advancement, Boards, Entry, View, QuestProgress

### Community 106 - "MouthLayer.java"
Cohesion: 0.11
Nodes (4): BackBannerLayer, GateRenderer, MarkerRenderer, MouthLayer

### Community 107 - "ConquestNetwork"
Cohesion: 0.06
Nodes (15): Bark, CameraAt, ChargeHits, CityClose, ConquestNetwork, GrandStrategyView, OpenBattleScreen, PeerageView (+7 more)

### Community 109 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 111 - "City"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 112 - "BattleShips"
Cohesion: 0.17
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 114 - "Arm"
Cohesion: 0.12
Nodes (10): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+2 more)

### Community 115 - "ConquestSavedData"
Cohesion: 0.07
Nodes (6): Cited Findings, ConquestSavedData, MapPreview, EconomyTick, PoiPrecomputer, ConquestSavedDataTest

### Community 116 - "Kind"
Cohesion: 0.13
Nodes (12): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY (+4 more)

### Community 117 - "Cited Findings"
Cohesion: 0.19
Nodes (10): A tick budget and staggered schedules make the fixes stick, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom?, Gaps, Inferences (+2 more)

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.15
Nodes (3): GrandStrategyScreen, Spot, GrandStrategyDateTest

### Community 121 - "SeaCalm"
Cohesion: 0.22
Nodes (4): Sea, Depths, SeaCalm, SwellTest

### Community 122 - "CapitalLayout"
Cohesion: 0.18
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 124 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 125 - ".view"
Cohesion: 0.09
Nodes (12): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+4 more)

### Community 126 - "Gate"
Cohesion: 0.10
Nodes (8): Engine, LADDERS, RAMS, TOWERS, Gate, GateState, Plan, WallTower

### Community 129 - "Body"
Cohesion: 0.11
Nodes (18): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+10 more)

### Community 130 - "BlockPos"
Cohesion: 0.14
Nodes (5): BoardAction, GateSet, GateView, MarkerSet, MarkerView

### Community 132 - "BattleSounds"
Cohesion: 0.23
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 134 - ".render"
Cohesion: 0.11
Nodes (8): Cited Findings, BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer, TitleLayer, UnitLayer

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (24): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings, Cited Findings (+16 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 6. Sodium (and Iris) compatibility, 8. Lessons from performance mods that a content mod can apply, Cited Findings (+28 more)

### Community 141 - ".onLevelLoad"
Cohesion: 0.22
Nodes (3): Cited Findings, CityIndex, ConquestRuntime

### Community 142 - "CourtSet"
Cohesion: 0.19
Nodes (4): CourtSet, Seat, Style, BackBanner

### Community 149 - "RoadNetwork"
Cohesion: 0.15
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 152 - "Job"
Cohesion: 0.11
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 156 - ".plan"
Cohesion: 0.12
Nodes (7): Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 163 - "Machine"
Cohesion: 0.10
Nodes (12): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+4 more)

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.06
Nodes (26): WarHorn, Armoury, Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL (+18 more)

### Community 186 - "SoldierFigure"
Cohesion: 0.14
Nodes (9): Corners, Boxes, Palette, Shape, SoldierFigure, Stuff, CLOTH, METAL (+1 more)

### Community 205 - "Morale"
Cohesion: 0.18
Nodes (8): Morale, State, RALLIED, ROUTED, SHATTERED, STEADY, UnitInfo, UnitsState

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 217 - ".simulate"
Cohesion: 0.13
Nodes (3): WarPlan, TickTimes, GameCharacter

### Community 218 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.09
Nodes (11): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, CameraView, CityManager, GrandStrategy (+3 more)

### Community 219 - ".view"
Cohesion: 0.12
Nodes (8): Merit, Peer, Peerage, Taken, Option, Talk, View, DialogueTest

### Community 221 - "Siege"
Cohesion: 0.17
Nodes (6): GateAction, Region, IN, OUT, WALL, Siege

### Community 224 - "RecruitFigures.java"
Cohesion: 0.04
Nodes (4): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), DistantDepth

### Community 225 - ".hash"
Cohesion: 0.13
Nodes (5): ChunkCoord, FarmLayout, NobleAnnex, OriginRoller, Roll

### Community 231 - "Homeland"
Cohesion: 0.08
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 235 - ".order"
Cohesion: 0.25
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

### Community 236 - "Kind"
Cohesion: 0.22
Nodes (7): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.06
Nodes (7): Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, Cited Findings, DistantArmyRenderer, Figure, Hull, Rects, Placed

### Community 240 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 249 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.06
Nodes (10): BattleSpot, FactionInfoReply, FigureStrike, HornOrders, PoliticsView, SetSetting, TalkChoice, TeleportTo (+2 more)

### Community 252 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 257 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - ".passOnOrders"
Cohesion: 0.06
Nodes (8): Rates, Landing, Live, Shape, LINE, SQUARE, V, WEDGE

### Community 267 - "GateBlockEntity"
Cohesion: 0.09
Nodes (9): GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING, GateGeometry, Hinge (+1 more)

### Community 274 - "Lands"
Cohesion: 0.06
Nodes (11): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Fort, Lands, Stronghold (+3 more)

### Community 275 - "CourtView"
Cohesion: 0.08
Nodes (7): Camera, CourtView, Figure, NationScreen, Demographics, Share, Organization

### Community 276 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.05
Nodes (4): Door, CityTerrainPiece, FarmPiece, PlaceholderPiece

### Community 277 - "Realm"
Cohesion: 0.11
Nodes (4): Hook, Realm, Succession, Taking

### Community 288 - "Peerage"
Cohesion: 0.10
Nodes (7): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageSync, PeerageTest

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "BattlePlan.java"
Cohesion: 0.04
Nodes (50): Ambushes from woods, Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Character page, Characters (holders, skills, prowess, traits) (+42 more)

### Community 292 - "How Total War calculates battles between units"
Cohesion: 0.11
Nodes (17): 1. Combat is per soldier, not per unit, 1. Flank and rear shock, 2. Does the swing land?, 2. Nearby rout (fright), 3. Army-wide losses, 3. How much damage the hit does, 4. Charges, 4. Shattering (+9 more)

### Community 293 - ".readPlacements"
Cohesion: 0.27
Nodes (3): Offsets, Placement, PlacementOffsetsTest

### Community 294 - ".send"
Cohesion: 0.14
Nodes (6): Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel

### Community 295 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 300 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 303 - ".postChunk"
Cohesion: 0.13
Nodes (4): Cited Findings, BarracksPeople, CityGuards, ConquestServer

### Community 305 - "7. Client tick, stutter and sound"
Cohesion: 0.40
Nodes (5): 7. Client tick, stutter and sound, Cited Findings, Gaps, Inferences, Takeaway

### Community 306 - "Clash"
Cohesion: 0.27
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 308 - "State"
Cohesion: 0.50
Nodes (4): State, CARRIED, RAISING, UP

### Community 309 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 310 - "ConquestWorldScreen"
Cohesion: 0.22
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 318 - "RecruitsBattle.java"
Cohesion: 0.05
Nodes (4): /council <topic>, /pve debug recruits, TickWeave experiment, Officers

### Community 321 - "Kind"
Cohesion: 0.50
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 326 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.16
Nodes (5): ChunkMapCameraMixin, CityVegetationMixin, ClientChunkWindowMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 327 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 344 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.06
Nodes (12): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN, Role, BEARER, GENERAL (+4 more)

### Community 349 - "net.minecraft.core.BlockPos"
Cohesion: 0.14
Nodes (3): PlannedTent, Saved, WarCamp

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **414 isolated node(s):** `ADVANCE`, `CAVALRY_CHARGE`, `CAVALRY_RECALL`, `WALL`, `GENERAL_CHARGES` (+409 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1350 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **211 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `net.minecraft.world.level.ChunkPos`, `.army`, `.passOnOrders`, `UnitOrder`, `.block`, `PveCommands.java`, `WarCouncil`, `PveCommands Registration`, `.sendBackBanner`, `net.minecraft.world.item.ItemStack`, `rafasconquest.faction.Faction`, `net.minecraft.server.MinecraftServer`, `War`, `UnitMode`, `ConquestEvents.java`, `.inCity`, `Override`, `Row`, `RecruitsBattle.java`, `RafasConquest.java`, `net.minecraft.server.level.ServerLevel`, `Phase`, `Work`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `.simulate`, `net.minecraft.server.level.ServerPlayer`, `net.minecraft.core.BlockPos`, `.generate`, `net.neoforged.bus.api.SubscribeEvent`, `net.neoforged.bus.api.SubscribeEvent`, `Trait`?**
  _High betweenness centrality (0.081) - this node is a cross-community bridge._
- **What connects `ADVANCE`, `CAVALRY_CHARGE`, `CAVALRY_RECALL` to the rest of the system?**
  _414 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.04318448883666275 - nodes in this community are weakly interconnected._
- **Why does `ConquestNetwork` connect `ConquestNetwork` to `BlockPos`, `BattleScreen`, `CourtSet`, `net.minecraft.client.gui.GuiGraphics`, `list`, `CityManagerScreen`, `RafasConquest.java`, `.sendBackBanner`, `.send`, `RecruitsBattle`, `BattleSnapshot`, `War`, `map`, `ClientScreens`, `RecruitsBattle.java`, `BoardView`, `Morale`, `net.minecraft.world.level.block.state.BlockState`, `DialogueScreen`, `net.minecraft.network.FriendlyByteBuf`?**
  _High betweenness centrality (0.047) - this node is a cross-community bridge._
- **Should `Townsperson` be split into smaller, more focused modules?**
  _Cohesion score 0.057692307692307696 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._