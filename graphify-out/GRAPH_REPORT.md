# Graph Report - RafasConquest  (2026-11-14)

## Corpus Check
- 554 files · ~475,516 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 119 file(s) not represented in the graph (top: .nbt 84, .toml 18, (none) 3)

## Summary
- 7571 nodes · 24356 edges · 393 communities (156 shown, 237 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 3205 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `e08ab929`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- .measure
- War
- net.minecraft.nbt.CompoundTag
- .army
- Lands
- BattleScreen
- DistantHorizonsArmies.java
- CityGridBuilder
- Politics
- rafasconquest.battle.FieldBlock
- Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)
- BaseMapGenerator.java
- BaseMap
- net.minecraft.server.MinecraftServer
- net.minecraft.server.level.ServerLevel
- ConquestServer
- Battle Map Web Page & Site
- Person
- Sea.java
- .fire
- Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)
- net.minecraft.network.FriendlyByteBuf
- HorizontalStretch
- CityManagerScreen
- .plan
- net.minecraft.server.level.ServerPlayer
- Arm
- WarCouncil
- City
- .place
- City
- .view
- PveCommands Registration
- PoliticalStyle
- CityPlan
- net.minecraft.core.Vec3i
- Machine
- net.minecraft.core.BlockPos
- SiegeWorks
- rafasconquest.faction.Faction
- Rank
- Front
- GateBlockEntity
- arraylist
- FieldBattle
- Personalities
- SeaLanes
- Characters (holders, skills, prowess, traits)
- .step
- .enlist
- .view
- Faction
- Barracks
- uuid
- CityStyles
- net.minecraft.world.item.ItemStack
- FactionMapScreen
- .deployment
- com.mojang.blaze3d.vertex.PoseStack
- Peerage
- TitleMapTest
- Bodies
- StyleLibrary
- org.jetbrains.annotations.Nullable
- CapitalLayout
- BattleMap.java
- Row
- .generate
- net.minecraft.world.item.DyeColor
- RecruitsBattle
- MapLands
- GoldChurch.java
- .rightClick
- Centre
- RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots
- Duty
- net.minecraft.server.level.ServerLevel
- Ownership & Territory
- .wallSegments
- FormationSnapshot
- RafasConquest.java
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- AgeTest.java
- DialogueScreen
- .render
- .postChunk
- .encode
- .accept
- Grade
- org.spongepowered.asm.mixin.Mixin
- FactionInfoScreen
- OrganizationScreen
- GrappleHookRenderer.java
- Cloth
- FigureBatches
- net.minecraft.world.phys.Vec3
- .state
- How Total War calculates battles between units
- Override
- .generate
- SimpleChannel.java
- CityVegetationMixin.java
- .plan
- WaterSwell
- Ride
- Body
- BattlePlan
- RankKits
- .feudFight
- GrandStrategyScreen
- CityTerrain
- FigureGear
- CityTerrainPiece
- DayClockTest
- Realm
- BattleScreen.java
- BattleShips
- Terrain
- TitleOwnersScreen
- .drawEmpire
- .shift
- .choose
- SeaCalm
- ConquestSavedData
- RafasConquest client-side performance audit (rendering, screens, client tick, sound)
- Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)
- StatsScreen
- ClientScreens
- .board
- GateBlockEntity
- NationScreen
- MapView
- CLAUDE.md
- ChunkMapCameraMixin
- MapLayer
- .render
- RoadNetwork
- ClientPolitics
- BattleBannerBlock
- RecruitFigures
- Kit
- Small Cluster 170
- RafasConquest (PvE addon for Villager Recruits)
- ConquestEvents
- BattleSnapshot
- .siege
- How do world generation and structure placement work, and what do they cost?
- .noble
- ConquestEvents.java
- MapContext
- WallTemplates
- RecruitsArrowCleanerMixin.java
- BattleSounds
- BlockPos
- UnitOrder
- Cited Findings
- net.neoforged.bus.api.SubscribeEvent
- BattleStandardItem.java
- Plan
- net.minecraft.client.Minecraft
- Kind
- .hash
- ConquestGameRules
- .deploy
- Hull
- .findGenerationPoint
- net.neoforged.fml.common.EventBusSubscriber
- .build
- Land
- Settlement
- Profiling run sheet (baseline)
- .measure
- net.minecraft.sounds.SoundEvent
- ArtilleryScreen
- .rout
- Cited Findings
- Organizations
- .world
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- ClientModEvents.java
- .touches
- .readPlacements
- Cited Findings
- Kind
- TechTreeScreen
- BoardScreen
- .walks
- .onLevelLoad
- GatePlanksBlock.java
- .officers
- Race
- CityLayout
- Call
- Recruiter
- ConquestNetwork
- Override
- rafasconquest.battle.BattlePlan.Point
- .mouseClicked
- GateScreen
- RecruitsBattle.java
- SableSeen
- assertequals
- .onLogout
- .findGenerationPoint
- Phase
- .order
- .offTheBack
- OarFigures
- Task
- Outcome
- .soundHorn
- Override
- PoliticsData
- .onClientExtensions
- .load
- Build
- SwingModes
- .bark
- FieldRules
- ThrownFigures
- ClientBarks
- Override
- com.talhanation.recruits.entities.AbstractRecruitEntity
- Kind
- .view
- RoadNetwork
- WarHorn
- BackBanners
- .get
- .eachSpeakersRunIsAPageWithTheChoicesOnTheLast

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 304 edges
2. `FieldBlock` - 222 edges
3. `FieldBattle` - 209 edges
4. `FieldArmy` - 127 edges
5. `ConquestNetwork` - 124 edges
6. `Lands` - 89 edges
7. `BattleScreen` - 86 edges
8. `Siege` - 82 edges
9. `BaseMap` - 80 edges
10. `Politics` - 79 edges

