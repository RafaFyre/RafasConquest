# Graph Report - RafasConquest  (2026-11-21)

## Corpus Check
- 689 files · ~561,715 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 8570 nodes · 27887 edges · 420 communities (159 shown, 261 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3531 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `767832c4`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- rafasconquest.battle.BattlePlan.Point
- .render
- net.minecraft.nbt.CompoundTag
- org.junit.jupiter.api.Test
- MediumCannonEntity
- BattleScreen
- Front
- net.minecraft.core.BlockPos
- Politics
- net.minecraft.world.level.Level
- ConquestEvents
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Point
- PveCommands.java
- BattleMap.java
- Morale
- RafasConquest Project Site
- Person
- net.minecraft.world.level.block.Block
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- .view
- CityHud
- Tier
- net.minecraft.core.Vec3i
- .place
- City
- CityGridBuilder.java
- .allSitesInBox
- .column
- BattleShips
- PveCommands
- .map
- CityPlan
- Characters (holders, skills, prowess, traits)
- Ranks
- WaterSwell
- .build
- Faction
- BaseMap
- net.minecraft.network.FriendlyByteBuf
- SiegeWorks
- org.joml.Matrix4f
- rafasconquest.battle.FormationSnapshot
- Personality
- SeaLanes
- SimpleChannel
- .postChunk
- BattleSounds
- Kit
- DistantOcclusion
- MapLayer
- CityStyles
- BattlePlan
- CityTerrain
- .saveReloadRoundTripIsIdentical
- .world
- WorldSettings
- .feudFight
- Bodies
- StyleLibrary
- SwellTest
- 1. How we avoid breaking anything
- .deployment
- Row
- School
- DistantShips
- Age
- MapLands
- WarCouncil
- .step
- net.minecraft.world.item.ItemStack
- RecruitFigures
- TechTreeScreen
- CityGridBuilder
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- Candidate
- RoadLayer
- Recruiter
- org.jetbrains.annotations.Nullable
- Terrain
- CityTerrainPiece
- Roads
- HorizontalStretch
- DialogueScreen
- OrganizationScreen
- Goal
- MediumCannonEntity.java
- .of
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Homeland
- FactionInfoScreen
- Person
- UnitOrder
- .passOnOrders
- Land
- FaceOffScreen
- How Total War calculates battles between units
- ConquestWorldScreen
- .map
- CityPanel
- NationScreen
- BattleBannerBlockEntity
- PoliticalStyle
- Arm
- .passOnOrders
- SeatCitiesTest
- MarkerScreen
- CourtSet
- GrandStrategyScreen
- BaseMapStore
- Cloth
- Quests
- .findGenerationPoint
- DayClockTest
- MapContext
- CapeCurio
- CityManagerScreen
- TitleOwnersScreen
- ChunkWait.java
- .shift
- RafasConquest.java
- Hull
- GateBlockEntity
- net.minecraft.world.level.block.state.BlockState
- SeaCalm
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .choose
- BackBanners
- Override
- DistantShipsTest
- .draw
- Barracks
- CLAUDE.md
- .view
- ConquestConfig
- UnitCards
- BattleBannerBlock.java
- Call
- net.minecraft.sounds.SoundEvent
- org.spongepowered.asm.mixin.Mixin
- .deploy
- .rightClick
- .boundsAt
- Weapon
- /pve debug commands
- ConquestNetwork
- .load
- com.mojang.blaze3d.vertex.PoseStack
- PetitionScreen
- DistantHorizonsArmies
- Ride
- .noble
- Determinism rules
- .tick
- .say
- ArmySpawnScreen
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Settlement
- RecruitFigures.java
- ClientPolitics
- Duty
- GateScreen
- 3. GUI and map screens (strategy map, faction map, battle overlays)
- DistantHorizonsLink
- Override
- ClothItemRenderer
- BattleType
- PoliticsTest
- GrappleHook
- DistantArmyRenderer
- Body
- 8. Lessons from performance mods that a content mod can apply
- .onLevelLoad
- RafasConquest (PvE addon for Villager Recruits)
- Wind
- CombatMode
- Kind
- ClientModEvents
- FleetRole
- Doctrine
- BoardScreen
- WeaponSkills
- ProvinceMap
- ConquestGameRules
- SablePhysics
- Profiling run sheet (baseline)
- CameraView
- NobleAnnex
- net.neoforged.bus.api.SubscribeEvent
- 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel)
- map
- .findGenerationPoint
- RecruitsBattle
- TitleLayer
- .measure
- net.minecraft.client.Minecraft
- TitleMap
- .touches
- Kind
- ClothRenderer
- .roadStart
- ClientScreens
- .generate
- CannonShots
- FarFlags
- .onLogout
- Phase
- Kind
- Machine
- SwingModes
- Piece
- DistantGeneration
- .order
- Distribution
- MarkerBlock
- WallTemplates
- OarFigures
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- Deflects
- .encode
- BattleKeepInventory
- BlockGrid
- FormationSnapshot
- FieldBlock
- Task
- 4. Depth buffers and post effects (DH depth, occlusion, GPU sync)
- ShipKind
- Missile
- .fire
- Cited Findings
- net.minecraft.core.Direction
- net.minecraft.world.level.block.entity.BlockEntity

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 321 edges
2. `FieldBlock` - 250 edges
3. `FieldBattle` - 248 edges
4. `Point` - 225 edges
5. `BaseMap` - 152 edges
6. `FieldArmy` - 142 edges
7. `ConquestNetwork` - 133 edges
8. `Lands` - 94 edges
9. `BattleScreen` - 89 edges
10. `Siege` - 86 edges

