# Graph Report - RafasConquest  (2026-11-16)

## Corpus Check
- 578 files · ~507,278 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7964 nodes · 25735 edges · 420 communities (157 shown, 263 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3283 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `5037b06d`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Calibration
- World map (CK3-style political map)
- net.minecraft.nbt.CompoundTag
- org.junit.jupiter.api.Test
- Feud
- BattleScreen
- ConquestSavedData
- .passOnOrders
- ArtilleryMount.java
- RecruitsBattle
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- PoliticsTest
- BaseMap
- RecruitsBattle.java
- net.minecraft.world.level.block.state.BlockState
- map
- RafasConquest Project Site
- Person
- CityLayout
- .postChunk
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- .encode
- net.minecraft.server.level.ServerLevel
- CityHud
- Tier
- UnitMode
- ConquestEvents
- .enlist
- net.neoforged.bus.api.SubscribeEvent
- .map
- .render
- BattleShips
- PveCommands
- .map
- CityPlan
- WaterSwell
- .send
- rafasconquest.faction.Faction
- Succession
- Arm
- Point
- net.minecraft.client.gui.GuiGraphics
- .view
- Personality
- SeaLanes
- BattlePlan
- .bark
- net.minecraft.world.level.block.state.BlockState
- Cited Findings
- Barracks
- .render
- CityStyles
- .siege
- .deployment
- CityTerrain
- net.minecraft.world.phys.Vec3
- HorizontalStretch
- TitleMap
- Bodies
- StyleLibrary
- Kind
- WorldSettings
- BattleMap.java
- Row
- School
- DistantShips
- org.spongepowered.asm.mixin.Mixin
- MapLands
- WarCouncil
- MarkerBlock.java
- Roads
- RecruitFigures
- .plan
- net.minecraft.core.Direction
- Candidate
- Faction
- SailBlock.java
- BattleBannerBlockEntity
- .fire
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- ClientScreens
- DialogueScreen
- Recruiter
- CityVariant
- .hash
- Characters (holders, skills, prowess, traits)
- MapLayer
- CityGridBuilder
- FactionInfoScreen
- NationScreen
- .deploy
- .aWoodNearAWingHidesAnAmbushThatStaysPutUntilItStrikes
- FigureBatches
- rafasconquest.battle.BattlePlan.Point
- WarCamp
- How Total War calculates battles between units
- MapContext
- net.minecraft.server.level.ServerPlayer
- CityPanel
- .step
- ShipHulls
- RoadNetwork
- .choose
- SoldierFigure
- Retinues
- .generate
- net.minecraft.server.level.ServerPlayer
- GrandStrategyScreen
- net.minecraft.world.level.Level
- DistantHorizonsLink
- SableLink
- Quests
- PoliticalStyle
- FormationSnapshot
- .findGenerationPoint
- CityTerrainPiece
- Terrain
- TitleOwnersScreen
- ProvinceMap
- .shift
- ClothRenderer
- Cloth
- .accept
- ClothItemRenderer
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- net.minecraft.resources.ResourceLocation
- Ride
- CityManagerScreen
- FieldRules
- .tick
- CLAUDE.md
- Override
- ThrownFigures
- BackBannerLayer.java
- NobleDistrictTest
- net.minecraft.sounds.SoundEvent
- ChunkMapCameraMixin.java
- UnitOrder
- Kit
- StrategyRelief
- /pve debug commands
- Settlement
- Site
- Organizations
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- Call
- ConquestConfig
- .noble
- net.minecraft.world.level.block.Rotation
- ArmySpawnScreen
- Job
- RafasConquest.java
- SiegeWorks
- RecruitFigures.java
- .findGenerationPoint
- TechTreeScreen
- Duty
- org.spongepowered.asm.mixin.injection.Inject
- ClientTownsfolk
- Piece
- DistantArmyRenderer
- FieldArmy
- .issue
- ConquestGameRules
- .onLevelLoad
- TitleStyle
- ClientModEvents.java
- .boundsAt
- HarbourCut
- 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling
- SableSeen
- Profiling run sheet (baseline)
- .measure
- GateScreen
- .offTheBack
- SeaCalm
- .roadStart
- .second
- Ranks (Soldier to Grand Marshal)
- DayClockTest
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- BattleKeepInventory
- Cited Findings
- Where are full scans, nested loops, repeated lookups and per-tick allocations?
- BackBanners
- Kind
- FaceOffScreen
- CourtView
- GatePlanksBlock.java
- .moraleOf
- PoliticsData
- CameraView
- Doctrine
- ItemFlags.java
- net.minecraft.world.item.ItemStack
- FactionMapScreen
- 6. Sodium (and Iris) compatibility
- 7. Client tick, stutter and sound
- OrganizationScreen
- GateBlockEntity
- Machine
- Saved data: how big, how often written, is everything re-serialized?
- list
- BattleSnapshot
- Lands
- BlockGrid
- Order
- WallTemplates
- OarFigures
- DistantHorizonsArmies
- .inventoryTick
- RafasConquest (PvE addon for Villager Recruits)
- Kind
- WarHorn
- FieldBlock
- DistantGeneration
- org.joml.Matrix4f
- BoardScreen
- Trait
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- Build
- ClientBarks
- DistantOcclusion
- .touches
- HornCall
- SwingModes
- Task
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Cities (grid, plots, vanilla village buildings)
- ShipKind

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 310 edges
2. `FieldBlock` - 222 edges
3. `FieldBattle` - 210 edges
4. `ConquestNetwork` - 129 edges
5. `FieldArmy` - 128 edges
6. `Point` - 116 edges
7. `Lands` - 89 edges
8. `Siege` - 86 edges
9. `BattleScreen` - 86 edges
10. `Politics` - 79 edges

## Surprising Connections (you probably didn't know these)
- `Cited Findings` --references--> `MapView`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/MapView.java
- `Every frame pays for disk listings, per-rectangle flushes and unculled soldiers` --references--> `TitleLayer`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/client/map/TitleLayer.java
- `Cited Findings` --references--> `FormationSnapshot`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/battle/FormationSnapshot.java
- `Cited Findings` --references--> `Sprite`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/battle/FormationSnapshot.java
- `A tick budget and staggered schedules make the fixes stick` --references--> `BaseMap`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/map/BaseMap.java

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

## Communities (420 total, 263 thin omitted)

### Community 0 - "Calibration"
Cohesion: 0.12
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 1 - "World map (CK3-style political map)"
Cohesion: 0.33
Nodes (7): Base map generated from seed, Command Table block, Faction Info screen, World map layers, Province size setting, Provinces (ProvinceMap), World map (CK3-style political map)

### Community 2 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.06
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - "org.junit.jupiter.api.Test"
Cohesion: 0.07
Nodes (6): DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, SwingModesTest, GrandStrategyDateTest, OarRowingTest

### Community 4 - "Feud"
Cohesion: 0.09
Nodes (8): Clash, Feud, Kind, DUEL, SKIRMISH, Lord, Feuds, Front

### Community 5 - "BattleScreen"
Cohesion: 0.07
Nodes (4): BattleScreen, Entry, Menu, HornOrders

### Community 6 - "ConquestSavedData"
Cohesion: 0.06
Nodes (11): Cited Findings, ConquestSavedData, ResourceType, FOOD, Poi, PoiType, CAPITAL, CITY (+3 more)

### Community 8 - "ArtilleryMount.java"
Cohesion: 0.07
Nodes (4): ConquestContent, Oar, Sail, MarkerBlockEntity

### Community 9 - "RecruitsBattle"
Cohesion: 0.04
Nodes (19): Cited Findings, Card, Enlisted, Figure, Officers, RecruitsBattle, Role, BEARER (+11 more)

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 13 - "BaseMap"
Cohesion: 0.09
Nodes (11): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, ClientMapCache, MapPreview, BaseMap (+3 more)

### Community 14 - "RecruitsBattle.java"
Cohesion: 0.09
Nodes (30): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+22 more)

