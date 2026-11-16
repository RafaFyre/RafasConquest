# Graph Report - RafasConquest  (2026-11-16)

## Corpus Check
- 576 files · ~505,546 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7931 nodes · 25619 edges · 406 communities (154 shown, 252 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3280 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `f64865cb`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Calibration
- net.minecraft.world.level.ChunkPos
- org.junit.jupiter.api.Test
- Lands
- BattleScreen
- FactionInfoTest.java
- RecruitsBattle.java
- Sail
- rafasconquest.battle.FieldBlock
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- PoliticsTest
- BaseMap
- PveCommands.java
- net.minecraft.world.entity.LivingEntity
- ConquestContent.java
- RafasConquest Project Site
- Person
- CityLayout
- .generate
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- Kind
- PoiPrecomputer.java
- CityHud
- Tier
- net.minecraft.server.level.ServerPlayer
- ConquestEvents
- .enlist
- net.neoforged.bus.api.SubscribeEvent
- .map
- Land
- BattleShips
- PveCommands
- CityPlan
- Cities (grid, plots, vanilla village buildings)
- ConquestServer
- WaterSwell
- .send
- rafasconquest.faction.Faction
- Realm
- Arm
- .map
- net.minecraft.client.gui.GuiGraphics
- .view
- Personality
- SeaLanes
- BattlePlan
- .bark
- MarkerBlock.java
- .postChunk
- Barracks
- .render
- CityStyles
- Cited Findings
- CityVariant
- CityTerrain
- BattleSounds
- AgeTest.java
- TitleMap
- Bodies
- StyleLibrary
- RecruitsBattle
- WorldSettings
- BattleMap.java
- Row
- School
- DistantShips
- org.spongepowered.asm.mixin.Mixin
- MapLands
- WarCouncil
- .get
- City
- RecruitFigures
- .plan
- net.minecraft.server.level.ServerLevel
- Candidate
- Faction
- .readPlacements
- FormationSnapshot
- UnitOrder
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- ClientScreens
- DialogueScreen
- GrappleHookRenderer.java
- .boundsAt
- Recruiter
- MapLayer
- CityGridBuilder
- FactionInfoScreen
- CourtView
- Override
- Cloth
- FigureBatches
- rafasconquest.battle.BattlePlan.Point
- net.minecraft.core.BlockPos
- How Total War calculates battles between units
- MapContext
- net.minecraft.server.level.ServerPlayer
- CityPanel
- .step
- .plan
- RoadNetwork
- .choose
- SoldierFigure
- .accept
- .moraleOf
- ConquestNetwork
- GrandStrategyScreen
- net.minecraft.world.level.Level
- ConquestSavedData
- Quest
- Rank
- .layout
- CityLayout
- CityTerrainPiece
- Terrain
- TitleOwnersScreen
- ProvinceMap
- .shift
- com.mojang.blaze3d.vertex.PoseStack
- SeaCalm
- Countryside
- .place
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .changed
- HorizontalStretch
- CityManagerScreen
- .offTheBack
- Work
- CLAUDE.md
- MarkerScreen
- .onLogout
- .render
- SiegeWorks
- net.minecraft.sounds.SoundEvent
- CameraView.java
- .rightClick
- Kit
- StrategyRelief
- /pve debug commands
- Settlement
- .findGenerationPoint
- ClientPolitics
- ConquestWorldScreen
- Call
- SailBlock.java
- .noble
- net.minecraft.core.Vec3i
- ArmySpawnScreen
- Rank
- .of
- RecruitFigures.java
- .roadStart
- net.minecraft.nbt.CompoundTag
- DistantHorizonsArmies
- Duty
- DistantHorizonsLink
- .findGenerationPoint
- Kind
- net.minecraft.client.Minecraft
- FieldArmy
- RankKits
- ConquestGameRules
- .onLevelLoad
- ClientModEvents
- .encode
- HarbourCut
- Characters (holders, skills, prowess, traits)
- SableSeen
- Profiling run sheet (baseline)
- .measure
- Clash
- ShipKind
- .findGenerationPoint
- PlaceholderPiece
- BattleType
- BlockGrid
- GateScreen
- DayClockTest
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- BattleKeepInventory
- HarbourCut
- Sea.java
- BackBanners
- Kind
- FaceOffScreen
- .candidateChunk
- net.minecraft.world.level.block.state.BlockState
- net.minecraft.network.FriendlyByteBuf
- State
- ChunkMapCameraMixin
- Doctrine
- State
- Armoury
- WarHorn
- FactionMapScreen
- Phase
- rafasconquest.battle.FieldBlock
- OrganizationScreen
- GateBlockEntity
- Machine
- NobleDistrictTest
- list
- .get
- Lands
- Muster
- .onClientExtensions
- OarFigures
- .postProcess
- ItemFlags.java
- Cited Findings
- DistantGeneration
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- org.joml.Matrix4f
- BoardScreen
- .officers
- Cited Findings
- UnitMode
- ClientBarks
- .inCity
- DistantOcclusion
- net.minecraft.world.level.block.Block
- How do world generation and structure placement work, and what do they cost?
- SwingModes

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 311 edges
2. `FieldBlock` - 222 edges
3. `FieldBattle` - 210 edges
4. `FieldArmy` - 128 edges
5. `ConquestNetwork` - 126 edges
6. `Point` - 116 edges
7. `Lands` - 89 edges
8. `Siege` - 86 edges
9. `BattleScreen` - 86 edges
10. `Politics` - 79 edges

## Surprising Connections (you probably didn't know these)
- `Cited Findings` --references--> `MapView`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/MapView.java
- `Cited Findings` --references--> `Sprite`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/battle/FormationSnapshot.java
- `A tick budget and staggered schedules make the fixes stick` --references--> `BaseMap`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/map/BaseMap.java

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

## Communities (406 total, 252 thin omitted)

### Community 0 - "Calibration"
Cohesion: 0.18
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.08
Nodes (7): BarracksPiece, CityRoadPiece, CountryPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece

### Community 3 - "org.junit.jupiter.api.Test"
Cohesion: 0.06
Nodes (4): DoctrineTest, FieldBattleTest, GrandStrategyDateTest, OarRowingTest

### Community 4 - "Lands"
Cohesion: 0.10
Nodes (4): Feud, Lord, Feuds, Front

### Community 6 - "FactionInfoTest.java"
Cohesion: 0.12
Nodes (11): FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, Poi, PoiType (+3 more)

### Community 7 - "RecruitsBattle.java"
Cohesion: 0.06
Nodes (3): /council <topic>, TickWeave experiment, Officers

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 13 - "BaseMap"
Cohesion: 0.07
Nodes (10): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, BaseMap, Nation, BaseMapStore (+2 more)

### Community 14 - "PveCommands.java"
Cohesion: 0.08
Nodes (31): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+23 more)

