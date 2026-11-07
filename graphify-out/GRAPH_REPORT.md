# Graph Report - RafasConquest  (2026-11-07)

## Corpus Check
- 408 files · ~319,091 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 115 file(s) not represented in the graph (top: .nbt 84, .toml 14, (none) 3)

## Summary
- 5638 nodes · 17860 edges · 305 communities (128 shown, 177 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2300 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `66540dcd`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- RecruitsBattle
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsQuality.java
- CityLayout
- Duty
- .passOnOrders
- CourtView
- ConquestNetwork
- Quest
- BaseMap
- .render
- CityVariant
- net.minecraft.client.gui.GuiGraphics
- Battle Map Web Page & Site
- Person
- FactionInfoScreen
- PoliticsTest
- PveCommands.java
- CityManagerScreen
- .plan
- net.minecraft.world.item.ItemStack
- .shift
- WarCouncil
- rafasconquest.map.BaseMap
- ClientPolitics
- net.minecraft.network.FriendlyByteBuf
- Capital Layout & Seed Hash
- PveCommands Registration
- School
- CityPlan
- Land
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- list
- rafasconquest.faction.Faction
- World map (CK3-style political map)
- Politics.Front
- net.neoforged.neoforge.network.handling.IPayloadContext
- Realm
- net.minecraft.core.Vec3i
- Personalities
- SeaLanes
- MouthLayer.java
- CityGridBuilder
- Settlement
- BattleSounds
- net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
- ConquestSavedData
- net.minecraft.core.BlockPos
- ConquestServer
- View
- Faction Map View
- .second
- Swell
- HorizontalStretch
- TitleMapTest
- .measure
- StyleLibrary
- CityGridBuilder.java
- MarkerSpec
- .postChunk
- RafasConquest.java
- .of
- .wallSegments
- DistantOcclusion
- net.minecraft.world.phys.Vec3
- MapLands
- net.minecraft.resources.ResourceLocation
- PlaceMusicClient
- Recruiter
- Faction
- rafasconquest.battle.FormationSnapshot
- GrandStrategyScreen
- Ownership & Territory
- Peerage
- net.minecraft.server.level.ServerPlayer
- Nation Rows
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- Job
- DialogueScreen
- .view
- com.talhanation.recruits.entities.AbstractRecruitEntity
- ConquestContent.java
- BoardScreen
- .generate
- UnitMode
- WaterSwell
- .ships
- .aWoodNearAWingHidesAnAmbushThatStaysPutUntilItStrikes
- CityTerrainPiece
- CityStyles
- ProvinceMap
- .player
- Strategy
- DistantArmyRenderer
- net.minecraft.world.level.block.Rotation
- Quests
- Rank
- DistantArmyRenderer.java
- RoadNetwork
- City
- Trait
- PoiPrecomputer.java
- .at
- Wars (daily nation decisions)
- .view
- .world
- MapContext
- BattlePlan
- MapView
- net.minecraft.client.gui.GuiGraphics
- .onLevelLoad
- Kind
- org.joml.Matrix4f
- Townsperson
- Distribution
- City
- OrganizationScreen
- BattleShips
- Homeland
- .boundsAt
- FormationSnapshot
- DistantArmies
- FieldBlock
- ClientScreens
- Role
- RoadFeature
- Lands
- Characters (holders, skills, prowess, traits)
- Ranks (Soldier to Grand Marshal)
- CLAUDE.md
- Arm
- UnitOrder
- .plan
- .render
- Lands
- .officers
- UnitInfo
- Small Cluster 170
- ClientModEvents.java
- org.jetbrains.annotations.Nullable
- .issue
- Kind
- .pack
- ConquestWorldScreen
- RecruitsArrowCleanerMixin.java
- .render
- .inCity
- ProvinceNames
- .enlist
- .send
- net.neoforged.bus.api.SubscribeEvent
- RecruitmentPost
- RafasConquest (PvE addon for Villager Recruits)
- ConquestGameRules
- Kit
- Organizations
- PoliticalStyle
- Call
- Phase
- net.minecraft.world.level.block.state.BlockState
- Ranks
- Cities (grid, plots, vanilla village buildings)
- BattleSite.Water
- ClientBarks
- BattleKeepInventory
- Build
- ItemFlags.java
- Ramming
- Politics
- DistantGeneration
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- WorldFeatures
- BattleSnapshot
- Shape
- Clash
- .sendBattleSpot
- SetSetting
- SeaCalm
- WarHorn

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 256 edges
2. `FieldBattle` - 149 edges
3. `FieldBlock` - 139 edges
4. `ConquestNetwork` - 108 edges
5. `FieldArmy` - 100 edges
6. `Lands` - 89 edges
7. `Politics` - 78 edges
8. `BaseMap` - 75 edges
9. `BattleScreen` - 60 edges
10. `Person` - 58 edges

## Surprising Connections (you probably didn't know these)
- `Roads and cities` --conceptually_related_to--> `City slots (walls, common, noble, landmarks)`  [INFERRED]
  docs/index.html → src/main/resources/rafasconquest_structures/README.txt
- `Battles (centre and two wings)` --conceptually_related_to--> `Battle Map web page`  [INFERRED]
  docs/index.html → src/main/resources/assets/rafasconquest/web/battle_map.html
- `Battle blocks (files, ranks, arm, duty, wing)` --conceptually_related_to--> `Battles (centre and two wings)`  [INFERRED]
  src/main/resources/assets/rafasconquest/web/battle_map.html → docs/index.html
- `Land titles (counties, duchies, kingdoms, empires)` --conceptually_related_to--> `Lone castles (small, medium, large, grand)`  [INFERRED]
  docs/index.html → src/main/resources/rafasconquest_structures/README.txt
- `Peerage (military and noble ladders)` --conceptually_related_to--> `Small Barracks (recruitment post, quest board)`  [INFERRED]
  docs/index.html → src/main/resources/rafasconquest_structures/README.txt

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

## Communities (305 total, 177 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.07
Nodes (5): FieldArmy, Event, FieldBattle, Order, Volley

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.07
Nodes (7): CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourPiece, PlaceholderPiece, RecruitmentPostPiece

### Community 3 - ".army"
Cohesion: 0.09
Nodes (4): Point, DoctrineTest, FieldBattleTest, ArmouryTest

### Community 4 - "CityTerrain"
Cohesion: 0.06
Nodes (21): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+13 more)

