# Graph Report - RafasConquest  (2026-11-15)

## Corpus Check
- 575 files · ~502,231 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7902 nodes · 25524 edges · 395 communities (155 shown, 240 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3278 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `f1dd6737`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- .measure
- .siege
- net.minecraft.nbt.CompoundTag
- org.junit.jupiter.api.Test
- Lands
- BattleScreen
- DistantHorizonsArmies
- GatePlanksBlock.java
- net.minecraft.world.level.block.entity.BlockEntity
- net.minecraft.network.RegistryFriendlyByteBuf
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- .save
- BaseMap
- .map
- ConquestWorldgen
- PoliticsTest
- RafasConquest Project Site
- Person
- CityLayout
- .generate
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- .world
- PveCommands.java
- CityHud
- Tier
- .step
- net.neoforged.bus.api.SubscribeEvent
- WarCouncil
- .enlist
- Land
- BattleShips
- PveCommands
- ClothRenderer
- CityPlan
- BattleBannerRenderer.java
- Machine
- WaterSwell
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- rafasconquest.faction.Faction
- MarkerScreen
- Arm
- Realm
- net.minecraft.client.gui.GuiGraphics
- Phase
- Personality
- SeaLanes
- BattlePlan
- .bark
- com.mojang.blaze3d.vertex.PoseStack
- .postChunk
- Barracks
- .render
- CityStyles
- net.minecraft.world.item.ItemStack
- WorldSettings
- CityTerrain
- rafasconquest.battle.FormationSnapshot
- HorizontalStretch
- TitleMap
- Bodies
- StyleLibrary
- RecruitsBattle
- WorldSettings
- BattleMap.java
- Row
- School
- DistantShips
- org.spongepowered.asm.mixin.Mixin
- MapLands
- War
- .general
- CapeCurio.java
- RecruitFigures
- .plan
- net.minecraft.core.Direction
- Candidate
- Homeland
- Sea.java
- org.jetbrains.annotations.Nullable
- UnitOrder
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- ClientScreens
- DialogueScreen
- GrappleHookRenderer.java
- ConquestSavedData
- net.minecraft.world.level.block.state.BlockState
- .order
- RoadLayer
- CityGridBuilder
- FactionInfoScreen
- CourtView
- BlockGrid
- Cloth
- FigureBatches
- ChunkMapCameraMixin
- net.minecraft.core.BlockPos
- How Total War calculates battles between units
- MapView
- Garrison
- CityPanel
- Task
- .plan
- RoadNetwork
- .choose
- Body
- TechTreeScreen
- Duty
- ConquestNetwork
- GrandStrategyScreen
- Roads
- Settlement
- Faction
- .board
- Kit
- .begin
- .findGenerationPoint
- CityTerrainPiece
- Terrain
- TitleOwnersScreen
- ProvinceMap
- .shift
- .render
- SeaCalm
- Cited Findings
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .inventoryTick
- net.minecraft.server.level.ServerPlayer
- Override
- Grade
- SiegeWorks
- CLAUDE.md
- .render
- Type
- Succession
- net.minecraft.sounds.SoundEvent
- .settings
- Cities (grid, plots, vanilla village buildings)
- Grid
- net.minecraft.world.item.Item
- LabelLayer
- /pve debug commands
- .findGenerationPoint
- NationsTest
- ClientPolitics
- map
- Call
- Peerage
- .noble
- net.minecraft.core.Vec3i
- ArmySpawnScreen
- rafasconquest.character.Rank
- .item
- .onLogout
- RecruitFigures.java
- 4. Depth buffers and post effects (DH depth, occlusion, GPU sync)
- How do world generation and structure placement work, and what do they cost?
- .build
- Siege
- SableSeen
- HornCall
- Piece
- net.minecraft.client.Minecraft
- FieldArmy
- .place
- ConquestGameRules
- WallTemplates
- NetworkDirection
- ClientModEvents.java
- .hash
- .build
- Characters (holders, skills, prowess, traits)
- WarHorn
- Profiling run sheet (baseline)
- .measure
- Recruiter
- BattleType
- .at
- DayClockTest
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- Build
- .touches
- Body
- Kind
- FaceOffScreen
- Kind
- BattleSnapshot
- FlagBlock.java
- .onLevelLoad
- Doctrine
- PoliticalStyle
- Distribution
- Outcome
- net.minecraft.world.level.Level
- CityVegetationMixin.java
- OrganizationScreen
- GateBlockEntity
- Plan
- list
- Lands
- GrappleHookRenderer.java
- ShareLiveTest
- SwingModes
- OarFigures
- Armoury
- .offTheBack
- DistantGeneration
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- org.joml.Matrix4f
- GrandStrategyDateTest.java
- BattleKeepInventory.java
- BoardScreen
- Layout
- ItemFlags.java
- ClientBarks
- UnitMode
- NobleDistrictTest
- .roadStart
- DistantOcclusion
- Morale
- ShipKind
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 312 edges
2. `FieldBlock` - 222 edges
3. `FieldBattle` - 221 edges
4. `Point` - 164 edges
5. `FieldArmy` - 128 edges
6. `ConquestNetwork` - 126 edges
7. `Lands` - 89 edges
8. `Siege` - 86 edges
9. `BattleScreen` - 86 edges
10. `Politics` - 79 edges

## Surprising Connections (you probably didn't know these)
- `Cited Findings` --references--> `MapView`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/MapView.java
- `A tick budget and staggered schedules make the fixes stick` --references--> `BaseMap`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/map/BaseMap.java
- `Cited Findings` --references--> `ChunkMapCameraMixin`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/mixin/ChunkMapCameraMixin.java

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

## Communities (395 total, 240 thin omitted)

### Community 0 - ".measure"
Cohesion: 0.19
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 2 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.06
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - "org.junit.jupiter.api.Test"
Cohesion: 0.06
Nodes (5): DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, OarRowingTest, ArmouryTest

### Community 4 - "Lands"
Cohesion: 0.10
Nodes (8): Clash, Feud, Kind, DUEL, SKIRMISH, Lord, Feuds, Front

### Community 5 - "BattleScreen"
Cohesion: 0.07
Nodes (4): BattleScreen, Entry, Menu, HornOrders

### Community 6 - "DistantHorizonsArmies"
Cohesion: 0.06
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 8 - "net.minecraft.world.level.block.entity.BlockEntity"
Cohesion: 0.08
Nodes (4): ArtilleryMount, ConquestContent, Oar, Sail

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 13 - "BaseMap"
Cohesion: 0.15
Nodes (9): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, BaseMap, Nation, BaseMapStore (+1 more)

### Community 14 - ".map"
Cohesion: 0.13
Nodes (4): Cited Findings, FeudDuel, PoliticsSync, Wars

### Community 15 - "ConquestWorldgen"
Cohesion: 0.08
Nodes (5): CityPlacement, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement

### Community 17 - "RafasConquest Project Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.14
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 19 - "CityLayout"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 20 - ".generate"
Cohesion: 0.16
Nodes (3): ProvinceNames, BaseMapGenerator, Heights

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 22 - ".world"
Cohesion: 0.14
Nodes (3): Taking, RealmPlannerTest, World

### Community 23 - "PveCommands.java"
Cohesion: 0.04
Nodes (17): /pve debug citystats [samples], /pve debug cityterrain, /pve politics, /pve politics war <attacker> <defender> [generals], SeedHash, FactionBannerNbt, FactionType, BANDIT (+9 more)

### Community 24 - "CityHud"
Cohesion: 0.09
Nodes (3): CityHud, Spot, CityHudPreviewTest

### Community 25 - "Tier"
Cohesion: 0.21
Nodes (9): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+1 more)

