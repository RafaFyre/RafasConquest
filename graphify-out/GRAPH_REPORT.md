# Graph Report - RafasConquest  (2026-11-11)

## Corpus Check
- 466 files · ~410,079 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 6646 nodes · 21108 edges · 358 communities (146 shown, 212 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2832 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `32840a54`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FieldBlock
- Politics
- net.minecraft.world.level.levelgen.structure.BoundingBox
- .army
- CityTerrain
- BattleScreen
- .onLogout
- SableLink
- SiegeWorks
- ConquestNetwork
- net.minecraft.world.entity.Entity
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- Roads
- BaseMap
- RecruitsBattle
- WarCamp.java
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
- RecruitsFactions.java
- net.minecraft.network.FriendlyByteBuf
- WorldSettings
- PveCommands Registration
- School
- CityPlan
- Land
- Townsperson
- .render
- net.minecraft.core.Direction
- rafasconquest.faction.Faction
- SeaCalm
- Front
- net.minecraft.server.MinecraftServer
- Faction
- ConquestServer
- Personalities
- SeaLanes
- org.spongepowered.asm.mixin.Mixin
- .feudFight
- Site
- Centre
- Barracks
- Row
- .name
- SeatCities
- Faction Map View
- RecruitsBattle
- Cited Findings
- MarkerBlock.java
- TitleMapTest
- .saveFormation
- StyleLibrary
- FormationSnapshot
- Layout
- RafasConquest.java
- rafasconquest.battle.FieldBlock
- ShipHulls
- DistantOcclusion
- net.minecraft.world.level.Level
- MapLands
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- PlaceMusicClient
- GateBlockEntity
- Quests
- BattleType
- Ownership & Territory
- .choose
- .view
- PoliticsTest
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- .stat
- Retinues
- MapLayer
- .generate
- ConquestSavedData
- .cellMax
- FactionInfoScreen
- arraylist
- ConquestWorldgen
- CityStyles
- com.talhanation.recruits.entities.AbstractRecruitEntity
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
- net.minecraft.nbt.CompoundTag
- GrandStrategyScreen
- Terrain
- ClientPolitics
- .column
- UnitOrder
- BlockPos
- CityLayout
- .place
- CityGridBuilder.java
- BattleShips
- .render
- .hash
- Quest
- .roadStart
- Cited Findings
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- ConquestWorldScreen
- CourtSet
- CityGridBuilder
- Homeland
- Job
- Grid
- CLAUDE.md
- Lands
- net.minecraft.world.level.block.state.BlockState
- .plan
- .plus
- How Total War calculates battles between units
- net.minecraft.network.RegistryFriendlyByteBuf
- .world
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- DayClockTest
- MapContext
- BoardScreen
- CountryPiece
- NobleDistrictTest
- CityLayoutTest
- .pack
- GatePlanksBlock.java
- .drawEmpire
- Point
- .moraleOf
- DistantArmyRenderer.java
- Piece
- .plan
- .view
- .touches
- net.neoforged.bus.api.SubscribeEvent
- Ranks
- SetSetting
- DistantArmyRenderer
- net.minecraft.resources.ResourceLocation
- ConquestGameRules
- net.minecraft.core.Vec3i
- Trait
- Call
- Ramming
- ClientEvents
- Profiling run sheet (baseline)
- .measure
- Swell
- HarbourCut
- CourtView
- CourtView
- .boundsAt
- Override
- 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound
- Kind
- WarCamp
- DistantGeneration
- QuestBoardBlock
- ConquestConfig
- BlockGrid
- CountryStructure
- ShipKind
- .postChunk
- .of
- .second
- BattleKeepInventory
- ChunkMapAccess
- net.minecraft.world.level.biome.Biome
- Task
- .onLevelLoad
- rafasconquest.battle.FormationSnapshot
- TalkChoice
- Role
- .named
- .build
- Duty
- Recruiter
- .sendPeerage
- WorldStretch
- CityManager
- ClientBarks
- 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?
- UnitMode
- Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI
- View
- Kind
- ClientModEvents.java
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 283 edges
2. `FieldBattle` - 184 edges
3. `FieldBlock` - 182 edges
4. `FieldArmy` - 128 edges
5. `ConquestNetwork` - 119 edges
6. `Lands` - 89 edges
7. `Point` - 84 edges
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

## Communities (358 total, 212 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.05
Nodes (7): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 1 - "Politics"
Cohesion: 0.09
Nodes (9): Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Muster, CEREMONIAL, OFFENSIVE, Politics (+1 more)

### Community 2 - "net.minecraft.world.level.levelgen.structure.BoundingBox"
Cohesion: 0.07
Nodes (8): BarracksPiece, CityRoadPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece, PlaceholderPiece

### Community 3 - ".army"
Cohesion: 0.08
Nodes (3): DoctrineTest, FieldBattleTest, GrandStrategyDateTest

### Community 4 - "CityTerrain"
Cohesion: 0.11
Nodes (12): CityTerrain, Edge, CARVE, FILL, VALLEY, Heights, Plan, Side (+4 more)

### Community 5 - "BattleScreen"
Cohesion: 0.07
Nodes (4): BattleScreen, Entry, Menu, HornOrders

### Community 6 - ".onLogout"
Cohesion: 0.04
Nodes (11): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+3 more)

