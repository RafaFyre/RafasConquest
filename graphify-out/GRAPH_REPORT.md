# Graph Report - RafasConquest  (2026-11-21)

## Corpus Check
- 691 files · ~564,111 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 8611 nodes · 27968 edges · 420 communities (154 shown, 266 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3509 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `554e0145`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- .state
- FieldArmy
- net.minecraft.nbt.CompoundTag
- org.junit.jupiter.api.Test
- MediumCannonEntity
- BattleScreen
- Front
- net.minecraft.core.BlockPos
- Politics
- net.minecraft.world.level.Level
- net.neoforged.bus.api.SubscribeEvent
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Point
- PveCommands.java
- map
- Morale
- RafasConquest Project Site
- .world
- net.minecraft.world.entity.Entity
- net.minecraft.world.level.block.Block
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- Peerage
- CityHud
- Tier
- net.minecraft.core.Vec3i
- .place
- City
- CityLayout.java
- .allSitesInBox
- .column
- BattleShips
- PveCommands
- War
- CityPlan
- Characters (holders, skills, prowess, traits)
- Ranks
- WaterSwell
- .build
- Faction
- net.minecraft.client.gui.GuiGraphics
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
- RecruitsBattle.java
- .siege
- CityTerrain
- ConquestSavedData
- .world
- WorldSettings
- net.minecraft.server.MinecraftServer
- Bodies
- StyleLibrary
- CityGridBuilder
- Ranks
- BattlePlan
- Row
- School
- DistantShips
- TechTreeScreen
- MapLands
- WarCouncil
- .step
- net.minecraft.world.item.ItemStack
- RecruitFigures
- net.minecraft.server.level.ServerPlayer
- CityGridBuilder.java
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- Candidate
- RoadLayer
- Recruiter
- Enlisted
- Terrain
- .view
- Roads
- BlockPos
- DialogueScreen
- BaseMap
- net.minecraft.resources.ResourceLocation
- .lay
- .of
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- CityHud.java
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
- BaseMapStore
- Cloth
- Quests
- .findGenerationPoint
- DayClockTest
- MapContext
- Body
- CityManagerScreen
- TitleOwnersScreen
- .deploy
- .shift
- ConquestContent
- Hull
- GateBlockEntity
- net.minecraft.world.level.block.state.BlockState
- SeaCalm
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .choose
- ClientEvents.java
- Kind
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
- WeaponSkills.java
- /pve debug commands
- ConquestNetwork
- .build
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
- rafasconquest.battle.BattlePlan.Point
- org.joml.Vector3d
- net.minecraft.world.level.saveddata.SavedData
- DistantHorizonsLink
- Override
- .onRegisterClientCommands
- BattleType
- PoliticsTest
- GrappleHook
- ClientTownsfolk
- Body
- StrategyRelief
- .onLevelLoad
- .at
- Wind
- CombatMode
- Kind
- net.neoforged.fml.common.EventBusSubscriber
- BlockGrid
- Doctrine
- BoardScreen
- WeaponSkills
- ProvinceMap
- .moraleOf
- SablePhysics
- Profiling run sheet (baseline)
- CameraView
- .hash
- .soundHorn
- Override
- PoiPrecomputer.java
- .findGenerationPoint
- RecruitsBattle
- .aNewCardsFileAddsACardThatDeploysByItsKind
- TitleLayer
- .measure
- net.minecraft.client.Minecraft
- TitleMap
- .touches
- Kind
- BattleSnapshot
- .offTheBack
- .candidateChunk
- WoolTest
- .walks
- ClientScreens
- .generate
- ShareLiveTest
- ClientBarks
- CannonShots
- Phase
- Machine
- SwingModes
- Plan
- DistantGeneration
- .order
- net.minecraft.nbt.CompoundTag
- OarFigures
- FormationSnapshot
- FieldBlock
- Task
- ShipKind
- .fire
- Cited Findings
- net.minecraft.core.Direction
- MediumCannonEntity.java
- .sendTalk

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 320 edges
2. `FieldBlock` - 250 edges
3. `FieldBattle` - 246 edges
4. `Point` - 225 edges
5. `BaseMap` - 150 edges
6. `FieldArmy` - 142 edges
7. `ConquestNetwork` - 139 edges
8. `Lands` - 94 edges
9. `BattleScreen` - 89 edges
10. `GrandStrategyScreen` - 87 edges

## Surprising Connections (you probably didn't know these)
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/map/BaseMap.java
- `1. How we avoid breaking anything` --references--> `DistantShipsTest`  [INFERRED]
  reports/RafasConquest optimization plan 2026-11-18.md → src/test/java/rafasconquest/client/DistantShipsTest.java
- `Battle code costs 0.2 ms; the multi-second freezes come from blocking` --references--> `RecruitsArrowCleanerMixin`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/mixin/RecruitsArrowCleanerMixin.java
- `Stage 3: client frame time (2–3 jars, only where stage 0 showed a cost)` --references--> `BaseMapLayer`  [INFERRED]
  reports/RafasConquest optimization plan 2026-11-18.md → src/main/java/rafasconquest/client/map/BaseMapLayer.java
- `Cited Findings` --references--> `MapView`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/MapView.java

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

## Communities (420 total, 266 thin omitted)

### Community 0 - ".state"
Cohesion: 0.05
Nodes (14): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, BattleMap (+6 more)

### Community 1 - "FieldArmy"
Cohesion: 0.08
Nodes (3): FieldArmy, Event, Order

### Community 2 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.06
Nodes (10): BarracksPiece, CityRoadPiece, CityTerrainPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece (+2 more)

### Community 3 - "org.junit.jupiter.api.Test"
Cohesion: 0.05
Nodes (10): DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, WindTest, GrandStrategyDateTest, OarRowingTest, ArmouryTest, RetinuesTest (+2 more)

### Community 6 - "Front"
Cohesion: 0.10
Nodes (7): Clash, Feud, Kind, DUEL, SKIRMISH, Lord, Feuds

### Community 7 - "net.minecraft.core.BlockPos"
Cohesion: 0.09
Nodes (5): PlannedTent, WarCamp, CourtSet, Seat, Style

### Community 8 - "Politics"
Cohesion: 0.06
Nodes (14): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Lands, Stronghold, Claim (+6 more)

### Community 9 - "net.minecraft.world.level.Level"
Cohesion: 0.06
Nodes (7): Sea, GrandStrategyBlock, MediumCannonBlock, OarBlock, SailBlock, StrategyBoardBlock, WorldMapItem

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (24): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings, Cited Findings (+16 more)

### Community 13 - "Point"
Cohesion: 0.05
Nodes (19): Point, Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate (+11 more)

### Community 14 - "PveCommands.java"
Cohesion: 0.07
Nodes (32): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+24 more)