## Surprising Connections (you probably didn't know these)
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/map/BaseMap.java
- `Stage 3: client frame time (2–3 jars, only where stage 0 showed a cost)` --references--> `BaseMapLayer`  [INFERRED]
  reports/RafasConquest optimization plan 2026-11-18.md → src/main/java/rafasconquest/client/map/BaseMapLayer.java
- `Cited Findings` --references--> `MapView`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/MapView.java
- `Battle code costs 0.2 ms; the multi-second freezes come from blocking` --references--> `RecruitsArrowCleanerMixin`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/mixin/RecruitsArrowCleanerMixin.java
- `Cited Findings` --references--> `RecruitsArrowCleanerMixin`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/mixin/RecruitsArrowCleanerMixin.java

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

## Communities (420 total, 261 thin omitted)

### Community 0 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.06
Nodes (16): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, FieldRules (+8 more)

### Community 1 - ".render"
Cohesion: 0.10
Nodes (7): CharacterScreen, Retainer, Spot, Portraits, Names, Honour, Honours

### Community 2 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.06
Nodes (15): BarracksPiece, CityRoadPiece, Build, CASTLE, HAMLET, RUIN, TOWER, WELL (+7 more)

### Community 3 - "org.junit.jupiter.api.Test"
Cohesion: 0.05
Nodes (10): DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, GroundPlanTest, Land, WindTest, SwingModesTest, GrandStrategyDateTest (+2 more)

### Community 4 - "MediumCannonEntity"
Cohesion: 0.07
Nodes (4): CannonAim, MediumCannonEntity, Order, CannonAimTest

### Community 6 - "Front"
Cohesion: 0.09
Nodes (4): Feud, Lord, Feuds, Lands

### Community 7 - "net.minecraft.core.BlockPos"
Cohesion: 0.07
Nodes (6): PlannedTent, Saved, WarCamp, CannonControl, Placed, GateGeometryTest

### Community 8 - "Politics"
Cohesion: 0.07
Nodes (12): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Fort, Front, Muster (+4 more)

### Community 9 - "net.minecraft.world.level.Level"
Cohesion: 0.08
Nodes (4): MediumCannonBlock, OarBlock, SailBlock, WorldMapItem

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.07
Nodes (28): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings (+20 more)

### Community 13 - "Point"
Cohesion: 0.04
Nodes (20): Point, Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate (+12 more)

### Community 14 - "PveCommands.java"
Cohesion: 0.07
Nodes (31): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+23 more)

### Community 15 - "BattleMap.java"
Cohesion: 0.05
Nodes (3): Battle map developer web page, Morale, rout and rally, SaveGuard

### Community 16 - "Morale"
Cohesion: 0.28
Nodes (6): Morale, State, RALLIED, ROUTED, SHATTERED, STEADY

