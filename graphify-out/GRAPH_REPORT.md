# Graph Report - RafasConquest  (2026-11-05)

## Corpus Check
- 374 files · ~283,215 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 97 file(s) not represented in the graph (top: .nbt 84, (none) 3, .mcfunction 2)

## Summary
- 5085 nodes · 16068 edges · 252 communities (108 shown, 144 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2059 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `a5c445df`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- RecruitsBattle.java
- WorldSettings
- .passOnOrders
- net.minecraft.world.level.block.state.BlockState
- net.minecraft.nbt.CompoundTag
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
- Lands
- Land
- Capital Layout & Seed Hash
- PveCommands Registration
- .state
- StyleLibrary
- CourtView
- Cities (grid, plots, vanilla village buildings)
- BaseMap
- RecruitsBattle
- net.minecraft.server.level.ServerLevel
- FactionInfoScreen
- .send
- HorizontalStretch
- .named
- Task
- Personalities
- DayClockTest
- War
- Stat
- .generate
- CityPlan
- BoardScreen
- .start
- WarCamp
- .hash
- .postChunk
- Faction Map View
- MapContext
- Peerage
- .render
- Townsperson
- TitleMap
- Organizations
- net.minecraft.core.Vec3i
- DistantArmies
- DistantHorizonsArmies.java
- .onLevelLoad
- .facingX
- .postChunk
- .touches
- City
- Battles (prototype)
- Map Lands
- .drawEmpire
- View
- DistantArmyRenderer
- .boundsAt
- DistantArmyRenderer.java
- City Walls
- Ownership & Territory
- .choose
- ConquestNetwork
- Nation Rows
- Builder
- Battle Sounds
- rafasconquest_world SavedData
- PoiPrecomputer.java
- ClientEvents
- .player
- .noble
- Stats & Settings Screens
- ClientModEvents.java
- .of
- RecruiterScreen
- .render
- Ranks (Soldier to Grand Marshal)
- Determinism rules
- Battle Site
- Realm
- Strategy
- .enlist
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
- net.minecraft.server.level.ServerPlayer
- Homeland
- Faction
- Feud
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
- WarCouncil.java
- Distribution
- DistantHorizonsArmies
- ConquestEvents.java
- Organization Screen
- FormationSnapshot
- NationScreen
- MarkerScreen
- .begin
- net.minecraft.server.level.ServerLevel
- .of
- MapLayer
- Recruiter
- net.minecraft.world.level.saveddata.SavedData
- net.minecraft.resources.ResourceLocation
- CLAUDE.md
- Poi
- CityTerrainPiece
- UnitInfo
- .plan
- .place
- net.neoforged.bus.api.SubscribeEvent
- UnitOrder
- Small Cluster 170
- org.junit.jupiter.api.Test
- rafasconquest.battle.FormationSnapshot
- .pack
- GoldChurch.java
- SeatCitiesTest
- ClientScreens
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- Roads
- .findGenerationPoint
- WallTemplates
- Call
- .at
- Tier
- Build
- BlockGrid
- Volleys
- net.minecraft.network.FriendlyByteBuf

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 249 edges
2. `FieldBattle` - 132 edges
3. `FieldBlock` - 120 edges
4. `ConquestNetwork` - 105 edges
5. `FieldArmy` - 94 edges
6. `BaseMap` - 86 edges
7. `Lands` - 79 edges
8. `Politics` - 72 edges
9. `ConquestSavedData` - 60 edges
10. `BattleScreen` - 60 edges

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

## Communities (252 total, 144 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.06
Nodes (8): FieldArmy, Event, FieldBattle, Order, Outcome, Volley, FieldBlock, Rates

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.12
Nodes (4): CityRoadPiece, CountryPiece, HarbourPiece, RecruitmentPostPiece

### Community 3 - ".army"
Cohesion: 0.08
Nodes (3): Point, DoctrineTest, FieldBattleTest

### Community 4 - "CityTerrain"
Cohesion: 0.06
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.09
Nodes (3): BattleScreen, Entry, Menu

### Community 7 - "WorldSettings"
Cohesion: 0.10
Nodes (6): CityLayout, District, Site, SlopeTile, WorldSettings, CityLayoutTest

### Community 9 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (6): CommandTableBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, WorldMapItem, MarkerBlock

### Community 10 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.06
Nodes (6): MarkerRenderer, MarkerBlockEntity, MarkerSpec, FarmPiece, GoldChurchPiece, PlaceholderPiece

### Community 11 - "Politics"
Cohesion: 0.11
Nodes (3): Event, Front, Politics

### Community 12 - "ConquestSavedData"
Cohesion: 0.08
Nodes (5): ConquestSavedData, ConquestServer, StructureLink, EconomyTick, ConquestSavedDataTest

### Community 14 - "ProvinceMap"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 15 - "RoadNetwork"
Cohesion: 0.16
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "CityVariant"
Cohesion: 0.17
Nodes (7): PlacedWall, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 19 - "Realm"
Cohesion: 0.21
Nodes (4): Kind, FRIEND, RIVAL, Realm

### Community 21 - ".roadStart"
Cohesion: 0.11
Nodes (5): DistrictKey, Townsfolk, Frame, Harbour, StreetWalksTest

### Community 22 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 23 - "Commands Guide"
Cohesion: 0.09
Nodes (29): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+21 more)