### Community 15 - "map"
Cohesion: 0.03
Nodes (6): Battle map developer web page, Morale, rout and rally, GatePlanksBlock, SaveGuard, RafasConquest, SwingModesTest

### Community 16 - "Morale"
Cohesion: 0.28
Nodes (6): Morale, State, RALLIED, ROUTED, SHATTERED, STEADY

### Community 17 - "RafasConquest Project Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - ".world"
Cohesion: 0.18
Nodes (4): Demographics, Share, RealmPlannerTest, World

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 23 - "Peerage"
Cohesion: 0.08
Nodes (10): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+2 more)

### Community 25 - "Tier"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 27 - ".place"
Cohesion: 0.14
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 28 - "City"
Cohesion: 0.09
Nodes (8): City, Grid, SeatCities, CityIndex, CountrysideTest, World, SeatCitiesTest, World

### Community 29 - "CityLayout.java"
Cohesion: 0.05
Nodes (6): CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 30 - ".allSitesInBox"
Cohesion: 0.10
Nodes (5): CityLayout, District, Site, WorldSettings, CityLayoutTest

### Community 31 - ".column"
Cohesion: 0.12
Nodes (7): Column, Kind, NONE, PLATE, SLOPE, CityTerrainTest, NobleAnnexTest

### Community 32 - "BattleShips"
Cohesion: 0.10
Nodes (5): BattleShips, Afloat, Helm, Ram, SableLink