### Community 26 - ".step"
Cohesion: 0.12
Nodes (6): FigureMotion, Ground, Match, Placed, State, FigureMotionTest

### Community 27 - "net.neoforged.bus.api.SubscribeEvent"
Cohesion: 0.04
Nodes (3): Post, ClientEvents, ConquestEvents

### Community 28 - "WarCouncil"
Cohesion: 0.16
Nodes (6): Facts, Officer, Said, Talk, WarCouncil, WarCouncilPagesTest

### Community 30 - ".enlist"
Cohesion: 0.05
Nodes (6): BattleMap, GroundPlanTest, Land, Land, Land, Land

### Community 31 - "Land"
Cohesion: 0.17
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 32 - "BattleShips"
Cohesion: 0.14
Nodes (6): Hull, Ramming, BattleShips, Helm, Ram, RammingTest

### Community 34 - "ClothRenderer"
Cohesion: 0.20
Nodes (6): ClothRenderer, Fabric, Far, Kind, Layer, Live

### Community 36 - "BattleBannerRenderer.java"
Cohesion: 0.11
Nodes (4): BattleBannerRenderer, GateRenderer, MarkerRenderer, MarkerBlockEntity

### Community 37 - "Machine"
Cohesion: 0.07
Nodes (13): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+5 more)

