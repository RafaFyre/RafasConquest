# Graph Report - RafasConquest  (2026-11-05)

## Corpus Check
- 377 files · ~292,600 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 97 file(s) not represented in the graph (top: .nbt 84, (none) 3, .mcfunction 2)

## Summary
- 5209 nodes · 16549 edges · 263 communities (104 shown, 159 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2199 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `f8f1f223`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- ProvinceMap
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- net.neoforged.bus.api.SubscribeEvent
- CityLayout
- list
- net.minecraft.client.Minecraft
- net.minecraft.nbt.CompoundTag
- BattleMap
- ConquestSavedData
- BaseMap
- net.neoforged.neoforge.network.handling.IPayloadContext
- RoadNetwork
- RecruitsBattle
- Battle Map Web Page & Site
- Person
- .passOnOrders
- BattleMap.java
- Politics
- .onLogout
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
- BattleKeepInventory
- StyleLibrary
- CourtView
- net.minecraft.world.level.saveddata.SavedData
- Kind
- net.minecraft.network.FriendlyByteBuf
- rafasconquest.faction.Faction
- FactionInfoScreen
- CourtView.java
- HorizontalStretch
- Task
- Personalities
- Ship
- BattleRole
- net.minecraft.world.level.biome.Biome
- CityPlan
- BoardScreen
- .blocksPlacedByHandBeforeTheBattleKeepTheirPlaces
- WarCamp
- .postChunk
- .broadcast
- Faction Map View
- MapContext
- Peer
- .render
- Townsperson
- TitleMap
- Organizations
- DistantArmyRenderer
- Poi
- DistantHorizonsArmies.java
- .onLevelLoad
- CourtSet
- optional
- .touches
- City
- UnitOrder
- MapLands
- .drawEmpire
- org.jetbrains.annotations.Nullable
- DistantArmyRenderer
- .boundsAt
- DistantArmyRenderer.java
- City Walls
- Ownership & Territory
- .choose
- ConquestNetwork
- Nation Rows
- map
- Battle Sounds
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- Terrain
- ConquestNetwork.java
- CityPlacement.java
- net.minecraft.world.level.block.Rotation
- .noble
- Stats & Settings Screens
- .generate
- ConquestContent.java
- View
- ClientModEvents.java
- OrganizationScreen
- Arm
- Battle Site
- .world
- .buildCell
- Call
- Magic Schools
- DistantHorizonsQuality.java
- TitleMap
- MapLayer
- Lands
- SeatCitiesTest
- Doctrine
- Settlement
- GrandStrategyScreen
- org.jetbrains.annotations.Nullable
- .view
- Homeland
- Faction
- GameCharacter
- BattlePlan
- Quest
- CityStyles
- .measure
- Duty
- Ranks
- NationsTest
- .plan
- Recruiter
- ShipHulls
- Trait
- Cities (grid, plots, vanilla village buildings)
- BlockGrid
- Person
- .render
- CityPlacement
- FormationSnapshot
- ClientPolitics
- Plan
- .at
- SeaLanes
- BackBannerLayer.java
- net.minecraft.nbt.CompoundTag
- War
- .build
- CLAUDE.md
- CityTerrainPiece
- ConquestGameRules
- .plan
- .place
- CountryPlacement
- Phase
- DojoPlacement
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
- Kind
- WorldFeatures
- Shape
- DistantGeneration
- ConquestEvents
- TitleStyle
- RecruiterScreen
- Kind
- ProvinceNames
- CityManager
- UnitMode
- net.minecraft.world.level.block.state.BlockState

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 247 edges
2. `FieldBattle` - 132 edges
3. `FieldBlock` - 120 edges
4. `ConquestNetwork` - 102 edges
5. `FieldArmy` - 94 edges
6. `BaseMap` - 94 edges
7. `Lands` - 89 edges
8. `Politics` - 77 edges
9. `Person` - 69 edges
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

## Communities (263 total, 159 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.06
Nodes (7): FieldArmy, Event, FieldBattle, Order, Volley, FieldBlock, Rates

### Community 1 - "ProvinceMap"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.09
Nodes (5): CityRoadPiece, CountryPiece, GoldChurchPiece, HarbourPiece, RecruitmentPostPiece

### Community 3 - ".army"
Cohesion: 0.08
Nodes (5): Point, DoctrineTest, FieldBattleTest, GroundPlanTest, Land

### Community 4 - "CityTerrain"
Cohesion: 0.07
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.09
Nodes (4): BattleScreen, Entry, Menu, UnitInfo

### Community 7 - "CityLayout"
Cohesion: 0.09
Nodes (6): CityLayout, District, Site, SlopeTile, WorldSettings, CityLayoutTest

### Community 8 - "list"
Cohesion: 0.08
Nodes (3): Mantra schools and dojos, Roads (high, city, town roads; bridges; signposts), Seat cities

### Community 9 - "net.minecraft.client.Minecraft"
Cohesion: 0.23
Nodes (3): DistantDepth, Arrow, ArrowVolleys

### Community 10 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.07
Nodes (5): MarkerRenderer, CharacterMarkers, Placed, MarkerBlockEntity, MarkerSpec

### Community 12 - "ConquestSavedData"
Cohesion: 0.07
Nodes (6): ConquestSavedData, CityBanners, ConquestServer, EconomyTick, PoiPrecomputer, ConquestSavedDataTest

### Community 13 - "BaseMap"
Cohesion: 0.09
Nodes (5): MapPreview, BaseMap, Nation, BaseMapStore, Ready

### Community 14 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.10
Nodes (8): BackBanner, Builder, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel

### Community 15 - "RoadNetwork"
Cohesion: 0.16
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 16 - "RecruitsBattle"
Cohesion: 0.03
Nodes (12): Card, Enlisted, Officers, RecruitsBattle, Role, BEARER, GENERAL, GUARD (+4 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.24
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 20 - "BattleMap.java"
Cohesion: 0.04
Nodes (3): Battle map developer web page, Morale, rout and rally, RafasConquest

### Community 21 - "Politics"
Cohesion: 0.08
Nodes (3): Event, Politics, PoliticsTest

### Community 23 - "Commands Guide"
Cohesion: 0.06
Nodes (38): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+30 more)

### Community 25 - "TitleMap"
Cohesion: 0.29
Nodes (4): Nation, Nations, Plan, Seats

### Community 26 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.06
Nodes (21): WarHorn, BackBanners, Armoury, Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL (+13 more)

### Community 28 - "WarCouncil"
Cohesion: 0.18
Nodes (4): Facts, Officer, Talk, WarCouncil

### Community 29 - "rafasconquest.map.BaseMap"
Cohesion: 0.11
Nodes (4): FeudDuel, Goal, PoliticsSync, Wars

### Community 30 - "Feud"
Cohesion: 0.09
Nodes (8): Clash, Feud, Kind, DUEL, SKIRMISH, Lord, Feuds, Front

### Community 31 - "Land"
Cohesion: 0.16
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 32 - "Capital Layout & Seed Hash"
Cohesion: 0.10
Nodes (9): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, CapitalRelocationTest (+1 more)

### Community 36 - "CourtView"
Cohesion: 0.08
Nodes (5): Retainer, Camera, CourtView, Figure, NationScreen

### Community 38 - "Kind"
Cohesion: 0.25
Nodes (7): Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN

### Community 39 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.06
Nodes (9): BaseMapPart, BattleSnapshot, BattleSpot, PeerageView, PoliticsView, UnitsRequest, UnitsState, Volleys (+1 more)

### Community 42 - "CourtView.java"
Cohesion: 0.09
Nodes (3): FactionBanner, Layer, FactionBannerNbt

### Community 43 - "HorizontalStretch"
Cohesion: 0.12
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 45 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "Ship"
Cohesion: 0.17
Nodes (5): BattleShips, Helm, SableLink, SableShips, Ship

### Community 50 - "BattleRole"
Cohesion: 0.10
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 51 - "net.minecraft.world.level.biome.Biome"
Cohesion: 0.15
Nodes (3): BiomePalette, Entry, MapColors

### Community 52 - "CityPlan"
Cohesion: 0.21
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 54 - ".blocksPlacedByHandBeforeTheBattleKeepTheirPlaces"
Cohesion: 0.07
Nodes (11): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Ground, GroundPlan, Landing (+3 more)

### Community 59 - "MapContext"
Cohesion: 0.17
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 60 - "Peer"
Cohesion: 0.19
Nodes (5): Boards, Entry, View, Taken, Quest

### Community 61 - ".render"
Cohesion: 0.10
Nodes (5): CharacterScreen, Spot, ClientPeerage, Banner, Portraits

### Community 62 - "Townsperson"
Cohesion: 0.06
Nodes (7): RecruiterRenderer, TownspersonRenderer, Walk, DistrictKey, Townsfolk, Townsperson, StreetWalksTest

### Community 63 - "TitleMap"
Cohesion: 0.18
Nodes (3): TitleMap, Land, TitleMapTest

### Community 64 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 66 - "Poi"
Cohesion: 0.15
Nodes (8): ResourceType, FOOD, Poi, PoiType, CAPITAL, CITY, FARM, StructureLink

### Community 69 - "CourtSet"
Cohesion: 0.23
Nodes (3): CourtSet, Seat, Style

### Community 70 - "optional"
Cohesion: 0.13
Nodes (5): CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, FarmStructure

### Community 72 - "City"
Cohesion: 0.20
Nodes (5): Countryside, Plan, Grid, CountrysideTest, World

### Community 73 - "UnitOrder"
Cohesion: 0.12
Nodes (16): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+8 more)