### Community 34 - "War"
Cohesion: 0.08
Nodes (4): Army, Goal, PoliticsSync, War

### Community 35 - "CityPlan"
Cohesion: 0.12
Nodes (4): CityPlan, Piece, Building, NobleDistrictTest

### Community 36 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.03
Nodes (55): Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards (+47 more)

### Community 37 - "Ranks"
Cohesion: 0.10
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 38 - "WaterSwell"
Cohesion: 0.09
Nodes (9): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Made, WaterSwell, Swell (+1 more)

### Community 40 - "Faction"
Cohesion: 0.10
Nodes (3): RecruitsFactions, Faction, FactionInfoTest

### Community 41 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.05
Nodes (3): GateScreen, InventoryStatsButton, PetitionScreen

### Community 42 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.06
Nodes (9): BaseMapPart, BattleSpot, FactionInfoReply, PeerageView, PoliticsView, StatsView, UnitsRequest, UnitsState (+1 more)

### Community 43 - "SiegeWorks"
Cohesion: 0.07
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 44 - "org.joml.Matrix4f"
Cohesion: 0.08
Nodes (19): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, DistantDepth, Arrow, ArrowVolleys (+11 more)

### Community 45 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.08
Nodes (13): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, 7. Per-frame allocations in hot paths, Cited Findings, Cited Findings, Gaps, Gaps, Inferences, Inferences (+5 more)

### Community 46 - "Personality"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.22
Nodes (6): Lane, Leg, Port, SeaLanes, Ship, Voyage

### Community 48 - "SimpleChannel"
Cohesion: 0.07
Nodes (12): BackBanner, WorldStretch, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel (+4 more)

### Community 50 - ".postChunk"
Cohesion: 0.10
Nodes (6): Cited Findings, RecruitsBridge, BarracksPeople, CityBanners, CityGuards, PoiPrecomputer

### Community 51 - "BattleSounds"
Cohesion: 0.17
Nodes (6): BattleSounds, ChargeSound, DinSound, Queued, Shot, Source

### Community 52 - "Kit"
Cohesion: 0.10
Nodes (20): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+12 more)

### Community 55 - "MapLayer"
Cohesion: 0.15
Nodes (8): LegendEntry, MapLayer, Symbol, Place, PlaceMenuLayer, Row, TerritoryLayer, UnitLayer

### Community 57 - "RecruitsBattle.java"
Cohesion: 0.04
Nodes (3): /council <topic>, ConquestGameRules, MusterBars

### Community 59 - "CityTerrain"
Cohesion: 0.11
Nodes (11): CityTerrain, Edge, CARVE, FILL, Heights, Plan, Side, EAST (+3 more)

### Community 60 - "ConquestSavedData"
Cohesion: 0.06
Nodes (5): Cited Findings, ConquestSavedData, MapPreview, EconomyTick, ConquestSavedDataTest

### Community 61 - ".world"
Cohesion: 0.10
Nodes (8): Retainer, Hook, Kind, FAVOUR, SECRET, Succession, Taking, SuccessionFormatTest

### Community 62 - "WorldSettings"
Cohesion: 0.18
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 63 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.13
Nodes (3): Cited Findings, FeudDuel, Wars

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.12
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - "CityGridBuilder"
Cohesion: 0.10
Nodes (3): CityGridBuilder, Platform, PlaceMusicTest

### Community 67 - "Ranks"
Cohesion: 0.07
Nodes (13): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+5 more)

### Community 69 - "BattlePlan"
Cohesion: 0.07
Nodes (3): BattlePlan, Slot, BattlePlanTest