### Community 25 - "TitleMap"
Cohesion: 0.17
Nodes (6): Nation, Nations, Plan, Seats, NationsTest, World

### Community 26 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.06
Nodes (21): WarHorn, BackBannerLayer, BackBanners, Armoury, Kit, ARCHER_GUARD, CAPTAIN, CAVALRY (+13 more)

### Community 27 - "CityLayout.java"
Cohesion: 0.08
Nodes (4): CitySlopeStructure, CityStructure, ConquestWorldgen, FarmStructure

### Community 28 - "War Council & War Plans"
Cohesion: 0.15
Nodes (5): WarPlan, Facts, Officer, Talk, WarCouncil

### Community 29 - "Wars Service"
Cohesion: 0.10
Nodes (4): Goal, MusterBars, PoliticsSync, Wars

### Community 30 - "Lands"
Cohesion: 0.10
Nodes (4): Lord, Feuds, Lands, Claim

### Community 31 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 32 - "Capital Layout & Seed Hash"
Cohesion: 0.17
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 36 - "CourtView"
Cohesion: 0.17
Nodes (3): Camera, CourtView, Figure

### Community 37 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.06
Nodes (30): Ambushes from woods, Base map generated from seed, Character page, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Command Table block, Countryside (hamlets, villages, watchtowers, castles) (+22 more)

### Community 38 - "BaseMap"
Cohesion: 0.10
Nodes (6): ClientMapCache, MapPreview, BaseMap, Nation, BaseMapStore, Ready

### Community 39 - "RecruitsBattle"
Cohesion: 0.03
Nodes (24): Card, Enlisted, Figure, Officers, RecruitsBattle, Role, BEARER, GENERAL (+16 more)

### Community 43 - "HorizontalStretch"
Cohesion: 0.13
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 45 - "Task"
Cohesion: 0.22
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "DayClockTest"
Cohesion: 0.25
Nodes (3): DayClock, Step, DayClockTest

### Community 48 - "War"
Cohesion: 0.13
Nodes (5): Army, Muster, CEREMONIAL, OFFENSIVE, War

### Community 50 - "Stat"
Cohesion: 0.14
Nodes (9): Honour, Honours, Stat, DIPLOMACY, INTRIGUE, LEARNING, MARTIAL, PROWESS (+1 more)

### Community 51 - ".generate"
Cohesion: 0.07
Nodes (7): ProvinceNames, WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 53 - "BoardScreen"
Cohesion: 0.13
Nodes (4): BoardScreen, BoardAction, MarkerSet, MarkerView

### Community 54 - ".start"
Cohesion: 0.07
Nodes (10): Ground, GroundPlan, Api, DistantGeneration, BattleSite.Water, GroundPlanTest, Land, Land (+2 more)

### Community 56 - ".hash"
Cohesion: 0.16
Nodes (4): ChunkCoord, FarmLayout, OriginRoller, Roll

### Community 57 - ".postChunk"
Cohesion: 0.08
Nodes (5): RecruitsBridge, CityBanners, CityGuards, PoiPrecomputer, RecruitmentPosts

### Community 59 - "MapContext"
Cohesion: 0.15
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 60 - "Peerage"
Cohesion: 0.09
Nodes (10): Boards, Entry, View, Merit, Peer, Peerage, Taken, PeerageSync (+2 more)

### Community 61 - ".render"
Cohesion: 0.10
Nodes (5): CharacterScreen, Spot, ClientPeerage, Banner, Portraits

