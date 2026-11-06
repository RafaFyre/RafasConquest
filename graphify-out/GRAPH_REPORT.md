# Graph Report - RafasConquest  (2026-11-05)

## Corpus Check
- 379 files · ~296,374 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 97 file(s) not represented in the graph (top: .nbt 84, (none) 3, .mcfunction 2)

## Summary
- 5255 nodes · 16691 edges · 251 communities (106 shown, 145 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2218 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `38e568e4`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- ProvinceMap
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- PoiPrecomputer
- CityLayout
- list
- NationScreen
- MarkerSpec
- .state
- ConquestServer
- BaseMap
- net.neoforged.neoforge.network.handling.IPayloadContext
- RoadNetwork
- RecruitsBattle
- Battle Map Web Page & Site
- Person
- Plan
- BattleMap.java
- Politics
- Calibration
- Commands Guide
- CityManagerScreen
- TitleMap
- net.minecraft.world.item.ItemStack
- MarkerScreen
- WarCouncil
- rafasconquest.map.BaseMap
- Front
- .postChunk
- Capital Layout & Seed Hash
- PveCommands Registration
- .boxMax
- StyleLibrary
- .plan
- net.minecraft.world.level.saveddata.SavedData
- Realm
- net.minecraft.network.FriendlyByteBuf
- rafasconquest.faction.Faction
- FactionInfoScreen
- .shift
- HorizontalStretch
- PoliticalStyle
- UnitOrder
- Personalities
- .world
- Recruiter
- Job
- ClientModEvents.java
- BattleKeepInventory.java
- .openMarker
- .enlist
- WarCamp
- .sample
- ArrowVolleys
- Faction Map View
- MapContext
- Peerage
- .render
- ClientEvents
- TitleMap
- .render
- DistantArmyRenderer
- .onLogout
- DistantHorizonsArmies.java
- CourtSet
- PoiPrecomputer.java
- .touches
- City
- GameCharacter
- MapLands
- .drawEmpire
- ConquestEvents.java
- Strategy
- .boundsAt
- DistantArmyRenderer.java
- City Walls
- Ownership & Territory
- .choose
- ConquestNetwork
- Nation Rows
- map
- BattleSounds
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- Override
- ConquestNetwork.java
- RafasConquest (PvE addon for Villager Recruits)
- net.minecraft.world.level.block.Rotation
- CityPlan
- Stats & Settings Screens
- .generate
- RafasConquest.java
- View
- Phase
- OrganizationScreen
- net.minecraft.world.entity.Entity
- .world
- .postChunk
- Call
- Magic Schools
- DistantHorizonsQuality.java
- .plan
- BattleRole
- Lands
- BoardScreen
- Doctrine
- CountryStructure
- GrandStrategyScreen
- RecruitsBattle.java
- .view
- Homeland
- .candidateChunk
- GameCharacter
- BattlePlan
- Quest
- CityStyles
- .measure
- ConquestConfig
- Kind
- Characters (holders, skills, prowess, traits)
- UnitInfo
- .view
- ShipHulls
- Trait
- Distribution
- BlockGrid
- MapLayer
- WarPlan
- FormationSnapshot
- ClientPolitics
- .nearest
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- SeaLanes
- net.minecraft.nbt.CompoundTag
- Politics.Front
- .build
- CLAUDE.md
- CityTerrainPiece
- ConquestGameRules
- .plan
- ShipKind
- Small Cluster 170
- Kind
- ConquestSavedData
- .pack
- net.minecraft.core.Vec3i
- Rank
- Kind
- ClientScreens
- .player
- Roads
- CityGridBuilder
- net.neoforged.bus.api.SubscribeEvent
- RecruiterScreen
- Kind
- net.minecraft.server.level.ServerPlayer
- UnitMode
- net.minecraft.world.level.block.state.BlockState

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 249 edges
2. `FieldBattle` - 139 edges
3. `FieldBlock` - 128 edges
4. `ConquestNetwork` - 102 edges
5. `FieldArmy` - 97 edges
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

## Communities (251 total, 145 thin omitted)

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
Nodes (6): Point, Outcome, TickTimes, DoctrineTest, FieldBattleTest, ArmouryTest

### Community 4 - "CityTerrain"
Cohesion: 0.10
Nodes (14): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Plan (+6 more)

### Community 5 - "BattleScreen"
Cohesion: 0.09
Nodes (3): BattleScreen, Entry, Menu

### Community 7 - "CityLayout"
Cohesion: 0.09
Nodes (6): CityLayout, District, Site, SlopeTile, WorldSettings, CityLayoutTest

### Community 9 - "NationScreen"
Cohesion: 0.08
Nodes (6): Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 10 - "MarkerSpec"
Cohesion: 0.10
Nodes (3): MarkerRenderer, MarkerBlockEntity, MarkerSpec

### Community 12 - "ConquestServer"
Cohesion: 0.12
Nodes (4): RecruitsBridge, CityGuards, ConquestServer, StructureLink

### Community 13 - "BaseMap"
Cohesion: 0.09
Nodes (5): BaseMap, Nation, BaseMapStore, Ready, ConquestRuntime

### Community 14 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.08
Nodes (8): BackBanner, Builder, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel

### Community 15 - "RoadNetwork"
Cohesion: 0.09
Nodes (5): RoadFeature, Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 16 - "RecruitsBattle"
Cohesion: 0.03
Nodes (19): Card, Enlisted, Figure, Landing, Live, RecruitsBattle, Role, BEARER (+11 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.14
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 19 - "Plan"
Cohesion: 0.12
Nodes (8): Kind, BARRACKS, CHURCH, NOBLE, PALACE, POST, WALL, Plan

### Community 21 - "Politics"
Cohesion: 0.06
Nodes (8): Army, Event, Muster, CEREMONIAL, OFFENSIVE, Politics, War, PoliticsTest

### Community 22 - "Calibration"
Cohesion: 0.18
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 23 - "Commands Guide"
Cohesion: 0.06
Nodes (37): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+29 more)

### Community 25 - "TitleMap"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.06
Nodes (20): WarHorn, BackBannerLayer, BackBanners, Armoury, Kit, ARCHER_GUARD, CAPTAIN, CAVALRY (+12 more)

### Community 27 - "MarkerScreen"
Cohesion: 0.10
Nodes (5): BattleSite.Water, Cell, Terrain, Drop, MarkerScreen

### Community 28 - "WarCouncil"
Cohesion: 0.20
Nodes (4): Facts, Officer, Talk, WarCouncil

### Community 29 - "rafasconquest.map.BaseMap"
Cohesion: 0.09
Nodes (4): FeudDuel, MusterBars, PoliticsSync, Wars

### Community 30 - "Front"
Cohesion: 0.10
Nodes (8): Clash, Feud, Kind, DUEL, SKIRMISH, Lord, Feuds, Front

### Community 32 - "Capital Layout & Seed Hash"
Cohesion: 0.11
Nodes (9): CapitalLayout, Land, Site, OriginRoller, Roll, ProvinceNames, SeedHash, CapitalRelocationTest (+1 more)

### Community 36 - ".plan"
Cohesion: 0.26
Nodes (3): Heap, Search, ShippingRoutes

### Community 38 - "Realm"
Cohesion: 0.15
Nodes (5): Kind, FRIEND, RIVAL, Realm, CharacterMarkers

### Community 39 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.05
Nodes (11): BaseMapPart, BattleSnapshot, BattleSpot, FigureStrike, HornOrders, PeerageView, PoliticsView, SetSetting (+3 more)

### Community 40 - "rafasconquest.faction.Faction"
Cohesion: 0.10
Nodes (3): Api, DistantGeneration, RecruitsFactions

### Community 41 - "FactionInfoScreen"
Cohesion: 0.10
Nodes (6): FactionInfoScreen, Line, FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 42 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 43 - "HorizontalStretch"
Cohesion: 0.12
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 45 - "UnitOrder"
Cohesion: 0.12
Nodes (16): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+8 more)

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - ".world"
Cohesion: 0.15
Nodes (4): Honour, Honours, RealmPlannerTest, World

