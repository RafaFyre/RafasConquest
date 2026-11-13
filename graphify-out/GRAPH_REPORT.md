# Graph Report - RafasConquest  (2026-11-12)

## Corpus Check
- 508 files · ~454,201 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7285 nodes · 23247 edges · 355 communities (150 shown, 205 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3136 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `193c65cb`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FieldBlock
- net.minecraft.world.level.Level
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsLink
- CityGridBuilder
- Recruiter
- Politics
- ConquestServer
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- RecruitsBattle
- BaseMap
- UnitOrder
- PoliticsTest
- .begin
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.GuiGraphics
- PveCommands.java
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- RecruitsBattle.java
- HorizontalStretch
- CityManagerScreen
- .plan
- org.spongepowered.asm.mixin.Mixin
- .shift
- WarCouncil
- RafasConquest.java
- .view
- net.minecraft.network.FriendlyByteBuf
- .findGenerationPoint
- PveCommands Registration
- School
- CityPlan
- .of
- .send
- Quest
- net.minecraft.server.MinecraftServer
- rafasconquest.faction.Faction
- Faction
- Lands
- Lands
- net.minecraft.nbt.CompoundTag
- optional
- Personalities
- SeaLanes
- Outcome
- rafasconquest.battle.FormationSnapshot
- Capture
- PoiPrecomputer.java
- Centre
- Barracks
- Wind.java
- CityStyles
- City
- Faction Map View
- .step
- ClientScreens
- Peerage
- TitleMapTest
- Bodies
- StyleLibrary
- .step
- .choose
- RafasConquest.java
- Row
- net.minecraft.server.level.ServerLevel
- DistantShips
- BattlePlan
- MapLands
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Phase
- .board
- net.minecraft.world.level.block.state.BlockState
- com.mojang.blaze3d.vertex.PoseStack
- Terrain
- Ownership & Territory
- CityLayout
- Work
- Land
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- CityLayout
- com.mojang.blaze3d.vertex.PoseStack
- .enlist
- CityGridBuilder.java
- net.minecraft.core.Vec3i
- CityVariant
- FactionInfoScreen
- BattleRole
- .deployment
- Cloth
- FigureBatches
- NobleTitle
- net.neoforged.bus.api.SubscribeEvent
- CityTerrain
- Override
- net.minecraft.sounds.SoundEvent
- FigureGear
- City
- BattleShips
- WaterSwell
- Arm
- ConquestSavedData
- ConquestNetwork
- Cited Findings
- .findGenerationPoint
- GrandStrategyScreen
- .column
- WorldSettings
- Kit
- .view
- Override
- BattleShips
- GrappleHookRenderer.java
- MapView
- Roads
- Kind
- .render
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .onLevelLoad
- Homeland
- Townsfolk
- net.minecraft.world.level.block.Rotation
- NobleAnnex
- .place
- CLAUDE.md
- .onLogout
- Organization
- .plan
- CityGuards.java
- Machine
- .world
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- .general
- BoardScreen
- SoldierFigure
- Cities (grid, plots, vanilla village buildings)
- Task
- .noble
- Duty
- .view
- rafasconquest.battle.BattlePlan.Point
- HarbourCut
- RecruitFigures.java
- CityPlacement
- .order
- .render
- World map (CK3-style political map)
- net.minecraft.resources.ResourceLocation
- ConquestEvents
- BattleSounds
- Kind
- DistantArmyRenderer
- View
- DayClockTest
- ConquestGameRules
- Distribution
- GateBlockEntity
- Trait
- OrganizationScreen
- RankKits
- Ramming
- ThrownFigures
- Profiling run sheet (baseline)
- .measure
- .passOnOrders
- ClientModEvents.java
- BlockGrid
- net.neoforged.fml.common.EventBusSubscriber
- ClientPolitics
- War
- CourtView
- Realm
- .tick
- .rightClick
- Kind
- Characters (holders, skills, prowess, traits)
- How Total War calculates battles between units
- SeatCitiesTest
- .boundsAt
- BattleSnapshot
- UnitMode
- Job
- .roadStart
- .postChunk
- .bark
- WarPlan
- RafasConquest (PvE addon for Villager Recruits)
- ConquestWorldScreen
- org.jetbrains.annotations.Nullable
- net.minecraft.core.BlockPos
- assertequals
- UnitsState
- org.spongepowered.asm.mixin.Mixin
- Kind
- ShareLiveTest
- ClientBarks
- Cited Findings
- HornCall
- WarCamp
- ShipKind
- Corners

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 296 edges
2. `FieldBlock` - 256 edges
3. `FieldBattle` - 222 edges
4. `FieldArmy` - 127 edges
5. `ConquestNetwork` - 119 edges
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
- `Inferences` --references--> `RoadLayer`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Takeaway` --references--> `RoadLayer`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/RoadLayer.java
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

## Communities (355 total, 205 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.04
Nodes (8): The core change: morale becomes a sum, FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 1 - "net.minecraft.world.level.Level"
Cohesion: 0.07
Nodes (6): GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, WorldMapItem, MarkerBlock

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.06
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - ".army"
Cohesion: 0.06
Nodes (5): Point, DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, SiegeTest

### Community 4 - "CityTerrain"
Cohesion: 0.13
Nodes (10): Edge, CARVE, FILL, Heights, Plan, Side, EAST, NORTH (+2 more)

### Community 6 - "DistantHorizonsLink"
Cohesion: 0.17
Nodes (3): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink

### Community 7 - "CityGridBuilder"
Cohesion: 0.13
Nodes (4): CityGridBuilder, Ground, PlacedWall, Platform

### Community 8 - "Recruiter"
Cohesion: 0.07
Nodes (4): ConquestContent, GrappleHooks, ConquestEntities, Recruiter

### Community 9 - "Politics"
Cohesion: 0.12
Nodes (7): Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Event, Front, Politics

### Community 10 - "ConquestServer"
Cohesion: 0.14
Nodes (3): ConquestServer, StructureLink, PoiPrecomputer

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "RecruitsBattle"
Cohesion: 0.03
Nodes (16): Cited Findings, Card, Enlisted, Figure, GeneralTask, Live, RecruitsBattle, Role (+8 more)

### Community 13 - "BaseMap"
Cohesion: 0.06
Nodes (13): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, ClientMapCache, MapPreview, BaseMap (+5 more)

### Community 14 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 16 - ".begin"
Cohesion: 0.10
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.23
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.05
Nodes (3): GateScreen, InventoryStatsButton, Portraits

### Community 20 - "PveCommands.java"
Cohesion: 0.07
Nodes (34): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+26 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 22 - "RecruitsBattle.java"
Cohesion: 0.04
Nodes (11): /council <topic>, Battle phases and days, Enlisted players, Envelopment, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Battle roles (frontline, bait, support, flanker, kill squad), War camps, War council (+3 more)

### Community 25 - ".plan"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.15
Nodes (6): Cited Findings, CityVegetationMixin, ClientChunkWindowMixin, RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 27 - ".shift"
Cohesion: 0.20
Nodes (5): BattleSite, Part, Water, BattleSite.Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.15
Nodes (6): Facts, Officer, Said, Talk, WarCouncil, WarCouncilPagesTest

### Community 31 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.05
Nodes (12): BaseMapPart, BattleSpot, FigureStrike, PeerageView, PoliticsView, SetSetting, StatsView, TalkChoice (+4 more)

### Community 34 - "School"
Cohesion: 0.06
Nodes (17): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+9 more)

### Community 35 - "CityPlan"
Cohesion: 0.15
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 36 - ".of"
Cohesion: 0.16
Nodes (4): FactionBanner, Layer, FactionBannerNbt, CityBanners

### Community 37 - ".send"
Cohesion: 0.10
Nodes (7): BackBanner, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel

### Community 38 - "Quest"
Cohesion: 0.14
Nodes (9): Boards, Entry, View, Taken, Quest, Type, CLEAR, DELIVER (+1 more)

### Community 39 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.13
Nodes (4): Cited Findings, MusterBars, PoliticsData, Wars

### Community 41 - "Faction"
Cohesion: 0.11
Nodes (4): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction

### Community 42 - "Lands"
Cohesion: 0.10
Nodes (8): Clash, Feud, Kind, DUEL, SKIRMISH, Lord, FeudDuel, Feuds

### Community 43 - "Lands"
Cohesion: 0.10
Nodes (4): Cited Findings, Lands, Stronghold, Claim

### Community 44 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.05
Nodes (10): BattleBannerBlockEntity, MarkerBlockEntity, MarkerSpec, CityTerrainPiece, Build, CASTLE, HAMLET, RUIN (+2 more)

### Community 45 - "optional"
Cohesion: 0.06
Nodes (6): CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (13): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+5 more)

### Community 50 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.16
Nodes (6): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, DistantArmies

### Community 51 - "Capture"
Cohesion: 0.10
Nodes (8): Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures, RecruitFiguresTest

### Community 52 - "PoiPrecomputer.java"
Cohesion: 0.07
Nodes (13): TerritoryMap, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoliticalMap (+5 more)

### Community 53 - "Centre"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 57 - "City"
Cohesion: 0.10
Nodes (7): Countryside, Plan, City, Grid, SeatCities, CountrysideTest, World

### Community 59 - ".step"
Cohesion: 0.14
Nodes (5): FigureMotion, Match, Placed, State, FigureMotionTest

### Community 61 - "Peerage"
Cohesion: 0.11
Nodes (6): Advancement, Merit, Peer, Peerage, PeerageSync, QuestProgress

### Community 63 - "TitleMapTest"
Cohesion: 0.17
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.11
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - ".step"
Cohesion: 0.14
Nodes (7): Cited Findings, Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 67 - ".choose"
Cohesion: 0.17
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 71 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.15
Nodes (5): Land, Result, SiegeSurvey, Wall, SiegeSurveyTest

### Community 72 - "DistantShips"
Cohesion: 0.14
Nodes (6): DistantShips, Far, SableSeen, BattleKind, Part, ShipHulls

### Community 74 - "MapLands"
Cohesion: 0.10
Nodes (5): Gaps, Inferences, Takeaway, Where are full scans, nested loops, repeated lookups and per-tick allocations?, MapLands

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.05
Nodes (35): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps (+27 more)

### Community 76 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 77 - ".board"
Cohesion: 0.11
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 78 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.08
Nodes (5): Saved, BattleBannerBlock, BattleWallBannerBlock, CommandTableBlock, Builder

### Community 79 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.07
Nodes (7): BackBannerLayer, BattleBannerRenderer, CapeCurio, ClothItemRenderer, GateRenderer, MarkerRenderer, MouthLayer

### Community 82 - "Ownership & Territory"
Cohesion: 0.20
Nodes (5): Candidate, OwnershipResolver, Claim, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "CityLayout"
Cohesion: 0.10
Nodes (3): District, Site, CityLayoutTest

### Community 84 - "Work"
Cohesion: 0.10
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 85 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 86 - "Calibration"
Cohesion: 0.19
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.10
Nodes (16): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR, Strategy (+8 more)

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - "CityLayout"
Cohesion: 0.14
Nodes (8): CityLayout, Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 92 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.15
Nodes (7): BannerArt, Carried, ClothRenderer, Fabric, Kind, Layer, Live

### Community 95 - "CityGridBuilder.java"
Cohesion: 0.07
Nodes (3): BiomePalette, Entry, SettlementBiomes

### Community 98 - "CityVariant"
Cohesion: 0.16
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 100 - "BattleRole"
Cohesion: 0.25
Nodes (6): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT

### Community 102 - "Cloth"
Cohesion: 0.08
Nodes (6): GatePlanksBlock, Cloth, Solid, ClothTest, OffTheBackTest, WoolTest

### Community 103 - "FigureBatches"
Cohesion: 0.10
Nodes (9): Batch, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE, FigureBatches (+1 more)

### Community 105 - "NobleTitle"
Cohesion: 0.14
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 109 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.15
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 110 - "FigureGear"
Cohesion: 0.16
Nodes (7): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 111 - "City"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 112 - "BattleShips"
Cohesion: 0.15
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips, Ride

### Community 113 - "WaterSwell"
Cohesion: 0.06
Nodes (13): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Sea, Ground, WaterSwell (+5 more)

### Community 114 - "Arm"
Cohesion: 0.07
Nodes (22): 1. Flank and rear shock, 2. Nearby rout (fright), 3. Army-wide losses, 4. Shattering, Design: adding the four morale shocks (built 2026-11-11, untested in game), Tests (FieldBattleTest), Arm, ARCHERS (+14 more)

### Community 115 - "ConquestSavedData"
Cohesion: 0.07
Nodes (6): Cited Findings, ConquestSavedData, Poi, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 116 - "ConquestNetwork"
Cohesion: 0.07
Nodes (12): BoardAction, CameraAt, CityView, ConquestNetwork, FactionInfoReply, GateSet, GateView, MarkerSet (+4 more)

### Community 117 - "Cited Findings"
Cohesion: 0.11
Nodes (12): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+4 more)

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.14
Nodes (4): Inferences, GrandStrategyScreen, Spot, GrandStrategyDateTest

### Community 121 - ".column"
Cohesion: 0.13
Nodes (7): Column, Kind, NONE, PLATE, SLOPE, CityTerrainTest, NobleAnnexTest

### Community 122 - "WorldSettings"
Cohesion: 0.08
Nodes (12): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, ProvinceNames (+4 more)

### Community 124 - "Kit"
Cohesion: 0.11
Nodes (19): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+11 more)

### Community 125 - ".view"
Cohesion: 0.07
Nodes (14): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+6 more)

### Community 128 - "BattleShips"
Cohesion: 0.23
Nodes (3): BattleShips, Helm, Ram

### Community 129 - "GrappleHookRenderer.java"
Cohesion: 0.11
Nodes (4): GrappleHookRenderer, RecruiterRenderer, TownspersonRenderer, GrappleHook

### Community 130 - "MapView"
Cohesion: 0.08
Nodes (15): Cited Findings, BaseMapLayer, Hit, LabelLayer, MapContext, LegendEntry, MapLayer, Symbol (+7 more)

### Community 132 - "Roads"
Cohesion: 0.11
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 133 - "Kind"
Cohesion: 0.13
Nodes (12): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY (+4 more)

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.07
Nodes (28): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings (+20 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (40): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 6. Sodium (and Iris) compatibility, 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply (+32 more)

### Community 142 - "Homeland"
Cohesion: 0.08
Nodes (19): CourtSet, Seat, Style, Homeland, DESERT, JUNGLE, PLAINS, SAVANNA (+11 more)

### Community 143 - "Townsfolk"
Cohesion: 0.22
Nodes (3): DistrictKey, Townsfolk, StreetWalksTest

### Community 149 - ".place"
Cohesion: 0.08
Nodes (7): Cited Findings, CityIndex, RoadFeature, Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 155 - "Organization"
Cohesion: 0.21
Nodes (4): Organization, Organizations, Sanctum, OrganizationsTest

### Community 156 - ".plan"
Cohesion: 0.13
Nodes (7): Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 163 - "Machine"
Cohesion: 0.06
Nodes (21): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Machine, ARCBALLISTA, BATTERING_RAM (+13 more)

### Community 164 - ".world"
Cohesion: 0.16
Nodes (4): Demographics, Share, RealmPlannerTest, World

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.09
Nodes (7): WarHorn, BackBanners, BattleStandardItem, Armoury, Outfit, Style, ItemFlags

### Community 174 - ".general"
Cohesion: 0.18
Nodes (9): GameCharacter, Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN (+1 more)

### Community 186 - "SoldierFigure"
Cohesion: 0.09
Nodes (26): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+18 more)

### Community 204 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.18
Nodes (5): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), TickWeave experiment

### Community 205 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 217 - "Duty"
Cohesion: 0.11
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 219 - ".view"
Cohesion: 0.22
Nodes (4): Option, Talk, View, DialogueTest

### Community 221 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.05
Nodes (17): Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate, GateAction (+9 more)

### Community 224 - "RecruitFigures.java"
Cohesion: 0.03
Nodes (5): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), Figure, Banner

### Community 226 - ".order"
Cohesion: 0.25
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

### Community 228 - ".render"
Cohesion: 0.12
Nodes (5): CharacterScreen, Spot, ClientPeerage, Honour, Honours

### Community 231 - "World map (CK3-style political map)"
Cohesion: 0.33
Nodes (7): Base map generated from seed, Command Table block, Faction Info screen, World map layers, Province size setting, Provinces (ProvinceMap), World map (CK3-style political map)

### Community 235 - "BattleSounds"
Cohesion: 0.25
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 236 - "Kind"
Cohesion: 0.13
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.08
Nodes (5): Cited Findings, DistantDepth, DistantArmyRenderer, Rects, ShipSprite

### Community 240 - "View"
Cohesion: 0.09
Nodes (9): Bark, BoardView, ChargeHits, CityClose, GrandStrategyView, OpenBattleScreen, Place, SeaSwell (+1 more)

### Community 242 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 248 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 249 - "GateBlockEntity"
Cohesion: 0.07
Nodes (10): GateDoors, SableLink, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN (+2 more)

### Community 252 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 256 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 262 - ".measure"
Cohesion: 0.16
Nodes (3): ConquestConfig, CityStats, Totals

### Community 263 - ".passOnOrders"
Cohesion: 0.08
Nodes (6): Landing, Shape, LINE, SQUARE, V, WEDGE

### Community 274 - "War"
Cohesion: 0.09
Nodes (7): Army, Goal, Muster, CEREMONIAL, OFFENSIVE, PoliticsSync, War

### Community 275 - "CourtView"
Cohesion: 0.08
Nodes (5): Retainer, Camera, CourtView, Figure, NationScreen

### Community 277 - "Realm"
Cohesion: 0.11
Nodes (4): Hook, Realm, Succession, Taking

### Community 288 - ".rightClick"
Cohesion: 0.22
Nodes (3): Entry, Menu, HornOrders

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.08
Nodes (23): Battles (prototype), Blocks of 20 soldiers, Character page, Characters (holders, skills, prowess, traits), Court 3D scene (CourtView), Cultures (seven homelands), Demographics (population by culture and race), Epic Knights optional armour (Armoury) (+15 more)

### Community 292 - "How Total War calculates battles between units"
Cohesion: 0.09
Nodes (20): 1. Combat is per soldier, not per unit, 2. Does the swing land?, 3. How much damage the hit does, 4. Charges, 5. Missiles, 6. Leadership (morale): what actually wins battles, 7. Autoresolve, 8. Older games (Shogun 1, Medieval 1, Rome 1) (+12 more)

### Community 295 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 300 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 301 - "Job"
Cohesion: 0.22
Nodes (6): Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY, SHIELDMAN

### Community 302 - ".roadStart"
Cohesion: 0.08
Nodes (5): PlaceMusic, Frame, Harbour, CitySizesTest, PlaceMusicTest

### Community 303 - ".postChunk"
Cohesion: 0.18
Nodes (3): Cited Findings, BarracksPeople, CityGuards

### Community 309 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 310 - "ConquestWorldScreen"
Cohesion: 0.22
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 318 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.06
Nodes (7): Countryside (hamlets, villages, watchtowers, castles), Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, Nations by zone, Roads (high, city, town roads; bridges; signposts), Seat cities, Storm wall and war zone

### Community 320 - "net.minecraft.core.BlockPos"
Cohesion: 0.11
Nodes (4): GateGeometry, CharacterMarkers, Placed, GateGeometryTest

### Community 321 - "assertequals"
Cohesion: 0.06
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 323 - "UnitsState"
Cohesion: 0.18
Nodes (8): Morale, State, RALLIED, ROUTED, SHATTERED, STEADY, UnitInfo, UnitsState

### Community 326 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.08
Nodes (11): Inferences, Cited Findings, Camera, CameraView, Chunks, ChunkMapAccess, ChunkMapCameraMixin, TrackedEntityCameraMixin (+3 more)

### Community 327 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 339 - "Cited Findings"
Cohesion: 0.07
Nodes (14): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, Arrow, ArrowVolleys, BattleSpotRing (+6 more)

### Community 344 - "HornCall"
Cohesion: 0.40
Nodes (5): HornCall, ATTACKING, MOVING, RALLYING, RETREATING

### Community 351 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **414 isolated node(s):** `NORMAL`, `RED`, `NO_LIGHTMAP`, `NO_SHADER_COLOUR`, `WHITE` (+409 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1369 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **205 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `GrappleHookRenderer.java`, `net.minecraft.world.level.ChunkPos`, `.army`, `.passOnOrders`, `UnitOrder`, `net.neoforged.fml.common.EventBusSubscriber`, `War`, `PveCommands.java`, `RecruitsBattle.java`, `.shift`, `WarCouncil`, `PveCommands Registration`, `net.minecraft.world.item.ItemStack`, `rafasconquest.faction.Faction`, `UnitMode`, `.roadStart`, `.general`, `Peerage`, `RafasConquest.java`, `org.spongepowered.asm.mixin.Mixin`, `net.minecraft.server.level.ServerLevel`, `Row`, `Phase`, `Work`, `HornCall`, `Duty`, `rafasconquest.battle.BattlePlan.Point`, `.enlist`, `CityGridBuilder.java`, `WarCamp`, `ConquestEvents`, `net.neoforged.bus.api.SubscribeEvent`, `ConquestEvents.java`, `Trait`?**
  _High betweenness centrality (0.059) - this node is a cross-community bridge._
- **What connects `NORMAL`, `RED`, `NO_LIGHTMAP` to the rest of the system?**
  _414 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.0413031197037223 - nodes in this community are weakly interconnected._
- **Why does `FieldBlock` connect `FieldBlock` to `Bodies`, `.army`, `Machine`, `.passOnOrders`, `net.neoforged.bus.api.SubscribeEvent`, `BlockGrid`, `RecruitsBattle`, `Task`, `.begin`, `WarPlan`, `Work`, `RecruitsBattle.java`, `Duty`, `rafasconquest.battle.BattlePlan.Point`, `.enlist`, `ShipKind`?**
  _High betweenness centrality (0.050) - this node is a cross-community bridge._
- **Should `net.minecraft.world.level.Level` be split into smaller, more focused modules?**
  _Cohesion score 0.06783511846802986 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._