# Graph Report - RafasConquest  (2026-11-21)

## Corpus Check
- 694 files · ~567,136 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 8676 nodes · 28203 edges · 442 communities (162 shown, 280 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3527 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `b14dd271`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- .state
- BattlePlan
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
- .world
- .render
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- Peerage
- CityHud
- Tier
- net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
- .place
- City
- CityLayout.java
- .allSitesInBox
- BattleBannerBlockEntity
- BattleShips
- PveCommands
- .plan
- CityPlan
- Characters (holders, skills, prowess, traits)
- Ranks
- WaterSwell
- .findGenerationPoint
- CapeCurio.java
- net.minecraft.client.gui.GuiGraphics
- net.minecraft.network.FriendlyByteBuf
- SiegeWorks
- org.joml.Matrix4f
- net.minecraft.world.phys.Vec3
- Personality
- SeaLanes
- .send
- .postChunk
- LabelLayer
- Kit
- .onRenderLevel
- Homeland
- CityStyles
- net.minecraft.server.level.ServerLevel
- .groundCoversEveryTent
- CityTerrain
- Faction
- .world
- .hash
- net.minecraft.server.MinecraftServer
- Bodies
- StyleLibrary
- CityGridBuilder
- Talk.java
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
- ConquestNetwork
- CityGridBuilder.java
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- Candidate
- RoadLayer
- Recruiter
- UnitMode
- Terrain
- TechTreeScreen
- Roads
- Calibration
- DialogueScreen
- CityPlates
- HorizontalStretch
- ConquestWorldScreen
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- FactionInfoScreen
- Person
- UnitOrder
- .passOnOrders
- Land
- FaceOffScreen
- How Total War calculates battles between units
- .place
- .map
- CityPanel
- NationScreen
- .wallSegments
- PoliticalStyle
- Arm
- Trait
- Size
- MarkerScreen
- GameSpeed
- GrandStrategyScreen
- CityStock
- Cloth
- Quests
- Duty
- DayClockTest
- MapContext
- City
- CityManagerScreen
- TitleOwnersScreen
- BattleWallBannerBlock
- .shift
- ConquestContent.java
- Hull
- GateBlockEntity
- net.minecraft.world.phys.shapes.VoxelShape
- SeaCalm
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .choose
- BackBannerLayer.java
- .inCity
- OrganizationScreen
- .draw
- Barracks
- CLAUDE.md
- net.minecraft.server.level.ServerPlayer
- CountryStructure
- UnitCards
- BattleBannerBlock.java
- Call
- net.minecraft.sounds.SoundEvent
- org.spongepowered.asm.mixin.Mixin
- .deploy
- ChunkWait.java
- .boundsAt
- RecruitsBattle.java
- DistantHorizonsArmies.java
- /pve debug commands
- ConquestNetwork
- net.minecraft.network.RegistryFriendlyByteBuf
- com.mojang.blaze3d.vertex.PoseStack
- PetitionScreen
- DistantHorizonsArmies
- Ride
- .noble
- Determinism rules
- BodiesTest
- .say
- ArmySpawnScreen
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Countryside
- RecruitFigures.java
- ClientPolitics
- rafasconquest.battle.BattlePlan.Point
- Tier
- FarFlags
- DistantArmyRenderer
- Resource
- Clash
- BattleType
- PoliticsTest
- GrappleHook
- .onLogout
- Body
- StrategyRelief
- .onLevelLoad
- .at
- Wind
- CombatMode
- Override
- BattleKeepInventory
- BlockGrid
- Strategy
- BoardScreen
- WeaponSkills
- ProvinceMap
- Deflects
- ShipHulls.java
- Profiling run sheet (baseline)
- Chunks
- .soundHorn
- Override
- WarCamp.java
- .findGenerationPoint
- RecruitsBattle
- GateScreen
- TitleLayer
- .measure
- net.minecraft.client.Minecraft
- TitleMap
- Role
- Kind
- BattleSnapshot
- ClothRenderer
- Build
- .paint
- TitleStyle
- ClientScreens
- .generate
- ShareLiveTest
- .onClientExtensions
- FleetRole
- .load
- Phase
- Machine
- SwingModes
- Piece
- ConquestGameRules
- .order
- 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel)
- CityTerrainPiece
- OarFigures
- 5. Water/ocean waves (sea swell, foam, boats on waves)
- 7. Client tick, stutter and sound
- 8. Lessons from performance mods that a content mod can apply
- Shape
- BaseMapPart
- WorldStretch
- FormationSnapshot
- FieldBlock
- Task
- NobleDistrictTest
- net.neoforged.bus.api.SubscribeEvent
- .onRegisterRenderers
- Kind
- Ladder
- ShipKind
- ClientMapCache
- GrandStrategyDateTest.java
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- Cited Findings
- net.minecraft.core.Direction
- ConquestContent
- .sendTalk

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 319 edges
2. `FieldBlock` - 250 edges
3. `FieldBattle` - 246 edges
4. `Point` - 225 edges
5. `BaseMap` - 150 edges
6. `ConquestNetwork` - 145 edges
7. `FieldArmy` - 142 edges
8. `Lands` - 94 edges
9. `BattleScreen` - 89 edges
10. `GrandStrategyScreen` - 87 edges

