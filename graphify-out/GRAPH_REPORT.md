# Graph Report - RafasConquest  (2026-11-12)

## Corpus Check
- 506 files · ~452,323 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7269 nodes · 23201 edges · 376 communities (160 shown, 216 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3129 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `241a897c`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FieldBlock
- net.minecraft.world.level.Level
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsArmies.java
- CityGridBuilder
- net.minecraft.world.entity.EntityType
- ShipHulls
- ConquestServer
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- .second
- BaseMap
- UnitOrder
- .render
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
- net.minecraft.world.phys.Vec3
- .shift
- WarCouncil
- .view
- net.minecraft.network.FriendlyByteBuf
- .findGenerationPoint
- PveCommands Registration
- School
- CityPlan
- .of
- .send
- net.minecraft.world.entity.Entity
- net.minecraft.server.MinecraftServer
- rafasconquest.faction.Faction
- Faction
- Lands
- Lands
- net.minecraft.nbt.CompoundTag
- PoiPrecomputer.java
- Personalities
- SeaLanes
- Outcome
- rafasconquest.battle.FormationSnapshot
- Capture
- uuid
- Centre
- Barracks
- BattleScreen.java
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
- .startAt
- Phase
- .board
- net.minecraft.world.level.block.state.BlockState
- GateBlockEntity
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
- .generate
- net.minecraft.core.Vec3i
- DistantOcclusion
- FactionInfoScreen
- BattleRole
- DistantArmyRenderer.java
- Cloth
- FigureBatches
- NobleTitle
- MouthLayer.java
- BlockPos
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
- CapitalLayout
- RecruitFigures
- Body
- .view
- Override
- BattleShips
- GrappleHookRenderer.java
- MapLayer
- Roads
- Kind
- .render
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .onLevelLoad
- net.minecraft.world.level.block.Block
- Townsfolk
- GoldChurch.java
- .hash
- .place
- CLAUDE.md
- .onLogout
- .plan
- .plan
- MapContext
- PoliticalStyle
- Machine
- .world
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- .drawEmpire
- org.jetbrains.annotations.Nullable
- BoardScreen
- SoldierFigure
- .touches
- Task
- .noble
- Duty
- .view
- .view
- rafasconquest.battle.BattlePlan.Point
- .build
- net.minecraft.resources.ResourceLocation
- Countryside
- .stat
- Race
- net.minecraft.resources.ResourceLocation
- net.neoforged.bus.api.SubscribeEvent
- BattleSounds
- Kind
- DistantArmyRenderer
- View
- DayClockTest
- ConquestGameRules
- GateBlockEntity
- Trait
- OrganizationScreen
- Call
- Ramming
- Shape
- Profiling run sheet (baseline)
- .measure
- .passOnOrders
- .onClientExtensions
- BlockGrid
- BattleKeepInventory.java
- ClientPolitics
- Politics
- CourtView
- CityTerrainPiece
- Realm
- .tick
- .rightClick
- Kind
- RoadFeature.java
- How Total War calculates battles between units
- Debug
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
- .siege
- GatePlanksBlock
- list
- .hinge
- assertequals
- BattleType
- UnitsState
- .at
- org.spongepowered.asm.mixin.Mixin
- Kind
- Size
- ShareLiveTest
- ClientBarks
- Clash
- com.mojang.blaze3d.vertex.PoseStack
- com.talhanation.recruits.entities.AbstractRecruitEntity
- WarCamp
- ShipKind
- Build
- .named
- FigureBatchesTest
- DistantOcclusionTest.java
- 6. Sodium (and Iris) compatibility
- BaseMapPart
- WorldStretch
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- SetSetting
- RoadNetworkTest
- .groundCoversEveryTent

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
- `Every frame pays for disk listings, per-rectangle flushes and unculled soldiers` --references--> `RoadLayer`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Cited Findings` --references--> `RoadLayer`  [INFERRED]
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

## Communities (376 total, 216 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.04
Nodes (7): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 1 - "net.minecraft.world.level.Level"
Cohesion: 0.08
Nodes (5): Sea, GrandStrategyBlock, StrategyBoardBlock, WorldMapItem, Recruiter

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.07
Nodes (8): CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece, PlaceholderPiece

### Community 3 - ".army"
Cohesion: 0.07
Nodes (3): Point, FieldBattleBodiesTest, FieldBattleTest

### Community 4 - "CityTerrain"
Cohesion: 0.10
Nodes (11): CityTerrain, Edge, CARVE, FILL, Heights, Plan, Side, EAST (+3 more)

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.07
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "CityGridBuilder"
Cohesion: 0.09
Nodes (9): CityGridBuilder, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY (+1 more)

### Community 8 - "net.minecraft.world.entity.EntityType"
Cohesion: 0.09
Nodes (5): ConquestContent, GrappleHooks, ConquestEntities, RafasConquest, ConquestWorldgen

### Community 9 - "ShipHulls"
Cohesion: 0.42
Nodes (3): BattleKind, Part, ShipHulls

### Community 10 - "ConquestServer"
Cohesion: 0.14
Nodes (3): ConquestServer, StructureLink, PoiPrecomputer

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - ".second"
Cohesion: 0.05
Nodes (3): GeneralTask, RecruitsBattle, TickTimes

### Community 13 - "BaseMap"
Cohesion: 0.09
Nodes (10): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, ClientMapCache, BaseMap, Nation (+2 more)

### Community 14 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 15 - ".render"
Cohesion: 0.10
Nodes (10): 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Gaps, Inferences, Takeaway, CharacterScreen, Retainer, Spot (+2 more)

### Community 16 - ".begin"
Cohesion: 0.10
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.15
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 19 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.05
Nodes (3): Races (Sunborn, Vinekin, Meadowfolk, ...), GateScreen, InventoryStatsButton

### Community 20 - "PveCommands.java"
Cohesion: 0.09
Nodes (30): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+22 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.16
Nodes (4): Camera, Card, Enlisted, Unit

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.18
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 30 - ".view"
Cohesion: 0.17
Nodes (3): PlayerStats, View, Retinues

### Community 31 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.06
Nodes (11): BattleSpot, FactionInfoReply, FigureStrike, HornOrders, PeerageView, PoliticsView, StatsView, TalkChoice (+3 more)

### Community 32 - ".findGenerationPoint"
Cohesion: 0.11
Nodes (3): CityDistrictStructure, Ground, CityStructure

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.13
Nodes (3): CityPlan, Piece, NobleDistrictTest

### Community 36 - ".of"
Cohesion: 0.14
Nodes (5): FactionBanner, Layer, FactionBannerNbt, CityBanners, FactionBannerTest

### Community 37 - ".send"
Cohesion: 0.09
Nodes (11): BackBanner, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel, Type (+3 more)

### Community 39 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.10
Nodes (5): FeudDuel, Goal, MusterBars, PoliticsSync, Wars

### Community 41 - "Faction"
Cohesion: 0.07
Nodes (14): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction, FactionType, BANDIT, CITY_STATE, EMPIRE (+6 more)

### Community 42 - "Lands"
Cohesion: 0.13
Nodes (4): Feud, Lord, Feuds, Front

### Community 43 - "Lands"
Cohesion: 0.10
Nodes (4): Cited Findings, Lands, Stronghold, Claim

### Community 44 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.05
Nodes (7): Saved, BattleBannerBlockEntity, Palette, WallTemplates, Placed, MarkerBlockEntity, MarkerSpec

### Community 45 - "PoiPrecomputer.java"
Cohesion: 0.05
Nodes (5): ConquestConfig, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "SeaLanes"
Cohesion: 0.22
Nodes (6): Lane, Leg, Port, SeaLanes, Ship, Voyage

### Community 50 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.15
Nodes (6): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, DistantArmies

### Community 51 - "Capture"
Cohesion: 0.12
Nodes (4): Capture, Film, Horse, RecruitFiguresTest

### Community 53 - "Centre"
Cohesion: 0.17
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 54 - "Barracks"
Cohesion: 0.18
Nodes (3): Barracks, BarracksPiece, BarracksTest

### Community 56 - "CityStyles"
Cohesion: 0.22
Nodes (3): Building, CityStyles, Houses

### Community 57 - "City"
Cohesion: 0.16
Nodes (4): City, Grid, SeatCities, CityIndex

### Community 58 - "Faction Map View"
Cohesion: 0.14
Nodes (3): Cited Findings, FactionMapScreen, MapView

### Community 59 - ".step"
Cohesion: 0.14
Nodes (4): FigureMotion, Match, State, FigureMotionTest

### Community 61 - "Peerage"
Cohesion: 0.10
Nodes (10): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+2 more)

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.10
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.12
Nodes (4): Loaded, Offsets, Placement, StyleLibrary

### Community 66 - ".step"
Cohesion: 0.12
Nodes (8): Cited Findings, Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest, BattleSnapshotGearTest

### Community 67 - ".choose"
Cohesion: 0.17
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 71 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.19
Nodes (4): Land, Result, SiegeSurvey, Wall

### Community 72 - "DistantShips"
Cohesion: 0.18
Nodes (3): DistantShips, Far, SableSeen

### Community 73 - "BattlePlan"
Cohesion: 0.06
Nodes (8): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, BattlePlanTest

### Community 75 - ".startAt"
Cohesion: 0.12
Nodes (5): Inferences, Api, DistantGeneration, Officers, BattleSite.Water

### Community 76 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 77 - ".board"
Cohesion: 0.08
Nodes (12): TechTreeScreen, DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind (+4 more)

### Community 78 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.07
Nodes (4): BattleBannerBlock, BattleWallBannerBlock, GateBlock, MarkerBlock

### Community 79 - "GateBlockEntity"
Cohesion: 0.08
Nodes (8): 10. Overall ranking, and areas checked that look fine, Cited Findings, Gaps, Inferences, Takeaway, BattleBannerRenderer, GateRenderer, MarkerRenderer

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "CityLayout"
Cohesion: 0.09
Nodes (5): CityLayout, District, Site, WorldSettings, CityLayoutTest

### Community 84 - "Work"
Cohesion: 0.05
Nodes (37): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+29 more)

### Community 85 - "Land"
Cohesion: 0.14
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 86 - "Calibration"
Cohesion: 0.19
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.05
Nodes (30): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+22 more)

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - "CityLayout"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 92 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.17
Nodes (6): BannerArt, Carried, ClothRenderer, Kind, Layer, Live

### Community 95 - ".generate"
Cohesion: 0.07
Nodes (13): ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry, Homeland, DESERT, JUNGLE (+5 more)

### Community 98 - "DistantOcclusion"
Cohesion: 0.14
Nodes (6): Cited Findings, DistantOcclusion, Mode, OFF, ON, VIEW

### Community 100 - "BattleRole"
Cohesion: 0.18
Nodes (6): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT

### Community 101 - "DistantArmyRenderer.java"
Cohesion: 0.10
Nodes (5): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), DistantDepth, Figure

### Community 102 - "Cloth"
Cohesion: 0.14
Nodes (3): Cloth, Solid, ClothTest

### Community 103 - "FigureBatches"
Cohesion: 0.15
Nodes (3): Batch, Corners, FigureBatches

### Community 105 - "NobleTitle"
Cohesion: 0.16
Nodes (5): NobleTitle, COUNT, KNIGHT, NONE, PeerageSync

### Community 107 - "BlockPos"
Cohesion: 0.21
Nodes (4): GateSet, GateView, MarkerSet, MarkerView

### Community 109 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 110 - "FigureGear"
Cohesion: 0.18
Nodes (7): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 111 - "City"
Cohesion: 0.12
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 112 - "BattleShips"
Cohesion: 0.16
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips, Ride

### Community 113 - "WaterSwell"
Cohesion: 0.08
Nodes (11): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Ground, WaterSwell, Depths (+3 more)

### Community 114 - "Arm"
Cohesion: 0.06
Nodes (23): 1. Flank and rear shock, 2. Nearby rout (fright), 3. Army-wide losses, 4. Shattering, Design: adding the four morale shocks (built 2026-11-11, untested in game), Tests (FieldBattleTest), The core change: morale becomes a sum, Arm (+15 more)

### Community 115 - "ConquestSavedData"
Cohesion: 0.07
Nodes (7): Cited Findings, ConquestSavedData, MapPreview, Poi, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 116 - "ConquestNetwork"
Cohesion: 0.10
Nodes (7): Bark, CameraAt, CityView, ConquestNetwork, RequestBaseMap, RequestFactionInfo, StatsRequest

### Community 117 - "Cited Findings"
Cohesion: 0.17
Nodes (11): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom?, Gaps (+3 more)

### Community 121 - ".column"
Cohesion: 0.15
Nodes (6): Column, Kind, NONE, PLATE, SLOPE, CityTerrainTest

### Community 122 - "CapitalLayout"
Cohesion: 0.14
Nodes (6): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest, StreetWalksTest

### Community 123 - "RecruitFigures"
Cohesion: 0.22
Nodes (4): Layer, Look, Mesh, RecruitFigures

### Community 124 - "Body"
Cohesion: 0.11
Nodes (18): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+10 more)

### Community 125 - ".view"
Cohesion: 0.09
Nodes (12): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+4 more)

### Community 128 - "BattleShips"
Cohesion: 0.16
Nodes (4): BattleShips, Helm, Ram, SableLink

### Community 129 - "GrappleHookRenderer.java"
Cohesion: 0.09
Nodes (4): CapeCurio, ClothItemRenderer, GrappleHookRenderer, GrappleHook

### Community 130 - "MapLayer"
Cohesion: 0.15
Nodes (6): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer, UnitLayer

### Community 132 - "Roads"
Cohesion: 0.14
Nodes (10): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Plan (+2 more)

### Community 133 - "Kind"
Cohesion: 0.09
Nodes (16): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY (+8 more)

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (23): Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, Cited Findings, Gaps (+15 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings (+28 more)

### Community 142 - "net.minecraft.world.level.block.Block"
Cohesion: 0.09
Nodes (6): CourtSet, Seat, Style, CommandTableBlock, QuestBoardBlock, Builder

### Community 146 - "GoldChurch.java"
Cohesion: 0.13
Nodes (10): Enlisted players, Gold church, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter, The war horn (+2 more)

### Community 148 - ".hash"
Cohesion: 0.12
Nodes (6): ChunkCoord, FarmLayout, NobleAnnex, OriginRoller, Roll, SeedHash

### Community 149 - ".place"
Cohesion: 0.10
Nodes (5): Cited Findings, RoadFeature, Profile, RoadNetwork, Stretch

### Community 152 - ".onLogout"
Cohesion: 0.13
Nodes (3): BackBanners, Throw, ThrownFigures

### Community 155 - ".plan"
Cohesion: 0.27
Nodes (4): Link, Place, Spur, RoadsTest

### Community 156 - ".plan"
Cohesion: 0.12
Nodes (7): Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 157 - "MapContext"
Cohesion: 0.18
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 163 - "Machine"
Cohesion: 0.10
Nodes (12): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+4 more)

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.06
Nodes (25): WarHorn, BattleStandardItem, Armoury, Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY (+17 more)

### Community 174 - ".drawEmpire"
Cohesion: 0.27
Nodes (3): Hit, LabelLayer, LabelShape

### Community 186 - "SoldierFigure"
Cohesion: 0.18
Nodes (8): Boxes, Palette, Shape, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 205 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 206 - ".noble"
Cohesion: 0.23
Nodes (6): BlockPacker, Placed, Rect, Size, BlockPackerTest, PlacementOffsetsTest

### Community 217 - "Duty"
Cohesion: 0.11
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 218 - ".view"
Cohesion: 0.24
Nodes (7): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, GrandStrategy, View

### Community 219 - ".view"
Cohesion: 0.18
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 221 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.04
Nodes (17): Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate, GateAction (+9 more)

### Community 223 - ".build"
Cohesion: 0.11
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 226 - "Countryside"
Cohesion: 0.20
Nodes (4): Countryside, Plan, CountrysideTest, World

### Community 228 - ".stat"
Cohesion: 0.08
Nodes (11): ClientPeerage, Names, Honour, Honours, Stat, DIPLOMACY, INTRIGUE, LEARNING (+3 more)

### Community 231 - "Race"
Cohesion: 0.20
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 234 - "net.neoforged.bus.api.SubscribeEvent"
Cohesion: 0.04
Nodes (4): Post, ClientEvents, Registration, ConquestEvents

### Community 235 - "BattleSounds"
Cohesion: 0.25
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 236 - "Kind"
Cohesion: 0.18
Nodes (8): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Plan

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.09
Nodes (4): DistantArmyRenderer, Hull, Rects, Placed

### Community 240 - "View"
Cohesion: 0.10
Nodes (8): BoardView, ChargeHits, CityClose, GrandStrategyView, OpenBattleScreen, Place, SeaSwell, TalkView

### Community 242 - "DayClockTest"
Cohesion: 0.25
Nodes (3): DayClock, Step, DayClockTest

### Community 249 - "GateBlockEntity"
Cohesion: 0.09
Nodes (9): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+1 more)

### Community 252 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 257 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - ".passOnOrders"
Cohesion: 0.05
Nodes (4): Cited Findings, Landing, Live, DoctrineTest

### Community 274 - "Politics"
Cohesion: 0.05
Nodes (14): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Army, Event, Muster (+6 more)

### Community 275 - "CourtView"
Cohesion: 0.08
Nodes (6): Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 277 - "Realm"
Cohesion: 0.12
Nodes (4): Hook, Realm, Succession, Taking

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "RoadFeature.java"
Cohesion: 0.04
Nodes (45): Ambushes from woods, Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Character page, Characters (holders, skills, prowess, traits) (+37 more)

### Community 292 - "How Total War calculates battles between units"
Cohesion: 0.17
Nodes (11): 1. Combat is per soldier, not per unit, 2. Does the swing land?, 3. How much damage the hit does, 4. Charges, 5. Missiles, 6. Leadership (morale): what actually wins battles, 7. Autoresolve, 8. Older games (Shogun 1, Medieval 1, Rome 1) (+3 more)

### Community 293 - "Debug"
Cohesion: 0.33
Nodes (6): Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE

### Community 295 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 300 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 301 - "Job"
Cohesion: 0.20
Nodes (6): Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY, SHIELDMAN

### Community 302 - ".roadStart"
Cohesion: 0.11
Nodes (3): PlaceMusic, CitySizesTest, PlaceMusicTest

### Community 309 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 310 - "ConquestWorldScreen"
Cohesion: 0.20
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 321 - "assertequals"
Cohesion: 0.06
Nodes (7): SiegeSurveyTest, GrandStrategyDateTest, NobleAnnexTest, ArmouryTest, PeerageTest, RetinuesTest, TechTreeTest

### Community 322 - "BattleType"
Cohesion: 0.22
Nodes (5): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE

### Community 323 - "UnitsState"
Cohesion: 0.18
Nodes (8): Morale, State, RALLIED, ROUTED, SHATTERED, STEADY, UnitInfo, UnitsState

### Community 326 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.07
Nodes (14): Inferences, Cited Findings, Cited Findings, CameraView, Chunks, ChunkMapAccess, ChunkMapCameraMixin, CityVegetationMixin (+6 more)

### Community 327 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 328 - "Size"
Cohesion: 0.22
Nodes (6): Size, LARGE, MEDIUM, SMALL, XLARGE, XXLARGE

### Community 334 - "Clash"
Cohesion: 0.28
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 339 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.14
Nodes (4): Cited Findings, Arrow, ArrowVolleys, BattleSpotRing

### Community 344 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.11
Nodes (12): HornCall, ATTACKING, MOVING, RALLYING, RETREATING, Role, BEARER, GENERAL (+4 more)

### Community 351 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 352 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 362 - "6. Sodium (and Iris) compatibility"
Cohesion: 0.50
Nodes (4): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Takeaway

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **414 isolated node(s):** `BAIT`, `FLANKER`, `FRONTLINE`, `KILL_SQUAD`, `SUPPORT` (+409 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1370 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **216 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `.second` to `FieldBlock`, `BattleShips`, `net.minecraft.world.level.ChunkPos`, `.army`, `Shape`, `GrappleHookRenderer.java`, `.passOnOrders`, `net.minecraft.world.entity.EntityType`, `UnitOrder`, `.begin`, `PveCommands.java`, `RecruitsBattle.java`, `net.minecraft.world.phys.Vec3`, `WarCouncil`, `PveCommands Registration`, `Machine`, `net.minecraft.world.entity.Entity`, `net.minecraft.world.item.ItemStack`, `rafasconquest.faction.Faction`, `UnitMode`, `.kitPlayer`, `.siege`, `RafasConquest.java`, `org.spongepowered.asm.mixin.Mixin`, `net.minecraft.server.level.ServerLevel`, `.startAt`, `Phase`, `Work`, `Doctrine`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `Duty`, `WarCamp`, `rafasconquest.battle.BattlePlan.Point`, `.enlist`, `net.neoforged.bus.api.SubscribeEvent`?**
  _High betweenness centrality (0.056) - this node is a cross-community bridge._
- **What connects `BAIT`, `FLANKER`, `FRONTLINE` to the rest of the system?**
  _414 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.044900800556909155 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `net.minecraft.world.entity.EntityType`, `ConquestServer`, `.onLevelLoad`, `.render`, `ClientPolitics`, `net.minecraft.client.gui.GuiGraphics`, `CourtView`, `Realm`, `.place`, `.plan`, `WarCouncil`, `MapContext`, `PoliticalStyle`, `School`, `net.minecraft.world.item.ItemStack`, `net.minecraft.server.MinecraftServer`, `PoiPrecomputer.java`, `.postChunk`, `org.jetbrains.annotations.Nullable`, `uuid`, `Centre`, `City`, `list`, `TitleMapTest`, `RafasConquest.java`, `Terrain`, `.generate`, `City`, `Cited Findings`, `GrandStrategyScreen`, `OrganizationScreen`?**
  _High betweenness centrality (0.045) - this node is a cross-community bridge._
- **Should `net.minecraft.world.level.Level` be split into smaller, more focused modules?**
  _Cohesion score 0.08013468013468013 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._