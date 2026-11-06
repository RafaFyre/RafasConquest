# Graph Report - RafasConquest  (2026-11-05)

## Corpus Check
- 376 files · ~289,253 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 97 file(s) not represented in the graph (top: .nbt 84, (none) 3, .mcfunction 2)

## Summary
- 5164 nodes · 16344 edges · 259 communities (103 shown, 156 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2134 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `7688eb38`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- InventoryStatsButton
- WorldSettings
- CityVariant
- net.minecraft.world.level.block.state.BlockState
- net.minecraft.nbt.CompoundTag
- BattleMap.java
- ConquestSavedData
- BaseMap
- SimpleChannel.java
- RoadNetwork
- .passOnOrders
- Battle Map Web Page & Site
- Person
- DistantHorizonsArmies
- BattleMap.java
- ConquestEvents
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
- StyleLibrary
- CourtView
- Cities (grid, plots, vanilla village buildings)
- Realm
- com.talhanation.recruits.entities.AbstractRecruitEntity
- rafasconquest.faction.Faction
- FactionInfoScreen
- .of
- HorizontalStretch
- ConquestServer
- Task
- Personalities
- DayClockTest
- .build
- BattleRole
- .generate
- CityPlan
- BoardScreen
- .start
- WarCamp
- .fire
- .postChunk
- Faction Map View
- MapContext
- Peer
- net.minecraft.client.gui.GuiGraphics
- Townsperson
- TitleMap
- Organizations
- net.minecraft.resources.ResourceLocation
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
- net.minecraft.server.level.ServerPlayer
- Nation Rows
- Battle Sounds
- rafasconquest_world SavedData
- Terrain
- RecruitsBattle.java
- BackBannerLayer.java
- .noble
- Stats & Settings Screens
- .stat
- RafasConquest.java
- Centre
- ClientModEvents.java
- CourtView
- Arm
- Battle Site
- .world
- RecruitmentPost
- Call
- Magic Schools
- DistantHorizonsQuality.java
- TitleMap
- MapLayer
- Lands
- Kit
- Doctrine
- Settlement
- GrandStrategyScreen
- BattleKeepInventory.java
- .view
- Race
- Faction
- GameCharacter
- BattlePlan
- Quest
- CityStyles
- .measure
- Duty
- Site
- .enlist
- Profile
- Recruiter
- ShipHulls
- Trait
- ArrowVolleys
- BlockGrid
- Person
- .render
- PoliticalStyle
- FormationSnapshot
- ClientPolitics
- .issue
- BattleType
- net.minecraft.server.level.ServerLevel
- Kind
- War
- .onLogout
- CLAUDE.md
- CityTerrainPiece
- ConquestGameRules
- .plan
- .place
- BaseMapStore
- Politics.Front
- Small Cluster 170
- ConquestConfig
- .pack
- CityPlacement
- Rank
- Kind
- ClientScreens
- .player
- Roads
- CityGridBuilder
- QuestBoardBlock
- ItemFlags.java
- DistantGeneration
- ClientEvents.java
- Daily tick with /time edge cases
- RecruiterScreen
- ClientMapCache
- Shape
- WarHorn
- CityManager
- Build
- net.minecraft.core.BlockPos

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 248 edges
2. `FieldBattle` - 132 edges
3. `FieldBlock` - 120 edges
4. `ConquestNetwork` - 102 edges
5. `FieldArmy` - 94 edges
6. `BaseMap` - 89 edges
7. `Lands` - 83 edges
8. `Politics` - 76 edges
9. `Person` - 63 edges
10. `BattleScreen` - 60 edges

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

## Communities (259 total, 156 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.06
Nodes (6): FieldArmy, Event, FieldBattle, Order, Volley, FieldBlock

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.07
Nodes (8): StylePiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourPiece, PlaceholderPiece, RecruitmentPostPiece

### Community 4 - "CityTerrain"
Cohesion: 0.07
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.06
Nodes (21): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+13 more)

### Community 7 - "WorldSettings"
Cohesion: 0.10
Nodes (5): District, ChunkCoord, FarmLayout, WorldSettings, CityLayoutTest

