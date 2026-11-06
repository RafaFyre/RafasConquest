# Graph Report - RafasConquest  (2026-11-05)

## Corpus Check
- 379 files · ~296,256 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 97 file(s) not represented in the graph (top: .nbt 84, (none) 3, .mcfunction 2)

## Summary
- 5252 nodes · 16687 edges · 259 communities (111 shown, 148 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2218 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `884163c7`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- ProvinceMap
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- Feud
- CityLayout
- list
- NationScreen
- MarkerSpec
- ConquestSavedData
- BaseMap
- net.neoforged.neoforge.network.handling.IPayloadContext
- RoadNetwork
- RecruitsBattle
- Battle Map Web Page & Site
- Person
- .passOnOrders
- BattleMap.java
- PoliticsTest
- City
- Commands Guide
- CityManagerScreen
- TitleMap
- net.minecraft.world.item.ItemStack
- MarkerScreen
- WarCouncil
- rafasconquest.map.BaseMap
- Lands
- .send
- Capital Layout & Seed Hash
- PveCommands Registration
- StyleLibrary
- CourtView
- net.minecraft.world.level.saveddata.SavedData
- net.minecraft.core.BlockPos
- net.minecraft.network.FriendlyByteBuf
- rafasconquest.faction.Faction
- FactionInfoScreen
- .of
- HorizontalStretch
- PoliticalStyle
- Task
- Personalities
- .world
- Townsperson
- BattleRole
- net.minecraft.server.level.ServerLevel
- Building
- BoardScreen
- .blocksPlacedByHandBeforeTheBattleKeepTheirPlaces
- WarCamp
- DayClockTest
- Faction Map View
- MapContext
- Peer
- .render
- net.minecraft.resources.ResourceLocation
- TitleMap
- .render
- DistantArmyRenderer
- Kit
- DistantHorizonsArmies.java
- .onLevelLoad
- CourtSet
- ConquestWorldgen
- .touches
- City
- .officers
- MapLands
- .drawEmpire
- org.jetbrains.annotations.Nullable
- Strategy
- .boundsAt
- DistantArmyRenderer.java
- City Walls
- Ownership & Territory
- .choose
- .sender
- Nation Rows
- BattleSounds
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- rafasconquest_world SavedData
- ConquestNetwork.java
- Wars (daily nation decisions)
- net.minecraft.world.level.block.Rotation
- CityPlan
- Stats & Settings Screens
- .generate
- RafasConquest.java
- View
- BattleType
- OrganizationScreen
- Arm
- .world
- .postChunk
- Call
- Magic Schools
- DistantHorizonsQuality.java
- .plan
- net.minecraft.client.gui.GuiGraphics
- Goal
- .people
- Doctrine
- Settlement
- GrandStrategyScreen
- RecruitsBattle.java
- .view
- Race
- ItemFlags.java
- GameCharacter
- BattlePlan
- Quest
- CityStyles
- .measure
- Duty
- Ranks
- World map (CK3-style political map)
- Ranks (Soldier to Grand Marshal)
- Recruiter
- ShipHulls
- Trait
- Distribution
- BlockGrid
- Characters (holders, skills, prowess, traits)
- MapLayer
- WarPlan
- FormationSnapshot
- ClientPolitics
- .nearest
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- SeaLanes
- ClientEvents.java
- net.minecraft.nbt.CompoundTag
- Politics.Front
- .build
- CLAUDE.md
- CityTerrainPiece
- ConquestGameRules
- .plan
- .place
- ShipKind
- .raise
- .walks
- Small Cluster 170
- .saveReloadRoundTripIsIdentical
- .pack
- net.minecraft.core.Vec3i
- Rank
- Kind
- ClientScreens
- .player
- Roads
- CityGridBuilder
- .sendBattleSpot
- .sendPeerage
- SetSetting
- WarHorn
- DistantGeneration
- net.neoforged.bus.api.SubscribeEvent
- RankKits
- RecruiterScreen
- Kind
- CityManager
- UnitMode
- net.minecraft.world.level.block.state.BlockState

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 249 edges
2. `FieldBlock` - 157 edges
3. `FieldBattle` - 148 edges
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

## Communities (259 total, 148 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.06
Nodes (7): FieldArmy, Event, FieldBattle, Order, Volley, FieldBlock, Figure

### Community 1 - "ProvinceMap"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.09
Nodes (5): CityRoadPiece, CountryPiece, GoldChurchPiece, HarbourPiece, RecruitmentPostPiece

### Community 3 - ".army"
Cohesion: 0.08
Nodes (4): Point, DoctrineTest, FieldBattleTest, ArmouryTest

### Community 4 - "CityTerrain"
Cohesion: 0.08
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.06
Nodes (20): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+12 more)