### Community 17 - "RafasConquest Project Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.09
Nodes (12): Demographics, Share, Faith, Kind, FAVOUR, SECRET, Bond, Kind (+4 more)

### Community 20 - "net.minecraft.world.level.block.Block"
Cohesion: 0.17
Nodes (4): GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, Builder

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 23 - ".view"
Cohesion: 0.04
Nodes (31): Advancement, Boards, Entry, View, NobleTitle, COUNT, DUKE, EMPEROR (+23 more)

### Community 24 - "CityHud"
Cohesion: 0.09
Nodes (3): CityHud, Spot, CityHudPreviewTest

### Community 25 - "Tier"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 27 - ".place"
Cohesion: 0.09
Nodes (6): Cited Findings, RoadFeature, Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 28 - "City"
Cohesion: 0.09
Nodes (8): Countryside, Plan, City, Grid, SeatCities, CityIndex, CountrysideTest, World

### Community 29 - "CityGridBuilder.java"
Cohesion: 0.03
Nodes (11): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, CityStructure, ConquestWorldgen, CountryPlacement (+3 more)

### Community 30 - ".allSitesInBox"
Cohesion: 0.06
Nodes (12): CityLayout, District, Side, EAST, NORTH, SOUTH, WEST, Site (+4 more)

### Community 31 - ".column"
Cohesion: 0.12
Nodes (7): Column, Kind, NONE, PLATE, SLOPE, CityTerrainTest, NobleAnnexTest

### Community 32 - "BattleShips"
Cohesion: 0.10
Nodes (5): BattleShips, Afloat, Helm, Ram, SableLink

### Community 34 - ".map"
Cohesion: 0.08
Nodes (6): Cited Findings, Army, MusterBars, PoliticsSync, War, Wars

### Community 35 - "CityPlan"
Cohesion: 0.16
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 36 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.03
Nodes (52): Ambushes from woods, Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Character page, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings) (+44 more)

### Community 37 - "Ranks"
Cohesion: 0.06
Nodes (21): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+13 more)

### Community 38 - "WaterSwell"
Cohesion: 0.15
Nodes (7): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Made, WaterSwell

### Community 39 - ".build"
Cohesion: 0.12
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 40 - "Faction"
Cohesion: 0.11
Nodes (3): RecruitsBridge, RecruitsFactions, Faction

### Community 41 - "BaseMap"
Cohesion: 0.05
Nodes (3): InventoryStatsButton, Ranks, BaseMap

### Community 42 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.04
Nodes (12): BattleSnapshot, BattleSpot, BoardView, GrandStrategyView, PeerageView, PoliticsView, TalkView, UnitsRequest (+4 more)

### Community 43 - "SiegeWorks"
Cohesion: 0.10
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 44 - "org.joml.Matrix4f"
Cohesion: 0.10
Nodes (13): DistantDepth, Arrow, ArrowVolleys, Kind, ARROW, BALL, BIG_BALL, BIG_ROCK (+5 more)

### Community 45 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.11
Nodes (9): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Cited Findings, Gaps, Inferences, Takeaway, DistantArmies, Throw (+1 more)

### Community 46 - "Personality"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.07
Nodes (13): Lane, Leg, Port, Post, Propulsion, OARS, SAIL, STEAM (+5 more)

### Community 48 - "SimpleChannel"
Cohesion: 0.10
Nodes (5): BackBanner, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 50 - ".postChunk"
Cohesion: 0.07
Nodes (8): Cited Findings, ConquestSavedData, BarracksPeople, CityBanners, CityGuards, ConquestServer, EconomyTick, PoiPrecomputer

### Community 51 - "BattleSounds"
Cohesion: 0.17
Nodes (6): BattleSounds, ChargeSound, DinSound, Queued, Shot, Source

### Community 52 - "Kit"
Cohesion: 0.10
Nodes (20): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+12 more)

### Community 53 - "DistantOcclusion"
Cohesion: 0.07
Nodes (12): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, BattleSpotRing, DistantOcclusion, Mode (+4 more)

### Community 55 - "MapLayer"
Cohesion: 0.13
Nodes (8): LegendEntry, MapLayer, Symbol, Place, PlaceMenuLayer, Row, TerritoryLayer, UnitLayer