### Community 15 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (13): Saved, CourtSet, Seat, Style, BattleBannerBlock, BattleWallBannerBlock, FlagBlock, Mount (+5 more)

### Community 17 - "RafasConquest Project Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.12
Nodes (10): Culture, Faith, Marriages, Setter, Wedding, Hold, Person, Bond (+2 more)

### Community 19 - "CityLayout"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 20 - ".postChunk"
Cohesion: 0.08
Nodes (8): Cited Findings, RecruitsBridge, BarracksPeople, CityBanners, CityGuards, ConquestServer, StructureLink, PoiPrecomputer

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 22 - ".encode"
Cohesion: 0.10
Nodes (8): Hook, Kind, FRIEND, RIVAL, Realm, Succession, Taking, SuccessionFormatTest

### Community 23 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.04
Nodes (7): FactionBannerNbt, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 24 - "CityHud"
Cohesion: 0.09
Nodes (3): CityHud, Spot, CityHudPreviewTest

### Community 25 - "Tier"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 30 - ".map"
Cohesion: 0.06
Nodes (13): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Army, Goal, Muster (+5 more)

### Community 31 - ".render"
Cohesion: 0.13
Nodes (6): LegendEntry, MapLayer, Symbol, TerritoryLayer, TitleLayer, UnitLayer

