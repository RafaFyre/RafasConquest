# Graph Report - RafasConquest  (2026-11-05)

## Corpus Check
- 377 files · ~292,438 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 97 file(s) not represented in the graph (top: .nbt 84, (none) 3, .mcfunction 2)

## Summary
- 5208 nodes · 16543 edges · 245 communities (101 shown, 144 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2199 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `4d131eb0`
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
- CityVariant
- net.minecraft.client.Minecraft
- net.minecraft.nbt.CompoundTag
- .block
- ConquestSavedData
- BaseMap
- SimpleChannel.java
- RoadNetwork
- RecruitsBattle
- Battle Map Web Page & Site
- Person
- BattleMap.java
- PoliticsTest
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
- Build
- rafasconquest.faction.Faction
- FactionInfoScreen
- .of
- HorizontalStretch
- Task
- Personalities
- DayClockTest
- BattleRole
- .generate
- CityPlan
- BoardScreen
- .blocksPlacedByHandBeforeTheBattleKeepTheirPlaces
- WarCamp
- net.minecraft.core.BlockPos
- Faction Map View
- MapContext
- Peer
- .render
- Townsperson
- TitleMap
- Organizations
- net.minecraft.resources.ResourceLocation
- DistantHorizonsArmies.java
- .onLevelLoad
- Calibration
- CityLayout.java
- .touches
- City
- MapLands
- .drawEmpire
- org.jetbrains.annotations.Nullable
- DistantArmyRenderer
- .boundsAt
- DistantArmyRenderer.java
- City Walls
- Ownership & Territory
- .choose
- net.minecraft.server.level.ServerPlayer
- Nation Rows
- map
- Battle Sounds
- rafasconquest_world SavedData
- Terrain
- ConquestNetwork.java
- net.neoforged.neoforge.network.handling.IPayloadContext
- net.minecraft.world.level.block.Rotation
- .noble
- Stats & Settings Screens
- net.minecraft.server.level.ServerLevel
- .view
- ClientModEvents.java
- OrganizationScreen
- Arm
- Battle Site
- .world
- .postChunk
- Call
- Magic Schools
- DistantHorizonsQuality.java
- TitleMap
- MapLayer
- Lands
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
- Recruiter
- ShipHulls
- Trait
- BlockGrid
- Person
- .render
- PoliticalStyle
- FormationSnapshot
- ClientPolitics
- .roadStart
- BattleType
- SeaLanes
- PlaceholderPiece
- War
- .build
- CLAUDE.md
- CityTerrainPiece
- ConquestGameRules
- .plan
- .place
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
- CityLayoutTest
- DistantGeneration
- ConquestEvents
- RecruiterScreen
- CityManager
- UnitMode
- net.minecraft.world.level.block.state.BlockState
- Realm

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 248 edges
2. `FieldBattle` - 132 edges
3. `FieldBlock` - 120 edges
4. `ConquestNetwork` - 102 edges
5. `BaseMap` - 94 edges
6. `FieldArmy` - 94 edges
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

## Communities (245 total, 144 thin omitted)

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
Cohesion: 0.06
Nodes (5): Point, Outcome, TickTimes, DoctrineTest, FieldBattleTest

### Community 4 - "CityTerrain"
Cohesion: 0.07
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.06
Nodes (20): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+12 more)

### Community 7 - "CityLayout"
Cohesion: 0.13
Nodes (3): District, Site, SlopeTile

### Community 8 - "CityVariant"
Cohesion: 0.16
Nodes (7): PlacedWall, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 10 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.07
Nodes (5): MarkerRenderer, Palette, WallTemplates, MarkerBlockEntity, MarkerSpec

### Community 12 - "ConquestSavedData"
Cohesion: 0.07
Nodes (6): ConquestSavedData, ConquestServer, StructureLink, EconomyTick, PoiPrecomputer, ConquestSavedDataTest

### Community 13 - "BaseMap"
Cohesion: 0.15
Nodes (4): BaseMap, Nation, BaseMapStore, Ready

### Community 15 - "RoadNetwork"
Cohesion: 0.16
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 16 - "RecruitsBattle"
Cohesion: 0.03
Nodes (20): Card, Enlisted, Figure, GeneralTask, Live, Officers, RecruitsBattle, Role (+12 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.25
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 23 - "Commands Guide"
Cohesion: 0.09
Nodes (30): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+22 more)

### Community 25 - "TitleMap"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.05
Nodes (22): WarHorn, BackBannerLayer, BackBanners, Armoury, Kit, ARCHER_GUARD, CAPTAIN, CAVALRY (+14 more)

### Community 28 - "WarCouncil"
Cohesion: 0.20
Nodes (4): Facts, Officer, Talk, WarCouncil

### Community 29 - "rafasconquest.map.BaseMap"
Cohesion: 0.09
Nodes (8): Clash, Kind, DUEL, SKIRMISH, FeudDuel, MusterBars, PoliticsSync, Wars

### Community 30 - "Feud"
Cohesion: 0.11
Nodes (5): Feud, Lord, Feuds, Front, Politics