### Community 5 - "BattleScreen"
Cohesion: 0.09
Nodes (3): BattleScreen, Entry, Menu

### Community 6 - "DistantHorizonsQuality.java"
Cohesion: 0.08
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "CityLayout"
Cohesion: 0.07
Nodes (6): CityLayout, District, Site, SlopeTile, CityLayoutTest, CitySizesTest

### Community 8 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 10 - "CourtView"
Cohesion: 0.09
Nodes (4): Camera, CourtView, Figure, NationScreen

### Community 11 - "ConquestNetwork"
Cohesion: 0.08
Nodes (13): CityClose, CityView, ConquestNetwork, FigureStrike, HornOrders, MarkerSet, Place, RequestBaseMap (+5 more)

### Community 12 - "Quest"
Cohesion: 0.12
Nodes (11): Boards, Entry, View, Merit, Peer, Taken, Quest, Type (+3 more)

### Community 13 - "BaseMap"
Cohesion: 0.12
Nodes (5): MapPreview, BaseMap, Nation, BaseMapStore, Ready

### Community 14 - ".render"
Cohesion: 0.07
Nodes (11): CharacterScreen, Retainer, Spot, ClientPeerage, Banner, Portraits, Honour, Honours (+3 more)

### Community 15 - "CityVariant"
Cohesion: 0.17
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 16 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.06
Nodes (4): Character page, Court 3D scene (CourtView), Nation page, InventoryStatsButton

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.23
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 23 - "PveCommands.java"
Cohesion: 0.08
Nodes (30): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+22 more)

### Community 25 - ".plan"
Cohesion: 0.12
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.26
Nodes (4): BackBanners, Armoury, Outfit, Style

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.16
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 29 - "rafasconquest.map.BaseMap"
Cohesion: 0.08
Nodes (6): Army, FeudDuel, Goal, MusterBars, War, Wars

