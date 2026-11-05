# Graph Report - RafasConquest  (2026-11-05)

## Corpus Check
- 354 files · ~261,549 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 89 file(s) not represented in the graph (top: .nbt 76, (none) 3, .mcfunction 2)

## Summary
- 4772 nodes · 14868 edges · 230 communities (105 shown, 125 thin omitted)
- Extraction: 88% EXTRACTED · 12% INFERRED · 0% AMBIGUOUS · INFERRED: 1800 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `6bf05412`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- Shared Java Utilities & Tests
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- Quest
- WorldSettings
- CityPlacement
- net.minecraft.world.level.block.state.BlockState
- .block
- Politics
- list
- Lands
- ProvinceMap
- RoadNetwork
- org.jetbrains.annotations.Nullable
- Battle Map Web Page & Site
- Characters (holders, skills, prowess, traits)
- Recruiter
- BattleMap.java
- .simulate
- .postProcess
- Commands Guide
- CityManagerScreen
- Nations Planning
- net.minecraft.world.item.ItemStack
- net.minecraft.nbt.CompoundTag
- War Council & War Plans
- Wars Service
- Roads
- Builder
- Capital Layout & Seed Hash
- PveCommands Registration
- .passOnOrders
- Countryside
- City
- .plan
- PoliticalStyle
- Characters (holders, skills, prowess, traits)
- net.minecraft.server.level.ServerLevel
- FactionInfoScreen
- ArrowVolleys
- World Stretch Screen
- .place
- RecruitsFactions.java
- Personalities
- DayClockTest
- War
- Recruiter.java
- .render
- Base Map Generator
- StyleLibrary
- SimpleChannel.java
- Culture
- net.minecraft.core.BlockPos
- .of
- CityGridBuilder
- Faction Map View
- .drawEmpire
- ConquestServer
- .build
- Townsperson
- Title Map
- Field Rules & Arms
- .render
- DistantArmyRenderer.java
- Distant Horizons Armies
- Faction
- Battle Calibration
- .postChunk
- FormationSnapshot
- Settlement
- RafasConquest.java
- Map Lands
- RecruitsBattle
- rafasconquest.map.BaseMap
- View
- BattleKeepInventory.java
- City Walls
- Ownership & Territory
- Peerage
- Realm
- Nation Rows
- CourtSet
- Battle Sounds
- Rank
- BaseMap
- Type
- .complete
- Point
- Stats & Settings Screens
- Kit
- Distant Army Renderer
- RecruiterScreen
- BattleKeepInventory.java
- MapContext
- City Stats
- Battle Site
- Realm Planner Tests
- MarkerScreen
- net.neoforged.bus.api.SubscribeEvent
- Poi
- Magic Schools
- PoiPrecomputer.java
- WarPlan
- Kind
- PoliticsTest
- Client Politics
- Task
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- GrandStrategyScreen
- net.minecraft.core.BlockPos
- .view
- Land
- Distance Scaling
- BaseMapPart
- BattlePlan
- ConquestRuntime
- BoardScreen
- .plan
- ConquestGameRules
- Homeland
- ItemFlags.java
- net.minecraft.world.level.block.Rotation
- .nearest
- .render
- Trait
- SeatCitiesTest
- .player
- Horn Calls
- ConquestConfig
- FormationSnapshot
- Organization Screen
- PoliticsData
- NationScreen
- Role
- net.minecraft.server.level.ServerLevel
- FigureStrike
- Duty
- WarCamp.java
- PoliticsView
- Volleys
- WorldStretch
- CLAUDE.md
- BattleSite.Water
- ConquestNetwork
- BlockGrid
- WarHorn
- ConquestSavedData
- net.minecraft.network.FriendlyByteBuf
- ConquestEvents
- BattleRole
- net.minecraft.client.gui.GuiGraphics
- Small Cluster 170
- Role
- ClientScreens
- BackBannerLayer.java
- .sender
- UnitMode
- DistantHorizonsQuality.java
- Phase
- .onLogout

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 246 edges
2. `FieldBattle` - 122 edges
3. `FieldBlock` - 118 edges
4. `ConquestNetwork` - 105 edges
5. `BaseMap` - 94 edges
6. `FieldArmy` - 94 edges
7. `Point` - 70 edges
8. `ConquestSavedData` - 60 edges
9. `BattleScreen` - 60 edges
10. `Politics` - 51 edges