### Community 15 - "net.minecraft.world.entity.LivingEntity"
Cohesion: 0.07
Nodes (8): BattleBannerBlock, BattleBannerBlockEntity, FlagBlock, Mount, BARS, BLOCK, FENCE, WALL

### Community 16 - "ConquestContent.java"
Cohesion: 0.08
Nodes (3): ConquestContent, GrappleHooks, ConquestEntities

### Community 17 - "RafasConquest Project Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.15
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 19 - "CityLayout"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 20 - ".generate"
Cohesion: 0.05
Nodes (22): ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors, Homeland, DESERT (+14 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 22 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 23 - "PoiPrecomputer.java"
Cohesion: 0.06
Nodes (4): ConquestConfig, FactionBannerNbt, ConquestWorldgen, FarmStructure

### Community 24 - "CityHud"
Cohesion: 0.09
Nodes (3): CityHud, Spot, CityHudPreviewTest

### Community 25 - "Tier"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.10
Nodes (4): Card, Enlisted, Unit, CityManager

### Community 28 - ".enlist"
Cohesion: 0.05
Nodes (14): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT, GeneralTask (+6 more)

### Community 30 - ".map"
Cohesion: 0.14
Nodes (5): Cited Findings, FeudDuel, MusterBars, PoliticsData, Wars

### Community 31 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 32 - "BattleShips"
Cohesion: 0.12
Nodes (7): Hull, Ramming, BattleShips, Helm, Ram, SableLink, RammingTest

### Community 36 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.11
Nodes (11): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), Deploying to the land (GroundPlan), Package layout (core, data, worldgen, sim, bridge) (+3 more)

### Community 37 - "ConquestServer"
Cohesion: 0.11
Nodes (4): CityBanners, ConquestServer, StructureLink, PoiPrecomputer

### Community 38 - "WaterSwell"
Cohesion: 0.09
Nodes (9): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Made, WaterSwell, Swell (+1 more)

