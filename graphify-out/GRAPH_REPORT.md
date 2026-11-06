# Graph Report - RafasConquest  (2026-11-05)

## Corpus Check
- 375 files · ~286,487 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 97 file(s) not represented in the graph (top: .nbt 84, (none) 3, .mcfunction 2)

## Summary
- 5132 nodes · 16216 edges · 268 communities (107 shown, 161 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2103 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `e9e3f4fc`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- PoiPrecomputer.java
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- RecruitsBattle.java
- WorldSettings
- ConquestEvents.java
- net.minecraft.world.level.block.state.BlockState
- net.minecraft.nbt.CompoundTag
- RecruitsBattle
- ConquestSavedData
- BaseMap
- ClientModEvents.java
- RoadNetwork
- .passOnOrders
- Battle Map Web Page & Site
- net.minecraft.world.level.block.Rotation
- Realm
- BattleMap.java
- .build
- CourtSet
- Commands Guide
- CityManagerScreen
- TitleMap
- net.minecraft.world.item.ItemStack
- WarCouncil
- Wars Service
- Politics
- Land
- Capital Layout & Seed Hash
- PveCommands Registration
- .state
- StyleLibrary
- CourtView
- Cities (grid, plots, vanilla village buildings)
- Countryside
- com.talhanation.recruits.entities.AbstractRecruitEntity
- rafasconquest.faction.Faction
- FactionInfoScreen
- War
- HorizontalStretch
- ConquestServer
- Task
- Personalities
- DayClockTest
- BattleRole
- .generate
- CityPlan
- BoardScreen
- .start
- WarCamp
- Builder
- .postChunk
- Faction Map View
- MapContext
- Peerage
- net.minecraft.client.gui.GuiGraphics
- Recruiter
- TitleMap
- Organizations
- net.minecraft.core.Vec3i
- .broadcast
- DistantHorizonsArmies.java
- .onLevelLoad
- Calibration
- net.minecraft.world.level.levelgen.structure.Structure
- .touches
- City
- net.minecraft.world.phys.Vec3
- MapLands
- .drawEmpire
- Poi
- DistantArmyRenderer
- .boundsAt
- DistantArmyRenderer.java
- City Walls
- Ownership & Territory
- .choose
- net.minecraft.network.FriendlyByteBuf
- Nation Rows
- Battle Sounds
- rafasconquest_world SavedData
- PoliticalStyle
- .onLogout
- BackBannerLayer.java
- .noble
- Stats & Settings Screens
- Person
- ConquestWorldgen
- Centre
- .render
- .of
- Arm
- Battle Site
- .world
- RecruitmentPost
- Call
- Magic Schools
- DistantHorizonsQuality.java
- Quest
- MapLayer
- Lands
- net.neoforged.neoforge.network.handling.IPayloadContext
- Doctrine
- CountryStructure
- GrandStrategyScreen
- CityPlacement
- net.minecraft.server.level.ServerPlayer
- Race
- Faction
- Clash
- BattlePlan
- Quest
- CityStyles
- .measure
- Duty
- Role
- .enlist
- Recruiter
- ShipHulls
- Trait
- .nearest
- BlockGrid
- Person
- .fire
- Politics.Front
- FormationSnapshot
- NationScreen
- Retinues.java
- BattleType
- net.minecraft.server.level.ServerLevel
- .drawAligned
- Kind
- PoliticsTest
- UnitOrder
- CLAUDE.md
- CityTerrainPiece
- ConquestGameRules
- .plan
- .place
- net.neoforged.bus.api.SubscribeEvent
- DistantGeneration
- Small Cluster 170
- ConquestConfig
- .pack
- net.minecraft.core.Vec3i
- Rank
- Kind
- ClientScreens
- .player
- Roads
- CityGridBuilder
- .battleName
- net.minecraft.server.level.ServerPlayer
- UnitMode
- ArrowVolleys
- PlaceholderPiece
- ClientEvents
- .send
- RecruiterScreen
- net.neoforged.fml.common.EventBusSubscriber
- Shape
- RafasConquest (PvE addon for Villager Recruits)
- Phase
- rafasconquest_world SavedData
- Build
- FarmPlacement
- WorldMapItem.java
- BackBanners
- UnitInfo
- CityDistrictStructure
- FigureStrike
- PlacementOffsetsTest.java

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 248 edges
2. `FieldBattle` - 132 edges
3. `FieldBlock` - 120 edges
4. `ConquestNetwork` - 103 edges
5. `FieldArmy` - 94 edges
6. `BaseMap` - 82 edges
7. `Lands` - 81 edges
8. `Politics` - 75 edges
9. `BattleScreen` - 60 edges
10. `Person` - 59 edges

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

## Communities (268 total, 161 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.06
Nodes (6): FieldArmy, Event, FieldBattle, Order, Volley, FieldBlock

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.09
Nodes (5): CityRoadPiece, CountryPiece, GoldChurchPiece, HarbourPiece, RecruitmentPostPiece

### Community 3 - ".army"
Cohesion: 0.09
Nodes (3): Point, DoctrineTest, FieldBattleTest

### Community 4 - "CityTerrain"
Cohesion: 0.08
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.09
Nodes (3): BattleScreen, Entry, Menu

### Community 6 - "RecruitsBattle.java"
Cohesion: 0.06
Nodes (3): InventoryStatsButton, ConquestNetwork, FactionInfoReply

### Community 7 - "WorldSettings"
Cohesion: 0.10
Nodes (5): CityLayout, District, Site, SlopeTile, CityLayoutTest

### Community 9 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.07
Nodes (5): CommandTableBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, MarkerBlock

### Community 10 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.07
Nodes (5): MarkerRenderer, Palette, WallTemplates, MarkerBlockEntity, MarkerSpec

### Community 12 - "ConquestSavedData"
Cohesion: 0.09
Nodes (3): ConquestSavedData, EconomyTick, ConquestSavedDataTest

### Community 13 - "BaseMap"
Cohesion: 0.24
Nodes (4): BaseMap, Nation, BaseMapStore, Ready

### Community 15 - "RoadNetwork"
Cohesion: 0.15
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 19 - "Realm"
Cohesion: 0.20
Nodes (4): Kind, FRIEND, RIVAL, Realm

### Community 20 - "BattleMap.java"
Cohesion: 0.04
Nodes (3): Battle map developer web page, Morale, rout and rally, RafasConquest

### Community 21 - ".build"
Cohesion: 0.12
Nodes (3): Frame, Harbour, CitySizesTest

### Community 22 - "CourtSet"
Cohesion: 0.23
Nodes (3): CourtSet, Seat, Style

### Community 23 - "Commands Guide"
Cohesion: 0.09
Nodes (30): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+22 more)