### Community 7 - "SableLink"
Cohesion: 0.19
Nodes (3): Frame, SableLink, SiegeFrames

### Community 8 - "SiegeWorks"
Cohesion: 0.12
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 9 - "ConquestNetwork"
Cohesion: 0.08
Nodes (14): Bark, CameraAt, CityClose, CityView, ConquestNetwork, FactionInfoReply, FigureStrike, OpenBattleScreen (+6 more)

### Community 10 - "net.minecraft.world.entity.Entity"
Cohesion: 0.08
Nodes (16): Arm, ARCHERS, CAVALRY, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, SKIRMISHERS, ZWEIHANDLERS (+8 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "Roads"
Cohesion: 0.10
Nodes (15): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+7 more)

### Community 13 - "BaseMap"
Cohesion: 0.07
Nodes (12): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, ClientMapCache, MapPreview, BaseMap (+4 more)

### Community 14 - "RecruitsBattle"
Cohesion: 0.08
Nodes (12): Cited Findings, Camera, CameraView, Card, Enlisted, Shape, LINE, SQUARE (+4 more)

### Community 15 - "WarCamp.java"
Cohesion: 0.04
Nodes (35): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Character page, Characters (holders, skills, prowess, traits), Court 3D scene (CourtView), Cultures (seven homelands) (+27 more)

### Community 16 - "BattlePlan"
Cohesion: 0.06
Nodes (9): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, WarPlan (+1 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.13
Nodes (10): Culture, Marriages, Setter, Wedding, Organization, Organizations, Sanctum, Hold (+2 more)

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
Cohesion: 0.06
Nodes (15): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoliticalMap (+7 more)

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.16
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 29 - "TechTreeScreen"
Cohesion: 0.18
Nodes (4): TechTreeScreen, Tech, TechTree, TechTreeTest

### Community 30 - "RecruitsFactions.java"
Cohesion: 0.07
Nodes (7): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), TickWeave experiment

### Community 31 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.07
Nodes (8): BaseMapPart, BattleSpot, PoliticsView, StatsView, UnitInfo, UnitsRequest, UnitsState, Volleys

### Community 32 - "WorldSettings"
Cohesion: 0.15
Nodes (6): CapitalLayout, Land, Site, WorldSettings, CapitalRelocationTest, LayoutTest

### Community 34 - "School"
Cohesion: 0.14
Nodes (11): School, BLOOD, FIRE, ICE, IRON, LIGHT, LIGHTNING, SHADOW (+3 more)

### Community 36 - "Land"
Cohesion: 0.14
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 37 - "Townsperson"
Cohesion: 0.10
Nodes (5): Walk, DistrictKey, Townsfolk, Townsperson, StreetWalksTest

### Community 38 - ".render"
Cohesion: 0.13
Nodes (4): CharacterScreen, Spot, Banner, Portraits

### Community 39 - "net.minecraft.core.Direction"
Cohesion: 0.13
Nodes (7): Land, Result, SiegeSurvey, Wall, CityGates, Done, SiegeSurveyTest

### Community 41 - "SeaCalm"
Cohesion: 0.28
Nodes (3): Depths, SeaCalm, SwellTest

### Community 42 - "Front"
Cohesion: 0.15
Nodes (3): Lord, Feuds, Front

### Community 43 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.09
Nodes (7): Cited Findings, Army, Goal, MusterBars, PoliticsSync, War, Wars

### Community 44 - "Faction"
Cohesion: 0.10
Nodes (3): RecruitsBridge, Faction, FactionInfoTest

