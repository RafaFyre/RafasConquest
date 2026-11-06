# Graph Report - RafasConquest  (2026-11-05)

## Corpus Check
- 375 files · ~287,576 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 97 file(s) not represented in the graph (top: .nbt 84, (none) 3, .mcfunction 2)

## Summary
- 5141 nodes · 16250 edges · 253 communities (112 shown, 141 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2111 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `d2549d83`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- list
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- RecruitsBattle.java
- WorldSettings
- CityVariant
- net.minecraft.world.level.block.state.BlockState
- net.minecraft.nbt.CompoundTag
- rafasconquest.battle.BattlePlan.Point
- ConquestSavedData
- BaseMap
- SimpleChannel.java
- RoadNetwork
- .passOnOrders
- Battle Map Web Page & Site
- net.minecraft.world.level.block.Rotation
- .hash
- BattleMap.java
- .build
- CourtSet
- Commands Guide
- CityManagerScreen
- TitleMap
- net.minecraft.world.item.ItemStack
- WarCouncil
- rafasconquest.map.BaseMap
- Feud
- Land
- Capital Layout & Seed Hash
- PveCommands Registration
- .state
- StyleLibrary
- CourtView
- Cities (grid, plots, vanilla village buildings)
- Kind
- com.talhanation.recruits.entities.AbstractRecruitEntity
- rafasconquest.faction.Faction
- FactionInfoScreen
- View
- HorizontalStretch
- ConquestServer
- Task
- Personalities
- DayClockTest
- .walks
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
- Settlement
- .broadcast
- DistantHorizonsArmies.java
- .onLevelLoad
- Calibration
- net.minecraft.server.level.ServerLevel
- .touches
- City
- RecruitsBattle
- MapLands
- .drawEmpire
- Faction
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
- .world
- org.jetbrains.annotations.Nullable
- BackBannerLayer.java
- .noble
- Stats & Settings Screens
- RafasConquest.java
- Centre
- .render
- OrganizationScreen
- Arm
- Battle Site
- Realm
- RecruitmentPost
- Call
- Magic Schools
- DistantHorizonsQuality.java
- SeatCitiesTest
- MapLayer
- Lands
- ConquestNetwork
- Doctrine
- CountryStructure
- GrandStrategyScreen
- Job
- Retinues
- Homeland
- .of
- WallTemplates
- BattlePlan
- Quest
- CityStyles
- .measure
- Duty
- World map (CK3-style political map)
- .enlist
- Profile
- Recruiter
- ShipHulls
- Trait
- .nearest
- BlockGrid
- Person
- .render
- TitleStyle
- FormationSnapshot
- ClientPolitics
- BattleType
- net.minecraft.server.level.ServerLevel
- Kind
- PoliticsTest
- UnitOrder
- CLAUDE.md
- CityTerrainPiece
- ConquestGameRules
- .plan
- .place
- net.neoforged.bus.api.SubscribeEvent
- Muster
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
- net.minecraft.server.level.ServerPlayer
- UnitMode
- ClientEvents.java
- RecruiterScreen
- Shape
- Phase
- Build
- net.minecraft.core.BlockPos
- PoliticsData

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 248 edges
2. `FieldBattle` - 132 edges
3. `FieldBlock` - 120 edges
4. `ConquestNetwork` - 102 edges
5. `FieldArmy` - 94 edges
6. `Lands` - 82 edges
7. `BaseMap` - 80 edges
8. `Politics` - 75 edges
9. `BattleScreen` - 60 edges
10. `ConquestSavedData` - 55 edges

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

## Communities (253 total, 141 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.06
Nodes (6): FieldArmy, Event, FieldBattle, Order, Volley, FieldBlock

### Community 1 - "list"
Cohesion: 0.08
Nodes (8): Countryside (hamlets, villages, watchtowers, castles), Demographics (population by culture and race), Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, Nations by zone, Roads (high, city, town roads; bridges; signposts), Seat cities, Storm wall and war zone

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.08
Nodes (6): CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourPiece, PlaceholderPiece

### Community 3 - ".army"
Cohesion: 0.09
Nodes (3): Point, DoctrineTest, FieldBattleTest

### Community 4 - "CityTerrain"
Cohesion: 0.07
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.09
Nodes (4): BattleScreen, Entry, Menu, UnitInfo

### Community 7 - "WorldSettings"
Cohesion: 0.10
Nodes (6): CityLayout, District, Site, SlopeTile, WorldSettings, CityLayoutTest

### Community 8 - "CityVariant"
Cohesion: 0.18
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 9 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.08
Nodes (4): Saved, CommandTableBlock, QuestBoardBlock, MarkerBlock

### Community 10 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.06
Nodes (4): MarkerRenderer, Placed, MarkerBlockEntity, MarkerSpec

### Community 12 - "ConquestSavedData"
Cohesion: 0.09
Nodes (3): ConquestSavedData, EconomyTick, ConquestSavedDataTest

### Community 13 - "BaseMap"
Cohesion: 0.09
Nodes (6): ClientMapCache, BaseMap, Nation, BaseMapStore, Ready, PoliticalStyle

### Community 15 - "RoadNetwork"
Cohesion: 0.19
Nodes (3): RoadNetwork, Stretch, RoadNetworkTest

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 19 - ".hash"
Cohesion: 0.16
Nodes (5): ChunkCoord, FarmLayout, OriginRoller, Roll, ProvinceNames

### Community 21 - ".build"
Cohesion: 0.18
Nodes (3): Frame, Harbour, CitySizesTest

### Community 22 - "CourtSet"
Cohesion: 0.21
Nodes (3): CourtSet, Seat, Style

### Community 23 - "Commands Guide"
Cohesion: 0.09
Nodes (30): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+22 more)

