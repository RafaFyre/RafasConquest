# Graph Report - RafasConquest  (2026-11-06)

## Corpus Check
- 380 files · ~301,726 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 97 file(s) not represented in the graph (top: .nbt 84, (none) 3, .mcfunction 2)

## Summary
- 5310 nodes · 16896 edges · 256 communities (107 shown, 149 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2261 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `21a1e414`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- CityGridBuilder.java
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsArmies.java
- CityLayout
- Politics
- Arm
- MarkerSpec
- .state
- BaseMap
- net.minecraft.server.level.ServerLevel
- RoadNetwork
- ConquestSavedData
- Battle Map Web Page & Site
- Person
- Plan
- BattleMap.java
- PoliticsTest
- Calibration
- Commands Guide
- CityManagerScreen
- TitleMap
- net.minecraft.world.item.ItemStack
- MarkerScreen
- WarCouncil
- rafasconquest.map.BaseMap
- Lands
- .postChunk
- Capital Layout & Seed Hash
- PveCommands Registration
- ClientModEvents.java
- StyleLibrary
- .plan
- ConquestContent.java
- uuid
- .boxMax
- rafasconquest.faction.Faction
- .of
- RecruitsBattle
- HorizontalStretch
- ConquestNetwork
- CourtView
- Personalities
- SeaLanes
- Townsperson
- .fire
- net.minecraft.core.Vec3i
- net.minecraft.world.entity.Entity
- BoardScreen
- .place
- WarCamp
- CityLayout
- RecruitmentPost
- Faction Map View
- .plan
- Peerage
- .render
- TitleMap
- .render
- DistantArmyRenderer.java
- Succession
- .onRenderLevel
- Realm
- CourtSet
- DistantArmyRenderer
- Centre
- .place
- .onLevelLoad
- MapLands
- .drawEmpire
- FactionInfoScreen
- .plan
- CityVariant
- .sample
- BattleRole
- Ownership & Territory
- .choose
- Stat
- Nation Rows
- .touches
- BattleSounds
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- .broadcast
- ConquestNetwork.java
- DistantHorizonsQuality.java
- CityTerrainPiece
- CityPlan
- Stats & Settings Screens
- .generate
- map
- ClientEvents
- net.minecraft.client.gui.GuiGraphics
- OrganizationScreen
- ConquestWorldScreen
- .world
- SimpleChannel.java
- .conquer
- Magic Schools
- UnitOrder
- Ranks
- Feud
- ArrowVolleys
- Doctrine
- Settlement
- GrandStrategyScreen
- Strategy
- .view
- Homeland
- BattleType
- BattleKeepInventory
- BattlePlan
- Quest
- CityStyles
- .measure
- Tier
- RecruitsBattle.java
- Characters (holders, skills, prowess, traits)
- Organizations
- Recruiter
- net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
- Trait
- Far
- ConquestConfig
- World map (CK3-style political map)
- WarPlan
- .boundsAt
- FormationSnapshot
- ClientPolitics
- GameCharacter
- TitleStyle
- SeaLanes
- Call
- net.minecraft.world.level.block.Rotation
- Kind
- Politics.Front
- CLAUDE.md
- Task
- ConquestGameRules
- .plan
- Front
- PoiType
- Small Cluster 170
- net.minecraft.server.level.ServerPlayer
- Faction
- .pack
- Rank
- Kind
- ClientScreens
- .player
- Roads
- CityGridBuilder
- BlockGrid
- DistantGeneration
- ConquestEvents
- RecruiterScreen
- .stop
- .passOnOrders
- net.minecraft.world.level.block.state.BlockState
- Role
- Distribution

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 251 edges
2. `FieldBlock` - 169 edges
3. `FieldBattle` - 158 edges
4. `ConquestNetwork` - 102 edges
5. `FieldArmy` - 99 edges
6. `BaseMap` - 94 edges
7. `Lands` - 89 edges
8. `Politics` - 77 edges
9. `Person` - 67 edges
10. `Realm` - 62 edges

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

## Communities (256 total, 149 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.05
Nodes (7): FieldArmy, Event, FieldBattle, Order, Outcome, Target, Volley

### Community 1 - "CityGridBuilder.java"
Cohesion: 0.04
Nodes (9): CityDistrictStructure, CityPlacement, CitySlopeStructure, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement (+1 more)

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.08
Nodes (6): CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourPiece, PlaceholderPiece

### Community 3 - ".army"
Cohesion: 0.09
Nodes (3): Point, DoctrineTest, FieldBattleTest

### Community 4 - "CityTerrain"
Cohesion: 0.09
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.09
Nodes (3): BattleScreen, Entry, Menu

### Community 7 - "CityLayout"
Cohesion: 0.09
Nodes (4): District, Site, SlopeTile, CityLayoutTest

### Community 8 - "Politics"
Cohesion: 0.07
Nodes (7): Army, Muster, CEREMONIAL, OFFENSIVE, Politics, PoliticsData, War

### Community 9 - "Arm"
Cohesion: 0.09
Nodes (16): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, Striker, ARCHER_MELEE, ARROWS (+8 more)

### Community 10 - "MarkerSpec"
Cohesion: 0.07
Nodes (11): MarkerRenderer, Palette, WallTemplates, MarkerBlockEntity, MarkerSpec, Build, CASTLE, HAMLET (+3 more)

### Community 13 - "BaseMap"
Cohesion: 0.10
Nodes (6): ClientMapCache, BaseMap, Nation, BaseMapStore, Ready, PoliticalStyle

### Community 15 - "RoadNetwork"
Cohesion: 0.14
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 16 - "ConquestSavedData"
Cohesion: 0.10
Nodes (5): ConquestSavedData, CityBanners, ConquestServer, EconomyTick, PoiPrecomputer

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.23
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "Plan"
Cohesion: 0.15
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, POST, WALL, Piece (+1 more)

### Community 20 - "BattleMap.java"
Cohesion: 0.04
Nodes (4): Battle map developer web page, Morale, rout and rally, StreetWalks, RafasConquest

### Community 22 - "Calibration"
Cohesion: 0.29
Nodes (3): Calibration, Fit, CalibrationTest

### Community 23 - "Commands Guide"
Cohesion: 0.06
Nodes (30): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+22 more)

