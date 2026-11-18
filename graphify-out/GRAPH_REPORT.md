# Graph Report - RafasConquest  (2026-11-17)

## Corpus Check
- 621 files · ~518,543 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 8133 nodes · 26301 edges · 429 communities (170 shown, 259 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3350 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `6f2999d6`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Calibration
- net.minecraft.network.FriendlyByteBuf
- net.minecraft.world.level.ChunkPos
- org.junit.jupiter.api.Test
- RafasConquest.java
- BattleScreen
- .sendToServer
- BattleBannerBlockEntity
- net.minecraft.world.level.Level
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- PoliticsTest
- BaseMap
- RecruitsBattle.java
- BattleMap.java
- org.jetbrains.annotations.Nullable
- RafasConquest Project Site
- Person
- CityLayout
- .postChunk
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- .enlist
- .state
- CityHud
- Tier
- PoiPrecomputer.java
- ConquestEvents
- .view
- net.neoforged.bus.api.SubscribeEvent
- WorldSettings
- com.talhanation.recruits.entities.AbstractRecruitEntity
- BattleShips
- PveCommands
- .map
- CityPlan
- City
- RecruitsBattle.java
- WaterSwell
- net.minecraft.server.level.ServerPlayer
- rafasconquest.faction.Faction
- BlockPos
- Arm
- Peerage
- net.minecraft.client.gui.GuiGraphics
- .view
- Personality
- SeaLanes
- BattlePlan
- net.minecraft.world.level.levelgen.structure.BoundingBox
- .plus
- FieldRules
- Homeland
- Barracks
- .render
- CityStyles
- Faction
- com.google.gson.JsonObject
- CityTerrain
- BattleSounds
- HorizontalStretch
- TitleMap
- Bodies
- StyleLibrary
- Kind
- WorldSettings
- .champion
- Row
- School
- DistantShips
- org.spongepowered.asm.mixin.Mixin
- MapLands
- WarCouncil
- FigureGear
- net.minecraft.world.item.ItemStack
- RecruitFigures
- City
- net.minecraft.server.level.ServerLevel
- Candidate
- ConquestSavedData
- Recruiter
- net.minecraft.world.level.block.entity.BlockEntity
- Succession
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- Cited Findings
- DialogueScreen
- CourtView
- .touches
- TechTreeScreen
- Characters (holders, skills, prowess, traits)
- Grid
- CityGridBuilder
- FactionInfoScreen
- NationScreen
- Land
- War
- FigureBatches
- Point
- WarCamp
- How Total War calculates battles between units
- MapContext
- Garrison
- CityPanel
- .step
- .send
- .place
- .inCity
- Body
- .choose
- .generate
- ConquestNetwork
- GrandStrategyScreen
- net.minecraft.world.entity.LivingEntity
- DistantHorizonsArmies
- .item
- Quests
- NobleTitle
- FormationSnapshot
- net.minecraft.core.BlockPos
- CityTerrainPiece
- Terrain
- TitleOwnersScreen
- ProvinceMap
- .shift
- com.mojang.blaze3d.vertex.PoseStack
- Cloth
- .world
- BattleType
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- net.minecraft.nbt.CompoundTag
- Ride
- CityManagerScreen
- .rightClick
- .place
- CLAUDE.md
- Override
- HarbourCut
- RoadLayer
- GrappleHookRenderer.java
- net.minecraft.sounds.SoundEvent
- Rank
- ClientScreens
- UnitOrder
- Realm
- LabelLayer
- /pve debug commands
- Settlement
- ChunkMapCameraMixin.java
- ClientPolitics
- .accept
- Call
- .render
- .noble
- Size
- ArmySpawnScreen
- How do world generation and structure placement work, and what do they cost?
- FarFlags.java
- SiegeWorks
- RecruitFigures.java
- Hull
- ConquestWorldScreen
- net.minecraft.world.level.block.Rotation
- com.talhanation.recruits.entities.AbstractRecruitEntity
- GoldChurch.java
- ClientModEvents
- Piece
- DistantArmyRenderer
- City
- ShipHulls
- SableSeen.java
- ConquestGameRules
- .onLevelLoad
- .plan
- Organizations
- NobleAnnex
- .findGenerationPoint
- HorizontalStretch
- .build
- Profiling run sheet (baseline)
- .measure
- Duty
- SeaCalm
- BoardScreen
- RecruitsBattle
- Weapon
- DayClockTest
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- BattleKeepInventory
- BattleSnapshot
- MarkerScreen
- .onLogout
- Kind
- FaceOffScreen
- .findGenerationPoint
- net.minecraft.world.level.block.state.BlockState
- .offTheBack
- GateScreen
- .boundsAt
- .findGenerationPoint
- Doctrine
- BlockGrid
- .at
- FactionMapScreen
- .save
- Outcome
- Order
- OrganizationScreen
- GateBlockEntity
- Machine
- Role
- ShipKind
- UnitMode
- list
- WoolTest
- Lands
- WeaponSkills
- Build
- MarkerBlock.java
- WallTemplates
- OarFigures
- GatePlanksBlock.java
- .moraleOf
- FieldBlock
- .deployment
- Shape
- org.joml.Matrix4f
- ConquestConfig
- Volleys
- Trait
- Cited Findings
- Job
- Task
- Kind
- ArtilleryScreen
- 10. Overall ranking, and areas checked that look fine
- ClientBarks
- Phase
- Missile
- .roadStart
- HornOrders
- DistantOcclusion
- .get
- Muster
- .groundCoversEveryTent
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Role
- .choose
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 315 edges
2. `FieldBattle` - 232 edges
3. `FieldBlock` - 231 edges
4. `ConquestNetwork` - 134 edges
5. `Point` - 130 edges
6. `FieldArmy` - 129 edges
7. `Lands` - 89 edges
8. `BattleScreen` - 87 edges
9. `Siege` - 86 edges
10. `Politics` - 79 edges

## Surprising Connections (you probably didn't know these)
- `Cited Findings` --references--> `MapView`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/client/map/MapView.java
- `Cited Findings` --references--> `FormationSnapshot`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/battle/FormationSnapshot.java
- `Cited Findings` --references--> `Sprite`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/battle/FormationSnapshot.java
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

## Communities (429 total, 259 thin omitted)

### Community 0 - "Calibration"
Cohesion: 0.12
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 1 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.07
Nodes (9): BaseMapPart, BattleSpot, FactionInfoReply, GateView, PeerageView, PoliticsView, UnitsRequest, UnitsState (+1 more)

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.12
Nodes (3): CityRoadPiece, HarbourCutPiece, HarbourPiece

### Community 3 - "org.junit.jupiter.api.Test"
Cohesion: 0.05
Nodes (9): DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, SwingModesTest, GrandStrategyDateTest, OarRowingTest, ArmouryTest, RetinuesTest (+1 more)

### Community 4 - "RafasConquest.java"
Cohesion: 0.04
Nodes (8): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), TickWeave experiment, GrappleHooks, ConquestEntities, RafasConquest