### Community 75 - ".drawEmpire"
Cohesion: 0.23
Nodes (3): Hit, LabelLayer, LabelShape

### Community 76 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.06
Nodes (8): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, PoliticalMap, PoliticalStyle, FactionInfo

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
Cohesion: 0.08
Nodes (14): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+6 more)

### Community 84 - "ConquestNetwork"
Cohesion: 0.06
Nodes (15): BoardAction, CityClose, CityView, ConquestNetwork, FactionInfoReply, FigureStrike, HornOrders, MarkerSet (+7 more)

### Community 87 - "Battle Sounds"
Cohesion: 0.21
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 92 - "net.minecraft.world.level.block.Rotation"
Cohesion: 0.16
Nodes (3): PlacedWall, GoldChurch, Layout

### Community 93 - ".noble"
Cohesion: 0.18
Nodes (8): Kind, BARRACKS, CHURCH, NOBLE, PALACE, POST, WALL, Piece

### Community 98 - "View"
Cohesion: 0.12
Nodes (4): BoardView, GrandStrategyView, StatsView, TalkView

### Community 101 - "Arm"
Cohesion: 0.07
Nodes (18): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, Calibration, Fit, Striker (+10 more)

### Community 102 - "Battle Site"
Cohesion: 0.20
Nodes (5): BattleSite, Part, Water, BattleSite.Water, BattleSiteTest