### Community 45 - "ConquestServer"
Cohesion: 0.14
Nodes (3): ConquestServer, StructureLink, PoiPrecomputer

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.14
Nodes (8): Battle code costs 0.2 ms; the multi-second freezes come from blocking, Cited Findings, ChunkMapCameraMixin, CityVegetationMixin, ClientChunkWindowMixin, RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 50 - ".feudFight"
Cohesion: 0.15
Nodes (6): Clash, Feud, Kind, DUEL, SKIRMISH, FeudDuel

### Community 51 - "Site"
Cohesion: 0.10
Nodes (3): District, Site, SlopeTile

### Community 53 - "Centre"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 57 - "SeatCities"
Cohesion: 0.12
Nodes (3): SeatCities, SeatCitiesTest, World

### Community 58 - "Faction Map View"
Cohesion: 0.16
Nodes (3): Cited Findings, FactionMapScreen, MapView

### Community 59 - "RecruitsBattle"
Cohesion: 0.05
Nodes (10): HornCall, ATTACKING, MOVING, RALLYING, RETREATING, Officers, RecruitsBattle, BattleSite.Water (+2 more)

### Community 60 - "Cited Findings"
Cohesion: 0.09
Nodes (10): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, BattleSounds, ChargeSound, DinSound (+2 more)

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 65 - "StyleLibrary"
Cohesion: 0.11
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - "FormationSnapshot"
Cohesion: 0.05
Nodes (25): Cited Findings, FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, HEAVY_CAVALRY (+17 more)

### Community 69 - "RafasConquest.java"
Cohesion: 0.04
Nodes (4): Battle map developer web page, Morale, rout and rally, StreetWalks, RafasConquest

### Community 70 - "rafasconquest.battle.FieldBlock"
Cohesion: 0.07
Nodes (4): Figure, Landing, Live, ChargeHits

### Community 71 - "ShipHulls"
Cohesion: 0.14
Nodes (6): DistantShips, Far, SableSeen, BattleKind, Part, ShipHulls

### Community 72 - "DistantOcclusion"
Cohesion: 0.13
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 73 - "net.minecraft.world.level.Level"
Cohesion: 0.12
Nodes (4): Sea, GrandStrategyBlock, StrategyBoardBlock, WorldMapItem

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.06
Nodes (30): Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps, Gaps, Gaps (+22 more)

### Community 76 - "PlaceMusicClient"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 77 - "GateBlockEntity"
Cohesion: 0.07
Nodes (11): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+3 more)

### Community 78 - "Quests"
Cohesion: 0.13
Nodes (5): DataFiles, Dialogue, Kind, Quests, Target

### Community 79 - "BattleType"
Cohesion: 0.14
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.15
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 84 - ".view"
Cohesion: 0.19
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 86 - "Calibration"
Cohesion: 0.14
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.07
Nodes (21): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+13 more)

### Community 89 - "AgeTest.java"
Cohesion: 0.12
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - ".stat"
Cohesion: 0.09
Nodes (11): ClientPeerage, Names, Honour, Honours, Stat, DIPLOMACY, INTRIGUE, LEARNING (+3 more)

### Community 92 - "Retinues"
Cohesion: 0.15
Nodes (4): PlayerStats, View, Retinues, RetinuesTest

### Community 94 - "MapLayer"
Cohesion: 0.23
Nodes (4): LegendEntry, MapLayer, Symbol, TerritoryLayer

### Community 96 - "ConquestSavedData"
Cohesion: 0.10
Nodes (4): Cited Findings, ConquestSavedData, EconomyTick, ConquestSavedDataTest

### Community 99 - "FactionInfoScreen"
Cohesion: 0.10
Nodes (3): ClientScreens, FactionInfoScreen, Line

### Community 100 - "arraylist"
Cohesion: 0.06
Nodes (14): Base map generated from seed, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Faction Info screen, Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, World map layers, Nations by zone (+6 more)

### Community 103 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.16
Nodes (6): Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER

### Community 105 - "Realm"
Cohesion: 0.11
Nodes (5): Retainer, Hook, Realm, Succession, Taking

### Community 106 - "MouthLayer.java"
Cohesion: 0.09
Nodes (4): BackBannerLayer, GateRenderer, MarkerRenderer, MouthLayer

### Community 107 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.31
Nodes (6): Boxes, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 109 - ".send"
Cohesion: 0.17
Nodes (5): Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel

### Community 110 - "Peerage"
Cohesion: 0.11
Nodes (9): Boards, Entry, View, Merit, Peer, Peerage, Taken, PeerageSync (+1 more)