### Community 59 - "CityTerrain"
Cohesion: 0.10
Nodes (11): CityTerrain, Edge, CARVE, FILL, Heights, Plan, Side, EAST (+3 more)

### Community 60 - ".saveReloadRoundTripIsIdentical"
Cohesion: 0.09
Nodes (9): Cited Findings, ResourceType, FOOD, Poi, PoiType, CAPITAL, CITY, FARM (+1 more)

### Community 61 - ".world"
Cohesion: 0.09
Nodes (6): Hook, Realm, Succession, Taking, RealmPlannerTest, World

### Community 62 - "WorldSettings"
Cohesion: 0.09
Nodes (10): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, WorldSettings (+2 more)

### Community 63 - ".feudFight"
Cohesion: 0.15
Nodes (5): Clash, Kind, DUEL, SKIRMISH, FeudDuel

### Community 65 - "StyleLibrary"
Cohesion: 0.11
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 67 - "1. How we avoid breaking anything"
Cohesion: 0.18
Nodes (7): 1. How we avoid breaking anything, 2. Where the 10 November gameplan stands, 3. New costs since 10 November, 5. Order and size, Core-function check after every stage (about 10 minutes in game), RafasConquest optimization plan (18 November 2026), ClientTownsfolkTest

### Community 71 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 72 - "DistantShips"
Cohesion: 0.11
Nodes (6): DistantShips, Far, HullKey, Riding, Want, DeckLiftTest

### Community 73 - "Age"
Cohesion: 0.12
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 74 - "MapLands"
Cohesion: 0.10
Nodes (5): Gaps, Inferences, Takeaway, Where are full scans, nested loops, repeated lookups and per-tick allocations?, MapLands

### Community 75 - "WarCouncil"
Cohesion: 0.15
Nodes (6): Facts, Officer, Said, Talk, WarCouncil, WarCouncilPagesTest

### Community 76 - ".step"
Cohesion: 0.11
Nodes (6): FigureMotion, Ground, Match, Placed, State, FigureMotionTest

### Community 77 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.09
Nodes (8): WarHorn, AddonPieces, Armoury, Outfit, Style, ItemFlags, RankKits, ArmouryTest

### Community 78 - "RecruitFigures"
Cohesion: 0.08
Nodes (9): FigureGear, Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures (+1 more)

### Community 80 - "CityGridBuilder"
Cohesion: 0.08
Nodes (9): CityGridBuilder, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY (+1 more)

### Community 81 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.18
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 82 - "Candidate"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "RoadLayer"
Cohesion: 0.12
Nodes (17): 4. The stages, Stage 1: small, same-behaviour fixes (2 jars: server, then client), Stage 2: flatten server spikes (2–3 jars), Stage 3: client frame time (2–3 jars, only where stage 0 showed a cost), Stage 4: changes that need your design call, A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization (+9 more)

### Community 84 - "Recruiter"
Cohesion: 0.11
Nodes (4): RecruiterRenderer, Recruiter, Offer, Recruitment

### Community 88 - "Roads"
Cohesion: 0.10
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 95 - ".of"
Cohesion: 0.22
Nodes (3): FactionBanner, Layer, FactionBannerTest

### Community 96 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.09
Nodes (12): BuiltInFiles, TypeToken<Map<String, String>>, Outcome, EDITED_KEPT, OLD_REPLACED, SAME, UPDATED, WRITTEN (+4 more)

### Community 98 - "Homeland"
Cohesion: 0.17
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 99 - "FactionInfoScreen"
Cohesion: 0.12
Nodes (5): ClientBarks, Line, FactionInfoScreen, Line, StructureLink

### Community 100 - "Person"
Cohesion: 0.15
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 102 - "UnitOrder"
Cohesion: 0.11
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 103 - ".passOnOrders"
Cohesion: 0.07
Nodes (8): GunMark, Landing, Live, Shape, LINE, SQUARE, V, WEDGE

### Community 105 - "Land"
Cohesion: 0.14
Nodes (7): Stage 0: measure (one jar, no behaviour change), One JVM, six scenarios and JFR beat guesswork on Windows, Heap, Land, Router, Search, ShippingRoutes

