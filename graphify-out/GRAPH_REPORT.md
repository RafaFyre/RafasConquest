# Graph Report - RafasConquest  (2026-11-13)

## Corpus Check
- 511 files · ~458,402 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7320 nodes · 23400 edges · 383 communities (157 shown, 226 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3150 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `2f382ce8`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FieldBlock
- net.minecraft.world.level.block.state.BlockState
- net.minecraft.world.level.ChunkPos
- .army
- MapContext
- BattleScreen
- CityGridBuilder
- Lands
- RecruitsBattle
- .findGenerationPoint
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- Recruiter
- BaseMap
- UnitOrder
- PoliticsTest
- .deploy
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.GuiGraphics
- PveCommands.java
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- RafasConquest.java
- HorizontalStretch
- CityManagerScreen
- .plan
- org.spongepowered.asm.mixin.Mixin
- .shift
- WarCouncil
- net.minecraft.server.level.ServerLevel
- Retinues
- net.minecraft.network.RegistryFriendlyByteBuf
- MouthLayer.java
- PveCommands Registration
- School
- CityPlan
- ConquestSavedData
- ChunkMapCameraMixin.java
- Arm
- .map
- rafasconquest.faction.Faction
- net.minecraft.network.FriendlyByteBuf
- Lands
- RecruitFigures
- rafasconquest.battle.FieldBlock
- FieldArmy
- Personalities
- SeaLanes
- net.neoforged.neoforge.network.handling.IPayloadContext
- Cited Findings
- BattlePlan
- PoiPrecomputer.java
- Centre
- Barracks
- .walks
- CityStyles
- SeatCities
- Faction Map View
- .step
- ClientScreens
- net.minecraft.core.Vec3i
- TitleMapTest
- Bodies
- StyleLibrary
- .step
- .choose
- BattleMap.java
- Row
- SableLink
- Ride
- .deployment
- MapLands
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- .hash
- .board
- ClientEvents
- SiegeSurvey
- Ownership & Territory
- CityLayout
- Politics
- Land
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- Faction
- .postChunk
- .state
- PoliticalStyle
- PlaceMusic.java
- net.minecraft.core.BlockPos
- FactionInfoScreen
- Point
- CityTerrainPiece
- Cloth
- FigureBatches
- .view
- .view
- .say
- Override
- .of
- net.minecraft.world.level.block.entity.BlockEntity
- NobleTitle
- CameraView
- WaterSwell
- SiegeWorks
- Homeland
- WallTemplates
- Cited Findings
- .build
- GrandStrategyScreen
- CityTerrain
- WorldSettings
- .accept
- City
- com.mojang.blaze3d.vertex.PoseStack
- Settlement
- BattleShips
- GrappleHook
- .render
- CourtView
- Roads
- .findGenerationPoint
- SeaCalm
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .onLevelLoad
- GoldChurch.java
- Realm
- net.minecraft.resources.ResourceLocation
- MapLayer
- RoadNetwork
- CLAUDE.md
- War
- Rank
- .plan
- .sendVolleys
- RoadNetwork
- Machine
- FigureGear
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- RecruitsBattle
- BlockPos
- BoardScreen
- SoldierFigure
- Build
- .soundHorn
- .noble
- Duty
- ConquestNetwork
- net.minecraft.server.level.ServerPlayer
- OrganizationScreen
- Stat
- RecruitFigures.java
- DistantOcclusion
- CityVegetationMixin.java
- .render
- BattleSnapshot
- Townsperson
- net.neoforged.bus.api.SubscribeEvent
- .send
- Piece
- DistantArmyRenderer
- BattleSounds
- Body
- ConquestGameRules
- .boundsAt
- GateBlockEntity
- Trait
- Feud
- Ride
- .plan
- Phase
- Profiling run sheet (baseline)
- .measure
- net.minecraft.sounds.SoundEvent
- .world
- net.minecraft.world.level.biome.Biome
- BattleKeepInventory
- ClientPolitics
- rafasconquest.core.WorldSettings
- CourtView
- .fire
- Realm
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- CityLayout
- Kind
- .onLogout
- Kind
- Enlisted players
- How Total War calculates battles between units
- Role
- net.minecraft.server.level.ServerLevel
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- .sendToServer
- .render
- Corners
- UnitsState
- DistantHorizonsArmies
- Call
- Race
- DistantHorizonsLink
- RafasConquest (PvE addon for Villager Recruits)
- Override
- .at
- Size
- Ranks
- arraylist
- ClientBarks
- assertequals
- .rout
- Characters (holders, skills, prowess, traits)
- ShareLiveTest
- Kind
- DistantGeneration
- rafasconquest.battle.BattlePlan.Point
- .siege
- Wars (daily nation decisions)
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- net.neoforged.fml.common.EventBusSubscriber
- Ranks (Soldier to Grand Marshal)
- Where are full scans, nested loops, repeated lookups and per-tick allocations?
- .save
- .order
- .sendBattleSpot
- .sendGrandStrategy
- Overall: which server/common hotspots matter most (ranked)?
- Mixins on server paths: what they hook, how hot, added cost
- Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 299 edges
2. `FieldBlock` - 222 edges
3. `FieldBattle` - 212 edges
4. `Point` - 153 edges
5. `FieldArmy` - 127 edges
6. `ConquestNetwork` - 119 edges
7. `Lands` - 89 edges
8. `Siege` - 84 edges
9. `BattleScreen` - 82 edges
10. `Politics` - 79 edges

## Surprising Connections (you probably didn't know these)
- `Battle code costs 0.2 ms; the multi-second freezes come from blocking` --references--> `RecruitsArrowCleanerMixin`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/mixin/RecruitsArrowCleanerMixin.java
- `Conclusion` --references--> `RoadLayer`  [INFERRED]
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

## Communities (383 total, 226 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.03
Nodes (18): BlockGrid, FieldBattle, Target, Volley, FieldBlock, ShipKind, ARCHERS, CAMP (+10 more)

### Community 1 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.05
Nodes (9): BattleBannerBlock, BattleWallBannerBlock, CommandTableBlock, GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, Builder (+1 more)

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.06
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - ".army"
Cohesion: 0.08
Nodes (4): DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, GrandStrategyDateTest

### Community 4 - "MapContext"
Cohesion: 0.15
Nodes (5): MapContext, Place, PlaceMenuLayer, PoiIconLayer, LabelShape

### Community 7 - "CityGridBuilder"
Cohesion: 0.08
Nodes (10): Houses, CityGridBuilder, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA (+2 more)

### Community 8 - "Lands"
Cohesion: 0.09
Nodes (4): Cited Findings, Lands, Stronghold, Claim

### Community 9 - "RecruitsBattle"
Cohesion: 0.06
Nodes (22): Camera, Card, Enlisted, Role, BEARER, GENERAL, GUARD, LEADER (+14 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 13 - "BaseMap"
Cohesion: 0.10
Nodes (10): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, MapPreview, BaseMap, Nation (+2 more)

### Community 14 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.15
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 19 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.04
Nodes (7): Character page, Court 3D scene (CourtView), Demographics (population by culture and race), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), GateScreen, InventoryStatsButton

### Community 20 - "PveCommands.java"
Cohesion: 0.10
Nodes (30): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+22 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.20
Nodes (5): ClientChunkWindowMixin, DhShaderLoaderMixin, DhTerrainShaderMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.16
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 34 - "School"
Cohesion: 0.09
Nodes (14): Dojos, Ground, School, BLOOD, FIRE, ICE, IRON, LIGHT (+6 more)

### Community 35 - "CityPlan"
Cohesion: 0.16
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 36 - "ConquestSavedData"
Cohesion: 0.07
Nodes (6): Cited Findings, ConquestSavedData, Poi, EconomyTick, PoiPrecomputer, ConquestSavedDataTest

### Community 38 - "Arm"
Cohesion: 0.09
Nodes (15): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+7 more)

