# Graph Report - RafasConquest  (2026-11-11)

## Corpus Check
- 468 files · ~413,348 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 6682 nodes · 21233 edges · 336 communities (140 shown, 196 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2842 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `c70116f4`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FieldBlock
- Politics
- net.minecraft.world.level.levelgen.structure.BoundingBox
- .army
- CityTerrain
- BattleScreen
- .onLogout
- net.minecraft.core.BlockPos
- SiegeWorks
- com.mojang.blaze3d.vertex.PoseStack
- net.minecraft.world.entity.Entity
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- Roads
- BaseMap
- Enlisted
- WarCouncil.java
- BattlePlan
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.screens.Screen
- PveCommands.java
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- HorizontalStretch
- CityManagerScreen
- .plan
- org.jetbrains.annotations.Nullable
- .shift
- WarCouncil
- TechTreeScreen
- CityVariant
- ConquestNetwork
- WorldSettings
- PveCommands Registration
- School
- CityPlan
- Land
- Townsperson
- .render
- net.minecraft.core.Direction
- rafasconquest.faction.Faction
- SeaCalm
- Feud
- net.minecraft.server.MinecraftServer
- Faction
- ConquestServer
- Personalities
- SeaLanes
- org.spongepowered.asm.mixin.Mixin
- War
- .findGenerationPoint
- Centre
- Barracks
- Row
- ClientScreens
- SeatCities
- Faction Map View
- RecruitsBattle
- Cited Findings
- MarkerBlock.java
- TitleMapTest
- .saveFormation
- StyleLibrary
- FormationSnapshot
- Layout
- RafasConquest.java
- rafasconquest.battle.FieldBlock
- ShipHulls
- DistantOcclusion
- net.minecraft.world.level.Level
- MapLands
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- PlaceMusicClient
- GateBlockEntity
- Quests
- BattleType
- Recruiter
- Ownership & Territory
- .choose
- Grade
- PoliticsTest
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- .tick
- Retinues
- MapLayer
- .generate
- ConquestSavedData
- .cellMax
- FactionInfoScreen
- CameraView
- CityStyles
- com.talhanation.recruits.entities.AbstractRecruitEntity
- Realm
- MouthLayer.java
- FigureGear
- MarkerScreen
- .send
- Peerage
- .of
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- WaterSwell
- CityTerrainPiece
- WarPlan
- net.minecraft.nbt.CompoundTag
- RoadLayer
- GateBlockEntity.java
- GrandStrategyScreen
- Terrain
- ClientPolitics
- Kind
- Ranks (Soldier to Grand Marshal)
- Cited Findings
- CityLayout
- .place
- PoiPrecomputer.java
- BattleShips
- .render
- .send
- Strategy
- .roadStart
- Override
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- ConquestWorldScreen
- CourtSet
- CityGridBuilder
- Homeland
- .view
- Grid
- CLAUDE.md
- Lands
- net.minecraft.world.level.block.state.BlockState
- .plan
- .place
- How Total War calculates battles between units
- RafasConquest (PvE addon for Villager Recruits)
- .world
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- DayClockTest
- MapContext
- BoardScreen
- CountryPiece
- BattleRole
- .at
- .pack
- GatePlanksBlock.java
- .drawEmpire
- Point
- Morale
- DistantArmyRenderer.java
- Piece
- .save
- net.minecraft.server.level.ServerPlayer
- .touches
- net.neoforged.bus.api.SubscribeEvent
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- .nearest
- DistantArmyRenderer
- net.minecraft.resources.ResourceLocation
- ConquestGameRules
- net.minecraft.core.Vec3i
- Trait
- HornCall
- Call
- Ramming
- .onLogout
- Profiling run sheet (baseline)
- .measure
- .general
- Shape
- OrganizationScreen
- CourtView
- .boundsAt
- Override
- 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound
- Kind
- WarCamp
- Builder
- ConquestConfig
- Settlement
- .postChunk
- .second
- .onLevelLoad
- rafasconquest.battle.FormationSnapshot
- Role
- .build
- Duty
- Recruiter
- ClientBarks
- .step
- UnitMode
- Kind
- ClientModEvents.java

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 287 edges
2. `FieldBattle` - 184 edges
3. `FieldBlock` - 182 edges
4. `FieldArmy` - 128 edges
5. `ConquestNetwork` - 121 edges
6. `Lands` - 89 edges
7. `Point` - 84 edges
8. `BattleScreen` - 82 edges
9. `Politics` - 79 edges
10. `BaseMap` - 77 edges

## Surprising Connections (you probably didn't know these)
- `Conclusion` --references--> `RoadLayer`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Cited Findings` --references--> `RoadLayer`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `Takeaway` --references--> `RoadLayer`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/RoadLayer.java
- `A tick budget and staggered schedules make the fixes stick` --references--> `BaseMap`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/map/BaseMap.java

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

## Communities (336 total, 196 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.04
Nodes (20): BlockGrid, FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock (+12 more)

### Community 1 - "Politics"
Cohesion: 0.12
Nodes (7): Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Event, Front, Politics

### Community 2 - "net.minecraft.world.level.levelgen.structure.BoundingBox"
Cohesion: 0.10
Nodes (5): BarracksPiece, CityRoadPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece

### Community 3 - ".army"
Cohesion: 0.07
Nodes (7): DoctrineTest, FieldBattleTest, GroundPlanTest, Land, GrandStrategyDateTest, ArmouryTest, RetinuesTest

### Community 4 - "CityTerrain"
Cohesion: 0.08
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.08
Nodes (4): BattleScreen, Entry, Menu, UnitInfo

### Community 6 - ".onLogout"
Cohesion: 0.08
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "net.minecraft.core.BlockPos"
Cohesion: 0.09
Nodes (4): Placed, FarmPiece, PlaceholderPiece, GateGeometryTest

### Community 9 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.13
Nodes (5): Cited Findings, Arrow, ArrowVolleys, BattleSpotRing, SableSeen

### Community 10 - "net.minecraft.world.entity.Entity"
Cohesion: 0.06
Nodes (23): 1. Flank and rear shock, 2. Nearby rout (fright), 3. Army-wide losses, 4. Shattering, Design: adding the four morale shocks (built 2026-11-11, untested in game), Tests (FieldBattleTest), The core change: morale becomes a sum, Arm (+15 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "Roads"
Cohesion: 0.17
Nodes (6): Link, Place, Roads, Spur, NobleAnnexTest, RoadsTest

### Community 13 - "BaseMap"
Cohesion: 0.07
Nodes (10): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, BaseMap, Nation, BaseMapStore (+2 more)

### Community 14 - "Enlisted"
Cohesion: 0.09
Nodes (20): Card, Enlisted, Unit, UnitOrder, BRACE, CHARGE, FOLLOW, GO (+12 more)

### Community 15 - "WarCouncil.java"
Cohesion: 0.04
Nodes (40): Ambushes from woods, Base map generated from seed, Battle phases and days, Character page, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Command Table block (+32 more)

### Community 16 - "BattlePlan"
Cohesion: 0.06
Nodes (8): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, BattlePlanTest

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.15
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 20 - "PveCommands.java"
Cohesion: 0.08
Nodes (31): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+23 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.10
Nodes (13): FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoliticalMap, FactionInfo (+5 more)

### Community 27 - ".shift"
Cohesion: 0.20
Nodes (5): BattleSite, Part, Water, BattleSite.Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.16
Nodes (7): Facts, Officer, Said, Talk, WarCouncil, Option, WarCouncilPagesTest

### Community 29 - "TechTreeScreen"
Cohesion: 0.18
Nodes (4): TechTreeScreen, Tech, TechTree, TechTreeTest

### Community 30 - "CityVariant"
Cohesion: 0.20
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 31 - "ConquestNetwork"
Cohesion: 0.03
Nodes (28): BaseMapPart, BattleSpot, BoardAction, BoardView, CameraAt, CityView, ConquestNetwork, FactionInfoReply (+20 more)

### Community 32 - "WorldSettings"
Cohesion: 0.06
Nodes (14): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, NobleAnnex, OriginRoller, Roll (+6 more)

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.15
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 36 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 37 - "Townsperson"
Cohesion: 0.10
Nodes (5): Walk, DistrictKey, Townsfolk, Townsperson, StreetWalksTest

### Community 38 - ".render"
Cohesion: 0.10
Nodes (5): CharacterScreen, Spot, ClientPeerage, Banner, Portraits

### Community 39 - "net.minecraft.core.Direction"
Cohesion: 0.24
Nodes (5): Land, Result, SiegeSurvey, Wall, SiegeSurveyTest

### Community 41 - "SeaCalm"
Cohesion: 0.17
Nodes (3): Depths, SeaCalm, SwellTest

### Community 42 - "Feud"
Cohesion: 0.10
Nodes (8): Clash, Feud, Kind, DUEL, SKIRMISH, Lord, FeudDuel, Feuds

### Community 43 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.14
Nodes (4): Cited Findings, Goal, MusterBars, Wars

### Community 44 - "Faction"
Cohesion: 0.08
Nodes (4): RecruitsBridge, MapPreview, Faction, FactionInfoTest

### Community 45 - "ConquestServer"
Cohesion: 0.13
Nodes (4): CityBanners, ConquestServer, StructureLink, PoiPrecomputer

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (38): Honour, Honours, Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS (+30 more)

### Community 47 - "SeaLanes"
Cohesion: 0.15
Nodes (11): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+3 more)

### Community 48 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.10
Nodes (11): Inferences, Cited Findings, Cited Findings, ChunkMapAccess, ChunkMapCameraMixin, CityVegetationMixin, ClientChunkWindowMixin, RecruitsArrowCleanerMixin (+3 more)

### Community 50 - "War"
Cohesion: 0.09
Nodes (6): Army, Muster, CEREMONIAL, OFFENSIVE, PoliticsSync, War

### Community 53 - "Centre"
Cohesion: 0.14
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 56 - "ClientScreens"
Cohesion: 0.11
Nodes (6): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Gaps, Inferences, Takeaway, ClientScreens, ClientMapCache

### Community 57 - "SeatCities"
Cohesion: 0.12
Nodes (6): City, Grid, SeatCities, CityIndex, SeatCitiesTest, World

### Community 59 - "RecruitsBattle"
Cohesion: 0.06
Nodes (4): Cited Findings, Figure, Landing, RecruitsBattle

### Community 60 - "Cited Findings"
Cohesion: 0.12
Nodes (6): Cited Findings, BattleSounds, ChargeSound, DinSound, Source, DistantArmies

### Community 63 - "TitleMapTest"
Cohesion: 0.13
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 65 - "StyleLibrary"
Cohesion: 0.11
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - "FormationSnapshot"
Cohesion: 0.15
Nodes (6): Cited Findings, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 69 - "RafasConquest.java"
Cohesion: 0.04
Nodes (4): Battle map developer web page, Morale, rout and rally, StreetWalks, RafasConquest

### Community 71 - "ShipHulls"
Cohesion: 0.17
Nodes (5): DistantShips, Far, BattleKind, Part, ShipHulls

### Community 72 - "DistantOcclusion"
Cohesion: 0.13
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 73 - "net.minecraft.world.level.Level"
Cohesion: 0.13
Nodes (4): Sea, GrandStrategyBlock, StrategyBoardBlock, WorldMapItem

### Community 74 - "MapLands"
Cohesion: 0.10
Nodes (5): Gaps, Inferences, Takeaway, Where are full scans, nested loops, repeated lookups and per-tick allocations?, MapLands

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.08
Nodes (24): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+16 more)

### Community 76 - "PlaceMusicClient"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 77 - "GateBlockEntity"
Cohesion: 0.07
Nodes (13): GateDoors, Frame, Ram, SableLink, Door, GateBlockEntity, Swing, CLOSED (+5 more)

### Community 78 - "Quests"
Cohesion: 0.12
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 79 - "BattleType"
Cohesion: 0.14
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.06
Nodes (17): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+9 more)

### Community 84 - "Grade"
Cohesion: 0.16
Nodes (9): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Plan (+1 more)

### Community 86 - "Calibration"
Cohesion: 0.14
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.18
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 94 - "MapLayer"
Cohesion: 0.18
Nodes (5): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer

### Community 95 - ".generate"
Cohesion: 0.11
Nodes (5): BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 96 - "ConquestSavedData"
Cohesion: 0.09
Nodes (4): Cited Findings, ConquestSavedData, EconomyTick, ConquestSavedDataTest

### Community 103 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.14
Nodes (8): Officers, Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER, Side

### Community 105 - "Realm"
Cohesion: 0.11
Nodes (5): Hook, Realm, Succession, Taking, CharacterMarkers

### Community 106 - "MouthLayer.java"
Cohesion: 0.11
Nodes (4): BackBannerLayer, GateRenderer, MarkerRenderer, MouthLayer

### Community 107 - "FigureGear"
Cohesion: 0.14
Nodes (13): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD, Boxes (+5 more)

### Community 109 - ".send"
Cohesion: 0.06
Nodes (14): BackBanner, Bark, ChargeHits, CityClose, OpenBattleScreen, Place, SeaSwell, Entry (+6 more)

### Community 110 - "Peerage"
Cohesion: 0.07
Nodes (13): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+5 more)

### Community 111 - ".of"
Cohesion: 0.18
Nodes (4): FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 112 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.15
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips, Ride

### Community 113 - "WaterSwell"
Cohesion: 0.12
Nodes (8): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, WaterSwell, Swell, SeaFoamTest

### Community 115 - "WarPlan"
Cohesion: 0.19
Nodes (6): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, WarPlan

### Community 116 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.09
Nodes (3): Palette, WallTemplates, NoblePathPiece

### Community 117 - "RoadLayer"
Cohesion: 0.14
Nodes (13): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+5 more)

### Community 123 - "Kind"
Cohesion: 0.15
Nodes (10): Kind, ARCHERS, BEARER, CAVALRY, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY (+2 more)

### Community 124 - "Ranks (Soldier to Grand Marshal)"
Cohesion: 0.18
Nodes (11): Battles (prototype), Blocks of 20 soldiers, Enlisted players, Gold church, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Land grants (seven ways), Peerage (military and noble ladders), Quest board and quests (+3 more)

### Community 125 - "Cited Findings"
Cohesion: 0.20
Nodes (5): 10. Overall ranking, and areas checked that look fine, Cited Findings, Gaps, Inferences, Takeaway

### Community 126 - "CityLayout"
Cohesion: 0.07
Nodes (10): CityLayout, Side, EAST, NORTH, SOUTH, WEST, Site, SlopeTile (+2 more)

### Community 128 - ".place"
Cohesion: 0.07
Nodes (12): Cited Findings, Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Takeaway, Api, DistantGeneration, RoadFeature (+4 more)

### Community 129 - "PoiPrecomputer.java"
Cohesion: 0.04
Nodes (10): ConquestContent, ConquestEntities, CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement (+2 more)

### Community 130 - "BattleShips"
Cohesion: 0.19
Nodes (3): BattleShips, Helm, SiegeFrames

### Community 134 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 137 - "Override"
Cohesion: 0.24
Nodes (3): Land, Land, Land

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.07
Nodes (23): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, Cited Findings, Cited Findings, Gaps (+15 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (40): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 6. Sodium (and Iris) compatibility, 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply (+32 more)

### Community 141 - "ConquestWorldScreen"
Cohesion: 0.20
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 142 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 143 - "CityGridBuilder"
Cohesion: 0.11
Nodes (4): CityGridBuilder, Ground, PlacedWall, Platform

### Community 146 - "Homeland"
Cohesion: 0.10
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 148 - ".view"
Cohesion: 0.07
Nodes (19): Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY, SHIELDMAN, Rank, CAPTAIN (+11 more)

### Community 149 - "Grid"
Cohesion: 0.19
Nodes (4): Countryside, Plan, CountrysideTest, World

### Community 152 - "Lands"
Cohesion: 0.10
Nodes (4): Cited Findings, Lands, Stronghold, Claim

### Community 156 - ".plan"
Cohesion: 0.13
Nodes (7): Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 160 - "How Total War calculates battles between units"
Cohesion: 0.17
Nodes (11): 1. Combat is per soldier, not per unit, 2. Does the swing land?, 3. How much damage the hit does, 4. Charges, 5. Missiles, 6. Leadership (morale): what actually wins battles, 7. Autoresolve, 8. Older games (Shogun 1, Medieval 1, Rome 1) (+3 more)

### Community 163 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 164 - ".world"
Cohesion: 0.17
Nodes (4): Demographics, Share, RealmPlannerTest, World

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.07
Nodes (23): WarHorn, Armoury, Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL (+15 more)

### Community 174 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 204 - "CountryPiece"
Cohesion: 0.16
Nodes (7): Build, CASTLE, HAMLET, RUIN, TOWER, WELL, CountryPiece

### Community 205 - "BattleRole"
Cohesion: 0.22
Nodes (6): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT

### Community 217 - ".pack"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 219 - ".drawEmpire"
Cohesion: 0.27
Nodes (3): Hit, LabelLayer, LabelShape

### Community 221 - "Point"
Cohesion: 0.06
Nodes (24): Point, Engine, LADDERS, RAMS, TOWERS, Gate, GateAction, GateState (+16 more)

### Community 223 - "Morale"
Cohesion: 0.28
Nodes (6): Morale, State, RALLIED, ROUTED, SHATTERED, STEADY

### Community 224 - "DistantArmyRenderer.java"
Cohesion: 0.06
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 226 - "Piece"
Cohesion: 0.13
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 231 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.09
Nodes (10): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, CityManager, GrandStrategy, View (+2 more)

### Community 234 - "net.neoforged.bus.api.SubscribeEvent"
Cohesion: 0.04
Nodes (4): Post, BattleKeepInventory, ClientEvents, ConquestEvents

### Community 235 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.25
Nodes (5): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Battle roles (frontline, bait, support, flanker, kill squad), Victory, pursuit and surrender

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.11
Nodes (5): DistantDepth, DistantArmyRenderer, Figure, Hull, Placed

### Community 238 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.22
Nodes (3): UnitLayer, RecruiterRenderer, TownspersonRenderer

### Community 252 - "Trait"
Cohesion: 0.25
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 254 - "HornCall"
Cohesion: 0.33
Nodes (5): HornCall, ATTACKING, MOVING, RALLYING, RETREATING

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 257 - ".onLogout"
Cohesion: 0.12
Nodes (3): BackBanners, Throw, ThrownFigures

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 267 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 275 - "CourtView"
Cohesion: 0.08
Nodes (5): Retainer, Camera, CourtView, Figure, NationScreen

### Community 277 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 288 - "9. Memory: client caches, textures and NativeImages that are never freed or grow without bound"
Cohesion: 0.40
Nodes (5): 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Gaps, Inferences, Takeaway

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 295 - "Builder"
Cohesion: 0.14
Nodes (5): QuestBoardBlock, Builder, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 301 - "Settlement"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 303 - ".postChunk"
Cohesion: 0.17
Nodes (3): Cited Findings, BarracksPeople, CityGuards

### Community 306 - ".second"
Cohesion: 0.06
Nodes (10): GeneralTask, Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK (+2 more)

### Community 320 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 322 - ".build"
Cohesion: 0.11
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 323 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 339 - ".step"
Cohesion: 0.16
Nodes (4): FigureMotion, Match, State, FigureMotionTest

### Community 344 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 352 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

## Ambiguous Edges - Review These
- `Roads (high, city, town roads; bridges; signposts)` → `Mustering (offensive and ceremonial)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **376 isolated node(s):** `SOLDIER`, `LEADER`, `GENERAL`, `BEARER`, `GUARD` (+371 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1282 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **196 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `net.minecraft.world.level.levelgen.structure.BoundingBox`, `.army`, `.general`, `.roadStart`, `SiegeWorks`, `Shape`, `Enlisted`, `PveCommands.java`, `.shift`, `WarCouncil`, `PveCommands Registration`, `WarCamp`, `net.minecraft.world.item.ItemStack`, `rafasconquest.faction.Faction`, `net.minecraft.core.Direction`, `.second`, `War`, `org.jetbrains.annotations.Nullable`, `Row`, `.saveFormation`, `RafasConquest.java`, `rafasconquest.battle.FieldBlock`, `Calibration`, `UnitMode`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `net.minecraft.server.level.ServerPlayer`, `net.neoforged.bus.api.SubscribeEvent`, `Peerage`, `HornCall`?**
  _High betweenness centrality (0.079) - this node is a cross-community bridge._
- **What connects `SOLDIER`, `LEADER`, `GENERAL` to the rest of the system?**
  _376 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.03697107081174439 - nodes in this community are weakly interconnected._
- **Why does `ConquestNetwork` connect `ConquestNetwork` to `map`, `.send`, `BattleScreen`, `net.minecraft.server.level.ServerPlayer`, `net.minecraft.world.level.Level`, `.send`, `net.minecraft.client.gui.screens.Screen`, `org.jetbrains.annotations.Nullable`, `list`, `CityManagerScreen`, `rafasconquest.battle.FormationSnapshot`, `MarkerBlock.java`?**
  _High betweenness centrality (0.041) - this node is a cross-community bridge._
- **Should `Politics` be split into smaller, more focused modules?**
  _Cohesion score 0.12121212121212122 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Roads (high, city, town roads; bridges; signposts)` and `Mustering (offensive and ceremonial)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._