### Community 31 - "Land"
Cohesion: 0.19
Nodes (4): Heap, Land, Search, ShippingRoutes

### Community 32 - "Capital Layout & Seed Hash"
Cohesion: 0.10
Nodes (8): CapitalLayout, Land, Site, OriginRoller, Roll, WorldSettings, CapitalRelocationTest, LayoutTest

### Community 35 - "StyleLibrary"
Cohesion: 0.15
Nodes (3): Loaded, StyleLibrary, PlacementOffsetsTest

### Community 36 - "CourtView"
Cohesion: 0.09
Nodes (4): Camera, CourtView, Figure, NationScreen

### Community 38 - "Kind"
Cohesion: 0.25
Nodes (7): Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN

### Community 39 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 42 - ".of"
Cohesion: 0.17
Nodes (4): FactionBanner, Layer, CityBanners, FactionBannerTest

### Community 43 - "HorizontalStretch"
Cohesion: 0.13
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 45 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 50 - "BattleRole"
Cohesion: 0.08
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 51 - ".generate"
Cohesion: 0.07
Nodes (7): ProvinceNames, WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 52 - "CityPlan"
Cohesion: 0.24
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 54 - ".blocksPlacedByHandBeforeTheBattleKeepTheirPlaces"
Cohesion: 0.09
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 55 - "WarCamp"
Cohesion: 0.12
Nodes (5): Saved, WarCamp, CourtSet, Seat, Style

### Community 59 - "MapContext"
Cohesion: 0.17
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 60 - "Peer"
Cohesion: 0.13
Nodes (8): Boards, Entry, View, Merit, Peer, Taken, Quest, QuestProgress

### Community 61 - ".render"
Cohesion: 0.07
Nodes (8): CharacterScreen, Retainer, Spot, ClientPeerage, Banner, Portraits, Honour, Honours

### Community 63 - "TitleMap"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Organizations"
Cohesion: 0.18
Nodes (4): Organization, Organizations, Sanctum, OrganizationsTest

### Community 69 - "Calibration"
Cohesion: 0.19
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 70 - "CityLayout.java"
Cohesion: 0.06
Nodes (8): CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 72 - "City"
Cohesion: 0.13
Nodes (5): Countryside, Plan, City, CountrysideTest, World

### Community 76 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.06
Nodes (11): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, FactionInfo, Poi, PoiType (+3 more)

### Community 77 - "DistantArmyRenderer"
Cohesion: 0.10
Nodes (5): DistantDepth, DistantArmyRenderer, Figure, DistantHorizonsLink, DistantShips

### Community 78 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 79 - "DistantArmyRenderer.java"
Cohesion: 0.15
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 81 - "City Walls"
Cohesion: 0.15
Nodes (8): CityLayout, Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.08
Nodes (12): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+4 more)

### Community 84 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.03
Nodes (29): BackBanner, BaseMapPart, BattleSnapshot, BattleSpot, BoardAction, BoardView, CityClose, CityView (+21 more)

### Community 87 - "Battle Sounds"
Cohesion: 0.18
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 89 - "Terrain"
Cohesion: 0.20
Nodes (3): Cell, Terrain, UnitLayer

### Community 90 - "ConquestNetwork.java"
Cohesion: 0.05
Nodes (4): Character page, Court 3D scene (CourtView), Nation page, InventoryStatsButton

### Community 91 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.13
Nodes (5): Builder, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 93 - ".noble"
Cohesion: 0.13
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, POST, WALL, Piece (+1 more)

### Community 96 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.05
Nodes (4): ConquestContent, FactionBannerNbt, ConquestEntities, RafasConquest

### Community 98 - ".view"
Cohesion: 0.32
Nodes (3): Option, Talk, View

### Community 101 - "Arm"
Cohesion: 0.07
Nodes (10): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, FieldRules, Side, FLANK (+2 more)

### Community 102 - "Battle Site"
Cohesion: 0.20
Nodes (5): BattleSite, Part, Water, BattleSite.Water, BattleSiteTest

### Community 103 - ".world"
Cohesion: 0.11
Nodes (5): Succession, Taking, RetinuesTest, RealmPlannerTest, World

### Community 105 - ".postChunk"
Cohesion: 0.11
Nodes (3): RecruitmentPost, RecruitmentPosts, RecruitmentPostTest

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
Cohesion: 0.13
Nodes (4): Grid, SeatCities, SeatCitiesTest, World

### Community 111 - "MapLayer"
Cohesion: 0.11
Nodes (6): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer, TitleLayer

### Community 112 - "Lands"
Cohesion: 0.07
Nodes (6): Lands, Stronghold, Claim, Muster, CEREMONIAL, OFFENSIVE

### Community 114 - "Doctrine"
Cohesion: 0.06
Nodes (23): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+15 more)

### Community 115 - "Settlement"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 117 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.03
Nodes (9): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Enlisted players, The general and guard, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Battle roles (frontline, bait, support, flanker, kill squad), TickWeave experiment, Victory, pursuit and surrender (+1 more)

