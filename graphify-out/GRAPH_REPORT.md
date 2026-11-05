# Graph Report - RafasConquest  (2026-11-05)

## Corpus Check
- 366 files · ~267,511 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 97 file(s) not represented in the graph (top: .nbt 84, (none) 3, .mcfunction 2)

## Summary
- 4867 nodes · 15143 edges · 227 communities (102 shown, 125 thin omitted)
- Extraction: 88% EXTRACTED · 12% INFERRED · 0% AMBIGUOUS · INFERRED: 1805 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `09d8c061`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- list
- net.minecraft.world.level.ChunkPos
- .army
- .plan
- BattleScreen
- list
- .allSitesInBox
- PoiPrecomputer.java
- net.minecraft.world.level.block.state.BlockState
- .block
- Politics
- WarCouncil.java
- CourtView
- ProvinceMap
- RoadNetwork
- Battle Map Web Page & Site
- Characters (holders, skills, prowess, traits)
- Quest
- BattleMap.java
- CityTerrain
- .postProcess
- Commands Guide
- CityManagerScreen
- Nations Planning
- net.minecraft.world.item.ItemStack
- SettlementBiomes
- War Council & War Plans
- Wars Service
- Roads
- Builder
- Capital Layout & Seed Hash
- PveCommands Registration
- com.talhanation.recruits.entities.AbstractRecruitEntity
- StyleLibrary
- Grid
- .plan
- BaseMap
- World map (CK3-style political map)
- net.minecraft.server.level.ServerLevel
- FactionInfoScreen
- Side
- World Stretch Screen
- Plan
- Plan
- Personalities
- DayClockTest
- War
- .render
- .generate
- CityPlan
- SimpleChannel.java
- Culture
- net.minecraft.core.BlockPos
- Call
- CityVariant
- Faction Map View
- .drawEmpire
- .at
- Townsperson
- Title Map
- Field Rules & Arms
- CityPlacement
- DistantArmyRenderer.java
- Distant Horizons Armies
- DistantGeneration
- Battle Calibration
- .postChunk
- FormationSnapshot
- Settlement
- RafasConquest.java
- Map Lands
- .nearest
- RecruitsBattle
- rafasconquest.map.BaseMap
- View
- Race.java
- City Walls
- Ownership & Territory
- .choose
- Kind
- Nation Rows
- CourtSet
- Battle Sounds
- Rank
- .walks
- Type
- Peerage
- .aWoodNearAWingHidesAnAmbushThatStaysPutUntilItStrikes
- Stats & Settings Screens
- WarPlan
- Distant Army Renderer
- RecruiterScreen
- BattleKeepInventory.java
- MapContext
- City Stats
- Battle Site
- Realm Planner Tests
- MarkerSpec
- Volleys
- RoadNetworkTest
- Magic Schools
- PoiPrecomputer.java
- MusterBars
- CityGridBuilder
- PoliticsTest
- Client Politics
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- GrandStrategyScreen
- Plan
- .view
- Homeland
- Distance Scaling
- CityManager
- BattlePlan
- .onLevelLoad
- CityStyles
- DistantHorizonsLink
- ConquestGameRules
- MarkerScreen
- Terrain
- Layout
- net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
- Trait
- SeatCities
- .player
- ConquestConfig
- DistantArmies
- Organization Screen
- PoliticsData
- NationScreen
- ClientEvents.java
- net.minecraft.server.level.ServerLevel
- SableShips.java
- Duty
- Faction
- .render
- .plan
- CLAUDE.md
- BoardScreen
- BlockGrid
- Culture
- ConquestSavedData
- net.minecraft.network.FriendlyByteBuf
- net.neoforged.bus.api.SubscribeEvent
- .get
- net.minecraft.client.gui.GuiGraphics
- Small Cluster 170
- ClientMapCache
- net.minecraft.core.BlockPos
- .pack
- .build
- net.neoforged.neoforge.network.handling.IPayloadContext
- .plan
- Build
- .candidateChunk
- net.minecraft.server.level.ServerPlayer
- DistantHorizonsQuality.java
- Phase

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 246 edges
2. `FieldBattle` - 122 edges
3. `FieldBlock` - 118 edges
4. `ConquestNetwork` - 105 edges
5. `BaseMap` - 95 edges
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

## Communities (227 total, 125 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.06
Nodes (10): Point, FieldArmy, Event, FieldBattle, Order, Outcome, Volley, FieldBlock (+2 more)

### Community 1 - "list"
Cohesion: 0.07
Nodes (5): CitySizesTest, ArmouryTest, RetinuesTest, NobleDistrictTest, PlacementOffsetsTest

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.08
Nodes (6): CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourPiece, PlaceholderPiece

### Community 5 - "BattleScreen"
Cohesion: 0.06
Nodes (21): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+13 more)

