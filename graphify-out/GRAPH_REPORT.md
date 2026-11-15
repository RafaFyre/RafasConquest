# Graph Report - RafasConquest  (2026-11-15)

## Corpus Check
- 560 files · ~490,116 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7733 nodes · 24929 edges · 393 communities (146 shown, 247 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3219 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `ea1488e3`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- .measure
- Lands
- net.minecraft.nbt.CompoundTag
- .army
- Politics
- BattleScreen
- DistantHorizonsArmies.java
- CityGridBuilder
- RecruitsBattle
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- Faction
- BaseMap
- rafasconquest.map.BaseMap
- .siege
- PoliticsTest
- Battle Map Web Page & Site
- Person
- CityLayout
- .generate
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- Succession
- Recruiter
- CityManagerScreen
- .plan
- .step
- Lands
- WarCouncil
- net.minecraft.world.level.block.HorizontalDirectionalBlock
- .place
- Land
- BattleShips
- PveCommands Registration
- ClothRenderer
- CityPlan
- net.minecraft.core.BlockPos
- Machine
- Ride
- org.jetbrains.annotations.Nullable
- rafasconquest.faction.Faction
- .view
- CapeCurio.java
- CityVariant
- rafasconquest.map.BaseMap
- com.mojang.blaze3d.vertex.PoseStack
- Personalities
- SeaLanes
- ConquestNetwork
- ConquestSavedData
- BattlePlan
- net.minecraft.world.phys.Vec3
- .postChunk
- Barracks
- .render
- CityStyles
- net.minecraft.world.item.ItemStack
- CityLayout
- CityTerrain
- BattleSounds
- AgeTest.java
- TitleMapTest
- Bodies
- StyleLibrary
- How do world generation and structure placement work, and what do they cost?
- WorldSettings
- RafasConquest.java
- Row
- .generate
- net.minecraft.world.item.DyeColor
- Ride
- MapLands
- .findGenerationPoint
- Arm
- Roads
- GatePlanksBlock.java
- Tier
- net.minecraft.server.level.ServerLevel
- Ownership & Territory
- .collect
- .layout
- .moraleOf
- .passOnOrders
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- ClientScreens
- DialogueScreen
- Wind
- Poi
- CityManagerScreen
- SiegeWorks
- Cited Findings
- .render
- FactionInfoScreen
- CourtView
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Cloth
- FigureBatches
- net.minecraft.core.BlockPos
- .block
- How Total War calculates battles between units
- .tick
- .readPlacements
- CityPanel
- RecruitsBattle
- .plan
- RoadNetwork
- MarkerScreen
- Body
- TechTreeScreen
- Duty
- Override
- GrandStrategyScreen
- Peerage
- Settlement
- RecruitsBattle.java
- .board
- .place
- Hull
- CityTerrainPiece
- .view
- TitleOwnersScreen
- ProvinceMap
- .shift
- Kit
- SeaCalm
- DayClockTest
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- BackBanners
- Ranks
- net.minecraft.world.level.block.entity.BlockEntity
- ClientPolitics
- MapView
- CLAUDE.md
- MapContext
- .build
- HorizontalStretch
- RafasConquest.java
- .render
- ChunkWait.java
- .of
- RecruitFigures
- StrategyRelief
- Small Cluster 170
- Terrain
- RafasConquest (PvE addon for Villager Recruits)
- net.neoforged.bus.api.SubscribeEvent
- Characters (holders, skills, prowess, traits)
- Call
- .view
- .noble
- net.minecraft.world.level.block.Rotation
- .roadStart
- MarkerSpec
- CourtSet
- ConquestWorldScreen
- net.minecraft.server.level.ServerLevel
- .bark
- UnitOrder
- Cited Findings
- .boundsAt
- Phase
- Kind
- net.minecraft.client.Minecraft
- Task
- Build
- ConquestGameRules
- .rightClick
- ClientModEvents.java
- PoliticsData
- HarbourCut
- CityPlacement
- BattleType
- Profiling run sheet (baseline)
- .measure
- net.minecraft.sounds.SoundEvent
- .findGenerationPoint
- State
- Peerage
- net.minecraft.world.level.Level
- ShipKind
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- Sea.java
- net.minecraft.world.entity.Entity
- net.minecraft.world.item.Item
- Grade
- Kind
- .offTheBack
- Kind
- .hash
- OrganizationScreen
- .onLevelLoad
- .touches
- Doctrine
- .nationOf
- RankKits
- WallTemplates
- net.minecraft.world.level.block.state.BlockState
- NobleDistrictTest
- .roadStart
- ItemFlags.java
- rafasconquest.battle.BattlePlan.Point
- Striker
- ShareLiveTest
- PveCommands.java
- Kind
- list
- UnitMode
- .findGenerationPoint
- org.jetbrains.annotations.Nullable
- .choose
- .load
- OarFigures
- net.minecraft.server.level.ServerPlayer
- .officers
- BattleKeepInventory
- SwingModes
- BoardScreen
- .at
- BattleSnapshot
- ArtilleryScreen
- Kind
- ClientBarks
- .onLogout
- .accept

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 308 edges
2. `FieldBlock` - 222 edges
3. `FieldBattle` - 209 edges
4. `FieldArmy` - 127 edges
5. `ConquestNetwork` - 123 edges
6. `Lands` - 89 edges
7. `BattleScreen` - 86 edges
8. `Siege` - 82 edges
9. `Politics` - 79 edges
10. `BaseMap` - 71 edges

## Surprising Connections (you probably didn't know these)
- `A tick budget and staggered schedules make the fixes stick` --references--> `BaseMap`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/map/BaseMap.java
- `Cited Findings` --references--> `MapView`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/MapView.java
- `Every frame pays for disk listings, per-rectangle flushes and unculled soldiers` --references--> `TitleLayer`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/client/map/TitleLayer.java

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

## Communities (393 total, 247 thin omitted)

### Community 0 - ".measure"
Cohesion: 0.30
Nodes (3): Calibration, Fit, CalibrationTest

### Community 1 - "Lands"
Cohesion: 0.09
Nodes (8): Clash, Feud, Kind, DUEL, SKIRMISH, Lord, FeudDuel, Feuds

### Community 2 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.07
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - ".army"
Cohesion: 0.07
Nodes (6): DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, SwingModesTest, GrandStrategyDateTest, OarRowingTest

### Community 4 - "Politics"
Cohesion: 0.09
Nodes (10): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Front, Muster, CEREMONIAL (+2 more)

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.07
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "CityGridBuilder"
Cohesion: 0.11
Nodes (5): Houses, CityGridBuilder, Ground, PlacedWall, Platform

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (46): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+38 more)