## Surprising Connections (you probably didn't know these)
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/map/BaseMap.java
- `Stage 3: client frame time (2–3 jars, only where stage 0 showed a cost)` --references--> `BaseMapLayer`  [INFERRED]
  reports/RafasConquest optimization plan 2026-11-18.md → src/main/java/rafasconquest/client/map/BaseMapLayer.java
- `Cited Findings` --references--> `MapView`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/MapView.java
- `1. How we avoid breaking anything` --references--> `DistantShipsTest`  [INFERRED]
  reports/RafasConquest optimization plan 2026-11-18.md → src/test/java/rafasconquest/client/DistantShipsTest.java
- `Battle code costs 0.2 ms; the multi-second freezes come from blocking` --references--> `RecruitsArrowCleanerMixin`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/mixin/RecruitsArrowCleanerMixin.java

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

## Communities (442 total, 280 thin omitted)

### Community 0 - ".state"
Cohesion: 0.06
Nodes (12): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+4 more)

### Community 2 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.06
Nodes (10): StylePiece, BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece (+2 more)

### Community 3 - "org.junit.jupiter.api.Test"
Cohesion: 0.05
Nodes (8): DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, GroundPlanTest, Land, WindTest, OarRowingTest, GameSpeedTest

### Community 4 - "MediumCannonEntity"
Cohesion: 0.06
Nodes (8): CannonAim, Ball, CannonShots, MediumCannonEntity, Order, Shot, ROUNDSHOT, CannonAimTest

### Community 5 - "BattleScreen"
Cohesion: 0.07
Nodes (4): BattleScreen, Entry, Menu, UnitInfo

### Community 6 - "Front"
Cohesion: 0.08
Nodes (5): Feud, Lord, Feuds, Lands, Claim

### Community 7 - "net.minecraft.core.BlockPos"
Cohesion: 0.05
Nodes (10): PlannedTent, Saved, WarCamp, CannonControl, CourtSet, Seat, Style, CharacterMarkers (+2 more)

### Community 8 - "Politics"
Cohesion: 0.06
Nodes (12): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Event, Front, Muster (+4 more)

### Community 9 - "net.minecraft.world.level.Level"
Cohesion: 0.05
Nodes (10): GateBlock, GrandStrategyBlock, MediumCannonBlock, OarBlock, QuestBoardBlock, SailBlock, StrategyBoardBlock, WorldMapItem (+2 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.07
Nodes (29): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings (+21 more)

### Community 13 - "Point"
Cohesion: 0.04
Nodes (21): Point, Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate (+13 more)

### Community 14 - "PveCommands.java"
Cohesion: 0.08
Nodes (31): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+23 more)

### Community 15 - "BattleMap.java"
Cohesion: 0.05
Nodes (3): Battle map developer web page, Morale, rout and rally, SwingModesTest

### Community 16 - "Morale"
Cohesion: 0.28
Nodes (6): Morale, State, RALLIED, ROUTED, SHATTERED, STEADY

