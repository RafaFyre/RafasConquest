# Graph Report - RafasConquest  (2026-11-19)

## Corpus Check
- 649 files · ~548,720 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 8416 nodes · 27317 edges · 430 communities (160 shown, 270 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3443 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `2a89d4a9`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- .measure
- net.minecraft.world.level.block.entity.BlockEntity
- net.minecraft.nbt.CompoundTag
- org.junit.jupiter.api.Test
- org.joml.Vector3d
- BattleScreen
- Front
- net.minecraft.world.phys.Vec3
- rafasconquest.battle.FieldBlock
- net.minecraft.world.phys.Vec3
- ConquestEvents
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- Arm
- Siege
- PveCommands.java
- BattleMap.java
- Morale
- RafasConquest Project Site
- Person
- RecruitsBattle
- net.minecraft.server.level.ServerPlayer
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- list
- .settings
- CityHud
- Tier
- net.minecraft.core.BlockPos
- RoadNetwork
- net.minecraft.core.Direction
- ConquestEvents.java
- .blocksPlacedByHandBeforeTheBattleKeepTheirPlaces
- Override
- SableLink
- PveCommands
- .map
- CityPlan
- FlagBlock.java
- .view
- WaterSwell
- Site
- rafasconquest.faction.Faction
- net.minecraft.client.gui.GuiGraphics
- .plan
- SiegeWorks
- ArrowVolleys
- FactionInfo.java
- Personality
- SeaLanes
- War
- .postChunk
- BattleKeepInventory.java
- Kit
- .onRenderLevel
- .render
- CityStyles
- RecruitsBattle.java
- Land
- CityTerrain
- .siege
- HorizontalStretch
- WorldSettings
- TitleMap
- Bodies
- StyleLibrary
- CityLayout
- BattlePlan
- Row
- School
- DistantShips
- Homeland
- MapLands
- WarCouncil
- .step
- net.minecraft.world.item.ItemStack
- RecruitFigures
- BaseMap
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- Candidate
- Recruiter
- ClientPolitics
- CityTerrainPiece
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- Quests
- DialogueScreen
- .saveReloadRoundTripIsIdentical
- Duty
- GatePlanksBlock.java
- .of
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Point
- FactionInfoScreen
- CourtView
- net.minecraft.network.RegistryFriendlyByteBuf
- BattleType
- Peerage
- FaceOffScreen
- How Total War calculates battles between units
- MapContext
- Garrison
- CityPanel
- NationScreen
- ClientEvents
- Clash
- Front
- .block
- ClothRenderer
- .place
- UnitOrder
- GrandStrategyScreen
- Cloth
- CityManagerScreen
- TechTreeScreen
- GrappleHook
- DistantHorizonsArmies
- TitleLayer
- MapLayer
- Terrain
- TitleOwnersScreen
- Size
- .shift
- MediumCannonEntity
- .wallSegments
- ConquestWorldScreen
- ShipHulls
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .choose
- Faction
- net.minecraft.client.model.geom.ModelPart
- MarkerScreen
- Barracks
- CLAUDE.md
- Chunks
- .accept
- net.minecraft.world.level.block.state.BlockState
- Call
- net.minecraft.sounds.SoundEvent
- org.spongepowered.asm.mixin.Mixin
- PoliticsData
- Countryside
- GrappleHooks.java
- StrategyRelief
- /pve debug commands
- ConquestNetwork
- com.google.gson.JsonObject
- Phase
- HarbourCut
- .noble
- .hash
- ArmySpawnScreen
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Settlement
- RecruitFigures.java
- GrandStrategy
- Duty
- BattleWallBannerBlock
- SeatCitiesTest
- Roads
- BattleSnapshot
- .encode
- .onLevelLoad
- DistantArmyRenderer
- Body
- BackBannerLayer.java
- .touches
- CombatMode
- Trait
- ClientModEvents
- .bark
- GateScreen
- ProvinceMap
- HorizontalStretch
- Deflects.java
- Profiling run sheet (baseline)
- .fire
- .at
- Doctrine
- Task
- SeaCalm
- ConquestGameRules
- .onLogout
- .candidateChunk
- DayClockTest
- CityPlacement
- City
- .measure
- net.minecraft.client.Minecraft
- Ride
- Job
- Kind
- World map (CK3-style political map)
- DistantGeneration
- ConquestConfig
- CityGridBuilder
- Sea
- .generate
- How do world generation and structure placement work, and what do they cost?
- SablePhysics
- BlockGrid
- NetworkDirection
- GoldChurch.java
- FactionMapScreen
- BoardScreen
- CannonShots
- .findGenerationPoint
- net.minecraft.client.renderer.MultiBufferSource
- Machine
- Kind
- .draw
- State
- Quest
- WarCamp.java
- Build
- WeaponSkills
- net.minecraft.world.level.block.Block
- WallTemplates
- OarFigures
- .world
- Cities (grid, plots, vanilla village buildings)
- ShareLiveTest
- FormationSnapshot
- FieldBlock
- Order
- Piece
- SwingModes
- ClientBarks
- ShipKind
- 10. Overall ranking, and areas checked that look fine
- net.minecraft.core.Direction

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 322 edges
2. `FieldBlock` - 233 edges
3. `FieldBattle` - 224 edges
4. `ConquestNetwork` - 143 edges
5. `FieldArmy` - 131 edges
6. `Point` - 123 edges
7. `Lands` - 94 edges
8. `BattleScreen` - 88 edges
9. `Siege` - 86 edges
10. `Politics` - 84 edges

## Surprising Connections (you probably didn't know these)
- `Stage 3: client frame time (2–3 jars, only where stage 0 showed a cost)` --references--> `BaseMapLayer`  [INFERRED]
  reports/RafasConquest optimization plan 2026-11-18.md → src/main/java/rafasconquest/client/map/BaseMapLayer.java
- `Cited Findings` --references--> `MapView`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/MapView.java
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

## Communities (430 total, 270 thin omitted)

### Community 0 - ".measure"
Cohesion: 0.19
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 1 - "net.minecraft.world.level.block.entity.BlockEntity"
Cohesion: 0.06
Nodes (7): BattleBannerBlockEntity, ConquestContent, Oar, Banner, PlacedBanners, Sail, SaveGuard

### Community 2 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.07
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - "org.junit.jupiter.api.Test"
Cohesion: 0.06
Nodes (5): DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, OarRowingTest, ArmouryTest

### Community 5 - "BattleScreen"
Cohesion: 0.07
Nodes (4): BattleScreen, Entry, Menu, HornOrders

### Community 6 - "Front"
Cohesion: 0.08
Nodes (8): Cited Findings, Feud, Lord, Feuds, Lands, Claim, Front, Politics

### Community 7 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.09
Nodes (6): PlannedTent, Saved, WarCamp, CourtSet, Seat, Style

### Community 8 - "rafasconquest.battle.FieldBlock"
Cohesion: 0.05
Nodes (5): Afloat, Figure, GunMark, Landing, Live

### Community 9 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.08
Nodes (13): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, Camera, BattleSounds, ChargeSound (+5 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "Arm"
Cohesion: 0.07
Nodes (10): Arm, Kind, ENGINE, FOOT, MOUNTED, RANGED, Missile, ARROWS (+2 more)

### Community 13 - "Siege"
Cohesion: 0.10
Nodes (11): Face, GateAction, Kind, CLOSE, OPEN, RAM, Region, IN (+3 more)

### Community 14 - "PveCommands.java"
Cohesion: 0.09
Nodes (30): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+22 more)

### Community 15 - "BattleMap.java"
Cohesion: 0.05
Nodes (4): Battle map developer web page, Morale, rout and rally, CameraView, SwingModesTest

### Community 16 - "Morale"
Cohesion: 0.17
Nodes (8): Morale, State, RALLIED, ROUTED, SHATTERED, STEADY, UnitInfo, UnitsState

### Community 17 - "RafasConquest Project Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.15
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 19 - "RecruitsBattle"
Cohesion: 0.03
Nodes (30): Cited Findings, Card, Enlisted, GeneralTask, HornCall, ATTACKING, MOVING, RALLYING (+22 more)

### Community 20 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.10
Nodes (5): Place, CityManager, PlayerStats, View, PlaceMusic

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 22 - "list"
Cohesion: 0.05
Nodes (7): Countryside (hamlets, villages, watchtowers, castles), Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, Nations by zone, Roads (high, city, town roads; bridges; signposts), Seat cities, Storm wall and war zone

### Community 23 - ".settings"
Cohesion: 0.07
Nodes (5): DistrictKey, Townsfolk, CitySizesTest, StreetWalksTest, PlaceMusicTest

### Community 24 - "CityHud"
Cohesion: 0.09
Nodes (3): CityHud, Spot, CityHudPreviewTest

### Community 25 - "Tier"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "net.minecraft.core.BlockPos"
Cohesion: 0.06
Nodes (6): CannonControl, GateGeometry, StylePiece, CityGates, Done, GateGeometryTest

### Community 27 - "RoadNetwork"
Cohesion: 0.14
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 28 - "net.minecraft.core.Direction"
Cohesion: 0.08
Nodes (10): GateDoors, SableLink, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN (+2 more)

### Community 29 - "ConquestEvents.java"
Cohesion: 0.05
Nodes (4): ConquestWorldgen, CountryPlacement, DojoPlacement, FarmStructure

### Community 30 - ".blocksPlacedByHandBeforeTheBattleKeepTheirPlaces"
Cohesion: 0.09
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 32 - "SableLink"
Cohesion: 0.12
Nodes (6): Hull, Ramming, BattleShips, Helm, Ram, RammingTest

### Community 34 - ".map"
Cohesion: 0.08
Nodes (7): Cited Findings, Army, FeudDuel, Goal, MusterBars, PoliticsSync, Wars

### Community 35 - "CityPlan"
Cohesion: 0.16
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 36 - "FlagBlock.java"
Cohesion: 0.08
Nodes (7): BattleBannerBlock, FlagBlock, Mount, BARS, BLOCK, FENCE, WALL

### Community 37 - ".view"
Cohesion: 0.08
Nodes (13): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+5 more)