### Community 8 - "CityVariant"
Cohesion: 0.09
Nodes (9): PlacedWall, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA, GoldChurch (+1 more)

### Community 9 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.09
Nodes (3): Saved, CommandTableBlock, MarkerBlock

### Community 10 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.11
Nodes (3): MarkerRenderer, MarkerBlockEntity, MarkerSpec

### Community 11 - "BattleMap.java"
Cohesion: 0.11
Nodes (3): Battle map developer web page, Morale, rout and rally, BattleMap

### Community 12 - "ConquestSavedData"
Cohesion: 0.09
Nodes (5): ConquestSavedData, MapPreview, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 14 - "SimpleChannel.java"
Cohesion: 0.10
Nodes (7): Builder, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel

### Community 15 - "RoadNetwork"
Cohesion: 0.19
Nodes (3): RoadNetwork, Stretch, RoadNetworkTest

### Community 16 - ".passOnOrders"
Cohesion: 0.07
Nodes (3): Figure, Live, RecruitsBattle

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.24
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "DistantHorizonsArmies"
Cohesion: 0.13
Nodes (3): DistantHorizonsArmies, DistantHorizonsLink, DistantShips

### Community 22 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 23 - "Commands Guide"
Cohesion: 0.09
Nodes (30): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+22 more)

### Community 25 - "TitleMap"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.35
Nodes (3): Armoury, Outfit, Style

### Community 28 - "WarCouncil"
Cohesion: 0.17
Nodes (4): Facts, Officer, Talk, WarCouncil

### Community 30 - "Feud"
Cohesion: 0.09
Nodes (9): Clash, Feud, Kind, DUEL, SKIRMISH, Lord, FeudDuel, Feuds (+1 more)

### Community 31 - "Land"
Cohesion: 0.16
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 32 - "Capital Layout & Seed Hash"
Cohesion: 0.12
Nodes (7): CapitalLayout, Land, Site, OriginRoller, Roll, CapitalRelocationTest, LayoutTest

### Community 36 - "CourtView"
Cohesion: 0.15
Nodes (3): NationScreen, Demographics, Share

### Community 37 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.08
Nodes (21): Ambushes from woods, Base map generated from seed, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Faction Info screen, Deploying to the land (GroundPlan) (+13 more)

### Community 38 - "Realm"
Cohesion: 0.19
Nodes (4): Kind, FRIEND, RIVAL, Realm

### Community 39 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.12
Nodes (8): Officers, Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER, Side

### Community 42 - ".of"
Cohesion: 0.18
Nodes (4): FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 43 - "HorizontalStretch"
Cohesion: 0.13
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 44 - "ConquestServer"
Cohesion: 0.16
Nodes (3): CityBanners, ConquestServer, PoiPrecomputer

### Community 45 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 48 - ".build"
Cohesion: 0.09
Nodes (4): Frame, Harbour, CitySizesTest, StreetWalksTest

### Community 50 - "BattleRole"
Cohesion: 0.10
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 51 - ".generate"
Cohesion: 0.06
Nodes (15): ProvinceNames, WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors, Homeland (+7 more)

### Community 52 - "CityPlan"
Cohesion: 0.24
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 54 - ".start"
Cohesion: 0.10
Nodes (8): Point, Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 59 - "MapContext"
Cohesion: 0.17
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 60 - "Peer"
Cohesion: 0.15
Nodes (8): Advancement, Boards, Entry, View, Merit, Peer, Taken, Quest

### Community 61 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.11
Nodes (5): CharacterScreen, Retainer, Spot, Banner, Portraits

### Community 62 - "Townsperson"
Cohesion: 0.12
Nodes (4): Walk, DistrictKey, Townsfolk, Townsperson

### Community 64 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 66 - ".broadcast"
Cohesion: 0.09
Nodes (9): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT (+1 more)

### Community 69 - "Calibration"
Cohesion: 0.19
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 70 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.04
Nodes (11): CityLayout, StreetWalks, TerritoryMap, CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, CountryPlacement (+3 more)

