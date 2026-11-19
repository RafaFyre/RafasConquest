# Graph Report - RafasConquest  (2026-11-18)

## Corpus Check
- 639 files · ~529,868 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 8280 nodes · 26858 edges · 416 communities (160 shown, 256 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3404 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `36fdc941`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- .measure
- ConquestContent
- net.minecraft.world.level.ChunkPos
- org.junit.jupiter.api.Test
- RecruitsBattle
- BattleScreen
- Front
- net.minecraft.world.level.Level
- SiegeWorks
- RafasConquest.java
- .plan
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- BattlePlan
- BaseMap
- PveCommands.java
- BattleMap.java
- Faction
- RafasConquest Project Site
- Person
- Arm
- net.minecraft.server.level.ServerPlayer
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- list
- .roadStart
- CityHud
- Tier
- rafasconquest.battle.BattlePlan.Arm
- .place
- ClientEvents
- FactionInfoTest.java
- .of
- CityManagerScreen
- BattleShips
- PveCommands
- .map
- CityPlan
- Land
- rafasconquest.battle.FormationSnapshot
- WaterSwell
- Site
- rafasconquest.faction.Faction
- org.joml.Matrix4f
- .onClientExtensions
- org.jetbrains.annotations.Nullable
- FlagBlock.java
- Personality
- SeaLanes
- .deployment
- .postChunk
- .distanceTo
- Kit
- net.minecraft.world.item.Item
- BoardScreen
- .render
- CityStyles
- ConquestSavedData
- ConquestNetwork
- CityTerrain
- BattleSounds
- HorizontalStretch
- TitleMap
- Bodies
- StyleLibrary
- WorldSettings
- War
- Row
- School
- DistantShips
- SoldierFigure
- MapLands
- WarCouncil
- CityPlacement.java
- net.minecraft.world.item.ItemStack
- RecruitFigures
- BaseMap
- GateBlockEntity
- Candidate
- GrappleHook
- Recruiter
- .fire
- net.minecraft.nbt.CompoundTag
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- SailBlock.java
- DialogueScreen
- Weapon
- net.minecraft.world.level.levelgen.structure.BoundingBox
- net.minecraft.world.level.block.state.BlockState
- RoadFeature.java
- CityLayout
- CityGridBuilder
- FactionInfoScreen
- NationScreen
- Quest
- FigureBatches
- Point
- net.minecraft.world.level.block.Block
- How Total War calculates battles between units
- MapContext
- GrandStrategy
- CityPanel
- .step
- .rightClick
- TechTreeScreen
- .touches
- .block
- Cited Findings
- .generate
- UnitOrder
- GrandStrategyScreen
- CapeCurio.java
- CityVariant
- ConquestConfig
- TechTreeScreen
- .deploy
- Kind
- DistantHorizonsArmies
- .order
- Terrain
- TitleOwnersScreen
- ProvinceMap
- .shift
- com.mojang.blaze3d.vertex.PoseStack
- Cloth
- Realm
- Ride
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .view
- CityLayout
- .view
- MarkerScreen
- Barracks
- CLAUDE.md
- Override
- ConquestEvents
- Duty
- net.minecraft.sounds.SoundEvent
- org.spongepowered.asm.mixin.Mixin
- ClientTownsfolk
- BattleWallBannerBlock
- Role
- StrategyRelief
- /pve debug commands
- Settlement
- ArtilleryScreen
- com.google.gson.JsonObject
- .onLogout
- CameraView
- net.minecraft.core.Vec3i
- .noble
- Build
- ArmySpawnScreen
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- OrganizationScreen
- TitleLayer
- RecruitFigures.java
- ChunkMapCameraMixin
- ArtilleryMount
- Roads
- .readPlacements
- RafasConquest.java
- .accept
- DistantArmyRenderer
- SableSeen
- ShipHulls
- MapContext
- CombatMode
- Trait
- Doctrine
- Body
- Call
- HorizontalStretch
- ConquestWorldScreen
- Profiling run sheet (baseline)
- WarCamp
- Missile
- .hash
- SeaCalm
- HarbourCut
- ClientPolitics
- Deflects
- DayClockTest
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- .plan
- .measure
- Politics
- GateScreen
- BattleKeepInventory
- Kind
- FaceOffScreen
- Way
- .findGenerationPoint
- NobleDistrictTest
- BackBanners
- CityVegetationMixin.java
- Sea
- .bark
- Job
- BlockGrid
- FactionMapScreen
- Phase
- SablePhysics
- BattleSnapshot
- SimpleChannel.java
- Machine
- Kind
- .draw
- Cited Findings
- Distribution
- Pre
- Layout
- .nationOf
- net.minecraft.world.entity.LivingEntity
- WallTemplates
- OarFigures
- RankKits
- Succession
- ClientModEvents
- Morale
- FieldBlock
- .declare
- ShareLiveTest
- BattleType
- .enlist
- SwingModes
- RecruitsBattle.java
- WarHorn
- ClientBarks
- ItemFlags
- ShipKind
- Garrison
- FormationSnapshotTest
- net.minecraft.server.level.ServerLevel

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 316 edges
2. `FieldBlock` - 232 edges
3. `FieldBattle` - 223 edges
4. `ConquestNetwork` - 137 edges
5. `FieldArmy` - 130 edges
6. `Point` - 120 edges
7. `Lands` - 94 edges
8. `BattleScreen` - 87 edges
9. `Siege` - 86 edges
10. `Politics` - 84 edges

## Surprising Connections (you probably didn't know these)
- `Takeaway` --references--> `RoadLayer`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `A tick budget and staggered schedules make the fixes stick` --references--> `BaseMap`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/map/BaseMap.java
- `Cited Findings` --references--> `CityVegetationMixin`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/mixin/CityVegetationMixin.java

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

## Communities (416 total, 256 thin omitted)

### Community 0 - ".measure"
Cohesion: 0.19
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 1 - "ConquestContent"
Cohesion: 0.04
Nodes (9): MarkerRenderer, BattleBannerBlockEntity, ConquestContent, Oar, Banner, PlacedBanners, Sail, MarkerBlockEntity (+1 more)

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.12
Nodes (3): CityRoadPiece, HarbourCutPiece, HarbourPiece

### Community 3 - "org.junit.jupiter.api.Test"
Cohesion: 0.06
Nodes (4): DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, OarRowingTest

### Community 4 - "RecruitsBattle"
Cohesion: 0.03
Nodes (33): Cited Findings, Camera, Afloat, Card, Enlisted, Figure, HornCall, ATTACKING (+25 more)

### Community 6 - "Front"
Cohesion: 0.13
Nodes (4): Feud, Lord, Feuds, Front

### Community 7 - "net.minecraft.world.level.Level"
Cohesion: 0.09
Nodes (4): GrandStrategyBlock, StrategyBoardBlock, WorldMapItem, MarkerBlock

### Community 8 - "SiegeWorks"
Cohesion: 0.12
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 9 - "RafasConquest.java"
Cohesion: 0.08
Nodes (3): GrappleHooks, ConquestEntities, RafasConquest

### Community 10 - ".plan"
Cohesion: 0.12
Nodes (6): Centre, TitleStyle, Faith, Bond, RealmPlanner, Seat

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 13 - "BaseMap"
Cohesion: 0.10
Nodes (8): 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom?, Gaps, Inferences, Takeaway, MapColors, PoliticalMap, LabelShape, PoliticalStyle

### Community 14 - "PveCommands.java"
Cohesion: 0.07
Nodes (32): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+24 more)