### Community 6 - "list"
Cohesion: 0.06
Nodes (11): Countryside (hamlets, villages, watchtowers, castles), Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, Nations by zone, Roads (high, city, town roads; bridges; signposts), Seat cities, Storm wall and war zone, Dojos (+3 more)

### Community 7 - ".allSitesInBox"
Cohesion: 0.13
Nodes (3): CityLayout, District, Site

### Community 8 - "PoiPrecomputer.java"
Cohesion: 0.06
Nodes (5): SeedHash, TerritoryMap, FactionBannerNbt, PoliticalMap, FactionInfo

### Community 9 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.07
Nodes (6): CommandTableBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, WorldMapItem, MarkerBlock

### Community 11 - "Politics"
Cohesion: 0.10
Nodes (9): Goal, Lands, Stronghold, Claim, Front, Muster, CEREMONIAL, OFFENSIVE (+1 more)

### Community 12 - "WarCouncil.java"
Cohesion: 0.09
Nodes (6): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), TickWeave experiment, Ranks

### Community 13 - "CourtView"
Cohesion: 0.18
Nodes (3): Camera, CourtView, Figure

### Community 14 - "ProvinceMap"
Cohesion: 0.17
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 15 - "RoadNetwork"
Cohesion: 0.11
Nodes (4): RoadFeature, Profile, RoadNetwork, Stretch

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.06
Nodes (30): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Characters (holders, skills, prowess, traits), Cultures (seven homelands), Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Enlisted players (+22 more)

### Community 19 - "Quest"
Cohesion: 0.19
Nodes (5): Boards, Entry, View, Taken, Quest

### Community 23 - "Commands Guide"
Cohesion: 0.09
Nodes (30): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+22 more)

### Community 25 - "Nations Planning"
Cohesion: 0.17
Nodes (6): Nation, Nations, Plan, Seats, NationsTest, World

### Community 26 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.08
Nodes (19): WarHorn, Armoury, Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE (+11 more)

### Community 28 - "War Council & War Plans"
Cohesion: 0.17
Nodes (4): Facts, Officer, Talk, WarCouncil

### Community 30 - "Roads"
Cohesion: 0.09
Nodes (17): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Land (+9 more)

### Community 31 - "Builder"
Cohesion: 0.13
Nodes (6): BackBanner, Builder, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 32 - "Capital Layout & Seed Hash"
Cohesion: 0.11
Nodes (8): CapitalLayout, Land, Site, OriginRoller, Roll, WorldSettings, CapitalRelocationTest, LayoutTest

### Community 34 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.04
Nodes (15): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT, GeneralTask (+7 more)

### Community 36 - "Grid"
Cohesion: 0.19
Nodes (5): Countryside, Plan, Grid, CountrysideTest, World

### Community 37 - ".plan"
Cohesion: 0.11
Nodes (7): Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 38 - "BaseMap"
Cohesion: 0.09
Nodes (5): Dojo, BaseMap, BaseMapStore, Ready, PoliticalStyle

### Community 39 - "World map (CK3-style political map)"
Cohesion: 0.33
Nodes (7): Base map generated from seed, Command Table block, Faction Info screen, World map layers, Province size setting, Provinces (ProvinceMap), World map (CK3-style political map)

### Community 41 - "FactionInfoScreen"
Cohesion: 0.07
Nodes (7): ClientScreens, FactionInfoScreen, Line, FactionBanner, Layer, RequestFactionInfo, FactionBannerTest

### Community 42 - "Side"
Cohesion: 0.24
Nodes (6): Heights, Side, EAST, NORTH, SOUTH, WEST

### Community 43 - "World Stretch Screen"
Cohesion: 0.13
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 45 - "Plan"
Cohesion: 0.27
Nodes (5): Edge, CARVE, FILL, VALLEY, Plan

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "DayClockTest"
Cohesion: 0.25
Nodes (3): DayClock, Step, DayClockTest

### Community 48 - "War"
Cohesion: 0.11
Nodes (4): Army, Event, War, Politics.Front

### Community 50 - ".render"
Cohesion: 0.11
Nodes (5): CharacterScreen, Retainer, Spot, ClientPeerage, Banner

### Community 51 - ".generate"
Cohesion: 0.08
Nodes (8): ProvinceNames, WorldFeatures, Nation, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 53 - "SimpleChannel.java"
Cohesion: 0.14
Nodes (3): StatsRequest, Msg, SimpleChannel

### Community 54 - "Culture"
Cohesion: 0.33
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 56 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 57 - "CityVariant"
Cohesion: 0.17
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 59 - ".drawEmpire"
Cohesion: 0.27
Nodes (3): Hit, LabelLayer, LabelShape