### Community 7 - ".sendToServer"
Cohesion: 0.08
Nodes (11): BackBanner, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel, Type (+3 more)

### Community 8 - "BattleBannerBlockEntity"
Cohesion: 0.17
Nodes (3): BattleBannerBlockEntity, Banner, PlacedBanners

### Community 9 - "net.minecraft.world.level.Level"
Cohesion: 0.07
Nodes (6): Sea, ArtilleryBlock, GrandStrategyBlock, QuestBoardBlock, SailBlock, WorldMapItem

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 13 - "BaseMap"
Cohesion: 0.06
Nodes (16): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, ClientMapCache, Junction, MapPreview (+8 more)

### Community 14 - "RecruitsBattle.java"
Cohesion: 0.09
Nodes (30): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+22 more)

### Community 16 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.07
Nodes (14): Base map generated from seed, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Faction Info screen, Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, World map layers, Nations by zone (+6 more)

### Community 17 - "RafasConquest Project Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.26
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "CityLayout"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 20 - ".postChunk"
Cohesion: 0.08
Nodes (7): Cited Findings, RecruitsBridge, BarracksPeople, CityBanners, CityGuards, ConquestServer, PoiPrecomputer

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 23 - ".state"
Cohesion: 0.10
Nodes (6): HornCall, ATTACKING, MOVING, RALLYING, RETREATING, BattleMap

