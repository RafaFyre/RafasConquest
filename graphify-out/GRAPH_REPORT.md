# Graph Report - RafasConquest  (2026-11-05)

## Corpus Check
- 351 files · ~257,217 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 88 file(s) not represented in the graph (top: .nbt 76, (none) 3, .mcmeta 2)

## Summary
- 4716 nodes · 14706 edges · 229 communities (102 shown, 127 thin omitted)
- Extraction: 88% EXTRACTED · 12% INFERRED · 0% AMBIGUOUS · INFERRED: 1789 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `8ef34d8f`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- SimpleChannel.java
- WorldSettings
- net.minecraft.world.level.block.Rotation
- MarkerBlock.java
- .state
- Politics & Fronts
- ConquestNetwork.java
- rafasconquest.battle.BattlePlan.Point
- ProvinceMap
- com.talhanation.recruits.entities.AbstractRecruitEntity
- Battle Map Web Page & Site
- PoiPrecomputer.java
- Townsperson
- BattleMap.java
- .findGenerationPoint
- .postProcess
- Commands Guide
- CityManagerScreen
- Nations Planning
- net.minecraft.world.item.ItemStack
- net.minecraft.nbt.CompoundTag
- War Council & War Plans
- Wars Service
- Roads
- Builder
- Capital Layout & Seed Hash
- PveCommands Registration
- RecruitsBattle
- Countryside
- Recruiter
- .plan
- City Plan Pieces
- arraylist
- Recruits Factions
- Faction Info Screen
- Character Screen & Portraits
- World Stretch Screen
- .enlist
- FieldBlock
- Personalities
- DayClockTest
- War
- net.minecraft.core.Vec3i
- .ringSites
- Base Map Generator
- StyleLibrary
- .view
- Realm
- net.minecraft.core.BlockPos
- Race
- CityGridBuilder
- Faction Map View
- Job
- ConquestServer
- .noble
- .place
- Title Map
- Field Rules & Arms
- Stat
- Distant Army Rendering
- Distant Horizons Armies
- PoliticalStyle
- Battle Calibration
- Recruitment Post Structure
- FormationSnapshot
- CountryStructure
- .view
- Map Lands
- net.minecraftforge.eventbus.api.SubscribeEvent
- net.minecraft.server.level.ServerPlayer
- CityLayoutTest
- net.minecraft.world.level.saveddata.SavedData
- RafasConquest.java
- City Walls
- Ownership & Territory
- Land Grants
- .sender
- Nation Rows
- RafasConquest (PvE addon for Villager Recruits)
- Battle Sounds
- City Styles
- Rank
- BaseMap
- Peerage
- Point
- Stats & Settings Screens
- Terrain Reading
- Distant Army Renderer
- RecruiterScreen
- BackBannerLayer.java
- MapContext
- City Stats
- Battle Site
- Realm Planner Tests
- .place
- Build
- Magic Schools
- ConquestWorldgen
- Doctrine
- Organizations
- PoliticsTest
- Client Politics
- Duty
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- GrandStrategyScreen
- .of
- .player
- NationScreen
- Distance Scaling
- net.minecraft.world.level.biome.Biome
- BattlePlan
- ConquestRuntime
- BoardScreen
- net.minecraft.core.BlockPos
- .sample
- net.minecraft.world.item.DyeColor
- Block Packer
- ArrowVolleys
- Strategy
- CityManager
- Trait
- NationsTest
- Distribution
- Horn Calls
- DistantHorizonsLink
- CourtView
- Organization Screen
- Quest
- Ranks (Soldier to Grand Marshal)
- Settlement
- SeatCitiesTest
- ClientMapCache
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- MarkerScreen
- Game Rules
- CityPlacement
- ClientScreens
- CLAUDE.md
- .begin
- FormationSnapshot
- Kind
- ConquestServer
- ConquestSavedData
- net.minecraft.network.FriendlyByteBuf
- net.minecraftforge.eventbus.api.SubscribeEvent
- BattleRole
- .render
- Small Cluster 170
- Role
- MapLayer
- Site
- rafasconquest.core.WorldSettings
- Battles (prototype)
- WarPlan
- .handleStatsRequest
- Kind
- UnitMode
- Kind
- GameCharacter
- Shape
- .readOffsets

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 247 edges
2. `FieldBattle` - 122 edges
3. `FieldBlock` - 118 edges
4. `ConquestNetwork` - 105 edges
5. `FieldArmy` - 94 edges
6. `BaseMap` - 89 edges
7. `Point` - 70 edges
8. `ConquestSavedData` - 61 edges
9. `BattleScreen` - 60 edges
10. `Politics` - 51 edges

