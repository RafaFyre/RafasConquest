# Graph Report - RafasConquest  (2026-11-05)

## Corpus Check
- 351 files · ~258,188 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 89 file(s) not represented in the graph (top: .nbt 76, (none) 3, .mcfunction 2)

## Summary
- 4731 nodes · 14741 edges · 243 communities (102 shown, 141 thin omitted)
- Extraction: 88% EXTRACTED · 12% INFERRED · 0% AMBIGUOUS · INFERRED: 1790 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `2af7170e`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- .board
- WorldSettings
- net.minecraft.core.Vec3i
- net.minecraft.world.level.block.state.BlockState
- .block
- Politics
- ConquestNetwork
- rafasconquest.battle.FieldBlock
- ProvinceMap
- DistantGeneration
- RecruitsBattle.java
- Battle Map Web Page & Site
- org.jetbrains.annotations.Nullable
- Recruiter
- net.minecraft.world.phys.Vec3
- .postProcess
- Commands Guide
- CityManagerScreen
- Nations Planning
- net.minecraft.world.item.ItemStack
- .fire
- War Council & War Plans
- Wars Service
- Roads
- Builder
- Capital Layout & Seed Hash
- PveCommands Registration
- .passOnOrders
- Countryside
- City
- .plan
- City Plan Pieces
- Characters (holders, skills, prowess, traits)
- net.minecraft.server.level.ServerLevel
- FactionInfoScreen
- ArrowVolleys
- World Stretch Screen
- rafasconquest.battle.BattlePlan.Point
- Faction
- Personalities
- DayClockTest
- War
- CommandTableBlock.java
- .render
- Base Map Generator
- StyleLibrary
- .sender
- Realm
- net.minecraft.core.BlockPos
- .of
- CityVariant
- Faction Map View
- .drawEmpire
- ConquestServer
- Plan
- Townsperson
- Title Map
- Field Rules & Arms
- .render
- DistantArmyRenderer.java
- Distant Horizons Armies
- PoliticalStyle
- Battle Calibration
- RecruitmentPost
- FormationSnapshot
- Settlement
- CharacterMarkers.java
- Map Lands
- net.minecraft.server.level.ServerPlayer
- .roadStart
- View
- BattleKeepInventory.java
- City Walls
- Ownership & Territory
- Land Grants
- CityGridBuilder
- Nation Rows
- CourtSet
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
- BattleKeepInventory.java
- MapContext
- City Stats
- Battle Site
- Realm Planner Tests
- MarkerView
- net.neoforged.bus.api.SubscribeEvent
- net.minecraft.nbt.CompoundTag
- Magic Schools
- PoiPrecomputer.java
- Doctrine
- Kind
- PoliticsTest
- Client Politics
- FieldBlock
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- GrandStrategyScreen
- net.minecraft.core.BlockPos
- .view
- CourtView
- Distance Scaling
- BattlePlan
- ConquestRuntime
- BoardScreen
- net.minecraft.resources.ResourceLocation
- .onLogout
- Race
- Block Packer
- net.minecraft.world.level.block.Rotation
- .accept
- .findGenerationPoint
- Trait
- SeatCitiesTest
- .player
- Horn Calls
- DistantHorizonsLink
- Kind
- Organization Screen
- PoliticsData
- NationScreen
- .hash
- Homeland
- CityLayoutTest
- Duty
- MarkerScreen
- Game Rules
- Kit
- CityManager
- CLAUDE.md
- .view
- ConquestNetwork
- .postChunk
- ConquestSavedData
- net.minecraft.network.FriendlyByteBuf
- ConquestEvents
- BattleRole
- net.minecraft.client.gui.GuiGraphics
- Small Cluster 170
- Role
- ClientScreens
- .render
- BackBannerLayer.java
- CountryStructure
- rafasconquest.core.WorldSettings
- Ranks
- Strategy
- Stat
- WarPlan
- UnitMode
- UnitInfo
- RafasConquest (PvE addon for Villager Recruits)
- Phase
- .drawAligned
- QuestBoardBlock
- Build
- ItemFlags.java
- Distribution
- .model
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- .postProcess
- ClientMapCache
- GameCharacter
- WarHorn
- .readOffsets

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 246 edges
2. `FieldBattle` - 122 edges
3. `FieldBlock` - 118 edges
4. `ConquestNetwork` - 105 edges
5. `FieldArmy` - 94 edges
6. `BaseMap` - 89 edges
7. `Point` - 70 edges
8. `BattleScreen` - 60 edges
9. `ConquestSavedData` - 60 edges
10. `Politics` - 51 edges