### Community 12 - "Faction"
Cohesion: 0.07
Nodes (8): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction, ResourceType, FOOD, EconomyTick, ConquestSavedDataTest

### Community 13 - "BaseMap"
Cohesion: 0.07
Nodes (10): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, BaseMap, Nation, BaseMapStore (+2 more)

### Community 14 - "rafasconquest.map.BaseMap"
Cohesion: 0.07
Nodes (5): Army, Goal, PoliticsSync, War, Wars

### Community 17 - "Battle Map Web Page & Site"
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

### Community 22 - "Succession"
Cohesion: 0.08
Nodes (10): Hook, Kind, FRIEND, RIVAL, Realm, Succession, Taking, RealmPlannerTest (+2 more)

### Community 24 - "CityManagerScreen"
Cohesion: 0.09
Nodes (3): CityHud, Spot, CityHudPreviewTest

### Community 25 - ".plan"
Cohesion: 0.17
Nodes (6): Nation, Nations, Plan, Seats, NationsTest, World

### Community 26 - ".step"
Cohesion: 0.14
Nodes (5): FigureMotion, Match, Placed, State, FigureMotionTest

### Community 27 - "Lands"
Cohesion: 0.12
Nodes (4): Cited Findings, Lands, Stronghold, Claim

### Community 28 - "WarCouncil"
Cohesion: 0.14
Nodes (6): Facts, Officer, Said, Talk, WarCouncil, WarCouncilPagesTest

