# Graph Report - RafasConquest  (2026-11-05)

## Corpus Check
- 377 files · ~290,115 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 97 file(s) not represented in the graph (top: .nbt 84, (none) 3, .mcfunction 2)

## Summary
- 5178 nodes · 16386 edges · 263 communities (113 shown, 150 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2145 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `2d9e8ce6`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- list
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- net.minecraft.network.FriendlyByteBuf
- CityLayout
- CityVariant
- net.minecraft.world.level.block.state.BlockState
- net.minecraft.nbt.CompoundTag
- .block
- ConquestSavedData
- BaseMap
- SimpleChannel.java
- RoadNetwork
- .passOnOrders
- Battle Map Web Page & Site
- Person
- DistantHorizonsLink
- BattleMap.java
- PoliticsTest
- CourtSet
- Commands Guide
- CityManagerScreen
- TitleMap
- net.minecraft.world.item.ItemStack
- MarkerScreen
- WarCouncil
- rafasconquest.map.BaseMap
- Feud
- Land
- Capital Layout & Seed Hash
- PveCommands Registration
- Ship
- StyleLibrary
- CourtView
- CourtView.java
- DoctrineTest
- com.talhanation.recruits.entities.AbstractRecruitEntity
- rafasconquest.faction.Faction
- FactionInfoScreen
- .of
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
- net.minecraft.core.BlockPos
- .postChunk
- Faction Map View
- MapContext
- Peer
- .render
- Townsperson
- TitleMap
- Organizations
- net.minecraft.resources.ResourceLocation
- RecruitsBattle
- DistantHorizonsArmies.java
- .onLevelLoad
- Calibration
- net.minecraft.server.level.ServerLevel
- .touches
- .world
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
- ConquestNetwork
- Nation Rows
- Battle Sounds
- rafasconquest_world SavedData
- Terrain
- ConquestNetwork.java
- net.neoforged.neoforge.network.handling.IPayloadContext
- net.minecraft.world.level.block.Rotation
- .noble
- Stats & Settings Screens
- RafasConquest.java
- View
- ClientModEvents.java
- OrganizationScreen
- Arm
- Battle Site
- .world
- RecruitmentPost
- Call
- Magic Schools
- DistantHorizonsQuality.java
- TitleMap
- MapLayer
- Politics
- .plan
- Doctrine
- Settlement
- GrandStrategyScreen
- org.jetbrains.annotations.Nullable
- .view
- Homeland
- .of
- GameCharacter
- BattlePlan
- Quest
- CityStyles
- .measure
- Duty
- UnitOrder
- .enlist
- NationsTest
- Recruiter
- ShipHulls
- Trait
- .render
- BlockGrid
- Person
- .render
- PoliticalStyle
- FormationSnapshot
- ClientPolitics
- .walks
- BattleType
- SeaLanes
- CountryStructure
- PlaceholderPiece
- War
- .build
- CLAUDE.md
- CityTerrainPiece
- ConquestGameRules
- .plan
- .place
- Override
- Phase
- Politics.Front
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
- net.minecraft.world.entity.Entity
- CityLayoutTest
- .at
- DistantGeneration
- net.neoforged.bus.api.SubscribeEvent
- rafasconquest_world SavedData
- RecruiterScreen
- Clash
- Shape
- .handleUnitsRequest
- net.minecraft.server.level.ServerPlayer
- World map (CK3-style political map)
- UnitMode
- Kind
- net.minecraft.core.BlockPos
- TitleStyle
- Kind

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 248 edges
2. `FieldBattle` - 132 edges
3. `FieldBlock` - 120 edges
4. `ConquestNetwork` - 102 edges
5. `FieldArmy` - 94 edges
6. `BaseMap` - 85 edges
7. `Lands` - 83 edges
8. `Politics` - 76 edges
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

## Communities (263 total, 150 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.06
Nodes (6): FieldArmy, Event, FieldBattle, Order, Volley, FieldBlock

### Community 1 - "list"
Cohesion: 0.08
Nodes (8): Countryside (hamlets, villages, watchtowers, castles), Demographics (population by culture and race), Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, Nations by zone, Roads (high, city, town roads; bridges; signposts), Seat cities, Storm wall and war zone

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.09
Nodes (4): CityRoadPiece, CountryPiece, GoldChurchPiece, HarbourPiece

### Community 3 - ".army"
Cohesion: 0.08
Nodes (4): Landing, FieldBattleTest, GroundPlanTest, Land

### Community 4 - "CityTerrain"
Cohesion: 0.13
Nodes (13): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Plan (+5 more)

### Community 5 - "BattleScreen"
Cohesion: 0.09
Nodes (4): BattleScreen, Entry, Menu, UnitInfo

### Community 6 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.08
Nodes (7): BaseMapPart, BattleSnapshot, BattleSpot, PeerageView, PoliticsView, Volleys, WorldStretch

### Community 7 - "CityLayout"
Cohesion: 0.12
Nodes (4): CityLayout, District, Site, SlopeTile

### Community 8 - "CityVariant"
Cohesion: 0.19
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 9 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.09
Nodes (3): Saved, CommandTableBlock, MarkerBlock

### Community 10 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.05
Nodes (11): MarkerRenderer, Palette, WallTemplates, MarkerBlockEntity, MarkerSpec, Build, CASTLE, HAMLET (+3 more)

### Community 12 - "ConquestSavedData"
Cohesion: 0.09
Nodes (3): ConquestSavedData, EconomyTick, ConquestSavedDataTest

### Community 13 - "BaseMap"
Cohesion: 0.11
Nodes (5): MapPreview, BaseMap, Nation, BaseMapStore, Ready

### Community 15 - "RoadNetwork"
Cohesion: 0.16
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.23
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 22 - "CourtSet"
Cohesion: 0.23
Nodes (3): CourtSet, Seat, Style

### Community 23 - "Commands Guide"
Cohesion: 0.09
Nodes (30): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+22 more)