### Community 15 - "BattleMap.java"
Cohesion: 0.04
Nodes (9): Battle map developer web page, Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Enlisted players, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Morale, rout and rally, Battle roles (frontline, bait, support, flanker, kill squad), The war horn (+1 more)

### Community 16 - "Faction"
Cohesion: 0.09
Nodes (5): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction, FactionInfoTest

### Community 17 - "RafasConquest Project Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.15
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 19 - "Arm"
Cohesion: 0.08
Nodes (6): Arm, Kind, ENGINE, FOOT, MOUNTED, RANGED

### Community 20 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.09
Nodes (5): Place, CityManager, PlayerStats, View, PlaceMusic

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 22 - "list"
Cohesion: 0.05
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 23 - ".roadStart"
Cohesion: 0.08
Nodes (5): DistrictKey, Townsfolk, CitySizesTest, StreetWalksTest, PlaceMusicTest

### Community 24 - "CityHud"
Cohesion: 0.09
Nodes (3): CityHud, Spot, CityHudPreviewTest

### Community 25 - "Tier"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "rafasconquest.battle.BattlePlan.Arm"
Cohesion: 0.11
Nodes (7): FieldRules, Rates, Side, FLANK, FRONT, REAR, UnitCardsTest

### Community 27 - ".place"
Cohesion: 0.07
Nodes (8): Cited Findings, CityIndex, ConquestRuntime, RoadFeature, Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 29 - "FactionInfoTest.java"
Cohesion: 0.12
Nodes (11): FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, FactionInfo, PoiType (+3 more)

