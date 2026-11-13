# Graph Report - RafasConquest  (2026-11-13)

## Corpus Check
- 511 files · ~458,515 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7320 nodes · 23400 edges · 388 communities (148 shown, 240 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3150 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `ffae4a9c`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Task
- net.minecraft.world.level.block.state.BlockState
- net.minecraft.world.level.ChunkPos
- .army
- arraylist
- BattleScreen
- DistantHorizonsArmies.java
- CityGridBuilder
- Lands
- net.minecraft.server.level.ServerPlayer
- .findGenerationPoint
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- Recruiter
- BaseMap
- UnitOrder
- PoliticsTest
- ConquestSavedData
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.GuiGraphics
- PveCommands.java
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- GateDoors.java
- HorizontalStretch
- CityManagerScreen
- .plan
- org.spongepowered.asm.mixin.Mixin
- rafasconquest.battle.FieldBlock
- WarCouncil
- GateBlockEntity.java
- .view
- .send
- CapeCurio.java
- PveCommands Registration
- School
- CityPlan
- net.minecraft.world.entity.Entity
- Chunks
- Arm
- .map
- rafasconquest.faction.Faction
- net.minecraft.client.renderer.MultiBufferSource
- Lands
- RecruitFigures
- .enlist
- FieldBlock
- Personalities
- SeaLanes
- net.minecraft.world.entity.EntityType
- Cited Findings
- BattlePlan
- ProvinceMap
- .plan
- Barracks
- .roadStart
- CityStyles
- SeatCities
- Faction Map View
- .step
- ClientScreens
- net.minecraft.core.Vec3i
- TitleMapTest
- Bodies
- StyleLibrary
- .step
- .choose
- BattleMap.java
- Row
- Homeland
- net.minecraft.world.item.DyeColor
- .walks
- MapLands
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- NobleAnnex
- .board
- ClientEvents
- RafasConquest.java
- net.minecraft.core.Direction
- Ownership & Territory
- CityLayout
- Politics
- Land
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- Faction
- .postChunk
- Outcome
- Terrain
- PoiPrecomputer.java
- WarCamp
- FactionInfoScreen
- Point
- net.minecraft.nbt.CompoundTag
- Cloth
- FigureBatches
- .view
- CameraView
- ClothRenderer
- Override
- .of
- CourtSet
- Work
- Corners
- WaterSwell
- SiegeWorks
- .generate
- Realm
- Cited Findings
- .build
- GrandStrategyScreen
- CityTerrain
- WorldSettings
- .deploy
- City
- com.mojang.blaze3d.vertex.PoseStack
- CountryStructure
- BattleShips
- GrappleHook
- MapView
- .cellMax
- .shift
- .findGenerationPoint
- SeaCalm
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- .onLevelLoad
- net.minecraft.world.level.block.Rotation
- .height
- net.minecraft.resources.ResourceLocation
- MapLayer
- RoadNetwork
- CLAUDE.md
- War
- BattleRole
- Kind
- .touches
- RoadNetwork
- Machine
- RecruitFigures
- net.minecraft.world.item.ItemStack
- Small Cluster 170
- RecruitsBattle
- CityPlacement.java
- BoardScreen
- SoldierFigure
- Hull
- ConquestWorldgen
- .noble
- Duty
- ConquestNetwork
- .view
- OrganizationScreen
- BattleSnapshot
- RecruitFigures.java
- Cited Findings
- .officers
- .render
- DistantOcclusion
- Townsperson
- net.neoforged.bus.api.SubscribeEvent
- .embody
- Kind
- DistantArmyRenderer
- BattleSounds
- Body
- ConquestGameRules
- .boundsAt
- GateBlockEntity
- Trait
- Politics.Front
- Ride
- Roads
- Phase
- Profiling run sheet (baseline)
- .measure
- net.minecraft.sounds.SoundEvent
- SableSeen
- .offTheBack
- BattleKeepInventory
- ClientPolitics
- How do world generation and structure placement work, and what do they cost?
- CourtView
- .fire
- Realm
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- CityLayout
- net.minecraft.world.entity.LivingEntity
- .onLogout
- Kind
- CityPlacement
- How Total War calculates battles between units
- Wind.java
- UnitMode
- RecruitsBattle.java
- TrackedEntityCameraMixin.java
- .sendToServer
- .render
- Override
- Call
- GatePlanksBlock.java
- .bark
- RafasConquest (PvE addon for Villager Recruits)
- Override
- Clash
- CountryPlacement
- Ranks
- ClientBarks
- assertequals
- .rout
- Organization
- Role
- DojoPlacement
- WorldMapItem.java
- .block
- .soundHorn
- .siege
- NobleDistrictTest
- ConquestConfig
- net.neoforged.fml.common.EventBusSubscriber
- CityVegetationMixin.java
- BlockGrid
- Weapon
- ThrownFigures
- 5. Water/ocean waves (sea swell, foam, boats on waves)
- Saved data: how big, how often written, is everything re-serialized?
- ShipKind
- Shape
- State
- Registration

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 299 edges
2. `FieldBlock` - 222 edges
3. `FieldBattle` - 212 edges
4. `Point` - 153 edges
5. `FieldArmy` - 127 edges
6. `ConquestNetwork` - 119 edges
7. `Lands` - 89 edges
8. `Siege` - 84 edges
9. `BattleScreen` - 82 edges
10. `Politics` - 79 edges

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

## Communities (388 total, 240 thin omitted)

### Community 0 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 1 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (7): CommandTableBlock, GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, Builder, MarkerBlock

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.06
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - ".army"
Cohesion: 0.06
Nodes (4): DoctrineTest, FieldBattleTest, GrandStrategyDateTest, RetinuesTest

### Community 4 - "arraylist"
Cohesion: 0.05
Nodes (18): Base map generated from seed, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Demographics (population by culture and race), Faction Info screen, Heralds (daily news), Land titles (county, duchy, kingdom, empire), Mantra schools and dojos (+10 more)

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.07
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "CityGridBuilder"
Cohesion: 0.07
Nodes (10): CityGridBuilder, Ground, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA (+2 more)

### Community 8 - "Lands"
Cohesion: 0.10
Nodes (3): Cited Findings, Lands, Claim

### Community 9 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.12
Nodes (4): Camera, Card, Enlisted, Unit

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 13 - "BaseMap"
Cohesion: 0.08
Nodes (10): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, BaseMap, Nation, BaseMapStore (+2 more)

### Community 14 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 16 - "ConquestSavedData"
Cohesion: 0.06
Nodes (8): Cited Findings, ConquestSavedData, MapPreview, Poi, EconomyTick, PoiPrecomputer, ConquestSavedDataTest, FactionInfoTest

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.14
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 19 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.05
Nodes (3): Races (Sunborn, Vinekin, Meadowfolk, ...), GateScreen, InventoryStatsButton

### Community 20 - "PveCommands.java"
Cohesion: 0.08
Nodes (30): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+22 more)

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 25 - ".plan"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.17
Nodes (7): Cited Findings, ClientChunkWindowMixin, DhShaderLoaderMixin, DhTerrainShaderMixin, RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 28 - "WarCouncil"
Cohesion: 0.12
Nodes (7): Facts, Officer, Said, Talk, WarCouncil, CityManager, WarCouncilPagesTest

