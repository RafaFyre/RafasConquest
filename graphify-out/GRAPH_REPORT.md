# Graph Report - RafasConquest  (2026-11-05)

## Corpus Check
- 371 files · ~275,481 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 97 file(s) not represented in the graph (top: .nbt 84, (none) 3, .mcfunction 2)

## Summary
- 4974 nodes · 15503 edges · 253 communities (107 shown, 146 thin omitted)
- Extraction: 88% EXTRACTED · 12% INFERRED · 0% AMBIGUOUS · INFERRED: 1899 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `f57f61d6`
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
- .passOnOrders
- net.minecraft.world.level.block.state.BlockState
- ConquestContent.java
- Politics
- ConquestSavedData
- PoliticalStyle
- ProvinceMap
- RoadNetwork
- CityGridBuilder
- Battle Map Web Page & Site
- CityVariant
- Realm
- BattleMap.java
- .roadStart
- CourtSet
- Commands Guide
- CityManagerScreen
- TitleMap
- net.minecraft.world.item.ItemStack
- CityLayout.java
- War Council & War Plans
- Wars Service
- Land
- Capital Layout & Seed Hash
- PveCommands Registration
- Point
- StyleLibrary
- CourtView
- Cities (grid, plots, vanilla village buildings)
- BaseMap
- RecruitsBattle
- net.minecraft.server.level.ServerLevel
- FactionInfoScreen
- .send
- HorizontalStretch
- net.minecraft.nbt.CompoundTag
- Task
- Personalities
- DayClockTest
- War
- rafasconquest.people.Person
- .generate
- CityPlan
- BoardScreen
- RafasConquest (PvE addon for Villager Recruits)
- WarCamp
- CityLayoutTest
- .postChunk
- Faction Map View
- MapContext
- Quest
- .render
- Recruiter
- TitleMap
- Person
- net.minecraft.core.BlockPos
- DistantArmies
- DistantHorizonsArmies.java
- .onLevelLoad
- Battle Calibration
- .postChunk
- .touches
- City
- Characters (holders, skills, prowess, traits)
- Map Lands
- .drawEmpire
- View
- DistantArmyRenderer
- .boundsAt
- DistantArmyRenderer.java
- City Walls
- Ownership & Territory
- .choose
- net.neoforged.neoforge.network.handling.IPayloadContext
- Nation Rows
- Builder
- Battle Sounds
- rafasconquest_world SavedData
- PoiPrecomputer.java
- org.jetbrains.annotations.Nullable
- ClientEvents
- Rank
- .noble
- Stats & Settings Screens
- ClientModEvents.java
- .officers
- RecruiterScreen
- .render
- Ranks (Soldier to Grand Marshal)
- Determinism rules
- Battle Site
- Realm
- Strategy
- Ranks
- Magic Schools
- RafasConquest.java
- Phase
- ArrowVolleys
- PoliticsTest
- Client Politics
- Doctrine
- Settlement
- GrandStrategyScreen
- .onRenderLevel
- .view
- Race
- .accept
- Homeland
- BattlePlan
- Quest
- CityStyles
- .measure
- Duty
- .drawAligned
- Terrain
- Role
- net.neoforged.fml.common.EventBusSubscriber
- ShipHulls
- Trait
- .model
- Distribution
- WarPlan
- ConquestGameRules
- Organization Screen
- FormationSnapshot
- NationScreen
- MarkerScreen
- .begin
- net.minecraft.server.level.ServerLevel
- .nearest
- MapLayer
- .view
- net.minecraft.world.level.saveddata.SavedData
- .world
- CLAUDE.md
- Kind
- CityTerrainPiece
- net.minecraft.network.FriendlyByteBuf
- .plan
- Shape
- ConquestEvents
- .sendBattleSpot
- Small Cluster 170
- PlacementOffsetsTest.java
- .pack
- GoldChurch.java
- Muster
- net.minecraft.server.level.ServerPlayer
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- Roads
- UnitMode
- BlockGrid
- ConquestNetwork

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 249 edges
2. `FieldBattle` - 141 edges
3. `FieldBlock` - 120 edges
4. `ConquestNetwork` - 104 edges
5. `FieldArmy` - 94 edges
6. `BaseMap` - 94 edges
7. `ConquestSavedData` - 60 edges
8. `BattleScreen` - 60 edges
9. `Politics` - 52 edges
10. `Lands` - 51 edges