### Community 25 - "TitleMap"
Cohesion: 0.16
Nodes (6): Nation, Nations, Plan, Seats, NationsTest, World

### Community 26 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.08
Nodes (18): WarHorn, Armoury, Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE (+10 more)

### Community 28 - "WarCouncil"
Cohesion: 0.20
Nodes (4): Facts, Officer, Talk, WarCouncil

### Community 29 - "rafasconquest.map.BaseMap"
Cohesion: 0.08
Nodes (6): RankKits, Army, Goal, PoliticsSync, War, Wars

### Community 30 - "Feud"
Cohesion: 0.09
Nodes (8): Clash, Feud, Kind, DUEL, SKIRMISH, Lord, FeudDuel, Politics.Front

### Community 31 - "Land"
Cohesion: 0.17
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 32 - "Capital Layout & Seed Hash"
Cohesion: 0.16
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 35 - "StyleLibrary"
Cohesion: 0.16
Nodes (3): Offsets, StyleLibrary, PlacementOffsetsTest

### Community 36 - "CourtView"
Cohesion: 0.08
Nodes (6): Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 37 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.15
Nodes (7): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), TickWeave experiment

### Community 38 - "Kind"
Cohesion: 0.33
Nodes (6): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER

### Community 39 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.11
Nodes (8): Officers, Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER, Side

### Community 41 - "FactionInfoScreen"
Cohesion: 0.10
Nodes (5): FactionInfoScreen, Line, FactionBanner, Layer, FactionBannerNbt

### Community 42 - "View"
Cohesion: 0.12
Nodes (4): BoardView, GrandStrategyView, StatsView, TalkView

### Community 43 - "HorizontalStretch"
Cohesion: 0.12
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 44 - "ConquestServer"
Cohesion: 0.10
Nodes (5): RecruitsBridge, CityBanners, ConquestServer, StructureLink, PoiPrecomputer

### Community 45 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 48 - ".walks"
Cohesion: 0.14
Nodes (3): DistrictKey, Townsfolk, StreetWalksTest

### Community 50 - "BattleRole"
Cohesion: 0.17
Nodes (6): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT

### Community 51 - ".generate"
Cohesion: 0.09
Nodes (6): WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 52 - "CityPlan"
Cohesion: 0.21
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 53 - "BoardScreen"
Cohesion: 0.13
Nodes (4): BoardScreen, BoardAction, MarkerSet, MarkerView

### Community 54 - ".start"
Cohesion: 0.09
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 56 - "Builder"
Cohesion: 0.13
Nodes (6): BackBanner, Builder, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 60 - "Peerage"
Cohesion: 0.11
Nodes (9): Boards, Entry, View, Merit, Peer, Peerage, Taken, PeerageSync (+1 more)

### Community 61 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.09
Nodes (7): CharacterScreen, Spot, ClientPeerage, Banner, Portraits, Honour, Honours

### Community 62 - "Recruiter"
Cohesion: 0.09
Nodes (4): RecruiterRenderer, TownspersonRenderer, Walk, Townsperson

