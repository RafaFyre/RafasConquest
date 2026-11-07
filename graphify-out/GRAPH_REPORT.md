# Graph Report - RafasConquest  (2026-11-07)

## Corpus Check
- 415 files · ~326,655 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 115 file(s) not represented in the graph (top: .nbt 84, .toml 14, (none) 3)

## Summary
- 5706 nodes · 18110 edges · 298 communities (116 shown, 182 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2309 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `1ab664c6`
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
- Shape
- NationScreen
- ConquestSavedData
- Peerage
- BaseMap
- .render
- net.minecraft.world.level.block.Rotation
- net.minecraft.client.gui.screens.Screen
- Battle Map Web Page & Site
- Person
- RafasConquest.java
- PoliticsTest
- PveCommands.java
- BattleScreen.java
- .plan
- CityGridBuilder
- .shift
- WarCouncil
- rafasconquest.map.BaseMap
- ClientPolitics
- ConquestNetwork
- WorldSettings
- PveCommands Registration
- School
- CityPlan
- Land
- ConquestEvents.java
- Characters (holders, skills, prowess, traits)
- rafasconquest.faction.Faction
- Politics.Front
- org.joml.Matrix4f
- .world
- net.minecraft.core.Vec3i
- Personalities
- SeaLanes
- MouthLayer.java
- NobleAnnex
- CountryStructure
- BattleSounds
- net.minecraft.world.item.DyeColor
- PoiPrecomputer
- net.minecraft.world.phys.Vec3
- net.minecraft.server.level.ServerLevel
- map
- Faction Map View
- net.minecraft.server.level.ServerLevel
- SeaCalm
- .passOnOrders
- TitleMapTest
- .measure
- StyleLibrary
- Build
- net.minecraft.nbt.CompoundTag
- RafasConquest.java
- CourtView.java
- .wallSegments
- DistantOcclusion
- Barracks
- MapLands
- RoadNetwork
- PlaceMusicClient
- net.minecraft.world.level.Level
- .send
- rafasconquest.battle.FormationSnapshot
- .describe
- Ownership & Territory
- .choose
- CityVariant
- Nation Rows
- Arm
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- .view
- DialogueScreen
- .view
- Arm
- SableShips
- BoardScreen
- .generate
- ProvinceMap
- WaterSwell
- FactionInfoScreen
- .facingX
- RoadFeature
- CityStyles
- HorizontalStretch
- Succession
- ClientModEvents.java
- DistantArmyRenderer
- Swell
- Quests
- Kit
- DistantArmyRenderer.java
- CourtView
- Trait
- org.jetbrains.annotations.Nullable
- Kind
- .render
- .view
- GrandStrategyScreen
- PoliticalStyle
- BattlePlan
- Grid
- RafasConquest (PvE addon for Villager Recruits)
- .onLevelLoad
- Realm
- Task
- Townsperson
- MapContext
- SeatCities
- OrganizationScreen
- BattleShips
- MarkerScreen
- .boundsAt
- BattleKeepInventory
- .onLogout
- BlockGrid
- ClientScreens
- CityTerrainPiece
- ClientEvents
- Lands
- Plan
- net.minecraft.resources.ResourceLocation
- CLAUDE.md
- Organizations
- .build
- .plan
- Terrain
- City
- Lands
- Enlisted
- Job
- Small Cluster 170
- ClientBarks
- UnitOrder
- RecruitsBattle.java
- BattleType
- Kind
- .pack
- Faction
- RecruitsArrowCleanerMixin.java
- .touches
- CourtSet
- .conquer
- StatsScreen
- .enlist
- CityPlacement
- .enlist
- net.minecraft.server.level.ServerPlayer
- net.neoforged.bus.api.SubscribeEvent
- Settlement
- PlaceholderPiece
- ConquestGameRules
- BattleRole
- ConquestConfig
- .start
- .walks
- Call
- Race
- net.minecraft.world.level.block.state.BlockState
- Kind
- .ships
- ShipKind
- net.minecraft.world.item.ItemStack
- Ramming
- FormationSnapshot
- RankKits
- com.talhanation.recruits.entities.AbstractRecruitEntity

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 256 edges
2. `FieldBattle` - 149 edges
3. `FieldBlock` - 139 edges
4. `ConquestNetwork` - 107 edges
5. `FieldArmy` - 100 edges
6. `Lands` - 89 edges
7. `BaseMap` - 78 edges
8. `Politics` - 78 edges
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

## Communities (298 total, 182 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.05
Nodes (7): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 1 - "RecruitsBattle"
Cohesion: 0.07
Nodes (3): Figure, Landing, RecruitsBattle

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.08
Nodes (6): BarracksPiece, CityRoadPiece, CountryPiece, GoldChurchPiece, HarbourPiece, NoblePathPiece

### Community 3 - ".army"
Cohesion: 0.09
Nodes (3): DoctrineTest, FieldBattleTest, RetinuesTest

### Community 4 - "CityTerrain"
Cohesion: 0.07
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.09
Nodes (4): BattleScreen, Entry, Menu, UnitInfo

### Community 6 - "DistantHorizonsQuality.java"
Cohesion: 0.08
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "CityLayout"
Cohesion: 0.07
Nodes (6): CityLayout, District, Site, SlopeTile, CityLayoutTest, CitySizesTest

### Community 8 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 9 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 11 - "ConquestSavedData"
Cohesion: 0.09
Nodes (4): ConquestSavedData, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 12 - "Peerage"
Cohesion: 0.08
Nodes (10): Boards, Entry, View, Merit, Peer, Peerage, Taken, PeerageSync (+2 more)

### Community 13 - "BaseMap"
Cohesion: 0.13
Nodes (5): MapPreview, BaseMap, Nation, BaseMapStore, Ready

### Community 14 - ".render"
Cohesion: 0.14
Nodes (4): CharacterScreen, Spot, Banner, Portraits

### Community 15 - "net.minecraft.world.level.block.Rotation"
Cohesion: 0.16
Nodes (3): PlacedWall, GoldChurch, Layout

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.23
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 23 - "PveCommands.java"
Cohesion: 0.08
Nodes (29): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+21 more)