## Surprising Connections (you probably didn't know these)
- `Cited Findings` --references--> `CityVegetationMixin`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/mixin/CityVegetationMixin.java
- `Cited Findings` --references--> `CityVegetationMixin`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/mixin/CityVegetationMixin.java
- `A tick budget and staggered schedules make the fixes stick` --references--> `BaseMap`  [INFERRED]
  reports/RafasConquest performance gameplan.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_client.md → src/main/java/rafasconquest/map/BaseMap.java
- `Inferences` --references--> `BaseMap`  [INFERRED]
  research_notes/RafasConquest performance gameplan/code_audit_server.md → src/main/java/rafasconquest/map/BaseMap.java

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

## Communities (393 total, 237 thin omitted)

### Community 0 - ".measure"
Cohesion: 0.12
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 1 - "War"
Cohesion: 0.11
Nodes (5): Army, Muster, CEREMONIAL, OFFENSIVE, War

### Community 2 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.06
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - ".army"
Cohesion: 0.07
Nodes (7): Point, DoctrineTest, FieldBattleBodiesTest, FieldBattleTest, SwingModesTest, GrandStrategyDateTest, OarRowingTest

### Community 4 - "Lands"
Cohesion: 0.08
Nodes (9): Cited Findings, Cited Findings, Gaps, How do the simulation and daily-decision systems schedule work? Do they spike?, Inferences, Takeaway, Lands, Stronghold (+1 more)

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.07
Nodes (5): Cited Findings, DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "CityGridBuilder"
Cohesion: 0.07
Nodes (11): CityGridBuilder, Ground, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA (+3 more)

### Community 8 - "Politics"
Cohesion: 0.09
Nodes (3): Claim, Event, PoliticsTest

### Community 9 - "rafasconquest.battle.FieldBlock"
Cohesion: 0.08
Nodes (3): Cited Findings, Landing, Live

### Community 11 - "Server-side performance techniques for a large NeoForge 1.21.1 content mod (RafasConquest)"
Cohesion: 0.04
Nodes (45): Bulk block placement in worldgen and at runtime (setBlock flags, section writes, lighting, heightmaps, chunk loads, tickets, WorldEdit/FAWE, Chunky), Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings, Cited Findings (+37 more)

