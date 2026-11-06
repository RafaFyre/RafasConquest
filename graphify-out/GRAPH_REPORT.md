# Graph Report - RafasConquest  (2026-11-05)

## Corpus Check
- 374 files · ~284,762 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 97 file(s) not represented in the graph (top: .nbt 84, (none) 3, .mcfunction 2)

## Summary
- 5105 nodes · 16109 edges · 248 communities (105 shown, 143 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2071 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `bf79c305`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- org.jetbrains.annotations.Nullable
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- RecruitsBattle.java
- WorldSettings
- ConquestEvents.java
- net.minecraft.world.level.block.state.BlockState
- net.minecraft.nbt.CompoundTag
- .claims
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
- net.minecraft.server.level.ServerLevel
- net.minecraft.server.level.ServerPlayer
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
- RecruitsBattle
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
- Townsperson
- TitleMap
- Organizations
- net.minecraft.core.Vec3i
- .broadcast
- DistantHorizonsArmies.java
- .onLevelLoad
- Calibration
- Plan
- .touches
- City
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- MapLands
- .drawEmpire
- DistantArmyRenderer
- .boundsAt
- DistantArmyRenderer.java
- City Walls
- Ownership & Territory
- .choose
- ConquestNetwork
- Nation Rows
- RafasConquest.java
- Battle Sounds
- rafasconquest_world SavedData
- PoliticalStyle
- ClientEvents.java
- BackBannerLayer.java
- .noble
- Stats & Settings Screens
- Person
- net.minecraft.resources.ResourceLocation
- Strategy
- .render
- .of
- Battle Site
- .world
- CourtView.java
- Call
- Magic Schools
- DistantHorizonsQuality.java
- .of
- MapLayer
- Politics
- Client Politics
- Doctrine
- CountryStructure
- GrandStrategyScreen
- CityPlacement
- net.minecraft.server.level.ServerPlayer
- Homeland
- Faction
- Clash
- BattlePlan
- Quest
- CityStyles
- .measure
- Duty
- Role
- Distribution
- OrganizationScreen
- Recruiter
- ShipHulls
- Trait
- .nearest
- BlockGrid
- Wars (daily nation decisions)
- Politics.Front
- FormationSnapshot
- NationScreen
- WarPlan
- BattleType
- net.minecraft.server.level.ServerLevel
- .drawAligned
- Kind
- PoliticsTest
- .people
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
- Kind
- Kind
- ClientScreens
- Ranks (Soldier to Grand Marshal)
- Roads
- CityGridBuilder
- .walks
- CityManager
- UnitMode
- Kind
- net.minecraft.nbt.CompoundTag
- Shape

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 249 edges
2. `FieldBattle` - 132 edges
3. `FieldBlock` - 120 edges
4. `ConquestNetwork` - 103 edges
5. `FieldArmy` - 94 edges
6. `BaseMap` - 82 edges
7. `Lands` - 79 edges
8. `Politics` - 72 edges
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

## Communities (248 total, 143 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.06
Nodes (8): FieldArmy, Event, FieldBattle, Order, Outcome, Volley, FieldBlock, Rates

### Community 1 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.05
Nodes (12): FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, FactionInfo, Poi (+4 more)

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.09
Nodes (5): CityRoadPiece, CountryPiece, GoldChurchPiece, HarbourPiece, RecruitmentPostPiece

### Community 3 - ".army"
Cohesion: 0.09
Nodes (3): Point, DoctrineTest, FieldBattleTest

### Community 4 - "CityTerrain"
Cohesion: 0.10
Nodes (14): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Plan (+6 more)

### Community 5 - "BattleScreen"
Cohesion: 0.06
Nodes (19): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+11 more)

### Community 7 - "WorldSettings"
Cohesion: 0.09
Nodes (5): CityLayout, District, Site, SlopeTile, CityLayoutTest

### Community 9 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (6): CommandTableBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, WorldMapItem, MarkerBlock

### Community 10 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.11
Nodes (3): MarkerRenderer, MarkerBlockEntity, MarkerSpec

