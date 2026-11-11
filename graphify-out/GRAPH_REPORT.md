# Graph Report - RafasConquest  (2026-11-11)

## Corpus Check
- 464 files · ~401,696 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 6545 nodes · 20728 edges · 350 communities (146 shown, 204 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2747 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `8b7ca20d`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FieldBlock
- PoiPrecomputer.java
- net.minecraft.world.level.levelgen.structure.BoundingBox
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsLink
- net.minecraft.network.FriendlyByteBuf
- SiegeWorks
- Arm
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- Roads
- BaseMap
- ConquestNetwork
- Peerage
- org.junit.jupiter.api.Test
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.screens.Screen
- RafasConquest Commands (guide v0.0.2)
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- HorizontalStretch
- CityManagerScreen
- .plan
- CityGates.java
- .shift
- WarCouncil
- Quests
- ConquestEvents.java
- Feud
- CapitalLayout
- PveCommands Registration
- School
- CityPlan
- Land
- net.minecraft.world.level.Level
- FormationSnapshot
- net.minecraft.core.Direction
- rafasconquest.faction.Faction
- SiegeWorks.java
- Faction
- War
- ConquestSavedData
- net.minecraft.server.level.ServerPlayer
- Personalities
- SeaLanes
- org.spongepowered.asm.mixin.Mixin
- .drawEmpire
- .roadStart
- com.talhanation.recruits.entities.AbstractRecruitEntity
- Centre
- .postChunk
- Row
- Retinues
- City
- Faction Map View
- .wallSegments
- CityVariant
- net.minecraft.world.level.block.state.BlockState
- TitleMapTest
- .plan
- StyleLibrary
- .render
- net.minecraft.world.item.ItemStack
- RafasConquest.java
- .render
- RecruitsBattle
- .onRegisterClientCommands
- CityGridBuilder
- MapLands
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- PlaceMusicClient
- net.minecraft.server.level.ServerLevel
- org.joml.Matrix4f
- .simulate
- SeaCalm
- Ownership & Territory
- .choose
- .view
- PoliticsTest
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- CourtView
- Barracks
- .generate
- ConquestServer
- WaterSwell
- FactionInfoScreen
- arraylist
- rafasconquest.battle.FieldBlock
- CityStyles
- Override
- Realm
- MouthLayer.java
- rafasconquest.battle.FormationSnapshot
- MarkerScreen
- Lands
- .player
- .of
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- PlaceholderPiece
- Cities (grid, plots, vanilla village buildings)
- RecruitsBattle.java
- GateBlockEntity.java
- RoadLayer
- .view
- GrandStrategyScreen
- Terrain
- ClientPolitics
- Strategy
- UnitOrder
- Settlement
- CityLayout
- .place
- net.minecraft.world.item.DyeColor
- BattleShips
- MapContext
- StylePiece
- BlockPos
- Kit
- .world
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- OrganizationScreen
- .lineUp
- Wars (daily nation decisions)
- QuestBoardBlock
- Job
- Townsperson
- CLAUDE.md
- net.minecraft.nbt.CompoundTag
- GroundPlan
- .plan
- PoliticalStyle
- Army
- View
- Small Cluster 170
- ClientEvents
- .hash
- .boundsAt
- ConquestWorldScreen
- NobleDistrictTest
- net.neoforged.neoforge.network.handling.IPayloadContext
- .pack
- net.minecraft.core.Vec3i
- .onLevelLoad
- Siege
- Cited Findings
- DistantArmyRenderer.java
- CityTerrainPiece
- .touches
- .sendVolleys
- Cited Findings
- net.neoforged.bus.api.SubscribeEvent
- BattleSounds
- ShipKind
- Phase
- .tick
- ConquestGameRules
- BattleRole
- Trait
- ClientBarks
- Call
- Ramming
- Task
- Profiling run sheet (baseline)
- .measure
- Distribution
- TitleMap
- Organizations
- CourtSet
- .inCity
- Kind
- .hinge
- BattleKeepInventory
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- Kind
- WarCamp
- ClientScreens
- Swell
- .named
- Characters (holders, skills, prowess, traits)
- RafasConquest (PvE addon for Villager Recruits)
- CountryPiece
- Type
- .view
- net.minecraft.world.phys.Vec3
- Shape
- GatePlanksBlock.java
- Role
- .general
- ItemFlags.java
- Cited Findings
- .projection
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- BlockGrid
- .build
- .block
- CityLayoutTest
- Kind
- ClientModEvents.java
- Ranks (Soldier to Grand Marshal)
- PoliticsData
- ConquestConfig
- How do the simulation and daily-decision systems schedule work? Do they spike?

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 279 edges
2. `FieldBattle` - 170 edges
3. `FieldBlock` - 164 edges
4. `ConquestNetwork` - 121 edges
5. `FieldArmy` - 111 edges
6. `Lands` - 89 edges
7. `Point` - 85 edges
8. `Politics` - 79 edges
9. `BaseMap` - 77 edges
10. `BattleScreen` - 75 edges

## Surprising Connections (you probably didn't know these)
- `Inferences` --references--> `PoliticalMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/map/PoliticalMap.java
- `Inferences` --references--> `Politics`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/politics/Politics.java
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

## Communities (350 total, 204 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.06
Nodes (7): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 1 - "PoiPrecomputer.java"
Cohesion: 0.07
Nodes (10): SeedHash, TerritoryMap, ResourceType, FOOD, PoliticalMap, FactionInfo, PoiType, CAPITAL (+2 more)

### Community 2 - "net.minecraft.world.level.levelgen.structure.BoundingBox"
Cohesion: 0.10
Nodes (5): BarracksPiece, CityRoadPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece

### Community 3 - ".army"
Cohesion: 0.06
Nodes (4): DoctrineTest, FieldBattleTest, GroundPlanTest, Land

### Community 4 - "CityTerrain"
Cohesion: 0.09
Nodes (19): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+11 more)

### Community 5 - "BattleScreen"
Cohesion: 0.08
Nodes (4): BattleScreen, Entry, Menu, UnitInfo

### Community 6 - "DistantHorizonsLink"
Cohesion: 0.06
Nodes (9): 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), Cited Findings, Gaps, Inferences, Takeaway, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+1 more)

### Community 7 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.06
Nodes (9): BaseMapPart, BattleSnapshot, FactionInfoReply, PeerageView, PoliticsView, StatsView, UnitsRequest, UnitsState (+1 more)

### Community 8 - "SiegeWorks"
Cohesion: 0.11
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 10 - "Arm"
Cohesion: 0.05
Nodes (27): Arm, ARCHERS, CAVALRY, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, SKIRMISHERS, ZWEIHANDLERS (+19 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "Roads"
Cohesion: 0.10
Nodes (15): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+7 more)

### Community 13 - "BaseMap"
Cohesion: 0.14
Nodes (9): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, BaseMap, Nation, BaseMapStore (+1 more)

### Community 14 - "ConquestNetwork"
Cohesion: 0.06
Nodes (20): BackBanner, Bark, CameraAt, ChargeHits, CityClose, CityView, ConquestNetwork, FigureStrike (+12 more)

### Community 15 - "Peerage"
Cohesion: 0.09
Nodes (11): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+3 more)

### Community 16 - "org.junit.jupiter.api.Test"
Cohesion: 0.05
Nodes (12): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, WarPlan (+4 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.23
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "net.minecraft.client.gui.screens.Screen"
Cohesion: 0.04
Nodes (7): Character page, Court 3D scene (CourtView), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), GateScreen, InventoryStatsButton, Portraits

### Community 20 - "RafasConquest Commands (guide v0.0.2)"
Cohesion: 0.09
Nodes (28): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+20 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 27 - ".shift"
Cohesion: 0.20
Nodes (5): BattleSite, Part, Water, BattleSite.Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.15
Nodes (6): Facts, Officer, Said, Talk, WarCouncil, WarCouncilPagesTest

### Community 29 - "Quests"
Cohesion: 0.07
Nodes (13): TechTreeScreen, DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind (+5 more)

### Community 30 - "ConquestEvents.java"
Cohesion: 0.03
Nodes (8): /pve debug citystats [samples], /pve debug cityterrain, ConquestEntities, CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, FarmStructure

### Community 31 - "Feud"
Cohesion: 0.11
Nodes (8): Clash, Feud, Kind, DUEL, SKIRMISH, Lord, FeudDuel, Feuds

### Community 32 - "CapitalLayout"
Cohesion: 0.15
Nodes (6): CapitalLayout, Land, Site, WorldSettings, CapitalRelocationTest, LayoutTest

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.15
Nodes (3): CityPlan, Piece, Building

### Community 36 - "Land"
Cohesion: 0.20
Nodes (4): Heap, Land, Search, ShippingRoutes

### Community 37 - "net.minecraft.world.level.Level"
Cohesion: 0.13
Nodes (3): GrandStrategyBlock, StrategyBoardBlock, WorldMapItem

### Community 38 - "FormationSnapshot"
Cohesion: 0.09
Nodes (11): Cited Findings, FormationSnapshot, Positions, Ship, Sprite, FigureMotion, Match, Placed (+3 more)

### Community 39 - "net.minecraft.core.Direction"
Cohesion: 0.24
Nodes (5): Land, Result, SiegeSurvey, Wall, SiegeSurveyTest

### Community 41 - "SiegeWorks.java"
Cohesion: 0.04
Nodes (6): Kind, CLOSE, OPEN, RAM, StreetWalks, RafasConquest

### Community 42 - "Faction"
Cohesion: 0.11
Nodes (8): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction, FactionType, BANDIT, CITY_STATE, EMPIRE

### Community 43 - "War"
Cohesion: 0.08
Nodes (6): Cited Findings, Goal, MusterBars, PoliticsSync, War, Wars

### Community 44 - "ConquestSavedData"
Cohesion: 0.06
Nodes (8): Cited Findings, ConquestSavedData, MapPreview, Poi, EconomyTick, PoiPrecomputer, ConquestSavedDataTest, FactionInfoTest

### Community 45 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.08
Nodes (12): Inferences, Cited Findings, Gaps, Inferences, Mixins on server paths: what they hook, how hot, added cost, Takeaway, CameraView, Chunks (+4 more)

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.12
Nodes (11): Battle code costs 0.2 ms; the multi-second freezes come from blocking, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, Cited Findings, Gaps, Inferences, Takeaway, CityVegetationMixin, ClientChunkWindowMixin (+3 more)

### Community 50 - ".drawEmpire"
Cohesion: 0.26
Nodes (3): Hit, LabelLayer, LabelShape

### Community 51 - ".roadStart"
Cohesion: 0.10
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Plan (+1 more)

### Community 52 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.11
Nodes (8): Officers, Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER, Side

### Community 53 - "Centre"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 54 - ".postChunk"
Cohesion: 0.16
Nodes (3): Cited Findings, BarracksPeople, CityGuards

### Community 57 - "City"
Cohesion: 0.10
Nodes (7): Countryside, Plan, City, Grid, CityIndex, CountrysideTest, World

### Community 58 - "Faction Map View"
Cohesion: 0.11
Nodes (5): Cited Findings, FactionMapScreen, MapView, Place, PlaceMenuLayer

### Community 59 - ".wallSegments"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 60 - "CityVariant"
Cohesion: 0.13
Nodes (7): PlacedWall, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 61 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.08
Nodes (5): Saved, CommandTableBlock, GateBlock, Builder, MarkerBlock

### Community 65 - "StyleLibrary"
Cohesion: 0.11
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - ".render"
Cohesion: 0.07
Nodes (14): CharacterScreen, Retainer, Spot, ClientPeerage, Banner, Honour, Honours, Stat (+6 more)

### Community 67 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.12
Nodes (6): WarHorn, BackBanners, Armoury, Outfit, Style, RankKits

### Community 72 - ".onRegisterClientCommands"
Cohesion: 0.25
Nodes (4): Mode, OFF, ON, VIEW

### Community 73 - "CityGridBuilder"
Cohesion: 0.10
Nodes (3): CityGridBuilder, Ground, Platform

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.08
Nodes (24): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+16 more)

### Community 76 - "PlaceMusicClient"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 77 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.07
Nodes (9): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+1 more)

### Community 78 - "org.joml.Matrix4f"
Cohesion: 0.21
Nodes (3): DistantDepth, Arrow, ArrowVolleys

### Community 79 - ".simulate"
Cohesion: 0.07
Nodes (7): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome, TickTimes

### Community 81 - "SeaCalm"
Cohesion: 0.22
Nodes (3): Sea, Depths, SeaCalm

### Community 82 - "Ownership & Territory"
Cohesion: 0.20
Nodes (5): Candidate, OwnershipResolver, Claim, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.15
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 84 - ".view"
Cohesion: 0.22
Nodes (4): Option, Talk, View, DialogueTest

### Community 86 - "Calibration"
Cohesion: 0.14
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.16
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 92 - "CourtView"
Cohesion: 0.08
Nodes (6): Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 95 - ".generate"
Cohesion: 0.06
Nodes (20): BaseMapGenerator, Heights, BiomePalette, Entry, Homeland, DESERT, JUNGLE, PLAINS (+12 more)

### Community 98 - "WaterSwell"
Cohesion: 0.18
Nodes (6): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, WaterSwell

### Community 100 - "arraylist"
Cohesion: 0.07
Nodes (15): Base map generated from seed, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Demographics (population by culture and race), Faction Info screen, Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, World map layers (+7 more)

### Community 101 - "rafasconquest.battle.FieldBlock"
Cohesion: 0.09
Nodes (3): Figure, Landing, Live

### Community 105 - "Realm"
Cohesion: 0.13
Nodes (4): Hook, Realm, Succession, Taking

### Community 106 - "MouthLayer.java"
Cohesion: 0.10
Nodes (4): BackBannerLayer, GateRenderer, MarkerRenderer, MouthLayer

### Community 107 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.27
Nodes (6): Boxes, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 109 - "Lands"
Cohesion: 0.07
Nodes (9): Cited Findings, Lands, Stronghold, Claim, Front, Muster, CEREMONIAL, OFFENSIVE (+1 more)

### Community 110 - ".player"
Cohesion: 0.16
Nodes (5): NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 111 - ".of"
Cohesion: 0.12
Nodes (5): FactionBanner, Layer, FactionBannerNbt, CityBanners, FactionBannerTest

### Community 112 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.20
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 114 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.18
Nodes (5): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), TickWeave experiment

### Community 115 - "RecruitsBattle.java"
Cohesion: 0.05
Nodes (3): /council <topic>, /pve debug recruits, Names

### Community 116 - "GateBlockEntity.java"
Cohesion: 0.11
Nodes (3): ConquestContent, MarkerBlockEntity, MarkerSpec

### Community 117 - "RoadLayer"
Cohesion: 0.17
Nodes (11): A tick budget and staggered schedules make the fixes stick, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom?, Gaps (+3 more)

### Community 118 - ".view"
Cohesion: 0.24
Nodes (7): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, GrandStrategy, View

### Community 123 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 124 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 125 - "Settlement"
Cohesion: 0.12
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 126 - "CityLayout"
Cohesion: 0.09
Nodes (5): CityLayout, District, Site, SlopeTile, CityLayoutTest

### Community 128 - ".place"
Cohesion: 0.06
Nodes (12): Cited Findings, Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Takeaway, Api, DistantGeneration, RoadFeature (+4 more)

### Community 129 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.14
Nodes (6): DistantShips, Far, SableSeen, BattleKind, Part, ShipHulls

### Community 130 - "BattleShips"
Cohesion: 0.20
Nodes (5): BattleShips, Frame, Helm, Ram, SableLink

### Community 132 - "MapContext"
Cohesion: 0.09
Nodes (8): BaseMapLayer, MapContext, LegendEntry, MapLayer, Symbol, PoiIconLayer, TerritoryLayer, UnitLayer

### Community 133 - "StylePiece"
Cohesion: 0.14
Nodes (3): StylePiece, GoldChurch, Layout

### Community 134 - "BlockPos"
Cohesion: 0.11
Nodes (5): BoardScreen, BoardAction, GateSet, GateView, MarkerView

### Community 136 - "Kit"
Cohesion: 0.11
Nodes (18): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+10 more)

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (24): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings, Cited Findings (+16 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.06
Nodes (35): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 5. Water/ocean waves (sea swell, foam, boats on waves), 6. Sodium (and Iris) compatibility, 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings (+27 more)

### Community 143 - "Wars (daily nation decisions)"
Cohesion: 0.22
Nodes (7): Battles (prototype), Blocks of 20 soldiers, Envelopment, Heralds (daily news), Mustering (offensive and ceremonial), War camps, Wars (daily nation decisions)

### Community 148 - "Job"
Cohesion: 0.22
Nodes (6): Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY, SHIELDMAN

### Community 149 - "Townsperson"
Cohesion: 0.05
Nodes (9): RecruiterRenderer, TownspersonRenderer, Walk, Barks, Ear, Recruiter, DistrictKey, Townsfolk (+1 more)

### Community 152 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.08
Nodes (4): Palette, WallTemplates, Placed, NoblePathPiece

### Community 155 - "GroundPlan"
Cohesion: 0.13
Nodes (5): Ground, GroundPlan, Land, Land, Land

### Community 156 - ".plan"
Cohesion: 0.12
Nodes (7): Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 164 - "View"
Cohesion: 0.15
Nodes (3): BoardView, GrandStrategyView, TalkView

### Community 178 - ".hash"
Cohesion: 0.14
Nodes (5): NobleAnnex, OriginRoller, Roll, ProvinceNames, NobleAnnexTest

### Community 181 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 204 - "ConquestWorldScreen"
Cohesion: 0.20
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 206 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.11
Nodes (5): BattleSpot, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 217 - ".pack"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 218 - "net.minecraft.core.Vec3i"
Cohesion: 0.08
Nodes (6): ChunkCoord, FarmLayout, CityPlacement, CountryPlacement, DojoPlacement, FarmPlacement

### Community 221 - "Siege"
Cohesion: 0.07
Nodes (15): Point, Engine, LADDERS, RAMS, TOWERS, Gate, GateAction, GateState (+7 more)

### Community 223 - "Cited Findings"
Cohesion: 0.09
Nodes (10): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Cited Findings, Gaps, Inferences, Takeaway, BattleSpotRing, DistantArmyRenderer (+2 more)

### Community 224 - "DistantArmyRenderer.java"
Cohesion: 0.05
Nodes (4): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), DistantOcclusion

### Community 232 - "Cited Findings"
Cohesion: 0.09
Nodes (9): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, DistantArmies, ClientMapCache, Throw (+1 more)

### Community 235 - "BattleSounds"
Cohesion: 0.20
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 236 - "ShipKind"
Cohesion: 0.33
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 237 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 249 - "BattleRole"
Cohesion: 0.25
Nodes (6): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT

### Community 252 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 257 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 267 - "TitleMap"
Cohesion: 0.10
Nodes (5): SeatCities, TitleMap, TitleStyle, SeatCitiesTest, World

### Community 274 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 275 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 278 - "Kind"
Cohesion: 0.15
Nodes (10): Kind, ARCHERS, BEARER, CAVALRY, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY (+2 more)

### Community 288 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.17
Nodes (9): Battle phases and days, Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Enlisted players, The general and guard, Battle roles (frontline, bait, support, flanker, kill squad), Victory, pursuit and surrender, War council (+1 more)

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 295 - "Swell"
Cohesion: 0.15
Nodes (3): Ride, Swell, SwellTest

### Community 300 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.33
Nodes (7): Characters (holders, skills, prowess, traits), Cultures (seven homelands), Epic Knights optional armour (Armoury), Faiths and cults, Martial titles (Sword Saint, Sword King, Sword God), Organizations, Sword sanctums

### Community 301 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 302 - "CountryPiece"
Cohesion: 0.16
Nodes (7): Build, CASTLE, HAMLET, RUIN, TOWER, WELL, CountryPiece

### Community 303 - "Type"
Cohesion: 0.33
Nodes (4): Type, CLEAR, DELIVER, SCOUT

### Community 305 - ".view"
Cohesion: 0.08
Nodes (14): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+6 more)

### Community 306 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.09
Nodes (10): Camera, Card, Enlisted, Unit, UnitMode, CHARGE, FOLLOW, HOLD (+2 more)

### Community 308 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 310 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 322 - ".build"
Cohesion: 0.11
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 327 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 332 - "Ranks (Soldier to Grand Marshal)"
Cohesion: 0.33
Nodes (6): Gold church, Land grants (seven ways), Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter

### Community 344 - "How do the simulation and daily-decision systems schedule work? Do they spike?"
Cohesion: 0.50
Nodes (4): Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway

## Ambiguous Edges - Review These
- `Roads (high, city, town roads; bridges; signposts)` → `Mustering (offensive and ceremonial)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **363 isolated node(s):** `ADVANCE`, `CAVALRY_CHARGE`, `CAVALRY_RECALL`, `WALL`, `GENERAL_CHARGES` (+358 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1244 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **204 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `FieldBlock`, `net.minecraft.world.item.DyeColor`, `net.minecraft.world.level.levelgen.structure.BoundingBox`, `.army`, `SiegeWorks`, `.isOurs`, `.inCity`, `.shift`, `WarCouncil`, `ConquestEvents.java`, `PveCommands Registration`, `WarCamp`, `net.minecraft.core.Direction`, `rafasconquest.faction.Faction`, `SiegeWorks.java`, `War`, `net.minecraft.server.level.ServerPlayer`, `net.minecraft.world.phys.Vec3`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `Shape`, `.general`, `Cited Findings`, `net.minecraft.world.item.ItemStack`, `.block`, `RafasConquest.java`, `.simulate`, `Calibration`, `rafasconquest.battle.FieldBlock`, `.sendVolleys`, `net.neoforged.bus.api.SubscribeEvent`, `Phase`, `UnitOrder`, `RecruitsBattle.java`, `Trait`?**
  _High betweenness centrality (0.081) - this node is a cross-community bridge._
- **What connects `ADVANCE`, `CAVALRY_CHARGE`, `CAVALRY_RECALL` to the rest of the system?**
  _363 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.05533769063180828 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `.place`, `PoiPrecomputer.java`, `MapContext`, `TitleMap`, `OrganizationScreen`, `net.minecraft.client.gui.screens.Screen`, `.plan`, `WarCouncil`, `PoliticalStyle`, `School`, `SiegeWorks.java`, `War`, `ConquestSavedData`, `.drawEmpire`, `Centre`, `.postChunk`, `City`, `.render`, `net.minecraft.world.item.ItemStack`, `.onLevelLoad`, `CourtView`, `.generate`, `arraylist`, `Cited Findings`, `Realm`, `.of`, `RecruitsBattle.java`, `RoadLayer`, `GrandStrategyScreen`, `Terrain`, `ClientPolitics`, `Settlement`?**
  _High betweenness centrality (0.038) - this node is a cross-community bridge._
- **Should `PoiPrecomputer.java` be split into smaller, more focused modules?**
  _Cohesion score 0.07239819004524888 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Roads (high, city, town roads; bridges; signposts)` and `Mustering (offensive and ceremonial)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._