## Surprising Connections (you probably didn't know these)
- `Roads and cities` --conceptually_related_to--> `City slots (walls, common, noble, landmarks)`  [INFERRED]
  docs/index.html → src/main/resources/rafasconquest_structures/README.txt
- `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)` --conceptually_related_to--> `/pve debug battle simulate`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf → RafasConquest-Commands.pdf
- `Technical architecture components` --semantically_similar_to--> `Real vs calculated battles (player within 400 blocks)`  [INFERRED] [semantically similar]
  Recruits PvE Addon — Design Doc.pdf → RafasConquest-Commands.pdf
- `Battles (centre and two wings)` --conceptually_related_to--> `Battle Map web page`  [INFERRED]
  docs/index.html → src/main/resources/assets/rafasconquest/web/battle_map.html
- `Battle blocks (files, ranks, arm, duty, wing)` --conceptually_related_to--> `Battles (centre and two wings)`  [INFERRED]
  src/main/resources/assets/rafasconquest/web/battle_map.html → docs/index.html

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

## Communities (230 total, 125 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.06
Nodes (9): Point, FieldArmy, Event, FieldBattle, Order, Outcome, Volley, FieldBlock (+1 more)

### Community 1 - "Shared Java Utilities & Tests"
Cohesion: 0.08
Nodes (4): ArmouryTest, RetinuesTest, NobleDistrictTest, PlacementOffsetsTest

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.07
Nodes (8): StylePiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourPiece, PlaceholderPiece, RecruitmentPostPiece

### Community 4 - "CityTerrain"
Cohesion: 0.05
Nodes (22): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+14 more)

### Community 5 - "BattleScreen"
Cohesion: 0.06
Nodes (20): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+12 more)

### Community 6 - "Quest"
Cohesion: 0.19
Nodes (5): Boards, Entry, View, Taken, Quest

### Community 7 - "WorldSettings"
Cohesion: 0.10
Nodes (5): CityLayout, District, Site, WorldSettings, CityLayoutTest

### Community 9 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (6): CommandTableBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, WorldMapItem, MarkerBlock

### Community 11 - "Politics"
Cohesion: 0.13
Nodes (6): Event, Front, Muster, CEREMONIAL, OFFENSIVE, Politics

### Community 12 - "list"
Cohesion: 0.07
Nodes (5): Countryside (hamlets, villages, watchtowers, castles), Demographics (population by culture and race), Mantra schools and dojos, Roads (high, city, town roads; bridges; signposts), Dojos

### Community 13 - "Lands"
Cohesion: 0.18
Nodes (3): Lands, Stronghold, Claim

### Community 14 - "ProvinceMap"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 15 - "RoadNetwork"
Cohesion: 0.15
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.10
Nodes (18): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Characters (holders, skills, prowess, traits), Cultures (seven homelands), Envelopment, Epic Knights optional armour (Armoury), Faiths and cults (+10 more)

### Community 19 - "Recruiter"
Cohesion: 0.12
Nodes (6): Recruiter, Offer, Recruitment, Option, Talk, View

### Community 23 - "Commands Guide"
Cohesion: 0.09
Nodes (30): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+22 more)

### Community 25 - "Nations Planning"
Cohesion: 0.17
Nodes (6): Nation, Nations, Plan, Seats, NationsTest, World

### Community 26 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.20
Nodes (3): Armoury, Outfit, Style

### Community 27 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.05
Nodes (11): MarkerRenderer, Palette, WallTemplates, MarkerBlockEntity, MarkerSpec, Build, CASTLE, HAMLET (+3 more)

### Community 28 - "War Council & War Plans"
Cohesion: 0.18
Nodes (4): Facts, Officer, Talk, WarCouncil

### Community 29 - "Wars Service"
Cohesion: 0.17
Nodes (3): MusterBars, PoliticsSync, Wars