### Community 48 - "Recruiter"
Cohesion: 0.05
Nodes (8): RecruiterRenderer, TownspersonRenderer, Walk, Recruiter, DistrictKey, Townsfolk, Townsperson, StreetWalksTest

### Community 50 - "Job"
Cohesion: 0.22
Nodes (6): Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY, SHIELDMAN

### Community 53 - ".openMarker"
Cohesion: 0.24
Nodes (3): BoardAction, MarkerSet, MarkerView

### Community 54 - ".enlist"
Cohesion: 0.06
Nodes (17): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Task, AMBUSH, BAIT (+9 more)

### Community 57 - "ArrowVolleys"
Cohesion: 0.23
Nodes (3): DistantDepth, Arrow, ArrowVolleys

### Community 60 - "Peerage"
Cohesion: 0.14
Nodes (6): Merit, Peer, Peerage, Taken, PeerageSync, QuestProgress

### Community 61 - ".render"
Cohesion: 0.09
Nodes (5): CharacterScreen, Spot, ClientPeerage, Banner, Portraits

### Community 63 - "TitleMap"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 65 - "DistantArmyRenderer"
Cohesion: 0.13
Nodes (3): DistantArmyRenderer, Figure, DistantShips

### Community 69 - "CourtSet"
Cohesion: 0.23
Nodes (3): CourtSet, Seat, Style