### Community 32 - "BattleShips"
Cohesion: 0.16
Nodes (6): Hull, Ramming, BattleShips, Helm, Ram, RammingTest

### Community 34 - ".map"
Cohesion: 0.16
Nodes (3): FeudDuel, MusterBars, Wars

### Community 38 - "WaterSwell"
Cohesion: 0.10
Nodes (8): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Made, WaterSwell, Swell

### Community 39 - ".send"
Cohesion: 0.09
Nodes (11): BackBanner, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel, Type (+3 more)

### Community 40 - "rafasconquest.faction.Faction"
Cohesion: 0.10
Nodes (4): RecruitsFactions, FactionBanner, Layer, FactionBannerTest

### Community 42 - "Arm"
Cohesion: 0.07
Nodes (23): Arm, ARCBALLISTAS, ARCHERS, CAVALRY, CULVERINS, ENGINES, HALBERDIERS, HEAVY_CAVALRY (+15 more)

### Community 43 - "Point"
Cohesion: 0.12
Nodes (3): Point, Ground, GroundPlan

### Community 44 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.04
Nodes (7): Character page, Court 3D scene (CourtView), Demographics (population by culture and race), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), InventoryStatsButton, Portraits

### Community 45 - ".view"
Cohesion: 0.06
Nodes (20): Advancement, Names, NobleTitle, COUNT, DUKE, EMPEROR, KING, KNIGHT (+12 more)

### Community 46 - "Personality"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (13): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+5 more)

### Community 51 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.07
Nodes (4): CommandTableBlock, GateGeometry, HelmBlock, GateGeometryTest

### Community 53 - "Cited Findings"
Cohesion: 0.09
Nodes (9): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Cited Findings, Gaps, Inferences, Takeaway, BattleSpotRing, FarFlags (+1 more)

### Community 55 - ".render"
Cohesion: 0.16
Nodes (4): CharacterScreen, Spot, Honour, Honours

### Community 59 - "CityTerrain"
Cohesion: 0.07
Nodes (17): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+9 more)

### Community 60 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.10
Nodes (8): Cited Findings, BattleSounds, ChargeSound, DinSound, Queued, Shot, Source, DistantArmies

### Community 61 - "HorizontalStretch"
Cohesion: 0.08
Nodes (10): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, Choice (+2 more)

### Community 63 - "TitleMap"
Cohesion: 0.15
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.12
Nodes (4): Loaded, Offsets, Placement, StyleLibrary

### Community 66 - "Kind"
Cohesion: 0.13
Nodes (13): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HALBERDIERS, HEAVY_CAVALRY (+5 more)