### Community 30 - "Roads"
Cohesion: 0.10
Nodes (15): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+7 more)

### Community 31 - "Builder"
Cohesion: 0.13
Nodes (6): BackBanner, Builder, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 32 - "Capital Layout & Seed Hash"
Cohesion: 0.10
Nodes (9): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, CapitalRelocationTest (+1 more)

### Community 34 - ".passOnOrders"
Cohesion: 0.07
Nodes (7): Figure, Live, Shape, LINE, SQUARE, V, WEDGE

### Community 35 - "Countryside"
Cohesion: 0.12
Nodes (11): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+3 more)

### Community 36 - "City"
Cohesion: 0.12
Nodes (7): City, SeatCities, Tier, COUNTY, DUCHY, EMPIRE, KINGDOM

### Community 37 - ".plan"
Cohesion: 0.16
Nodes (4): Faith, Bond, RealmPlanner, Seat

### Community 38 - "PoliticalStyle"
Cohesion: 0.19
Nodes (3): PoliticalMap, LabelShape, PoliticalStyle

### Community 39 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.09
Nodes (18): Ambushes from woods, Base map generated from seed, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Command Table block, Faction Info screen, Deploying to the land (GroundPlan), Land titles (county, duchy, kingdom, empire) (+10 more)

### Community 42 - "ArrowVolleys"
Cohesion: 0.15
Nodes (4): DistantDepth, Arrow, ArrowVolleys, Figure

### Community 43 - "World Stretch Screen"
Cohesion: 0.12
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "DayClockTest"
Cohesion: 0.25
Nodes (3): DayClock, Step, DayClockTest

### Community 48 - "War"
Cohesion: 0.14
Nodes (3): Goal, War, Politics.Front

### Community 49 - "Recruiter.java"
Cohesion: 0.18
Nodes (5): Gold church, Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter

### Community 50 - ".render"
Cohesion: 0.13
Nodes (5): CharacterScreen, Retainer, Spot, Banner, Portraits

### Community 51 - "Base Map Generator"
Cohesion: 0.07
Nodes (7): ProvinceNames, WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 52 - "StyleLibrary"
Cohesion: 0.05
Nodes (20): BlockPacker, Placed, Rect, Size, CityPlan, Kind, BARRACKS, CHURCH (+12 more)

### Community 53 - "SimpleChannel.java"
Cohesion: 0.14
Nodes (3): RequestFactionInfo, Msg, SimpleChannel

### Community 54 - "Culture"
Cohesion: 0.18
Nodes (5): Culture, Organizations, Sanctum, Hold, OrganizationsTest

### Community 56 - ".of"
Cohesion: 0.22
Nodes (3): FactionBanner, Layer, FactionBannerTest

### Community 57 - "CityGridBuilder"
Cohesion: 0.07
Nodes (10): Houses, CityGridBuilder, Ground, PlacedWall, CityVariant, DESERT, PLAINS, SAVANNA (+2 more)

### Community 60 - "ConquestServer"
Cohesion: 0.11
Nodes (4): CityBanners, ConquestServer, StructureLink, PoiPrecomputer

### Community 61 - ".build"
Cohesion: 0.09
Nodes (4): Frame, Harbour, CitySizesTest, StreetWalksTest

### Community 62 - "Townsperson"
Cohesion: 0.09
Nodes (6): RecruiterRenderer, TownspersonRenderer, Walk, DistrictKey, Townsfolk, Townsperson

### Community 63 - "Title Map"
Cohesion: 0.17
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Field Rules & Arms"
Cohesion: 0.11
Nodes (5): FieldRules, Side, FLANK, FRONT, REAR

### Community 65 - ".render"
Cohesion: 0.09
Nodes (12): ClientPeerage, Names, Honour, Honours, Person, Stat, DIPLOMACY, INTRIGUE (+4 more)

### Community 66 - "DistantArmyRenderer.java"
Cohesion: 0.12
Nodes (4): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), BattleSpotRing

### Community 68 - "Faction"
Cohesion: 0.15
Nodes (3): Faction, ResourceType, FOOD