### Community 70 - "PoiPrecomputer.java"
Cohesion: 0.03
Nodes (21): Faction, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, FactionInfo (+13 more)

### Community 72 - "City"
Cohesion: 0.12
Nodes (12): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+4 more)

### Community 75 - ".drawEmpire"
Cohesion: 0.27
Nodes (3): Hit, LabelLayer, LabelShape

### Community 77 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

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
Cohesion: 0.14
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 84 - "ConquestNetwork"
Cohesion: 0.09
Nodes (9): CityClose, CityView, ConquestNetwork, FactionInfoReply, RequestBaseMap, RequestFactionInfo, StatsRequest, StatsView (+1 more)

### Community 87 - "BattleSounds"
Cohesion: 0.20
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "Override"
Cohesion: 0.24
Nodes (3): Land, Land, Land

### Community 91 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 93 - "CityPlan"
Cohesion: 0.17
Nodes (4): CityPlan, Piece, Building, NobleDistrictTest

### Community 95 - ".generate"
Cohesion: 0.07
Nodes (7): Dojo, WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 96 - "RafasConquest.java"
Cohesion: 0.06
Nodes (7): Ambushes from woods, Cities (grid, plots, vanilla village buildings), Deploying to the land (GroundPlan), ConquestContent, PoliticalMap, ConquestEntities, RafasConquest

### Community 98 - "View"
Cohesion: 0.19
Nodes (3): BoardView, GrandStrategyView, TalkView

### Community 99 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 101 - "net.minecraft.world.entity.Entity"
Cohesion: 0.05
Nodes (23): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, Duty, BEARER, FLEEING (+15 more)

### Community 103 - ".world"
Cohesion: 0.14
Nodes (3): Hook, Succession, Taking

### Community 107 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 108 - "Magic Schools"
Cohesion: 0.09
Nodes (14): Dojos, Ground, School, BLOOD, FIRE, ICE, IRON, LIGHT (+6 more)

### Community 110 - ".plan"
Cohesion: 0.10
Nodes (6): City, Grid, SeatCities, CityIndex, SeatCitiesTest, World

### Community 111 - "BattleRole"
Cohesion: 0.25
Nodes (6): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT

### Community 112 - "Lands"
Cohesion: 0.11
Nodes (4): Goal, Lands, Stronghold, Claim

### Community 114 - "Doctrine"
Cohesion: 0.16
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 117 - "RecruitsBattle.java"
Cohesion: 0.05
Nodes (3): Envelopment, TickWeave experiment, War camps

### Community 118 - ".view"
Cohesion: 0.17
Nodes (4): GrandStrategy, View, Retinues, RetinuesTest

### Community 119 - "Homeland"
Cohesion: 0.08
Nodes (17): Retainer, Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP (+9 more)

### Community 121 - "GameCharacter"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 122 - "BattlePlan"
Cohesion: 0.07
Nodes (8): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, BattlePlanTest

### Community 123 - "Quest"
Cohesion: 0.13
Nodes (11): Ladder, MILITARY, NOBLE, NONE, Quest, Type, CLEAR, DELIVER (+3 more)

### Community 127 - "Kind"
Cohesion: 0.33
Nodes (6): Kind, CLEAR_ABOVE, NONE, PLATE, SLOPE, WALL_BASE

### Community 128 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.05
Nodes (39): Base map generated from seed, Battles (prototype), Blocks of 20 soldiers, Character page, Characters (holders, skills, prowess, traits), City wall, banners and wall guards, Command Table block, Countryside (hamlets, villages, watchtowers, castles) (+31 more)

### Community 130 - ".view"
Cohesion: 0.42
Nodes (3): Option, Talk, View

### Community 132 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 133 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 137 - "MapLayer"
Cohesion: 0.13
Nodes (7): BaseMapLayer, LegendEntry, MapLayer, Symbol, RoadLayer, TerritoryLayer, UnitLayer