### Community 12 - "BaseMapGenerator.java"
Cohesion: 0.06
Nodes (16): Ambushes from woods, Base map generated from seed, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Command Table block, Faction Info screen, Deploying to the land (GroundPlan), Land titles (county, duchy, kingdom, empire) (+8 more)

### Community 13 - "BaseMap"
Cohesion: 0.11
Nodes (10): Cited Findings, Gaps, Inferences, Memory: unbounded static caches, leaked Level/Entity references, large arrays, Takeaway, MapPreview, BaseMap, Nation (+2 more)

### Community 14 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.10
Nodes (4): Goal, MusterBars, PoliticsSync, Wars

### Community 16 - "ConquestServer"
Cohesion: 0.10
Nodes (5): Poi, CityBanners, ConquestServer, StructureLink, PoiPrecomputer

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.25
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 20 - ".fire"
Cohesion: 0.08
Nodes (5): MarkerRenderer, CharacterMarkers, Placed, MarkerBlockEntity, MarkerSpec

### Community 21 - "Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server, 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge), 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark, 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls), 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH), 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down, 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons, Cited Findings (+28 more)

### Community 22 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.05
Nodes (13): BaseMapPart, BattleSpot, FigureStrike, PeerageView, SetSetting, StatsView, TalkView, TeleportTo (+5 more)

### Community 25 - ".plan"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.06
Nodes (14): Camera, CameraView, Bark, ChargeHits, CityClose, OpenBattleScreen, Place, PoliticsView (+6 more)

### Community 27 - "Arm"
Cohesion: 0.09
Nodes (14): Arm, ARCHERS, CAVALRY, ENGINES, HEAVY_CAVALRY, HEAVY_INFANTRY, LEVY, MUSKETEERS (+6 more)

### Community 28 - "WarCouncil"
Cohesion: 0.16
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 29 - "City"
Cohesion: 0.08
Nodes (8): Countryside, Plan, Grid, SeatCities, CountrysideTest, World, SeatCitiesTest, World

### Community 31 - "City"
Cohesion: 0.09
Nodes (16): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+8 more)

### Community 32 - ".view"
Cohesion: 0.09
Nodes (12): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+4 more)

### Community 35 - "CityPlan"
Cohesion: 0.17
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 37 - "Machine"
Cohesion: 0.10
Nodes (12): Machine, ARCBALLISTA, BATTERING_RAM, CULVERIN, HWACHA, MANTLET, MONS_MEG, ONAGER (+4 more)

### Community 38 - "net.minecraft.core.BlockPos"
Cohesion: 0.04
Nodes (11): ArtilleryBlock, CommandTableBlock, GateBlock, GrandStrategyBlock, HelmBlock, OarBlock, QuestBoardBlock, SailBlock (+3 more)

### Community 39 - "SiegeWorks"
Cohesion: 0.09
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 41 - "Rank"
Cohesion: 0.10
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 42 - "Front"
Cohesion: 0.14
Nodes (4): Feud, Lord, Feuds, Front

### Community 43 - "GateBlockEntity"
Cohesion: 0.07
Nodes (11): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+3 more)

### Community 45 - "FieldBattle"
Cohesion: 0.07
Nodes (4): FieldArmy, Event, FieldBattle, Order

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (13): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+5 more)

### Community 48 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.08
Nodes (23): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Character page, Characters (holders, skills, prowess, traits), Countryside (hamlets, villages, watchtowers, castles), Court 3D scene (CourtView), Cultures (seven homelands) (+15 more)

### Community 50 - ".step"
Cohesion: 0.14
Nodes (5): FigureMotion, Match, Placed, State, FigureMotionTest

### Community 52 - ".view"
Cohesion: 0.09
Nodes (13): Names, NobleTitle, COUNT, DUKE, EMPEROR, KING, KNIGHT, NONE (+5 more)

### Community 53 - "Faction"
Cohesion: 0.08
Nodes (10): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction, ResourceType, FOOD, PoiType, CAPITAL (+2 more)