### Community 38 - "WaterSwell"
Cohesion: 0.10
Nodes (8): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Made, WaterSwell, Swell

### Community 40 - "rafasconquest.faction.Faction"
Cohesion: 0.11
Nodes (8): Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER, Side, RecruitsFactions

### Community 42 - "Arm"
Cohesion: 0.07
Nodes (24): Arm, ARCBALLISTAS, ARCHERS, CAVALRY, CULVERINS, ENGINES, HALBERDIERS, HEAVY_CAVALRY (+16 more)

### Community 43 - "Realm"
Cohesion: 0.22
Nodes (4): Kind, FRIEND, RIVAL, Realm

### Community 44 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.05
Nodes (5): Character page, Court 3D scene (CourtView), Nation page, GateScreen, InventoryStatsButton

### Community 45 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 46 - "Personality"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.14
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "BattlePlan"
Cohesion: 0.07
Nodes (3): BattlePlan, Slot, BattlePlanTest

### Community 53 - ".postChunk"
Cohesion: 0.08
Nodes (7): Cited Findings, RecruitsBridge, BarracksPeople, CityBanners, CityGuards, ConquestServer, PoiPrecomputer

### Community 55 - ".render"
Cohesion: 0.11
Nodes (6): CharacterScreen, Retainer, Spot, Names, Honour, Honours

### Community 57 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.27
Nodes (3): Armoury, Outfit, Style

### Community 58 - "WorldSettings"
Cohesion: 0.09
Nodes (7): CityLayout, District, Site, ChunkCoord, FarmLayout, WorldSettings, CityLayoutTest

### Community 59 - "CityTerrain"
Cohesion: 0.07
Nodes (18): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+10 more)

### Community 60 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.11
Nodes (6): Cited Findings, BattleSounds, ChargeSound, DinSound, Source, DistantArmies

### Community 61 - "HorizontalStretch"
Cohesion: 0.06
Nodes (13): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Age, BRONZE, GUNPOWDER, STEAM, STEEL (+5 more)

### Community 63 - "TitleMap"
Cohesion: 0.16
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.11
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - "RecruitsBattle"
Cohesion: 0.03
Nodes (14): Card, Enlisted, Figure, GeneralTask, Landing, Live, RecruitsBattle, Shape (+6 more)

### Community 67 - "WorldSettings"
Cohesion: 0.17
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 69 - "BattleMap.java"
Cohesion: 0.03
Nodes (4): Battle map developer web page, Morale, rout and rally, RafasConquest, SwingModesTest

### Community 70 - "Row"
Cohesion: 0.12
Nodes (3): Fort, Stronghold, Row

### Community 71 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 72 - "DistantShips"
Cohesion: 0.11
Nodes (9): DistantShips, Far, HullKey, Riding, Want, BattleKind, Part, ShipHulls (+1 more)

### Community 73 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.17
Nodes (6): ArrowSaveMixin, ClientChunkWindowMixin, DhShaderLoaderMixin, DhTerrainShaderMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 75 - "War"
Cohesion: 0.08
Nodes (6): Army, Goal, Muster, CEREMONIAL, OFFENSIVE, War

### Community 76 - ".general"
Cohesion: 0.09
Nodes (14): GameCharacter, Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN (+6 more)

### Community 77 - "CapeCurio.java"
Cohesion: 0.15
Nodes (4): BackBannerLayer, CapeCurio, Carried, MouthLayer

### Community 78 - "RecruitFigures"
Cohesion: 0.10
Nodes (8): Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures, RecruitFiguresTest

### Community 79 - ".plan"
Cohesion: 0.12
Nodes (3): SeatCities, SeatCitiesTest, World

### Community 81 - "net.minecraft.core.Direction"
Cohesion: 0.09
Nodes (7): Land, Result, SiegeSurvey, Wall, CityGates, Done, SiegeSurveyTest

### Community 82 - "Candidate"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "Homeland"
Cohesion: 0.09
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 85 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.04
Nodes (7): /pve debug recruits, ChunkWait, Wait, Officers, BiomePalette, Entry, MusterBars

### Community 87 - "UnitOrder"
Cohesion: 0.11
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.05
Nodes (27): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+19 more)

### Community 92 - "ConquestSavedData"
Cohesion: 0.07
Nodes (5): Cited Findings, ConquestSavedData, Poi, EconomyTick, ConquestSavedDataTest