### Community 29 - "GateBlockEntity.java"
Cohesion: 0.08
Nodes (4): BattleBannerBlockEntity, ConquestContent, MarkerBlockEntity, MarkerSpec

### Community 30 - ".view"
Cohesion: 0.06
Nodes (15): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+7 more)

### Community 31 - ".send"
Cohesion: 0.06
Nodes (17): BackBanner, Bark, ChargeHits, CityClose, OpenBattleScreen, Place, SeaSwell, Entry (+9 more)

### Community 32 - "CapeCurio.java"
Cohesion: 0.11
Nodes (4): BackBannerLayer, CapeCurio, Carried, MouthLayer

### Community 34 - "School"
Cohesion: 0.09
Nodes (14): Dojos, Ground, School, BLOOD, FIRE, ICE, IRON, LIGHT (+6 more)

### Community 35 - "CityPlan"
Cohesion: 0.13
Nodes (4): CityPlan, Piece, Plan, Building

### Community 38 - "Arm"
Cohesion: 0.08
Nodes (18): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+10 more)

### Community 39 - ".map"
Cohesion: 0.11
Nodes (5): Cited Findings, FeudDuel, MusterBars, PoliticsSync, Wars

### Community 41 - "net.minecraft.client.renderer.MultiBufferSource"
Cohesion: 0.15
Nodes (3): BattleBannerRenderer, GateRenderer, MarkerRenderer

### Community 42 - "Lands"
Cohesion: 0.12
Nodes (4): Feud, Lord, Feuds, Front

### Community 43 - "RecruitFigures"
Cohesion: 0.12
Nodes (4): Capture, Film, Horse, RecruitFiguresTest

### Community 45 - "FieldBlock"
Cohesion: 0.04
Nodes (7): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (13): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+5 more)

### Community 50 - "Cited Findings"
Cohesion: 0.18
Nodes (5): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Inferences, Takeaway, DistantArmies