### Community 25 - "TitleMap"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.07
Nodes (20): WarHorn, Armoury, Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE (+12 more)

### Community 28 - "WarCouncil"
Cohesion: 0.17
Nodes (4): Facts, Officer, Talk, WarCouncil

### Community 29 - "Wars Service"
Cohesion: 0.13
Nodes (3): FeudDuel, MusterBars, Wars

### Community 30 - "Politics"
Cohesion: 0.12
Nodes (4): Feud, Lord, Feuds, Front

### Community 31 - "Land"
Cohesion: 0.16
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 32 - "Capital Layout & Seed Hash"
Cohesion: 0.09
Nodes (11): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, SeedHash (+3 more)

### Community 36 - "CourtView"
Cohesion: 0.10
Nodes (5): Camera, CourtView, Figure, OrganizationScreen, Organization

### Community 37 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.18
Nodes (5): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), TickWeave experiment

### Community 38 - "Countryside"
Cohesion: 0.13
Nodes (12): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+4 more)

### Community 39 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.11
Nodes (7): Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER, Side

### Community 42 - "War"
Cohesion: 0.10
Nodes (4): Army, Goal, PoliticsSync, War

### Community 43 - "HorizontalStretch"
Cohesion: 0.12
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 44 - "ConquestServer"
Cohesion: 0.14
Nodes (4): CityBanners, ConquestServer, StructureLink, PoiPrecomputer

### Community 45 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "DayClockTest"
Cohesion: 0.25
Nodes (3): DayClock, Step, DayClockTest

### Community 50 - "BattleRole"
Cohesion: 0.08
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 51 - ".generate"
Cohesion: 0.07
Nodes (14): ProvinceNames, WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry, Homeland, DESERT (+6 more)

### Community 52 - "CityPlan"
Cohesion: 0.24
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 53 - "BoardScreen"
Cohesion: 0.15
Nodes (4): BoardScreen, BoardAction, MarkerSet, MarkerView

### Community 54 - ".start"
Cohesion: 0.08
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 56 - "Builder"
Cohesion: 0.15
Nodes (8): BackBanner, Builder, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel

### Community 58 - "Faction Map View"
Cohesion: 0.12
Nodes (3): FactionMapScreen, MapView, RequestFactionInfo