### Community 29 - "net.minecraft.world.level.block.HorizontalDirectionalBlock"
Cohesion: 0.10
Nodes (4): ArtilleryBlock, OarBlock, QuestBoardBlock, SailBlock

### Community 30 - ".place"
Cohesion: 0.09
Nodes (9): Point, BlockGrid, Ground, GroundPlan, GroundPlanTest, Land, Land, Land (+1 more)

### Community 31 - "Land"
Cohesion: 0.17
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 32 - "BattleShips"
Cohesion: 0.09
Nodes (5): BattleShips, GateDoors, Helm, Ram, SableLink

### Community 34 - "ClothRenderer"
Cohesion: 0.15
Nodes (7): BannerArt, ClothRenderer, Fabric, Far, Kind, Layer, Live

### Community 35 - "CityPlan"
Cohesion: 0.14
Nodes (3): CityPlan, Piece, Plan

### Community 36 - "net.minecraft.core.BlockPos"
Cohesion: 0.07
Nodes (5): GateGeometry, CharacterMarkers, Placed, StylePiece, GateGeometryTest

### Community 37 - "Machine"
Cohesion: 0.09
Nodes (12): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+4 more)

### Community 38 - "Ride"
Cohesion: 0.11
Nodes (8): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Made, WaterSwell, SeaFoamTest

### Community 39 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.04
Nodes (7): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 41 - ".view"
Cohesion: 0.08
Nodes (14): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+6 more)

### Community 42 - "CapeCurio.java"
Cohesion: 0.11
Nodes (5): BackBannerLayer, CapeCurio, ClothItemRenderer, Carried, MouthLayer

### Community 43 - "CityVariant"
Cohesion: 0.15
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 45 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.08
Nodes (5): BattleBannerRenderer, GateRenderer, GrappleHookRenderer, MarkerRenderer, GrappleHook

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.14
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "ConquestNetwork"
Cohesion: 0.02
Nodes (43): ArtillerySet, BackBanner, Bark, BaseMapPart, BattleSpot, BoardAction, BoardView, CameraAt (+35 more)

### Community 50 - "ConquestSavedData"
Cohesion: 0.05
Nodes (11): ConquestSavedData, SaveGuard, FactionType, BANDIT, CITY_STATE, EMPIRE, FactionInfo, PoiType (+3 more)

### Community 51 - "BattlePlan"
Cohesion: 0.05
Nodes (10): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, Outcome (+2 more)

### Community 52 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.10
Nodes (9): Camera, Card, Enlisted, Shape, LINE, SQUARE, V, WEDGE (+1 more)

### Community 53 - ".postChunk"
Cohesion: 0.08
Nodes (7): Cited Findings, BarracksPeople, CityBanners, CityGuards, ConquestServer, StructureLink, PoiPrecomputer

### Community 55 - ".render"
Cohesion: 0.12
Nodes (6): CharacterScreen, Spot, Banner, Portraits, Honour, Honours

### Community 57 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.15
Nodes (4): WarHorn, Armoury, Outfit, Style

### Community 58 - "CityLayout"
Cohesion: 0.11
Nodes (6): CityLayout, District, Site, SlopeTile, WorldSettings, CityLayoutTest

### Community 59 - "CityTerrain"
Cohesion: 0.06
Nodes (18): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+10 more)

### Community 60 - "BattleSounds"
Cohesion: 0.13
Nodes (6): Cited Findings, BattleSounds, ChargeSound, DinSound, Source, DistantArmies

### Community 61 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 63 - "TitleMapTest"
Cohesion: 0.16
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 66 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.32
Nodes (3): Inferences, Api, DistantGeneration

### Community 67 - "WorldSettings"
Cohesion: 0.18
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 71 - ".generate"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 72 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.11
Nodes (9): DistantShips, Far, HullKey, Riding, Want, BattleKind, Part, ShipHulls (+1 more)

### Community 73 - "Ride"
Cohesion: 0.22
Nodes (3): Ride, Swell, SwellTest

### Community 76 - "Arm"
Cohesion: 0.08
Nodes (16): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+8 more)