### Community 107 - "How Total War calculates battles between units"
Cohesion: 0.11
Nodes (17): 1. Combat is per soldier, not per unit, 1. Flank and rear shock, 2. Does the swing land?, 2. Nearby rout (fright), 3. Army-wide losses, 3. How much damage the hit does, 4. Charges, 4. Shattering (+9 more)

### Community 108 - "ConquestWorldScreen"
Cohesion: 0.25
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 109 - ".map"
Cohesion: 0.09
Nodes (5): Garrison, Army, GrandStrategy, View, GarrisonTest

### Community 110 - "CityPanel"
Cohesion: 0.14
Nodes (6): CityPanel, Mode, DISMISS, MARCH, MUSTER, NONE

### Community 111 - "NationScreen"
Cohesion: 0.08
Nodes (4): Camera, CourtView, Figure, NationScreen

### Community 112 - "BattleBannerBlockEntity"
Cohesion: 0.14
Nodes (3): BattleBannerBlockEntity, Banner, PlacedBanners

### Community 114 - "Arm"
Cohesion: 0.07
Nodes (6): Arm, Kind, ENGINE, FOOT, MOUNTED, RANGED

### Community 115 - ".passOnOrders"
Cohesion: 0.06
Nodes (8): WarPlan, Officers, Trait, BOLD, CAUTIOUS, CUNNING, PROUD, BattleMap

### Community 118 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.08
Nodes (4): GrandStrategyScreen, Marcher, Spot, GarrisonOrder

### Community 120 - "BaseMapStore"
Cohesion: 0.10
Nodes (5): MapPreview, BaseMapStore, Ready, FactionInfo, FactionInfoTest

### Community 121 - "Cloth"
Cohesion: 0.10
Nodes (5): Cloth, Solid, ClothTest, StretchTest, WoolTest

### Community 124 - "Quests"
Cohesion: 0.20
Nodes (4): DataFiles, Kind, Quests, Target

### Community 126 - "DayClockTest"
Cohesion: 0.25
Nodes (3): DayClock, Step, DayClockTest

### Community 127 - "MapContext"
Cohesion: 0.09
Nodes (6): Hit, LabelLayer, MapContext, PoiIconLayer, StrategyRelief, LabelShape

### Community 130 - "TitleOwnersScreen"
Cohesion: 0.21
Nodes (3): ClientPeerage, Owner, TitleOwnersScreen

### Community 132 - ".shift"
Cohesion: 0.16
Nodes (6): BattleSite, Part, Water, BattleSite.Water, BattleSite.Water, BattleSiteTest

### Community 133 - "RafasConquest.java"
Cohesion: 0.04
Nodes (3): GrappleHooks, ConquestEntities, RafasConquest

### Community 134 - "Hull"
Cohesion: 0.25
Nodes (3): Hull, Ramming, RammingTest

### Community 136 - "GateBlockEntity"
Cohesion: 0.08
Nodes (9): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+1 more)

### Community 137 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.11
Nodes (3): CommandTableBlock, GateBlock, HelmBlock

### Community 138 - "SeaCalm"
Cohesion: 0.17
Nodes (5): Sea, Depths, SeaCalm, Strengths, SeaCalmTest

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.10
Nodes (20): 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 5. Water/ocean waves (sea swell, foam, boats on waves), 6. Sodium (and Iris) compatibility, 7. Client tick, stutter and sound, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+12 more)

### Community 141 - ".choose"
Cohesion: 0.15
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 143 - "Override"
Cohesion: 0.24
Nodes (3): Land, Land, Land

### Community 151 - ".view"
Cohesion: 0.07
Nodes (7): Camera, CameraView, CityManager, PlayerStats, View, Retinues, RetinuesTest

### Community 156 - "BattleBannerBlock.java"
Cohesion: 0.06
Nodes (8): BattleBannerBlock, BattleWallBannerBlock, FlagBlock, Mount, BARS, BLOCK, FENCE, WALL

### Community 157 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 158 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 160 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.07
Nodes (17): Cited Findings, ArmorLegsMixin, ArrowSaveMixin, BetterCombatOneHandMixin, CityVegetationMixin, ClientChunkWindowMixin, DhShaderLoaderMixin, DhTerrainShaderMixin (+9 more)

