# Graph Report - RafasConquest  (2026-11-14)

## Corpus Check
- 543 files · ~468,612 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7474 nodes · 24001 edges · 399 communities (158 shown, 241 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3154 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `04be09bb`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- net.minecraft.network.FriendlyByteBuf
- net.minecraft.world.level.levelgen.structure.BoundingBox
- .army
- .siege
- BattleScreen
- DistantHorizonsArmies.java
- CityGridBuilder
- PoliticsTest
- Ride
- Peerage
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- arraylist
- BaseMap
- rafasconquest.map.BaseMap
- ConquestServer
- ConquestSavedData
- Battle Map Web Page & Site
- Person
- net.minecraft.core.BlockPos
- PveCommands.java
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- RecruitsBattle.java
- HorizontalStretch
- CityManagerScreen
- .plan
- org.spongepowered.asm.mixin.Mixin
- Arm
- WarCouncil
- org.jetbrains.annotations.Nullable
- .block
- rafasconquest.battle.FieldBlock
- Lands
- PveCommands Registration
- School
- CityPlan
- .send
- Machine
- net.minecraft.server.level.ServerPlayer
- SiegeWorks
- rafasconquest.faction.Faction
- com.talhanation.recruits.entities.AbstractRecruitEntity
- Front
- GateBlockEntity
- net.minecraft.world.level.block.state.BlockState
- FieldBlock
- Personalities
- SeaLanes
- Cities (grid, plots, vanilla village buildings)
- .say
- net.minecraft.core.Direction
- ProvinceMap
- .plus
- Barracks
- Cited Findings
- CityStyles
- City
- Faction Map View
- .step
- ClientScreens
- MapContext
- TitleMapTest
- Bodies
- StyleLibrary
- FormationSnapshot
- CapitalLayout
- BattleMap.java
- Row
- .generate
- net.minecraft.world.item.DyeColor
- .startAt
- MapLands
- GrappleHooks.java
- Recruiter
- Quests
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Duty
- net.minecraft.server.level.ServerLevel
- Ownership & Territory
- .wallSegments
- .layout
- Land
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- .stat
- .postChunk
- Terrain
- .bark
- Grade
- WarCamp
- FactionInfoScreen
- .view
- ClothRenderer
- Cloth
- FigureBatches
- net.minecraft.core.Vec3i
- Cited Findings
- How Total War calculates battles between units
- Override
- Homeland
- TechTreeScreen
- Rank
- WaterSwell
- FormationSnapshot
- Body
- Trait
- Cited Findings
- .build
- GrandStrategyScreen
- CityTerrain
- .hash
- CityTerrainPiece
- com.mojang.blaze3d.vertex.PoseStack
- GoldChurch.java
- BattleShips
- FigureMotionTest
- CapeCurio.java
- .render
- .shift
- .choose
- SeaCalm
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- Kit
- Organizations
- net.minecraft.nbt.CompoundTag
- NationScreen
- ArtilleryScreen
- CLAUDE.md
- net.minecraft.world.item.DyeColor
- War
- .place
- .roadStart
- .place
- Grid
- RecruitFigures
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- View
- Enlisted
- ThrownFigures
- .collect
- BattleSnapshot
- .load
- .noble
- ConquestWorldgen
- .roadStart
- MapLayer
- NationScreen
- FigureGear
- RecruitFigures.java
- FunctionalInterface
- .accept
- UnitOrder
- .render
- ConquestEvents
- WallTemplates
- Kind
- DistantArmyRenderer
- .boundsAt
- ConquestGameRules
- OrganizationScreen
- CityPlacement.java
- .drawEmpire
- Ranks
- BattlePlan
- Roads
- Settlement
- Profiling run sheet (baseline)
- .measure
- net.minecraft.sounds.SoundEvent
- ClientPolitics
- .rightClick
- org.joml.Matrix4f
- net.minecraft.client.gui.GuiGraphics
- Realm
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- net.minecraft.world.level.Level
- Wind.java
- .of
- BattleSounds
- Kind
- SeatCitiesTest
- .fire
- CityLayout
- .onLevelLoad
- .plan
- .end
- BoardScreen
- BattleKeepInventory
- Call
- ClientModEvents.java
- ConquestNetwork
- ConquestWorldScreen
- Point
- Role
- PoiPrecomputer.java
- assertequals
- Politics.Front
- .settings
- RafasConquest (PvE addon for Villager Recruits)
- GrappleHook
- .officers
- RecruitsBattle
- OarFigures
- RecruiterRenderer.java
- Characters (holders, skills, prowess, traits)
- .findGenerationPoint
- BattleType
- .findGenerationPoint
- .at
- Task
- .touches
- Build
- State
- Politics
- DojoPlacement
- .nationOf
- ItemFlags.java
- BlockGrid
- ClothItemRenderer.java
- ShipKind
- Kind
- .inCity
- NobleDistrictTest
- UnitMode
- Override
- Shape
- ArmouryTest

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 305 edges
2. `FieldBlock` - 222 edges
3. `FieldBattle` - 211 edges
4. `FieldArmy` - 127 edges
5. `ConquestNetwork` - 126 edges
6. `Point` - 108 edges
7. `Lands` - 89 edges
8. `BattleScreen` - 82 edges
9. `Siege` - 80 edges
10. `Politics` - 79 edges

## Surprising Connections (you probably didn't know these)
- `Cited Findings` --references--> `FormationSnapshot`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/battle/FormationSnapshot.java
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

## Communities (399 total, 241 thin omitted)

### Community 0 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.05
Nodes (13): BaseMapPart, BattleSpot, FigureStrike, GateView, HornOrders, PeerageView, PoliticsView, SetSetting (+5 more)

### Community 2 - "net.minecraft.world.level.levelgen.structure.BoundingBox"
Cohesion: 0.06
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.07
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "CityGridBuilder"
Cohesion: 0.08
Nodes (11): Houses, CityGridBuilder, Ground, PlacedWall, Platform, CityVariant, DESERT, PLAINS (+3 more)

### Community 9 - "Ride"
Cohesion: 0.13
Nodes (3): Ride, Swell, SwellTest

### Community 10 - "Peerage"
Cohesion: 0.06
Nodes (13): Advancement, Boards, Entry, View, NobleTitle, COUNT, KNIGHT, NONE (+5 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "arraylist"
Cohesion: 0.07
Nodes (5): Countryside (hamlets, villages, watchtowers, castles), Demographics (population by culture and race), Mantra schools and dojos, Roads (high, city, town roads; bridges; signposts), ConquestConfig

### Community 13 - "BaseMap"
Cohesion: 0.09
Nodes (10): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, BaseMap, Nation, BaseMapStore (+2 more)

### Community 14 - "rafasconquest.map.BaseMap"
Cohesion: 0.12
Nodes (7): Clash, Kind, DUEL, SKIRMISH, FeudDuel, MusterBars, Wars

### Community 15 - "ConquestServer"
Cohesion: 0.09
Nodes (4): CityBanners, ConquestServer, EconomyTick, PoiPrecomputer

### Community 16 - "ConquestSavedData"
Cohesion: 0.13
Nodes (3): Cited Findings, ConquestSavedData, ConquestSavedDataTest

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.25
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "net.minecraft.core.BlockPos"
Cohesion: 0.07
Nodes (3): CommandTableBlock, WorldMapItem, MarkerBlock

### Community 20 - "PveCommands.java"
Cohesion: 0.07
Nodes (29): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+21 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 22 - "RecruitsBattle.java"
Cohesion: 0.05
Nodes (3): /council <topic>, /pve debug recruits, Names

### Community 25 - ".plan"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.09
Nodes (18): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway, Cited Findings, Cited Findings, ArrowSaveMixin (+10 more)

### Community 27 - "Arm"
Cohesion: 0.07
Nodes (18): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+10 more)

### Community 28 - "WarCouncil"
Cohesion: 0.15
Nodes (6): Facts, Officer, Said, Talk, WarCouncil, WarCouncilPagesTest

### Community 32 - "Lands"
Cohesion: 0.11
Nodes (4): Cited Findings, Lands, Stronghold, Claim

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.12
Nodes (4): CityPlan, Piece, Plan, Building

### Community 36 - ".send"
Cohesion: 0.07
Nodes (13): BackBanner, Bark, ChargeHits, CityClose, OpenBattleScreen, SeaSwell, Streets, Entry (+5 more)

### Community 37 - "Machine"
Cohesion: 0.09
Nodes (12): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+4 more)