### Community 67 - "WorldSettings"
Cohesion: 0.19
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 70 - "Row"
Cohesion: 0.12
Nodes (3): Fort, Stronghold, Row

### Community 71 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 72 - "DistantShips"
Cohesion: 0.13
Nodes (6): DistantShips, Far, HullKey, Riding, Want, DistantShipsTest

### Community 73 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.21
Nodes (4): ArrowSaveMixin, DhTerrainShaderMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 75 - "WarCouncil"
Cohesion: 0.12
Nodes (7): Facts, Officer, Said, Talk, WarCouncil, Ranks, WarCouncilPagesTest

### Community 76 - "MarkerBlock.java"
Cohesion: 0.09
Nodes (4): GateBlock, MarkerBlock, GoldChurch, Layout

### Community 77 - "Roads"
Cohesion: 0.07
Nodes (19): Grade, CITY, DOJO, HIGH, SEA, TOWN, Heap, Junction (+11 more)

### Community 78 - "RecruitFigures"
Cohesion: 0.07
Nodes (9): FigureGear, Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures (+1 more)

### Community 79 - ".plan"
Cohesion: 0.08
Nodes (10): Countryside, Plan, City, Grid, SeatCities, CityIndex, CountrysideTest, World (+2 more)

### Community 81 - "net.minecraft.core.Direction"
Cohesion: 0.12
Nodes (7): ChunkWait, Wait, Land, Result, SiegeSurvey, Wall, SiegeSurveyTest

### Community 82 - "Candidate"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "Faction"
Cohesion: 0.10
Nodes (9): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), Faction, FactionType, BANDIT, CITY_STATE, EMPIRE, FactionInfo (+1 more)

### Community 84 - "SailBlock.java"
Cohesion: 0.14
Nodes (3): ArtilleryBlock, OarBlock, SailBlock

### Community 85 - "BattleBannerBlockEntity"
Cohesion: 0.12
Nodes (4): BattleBannerBlockEntity, Banner, PlacedBanners, SaveGuard

### Community 87 - ".fire"
Cohesion: 0.09
Nodes (3): ArtilleryMount, CharacterMarkers, Placed

### Community 92 - "CityVariant"
Cohesion: 0.15
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 94 - ".hash"
Cohesion: 0.10
Nodes (6): ChunkCoord, FarmLayout, NobleAnnex, OriginRoller, Roll, NobleAnnexTest

### Community 95 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.13
Nodes (16): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Characters (holders, skills, prowess, traits), Cultures (seven homelands), Envelopment, Epic Knights optional armour (Armoury), Faiths and cults (+8 more)

### Community 96 - "MapLayer"
Cohesion: 0.14
Nodes (13): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+5 more)

### Community 98 - "CityGridBuilder"
Cohesion: 0.10
Nodes (5): Houses, CityGridBuilder, Ground, PlacedWall, Platform

### Community 100 - "NationScreen"
Cohesion: 0.06
Nodes (13): ClientPolitics, NationScreen, Banner, Demographics, Share, Race, BOGFOLK, FROSTBORN (+5 more)

### Community 102 - ".aWoodNearAWingHidesAnAmbushThatStaysPutUntilItStrikes"
Cohesion: 0.20
Nodes (5): GroundPlanTest, Land, Land, Land, Land

### Community 103 - "FigureBatches"
Cohesion: 0.06
Nodes (12): Wind, Batch, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED (+4 more)

### Community 105 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.05
Nodes (18): Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate, GateAction (+10 more)

### Community 106 - "WarCamp"
Cohesion: 0.12
Nodes (3): Camera, PlannedTent, WarCamp

### Community 107 - "How Total War calculates battles between units"
Cohesion: 0.17
Nodes (11): 1. Combat is per soldier, not per unit, 2. Does the swing land?, 3. How much damage the hit does, 4. Charges, 5. Missiles, 6. Leadership (morale): what actually wins battles, 7. Autoresolve, 8. Older games (Shogun 1, Medieval 1, Rome 1) (+3 more)