### Community 25 - "TitleMap"
Cohesion: 0.17
Nodes (6): Nation, Nations, Plan, Seats, NationsTest, World

### Community 26 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.05
Nodes (22): WarHorn, BackBannerLayer, BackBanners, Armoury, Kit, ARCHER_GUARD, CAPTAIN, CAVALRY (+14 more)

### Community 27 - "MarkerScreen"
Cohesion: 0.07
Nodes (9): BattleSite, Part, Water, BattleSite.Water, Cell, Terrain, Drop, MarkerScreen (+1 more)

### Community 28 - "WarCouncil"
Cohesion: 0.18
Nodes (4): Facts, Officer, Talk, WarCouncil

### Community 29 - "rafasconquest.map.BaseMap"
Cohesion: 0.12
Nodes (3): MusterBars, PoliticsSync, Wars

### Community 30 - "Lands"
Cohesion: 0.10
Nodes (4): Goal, Lands, Stronghold, Claim

### Community 31 - ".postChunk"
Cohesion: 0.12
Nodes (3): RecruitsBridge, CityGuards, RecruitmentPosts

### Community 32 - "Capital Layout & Seed Hash"
Cohesion: 0.09
Nodes (10): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, WorldSettings (+2 more)

### Community 36 - ".plan"
Cohesion: 0.16
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 37 - "ConquestContent.java"
Cohesion: 0.10
Nodes (3): ConquestContent, WorldMapItem, ConquestEntities

### Community 38 - "uuid"
Cohesion: 0.10
Nodes (8): SeedHash, TerritoryMap, FactionType, BANDIT, CITY_STATE, EMPIRE, PoliticalMap, FactionInfo

### Community 41 - ".of"
Cohesion: 0.22
Nodes (3): FactionBanner, Layer, FactionBannerTest

### Community 42 - "RecruitsBattle"
Cohesion: 0.05
Nodes (9): Officers, RecruitsBattle, Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER (+1 more)

### Community 44 - "ConquestNetwork"
Cohesion: 0.03
Nodes (31): BaseMapPart, BattleSpot, BoardAction, BoardView, CityClose, CityView, ConquestNetwork, FactionInfoReply (+23 more)

### Community 45 - "CourtView"
Cohesion: 0.09
Nodes (4): Camera, CourtView, Figure, NationScreen

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "Townsperson"
Cohesion: 0.07
Nodes (6): RecruiterRenderer, TownspersonRenderer, Walk, DistrictKey, Townsfolk, Townsperson

### Community 54 - ".place"
Cohesion: 0.10
Nodes (8): Ground, GroundPlan, Figure, GroundPlanTest, Land, Land, Land, Land