### Community 13 - "BaseMap"
Cohesion: 0.16
Nodes (4): BaseMap, Nation, BaseMapStore, Ready

### Community 15 - "RoadNetwork"
Cohesion: 0.16
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 19 - "Realm"
Cohesion: 0.13
Nodes (4): Kind, FRIEND, RIVAL, Realm

### Community 21 - ".build"
Cohesion: 0.19
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
Nodes (19): WarHorn, Armoury, Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE (+11 more)

### Community 27 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.05
Nodes (6): FactionBannerNbt, CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, FarmStructure

### Community 28 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.07
Nodes (17): Facts, Officer, Talk, WarCouncil, Rank, CAPTAIN, COLONEL, CORPORAL (+9 more)

### Community 29 - "Wars Service"
Cohesion: 0.15
Nodes (3): FeudDuel, PoliticsSync, Wars

### Community 30 - "Politics"
Cohesion: 0.09
Nodes (5): Feud, Lord, Feuds, Lands, Front

### Community 31 - "Land"
Cohesion: 0.16
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 32 - "Capital Layout & Seed Hash"
Cohesion: 0.10
Nodes (11): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, SeedHash (+3 more)

### Community 36 - "CourtView"
Cohesion: 0.18
Nodes (3): Camera, CourtView, Figure

### Community 37 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.09
Nodes (19): Ambushes from woods, Base map generated from seed, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Faction Info screen, Deploying to the land (GroundPlan) (+11 more)

### Community 38 - "Countryside"
Cohesion: 0.22
Nodes (4): Countryside, Plan, CountrysideTest, World

### Community 39 - "RecruitsBattle"
Cohesion: 0.04
Nodes (14): Card, Enlisted, GeneralTask, Landing, Officers, RecruitsBattle, Role, BEARER (+6 more)

### Community 42 - "War"
Cohesion: 0.12
Nodes (3): Army, Goal, War

### Community 43 - "HorizontalStretch"
Cohesion: 0.07
Nodes (8): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, ProvinceMap, Terrain, Choice, HorizontalStretch, ProvinceMapTest

### Community 44 - "ConquestServer"
Cohesion: 0.10
Nodes (6): ConquestSavedData, CityBanners, ConquestServer, StructureLink, EconomyTick, PoiPrecomputer

### Community 45 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 50 - "BattleRole"
Cohesion: 0.11
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 51 - ".generate"
Cohesion: 0.07
Nodes (7): ProvinceNames, WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 52 - "CityPlan"
Cohesion: 0.21
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 54 - ".start"
Cohesion: 0.10
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 55 - "WarCamp"
Cohesion: 0.12
Nodes (4): Saved, WarCamp, CharacterMarkers, Placed

### Community 56 - "Builder"
Cohesion: 0.12
Nodes (5): BackBanner, Builder, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 57 - ".postChunk"
Cohesion: 0.12
Nodes (3): RecruitmentPost, RecruitmentPosts, RecruitmentPostTest

### Community 60 - "Peerage"
Cohesion: 0.09
Nodes (10): Boards, Entry, View, Merit, Peer, Peerage, Taken, PeerageSync (+2 more)

### Community 61 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.10
Nodes (5): CharacterScreen, Retainer, Spot, Banner, Portraits

### Community 63 - "TitleMap"
Cohesion: 0.15
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Organizations"
Cohesion: 0.33
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 65 - "net.minecraft.core.Vec3i"
Cohesion: 0.15
Nodes (3): CountryPlacement, DojoPlacement, FarmPlacement

### Community 66 - ".broadcast"
Cohesion: 0.08
Nodes (9): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT (+1 more)

### Community 69 - "Calibration"
Cohesion: 0.19
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 72 - "City"
Cohesion: 0.15
Nodes (4): Grid, SeatCities, SeatCitiesTest, World

### Community 73 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.17
Nodes (8): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Enlisted players, The general and guard, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Battle roles (frontline, bait, support, flanker, kill squad), Victory, pursuit and surrender, The war horn