### Community 108 - "MapContext"
Cohesion: 0.08
Nodes (11): Cited Findings, BaseMapLayer, Hit, LabelLayer, MapContext, MapView, Place, PlaceMenuLayer (+3 more)

### Community 109 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.06
Nodes (11): Boards, Entry, View, CityManager, Garrison, Army, GrandStrategy, View (+3 more)

### Community 110 - "CityPanel"
Cohesion: 0.16
Nodes (6): CityPanel, Mode, DISMISS, MARCH, MUSTER, NONE

### Community 111 - ".step"
Cohesion: 0.14
Nodes (5): FigureMotion, Match, Placed, State, FigureMotionTest

### Community 112 - "ShipHulls"
Cohesion: 0.24
Nodes (7): 10. Overall ranking, and areas checked that look fine, Gaps, Inferences, Takeaway, BattleKind, Part, ShipHulls

### Community 113 - "RoadNetwork"
Cohesion: 0.09
Nodes (6): Cited Findings, RoadFeature, Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 114 - ".choose"
Cohesion: 0.12
Nodes (11): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+3 more)

### Community 115 - "SoldierFigure"
Cohesion: 0.07
Nodes (32): Weapon, BOW, GREAT, LONG, NONE, SWORD, Body, BALLISTA (+24 more)

### Community 117 - ".generate"
Cohesion: 0.07
Nodes (14): ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors, Homeland, DESERT (+6 more)

### Community 118 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.03
Nodes (39): ArtillerySet, Bark, BaseMapPart, BattleSpot, BoardAction, BoardView, CameraAt, ChargeHits (+31 more)

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.09
Nodes (4): GrandStrategyScreen, Marcher, Spot, GarrisonOrder

### Community 121 - "net.minecraft.world.level.Level"
Cohesion: 0.09
Nodes (5): Sea, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, WorldMapItem

### Community 124 - "Quests"
Cohesion: 0.13
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 126 - "FormationSnapshot"
Cohesion: 0.14
Nodes (7): Cited Findings, Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 130 - "TitleOwnersScreen"
Cohesion: 0.19
Nodes (3): ClientPeerage, Owner, TitleOwnersScreen

### Community 131 - "ProvinceMap"
Cohesion: 0.13
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 132 - ".shift"
Cohesion: 0.16
Nodes (6): BattleSite, Part, Water, BattleSite.Water, BattleSite.Water, BattleSiteTest

### Community 133 - "ClothRenderer"
Cohesion: 0.06
Nodes (12): BannerArt, BattleBannerRenderer, CapeCurio, Carried, ClothRenderer, Fabric, Far, Kind (+4 more)

### Community 134 - "Cloth"
Cohesion: 0.12
Nodes (5): Cloth, Solid, ClothTest, StretchTest, WoolTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.07
Nodes (28): 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings (+20 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.07
Nodes (26): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 8. Lessons from performance mods that a content mod can apply, Cited Findings, Cited Findings, Cited Findings (+18 more)

### Community 146 - "FieldRules"
Cohesion: 0.27
Nodes (3): FieldRules, ShareLiveTest, W

### Community 152 - "Override"
Cohesion: 0.08
Nodes (6): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Drop, MarkerScreen, SettingsScreen

### Community 158 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 163 - "UnitOrder"
Cohesion: 0.11
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 164 - "Kit"
Cohesion: 0.10
Nodes (20): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+12 more)

### Community 174 - "Settlement"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 175 - "Site"
Cohesion: 0.08
Nodes (6): CityLayout, District, Site, SlopeTile, WorldSettings, CityLayoutTest

### Community 178 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 181 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.17
Nodes (8): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Enlisted players, The general and guard, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Battle roles (frontline, bait, support, flanker, kill squad), Victory, pursuit and surrender, The war horn

### Community 186 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 206 - ".noble"
Cohesion: 0.25
Nodes (6): BlockPacker, Placed, Rect, Size, BlockPackerTest, PlacementOffsetsTest

### Community 219 - "Job"
Cohesion: 0.06
Nodes (21): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+13 more)