### Community 103 - ".world"
Cohesion: 0.09
Nodes (6): Hook, Realm, Succession, Taking, RealmPlannerTest, World

### Community 107 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 108 - "Magic Schools"
Cohesion: 0.14
Nodes (11): School, BLOOD, FIRE, ICE, IRON, LIGHT, LIGHTNING, SHADOW (+3 more)

### Community 109 - "DistantHorizonsQuality.java"
Cohesion: 0.10
Nodes (6): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits, DistantHorizonsQuality

### Community 111 - "MapLayer"
Cohesion: 0.10
Nodes (7): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer, TitleLayer, UnitLayer

### Community 114 - "Doctrine"
Cohesion: 0.06
Nodes (23): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+15 more)

### Community 115 - "Settlement"
Cohesion: 0.18
Nodes (3): Settlement, CountryIndex, CountryStructure

### Community 118 - ".view"
Cohesion: 0.10
Nodes (7): GrandStrategy, View, PlayerStats, View, QuestProgress, Retinues, RetinuesTest

### Community 119 - "Homeland"
Cohesion: 0.10
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 120 - "Faction"
Cohesion: 0.12
Nodes (5): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction, FactionInfoTest

### Community 121 - "GameCharacter"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 122 - "BattlePlan"
Cohesion: 0.06
Nodes (8): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, BattlePlanTest

### Community 123 - "Quest"
Cohesion: 0.16
Nodes (10): Ladder, MILITARY, NOBLE, NONE, Type, CLEAR, DELIVER, SCOUT (+2 more)

### Community 126 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 129 - ".plan"
Cohesion: 0.23
Nodes (4): Dojo, Dojos, Ground, DojosTest

### Community 130 - "Recruiter"
Cohesion: 0.13
Nodes (6): Recruiter, Offer, Recruitment, Option, Talk, View

### Community 131 - "ShipHulls"
Cohesion: 0.22
Nodes (3): Part, ShipHulls, Loaded

### Community 132 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 133 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.18
Nodes (5): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), TickWeave experiment