### Community 63 - "TitleMap"
Cohesion: 0.18
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 65 - "Settlement"
Cohesion: 0.18
Nodes (3): Settlement, CountryIndex, CountryPlacement

### Community 66 - ".broadcast"
Cohesion: 0.10
Nodes (3): Api, DistantGeneration, TickTimes

### Community 67 - "DistantHorizonsArmies.java"
Cohesion: 0.11
Nodes (3): DistantHorizonsArmies, Figure, DistantShips

### Community 69 - "Calibration"
Cohesion: 0.14
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 70 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.05
Nodes (7): CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, DojoPlacement, FarmPlacement, FarmStructure

### Community 72 - "City"
Cohesion: 0.09
Nodes (12): Countryside, Plan, City, Grid, SeatCities, Tier, COUNTY, DUCHY (+4 more)

### Community 73 - "RecruitsBattle"
Cohesion: 0.08
Nodes (4): Card, Enlisted, RecruitsBattle, Unit

### Community 74 - "MapLands"
Cohesion: 0.06
Nodes (5): Cell, Terrain, Drop, MarkerScreen, MapLands

### Community 75 - ".drawEmpire"
Cohesion: 0.23
Nodes (3): Hit, LabelLayer, LabelShape

### Community 76 - "Faction"
Cohesion: 0.06
Nodes (16): SeedHash, Faction, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD (+8 more)

### Community 77 - "DistantArmyRenderer"
Cohesion: 0.12
Nodes (3): DistantArmyRenderer, Figure, DistantHorizonsLink

### Community 78 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 79 - "DistantArmyRenderer.java"
Cohesion: 0.09
Nodes (6): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), DistantDepth, Arrow, ArrowVolleys

### Community 81 - "City Walls"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.14
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 84 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.07
Nodes (9): BattleSnapshot, BattleSpot, FactionInfoReply, PeerageView, PoliticsView, UnitsRequest, UnitsState, Volleys (+1 more)

### Community 87 - "Battle Sounds"
Cohesion: 0.20
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 93 - ".noble"
Cohesion: 0.12
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, POST, WALL, Piece (+1 more)

### Community 96 - "RafasConquest.java"
Cohesion: 0.11
Nodes (3): ConquestContent, ConquestEntities, RafasConquest

### Community 98 - "Centre"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 101 - "Arm"
Cohesion: 0.12
Nodes (11): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, FieldRules, Rates, Side (+3 more)

### Community 102 - "Battle Site"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 103 - "Realm"
Cohesion: 0.08
Nodes (8): Retainer, Person, Kind, FRIEND, RIVAL, Realm, Succession, CharacterMarkers

### Community 105 - "RecruitmentPost"
Cohesion: 0.23
Nodes (3): RecruitmentPost, RecruitmentPostPiece, RecruitmentPostTest

### Community 107 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 108 - "Magic Schools"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 109 - "DistantHorizonsQuality.java"
Cohesion: 0.10
Nodes (6): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits, DistantHorizonsQuality

### Community 111 - "MapLayer"
Cohesion: 0.14
Nodes (6): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer, UnitLayer

### Community 112 - "Lands"
Cohesion: 0.08
Nodes (6): Feuds, Lands, Stronghold, Claim, Front, Politics

### Community 113 - "ConquestNetwork"
Cohesion: 0.09
Nodes (10): CityClose, CityView, ConquestNetwork, FigureStrike, HornOrders, RequestBaseMap, RequestFactionInfo, SetSetting (+2 more)

### Community 114 - "Doctrine"
Cohesion: 0.05
Nodes (30): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+22 more)

### Community 117 - "Job"
Cohesion: 0.20
Nodes (6): Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY, SHIELDMAN

### Community 118 - "Retinues"
Cohesion: 0.23
Nodes (3): GrandStrategy, View, Retinues

### Community 119 - "Homeland"
Cohesion: 0.10
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 122 - "BattlePlan"
Cohesion: 0.05
Nodes (12): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, WarPlan (+4 more)

### Community 123 - "Quest"
Cohesion: 0.14
Nodes (11): Ladder, MILITARY, NOBLE, NONE, Quest, Type, CLEAR, DELIVER (+3 more)

### Community 126 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 127 - "World map (CK3-style political map)"
Cohesion: 0.33
Nodes (7): Base map generated from seed, Command Table block, Faction Info screen, World map layers, Province size setting, Provinces (ProvinceMap), World map (CK3-style political map)

### Community 130 - "Recruiter"
Cohesion: 0.17
Nodes (4): Recruiter, Option, Talk, View