### Community 38 - "WaterSwell"
Cohesion: 0.10
Nodes (9): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Made, WaterSwell, Swell (+1 more)

### Community 39 - "Site"
Cohesion: 0.13
Nodes (4): SlopeTile, CitySlopeStructure, Frame, Harbour

### Community 42 - ".plan"
Cohesion: 0.12
Nodes (6): Centre, TitleStyle, Faith, Bond, RealmPlanner, Seat

### Community 44 - "ArrowVolleys"
Cohesion: 0.08
Nodes (14): DistantDepth, Arrow, ArrowVolleys, Kind, ARROW, BALL, BIG_BALL, BIG_ROCK (+6 more)

### Community 45 - "FactionInfo.java"
Cohesion: 0.09
Nodes (12): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, FactionInfo (+4 more)

### Community 46 - "Personality"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.22
Nodes (6): Lane, Leg, Port, SeaLanes, Ship, Voyage

### Community 48 - "War"
Cohesion: 0.06
Nodes (6): Event, Muster, CEREMONIAL, OFFENSIVE, War, PoliticsTest

### Community 50 - ".postChunk"
Cohesion: 0.12
Nodes (4): Cited Findings, BarracksPeople, CityGuards, ConquestServer

### Community 52 - "Kit"
Cohesion: 0.10
Nodes (20): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+12 more)