### Community 24 - "CityHud"
Cohesion: 0.09
Nodes (3): CityHud, Spot, CityHudPreviewTest

### Community 25 - "Tier"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "PoiPrecomputer.java"
Cohesion: 0.05
Nodes (11): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, FactionInfo, ConquestWorldgen, CountryPlacement (+3 more)

### Community 28 - ".view"
Cohesion: 0.15
Nodes (3): PlayerStats, View, Retinues

### Community 30 - "WorldSettings"
Cohesion: 0.11
Nodes (4): CityLayout, District, Site, CityLayoutTest

### Community 31 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.12
Nodes (6): Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER

### Community 34 - ".map"
Cohesion: 0.10
Nodes (4): FeudDuel, MusterBars, PoliticsSync, Wars

### Community 35 - "CityPlan"
Cohesion: 0.16
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 36 - "City"
Cohesion: 0.12
Nodes (13): Grade, CITY, DOJO, HIGH, SEA, TOWN, Link, Place (+5 more)

### Community 38 - "WaterSwell"
Cohesion: 0.16
Nodes (7): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Made, WaterSwell

### Community 39 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.07
Nodes (6): Camera, CameraView, Card, Enlisted, Unit, CityManager

### Community 41 - "BlockPos"
Cohesion: 0.13
Nodes (5): ArtillerySet, BoardAction, FarBanners, GateSet, MarkerView

### Community 43 - "Peerage"
Cohesion: 0.08
Nodes (11): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+3 more)

### Community 45 - ".view"
Cohesion: 0.19
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 46 - "Personality"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.22
Nodes (6): Lane, Leg, Port, SeaLanes, Ship, Voyage

### Community 50 - "net.minecraft.world.level.levelgen.structure.BoundingBox"
Cohesion: 0.16
Nodes (3): CountryPiece, FarmPiece, PlaceholderPiece

### Community 51 - ".plus"
Cohesion: 0.09
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 52 - "FieldRules"
Cohesion: 0.14
Nodes (7): FieldRules, Side, FLANK, FRONT, REAR, ShareLiveTest, W

### Community 53 - "Homeland"
Cohesion: 0.10
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 54 - "Barracks"
Cohesion: 0.18
Nodes (3): Barracks, BarracksPiece, BarracksTest

### Community 55 - ".render"
Cohesion: 0.16
Nodes (4): CharacterScreen, Spot, Honour, Honours

### Community 57 - "Faction"
Cohesion: 0.07
Nodes (11): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), Faction, ResourceType, FOOD, Poi, PoiType, CAPITAL (+3 more)

### Community 59 - "CityTerrain"
Cohesion: 0.07
Nodes (18): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+10 more)

### Community 60 - "BattleSounds"
Cohesion: 0.17
Nodes (6): BattleSounds, ChargeSound, DinSound, Queued, Shot, Source

### Community 61 - "HorizontalStretch"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 63 - "TitleMap"
Cohesion: 0.16
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.11
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - "Kind"
Cohesion: 0.13
Nodes (13): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HALBERDIERS, HEAVY_CAVALRY (+5 more)

### Community 67 - "WorldSettings"
Cohesion: 0.09
Nodes (10): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, WorldSettings (+2 more)

### Community 69 - ".champion"
Cohesion: 0.15
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 70 - "Row"
Cohesion: 0.13
Nodes (3): Fort, Stronghold, Row

### Community 71 - "School"
Cohesion: 0.09
Nodes (14): Dojos, Ground, School, BLOOD, FIRE, ICE, IRON, LIGHT (+6 more)