### Community 56 - "CityLayout"
Cohesion: 0.16
Nodes (8): CityLayout, Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 57 - "RecruitmentPost"
Cohesion: 0.23
Nodes (3): RecruitmentPost, RecruitmentPostPiece, RecruitmentPostTest

### Community 59 - ".plan"
Cohesion: 0.12
Nodes (3): SeatCities, SeatCitiesTest, World

### Community 60 - "Peerage"
Cohesion: 0.11
Nodes (8): Merit, Peer, Peerage, Taken, QuestProgress, Option, Talk, View

### Community 61 - ".render"
Cohesion: 0.10
Nodes (6): CharacterScreen, Retainer, Spot, ClientPeerage, Banner, Portraits

### Community 63 - "TitleMap"
Cohesion: 0.17
Nodes (3): TitleMap, Land, TitleMapTest

### Community 65 - "DistantArmyRenderer.java"
Cohesion: 0.15
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 67 - ".onRenderLevel"
Cohesion: 0.10
Nodes (3): BattleSpotRing, DistantHorizonsLink, DistantHorizonsQuality

### Community 68 - "Realm"
Cohesion: 0.20
Nodes (4): Kind, FRIEND, RIVAL, Realm

### Community 69 - "CourtSet"
Cohesion: 0.23
Nodes (3): CourtSet, Seat, Style

### Community 70 - "DistantArmyRenderer"
Cohesion: 0.16
Nodes (3): DistantDepth, DistantArmyRenderer, Figure

### Community 71 - "Centre"
Cohesion: 0.17
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 75 - ".drawEmpire"
Cohesion: 0.18
Nodes (4): Hit, LabelLayer, RoadLayer, LabelShape

### Community 77 - ".plan"
Cohesion: 0.20
Nodes (5): Countryside, Plan, Grid, CountrysideTest, World

### Community 78 - "CityVariant"
Cohesion: 0.20
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 81 - "BattleRole"
Cohesion: 0.19
Nodes (6): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT

### Community 82 - "Ownership & Territory"
Cohesion: 0.20
Nodes (5): Candidate, OwnershipResolver, Claim, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.14
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 84 - "Stat"
Cohesion: 0.14
Nodes (9): Honour, Honours, Stat, DIPLOMACY, INTRIGUE, LEARNING, MARTIAL, PROWESS (+1 more)

### Community 87 - "BattleSounds"
Cohesion: 0.18
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 91 - "DistantHorizonsQuality.java"
Cohesion: 0.13
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 93 - "CityPlan"
Cohesion: 0.21
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 95 - ".generate"
Cohesion: 0.08
Nodes (7): ProvinceNames, WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 99 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.08
Nodes (10): BaseMapLayer, MapContext, LegendEntry, MapLayer, Symbol, Place, PlaceMenuLayer, PoiIconLayer (+2 more)

### Community 101 - "ConquestWorldScreen"
Cohesion: 0.27
Nodes (3): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton

### Community 105 - "SimpleChannel.java"
Cohesion: 0.10
Nodes (5): BackBanner, Builder, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 108 - "Magic Schools"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 109 - "UnitOrder"
Cohesion: 0.06
Nodes (29): Card, Shape, LINE, SQUARE, V, WEDGE, Unit, UnitMode (+21 more)

### Community 111 - "Ranks"
Cohesion: 0.10
Nodes (7): Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY, SHIELDMAN, Ranks

### Community 112 - "Feud"
Cohesion: 0.18
Nodes (5): Clash, Kind, DUEL, SKIRMISH, FeudDuel

### Community 114 - "Doctrine"
Cohesion: 0.16
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 115 - "Settlement"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 117 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 118 - ".view"
Cohesion: 0.12
Nodes (6): GrandStrategy, View, PlayerStats, View, Retinues, RetinuesTest

### Community 119 - "Homeland"
Cohesion: 0.09
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 120 - "BattleType"
Cohesion: 0.22
Nodes (5): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE

### Community 122 - "BattlePlan"
Cohesion: 0.06
Nodes (8): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, BattlePlanTest

### Community 123 - "Quest"
Cohesion: 0.10
Nodes (14): Boards, Entry, View, Ladder, MILITARY, NOBLE, NONE, Quest (+6 more)

### Community 126 - "Tier"
Cohesion: 0.29
Nodes (5): Tier, COUNTY, DUCHY, EMPIRE, KINGDOM