## Surprising Connections (you probably didn't know these)
- `Roads and cities` --conceptually_related_to--> `City slots (walls, common, noble, landmarks)`  [INFERRED]
  docs/index.html → src/main/resources/rafasconquest_structures/README.txt
- `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)` --conceptually_related_to--> `/pve debug battle simulate`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf → RafasConquest-Commands.pdf
- `Battles (centre and two wings)` --conceptually_related_to--> `Battle Map web page`  [INFERRED]
  docs/index.html → src/main/resources/assets/rafasconquest/web/battle_map.html
- `Battle blocks (files, ranks, arm, duty, wing)` --conceptually_related_to--> `Battles (centre and two wings)`  [INFERRED]
  src/main/resources/assets/rafasconquest/web/battle_map.html → docs/index.html
- `Land titles (counties, duchies, kingdoms, empires)` --conceptually_related_to--> `Lone castles (small, medium, large, grand)`  [INFERRED]
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

## Communities (253 total, 146 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.06
Nodes (9): FieldArmy, Event, FieldBattle, Order, Volley, FieldBlock, Rates, Ground (+1 more)

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.07
Nodes (7): CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourPiece, PlaceholderPiece, RecruitmentPostPiece

### Community 3 - ".army"
Cohesion: 0.08
Nodes (4): TickTimes, DoctrineTest, FieldBattleTest, ArmouryTest

### Community 4 - "CityTerrain"
Cohesion: 0.07
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.06
Nodes (19): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+11 more)

### Community 6 - "RecruitsBattle.java"
Cohesion: 0.04
Nodes (6): Character page, Court 3D scene (CourtView), Demographics (population by culture and race), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), InventoryStatsButton

### Community 7 - "WorldSettings"
Cohesion: 0.09
Nodes (4): District, Site, SlopeTile, Ground

### Community 9 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (5): CommandTableBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, MarkerBlock

### Community 10 - "ConquestContent.java"
Cohesion: 0.10
Nodes (4): MarkerRenderer, ConquestContent, WorldMapItem, MarkerBlockEntity

### Community 11 - "Politics"
Cohesion: 0.11
Nodes (5): Goal, Lands, Claim, Front, Politics

### Community 12 - "ConquestSavedData"
Cohesion: 0.07
Nodes (5): ConquestSavedData, Poi, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 13 - "PoliticalStyle"
Cohesion: 0.14
Nodes (4): MapPreview, PoliticalMap, LabelShape, PoliticalStyle

### Community 14 - "ProvinceMap"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 15 - "RoadNetwork"
Cohesion: 0.09
Nodes (5): RoadFeature, Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 16 - "CityGridBuilder"
Cohesion: 0.13
Nodes (3): Houses, CityGridBuilder, PlacedWall

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "CityVariant"
Cohesion: 0.19
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 19 - "Realm"
Cohesion: 0.16
Nodes (4): Kind, FRIEND, RIVAL, Realm

### Community 21 - ".roadStart"
Cohesion: 0.11
Nodes (4): DistrictKey, Townsfolk, CitySizesTest, StreetWalksTest

### Community 22 - "CourtSet"
Cohesion: 0.23
Nodes (3): CourtSet, Seat, Style

### Community 23 - "Commands Guide"
Cohesion: 0.09
Nodes (29): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+21 more)

### Community 25 - "TitleMap"
Cohesion: 0.16
Nodes (6): Nation, Nations, Plan, Seats, NationsTest, World