### Community 55 - "uuid"
Cohesion: 0.10
Nodes (6): FactionType, BANDIT, CITY_STATE, EMPIRE, PoliticalMap, FactionInfo

### Community 57 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.18
Nodes (3): Armoury, Outfit, Style

### Community 58 - "FactionMapScreen"
Cohesion: 0.12
Nodes (3): Cited Findings, PetitionScreen, AbstractSliderButton

### Community 60 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.05
Nodes (13): BackBannerLayer, BannerArt, BattleBannerRenderer, CapeCurio, ClothItemRenderer, Carried, ClothRenderer, Fabric (+5 more)

### Community 61 - "Peerage"
Cohesion: 0.11
Nodes (10): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+2 more)

### Community 63 - "TitleMapTest"
Cohesion: 0.13
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Bodies"
Cohesion: 0.09
Nodes (5): Bodies, Kind, FOOT, HORSE, BodiesTest

### Community 66 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.05
Nodes (9): BlockGrid, Target, Volley, FieldBlock, ShipKind, ARCHERS, CAMP, SHOCK (+1 more)

### Community 67 - "CapitalLayout"
Cohesion: 0.18
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 69 - "BattleMap.java"
Cohesion: 0.04
Nodes (8): Battle map developer web page, Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Enlisted players, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Morale, rout and rally, Battle roles (frontline, bait, support, flanker, kill squad), The war horn

### Community 71 - ".generate"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 72 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.11
Nodes (9): DistantShips, Far, HullKey, Riding, Want, BattleKind, Part, ShipHulls (+1 more)

### Community 73 - "RecruitsBattle"
Cohesion: 0.05
Nodes (17): Cited Findings, Card, Enlisted, Figure, RecruitsBattle, Shape, LINE, SQUARE (+9 more)

### Community 75 - "GoldChurch.java"
Cohesion: 0.17
Nodes (7): Gold church, Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter, GoldChurch, Layout

### Community 76 - ".rightClick"
Cohesion: 0.24
Nodes (3): Entry, Menu, HornOrders

### Community 77 - "Centre"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 78 - "RafasConquest server/common code audit: CPU, memory, disk and bandwidth hotspots"
Cohesion: 0.08
Nodes (24): Battles: per-tick cost of FieldBattle, armies, BlockGrid, sieges, ramming and naval AI, Cited Findings, Cited Findings, Cited Findings, Gaps, Gaps, Gaps, Gaps (+16 more)

### Community 79 - "Duty"
Cohesion: 0.13
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 81 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.10
Nodes (7): Land, Result, SiegeSurvey, Wall, CityGates, Done, SiegeSurveyTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".wallSegments"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 84 - "FormationSnapshot"
Cohesion: 0.14
Nodes (7): Cited Findings, Engine, FormationSnapshot, Positions, Ship, Sprite, FormationSnapshotTest

### Community 85 - "RafasConquest.java"
Cohesion: 0.08
Nodes (3): GrappleHooks, ConquestEntities, RafasConquest

### Community 87 - "Doctrine"
Cohesion: 0.07
Nodes (21): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+13 more)

### Community 89 - "AgeTest.java"
Cohesion: 0.13
Nodes (8): Age, BRONZE, GUNPOWDER, STEAM, STEEL, Range, WorldFeatures, AgeTest

### Community 90 - "DialogueScreen"
Cohesion: 0.12
Nodes (3): DialogueScreen, BoardView, GrandStrategyView

### Community 91 - ".render"
Cohesion: 0.10
Nodes (7): CharacterScreen, Retainer, Spot, Banner, Portraits, Honour, Honours

### Community 95 - ".accept"
Cohesion: 0.17
Nodes (3): Ranks, Offer, Recruitment

### Community 96 - "Grade"
Cohesion: 0.16
Nodes (11): A tick budget and staggered schedules make the fixes stick, C2ME, Moonrise and Iris set the rules for every optimization, Conclusion, Every frame pays for disk listings, per-rectangle flushes and unculled soldiers, One JVM, six scenarios and JFR beat guesswork on Windows, Profile once, then flatten RafasConquest's spikes, 2. Map screens (FactionMapScreen, GrandStrategyScreen, layers): cached textures or per-frame redraw? Text volume? Work on mouse move and zoom?, Gaps (+3 more)