### Community 94 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.08
Nodes (5): BattleWallBannerBlock, CommandTableBlock, GateBlock, HelmBlock, Builder

### Community 95 - ".order"
Cohesion: 0.25
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

### Community 96 - "RoadLayer"
Cohesion: 0.14
Nodes (11): A tick budget and staggered schedules make the fixes stick, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom?, Gaps (+3 more)

### Community 98 - "CityGridBuilder"
Cohesion: 0.09
Nodes (9): CityGridBuilder, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY (+1 more)

### Community 100 - "CourtView"
Cohesion: 0.06
Nodes (9): ClientPeerage, Camera, CourtView, Figure, NationScreen, Banner, Portraits, Demographics (+1 more)

### Community 102 - "Cloth"
Cohesion: 0.13
Nodes (4): Cloth, Solid, ClothTest, WoolTest

### Community 103 - "FigureBatches"
Cohesion: 0.07
Nodes (11): Wind, Batch, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE (+3 more)

### Community 105 - "ChunkMapCameraMixin"
Cohesion: 0.27
Nodes (6): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway, ChunkMapCameraMixin

### Community 106 - "net.minecraft.core.BlockPos"
Cohesion: 0.06
Nodes (10): PlannedTent, Saved, WarCamp, CourtSet, Seat, Style, GateGeometry, CharacterMarkers (+2 more)

### Community 107 - "How Total War calculates battles between units"
Cohesion: 0.11
Nodes (17): 1. Combat is per soldier, not per unit, 1. Flank and rear shock, 2. Does the swing land?, 2. Nearby rout (fright), 3. Army-wide losses, 3. How much damage the hit does, 4. Charges, 4. Shattering (+9 more)

### Community 108 - "MapView"
Cohesion: 0.07
Nodes (8): Cited Findings, FactionMapScreen, MapView, PetitionScreen, AbstractSliderButton, Place, PlaceMenuLayer, Row

### Community 109 - "Garrison"
Cohesion: 0.11
Nodes (5): Garrison, Army, GrandStrategy, View, GarrisonTest

### Community 110 - "CityPanel"
Cohesion: 0.16
Nodes (6): CityPanel, Mode, DISMISS, MARCH, MUSTER, NONE

### Community 111 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 112 - ".plan"
Cohesion: 0.12
Nodes (5): TitleStyle, Faith, Bond, RealmPlanner, Seat

### Community 113 - "RoadNetwork"
Cohesion: 0.15
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 114 - ".choose"
Cohesion: 0.11
Nodes (11): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+3 more)

### Community 115 - "Body"
Cohesion: 0.15
Nodes (10): ShipSprite, Corners, Boxes, Palette, Shape, SoldierFigure, Stuff, CLOTH (+2 more)

### Community 116 - "TechTreeScreen"
Cohesion: 0.18
Nodes (4): TechTreeScreen, Tech, TechTree, TechTreeTest

### Community 117 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 118 - "ConquestNetwork"
Cohesion: 0.03
Nodes (40): ArtillerySet, Bark, BaseMapPart, BattleSpot, BoardAction, BoardView, CameraAt, ChargeHits (+32 more)

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.08
Nodes (5): GrandStrategyScreen, Marcher, Spot, City, CityIndex

### Community 121 - "Roads"
Cohesion: 0.20
Nodes (5): Link, Place, Roads, Spur, RoadsTest

### Community 122 - "Settlement"
Cohesion: 0.12
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 123 - "Faction"
Cohesion: 0.09
Nodes (5): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), MapPreview, Faction, FactionInfoTest

### Community 124 - ".board"
Cohesion: 0.13
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 125 - "Kit"
Cohesion: 0.10
Nodes (20): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+12 more)

### Community 131 - "ProvinceMap"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 132 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 133 - ".render"
Cohesion: 0.14
Nodes (5): ClientTownsfolk, Folk, StreetWalks, Walk, ClientTownsfolkTest

### Community 134 - "SeaCalm"
Cohesion: 0.20
Nodes (5): Depths, SeaCalm, Strengths, SeaCalmTest, SwellTest