### Community 163 - ".rightClick"
Cohesion: 0.27
Nodes (3): Entry, Menu, HornOrders

### Community 164 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 165 - "Weapon"
Cohesion: 0.33
Nodes (6): Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 175 - "ConquestNetwork"
Cohesion: 0.04
Nodes (39): Bark, BaseMapPart, BoardAction, CameraAt, CannonFire, CannonLay, CannonLeave, CannonShot (+31 more)

### Community 178 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.07
Nodes (7): BannerArt, BattleBannerRenderer, Carried, Layer, EpicKnightsCloaks, GateRenderer, MarkerRenderer

### Community 186 - "PetitionScreen"
Cohesion: 0.08
Nodes (6): Cited Findings, BaseMapLayer, FactionMapScreen, MapView, PetitionScreen, AbstractSliderButton

### Community 206 - ".noble"
Cohesion: 0.29
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 219 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.07
Nodes (29): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps (+21 more)

### Community 223 - "Settlement"
Cohesion: 0.12
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 224 - "RecruitFigures.java"
Cohesion: 0.05
Nodes (5): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), Figure, Batch

### Community 226 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 228 - "3. GUI and map screens (strategy map, faction map, battle overlays)"
Cohesion: 0.40
Nodes (5): 3. GUI and map screens (strategy map, faction map, battle overlays), Cited Findings, Gaps, Inferences, Takeaway

### Community 234 - "BattleType"
Cohesion: 0.15
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.06
Nodes (6): Cited Findings, ClientTownsfolk, Folk, DistantArmyRenderer, Rects, Walk

### Community 238 - "Body"
Cohesion: 0.08
Nodes (26): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+18 more)

### Community 240 - "8. Lessons from performance mods that a content mod can apply"
Cohesion: 0.40
Nodes (5): 8. Lessons from performance mods that a content mod can apply, Cited Findings, Gaps, Inferences, Takeaway

### Community 242 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 248 - "Kind"
Cohesion: 0.50
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 251 - "FleetRole"
Cohesion: 0.33
Nodes (6): FleetRole, FRONT, LINE, RESERVE, SCREEN, SUPPORT

### Community 252 - "Doctrine"
Cohesion: 0.08
Nodes (20): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD (+12 more)

### Community 255 - "ProvinceMap"
Cohesion: 0.17
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "CameraView"
Cohesion: 0.15
Nodes (6): Inferences, Cited Findings, Chunks, ChunkMapAccess, ChunkMapCameraMixin, TrackedEntityCameraMixin

### Community 268 - "1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel)"
Cohesion: 0.40
Nodes (5): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), Cited Findings, Gaps, Inferences, Takeaway

### Community 271 - "map"
Cohesion: 0.04
Nodes (4): BackBannerLayer, MouthLayer, Dialogue, FactionBannerNbt

### Community 281 - "RecruitsBattle"
Cohesion: 0.02
Nodes (27): Cited Findings, Card, Enlisted, Figure, GeneralTask, HornCall, ATTACKING, MOVING (+19 more)

### Community 285 - "net.minecraft.client.Minecraft"
Cohesion: 0.10
Nodes (6): FarGun, Model, OarFigures, Rowing, Columns, SableSeen

### Community 286 - "TitleMap"
Cohesion: 0.11
Nodes (5): TitleMap, TitleStyle, SiegeSurveyTest, Land, TitleMapTest

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "ClothRenderer"
Cohesion: 0.12
Nodes (6): ClothRenderer, Fabric, Far, Kind, Live, OffTheBackTest

### Community 294 - ".roadStart"
Cohesion: 0.08
Nodes (6): StreetWalks, DistrictKey, Townsfolk, PlaceMusic, StreetWalksTest, PlaceMusicTest

### Community 297 - ".generate"
Cohesion: 0.06
Nodes (15): ProvinceNames, Nation, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors, Homeland (+7 more)

### Community 304 - "CannonShots"
Cohesion: 0.31
Nodes (4): Ball, CannonShots, Shot, ROUNDSHOT

### Community 305 - "FarFlags"
Cohesion: 0.18
Nodes (4): FarFlags, Held, Banner, BattleStandardItem