### Community 98 - "org.spongepowered.asm.mixin.Mixin"
Cohesion: 0.17
Nodes (6): ArrowSaveMixin, ClientChunkWindowMixin, DhShaderLoaderMixin, DhTerrainShaderMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 99 - "FactionInfoScreen"
Cohesion: 0.09
Nodes (6): FactionInfoScreen, Line, FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 101 - "GrappleHookRenderer.java"
Cohesion: 0.11
Nodes (3): GrappleHookRenderer, RecruiterRenderer, GrappleHook

### Community 102 - "Cloth"
Cohesion: 0.12
Nodes (4): Cloth, Solid, ClothTest, WoolTest

### Community 103 - "FigureBatches"
Cohesion: 0.07
Nodes (11): Batch, Corners, Debug, NO_LIGHTMAP, NO_SHADER_COLOUR, NORMAL, RED, WHITE (+3 more)

### Community 105 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.10
Nodes (6): PlannedTent, Saved, WarCamp, CourtSet, Seat, Style

### Community 106 - ".state"
Cohesion: 0.06
Nodes (7): BattleMap, GroundPlanTest, Land, Land, Land, Land, WarCampTest

### Community 107 - "How Total War calculates battles between units"
Cohesion: 0.17
Nodes (11): 1. Combat is per soldier, not per unit, 2. Does the swing land?, 3. How much damage the hit does, 4. Charges, 5. Missiles, 6. Leadership (morale): what actually wins battles, 7. Autoresolve, 8. Older games (Shogun 1, Medieval 1, Rome 1) (+3 more)

### Community 109 - ".generate"
Cohesion: 0.07
Nodes (13): ProvinceNames, BaseMapGenerator, Heights, BiomePalette, Entry, Homeland, DESERT, JUNGLE (+5 more)

### Community 110 - "SimpleChannel.java"
Cohesion: 0.09
Nodes (11): BackBanner, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel, Type (+3 more)

### Community 112 - ".plan"
Cohesion: 0.16
Nodes (4): Faith, Bond, RealmPlanner, Seat

### Community 113 - "WaterSwell"
Cohesion: 0.14
Nodes (8): 4. Sea swell: CPU or shader? How many columns? Interaction with Sodium chunk meshing? Foam noise cost?, Cited Findings, Gaps, Inferences, Takeaway, Made, WaterSwell, SeaFoamTest

### Community 114 - "Ride"
Cohesion: 0.13
Nodes (3): Ride, Swell, SwellTest

### Community 115 - "Body"
Cohesion: 0.08
Nodes (26): Body, BALLISTA, BEARER, BOMBARD, CANNON, FOOT, GENERAL, HWACHA (+18 more)

### Community 118 - ".feudFight"
Cohesion: 0.16
Nodes (5): Clash, Kind, DUEL, SKIRMISH, FeudDuel

### Community 119 - "GrandStrategyScreen"
Cohesion: 0.15
Nodes (3): ClientMapCache, GrandStrategyScreen, Spot

### Community 121 - "CityTerrain"
Cohesion: 0.07
Nodes (18): CityTerrain, Column, Edge, CARVE, FILL, Heights, Kind, NONE (+10 more)

### Community 122 - "FigureGear"
Cohesion: 0.23
Nodes (7): FigureGear, Weapon, BOW, GREAT, LONG, NONE, SWORD

### Community 124 - "DayClockTest"
Cohesion: 0.20
Nodes (3): DayClock, Step, DayClockTest

### Community 126 - "Realm"
Cohesion: 0.16
Nodes (5): Organization, Kind, FRIEND, RIVAL, Realm

### Community 127 - "BattleScreen.java"
Cohesion: 0.06
Nodes (3): Mantra schools and dojos, Roads (high, city, town roads; bridges; signposts), Wind

### Community 128 - "BattleShips"
Cohesion: 0.14
Nodes (4): BattleShips, Helm, Ram, SableLink