### Community 53 - ".onRenderLevel"
Cohesion: 0.08
Nodes (12): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Cited Findings, Gaps, Inferences, BattleSpotRing, DistantOcclusion, Mode (+4 more)

### Community 55 - ".render"
Cohesion: 0.09
Nodes (8): CharacterScreen, Retainer, Spot, Banner, Portraits, Names, Honour, Honours

### Community 57 - "RecruitsBattle.java"
Cohesion: 0.03
Nodes (3): /council <topic>, Wind, RafasConquest

### Community 58 - "Land"
Cohesion: 0.16
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 59 - "CityTerrain"
Cohesion: 0.07
Nodes (18): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+10 more)

### Community 61 - "HorizontalStretch"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 62 - "WorldSettings"
Cohesion: 0.18
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 63 - "TitleMap"
Cohesion: 0.16
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.11
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 67 - "CityLayout"
Cohesion: 0.08
Nodes (6): CityLayout, District, Site, WorldSettings, CityStructure, CityLayoutTest

### Community 69 - "BattlePlan"
Cohesion: 0.06
Nodes (3): BattlePlan, Slot, BattlePlanTest

### Community 70 - "Row"
Cohesion: 0.12
Nodes (3): Fort, Stronghold, Row

### Community 71 - "School"
Cohesion: 0.09
Nodes (14): Dojos, Ground, School, BLOOD, FIRE, ICE, IRON, LIGHT (+6 more)