### Community 74 - "MapLands"
Cohesion: 0.06
Nodes (5): Cell, Terrain, Drop, MarkerScreen, MapLands

### Community 77 - "DistantArmyRenderer"
Cohesion: 0.09
Nodes (5): DistantDepth, DistantArmyRenderer, Figure, DistantHorizonsLink, DistantShips

### Community 78 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 79 - "DistantArmyRenderer.java"
Cohesion: 0.10
Nodes (5): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), Arrow, ArrowVolleys

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
Cohesion: 0.03
Nodes (32): BaseMapPart, BattleSnapshot, BattleSpot, BoardAction, BoardView, CityClose, CityView, ConquestNetwork (+24 more)

### Community 85 - "Nation Rows"
Cohesion: 0.12
Nodes (3): Fort, Stronghold, Row

### Community 86 - "RafasConquest.java"
Cohesion: 0.07
Nodes (3): ConquestContent, ConquestEntities, RafasConquest

### Community 87 - "Battle Sounds"
Cohesion: 0.20
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 89 - "PoliticalStyle"
Cohesion: 0.15
Nodes (4): MapPreview, PoliticalMap, LabelShape, PoliticalStyle

### Community 93 - ".noble"
Cohesion: 0.18
Nodes (8): Kind, BARRACKS, CHURCH, NOBLE, PALACE, POST, WALL, Piece

### Community 95 - "Person"
Cohesion: 0.09
Nodes (12): ClientPeerage, Names, Honour, Honours, Person, Stat, DIPLOMACY, INTRIGUE (+4 more)

### Community 98 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 100 - ".of"
Cohesion: 0.20
Nodes (3): FactionBanner, Layer, FactionBannerTest

### Community 102 - "Battle Site"
Cohesion: 0.20
Nodes (5): BattleSite, Part, Water, BattleSite.Water, BattleSiteTest

### Community 105 - "CourtView.java"
Cohesion: 0.08
Nodes (5): Character page, Court 3D scene (CourtView), Demographics (population by culture and race), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...)

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
Cohesion: 0.19
Nodes (5): LegendEntry, MapLayer, Symbol, RoadLayer, UnitLayer

### Community 112 - "Politics"
Cohesion: 0.10
Nodes (5): Muster, CEREMONIAL, OFFENSIVE, Politics, PoliticsData

### Community 114 - "Doctrine"
Cohesion: 0.16
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 115 - "CountryStructure"
Cohesion: 0.18
Nodes (3): Settlement, CountryIndex, CountryStructure

### Community 118 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.09
Nodes (9): GrandStrategy, View, NobleTitle, COUNT, KNIGHT, NONE, Retinues, PeerageTest (+1 more)

### Community 119 - "Homeland"
Cohesion: 0.09
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 120 - "Faction"
Cohesion: 0.09
Nodes (6): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction, CityGuards, FactionInfoTest

### Community 121 - "Clash"
Cohesion: 0.28
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 122 - "BattlePlan"
Cohesion: 0.05
Nodes (18): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, BattlePlan, Order, CHARGE (+10 more)

### Community 123 - "Quest"
Cohesion: 0.16
Nodes (10): Ladder, MILITARY, NOBLE, NONE, Type, CLEAR, DELIVER, SCOUT (+2 more)

### Community 126 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 127 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 128 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 130 - "Recruiter"
Cohesion: 0.13
Nodes (6): Recruiter, Offer, Recruitment, Option, Talk, View

### Community 131 - "ShipHulls"
Cohesion: 0.22
Nodes (3): Part, ShipHulls, Loaded

### Community 132 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 136 - "Wars (daily nation decisions)"
Cohesion: 0.12
Nodes (16): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Characters (holders, skills, prowess, traits), Cultures (seven homelands), Envelopment, Epic Knights optional armour (Armoury), Faiths and cults (+8 more)

### Community 139 - "FormationSnapshot"
Cohesion: 0.13
Nodes (5): FormationSnapshot, Positions, Sprite, DistantArmies, FormationSnapshotTest