### Community 17 - "RafasConquest Project Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - ".world"
Cohesion: 0.13
Nodes (5): Names, Honour, Honours, RealmPlannerTest, World

### Community 20 - ".render"
Cohesion: 0.14
Nodes (4): CharacterScreen, Retainer, Spot, Portraits

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 23 - "Peerage"
Cohesion: 0.05
Nodes (25): Advancement, Boards, Entry, View, NobleTitle, COUNT, DUKE, EMPEROR (+17 more)

### Community 25 - "Tier"
Cohesion: 0.17
Nodes (6): Nation, Nations, Plan, Seats, NationsTest, World

### Community 27 - ".place"
Cohesion: 0.14
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 28 - "City"
Cohesion: 0.11
Nodes (3): SeatCities, SeatCitiesTest, World

### Community 29 - "CityLayout.java"
Cohesion: 0.06
Nodes (6): CityPlacement, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 30 - ".allSitesInBox"
Cohesion: 0.07
Nodes (8): CityLayout, District, Site, WorldSettings, DistrictKey, Townsfolk, CityLayoutTest, CitySizesTest

### Community 31 - "BattleBannerBlockEntity"
Cohesion: 0.13
Nodes (4): BattleBannerBlockEntity, Banner, PlacedBanners, SaveGuard

### Community 32 - "BattleShips"
Cohesion: 0.12
Nodes (4): BattleShips, Helm, Ram, SableLink

### Community 34 - ".plan"
Cohesion: 0.12
Nodes (7): Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 36 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.03
Nodes (52): Ambushes from woods, Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Character page, Characters (holders, skills, prowess, traits) (+44 more)

### Community 37 - "Ranks"
Cohesion: 0.06
Nodes (22): Jobs (cavalry, footman, archer, shieldman, heavy infantry), BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job (+14 more)

### Community 38 - "WaterSwell"
Cohesion: 0.12
Nodes (9): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Columns, SableSeen, Made (+1 more)

### Community 39 - ".findGenerationPoint"
Cohesion: 0.07
Nodes (6): SlopeTile, HarbourCut, CitySlopeStructure, Frame, Harbour, HarbourCutTest

### Community 40 - "CapeCurio.java"
Cohesion: 0.10
Nodes (3): CapeCurio, ClothItemRenderer, BattleStandardItem

### Community 41 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.05
Nodes (4): InventoryStatsButton, BaseMap, BaseMapStore, Ready

### Community 42 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.06
Nodes (10): BattleSpot, CityState, FactionInfoReply, GateView, MarkerView, PeerageView, PoliticsView, UnitsRequest (+2 more)

### Community 43 - "SiegeWorks"
Cohesion: 0.10
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 44 - "org.joml.Matrix4f"
Cohesion: 0.08
Nodes (14): DistantDepth, Arrow, ArrowVolleys, Kind, ARROW, BALL, BIG_BALL, BIG_ROCK (+6 more)

### Community 45 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.09
Nodes (12): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, BattleSounds, ChargeSound, DinSound (+4 more)

### Community 46 - "Personality"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.22
Nodes (6): Lane, Leg, Port, SeaLanes, Ship, Voyage

### Community 48 - ".send"
Cohesion: 0.10
Nodes (7): CannonShot, FarBanners, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel

### Community 50 - ".postChunk"
Cohesion: 0.08
Nodes (6): BarracksPeople, CityBanners, CityGuards, ConquestServer, StructureLink, PoiPrecomputer

### Community 51 - "LabelLayer"
Cohesion: 0.19
Nodes (3): Hit, LabelLayer, LabelShape

### Community 52 - "Kit"
Cohesion: 0.10
Nodes (20): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+12 more)

### Community 53 - ".onRenderLevel"
Cohesion: 0.08
Nodes (12): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, BattleSpotRing, DistantOcclusion, Mode (+4 more)

### Community 55 - "Homeland"
Cohesion: 0.09
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 59 - "CityTerrain"
Cohesion: 0.06
Nodes (18): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+10 more)

### Community 60 - "Faction"
Cohesion: 0.04
Nodes (21): Cited Findings, RecruitsBridge, RecruitsFactions, ConquestSavedData, MapPreview, Faction, FactionType, BANDIT (+13 more)