### Community 6 - "Feud"
Cohesion: 0.12
Nodes (6): Clash, Feud, Kind, DUEL, SKIRMISH, FeudDuel

### Community 7 - "CityLayout"
Cohesion: 0.09
Nodes (6): CityLayout, District, Site, SlopeTile, WorldSettings, CityLayoutTest

### Community 8 - "list"
Cohesion: 0.07
Nodes (5): Countryside (hamlets, villages, watchtowers, castles), Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, Roads (high, city, town roads; bridges; signposts), Seat cities

### Community 9 - "NationScreen"
Cohesion: 0.15
Nodes (3): NationScreen, Demographics, Share

### Community 10 - "MarkerSpec"
Cohesion: 0.11
Nodes (3): MarkerRenderer, MarkerBlockEntity, MarkerSpec

### Community 12 - "ConquestSavedData"
Cohesion: 0.08
Nodes (8): RecruitsBridge, ConquestSavedData, CityBanners, CityGuards, ConquestServer, StructureLink, EconomyTick, PoiPrecomputer

### Community 13 - "BaseMap"
Cohesion: 0.13
Nodes (4): BaseMap, Nation, BaseMapStore, Ready

### Community 14 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.13
Nodes (6): BackBanner, Builder, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 15 - "RoadNetwork"
Cohesion: 0.15
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 16 - "RecruitsBattle"
Cohesion: 0.03
Nodes (22): Outcome, Card, Enlisted, GeneralTask, Phase, AFTERMATH, BATTLE, DEPLOYMENT (+14 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.14
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 20 - "BattleMap.java"
Cohesion: 0.04
Nodes (3): Battle map developer web page, Morale, rout and rally, BattleMap

### Community 22 - "City"
Cohesion: 0.14
Nodes (6): City, Tier, COUNTY, DUCHY, EMPIRE, KINGDOM

### Community 23 - "Commands Guide"
Cohesion: 0.09
Nodes (30): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+22 more)

### Community 25 - "TitleMap"
Cohesion: 0.17
Nodes (6): Nation, Nations, Plan, Seats, NationsTest, World

### Community 26 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.20
Nodes (3): Armoury, Outfit, Style

### Community 27 - "MarkerScreen"
Cohesion: 0.07
Nodes (9): BattleSite, Part, Water, BattleSite.Water, Cell, Terrain, Drop, MarkerScreen (+1 more)

### Community 28 - "WarCouncil"
Cohesion: 0.15
Nodes (4): Facts, Officer, Talk, WarCouncil

### Community 29 - "rafasconquest.map.BaseMap"
Cohesion: 0.09
Nodes (4): Army, PoliticsSync, War, Wars

### Community 30 - "Lands"
Cohesion: 0.08
Nodes (5): Lord, Feuds, Lands, Front, Politics

### Community 31 - ".send"
Cohesion: 0.14
Nodes (3): StatsRequest, Msg, SimpleChannel

### Community 32 - "Capital Layout & Seed Hash"
Cohesion: 0.09
Nodes (10): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, ProvinceNames (+2 more)

### Community 35 - "StyleLibrary"
Cohesion: 0.16
Nodes (3): Offsets, StyleLibrary, PlacementOffsetsTest

### Community 36 - "CourtView"
Cohesion: 0.13
Nodes (4): Retainer, Camera, CourtView, Figure

### Community 39 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.08
Nodes (6): HornOrders, StatsView, UnitsRequest, UnitsState, Volleys, WorldStretch

### Community 41 - "FactionInfoScreen"
Cohesion: 0.16
Nodes (3): FactionInfoScreen, Line, RequestFactionInfo

### Community 42 - ".of"
Cohesion: 0.18
Nodes (4): FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 43 - "HorizontalStretch"
Cohesion: 0.13
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 44 - "PoliticalStyle"
Cohesion: 0.19
Nodes (3): PoliticalMap, LabelShape, PoliticalStyle

### Community 45 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 50 - "BattleRole"
Cohesion: 0.09
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 53 - "BoardScreen"
Cohesion: 0.13
Nodes (4): BoardScreen, BoardAction, MarkerSet, MarkerView

### Community 54 - ".blocksPlacedByHandBeforeTheBattleKeepTheirPlaces"
Cohesion: 0.10
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 56 - "DayClockTest"
Cohesion: 0.25
Nodes (3): DayClock, Step, DayClockTest

### Community 60 - "Peer"
Cohesion: 0.19
Nodes (5): Boards, Entry, View, Taken, Quest

### Community 61 - ".render"
Cohesion: 0.08
Nodes (8): CharacterScreen, Spot, ClientPeerage, Banner, Portraits, Names, Honour, Honours

### Community 63 - "TitleMap"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 65 - "DistantArmyRenderer"
Cohesion: 0.12
Nodes (5): DistantDepth, Arrow, ArrowVolleys, DistantArmyRenderer, Figure