### Community 77 - "Roads"
Cohesion: 0.21
Nodes (5): Link, Place, Roads, Spur, RoadsTest

### Community 79 - "Tier"
Cohesion: 0.08
Nodes (8): Countryside, Plan, Grid, SeatCities, CountrysideTest, World, SeatCitiesTest, World

### Community 81 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.20
Nodes (5): Land, Result, SiegeSurvey, Wall, SiegeSurveyTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 84 - ".layout"
Cohesion: 0.14
Nodes (7): Cited Findings, Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 85 - ".moraleOf"
Cohesion: 0.16
Nodes (8): 1. Flank and rear shock, 2. Nearby rout (fright), 3. Army-wide losses, 4. Shattering, Design: adding the four morale shocks (built 2026-11-11, untested in game), Tests (FieldBattleTest), The core change: morale becomes a sum, Morale

### Community 95 - "SiegeWorks"
Cohesion: 0.08
Nodes (11): State, CARRIED, RAISING, UP, Work, SiegeWorks, Swing, CLOSED (+3 more)

### Community 96 - "Cited Findings"
Cohesion: 0.13
Nodes (13): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+5 more)

### Community 100 - "CourtView"
Cohesion: 0.07
Nodes (6): Retainer, ClientPeerage, Camera, CourtView, Figure, NationScreen

### Community 101 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.07
Nodes (28): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+20 more)

### Community 102 - "Cloth"
Cohesion: 0.13
Nodes (4): Cloth, Solid, ClothTest, WoolTest

### Community 103 - "FigureBatches"
Cohesion: 0.08
Nodes (12): ShipSprite, Batch, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED (+4 more)

### Community 107 - "How Total War calculates battles between units"
Cohesion: 0.17
Nodes (11): 1. Combat is per soldier, not per unit, 2. Does the swing land?, 3. How much damage the hit does, 4. Charges, 5. Missiles, 6. Leadership (morale): what actually wins battles, 7. Autoresolve, 8. Older games (Shogun 1, Medieval 1, Rome 1) (+3 more)

### Community 109 - ".readPlacements"
Cohesion: 0.27
Nodes (3): Offsets, Placement, PlacementOffsetsTest

### Community 110 - "CityPanel"
Cohesion: 0.19
Nodes (6): CityPanel, Mode, DISMISS, MARCH, MUSTER, NONE

### Community 111 - "RecruitsBattle"
Cohesion: 0.05
Nodes (15): HornCall, ATTACKING, MOVING, RALLYING, RETREATING, Live, Officers, RecruitsBattle (+7 more)

### Community 112 - ".plan"
Cohesion: 0.11
Nodes (7): TitleStyle, Demographics, Share, Faith, Bond, RealmPlanner, Seat

### Community 113 - "RoadNetwork"
Cohesion: 0.15
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 115 - "Body"
Cohesion: 0.09
Nodes (26): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+18 more)

### Community 117 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 118 - "Override"
Cohesion: 0.12
Nodes (4): Chunks, BattleSite.Water, BattleSite.Water, Politics.Front

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.09
Nodes (4): GrandStrategyScreen, Spot, City, CityIndex

### Community 121 - "Peerage"
Cohesion: 0.11
Nodes (12): Advancement, Boards, Entry, View, Merit, Peer, Taken, Quest (+4 more)

### Community 122 - "Settlement"
Cohesion: 0.19
Nodes (3): Settlement, CountryIndex, CountryStructure

### Community 124 - ".board"
Cohesion: 0.13
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 126 - ".place"
Cohesion: 0.14
Nodes (5): Cited Findings, Gaps, How do world generation and structure placement work, and what do they cost?, Takeaway, RoadFeature

### Community 127 - "Hull"
Cohesion: 0.30
Nodes (3): Hull, Ramming, RammingTest

### Community 129 - ".view"
Cohesion: 0.11
Nodes (9): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, Garrison, GrandStrategy, View (+1 more)

### Community 131 - "ProvinceMap"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 132 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 133 - "Kit"
Cohesion: 0.11
Nodes (19): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+11 more)

### Community 134 - "SeaCalm"
Cohesion: 0.18
Nodes (5): Ground, Depths, SeaCalm, Strengths, SeaCalmTest

