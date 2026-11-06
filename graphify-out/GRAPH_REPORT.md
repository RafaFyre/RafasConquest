# Graph Report - RafasConquest  (2026-11-06)

## Corpus Check
- 385 files · ~303,666 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 100 file(s) not represented in the graph (top: .nbt 84, (none) 3, .mcfunction 2)

## Summary
- 5365 nodes · 16981 edges · 272 communities (115 shown, 157 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2262 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `e51f2485`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- RafasConquest.java
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsQuality.java
- WorldSettings
- FieldBlock
- Build
- BattlePlan.java
- BaseMap
- Lands
- .place
- ConquestServer
- Battle Map Web Page & Site
- Person
- Plan
- BattleMap.java
- PoliticsTest
- CityLayoutTest
- Commands Guide
- CityManagerScreen
- TitleMap
- net.minecraft.world.item.ItemStack
- MarkerScreen
- WarCouncil
- rafasconquest.map.BaseMap
- Settlement
- .postChunk
- Capital Layout & Seed Hash
- PveCommands Registration
- School
- StyleLibrary
- .plan
- CityGridBuilder.java
- net.minecraft.client.gui.GuiGraphics
- Site
- rafasconquest.faction.Faction
- ClientEvents
- RecruitsBattle
- net.minecraft.network.FriendlyByteBuf
- ConquestNetwork
- .render
- Personalities
- SeaLanes
- Centre
- CityGridBuilder
- net.minecraft.core.Vec3i
- RecruitsBattle.java
- NationScreen
- .begin
- WarCamp
- map
- RecruitmentPost
- Faction Map View
- .state
- Peerage
- net.neoforged.neoforge.network.handling.IPayloadContext
- TitleMap
- ClientModEvents.java
- .of
- .plan
- MarkerSpec
- .world
- Arm
- Calibration
- HorizontalStretch
- DistantOcclusion
- .build
- MapLands
- net.minecraft.server.level.ServerPlayer
- FactionInfoScreen
- DayClockTest
- Faction
- rafasconquest.battle.FormationSnapshot
- MapLayer
- Ownership & Territory
- .choose
- Recruiter
- Nation Rows
- CityVariant
- BattleSounds
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- .drawEmpire
- CourtView
- CityTerrainPiece
- CityPlan
- Stats & Settings Screens
- .generate
- CourtSet
- PoliticalStyle
- Strategy
- CityVariant.java
- View
- Kind
- .send
- DistantArmyRenderer
- City
- .board
- .place
- DistantArmyRenderer.java
- City
- Doctrine
- Distribution
- GrandStrategyScreen
- .second
- Retinues
- Homeland
- .measure
- MapContext
- org.junit.jupiter.api.Test
- UnitInfo
- CityStyles
- BoardScreen
- Organizations
- PveCommands.java
- RafasConquest (PvE addon for Villager Recruits)
- .enlist
- .view
- Trait
- ShipHulls
- OrganizationScreen
- BlockGrid
- .onLevelLoad
- .boundsAt
- FormationSnapshot
- ClientPolitics
- Politics.Front
- BattleSnapshot
- SeaLanes
- Call
- Role
- .openMarker
- War
- CLAUDE.md
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- ConquestGameRules
- .plan
- GameCharacter
- Front
- net.minecraft.world.level.block.Rotation
- Small Cluster 170
- ConquestSavedData
- .pack
- .enlist
- Kind
- .render
- ClientScreens
- .player
- Realm
- .onLogout
- org.joml.Matrix4f
- DistantGeneration
- Peerage
- Task
- ConquestEvents
- net.neoforged.fml.common.EventBusSubscriber
- RecruiterScreen
- .drawAligned
- Clash
- .shipOf
- .nearest
- .sendBattleSpot
- net.minecraft.world.level.block.state.BlockState
- .readOffsets
- .aQuarterOfTheCityInTheMiddleOrAgainstAnEdge

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 251 edges
2. `FieldBlock` - 169 edges
3. `FieldBattle` - 158 edges
4. `ConquestNetwork` - 102 edges
5. `FieldArmy` - 99 edges
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

## Communities (272 total, 157 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.06
Nodes (8): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock, Figure

### Community 1 - "RafasConquest.java"
Cohesion: 0.06
Nodes (4): /pve politics, ConquestContent, ConquestEntities, RafasConquest

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.08
Nodes (7): CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourPiece, PlaceholderPiece, RecruitmentPostPiece

### Community 3 - ".army"
Cohesion: 0.08
Nodes (4): Point, DoctrineTest, FieldBattleTest, ArmouryTest

### Community 4 - "CityTerrain"
Cohesion: 0.06
Nodes (21): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+13 more)