### Community 72 - "City"
Cohesion: 0.14
Nodes (6): Countryside, Plan, City, Grid, CountrysideTest, World

### Community 73 - "RecruitsBattle"
Cohesion: 0.09
Nodes (9): Card, Enlisted, Unit, UnitMode, CHARGE, FOLLOW, HOLD, MOVE (+1 more)

### Community 75 - ".drawEmpire"
Cohesion: 0.23
Nodes (3): Hit, LabelLayer, LabelShape

### Community 76 - "Faction"
Cohesion: 0.07
Nodes (15): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoliticalMap (+7 more)

### Community 78 - ".boundsAt"
Cohesion: 0.35
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 79 - "DistantArmyRenderer.java"
Cohesion: 0.15
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 81 - "City Walls"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.20
Nodes (5): Candidate, OwnershipResolver, Claim, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.08
Nodes (12): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+4 more)

### Community 84 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.04
Nodes (28): BackBanner, BaseMapPart, BattleSnapshot, BattleSpot, BoardAction, BoardView, CityClose, CityView (+20 more)

### Community 85 - "Nation Rows"
Cohesion: 0.07
Nodes (5): Fort, Stronghold, Event, PoliticsTest, Row

### Community 87 - "Battle Sounds"
Cohesion: 0.20
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 90 - "RecruitsBattle.java"
Cohesion: 0.03
Nodes (6): Character page, Court 3D scene (CourtView), Demographics (population by culture and race), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), Ranks

### Community 93 - ".noble"
Cohesion: 0.15
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, POST, WALL, Piece (+1 more)

### Community 95 - ".stat"
Cohesion: 0.10
Nodes (10): ClientPeerage, Honour, Honours, Stat, DIPLOMACY, INTRIGUE, LEARNING, MARTIAL (+2 more)

### Community 96 - "RafasConquest.java"
Cohesion: 0.08
Nodes (3): ConquestContent, ConquestEntities, RafasConquest

### Community 98 - "Centre"
Cohesion: 0.17
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 100 - "CourtView"
Cohesion: 0.11
Nodes (5): Camera, CourtView, Figure, OrganizationScreen, Organization

### Community 101 - "Arm"
Cohesion: 0.12
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

### Community 109 - "DistantHorizonsQuality.java"
Cohesion: 0.10
Nodes (6): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits, DistantHorizonsQuality

### Community 110 - "TitleMap"
Cohesion: 0.10
Nodes (5): SeatCities, TitleMap, TitleStyle, SeatCitiesTest, World

### Community 111 - "MapLayer"
Cohesion: 0.11
Nodes (7): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer, TitleLayer, UnitLayer

### Community 112 - "Lands"
Cohesion: 0.08
Nodes (4): Lands, Claim, Politics, PoliticsData

### Community 113 - "Kit"
Cohesion: 0.14
Nodes (13): Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD, HEAVY_INFANTRY (+5 more)

### Community 114 - "Doctrine"
Cohesion: 0.07
Nodes (22): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+14 more)

### Community 115 - "Settlement"
Cohesion: 0.12
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 118 - ".view"
Cohesion: 0.11
Nodes (6): GrandStrategy, View, PlayerStats, View, QuestProgress, Retinues

### Community 119 - "Race"
Cohesion: 0.18
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 120 - "Faction"
Cohesion: 0.10
Nodes (3): RecruitsBridge, Faction, CityGuards

### Community 121 - "GameCharacter"
Cohesion: 0.17
Nodes (9): GameCharacter, Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN (+1 more)

### Community 122 - "BattlePlan"
Cohesion: 0.06
Nodes (10): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, BattlePlanTest (+2 more)

### Community 123 - "Quest"
Cohesion: 0.14
Nodes (10): Ladder, MILITARY, NOBLE, NONE, Type, CLEAR, DELIVER, SCOUT (+2 more)

### Community 126 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 130 - "Recruiter"
Cohesion: 0.13
Nodes (6): Recruiter, Offer, Recruitment, Option, Talk, View

### Community 131 - "ShipHulls"
Cohesion: 0.22
Nodes (3): Part, ShipHulls, Loaded