### Community 39 - ".send"
Cohesion: 0.09
Nodes (7): BackBanner, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel

### Community 41 - "Realm"
Cohesion: 0.09
Nodes (9): Hook, Kind, FRIEND, RIVAL, Realm, Succession, Taking, RealmPlannerTest (+1 more)

### Community 42 - "Arm"
Cohesion: 0.06
Nodes (27): Arm, ARCBALLISTAS, ARCHERS, CAVALRY, CULVERINS, ENGINES, HALBERDIERS, HEAVY_CAVALRY (+19 more)

### Community 43 - ".map"
Cohesion: 0.08
Nodes (4): Army, Goal, PoliticsSync, War

### Community 45 - ".view"
Cohesion: 0.08
Nodes (17): Names, NobleTitle, COUNT, DUKE, EMPEROR, KING, KNIGHT, NONE (+9 more)

### Community 46 - "Personality"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (13): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+5 more)

### Community 48 - "BattlePlan"
Cohesion: 0.05
Nodes (9): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, WarPlan (+1 more)

### Community 55 - ".render"
Cohesion: 0.11
Nodes (7): CharacterScreen, Retainer, Spot, Banner, Portraits, Honour, Honours

### Community 56 - "CityStyles"
Cohesion: 0.25
Nodes (3): Building, CityStyles, Houses

### Community 57 - "Cited Findings"
Cohesion: 0.09
Nodes (3): Cited Findings, SiegeTest, WarCampTest

### Community 58 - "CityVariant"
Cohesion: 0.16
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 59 - "CityTerrain"
Cohesion: 0.05
Nodes (19): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+11 more)

### Community 60 - "BattleSounds"
Cohesion: 0.11
Nodes (6): Cited Findings, BattleSounds, ChargeSound, DinSound, Source, DistantArmies

### Community 61 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 63 - "TitleMap"
Cohesion: 0.16
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 66 - "RecruitsBattle"
Cohesion: 0.04
Nodes (15): HornCall, ATTACKING, MOVING, RALLYING, RETREATING, Landing, Live, RecruitsBattle (+7 more)

### Community 67 - "WorldSettings"
Cohesion: 0.09
Nodes (9): CapitalLayout, Land, Site, NobleAnnex, OriginRoller, Roll, SeedHash, CapitalRelocationTest (+1 more)

### Community 69 - "BattleMap.java"
Cohesion: 0.03
Nodes (4): Battle map developer web page, Morale, rout and rally, RafasConquest, SwingModesTest

### Community 70 - "Row"
Cohesion: 0.12
Nodes (3): Fort, Stronghold, Row

### Community 71 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 72 - "DistantShips"
Cohesion: 0.11
Nodes (9): DistantShips, Far, HullKey, Riding, Want, BattleKind, Part, ShipHulls (+1 more)

### Community 73 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.14
Nodes (7): ArrowSaveMixin, CityVegetationMixin, ClientChunkWindowMixin, DhShaderLoaderMixin, DhTerrainShaderMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 75 - "WarCouncil"
Cohesion: 0.15
Nodes (6): Facts, Officer, Said, Talk, WarCouncil, WarCouncilPagesTest

### Community 76 - ".get"
Cohesion: 0.18
Nodes (4): Boards, Entry, View, QuestProgress

### Community 77 - "City"
Cohesion: 0.08
Nodes (16): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+8 more)

### Community 78 - "RecruitFigures"
Cohesion: 0.07
Nodes (9): FigureGear, Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures (+1 more)

### Community 79 - ".plan"
Cohesion: 0.12
Nodes (3): SeatCities, SeatCitiesTest, World

### Community 81 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.08
Nodes (10): ChunkWait, Wait, Land, Result, SiegeSurvey, Wall, ArtilleryMount, CityGates (+2 more)

### Community 82 - "Candidate"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 84 - ".readPlacements"
Cohesion: 0.27
Nodes (3): Offsets, Placement, PlacementOffsetsTest

### Community 85 - "FormationSnapshot"
Cohesion: 0.13
Nodes (13): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HALBERDIERS, HEAVY_CAVALRY (+5 more)

### Community 87 - "UnitOrder"
Cohesion: 0.11
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 91 - "GrappleHookRenderer.java"
Cohesion: 0.11
Nodes (3): GrappleHookRenderer, RecruiterRenderer, GrappleHook