### Community 30 - ".of"
Cohesion: 0.16
Nodes (5): FactionBanner, Layer, FactionBannerNbt, CityBanners, FactionBannerTest

### Community 32 - "BattleShips"
Cohesion: 0.11
Nodes (7): Hull, Ramming, BattleShips, Helm, Ram, SableLink, RammingTest

### Community 34 - ".map"
Cohesion: 0.08
Nodes (10): Cited Findings, Army, Clash, Kind, DUEL, SKIRMISH, FeudDuel, MusterBars (+2 more)

### Community 35 - "CityPlan"
Cohesion: 0.14
Nodes (3): CityPlan, Piece, Plan

### Community 36 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 37 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.13
Nodes (6): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, DistantArmies

### Community 38 - "WaterSwell"
Cohesion: 0.14
Nodes (8): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Made, WaterSwell, SeaFoamTest

### Community 42 - "org.joml.Matrix4f"
Cohesion: 0.10
Nodes (14): Arrow, ArrowVolleys, Kind, ARROW, BALL, BIG_BALL, BIG_ROCK, BOLT (+6 more)

### Community 45 - "FlagBlock.java"
Cohesion: 0.08
Nodes (7): BattleBannerBlock, FlagBlock, Mount, BARS, BLOCK, FENCE, WALL

### Community 46 - "Personality"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.08
Nodes (9): Lane, Leg, Port, Post, SeaLanes, Ship, Size, Voyage (+1 more)

### Community 50 - ".postChunk"
Cohesion: 0.14
Nodes (4): Cited Findings, BarracksPeople, CityGuards, ConquestServer

### Community 51 - ".distanceTo"
Cohesion: 0.09
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 52 - "Kit"
Cohesion: 0.10
Nodes (20): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+12 more)

### Community 53 - "net.minecraft.world.item.Item"
Cohesion: 0.18
Nodes (7): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 55 - ".render"
Cohesion: 0.08
Nodes (8): CharacterScreen, Retainer, Spot, Banner, Portraits, Names, Honour, Honours

### Community 57 - "ConquestSavedData"
Cohesion: 0.06
Nodes (7): Cited Findings, ConquestSavedData, MapPreview, Poi, EconomyTick, PoiPrecomputer, ConquestSavedDataTest

### Community 58 - "ConquestNetwork"
Cohesion: 0.03
Nodes (46): ArtillerySet, Bark, BaseMapPart, BattleSpot, BoardAction, BoardView, CameraAt, ChargeHits (+38 more)

### Community 59 - "CityTerrain"
Cohesion: 0.07
Nodes (18): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+10 more)

### Community 60 - "BattleSounds"
Cohesion: 0.15
Nodes (6): BattleSounds, ChargeSound, DinSound, Queued, Shot, Source

### Community 61 - "HorizontalStretch"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 63 - "TitleMap"
Cohesion: 0.16
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 67 - "WorldSettings"
Cohesion: 0.12
Nodes (8): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, WorldSettings, CapitalRelocationTest, LayoutTest

### Community 71 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 72 - "DistantShips"
Cohesion: 0.14
Nodes (6): DistantShips, Far, HullKey, Riding, Want, DistantShipsTest

### Community 73 - "SoldierFigure"
Cohesion: 0.20
Nodes (7): Boxes, Palette, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 75 - "WarCouncil"
Cohesion: 0.15
Nodes (6): Facts, Officer, Said, Talk, WarCouncil, WarCouncilPagesTest

### Community 76 - "CityPlacement.java"
Cohesion: 0.10
Nodes (3): CountryPlacement, DojoPlacement, FarmPlacement

### Community 77 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.14
Nodes (4): AddonPieces, Armoury, Outfit, Style

### Community 78 - "RecruitFigures"
Cohesion: 0.09
Nodes (8): Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures, RecruitFiguresTest

### Community 79 - "BaseMap"
Cohesion: 0.14
Nodes (9): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, BaseMap, Nation, BaseMapStore (+1 more)

### Community 81 - "GateBlockEntity"
Cohesion: 0.07
Nodes (9): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+1 more)

### Community 82 - "Candidate"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 87 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.07
Nodes (3): SaveGuard, CityTerrainPiece, NoblePathPiece

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.08
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), Bounds, DistanceScaling, DistanceScalingTest