### Community 66 - "Kit"
Cohesion: 0.15
Nodes (13): Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD, HEAVY_INFANTRY (+5 more)

### Community 67 - "DistantHorizonsArmies.java"
Cohesion: 0.08
Nodes (4): DistantHorizonsArmies, Figure, DistantHorizonsLink, DistantShips

### Community 69 - "CourtSet"
Cohesion: 0.30
Nodes (3): CourtSet, Seat, Style

### Community 70 - "ConquestWorldgen"
Cohesion: 0.06
Nodes (8): CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 72 - "City"
Cohesion: 0.17
Nodes (5): Countryside, Plan, Grid, CountrysideTest, World

### Community 76 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.06
Nodes (14): SeedHash, Faction, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD (+6 more)

### Community 77 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 78 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

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
Cohesion: 0.09
Nodes (13): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+5 more)

### Community 84 - ".sender"
Cohesion: 0.12
Nodes (4): CityView, FigureStrike, RequestBaseMap, TeleportTo

### Community 87 - "BattleSounds"
Cohesion: 0.09
Nodes (6): BattleSounds, ChargeSound, DinSound, Source, DistantArmies, BattleSnapshot

### Community 90 - "ConquestNetwork.java"
Cohesion: 0.05
Nodes (9): Character page, Court 3D scene (CourtView), Demographics (population by culture and race), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), InventoryStatsButton, CityClose, ConquestNetwork (+1 more)

### Community 91 - "Wars (daily nation decisions)"
Cohesion: 0.22
Nodes (7): Battles (prototype), Blocks of 20 soldiers, Envelopment, Heralds (daily news), Mustering (offensive and ceremonial), War camps, Wars (daily nation decisions)

### Community 93 - "CityPlan"
Cohesion: 0.12
Nodes (11): CityPlan, Kind, BARRACKS, CHURCH, NOBLE, PALACE, POST, WALL (+3 more)

### Community 95 - ".generate"
Cohesion: 0.06
Nodes (13): WorldFeatures, BaseMapGenerator, Heights, BiomePalette, MapColors, Homeland, DESERT, JUNGLE (+5 more)

### Community 96 - "RafasConquest.java"
Cohesion: 0.06
Nodes (4): ConquestContent, StreetWalks, ConquestEntities, RafasConquest

### Community 98 - "View"
Cohesion: 0.15
Nodes (3): BoardView, GrandStrategyView, TalkView

### Community 99 - "BattleType"
Cohesion: 0.22
Nodes (5): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE

### Community 101 - "Arm"
Cohesion: 0.07
Nodes (19): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, Calibration, Fit, Striker (+11 more)

### Community 103 - ".world"
Cohesion: 0.13
Nodes (4): Hook, Realm, Succession, Taking

### Community 105 - ".postChunk"
Cohesion: 0.12
Nodes (3): RecruitmentPost, RecruitmentPosts, RecruitmentPostTest

### Community 107 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 108 - "Magic Schools"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 109 - "DistantHorizonsQuality.java"
Cohesion: 0.08
Nodes (6): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits, DistantHorizonsQuality

### Community 110 - ".plan"
Cohesion: 0.14
Nodes (3): SeatCities, SeatCitiesTest, World

### Community 114 - "Doctrine"
Cohesion: 0.18
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 115 - "Settlement"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 117 - "RecruitsBattle.java"
Cohesion: 0.05
Nodes (10): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), Package layout (core, data, worldgen, sim, bridge), Nations by zone, Recruits faction registration (bridge package), Storm wall and war zone (+2 more)

### Community 118 - ".view"
Cohesion: 0.09
Nodes (7): GrandStrategy, View, PlayerStats, View, QuestProgress, Retinues, RetinuesTest

### Community 119 - "Race"
Cohesion: 0.20
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

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

### Community 128 - "World map (CK3-style political map)"
Cohesion: 0.33
Nodes (7): Base map generated from seed, Command Table block, Faction Info screen, World map layers, Province size setting, Provinces (ProvinceMap), World map (CK3-style political map)

### Community 129 - "Ranks (Soldier to Grand Marshal)"
Cohesion: 0.33
Nodes (6): Gold church, Land grants (seven ways), Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter

### Community 130 - "Recruiter"
Cohesion: 0.12
Nodes (6): Recruiter, Offer, Recruitment, Option, Talk, View

### Community 131 - "ShipHulls"
Cohesion: 0.21
Nodes (3): Part, ShipHulls, Loaded

### Community 132 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 133 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 136 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.14
Nodes (13): Battle phases and days, Characters (holders, skills, prowess, traits), Cultures (seven homelands), Enlisted players, Epic Knights optional armour (Armoury), Faiths and cults, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Martial titles (Sword Saint, Sword King, Sword God) (+5 more)