### Community 25 - "TitleMap"
Cohesion: 0.29
Nodes (4): Nation, Nations, Plan, Seats

### Community 26 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.06
Nodes (21): WarHorn, BackBanners, Armoury, Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL (+13 more)

### Community 28 - "WarCouncil"
Cohesion: 0.14
Nodes (4): Facts, Officer, Talk, WarCouncil

### Community 29 - "rafasconquest.map.BaseMap"
Cohesion: 0.13
Nodes (3): FeudDuel, PoliticsSync, Wars

### Community 30 - "Feud"
Cohesion: 0.09
Nodes (5): Feud, Lord, Feuds, Lands, Front

### Community 31 - "Land"
Cohesion: 0.16
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 32 - "Capital Layout & Seed Hash"
Cohesion: 0.12
Nodes (8): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, WorldSettings, CapitalRelocationTest, LayoutTest

### Community 34 - "Ship"
Cohesion: 0.17
Nodes (5): BattleShips, Helm, SableLink, SableShips, Ship

### Community 35 - "StyleLibrary"
Cohesion: 0.16
Nodes (3): Offsets, StyleLibrary, PlacementOffsetsTest

### Community 36 - "CourtView"
Cohesion: 0.08
Nodes (6): Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 37 - "CourtView.java"
Cohesion: 0.06
Nodes (8): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), TickWeave experiment, SeedHash

### Community 39 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.10
Nodes (8): Officers, Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER, Side

### Community 42 - ".of"
Cohesion: 0.23
Nodes (3): FactionBanner, Layer, FactionBannerTest

### Community 43 - "HorizontalStretch"
Cohesion: 0.07
Nodes (8): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, ProvinceMap, Terrain, Choice, HorizontalStretch, ProvinceMapTest