### Community 94 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 95 - "Recruiter"
Cohesion: 0.08
Nodes (5): Recruiter, CharacterMarkers, Placed, MarkerBlockEntity, MarkerSpec

### Community 96 - "MapLayer"
Cohesion: 0.08
Nodes (18): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+10 more)

### Community 98 - "CityGridBuilder"
Cohesion: 0.11
Nodes (3): CityGridBuilder, Ground, Platform

### Community 100 - "CourtView"
Cohesion: 0.07
Nodes (6): ClientPeerage, CourtView, Figure, NationScreen, Demographics, Share

### Community 102 - "Cloth"
Cohesion: 0.12
Nodes (4): Cloth, Solid, ClothTest, WoolTest

### Community 103 - "FigureBatches"
Cohesion: 0.07
Nodes (12): ShipSprite, Batch, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED (+4 more)

### Community 105 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.05
Nodes (18): Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate, GateAction (+10 more)

### Community 106 - "net.minecraft.core.BlockPos"
Cohesion: 0.08
Nodes (9): PlannedTent, Saved, WarCamp, CourtSet, Seat, Style, GateGeometry, PlacedWall (+1 more)

### Community 107 - "How Total War calculates battles between units"
Cohesion: 0.11
Nodes (17): 1. Combat is per soldier, not per unit, 1. Flank and rear shock, 2. Does the swing land?, 2. Nearby rout (fright), 3. Army-wide losses, 3. How much damage the hit does, 4. Charges, 4. Shattering (+9 more)

### Community 108 - "MapContext"
Cohesion: 0.08
Nodes (11): Cited Findings, BaseMapLayer, Hit, LabelLayer, MapContext, MapView, Place, PlaceMenuLayer (+3 more)

### Community 109 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.11
Nodes (5): Garrison, Army, GrandStrategy, View, GarrisonTest

### Community 110 - "CityPanel"
Cohesion: 0.14
Nodes (6): CityPanel, Mode, DISMISS, MARCH, MUSTER, NONE

### Community 111 - ".step"
Cohesion: 0.12
Nodes (6): FigureMotion, Ground, Match, Placed, State, FigureMotionTest

### Community 112 - ".plan"
Cohesion: 0.12
Nodes (6): Centre, TitleStyle, Faith, Bond, RealmPlanner, Seat

### Community 113 - "RoadNetwork"
Cohesion: 0.14
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 114 - ".choose"
Cohesion: 0.17
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 115 - "SoldierFigure"
Cohesion: 0.07
Nodes (32): Weapon, BOW, GREAT, LONG, NONE, SWORD, Body, BALLISTA (+24 more)

### Community 116 - ".accept"
Cohesion: 0.15
Nodes (3): Ranks, Offer, Recruitment

### Community 118 - "ConquestNetwork"
Cohesion: 0.05
Nodes (27): ArtillerySet, Bark, BoardAction, CameraAt, ChargeHits, CityClose, CityView, ConquestNetwork (+19 more)

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.10
Nodes (3): GrandStrategyScreen, Marcher, Spot

### Community 121 - "net.minecraft.world.level.Level"
Cohesion: 0.10
Nodes (3): GrandStrategyBlock, StrategyBoardBlock, WorldMapItem

### Community 123 - "ConquestSavedData"
Cohesion: 0.08
Nodes (6): Cited Findings, ConquestSavedData, MapPreview, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 124 - "Quest"
Cohesion: 0.08
Nodes (15): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Quest, Type (+7 more)

### Community 125 - "Rank"
Cohesion: 0.13
Nodes (10): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+2 more)

### Community 126 - ".layout"
Cohesion: 0.14
Nodes (7): Cited Findings, Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 127 - "CityLayout"
Cohesion: 0.11
Nodes (5): CityLayout, District, Site, WorldSettings, CityLayoutTest

### Community 131 - "ProvinceMap"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 132 - ".shift"
Cohesion: 0.16
Nodes (6): BattleSite, Part, Water, BattleSite.Water, BattleSite.Water, BattleSiteTest

### Community 133 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.04
Nodes (16): BackBannerLayer, BannerArt, BattleBannerRenderer, CapeCurio, ClothItemRenderer, Carried, ClothRenderer, Fabric (+8 more)

### Community 134 - "SeaCalm"
Cohesion: 0.22
Nodes (5): Depths, SeaCalm, Strengths, SeaCalmTest, SwellTest