## Surprising Connections (you probably didn't know these)
- `Roads and cities` --conceptually_related_to--> `City slots (walls, common, noble, landmarks)`  [INFERRED]
  docs/index.html → src/main/resources/rafasconquest_structures/README.txt
- `Technical architecture components` --semantically_similar_to--> `Real vs calculated battles (player within 400 blocks)`  [INFERRED] [semantically similar]
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

## Communities (243 total, 141 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.09
Nodes (5): FieldArmy, Event, FieldBattle, Order, Outcome

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.07
Nodes (5): StructureLink, CityRoadPiece, CountryPiece, FarmPiece, PlaceholderPiece

### Community 4 - "CityTerrain"
Cohesion: 0.05
Nodes (22): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+14 more)

### Community 5 - "BattleScreen"
Cohesion: 0.06
Nodes (19): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+11 more)

### Community 6 - ".board"
Cohesion: 0.12
Nodes (10): Ladder, MILITARY, NOBLE, NONE, Type, CLEAR, DELIVER, SCOUT (+2 more)

### Community 8 - "net.minecraft.core.Vec3i"
Cohesion: 0.11
Nodes (3): Offsets, StylePiece, CityPlacement

### Community 9 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.11
Nodes (3): Saved, CommandTableBlock, MarkerBlock

### Community 11 - "Politics"
Cohesion: 0.10
Nodes (9): Lands, Stronghold, Claim, Event, Front, Muster, CEREMONIAL, OFFENSIVE (+1 more)

### Community 13 - "rafasconquest.battle.FieldBlock"
Cohesion: 0.10
Nodes (6): Figure, Shape, LINE, SQUARE, V, WEDGE

### Community 14 - "ProvinceMap"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 16 - "RecruitsBattle.java"
Cohesion: 0.05
Nodes (13): Battles (prototype), Blocks of 20 soldiers, Enlisted players, Envelopment, Gold church, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Land grants (seven ways), Peerage (military and noble ladders) (+5 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 21 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.10
Nodes (8): Officers, Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER, Side

### Community 23 - "Commands Guide"
Cohesion: 0.09
Nodes (30): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+22 more)

### Community 25 - "Nations Planning"
Cohesion: 0.17
Nodes (6): Nation, Nations, Plan, Seats, NationsTest, World

### Community 26 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.18
Nodes (4): Armoury, Outfit, Style, RankKits

### Community 28 - "War Council & War Plans"
Cohesion: 0.18
Nodes (4): Facts, Officer, Talk, WarCouncil

### Community 29 - "Wars Service"
Cohesion: 0.16
Nodes (3): MusterBars, PoliticsSync, Wars

### Community 30 - "Roads"
Cohesion: 0.05
Nodes (20): Grade, CITY, DOJO, HIGH, TOWN, Junction, Land, Link (+12 more)

### Community 31 - "Builder"
Cohesion: 0.10
Nodes (7): Builder, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel

### Community 32 - "Capital Layout & Seed Hash"
Cohesion: 0.15
Nodes (6): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest, StreetWalksTest

### Community 35 - "Countryside"
Cohesion: 0.18
Nodes (4): Plan, Grid, CountrysideTest, World

### Community 36 - "City"
Cohesion: 0.10
Nodes (9): Dojo, City, SeatCities, Tier, COUNTY, DUCHY, EMPIRE, KINGDOM (+1 more)

### Community 37 - ".plan"
Cohesion: 0.13
Nodes (7): Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 38 - "City Plan Pieces"
Cohesion: 0.21
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 39 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.04
Nodes (39): Ambushes from woods, Base map generated from seed, Battle phases and days, Character page, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Command Table block (+31 more)

### Community 42 - "ArrowVolleys"
Cohesion: 0.20
Nodes (3): Arrow, ArrowVolleys, Figure

### Community 43 - "World Stretch Screen"
Cohesion: 0.12
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 45 - "Faction"
Cohesion: 0.08
Nodes (14): SeedHash, Faction, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD (+6 more)

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 48 - "War"
Cohesion: 0.12
Nodes (3): Goal, War, Politics.Front

### Community 49 - "CommandTableBlock.java"
Cohesion: 0.14
Nodes (3): GrandStrategyBlock, StrategyBoardBlock, WorldMapItem

### Community 50 - ".render"
Cohesion: 0.16
Nodes (4): CharacterScreen, Spot, Banner, Portraits

### Community 51 - "Base Map Generator"
Cohesion: 0.11
Nodes (5): ProvinceNames, WorldFeatures, BaseMapGenerator, Heights, BiomePalette