## Surprising Connections (you probably didn't know these)
- `Roads and cities` --conceptually_related_to--> `City slots (walls, common, noble, landmarks)`  [INFERRED]
  docs/index.html → src/main/resources/rafasconquest_structures/README.txt
- `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)` --conceptually_related_to--> `/pve debug battle simulate`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf → RafasConquest-Commands.pdf
- `Technical architecture components` --semantically_similar_to--> `Real vs calculated battles (player within 400 blocks)`  [INFERRED] [semantically similar]
  Recruits PvE Addon — Design Doc.pdf → RafasConquest-Commands.pdf
- `Battles (centre and two wings)` --conceptually_related_to--> `Battle Map web page`  [INFERRED]
  docs/index.html → src/main/resources/assets/rafasconquest/web/battle_map.html
- `Battle blocks (files, ranks, arm, duty, wing)` --conceptually_related_to--> `Battles (centre and two wings)`  [INFERRED]
  src/main/resources/assets/rafasconquest/web/battle_map.html → docs/index.html

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

## Communities (229 total, 127 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.08
Nodes (5): FieldArmy, Event, FieldBattle, Order, Outcome

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.08
Nodes (6): CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, PlaceholderPiece, RecruitmentPostPiece

### Community 3 - ".army"
Cohesion: 0.08
Nodes (5): TickTimes, DoctrineTest, FieldBattleTest, ArmouryTest, RetinuesTest

### Community 4 - "CityTerrain"
Cohesion: 0.05
Nodes (22): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+14 more)

### Community 5 - "BattleScreen"
Cohesion: 0.06
Nodes (20): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+12 more)

### Community 6 - "SimpleChannel.java"
Cohesion: 0.12
Nodes (6): Msg, SimpleChannel, Type, CLEAR, DELIVER, SCOUT

### Community 9 - "MarkerBlock.java"
Cohesion: 0.06
Nodes (6): CommandTableBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, WorldMapItem, MarkerBlock

### Community 11 - "Politics & Fronts"
Cohesion: 0.10
Nodes (10): Goal, Lands, Stronghold, Claim, Event, Front, Muster, CEREMONIAL (+2 more)

### Community 14 - "ProvinceMap"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 15 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.10
Nodes (8): Officers, Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER, Side

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "PoiPrecomputer.java"
Cohesion: 0.06
Nodes (12): Faction, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, FactionInfo (+4 more)

### Community 19 - "Townsperson"
Cohesion: 0.07
Nodes (6): RecruiterRenderer, TownspersonRenderer, Walk, DistrictKey, Townsfolk, Townsperson

### Community 23 - "Commands Guide"
Cohesion: 0.09
Nodes (30): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+22 more)

### Community 25 - "Nations Planning"
Cohesion: 0.18
Nodes (9): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+1 more)

### Community 26 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.07
Nodes (20): WarHorn, InventoryStatsButton, Armoury, Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL (+12 more)

### Community 27 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.08
Nodes (5): MarkerRenderer, Palette, WallTemplates, MarkerBlockEntity, MarkerSpec

### Community 28 - "War Council & War Plans"
Cohesion: 0.18
Nodes (4): Facts, Officer, Talk, WarCouncil

### Community 30 - "Roads"
Cohesion: 0.11
Nodes (15): Grade, CITY, DOJO, HIGH, TOWN, Junction, Land, Link (+7 more)