### Community 62 - "Townsperson"
Cohesion: 0.06
Nodes (7): RecruiterRenderer, TownspersonRenderer, Walk, Recruiter, DistrictKey, Townsfolk, Townsperson

### Community 63 - "Title Map"
Cohesion: 0.16
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Field Rules & Arms"
Cohesion: 0.10
Nodes (11): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, FieldRules, Rates, Side (+3 more)

### Community 66 - "DistantArmyRenderer.java"
Cohesion: 0.13
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 69 - "Battle Calibration"
Cohesion: 0.19
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 70 - ".postChunk"
Cohesion: 0.23
Nodes (3): RecruitmentPost, RecruitmentPostPiece, RecruitmentPostTest

### Community 71 - "FormationSnapshot"
Cohesion: 0.12
Nodes (11): FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN (+3 more)

### Community 72 - "Settlement"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 73 - "RafasConquest.java"
Cohesion: 0.05
Nodes (4): ConquestContent, StreetWalks, ConquestEntities, RafasConquest

### Community 76 - "RecruitsBattle"
Cohesion: 0.05
Nodes (4): Figure, Live, RecruitsBattle, TickTimes

### Community 77 - "rafasconquest.map.BaseMap"
Cohesion: 0.06
Nodes (6): InventoryStatsButton, Portraits, CityClose, ConquestNetwork, FactionInfoReply, RequestBaseMap

### Community 78 - "View"
Cohesion: 0.15
Nodes (3): BoardView, GrandStrategyView, TalkView

### Community 79 - "Race.java"
Cohesion: 0.33
Nodes (5): Character page, Court 3D scene (CourtView), Demographics (population by culture and race), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...)

### Community 81 - "City Walls"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.20
Nodes (5): Candidate, OwnershipResolver, Claim, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.14
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 84 - "Kind"
Cohesion: 0.33
Nodes (6): Kind, CLEAR_ABOVE, NONE, PLATE, SLOPE, WALL_BASE

### Community 86 - "CourtSet"
Cohesion: 0.23
Nodes (3): CourtSet, Seat, Style

### Community 87 - "Battle Sounds"
Cohesion: 0.20
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 89 - "Rank"
Cohesion: 0.10
Nodes (14): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+6 more)

### Community 91 - "Type"
Cohesion: 0.13
Nodes (10): Ladder, MILITARY, NOBLE, NONE, Type, CLEAR, DELIVER, SCOUT (+2 more)

### Community 92 - "Peerage"
Cohesion: 0.11
Nodes (8): Merit, Peer, Peerage, PeerageSync, QuestProgress, Option, Talk, View

### Community 93 - ".aWoodNearAWingHidesAnAmbushThatStaysPutUntilItStrikes"
Cohesion: 0.20
Nodes (5): GroundPlanTest, Land, Land, Land, Land

### Community 95 - "WarPlan"
Cohesion: 0.05
Nodes (31): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+23 more)

### Community 96 - "Distant Army Renderer"
Cohesion: 0.09
Nodes (6): DistantDepth, Arrow, ArrowVolleys, DistantArmyRenderer, Figure, DistantShips

### Community 102 - "Battle Site"
Cohesion: 0.20
Nodes (5): BattleSite, Part, Water, BattleSite.Water, BattleSiteTest

### Community 103 - "Realm Planner Tests"
Cohesion: 0.16
Nodes (4): Honour, Honours, RealmPlannerTest, World

### Community 105 - "MarkerSpec"
Cohesion: 0.07
Nodes (5): MarkerRenderer, Palette, WallTemplates, MarkerBlockEntity, MarkerSpec

### Community 108 - "Magic Schools"
Cohesion: 0.14
Nodes (11): School, BLOOD, FIRE, ICE, IRON, LIGHT, LIGHTNING, SHADOW (+3 more)

### Community 109 - "PoiPrecomputer.java"
Cohesion: 0.07
Nodes (7): CityDistrictStructure, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 111 - "CityGridBuilder"
Cohesion: 0.10
Nodes (3): CityGridBuilder, Ground, PlacedWall

### Community 117 - "Plan"
Cohesion: 0.19
Nodes (8): Kind, BARRACKS, CHURCH, NOBLE, PALACE, POST, WALL, Piece

### Community 118 - ".view"
Cohesion: 0.18
Nodes (3): GrandStrategy, View, Retinues

### Community 119 - "Homeland"
Cohesion: 0.10
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 120 - "Distance Scaling"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 122 - "BattlePlan"
Cohesion: 0.06
Nodes (8): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, BattlePlanTest

### Community 128 - "Terrain"
Cohesion: 0.20
Nodes (3): Cell, Terrain, UnitLayer

### Community 130 - "net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate"
Cohesion: 0.22
Nodes (3): Part, ShipHulls, Loaded