### Community 89 - "SailBlock.java"
Cohesion: 0.12
Nodes (3): ArtilleryBlock, OarBlock, SailBlock

### Community 91 - "Weapon"
Cohesion: 0.18
Nodes (9): Weapon, ARCBALLISTA, CULVERIN, HWACHA, MONS_MEG, ONAGER, SERPENTINE, TOWER_CROSSBOW (+1 more)

### Community 92 - "net.minecraft.world.level.levelgen.structure.BoundingBox"
Cohesion: 0.16
Nodes (3): CountryPiece, FarmPiece, PlaceholderPiece

### Community 94 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.11
Nodes (3): CommandTableBlock, GateBlock, HelmBlock

### Community 95 - "RoadFeature.java"
Cohesion: 0.04
Nodes (46): Ambushes from woods, Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Character page, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings) (+38 more)

### Community 96 - "CityLayout"
Cohesion: 0.09
Nodes (4): CityLayout, District, Site, CityLayoutTest

### Community 98 - "CityGridBuilder"
Cohesion: 0.10
Nodes (5): Houses, CityGridBuilder, Ground, PlacedWall, Platform

### Community 99 - "FactionInfoScreen"
Cohesion: 0.16
Nodes (3): FactionInfoScreen, Line, StructureLink

### Community 100 - "NationScreen"
Cohesion: 0.08
Nodes (5): CourtView, Figure, NationScreen, Demographics, Share

### Community 101 - "Quest"
Cohesion: 0.13
Nodes (9): Boards, Entry, View, Taken, Quest, Type, CLEAR, DELIVER (+1 more)

### Community 103 - "FigureBatches"
Cohesion: 0.07
Nodes (12): ShipSprite, Batch, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED (+4 more)

### Community 105 - "Point"
Cohesion: 0.04
Nodes (19): Point, Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate (+11 more)

### Community 106 - "net.minecraft.world.level.block.Block"
Cohesion: 0.14
Nodes (3): GatePlanksBlock, QuestBoardBlock, Builder

### Community 107 - "How Total War calculates battles between units"
Cohesion: 0.11
Nodes (17): 1. Combat is per soldier, not per unit, 1. Flank and rear shock, 2. Does the swing land?, 2. Nearby rout (fright), 3. Army-wide losses, 3. How much damage the hit does, 4. Charges, 4. Shattering (+9 more)

### Community 108 - "MapContext"
Cohesion: 0.10
Nodes (9): Cited Findings, BaseMapLayer, Hit, LabelLayer, MapContext, MapView, Place, PlaceMenuLayer (+1 more)

### Community 109 - "GrandStrategy"
Cohesion: 0.14
Nodes (3): Army, GrandStrategy, View

### Community 110 - "CityPanel"
Cohesion: 0.15
Nodes (6): CityPanel, Mode, DISMISS, MARCH, MUSTER, NONE

### Community 111 - ".step"
Cohesion: 0.08
Nodes (17): 1. How we avoid breaking anything, 2. Where the 10 November gameplan stands, 3. New costs since 10 November, 4. The stages, 5. Order and size, Core-function check after every stage (about 10 minutes in game), RafasConquest optimization plan (18 November 2026), Stage 0: measure (one jar, no behaviour change) (+9 more)

### Community 112 - ".rightClick"
Cohesion: 0.27
Nodes (3): Entry, Menu, HornOrders

### Community 113 - "TechTreeScreen"
Cohesion: 0.18
Nodes (4): TechTreeScreen, Tech, TechTree, TechTreeTest

### Community 116 - "Cited Findings"
Cohesion: 0.06
Nodes (12): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, BattleSpotRing, DistantOcclusion, Mode (+4 more)

### Community 117 - ".generate"
Cohesion: 0.06
Nodes (20): BaseMapGenerator, Heights, BiomePalette, Entry, Homeland, DESERT, JUNGLE, PLAINS (+12 more)

### Community 118 - "UnitOrder"
Cohesion: 0.11
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.08
Nodes (4): GrandStrategyScreen, Marcher, Spot, GrandStrategyDateTest

### Community 121 - "CapeCurio.java"
Cohesion: 0.19
Nodes (3): BackBannerLayer, CapeCurio, MouthLayer

### Community 122 - "CityVariant"
Cohesion: 0.16
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 124 - "TechTreeScreen"
Cohesion: 0.13
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 125 - ".deploy"
Cohesion: 0.13
Nodes (3): WarPlan, Officers, GameCharacter