### Community 26 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.06
Nodes (21): WarHorn, BackBannerLayer, BackBanners, Armoury, Kit, ARCHER_GUARD, CAPTAIN, CAVALRY (+13 more)

### Community 27 - "CityLayout.java"
Cohesion: 0.05
Nodes (8): CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 28 - "War Council & War Plans"
Cohesion: 0.17
Nodes (4): Facts, Officer, Talk, WarCouncil

### Community 31 - "Land"
Cohesion: 0.16
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 32 - "Capital Layout & Seed Hash"
Cohesion: 0.09
Nodes (8): CapitalLayout, Land, Site, OriginRoller, Roll, WorldSettings, CapitalRelocationTest, LayoutTest

### Community 34 - "Point"
Cohesion: 0.06
Nodes (7): Point, BattleMap, GroundPlanTest, Land, Land, Land, Land

### Community 36 - "CourtView"
Cohesion: 0.18
Nodes (3): Camera, CourtView, Figure

### Community 37 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.08
Nodes (21): Ambushes from woods, Base map generated from seed, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Faction Info screen, Deploying to the land (GroundPlan) (+13 more)

### Community 38 - "BaseMap"
Cohesion: 0.11
Nodes (5): ClientMapCache, BaseMap, Nation, BaseMapStore, Ready

### Community 39 - "RecruitsBattle"
Cohesion: 0.04
Nodes (12): Card, Enlisted, Figure, GeneralTask, RecruitsBattle, Role, BEARER, GENERAL (+4 more)

### Community 40 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.14
Nodes (3): Api, DistantGeneration, RecruitsFactions

### Community 41 - "FactionInfoScreen"
Cohesion: 0.07
Nodes (7): ClientScreens, FactionInfoScreen, Line, FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 42 - ".send"
Cohesion: 0.14
Nodes (3): StatsRequest, Msg, SimpleChannel

### Community 43 - "HorizontalStretch"
Cohesion: 0.12
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 44 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.06
Nodes (11): Palette, WallTemplates, CharacterMarkers, Placed, MarkerSpec, Build, CASTLE, HAMLET (+3 more)

### Community 45 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 48 - "War"
Cohesion: 0.09
Nodes (4): Army, PoliticsSync, War, Politics.Front

### Community 50 - "rafasconquest.people.Person"
Cohesion: 0.08
Nodes (11): ClientPeerage, Names, Honour, Honours, Stat, DIPLOMACY, INTRIGUE, LEARNING (+3 more)

### Community 51 - ".generate"
Cohesion: 0.07
Nodes (7): ProvinceNames, WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 52 - "CityPlan"
Cohesion: 0.24
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 53 - "BoardScreen"
Cohesion: 0.13
Nodes (4): BoardScreen, BoardAction, MarkerSet, MarkerView

### Community 54 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 56 - "CityLayoutTest"
Cohesion: 0.18
Nodes (3): ChunkCoord, FarmLayout, CityLayoutTest

### Community 57 - ".postChunk"
Cohesion: 0.08
Nodes (7): RecruitsBridge, CityBanners, CityGuards, ConquestServer, StructureLink, PoiPrecomputer, RecruitmentPosts

### Community 60 - "Quest"
Cohesion: 0.20
Nodes (5): Boards, Entry, View, Taken, Quest

### Community 61 - ".render"
Cohesion: 0.17
Nodes (4): CharacterScreen, Spot, Banner, Portraits

### Community 62 - "Recruiter"
Cohesion: 0.07
Nodes (5): RecruiterRenderer, TownspersonRenderer, Walk, Recruiter, Townsperson

### Community 63 - "TitleMap"
Cohesion: 0.11
Nodes (9): Tier, COUNTY, DUCHY, EMPIRE, KINGDOM, TitleMap, TitleStyle, Land (+1 more)

### Community 64 - "Person"
Cohesion: 0.13
Nodes (13): Culture, Organizations, Sanctum, Hold, Kind, CAPITAL, CASTLE, CITY (+5 more)