### Community 38 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.08
Nodes (6): Camera, CameraView, Chunks, CityManager, GrandStrategy, View

### Community 39 - "SiegeWorks"
Cohesion: 0.07
Nodes (11): State, CARRIED, RAISING, UP, Work, SiegeWorks, Swing, CLOSED (+3 more)

### Community 41 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.11
Nodes (6): Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER

### Community 42 - "Front"
Cohesion: 0.13
Nodes (4): Feud, Lord, Feuds, Front

### Community 43 - "GateBlockEntity"
Cohesion: 0.09
Nodes (6): GateDoors, GatePlanksBlock, Ram, SableLink, Door, Hinge

### Community 44 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.09
Nodes (4): Saved, BattleBannerBlock, BattleWallBannerBlock, GateBlock

### Community 45 - "FieldBlock"
Cohesion: 0.04
Nodes (8): The core change: morale becomes a sum, FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (38): Honour, Honours, Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS (+30 more)

### Community 47 - "SeaLanes"
Cohesion: 0.14
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.08
Nodes (18): Ambushes from woods, Base map generated from seed, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Command Table block, Faction Info screen, Deploying to the land (GroundPlan), Land titles (county, duchy, kingdom, empire) (+10 more)

### Community 51 - "net.minecraft.core.Direction"
Cohesion: 0.10
Nodes (5): GateGeometry, HelmBlock, CityGates, Done, GateGeometryTest

