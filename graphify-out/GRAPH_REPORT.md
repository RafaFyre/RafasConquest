# Graph Report - RafasConquest  (2026-11-14)

## Corpus Check
- 550 files · ~472,074 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7513 nodes · 24142 edges · 386 communities (146 shown, 240 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3172 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `064260d0`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- .measure
- Politics
- net.minecraft.world.level.levelgen.structure.BoundingBox
- .army
- .allSitesInBox
- BattleScreen
- DistantHorizonsArmies.java
- CityGridBuilder
- PoliticsTest
- ClothRenderer
- Peerage
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- org.jetbrains.annotations.Nullable
- BaseMap
- rafasconquest.map.BaseMap
- net.minecraft.world.level.Level
- ConquestSavedData
- Battle Map Web Page & Site
- Person
- net.minecraft.world.level.block.state.BlockState
- .fire
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- RecruitsBattle.java
- HorizontalStretch
- CityManagerScreen
- .plan
- net.minecraft.server.level.ServerPlayer
- Arm
- WarCouncil
- City
- .place
- .sail
- Lands
- PveCommands Registration
- School
- CityPlan
- net.minecraft.core.Vec3i
- Machine
- War
- SiegeWorks
- rafasconquest.faction.Faction
- CityVariant
- Front
- GateBlockEntity
- FieldBlock
- Personalities
- SeaLanes
- Characters (holders, skills, prowess, traits)
- Plan
- .startAt
- ProvinceMap
- Faction
- Barracks
- .siege
- CityStyles
- SeatCitiesTest
- Faction Map View
- .step
- net.minecraft.client.renderer.MultiBufferSource
- MapContext
- TitleMapTest
- Bodies
- StyleLibrary
- FigureGear
- CapitalLayout
- BattleMap.java
- Row
- .generate
- net.minecraft.world.item.DyeColor
- RecruitsBattle
- MapLands
- Kit
- .rightClick
- Quests
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Duty
- net.minecraft.server.level.ServerLevel
- Ownership & Territory
- .wallSegments
- FormationSnapshot
- Land
- Front
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- .render
- .postChunk
- org.junit.jupiter.api.Test
- .view
- Grade
- Cited Findings
- FactionInfoScreen
- ClientEvents
- CapeCurio.java
- Cloth
- FigureBatches
- net.minecraft.world.phys.Vec3
- .block
- How Total War calculates battles between units
- Override
- Race
- SimpleChannel.java
- com.mojang.blaze3d.vertex.PoseStack
- .view
- WaterSwell
- Ride
- Body
- Trait
- uuid
- HarbourCut
- GrandStrategyScreen
- CityTerrain
- DistantOcclusion
- CityTerrainPiece
- .tick
- GoldChurch.java
- BattleShips
- Terrain
- .touches
- .drawEmpire
- .shift
- .choose
- SeaCalm
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- ClientScreens
- TechTreeScreen
- ArtilleryMount.java
- NationScreen
- SableLink
- CLAUDE.md
- DayClockTest
- MapLayer
- BattleStandardItem.java
- RoadNetwork
- RoadNetwork
- net.minecraft.core.BlockPos
- RecruitFigures
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- RafasConquest (PvE addon for Villager Recruits)
- GrappleHookRenderer.java
- BattleSnapshot
- .readPlacements
- Politics.Front
- Phase
- .noble
- RafasConquest.java
- .render
- WallTemplates
- ClientBarks
- BattleSounds
- RecruitFigures.java
- .at
- Ranks
- UnitOrder
- .render
- net.neoforged.bus.api.SubscribeEvent
- BattleKeepInventory
- Kind
- net.minecraft.client.Minecraft
- How do world generation and structure placement work, and what do they cost?
- .boundsAt
- ConquestGameRules
- OrganizationScreen
- .build
- .findGenerationPoint
- GatePlanksBlock.java
- BattlePlan
- Roads
- Settlement
- Profiling run sheet (baseline)
- .measure
- net.minecraft.sounds.SoundEvent
- ClientPolitics
- .glow
- net.minecraft.client.gui.GuiGraphics
- Realm
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- .onClientExtensions
- Role
- .of
- RankKits
- Kind
- Corners
- ShareLiveTest.java
- .cellMax
- .onLevelLoad
- .plan
- Override
- BoardScreen
- Cited Findings
- Call
- Recruiter
- ConquestNetwork
- Override
- Siege
- NobleAnnex
- PveCommands.java
- assertequals
- .inCity
- .load
- .findGenerationPoint
- Realm
- PoliticsData
- Task
- .onRegisterRenderers
- OarFigures
- net.minecraft.world.phys.Vec3
- Where are full scans, nested loops, repeated lookups and per-tick allocations?
- BlockGrid
- HornCall
- .view
- Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI
- State
- Build
- Cited Findings
- 6. Sodium (and Iris) compatibility
- SwingModes
- WarHorn
- CityManager
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- ShipKind

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 305 edges
2. `FieldBlock` - 222 edges
3. `FieldBattle` - 209 edges
4. `ConquestNetwork` - 128 edges
5. `FieldArmy` - 127 edges
6. `Lands` - 89 edges
7. `BattleScreen` - 86 edges
8. `Siege` - 82 edges
9. `Politics` - 79 edges
10. `BaseMap` - 77 edges

## Surprising Connections (you probably didn't know these)
- `Inferences` --references--> `PoliticalMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/map/PoliticalMap.java
- `A tick budget and staggered schedules make the fixes stick` --references--> `BaseMap`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/map/BaseMap.java
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

## Communities (386 total, 240 thin omitted)

### Community 0 - ".measure"
Cohesion: 0.19
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 1 - "Politics"
Cohesion: 0.13
Nodes (9): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Muster, CEREMONIAL, OFFENSIVE (+1 more)

### Community 2 - "net.minecraft.world.level.levelgen.structure.BoundingBox"
Cohesion: 0.06
Nodes (10): StylePiece, BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece (+2 more)

### Community 3 - ".army"
Cohesion: 0.06
Nodes (4): Point, DoctrineTest, FieldBattleBodiesTest, FieldBattleTest

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.07
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "CityGridBuilder"
Cohesion: 0.11
Nodes (4): Houses, CityGridBuilder, Ground, Platform

### Community 9 - "ClothRenderer"
Cohesion: 0.28
Nodes (4): ClothRenderer, Fabric, Kind, Live

### Community 10 - "Peerage"
Cohesion: 0.07
Nodes (11): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+3 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 13 - "BaseMap"
Cohesion: 0.12
Nodes (10): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, ClientMapCache, BaseMap, Nation (+2 more)

### Community 14 - "rafasconquest.map.BaseMap"
Cohesion: 0.13
Nodes (4): FeudDuel, MusterBars, PoliticsSync, Wars

### Community 15 - "net.minecraft.world.level.Level"
Cohesion: 0.06
Nodes (8): Sea, ArtilleryBlock, GrandStrategyBlock, OarBlock, QuestBoardBlock, SailBlock, StrategyBoardBlock, WorldMapItem

### Community 16 - "ConquestSavedData"
Cohesion: 0.07
Nodes (6): Cited Findings, ConquestSavedData, Poi, EconomyTick, PoiPrecomputer, ConquestSavedDataTest

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.15
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 19 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (8): BattleBannerBlock, BattleWallBannerBlock, GateBlock, Builder, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, MarkerBlock

### Community 20 - ".fire"
Cohesion: 0.09
Nodes (3): Placed, MarkerBlockEntity, MarkerSpec

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.07
Nodes (17): Inferences, Cited Findings, Cited Findings, Camera, CameraView, Chunks, ArrowSaveMixin, ChunkMapAccess (+9 more)

### Community 27 - "Arm"
Cohesion: 0.08
Nodes (16): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+8 more)

### Community 28 - "WarCouncil"
Cohesion: 0.13
Nodes (6): Facts, Officer, Said, Talk, WarCouncil, WarCouncilPagesTest

### Community 29 - "City"
Cohesion: 0.08
Nodes (9): Cited Findings, Countryside, Plan, City, Grid, SeatCities, CityIndex, CountrysideTest (+1 more)

### Community 30 - ".place"
Cohesion: 0.11
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 32 - "Lands"
Cohesion: 0.10
Nodes (4): Cited Findings, Lands, Stronghold, Claim

### Community 34 - "School"
Cohesion: 0.11
Nodes (13): Dojos, Ground, School, BLOOD, FIRE, ICE, IRON, LIGHT (+5 more)

### Community 35 - "CityPlan"
Cohesion: 0.12
Nodes (4): CityPlan, Piece, Plan, NobleDistrictTest

### Community 36 - "net.minecraft.core.Vec3i"
Cohesion: 0.10
Nodes (4): CityPlacement, CountryPlacement, DojoPlacement, FarmPlacement

### Community 37 - "Machine"
Cohesion: 0.08
Nodes (13): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+5 more)

### Community 38 - "War"
Cohesion: 0.10
Nodes (3): Army, Goal, War

### Community 41 - "CityVariant"
Cohesion: 0.12
Nodes (7): PlacedWall, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 42 - "Front"
Cohesion: 0.10
Nodes (8): Clash, Feud, Kind, DUEL, SKIRMISH, Lord, Feuds, Front

### Community 43 - "GateBlockEntity"
Cohesion: 0.07
Nodes (11): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+3 more)

### Community 45 - "FieldBlock"
Cohesion: 0.04
Nodes (10): The core change: morale becomes a sum, FieldArmy, Event, FieldBattle, Morale, Order, Target, Volley (+2 more)

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.14
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.03
Nodes (48): Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Character page, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards (+40 more)

### Community 50 - "Plan"
Cohesion: 0.10
Nodes (8): Engine, LADDERS, RAMS, TOWERS, Gate, GateState, Plan, WallTower

### Community 51 - ".startAt"
Cohesion: 0.07
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 52 - "ProvinceMap"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 53 - "Faction"
Cohesion: 0.09
Nodes (5): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction, FactionInfoTest

### Community 58 - "Faction Map View"
Cohesion: 0.15
Nodes (3): Cited Findings, FactionMapScreen, MapView

### Community 59 - ".step"
Cohesion: 0.12
Nodes (6): FigureMotion, Ground, Match, Placed, State, FigureMotionTest

### Community 60 - "net.minecraft.client.renderer.MultiBufferSource"
Cohesion: 0.14
Nodes (3): BattleBannerRenderer, GateRenderer, MarkerRenderer

### Community 61 - "MapContext"
Cohesion: 0.15
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 66 - "FigureGear"
Cohesion: 0.16
Nodes (7): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 67 - "CapitalLayout"
Cohesion: 0.08
Nodes (10): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, WorldSettings (+2 more)

### Community 71 - ".generate"
Cohesion: 0.07
Nodes (14): Dojo, ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry, Homeland, DESERT (+6 more)

### Community 72 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.11
Nodes (9): DistantShips, Far, HullKey, Riding, Want, BattleKind, Part, ShipHulls (+1 more)

### Community 73 - "RecruitsBattle"
Cohesion: 0.02
Nodes (27): Cited Findings, Card, Enlisted, Figure, GeneralTask, Live, RecruitsBattle, Role (+19 more)

### Community 75 - "Kit"
Cohesion: 0.11
Nodes (19): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+11 more)

### Community 76 - ".rightClick"
Cohesion: 0.30
Nodes (3): Entry, Menu, HornOrders

### Community 77 - "Quests"
Cohesion: 0.10
Nodes (13): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Type, CLEAR (+5 more)

### Community 78 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.10
Nodes (19): Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps, Inferences, Inferences (+11 more)

### Community 79 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 81 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.13
Nodes (6): ChunkWait, Wait, Land, Result, SiegeSurvey, Wall

### Community 82 - "Ownership & Territory"
Cohesion: 0.20
Nodes (5): Candidate, OwnershipResolver, Claim, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".wallSegments"
Cohesion: 0.10
Nodes (9): CityLayout, Side, EAST, NORTH, SOUTH, WEST, Site, WallSegment (+1 more)

### Community 84 - "FormationSnapshot"
Cohesion: 0.08
Nodes (19): Cited Findings, Engine, FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, ENGINES (+11 more)

### Community 85 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 87 - "Doctrine"
Cohesion: 0.06
Nodes (25): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender, Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT (+17 more)

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - ".render"
Cohesion: 0.09
Nodes (8): CharacterScreen, Retainer, Spot, ClientPeerage, Banner, Portraits, Honour, Honours

### Community 92 - ".postChunk"
Cohesion: 0.11
Nodes (5): Cited Findings, BarracksPeople, CityGuards, ConquestServer, StructureLink

### Community 94 - "org.junit.jupiter.api.Test"
Cohesion: 0.04
Nodes (13): BattlePlanTest, SiegeSurveyTest, SwingModesTest, WarCampTest, GrandStrategyDateTest, OarRowingTest, CitySizesTest, DojosTest (+5 more)

### Community 95 - ".view"
Cohesion: 0.10
Nodes (11): Barks, Ear, Names, NobleTitle, COUNT, KNIGHT, NONE, Option (+3 more)

### Community 96 - "Grade"
Cohesion: 0.16
Nodes (10): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, Inferences (+2 more)

### Community 98 - "Cited Findings"
Cohesion: 0.13
Nodes (8): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, Arrow, ArrowVolleys, BattleSpotRing

### Community 101 - "CapeCurio.java"
Cohesion: 0.12
Nodes (4): BackBannerLayer, CapeCurio, Carried, MouthLayer

### Community 102 - "Cloth"
Cohesion: 0.09
Nodes (5): Cloth, Solid, ClothTest, OffTheBackTest, WoolTest

### Community 103 - "FigureBatches"
Cohesion: 0.08
Nodes (10): Batch, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE, FigureBatches (+2 more)

### Community 105 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.09
Nodes (6): PlannedTent, Saved, WarCamp, CourtSet, Seat, Style

### Community 106 - ".block"
Cohesion: 0.07
Nodes (3): Officers, GameCharacter, BattleMap

### Community 107 - "How Total War calculates battles between units"
Cohesion: 0.08
Nodes (22): 1. Combat is per soldier, not per unit, 1. Flank and rear shock, 2. Does the swing land?, 2. Nearby rout (fright), 3. Army-wide losses, 3. How much damage the hit does, 4. Charges, 4. Shattering (+14 more)

### Community 109 - "Race"
Cohesion: 0.18
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 110 - "SimpleChannel.java"
Cohesion: 0.12
Nodes (4): BackBanner, Entry, Msg, SimpleChannel

### Community 111 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.19
Nodes (3): BannerArt, ClothItemRenderer, Layer

### Community 112 - ".view"
Cohesion: 0.11
Nodes (12): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+4 more)

### Community 113 - "WaterSwell"
Cohesion: 0.11
Nodes (9): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Columns, SableSeen, Made (+1 more)

### Community 114 - "Ride"
Cohesion: 0.14
Nodes (3): Ride, Swell, SwellTest

### Community 115 - "Body"
Cohesion: 0.09
Nodes (26): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+18 more)

### Community 116 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 117 - "uuid"
Cohesion: 0.08
Nodes (14): SeedHash, TerritoryMap, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD (+6 more)

### Community 121 - "CityTerrain"
Cohesion: 0.07
Nodes (17): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+9 more)

### Community 122 - "DistantOcclusion"
Cohesion: 0.11
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 128 - "BattleShips"
Cohesion: 0.17
Nodes (5): Hull, Ramming, BattleShips, Helm, RammingTest

### Community 131 - ".drawEmpire"
Cohesion: 0.21
Nodes (3): Hit, LabelLayer, MapPreview

### Community 132 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 133 - ".choose"
Cohesion: 0.15
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 134 - "SeaCalm"
Cohesion: 0.26
Nodes (4): Depths, SeaCalm, Strengths, SeaCalmTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.07
Nodes (27): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings (+19 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings (+28 more)

### Community 143 - "ArtilleryMount.java"
Cohesion: 0.06
Nodes (5): ArtilleryMount, BattleBannerBlockEntity, ConquestContent, Oar, Sail

### Community 146 - "NationScreen"
Cohesion: 0.08
Nodes (6): Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 152 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 155 - "MapLayer"
Cohesion: 0.18
Nodes (5): LegendEntry, MapLayer, Symbol, TerritoryLayer, UnitLayer

### Community 164 - "RecruitFigures"
Cohesion: 0.09
Nodes (8): Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures, RecruitFiguresTest

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.19
Nodes (3): Armoury, Outfit, Style

### Community 174 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.17
Nodes (8): Arrows in flight (figure volleys), Battle sounds, Recruits PvE Addon Design Doc (PDF), Seeing the whole field (distant flat figures), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 186 - ".readPlacements"
Cohesion: 0.27
Nodes (3): Offsets, Placement, PlacementOffsetsTest

### Community 205 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 217 - "RafasConquest.java"
Cohesion: 0.04
Nodes (7): Wind, GrappleHooks, ConquestEntities, RafasConquest, CityStructure, ConquestWorldgen, FarmStructure

### Community 223 - "BattleSounds"
Cohesion: 0.24
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 226 - "Ranks"
Cohesion: 0.08
Nodes (13): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+5 more)

### Community 228 - "UnitOrder"
Cohesion: 0.11
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 232 - ".render"
Cohesion: 0.13
Nodes (7): ClientTownsfolk, Folk, StreetWalks, Walk, Streets, ClientTownsfolkTest, StreetWalksTest

### Community 236 - "Kind"
Cohesion: 0.22
Nodes (7): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL

### Community 237 - "net.minecraft.client.Minecraft"
Cohesion: 0.08
Nodes (4): Cited Findings, DistantDepth, DistantArmyRenderer, Rects

### Community 238 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.20
Nodes (6): Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Takeaway, Api, DistantGeneration

### Community 242 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 255 - "BattlePlan"
Cohesion: 0.09
Nodes (7): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot

### Community 256 - "Roads"
Cohesion: 0.11
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 257 - "Settlement"
Cohesion: 0.12
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 271 - ".glow"
Cohesion: 0.29
Nodes (4): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Gaps, Inferences, Takeaway

### Community 274 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.04
Nodes (3): /council <topic>, GateScreen, InventoryStatsButton

### Community 277 - "Realm"
Cohesion: 0.08
Nodes (8): Hook, Kind, FAVOUR, SECRET, Succession, Taking, RealmPlannerTest, World

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.20
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 283 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 285 - ".of"
Cohesion: 0.16
Nodes (5): FactionBanner, Layer, FactionBannerNbt, CityBanners, FactionBannerTest

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 298 - ".plan"
Cohesion: 0.15
Nodes (5): Centre, Faith, Bond, RealmPlanner, Seat

### Community 303 - "Cited Findings"
Cohesion: 0.13
Nodes (6): 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom?, Cited Findings, Gaps, MapColors, LabelShape, PoliticalStyle

### Community 305 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 306 - "Recruiter"
Cohesion: 0.11
Nodes (4): RecruiterRenderer, Recruiter, Offer, Recruitment

### Community 308 - "ConquestNetwork"
Cohesion: 0.03
Nodes (37): ArtillerySet, Bark, BaseMapPart, BattleSpot, BoardAction, BoardView, CameraAt, ChargeHits (+29 more)

### Community 310 - "Override"
Cohesion: 0.15
Nodes (4): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, SettingsScreen

### Community 313 - "Siege"
Cohesion: 0.10
Nodes (7): Face, GateAction, Region, IN, OUT, WALL, Siege

### Community 318 - "PveCommands.java"
Cohesion: 0.09
Nodes (30): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+22 more)

### Community 321 - "assertequals"
Cohesion: 0.06
Nodes (5): Kind, CLOSE, OPEN, RAM, TechTreeTest

### Community 325 - "Realm"
Cohesion: 0.14
Nodes (5): Kind, FRIEND, RIVAL, Realm, CharacterMarkers

### Community 327 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 330 - "OarFigures"
Cohesion: 0.16
Nodes (3): Model, OarFigures, Rowing

### Community 337 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.09
Nodes (4): Cited Findings, DistantArmies, Throw, ThrownFigures

### Community 339 - "Where are full scans, nested loops, repeated lookups and per-tick allocations?"
Cohesion: 0.33
Nodes (4): Gaps, Inferences, Takeaway, Where are full scans, nested loops, repeated lookups and per-tick allocations?

### Community 345 - "HornCall"
Cohesion: 0.33
Nodes (5): HornCall, ATTACKING, MOVING, RALLYING, RETREATING

### Community 350 - "Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI"
Cohesion: 0.40
Nodes (5): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Gaps, Inferences, Takeaway

### Community 351 - "State"
Cohesion: 0.50
Nodes (4): State, CARRIED, RAISING, UP

### Community 352 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 355 - "Cited Findings"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?

### Community 357 - "6. Sodium (and Iris) compatibility"
Cohesion: 0.50
Nodes (4): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Takeaway

### Community 388 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

## Ambiguous Edges - Review These
- `Roads (high, city, town roads; bridges; signposts)` → `Mustering (offensive and ceremonial)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **415 isolated node(s):** `ARCHER_MELEE`, `ARROWS`, `CAVALRY`, `INFANTRY`, `CEREMONIAL` (+410 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1381 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **240 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `net.minecraft.world.level.levelgen.structure.BoundingBox`, `.army`, `org.jetbrains.annotations.Nullable`, `rafasconquest.map.BaseMap`, `SableLink`, `RecruitsBattle.java`, `net.minecraft.server.level.ServerPlayer`, `Arm`, `WarCouncil`, `.sail`, `PveCommands Registration`, `War`, `net.minecraft.world.item.ItemStack`, `rafasconquest.faction.Faction`, `SiegeWorks`, `FieldBlock`, `GrappleHookRenderer.java`, `.startAt`, `PveCommands.java`, `.inCity`, `BattleMap.java`, `.generate`, `Phase`, `net.minecraft.server.level.ServerLevel`, `net.minecraft.world.phys.Vec3`, `HornCall`, `RafasConquest.java`, `UnitOrder`, `net.minecraft.world.phys.Vec3`, `.block`, `net.neoforged.bus.api.SubscribeEvent`, `.sendBackBanner`?**
  _High betweenness centrality (0.094) - this node is a cross-community bridge._
- **What connects `ARCHER_MELEE`, `ARROWS`, `CAVALRY` to the rest of the system?**
  _415 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Politics` be split into smaller, more focused modules?**
  _Cohesion score 0.12727272727272726 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `Settlement`, `Terrain`, `.drawEmpire`, `ClientPolitics`, `org.jetbrains.annotations.Nullable`, `rafasconquest.map.BaseMap`, `ArtilleryMount.java`, `ConquestSavedData`, `net.minecraft.client.gui.GuiGraphics`, `NationScreen`, `WarCouncil`, `City`, `RoadNetwork`, `net.minecraft.world.item.ItemStack`, `.onLevelLoad`, `.plan`, `GateDoors.java`, `Cited Findings`, `MapContext`, `TitleMapTest`, `Realm`, `.generate`, `RafasConquest.java`, `.render`, `.postChunk`, `Grade`, `uuid`, `GrandStrategyScreen`, `OrganizationScreen`?**
  _High betweenness centrality (0.040) - this node is a cross-community bridge._
- **Should `net.minecraft.world.level.levelgen.structure.BoundingBox` be split into smaller, more focused modules?**
  _Cohesion score 0.058640458640458644 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Roads (high, city, town roads; bridges; signposts)` and `Mustering (offensive and ceremonial)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._