### Community 31 - "Builder"
Cohesion: 0.11
Nodes (7): BackBanner, BoardView, Builder, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 32 - "Capital Layout & Seed Hash"
Cohesion: 0.21
Nodes (4): CapitalLayout, Land, Site, CapitalRelocationTest

### Community 34 - "RecruitsBattle"
Cohesion: 0.08
Nodes (4): Card, Enlisted, RecruitsBattle, Unit

### Community 35 - "Countryside"
Cohesion: 0.12
Nodes (7): Countryside, Plan, City, Grid, SeatCities, CountrysideTest, World

### Community 36 - "Recruiter"
Cohesion: 0.17
Nodes (4): Recruiter, Option, Talk, View

### Community 37 - ".plan"
Cohesion: 0.18
Nodes (4): Faith, Bond, RealmPlanner, Seat

### Community 38 - "City Plan Pieces"
Cohesion: 0.16
Nodes (4): CityPlan, Piece, Building, NobleDistrictTest

### Community 39 - "arraylist"
Cohesion: 0.05
Nodes (30): Base map generated from seed, Character page, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Court 3D scene (CourtView) (+22 more)

### Community 41 - "Faction Info Screen"
Cohesion: 0.15
Nodes (3): FactionInfoScreen, Line, RequestFactionInfo

### Community 42 - "Character Screen & Portraits"
Cohesion: 0.10
Nodes (6): CharacterScreen, Retainer, Spot, ClientPeerage, Banner, Portraits

### Community 43 - "World Stretch Screen"
Cohesion: 0.13
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 45 - "FieldBlock"
Cohesion: 0.07
Nodes (10): BlockGrid, Volley, FieldBlock, Task, AMBUSH, BAIT, FLANK, KILL (+2 more)

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 50 - ".ringSites"
Cohesion: 0.12
Nodes (7): ChunkCoord, FarmLayout, OriginRoller, Roll, ProvinceNames, SeedHash, LayoutTest

### Community 51 - "Base Map Generator"
Cohesion: 0.08
Nodes (11): WorldFeatures, BaseMapGenerator, Heights, Homeland, DESERT, JUNGLE, PLAINS, SAVANNA (+3 more)

### Community 54 - "Realm"
Cohesion: 0.14
Nodes (6): Names, Culture, Organization, Hold, Person, Realm

### Community 56 - "Race"
Cohesion: 0.18
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 57 - "CityGridBuilder"
Cohesion: 0.07
Nodes (9): CityGridBuilder, Ground, PlacedWall, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY (+1 more)

### Community 59 - "Job"
Cohesion: 0.20
Nodes (6): Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY, SHIELDMAN

### Community 60 - "ConquestServer"
Cohesion: 0.12
Nodes (3): Poi, CityBanners, PoiPrecomputer

### Community 61 - ".noble"
Cohesion: 0.13
Nodes (8): Kind, BARRACKS, CHURCH, NOBLE, PALACE, POST, WALL, Plan

### Community 62 - ".place"
Cohesion: 0.16
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 63 - "Title Map"
Cohesion: 0.17
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Field Rules & Arms"
Cohesion: 0.12
Nodes (11): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, FieldRules, Rates, Side (+3 more)

### Community 65 - "Stat"
Cohesion: 0.13
Nodes (9): Honour, Honours, Stat, DIPLOMACY, INTRIGUE, LEARNING, MARTIAL, PROWESS (+1 more)

### Community 66 - "Distant Army Rendering"
Cohesion: 0.17
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 68 - "PoliticalStyle"
Cohesion: 0.14
Nodes (4): MapColors, PoliticalMap, LabelShape, PoliticalStyle

### Community 69 - "Battle Calibration"
Cohesion: 0.19
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 70 - "Recruitment Post Structure"
Cohesion: 0.14
Nodes (3): RecruitmentPost, RecruitmentPosts, RecruitmentPostTest

### Community 73 - ".view"
Cohesion: 0.20
Nodes (3): PlayerStats, View, Retinues