### Community 61 - ".world"
Cohesion: 0.10
Nodes (8): Hook, Kind, FAVOUR, SECRET, Realm, Succession, Taking, SuccessionFormatTest

### Community 62 - ".hash"
Cohesion: 0.06
Nodes (14): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, NobleAnnex, OriginRoller, Roll (+6 more)

### Community 63 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.07
Nodes (8): Cited Findings, FeudDuel, Goal, MusterBars, PoliticsSync, War, Wars, Politics.Front

### Community 65 - "StyleLibrary"
Cohesion: 0.15
Nodes (4): Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - "CityGridBuilder"
Cohesion: 0.07
Nodes (10): CityGridBuilder, Ground, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA (+2 more)

### Community 70 - "Row"
Cohesion: 0.12
Nodes (3): Fort, Stronghold, Row

### Community 71 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 72 - "DistantShips"
Cohesion: 0.10
Nodes (7): DistantShips, Far, HullKey, Riding, Want, DeckLiftTest, DistantShipsTest

### Community 73 - "Age"
Cohesion: 0.12
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 74 - "MapLands"
Cohesion: 0.09
Nodes (6): Cited Findings, Gaps, Inferences, Takeaway, Where are full scans, nested loops, repeated lookups and per-tick allocations?, MapLands

### Community 75 - "WarCouncil"
Cohesion: 0.13
Nodes (7): Facts, Officer, Said, Talk, WarCouncil, Option, WarCouncilPagesTest

### Community 76 - ".step"
Cohesion: 0.11
Nodes (5): FigureMotion, Ground, Match, State, FigureMotionTest

### Community 77 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.09
Nodes (8): WarHorn, AddonPieces, Armoury, Outfit, Style, ItemFlags, RankKits, ArmouryTest

### Community 78 - "RecruitFigures"
Cohesion: 0.07
Nodes (15): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD, Capture (+7 more)

### Community 79 - "ConquestNetwork"
Cohesion: 0.06
Nodes (25): Bark, CannonFire, CannonLay, CannonLeave, CannonTaken, ChargeHits, CityClose, CityFlatten (+17 more)

### Community 80 - "CityGridBuilder.java"
Cohesion: 0.08
Nodes (9): Enlisted players, Gold church, Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter, The war horn, GoldChurch (+1 more)

### Community 81 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.18
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 82 - "Candidate"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "RoadLayer"
Cohesion: 0.08
Nodes (23): 2. Where the 10 November gameplan stands, 3. New costs since 10 November, 4. The stages, 5. Order and size, RafasConquest optimization plan (18 November 2026), Stage 0: measure (one jar, no behaviour change), Stage 1: small, same-behaviour fixes (2 jars: server, then client), Stage 2: flatten server spikes (2–3 jars) (+15 more)

### Community 84 - "Recruiter"
Cohesion: 0.12
Nodes (4): RecruiterRenderer, Recruiter, Offer, Recruitment

### Community 85 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 88 - "Roads"
Cohesion: 0.11
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 89 - "Calibration"
Cohesion: 0.19
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 95 - "ConquestWorldScreen"
Cohesion: 0.25
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 96 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.06
Nodes (15): BuiltInFiles, TypeToken<Map<String, String>>, Outcome, EDITED_KEPT, OLD_REPLACED, SAME, UPDATED, WRITTEN (+7 more)

### Community 99 - "FactionInfoScreen"
Cohesion: 0.08
Nodes (7): ClientBarks, Line, FactionInfoScreen, Line, FactionBanner, Layer, FactionBannerTest

### Community 100 - "Person"
Cohesion: 0.15
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 102 - "UnitOrder"
Cohesion: 0.11
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 103 - ".passOnOrders"
Cohesion: 0.06
Nodes (5): Cited Findings, Afloat, Figure, GunMark, Live

### Community 105 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 107 - "How Total War calculates battles between units"
Cohesion: 0.11
Nodes (17): 1. Combat is per soldier, not per unit, 1. Flank and rear shock, 2. Does the swing land?, 2. Nearby rout (fright), 3. Army-wide losses, 3. How much damage the hit does, 4. Charges, 4. Shattering (+9 more)