### Community 52 - "ProvinceMap"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 53 - ".plus"
Cohesion: 0.11
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 55 - "Cited Findings"
Cohesion: 0.07
Nodes (13): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Cited Findings, Gaps, Inferences, Takeaway, BattleSpotRing, DistantOcclusion (+5 more)

### Community 57 - "City"
Cohesion: 0.14
Nodes (3): City, SeatCities, CityIndex

### Community 58 - "Faction Map View"
Cohesion: 0.14
Nodes (3): Cited Findings, FactionMapScreen, MapView

### Community 59 - ".step"
Cohesion: 0.25
Nodes (4): FigureMotion, Match, Placed, State

### Community 61 - "MapContext"
Cohesion: 0.18
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.11
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - "FormationSnapshot"
Cohesion: 0.13
Nodes (12): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY (+4 more)

### Community 67 - "CapitalLayout"
Cohesion: 0.18
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 69 - "BattleMap.java"
Cohesion: 0.04
Nodes (3): Battle map developer web page, Morale, rout and rally, RafasConquest

### Community 71 - ".generate"
Cohesion: 0.10
Nodes (6): ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 72 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.11
Nodes (9): DistantShips, Far, HullKey, Riding, Want, BattleKind, Part, ShipHulls (+1 more)

### Community 77 - "Quests"
Cohesion: 0.10
Nodes (13): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Type, CLEAR (+5 more)

### Community 78 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.06
Nodes (31): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+23 more)

### Community 79 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 81 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.18
Nodes (4): Land, SiegeSurvey, Wall, SiegeSurveyTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".wallSegments"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 84 - ".layout"
Cohesion: 0.30
Nodes (3): Cited Findings, Sprite, FormationSnapshotTest

### Community 85 - "Land"
Cohesion: 0.17
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 86 - "Calibration"
Cohesion: 0.14
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.06
Nodes (25): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender, Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT (+17 more)

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - ".stat"
Cohesion: 0.12
Nodes (4): CharacterScreen, Retainer, Spot, ClientPeerage