### Community 69 - "Battle Calibration"
Cohesion: 0.19
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 71 - "FormationSnapshot"
Cohesion: 0.25
Nodes (3): Positions, Sprite, FormationSnapshotTest

### Community 72 - "Settlement"
Cohesion: 0.16
Nodes (3): Settlement, CountryIndex, CountryStructure

### Community 73 - "RafasConquest.java"
Cohesion: 0.07
Nodes (4): ConquestContent, StreetWalks, ConquestEntities, RafasConquest

### Community 74 - "Map Lands"
Cohesion: 0.10
Nodes (3): Cell, Terrain, MapLands

### Community 76 - "RecruitsBattle"
Cohesion: 0.03
Nodes (9): Api, DistantGeneration, Card, Enlisted, GeneralTask, RecruitsBattle, Side, TickTimes (+1 more)

### Community 77 - "rafasconquest.map.BaseMap"
Cohesion: 0.08
Nodes (4): Character page, Court 3D scene (CourtView), Nation page, InventoryStatsButton

### Community 78 - "View"
Cohesion: 0.15
Nodes (3): BoardView, GrandStrategyView, TalkView

### Community 81 - "City Walls"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "Peerage"
Cohesion: 0.08
Nodes (14): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+6 more)

### Community 84 - "Realm"
Cohesion: 0.21
Nodes (4): Kind, FRIEND, RIVAL, Realm

### Community 86 - "CourtSet"
Cohesion: 0.25
Nodes (3): CourtSet, Seat, Style

### Community 87 - "Battle Sounds"
Cohesion: 0.22
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 89 - "Rank"
Cohesion: 0.12
Nodes (10): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+2 more)

### Community 90 - "BaseMap"
Cohesion: 0.11
Nodes (5): ClientMapCache, BaseMap, Nation, BaseMapStore, Ready

### Community 91 - "Type"
Cohesion: 0.16
Nodes (10): Ladder, MILITARY, NOBLE, NONE, Type, CLEAR, DELIVER, SCOUT (+2 more)

### Community 93 - "Point"
Cohesion: 0.11
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 95 - "Kit"
Cohesion: 0.14
Nodes (13): Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD, HEAVY_INFANTRY (+5 more)

### Community 96 - "Distant Army Renderer"
Cohesion: 0.13
Nodes (3): Figure, DistantArmyRenderer, DistantHorizonsLink

### Community 100 - "MapContext"
Cohesion: 0.19
Nodes (3): MapContext, PoiIconLayer, FactionInfo

### Community 102 - "Battle Site"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 103 - "Realm Planner Tests"
Cohesion: 0.19
Nodes (4): Demographics, Share, RealmPlannerTest, World

### Community 107 - "Poi"
Cohesion: 0.19
Nodes (5): Poi, PoiType, CAPITAL, CITY, FARM

### Community 108 - "Magic Schools"
Cohesion: 0.13
Nodes (12): Dojo, School, BLOOD, FIRE, ICE, IRON, LIGHT, LIGHTNING (+4 more)

### Community 109 - "PoiPrecomputer.java"
Cohesion: 0.06
Nodes (8): SeedHash, CityDistrictStructure, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 110 - "WarPlan"
Cohesion: 0.06
Nodes (26): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender, Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT (+18 more)

### Community 111 - "Kind"
Cohesion: 0.29
Nodes (7): Kind, CAPITAL, CASTLE, CITY, LORD, RETAINER, TOWN

### Community 114 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 118 - ".view"
Cohesion: 0.12
Nodes (6): CityManager, GrandStrategy, View, PlayerStats, View, Retinues

### Community 119 - "Land"
Cohesion: 0.28
Nodes (3): Land, Search, ShippingRoutes

### Community 120 - "Distance Scaling"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 122 - "BattlePlan"
Cohesion: 0.06
Nodes (13): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, BattlePlan, Order, CHARGE (+5 more)

### Community 127 - "Homeland"
Cohesion: 0.09
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 132 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 134 - ".player"
Cohesion: 0.28
Nodes (4): NobleTitle, COUNT, KNIGHT, NONE