### Community 109 - ".map"
Cohesion: 0.08
Nodes (5): Garrison, Army, GrandStrategy, View, GarrisonTest

### Community 110 - "CityPanel"
Cohesion: 0.17
Nodes (6): CityPanel, Mode, DISMISS, MARCH, MUSTER, NONE

### Community 111 - "NationScreen"
Cohesion: 0.08
Nodes (6): Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 112 - ".wallSegments"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 114 - "Arm"
Cohesion: 0.06
Nodes (10): Arm, Kind, ENGINE, FOOT, MOUNTED, RANGED, Missile, ARROWS (+2 more)

### Community 115 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 116 - "Size"
Cohesion: 0.11
Nodes (6): Post, Propulsion, OARS, SAIL, STEAM, Size

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.07
Nodes (4): GrandStrategyScreen, Marcher, Spot, GameSpeedRequest

### Community 121 - "Cloth"
Cohesion: 0.10
Nodes (5): Cloth, Solid, ClothTest, StretchTest, WoolTest

### Community 124 - "Quests"
Cohesion: 0.14
Nodes (5): DataFiles, Kind, Quests, Target, PeerageTest

### Community 125 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 126 - "DayClockTest"
Cohesion: 0.22
Nodes (3): DayClock, Step, DayClockTest

### Community 127 - "MapContext"
Cohesion: 0.08
Nodes (13): Cited Findings, BaseMapLayer, MapContext, LegendEntry, MapLayer, Symbol, MapView, Place (+5 more)

### Community 130 - "TitleOwnersScreen"
Cohesion: 0.19
Nodes (3): ClientPeerage, Owner, TitleOwnersScreen

### Community 132 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 134 - "Hull"
Cohesion: 0.25
Nodes (3): Hull, Ramming, RammingTest

### Community 136 - "GateBlockEntity"
Cohesion: 0.07
Nodes (9): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+1 more)

### Community 138 - "SeaCalm"
Cohesion: 0.16
Nodes (5): Sea, Depths, SeaCalm, Strengths, SeaCalmTest

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.12
Nodes (15): 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 6. Sodium (and Iris) compatibility, Cited Findings, Cited Findings, Cited Findings, Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest), Gaps (+7 more)

### Community 141 - ".choose"
Cohesion: 0.14
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 142 - "BackBannerLayer.java"
Cohesion: 0.13
Nodes (3): BackBannerLayer, BackBanners, MouthLayer

### Community 151 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.06
Nodes (7): Camera, CameraView, CityManager, PlayerStats, View, Retinues, RetinuesTest

### Community 156 - "BattleBannerBlock.java"
Cohesion: 0.08
Nodes (7): BattleBannerBlock, FlagBlock, Mount, BARS, BLOCK, FENCE, WALL

### Community 157 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 158 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 160 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.06
Nodes (22): Inferences, Cited Findings, Cited Findings, ArmorLegsMixin, ArrowSaveMixin, BetterCombatOneHandMixin, ChunkMapAccess, ChunkMapCameraMixin (+14 more)

### Community 164 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 165 - "RecruitsBattle.java"
Cohesion: 0.03
Nodes (6): Kind, CLOSE, OPEN, RAM, ClientModEvents, RafasConquest

### Community 167 - "DistantHorizonsArmies.java"
Cohesion: 0.09
Nodes (6): Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Takeaway, Api, DistantGeneration

### Community 175 - "ConquestNetwork"
Cohesion: 0.07
Nodes (8): CameraAt, CityView, FigureStrike, HornOrders, SetSetting, TalkChoice, TeleportTo, Entry

### Community 178 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.07
Nodes (8): BannerArt, BattleBannerRenderer, Carried, Layer, EpicKnightsCloaks, Held, GateRenderer, MarkerRenderer

### Community 186 - "PetitionScreen"
Cohesion: 0.10
Nodes (3): FactionMapScreen, PetitionScreen, AbstractSliderButton

### Community 203 - "DistantHorizonsArmies"
Cohesion: 0.08
Nodes (9): 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), Cited Findings, Gaps, Inferences, Takeaway, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+1 more)