### Community 137 - "MapLayer"
Cohesion: 0.18
Nodes (5): LegendEntry, MapLayer, Symbol, RoadLayer, TerritoryLayer

### Community 139 - "FormationSnapshot"
Cohesion: 0.12
Nodes (11): FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN (+3 more)

### Community 142 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.33
Nodes (4): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender

### Community 143 - "SeaLanes"
Cohesion: 0.05
Nodes (21): Hull, Ramming, BattleShips, Landing, Helm, Ram, SableLink, SableShips (+13 more)

### Community 146 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.07
Nodes (10): Palette, WallTemplates, Build, CASTLE, HAMLET, RUIN, TOWER, WELL (+2 more)

### Community 149 - ".build"
Cohesion: 0.19
Nodes (3): Frame, Harbour, CitySizesTest

### Community 156 - ".plan"
Cohesion: 0.13
Nodes (7): Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 162 - "ShipKind"
Cohesion: 0.33
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 163 - ".raise"
Cohesion: 0.33
Nodes (4): Stronghold, Muster, CEREMONIAL, OFFENSIVE

### Community 178 - ".saveReloadRoundTripIsIdentical"
Cohesion: 0.06
Nodes (4): ConquestConfig, MapPreview, ConquestSavedDataTest, FactionInfoTest

### Community 179 - ".pack"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 204 - "Rank"
Cohesion: 0.13
Nodes (10): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+2 more)

### Community 206 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 217 - "ClientScreens"
Cohesion: 0.11
Nodes (3): ClientScreens, ClientMapCache, BaseMapPart

### Community 218 - ".player"
Cohesion: 0.12
Nodes (6): NobleTitle, COUNT, KNIGHT, NONE, PeerageSync, PeerageTest

### Community 219 - "Roads"
Cohesion: 0.08
Nodes (19): Grade, CITY, DOJO, HIGH, SEA, TOWN, Heap, Junction (+11 more)

### Community 221 - "CityGridBuilder"
Cohesion: 0.06
Nodes (9): CityGridBuilder, Ground, PlacedWall, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY (+1 more)

### Community 234 - "net.neoforged.bus.api.SubscribeEvent"
Cohesion: 0.04
Nodes (4): BattleKeepInventory, ClientEvents, ClientModEvents, ConquestEvents

### Community 247 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 255 - "UnitMode"
Cohesion: 0.16
Nodes (11): Shape, LINE, SQUARE, V, WEDGE, UnitMode, CHARGE, FOLLOW (+3 more)

### Community 257 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (7): Saved, CommandTableBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, WorldMapItem, MarkerBlock

## Ambiguous Edges - Review These
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **329 isolated node(s):** `ADVANCE`, `CAVALRY_CHARGE`, `CAVALRY_RECALL`, `WALL`, `GENERAL_CHARGES` (+324 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 930 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **148 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `org.jetbrains.annotations.Nullable`, `net.minecraft.world.level.ChunkPos`, `.army`, `BattleScreen`, `Feud`, `.state`, `BaseMap`, `SeaLanes`, `.passOnOrders`, `BattleMap.java`, `net.minecraft.world.item.ItemStack`, `MarkerScreen`, `WarCouncil`, `PveCommands Registration`, `rafasconquest.faction.Faction`, `net.minecraft.server.level.ServerLevel`, `WarCamp`, `Recruiter.java`, `CourtSet`, `.officers`, `.generate`, `Arm`, `net.neoforged.bus.api.SubscribeEvent`, `RecruitsBattle.java`, `UnitMode`?**
  _High betweenness centrality (0.068) - this node is a cross-community bridge._
- **What connects `ADVANCE`, `CAVALRY_CHARGE`, `CAVALRY_RECALL` to the rest of the system?**
  _329 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.06018882769472856 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `ProvinceMap`, `Recruiter`, `list`, `NationScreen`, `ConquestSavedData`, `RoadNetwork`, `City`, `net.minecraft.world.item.ItemStack`, `MarkerScreen`, `.plan`, `WarCouncil`, `rafasconquest.map.BaseMap`, `.place`, `CourtView`, `PoliticalStyle`, `assertequals`, `Faction Map View`, `MapContext`, `.render`, `TitleMap`, `.onLevelLoad`, `City`, `MapLands`, `org.jetbrains.annotations.Nullable`, `.choose`, `ClientScreens`, `ConquestNetwork.java`, `.generate`, `OrganizationScreen`, `.world`, `.postChunk`, `Magic Schools`, `Settlement`, `RecruitsBattle.java`, `.view`?**
  _High betweenness centrality (0.064) - this node is a cross-community bridge._
- **Should `net.minecraft.world.level.ChunkPos` be split into smaller, more focused modules?**
  _Cohesion score 0.0907928388746803 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._