### Community 309 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 310 - "Kind"
Cohesion: 0.50
Nodes (3): Kind, FOOT, HORSE

### Community 313 - "Machine"
Cohesion: 0.07
Nodes (7): Machine, Role, MANTLET, RAM, SHOOTER, TOWER, ShipEngineBlock

### Community 319 - "Piece"
Cohesion: 0.13
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 323 - "DistantGeneration"
Cohesion: 0.20
Nodes (6): Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Takeaway, Api, DistantGeneration

### Community 324 - ".order"
Cohesion: 0.40
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

### Community 325 - "Distribution"
Cohesion: 0.12
Nodes (14): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, GameCharacter, Role, ADVISOR (+6 more)

### Community 326 - "MarkerBlock"
Cohesion: 0.13
Nodes (3): MarkerBlock, GoldChurch, Layout

### Community 330 - "OarFigures"
Cohesion: 0.07
Nodes (12): Wind, ShipSprite, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED (+4 more)

### Community 350 - "FormationSnapshot"
Cohesion: 0.07
Nodes (19): Engine, FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL (+11 more)

### Community 355 - "FieldBlock"
Cohesion: 0.03
Nodes (8): The core change: morale becomes a sum, FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 357 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 367 - "4. Depth buffers and post effects (DH depth, occlusion, GPU sync)"
Cohesion: 0.29
Nodes (6): 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), Cited Findings, Gaps, Inferences, Takeaway, DhApiBeforeApplyShaderRenderEvent

### Community 380 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, GUNSHIP, SHOCK, SWORDSMEN

### Community 384 - "Missile"
Cohesion: 0.29
Nodes (4): Missile, ARROWS, NONE, SHOT

### Community 421 - "Cited Findings"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?

### Community 431 - "net.minecraft.core.Direction"
Cohesion: 0.17
Nodes (4): Land, Result, SiegeSurvey, Wall

### Community 443 - "net.minecraft.world.level.block.entity.BlockEntity"
Cohesion: 0.08
Nodes (4): ConquestContent, Oar, Sail, MarkerBlockEntity

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **435 isolated node(s):** `FOOT`, `RANGED`, `MOUNTED`, `ENGINE`, `NONE` (+430 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1567 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **261 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `rafasconquest.battle.BattlePlan.Point`, `net.minecraft.nbt.CompoundTag`, `ChunkWait.java`, `org.junit.jupiter.api.Test`, `.shift`, `net.minecraft.core.BlockPos`, `ConquestEvents`, `Point`, `PveCommands.java`, `map`, `.isOurs`, `.view`, `.view`, `CityGridBuilder.java`, `org.spongepowered.asm.mixin.Mixin`, `BattleShips`, `PveCommands`, `.roadStart`, `BaseMap`, `.generate`, `SiegeWorks`, `net.minecraft.core.Direction`, `.postChunk`, `BattleSounds`, `Phase`, `RecruitsBattle.java`, `Machine`, `.feudFight`, `WarCouncil`, `net.minecraft.world.item.ItemStack`, `UnitOrder`, `.passOnOrders`, `GrappleHook`, `.map`, `Arm`, `.passOnOrders`, `BaseMapStore`?**
  _High betweenness centrality (0.069) - this node is a cross-community bridge._
- **What connects `FOOT`, `RANGED`, `MOUNTED` to the rest of the system?**
  _435 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `rafasconquest.battle.BattlePlan.Point` be split into smaller, more focused modules?**
  _Cohesion score 0.05901639344262295 - nodes in this community are weakly interconnected._
- **Why does `FieldBattle` connect `FieldBlock` to `Bodies`, `rafasconquest.battle.BattlePlan.Point`, `.deploy`, `org.junit.jupiter.api.Test`, `PveCommands`, `.passOnOrders`, `Machine`, `BattleType`, `Point`, `PveCommands.java`, `BattleMap.java`, `Morale`, `.passOnOrders`, `org.jetbrains.annotations.Nullable`, `list`, `RecruitsBattle`, `Call`?**
  _High betweenness centrality (0.050) - this node is a cross-community bridge._
- **Should `.render` be split into smaller, more focused modules?**
  _Cohesion score 0.1039136302294197 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._