### Community 139 - "FormationSnapshot"
Cohesion: 0.13
Nodes (5): FormationSnapshot, Positions, Sprite, DistantArmies, FormationSnapshotTest

### Community 142 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.33
Nodes (4): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender

### Community 143 - "SeaLanes"
Cohesion: 0.05
Nodes (20): Hull, Ramming, BattleShips, Helm, Ram, SableLink, SableShips, Lane (+12 more)

### Community 146 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.07
Nodes (11): Palette, WallTemplates, Placed, Build, CASTLE, HAMLET, RUIN, TOWER (+3 more)

### Community 149 - ".build"
Cohesion: 0.13
Nodes (3): Ground, Frame, Harbour

### Community 156 - ".plan"
Cohesion: 0.16
Nodes (5): Centre, Faith, Bond, RealmPlanner, Seat

### Community 162 - "ShipKind"
Cohesion: 0.40
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 174 - "Kind"
Cohesion: 0.18
Nodes (7): Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN

### Community 178 - "ConquestSavedData"
Cohesion: 0.09
Nodes (3): ConquestSavedData, EconomyTick, ConquestSavedDataTest

### Community 179 - ".pack"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 181 - "net.minecraft.core.Vec3i"
Cohesion: 0.12
Nodes (3): Offsets, StylePiece, CityPlacement

### Community 204 - "Rank"
Cohesion: 0.07
Nodes (14): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+6 more)

### Community 206 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 218 - ".player"
Cohesion: 0.15
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 219 - "Roads"
Cohesion: 0.10
Nodes (16): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Land (+8 more)

### Community 221 - "CityGridBuilder"
Cohesion: 0.09
Nodes (8): CityGridBuilder, PlacedWall, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 247 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 250 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.16
Nodes (6): Boards, Entry, View, CityManager, PlayerStats, View

### Community 255 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 257 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (6): CommandTableBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, WorldMapItem, MarkerBlock

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Roads (high, city, town roads; bridges; signposts)` → `Mustering (offensive and ceremonial)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **329 isolated node(s):** `SOLDIER`, `LEADER`, `GENERAL`, `BEARER`, `GUARD` (+324 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 932 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **145 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `BaseMap` connect `BaseMap` to `.view`, `PoiPrecomputer`, `list`, `NationScreen`, `ClientPolitics`, `RoadNetwork`, `net.minecraft.world.item.ItemStack`, `MarkerScreen`, `.plan`, `WarCouncil`, `rafasconquest.map.BaseMap`, `.postChunk`, `Realm`, `PoliticalStyle`, `.sample`, `MapContext`, `Peerage`, `.render`, `TitleMap`, `.onLevelLoad`, `PoiPrecomputer.java`, `City`, `MapLands`, `ConquestEvents.java`, `.choose`, `map`, `ClientScreens`, `ConquestNetwork.java`, `.generate`, `RafasConquest.java`, `OrganizationScreen`, `.plan`, `CountryStructure`, `GrandStrategyScreen`, `.view`, `Homeland`?**
  _High betweenness centrality (0.082) - this node is a cross-community bridge._
- **What connects `SOLDIER`, `LEADER`, `GENERAL` to the rest of the system?**
  _329 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.05891472868217054 - nodes in this community are weakly interconnected._
- **Why does `RecruitsBattle` connect `RecruitsBattle` to `org.jetbrains.annotations.Nullable`, `net.minecraft.world.level.ChunkPos`, `.army`, `Trait`, `.onServerTick`, `.state`, `BaseMap`, `BattleMap.java`, `Calibration`, `net.minecraft.world.item.ItemStack`, `WarCouncil`, `rafasconquest.map.BaseMap`, `PveCommands Registration`, `rafasconquest.faction.Faction`, `UnitOrder`, `BattleKeepInventory.java`, `.enlist`, `WarCamp`, `.sample`, `Peerage`, `GameCharacter`, `.generate`, `RafasConquest.java`, `Phase`, `net.minecraft.world.entity.Entity`, `net.neoforged.bus.api.SubscribeEvent`, `RecruitsBattle.java`, `net.minecraft.server.level.ServerPlayer`, `UnitMode`?**
  _High betweenness centrality (0.071) - this node is a cross-community bridge._
- **Should `net.minecraft.world.level.ChunkPos` be split into smaller, more focused modules?**
  _Cohesion score 0.08772635814889336 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Roads (high, city, town roads; bridges; signposts)` and `Mustering (offensive and ceremonial)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._