### Community 126 - "Kind"
Cohesion: 0.13
Nodes (13): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HALBERDIERS, HEAVY_CAVALRY (+5 more)

### Community 127 - "DistantHorizonsArmies"
Cohesion: 0.05
Nodes (9): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+1 more)

### Community 128 - ".order"
Cohesion: 0.40
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

### Community 130 - "TitleOwnersScreen"
Cohesion: 0.21
Nodes (3): ClientPeerage, Owner, TitleOwnersScreen

### Community 131 - "ProvinceMap"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 132 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 133 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.05
Nodes (14): BannerArt, BattleBannerRenderer, Carried, ClothRenderer, Fabric, Far, Kind, Layer (+6 more)

### Community 134 - "Cloth"
Cohesion: 0.10
Nodes (5): Cloth, Solid, ClothTest, StretchTest, WoolTest

### Community 136 - "Realm"
Cohesion: 0.11
Nodes (4): Realm, Taking, RealmPlannerTest, World

### Community 137 - "Ride"
Cohesion: 0.14
Nodes (3): Ride, Swell, SwellTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.07
Nodes (28): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings (+20 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings (+28 more)

### Community 141 - ".view"
Cohesion: 0.07
Nodes (17): Grant, LandGrants, NobleTitle, COUNT, DUKE, EMPEROR, KING, KNIGHT (+9 more)

### Community 142 - "CityLayout"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 143 - ".view"
Cohesion: 0.07
Nodes (14): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+6 more)

### Community 149 - "Barracks"
Cohesion: 0.23
Nodes (3): Barracks, BarracksPiece, BarracksTest

### Community 152 - "Override"
Cohesion: 0.14
Nodes (4): BattleSite.Water, BattleSite.Water, SettingsScreen, Politics.Front

### Community 157 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 158 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 160 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.09
Nodes (13): BetterCombatOneHandMixin, ClientChunkWindowMixin, DhShaderLoaderMixin, DhTerrainShaderMixin, EpicKnightsCloakMixin, EpicKnightsJoustingMixin, EpicKnightsSlownessMixin, EpicKnightsTwoHandedMixin (+5 more)

### Community 162 - "ClientTownsfolk"
Cohesion: 0.18
Nodes (5): ClientTownsfolk, Folk, StreetWalks, Walk, ClientTownsfolkTest

### Community 164 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 174 - "Settlement"
Cohesion: 0.09
Nodes (11): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+3 more)

### Community 181 - ".onLogout"
Cohesion: 0.18
Nodes (3): ClientMapCache, Throw, ThrownFigures

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 217 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 219 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.05
Nodes (34): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+26 more)

### Community 224 - "RecruitFigures.java"
Cohesion: 0.07
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 226 - "ChunkMapCameraMixin"
Cohesion: 0.13
Nodes (11): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway, Cited Findings, Cited Findings, ArrowSaveMixin (+3 more)

### Community 232 - "Roads"
Cohesion: 0.11
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 234 - ".readPlacements"
Cohesion: 0.27
Nodes (3): Offsets, Placement, PlacementOffsetsTest

### Community 235 - "RafasConquest.java"
Cohesion: 0.05
Nodes (3): CityStructure, ConquestWorldgen, FarmStructure

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.05
Nodes (6): Cited Findings, DistantDepth, DistantArmyRenderer, Rects, FarFlags, Shape

### Community 240 - "ShipHulls"
Cohesion: 0.21
Nodes (3): BattleKind, Part, ShipHulls

### Community 242 - "MapContext"
Cohesion: 0.09
Nodes (14): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, LegendEntry (+6 more)

### Community 248 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 252 - "Doctrine"
Cohesion: 0.10
Nodes (16): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR, Strategy (+8 more)

### Community 254 - "Body"
Cohesion: 0.11
Nodes (18): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+10 more)

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 257 - "ConquestWorldScreen"
Cohesion: 0.24
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 262 - "WarCamp"
Cohesion: 0.09
Nodes (8): PlannedTent, Saved, WarCamp, CourtSet, Seat, Style, GateGeometry, GateGeometryTest

### Community 263 - "Missile"
Cohesion: 0.50
Nodes (4): Missile, ARROWS, NONE, SHOT

### Community 266 - ".hash"
Cohesion: 0.15
Nodes (4): NobleAnnex, OriginRoller, Roll, ProvinceNames