### Community 65 - "net.minecraft.core.BlockPos"
Cohesion: 0.11
Nodes (3): Offsets, StylePiece, CityPlacement

### Community 68 - ".onLevelLoad"
Cohesion: 0.14
Nodes (4): CityIndex, ConquestRuntime, Frame, Harbour

### Community 69 - "Battle Calibration"
Cohesion: 0.19
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 72 - "City"
Cohesion: 0.11
Nodes (4): Grid, SeatCities, SeatCitiesTest, World

### Community 73 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.15
Nodes (12): Battles (prototype), Blocks of 20 soldiers, Characters (holders, skills, prowess, traits), Cultures (seven homelands), Epic Knights optional armour (Armoury), Faiths and cults, Heralds (daily news), Martial titles (Sword Saint, Sword King, Sword God) (+4 more)

### Community 76 - "View"
Cohesion: 0.15
Nodes (3): BoardView, GrandStrategyView, TalkView

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
Cohesion: 0.16
Nodes (8): CityLayout, Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.13
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 84 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.11
Nodes (5): CityView, FigureStrike, HornOrders, SetSetting, Entry

### Community 86 - "Builder"
Cohesion: 0.16
Nodes (5): BackBanner, Builder, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 87 - "Battle Sounds"
Cohesion: 0.20
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 89 - "PoiPrecomputer.java"
Cohesion: 0.09
Nodes (13): SeedHash, Faction, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD (+5 more)

### Community 92 - "Rank"
Cohesion: 0.10
Nodes (11): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+3 more)

### Community 93 - ".noble"
Cohesion: 0.12
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, POST, WALL, Piece (+1 more)

### Community 100 - "Ranks (Soldier to Grand Marshal)"
Cohesion: 0.20
Nodes (9): Enlisted players, Gold church, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Land grants (seven ways), Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter (+1 more)

### Community 102 - "Battle Site"
Cohesion: 0.20
Nodes (5): BattleSite, Part, Water, BattleSite.Water, BattleSiteTest

### Community 103 - "Realm"
Cohesion: 0.18
Nodes (4): Demographics, Share, RealmPlannerTest, World

### Community 105 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 107 - "Ranks"
Cohesion: 0.07
Nodes (13): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+5 more)

### Community 108 - "Magic Schools"
Cohesion: 0.09
Nodes (14): Dojo, Ground, School, BLOOD, FIRE, ICE, IRON, LIGHT (+6 more)

### Community 110 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 111 - "ArrowVolleys"
Cohesion: 0.23
Nodes (3): DistantDepth, Arrow, ArrowVolleys

### Community 114 - "Doctrine"
Cohesion: 0.18
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 115 - "Settlement"
Cohesion: 0.09
Nodes (11): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+3 more)

### Community 118 - ".view"
Cohesion: 0.11
Nodes (5): GrandStrategy, View, QuestProgress, Retinues, RetinuesTest

### Community 119 - "Race"
Cohesion: 0.18
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 121 - "Homeland"
Cohesion: 0.18
Nodes (8): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA

### Community 122 - "BattlePlan"
Cohesion: 0.05
Nodes (18): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, BattlePlan, Order, CHARGE (+10 more)

### Community 123 - "Quest"
Cohesion: 0.16
Nodes (10): Ladder, MILITARY, NOBLE, NONE, Type, CLEAR, DELIVER, SCOUT (+2 more)

### Community 125 - ".measure"
Cohesion: 0.15
Nodes (3): ConquestConfig, CityStats, Totals

### Community 126 - "Duty"
Cohesion: 0.08
Nodes (22): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+14 more)

### Community 129 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 132 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 134 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 139 - "FormationSnapshot"
Cohesion: 0.13
Nodes (5): FormationSnapshot, Positions, Sprite, DistantArmies, FormationSnapshotTest

### Community 142 - ".begin"
Cohesion: 0.09
Nodes (8): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome, Landing, Side