### Community 72 - "DistantShips"
Cohesion: 0.08
Nodes (8): Takeaway, DistantShips, Far, HullKey, Riding, Want, DeckLiftTest, DistantShipsTest

### Community 73 - "Homeland"
Cohesion: 0.09
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 75 - "WarCouncil"
Cohesion: 0.12
Nodes (6): Facts, Officer, Said, Talk, WarCouncil, WarCouncilPagesTest

### Community 76 - ".step"
Cohesion: 0.09
Nodes (12): 1. How we avoid breaking anything, 2. Where the 10 November gameplan stands, 3. New costs since 10 November, 5. Order and size, Core-function check after every stage (about 10 minutes in game), RafasConquest optimization plan (18 November 2026), FigureMotion, Match (+4 more)

### Community 77 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.09
Nodes (7): WarHorn, BattleStandardItem, AddonPieces, Armoury, Outfit, Style, ItemFlags

### Community 78 - "RecruitFigures"
Cohesion: 0.07
Nodes (15): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD, Capture (+7 more)

### Community 79 - "BaseMap"
Cohesion: 0.06
Nodes (12): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, MapPreview, BaseMap, Nation (+4 more)

### Community 81 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.20
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 82 - "Candidate"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 84 - "Recruiter"
Cohesion: 0.10
Nodes (3): RecruiterRenderer, WorldMapItem, Recruiter

### Community 89 - "Quests"
Cohesion: 0.13
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 91 - ".saveReloadRoundTripIsIdentical"
Cohesion: 0.07
Nodes (6): Cited Findings, ConquestSavedData, Poi, EconomyTick, PoiPrecomputer, ConquestSavedDataTest

### Community 92 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 95 - ".of"
Cohesion: 0.16
Nodes (5): FactionBanner, Layer, FactionBannerNbt, CityBanners, FactionBannerTest

### Community 96 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (24): 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings, Cited Findings (+16 more)

### Community 98 - "Point"
Cohesion: 0.11
Nodes (6): Point, Gate, GateState, Plan, Post, WallTower

### Community 99 - "FactionInfoScreen"
Cohesion: 0.16
Nodes (3): FactionInfoScreen, Line, StructureLink

### Community 100 - "CourtView"
Cohesion: 0.11
Nodes (5): Camera, CourtView, Figure, OrganizationScreen, Organization

### Community 103 - "BattleType"
Cohesion: 0.16
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 105 - "Peerage"
Cohesion: 0.08
Nodes (15): NobleTitle, COUNT, DUKE, EMPEROR, KING, KNIGHT, NONE, Merit (+7 more)

### Community 107 - "How Total War calculates battles between units"
Cohesion: 0.17
Nodes (11): 1. Combat is per soldier, not per unit, 2. Does the swing land?, 3. How much damage the hit does, 4. Charges, 5. Missiles, 6. Leadership (morale): what actually wins battles, 7. Autoresolve, 8. Older games (Shogun 1, Medieval 1, Rome 1) (+3 more)

### Community 108 - "MapContext"
Cohesion: 0.09
Nodes (8): Cited Findings, BaseMapLayer, Hit, LabelLayer, MapContext, MapView, PoiIconLayer, LabelShape

### Community 110 - "CityPanel"
Cohesion: 0.15
Nodes (6): CityPanel, Mode, DISMISS, MARCH, MUSTER, NONE

