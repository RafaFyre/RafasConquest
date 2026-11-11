# Graph Report - RafasConquest  (2026-11-11)

## Corpus Check
- 457 files · ~392,205 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 115 file(s) not represented in the graph (top: .nbt 84, .toml 14, (none) 3)

## Summary
- 6408 nodes · 20295 edges · 347 communities (155 shown, 192 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2704 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `5dde4931`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FieldBlock
- org.jetbrains.annotations.Nullable
- net.minecraft.world.level.levelgen.structure.BoundingBox
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsArmies.java
- net.minecraft.server.MinecraftServer
- SiegeWorks
- FieldBlock
- Arm
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- Roads
- BaseMap
- ConquestNetwork
- PoiPrecomputer.java
- BattlePlan
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.screens.Screen
- RafasConquest Commands (guide v0.0.2)
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- HorizontalStretch
- CityManagerScreen
- .plan
- .findGenerationPoint
- .shift
- WarCouncil
- Quests
- RecruitsBattle
- Lands
- CapitalLayout
- PveCommands Registration
- School
- CityPlan
- Land
- .begin
- FormationSnapshot
- net.minecraft.core.Direction
- rafasconquest.faction.Faction
- Poi
- Faction
- ConquestSavedData
- net.minecraft.server.level.ServerPlayer
- Personalities
- SeaLanes
- Characters (holders, skills, prowess, traits)
- .drawEmpire
- .roadStart
- Cited Findings
- Centre
- Homeland
- Row
- Peerage
- City
- Faction Map View
- net.minecraft.network.FriendlyByteBuf
- GameCharacter
- net.minecraft.world.level.block.state.BlockState
- TitleMapTest
- .measure
- StyleLibrary
- .render
- .view
- RafasConquest.java
- .render
- .postChunk
- DistantOcclusion
- CityGridBuilder
- MapLands
- org.spongepowered.asm.mixin.Mixin
- PlaceMusicClient
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- ConquestServer
- net.neoforged.neoforge.network.handling.IPayloadContext
- SeaCalm
- Ownership & Territory
- .choose
- .view
- PoliticsTest
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- OrganizationScreen
- net.minecraft.world.item.ItemStack
- Barracks
- .generate
- Override
- WaterSwell
- FactionInfoScreen
- ClientScreens
- .world
- CityStyles
- WallTemplates
- Realm
- MouthLayer.java
- rafasconquest.battle.FormationSnapshot
- DayClockTest
- .cellMax
- .of
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- MarkerScreen
- ClientModEvents.java
- GateBlockEntity.java
- RoadLayer
- CityLayout
- GrandStrategyScreen
- Terrain
- ClientPolitics
- Cited Findings
- UnitOrder
- Settlement
- .wallSegments
- RoadNetwork
- net.minecraft.world.item.DyeColor
- BattleShips
- MapContext
- RafasConquest (PvE addon for Villager Recruits)
- BlockPos
- Swell
- .build
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- ChunkMapCameraMixin.java
- net.minecraft.core.Vec3i
- net.minecraft.resources.ResourceLocation
- Lands
- .view
- Townsperson
- CLAUDE.md
- .accept
- Kit
- .plan
- CourtView
- net.minecraft.world.level.block.Rotation
- .feudFight
- SableLink
- View
- Small Cluster 170
- ClientEvents
- .hash
- net.minecraft.world.level.biome.Biome
- Duty
- SeatCitiesTest
- .pack
- Organizations
- .onLevelLoad
- Siege
- DistantArmyRenderer
- DistantArmyRenderer.java
- .plan
- .touches
- Cited Findings
- net.neoforged.bus.api.SubscribeEvent
- CityVegetationMixin.java
- SwellTest
- Strategy
- ConquestGameRules
- Recruiter
- Trait
- PoliticsData
- Call
- Ramming
- .enlist
- Profiling run sheet (baseline)
- .syncGates
- Race
- Role
- DistantGeneration
- net.minecraft.core.BlockPos
- Job
- BattleKeepInventory
- Kind
- WarCamp
- Distribution
- 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound
- .begin
- Cities (grid, plots, vanilla village buildings)
- Phase
- Build
- list
- Rank
- UnitMode
- NobleDistrictTest
- GatePlanksBlock.java
- World map (CK3-style political map)
- CameraView
- Kind
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- .candidateChunk
- Type
- 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel)
- 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling
- 5. Water/ocean waves (sea swell, foam, boats on waves)
- Work
- ConquestConfig
- ClientBarks
- Enlisted players
- Kind
- 6. Sodium (and Iris) compatibility
- Kind
- 7. Client tick, stutter and sound
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- Saved data: how big, how often written, is everything re-serialized?
- Networking: what is sent, how big, how often, to whom?
- MapColors

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 269 edges
2. `FieldBlock` - 196 edges
3. `FieldBattle` - 166 edges
4. `Point` - 126 edges
5. `ConquestNetwork` - 119 edges
6. `FieldArmy` - 111 edges
7. `Lands` - 89 edges
8. `Politics` - 79 edges
9. `BaseMap` - 77 edges
10. `BattleScreen` - 70 edges