### Community 205 - "Ride"
Cohesion: 0.14
Nodes (3): Ride, Swell, SwellTest

### Community 206 - ".noble"
Cohesion: 0.29
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 219 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.07
Nodes (29): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps (+21 more)

### Community 223 - "Countryside"
Cohesion: 0.13
Nodes (12): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+4 more)

### Community 224 - "RecruitFigures.java"
Cohesion: 0.04
Nodes (4): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), Wind

### Community 226 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.09
Nodes (7): FieldRules, Rates, Side, FLANK, FRONT, REAR, UnitCardsTest

### Community 227 - "Tier"
Cohesion: 0.18
Nodes (6): Tier, COUNTY, DUCHY, EMPIRE, KINGDOM, Nation

### Community 228 - "FarFlags"
Cohesion: 0.24
Nodes (3): FarFlags, Placed, Banner

### Community 229 - "DistantArmyRenderer"
Cohesion: 0.06
Nodes (6): Cited Findings, DistantArmyRenderer, Rects, ShipSprite, Corners, FigureBatchesTest

### Community 231 - "Resource"
Cohesion: 0.20
Nodes (9): Resource, FOOD, GOLD, HIGH_GRADE, HOUSING, LUXURY, POPULATION, RAW (+1 more)

### Community 232 - "Clash"
Cohesion: 0.24
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 234 - "BattleType"
Cohesion: 0.14
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 237 - ".onLogout"
Cohesion: 0.08
Nodes (8): 1. How we avoid breaking anything, Core-function check after every stage (about 10 minutes in game), ClientTownsfolk, Folk, Throw, ThrownFigures, Walk, ClientTownsfolkTest

### Community 238 - "Body"
Cohesion: 0.08
Nodes (26): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+18 more)

### Community 241 - ".onLevelLoad"
Cohesion: 0.10
Nodes (4): ConquestConfig, ConquestRuntime, CountryIndex, SettlementBiomes

### Community 248 - "Override"
Cohesion: 0.24
Nodes (3): Land, Land, Land

### Community 252 - "Strategy"
Cohesion: 0.11
Nodes (12): Formation, GREAT_SHIELD, GREAT_SPEAR, Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE (+4 more)

### Community 255 - "ProvinceMap"
Cohesion: 0.17
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 257 - "ShipHulls.java"
Cohesion: 0.06
Nodes (3): GatePlanksBlock, SablePhysics, GateGeometry

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 267 - ".soundHorn"
Cohesion: 0.40
Nodes (5): HornCall, ATTACKING, MOVING, RALLYING, RETREATING

### Community 281 - "RecruitsBattle"
Cohesion: 0.03
Nodes (14): Card, Enlisted, GeneralTask, Officers, RecruitsBattle, Role, BEARER, GENERAL (+6 more)

### Community 285 - "net.minecraft.client.Minecraft"
Cohesion: 0.13
Nodes (4): FarGun, Model, OarFigures, Rowing

### Community 286 - "TitleMap"
Cohesion: 0.14
Nodes (4): TitleMap, SiegeSurveyTest, Land, TitleMapTest

### Community 288 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "ClothRenderer"
Cohesion: 0.13
Nodes (6): ClothRenderer, Fabric, Far, Kind, Live, OffTheBackTest

### Community 292 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 297 - ".generate"
Cohesion: 0.12
Nodes (4): BaseMapGenerator, Heights, BiomePalette, Entry

### Community 304 - "FleetRole"
Cohesion: 0.33
Nodes (6): FleetRole, FRONT, LINE, RESERVE, SCREEN, SUPPORT

### Community 309 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 313 - "Machine"
Cohesion: 0.07
Nodes (7): Machine, Role, MANTLET, RAM, SHOOTER, TOWER, ShipEngineBlock

### Community 319 - "Piece"
Cohesion: 0.12
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 324 - ".order"
Cohesion: 0.40
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

### Community 326 - "1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel)"
Cohesion: 0.40
Nodes (5): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), Cited Findings, Gaps, Inferences, Takeaway

### Community 330 - "OarFigures"
Cohesion: 0.11
Nodes (9): Batch, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE, FigureBatches (+1 more)