### Community 92 - ".postChunk"
Cohesion: 0.12
Nodes (4): Cited Findings, RecruitsBridge, BarracksPeople, CityGuards

### Community 96 - "Grade"
Cohesion: 0.12
Nodes (13): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+5 more)

### Community 99 - "FactionInfoScreen"
Cohesion: 0.16
Nodes (3): FactionInfoScreen, Line, StructureLink

### Community 100 - ".view"
Cohesion: 0.18
Nodes (6): Merit, Peer, Option, Talk, View, DialogueTest

### Community 101 - "ClothRenderer"
Cohesion: 0.20
Nodes (6): BannerArt, ClothRenderer, Fabric, Kind, Layer, Live

### Community 102 - "Cloth"
Cohesion: 0.12
Nodes (4): Cloth, Solid, ClothTest, WoolTest

### Community 103 - "FigureBatches"
Cohesion: 0.07
Nodes (12): ShipSprite, Batch, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED (+4 more)

### Community 106 - "Cited Findings"
Cohesion: 0.11
Nodes (6): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, ClientMapCache

### Community 107 - "How Total War calculates battles between units"
Cohesion: 0.11
Nodes (17): 1. Combat is per soldier, not per unit, 1. Flank and rear shock, 2. Does the swing land?, 2. Nearby rout (fright), 3. Army-wide losses, 3. How much damage the hit does, 4. Charges, 4. Shattering (+9 more)

### Community 109 - "Homeland"
Cohesion: 0.07
Nodes (21): 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Gaps, Inferences, Takeaway, Homeland, DESERT, JUNGLE (+13 more)

### Community 112 - "Rank"
Cohesion: 0.09
Nodes (13): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+5 more)

### Community 113 - "WaterSwell"
Cohesion: 0.10
Nodes (10): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Columns, SableSeen, Made (+2 more)

### Community 114 - "FormationSnapshot"
Cohesion: 0.29
Nodes (3): Engine, FormationSnapshot, Ship

### Community 115 - "Body"
Cohesion: 0.08
Nodes (26): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+18 more)

### Community 116 - "Trait"
Cohesion: 0.25
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 117 - "Cited Findings"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?

### Community 118 - ".build"
Cohesion: 0.11
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 121 - "CityTerrain"
Cohesion: 0.06
Nodes (18): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+10 more)

### Community 122 - ".hash"
Cohesion: 0.12
Nodes (6): ChunkCoord, FarmLayout, NobleAnnex, OriginRoller, Roll, SeedHash

### Community 126 - "GoldChurch.java"
Cohesion: 0.17
Nodes (7): Gold church, Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter, GoldChurch, Layout

### Community 128 - "BattleShips"
Cohesion: 0.14
Nodes (5): Hull, Ramming, BattleShips, Helm, RammingTest

### Community 130 - "CapeCurio.java"
Cohesion: 0.15
Nodes (4): BackBannerLayer, CapeCurio, Carried, MouthLayer

### Community 132 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 133 - ".choose"
Cohesion: 0.12
Nodes (11): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+3 more)

### Community 134 - "SeaCalm"
Cohesion: 0.24
Nodes (4): Depths, SeaCalm, Strengths, SeaCalmTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (23): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, Cited Findings, Cited Findings, Gaps (+15 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings (+28 more)

### Community 141 - "Kit"
Cohesion: 0.11
Nodes (19): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+11 more)

### Community 142 - "Organizations"
Cohesion: 0.22
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 143 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.05
Nodes (6): ArtilleryMount, BattleBannerBlockEntity, ConquestContent, GateBlockEntity, Oar, Sail

### Community 146 - "NationScreen"
Cohesion: 0.12
Nodes (4): NationScreen, Banner, Demographics, Share

### Community 152 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.17
Nodes (3): CourtSet, Seat, Style

### Community 155 - "War"
Cohesion: 0.09
Nodes (4): Army, Goal, PoliticsSync, War