### Community 72 - "DistantShips"
Cohesion: 0.14
Nodes (6): DistantShips, Far, HullKey, Riding, Want, DistantShipsTest

### Community 73 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.15
Nodes (7): ArrowSaveMixin, CityVegetationMixin, ClientChunkWindowMixin, DhShaderLoaderMixin, DhTerrainShaderMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 75 - "WarCouncil"
Cohesion: 0.16
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 76 - "FigureGear"
Cohesion: 0.14
Nodes (7): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 77 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.05
Nodes (28): SwingModes, WarHorn, BattleStandardItem, AddonPieces, Armoury, Kit, ARCHER, ARCHER_GUARD (+20 more)

### Community 78 - "RecruitFigures"
Cohesion: 0.09
Nodes (8): Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures, RecruitFiguresTest

### Community 79 - "City"
Cohesion: 0.12
Nodes (3): SeatCities, SeatCitiesTest, World

### Community 81 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.07
Nodes (10): ChunkWait, Wait, Land, Result, SiegeSurvey, Wall, ArtilleryMount, CityGates (+2 more)

### Community 82 - "Candidate"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "ConquestSavedData"
Cohesion: 0.09
Nodes (4): Cited Findings, ConquestSavedData, EconomyTick, ConquestSavedDataTest

### Community 84 - "Recruiter"
Cohesion: 0.09
Nodes (4): RecruiterRenderer, Barks, Ear, Recruiter

### Community 85 - "net.minecraft.world.level.block.entity.BlockEntity"
Cohesion: 0.07
Nodes (6): MarkerRenderer, ConquestContent, Oar, Sail, MarkerBlockEntity, MarkerSpec

### Community 87 - "Succession"
Cohesion: 0.17
Nodes (3): Hook, Succession, SuccessionFormatTest

### Community 89 - "Cited Findings"
Cohesion: 0.10
Nodes (8): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, DistantArmies, Throw, ThrownFigures

### Community 91 - "CourtView"
Cohesion: 0.13
Nodes (4): Camera, CourtView, Figure, Organization

### Community 94 - "TechTreeScreen"
Cohesion: 0.18
Nodes (4): TechTreeScreen, Tech, TechTree, TechTreeTest

### Community 95 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.05
Nodes (35): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Character page, Characters (holders, skills, prowess, traits), Court 3D scene (CourtView), Cultures (seven homelands) (+27 more)

### Community 96 - "Grid"
Cohesion: 0.19
Nodes (5): Countryside, Plan, Grid, CountrysideTest, World

### Community 98 - "CityGridBuilder"
Cohesion: 0.08
Nodes (10): Houses, CityGridBuilder, Ground, Platform, CityVariant, DESERT, PLAINS, SAVANNA (+2 more)

### Community 99 - "FactionInfoScreen"
Cohesion: 0.16
Nodes (3): FactionInfoScreen, Line, StructureLink

### Community 100 - "NationScreen"
Cohesion: 0.11
Nodes (5): NationScreen, Banner, Portraits, Demographics, Share

### Community 101 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 102 - "War"
Cohesion: 0.08
Nodes (3): Army, Goal, War

### Community 103 - "FigureBatches"
Cohesion: 0.07
Nodes (11): Batch, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE (+3 more)

### Community 105 - "Point"
Cohesion: 0.04
Nodes (19): Point, Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate (+11 more)

### Community 106 - "WarCamp"
Cohesion: 0.09
Nodes (6): PlannedTent, Saved, WarCamp, CourtSet, Seat, Style

### Community 107 - "How Total War calculates battles between units"
Cohesion: 0.17
Nodes (11): 1. Combat is per soldier, not per unit, 2. Does the swing land?, 3. How much damage the hit does, 4. Charges, 5. Missiles, 6. Leadership (morale): what actually wins battles, 7. Autoresolve, 8. Older games (Shogun 1, Medieval 1, Rome 1) (+3 more)