### Community 332 - "5. Water/ocean waves (sea swell, foam, boats on waves)"
Cohesion: 0.40
Nodes (5): 5. Water/ocean waves (sea swell, foam, boats on waves), Cited Findings, Gaps, Inferences, Takeaway

### Community 334 - "7. Client tick, stutter and sound"
Cohesion: 0.40
Nodes (5): 7. Client tick, stutter and sound, Cited Findings, Gaps, Inferences, Takeaway

### Community 336 - "8. Lessons from performance mods that a content mod can apply"
Cohesion: 0.40
Nodes (5): 8. Lessons from performance mods that a content mod can apply, Cited Findings, Gaps, Inferences, Takeaway

### Community 338 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 350 - "FormationSnapshot"
Cohesion: 0.08
Nodes (19): Engine, FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL (+11 more)

### Community 355 - "FieldBlock"
Cohesion: 0.04
Nodes (8): The core change: morale becomes a sum, FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 357 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 376 - "Kind"
Cohesion: 0.50
Nodes (3): Kind, FOOT, HORSE

### Community 379 - "Ladder"
Cohesion: 0.50
Nodes (4): Ladder, MILITARY, NOBLE, NONE

### Community 380 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, GUNSHIP, SHOCK, SWORDSMEN

### Community 421 - "Cited Findings"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?

### Community 431 - "net.minecraft.core.Direction"
Cohesion: 0.10
Nodes (6): Land, Result, SiegeSurvey, Wall, CityGates, Done

### Community 443 - "ConquestContent"
Cohesion: 0.07
Nodes (5): ConquestContent, Oar, Sail, MarkerBlockEntity, MarkerSpec

### Community 453 - ".sendTalk"
Cohesion: 0.12
Nodes (4): BoardView, GrandStrategyView, StatsView, TalkView

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **443 isolated node(s):** `POPULATION`, `WORKERS`, `HOUSING`, `FOOD`, `LUXURY` (+438 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1581 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **280 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `.state`, `.thrown`, `net.minecraft.nbt.CompoundTag`, `org.junit.jupiter.api.Test`, `.onServerTick`, `net.minecraft.core.BlockPos`, `.soundHorn`, `Point`, `.inCity`, `.isOurs`, `net.minecraft.server.level.ServerPlayer`, `org.spongepowered.asm.mixin.Mixin`, `PveCommands`, `BattleShips`, `ChunkWait.java`, `RecruitsBattle.java`, `net.minecraft.client.gui.GuiGraphics`, `.generate`, `SiegeWorks`, `net.minecraft.world.phys.Vec3`, `net.minecraft.core.Direction`, `net.minecraft.network.RegistryFriendlyByteBuf`, `Phase`, `net.minecraft.server.level.ServerLevel`, `Machine`, `net.minecraft.server.MinecraftServer`, `WarCouncil`, `net.minecraft.world.item.ItemStack`, `Shape`, `UnitMode`, `rafasconquest.battle.BattlePlan.Point`, `UnitOrder`, `.passOnOrders`, `GrappleHook`, `.map`?**
  _High betweenness centrality (0.098) - this node is a cross-community bridge._
- **What connects `POPULATION`, `WORKERS`, `HOUSING` to the rest of the system?**
  _443 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `.state` be split into smaller, more focused modules?**
  _Cohesion score 0.06253652834599649 - nodes in this community are weakly interconnected._
- **Why does `Point` connect `Point` to `.state`, `BattlePlan`, `org.junit.jupiter.api.Test`, `.shift`, `.embody`, `list`, `RecruitsBattle`, `PveCommands`, `.deploy`, `RecruitsBattle.java`, `SiegeWorks`, `net.minecraft.world.phys.Vec3`, `net.minecraft.server.level.ServerLevel`, `net.minecraft.server.MinecraftServer`, `.deployment`, `FieldBlock`, `.passOnOrders`, `.map`, `BlockGrid`?**
  _High betweenness centrality (0.044) - this node is a cross-community bridge._
- **Should `BattlePlan` be split into smaller, more focused modules?**
  _Cohesion score 0.10804597701149425 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._