### Community 63 - "TitleMap"
Cohesion: 0.17
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Organizations"
Cohesion: 0.33
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 65 - "net.minecraft.core.Vec3i"
Cohesion: 0.09
Nodes (4): CityPlacement, CountryPlacement, DojoPlacement, FarmPlacement

### Community 68 - ".onLevelLoad"
Cohesion: 0.14
Nodes (3): ConquestConfig, CityIndex, ConquestRuntime

### Community 69 - ".facingX"
Cohesion: 0.07
Nodes (13): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, FieldRules (+5 more)

### Community 72 - "City"
Cohesion: 0.11
Nodes (7): Countryside, Plan, City, Grid, SeatCities, CountrysideTest, World

### Community 73 - "Battles (prototype)"
Cohesion: 0.29
Nodes (6): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Envelopment, War camps, War council

### Community 75 - ".drawEmpire"
Cohesion: 0.27
Nodes (3): Hit, LabelLayer, LabelShape

### Community 76 - "View"
Cohesion: 0.12
Nodes (4): BoardView, GrandStrategyView, StatsView, TalkView

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

### Community 84 - "ConquestNetwork"
Cohesion: 0.09
Nodes (11): CityClose, CityView, ConquestNetwork, FactionInfoReply, FigureStrike, HornOrders, RequestBaseMap, RequestFactionInfo (+3 more)

### Community 85 - "Nation Rows"
Cohesion: 0.12
Nodes (3): Fort, Stronghold, Row

### Community 86 - "Builder"
Cohesion: 0.13
Nodes (6): BackBanner, Builder, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 87 - "Battle Sounds"
Cohesion: 0.19
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 89 - "PoiPrecomputer.java"
Cohesion: 0.06
Nodes (7): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, PoliticalMap, FactionInfo

### Community 92 - ".player"
Cohesion: 0.16
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 93 - ".noble"
Cohesion: 0.12
Nodes (8): Kind, BARRACKS, CHURCH, NOBLE, PALACE, POST, WALL, Plan

### Community 100 - "Ranks (Soldier to Grand Marshal)"
Cohesion: 0.18
Nodes (10): Epic Knights optional armour (Armoury), Gold church, Land grants (seven ways), Martial titles (Sword Saint, Sword King, Sword God), Organizations, Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal) (+2 more)

### Community 102 - "Battle Site"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 105 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 107 - ".enlist"
Cohesion: 0.06
Nodes (18): GeneralTask, Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY, SHIELDMAN, Rank (+10 more)

### Community 108 - "Magic Schools"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 109 - "RafasConquest.java"
Cohesion: 0.10
Nodes (3): ConquestContent, ConquestEntities, RafasConquest

### Community 110 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 111 - "ArrowVolleys"
Cohesion: 0.25
Nodes (3): DistantDepth, Arrow, ArrowVolleys

### Community 114 - "Doctrine"
Cohesion: 0.16
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 115 - "Settlement"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 117 - ".onRenderLevel"
Cohesion: 0.10
Nodes (6): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits, DistantHorizonsQuality

### Community 118 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.10
Nodes (7): Retainer, CityManager, GrandStrategy, View, PlayerStats, View, Retinues

### Community 119 - "Homeland"
Cohesion: 0.09
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 120 - "Faction"
Cohesion: 0.14
Nodes (4): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), Faction, FactionInfoTest

### Community 121 - "Feud"
Cohesion: 0.11
Nodes (7): Clash, Feud, Kind, DUEL, SKIRMISH, FeudDuel, Politics.Front

### Community 122 - "BattlePlan"
Cohesion: 0.06
Nodes (13): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, BattlePlan, Order, CHARGE (+5 more)

### Community 123 - "Quest"
Cohesion: 0.16
Nodes (10): Ladder, MILITARY, NOBLE, NONE, Type, CLEAR, DELIVER, SCOUT (+2 more)

### Community 124 - "CityStyles"
Cohesion: 0.21
Nodes (3): Building, CityStyles, Houses

### Community 126 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 129 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 132 - "WarCouncil.java"
Cohesion: 0.08
Nodes (12): /council <topic>, BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Trait (+4 more)

### Community 134 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 136 - "DistantHorizonsArmies"
Cohesion: 0.13
Nodes (3): DistantHorizonsArmies, DistantHorizonsLink, DistantShips

### Community 139 - "FormationSnapshot"
Cohesion: 0.11
Nodes (11): FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN (+3 more)

### Community 140 - "NationScreen"
Cohesion: 0.14
Nodes (3): NationScreen, Demographics, Share