### Community 71 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 72 - "DistantShips"
Cohesion: 0.11
Nodes (6): DistantShips, Far, HullKey, Riding, Want, DeckLiftTest

### Community 73 - "TechTreeScreen"
Cohesion: 0.05
Nodes (17): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, TechTreeScreen, Age, BRONZE, GUNPOWDER, STEAM (+9 more)

### Community 75 - "WarCouncil"
Cohesion: 0.13
Nodes (7): Cited Findings, Facts, Officer, Said, Talk, WarCouncil, WarCouncilPagesTest

### Community 76 - ".step"
Cohesion: 0.07
Nodes (17): 1. How we avoid breaking anything, 2. Where the 10 November gameplan stands, 3. New costs since 10 November, 4. The stages, 5. Order and size, Core-function check after every stage (about 10 minutes in game), RafasConquest optimization plan (18 November 2026), Stage 0: measure (one jar, no behaviour change) (+9 more)

### Community 77 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.09
Nodes (7): WarHorn, BattleStandardItem, AddonPieces, Armoury, Outfit, Style, ItemFlags

### Community 78 - "RecruitFigures"
Cohesion: 0.07
Nodes (8): Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures, RecruitFiguresTest

### Community 79 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.07
Nodes (12): Bark, CannonShot, ChargeHits, CityClose, GameSpeedState, OpenBattleScreen, Place, SeaSwell (+4 more)

### Community 80 - "CityGridBuilder.java"
Cohesion: 0.06
Nodes (11): Ambushes from woods, Deploying to the land (GroundPlan), PlacedWall, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY (+3 more)

### Community 81 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.20
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 82 - "Candidate"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "RoadLayer"
Cohesion: 0.19
Nodes (11): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+3 more)

### Community 85 - "Enlisted"
Cohesion: 0.10
Nodes (14): Card, Enlisted, Shape, LINE, SQUARE, V, WEDGE, Unit (+6 more)

### Community 87 - ".view"
Cohesion: 0.11
Nodes (12): NobleTitle, COUNT, DUKE, EMPEROR, KING, KNIGHT, NONE, PeerageSync (+4 more)

### Community 88 - "Roads"
Cohesion: 0.11
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 89 - "BlockPos"
Cohesion: 0.08
Nodes (10): CannonControl, CannonFire, CannonLay, CannonLeave, CannonTaken, FarBanners, GateView, MarkerSet (+2 more)

### Community 91 - "BaseMap"
Cohesion: 0.05
Nodes (16): Character page, Court 3D scene (CourtView), Nation page, OrganizationScreen, BaseMap, Names, Organization, Organizations (+8 more)

### Community 92 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.12
Nodes (3): EpicKnightsCloaks, ArmorLegsMixin, EpicKnightsCloakMixin

### Community 95 - ".of"
Cohesion: 0.22
Nodes (3): FactionBanner, Layer, FactionBannerTest

### Community 96 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.07
Nodes (13): BuiltInFiles, TypeToken<Map<String, String>>, Outcome, EDITED_KEPT, OLD_REPLACED, SAME, UPDATED, WRITTEN (+5 more)

### Community 100 - "Person"
Cohesion: 0.11
Nodes (8): Culture, Faith, Marriages, Setter, Wedding, Hold, RealmPlanner, Seat

### Community 102 - "UnitOrder"
Cohesion: 0.11
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 103 - ".passOnOrders"
Cohesion: 0.06
Nodes (3): Figure, GunMark, Live

### Community 105 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 107 - "How Total War calculates battles between units"
Cohesion: 0.11
Nodes (17): 1. Combat is per soldier, not per unit, 1. Flank and rear shock, 2. Does the swing land?, 2. Nearby rout (fright), 3. Army-wide losses, 3. How much damage the hit does, 4. Charges, 4. Shattering (+9 more)

### Community 108 - ".place"
Cohesion: 0.15
Nodes (5): Cited Findings, Gaps, How do world generation and structure placement work, and what do they cost?, Takeaway, RoadFeature