### Community 108 - "MapContext"
Cohesion: 0.07
Nodes (14): Cited Findings, BaseMapLayer, MapContext, LegendEntry, MapLayer, Symbol, MapView, Place (+6 more)

### Community 109 - "Garrison"
Cohesion: 0.11
Nodes (5): Garrison, Army, GrandStrategy, View, GarrisonTest

### Community 110 - "CityPanel"
Cohesion: 0.17
Nodes (6): CityPanel, Mode, DISMISS, MARCH, MUSTER, NONE

### Community 111 - ".step"
Cohesion: 0.12
Nodes (6): FigureMotion, Ground, Match, Placed, State, FigureMotionTest

### Community 112 - ".send"
Cohesion: 0.12
Nodes (4): BoardView, GrandStrategyView, StatsView, TalkView

### Community 113 - ".place"
Cohesion: 0.14
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 115 - "Body"
Cohesion: 0.08
Nodes (26): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+18 more)

### Community 116 - ".choose"
Cohesion: 0.16
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 117 - ".generate"
Cohesion: 0.11
Nodes (6): Dojo, ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry

### Community 118 - "ConquestNetwork"
Cohesion: 0.06
Nodes (19): Bark, CameraAt, ChargeHits, CityClose, CityView, ConquestNetwork, CycleSwing, FigureStrike (+11 more)

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.09
Nodes (3): GrandStrategyScreen, Marcher, Spot

### Community 121 - "net.minecraft.world.entity.LivingEntity"
Cohesion: 0.07
Nodes (7): BattleBannerBlock, FlagBlock, Mount, BARS, BLOCK, FENCE, WALL

### Community 122 - "DistantHorizonsArmies"
Cohesion: 0.05
Nodes (9): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink (+1 more)

### Community 123 - ".item"
Cohesion: 0.20
Nodes (3): FactionBanner, Layer, FactionBannerTest

### Community 124 - "Quests"
Cohesion: 0.13
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 125 - "NobleTitle"
Cohesion: 0.12
Nodes (9): Names, NobleTitle, COUNT, DUKE, EMPEROR, KING, KNIGHT, NONE (+1 more)

### Community 126 - "FormationSnapshot"
Cohesion: 0.11
Nodes (12): Cited Findings, Kind, ENGINE, FOOT, MOUNTED, RANGED, Engine, FormationSnapshot (+4 more)

### Community 127 - "net.minecraft.core.BlockPos"
Cohesion: 0.11
Nodes (3): CommandTableBlock, CharacterMarkers, Placed

### Community 130 - "TitleOwnersScreen"
Cohesion: 0.18
Nodes (3): ClientPeerage, Owner, TitleOwnersScreen

### Community 131 - "ProvinceMap"
Cohesion: 0.17
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 132 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 133 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.06
Nodes (14): BackBannerLayer, BannerArt, BattleBannerRenderer, CapeCurio, ClothItemRenderer, Carried, ClothRenderer, Fabric (+6 more)

### Community 134 - "Cloth"
Cohesion: 0.14
Nodes (4): Cloth, Solid, ClothTest, StretchTest

### Community 136 - ".world"
Cohesion: 0.13
Nodes (4): Retainer, Taking, RealmPlannerTest, World

### Community 137 - "BattleType"
Cohesion: 0.17
Nodes (5): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (24): 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings, Cited Findings (+16 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (40): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 6. Sodium (and Iris) compatibility, 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply (+32 more)

### Community 142 - "Ride"
Cohesion: 0.14
Nodes (3): Ride, Swell, SwellTest

### Community 152 - "Override"
Cohesion: 0.10
Nodes (5): Chunks, BattleSite.Water, BattleSite.Water, SettingsScreen, Politics.Front

### Community 156 - "RoadLayer"
Cohesion: 0.11
Nodes (13): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+5 more)

### Community 158 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 160 - "Rank"
Cohesion: 0.13
Nodes (10): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+2 more)

### Community 163 - "UnitOrder"
Cohesion: 0.11
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 164 - "Realm"
Cohesion: 0.19
Nodes (4): Kind, FRIEND, RIVAL, Realm