### Community 130 - "TitleOwnersScreen"
Cohesion: 0.21
Nodes (3): ClientPeerage, Owner, TitleOwnersScreen

### Community 131 - ".drawEmpire"
Cohesion: 0.27
Nodes (3): Hit, LabelLayer, LabelShape

### Community 132 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 133 - ".choose"
Cohesion: 0.16
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 134 - "SeaCalm"
Cohesion: 0.19
Nodes (5): Ground, Depths, SeaCalm, Strengths, SeaCalmTest

### Community 136 - "ConquestSavedData"
Cohesion: 0.10
Nodes (5): Cited Findings, ConquestSavedData, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 138 - "RafasConquest client-side performance audit (rendering, screens, client tick, sound)"
Cohesion: 0.08
Nodes (23): 10. Overall ranking, and areas checked that look fine, 3. Depth texture / DH occlusion: per-frame framebuffer copies, blits, GPU→CPU syncs, reallocations, program switches? Skipped when not needed?, 7. Per-frame allocations in hot paths, 8. Sounds: how many instances are created and updated, and how often?, 9. Memory: client caches, textures and NativeImages that are never freed or grow without bound, Cited Findings, Cited Findings, Gaps (+15 more)

### Community 139 - "Client rendering and frame-time techniques for a NeoForge 1.21.1 mod (RafasConquest)"
Cohesion: 0.05
Nodes (36): 1. Custom world rendering in 1.21.1 (RenderLevelStageEvent, VertexBuffer vs immediate mode, batching, Flywheel), 2. Rendering hundreds of mobs (Recruits soldiers, ships): culling, LOD, impostors, animation throttling, 3. GUI and map screens (strategy map, faction map, battle overlays), 4. Depth buffers and post effects (DH depth, occlusion, GPU sync), 5. Water/ocean waves (sea swell, foam, boats on waves), 7. Client tick, stutter and sound, 8. Lessons from performance mods that a content mod can apply, Cited Findings (+28 more)

### Community 142 - ".board"
Cohesion: 0.12
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 143 - "GateBlockEntity"
Cohesion: 0.06
Nodes (5): ArtilleryMount, BattleBannerBlockEntity, ConquestContent, Oar, Sail

### Community 146 - "NationScreen"
Cohesion: 0.09
Nodes (6): Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 152 - "ChunkMapCameraMixin"
Cohesion: 0.27
Nodes (6): 6. Sodium (and Iris) compatibility, Cited Findings, Gaps, Inferences, Takeaway, ChunkMapCameraMixin

### Community 155 - "MapLayer"
Cohesion: 0.12
Nodes (7): Cited Findings, BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer, UnitLayer

### Community 164 - "RecruitFigures"
Cohesion: 0.09
Nodes (8): Capture, Film, Horse, Layer, Look, Mesh, RecruitFigures, RecruitFiguresTest

### Community 167 - "Kit"
Cohesion: 0.11
Nodes (19): Kit, ARCHER, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD (+11 more)

### Community 174 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.17
Nodes (8): Arrows in flight (figure volleys), Battle sounds, Recruits PvE Addon Design Doc (PDF), Seeing the whole field (distant flat figures), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 205 - "How do world generation and structure placement work, and what do they cost?"
Cohesion: 0.20
Nodes (6): Gaps, How do world generation and structure placement work, and what do they cost?, Inferences, Takeaway, Api, DistantGeneration

### Community 206 - ".noble"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 217 - "ConquestEvents.java"
Cohesion: 0.05
Nodes (8): ConquestConfig, SeedHash, CityDistrictStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 221 - "RecruitsArrowCleanerMixin.java"
Cohesion: 0.11
Nodes (13): Battle code costs 0.2 ms; the multi-second freezes come from blocking, 5. Mixins: what client methods do they inject into, how hot, added per-call cost, and conflicts with Sodium/Iris/DH fast paths?, Cited Findings, Gaps, Inferences, Takeaway, Cited Findings, Gaps (+5 more)