### Community 44 - "ConquestServer"
Cohesion: 0.09
Nodes (5): RecruitsBridge, CityBanners, ConquestServer, StructureLink, PoiPrecomputer

### Community 45 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "DayClockTest"
Cohesion: 0.25
Nodes (3): DayClock, Step, DayClockTest

### Community 50 - "BattleRole"
Cohesion: 0.07
Nodes (20): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Enlisted players, The general and guard, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Battle roles (frontline, bait, support, flanker, kill squad), Victory, pursuit and surrender, The war horn (+12 more)

### Community 51 - ".generate"
Cohesion: 0.08
Nodes (6): ProvinceNames, WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry

### Community 52 - "CityPlan"
Cohesion: 0.17
Nodes (4): CityPlan, Piece, Building, NobleDistrictTest

### Community 53 - "BoardScreen"
Cohesion: 0.13
Nodes (4): BoardScreen, BoardAction, MarkerSet, MarkerView

### Community 54 - ".start"
Cohesion: 0.10
Nodes (3): Point, Ground, GroundPlan

### Community 56 - "net.minecraft.core.BlockPos"
Cohesion: 0.12
Nodes (3): CharacterMarkers, Placed, StylePiece

### Community 59 - "MapContext"
Cohesion: 0.16
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 60 - "Peer"
Cohesion: 0.12
Nodes (8): Boards, Entry, View, Merit, Peer, Taken, Quest, QuestProgress

### Community 61 - ".render"
Cohesion: 0.07
Nodes (13): CharacterScreen, Retainer, Spot, ClientPeerage, Banner, Portraits, Tier, COUNTY (+5 more)

### Community 63 - "TitleMap"
Cohesion: 0.17
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "Organizations"
Cohesion: 0.33
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 67 - "DistantHorizonsArmies.java"
Cohesion: 0.11
Nodes (3): DistantHorizonsArmies, Figure, DistantShips

### Community 69 - "Calibration"
Cohesion: 0.17
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 70 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.05
Nodes (6): FactionBannerNbt, CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, FarmStructure

### Community 73 - "RecruitsBattle"
Cohesion: 0.15
Nodes (3): Card, Enlisted, Unit

### Community 75 - ".drawEmpire"
Cohesion: 0.23
Nodes (3): Hit, LabelLayer, LabelShape

### Community 76 - "Faction"
Cohesion: 0.08
Nodes (13): Faction, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, FactionInfo (+5 more)

### Community 77 - "DistantArmyRenderer"
Cohesion: 0.11
Nodes (5): DistantDepth, Arrow, ArrowVolleys, DistantArmyRenderer, Figure

### Community 78 - ".boundsAt"
Cohesion: 0.21
Nodes (5): Bounds, DistanceScaling, OriginRoller, Roll, DistanceScalingTest

### Community 79 - "DistantArmyRenderer.java"
Cohesion: 0.12
Nodes (4): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), BattleSpotRing

### Community 81 - "City Walls"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.08
Nodes (12): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+4 more)

### Community 84 - "ConquestNetwork"
Cohesion: 0.09
Nodes (11): CityClose, CityView, ConquestNetwork, FactionInfoReply, FigureStrike, HornOrders, RequestBaseMap, RequestFactionInfo (+3 more)

### Community 85 - "Nation Rows"
Cohesion: 0.10
Nodes (3): Fort, Stronghold, Row

### Community 87 - "Battle Sounds"
Cohesion: 0.11
Nodes (5): BattleSounds, ChargeSound, DinSound, Source, DistantArmies

### Community 90 - "ConquestNetwork.java"
Cohesion: 0.06
Nodes (6): Character page, Court 3D scene (CourtView), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), InventoryStatsButton, Ranks

### Community 91 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.14
Nodes (6): BackBanner, Builder, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 92 - "net.minecraft.world.level.block.Rotation"
Cohesion: 0.16
Nodes (3): PlacedWall, GoldChurch, Layout