### Community 160 - ".place"
Cohesion: 0.14
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 163 - "Grid"
Cohesion: 0.20
Nodes (5): Countryside, Plan, Grid, CountrysideTest, World

### Community 164 - "RecruitFigures"
Cohesion: 0.10
Nodes (8): Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures, RecruitFiguresTest

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.13
Nodes (5): WarHorn, BackBanners, Armoury, Outfit, Style

### Community 174 - "View"
Cohesion: 0.15
Nodes (4): BoardView, GrandStrategyView, StatsView, TalkView

### Community 178 - "Enlisted"
Cohesion: 0.15
Nodes (3): Card, Enlisted, Unit

### Community 206 - ".noble"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 217 - "ConquestWorldgen"
Cohesion: 0.12
Nodes (3): CitySlopeStructure, ConquestWorldgen, FarmStructure

### Community 218 - ".roadStart"
Cohesion: 0.23
Nodes (3): DistrictKey, Townsfolk, StreetWalksTest

### Community 219 - "MapLayer"
Cohesion: 0.15
Nodes (6): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer, UnitLayer

### Community 221 - "NationScreen"
Cohesion: 0.17
Nodes (3): Camera, CourtView, Figure

### Community 223 - "FigureGear"
Cohesion: 0.18
Nodes (7): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 228 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 232 - ".render"
Cohesion: 0.15
Nodes (5): ClientTownsfolk, Folk, StreetWalks, Walk, ClientTownsfolkTest

### Community 234 - "ConquestEvents"
Cohesion: 0.05
Nodes (3): Post, ClientEvents, ConquestEvents

### Community 236 - "Kind"
Cohesion: 0.22
Nodes (7): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.10
Nodes (3): DistantDepth, DistantArmyRenderer, Rects

### Community 242 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 252 - ".drawEmpire"
Cohesion: 0.27
Nodes (3): Hit, LabelLayer, LabelShape

### Community 254 - "Ranks"
Cohesion: 0.06
Nodes (17): Enlisted players, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Battle roles (frontline, bait, support, flanker, kill squad), The war horn, BattleRole, BAIT, FLANKER, FRONTLINE (+9 more)

### Community 255 - "BattlePlan"
Cohesion: 0.04
Nodes (13): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, WarPlan (+5 more)

### Community 256 - "Roads"
Cohesion: 0.10
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

### Community 271 - "org.joml.Matrix4f"
Cohesion: 0.21
Nodes (3): Arrow, ArrowVolleys, Figure

### Community 274 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.05
Nodes (3): Races (Sunborn, Vinekin, Meadowfolk, ...), GateScreen, InventoryStatsButton

### Community 277 - "Realm"
Cohesion: 0.10
Nodes (6): Hook, Realm, Succession, Taking, RealmPlannerTest, World

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.21
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 282 - "net.minecraft.world.level.Level"
Cohesion: 0.08
Nodes (7): ArtilleryBlock, GrandStrategyBlock, OarBlock, QuestBoardBlock, SailBlock, StrategyBoardBlock, Builder

### Community 285 - ".of"
Cohesion: 0.18
Nodes (4): FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 288 - "BattleSounds"
Cohesion: 0.25
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 292 - ".fire"
Cohesion: 0.08
Nodes (5): MarkerRenderer, CharacterMarkers, Placed, MarkerBlockEntity, MarkerSpec

### Community 295 - "CityLayout"
Cohesion: 0.09
Nodes (6): CityLayout, District, Site, SlopeTile, WorldSettings, CityLayoutTest

### Community 298 - ".plan"
Cohesion: 0.12
Nodes (8): Centre, Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 300 - ".end"
Cohesion: 0.06
Nodes (12): Inferences, Api, DistantGeneration, Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE (+4 more)

### Community 305 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 306 - "ClientModEvents.java"
Cohesion: 0.07
Nodes (5): ClientBarks, Line, ClientModEvents, net.neoforged.neoforge.client.extensions.common.IClientItemExtensions, Registration