### Community 128 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.03
Nodes (49): Ambushes from woods, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Character page, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings) (+41 more)

### Community 129 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 130 - "Recruiter"
Cohesion: 0.18
Nodes (3): Recruiter, Offer, Recruitment

### Community 131 - "net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate"
Cohesion: 0.18
Nodes (3): Part, ShipHulls, Loaded

### Community 132 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 133 - "Far"
Cohesion: 0.17
Nodes (3): DistantShips, Far, SableSeen

### Community 136 - "World map (CK3-style political map)"
Cohesion: 0.33
Nodes (7): Base map generated from seed, Command Table block, Faction Info screen, World map layers, Province size setting, Provinces (ProvinceMap), World map (CK3-style political map)

### Community 138 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 139 - "FormationSnapshot"
Cohesion: 0.10
Nodes (12): FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN (+4 more)

### Community 143 - "SeaLanes"
Cohesion: 0.09
Nodes (9): Hull, Ramming, BattleShips, Landing, Helm, Ram, SableLink, SableShips (+1 more)

### Community 144 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 146 - "net.minecraft.world.level.block.Rotation"
Cohesion: 0.13
Nodes (3): PlacedWall, GoldChurch, Layout

### Community 148 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 152 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 156 - ".plan"
Cohesion: 0.13
Nodes (6): Demographics, Share, Faith, Bond, RealmPlanner, Seat

### Community 163 - "Front"
Cohesion: 0.13
Nodes (4): Feud, Lord, Feuds, Front

### Community 167 - "PoiType"
Cohesion: 0.11
Nodes (8): ResourceType, FOOD, Poi, PoiType, CAPITAL, CITY, FARM, StructureLink

### Community 174 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.09
Nodes (3): Enlisted, GeneralTask, CityManager

### Community 179 - ".pack"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 204 - "Rank"
Cohesion: 0.11
Nodes (11): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+3 more)

### Community 206 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 218 - ".player"
Cohesion: 0.14
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageSync

### Community 219 - "Roads"
Cohesion: 0.11
Nodes (15): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+7 more)

### Community 221 - "CityGridBuilder"
Cohesion: 0.08
Nodes (4): CityGridBuilder, Ground, Frame, Harbour

### Community 249 - ".stop"
Cohesion: 0.09
Nodes (9): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT (+1 more)

### Community 254 - ".passOnOrders"
Cohesion: 0.05
Nodes (20): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+12 more)

### Community 257 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.07
Nodes (6): Saved, CommandTableBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, MarkerBlock

### Community 259 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 262 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

## Ambiguous Edges - Review These
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **329 isolated node(s):** `ADVANCE`, `CAVALRY_CHARGE`, `CAVALRY_RECALL`, `WALL`, `GENERAL_CHARGES` (+324 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 946 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **149 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `org.jetbrains.annotations.Nullable`, `net.minecraft.world.level.ChunkPos`, `.army`, `Arm`, `.state`, `GameCharacter`, `net.minecraft.server.level.ServerLevel`, `SeaLanes`, `BattleMap.java`, `Calibration`, `net.minecraft.world.item.ItemStack`, `MarkerScreen`, `WarCouncil`, `.onServerTick`, `PveCommands Registration`, `rafasconquest.faction.Faction`, `net.minecraft.server.level.ServerPlayer`, `net.minecraft.world.entity.Entity`, `.place`, `WarCamp`, `Peerage`, `.broadcast`, `UnitOrder`, `Feud`, `.stop`, `.passOnOrders`, `RecruitsBattle.java`?**
  _High betweenness centrality (0.079) - this node is a cross-community bridge._
- **What connects `ADVANCE`, `CAVALRY_CHARGE`, `CAVALRY_RECALL` to the rest of the system?**
  _329 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.051446088231374115 - nodes in this community are weakly interconnected._
- **Why does `ConquestNetwork` connect `ConquestNetwork` to `map`, `net.minecraft.world.level.block.state.BlockState`, `BattleScreen`, `SimpleChannel.java`, `list`, `net.minecraft.server.level.ServerPlayer`, `net.minecraft.world.entity.Entity`, `BattleMap.java`, `CityManagerScreen`, `.stop`, `ConquestNetwork.java`, `RecruitsBattle.java`?**
  _High betweenness centrality (0.061) - this node is a cross-community bridge._
- **Should `CityGridBuilder.java` be split into smaller, more focused modules?**
  _Cohesion score 0.042296918767507005 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._