### Community 131 - "ShipHulls"
Cohesion: 0.22
Nodes (3): Part, ShipHulls, Loaded

### Community 132 - "Trait"
Cohesion: 0.25
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 136 - "Person"
Cohesion: 0.05
Nodes (34): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Character page, Characters (holders, skills, prowess, traits), Court 3D scene (CourtView), Cultures (seven homelands) (+26 more)

### Community 139 - "FormationSnapshot"
Cohesion: 0.12
Nodes (5): FormationSnapshot, Positions, Sprite, DistantArmies, FormationSnapshotTest

### Community 142 - "BattleType"
Cohesion: 0.15
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 143 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.07
Nodes (16): BattleShips, Helm, SableLink, SableShips, Lane, Leg, Port, SeaLanes (+8 more)

### Community 146 - "Kind"
Cohesion: 0.25
Nodes (7): Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN

### Community 149 - "UnitOrder"
Cohesion: 0.12
Nodes (16): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+8 more)

### Community 156 - ".plan"
Cohesion: 0.12
Nodes (7): Centre, Culture, Faith, Hold, Bond, RealmPlanner, Seat

### Community 163 - "net.neoforged.bus.api.SubscribeEvent"
Cohesion: 0.04
Nodes (3): BattleKeepInventory, ClientModEvents, ConquestEvents

### Community 167 - "Muster"
Cohesion: 0.67
Nodes (3): Muster, CEREMONIAL, OFFENSIVE

### Community 179 - ".pack"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 204 - "Rank"
Cohesion: 0.07
Nodes (15): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+7 more)

### Community 206 - "Kind"
Cohesion: 0.18
Nodes (10): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, LORD, RETAINER (+2 more)

### Community 218 - ".player"
Cohesion: 0.15
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 219 - "Roads"
Cohesion: 0.11
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 221 - "CityGridBuilder"
Cohesion: 0.09
Nodes (3): CityGridBuilder, Ground, PlacedWall

### Community 228 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 234 - "ClientEvents.java"
Cohesion: 0.06
Nodes (3): BackBanners, BattleSpotRing, ClientEvents

### Community 248 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 250 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 255 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 257 - "net.minecraft.core.BlockPos"
Cohesion: 0.14
Nodes (3): GrandStrategyBlock, StrategyBoardBlock, WorldMapItem

## Ambiguous Edges - Review These
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **321 isolated node(s):** `COUNTY`, `DUCHY`, `KINGDOM`, `EMPIRE`, `FRIEND` (+316 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 911 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **141 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `.enlist`, `net.minecraft.world.level.ChunkPos`, `rafasconquest.battle.BattlePlan.Point`, `.passOnOrders`, `BattleMap.java`, `UnitOrder`, `CourtSet`, `Commands Guide`, `net.minecraft.world.item.ItemStack`, `WarCouncil`, `Feud`, `PveCommands Registration`, `.state`, `net.neoforged.bus.api.SubscribeEvent`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `rafasconquest.faction.Faction`, `.start`, `WarCamp`, `.broadcast`, `Calibration`, `net.minecraft.server.level.ServerLevel`, `Faction`, `org.jetbrains.annotations.Nullable`, `net.minecraft.server.level.ServerPlayer`, `UnitMode`, `Doctrine`, `Shape`, `Phase`?**
  _High betweenness centrality (0.084) - this node is a cross-community bridge._
- **What connects `COUNTY`, `DUCHY`, `KINGDOM` to the rest of the system?**
  _321 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.06221889055472264 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `list`, `Recruiter`, `RecruitsBattle.java`, `TitleStyle`, `ClientPolitics`, `RoadNetwork`, `net.minecraft.world.item.ItemStack`, `.plan`, `WarCouncil`, `rafasconquest.map.BaseMap`, `.place`, `ConquestServer`, `.generate`, `.postChunk`, `MapContext`, `Peerage`, `TitleMap`, `Settlement`, `.onLevelLoad`, `net.minecraft.server.level.ServerLevel`, `City`, `MapLands`, `Faction`, `.choose`, `Nation Rows`, `RafasConquest.java`, `OrganizationScreen`, `Realm`, `Magic Schools`, `GrandStrategyScreen`, `.of`, `Quest`?**
  _High betweenness centrality (0.047) - this node is a cross-community bridge._
- **Should `list` be split into smaller, more focused modules?**
  _Cohesion score 0.07572298325722983 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._