### Community 75 - "net.minecraftforge.eventbus.api.SubscribeEvent"
Cohesion: 0.07
Nodes (3): BackBanners, BattleSpotRing, ClientEvents

### Community 79 - "RafasConquest.java"
Cohesion: 0.06
Nodes (6): Ambushes from woods, Deploying to the land (GroundPlan), ConquestContent, ConquestEntities, RafasConquest, ConquestWorldgen

### Community 81 - "City Walls"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "Land Grants"
Cohesion: 0.08
Nodes (13): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+5 more)

### Community 84 - ".sender"
Cohesion: 0.07
Nodes (6): CityView, FigureStrike, HornOrders, RequestBaseMap, SetSetting, TeleportTo

### Community 86 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.13
Nodes (9): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), Epic Knights optional armour (Armoury), Martial titles (Sword Saint, Sword King, Sword God), Organizations, RafasConquest (PvE addon for Villager Recruits), Sword sanctums (+1 more)

### Community 87 - "Battle Sounds"
Cohesion: 0.22
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 89 - "Rank"
Cohesion: 0.08
Nodes (13): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+5 more)

### Community 90 - "BaseMap"
Cohesion: 0.12
Nodes (6): BaseMap, Nation, BaseMapStore, Ready, Quests, Target

### Community 92 - "Peerage"
Cohesion: 0.14
Nodes (6): Merit, Peer, Peerage, Taken, PeerageSync, QuestProgress

### Community 93 - "Point"
Cohesion: 0.11
Nodes (8): Point, Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 100 - "MapContext"
Cohesion: 0.11
Nodes (8): BaseMapLayer, Hit, LabelLayer, MapContext, MapView, Place, PlaceMenuLayer, PoiIconLayer

### Community 102 - "Battle Site"
Cohesion: 0.20
Nodes (5): BattleSite, Part, Water, BattleSite.Water, BattleSiteTest

### Community 103 - "Realm Planner Tests"
Cohesion: 0.18
Nodes (4): Demographics, Share, RealmPlannerTest, World

### Community 107 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 108 - "Magic Schools"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 109 - "ConquestWorldgen"
Cohesion: 0.05
Nodes (8): StreetWalks, FactionBannerNbt, CityDistrictStructure, CityStructure, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 110 - "Doctrine"
Cohesion: 0.16
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 111 - "Organizations"
Cohesion: 0.33
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 114 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 117 - ".of"
Cohesion: 0.20
Nodes (3): FactionBanner, Layer, FactionBannerTest

### Community 118 - ".player"
Cohesion: 0.19
Nodes (6): GrandStrategy, View, NobleTitle, COUNT, KNIGHT, NONE

### Community 120 - "Distance Scaling"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 122 - "BattlePlan"
Cohesion: 0.07
Nodes (8): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, BattlePlanTest

### Community 124 - "BoardScreen"
Cohesion: 0.13
Nodes (4): BoardScreen, BoardAction, MarkerSet, MarkerView

### Community 125 - "net.minecraft.core.BlockPos"
Cohesion: 0.14
Nodes (3): Saved, CharacterMarkers, Placed

### Community 127 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.25
Nodes (3): CourtSet, Seat, Style

### Community 128 - "Block Packer"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 129 - "ArrowVolleys"
Cohesion: 0.16
Nodes (4): DistantDepth, Arrow, ArrowVolleys, Figure

### Community 130 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 132 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 134 - "Distribution"
Cohesion: 0.20
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 135 - "Horn Calls"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 139 - "Quest"
Cohesion: 0.17
Nodes (8): Boards, Entry, View, Ladder, MILITARY, NOBLE, NONE, Quest

### Community 140 - "Ranks (Soldier to Grand Marshal)"
Cohesion: 0.22
Nodes (9): Enlisted players, Gold church, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Land grants (seven ways), Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter (+1 more)

### Community 144 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.25
Nodes (5): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Battle roles (frontline, bait, support, flanker, kill squad), Victory, pursuit and surrender

### Community 151 - ".begin"
Cohesion: 0.06
Nodes (10): Api, DistantGeneration, Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT (+2 more)