### Community 109 - ".map"
Cohesion: 0.09
Nodes (5): Garrison, Army, GrandStrategy, View, GarrisonTest

### Community 110 - "CityPanel"
Cohesion: 0.14
Nodes (6): CityPanel, Mode, DISMISS, MARCH, MUSTER, NONE

### Community 111 - "NationScreen"
Cohesion: 0.05
Nodes (10): CharacterScreen, Spot, ClientPeerage, Camera, CourtView, Figure, NationScreen, Portraits (+2 more)

### Community 112 - ".wallSegments"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 114 - "Arm"
Cohesion: 0.07
Nodes (10): Arm, Kind, ENGINE, FOOT, MOUNTED, RANGED, Missile, ARROWS (+2 more)

### Community 115 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 116 - "Size"
Cohesion: 0.11
Nodes (6): Post, Propulsion, OARS, SAIL, STEAM, Size

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.08
Nodes (4): GrandStrategyScreen, Marcher, Spot, GarrisonOrder

### Community 120 - "BaseMapStore"
Cohesion: 0.22
Nodes (3): BaseMapStore, Ready, RequestBaseMap

### Community 121 - "Cloth"
Cohesion: 0.13
Nodes (4): Cloth, Solid, ClothTest, StretchTest

### Community 124 - "Quests"
Cohesion: 0.13
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 126 - "DayClockTest"
Cohesion: 0.25
Nodes (3): DayClock, Step, DayClockTest

### Community 127 - "MapContext"
Cohesion: 0.10
Nodes (8): Cited Findings, BaseMapLayer, Hit, LabelLayer, MapContext, MapView, PoiIconLayer, LabelShape

### Community 128 - "Body"
Cohesion: 0.11
Nodes (18): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+10 more)

### Community 132 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 133 - "ConquestContent"
Cohesion: 0.06
Nodes (10): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD, ConquestContent (+2 more)

### Community 134 - "Hull"
Cohesion: 0.27
Nodes (3): Hull, Ramming, RammingTest

### Community 136 - "GateBlockEntity"
Cohesion: 0.06
Nodes (11): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+3 more)

### Community 137 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.07
Nodes (6): Saved, BattleWallBannerBlock, CommandTableBlock, GateBlock, HelmBlock, MarkerBlock

### Community 138 - "SeaCalm"
Cohesion: 0.22
Nodes (4): Depths, SeaCalm, Strengths, SeaCalmTest

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.06
Nodes (35): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 5. Water/ocean waves (sea swell, foam, boats on waves), 6. Sodium (and Iris) compatibility, 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings (+27 more)

### Community 141 - ".choose"
Cohesion: 0.12
Nodes (11): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+3 more)

### Community 143 - "Kind"
Cohesion: 0.13
Nodes (13): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HALBERDIERS, HEAVY_CAVALRY (+5 more)

### Community 151 - ".view"
Cohesion: 0.20
Nodes (3): PlayerStats, View, Retinues

### Community 156 - "BattleBannerBlock.java"
Cohesion: 0.07
Nodes (7): BattleBannerBlock, FlagBlock, Mount, BARS, BLOCK, FENCE, WALL

### Community 157 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 158 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.15
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 160 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.08
Nodes (18): Inferences, Cited Findings, Cited Findings, ArrowSaveMixin, ChunkMapAccess, ChunkMapCameraMixin, CityVegetationMixin, ClientChunkWindowMixin (+10 more)

### Community 163 - ".rightClick"
Cohesion: 0.24
Nodes (3): Entry, Menu, HornOrders

### Community 164 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 175 - "ConquestNetwork"
Cohesion: 0.07
Nodes (14): CameraAt, CityView, ConquestNetwork, CycleSwing, Deflect, FigureStrike, GameSpeedRequest, GateSet (+6 more)

### Community 178 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.04
Nodes (20): BackBannerLayer, BannerArt, BattleBannerRenderer, CapeCurio, ClothItemRenderer, Carried, ClothRenderer, Fabric (+12 more)