### Community 5 - "BattleScreen"
Cohesion: 0.06
Nodes (19): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+11 more)

### Community 6 - "DistantHorizonsQuality.java"
Cohesion: 0.08
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "WorldSettings"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 8 - "FieldBlock"
Cohesion: 0.06
Nodes (20): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+12 more)

### Community 10 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 12 - "BattlePlan.java"
Cohesion: 0.04
Nodes (45): Ambushes from woods, Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards (+37 more)

### Community 13 - "BaseMap"
Cohesion: 0.13
Nodes (5): MapPreview, BaseMap, Nation, BaseMapStore, Ready

### Community 14 - "Lands"
Cohesion: 0.06
Nodes (9): Fort, Lands, Stronghold, Claim, Muster, CEREMONIAL, OFFENSIVE, Politics (+1 more)

### Community 15 - ".place"
Cohesion: 0.15
Nodes (4): Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 16 - "ConquestServer"
Cohesion: 0.11
Nodes (6): RecruitsBridge, CityBanners, CityGuards, ConquestServer, StructureLink, PoiPrecomputer

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.23
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "Plan"
Cohesion: 0.18
Nodes (8): Kind, BARRACKS, CHURCH, NOBLE, PALACE, POST, WALL, Plan

### Community 22 - "CityLayoutTest"
Cohesion: 0.08
Nodes (5): DistrictKey, Townsfolk, CityLayoutTest, CitySizesTest, StreetWalksTest

### Community 23 - "Commands Guide"
Cohesion: 0.10
Nodes (26): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+18 more)

### Community 25 - "TitleMap"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.06
Nodes (21): WarHorn, BackBannerLayer, BackBanners, Armoury, Kit, ARCHER_GUARD, CAPTAIN, CAVALRY (+13 more)

### Community 27 - "MarkerScreen"
Cohesion: 0.07
Nodes (9): BattleSite, Part, Water, BattleSite.Water, Cell, Terrain, Drop, MarkerScreen (+1 more)

### Community 28 - "WarCouncil"
Cohesion: 0.22
Nodes (4): Facts, Officer, Talk, WarCouncil

### Community 29 - "rafasconquest.map.BaseMap"
Cohesion: 0.11
Nodes (4): FeudDuel, MusterBars, PoliticsSync, Wars

### Community 30 - "Settlement"
Cohesion: 0.12
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 32 - "Capital Layout & Seed Hash"
Cohesion: 0.13
Nodes (7): CapitalLayout, Land, Site, OriginRoller, Roll, CapitalRelocationTest, LayoutTest

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 36 - ".plan"
Cohesion: 0.24
Nodes (3): Heap, Search, ShippingRoutes

### Community 37 - "CityGridBuilder.java"
Cohesion: 0.05
Nodes (9): /pve debug citystats [samples], CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement (+1 more)

### Community 38 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.05
Nodes (4): Character page, Court 3D scene (CourtView), Nation page, InventoryStatsButton

### Community 39 - "Site"
Cohesion: 0.11
Nodes (6): CityLayout, Site, SlopeTile, ChunkCoord, FarmLayout, WorldSettings

### Community 42 - "RecruitsBattle"
Cohesion: 0.03
Nodes (18): Card, Enlisted, Officers, RecruitsBattle, Role, BEARER, GENERAL, GUARD (+10 more)

### Community 43 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.07
Nodes (6): BaseMapPart, FactionInfoReply, PeerageView, PoliticsView, Volleys, WorldStretch

### Community 44 - "ConquestNetwork"
Cohesion: 0.10
Nodes (10): CityClose, CityView, ConquestNetwork, FigureStrike, HornOrders, RequestBaseMap, RequestFactionInfo, SetSetting (+2 more)

### Community 45 - ".render"
Cohesion: 0.07
Nodes (8): CharacterScreen, Retainer, Spot, ClientPeerage, Banner, Portraits, Honour, Honours

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "Centre"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 51 - "net.minecraft.core.Vec3i"
Cohesion: 0.12
Nodes (3): Offsets, StylePiece, CityPlacement

### Community 52 - "RecruitsBattle.java"
Cohesion: 0.05
Nodes (3): /pve debug recruits, Envelopment, War camps

### Community 53 - "NationScreen"
Cohesion: 0.14
Nodes (3): NationScreen, Demographics, Share

### Community 54 - ".begin"
Cohesion: 0.11
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 55 - "WarCamp"
Cohesion: 0.09
Nodes (4): Saved, WarCamp, CharacterMarkers, Placed