### Community 25 - ".plan"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "CityGridBuilder"
Cohesion: 0.12
Nodes (3): CityGridBuilder, Ground, Platform

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.15
Nodes (7): Facts, Officer, Said, Talk, WarCouncil, Option, WarCouncilPagesTest

### Community 29 - "rafasconquest.map.BaseMap"
Cohesion: 0.07
Nodes (10): Army, Clash, Kind, DUEL, SKIRMISH, FeudDuel, Goal, PoliticsSync (+2 more)

### Community 31 - "ConquestNetwork"
Cohesion: 0.03
Nodes (29): Bark, BaseMapPart, BattleSnapshot, BattleSpot, BoardView, CityClose, CityView, ConquestNetwork (+21 more)

### Community 32 - "WorldSettings"
Cohesion: 0.10
Nodes (9): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, SeedHash, WorldSettings, CapitalRelocationTest (+1 more)

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.12
Nodes (3): CityPlan, Piece, NobleDistrictTest

### Community 36 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 37 - "ConquestEvents.java"
Cohesion: 0.06
Nodes (8): CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 39 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.04
Nodes (40): Ambushes from woods, Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Character page, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings) (+32 more)

### Community 44 - ".world"
Cohesion: 0.14
Nodes (3): Retainer, RealmPlannerTest, World

### Community 45 - "net.minecraft.core.Vec3i"
Cohesion: 0.09
Nodes (3): CharacterMarkers, Placed, StylePiece

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 52 - "BattleSounds"
Cohesion: 0.20
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 53 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.33
Nodes (3): BattleKind, Part, ShipHulls

### Community 55 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.09
Nodes (3): WarCamp, WarHorn, ItemFlags

### Community 56 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.13
Nodes (4): BarracksPeople, CityGuards, ConquestServer, StructureLink

### Community 60 - "SeaCalm"
Cohesion: 0.16
Nodes (4): Sea, Depths, SeaCalm, SwellTest

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 65 - "StyleLibrary"
Cohesion: 0.11
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 67 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.07
Nodes (5): MarkerRenderer, Palette, WallTemplates, MarkerBlockEntity, MarkerSpec

### Community 69 - "RafasConquest.java"
Cohesion: 0.04
Nodes (4): Battle map developer web page, Morale, rout and rally, StreetWalks, RafasConquest

### Community 70 - "CourtView.java"
Cohesion: 0.08
Nodes (5): FactionBanner, Layer, FactionBannerNbt, PoliticalMap, CityBanners

### Community 71 - ".wallSegments"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 72 - "DistantOcclusion"
Cohesion: 0.11
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 75 - "RoadNetwork"
Cohesion: 0.14
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 76 - "PlaceMusicClient"
Cohesion: 0.19
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 77 - "net.minecraft.world.level.Level"
Cohesion: 0.06
Nodes (10): Gold church, Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock (+2 more)