### Community 111 - "NationScreen"
Cohesion: 0.14
Nodes (3): NationScreen, Demographics, Share

### Community 113 - "Clash"
Cohesion: 0.14
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 114 - "Front"
Cohesion: 0.11
Nodes (6): Engine, LADDERS, RAMS, TOWERS, Front, Rows

### Community 116 - "ClothRenderer"
Cohesion: 0.13
Nodes (6): ClothRenderer, Fabric, Far, Kind, Live, OffTheBackTest

### Community 118 - "UnitOrder"
Cohesion: 0.11
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.09
Nodes (4): GrandStrategyScreen, Marcher, Spot, GrandStrategyDateTest

### Community 121 - "Cloth"
Cohesion: 0.10
Nodes (5): Cloth, Solid, ClothTest, StretchTest, WoolTest

### Community 124 - "TechTreeScreen"
Cohesion: 0.18
Nodes (4): TechTreeScreen, Tech, TechTree, TechTreeTest

### Community 126 - "DistantHorizonsArmies"
Cohesion: 0.05
Nodes (9): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+1 more)

### Community 127 - "TitleLayer"
Cohesion: 0.08
Nodes (20): 4. The stages, Stage 0: measure (one jar, no behaviour change), Stage 1: small, same-behaviour fixes (2 jars: server, then client), Stage 2: flatten server spikes (2–3 jars), Stage 3: client frame time (2–3 jars, only where stage 0 showed a cost), Stage 4: changes that need your design call, A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking (+12 more)

### Community 128 - "MapLayer"
Cohesion: 0.16
Nodes (8): LegendEntry, MapLayer, Symbol, Place, PlaceMenuLayer, Row, TerritoryLayer, UnitLayer

### Community 130 - "TitleOwnersScreen"
Cohesion: 0.19
Nodes (3): ClientPeerage, Owner, TitleOwnersScreen

### Community 132 - ".shift"
Cohesion: 0.16
Nodes (6): BattleSite, Part, Water, BattleSite.Water, BattleSite.Water, BattleSiteTest

### Community 134 - "MediumCannonEntity"
Cohesion: 0.09
Nodes (4): MediumCannonEntity, Order, Shot, ROUNDSHOT

### Community 136 - ".wallSegments"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 137 - "ConquestWorldScreen"
Cohesion: 0.22
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 138 - "ShipHulls"
Cohesion: 0.16
Nodes (4): BattleKind, Part, ShipHulls, ShipHullsTest

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (40): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 6. Sodium (and Iris) compatibility, 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply (+32 more)

### Community 141 - ".choose"
Cohesion: 0.12
Nodes (11): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+3 more)

### Community 142 - "Faction"
Cohesion: 0.10
Nodes (5): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction, FactionInfoTest

### Community 155 - ".accept"
Cohesion: 0.16
Nodes (3): Ranks, Offer, Recruitment

### Community 156 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (6): CommandTableBlock, GateBlock, GrandStrategyBlock, HelmBlock, StrategyBoardBlock, MarkerBlock

### Community 157 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 158 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 160 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.07
Nodes (18): Inferences, Cited Findings, Cited Findings, ArrowSaveMixin, BetterCombatOneHandMixin, ChunkMapAccess, ChunkMapCameraMixin, CityVegetationMixin (+10 more)

### Community 163 - "Countryside"
Cohesion: 0.22
Nodes (4): Countryside, Plan, CountrysideTest, World

### Community 175 - "ConquestNetwork"
Cohesion: 0.03
Nodes (47): Bark, BaseMapPart, BattleSpot, BoardAction, BoardView, CameraAt, CannonFire, CannonLay (+39 more)

### Community 186 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 217 - ".hash"
Cohesion: 0.10
Nodes (7): Bounds, DistanceScaling, NobleAnnex, OriginRoller, Roll, ProvinceNames, DistanceScalingTest

### Community 219 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.05
Nodes (37): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps (+29 more)