## Surprising Connections (you probably didn't know these)
- `Conclusion` --references--> `RoadLayer`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Cited Findings` --references--> `RoadLayer`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Takeaway` --references--> `RoadLayer`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `A tick budget and staggered schedules make the fixes stick` --references--> `BaseMap`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/map/BaseMap.java

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

## Communities (347 total, 192 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.05
Nodes (9): Point, BlockGrid, FieldArmy, Event, FieldBattle, Order, Target, Volley (+1 more)

### Community 1 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.04
Nodes (10): Ambushes from woods, Deploying to the land (GroundPlan), ConquestContent, StreetWalks, ConquestEntities, CityDistrictStructure, CitySlopeStructure, CityStructure (+2 more)

### Community 2 - "net.minecraft.world.level.levelgen.structure.BoundingBox"
Cohesion: 0.06
Nodes (10): BarracksPiece, CityRoadPiece, CityTerrainPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece (+2 more)

### Community 3 - ".army"
Cohesion: 0.08
Nodes (3): DoctrineTest, FieldBattleTest, ArmouryTest

### Community 4 - "CityTerrain"
Cohesion: 0.08
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.08
Nodes (4): BattleScreen, Entry, Menu, UnitInfo

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.06
Nodes (9): 10. Overall ranking, and areas checked that look fine, Cited Findings, Gaps, Inferences, Takeaway, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+1 more)

### Community 7 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.08
Nodes (7): Cited Findings, Army, Goal, MusterBars, PoliticsSync, War, Wars

### Community 9 - "FieldBlock"
Cohesion: 0.05
Nodes (12): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN, Landing, Live, Shape (+4 more)