### Community 203 - "DistantHorizonsArmies"
Cohesion: 0.14
Nodes (7): 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), Cited Findings, Gaps, Inferences, Takeaway, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent

### Community 206 - ".noble"
Cohesion: 0.29
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 219 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.06
Nodes (33): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps (+25 more)

### Community 223 - "Settlement"
Cohesion: 0.09
Nodes (11): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+3 more)

### Community 224 - "RecruitFigures.java"
Cohesion: 0.05
Nodes (5): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), Distant Horizons (recommended mod), Figure

### Community 226 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.07
Nodes (19): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+11 more)

### Community 232 - ".onRegisterClientCommands"
Cohesion: 0.25
Nodes (4): Mode, OFF, ON, VIEW

### Community 234 - "BattleType"
Cohesion: 0.14
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 237 - "ClientTownsfolk"
Cohesion: 0.16
Nodes (6): ClientTownsfolk, Folk, StreetWalks, Walk, Streets, ClientTownsfolkTest

### Community 238 - "Body"
Cohesion: 0.19
Nodes (8): Boxes, Palette, Shape, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 248 - "Kind"
Cohesion: 0.50
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 249 - "net.neoforged.fml.common.EventBusSubscriber"
Cohesion: 0.06
Nodes (6): BattleKeepInventory, ClientModEvents, net.neoforged.neoforge.client.extensions.common.IClientItemExtensions, Registration, Deflects, Window

### Community 251 - "BlockGrid"
Cohesion: 0.22
Nodes (7): BlockGrid, FleetRole, FRONT, LINE, RESERVE, SCREEN, SUPPORT

### Community 252 - "Doctrine"
Cohesion: 0.05
Nodes (30): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+22 more)

### Community 255 - "ProvinceMap"
Cohesion: 0.17
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "CameraView"
Cohesion: 0.13
Nodes (3): Camera, CameraView, Chunks

### Community 266 - ".hash"
Cohesion: 0.15
Nodes (4): NobleAnnex, OriginRoller, Roll, ProvinceNames

### Community 267 - ".soundHorn"
Cohesion: 0.33
Nodes (5): HornCall, ATTACKING, MOVING, RALLYING, RETREATING

### Community 271 - "PoiPrecomputer.java"
Cohesion: 0.05
Nodes (16): Inferences, SeedHash, FactionBannerNbt, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType (+8 more)

### Community 281 - "RecruitsBattle"
Cohesion: 0.04
Nodes (12): GeneralTask, Landing, Officers, RecruitsBattle, Role, BEARER, GENERAL, GUARD (+4 more)

### Community 285 - "net.minecraft.client.Minecraft"
Cohesion: 0.06
Nodes (9): Cited Findings, DistantArmyRenderer, Rects, FarGun, Model, OarFigures, Rowing, Columns (+1 more)

### Community 286 - "TitleMap"
Cohesion: 0.11
Nodes (5): TitleMap, TitleStyle, SiegeSurveyTest, Land, TitleMapTest

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 294 - ".walks"
Cohesion: 0.12
Nodes (4): DistrictKey, Townsfolk, CitySizesTest, StreetWalksTest

### Community 297 - ".generate"
Cohesion: 0.06
Nodes (21): Nation, BaseMapGenerator, Heights, BiomePalette, Entry, Homeland, DESERT, JUNGLE (+13 more)

### Community 304 - "CannonShots"
Cohesion: 0.26
Nodes (5): Ball, CannonShots, Order, Shot, ROUNDSHOT

### Community 309 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 313 - "Machine"
Cohesion: 0.07
Nodes (7): Machine, Role, MANTLET, RAM, SHOOTER, TOWER, ShipEngineBlock

### Community 319 - "Plan"
Cohesion: 0.20
Nodes (8): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Plan

### Community 323 - "DistantGeneration"
Cohesion: 0.32
Nodes (3): Inferences, Api, DistantGeneration

### Community 324 - ".order"
Cohesion: 0.40
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