### Community 143 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.07
Nodes (16): BattleShips, Helm, SableLink, SableShips, Lane, Leg, Port, SeaLanes (+8 more)

### Community 145 - "MapLayer"
Cohesion: 0.19
Nodes (5): LegendEntry, MapLayer, Symbol, RoadLayer, UnitLayer

### Community 146 - ".view"
Cohesion: 0.10
Nodes (11): NobleTitle, COUNT, KNIGHT, NONE, Merit, Peer, Peerage, PeerageSync (+3 more)

### Community 151 - "Kind"
Cohesion: 0.18
Nodes (7): Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN

### Community 155 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.10
Nodes (5): StatsView, UnitInfo, UnitsRequest, UnitsState, Volleys

### Community 156 - ".plan"
Cohesion: 0.17
Nodes (4): Faith, Bond, RealmPlanner, Seat

### Community 157 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 179 - ".pack"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 204 - "Muster"
Cohesion: 0.67
Nodes (3): Muster, CEREMONIAL, OFFENSIVE

### Community 206 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.14
Nodes (4): PeerageView, CityManager, PlayerStats, View

### Community 218 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.15
Nodes (9): Battle phases and days, Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Envelopment, The general and guard, Battle roles (frontline, bait, support, flanker, kill squad), Victory, pursuit and surrender, War camps (+1 more)

### Community 219 - "Roads"
Cohesion: 0.12
Nodes (15): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+7 more)

### Community 223 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 249 - "ConquestNetwork"
Cohesion: 0.08
Nodes (10): BaseMapPart, BattleSnapshot, CityClose, ConquestNetwork, FactionInfoReply, PoliticsView, RequestBaseMap, RequestFactionInfo (+2 more)

## Ambiguous Edges - Review These
- `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)` → `/pve debug battle simulate`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **316 isolated node(s):** `NORTH`, `EAST`, `SOUTH`, `WEST`, `ARCHER_GUARD` (+311 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 901 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **146 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `org.jetbrains.annotations.Nullable`, `net.minecraft.world.level.ChunkPos`, `.army`, `BattleScreen`, `.passOnOrders`, `.begin`, `net.minecraft.server.level.ServerLevel`, `BattleMap.java`, `Commands Guide`, `net.minecraft.world.item.ItemStack`, `CityLayout.java`, `War Council & War Plans`, `Wars Service`, `.broadcast`, `Shape`, `PveCommands Registration`, `Point`, `ConquestEvents`, `BaseMap`, `net.minecraft.server.level.ServerLevel`, `.generate`, `WarCamp`, `.postChunk`, `net.minecraft.server.level.ServerPlayer`, `org.jetbrains.annotations.Nullable`, `UnitMode`, `.officers`, `Battle Site`, `Phase`, `.view`?**
  _High betweenness centrality (0.082) - this node is a cross-community bridge._
- **What connects `NORTH`, `EAST`, `SOUTH` to the rest of the system?**
  _316 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.05616362631288004 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `Terrain`, `list`, `RecruitsBattle.java`, `Organization Screen`, `PoliticalStyle`, `ProvinceMap`, `RoadNetwork`, `.view`, `Realm`, `net.minecraft.world.item.ItemStack`, `CityLayout.java`, `.plan`, `War Council & War Plans`, `Wars Service`, `rafasconquest.people.Person`, `.generate`, `.postChunk`, `MapContext`, `TitleMap`, `.onLevelLoad`, `.postChunk`, `City`, `.choose`, `PoiPrecomputer.java`, `org.jetbrains.annotations.Nullable`, `Roads`, `.render`, `Magic Schools`, `RafasConquest.java`, `Client Politics`, `Settlement`, `GrandStrategyScreen`, `Quest`?**
  _High betweenness centrality (0.048) - this node is a cross-community bridge._
- **Should `list` be split into smaller, more focused modules?**
  _Cohesion score 0.06421821305841924 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)` and `/pve debug battle simulate`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._