### Community 132 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 133 - "ArrowVolleys"
Cohesion: 0.23
Nodes (3): DistantDepth, Arrow, ArrowVolleys

### Community 136 - "Person"
Cohesion: 0.06
Nodes (30): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Braced walls against cavalry, Characters (holders, skills, prowess, traits), Cultures (seven homelands), Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Enlisted players (+22 more)

### Community 139 - "FormationSnapshot"
Cohesion: 0.13
Nodes (5): FormationSnapshot, Positions, Sprite, DistantArmies, FormationSnapshotTest

### Community 142 - "BattleType"
Cohesion: 0.11
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 143 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.07
Nodes (16): BattleShips, Helm, SableLink, SableShips, Lane, Leg, Port, SeaLanes (+8 more)

### Community 146 - "Kind"
Cohesion: 0.18
Nodes (7): Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN

### Community 148 - "War"
Cohesion: 0.09
Nodes (7): Army, Goal, Muster, CEREMONIAL, OFFENSIVE, PoliticsSync, War

### Community 156 - ".plan"
Cohesion: 0.15
Nodes (4): Faith, Bond, RealmPlanner, Seat

### Community 179 - ".pack"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 204 - "Rank"
Cohesion: 0.12
Nodes (10): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+2 more)

### Community 206 - "Kind"
Cohesion: 0.17
Nodes (11): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, LORD, RETAINER (+3 more)

### Community 218 - ".player"
Cohesion: 0.22
Nodes (5): Names, NobleTitle, COUNT, KNIGHT, NONE

### Community 219 - "Roads"
Cohesion: 0.12
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 248 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 255 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 257 - "net.minecraft.core.BlockPos"
Cohesion: 0.13
Nodes (3): GrandStrategyBlock, StrategyBoardBlock, WorldMapItem

## Ambiguous Edges - Review These
- `Roads (high, city, town roads; bridges; signposts)` → `Mustering (offensive and ceremonial)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **322 isolated node(s):** `CAPITAL`, `CITY`, `TOWN`, `CASTLE`, `LORD` (+317 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 911 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **156 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `.passOnOrders` to `org.jetbrains.annotations.Nullable`, `.enlist`, `net.minecraft.world.level.ChunkPos`, `Trait`, `BattleScreen`, `BattleMap.java`, `War`, `ConquestEvents`, `Commands Guide`, `net.minecraft.world.item.ItemStack`, `WarCouncil`, `PveCommands Registration`, `.state`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `rafasconquest.faction.Faction`, `WarCamp`, `.broadcast`, `net.minecraft.server.level.ServerLevel`, `RecruitsBattle`, `net.minecraft.server.level.ServerPlayer`, `RecruitsBattle.java`, `net.minecraft.world.entity.Entity`, `Battle Site`, `.view`, `Shape`, `GameCharacter`?**
  _High betweenness centrality (0.077) - this node is a cross-community bridge._
- **What connects `CAPITAL`, `CITY`, `TOWN` to the rest of the system?**
  _322 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.05688888888888889 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `list`, `Recruiter`, `PoliticalStyle`, `ClientPolitics`, `RoadNetwork`, `.style`, `.plan`, `WarCouncil`, `rafasconquest.map.BaseMap`, `.place`, `BaseMapStore`, `CourtView`, `Realm`, `ConquestServer`, `.generate`, `.postChunk`, `MapContext`, `Peer`, `net.minecraft.client.gui.GuiGraphics`, `.onLevelLoad`, `net.minecraft.server.level.ServerLevel`, `City`, `Faction`, `.choose`, `Terrain`, `RecruitsBattle.java`, `Centre`, `CourtView`, `Magic Schools`, `TitleMap`, `Kit`, `Settlement`, `GrandStrategyScreen`, `.view`, `ClientMapCache`?**
  _High betweenness centrality (0.066) - this node is a cross-community bridge._
- **Should `list` be split into smaller, more focused modules?**
  _Cohesion score 0.05415713196033562 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Roads (high, city, town roads; bridges; signposts)` and `Mustering (offensive and ceremonial)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._