### Community 152 - "FormationSnapshot"
Cohesion: 0.19
Nodes (3): FormationSnapshot, Positions, DistantArmies

### Community 154 - "Kind"
Cohesion: 0.25
Nodes (7): Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN

### Community 155 - "ConquestServer"
Cohesion: 0.13
Nodes (4): RecruitsBridge, CityGuards, ConquestServer, StructureLink

### Community 157 - "ConquestSavedData"
Cohesion: 0.09
Nodes (3): ConquestSavedData, EconomyTick, ConquestSavedDataTest

### Community 162 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.06
Nodes (10): BattleSnapshot, BattleSpot, FactionInfoReply, GrandStrategyView, PeerageView, TalkView, UnitsRequest, UnitsState (+2 more)

### Community 163 - "net.minecraftforge.eventbus.api.SubscribeEvent"
Cohesion: 0.05
Nodes (3): BattleKeepInventory, ClientModEvents, ConquestEvents

### Community 165 - "BattleRole"
Cohesion: 0.25
Nodes (6): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT

### Community 173 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 176 - "MapLayer"
Cohesion: 0.14
Nodes (6): LegendEntry, MapLayer, Symbol, RoadLayer, TerritoryLayer, UnitLayer

### Community 219 - "Battles (prototype)"
Cohesion: 0.29
Nodes (6): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Envelopment, War camps, War council

### Community 222 - "Kind"
Cohesion: 0.29
Nodes (7): Kind, CAPITAL, CASTLE, CITY, LORD, RETAINER, TOWN

### Community 223 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 224 - "Kind"
Cohesion: 0.33
Nodes (6): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER

### Community 226 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

## Ambiguous Edges - Review These
- `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)` → `/pve debug battle simulate`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Roads (high, city, town roads; bridges; signposts)` → `Mustering (offensive and ceremonial)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **307 isolated node(s):** `CASTLE`, `HAMLET`, `RUIN`, `TOWER`, `WELL` (+302 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 845 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **127 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `net.minecraft.world.level.ChunkPos`, `.army`, `BattleScreen`, `.state`, `rafasconquest.battle.BattlePlan.Point`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `RecruitsBattle.java`, `BattleMap.java`, `.begin`, `Commands Guide`, `net.minecraft.world.item.ItemStack`, `War Council & War Plans`, `PveCommands Registration`, `net.minecraftforge.eventbus.api.SubscribeEvent`, `Recruits Factions`, `.enlist`, `War`, `net.minecraft.core.BlockPos`, `net.minecraft.server.level.ServerPlayer`, `Peerage`, `UnitMode`, `GameCharacter`, `Shape`, `Battle Site`, `net.minecraft.world.entity.Entity`?**
  _High betweenness centrality (0.072) - this node is a cross-community bridge._
- **What connects `CASTLE`, `HAMLET`, `RUIN` to the rest of the system?**
  _307 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.08209876543209876 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `Organization Screen`, `ConquestNetwork.java`, `Settlement`, `ProvinceMap`, `ClientMapCache`, `RecruitsBattle.java`, `PoiPrecomputer.java`, `net.minecraft.world.item.ItemStack`, `War Council & War Plans`, `Wars Service`, `Countryside`, `Recruiter`, `arraylist`, `.render`, `Character Screen & Portraits`, `Base Map Generator`, `Realm`, `ConquestServer`, `.place`, `Title Map`, `PoliticalStyle`, `Recruitment Post Structure`, `RafasConquest.java`, `Land Grants`, `Peerage`, `Terrain Reading`, `MapContext`, `Magic Schools`, `Client Politics`, `GrandStrategyScreen`, `ConquestRuntime`, `.sample`?**
  _High betweenness centrality (0.048) - this node is a cross-community bridge._
- **Should `Shared Java Utilities & Tests` be split into smaller, more focused modules?**
  _Cohesion score 0.09803921568627451 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)` and `/pve debug battle simulate`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Roads (high, city, town roads; bridges; signposts)` and `Mustering (offensive and ceremonial)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._