### Community 132 - "Trait"
Cohesion: 0.15
Nodes (6): Officers, Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 133 - "SeatCities"
Cohesion: 0.08
Nodes (8): SeatCities, Tier, COUNTY, DUCHY, EMPIRE, KINGDOM, SeatCitiesTest, World

### Community 134 - ".player"
Cohesion: 0.18
Nodes (5): NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 140 - "NationScreen"
Cohesion: 0.14
Nodes (3): NationScreen, Demographics, Share

### Community 142 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.07
Nodes (6): RecruitsBridge, CityBanners, CityGuards, ConquestServer, PoiPrecomputer, RecruitmentPosts

### Community 143 - "SableShips.java"
Cohesion: 0.07
Nodes (13): SableLink, SableShips, Lane, Leg, Port, SeaLanes, Ship, Size (+5 more)

### Community 144 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 145 - "Faction"
Cohesion: 0.07
Nodes (15): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), Faction, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType (+7 more)

### Community 152 - "BoardScreen"
Cohesion: 0.14
Nodes (4): BoardScreen, BoardAction, MarkerSet, MarkerView

### Community 156 - "Culture"
Cohesion: 0.17
Nodes (9): Culture, Hold, Kind, CAPITAL, CASTLE, CITY, LORD, RETAINER (+1 more)

### Community 157 - "ConquestSavedData"
Cohesion: 0.08
Nodes (5): ConquestSavedData, MapPreview, Poi, ConquestSavedDataTest, FactionInfoTest

### Community 162 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.06
Nodes (9): BattleSnapshot, BattleSpot, FigureStrike, HornOrders, PeerageView, SetSetting, TeleportTo, UnitsRequest (+1 more)

### Community 163 - "net.neoforged.bus.api.SubscribeEvent"
Cohesion: 0.06
Nodes (3): BattleKeepInventory, ClientEvents, ConquestEvents

### Community 165 - ".get"
Cohesion: 0.10
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 167 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.14
Nodes (6): BaseMapLayer, LegendEntry, MapLayer, Symbol, RoadLayer, TerritoryLayer

### Community 176 - "net.minecraft.core.BlockPos"
Cohesion: 0.07
Nodes (4): CharacterMarkers, Placed, Offsets, StylePiece

### Community 179 - ".pack"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 221 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 222 - ".candidateChunk"
Cohesion: 0.23
Nodes (3): ChunkCoord, FarmLayout, CityLayoutTest

### Community 223 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.07
Nodes (14): Card, Enlisted, Shape, LINE, SQUARE, V, WEDGE, Unit (+6 more)

### Community 225 - "DistantHorizonsQuality.java"
Cohesion: 0.13
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 226 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

## Ambiguous Edges - Review These
- `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)` → `/pve debug battle simulate`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **312 isolated node(s):** `SMALL`, `MEDIUM`, `LARGE`, `XLARGE`, `CAPITAL` (+307 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 880 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **125 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `net.minecraft.world.level.ChunkPos`, `.army`, `Trait`, `BattleScreen`, `.block`, `net.minecraft.server.level.ServerLevel`, `org.jetbrains.annotations.Nullable`, `BattleMap.java`, `Commands Guide`, `net.minecraft.world.item.ItemStack`, `War Council & War Plans`, `Wars Service`, `PveCommands Registration`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `net.neoforged.bus.api.SubscribeEvent`, `net.minecraft.server.level.ServerLevel`, `net.minecraft.core.BlockPos`, `Field Rules & Arms`, `WarPlan`, `net.minecraft.server.level.ServerPlayer`, `Phase`, `Battle Site`?**
  _High betweenness centrality (0.115) - this node is a cross-community bridge._
- **What connects `SMALL`, `MEDIUM`, `LARGE` to the rest of the system?**
  _312 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.05697167755991286 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `Terrain`, `list`, `PoiPrecomputer.java`, `Organization Screen`, `WarCouncil.java`, `ProvinceMap`, `SableShips.java`, `org.jetbrains.annotations.Nullable`, `net.minecraft.server.level.ServerLevel`, `RoadNetwork`, `.render`, `net.minecraft.world.item.ItemStack`, `War Council & War Plans`, `Wars Service`, `Roads`, `ConquestSavedData`, `Grid`, `ClientMapCache`, `.generate`, `Title Map`, `Settlement`, `rafasconquest.map.BaseMap`, `.choose`, `Type`, `Peerage`, `MapContext`, `CityGridBuilder`, `Client Politics`, `GrandStrategyScreen`, `.onLevelLoad`?**
  _High betweenness centrality (0.075) - this node is a cross-community bridge._
- **Should `list` be split into smaller, more focused modules?**
  _Cohesion score 0.07287093942054433 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)` and `/pve debug battle simulate`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._