### Community 136 - "Countryside"
Cohesion: 0.19
Nodes (5): Countryside, Plan, Grid, CountrysideTest, World

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.07
Nodes (28): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings (+20 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings (+28 more)

### Community 141 - ".changed"
Cohesion: 0.14
Nodes (4): PlayerStats, View, Retinues, RetinuesTest

### Community 155 - ".onLogout"
Cohesion: 0.11
Nodes (7): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Gaps, Inferences, Takeaway, ClientMapCache, Throw, ThrownFigures

### Community 158 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.13
Nodes (5): Queued, Shot, PlaceMusicClient, Track, PlaceMusicClientTest

### Community 160 - "CameraView.java"
Cohesion: 0.13
Nodes (4): Camera, CameraView, Chunks, ChunkMapAccess

### Community 163 - ".rightClick"
Cohesion: 0.24
Nodes (3): Entry, Menu, HornOrders

### Community 164 - "Kit"
Cohesion: 0.10
Nodes (20): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+12 more)

### Community 174 - "Settlement"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 181 - "ConquestWorldScreen"
Cohesion: 0.22
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 186 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 205 - "SailBlock.java"
Cohesion: 0.10
Nodes (4): ArtilleryBlock, Oar, OarBlock, SailBlock

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 217 - "net.minecraft.core.Vec3i"
Cohesion: 0.07
Nodes (5): StylePiece, CityPlacement, CountryPlacement, DojoPlacement, FarmPlacement

### Community 219 - "Rank"
Cohesion: 0.10
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 221 - ".of"
Cohesion: 0.20
Nodes (3): FactionBanner, Layer, FactionBannerTest

### Community 224 - "RecruitFigures.java"
Cohesion: 0.06
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 225 - ".roadStart"
Cohesion: 0.08
Nodes (6): StreetWalks, DistrictKey, Townsfolk, CitySizesTest, StreetWalksTest, PlaceMusicTest

### Community 226 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.06
Nodes (8): Palette, WallTemplates, Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 232 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 234 - "DistantHorizonsLink"
Cohesion: 0.19
Nodes (3): DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 236 - "Kind"
Cohesion: 0.16
Nodes (8): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Plan

### Community 237 - "net.minecraft.client.Minecraft"
Cohesion: 0.07
Nodes (6): ClientTownsfolk, Folk, DistantArmyRenderer, Rects, Walk, ClientTownsfolkTest

### Community 238 - "FieldArmy"
Cohesion: 0.04
Nodes (11): Point, FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock (+3 more)

### Community 255 - "HarbourCut"
Cohesion: 0.18
Nodes (3): Frame, Harbour, HarbourCutTest

### Community 256 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.04
Nodes (49): Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Character page, Characters (holders, skills, prowess, traits), Command Table block (+41 more)

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "Clash"
Cohesion: 0.24
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 266 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 272 - "BattleType"
Cohesion: 0.15
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 278 - "DayClockTest"
Cohesion: 0.25
Nodes (3): DayClock, Step, DayClockTest

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.13
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips, Ride

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "FaceOffScreen"
Cohesion: 0.21
Nodes (3): FaceOffScreen, OverMap, GarrisonOrder

### Community 293 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.10
Nodes (3): BattleWallBannerBlock, CommandTableBlock, HelmBlock

### Community 294 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.04
Nodes (14): BaseMapPart, BattleSnapshot, BattleSpot, BoardView, GrandStrategyView, PeerageView, TalkView, UnitInfo (+6 more)

### Community 295 - "State"
Cohesion: 0.40
Nodes (5): State, RALLIED, ROUTED, SHATTERED, STEADY

### Community 298 - "ChunkMapCameraMixin"
Cohesion: 0.16
Nodes (10): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway, Cited Findings, Cited Findings, ChunkMapCameraMixin (+2 more)

### Community 300 - "Doctrine"
Cohesion: 0.07
Nodes (21): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+13 more)

### Community 301 - "State"
Cohesion: 0.50
Nodes (4): State, CARRIED, RAISING, UP

### Community 302 - "Armoury"
Cohesion: 0.27
Nodes (3): Armoury, Outfit, Style

### Community 305 - "FactionMapScreen"
Cohesion: 0.09
Nodes (3): FactionMapScreen, PetitionScreen, AbstractSliderButton