### Community 111 - ".of"
Cohesion: 0.19
Nodes (4): FactionBanner, Layer, FactionBannerNbt, CityBanners

### Community 113 - "WaterSwell"
Cohesion: 0.18
Nodes (6): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, WaterSwell

### Community 114 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 115 - "NobleTitle"
Cohesion: 0.28
Nodes (4): NobleTitle, COUNT, KNIGHT, NONE

### Community 117 - "RoadLayer"
Cohesion: 0.17
Nodes (11): A tick budget and staggered schedules make the fixes stick, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom?, Gaps (+3 more)

### Community 118 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.05
Nodes (5): Saved, Placed, MarkerBlockEntity, MarkerSpec, CityTerrainPiece

### Community 123 - ".column"
Cohesion: 0.15
Nodes (8): Column, Kind, CLEAR_ABOVE, NONE, PLATE, SLOPE, WALL_BASE, CityTerrainTest

### Community 124 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 125 - "BlockPos"
Cohesion: 0.16
Nodes (4): GateSet, GateView, MarkerSet, MarkerView

### Community 126 - "CityLayout"
Cohesion: 0.15
Nodes (8): CityLayout, Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 128 - ".place"
Cohesion: 0.08
Nodes (7): Cited Findings, CityIndex, RoadFeature, Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 129 - "CityGridBuilder.java"
Cohesion: 0.06
Nodes (5): CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, FarmStructure

### Community 130 - "BattleShips"
Cohesion: 0.27
Nodes (3): BattleShips, Helm, Ram

### Community 133 - ".hash"
Cohesion: 0.12
Nodes (5): NobleAnnex, OriginRoller, Roll, ProvinceNames, NobleAnnexTest

### Community 134 - "Quest"
Cohesion: 0.10
Nodes (11): Advancement, Ladder, MILITARY, NOBLE, NONE, Quest, Type, CLEAR (+3 more)

### Community 136 - ".roadStart"
Cohesion: 0.09
Nodes (10): PlaceMusic, Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL (+2 more)

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.10
Nodes (19): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, Cited Findings, Cited Findings, Gaps, Gaps (+11 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (41): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 6. Sodium (and Iris) compatibility, 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply (+33 more)

### Community 141 - "ConquestWorldScreen"
Cohesion: 0.20
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 142 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 143 - "CityGridBuilder"
Cohesion: 0.07
Nodes (10): CityGridBuilder, Ground, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA (+2 more)

### Community 146 - "Homeland"
Cohesion: 0.10
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 148 - "Job"
Cohesion: 0.06
Nodes (21): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+13 more)

### Community 149 - "Grid"
Cohesion: 0.12
Nodes (13): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+5 more)

### Community 152 - "Lands"
Cohesion: 0.11
Nodes (3): Lands, Stronghold, Claim

### Community 156 - ".plan"
Cohesion: 0.12
Nodes (8): Centre, Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 157 - ".plus"
Cohesion: 0.08
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 160 - "How Total War calculates battles between units"
Cohesion: 0.17
Nodes (11): 1. Combat is per soldier, not per unit, 2. Does the swing land?, 3. How much damage the hit does, 4. Charges, 5. Missiles, 6. Leadership (morale): what actually wins battles, 7. Autoresolve, 8. Older games (Shogun 1, Medieval 1, Rome 1) (+3 more)

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.06
Nodes (26): WarHorn, BackBanners, Armoury, Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY (+18 more)

### Community 174 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 178 - "MapContext"
Cohesion: 0.17
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 204 - "CountryPiece"
Cohesion: 0.16
Nodes (7): Build, CASTLE, HAMLET, RUIN, TOWER, WELL, CountryPiece

### Community 206 - "CityLayoutTest"
Cohesion: 0.18
Nodes (3): ChunkCoord, FarmLayout, CityLayoutTest

### Community 217 - ".pack"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 219 - ".drawEmpire"
Cohesion: 0.29
Nodes (3): Hit, LabelLayer, LabelShape

### Community 221 - "Point"
Cohesion: 0.07
Nodes (15): Point, Engine, LADDERS, RAMS, TOWERS, Gate, GateAction, GateState (+7 more)

### Community 223 - ".moraleOf"
Cohesion: 0.11
Nodes (13): 1. Flank and rear shock, 2. Nearby rout (fright), 3. Army-wide losses, 4. Shattering, Design: adding the four morale shocks (built 2026-11-11, untested in game), Tests (FieldBattleTest), The core change: morale becomes a sum, Morale (+5 more)