### Community 39 - ".map"
Cohesion: 0.11
Nodes (4): Goal, MusterBars, PoliticsSync, Wars

### Community 41 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.09
Nodes (6): BoardView, FactionInfoReply, HornOrders, PeerageView, PoliticsView, TalkView

### Community 42 - "Lands"
Cohesion: 0.17
Nodes (3): Lord, Feuds, Front

### Community 43 - "RecruitFigures"
Cohesion: 0.10
Nodes (8): Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures, RecruitFiguresTest

### Community 45 - "FieldArmy"
Cohesion: 0.07
Nodes (3): FieldArmy, Event, Order

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "SeaLanes"
Cohesion: 0.22
Nodes (6): Lane, Leg, Port, SeaLanes, Ship, Voyage

### Community 48 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.09
Nodes (6): CameraAt, CityView, SetSetting, TalkChoice, TeleportTo, Entry

### Community 50 - "Cited Findings"
Cohesion: 0.10
Nodes (8): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, DistantArmies, Throw, ThrownFigures

### Community 52 - "PoiPrecomputer.java"
Cohesion: 0.07
Nodes (13): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoliticalMap (+5 more)

### Community 53 - "Centre"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 55 - ".walks"
Cohesion: 0.08
Nodes (4): PlaceMusic, CitySizesTest, StreetWalksTest, PlaceMusicTest