### Community 136 - "Person"
Cohesion: 0.05
Nodes (38): Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Character page, Characters (holders, skills, prowess, traits), Command Table block, Countryside (hamlets, villages, watchtowers, castles) (+30 more)

### Community 139 - "FormationSnapshot"
Cohesion: 0.13
Nodes (5): FormationSnapshot, Positions, Sprite, DistantArmies, FormationSnapshotTest

### Community 140 - "ClientPolitics"
Cohesion: 0.09
Nodes (8): ClientPolitics, Tier, COUNTY, DUCHY, EMPIRE, KINGDOM, Honour, Honours

### Community 143 - "SeaLanes"
Cohesion: 0.15
Nodes (10): Lane, Leg, Port, SeaLanes, Size, LARGE, MEDIUM, SMALL (+2 more)

### Community 146 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.08
Nodes (10): Palette, WallTemplates, Build, CASTLE, HAMLET, RUIN, TOWER, WELL (+2 more)

### Community 148 - "War"
Cohesion: 0.08
Nodes (7): Army, Stronghold, Muster, CEREMONIAL, OFFENSIVE, War, Politics.Front

### Community 149 - ".build"
Cohesion: 0.13
Nodes (3): Frame, Harbour, CitySizesTest

### Community 156 - ".plan"
Cohesion: 0.10
Nodes (10): Centre, Demographics, Share, Faith, Bond, Kind, FRIEND, RIVAL (+2 more)

### Community 163 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 179 - ".pack"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 204 - "Rank"
Cohesion: 0.14
Nodes (10): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+2 more)

### Community 206 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 218 - ".player"
Cohesion: 0.15
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 219 - "Roads"
Cohesion: 0.12
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 221 - "CityGridBuilder"
Cohesion: 0.09
Nodes (8): CityGridBuilder, Ground, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 223 - "Kind"
Cohesion: 0.33
Nodes (6): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER

### Community 231 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 247 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 255 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 257 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (6): CommandTableBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, WorldMapItem, MarkerBlock

## Ambiguous Edges - Review These
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **325 isolated node(s):** `ARCHERS`, `CAVALRY`, `SHIELDWALL`, `SWORDSMEN`, `FLANK` (+320 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 919 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **159 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `BaseMap` connect `BaseMap` to `.plan`, `Recruiter`, `list`, `ClientPolitics`, `ConquestSavedData`, `RoadNetwork`, `net.minecraft.world.item.ItemStack`, `.plan`, `WarCouncil`, `Feud`, `rafasconquest.map.BaseMap`, `.place`, `CourtView`, `.postChunk`, `MapContext`, `.render`, `.onLevelLoad`, `City`, `MapLands`, `org.jetbrains.annotations.Nullable`, `.choose`, `map`, `Terrain`, `ClientScreens`, `ConquestNetwork.java`, `.generate`, `OrganizationScreen`, `.world`, `.buildCell`, `Settlement`, `GrandStrategyScreen`, `TitleStyle`, `.view`?**
  _High betweenness centrality (0.079) - this node is a cross-community bridge._
- **What connects `ARCHERS`, `CAVALRY`, `SHIELDWALL` to the rest of the system?**
  _325 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.061442089830734375 - nodes in this community are weakly interconnected._
- **Why does `RecruitsBattle` connect `RecruitsBattle` to `org.jetbrains.annotations.Nullable`, `net.minecraft.world.level.ChunkPos`, `.army`, `Trait`, `UnitMode`, `BattleMap`, `BaseMap`, `.passOnOrders`, `BattleMap.java`, `net.minecraft.world.item.ItemStack`, `WarCouncil`, `rafasconquest.map.BaseMap`, `PveCommands Registration`, `Phase`, `rafasconquest.faction.Faction`, `.onServerTick`, `net.minecraft.world.level.biome.Biome`, `.blocksPlacedByHandBeforeTheBattleKeepTheirPlaces`, `WarCamp`, `.broadcast`, `UnitOrder`, `map`, `Arm`, `Battle Site`, `Shape`, `Doctrine`, `org.jetbrains.annotations.Nullable`, `Ranks`?**
  _High betweenness centrality (0.062) - this node is a cross-community bridge._
- **Should `net.minecraft.world.level.ChunkPos` be split into smaller, more focused modules?**
  _Cohesion score 0.089738430583501 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._