### Community 31 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.08
Nodes (6): BaseMapPart, FactionInfoReply, PeerageView, PoliticsView, Volleys, WorldStretch

### Community 32 - "Capital Layout & Seed Hash"
Cohesion: 0.10
Nodes (10): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, WorldSettings (+2 more)

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.10
Nodes (12): CityPlan, Kind, BARRACKS, CHURCH, NOBLE, PALACE, POST, WALL (+4 more)

### Community 36 - "Land"
Cohesion: 0.16
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 38 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.29
Nodes (7): Battle phases and days, Enlisted players, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Battle roles (frontline, bait, support, flanker, kill squad), War camps, War council, The war horn

### Community 39 - "list"
Cohesion: 0.07
Nodes (9): Countryside (hamlets, villages, watchtowers, castles), Demographics (population by culture and race), Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, Nations by zone, Races (Sunborn, Vinekin, Meadowfolk, ...), Roads (high, city, town roads; bridges; signposts), Seat cities (+1 more)

### Community 41 - "World map (CK3-style political map)"
Cohesion: 0.33
Nodes (7): Base map generated from seed, Command Table block, Faction Info screen, World map layers, Province size setting, Provinces (ProvinceMap), World map (CK3-style political map)

### Community 43 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.18
Nodes (6): BackBanner, Builder, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 44 - "Realm"
Cohesion: 0.11
Nodes (4): Hook, Realm, Succession, Taking

### Community 45 - "net.minecraft.core.Vec3i"
Cohesion: 0.08
Nodes (4): CharacterMarkers, Placed, StylePiece, CityPlacement

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.15
Nodes (11): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+3 more)

### Community 50 - "CityGridBuilder"
Cohesion: 0.09
Nodes (4): CityGridBuilder, Ground, Frame, Harbour

### Community 51 - "Settlement"
Cohesion: 0.12
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 52 - "BattleSounds"
Cohesion: 0.20
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 53 - "net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate"
Cohesion: 0.15
Nodes (5): DistantShips, Far, BattleKind, Part, ShipHulls

### Community 54 - "ConquestSavedData"
Cohesion: 0.07
Nodes (9): ResourceType, FOOD, Poi, PoiType, CAPITAL, CITY, FARM, StructureLink (+1 more)

### Community 55 - "net.minecraft.core.BlockPos"
Cohesion: 0.11
Nodes (5): Saved, WarCamp, CourtSet, Seat, Style

### Community 56 - "ConquestServer"
Cohesion: 0.12
Nodes (5): ConquestSavedData, CityBanners, ConquestServer, EconomyTick, PoiPrecomputer

### Community 57 - "View"
Cohesion: 0.12
Nodes (4): BoardView, GrandStrategyView, StatsView, TalkView

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 65 - "StyleLibrary"
Cohesion: 0.12
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - "CityGridBuilder.java"
Cohesion: 0.06
Nodes (8): CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 67 - "MarkerSpec"
Cohesion: 0.07
Nodes (5): MarkerRenderer, Palette, WallTemplates, MarkerBlockEntity, MarkerSpec

### Community 69 - "RafasConquest.java"
Cohesion: 0.05
Nodes (3): Battle map developer web page, Morale, rout and rally, RafasConquest

### Community 70 - ".of"
Cohesion: 0.18
Nodes (4): FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 71 - ".wallSegments"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 72 - "DistantOcclusion"
Cohesion: 0.14
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 73 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.13
Nodes (4): Card, Enlisted, Figure, Unit

### Community 75 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.18
Nodes (3): UnitLayer, RecruiterRenderer, TownspersonRenderer

### Community 76 - "PlaceMusicClient"
Cohesion: 0.19
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 77 - "Recruiter"
Cohesion: 0.11
Nodes (3): Barks, Ear, Recruiter

### Community 78 - "Faction"
Cohesion: 0.12
Nodes (5): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction, FactionInfoTest

### Community 79 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.28
Nodes (6): Boxes, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "Peerage"
Cohesion: 0.08
Nodes (12): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+4 more)

### Community 84 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.10
Nodes (6): Bark, MarkerView, CityManager, PlayerStats, View, PoliticsSync