### Community 135 - "Horn Calls"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 137 - "FormationSnapshot"
Cohesion: 0.10
Nodes (9): FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN (+1 more)

### Community 140 - "NationScreen"
Cohesion: 0.10
Nodes (3): Camera, CourtView, NationScreen

### Community 141 - "Role"
Cohesion: 0.33
Nodes (6): Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER

### Community 142 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.12
Nodes (3): RecruitsBridge, CityGuards, RecruitmentPosts

### Community 144 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 145 - "WarCamp.java"
Cohesion: 0.07
Nodes (4): FactionType, BANDIT, CITY_STATE, EMPIRE

### Community 152 - "ConquestNetwork"
Cohesion: 0.10
Nodes (9): BoardAction, CityClose, ConquestNetwork, MarkerSet, MarkerView, PeerageView, RequestBaseMap, StatsRequest (+1 more)

### Community 157 - "ConquestSavedData"
Cohesion: 0.09
Nodes (5): ConquestSavedData, MapPreview, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 162 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.09
Nodes (5): BattleSnapshot, BattleSpot, FactionInfoReply, UnitsRequest, UnitsState

### Community 165 - "BattleRole"
Cohesion: 0.08
Nodes (16): Enlisted players, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Battle roles (frontline, bait, support, flanker, kill squad), The war horn, BattleRole, BAIT, FLANKER, FRONTLINE (+8 more)

### Community 167 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.10
Nodes (7): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer, TitleLayer, UnitLayer

### Community 173 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 218 - ".sender"
Cohesion: 0.11
Nodes (4): CityView, HornOrders, SetSetting, TeleportTo

### Community 223 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 225 - "DistantHorizonsQuality.java"
Cohesion: 0.10
Nodes (6): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits, DistantHorizonsQuality

### Community 226 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

## Ambiguous Edges - Review These
- `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)` → `/pve debug battle simulate`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **308 isolated node(s):** `TOWN`, `CITY`, `HIGH`, `DOJO`, `SEA` (+303 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 856 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **125 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `net.minecraft.world.level.ChunkPos`, `.army`, `BattleScreen`, `.block`, `Role`, `net.minecraft.server.level.ServerLevel`, `org.jetbrains.annotations.Nullable`, `BattleMap.java`, `.simulate`, `Commands Guide`, `net.minecraft.world.item.ItemStack`, `War Council & War Plans`, `PveCommands Registration`, `.passOnOrders`, `ConquestEvents`, `net.minecraft.server.level.ServerLevel`, `Base Map Generator`, `net.minecraft.core.BlockPos`, `Field Rules & Arms`, `BattleKeepInventory.java`, `BaseMap`, `.complete`, `UnitMode`, `Phase`, `.view`?**
  _High betweenness centrality (0.085) - this node is a cross-community bridge._
- **What connects `TOWN`, `CITY`, `HIGH` to the rest of the system?**
  _308 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.06423586383706024 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `Organization Screen`, `list`, `ProvinceMap`, `net.minecraft.server.level.ServerLevel`, `org.jetbrains.annotations.Nullable`, `RoadNetwork`, `Recruiter`, `net.minecraft.world.item.ItemStack`, `War Council & War Plans`, `Wars Service`, `Roads`, `Countryside`, `City`, `.plan`, `PoliticalStyle`, `net.minecraft.client.gui.GuiGraphics`, `.place`, `RecruitsFactions.java`, `Base Map Generator`, `ConquestServer`, `Title Map`, `.render`, `.postChunk`, `Settlement`, `RafasConquest.java`, `Map Lands`, `rafasconquest.map.BaseMap`, `Peerage`, `Realm`, `Type`, `Kit`, `MapContext`, `Magic Schools`, `PoiPrecomputer.java`, `Client Politics`, `GrandStrategyScreen`, `ConquestRuntime`, `Homeland`?**
  _High betweenness centrality (0.051) - this node is a cross-community bridge._
- **Should `Shared Java Utilities & Tests` be split into smaller, more focused modules?**
  _Cohesion score 0.08143839238498149 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)` and `/pve debug battle simulate`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._