### Community 137 - "Cited Findings"
Cohesion: 0.12
Nodes (14): Battle code costs 0.2 ms; the multi-second freezes come from blocking, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, Cited Findings, Gaps, Inferences, Takeaway, Cited Findings, Gaps (+6 more)

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (24): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings, Cited Findings (+16 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.06
Nodes (31): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings, Cited Findings (+23 more)

### Community 142 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.09
Nodes (7): Camera, CameraView, CityManager, PlayerStats, View, Retinues, RetinuesTest

### Community 143 - "Override"
Cohesion: 0.06
Nodes (6): Chunks, BattleSite.Water, BattleSite.Water, CityManagerScreen, SettingsScreen, Politics.Front

### Community 146 - "Grade"
Cohesion: 0.15
Nodes (9): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Plan (+1 more)

### Community 149 - "SiegeWorks"
Cohesion: 0.09
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 152 - ".render"
Cohesion: 0.08
Nodes (9): BaseMapLayer, MapContext, LegendEntry, MapLayer, Symbol, PoiIconLayer, TerritoryLayer, TitleLayer (+1 more)

### Community 155 - "Type"
Cohesion: 0.33
Nodes (4): Type, CLEAR, DELIVER, SCOUT

### Community 156 - "Succession"
Cohesion: 0.13
Nodes (6): Hook, Kind, FAVOUR, SECRET, Succession, SuccessionFormatTest

### Community 157 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 158 - ".settings"
Cohesion: 0.10
Nodes (4): DistrictKey, Townsfolk, CitySizesTest, StreetWalksTest

### Community 160 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.08
Nodes (20): Ambushes from woods, Base map generated from seed, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Demographics (population by culture and race), Faction Info screen (+12 more)

### Community 163 - "Grid"
Cohesion: 0.18
Nodes (5): Countryside, Plan, Grid, CountrysideTest, World

### Community 164 - "net.minecraft.world.item.Item"
Cohesion: 0.16
Nodes (7): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 167 - "LabelLayer"
Cohesion: 0.12
Nodes (5): Hit, LabelLayer, StrategyRelief, PoliticalMap, LabelShape

### Community 174 - ".findGenerationPoint"
Cohesion: 0.11
Nodes (3): CityDistrictStructure, Ground, CityStructure

### Community 186 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 205 - "Peerage"
Cohesion: 0.06
Nodes (22): Advancement, Boards, Entry, View, NobleTitle, COUNT, DUKE, EMPEROR (+14 more)

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 219 - "rafasconquest.character.Rank"
Cohesion: 0.05
Nodes (25): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+17 more)

### Community 221 - ".item"
Cohesion: 0.22
Nodes (3): FactionBanner, Layer, FactionBannerTest

### Community 223 - ".onLogout"
Cohesion: 0.09
Nodes (8): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Gaps, Inferences, Takeaway, BackBanners, ClientMapCache, Throw, ThrownFigures

### Community 224 - "RecruitFigures.java"
Cohesion: 0.05
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 225 - "4. Depth buffers and post effects (DH depth, occlusion, GPU sync)"
Cohesion: 0.40
Nodes (5): 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), Cited Findings, Gaps, Inferences, Takeaway

### Community 226 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.40
Nodes (4): Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Takeaway

### Community 232 - "Siege"
Cohesion: 0.05
Nodes (17): Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate, GateAction (+9 more)

### Community 235 - "HornCall"
Cohesion: 0.40
Nodes (5): HornCall, ATTACKING, MOVING, RALLYING, RETREATING

### Community 237 - "net.minecraft.client.Minecraft"
Cohesion: 0.08
Nodes (4): Cited Findings, DistantDepth, DistantArmyRenderer, Rects

### Community 238 - "FieldArmy"
Cohesion: 0.03
Nodes (11): The core change: morale becomes a sum, Point, FieldArmy, Event, FieldBattle, Order, Target, Volley (+3 more)

### Community 249 - "NetworkDirection"
Cohesion: 0.50
Nodes (3): NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 252 - "ClientModEvents.java"
Cohesion: 0.09
Nodes (3): ClientModEvents, net.neoforged.neoforge.client.extensions.common.IClientItemExtensions, Registration

### Community 254 - ".hash"
Cohesion: 0.12
Nodes (6): Bounds, DistanceScaling, NobleAnnex, OriginRoller, Roll, DistanceScalingTest

### Community 256 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.05
Nodes (36): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Characters (holders, skills, prowess, traits), Cultures (seven homelands), Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod) (+28 more)

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 272 - "BattleType"
Cohesion: 0.25
Nodes (5): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE

### Community 278 - "DayClockTest"
Cohesion: 0.25
Nodes (3): DayClock, Step, DayClockTest

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.15
Nodes (6): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips, Ride