### Community 10 - "Arm"
Cohesion: 0.11
Nodes (11): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, FieldRules, Rates, Side (+3 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "Roads"
Cohesion: 0.09
Nodes (16): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Land (+8 more)

### Community 13 - "BaseMap"
Cohesion: 0.08
Nodes (10): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, BaseMap, Nation, BaseMapStore (+2 more)

### Community 14 - "ConquestNetwork"
Cohesion: 0.04
Nodes (21): Bark, BaseMapPart, BattleSpot, CameraAt, ChargeHits, CityClose, CityView, ConquestNetwork (+13 more)

### Community 16 - "BattlePlan"
Cohesion: 0.07
Nodes (8): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, BattlePlanTest

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.23
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "net.minecraft.client.gui.screens.Screen"
Cohesion: 0.05
Nodes (5): Character page, Court 3D scene (CourtView), Nation page, GateScreen, InventoryStatsButton

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
Cohesion: 0.20
Nodes (5): BattleSite, Part, Water, BattleSite.Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.16
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 29 - "Quests"
Cohesion: 0.11
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 30 - "RecruitsBattle"
Cohesion: 0.04
Nodes (15): Cited Findings, Camera, Card, Enlisted, Figure, Officers, RecruitsBattle, Role (+7 more)

### Community 31 - "Lands"
Cohesion: 0.08
Nodes (12): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Lands, Stronghold, Claim (+4 more)

### Community 32 - "CapitalLayout"
Cohesion: 0.18
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 34 - "School"
Cohesion: 0.14
Nodes (11): School, BLOOD, FIRE, ICE, IRON, LIGHT, LIGHTNING, SHADOW (+3 more)

### Community 36 - "Land"
Cohesion: 0.26
Nodes (3): Heap, Search, ShippingRoutes

### Community 37 - ".begin"
Cohesion: 0.05
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 38 - "FormationSnapshot"
Cohesion: 0.10
Nodes (13): Cited Findings, FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL (+5 more)

### Community 39 - "net.minecraft.core.Direction"
Cohesion: 0.12
Nodes (7): Land, Result, SiegeSurvey, Wall, CityGates, Done, SiegeSurveyTest

### Community 42 - "Poi"
Cohesion: 0.09
Nodes (11): FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, Poi, PoiType (+3 more)

### Community 43 - "Faction"
Cohesion: 0.10
Nodes (6): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, MapPreview, Faction, FactionInfoTest

### Community 44 - "ConquestSavedData"
Cohesion: 0.09
Nodes (4): Cited Findings, ConquestSavedData, EconomyTick, ConquestSavedDataTest

### Community 45 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.16
Nodes (4): Boards, Entry, View, CityManager

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (38): Honour, Honours, Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS (+30 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.10
Nodes (18): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Characters (holders, skills, prowess, traits), Cultures (seven homelands), Envelopment, Epic Knights optional armour (Armoury), Faiths and cults (+10 more)

### Community 50 - ".drawEmpire"
Cohesion: 0.27
Nodes (3): Hit, LabelLayer, LabelShape

### Community 52 - "Cited Findings"
Cohesion: 0.19
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 53 - "Centre"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 54 - "Homeland"
Cohesion: 0.15
Nodes (8): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA

### Community 56 - "Peerage"
Cohesion: 0.09
Nodes (8): Advancement, Merit, Peer, Peerage, Taken, PeerageSync, Quest, QuestProgress

### Community 57 - "City"
Cohesion: 0.10
Nodes (8): Countryside, Plan, City, Grid, SeatCities, CityIndex, CountrysideTest, World

### Community 58 - "Faction Map View"
Cohesion: 0.16
Nodes (3): Cited Findings, FactionMapScreen, MapView

### Community 59 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.07
Nodes (8): BattleSnapshot, FactionInfoReply, PoliticsView, SetSetting, StatsView, TeleportTo, UnitsRequest, UnitsState

### Community 61 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (6): CommandTableBlock, GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, MarkerBlock

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 65 - "StyleLibrary"
Cohesion: 0.10
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - ".render"
Cohesion: 0.09
Nodes (6): CharacterScreen, Retainer, Spot, ClientPeerage, Banner, Portraits

### Community 67 - ".view"
Cohesion: 0.18
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 69 - "RafasConquest.java"
Cohesion: 0.04
Nodes (4): Battle map developer web page, Morale, rout and rally, BattleMap, RafasConquest

### Community 71 - ".postChunk"
Cohesion: 0.15
Nodes (3): Cited Findings, BarracksPeople, CityGuards

### Community 72 - "DistantOcclusion"
Cohesion: 0.13
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 73 - "CityGridBuilder"
Cohesion: 0.08
Nodes (9): CityGridBuilder, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY (+1 more)

### Community 75 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.27
Nodes (3): RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 76 - "PlaceMusicClient"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 77 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.08
Nodes (6): GateDoors, Door, GateBlockEntity, GateGeometry, Hinge, GateGeometryTest

### Community 78 - "ConquestServer"
Cohesion: 0.16
Nodes (3): ConquestServer, StructureLink, PoiPrecomputer

### Community 79 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.13
Nodes (6): BackBanner, Builder, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 81 - "SeaCalm"
Cohesion: 0.24
Nodes (3): Sea, Depths, SeaCalm

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.15
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 84 - ".view"
Cohesion: 0.09
Nodes (13): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, GrandStrategy, View, Names (+5 more)

### Community 86 - "Calibration"
Cohesion: 0.19
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.16
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 89 - "AgeTest.java"
Cohesion: 0.12
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 93 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.11
Nodes (6): WarHorn, BackBanners, Armoury, Outfit, Style, ItemFlags

### Community 96 - "Override"
Cohesion: 0.14
Nodes (4): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Politics.Front

### Community 98 - "WaterSwell"
Cohesion: 0.18
Nodes (6): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, WaterSwell

### Community 102 - "CityStyles"
Cohesion: 0.22
Nodes (3): Building, CityStyles, Houses

### Community 105 - "Realm"
Cohesion: 0.12
Nodes (4): Hook, Realm, Succession, Taking

### Community 106 - "MouthLayer.java"
Cohesion: 0.12
Nodes (4): BackBannerLayer, GateRenderer, MarkerRenderer, MouthLayer

### Community 107 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.31
Nodes (6): Boxes, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 108 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 110 - ".of"
Cohesion: 0.16
Nodes (5): FactionBanner, Layer, FactionBannerNbt, CityBanners, FactionBannerTest

### Community 111 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.11
Nodes (18): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Gaps, Gaps, Gaps, Gaps, Inferences, Inferences (+10 more)

### Community 112 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.20
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 117 - "RoadLayer"
Cohesion: 0.15
Nodes (12): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+4 more)

### Community 118 - "CityLayout"
Cohesion: 0.08
Nodes (6): CityLayout, District, Site, SlopeTile, WorldSettings, CityLayoutTest

### Community 123 - "Cited Findings"
Cohesion: 0.13
Nodes (7): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, BattleSpotRing, SableSeen

### Community 124 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 125 - "Settlement"
Cohesion: 0.12
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 126 - ".wallSegments"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 128 - "RoadNetwork"
Cohesion: 0.09
Nodes (6): Cited Findings, RoadFeature, Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 129 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.16
Nodes (5): DistantShips, Far, BattleKind, Part, ShipHulls

### Community 130 - "BattleShips"
Cohesion: 0.27
Nodes (3): BattleShips, Helm, Ram

### Community 132 - "MapContext"
Cohesion: 0.09
Nodes (10): BaseMapLayer, MapContext, LegendEntry, MapLayer, Symbol, Place, PlaceMenuLayer, PoiIconLayer (+2 more)

### Community 133 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 134 - "BlockPos"
Cohesion: 0.10
Nodes (6): BoardScreen, BoardAction, GateSet, GateView, MarkerSet, MarkerView

### Community 137 - ".build"
Cohesion: 0.12
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.10
Nodes (19): 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, Cited Findings, Cited Findings, Gaps, Gaps (+11 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.12
Nodes (16): 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 8. Lessons from performance mods that a content mod can apply, Cited Findings, Cited Findings, Cited Findings, Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest), Gaps (+8 more)

### Community 141 - "ChunkMapCameraMixin.java"
Cohesion: 0.25
Nodes (5): Cited Findings, Cited Findings, ChunkMapAccess, ChunkMapCameraMixin, TrackedEntityCameraMixin

### Community 142 - "net.minecraft.core.Vec3i"
Cohesion: 0.10
Nodes (4): CityPlacement, CountryPlacement, DojoPlacement, FarmPlacement

### Community 143 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.22
Nodes (3): UnitLayer, RecruiterRenderer, TownspersonRenderer

### Community 146 - "Lands"
Cohesion: 0.12
Nodes (4): Feud, Lord, Feuds, Front

### Community 148 - ".view"
Cohesion: 0.16
Nodes (4): PlayerStats, View, Retinues, RetinuesTest

### Community 149 - "Townsperson"
Cohesion: 0.10
Nodes (5): Walk, DistrictKey, Townsfolk, Townsperson, StreetWalksTest

### Community 155 - "Kit"
Cohesion: 0.15
Nodes (13): Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD, HEAVY_INFANTRY (+5 more)

### Community 156 - ".plan"
Cohesion: 0.11
Nodes (8): Centre, Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 157 - "CourtView"
Cohesion: 0.09
Nodes (6): Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 160 - "net.minecraft.world.level.block.Rotation"
Cohesion: 0.17
Nodes (7): Gold church, Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter, GoldChurch, Layout

### Community 163 - ".feudFight"
Cohesion: 0.18
Nodes (5): Clash, Kind, DUEL, SKIRMISH, FeudDuel

### Community 164 - "SableLink"
Cohesion: 0.19
Nodes (3): Frame, SableLink, SiegeFrames

### Community 167 - "View"
Cohesion: 0.15
Nodes (3): BoardView, GrandStrategyView, TalkView

### Community 178 - ".hash"
Cohesion: 0.09
Nodes (8): Bounds, DistanceScaling, NobleAnnex, OriginRoller, Roll, ProvinceNames, DistanceScalingTest, NobleAnnexTest

### Community 204 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 217 - ".pack"
Cohesion: 0.29
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 218 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 221 - "Siege"
Cohesion: 0.07
Nodes (14): Engine, LADDERS, RAMS, TOWERS, Gate, GateAction, GateState, Plan (+6 more)

### Community 223 - "DistantArmyRenderer"
Cohesion: 0.11
Nodes (5): Arrow, ArrowVolleys, DistantArmyRenderer, Figure, Hull

### Community 224 - "DistantArmyRenderer.java"
Cohesion: 0.07
Nodes (4): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), DistantDepth