### Community 223 - "Settlement"
Cohesion: 0.12
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 224 - "RecruitFigures.java"
Cohesion: 0.04
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 225 - "GrandStrategy"
Cohesion: 0.23
Nodes (3): Army, GrandStrategy, View

### Community 226 - "Duty"
Cohesion: 0.10
Nodes (12): 1. Flank and rear shock, 2. Nearby rout (fright), 3. Army-wide losses, 4. Shattering, Design: adding the four morale shocks (built 2026-11-11, untested in game), Tests (FieldBattleTest), FieldRules, Rates (+4 more)

### Community 232 - "Roads"
Cohesion: 0.12
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 238 - "Body"
Cohesion: 0.08
Nodes (26): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+18 more)

### Community 240 - "BackBannerLayer.java"
Cohesion: 0.17
Nodes (3): BackBannerLayer, BackBanners, MouthLayer

### Community 248 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 249 - "ClientModEvents"
Cohesion: 0.09
Nodes (3): ClientModEvents, net.neoforged.neoforge.client.extensions.common.IClientItemExtensions, Registration

### Community 255 - "ProvinceMap"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 262 - ".fire"
Cohesion: 0.09
Nodes (4): CharacterMarkers, Placed, MarkerBlockEntity, MarkerSpec

### Community 265 - "Doctrine"
Cohesion: 0.05
Nodes (31): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+23 more)

### Community 266 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 267 - "SeaCalm"
Cohesion: 0.19
Nodes (5): Ground, Depths, SeaCalm, Strengths, SeaCalmTest

### Community 272 - ".onLogout"
Cohesion: 0.09
Nodes (7): ClientTownsfolk, Folk, ClientMapCache, Throw, ThrownFigures, StreetWalks, Walk

### Community 274 - ".candidateChunk"
Cohesion: 0.18
Nodes (3): ChunkCoord, FarmLayout, FarmPlacement

### Community 278 - "DayClockTest"
Cohesion: 0.25
Nodes (3): DayClock, Step, DayClockTest

### Community 283 - "City"
Cohesion: 0.15
Nodes (4): City, Grid, SeatCities, CityIndex

### Community 285 - "net.minecraft.client.Minecraft"
Cohesion: 0.11
Nodes (6): FarGun, Model, OarFigures, Rowing, Columns, SableSeen

### Community 288 - "Job"
Cohesion: 0.10
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "World map (CK3-style political map)"
Cohesion: 0.33
Nodes (7): Base map generated from seed, Command Table block, Faction Info screen, World map layers, Province size setting, Provinces (ProvinceMap), World map (CK3-style political map)

### Community 294 - "CityGridBuilder"
Cohesion: 0.09
Nodes (9): CityGridBuilder, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY (+1 more)

### Community 297 - ".generate"
Cohesion: 0.11
Nodes (5): Dojo, BaseMapGenerator, Heights, BiomePalette, Entry

### Community 298 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.40
Nodes (4): Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Takeaway

### Community 302 - "NetworkDirection"
Cohesion: 0.50
Nodes (3): NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 303 - "GoldChurch.java"
Cohesion: 0.20
Nodes (5): Gold church, Quest board and quests, Recruitment posts and recruiter, GoldChurch, Layout

### Community 305 - "FactionMapScreen"
Cohesion: 0.09
Nodes (3): FactionMapScreen, PetitionScreen, AbstractSliderButton

### Community 311 - "net.minecraft.client.renderer.MultiBufferSource"
Cohesion: 0.06
Nodes (10): BannerArt, BattleBannerRenderer, CapeCurio, ClothItemRenderer, Carried, Layer, FarFlags, Held (+2 more)

### Community 313 - "Machine"
Cohesion: 0.07
Nodes (6): Machine, Role, MANTLET, RAM, SHOOTER, TOWER

### Community 314 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 318 - "State"
Cohesion: 0.50
Nodes (4): State, CARRIED, RAISING, UP

### Community 319 - "Quest"
Cohesion: 0.13
Nodes (9): Boards, Entry, View, Taken, Quest, Type, CLEAR, DELIVER (+1 more)