### Community 60 - "Peerage"
Cohesion: 0.13
Nodes (5): Merit, Peer, Peerage, PeerageSync, QuestProgress

### Community 61 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.11
Nodes (5): CharacterScreen, Retainer, Spot, Banner, Portraits

### Community 62 - "Recruiter"
Cohesion: 0.05
Nodes (8): RecruiterRenderer, TownspersonRenderer, Walk, Recruiter, DistrictKey, Townsfolk, Townsperson, StreetWalksTest

### Community 63 - "TitleMap"
Cohesion: 0.15
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 69 - "Calibration"
Cohesion: 0.14
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 70 - "net.minecraft.world.level.levelgen.structure.Structure"
Cohesion: 0.10
Nodes (3): CitySlopeStructure, CityStructure, FarmStructure

### Community 72 - "City"
Cohesion: 0.11
Nodes (3): SeatCities, SeatCitiesTest, World

### Community 73 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.17
Nodes (3): Card, Enlisted, Unit

### Community 74 - "MapLands"
Cohesion: 0.06
Nodes (5): Cell, Terrain, Drop, MarkerScreen, MapLands

### Community 75 - ".drawEmpire"
Cohesion: 0.27
Nodes (3): Hit, LabelLayer, LabelShape

### Community 76 - "Poi"
Cohesion: 0.11
Nodes (11): FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, Poi, PoiType (+3 more)

### Community 77 - "DistantArmyRenderer"
Cohesion: 0.13
Nodes (3): DistantArmyRenderer, Figure, DistantShips

### Community 78 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 79 - "DistantArmyRenderer.java"
Cohesion: 0.15
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 81 - "City Walls"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.13
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 84 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.05
Nodes (12): BaseMapPart, BattleSnapshot, BattleSpot, BoardView, GrandStrategyView, PeerageView, PoliticsView, SetSetting (+4 more)

### Community 87 - "Battle Sounds"
Cohesion: 0.20
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 93 - ".noble"
Cohesion: 0.15
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, POST, WALL, Piece (+1 more)

### Community 95 - "Person"
Cohesion: 0.09
Nodes (11): ClientPeerage, Names, Honour, Honours, Stat, DIPLOMACY, INTRIGUE, LEARNING (+3 more)

### Community 96 - "ConquestWorldgen"
Cohesion: 0.13
Nodes (3): ConquestContent, ConquestEntities, ConquestWorldgen

### Community 98 - "Centre"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 100 - ".of"
Cohesion: 0.18
Nodes (4): FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 101 - "Arm"
Cohesion: 0.13
Nodes (11): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, FieldRules, Rates, Side (+3 more)

### Community 102 - "Battle Site"
Cohesion: 0.20
Nodes (5): BattleSite, Part, Water, BattleSite.Water, BattleSiteTest

### Community 103 - ".world"
Cohesion: 0.12
Nodes (3): Succession, RealmPlannerTest, World

### Community 107 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 108 - "Magic Schools"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 110 - "Quest"
Cohesion: 0.19
Nodes (5): Boards, Entry, View, Taken, Quest

### Community 111 - "MapLayer"
Cohesion: 0.18
Nodes (5): LegendEntry, MapLayer, Symbol, RoadLayer, UnitLayer

### Community 112 - "Lands"
Cohesion: 0.08
Nodes (9): Lands, Stronghold, Claim, Event, Muster, CEREMONIAL, OFFENSIVE, Politics (+1 more)

### Community 113 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.15
Nodes (3): CityView, StatsRequest, StatsView

### Community 114 - "Doctrine"
Cohesion: 0.06
Nodes (25): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender, Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT (+17 more)

### Community 118 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.12
Nodes (6): GrandStrategy, View, PlayerStats, View, Retinues, RetinuesTest

### Community 119 - "Race"
Cohesion: 0.20
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 120 - "Faction"
Cohesion: 0.10
Nodes (6): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, MapPreview, Faction, FactionInfoTest

### Community 121 - "Clash"
Cohesion: 0.31
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 122 - "BattlePlan"
Cohesion: 0.05
Nodes (9): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, WarPlan (+1 more)

### Community 123 - "Quest"
Cohesion: 0.16
Nodes (10): Ladder, MILITARY, NOBLE, NONE, Type, CLEAR, DELIVER, SCOUT (+2 more)

### Community 126 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 127 - "Role"
Cohesion: 0.17
Nodes (9): GameCharacter, Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN (+1 more)