### Community 226 - ".plan"
Cohesion: 0.23
Nodes (4): Dojo, Dojos, Ground, DojosTest

### Community 232 - "Cited Findings"
Cohesion: 0.08
Nodes (9): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, DistantArmies, ClientMapCache, Throw (+1 more)

### Community 238 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 249 - "Recruiter"
Cohesion: 0.10
Nodes (4): WorldMapItem, Barks, Ear, Recruiter

### Community 252 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 257 - ".enlist"
Cohesion: 0.11
Nodes (9): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT, GeneralTask (+1 more)

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 262 - ".syncGates"
Cohesion: 0.17
Nodes (5): Swing, CLOSED, CLOSING, OPEN, OPENING

### Community 263 - "Race"
Cohesion: 0.18
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 267 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 277 - "DistantGeneration"
Cohesion: 0.20
Nodes (6): Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Takeaway, Api, DistantGeneration

### Community 278 - "net.minecraft.core.BlockPos"
Cohesion: 0.12
Nodes (3): CharacterMarkers, Placed, StylePiece

### Community 283 - "Job"
Cohesion: 0.22
Nodes (6): Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY, SHIELDMAN

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "WarCamp"
Cohesion: 0.11
Nodes (5): Saved, WarCamp, CourtSet, Seat, Style