### Community 136 - "DayClockTest"
Cohesion: 0.20
Nodes (3): DayClock, Step, DayClockTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.06
Nodes (32): 10. Overall ranking, and areas checked that look fine, 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings (+24 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings (+28 more)

### Community 142 - "Ranks"
Cohesion: 0.08
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 143 - "net.minecraft.world.level.block.entity.BlockEntity"
Cohesion: 0.05
Nodes (7): BattleBannerBlockEntity, ConquestContent, Door, GateBlockEntity, Hinge, Oar, Sail

### Community 149 - "MapView"
Cohesion: 0.08
Nodes (8): FactionMapScreen, PetitionScreen, AbstractSliderButton, Tier, COUNTY, DUCHY, EMPIRE, KINGDOM

### Community 152 - "MapContext"
Cohesion: 0.07
Nodes (16): Cited Findings, BaseMapLayer, Hit, LabelLayer, MapContext, LegendEntry, MapLayer, Symbol (+8 more)

### Community 158 - ".render"
Cohesion: 0.18
Nodes (5): ClientTownsfolk, Folk, StreetWalks, Walk, ClientTownsfolkTest

### Community 163 - ".of"
Cohesion: 0.18
Nodes (4): FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 164 - "RecruitFigures"
Cohesion: 0.10
Nodes (8): Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures, RecruitFiguresTest

### Community 175 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.17
Nodes (8): Arrows in flight (figure volleys), Battle sounds, Recruits PvE Addon Design Doc (PDF), Seeing the whole field (distant flat figures), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 181 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.04
Nodes (46): Ambushes from woods, Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Character page, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings) (+38 more)

### Community 186 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 205 - ".view"
Cohesion: 0.19
Nodes (4): Option, Talk, View, DialogueTest

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 221 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 223 - "ConquestWorldScreen"
Cohesion: 0.22
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 225 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.04
Nodes (7): GrappleHooks, ConquestEntities, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 228 - "UnitOrder"
Cohesion: 0.11
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 232 - "Cited Findings"
Cohesion: 0.07
Nodes (10): Cited Findings, Arrow, ArrowVolleys, BattleSpotRing, DistantOcclusion, Mode, OFF, ON (+2 more)

### Community 234 - ".boundsAt"
Cohesion: 0.35
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 235 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 236 - "Kind"
Cohesion: 0.22
Nodes (7): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL

### Community 237 - "net.minecraft.client.Minecraft"
Cohesion: 0.07
Nodes (5): Cited Findings, DistantDepth, DistantArmyRenderer, Figure, Rects

### Community 238 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 242 - "Build"
Cohesion: 0.29
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 248 - ".rightClick"
Cohesion: 0.20
Nodes (3): Entry, Menu, HornOrders

### Community 252 - "ClientModEvents.java"
Cohesion: 0.11
Nodes (3): ClientModEvents, net.neoforged.neoforge.client.extensions.common.IClientItemExtensions, Registration

### Community 257 - "BattleType"
Cohesion: 0.22
Nodes (5): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 267 - "State"
Cohesion: 0.40
Nodes (5): State, RALLIED, ROUTED, SHATTERED, STEADY

### Community 271 - "Peerage"
Cohesion: 0.09
Nodes (11): Names, NobleTitle, COUNT, DUKE, EMPEROR, KING, KNIGHT, NONE (+3 more)

### Community 274 - "net.minecraft.world.level.Level"
Cohesion: 0.07
Nodes (5): CommandTableBlock, GateBlock, GrandStrategyBlock, StrategyBoardBlock, WorldMapItem

### Community 277 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.18
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 285 - "net.minecraft.world.item.Item"
Cohesion: 0.16
Nodes (7): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 288 - "Grade"
Cohesion: 0.16
Nodes (9): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Plan (+1 more)

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 292 - "Kind"
Cohesion: 0.13
Nodes (12): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY (+4 more)

### Community 293 - ".hash"
Cohesion: 0.12
Nodes (6): ChunkCoord, FarmLayout, NobleAnnex, OriginRoller, Roll, SeedHash