### Community 142 - ".begin"
Cohesion: 0.12
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Landing

### Community 143 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.08
Nodes (15): BattleShips, Helm, SableLink, SableShips, Lane, Leg, Port, SeaLanes (+7 more)

### Community 144 - ".of"
Cohesion: 0.18
Nodes (4): FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 145 - "MapLayer"
Cohesion: 0.18
Nodes (5): LegendEntry, MapLayer, Symbol, RoadLayer, UnitLayer

### Community 146 - "Recruiter"
Cohesion: 0.12
Nodes (6): Recruiter, Offer, Recruitment, Option, Talk, View

### Community 151 - "Poi"
Cohesion: 0.14
Nodes (7): ResourceType, FOOD, Poi, PoiType, CAPITAL, CITY, FARM

### Community 155 - "UnitInfo"
Cohesion: 0.22
Nodes (3): UnitInfo, UnitsRequest, UnitsState

### Community 156 - ".plan"
Cohesion: 0.09
Nodes (14): Culture, Faith, Hold, Kind, CAPITAL, CASTLE, CITY, LORD (+6 more)

### Community 167 - "UnitOrder"
Cohesion: 0.12
Nodes (16): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+8 more)

### Community 174 - "org.junit.jupiter.api.Test"
Cohesion: 0.08
Nodes (5): CitySizesTest, ArmouryTest, RetinuesTest, NobleDistrictTest, PlacementOffsetsTest

### Community 179 - ".pack"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 181 - "GoldChurch.java"
Cohesion: 0.15
Nodes (4): Offsets, StylePiece, GoldChurch, Layout

### Community 218 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.17
Nodes (8): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Enlisted players, The general and guard, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Battle roles (frontline, bait, support, flanker, kill squad), Victory, pursuit and surrender, The war horn

### Community 219 - "Roads"
Cohesion: 0.12
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 225 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 231 - "Tier"
Cohesion: 0.25
Nodes (5): Tier, COUNTY, DUCHY, EMPIRE, KINGDOM

### Community 232 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 249 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.08
Nodes (5): BaseMapPart, BattleSpot, PeerageView, PoliticsView, WorldStretch

## Ambiguous Edges - Review These
- `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)` → `/pve debug battle simulate`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Roads (high, city, town roads; bridges; signposts)` → `Mustering (offensive and ceremonial)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **318 isolated node(s):** `SOLDIER`, `LEADER`, `GENERAL`, `BEARER`, `GUARD` (+313 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 903 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **144 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `org.jetbrains.annotations.Nullable`, `net.minecraft.world.level.ChunkPos`, `.army`, `net.minecraft.server.level.ServerLevel`, `.passOnOrders`, `.begin`, `BattleMap.java`, `Commands Guide`, `net.minecraft.world.item.ItemStack`, `CityLayout.java`, `War Council & War Plans`, `Wars Service`, `PveCommands Registration`, `.state`, `net.neoforged.bus.api.SubscribeEvent`, `BaseMap`, `UnitOrder`, `net.minecraft.server.level.ServerLevel`, `Task`, `.generate`, `.start`, `WarCamp`, `Peerage`, `.facingX`, `org.jetbrains.annotations.Nullable`, `.of`, `.enlist`, `Phase`, `net.minecraft.server.level.ServerPlayer`?**
  _High betweenness centrality (0.093) - this node is a cross-community bridge._
- **What connects `SOLDIER`, `LEADER`, `GENERAL` to the rest of the system?**
  _318 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.06084447941253606 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `Terrain`, `org.jetbrains.annotations.Nullable`, `RecruitsBattle.java`, `Organization Screen`, `PoliticalStyle`, `ProvinceMap`, `RoadNetwork`, `Recruiter`, `Realm`, `net.minecraft.world.item.ItemStack`, `CityLayout.java`, `.plan`, `War Council & War Plans`, `Lands`, `Wars Service`, `.place`, `.generate`, `.postChunk`, `Faction Map View`, `MapContext`, `Peerage`, `TitleMap`, `.onLevelLoad`, `.postChunk`, `City`, `.choose`, `PoiPrecomputer.java`, `org.jetbrains.annotations.Nullable`, `Tier`, `Magic Schools`, `Settlement`, `Quest`?**
  _High betweenness centrality (0.053) - this node is a cross-community bridge._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.08333333333333333 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)` and `/pve debug battle simulate`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Roads (high, city, town roads; bridges; signposts)` and `Mustering (offensive and ceremonial)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._