### Community 221 - "RafasConquest.java"
Cohesion: 0.05
Nodes (3): GrappleHooks, ConquestEntities, RafasConquest

### Community 223 - "SiegeWorks"
Cohesion: 0.09
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 224 - "RecruitFigures.java"
Cohesion: 0.06
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 228 - "TechTreeScreen"
Cohesion: 0.18
Nodes (4): TechTreeScreen, Tech, TechTree, TechTreeTest

### Community 232 - "Duty"
Cohesion: 0.13
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 234 - "org.spongepowered.asm.mixin.injection.Inject"
Cohesion: 0.23
Nodes (3): CityVegetationMixin, ClientChunkWindowMixin, DhShaderLoaderMixin

### Community 235 - "ClientTownsfolk"
Cohesion: 0.16
Nodes (5): ClientTownsfolk, Folk, StreetWalks, Walk, ClientTownsfolkTest

### Community 236 - "Piece"
Cohesion: 0.13
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.08
Nodes (5): DistantDepth, DistantArmyRenderer, Figure, Rects, ShipSprite

### Community 238 - "FieldArmy"
Cohesion: 0.08
Nodes (3): FieldArmy, Event, Order

### Community 254 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 255 - "HarbourCut"
Cohesion: 0.12
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 256 - "2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling"
Cohesion: 0.40
Nodes (5): 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, Cited Findings, Gaps, Inferences, Takeaway

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 267 - "SeaCalm"
Cohesion: 0.19
Nodes (5): Ground, Depths, SeaCalm, Strengths, SeaCalmTest

### Community 271 - ".roadStart"
Cohesion: 0.08
Nodes (5): DistrictKey, Townsfolk, CitySizesTest, StreetWalksTest, PlaceMusicTest

### Community 272 - ".second"
Cohesion: 0.05
Nodes (10): GeneralTask, Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK (+2 more)

### Community 274 - "Ranks (Soldier to Grand Marshal)"
Cohesion: 0.33
Nodes (6): Gold church, Land grants (seven ways), Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter

### Community 278 - "DayClockTest"
Cohesion: 0.25
Nodes (3): DayClock, Step, DayClockTest

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.20
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 287 - "Where are full scans, nested loops, repeated lookups and per-tick allocations?"
Cohesion: 0.33
Nodes (4): Gaps, Inferences, Takeaway, Where are full scans, nested loops, repeated lookups and per-tick allocations?

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 292 - "CourtView"
Cohesion: 0.11
Nodes (3): Retainer, CourtView, Figure

### Community 295 - ".moraleOf"
Cohesion: 0.10
Nodes (13): 1. Flank and rear shock, 2. Nearby rout (fright), 3. Army-wide losses, 4. Shattering, Design: adding the four morale shocks (built 2026-11-11, untested in game), Tests (FieldBattleTest), The core change: morale becomes a sum, Morale (+5 more)

### Community 298 - "CameraView"
Cohesion: 0.19
Nodes (4): Cited Findings, CameraView, RecruitsArrowCleanerMixin, TrackedEntityCameraMixin

### Community 300 - "Doctrine"
Cohesion: 0.05
Nodes (30): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+22 more)

### Community 302 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.27
Nodes (3): Armoury, Outfit, Style

### Community 305 - "FactionMapScreen"
Cohesion: 0.09
Nodes (3): FactionMapScreen, PetitionScreen, AbstractSliderButton

### Community 306 - "6. Sodium (and Iris) compatibility"
Cohesion: 0.40
Nodes (5): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway

### Community 308 - "7. Client tick, stutter and sound"
Cohesion: 0.40
Nodes (5): 7. Client tick, stutter and sound, Cited Findings, Gaps, Inferences, Takeaway

### Community 311 - "GateBlockEntity"
Cohesion: 0.08
Nodes (9): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+1 more)

### Community 313 - "Machine"
Cohesion: 0.07
Nodes (13): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+5 more)