### Community 57 - "SeatCities"
Cohesion: 0.13
Nodes (4): Grid, SeatCities, SeatCitiesTest, World

### Community 59 - ".step"
Cohesion: 0.12
Nodes (6): FigureMotion, Ground, Match, Placed, State, FigureMotionTest

### Community 63 - "TitleMapTest"
Cohesion: 0.18
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.12
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - ".step"
Cohesion: 0.14
Nodes (7): Cited Findings, Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 67 - ".choose"
Cohesion: 0.11
Nodes (11): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+3 more)

### Community 72 - "Ride"
Cohesion: 0.15
Nodes (7): Takeaway, DistantShips, Far, Riding, BattleKind, Part, ShipHulls

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.12
Nodes (14): Cited Findings, Cited Findings, Gaps, Gaps, Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Inferences (+6 more)

### Community 76 - ".hash"
Cohesion: 0.11
Nodes (6): ChunkCoord, FarmLayout, NobleAnnex, OriginRoller, Roll, ProvinceNames

### Community 77 - ".board"
Cohesion: 0.13
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 81 - "SiegeSurvey"
Cohesion: 0.10
Nodes (7): Land, Result, SiegeSurvey, Wall, Cell, Terrain, SiegeSurveyTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "CityLayout"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 84 - "Politics"
Cohesion: 0.11
Nodes (7): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Event, Politics

### Community 85 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 86 - "Calibration"
Cohesion: 0.17
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.07
Nodes (23): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+15 more)

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - "Faction"
Cohesion: 0.09
Nodes (5): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction, FactionInfoTest

### Community 92 - ".postChunk"
Cohesion: 0.15
Nodes (4): Cited Findings, BarracksPeople, CityGuards, ConquestServer

### Community 96 - "PlaceMusic.java"
Cohesion: 0.04
Nodes (13): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), GrappleHooks, FactionBannerNbt, ConquestEntities, CityStructure (+5 more)

### Community 98 - "net.minecraft.core.BlockPos"
Cohesion: 0.07
Nodes (10): PlannedTent, Saved, WarCamp, CourtSet, Seat, Style, GateGeometry, CityGates (+2 more)

### Community 99 - "FactionInfoScreen"
Cohesion: 0.16
Nodes (3): FactionInfoScreen, Line, StructureLink

### Community 100 - "Point"
Cohesion: 0.05
Nodes (18): Point, Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate (+10 more)

### Community 102 - "Cloth"
Cohesion: 0.08
Nodes (6): Cloth, Solid, Wind, ClothTest, OffTheBackTest, WoolTest

### Community 103 - "FigureBatches"
Cohesion: 0.10
Nodes (9): Batch, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE, FigureBatches (+1 more)

### Community 105 - ".view"
Cohesion: 0.09
Nodes (10): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+2 more)

### Community 106 - ".view"
Cohesion: 0.18
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 109 - ".of"
Cohesion: 0.17
Nodes (4): FactionBanner, Layer, CityBanners, FactionBannerTest

### Community 111 - "NobleTitle"
Cohesion: 0.14
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageSync

### Community 112 - "CameraView"
Cohesion: 0.13
Nodes (7): Inferences, Cited Findings, Cited Findings, CameraView, ChunkMapAccess, RecruitsArrowCleanerMixin, TrackedEntityCameraMixin

### Community 113 - "WaterSwell"
Cohesion: 0.12
Nodes (9): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Columns, SableSeen, Made (+1 more)