### Community 292 - "Distribution"
Cohesion: 0.22
Nodes (6): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, WarPlan

### Community 295 - "9. Memory: client caches, textures and NativeImages that are never freed or grow without bound"
Cohesion: 0.25
Nodes (5): 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Gaps, Inferences, Takeaway

### Community 298 - ".begin"
Cohesion: 0.07
Nodes (7): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome, TickTimes

### Community 300 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.25
Nodes (3): Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, TickWeave experiment

### Community 301 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 302 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 303 - "list"
Cohesion: 0.07
Nodes (8): Countryside (hamlets, villages, watchtowers, castles), Demographics (population by culture and race), Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, Nations by zone, Roads (high, city, town roads; bridges; signposts), Seat cities, Storm wall and war zone

### Community 305 - "Rank"
Cohesion: 0.07
Nodes (16): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Rank, CAPTAIN (+8 more)

### Community 306 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 310 - "World map (CK3-style political map)"
Cohesion: 0.33
Nodes (7): Base map generated from seed, Command Table block, Faction Info screen, World map layers, Province size setting, Provinces (ProvinceMap), World map (CK3-style political map)

### Community 312 - "Kind"
Cohesion: 0.18
Nodes (8): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Plan

### Community 316 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.33
Nodes (4): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender

### Community 319 - "Type"
Cohesion: 0.33
Nodes (4): Type, CLEAR, DELIVER, SCOUT