### Community 86 - "Calibration"
Cohesion: 0.11
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.20
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "Job"
Cohesion: 0.10
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 90 - "DialogueScreen"
Cohesion: 0.05
Nodes (7): Cell, Terrain, DialogueScreen, Drop, MarkerScreen, SettingsScreen, StatsScreen

### Community 91 - ".view"
Cohesion: 0.18
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 92 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.11
Nodes (7): Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER, Side

### Community 93 - "ConquestContent.java"
Cohesion: 0.12
Nodes (3): ConquestContent, WorldMapItem, ConquestEntities

### Community 95 - ".generate"
Cohesion: 0.11
Nodes (5): BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 96 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 100 - ".aWoodNearAWingHidesAnAmbushThatStaysPutUntilItStrikes"
Cohesion: 0.10
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 103 - "ProvinceMap"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 105 - ".player"
Cohesion: 0.14
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 106 - "Strategy"
Cohesion: 0.17
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 107 - "DistantArmyRenderer"
Cohesion: 0.11
Nodes (3): DistantArmyRenderer, Figure, Hull

### Community 108 - "net.minecraft.world.level.block.Rotation"
Cohesion: 0.18
Nodes (3): PlacedWall, GoldChurch, Layout

### Community 109 - "Quests"
Cohesion: 0.13
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 110 - "Rank"
Cohesion: 0.11
Nodes (12): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+4 more)

### Community 111 - "DistantArmyRenderer.java"
Cohesion: 0.06
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 112 - "RoadNetwork"
Cohesion: 0.14
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 113 - "City"
Cohesion: 0.12
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 114 - "Trait"
Cohesion: 0.14
Nodes (6): Officers, Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 115 - "PoiPrecomputer.java"
Cohesion: 0.07
Nodes (7): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, PoliticalMap, FactionInfo

### Community 117 - "Wars (daily nation decisions)"
Cohesion: 0.22
Nodes (7): Battles (prototype), Blocks of 20 soldiers, Envelopment, Heralds (daily news), Land grants (seven ways), Mustering (offensive and ceremonial), Wars (daily nation decisions)

### Community 118 - ".view"
Cohesion: 0.13
Nodes (5): GrandStrategy, View, QuestProgress, Retinues, RetinuesTest

### Community 121 - "MapContext"
Cohesion: 0.17
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 122 - "BattlePlan"
Cohesion: 0.06
Nodes (9): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, WarPlan (+1 more)

### Community 124 - "MapView"
Cohesion: 0.29
Nodes (3): Hit, LabelLayer, LabelShape

### Community 125 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.18
Nodes (5): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer

### Community 126 - ".onLevelLoad"
Cohesion: 0.15
Nodes (3): ConquestConfig, CityIndex, ConquestRuntime

### Community 128 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 129 - "org.joml.Matrix4f"
Cohesion: 0.17
Nodes (3): DistantDepth, Arrow, ArrowVolleys

### Community 130 - "Townsperson"
Cohesion: 0.12
Nodes (5): Walk, DistrictKey, Townsfolk, Townsperson, StreetWalksTest

### Community 132 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 133 - "City"
Cohesion: 0.09
Nodes (9): Countryside, Plan, City, Grid, SeatCities, CountrysideTest, World, SeatCitiesTest (+1 more)

### Community 136 - "BattleShips"
Cohesion: 0.14
Nodes (5): BattleShips, Helm, Ram, SableLink, SableShips

### Community 137 - "Homeland"
Cohesion: 0.10
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 138 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 139 - "FormationSnapshot"
Cohesion: 0.12
Nodes (12): FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN (+4 more)

### Community 141 - "FieldBlock"
Cohesion: 0.05
Nodes (15): BlockGrid, Target, FieldBlock, ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN (+7 more)

### Community 143 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 146 - "Lands"
Cohesion: 0.11
Nodes (4): Fort, Lands, Stronghold, Claim

### Community 148 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.29
Nodes (7): Characters (holders, skills, prowess, traits), Cultures (seven homelands), Epic Knights optional armour (Armoury), Faiths and cults, Martial titles (Sword Saint, Sword King, Sword God), Organizations, Sword sanctums