### Community 53 - ".sender"
Cohesion: 0.08
Nodes (6): CityView, FigureStrike, HornOrders, SetSetting, TalkChoice, TeleportTo

### Community 54 - "Realm"
Cohesion: 0.06
Nodes (9): Countryside, Dojos, Names, Organization, Organizations, Sanctum, Person, Realm (+1 more)

### Community 56 - ".of"
Cohesion: 0.18
Nodes (4): FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 57 - "CityVariant"
Cohesion: 0.15
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 59 - ".drawEmpire"
Cohesion: 0.23
Nodes (3): Hit, LabelLayer, LabelShape

### Community 60 - "ConquestServer"
Cohesion: 0.09
Nodes (5): RecruitsBridge, CityBanners, CityGuards, ConquestServer, PoiPrecomputer

### Community 61 - "Plan"
Cohesion: 0.14
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, POST, WALL, Piece (+1 more)

### Community 63 - "Title Map"
Cohesion: 0.15
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 64 - "Field Rules & Arms"
Cohesion: 0.09
Nodes (12): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, Volley, FieldRules, Rates (+4 more)

### Community 65 - ".render"
Cohesion: 0.14
Nodes (3): ClientPeerage, Honour, Honours

### Community 66 - "DistantArmyRenderer.java"
Cohesion: 0.13
Nodes (4): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), BattleSpotRing

### Community 69 - "Battle Calibration"
Cohesion: 0.09
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 70 - "RecruitmentPost"
Cohesion: 0.24
Nodes (3): RecruitmentPost, RecruitmentPostPiece, RecruitmentPostTest

### Community 71 - "FormationSnapshot"
Cohesion: 0.11
Nodes (5): FormationSnapshot, Positions, Sprite, DistantArmies, FormationSnapshotTest

### Community 72 - "Settlement"
Cohesion: 0.19
Nodes (8): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex

### Community 76 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.09
Nodes (4): Card, Enlisted, RecruitsBattle, Unit

### Community 78 - "View"
Cohesion: 0.16
Nodes (3): BoardView, GrandStrategyView, TalkView

### Community 81 - "City Walls"
Cohesion: 0.17
Nodes (8): CityLayout, Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "Land Grants"
Cohesion: 0.14
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 84 - "CityGridBuilder"
Cohesion: 0.14
Nodes (3): Houses, CityGridBuilder, Ground

### Community 86 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 87 - "Battle Sounds"
Cohesion: 0.20
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 89 - "Rank"
Cohesion: 0.12
Nodes (10): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+2 more)

### Community 90 - "BaseMap"
Cohesion: 0.13
Nodes (5): MapPreview, BaseMap, Nation, BaseMapStore, Ready

### Community 92 - "Peerage"
Cohesion: 0.09
Nodes (10): Boards, Entry, View, Merit, Peer, Peerage, Taken, PeerageSync (+2 more)

### Community 93 - "Point"
Cohesion: 0.11
Nodes (8): Point, Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 96 - "Distant Army Renderer"
Cohesion: 0.15
Nodes (3): DistantDepth, Figure, DistantArmyRenderer

### Community 100 - "MapContext"
Cohesion: 0.18
Nodes (3): MapContext, Place, PlaceMenuLayer

### Community 102 - "Battle Site"
Cohesion: 0.20
Nodes (5): BattleSite, Part, Water, BattleSite.Water, BattleSiteTest

### Community 103 - "Realm Planner Tests"
Cohesion: 0.19
Nodes (4): Demographics, Share, RealmPlannerTest, World

### Community 105 - "MarkerView"
Cohesion: 0.15
Nodes (4): BoardAction, MarkerSet, MarkerView, MarkerSpec

### Community 108 - "Magic Schools"
Cohesion: 0.14
Nodes (11): School, BLOOD, FIRE, ICE, IRON, LIGHT, LIGHTNING, SHADOW (+3 more)

### Community 109 - "PoiPrecomputer.java"
Cohesion: 0.04
Nodes (10): StreetWalks, Entry, RafasConquest, CityDistrictStructure, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement (+2 more)

### Community 110 - "Doctrine"
Cohesion: 0.16
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 111 - "Kind"
Cohesion: 0.20
Nodes (8): Hold, Kind, CAPITAL, CASTLE, CITY, LORD, RETAINER, TOWN

### Community 114 - "FieldBlock"
Cohesion: 0.07
Nodes (9): BlockGrid, FieldBlock, Task, AMBUSH, BAIT, FLANK, KILL, NONE (+1 more)