### Community 60 - "Peerage"
Cohesion: 0.15
Nodes (6): Boards, Entry, View, Taken, Quest, QuestProgress

### Community 61 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.15
Nodes (6): BackBanner, Builder, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 63 - "TitleMap"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 65 - ".of"
Cohesion: 0.18
Nodes (4): FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 66 - ".plan"
Cohesion: 0.14
Nodes (3): SeatCities, SeatCitiesTest, World

### Community 67 - "MarkerSpec"
Cohesion: 0.07
Nodes (5): MarkerRenderer, Palette, WallTemplates, MarkerBlockEntity, MarkerSpec

### Community 68 - ".world"
Cohesion: 0.11
Nodes (4): Hook, Succession, Taking, RealmPlannerTest

### Community 69 - "Arm"
Cohesion: 0.14
Nodes (11): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, FieldRules, Rates, Side (+3 more)

### Community 70 - "Calibration"
Cohesion: 0.18
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 71 - "HorizontalStretch"
Cohesion: 0.12
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 72 - "DistantOcclusion"
Cohesion: 0.11
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 73 - ".build"
Cohesion: 0.15
Nodes (3): District, Frame, Harbour

### Community 75 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.14
Nodes (3): CityManager, PlayerStats, View

### Community 77 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 78 - "Faction"
Cohesion: 0.11
Nodes (4): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), Faction, EconomyTick

### Community 79 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.28
Nodes (6): Boxes, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 81 - "MapLayer"
Cohesion: 0.19
Nodes (5): LegendEntry, MapLayer, Symbol, RoadLayer, UnitLayer

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.20
Nodes (8): Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY, RUINS, SPARE_COUNTY_VASSAL

### Community 84 - "Recruiter"
Cohesion: 0.07
Nodes (5): RecruiterRenderer, TownspersonRenderer, Walk, Recruiter, Townsperson

### Community 86 - "CityVariant"
Cohesion: 0.16
Nodes (7): PlacedWall, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 87 - "BattleSounds"
Cohesion: 0.13
Nodes (5): BattleSounds, ChargeSound, DinSound, Source, DistantArmies

### Community 89 - ".drawEmpire"
Cohesion: 0.23
Nodes (3): Hit, LabelLayer, LabelShape

### Community 91 - "CourtView"
Cohesion: 0.14
Nodes (4): Camera, CourtView, Figure, Organization

### Community 93 - "CityPlan"
Cohesion: 0.19
Nodes (3): CityPlan, Piece, Building

### Community 95 - ".generate"
Cohesion: 0.07
Nodes (13): ProvinceNames, WorldFeatures, BaseMapGenerator, Heights, MapColors, Homeland, DESERT, JUNGLE (+5 more)

### Community 96 - "CourtSet"
Cohesion: 0.23
Nodes (3): CourtSet, Seat, Style

### Community 99 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 101 - "View"
Cohesion: 0.12
Nodes (4): BoardView, GrandStrategyView, StatsView, TalkView

### Community 103 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 107 - "DistantArmyRenderer"
Cohesion: 0.11
Nodes (3): DistantArmyRenderer, Figure, Hull

### Community 108 - "City"
Cohesion: 0.11
Nodes (6): Countryside, Plan, City, Grid, CountrysideTest, World

### Community 109 - ".board"
Cohesion: 0.15
Nodes (10): Ladder, MILITARY, NOBLE, NONE, Type, CLEAR, DELIVER, SCOUT (+2 more)

### Community 111 - "DistantArmyRenderer.java"
Cohesion: 0.08
Nodes (4): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), DistantDepth

### Community 113 - "City"
Cohesion: 0.12
Nodes (16): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Land (+8 more)

### Community 114 - "Doctrine"
Cohesion: 0.16
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 115 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 117 - ".second"
Cohesion: 0.07
Nodes (9): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT (+1 more)

### Community 118 - "Retinues"
Cohesion: 0.17
Nodes (4): GrandStrategy, View, Retinues, RetinuesTest

### Community 119 - "Homeland"
Cohesion: 0.20
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 122 - "org.junit.jupiter.api.Test"
Cohesion: 0.05
Nodes (10): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, Outcome (+2 more)

### Community 123 - "UnitInfo"
Cohesion: 0.22
Nodes (3): UnitInfo, UnitsRequest, UnitsState

### Community 126 - "Organizations"
Cohesion: 0.33
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 127 - "PveCommands.java"
Cohesion: 0.06
Nodes (16): /pve debug cityterrain, /pve politics war <attacker> <defender> [generals], SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType (+8 more)