### Community 114 - "SiegeWorks"
Cohesion: 0.09
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 115 - "Homeland"
Cohesion: 0.10
Nodes (11): Dojo, BaseMapGenerator, Heights, Homeland, DESERT, JUNGLE, PLAINS, SAVANNA (+3 more)

### Community 117 - "Cited Findings"
Cohesion: 0.11
Nodes (13): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+5 more)

### Community 118 - ".build"
Cohesion: 0.11
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 121 - "CityTerrain"
Cohesion: 0.05
Nodes (19): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+11 more)

### Community 122 - "WorldSettings"
Cohesion: 0.18
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 124 - "City"
Cohesion: 0.13
Nodes (6): Countryside, Plan, City, CityIndex, CountrysideTest, World

### Community 125 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.07
Nodes (12): BannerArt, BattleBannerRenderer, CapeCurio, ClothItemRenderer, Carried, ClothRenderer, Fabric, Kind (+4 more)

### Community 126 - "Settlement"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 128 - "BattleShips"
Cohesion: 0.14
Nodes (6): Hull, Ramming, BattleShips, Helm, Ram, RammingTest

### Community 129 - "GrappleHook"
Cohesion: 0.18
Nodes (3): GrappleHookRenderer, Registration, GrappleHook

### Community 130 - ".render"
Cohesion: 0.20
Nodes (5): Cited Findings, BaseMapLayer, Hit, LabelLayer, MapView

### Community 131 - "CourtView"
Cohesion: 0.14
Nodes (4): Retainer, Camera, CourtView, Figure

### Community 132 - "Roads"
Cohesion: 0.14
Nodes (10): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Plan (+2 more)

### Community 134 - "SeaCalm"
Cohesion: 0.19
Nodes (5): Sea, Depths, SeaCalm, Strengths, SeaCalmTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.07
Nodes (28): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings (+20 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (40): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 6. Sodium (and Iris) compatibility, 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply (+32 more)

### Community 143 - "Realm"
Cohesion: 0.14
Nodes (5): Bond, Kind, FRIEND, RIVAL, Realm

### Community 148 - "MapLayer"
Cohesion: 0.20
Nodes (5): LegendEntry, MapLayer, Symbol, TerritoryLayer, UnitLayer

### Community 152 - "War"
Cohesion: 0.11
Nodes (5): Army, Muster, CEREMONIAL, OFFENSIVE, War

### Community 155 - "Rank"
Cohesion: 0.08
Nodes (15): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Rank, CAPTAIN (+7 more)

### Community 156 - ".plan"
Cohesion: 0.20
Nodes (4): Centre, Faith, RealmPlanner, Seat

### Community 160 - "RoadNetwork"
Cohesion: 0.14
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 163 - "Machine"
Cohesion: 0.06
Nodes (21): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Machine, ARCBALLISTA, BATTERING_RAM (+13 more)

### Community 164 - "FigureGear"
Cohesion: 0.18
Nodes (7): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.05
Nodes (27): WarHorn, BattleStandardItem, Armoury, Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY (+19 more)

### Community 174 - "RecruitsBattle"
Cohesion: 0.04
Nodes (6): Cited Findings, Outcome, Figure, Officers, RecruitsBattle, TickTimes

### Community 178 - "BlockPos"
Cohesion: 0.17
Nodes (4): GateSet, GateView, MarkerSet, MarkerView

### Community 186 - "SoldierFigure"
Cohesion: 0.18
Nodes (8): Boxes, Palette, Shape, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 204 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 205 - ".soundHorn"
Cohesion: 0.33
Nodes (5): HornCall, ATTACKING, MOVING, RALLYING, RETREATING

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 217 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 218 - "ConquestNetwork"
Cohesion: 0.08
Nodes (13): Bark, ChargeHits, CityClose, ConquestNetwork, FigureStrike, OpenBattleScreen, Place, RequestBaseMap (+5 more)

### Community 219 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.10
Nodes (10): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, CityManager, GrandStrategy, View (+2 more)

### Community 223 - "Stat"
Cohesion: 0.14
Nodes (9): Honour, Honours, Stat, DIPLOMACY, INTRIGUE, LEARNING, MARTIAL, PROWESS (+1 more)

### Community 224 - "RecruitFigures.java"
Cohesion: 0.05
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 225 - "DistantOcclusion"
Cohesion: 0.07
Nodes (13): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Arrow, ArrowVolleys, BattleSpotRing, DistantOcclusion (+5 more)

### Community 228 - ".render"
Cohesion: 0.12
Nodes (5): CharacterScreen, Spot, ClientPeerage, Banner, Portraits

### Community 232 - "Townsperson"
Cohesion: 0.12
Nodes (4): Walk, DistrictKey, Townsfolk, Townsperson

### Community 235 - ".send"
Cohesion: 0.12
Nodes (9): Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel, Type, CLEAR, DELIVER (+1 more)

### Community 236 - "Piece"
Cohesion: 0.13
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.07
Nodes (5): Cited Findings, DistantDepth, DistantArmyRenderer, Figure, Rects

### Community 240 - "BattleSounds"
Cohesion: 0.23
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 242 - "Body"
Cohesion: 0.11
Nodes (18): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+10 more)