### Community 93 - ".noble"
Cohesion: 0.12
Nodes (8): Kind, BARRACKS, CHURCH, NOBLE, PALACE, POST, WALL, Plan

### Community 96 - "RafasConquest.java"
Cohesion: 0.10
Nodes (4): ConquestContent, PoliticalMap, ConquestEntities, RafasConquest

### Community 98 - "View"
Cohesion: 0.12
Nodes (4): BoardView, GrandStrategyView, StatsView, TalkView

### Community 101 - "Arm"
Cohesion: 0.11
Nodes (11): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, FieldRules, Rates, Side (+3 more)

### Community 102 - "Battle Site"
Cohesion: 0.20
Nodes (5): BattleSite, Part, Water, BattleSite.Water, BattleSiteTest

### Community 103 - ".world"
Cohesion: 0.08
Nodes (5): Hook, Realm, Succession, RealmPlannerTest, World

### Community 105 - "RecruitmentPost"
Cohesion: 0.20
Nodes (3): RecruitmentPost, RecruitmentPostPiece, RecruitmentPostTest

### Community 107 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 108 - "Magic Schools"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 109 - "DistantHorizonsQuality.java"
Cohesion: 0.13
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 110 - "TitleMap"
Cohesion: 0.13
Nodes (5): City, Grid, SeatCities, SeatCitiesTest, World

### Community 111 - "MapLayer"
Cohesion: 0.13
Nodes (6): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer, UnitLayer

### Community 112 - "Politics"
Cohesion: 0.09
Nodes (7): Claim, Event, Muster, CEREMONIAL, OFFENSIVE, Politics, PoliticsData

### Community 114 - "Doctrine"
Cohesion: 0.06
Nodes (23): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+15 more)

### Community 115 - "Settlement"
Cohesion: 0.15
Nodes (10): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+2 more)

### Community 118 - ".view"
Cohesion: 0.13
Nodes (6): GrandStrategy, View, PlayerStats, View, Retinues, RetinuesTest

### Community 119 - "Homeland"
Cohesion: 0.09
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 121 - "GameCharacter"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 122 - "BattlePlan"
Cohesion: 0.07
Nodes (9): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, BattlePlanTest (+1 more)

### Community 123 - "Quest"
Cohesion: 0.18
Nodes (10): Ladder, MILITARY, NOBLE, NONE, Type, CLEAR, DELIVER, SCOUT (+2 more)

### Community 126 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 127 - "UnitOrder"
Cohesion: 0.12
Nodes (16): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+8 more)

### Community 130 - "Recruiter"
Cohesion: 0.12
Nodes (6): Recruiter, Offer, Recruitment, Option, Talk, View

### Community 131 - "ShipHulls"
Cohesion: 0.22
Nodes (3): Part, ShipHulls, Loaded

### Community 132 - "Trait"
Cohesion: 0.25
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 136 - "Person"
Cohesion: 0.09
Nodes (22): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Characters (holders, skills, prowess, traits), Cultures (seven homelands), Envelopment, Epic Knights optional armour (Armoury), Faiths and cults (+14 more)

### Community 139 - "FormationSnapshot"
Cohesion: 0.11
Nodes (11): FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN (+3 more)

### Community 141 - ".walks"
Cohesion: 0.22
Nodes (3): DistrictKey, Townsfolk, StreetWalksTest

### Community 142 - "BattleType"
Cohesion: 0.17
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 143 - "SeaLanes"
Cohesion: 0.15
Nodes (10): Lane, Leg, Port, SeaLanes, Size, LARGE, MEDIUM, SMALL (+2 more)

### Community 148 - "War"
Cohesion: 0.11
Nodes (3): Army, Goal, War

### Community 156 - ".plan"
Cohesion: 0.12
Nodes (8): Centre, Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 162 - "Override"
Cohesion: 0.24
Nodes (3): Land, Land, Land