### Community 308 - "ConquestNetwork"
Cohesion: 0.07
Nodes (12): ArtillerySet, BoardAction, CameraAt, CityView, ConquestNetwork, FactionInfoReply, GateSet, MarkerSet (+4 more)

### Community 310 - "ConquestWorldScreen"
Cohesion: 0.22
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 313 - "Point"
Cohesion: 0.05
Nodes (18): Point, Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate (+10 more)

### Community 315 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 318 - "PoiPrecomputer.java"
Cohesion: 0.08
Nodes (14): Faction, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoliticalMap (+6 more)

### Community 321 - "assertequals"
Cohesion: 0.06
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 325 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.17
Nodes (8): Arrows in flight (figure volleys), Battle sounds, Recruits PvE Addon Design Doc (PDF), Seeing the whole field (distant flat figures), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 329 - "RecruitsBattle"
Cohesion: 0.06
Nodes (8): Cited Findings, HornCall, ATTACKING, MOVING, RALLYING, RETREATING, Landing, RecruitsBattle

### Community 330 - "OarFigures"
Cohesion: 0.17
Nodes (3): Model, OarFigures, Rowing

### Community 337 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.09
Nodes (21): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Character page, Characters (holders, skills, prowess, traits), Court 3D scene (CourtView), Cultures (seven homelands), Envelopment (+13 more)

### Community 344 - "BattleType"
Cohesion: 0.15
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 352 - "Task"
Cohesion: 0.22
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 364 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 367 - "State"
Cohesion: 0.20
Nodes (8): Morale, State, RALLIED, ROUTED, SHATTERED, STEADY, UnitInfo, UnitsState

### Community 369 - "Politics"
Cohesion: 0.10
Nodes (10): Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Muster, CEREMONIAL, OFFENSIVE (+2 more)

### Community 388 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 389 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 392 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 394 - "Override"
Cohesion: 0.15
Nodes (3): BattleSite.Water, BattleSite.Water, SettingsScreen

### Community 402 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **414 isolated node(s):** `COUNT`, `KNIGHT`, `NONE`, `NO_LIGHTMAP`, `NO_SHADER_COLOUR` (+409 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1381 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **241 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `net.minecraft.world.level.levelgen.structure.BoundingBox`, `.army`, `.inCity`, `UnitMode`, `Peerage`, `rafasconquest.map.BaseMap`, `Shape`, `PveCommands.java`, `RecruitsBattle.java`, `WarCouncil`, `org.jetbrains.annotations.Nullable`, `.block`, `rafasconquest.battle.FieldBlock`, `PveCommands Registration`, `net.minecraft.server.level.ServerPlayer`, `SiegeWorks`, `net.minecraft.world.item.ItemStack`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `rafasconquest.faction.Faction`, `.end`, `FieldBlock`, `Enlisted`, `.say`, `BattleMap.java`, `GrappleHook`, `.officers`, `.startAt`, `net.minecraft.server.level.ServerLevel`, `Calibration`, `Task`, `WarCamp`, `UnitOrder`, `ConquestEvents`, `ConquestEvents.java`?**
  _High betweenness centrality (0.072) - this node is a cross-community bridge._
- **What connects `COUNT`, `KNIGHT`, `NONE` to the rest of the system?**
  _414 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `net.minecraft.network.FriendlyByteBuf` be split into smaller, more focused modules?**
  _Cohesion score 0.05142857142857143 - nodes in this community are weakly interconnected._
- **Why does `ConquestSavedData` connect `ConquestSavedData` to `PveCommands Registration`, `FactionInfoScreen`, `BattleMap.java`, `rafasconquest.faction.Faction`, `arraylist`, `ConquestServer`, `ConquestEvents.java`, `.collect`, `War`, `.postChunk`, `org.jetbrains.annotations.Nullable`, `PoiPrecomputer.java`?**
  _High betweenness centrality (0.041) - this node is a cross-community bridge._
- **Should `GateDoors.java` be split into smaller, more focused modules?**
  _Cohesion score 0.09009009009009009 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._