### Community 78 - ".send"
Cohesion: 0.08
Nodes (12): BackBanner, Builder, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel (+4 more)

### Community 79 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.28
Nodes (6): Boxes, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 81 - ".describe"
Cohesion: 0.09
Nodes (10): ClientPeerage, Honour, Honours, Stat, DIPLOMACY, INTRIGUE, LEARNING, MARTIAL (+2 more)

### Community 82 - "Ownership & Territory"
Cohesion: 0.20
Nodes (5): Candidate, OwnershipResolver, Claim, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.13
Nodes (11): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+3 more)

### Community 84 - "CityVariant"
Cohesion: 0.15
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 86 - "Arm"
Cohesion: 0.14
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.05
Nodes (30): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+22 more)

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - ".view"
Cohesion: 0.07
Nodes (13): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+5 more)

### Community 91 - ".view"
Cohesion: 0.22
Nodes (3): Talk, View, DialogueTest

### Community 92 - "Arm"
Cohesion: 0.12
Nodes (11): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, FieldRules, Rates, Side (+3 more)

### Community 95 - ".generate"
Cohesion: 0.07
Nodes (13): WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry, Homeland, DESERT, JUNGLE (+5 more)

### Community 96 - "ProvinceMap"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 100 - ".facingX"
Cohesion: 0.09
Nodes (8): Point, Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 102 - "CityStyles"
Cohesion: 0.22
Nodes (3): Building, CityStyles, Houses

### Community 103 - "HorizontalStretch"
Cohesion: 0.10
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 107 - "DistantArmyRenderer"
Cohesion: 0.09
Nodes (5): DistantArmyRenderer, Figure, Hull, DistantShips, Far

### Community 109 - "Quests"
Cohesion: 0.13
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 110 - "Kit"
Cohesion: 0.14
Nodes (13): Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD, HEAVY_INFANTRY (+5 more)

### Community 111 - "DistantArmyRenderer.java"
Cohesion: 0.10
Nodes (4): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), DistantDepth

### Community 113 - "CourtView"
Cohesion: 0.18
Nodes (3): Camera, CourtView, Figure

### Community 114 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 115 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.08
Nodes (12): TerritoryMap, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, FactionInfo (+4 more)

### Community 116 - "Kind"
Cohesion: 0.33
Nodes (6): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER

### Community 117 - ".render"
Cohesion: 0.12
Nodes (6): LegendEntry, MapLayer, Symbol, RoadLayer, TerritoryLayer, TitleLayer

### Community 118 - ".view"
Cohesion: 0.10
Nodes (10): GrandStrategy, View, Names, NobleTitle, COUNT, KNIGHT, NONE, PlayerStats (+2 more)

### Community 122 - "BattlePlan"
Cohesion: 0.06
Nodes (8): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, BattlePlanTest

### Community 124 - "Grid"
Cohesion: 0.20
Nodes (5): Countryside, Plan, Grid, CountrysideTest, World

### Community 125 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.17
Nodes (8): City wall, banners and wall guards, Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), Package layout (core, data, worldgen, sim, bridge), RafasConquest (PvE addon for Villager Recruits), Recruits faction registration (bridge package), Villager Recruits

### Community 128 - "Realm"
Cohesion: 0.20
Nodes (4): Kind, FRIEND, RIVAL, Realm

### Community 129 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 130 - "Townsperson"
Cohesion: 0.10
Nodes (6): Walk, Barks, Ear, DistrictKey, Townsfolk, Townsperson

### Community 132 - "MapContext"
Cohesion: 0.10
Nodes (9): BaseMapLayer, Hit, LabelLayer, MapContext, MapView, Place, PlaceMenuLayer, PoiIconLayer (+1 more)

### Community 133 - "SeatCities"
Cohesion: 0.12
Nodes (3): SeatCities, SeatCitiesTest, World

### Community 136 - "BattleShips"
Cohesion: 0.23
Nodes (4): BattleShips, Helm, Ram, SableLink

### Community 138 - ".boundsAt"
Cohesion: 0.15
Nodes (6): Bounds, DistanceScaling, OriginRoller, Roll, ProvinceNames, DistanceScalingTest

### Community 140 - ".onLogout"
Cohesion: 0.12
Nodes (3): BackBanners, BattleSpotRing, ClientMapCache

### Community 146 - "Lands"
Cohesion: 0.07
Nodes (8): Lands, Stronghold, Claim, Muster, CEREMONIAL, OFFENSIVE, Politics, PoliticsData