### Community 318 - "Saved data: how big, how often written, is everything re-serialized?"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Saved data: how big, how often written, is everything re-serialized?, Takeaway

### Community 321 - "list"
Cohesion: 0.04
Nodes (11): Countryside (hamlets, villages, watchtowers, castles), Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, Nations by zone, Roads (high, city, town roads; bridges; signposts), Seat cities, Storm wall and war zone, Kind (+3 more)

### Community 322 - "BattleSnapshot"
Cohesion: 0.14
Nodes (8): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, BattleSnapshot, PlaceMusic, BattleSnapshotGearTest

### Community 323 - "Lands"
Cohesion: 0.08
Nodes (9): Cited Findings, Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Lands, Claim (+1 more)

### Community 327 - "Order"
Cohesion: 0.40
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

### Community 330 - "OarFigures"
Cohesion: 0.17
Nodes (3): Model, OarFigures, Rowing

### Community 347 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 350 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 355 - "FieldBlock"
Cohesion: 0.05
Nodes (5): FieldBattle, Outcome, Target, Volley, FieldBlock

### Community 362 - "DistantGeneration"
Cohesion: 0.20
Nodes (6): Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Takeaway, Api, DistantGeneration

### Community 365 - "org.joml.Matrix4f"
Cohesion: 0.11
Nodes (13): Arrow, ArrowVolleys, Kind, ARROW, BALL, BIG_BALL, BIG_ROCK, BOLT (+5 more)

### Community 379 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 393 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 407 - "DistantOcclusion"
Cohesion: 0.12
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 411 - "HornCall"
Cohesion: 0.40
Nodes (5): HornCall, ATTACKING, MOVING, RALLYING, RETREATING

### Community 414 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 415 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.10
Nodes (19): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps, Inferences (+11 more)

### Community 419 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.18
Nodes (5): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), TickWeave experiment

### Community 433 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **445 isolated node(s):** `ARCHER_MELEE`, `ARROWS`, `CAVALRY`, `INFANTRY`, `BANDIT` (+440 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1455 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **263 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `Calibration`, `net.minecraft.nbt.CompoundTag`, `org.junit.jupiter.api.Test`, `.shift`, `.passOnOrders`, `PoliticsTest`, `net.minecraft.resources.ResourceLocation`, `RecruitsBattle.java`, `.second`, `UnitMode`, `HornCall`, `.enlist`, `net.neoforged.bus.api.SubscribeEvent`, `.map`, `PveCommands`, `UnitOrder`, `org.jetbrains.annotations.Nullable`, `rafasconquest.faction.Faction`, `Doctrine`, `.view`, `net.minecraft.world.item.ItemStack`, `.siege`, `BattleSnapshot`, `BattleMap.java`, `WarCouncil`, `net.minecraft.core.Direction`, `SiegeWorks`, `.thrown`, `FieldBlock`, `rafasconquest.battle.BattlePlan.Point`, `WarCamp`, `net.minecraft.server.level.ServerPlayer`, `Trait`, `SableLink`?**
  _High betweenness centrality (0.049) - this node is a cross-community bridge._
- **What connects `ARCHER_MELEE`, `ARROWS`, `CAVALRY` to the rest of the system?**
  _445 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Calibration` be split into smaller, more focused modules?**
  _Cohesion score 0.12169312169312169 - nodes in this community are weakly interconnected._
- **Why does `FieldBattle` connect `FieldBlock` to `Bodies`, `list`, `org.junit.jupiter.api.Test`, `.deploy`, `.aWoodNearAWingHidesAnAmbushThatStaysPutUntilItStrikes`, `.moraleOf`, `.passOnOrders`, `RecruitsBattle`, `Machine`, `Point`, `rafasconquest.battle.BattlePlan.Point`, `FieldArmy`, `.second`, `.siege`, `Call`, `.map`?**
  _High betweenness centrality (0.029) - this node is a cross-community bridge._
- **Should `net.minecraft.nbt.CompoundTag` be split into smaller, more focused modules?**
  _Cohesion score 0.060306824193263976 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._