### Community 283 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 288 - "Body"
Cohesion: 0.11
Nodes (18): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+10 more)

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 292 - "Kind"
Cohesion: 0.08
Nodes (20): Cited Findings, Engine, FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, ENGINES (+12 more)

### Community 295 - "FlagBlock.java"
Cohesion: 0.07
Nodes (7): BattleBannerBlock, FlagBlock, Mount, BARS, BLOCK, FENCE, WALL

### Community 300 - "Doctrine"
Cohesion: 0.10
Nodes (16): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR, Strategy (+8 more)

### Community 302 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 306 - "net.minecraft.world.level.Level"
Cohesion: 0.05
Nodes (8): ArtilleryBlock, GrandStrategyBlock, OarBlock, QuestBoardBlock, SailBlock, StrategyBoardBlock, WorldMapItem, MarkerBlock

### Community 311 - "GateBlockEntity"
Cohesion: 0.07
Nodes (9): BattleBannerBlockEntity, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+1 more)

### Community 313 - "Plan"
Cohesion: 0.16
Nodes (8): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Plan

### Community 321 - "list"
Cohesion: 0.05
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 323 - "Lands"
Cohesion: 0.07
Nodes (10): Cited Findings, Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Lands, Claim (+2 more)

### Community 330 - "OarFigures"
Cohesion: 0.17
Nodes (3): Model, OarFigures, Rowing

### Community 364 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.07
Nodes (29): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps (+21 more)

### Community 365 - "org.joml.Matrix4f"
Cohesion: 0.11
Nodes (13): Arrow, ArrowVolleys, Kind, ARROW, BALL, BIG_BALL, BIG_ROCK, BOLT (+5 more)

### Community 401 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 407 - "DistantOcclusion"
Cohesion: 0.09
Nodes (12): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, BattleSpotRing, DistantOcclusion, Mode (+4 more)

### Community 414 - "Morale"
Cohesion: 0.18
Nodes (8): Morale, State, RALLIED, ROUTED, SHATTERED, STEADY, UnitInfo, UnitsState

### Community 415 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

## Ambiguous Edges - Review These
- `Roads (high, city, town roads; bridges; signposts)` → `Mustering (offensive and ceremonial)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **445 isolated node(s):** `ARCHER_MELEE`, `ARROWS`, `CAVALRY`, `INFANTRY`, `NO_LIGHTMAP` (+440 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1446 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **240 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `FieldBattle` connect `FieldArmy` to `Bodies`, `list`, `.siege`, `org.junit.jupiter.api.Test`, `RecruitsBattle`, `Machine`, `.begin`, `Siege`, `rafasconquest.faction.Faction`, `BattleType`, `Outcome`, `org.jetbrains.annotations.Nullable`, `.enlist`, `Call`, `Morale`?**
  _High betweenness centrality (0.064) - this node is a cross-community bridge._
- **What connects `ARCHER_MELEE`, `ARROWS`, `CAVALRY` to the rest of the system?**
  _445 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `.siege` be split into smaller, more focused modules?**
  _Cohesion score 0.10359408033826638 - nodes in this community are weakly interconnected._
- **Why does `RecruitsBattle` connect `RecruitsBattle` to `.siege`, `net.minecraft.nbt.CompoundTag`, `org.junit.jupiter.api.Test`, `net.minecraft.server.level.ServerPlayer`, `Override`, `.map`, `UnitMode`, `.roadStart`, `SiegeWorks`, `PveCommands.java`, `net.neoforged.bus.api.SubscribeEvent`, `WarCouncil`, `ConquestEvents.java`, `.enlist`, `PveCommands`, `Machine`, `dev.ryanhcode.sable.sublevel.ServerSubLevel`, `rafasconquest.faction.Faction`, `Arm`, `Phase`, `Outcome`, `net.minecraft.world.item.ItemStack`, `rafasconquest.battle.FormationSnapshot`, `BattleMap.java`, `War`, `.general`, `net.minecraft.core.Direction`, `org.jetbrains.annotations.Nullable`, `UnitOrder`, `GrappleHookRenderer.java`, `net.minecraft.core.BlockPos`, `HornCall`, `FieldArmy`, `.begin`?**
  _High betweenness centrality (0.060) - this node is a cross-community bridge._
- **Should `net.minecraft.nbt.CompoundTag` be split into smaller, more focused modules?**
  _Cohesion score 0.05791505791505792 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Roads (high, city, town roads; bridges; signposts)` and `Mustering (offensive and ceremonial)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._