### Community 51 - "BattlePlan"
Cohesion: 0.05
Nodes (9): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, WarPlan (+1 more)

### Community 52 - "ProvinceMap"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 53 - ".plan"
Cohesion: 0.15
Nodes (5): Centre, Faith, Bond, RealmPlanner, Seat

### Community 57 - "SeatCities"
Cohesion: 0.10
Nodes (5): City, SeatCities, CityIndex, SeatCitiesTest, World

### Community 59 - ".step"
Cohesion: 0.14
Nodes (5): FigureMotion, Match, Placed, State, FigureMotionTest

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (5): Gaps, TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.10
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 65 - "StyleLibrary"
Cohesion: 0.10
Nodes (9): 10. Overall ranking, and areas checked that look fine, Gaps, Inferences, Takeaway, Loaded, Offsets, Placement, StyleLibrary (+1 more)

### Community 66 - ".step"
Cohesion: 0.15
Nodes (7): Cited Findings, Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 67 - ".choose"
Cohesion: 0.12
Nodes (11): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+3 more)

### Community 71 - "Homeland"
Cohesion: 0.09
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 72 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.17
Nodes (6): DistantShips, Far, Riding, BattleKind, Part, ShipHulls

### Community 73 - ".walks"
Cohesion: 0.22
Nodes (3): DistrictKey, Townsfolk, StreetWalksTest

### Community 75 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.07
Nodes (26): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps, Gaps (+18 more)

### Community 77 - ".board"
Cohesion: 0.13
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 81 - "net.minecraft.core.Direction"
Cohesion: 0.10
Nodes (12): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, Land, Result, SiegeSurvey (+4 more)

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "CityLayout"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 84 - "Politics"
Cohesion: 0.07
Nodes (11): Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Stronghold, Event, Muster, CEREMONIAL (+3 more)

### Community 85 - "Land"
Cohesion: 0.26
Nodes (3): Heap, Search, ShippingRoutes

### Community 86 - "Calibration"
Cohesion: 0.13
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.07
Nodes (21): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+13 more)

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 91 - "Faction"
Cohesion: 0.11
Nodes (4): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction

### Community 92 - ".postChunk"
Cohesion: 0.12
Nodes (5): Cited Findings, BarracksPeople, CityGuards, ConquestServer, StructureLink

### Community 96 - "PoiPrecomputer.java"
Cohesion: 0.04
Nodes (18): /pve debug citystats [samples], Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), SeedHash, FactionType, BANDIT (+10 more)

### Community 100 - "Point"
Cohesion: 0.05
Nodes (18): Point, Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate (+10 more)

### Community 101 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.06
Nodes (9): Palette, WallTemplates, CityTerrainPiece, Build, CASTLE, HAMLET, RUIN, TOWER (+1 more)

### Community 102 - "Cloth"
Cohesion: 0.11
Nodes (4): Cloth, Solid, ClothTest, WoolTest

### Community 103 - "FigureBatches"
Cohesion: 0.10
Nodes (9): Batch, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE, FigureBatches (+1 more)

### Community 105 - ".view"
Cohesion: 0.06
Nodes (20): Advancement, Boards, Entry, View, Names, NobleTitle, COUNT, KNIGHT (+12 more)

### Community 106 - "CameraView"
Cohesion: 0.16
Nodes (4): Cited Findings, CameraView, ChunkMapAccess, ChunkMapCameraMixin

### Community 107 - "ClothRenderer"
Cohesion: 0.28
Nodes (4): ClothRenderer, Fabric, Kind, Live

### Community 109 - ".of"
Cohesion: 0.16
Nodes (5): FactionBanner, Layer, FactionBannerNbt, CityBanners, FactionBannerTest

### Community 110 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 113 - "WaterSwell"
Cohesion: 0.13
Nodes (3): Made, WaterSwell, SeaFoamTest

### Community 115 - ".generate"
Cohesion: 0.08
Nodes (7): Dojo, ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 116 - "Realm"
Cohesion: 0.19
Nodes (4): Kind, FRIEND, RIVAL, Realm

### Community 117 - "Cited Findings"
Cohesion: 0.15
Nodes (12): A tick budget and staggered schedules make the fixes stick, Battle code costs 0.2 ms; the multi-second freezes come from blocking, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom? (+4 more)

### Community 118 - ".build"
Cohesion: 0.12
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 121 - "CityTerrain"
Cohesion: 0.07
Nodes (17): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+9 more)

### Community 122 - "WorldSettings"
Cohesion: 0.09
Nodes (10): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, WorldSettings (+2 more)