### Community 149 - "Ranks (Soldier to Grand Marshal)"
Cohesion: 0.50
Nodes (5): Gold church, Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter

### Community 152 - "Arm"
Cohesion: 0.07
Nodes (17): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, BattleType, AMPHIBIOUS, LAND (+9 more)

### Community 155 - "UnitOrder"
Cohesion: 0.12
Nodes (16): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+8 more)

### Community 156 - ".plan"
Cohesion: 0.13
Nodes (7): Centre, Demographics, Share, Faith, Bond, RealmPlanner, Seat

### Community 163 - "Lands"
Cohesion: 0.12
Nodes (4): Feud, Lord, Feuds, Front

### Community 167 - "UnitInfo"
Cohesion: 0.22
Nodes (3): UnitInfo, UnitsRequest, UnitsState

### Community 206 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 217 - ".pack"
Cohesion: 0.32
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 218 - "ConquestWorldScreen"
Cohesion: 0.27
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 219 - "RecruitsArrowCleanerMixin.java"
Cohesion: 0.17
Nodes (4): CityVegetationMixin, RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 240 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 249 - "Kit"
Cohesion: 0.14
Nodes (13): Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD, HEAVY_INFANTRY (+5 more)

### Community 252 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 257 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (6): Sea, CommandTableBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, MarkerBlock

### Community 262 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.22
Nodes (5): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), TickWeave experiment

### Community 286 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 288 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 289 - "Politics"
Cohesion: 0.09
Nodes (6): Event, Muster, CEREMONIAL, OFFENSIVE, Politics, PoliticsData

### Community 293 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.33
Nodes (4): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender

### Community 297 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 298 - "Clash"
Cohesion: 0.32
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 301 - "SeaCalm"
Cohesion: 0.23
Nodes (3): Depths, SeaCalm, SwellTest

## Ambiguous Edges - Review These
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **335 isolated node(s):** `BEARER`, `GENERAL`, `GUARD`, `LEADER`, `SOLDIER` (+330 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1005 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **177 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `org.jetbrains.annotations.Nullable`, `Phase`, `net.minecraft.world.level.ChunkPos`, `.passOnOrders`, `net.minecraft.world.item.ItemStack`, `UnitOrder`, `WarCouncil`, `rafasconquest.map.BaseMap`, `PveCommands Registration`, `.officers`, `net.minecraft.server.level.ServerLevel`, `rafasconquest.faction.Faction`, `Shape`, `org.jetbrains.annotations.Nullable`, `net.minecraft.core.BlockPos`, `.second`, `RafasConquest.java`, `net.minecraft.world.phys.Vec3`, `net.minecraft.server.level.ServerPlayer`, `Nation Rows`, `Calibration`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `UnitMode`, `.inCity`, `.aWoodNearAWingHidesAnAmbushThatStaysPutUntilItStrikes`, `.enlist`, `net.neoforged.bus.api.SubscribeEvent`, `Trait`, `net.minecraft.world.entity.Entity`?**
  _High betweenness centrality (0.086) - this node is a cross-community bridge._
- **What connects `BEARER`, `GENERAL`, `GUARD` to the rest of the system?**
  _335 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.06545454545454546 - nodes in this community are weakly interconnected._
- **Why does `ConquestNetwork` connect `ConquestNetwork` to `net.minecraft.world.level.block.state.BlockState`, `BattleScreen`, `RafasConquest.java`, `list`, `BattleSnapshot`, `UnitInfo`, `.send`, `net.neoforged.neoforge.network.handling.IPayloadContext`, `.sendBattleSpot`, `SetSetting`, `net.minecraft.client.gui.GuiGraphics`, `net.minecraft.server.level.ServerPlayer`, `org.jetbrains.annotations.Nullable`, `CityManagerScreen`, `View`, `BoardScreen`, `net.minecraft.network.FriendlyByteBuf`?**
  _High betweenness centrality (0.034) - this node is a cross-community bridge._
- **Should `RecruitsBattle` be split into smaller, more focused modules?**
  _Cohesion score 0.061016949152542375 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._