### Community 167 - "LabelLayer"
Cohesion: 0.11
Nodes (3): Hit, LabelLayer, StrategyRelief

### Community 174 - "Settlement"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 175 - "ChunkMapCameraMixin.java"
Cohesion: 0.14
Nodes (7): Inferences, Cited Findings, Cited Findings, ChunkMapAccess, ChunkMapCameraMixin, RecruitsArrowCleanerMixin, TrackedEntityCameraMixin

### Community 181 - ".accept"
Cohesion: 0.15
Nodes (3): Ranks, Offer, Recruitment

### Community 186 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 205 - ".render"
Cohesion: 0.16
Nodes (6): ClientTownsfolk, Folk, StreetWalks, Walk, Streets, ClientTownsfolkTest

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 219 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.32
Nodes (3): Inferences, Api, DistantGeneration

### Community 223 - "SiegeWorks"
Cohesion: 0.07
Nodes (11): State, CARRIED, RAISING, UP, Work, SiegeWorks, Swing, CLOSED (+3 more)

### Community 224 - "RecruitFigures.java"
Cohesion: 0.06
Nodes (4): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), DistantDepth

### Community 225 - "Hull"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 226 - "ConquestWorldScreen"
Cohesion: 0.22
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 234 - "GoldChurch.java"
Cohesion: 0.16
Nodes (3): GoldChurch, Layout, GoldChurchPiece

### Community 235 - "ClientModEvents"
Cohesion: 0.11
Nodes (3): ClientModEvents, net.neoforged.neoforge.client.extensions.common.IClientItemExtensions, Registration

### Community 236 - "Piece"
Cohesion: 0.13
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.05
Nodes (13): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Cited Findings, Gaps, Inferences, Takeaway, BattleSpotRing, DistantArmyRenderer (+5 more)

### Community 240 - "ShipHulls"
Cohesion: 0.21
Nodes (3): BattleKind, Part, ShipHulls

### Community 249 - ".plan"
Cohesion: 0.14
Nodes (4): Faith, Bond, RealmPlanner, Seat

### Community 252 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 267 - "SeaCalm"
Cohesion: 0.22
Nodes (4): Depths, SeaCalm, Strengths, SeaCalmTest

### Community 272 - "RecruitsBattle"
Cohesion: 0.06
Nodes (3): Cited Findings, Landing, RecruitsBattle

### Community 274 - "Weapon"
Cohesion: 0.18
Nodes (9): Weapon, ARCBALLISTA, CULVERIN, HWACHA, MONS_MEG, ONAGER, SERPENTINE, TOWER_CROSSBOW (+1 more)

### Community 278 - "DayClockTest"
Cohesion: 0.25
Nodes (3): DayClock, Step, DayClockTest

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.17
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 293 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.11
Nodes (5): BattleWallBannerBlock, HelmBlock, OarBlock, StrategyBoardBlock, Builder

### Community 297 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 298 - ".findGenerationPoint"
Cohesion: 0.15
Nodes (3): DistrictKey, Townsfolk, CityDistrictStructure

### Community 300 - "Doctrine"
Cohesion: 0.06
Nodes (22): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+14 more)

### Community 309 - "Order"
Cohesion: 0.40
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

### Community 311 - "GateBlockEntity"
Cohesion: 0.08
Nodes (8): GateDoors, Ram, SableLink, Door, GateBlockEntity, GateGeometry, Hinge, GateGeometryTest

### Community 314 - "Role"
Cohesion: 0.13
Nodes (10): Officers, GameCharacter, Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN (+2 more)

### Community 318 - "ShipKind"
Cohesion: 0.33
Nodes (6): ShipKind, ARCHERS, CAMP, GUNSHIP, SHOCK, SWORDSMEN

### Community 319 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 321 - "list"
Cohesion: 0.06
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 323 - "Lands"
Cohesion: 0.07
Nodes (14): Cited Findings, Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Feud, Lord (+6 more)