### Community 248 - ".boundsAt"
Cohesion: 0.35
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 249 - "GateBlockEntity"
Cohesion: 0.08
Nodes (9): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+1 more)

### Community 252 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 254 - "Feud"
Cohesion: 0.11
Nodes (7): Clash, Feud, Kind, DUEL, SKIRMISH, FeudDuel, Politics.Front

### Community 255 - "Ride"
Cohesion: 0.19
Nodes (3): Ride, Swell, SwellTest

### Community 256 - ".plan"
Cohesion: 0.28
Nodes (4): Link, Place, Spur, RoadsTest

### Community 257 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 275 - "CourtView"
Cohesion: 0.13
Nodes (3): NationScreen, Demographics, Share

### Community 276 - ".fire"
Cohesion: 0.07
Nodes (5): ConquestContent, CharacterMarkers, Placed, MarkerBlockEntity, MarkerSpec

### Community 277 - "Realm"
Cohesion: 0.13
Nodes (3): Hook, Succession, Taking

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.20
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 282 - "CityLayout"
Cohesion: 0.09
Nodes (5): CityLayout, District, Site, WorldSettings, CityLayoutTest

### Community 283 - "Kind"
Cohesion: 0.13
Nodes (12): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY (+4 more)

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "Enlisted players"
Cohesion: 0.18
Nodes (8): Battle phases and days, Enlisted players, Envelopment, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Battle roles (frontline, bait, support, flanker, kill squad), War camps, War council, The war horn

### Community 292 - "How Total War calculates battles between units"
Cohesion: 0.17
Nodes (11): 1. Combat is per soldier, not per unit, 2. Does the swing land?, 3. How much damage the hit does, 4. Charges, 5. Missiles, 6. Leadership (morale): what actually wins battles, 7. Autoresolve, 8. Older games (Shogun 1, Medieval 1, Rome 1) (+3 more)

### Community 294 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 295 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.05
Nodes (3): /council <topic>, TickWeave experiment, StreetWalks

### Community 302 - "UnitsState"
Cohesion: 0.21
Nodes (3): UnitInfo, UnitsRequest, UnitsState

### Community 305 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 306 - "Race"
Cohesion: 0.18
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 309 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 310 - "Override"
Cohesion: 0.13
Nodes (5): BattleSite.Water, ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, SettingsScreen

### Community 313 - "Size"
Cohesion: 0.22
Nodes (6): Size, LARGE, MEDIUM, SMALL, XLARGE, XXLARGE

### Community 315 - "Ranks"
Cohesion: 0.15
Nodes (7): Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY, SHIELDMAN, Ranks

### Community 318 - "arraylist"
Cohesion: 0.05
Nodes (14): Base map generated from seed, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Faction Info screen, Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, World map layers, Nations by zone (+6 more)

### Community 321 - "assertequals"
Cohesion: 0.06
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 323 - ".rout"
Cohesion: 0.10
Nodes (13): 1. Flank and rear shock, 2. Nearby rout (fright), 3. Army-wide losses, 4. Shattering, Design: adding the four morale shocks (built 2026-11-11, untested in game), Tests (FieldBattleTest), The core change: morale becomes a sum, Morale (+5 more)

### Community 324 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.29
Nodes (7): Characters (holders, skills, prowess, traits), Cultures (seven homelands), Epic Knights optional armour (Armoury), Faiths and cults, Martial titles (Sword Saint, Sword King, Sword God), Organizations, Sword sanctums