### Community 118 - ".view"
Cohesion: 0.19
Nodes (3): GrandStrategy, View, Retinues

### Community 119 - "Homeland"
Cohesion: 0.09
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 120 - "Faction"
Cohesion: 0.09
Nodes (8): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction, ResourceType, FOOD, CityGuards, FactionInfoTest

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

### Community 132 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 136 - "Person"
Cohesion: 0.05
Nodes (42): Ambushes from woods, Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards (+34 more)

### Community 138 - "PoliticalStyle"
Cohesion: 0.14
Nodes (4): MapPreview, PoliticalMap, LabelShape, PoliticalStyle

### Community 139 - "FormationSnapshot"
Cohesion: 0.14
Nodes (5): FormationSnapshot, Positions, Sprite, DistantArmies, FormationSnapshotTest

### Community 141 - ".roadStart"
Cohesion: 0.13
Nodes (3): DistrictKey, Townsfolk, StreetWalksTest

### Community 142 - "BattleType"
Cohesion: 0.18
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Landing

### Community 143 - "SeaLanes"
Cohesion: 0.07
Nodes (16): BattleShips, Helm, SableLink, SableShips, Lane, Leg, Port, SeaLanes (+8 more)

### Community 148 - "War"
Cohesion: 0.12
Nodes (3): Army, Goal, War

### Community 149 - ".build"
Cohesion: 0.19
Nodes (3): Frame, Harbour, CitySizesTest

### Community 156 - ".plan"
Cohesion: 0.14
Nodes (7): Centre, Demographics, Share, Faith, Bond, RealmPlanner, Seat

### Community 163 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 179 - ".pack"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 181 - "net.minecraft.core.Vec3i"
Cohesion: 0.12
Nodes (3): Offsets, StylePiece, CityPlacement

### Community 204 - "Rank"
Cohesion: 0.08
Nodes (15): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+7 more)

### Community 206 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 218 - ".player"
Cohesion: 0.15
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 219 - "Roads"
Cohesion: 0.13
Nodes (15): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+7 more)

### Community 228 - "CityLayoutTest"
Cohesion: 0.20
Nodes (3): ChunkCoord, FarmLayout, CityLayoutTest

### Community 255 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 257 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (6): CommandTableBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, WorldMapItem, MarkerBlock

### Community 259 - "Realm"
Cohesion: 0.10
Nodes (8): Hook, Kind, FAVOUR, SECRET, Kind, FRIEND, RIVAL, Realm

## Ambiguous Edges - Review These
- `Roads (high, city, town roads; bridges; signposts)` → `Mustering (offensive and ceremonial)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **325 isolated node(s):** `CAPITAL`, `CITY`, `TOWN`, `CASTLE`, `LORD` (+320 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 919 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **144 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `BaseMap` connect `BaseMap` to `Realm`, `PoliticalStyle`, `ClientPolitics`, `ConquestSavedData`, `RoadNetwork`, `BattleMap.java`, `net.minecraft.world.item.ItemStack`, `.plan`, `WarCouncil`, `rafasconquest.map.BaseMap`, `.place`, `CourtView`, `list`, `.generate`, `MapContext`, `Peer`, `.render`, `TitleMap`, `.onLevelLoad`, `CityLayout.java`, `City`, `MapLands`, `org.jetbrains.annotations.Nullable`, `.choose`, `map`, `Terrain`, `ClientScreens`, `ConquestNetwork.java`, `net.minecraft.server.level.ServerLevel`, `.view`, `OrganizationScreen`, `.postChunk`, `Magic Schools`, `Settlement`, `GrandStrategyScreen`, `.view`?**
  _High betweenness centrality (0.073) - this node is a cross-community bridge._
- **What connects `CAPITAL`, `CITY`, `TOWN` to the rest of the system?**
  _325 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.062186695569706005 - nodes in this community are weakly interconnected._
- **Why does `RecruitsBattle` connect `RecruitsBattle` to `org.jetbrains.annotations.Nullable`, `net.minecraft.world.level.ChunkPos`, `.army`, `Trait`, `BattleScreen`, `.block`, `BattleType`, `BattleMap.java`, `Commands Guide`, `net.minecraft.world.item.ItemStack`, `WarCouncil`, `rafasconquest.map.BaseMap`, `PveCommands Registration`, `Phase`, `rafasconquest.faction.Faction`, `.onServerTick`, `.generate`, `.blocksPlacedByHandBeforeTheBattleKeepTheirPlaces`, `WarCamp`, `net.minecraft.server.level.ServerPlayer`, `net.minecraft.server.level.ServerLevel`, `Arm`, `Battle Site`, `Doctrine`, `org.jetbrains.annotations.Nullable`, `UnitMode`?**
  _High betweenness centrality (0.064) - this node is a cross-community bridge._
- **Should `net.minecraft.world.level.ChunkPos` be split into smaller, more focused modules?**
  _Cohesion score 0.08780903665814152 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Roads (high, city, town roads; bridges; signposts)` and `Mustering (offensive and ceremonial)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._