### Community 300 - "Doctrine"
Cohesion: 0.06
Nodes (25): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender, Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT (+17 more)

### Community 306 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (9): Saved, BattleBannerBlock, BattleWallBannerBlock, HelmBlock, Builder, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER (+1 more)

### Community 310 - ".roadStart"
Cohesion: 0.12
Nodes (4): DistrictKey, Townsfolk, CitySizesTest, StreetWalksTest

### Community 313 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.04
Nodes (19): Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate, GateAction (+11 more)

### Community 314 - "Striker"
Cohesion: 0.33
Nodes (5): Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY

### Community 318 - "PveCommands.java"
Cohesion: 0.08
Nodes (34): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+26 more)

### Community 319 - "Kind"
Cohesion: 0.33
Nodes (6): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER

### Community 321 - "list"
Cohesion: 0.06
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 323 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 327 - ".choose"
Cohesion: 0.16
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 330 - "OarFigures"
Cohesion: 0.12
Nodes (5): Model, OarFigures, Rowing, Columns, SableSeen

### Community 336 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.06
Nodes (20): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway, Cited Findings, Cited Findings, CameraView (+12 more)

### Community 344 - ".officers"
Cohesion: 0.09
Nodes (14): GameCharacter, Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN (+6 more)

### Community 398 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 407 - ".onLogout"
Cohesion: 0.12
Nodes (7): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Gaps, Inferences, Takeaway, ClientMapCache, Throw, ThrownFigures

### Community 412 - ".accept"
Cohesion: 0.18
Nodes (3): Ranks, Offer, Recruitment

## Ambiguous Edges - Review These
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **422 isolated node(s):** `ARCHER_MELEE`, `ARROWS`, `CAVALRY`, `INFANTRY`, `NO_LIGHTMAP` (+417 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1406 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **247 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `net.minecraft.nbt.CompoundTag`, `.army`, `Politics`, `BattleSnapshot`, `RecruitsBattle`, `rafasconquest.map.BaseMap`, `net.minecraft.world.entity.Entity`, `WarCouncil`, `RafasConquest.java`, `ChunkWait.java`, `PveCommands Registration`, `BattleShips`, `rafasconquest.faction.Faction`, `com.mojang.blaze3d.vertex.PoseStack`, `ConquestNetwork`, `net.neoforged.bus.api.SubscribeEvent`, `net.minecraft.world.phys.Vec3`, `rafasconquest.battle.BattlePlan.Point`, `net.minecraft.world.item.ItemStack`, `Striker`, `UnitMode`, `org.jetbrains.annotations.Nullable`, `RafasConquest.java`, `net.minecraft.server.level.ServerPlayer`, `net.minecraft.server.level.ServerLevel`, `.passOnOrders`, `.officers`, `SiegeWorks`, `net.minecraft.server.level.ServerLevel`, `UnitOrder`, `net.minecraft.core.BlockPos`, `.block`, `Phase`, `Override`, `RecruitsBattle.java`?**
  _High betweenness centrality (0.076) - this node is a cross-community bridge._
- **What connects `ARCHER_MELEE`, `ARROWS`, `CAVALRY` to the rest of the system?**
  _422 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Lands` be split into smaller, more focused modules?**
  _Cohesion score 0.09234972677595628 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `ProvinceMap`, `Politics`, `arraylist`, `rafasconquest.map.BaseMap`, `.generate`, `MapView`, `Succession`, `.onLogout`, `WarCouncil`, `RafasConquest.java`, `OrganizationScreen`, `StrategyRelief`, `.onLevelLoad`, `.postChunk`, `Barracks`, `.render`, `net.minecraft.world.item.ItemStack`, `.generate`, `Tier`, `Cited Findings`, `net.minecraft.server.level.ServerLevel`, `.plan`, `GrandStrategyScreen`, `Settlement`, `.place`?**
  _High betweenness centrality (0.032) - this node is a cross-community bridge._
- **Should `net.minecraft.nbt.CompoundTag` be split into smaller, more focused modules?**
  _Cohesion score 0.06523005241700641 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._