### Community 320 - "1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel)"
Cohesion: 0.40
Nodes (5): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), Cited Findings, Gaps, Inferences, Takeaway

### Community 321 - "2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling"
Cohesion: 0.40
Nodes (5): 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, Cited Findings, Gaps, Inferences, Takeaway

### Community 322 - "5. Water/ocean waves (sea swell, foam, boats on waves)"
Cohesion: 0.40
Nodes (5): 5. Water/ocean waves (sea swell, foam, boats on waves), Cited Findings, Gaps, Inferences, Takeaway

### Community 323 - "Work"
Cohesion: 0.17
Nodes (5): State, CARRIED, RAISING, UP, Work

### Community 326 - "Enlisted players"
Cohesion: 0.33
Nodes (4): Enlisted players, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Battle roles (frontline, bait, support, flanker, kill squad), The war horn

### Community 327 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 328 - "6. Sodium (and Iris) compatibility"
Cohesion: 0.40
Nodes (5): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway

### Community 332 - "Kind"
Cohesion: 0.50
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 333 - "7. Client tick, stutter and sound"
Cohesion: 0.40
Nodes (5): 7. Client tick, stutter and sound, Cited Findings, Gaps, Inferences, Takeaway

### Community 341 - "Saved data: how big, how often written, is everything re-serialized?"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Saved data: how big, how often written, is everything re-serialized?, Takeaway

### Community 342 - "Networking: what is sent, how big, how often, to whom?"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Networking: what is sent, how big, how often, to whom?, Takeaway

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Roads (high, city, town roads; bridges; signposts)` → `Mustering (offensive and ceremonial)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **352 isolated node(s):** `ARCHERS`, `CAVALRY`, `SHIELDWALL`, `SWORDSMEN`, `ARCHER_MELEE` (+347 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1215 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **192 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `org.jetbrains.annotations.Nullable`, `net.minecraft.world.level.levelgen.structure.BoundingBox`, `.enlist`, `.army`, `SiegeWorks`, `FieldBlock`, `Arm`, `.shift`, `WarCouncil`, `PveCommands Registration`, `WarCamp`, `.feudFight`, `.begin`, `net.minecraft.core.Direction`, `rafasconquest.faction.Faction`, `.begin`, `Phase`, `net.minecraft.server.level.ServerPlayer`, `UnitMode`, `.roadStart`, `net.minecraft.world.level.biome.Biome`, `Row`, `Peerage`, `GameCharacter`, `RafasConquest.java`, `.onServerTick`, `net.minecraft.world.item.ItemStack`, `UnitOrder`, `org.jetbrains.annotations.Nullable`, `Trait`?**
  _High betweenness centrality (0.080) - this node is a cross-community bridge._
- **What connects `ARCHERS`, `CAVALRY`, `SHIELDWALL` to the rest of the system?**
  _352 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.05134099616858238 - nodes in this community are weakly interconnected._
- **Why does `ConquestNetwork` connect `ConquestNetwork` to `BattleScreen`, `BlockPos`, `View`, `RafasConquest.java`, `net.neoforged.neoforge.network.handling.IPayloadContext`, `list`, `net.minecraft.client.gui.screens.Screen`, `org.jetbrains.annotations.Nullable`, `CityManagerScreen`, `net.minecraft.network.FriendlyByteBuf`, `net.minecraft.world.level.block.state.BlockState`?**
  _High betweenness centrality (0.045) - this node is a cross-community bridge._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.04021879021879022 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Roads (high, city, town roads; bridges; signposts)` and `Mustering (offensive and ceremonial)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._