### Community 223 - "BattleSounds"
Cohesion: 0.18
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 226 - "BlockPos"
Cohesion: 0.17
Nodes (4): BoardAction, GateSet, GateView, MarkerView

### Community 228 - "UnitOrder"
Cohesion: 0.11
Nodes (18): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+10 more)

### Community 232 - "Cited Findings"
Cohesion: 0.06
Nodes (14): 1. Which render events and stages does the mod hook, and what does each do per frame? Is geometry rebuilt and re-uploaded every frame?, Cited Findings, Gaps, Inferences, Takeaway, Arrow, ArrowVolleys, BattleSpotRing (+6 more)

### Community 236 - "Plan"
Cohesion: 0.11
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 237 - "net.minecraft.client.Minecraft"
Cohesion: 0.08
Nodes (5): DistantDepth, DistantArmyRenderer, Figure, Rects, ShipSprite

### Community 238 - "Kind"
Cohesion: 0.13
Nodes (12): Kind, ARCHERS, BEARER, CAVALRY, ENGINES, GENERAL, HEAVY_CAVALRY, HEAVY_INFANTRY (+4 more)

### Community 242 - ".hash"
Cohesion: 0.12
Nodes (6): Bounds, DistanceScaling, NobleAnnex, OriginRoller, Roll, DistanceScalingTest

### Community 249 - "Hull"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 255 - ".build"
Cohesion: 0.12
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 256 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 257 - "Settlement"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 259 - "Profiling run sheet (baseline)"
Cohesion: 0.33
Nodes (5): Before you start, Each scenario, JFR (allocations; spark can't record them on Windows), Profiling run sheet (baseline), Results

### Community 263 - "net.minecraft.sounds.SoundEvent"
Cohesion: 0.18
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 267 - ".rout"
Cohesion: 0.08
Nodes (13): 1. Flank and rear shock, 2. Nearby rout (fright), 3. Army-wide losses, 4. Shattering, Design: adding the four morale shocks (built 2026-11-11, untested in game), Tests (FieldBattleTest), The core change: morale becomes a sum, Morale (+5 more)

### Community 271 - "Cited Findings"
Cohesion: 0.11
Nodes (6): 6. Client tick handlers and packet handlers: per-tick allocations, entity scans, heavy render-thread work on packet arrival, Cited Findings, Gaps, Inferences, Takeaway, DistantArmies

### Community 274 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 277 - ".world"
Cohesion: 0.10
Nodes (5): Hook, Succession, Taking, RealmPlannerTest, World

### Community 281 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.21
Nodes (5): Gaps, Inferences, Takeaway, What runs every server tick or level tick, and how much does each do?, SableShips

### Community 285 - ".readPlacements"
Cohesion: 0.27
Nodes (3): Offsets, Placement, PlacementOffsetsTest

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 295 - ".walks"
Cohesion: 0.11
Nodes (5): StreetWalks, DistrictKey, Townsfolk, CitySizesTest, StreetWalksTest

### Community 300 - ".officers"
Cohesion: 0.09
Nodes (14): GameCharacter, Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN (+6 more)

### Community 302 - "Race"
Cohesion: 0.17
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 303 - "CityLayout"
Cohesion: 0.08
Nodes (7): CityLayout, District, Site, ChunkCoord, FarmLayout, WorldSettings, CityLayoutTest

### Community 305 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 308 - "ConquestNetwork"
Cohesion: 0.08
Nodes (11): ArtillerySet, CameraAt, CityView, ConquestNetwork, CycleSwing, FactionInfoReply, MarkerSet, RequestBaseMap (+3 more)

### Community 310 - "Override"
Cohesion: 0.22
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 313 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.05
Nodes (17): Engine, LADDERS, RAMS, TOWERS, Face, Front, Gate, GateAction (+9 more)

### Community 314 - ".mouseClicked"
Cohesion: 0.42
Nodes (3): Place, PlaceMenuLayer, Row

### Community 318 - "RecruitsBattle.java"
Cohesion: 0.03
Nodes (34): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+26 more)