### Community 130 - "Recruiter"
Cohesion: 0.20
Nodes (5): Offer, Recruitment, Option, Talk, View

### Community 132 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 136 - "Person"
Cohesion: 0.04
Nodes (46): Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Character page, Characters (holders, skills, prowess, traits), Command Table block, Countryside (hamlets, villages, watchtowers, castles) (+38 more)

### Community 139 - "FormationSnapshot"
Cohesion: 0.13
Nodes (5): FormationSnapshot, Positions, Sprite, DistantArmies, FormationSnapshotTest

### Community 140 - "NationScreen"
Cohesion: 0.09
Nodes (4): ClientPolitics, NationScreen, Demographics, Share

### Community 142 - "BattleType"
Cohesion: 0.13
Nodes (7): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome, Landing

### Community 143 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.07
Nodes (16): BattleShips, Helm, SableLink, SableShips, Lane, Leg, Port, SeaLanes (+8 more)

### Community 146 - "Kind"
Cohesion: 0.18
Nodes (7): Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN

### Community 149 - "UnitOrder"
Cohesion: 0.12
Nodes (16): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+8 more)

### Community 156 - ".plan"
Cohesion: 0.12
Nodes (6): Culture, Faith, Hold, Bond, RealmPlanner, Seat

### Community 179 - ".pack"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 204 - "Rank"
Cohesion: 0.14
Nodes (10): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+2 more)

### Community 206 - "Kind"
Cohesion: 0.18
Nodes (10): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, LORD, RETAINER (+2 more)

### Community 218 - ".player"
Cohesion: 0.18
Nodes (5): NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 219 - "Roads"
Cohesion: 0.10
Nodes (16): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+8 more)

### Community 221 - "CityGridBuilder"
Cohesion: 0.07
Nodes (9): CityGridBuilder, Ground, PlacedWall, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY (+1 more)

### Community 228 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 231 - "ArrowVolleys"
Cohesion: 0.23
Nodes (3): DistantDepth, Arrow, ArrowVolleys

### Community 248 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 249 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 250 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 255 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

## Ambiguous Edges - Review These
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **321 isolated node(s):** `CAPITAL`, `CITY`, `TOWN`, `CASTLE`, `LORD` (+316 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 910 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **161 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `org.jetbrains.annotations.Nullable`, `.enlist`, `net.minecraft.world.level.ChunkPos`, `net.minecraft.world.entity.Entity`, `.army`, `Trait`, `ConquestEvents.java`, `BattleType`, `.passOnOrders`, `BattleMap.java`, `UnitOrder`, `Commands Guide`, `net.minecraft.world.item.ItemStack`, `CityGridBuilder.java`, `WarCouncil`, `Wars Service`, `PveCommands Registration`, `.state`, `net.neoforged.bus.api.SubscribeEvent`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `rafasconquest.faction.Faction`, `.start`, `WarCamp`, `Peerage`, `.broadcast`, `Calibration`, `net.minecraft.world.phys.Vec3`, `org.jetbrains.annotations.Nullable`, `.battleName`, `net.minecraft.server.level.ServerPlayer`, `UnitMode`, `Battle Site`, `Shape`, `Phase`, `Role`?**
  _High betweenness centrality (0.076) - this node is a cross-community bridge._
- **What connects `CAPITAL`, `CITY`, `TOWN` to the rest of the system?**
  _321 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.06129476584022039 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `PoiPrecomputer.java`, `Recruiter`, `RecruitsBattle.java`, `Person`, `ConquestEvents.java`, `Retinues.java`, `RoadNetwork`, `Realm`, `BattleMap.java`, `TitleMap`, `net.minecraft.world.item.ItemStack`, `CityGridBuilder.java`, `WarCouncil`, `Wars Service`, `.place`, `CourtView`, `Countryside`, `ConquestServer`, `list`, `.generate`, `.postChunk`, `MapContext`, `Peerage`, `net.minecraft.client.gui.GuiGraphics`, `TitleMap`, `.onLevelLoad`, `MapLands`, `.choose`, `PoliticalStyle`, `ClientScreens`, `Roads`, `org.jetbrains.annotations.Nullable`, `.battleName`, `Person`, `Centre`, `Magic Schools`, `CountryStructure`, `GrandStrategyScreen`?**
  _High betweenness centrality (0.048) - this node is a cross-community bridge._
- **Should `PoiPrecomputer.java` be split into smaller, more focused modules?**
  _Cohesion score 0.07541478129713423 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._