### Community 124 - "City"
Cohesion: 0.12
Nodes (13): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+5 more)

### Community 125 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.19
Nodes (3): BannerArt, ClothItemRenderer, Layer

### Community 128 - "BattleShips"
Cohesion: 0.16
Nodes (4): BattleShips, Helm, Ram, SableLink

### Community 130 - "MapView"
Cohesion: 0.12
Nodes (9): Cited Findings, Hit, LabelLayer, MapContext, MapView, Place, PlaceMenuLayer, PoiIconLayer (+1 more)

### Community 132 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 134 - "SeaCalm"
Cohesion: 0.18
Nodes (5): Ground, Depths, SeaCalm, Strengths, SeaCalmTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (24): 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings, Cited Findings (+16 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.06
Nodes (31): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings, Cited Findings (+23 more)

### Community 143 - ".height"
Cohesion: 0.24
Nodes (6): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Swell

### Community 148 - "MapLayer"
Cohesion: 0.16
Nodes (6): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer, UnitLayer

### Community 152 - "War"
Cohesion: 0.10
Nodes (3): Army, Goal, War

### Community 155 - "BattleRole"
Cohesion: 0.25
Nodes (6): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT

### Community 156 - "Kind"
Cohesion: 0.13
Nodes (12): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY (+4 more)

### Community 160 - "RoadNetwork"
Cohesion: 0.16
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 163 - "Machine"
Cohesion: 0.06
Nodes (21): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Machine, ARCBALLISTA, BATTERING_RAM (+13 more)

### Community 164 - "RecruitFigures"
Cohesion: 0.14
Nodes (5): FigureGear, Layer, Look, Mesh, RecruitFigures

### Community 167 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.06
Nodes (27): WarHorn, BattleStandardItem, Armoury, Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY (+19 more)

### Community 174 - "RecruitsBattle"
Cohesion: 0.07
Nodes (10): Landing, Live, RecruitsBattle, Role, BEARER, GENERAL, GUARD, LEADER (+2 more)

### Community 186 - "SoldierFigure"
Cohesion: 0.19
Nodes (8): Boxes, Palette, Shape, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 204 - "Hull"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 217 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 218 - "ConquestNetwork"
Cohesion: 0.04
Nodes (26): BaseMapPart, BattleSpot, BoardAction, CameraAt, CityView, ConquestNetwork, FactionInfoReply, FigureStrike (+18 more)

### Community 219 - ".view"
Cohesion: 0.14
Nodes (5): BoardView, GrandStrategyView, TalkView, GrandStrategy, View

### Community 224 - "RecruitFigures.java"
Cohesion: 0.04
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 225 - "Cited Findings"
Cohesion: 0.09
Nodes (10): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Cited Findings, Gaps, Inferences, Takeaway, Arrow, ArrowVolleys (+2 more)

### Community 228 - ".render"
Cohesion: 0.09
Nodes (8): CharacterScreen, Retainer, Spot, ClientPeerage, Banner, Portraits, Honour, Honours

### Community 231 - "DistantOcclusion"
Cohesion: 0.14
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 236 - "Kind"
Cohesion: 0.22
Nodes (7): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL

### Community 237 - "DistantArmyRenderer"
Cohesion: 0.08
Nodes (4): DistantDepth, DistantArmyRenderer, Rects, ShipSprite

### Community 240 - "BattleSounds"
Cohesion: 0.25
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 242 - "Body"
Cohesion: 0.11
Nodes (18): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+10 more)

### Community 248 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 249 - "GateBlockEntity"
Cohesion: 0.09
Nodes (9): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+1 more)

### Community 252 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 256 - "Roads"
Cohesion: 0.09
Nodes (16): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Land (+8 more)

### Community 257 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 274 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.32
Nodes (3): Inferences, Api, DistantGeneration

### Community 275 - "CourtView"
Cohesion: 0.08
Nodes (6): Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 276 - ".fire"
Cohesion: 0.10
Nodes (5): Saved, GateGeometry, CharacterMarkers, Placed, GateGeometryTest

### Community 277 - "Realm"
Cohesion: 0.09
Nodes (8): Hook, Kind, FAVOUR, SECRET, Succession, Taking, RealmPlannerTest, World

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.21
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 282 - "CityLayout"
Cohesion: 0.12
Nodes (5): CityLayout, District, Site, SlopeTile, CityLayoutTest

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 292 - "How Total War calculates battles between units"
Cohesion: 0.11
Nodes (17): 1. Combat is per soldier, not per unit, 1. Flank and rear shock, 2. Does the swing land?, 2. Nearby rout (fright), 3. Army-wide losses, 3. How much damage the hit does, 4. Charges, 4. Shattering (+9 more)