### Community 224 - "DistantArmyRenderer.java"
Cohesion: 0.07
Nodes (4): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), DistantDepth

### Community 228 - ".plan"
Cohesion: 0.23
Nodes (4): Dojo, Dojos, Ground, DojosTest

### Community 231 - ".view"
Cohesion: 0.24
Nodes (7): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, GrandStrategy, View

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.06
Nodes (12): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Cited Findings, Gaps, Inferences, Takeaway, Arrow, ArrowVolleys (+4 more)

### Community 238 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.22
Nodes (3): UnitLayer, RecruiterRenderer, TownspersonRenderer

### Community 249 - "net.minecraft.core.Vec3i"
Cohesion: 0.07
Nodes (5): StylePiece, CityPlacement, CountryPlacement, DojoPlacement, FarmPlacement

### Community 252 - "Trait"
Cohesion: 0.22
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

### Community 274 - "CourtView"
Cohesion: 0.12
Nodes (4): Camera, CourtView, Figure, OrganizationScreen

### Community 275 - "CourtView"
Cohesion: 0.14
Nodes (3): NationScreen, Demographics, Share

### Community 277 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 283 - "Override"
Cohesion: 0.13
Nodes (3): Chunks, SettingsScreen, Politics.Front

### Community 288 - "9. Memory: client caches, textures and NativeImages that are never freed or grow without bound"
Cohesion: 0.25
Nodes (5): 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Gaps, Inferences, Takeaway

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 292 - "DistantGeneration"
Cohesion: 0.32
Nodes (3): Inferences, Api, DistantGeneration

### Community 302 - "ShipKind"
Cohesion: 0.33
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 303 - ".postChunk"
Cohesion: 0.13
Nodes (3): Cited Findings, BarracksPeople, CityGuards

### Community 311 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 320 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 322 - ".build"
Cohesion: 0.17
Nodes (3): Frame, Harbour, HarbourCutTest

### Community 323 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 324 - "Recruiter"
Cohesion: 0.12
Nodes (3): Barks, Ear, Recruiter

### Community 335 - "5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?"
Cohesion: 0.50
Nodes (4): 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, Gaps, Inferences, Takeaway

### Community 344 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 345 - "Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI"
Cohesion: 0.29
Nodes (5): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Gaps, Inferences, Takeaway

### Community 351 - "View"
Cohesion: 0.15
Nodes (3): BoardView, GrandStrategyView, TalkView

### Community 352 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

## Ambiguous Edges - Review These
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **371 isolated node(s):** `SOLDIER`, `LEADER`, `GENERAL`, `BEARER`, `GUARD` (+366 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1271 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **212 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `net.minecraft.world.level.levelgen.structure.BoundingBox`, `.army`, `SiegeWorks`, `Cited Findings`, `RecruitsBattle`, `PveCommands.java`, `WarCouncil`, `PveCommands Registration`, `WarCamp`, `net.minecraft.world.item.ItemStack`, `rafasconquest.faction.Faction`, `net.minecraft.core.Direction`, `.postChunk`, `.of`, `.second`, `.feudFight`, `org.jetbrains.annotations.Nullable`, `net.minecraft.server.level.ServerLevel`, `.saveFormation`, `RafasConquest.java`, `rafasconquest.battle.FieldBlock`, `ConquestEvents.java`, `.onServerTick`, `Calibration`, `UnitMode`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `Phase`, `UnitOrder`?**
  _High betweenness centrality (0.076) - this node is a cross-community bridge._
- **What connects `SOLDIER`, `LEADER`, `GENERAL` to the rest of the system?**
  _371 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.05194358445132439 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `.place`, `Politics`, `Roads`, `CourtView`, `net.minecraft.client.gui.screens.Screen`, `CourtView`, `Grid`, `org.jetbrains.annotations.Nullable`, `.plan`, `WarCouncil`, `.render`, `net.minecraft.world.item.ItemStack`, `net.minecraft.server.MinecraftServer`, `ConquestServer`, `.postChunk`, `MapContext`, `org.jetbrains.annotations.Nullable`, `.onLevelLoad`, `TitleMapTest`, `.generate`, `arraylist`, `.plan`, `Realm`, `RoadLayer`, `GrandStrategyScreen`, `Terrain`, `ClientPolitics`?**
  _High betweenness centrality (0.045) - this node is a cross-community bridge._
- **Should `Politics` be split into smaller, more focused modules?**
  _Cohesion score 0.0851063829787234 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._