### Community 306 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 308 - "rafasconquest.battle.FieldBlock"
Cohesion: 0.13
Nodes (6): Figure, Shape, LINE, SQUARE, V, WEDGE

### Community 311 - "GateBlockEntity"
Cohesion: 0.08
Nodes (9): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+1 more)

### Community 313 - "Machine"
Cohesion: 0.06
Nodes (17): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+9 more)

### Community 321 - "list"
Cohesion: 0.06
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 323 - "Lands"
Cohesion: 0.08
Nodes (9): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Lands, Claim, Event (+1 more)

### Community 325 - "Muster"
Cohesion: 0.67
Nodes (3): Muster, CEREMONIAL, OFFENSIVE

### Community 330 - "OarFigures"
Cohesion: 0.16
Nodes (3): Model, OarFigures, Rowing

### Community 362 - "DistantGeneration"
Cohesion: 0.32
Nodes (3): Inferences, Api, DistantGeneration

### Community 364 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.07
Nodes (28): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+20 more)

### Community 365 - "org.joml.Matrix4f"
Cohesion: 0.09
Nodes (15): DistantDepth, Arrow, ArrowVolleys, Kind, ARROW, BALL, BIG_BALL, BIG_ROCK (+7 more)

### Community 379 - ".officers"
Cohesion: 0.09
Nodes (14): GameCharacter, Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN (+6 more)

### Community 389 - "Cited Findings"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?

### Community 394 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 407 - "DistantOcclusion"
Cohesion: 0.06
Nodes (14): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Cited Findings, Gaps, Inferences, Takeaway, BattleSpotRing, DistantOcclusion (+6 more)

### Community 408 - "net.minecraft.world.level.block.Block"
Cohesion: 0.13
Nodes (3): GatePlanksBlock, QuestBoardBlock, Builder

### Community 411 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.50
Nodes (3): Gaps, How do world generation and structure placement work, and what do they cost?, Takeaway

## Ambiguous Edges - Review These
- `Roads (high, city, town roads; bridges; signposts)` → `Mustering (offensive and ceremonial)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **445 isolated node(s):** `ARCHER_MELEE`, `ARROWS`, `CAVALRY`, `INFANTRY`, `BEARER` (+440 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1450 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **252 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `Calibration`, `net.minecraft.world.level.ChunkPos`, `org.junit.jupiter.api.Test`, `.shift`, `Lands`, `RecruitsBattle.java`, `rafasconquest.battle.FieldBlock`, `UnitMode`, `BaseMap`, `PveCommands.java`, `BattleType`, `Work`, `net.minecraft.server.level.ServerPlayer`, `.enlist`, `net.neoforged.bus.api.SubscribeEvent`, `SiegeWorks`, `CameraView.java`, `PveCommands`, `.send`, `rafasconquest.faction.Faction`, `Arm`, `.map`, `.view`, `Phase`, `rafasconquest.battle.FieldBlock`, `Cited Findings`, `BattleMap.java`, `WarCouncil`, `net.minecraft.server.level.ServerLevel`, `UnitOrder`, `net.minecraft.world.item.ItemStack`, `GrappleHookRenderer.java`, `ConquestEvents.java`, `.tower`, `Cited Findings`, `rafasconquest.battle.BattlePlan.Point`, `net.minecraft.core.BlockPos`, `net.minecraft.server.level.ServerPlayer`, `.officers`?**
  _High betweenness centrality (0.053) - this node is a cross-community bridge._
- **What connects `ARCHER_MELEE`, `ARROWS`, `CAVALRY` to the rest of the system?**
  _445 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `list` be split into smaller, more focused modules?**
  _Cohesion score 0.09062980030721966 - nodes in this community are weakly interconnected._
- **Why does `BattleScreen` connect `BattleScreen` to `.rightClick`, `net.minecraft.client.gui.GuiGraphics`, `net.minecraft.client.Minecraft`, `.moraleOf`, `DistantOcclusion`, `net.minecraft.server.level.ServerPlayer`, `.onLogout`, `BattleSounds`, `net.minecraft.sounds.SoundEvent`?**
  _High betweenness centrality (0.040) - this node is a cross-community bridge._
- **Should `net.minecraft.world.level.ChunkPos` be split into smaller, more focused modules?**
  _Cohesion score 0.07917383820998279 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Roads (high, city, town roads; bridges; signposts)` and `Mustering (offensive and ceremonial)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._