### Community 326 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 327 - "MarkerBlock.java"
Cohesion: 0.10
Nodes (3): GateBlock, MarkerBlock, PlacedWall

### Community 330 - "OarFigures"
Cohesion: 0.17
Nodes (3): Model, OarFigures, Rowing

### Community 350 - ".moraleOf"
Cohesion: 0.11
Nodes (13): 1. Flank and rear shock, 2. Nearby rout (fright), 3. Army-wide losses, 4. Shattering, Design: adding the four morale shocks (built 2026-11-11, untested in game), Tests (FieldBattleTest), The core change: morale becomes a sum, Morale (+5 more)

### Community 355 - "FieldBlock"
Cohesion: 0.04
Nodes (8): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock, Rates

### Community 364 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 365 - "org.joml.Matrix4f"
Cohesion: 0.14
Nodes (12): Arrow, ArrowVolleys, Kind, ARROW, BALL, BIG_BALL, BIG_ROCK, BOLT (+4 more)

### Community 379 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 385 - "Cited Findings"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?

### Community 386 - "Job"
Cohesion: 0.10
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 387 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 389 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 393 - "10. Overall ranking, and areas checked that look fine"
Cohesion: 0.50
Nodes (4): 10. Overall ranking, and areas checked that look fine, Gaps, Inferences, Takeaway

### Community 401 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 403 - "Missile"
Cohesion: 0.50
Nodes (4): Missile, ARROWS, NONE, SHOT

### Community 407 - "DistantOcclusion"
Cohesion: 0.11
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 411 - "Muster"
Cohesion: 0.67
Nodes (3): Muster, CEREMONIAL, OFFENSIVE

### Community 415 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.06
Nodes (31): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+23 more)

### Community 422 - "Role"
Cohesion: 0.40
Nodes (5): Role, MANTLET, RAM, SHOOTER, TOWER

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **431 isolated node(s):** `ADVANCE`, `CAVALRY_CHARGE`, `CAVALRY_RECALL`, `WALL`, `GENERAL_CHARGES` (+426 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1467 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **259 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `Calibration`, `net.minecraft.world.level.ChunkPos`, `org.junit.jupiter.api.Test`, `.facingX`, `ShipHulls.java`, `RecruitsBattle.java`, `BattleMap.java`, `Phase`, `.enlist`, `.state`, `net.neoforged.bus.api.SubscribeEvent`, `GrappleHookRenderer.java`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `PveCommands`, `.map`, `UnitOrder`, `RecruitsBattle.java`, `net.minecraft.server.level.ServerPlayer`, `rafasconquest.faction.Faction`, `Peerage`, `net.minecraft.client.gui.GuiGraphics`, `Outcome`, `Role`, `UnitMode`, `WarCouncil`, `net.minecraft.world.item.ItemStack`, `net.minecraft.server.level.ServerLevel`, `FarFlags.java`, `SiegeWorks`, `FieldBlock`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `WarCamp`, `Shape`, `Garrison`?**
  _High betweenness centrality (0.082) - this node is a cross-community bridge._
- **What connects `ADVANCE`, `CAVALRY_CHARGE`, `CAVALRY_RECALL` to the rest of the system?**
  _431 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Calibration` be split into smaller, more focused modules?**
  _Cohesion score 0.12433862433862433 - nodes in this community are weakly interconnected._
- **Why does `FieldBattle` connect `FieldBlock` to `org.junit.jupiter.api.Test`, `.facingX`, `BattleType`, `ShipHulls.java`, `RecruitsBattle.java`, `RecruitsBattle`, `.state`, `PveCommands`, `RecruitsBattle.java`, `rafasconquest.faction.Faction`, `Doctrine`, `.plus`, `Outcome`, `Machine`, `Call`, `Bodies`, `list`, `.moraleOf`, `Point`?**
  _High betweenness centrality (0.041) - this node is a cross-community bridge._
- **Should `net.minecraft.network.FriendlyByteBuf` be split into smaller, more focused modules?**
  _Cohesion score 0.07396870554765292 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._