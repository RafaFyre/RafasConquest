# Graph Report - RafasConquest  (2026-11-21)

## Corpus Check
- 658 files · ~555,310 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 8463 nodes · 27564 edges · 422 communities (154 shown, 268 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3494 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `2411cca2`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- .measure
- .render
- net.minecraft.nbt.CompoundTag
- org.junit.jupiter.api.Test
- MediumCannonEntity
- BattleScreen
- Front
- WarCamp
- Politics
- net.minecraft.world.level.block.state.BlockState
- ConquestEvents
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Point
- PveCommands.java
- BattleMap.java
- .rout
- RafasConquest Project Site
- Person
- RecruitsBattle
- BattlePlan
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- .view
- CityHud
- Tier
- net.minecraft.core.Vec3i
- RoadNetwork
- .plan
- PlaceMusic.java
- .allSitesInBox
- GrappleHooks.java
- BattleShips
- PveCommands
- .map
- CityPlan
- Characters (holders, skills, prowess, traits)
- Ranks
- WaterSwell
- HarbourCut
- rafasconquest.faction.Faction
- net.minecraft.client.gui.GuiGraphics
- comparator
- SiegeWorks
- ArrowVolleys
- rafasconquest.battle.FormationSnapshot
- Personality
- SeaLanes
- SimpleChannel
- .postChunk
- .deployment
- Kit
- DistantOcclusion
- .feudFight
- CityStyles
- RecruitsBattle.java
- RecruiterRenderer.java
- CityTerrain
- .sample
- HorizontalStretch
- WorldSettings
- TitleMap
- Bodies
- StyleLibrary
- .roadStart
- PoiPrecomputer.java
- Row
- School
- DistantShips
- TechTreeScreen
- MapLands
- WarCouncil
- .step
- net.minecraft.world.item.ItemStack
- RecruitFigures
- BaseMap
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- Candidate
- RoadLayer
- Recruiter
- net.minecraft.server.level.ServerLevel
- CityTerrainPiece
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- .inBox
- DialogueScreen
- OrganizationScreen
- Lands
- FarFlags
- .of
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- GatePlanksBlock.java
- FactionInfoScreen
- Person
- UnitOrder
- Land
- FaceOffScreen
- How Total War calculates battles between units
- LabelLayer
- .map
- CityPanel
- NationScreen
- net.minecraft.world.level.block.entity.BlockEntity
- Faction
- Arm
- .block
- .roadStart
- MarkerScreen
- rafasconquest.battle.FieldBlock
- GrandStrategyScreen
- Cloth
- CityManagerScreen
- Quests
- Roads
- DayClockTest
- MapView
- net.minecraft.world.item.Item
- Terrain
- TitleOwnersScreen
- PoliticsData
- .shift
- .siege
- Capture
- GateBlockEntity
- MarkerSpec
- SeaCalm
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .choose
- .render
- .view
- .collect
- Barracks
- CLAUDE.md
- Cited Findings
- com.google.gson.JsonObject
- Builder
- Call
- net.minecraft.sounds.SoundEvent
- org.spongepowered.asm.mixin.Mixin
- Ground
- net.minecraft.world.phys.Vec3
- net.minecraft.world.level.block.Rotation
- DistantHorizonsArmies
- /pve debug commands
- net.minecraft.server.level.ServerPlayer
- com.mojang.blaze3d.vertex.PoseStack
- PetitionScreen
- Size
- .noble
- .say
- ArmySpawnScreen
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Settlement
- RecruitFigures.java
- Body
- rafasconquest.battle.BattlePlan.Arm
- .wallSegments
- Override
- SableLink
- Grade
- MapContext
- GrappleHook
- DistantArmyRenderer
- Body
- Kind
- .findGenerationPoint
- CombatMode
- Trait
- ClientModEvents
- Doctrine
- net.minecraft.world.entity.LivingEntity
- ProvinceMap
- HorizontalStretch
- StrategyRelief
- Profiling run sheet (baseline)
- RoadLayer
- CameraView
- MapLayer
- .boundsAt
- net.neoforged.bus.api.SubscribeEvent
- BattleSnapshot
- .findGenerationPoint
- BattleType
- .enlist
- ConquestWorldScreen
- TitleLayer
- .measure
- net.minecraft.client.Minecraft
- .at
- .touches
- Kind
- ClothRenderer
- BackBannerLayer.java
- CityGridBuilder
- ClientScreens
- .generate
- NobleAnnex
- Sea
- .order
- .aNewCardsFileAddsACardThatDeploysByItsKind
- org.joml.Vector3d
- FactionMapScreen
- Phase
- GateScreen
- .mouseClicked
- Machine
- .soundHorn
- .draw
- SwingModes
- Build
- net.minecraft.core.BlockPos
- WallTemplates
- OarFigures
- Deflects
- Succession
- BattleKeepInventory
- BlockGrid
- Missile
- FormationSnapshot
- FieldBlock
- FieldBlock
- Kind
- ShareLiveTest
- ShipKind
- ClientBarks
- net.minecraft.core.Direction

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 323 edges
2. `FieldBlock` - 245 edges
3. `FieldBattle` - 238 edges
4. `Point` - 175 edges
5. `ConquestNetwork` - 143 edges
6. `FieldArmy` - 140 edges
7. `Lands` - 94 edges
8. `BattleScreen` - 88 edges
9. `Siege` - 86 edges
10. `Politics` - 84 edges

## Surprising Connections (you probably didn't know these)
- `Takeaway` --references--> `RoadLayer`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Stage 3: client frame time (2–3 jars, only where stage 0 showed a cost)` --references--> `BaseMapLayer`  [INFERRED]
  reports/RafasConquest optimization plan 2026-11-18.md → src/main/java/rafasconquest/client/map/BaseMapLayer.java
- `Cited Findings` --references--> `MapView`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/MapView.java
- `Stage 3: client frame time (2–3 jars, only where stage 0 showed a cost)` --references--> `TitleLayer`  [INFERRED]
  reports/RafasConquest optimization plan 2026-11-18.md → src/main/java/rafasconquest/client/map/TitleLayer.java
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

## Communities (422 total, 268 thin omitted)

### Community 0 - ".measure"
Cohesion: 0.29
Nodes (3): Calibration, Fit, CalibrationTest

### Community 1 - ".render"
Cohesion: 0.09
Nodes (8): CharacterScreen, Retainer, Spot, Banner, Portraits, Names, Honour, Honours

### Community 2 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.06
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - "org.junit.jupiter.api.Test"
Cohesion: 0.06
Nodes (7): DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, SwingModesTest, GrandStrategyDateTest, OarRowingTest, WeaponSkillsTest

### Community 4 - "MediumCannonEntity"
Cohesion: 0.07
Nodes (6): CannonAim, MediumCannonEntity, Order, Shot, ROUNDSHOT, CannonAimTest

### Community 5 - "BattleScreen"
Cohesion: 0.07
Nodes (3): BattleScreen, Entry, Menu

### Community 6 - "Front"
Cohesion: 0.13
Nodes (4): Feud, Lord, Feuds, Front

### Community 7 - "WarCamp"
Cohesion: 0.10
Nodes (6): PlannedTent, Saved, WarCamp, CourtSet, Seat, Style

### Community 8 - "Politics"
Cohesion: 0.06
Nodes (7): Event, Muster, CEREMONIAL, OFFENSIVE, Politics, War, PoliticsTest

### Community 9 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (8): GrandStrategyBlock, MediumCannonBlock, OarBlock, QuestBoardBlock, SailBlock, StrategyBoardBlock, WorldMapItem, MarkerBlock

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.07
Nodes (28): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings (+20 more)

### Community 13 - "Point"
Cohesion: 0.04
Nodes (22): Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate, GateAction (+14 more)

### Community 14 - "PveCommands.java"
Cohesion: 0.06
Nodes (36): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+28 more)

### Community 15 - "BattleMap.java"
Cohesion: 0.05
Nodes (7): Battle map developer web page, Enlisted players, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Morale, rout and rally, Battle roles (frontline, bait, support, flanker, kill squad), War council, The war horn

### Community 16 - ".rout"
Cohesion: 0.08
Nodes (13): 1. Flank and rear shock, 2. Nearby rout (fright), 3. Army-wide losses, 4. Shattering, Design: adding the four morale shocks (built 2026-11-11, untested in game), Tests (FieldBattleTest), The core change: morale becomes a sum, Morale (+5 more)

### Community 17 - "RafasConquest Project Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.12
Nodes (6): Centre, TitleStyle, Faith, Bond, RealmPlanner, Seat

### Community 19 - "RecruitsBattle"
Cohesion: 0.04
Nodes (27): Cited Findings, Camera, Afloat, Card, Enlisted, Figure, GunMark, RecruitsBattle (+19 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 23 - ".view"
Cohesion: 0.05
Nodes (23): Advancement, Boards, Entry, View, NobleTitle, COUNT, DUKE, EMPEROR (+15 more)

### Community 24 - "CityHud"
Cohesion: 0.09
Nodes (3): CityHud, Spot, CityHudPreviewTest

### Community 25 - "Tier"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "net.minecraft.core.Vec3i"
Cohesion: 0.08
Nodes (5): StylePiece, CityPlacement, ConquestWorldgen, FarmPlacement, FarmStructure

### Community 27 - "RoadNetwork"
Cohesion: 0.09
Nodes (6): Cited Findings, RoadFeature, Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 28 - ".plan"
Cohesion: 0.08
Nodes (8): Countryside, Plan, Grid, SeatCities, CountrysideTest, World, SeatCitiesTest, World

### Community 29 - "PlaceMusic.java"
Cohesion: 0.05
Nodes (3): ConquestConfig, FactionBannerNbt, DojoPlacement

### Community 30 - ".allSitesInBox"
Cohesion: 0.10
Nodes (3): District, CityStructure, CityLayoutTest

### Community 32 - "BattleShips"
Cohesion: 0.14
Nodes (6): Hull, Ramming, BattleShips, Helm, Ram, RammingTest

### Community 34 - ".map"
Cohesion: 0.10
Nodes (5): Army, Goal, MusterBars, PoliticsSync, Wars

### Community 35 - "CityPlan"
Cohesion: 0.15
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 36 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.05
Nodes (39): Ambushes from woods, Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Character page, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings) (+31 more)

### Community 37 - "Ranks"
Cohesion: 0.06
Nodes (21): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+13 more)

### Community 38 - "WaterSwell"
Cohesion: 0.08
Nodes (11): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Columns, SableSeen, Made (+3 more)

### Community 39 - "HarbourCut"
Cohesion: 0.12
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 43 - "SiegeWorks"
Cohesion: 0.10
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 44 - "ArrowVolleys"
Cohesion: 0.11
Nodes (13): Arrow, ArrowVolleys, Kind, ARROW, BALL, BIG_BALL, BIG_ROCK, BOLT (+5 more)

### Community 45 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.10
Nodes (8): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, DistantArmies, Throw, ThrownFigures

### Community 46 - "Personality"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.22
Nodes (6): Lane, Leg, Port, SeaLanes, Ship, Voyage

### Community 48 - "SimpleChannel"
Cohesion: 0.09
Nodes (11): BackBanner, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel, Type (+3 more)

### Community 50 - ".postChunk"
Cohesion: 0.08
Nodes (8): Cited Findings, ConquestSavedData, BarracksPeople, CityBanners, CityGuards, ConquestServer, StructureLink, PoiPrecomputer

### Community 52 - "Kit"
Cohesion: 0.10
Nodes (20): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+12 more)

### Community 53 - "DistantOcclusion"
Cohesion: 0.07
Nodes (14): Stage 0: measure (one jar, no behaviour change), 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Cited Findings, Gaps, Inferences, Takeaway, BattleSpotRing (+6 more)

### Community 55 - ".feudFight"
Cohesion: 0.16
Nodes (5): Clash, Kind, DUEL, SKIRMISH, FeudDuel

### Community 59 - "CityTerrain"
Cohesion: 0.07
Nodes (18): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+10 more)

### Community 61 - "HorizontalStretch"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 62 - "WorldSettings"
Cohesion: 0.09
Nodes (10): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, WorldSettings (+2 more)

### Community 63 - "TitleMap"
Cohesion: 0.16
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.08
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.12
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 67 - ".roadStart"
Cohesion: 0.08
Nodes (5): DistrictKey, Townsfolk, CitySizesTest, StreetWalksTest, PlaceMusicTest

### Community 69 - "PoiPrecomputer.java"
Cohesion: 0.08
Nodes (13): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, FactionInfo (+5 more)

### Community 71 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 72 - "DistantShips"
Cohesion: 0.10
Nodes (7): DistantShips, Far, HullKey, Riding, Want, DeckLiftTest, DistantShipsTest

### Community 73 - "TechTreeScreen"
Cohesion: 0.18
Nodes (4): TechTreeScreen, Tech, TechTree, TechTreeTest

### Community 74 - "MapLands"
Cohesion: 0.09
Nodes (5): Gaps, Inferences, Takeaway, Where are full scans, nested loops, repeated lookups and per-tick allocations?, MapLands

### Community 75 - "WarCouncil"
Cohesion: 0.08
Nodes (10): Facts, Officer, Said, Talk, WarCouncil, Ranks, SaveGuard, Offer (+2 more)

### Community 76 - ".step"
Cohesion: 0.07
Nodes (17): 1. How we avoid breaking anything, 2. Where the 10 November gameplan stands, 3. New costs since 10 November, 4. The stages, 5. Order and size, Core-function check after every stage (about 10 minutes in game), RafasConquest optimization plan (18 November 2026), Stage 1: small, same-behaviour fixes (2 jars: server, then client) (+9 more)

### Community 77 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.08
Nodes (9): WarHorn, BattleStandardItem, AddonPieces, Armoury, Outfit, Style, ItemFlags, RankKits (+1 more)

### Community 78 - "RecruitFigures"
Cohesion: 0.16
Nodes (4): Layer, Look, Mesh, RecruitFigures

### Community 79 - "BaseMap"
Cohesion: 0.11
Nodes (10): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, ClientMapCache, BaseMap, Nation (+2 more)

### Community 81 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.12
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, GateDoors, SableShips

### Community 82 - "Candidate"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "RoadLayer"
Cohesion: 0.19
Nodes (6): 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom?, Gaps, Inferences, Takeaway, PoliticalMap, PoliticalStyle

### Community 89 - ".inBox"
Cohesion: 0.15
Nodes (4): City, CityGates, Done, CityIndex

### Community 92 - "Lands"
Cohesion: 0.09
Nodes (9): Cited Findings, Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Lands, Stronghold (+1 more)

### Community 95 - ".of"
Cohesion: 0.20
Nodes (3): FactionBanner, Layer, FactionBannerTest

### Community 96 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.16
Nodes (4): BattleKind, Part, ShipHulls, ShipHullsTest

### Community 100 - "Person"
Cohesion: 0.14
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 102 - "UnitOrder"
Cohesion: 0.09
Nodes (19): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+11 more)

### Community 105 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 107 - "How Total War calculates battles between units"
Cohesion: 0.17
Nodes (11): 1. Combat is per soldier, not per unit, 2. Does the swing land?, 3. How much damage the hit does, 4. Charges, 5. Missiles, 6. Leadership (morale): what actually wins battles, 7. Autoresolve, 8. Older games (Shogun 1, Medieval 1, Rome 1) (+3 more)

### Community 108 - "LabelLayer"
Cohesion: 0.21
Nodes (3): Hit, LabelLayer, LabelShape

### Community 109 - ".map"
Cohesion: 0.09
Nodes (5): Garrison, Army, GrandStrategy, View, GarrisonTest

### Community 110 - "CityPanel"
Cohesion: 0.15
Nodes (6): CityPanel, Mode, DISMISS, MARCH, MUSTER, NONE

### Community 111 - "NationScreen"
Cohesion: 0.05
Nodes (7): ClientPeerage, ClientPolitics, CourtView, Figure, NationScreen, Demographics, Share

### Community 112 - "net.minecraft.world.level.block.entity.BlockEntity"
Cohesion: 0.06
Nodes (7): BattleBannerRenderer, BattleBannerBlockEntity, ConquestContent, Oar, Banner, PlacedBanners, Sail

### Community 113 - "Faction"
Cohesion: 0.10
Nodes (4): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction

### Community 114 - "Arm"
Cohesion: 0.07
Nodes (6): Arm, Kind, ENGINE, FOOT, MOUNTED, RANGED

### Community 115 - ".block"
Cohesion: 0.04
Nodes (10): Point, WarPlan, Landing, BattleMap, GroundPlanTest, Land, Land, Land (+2 more)

### Community 116 - ".roadStart"
Cohesion: 0.13
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.08
Nodes (4): GrandStrategyScreen, Marcher, Spot, GarrisonOrder

### Community 121 - "Cloth"
Cohesion: 0.09
Nodes (5): Cloth, Solid, ClothTest, StretchTest, WoolTest

### Community 124 - "Quests"
Cohesion: 0.13
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 125 - "Roads"
Cohesion: 0.20
Nodes (5): Link, Place, Roads, Spur, RoadsTest

### Community 126 - "DayClockTest"
Cohesion: 0.20
Nodes (3): DayClock, Step, DayClockTest

### Community 127 - "MapView"
Cohesion: 0.27
Nodes (3): Cited Findings, BaseMapLayer, MapView

### Community 128 - "net.minecraft.world.item.Item"
Cohesion: 0.18
Nodes (7): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 132 - ".shift"
Cohesion: 0.16
Nodes (6): BattleSite, Part, Water, BattleSite.Water, BattleSite.Water, BattleSiteTest

### Community 134 - "Capture"
Cohesion: 0.15
Nodes (4): Capture, Film, Horse, RecruitFiguresTest

### Community 136 - "GateBlockEntity"
Cohesion: 0.09
Nodes (8): Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING, Hinge

### Community 137 - "MarkerSpec"
Cohesion: 0.08
Nodes (5): MarkerRenderer, CharacterMarkers, Placed, MarkerBlockEntity, MarkerSpec

### Community 138 - "SeaCalm"
Cohesion: 0.14
Nodes (6): Depths, SeaCalm, Strengths, Ride, SeaCalmTest, SwellTest

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (40): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 6. Sodium (and Iris) compatibility, 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply (+32 more)

### Community 141 - ".choose"
Cohesion: 0.16
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 142 - ".render"
Cohesion: 0.15
Nodes (5): ClientTownsfolk, Folk, StreetWalks, Walk, ClientTownsfolkTest

### Community 143 - ".view"
Cohesion: 0.08
Nodes (6): CityManager, PlayerStats, View, Retinues, PlaceMusic, RetinuesTest

### Community 146 - ".collect"
Cohesion: 0.17
Nodes (3): MapPreview, EconomyTick, FactionInfoTest

### Community 152 - "Cited Findings"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?

### Community 156 - "Builder"
Cohesion: 0.06
Nodes (10): BattleBannerBlock, BattleWallBannerBlock, FlagBlock, Mount, BARS, BLOCK, FENCE, WALL (+2 more)

### Community 157 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 158 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 160 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.06
Nodes (19): Inferences, Cited Findings, Cited Findings, ArmorLegsMixin, ArrowSaveMixin, BetterCombatOneHandMixin, ChunkMapAccess, ChunkMapCameraMixin (+11 more)

### Community 163 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.17
Nodes (6): BattleSounds, ChargeSound, DinSound, Queued, Shot, Source

### Community 167 - "DistantHorizonsArmies"
Cohesion: 0.05
Nodes (9): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+1 more)

### Community 175 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.03
Nodes (44): Bark, BaseMapPart, BattleSpot, BoardAction, BoardView, CameraAt, CannonFire, CannonLay (+36 more)

### Community 178 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.07
Nodes (7): BannerArt, CapeCurio, ClothItemRenderer, Carried, Layer, EpicKnightsCloaks, GateRenderer

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 219 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.06
Nodes (30): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+22 more)

### Community 223 - "Settlement"
Cohesion: 0.07
Nodes (11): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, ConquestRuntime (+3 more)

### Community 224 - "RecruitFigures.java"
Cohesion: 0.04
Nodes (5): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), Figure, SailFigures

### Community 225 - "Body"
Cohesion: 0.11
Nodes (18): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+10 more)

### Community 226 - "rafasconquest.battle.BattlePlan.Arm"
Cohesion: 0.08
Nodes (19): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+11 more)

### Community 228 - ".wallSegments"
Cohesion: 0.11
Nodes (9): CityLayout, Side, EAST, NORTH, SOUTH, WEST, Site, WallSegment (+1 more)

### Community 234 - "Grade"
Cohesion: 0.17
Nodes (9): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Plan (+1 more)

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.09
Nodes (3): DistantDepth, DistantArmyRenderer, Rects

### Community 238 - "Body"
Cohesion: 0.19
Nodes (8): Boxes, Palette, Shape, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 240 - "Kind"
Cohesion: 0.13
Nodes (13): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HALBERDIERS, HEAVY_CAVALRY (+5 more)

### Community 248 - "Trait"
Cohesion: 0.16
Nodes (6): Officers, Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 249 - "ClientModEvents"
Cohesion: 0.09
Nodes (3): ClientModEvents, net.neoforged.neoforge.client.extensions.common.IClientItemExtensions, Registration

### Community 252 - "Doctrine"
Cohesion: 0.05
Nodes (34): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender, Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT (+26 more)

### Community 254 - "net.minecraft.world.entity.LivingEntity"
Cohesion: 0.15
Nodes (4): EpicKnightsJoustingMixin, EpicKnightsSlownessMixin, ShoulderSurfingPickMixin, WeaponSkills

### Community 255 - "ProvinceMap"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 262 - "RoadLayer"
Cohesion: 0.21
Nodes (8): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, RoadLayer

### Community 265 - "MapLayer"
Cohesion: 0.20
Nodes (5): LegendEntry, MapLayer, Symbol, TerritoryLayer, UnitLayer

### Community 266 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 272 - "BattleSnapshot"
Cohesion: 0.16
Nodes (4): BattleSnapshot, UnitInfo, UnitsState, BattleSnapshotGearTest

### Community 278 - "BattleType"
Cohesion: 0.13
Nodes (5): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE

### Community 281 - ".enlist"
Cohesion: 0.07
Nodes (3): Outcome, GeneralTask, TickTimes

### Community 282 - "ConquestWorldScreen"
Cohesion: 0.24
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 285 - "net.minecraft.client.Minecraft"
Cohesion: 0.14
Nodes (4): FarGun, Model, OarFigures, Rowing

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "ClothRenderer"
Cohesion: 0.13
Nodes (6): ClothRenderer, Fabric, Far, Kind, Live, OffTheBackTest

### Community 292 - "BackBannerLayer.java"
Cohesion: 0.17
Nodes (3): BackBannerLayer, BackBanners, MouthLayer

### Community 294 - "CityGridBuilder"
Cohesion: 0.08
Nodes (10): Houses, CityGridBuilder, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA (+2 more)

### Community 297 - ".generate"
Cohesion: 0.05
Nodes (22): ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors, Homeland, DESERT (+14 more)

### Community 301 - ".order"
Cohesion: 0.13
Nodes (10): Order, CHARGE, ENGAGE, HOLD, MARCH, Striker, ARCHER_MELEE, ARROWS (+2 more)

### Community 309 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 311 - ".mouseClicked"
Cohesion: 0.44
Nodes (3): Place, PlaceMenuLayer, Row

### Community 313 - "Machine"
Cohesion: 0.07
Nodes (6): Machine, Role, MANTLET, RAM, SHOOTER, TOWER

### Community 314 - ".soundHorn"
Cohesion: 0.33
Nodes (5): HornCall, ATTACKING, MOVING, RALLYING, RETREATING

### Community 324 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 326 - "net.minecraft.core.BlockPos"
Cohesion: 0.11
Nodes (4): CannonControl, CommandTableBlock, GateGeometry, GateGeometryTest

### Community 330 - "OarFigures"
Cohesion: 0.09
Nodes (11): ShipSprite, Batch, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED (+3 more)

### Community 336 - "Succession"
Cohesion: 0.07
Nodes (10): Hook, Kind, FRIEND, RIVAL, Realm, Succession, Taking, RealmPlannerTest (+2 more)

### Community 343 - "Missile"
Cohesion: 0.29
Nodes (4): Missile, ARROWS, NONE, SHOT

### Community 350 - "FormationSnapshot"
Cohesion: 0.12
Nodes (7): Cited Findings, Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 355 - "FieldBlock"
Cohesion: 0.05
Nodes (4): FieldArmy, Event, FieldBattle, Order

### Community 357 - "FieldBlock"
Cohesion: 0.04
Nodes (16): Target, Volley, FieldBlock, FleetRole, FRONT, LINE, RESERVE, SCREEN (+8 more)

### Community 364 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 380 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, GUNSHIP, SHOCK, SWORDSMEN

### Community 431 - "net.minecraft.core.Direction"
Cohesion: 0.13
Nodes (6): Land, Result, SiegeSurvey, Wall, HelmBlock, SiegeSurveyTest

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **428 isolated node(s):** `FOOT`, `RANGED`, `MOUNTED`, `ENGINE`, `NONE` (+423 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1507 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **268 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `.measure`, `net.minecraft.nbt.CompoundTag`, `org.junit.jupiter.api.Test`, `.shift`, `.siege`, `WarCamp`, `ConquestEvents`, `PveCommands.java`, `.view`, `BattleMap.java`, `.enlist`, `PlaceMusic.java`, `PveCommands`, `.map`, `rafasconquest.faction.Faction`, `.generate`, `comparator`, `SiegeWorks`, `net.minecraft.client.gui.GuiGraphics`, `.order`, `net.minecraft.core.Direction`, `.isOurs`, `Phase`, `RecruitsBattle.java`, `.soundHorn`, `WarCouncil`, `net.minecraft.world.item.ItemStack`, `RecruitFigures.java`, `UnitOrder`, `SableLink`, `GrappleHook`, `.map`, `Arm`, `.block`, `rafasconquest.battle.FieldBlock`, `Trait`, `Doctrine`, `net.minecraft.world.entity.LivingEntity`?**
  _High betweenness centrality (0.081) - this node is a cross-community bridge._
- **What connects `FOOT`, `RANGED`, `MOUNTED` to the rest of the system?**
  _428 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `.render` be split into smaller, more focused modules?**
  _Cohesion score 0.09302325581395349 - nodes in this community are weakly interconnected._
- **Why does `Point` connect `.block` to `org.junit.jupiter.api.Test`, `.shift`, `.siege`, `BattleScreen`, `Point`, `.rout`, `RecruitsBattle`, `BattlePlan`, `list`, `.enlist`, `PveCommands`, `Ground`, `.map`, `rafasconquest.faction.Faction`, `net.minecraft.client.gui.GuiGraphics`, `SiegeWorks`, `RecruitsBattle.java`, `BlockGrid`, `FieldBlock`, `FieldBlock`, `.map`, `rafasconquest.battle.FieldBlock`?**
  _High betweenness centrality (0.043) - this node is a cross-community bridge._
- **Should `net.minecraft.nbt.CompoundTag` be split into smaller, more focused modules?**
  _Cohesion score 0.06227106227106227 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._