### Community 322 - "WarCamp.java"
Cohesion: 0.05
Nodes (32): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Character page, Characters (holders, skills, prowess, traits), Court 3D scene (CourtView), Cultures (seven homelands) (+24 more)

### Community 324 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 325 - "WeaponSkills"
Cohesion: 0.13
Nodes (5): EpicKnightsJoustingMixin, EpicKnightsSlownessMixin, ShoulderSurfingPickMixin, WeaponSkills, WeaponSkillsTest

### Community 326 - "net.minecraft.world.level.block.Block"
Cohesion: 0.11
Nodes (5): MediumCannonBlock, OarBlock, QuestBoardBlock, SailBlock, Builder

### Community 330 - "OarFigures"
Cohesion: 0.07
Nodes (12): ShipSprite, Batch, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED (+4 more)

### Community 336 - ".world"
Cohesion: 0.09
Nodes (9): Hook, Kind, FRIEND, RIVAL, Realm, Succession, Taking, RealmPlannerTest (+1 more)

### Community 342 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.18
Nodes (5): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), TickWeave experiment

### Community 350 - "FormationSnapshot"
Cohesion: 0.08
Nodes (20): Cited Findings, Engine, FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, ENGINES (+12 more)

### Community 355 - "FieldBlock"
Cohesion: 0.04
Nodes (8): The core change: morale becomes a sum, FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 357 - "Order"
Cohesion: 0.25
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

### Community 367 - "Piece"
Cohesion: 0.13
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 417 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, GUNSHIP, SHOCK, SWORDSMEN

### Community 422 - "10. Overall ranking, and areas checked that look fine"
Cohesion: 0.50
Nodes (4): 10. Overall ranking, and areas checked that look fine, Gaps, Inferences, Takeaway

### Community 431 - "net.minecraft.core.Direction"
Cohesion: 0.10
Nodes (7): ChunkWait, Wait, Land, Result, SiegeSurvey, Wall, SiegeSurveyTest

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **423 isolated node(s):** `SOLDIER`, `LEADER`, `GENERAL`, `BEARER`, `GUARD` (+418 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1504 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **270 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `net.minecraft.nbt.CompoundTag`, `org.junit.jupiter.api.Test`, `.shift`, `net.minecraft.world.phys.Vec3`, `rafasconquest.battle.FieldBlock`, `net.minecraft.world.phys.Vec3`, `Doctrine`, `ConquestEvents`, `PveCommands.java`, `BattleMap.java`, `net.minecraft.server.level.ServerPlayer`, `ConquestEvents.java`, `PveCommands`, `.map`, `rafasconquest.faction.Faction`, `net.minecraft.client.gui.GuiGraphics`, `.generate`, `SiegeWorks`, `net.minecraft.core.Direction`, `BattleKeepInventory.java`, `RecruitsBattle.java`, `Phase`, `.siege`, `.onServerTick`, `WeaponSkills`, `WarCouncil`, `net.minecraft.world.item.ItemStack`, `Duty`, `FieldBlock`, `BattleType`, `Peerage`, `Garrison`, `.block`, `UnitOrder`, `GrappleHook`?**
  _High betweenness centrality (0.062) - this node is a cross-community bridge._
- **What connects `SOLDIER`, `LEADER`, `GENERAL` to the rest of the system?**
  _423 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `net.minecraft.world.level.block.entity.BlockEntity` be split into smaller, more focused modules?**
  _Cohesion score 0.05952380952380952 - nodes in this community are weakly interconnected._
- **Why does `FieldBlock` connect `FieldBlock` to `ShipKind`, `Duty`, `org.junit.jupiter.api.Test`, `Point`, `org.jetbrains.annotations.Nullable`, `rafasconquest.battle.FieldBlock`, `Task`, `SiegeWorks`, `BlockGrid`, `Siege`, `.siege`, `Front`, `.block`, `Machine`, `Duty`, `.blocksPlacedByHandBeforeTheBattleKeepTheirPlaces`?**
  _High betweenness centrality (0.047) - this node is a cross-community bridge._
- **Should `net.minecraft.nbt.CompoundTag` be split into smaller, more focused modules?**
  _Cohesion score 0.06613861386138614 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._