### Community 321 - "assertequals"
Cohesion: 0.05
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 322 - ".onLogout"
Cohesion: 0.11
Nodes (4): ClientTownsfolk, Folk, Walk, ClientTownsfolkTest

### Community 325 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 327 - ".order"
Cohesion: 0.25
Nodes (5): Order, CHARGE, ENGAGE, HOLD, MARCH

### Community 330 - "OarFigures"
Cohesion: 0.16
Nodes (3): Model, OarFigures, Rowing

### Community 336 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 339 - ".soundHorn"
Cohesion: 0.33
Nodes (5): HornCall, ATTACKING, MOVING, RALLYING, RETREATING

### Community 352 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 361 - "FieldRules"
Cohesion: 0.27
Nodes (3): FieldRules, ShareLiveTest, W

### Community 369 - "Override"
Cohesion: 0.14
Nodes (3): Chunks, SettingsScreen, Politics.Front

### Community 370 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.06
Nodes (12): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Role, BEARER, GENERAL (+4 more)

### Community 371 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 376 - ".view"
Cohesion: 0.21
Nodes (7): Cited Findings, Gaps, Inferences, Takeaway, Which areas were checked and look fine?, GrandStrategy, View

### Community 379 - "RoadNetwork"
Cohesion: 0.15
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

## Ambiguous Edges - Review These
- `Roads (high, city, town roads; bridges; signposts)` → `Mustering (offensive and ceremonial)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **418 isolated node(s):** `ARCHER_MELEE`, `ARROWS`, `CAVALRY`, `INFANTRY`, `CEREMONIAL` (+413 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1389 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **237 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `.measure`, `BattleShips`, `net.minecraft.nbt.CompoundTag`, `.army`, `Politics`, `rafasconquest.battle.FieldBlock`, `net.minecraft.server.MinecraftServer`, `net.minecraft.server.level.ServerLevel`, `Cited Findings`, `net.minecraft.server.level.ServerPlayer`, `WarCouncil`, `PveCommands Registration`, `SiegeWorks`, `.officers`, `ConquestEvents`, `.enlist`, `rafasconquest.battle.BattlePlan.Point`, `net.minecraft.world.item.ItemStack`, `.siege`, `Peerage`, `RecruitsBattle.java`, `.isOurs`, `Phase`, `BattleMap.java`, `net.minecraft.server.level.ServerLevel`, `.soundHorn`, `ConquestEvents.java`, `ConquestEvents.java`, `UnitOrder`, `GrappleHookRenderer.java`, `net.minecraft.world.phys.Vec3`, `.state`, `net.neoforged.bus.api.SubscribeEvent`, `.generate`, `com.talhanation.recruits.entities.AbstractRecruitEntity`?**
  _High betweenness centrality (0.059) - this node is a cross-community bridge._
- **What connects `ARCHER_MELEE`, `ARROWS`, `CAVALRY` to the rest of the system?**
  _418 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `.measure` be split into smaller, more focused modules?**
  _Cohesion score 0.12433862433862433 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `Settlement`, `Terrain`, `Politics`, `BaseMapGenerator.java`, `net.minecraft.server.MinecraftServer`, `GateBlockEntity`, `ConquestServer`, `net.minecraft.server.level.ServerLevel`, `NationScreen`, `.fire`, `WarCouncil`, `City`, `RoadNetwork`, `City`, `PoliticalStyle`, `.onLevelLoad`, `arraylist`, `uuid`, `net.minecraft.world.item.ItemStack`, `FactionMapScreen`, `TitleMapTest`, `.generate`, `Centre`, `RafasConquest.java`, `ConquestEvents.java`, `MapContext`, `.render`, `.postChunk`, `Grade`, `OrganizationScreen`, `.generate`, `.plan`, `GrandStrategyScreen`, `Realm`, `BattleScreen.java`?**
  _High betweenness centrality (0.040) - this node is a cross-community bridge._
- **Should `War` be split into smaller, more focused modules?**
  _Cohesion score 0.10574712643678161 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Roads (high, city, town roads; bridges; signposts)` and `Mustering (offensive and ceremonial)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._