### Community 294 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 295 - "RecruitsBattle.java"
Cohesion: 0.06
Nodes (3): /council <topic>, /pve debug recruits, TickWeave experiment

### Community 297 - "TrackedEntityCameraMixin.java"
Cohesion: 0.22
Nodes (6): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway, TrackedEntityCameraMixin

### Community 298 - ".sendToServer"
Cohesion: 0.22
Nodes (3): Entry, Menu, HornOrders

### Community 302 - "Override"
Cohesion: 0.24
Nodes (3): Land, Land, Land

### Community 305 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 309 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 310 - "Override"
Cohesion: 0.12
Nodes (5): BattleSite.Water, ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, SettingsScreen

### Community 311 - "Clash"
Cohesion: 0.28
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 315 - "Ranks"
Cohesion: 0.22
Nodes (6): Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY, SHIELDMAN

### Community 321 - "assertequals"
Cohesion: 0.07
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 323 - ".rout"
Cohesion: 0.18
Nodes (7): The core change: morale becomes a sum, Morale, State, RALLIED, ROUTED, SHATTERED, STEADY

### Community 324 - "Organization"
Cohesion: 0.06
Nodes (29): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Character page, Characters (holders, skills, prowess, traits), Court 3D scene (CourtView), Cultures (seven homelands) (+21 more)

### Community 326 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 330 - ".block"
Cohesion: 0.07
Nodes (4): BattleMap, GroundPlanTest, Land, WarCampTest

### Community 333 - ".soundHorn"
Cohesion: 0.33
Nodes (5): HornCall, ATTACKING, MOVING, RALLYING, RETREATING

### Community 364 - "Weapon"
Cohesion: 0.33
Nodes (6): Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 366 - "5. Water/ocean waves (sea swell, foam, boats on waves)"
Cohesion: 0.40
Nodes (5): 5. Water/ocean waves (sea swell, foam, boats on waves), Cited Findings, Gaps, Inferences, Takeaway

### Community 367 - "Saved data: how big, how often written, is everything re-serialized?"
Cohesion: 0.40
Nodes (5): Cited Findings, Gaps, Inferences, Saved data: how big, how often written, is everything re-serialized?, Takeaway

### Community 370 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 371 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 373 - "State"
Cohesion: 0.50
Nodes (4): State, CARRIED, RAISING, UP

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **414 isolated node(s):** `ARCHERS`, `CAMP`, `SHOCK`, `SWORDSMEN`, `AMBUSH` (+409 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1371 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **240 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `BattleShips`, `Phase`, `net.minecraft.world.level.ChunkPos`, `.army`, `GrappleHook`, `net.minecraft.server.level.ServerPlayer`, `UnitOrder`, `PveCommands.java`, `rafasconquest.battle.FieldBlock`, `WarCouncil`, `.send`, `PveCommands Registration`, `net.minecraft.world.entity.Entity`, `UnitMode`, `net.minecraft.world.item.ItemStack`, `RecruitsBattle.java`, `rafasconquest.faction.Faction`, `.enlist`, `Override`, `.roadStart`, `org.jetbrains.annotations.Nullable`, `BattleMap.java`, `.block`, `.soundHorn`, `ClientEvents`, `RafasConquest.java`, `net.minecraft.core.Direction`, `Calibration`, `.officers`, `WarCamp`, `.view`, `Work`, `SiegeWorks`, `Shape`?**
  _High betweenness centrality (0.079) - this node is a cross-community bridge._
- **What connects `ARCHERS`, `CAMP`, `SHOCK` to the rest of the system?**
  _414 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `net.minecraft.world.level.block.state.BlockState` be split into smaller, more focused modules?**
  _Cohesion score 0.06269113149847094 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `MapView`, `arraylist`, `.onLevelLoad`, `ConquestSavedData`, `ClientPolitics`, `net.minecraft.client.gui.GuiGraphics`, `CourtView`, `RoadNetwork`, `WarCouncil`, `net.minecraft.world.item.ItemStack`, `.map`, `.plan`, `SeatCities`, `ClientScreens`, `TitleMapTest`, `Politics`, `.postChunk`, `OrganizationScreen`, `Terrain`, `PoiPrecomputer.java`, `.render`, `.view`, `.generate`, `Realm`, `Cited Findings`, `GrandStrategyScreen`, `City`?**
  _High betweenness centrality (0.031) - this node is a cross-community bridge._
- **Should `net.minecraft.world.level.ChunkPos` be split into smaller, more focused modules?**
  _Cohesion score 0.0603464686845612 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._