### Community 329 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.05
Nodes (9): Palette, WallTemplates, Placed, Build, CASTLE, HAMLET, RUIN, TOWER (+1 more)

### Community 330 - "OarFigures"
Cohesion: 0.06
Nodes (13): Wind, ShipSprite, Batch, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL (+5 more)

### Community 350 - "FormationSnapshot"
Cohesion: 0.10
Nodes (6): Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 355 - "FieldBlock"
Cohesion: 0.04
Nodes (4): FieldBattle, Target, Volley, FieldBlock

### Community 357 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 380 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, GUNSHIP, SHOCK, SWORDSMEN

### Community 421 - "Cited Findings"
Cohesion: 0.50
Nodes (4): Gaps, Inferences, Takeaway, Which areas were checked and look fine?

### Community 431 - "net.minecraft.core.Direction"
Cohesion: 0.12
Nodes (6): ChunkWait, Wait, Land, Result, SiegeSurvey, Wall

### Community 443 - "MediumCannonEntity.java"
Cohesion: 0.07
Nodes (5): BattleBannerBlockEntity, Oar, Banner, PlacedBanners, Sail

### Community 453 - ".sendTalk"
Cohesion: 0.15
Nodes (3): BoardView, GrandStrategyView, TalkView

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **435 isolated node(s):** `ARCHER_MELEE`, `ARROWS`, `CAVALRY`, `INFANTRY`, `FLANK` (+430 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1570 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **266 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `.state`, `net.minecraft.nbt.CompoundTag`, `org.junit.jupiter.api.Test`, `net.minecraft.core.BlockPos`, `net.neoforged.bus.api.SubscribeEvent`, `.soundHorn`, `Point`, `net.minecraft.world.entity.Entity`, `Peerage`, `BattleShips`, `PveCommands`, `War`, `net.minecraft.client.gui.GuiGraphics`, `SiegeWorks`, `org.joml.Matrix4f`, `net.minecraft.core.Direction`, `.slowSpentHorses`, `Phase`, `RecruitsBattle.java`, `.siege`, `net.minecraft.server.MinecraftServer`, `WarCouncil`, `net.minecraft.world.item.ItemStack`, `net.minecraft.server.level.ServerPlayer`, `Enlisted`, `net.minecraft.resources.ResourceLocation`, `FieldBlock`, `UnitOrder`, `.passOnOrders`, `GrappleHook`, `.map`, `Doctrine`?**
  _High betweenness centrality (0.062) - this node is a cross-community bridge._
- **What connects `ARCHER_MELEE`, `ARROWS`, `CAVALRY` to the rest of the system?**
  _435 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `.state` be split into smaller, more focused modules?**
  _Cohesion score 0.053613053613053616 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `TitleOwnersScreen`, `ProvinceMap`, `Front`, `.choose`, `ClientEvents.java`, `map`, `PoiPrecomputer.java`, `.draw`, `Peerage`, `.view`, `TitleLayer`, `City`, `CityLayout.java`, `TitleMap`, `.fire`, `.place`, `net.minecraft.client.gui.GuiGraphics`, `.generate`, `.postChunk`, `RecruitsBattle.java`, `PetitionScreen`, `MediumCannonEntity.java`, `ConquestSavedData`, `net.minecraft.server.MinecraftServer`, `Ranks`, `School`, `WarCouncil`, `net.minecraft.world.item.ItemStack`, `RoadLayer`, `Terrain`, `.view`, `ArmySpawnScreen`, `RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots`, `Settlement`, `RecruitFigures.java`, `ClientPolitics`, `.place`, `.map`, `NationScreen`, `StrategyRelief`, `PoliticalStyle`, `.onLevelLoad`, `GrandStrategyScreen`, `BaseMapStore`, `Quests`, `MapContext`?**
  _High betweenness centrality (0.047) - this node is a cross-community bridge._
- **Should `FieldArmy` be split into smaller, more focused modules?**
  _Cohesion score 0.07592592592592592 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._