### Community 163 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 179 - ".pack"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 181 - "net.minecraft.core.Vec3i"
Cohesion: 0.08
Nodes (4): CityPlacement, CountryPlacement, DojoPlacement, FarmPlacement

### Community 204 - "Rank"
Cohesion: 0.12
Nodes (10): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+2 more)

### Community 206 - "Kind"
Cohesion: 0.17
Nodes (11): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, LORD, RETAINER (+3 more)

### Community 218 - ".player"
Cohesion: 0.15
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 219 - "Roads"
Cohesion: 0.12
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 234 - "net.neoforged.bus.api.SubscribeEvent"
Cohesion: 0.04
Nodes (3): BattleKeepInventory, ClientEvents, ConquestEvents

### Community 247 - "Clash"
Cohesion: 0.28
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 248 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 254 - "World map (CK3-style political map)"
Cohesion: 0.33
Nodes (7): Base map generated from seed, Command Table block, Faction Info screen, World map layers, Province size setting, Provinces (ProvinceMap), World map (CK3-style political map)

### Community 255 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 256 - "Kind"
Cohesion: 0.33
Nodes (6): Kind, CLEAR_ABOVE, NONE, PLATE, SLOPE, WALL_BASE

### Community 257 - "net.minecraft.core.BlockPos"
Cohesion: 0.11
Nodes (4): GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, WorldMapItem

### Community 259 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

## Ambiguous Edges - Review These
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **324 isolated node(s):** `FAVOUR`, `SECRET`, `ARCHERS`, `CAVALRY`, `SHIELDWALL` (+319 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 918 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **150 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `BaseMap` connect `BaseMap` to `list`, `TitleStyle`, `Recruiter`, `PoliticalStyle`, `ConquestSavedData`, `RoadNetwork`, `net.minecraft.world.item.ItemStack`, `.plan`, `WarCouncil`, `rafasconquest.map.BaseMap`, `.place`, `CourtView`, `ConquestServer`, `.generate`, `.postChunk`, `MapContext`, `Peer`, `.render`, `.onLevelLoad`, `net.minecraft.server.level.ServerLevel`, `.choose`, `Terrain`, `ClientScreens`, `ConquestNetwork.java`, `RafasConquest.java`, `OrganizationScreen`, `.world`, `Magic Schools`, `TitleMap`, `Settlement`, `GrandStrategyScreen`, `org.jetbrains.annotations.Nullable`, `.view`, `Homeland`?**
  _High betweenness centrality (0.079) - this node is a cross-community bridge._
- **What connects `FAVOUR`, `SECRET`, `ARCHERS` to the rest of the system?**
  _324 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.05979543666404406 - nodes in this community are weakly interconnected._
- **Why does `RecruitsBattle` connect `RecruitsBattle` to `org.jetbrains.annotations.Nullable`, `.enlist`, `net.minecraft.world.level.ChunkPos`, `.army`, `UnitMode`, `.block`, `BaseMap`, `.passOnOrders`, `BattleMap.java`, `Commands Guide`, `net.minecraft.world.item.ItemStack`, `WarCouncil`, `rafasconquest.map.BaseMap`, `PveCommands Registration`, `Phase`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `rafasconquest.faction.Faction`, `.generate`, `WarCamp`, `Peer`, `Calibration`, `net.minecraft.server.level.ServerLevel`, `RecruitsBattle`, `net.minecraft.world.entity.Entity`, `Arm`, `Battle Site`, `net.neoforged.bus.api.SubscribeEvent`, `Doctrine`, `org.jetbrains.annotations.Nullable`, `Shape`, `net.minecraft.server.level.ServerPlayer`, `UnitOrder`?**
  _High betweenness centrality (0.053) - this node is a cross-community bridge._
- **Should `list` be split into smaller, more focused modules?**
  _Cohesion score 0.07927565392354124 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._