### Community 128 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 129 - ".enlist"
Cohesion: 0.09
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 130 - ".view"
Cohesion: 0.36
Nodes (3): Option, Talk, View

### Community 132 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 133 - "ShipHulls"
Cohesion: 0.18
Nodes (5): DistantShips, Far, BattleKind, Part, ShipHulls

### Community 137 - ".onLevelLoad"
Cohesion: 0.13
Nodes (3): ConquestConfig, CityIndex, ConquestRuntime

### Community 138 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 139 - "FormationSnapshot"
Cohesion: 0.11
Nodes (12): FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN (+4 more)

### Community 143 - "SeaLanes"
Cohesion: 0.08
Nodes (9): Hull, Ramming, BattleShips, Helm, Ram, SableLink, SableShips, SableSeen (+1 more)

### Community 144 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 146 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 148 - ".openMarker"
Cohesion: 0.18
Nodes (3): BoardAction, MarkerSet, MarkerView

### Community 149 - "War"
Cohesion: 0.09
Nodes (8): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Army, Goal, War

### Community 152 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.33
Nodes (4): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender

### Community 156 - ".plan"
Cohesion: 0.15
Nodes (5): Faith, Bond, RealmPlanner, Seat, World

### Community 163 - "Front"
Cohesion: 0.12
Nodes (4): Feud, Lord, Feuds, Front

### Community 178 - "ConquestSavedData"
Cohesion: 0.08
Nodes (3): ConquestSavedData, ConquestSavedDataTest, FactionInfoTest

### Community 179 - ".pack"
Cohesion: 0.35
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 204 - ".enlist"
Cohesion: 0.10
Nodes (12): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+4 more)

### Community 206 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 218 - ".player"
Cohesion: 0.15
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 219 - "Realm"
Cohesion: 0.18
Nodes (4): Kind, FRIEND, RIVAL, Realm

### Community 228 - "Peerage"
Cohesion: 0.12
Nodes (6): Grant, LandGrants, Merit, Peer, Peerage, PeerageSync

### Community 232 - "Task"
Cohesion: 0.17
Nodes (8): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT, GeneralTask

### Community 248 - "Clash"
Cohesion: 0.28
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 249 - ".shipOf"
Cohesion: 0.25
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 257 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (6): CommandTableBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, WorldMapItem, MarkerBlock

## Ambiguous Edges - Review These
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **335 isolated node(s):** `OFF`, `ON`, `VIEW`, `CASTLE`, `HAMLET` (+330 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 965 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **157 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `org.jetbrains.annotations.Nullable`, `RafasConquest.java`, `net.minecraft.world.level.ChunkPos`, `.army`, `.onServerTick`, `Trait`, `BattleScreen`, `FieldBlock`, `BaseMap`, `BattleMap.java`, `net.minecraft.world.item.ItemStack`, `MarkerScreen`, `WarCouncil`, `GameCharacter`, `rafasconquest.map.BaseMap`, `PveCommands Registration`, `rafasconquest.faction.Faction`, `RecruitsBattle.java`, `WarCamp`, `map`, `.state`, `Calibration`, `net.minecraft.server.level.ServerPlayer`, `.enlist`, `CityVariant.java`, `Peerage`, `Task`, `.second`, `.shipOf`, `org.junit.jupiter.api.Test`?**
  _High betweenness centrality (0.076) - this node is a cross-community bridge._
- **What connects `OFF`, `ON`, `VIEW` to the rest of the system?**
  _335 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.05572139303482587 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `RafasConquest.java`, `.view`, `OrganizationScreen`, `.onLevelLoad`, `list`, `ClientPolitics`, `.place`, `ConquestServer`, `net.minecraft.world.item.ItemStack`, `MarkerScreen`, `.plan`, `WarCouncil`, `Settlement`, `rafasconquest.map.BaseMap`, `.postChunk`, `School`, `CityGridBuilder.java`, `net.minecraft.client.gui.GuiGraphics`, `.render`, `Centre`, `ConquestSavedData`, `NationScreen`, `map`, `TitleMap`, `MapLands`, `ClientScreens`, `Realm`, `.generate`, `PoliticalStyle`, `Peerage`, `City`, `.place`, `GrandStrategyScreen`, `Retinues`, `MapContext`, `PveCommands.java`?**
  _High betweenness centrality (0.040) - this node is a cross-community bridge._
- **Should `RafasConquest.java` be split into smaller, more focused modules?**
  _Cohesion score 0.061955965181771634 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._