### Community 148 - "Plan"
Cohesion: 0.08
Nodes (10): PlaceMusic, Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL (+2 more)

### Community 149 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.23
Nodes (3): UnitLayer, RecruiterRenderer, TownspersonRenderer

### Community 152 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 156 - ".plan"
Cohesion: 0.14
Nodes (6): Demographics, Share, Faith, Bond, RealmPlanner, Seat

### Community 160 - "City"
Cohesion: 0.09
Nodes (16): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+8 more)

### Community 163 - "Lands"
Cohesion: 0.12
Nodes (4): Feud, Lord, Feuds, Front

### Community 164 - "Enlisted"
Cohesion: 0.10
Nodes (9): Card, Enlisted, Unit, UnitMode, CHARGE, FOLLOW, HOLD, MOVE (+1 more)

### Community 167 - "Job"
Cohesion: 0.22
Nodes (6): Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY, SHIELDMAN

### Community 178 - "UnitOrder"
Cohesion: 0.12
Nodes (16): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+8 more)

### Community 181 - "RecruitsBattle.java"
Cohesion: 0.05
Nodes (7): /pve debug recruits, Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Enlisted players, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Battle roles (frontline, bait, support, flanker, kill squad), The war horn

### Community 204 - "BattleType"
Cohesion: 0.17
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 206 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 217 - ".pack"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 219 - "RecruitsArrowCleanerMixin.java"
Cohesion: 0.17
Nodes (4): CityVegetationMixin, RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 223 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 226 - ".enlist"
Cohesion: 0.06
Nodes (10): GeneralTask, Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK (+2 more)

### Community 249 - "BattleRole"
Cohesion: 0.25
Nodes (6): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT

### Community 252 - ".start"
Cohesion: 0.12
Nodes (4): Api, DistantGeneration, BattleSite.Water, FactionBannerTest

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Race"
Cohesion: 0.18
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 257 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.08
Nodes (3): Saved, CommandTableBlock, MarkerBlock

### Community 259 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 267 - "ShipKind"
Cohesion: 0.33
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 287 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.19
Nodes (3): Armoury, Outfit, Style

### Community 288 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 295 - "FormationSnapshot"
Cohesion: 0.09
Nodes (13): FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN (+5 more)

### Community 300 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.12
Nodes (8): Officers, Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER, Side

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **335 isolated node(s):** `CARVE`, `FILL`, `VALLEY`, `NORTH`, `EAST` (+330 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1020 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **182 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `org.jetbrains.annotations.Nullable`, `net.minecraft.world.level.ChunkPos`, `.army`, `Shape`, `Peerage`, `ClientEvents`, `PveCommands.java`, `WarCouncil`, `rafasconquest.map.BaseMap`, `ConquestNetwork`, `PveCommands Registration`, `Enlisted`, `rafasconquest.faction.Faction`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `UnitOrder`, `RecruitsBattle.java`, `net.minecraft.world.item.DyeColor`, `net.minecraft.world.phys.Vec3`, `map`, `net.minecraft.server.level.ServerLevel`, `.passOnOrders`, `RafasConquest.java`, `Arm`, `Doctrine`, `.enlist`, `.enlist`, `net.minecraft.server.level.ServerPlayer`, `.start`?**
  _High betweenness centrality (0.052) - this node is a cross-community bridge._
- **What connects `CARVE`, `FILL`, `VALLEY` to the rest of the system?**
  _335 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.049845170312656076 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `Realm`, `MapContext`, `OrganizationScreen`, `NationScreen`, `.onLogout`, `.render`, `net.minecraft.client.gui.screens.Screen`, `arraylist`, `assertequals`, `.plan`, `WarCouncil`, `Terrain`, `net.minecraft.world.item.ItemStack`, `City`, `ClientPolitics`, `School`, `rafasconquest.map.BaseMap`, `ConquestEvents.java`, `CountryStructure`, `PoiPrecomputer`, `net.minecraft.server.level.ServerLevel`, `TitleMapTest`, `CourtView.java`, `MapLands`, `Nation Rows`, `.generate`, `ProvinceMap`, `RoadFeature`, `Settlement`, `Kit`, `org.jetbrains.annotations.Nullable`, `GrandStrategyScreen`, `PoliticalStyle`, `Grid`, `.onLevelLoad`?**
  _High betweenness centrality (0.036) - this node is a cross-community bridge._
- **Should `RecruitsBattle` be split into smaller, more focused modules?**
  _Cohesion score 0.0672316384180791 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._