### Community 267 - "SeaCalm"
Cohesion: 0.18
Nodes (5): Ground, Depths, SeaCalm, Strengths, SeaCalmTest

### Community 271 - "HarbourCut"
Cohesion: 0.13
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 278 - "DayClockTest"
Cohesion: 0.25
Nodes (3): DayClock, Step, DayClockTest

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.18
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 283 - ".plan"
Cohesion: 0.11
Nodes (7): City, Grid, SeatCities, CountrysideTest, World, SeatCitiesTest, World

### Community 285 - "Politics"
Cohesion: 0.06
Nodes (13): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Lands, Stronghold, Claim (+5 more)

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 292 - "Way"
Cohesion: 0.25
Nodes (8): Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY, RUINS, SPARE_COUNTY_VASSAL

### Community 302 - "Job"
Cohesion: 0.08
Nodes (13): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+5 more)

### Community 305 - "FactionMapScreen"
Cohesion: 0.10
Nodes (3): FactionMapScreen, PetitionScreen, AbstractSliderButton

### Community 306 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 313 - "Machine"
Cohesion: 0.07
Nodes (6): Machine, Role, MANTLET, RAM, SHOOTER, TOWER

### Community 314 - "Kind"
Cohesion: 0.22
Nodes (7): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL

### Community 318 - "Cited Findings"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?

### Community 319 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 322 - "Layout"
Cohesion: 0.17
Nodes (3): GoldChurch, Layout, GoldChurchPiece

### Community 330 - "OarFigures"
Cohesion: 0.17
Nodes (3): Model, OarFigures, Rowing

### Community 336 - "Succession"
Cohesion: 0.10
Nodes (9): Hook, Kind, FAVOUR, SECRET, Kind, FRIEND, RIVAL, Succession (+1 more)

### Community 350 - "Morale"
Cohesion: 0.28
Nodes (6): Morale, State, RALLIED, ROUTED, SHATTERED, STEADY

### Community 355 - "FieldBlock"
Cohesion: 0.04
Nodes (8): The core change: morale becomes a sum, FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 371 - "BattleType"
Cohesion: 0.15
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 379 - ".enlist"
Cohesion: 0.04
Nodes (9): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT, GeneralTask (+1 more)

### Community 395 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, GUNSHIP, SHOCK, SWORDSMEN

### Community 418 - "FormationSnapshotTest"
Cohesion: 0.14
Nodes (7): Cited Findings, Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 431 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.09
Nodes (9): ChunkWait, Wait, Land, Result, SiegeSurvey, Wall, CityGates, Done (+1 more)

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **430 isolated node(s):** `FOOT`, `RANGED`, `MOUNTED`, `ENGINE`, `NONE` (+425 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1477 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **256 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `net.minecraft.world.level.ChunkPos`, `org.junit.jupiter.api.Test`, `RecruitsBattle.java`, `WarCamp`, `SiegeWorks`, `.view`, `PveCommands.java`, `BattleMap.java`, `net.minecraft.server.level.ServerPlayer`, `ConquestEvents`, `PveCommands`, `.map`, `org.jetbrains.annotations.Nullable`, `org.jetbrains.annotations.Nullable`, `.bark`, `net.minecraft.server.level.ServerLevel`, `Phase`, `Pre`, `.sweep`, `WarCouncil`, `net.minecraft.world.item.ItemStack`, `GrappleHook`, `FieldBlock`, `Point`, `GrandStrategy`, `BattleType`, `.block`, `.generate`, `UnitOrder`, `.isOurs`, `.enlist`, `.deploy`?**
  _High betweenness centrality (0.081) - this node is a cross-community bridge._
- **What connects `FOOT`, `RANGED`, `MOUNTED` to the rest of the system?**
  _430 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `ConquestContent` be split into smaller, more focused modules?**
  _Cohesion score 0.042901234567901236 - nodes in this community are weakly interconnected._
- **Why does `FieldBattle` connect `FieldBlock` to `Bodies`, `org.junit.jupiter.api.Test`, `RecruitsBattle`, `Point`, `BattlePlan`, `ShipHulls.java`, `PveCommands.java`, `.block`, `BattleType`, `.distanceTo`, `list`, `Machine`, `rafasconquest.battle.BattlePlan.Arm`, `.enlist`, `.deploy`, `Morale`, `Call`?**
  _High betweenness centrality (0.030) - this node is a cross-community bridge._
- **Should `net.minecraft.world.level.ChunkPos` be split into smaller, more focused modules?**
  _Cohesion score 0.11564625850340136 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._