### Community 140 - "NationScreen"
Cohesion: 0.15
Nodes (3): NationScreen, Demographics, Share

### Community 142 - "BattleType"
Cohesion: 0.20
Nodes (5): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE

### Community 143 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.07
Nodes (16): BattleShips, Helm, SableLink, SableShips, Lane, Leg, Port, SeaLanes (+8 more)

### Community 146 - "Kind"
Cohesion: 0.25
Nodes (7): Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN

### Community 148 - "PoliticsTest"
Cohesion: 0.12
Nodes (3): Hold, Event, PoliticsTest

### Community 156 - ".plan"
Cohesion: 0.12
Nodes (6): Centre, Culture, Faith, Bond, RealmPlanner, Seat

### Community 179 - ".pack"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 204 - "Kind"
Cohesion: 0.33
Nodes (6): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER

### Community 206 - "Kind"
Cohesion: 0.20
Nodes (9): Kind, CAPITAL, CASTLE, CHILD, CITY, LORD, RETAINER, SIBLING (+1 more)

### Community 218 - "Ranks (Soldier to Grand Marshal)"
Cohesion: 0.33
Nodes (6): Gold church, Land grants (seven ways), Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter

### Community 219 - "Roads"
Cohesion: 0.11
Nodes (15): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+7 more)

### Community 221 - "CityGridBuilder"
Cohesion: 0.07
Nodes (9): CityGridBuilder, Ground, PlacedWall, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY (+1 more)

### Community 228 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 231 - "Kind"
Cohesion: 0.33
Nodes (6): Kind, CLEAR_ABOVE, NONE, PLATE, SLOPE, WALL_BASE

### Community 232 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.08
Nodes (10): Palette, WallTemplates, Build, CASTLE, HAMLET, RUIN, TOWER, WELL (+2 more)

### Community 248 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

## Ambiguous Edges - Review These
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **320 isolated node(s):** `SOLDIER`, `LEADER`, `GENERAL`, `BEARER`, `GUARD` (+315 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 904 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **143 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `org.jetbrains.annotations.Nullable`, `org.jetbrains.annotations.Nullable`, `net.minecraft.world.level.ChunkPos`, `.army`, `Trait`, `BattleScreen`, `ConquestEvents.java`, `BaseMap`, `net.minecraft.server.level.ServerLevel`, `.passOnOrders`, `BattleMap.java`, `Commands Guide`, `net.minecraft.world.item.ItemStack`, `net.minecraft.server.level.ServerLevel`, `net.minecraft.server.level.ServerPlayer`, `PveCommands Registration`, `.state`, `net.neoforged.bus.api.SubscribeEvent`, `rafasconquest.faction.Faction`, `.generate`, `WarCamp`, `Builder`, `Peerage`, `.broadcast`, `RecruitsBattle.java`, `UnitMode`, `Battle Site`, `.of`, `Shape`?**
  _High betweenness centrality (0.096) - this node is a cross-community bridge._
- **What connects `SOLDIER`, `LEADER`, `GENERAL` to the rest of the system?**
  _320 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.06084447941253606 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `org.jetbrains.annotations.Nullable`, `OrganizationScreen`, `Recruiter`, `RecruitsBattle.java`, `ConquestEvents.java`, `RoadNetwork`, `Realm`, `net.minecraft.world.item.ItemStack`, `net.minecraft.server.level.ServerLevel`, `.plan`, `net.minecraft.server.level.ServerPlayer`, `Wars Service`, `.place`, `ConquestServer`, `list`, `.generate`, `.postChunk`, `MapContext`, `Peerage`, `TitleMap`, `.onLevelLoad`, `City`, `MapLands`, `.choose`, `RafasConquest.java`, `PoliticalStyle`, `ClientScreens`, `Roads`, `Person`, `Magic Schools`, `Client Politics`, `CountryStructure`, `GrandStrategyScreen`, `Homeland`?**
  _High betweenness centrality (0.055) - this node is a cross-community bridge._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.05307797537619699 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._