### Community 327 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 329 - "DistantGeneration"
Cohesion: 0.33
Nodes (3): Inferences, Api, DistantGeneration

### Community 330 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.07
Nodes (7): GeneralTask, GroundPlanTest, Land, Land, Land, Land, WarCampTest

### Community 337 - "Wars (daily nation decisions)"
Cohesion: 0.33
Nodes (5): Battles (prototype), Blocks of 20 soldiers, Heralds (daily news), Mustering (offensive and ceremonial), Wars (daily nation decisions)

### Community 349 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.33
Nodes (4): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender

### Community 359 - "Ranks (Soldier to Grand Marshal)"
Cohesion: 0.40
Nodes (6): Gold church, Land grants (seven ways), Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter

### Community 361 - "Where are full scans, nested loops, repeated lookups and per-tick allocations?"
Cohesion: 0.33
Nodes (4): Gaps, Inferences, Takeaway, Where are full scans, nested loops, repeated lookups and per-tick allocations?

### Community 364 - ".order"
Cohesion: 0.25
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

### Community 367 - "Overall: which server/common hotspots matter most (ranked)?"
Cohesion: 0.50
Nodes (4): Gaps, Inferences, Overall: which server/common hotspots matter most (ranked)?, Takeaway

### Community 370 - "Mixins on server paths: what they hook, how hot, added cost"
Cohesion: 0.50
Nodes (4): Gaps, Inferences, Mixins on server paths: what they hook, how hot, added cost, Takeaway

### Community 377 - "Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI"
Cohesion: 0.40
Nodes (5): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Gaps, Inferences, Takeaway

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Roads (high, city, town roads; bridges; signposts)` → `Mustering (offensive and ceremonial)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **414 isolated node(s):** `ARCHERS`, `CAMP`, `SHOCK`, `SWORDSMEN`, `AMBUSH` (+409 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1371 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **226 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `Phase`, `net.minecraft.world.level.ChunkPos`, `.army`, `GrappleHook`, `RecruitsBattle`, `net.minecraft.world.level.biome.Biome`, `UnitOrder`, `PveCommands.java`, `RafasConquest.java`, `WarCouncil`, `net.minecraft.server.level.ServerLevel`, `.sendVolleys`, `net.minecraft.network.RegistryFriendlyByteBuf`, `PveCommands Registration`, `net.minecraft.world.item.ItemStack`, `net.minecraft.server.level.ServerLevel`, `rafasconquest.faction.Faction`, `.map`, `rafasconquest.battle.FieldBlock`, `.walks`, `BattleMap.java`, `rafasconquest.battle.BattlePlan.Point`, `.soundHorn`, `ClientEvents`, `.siege`, `SiegeSurvey`, `Calibration`, `Doctrine`, `net.minecraft.server.level.ServerPlayer`, `.state`, `PlaceMusic.java`, `net.minecraft.core.BlockPos`, `net.neoforged.bus.api.SubscribeEvent`, `SiegeWorks`?**
  _High betweenness centrality (0.050) - this node is a cross-community bridge._
- **What connects `ARCHERS`, `CAMP`, `SHOCK` to the rest of the system?**
  _414 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.03283582089552239 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `MapContext`, `.onLevelLoad`, `Realm`, `ClientPolitics`, `net.minecraft.client.gui.GuiGraphics`, `CourtView`, `RoadNetwork`, `RafasConquest.java`, `.plan`, `WarCouncil`, `.onLogout`, `ConquestSavedData`, `net.minecraft.world.item.ItemStack`, `.map`, `PoiPrecomputer.java`, `SeatCities`, `arraylist`, `SiegeSurvey`, `Politics`, `.postChunk`, `OrganizationScreen`, `PoliticalStyle`, `.render`, `.view`, `Homeland`, `Cited Findings`, `GrandStrategyScreen`, `City`, `Settlement`?**
  _High betweenness centrality (0.027) - this node is a cross-community bridge._
- **Should `net.minecraft.world.level.block.state.BlockState` be split into smaller, more focused modules?**
  _Cohesion score 0.04959209961356806 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Roads (high, city, town roads; bridges; signposts)` and `Mustering (offensive and ceremonial)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._