### Community 118 - ".view"
Cohesion: 0.15
Nodes (4): PlayerStats, View, Retinues, RetinuesTest

### Community 120 - "Distance Scaling"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 122 - "BattlePlan"
Cohesion: 0.06
Nodes (11): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, Ground (+3 more)

### Community 127 - "Race"
Cohesion: 0.12
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 128 - "Block Packer"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 129 - "net.minecraft.world.level.block.Rotation"
Cohesion: 0.18
Nodes (3): PlacedWall, GoldChurch, Layout

### Community 132 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 134 - ".player"
Cohesion: 0.14
Nodes (7): GrandStrategy, View, NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 135 - "Horn Calls"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 137 - "Kind"
Cohesion: 0.25
Nodes (7): Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN

### Community 141 - ".hash"
Cohesion: 0.20
Nodes (4): ChunkCoord, FarmLayout, OriginRoller, Roll

### Community 142 - "Homeland"
Cohesion: 0.15
Nodes (9): Culture, Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP (+1 more)

### Community 144 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 148 - "Kit"
Cohesion: 0.14
Nodes (13): Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD, HEAVY_INFANTRY (+5 more)

### Community 151 - ".view"
Cohesion: 0.36
Nodes (3): Option, Talk, View

### Community 152 - "ConquestNetwork"
Cohesion: 0.08
Nodes (10): BackBanner, BaseMapPart, CityClose, ConquestNetwork, PoliticsView, RequestBaseMap, RequestFactionInfo, StatsRequest (+2 more)

### Community 157 - "ConquestSavedData"
Cohesion: 0.08
Nodes (5): ConquestSavedData, Poi, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 162 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.11
Nodes (5): BattleSnapshot, BattleSpot, FactionInfoReply, PeerageView, StatsView

### Community 165 - "BattleRole"
Cohesion: 0.10
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 167 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.13
Nodes (6): LegendEntry, MapLayer, Symbol, PoiIconLayer, RoadLayer, UnitLayer

### Community 173 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 220 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 221 - "Stat"
Cohesion: 0.18
Nodes (7): Stat, DIPLOMACY, INTRIGUE, LEARNING, MARTIAL, PROWESS, STEWARDSHIP

### Community 223 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 224 - "UnitInfo"
Cohesion: 0.22
Nodes (3): UnitInfo, UnitsRequest, UnitsState

### Community 225 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 226 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 229 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 231 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 233 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.33
Nodes (4): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **307 isolated node(s):** `NORTH`, `EAST`, `SOUTH`, `WEST`, `WALL` (+302 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 846 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **141 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `net.minecraft.server.level.ServerPlayer` to `net.minecraft.world.level.ChunkPos`, `.army`, `BattleScreen`, `.block`, `rafasconquest.battle.FieldBlock`, `RecruitsBattle.java`, `org.jetbrains.annotations.Nullable`, `net.minecraft.world.phys.Vec3`, `Commands Guide`, `net.minecraft.world.item.ItemStack`, `War Council & War Plans`, `Wars Service`, `PveCommands Registration`, `.passOnOrders`, `ConquestEvents`, `net.minecraft.server.level.ServerLevel`, `rafasconquest.battle.BattlePlan.Point`, `net.minecraft.core.BlockPos`, `Battle Calibration`, `BattleKeepInventory.java`, `Peerage`, `UnitMode`, `Phase`, `City Stats`, `Battle Site`, `GameCharacter`, `net.minecraft.world.entity.Entity`?**
  _High betweenness centrality (0.091) - this node is a cross-community bridge._
- **What connects `NORTH`, `EAST`, `SOUTH` to the rest of the system?**
  _307 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.08568646543330087 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `.board`, `Organization Screen`, `ConquestNetwork`, `ProvinceMap`, `org.jetbrains.annotations.Nullable`, `BattleMap.java`, `Kit`, `.view`, `net.minecraft.world.item.ItemStack`, `.postChunk`, `War Council & War Plans`, `Wars Service`, `Roads`, `Countryside`, `City`, `Faction`, `Base Map Generator`, `Realm`, `ConquestServer`, `Title Map`, `.render`, `PoliticalStyle`, `Settlement`, `.render`, `Land Grants`, `Peerage`, `Terrain Reading`, `MapContext`, `.model`, `ClientMapCache`, `PoiPrecomputer.java`, `Client Politics`, `GrandStrategyScreen`, `ConquestRuntime`?**
  _High betweenness centrality (0.067) - this node is a cross-community bridge._
- **Should `Shared Java Utilities & Tests` be split into smaller, more focused modules?**
  _Cohesion score 0.08208955223880597 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._