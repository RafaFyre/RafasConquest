# Graph Report - RafasConquest  (2026-11-07)

## Corpus Check
- 415 files · ~326,239 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 115 file(s) not represented in the graph (top: .nbt 84, .toml 14, (none) 3)

## Summary
- 5705 nodes · 18107 edges · 292 communities (109 shown, 183 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2309 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `51c503b2`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- RecruitsBattle
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsQuality.java
- CityLayout
- Duty
- net.minecraft.server.level.ServerPlayer
- CourtView
- ConquestServer
- Peerage
- BaseMap
- .render
- CityVariant
- net.minecraft.client.gui.GuiGraphics
- Battle Map Web Page & Site
- Person
- FactionInfoScreen
- RafasConquest.java
- PoliticsTest
- PveCommands.java
- CityManagerScreen
- .plan
- CityGridBuilder
- .shift
- WarCouncil
- rafasconquest.map.BaseMap
- ClientPolitics
- ConquestNetwork
- WorldSettings
- PveCommands Registration
- School
- CityPlan
- Router
- ConquestEvents.java
- net.minecraft.world.phys.Vec3
- Characters (holders, skills, prowess, traits)
- rafasconquest.faction.Faction
- Politics.Front
- org.joml.Matrix4f
- Realm
- net.minecraft.core.Vec3i
- Personalities
- SeaLanes
- MouthLayer.java
- NobleAnnex
- CountryStructure
- BattleSounds
- net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
- ConquestSavedData
- net.minecraft.core.BlockPos
- net.minecraft.server.level.ServerLevel
- MarkerScreen
- Faction Map View
- .second
- Swell
- .passOnOrders
- TitleMapTest
- .measure
- StyleLibrary
- net.minecraft.nbt.CompoundTag
- MarkerSpec
- RafasConquest.java
- .of
- .wallSegments
- DistantOcclusion
- Barracks
- MapLands
- .drawAligned
- PlaceMusicClient
- Recruiter
- Faction
- rafasconquest.battle.FormationSnapshot
- GrandStrategyScreen
- Ownership & Territory
- Peerage
- Grade
- Nation Rows
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- .view
- DialogueScreen
- .view
- SimpleChannel.java
- SableShips
- BoardScreen
- .generate
- net.minecraft.resources.ResourceLocation
- WaterSwell
- .ships
- rafasconquest.battle.BattlePlan.Point
- net.minecraft.nbt.CompoundTag
- CityStyles
- HorizontalStretch
- .player
- ClientModEvents.java
- DistantArmyRenderer
- ClientEvents
- Quests
- Terrain
- DistantArmyRenderer.java
- StatsScreen
- Trait
- PoiPrecomputer.java
- Countryside
- net.minecraft.client.gui.GuiGraphics
- Retinues
- Clash
- .drawEmpire
- BattlePlan
- Land
- RafasConquest (PvE addon for Villager Recruits)
- .onLevelLoad
- Kind
- Task
- Townsperson
- MapContext
- City
- OrganizationScreen
- BattleShips
- .at
- .boundsAt
- BattleKeepInventory
- .onLogout
- BlockGrid
- ClientScreens
- Role
- Ranks (Soldier to Grand Marshal)
- Lands
- Plan
- NobleDistrictTest
- CLAUDE.md
- .named
- .build
- .plan
- ConquestConfig
- Roads
- Lands
- UnitMode
- Job
- Small Cluster 170
- ClientBarks
- .mapWater
- org.jetbrains.annotations.Nullable
- BattleType
- Kind
- .pack
- .of
- RoadFeature
- .touches
- CourtSet
- TitleStyle
- ProvinceNames
- Phase
- .enlist
- CityManager
- net.neoforged.bus.api.SubscribeEvent
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- ConquestGameRules
- PlaceholderPiece
- DistantGeneration
- Call
- net.minecraft.world.level.block.state.BlockState
- MapLayer
- ShipKind
- net.minecraft.world.item.ItemStack
- Ramming
- War
- FormationSnapshot
- Sea.java

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 256 edges
2. `FieldBattle` - 149 edges
3. `FieldBlock` - 139 edges
4. `ConquestNetwork` - 107 edges
5. `FieldArmy` - 100 edges
6. `Lands` - 89 edges
7. `BaseMap` - 78 edges
8. `Politics` - 78 edges
9. `BattleScreen` - 60 edges
10. `Person` - 58 edges

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

## Communities (292 total, 183 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.05
Nodes (7): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 1 - "RecruitsBattle"
Cohesion: 0.05
Nodes (3): Officers, RecruitsBattle, Side

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.08
Nodes (6): BarracksPiece, CityRoadPiece, CountryPiece, GoldChurchPiece, HarbourPiece, NoblePathPiece

### Community 4 - "CityTerrain"
Cohesion: 0.07
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.06
Nodes (19): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+11 more)

### Community 6 - "DistantHorizonsQuality.java"
Cohesion: 0.08
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "CityLayout"
Cohesion: 0.08
Nodes (5): CityLayout, District, Site, SlopeTile, CityLayoutTest

### Community 8 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 9 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.12
Nodes (8): Card, Enlisted, Shape, LINE, SQUARE, V, WEDGE, Unit

### Community 10 - "CourtView"
Cohesion: 0.06
Nodes (12): Camera, CourtView, Figure, NationScreen, Race, BOGFOLK, FROSTBORN, GRASSWALKERS (+4 more)

### Community 11 - "ConquestServer"
Cohesion: 0.16
Nodes (4): CityBanners, ConquestServer, StructureLink, PoiPrecomputer

### Community 12 - "Peerage"
Cohesion: 0.08
Nodes (11): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+3 more)

### Community 13 - "BaseMap"
Cohesion: 0.08
Nodes (6): ClientMapCache, MapPreview, BaseMap, Nation, BaseMapStore, Ready

### Community 14 - ".render"
Cohesion: 0.08
Nodes (8): CharacterScreen, Spot, ClientPeerage, Banner, Portraits, Names, Honour, Honours

### Community 15 - "CityVariant"
Cohesion: 0.07
Nodes (9): PlacedWall, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA, GoldChurch (+1 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.14
Nodes (9): Culture, Marriages, Setter, Wedding, Organizations, Sanctum, Hold, Person (+1 more)

### Community 20 - "RafasConquest.java"
Cohesion: 0.07
Nodes (4): ConquestContent, StreetWalks, ConquestEntities, RafasConquest

### Community 23 - "PveCommands.java"
Cohesion: 0.05
Nodes (42): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+34 more)

### Community 25 - ".plan"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "CityGridBuilder"
Cohesion: 0.12
Nodes (4): Houses, CityGridBuilder, Ground, Platform

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.17
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 29 - "rafasconquest.map.BaseMap"
Cohesion: 0.13
Nodes (3): FeudDuel, MusterBars, Wars

### Community 31 - "ConquestNetwork"
Cohesion: 0.03
Nodes (33): Bark, BaseMapPart, BattleSnapshot, BattleSpot, BoardAction, BoardView, CityClose, ConquestNetwork (+25 more)

### Community 32 - "WorldSettings"
Cohesion: 0.08
Nodes (10): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, WorldSettings (+2 more)

### Community 34 - "School"
Cohesion: 0.09
Nodes (14): Dojos, Ground, School, BLOOD, FIRE, ICE, IRON, LIGHT (+6 more)

### Community 35 - "CityPlan"
Cohesion: 0.14
Nodes (3): CityPlan, Piece, Building

### Community 37 - "ConquestEvents.java"
Cohesion: 0.04
Nodes (8): CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 38 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.08
Nodes (6): Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER

### Community 39 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.07
Nodes (25): Ambushes from woods, Character page, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings), Countryside (hamlets, villages, watchtowers, castles), Court 3D scene (CourtView), Cultures (seven homelands), Demographics (population by culture and race) (+17 more)

### Community 43 - "org.joml.Matrix4f"
Cohesion: 0.14
Nodes (3): DistantDepth, Arrow, ArrowVolleys

### Community 44 - "Realm"
Cohesion: 0.09
Nodes (7): Retainer, Hook, Realm, Succession, Taking, RealmPlannerTest, World

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.15
Nodes (11): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+3 more)

### Community 48 - "MouthLayer.java"
Cohesion: 0.12
Nodes (3): BackBannerLayer, BackBanners, MouthLayer

### Community 52 - "BattleSounds"
Cohesion: 0.13
Nodes (5): BattleSounds, ChargeSound, DinSound, Source, DistantArmies

### Community 53 - "net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate"
Cohesion: 0.38
Nodes (3): BattleKind, Part, ShipHulls

### Community 54 - "ConquestSavedData"
Cohesion: 0.07
Nodes (5): ConquestSavedData, Poi, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 60 - "Swell"
Cohesion: 0.14
Nodes (5): Depths, SeaCalm, Ride, Swell, SwellTest

### Community 61 - ".passOnOrders"
Cohesion: 0.08
Nodes (3): Figure, Landing, Live

### Community 63 - "TitleMapTest"
Cohesion: 0.16
Nodes (3): TitleMap, Land, TitleMapTest

### Community 65 - "StyleLibrary"
Cohesion: 0.11
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.08
Nodes (9): Palette, WallTemplates, Placed, Build, CASTLE, HAMLET, RUIN, TOWER (+1 more)

### Community 67 - "MarkerSpec"
Cohesion: 0.11
Nodes (3): MarkerRenderer, MarkerBlockEntity, MarkerSpec

### Community 69 - "RafasConquest.java"
Cohesion: 0.04
Nodes (9): Base map generated from seed, Battle map developer web page, Command Table block, Faction Info screen, World map layers, Morale, rout and rally, Province size setting, Provinces (ProvinceMap) (+1 more)

### Community 70 - ".of"
Cohesion: 0.20
Nodes (3): FactionBanner, Layer, FactionBannerNbt

### Community 71 - ".wallSegments"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 72 - "DistantOcclusion"
Cohesion: 0.14
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 76 - "PlaceMusicClient"
Cohesion: 0.19
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 77 - "Recruiter"
Cohesion: 0.10
Nodes (4): WorldMapItem, Barks, Ear, Recruiter

### Community 78 - "Faction"
Cohesion: 0.15
Nodes (3): RecruitsBridge, Faction, CityGuards

### Community 79 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.28
Nodes (6): Boxes, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - "Peerage"
Cohesion: 0.14
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 84 - "Grade"
Cohesion: 0.16
Nodes (9): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Plan (+1 more)

### Community 86 - "Calibration"
Cohesion: 0.18
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.07
Nodes (22): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+14 more)

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - ".view"
Cohesion: 0.08
Nodes (14): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+6 more)

### Community 91 - ".view"
Cohesion: 0.19
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 92 - "SimpleChannel.java"
Cohesion: 0.09
Nodes (6): BackBanner, Builder, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 95 - ".generate"
Cohesion: 0.06
Nodes (15): Dojo, WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors, Homeland (+7 more)

### Community 100 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.14
Nodes (3): Point, Ground, GroundPlan

### Community 103 - "HorizontalStretch"
Cohesion: 0.07
Nodes (8): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, ProvinceMap, Terrain, Choice, HorizontalStretch, ProvinceMapTest

### Community 105 - ".player"
Cohesion: 0.16
Nodes (5): NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 107 - "DistantArmyRenderer"
Cohesion: 0.11
Nodes (5): DistantArmyRenderer, Figure, Hull, DistantShips, Far

### Community 109 - "Quests"
Cohesion: 0.11
Nodes (13): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Type, CLEAR (+5 more)

### Community 111 - "DistantArmyRenderer.java"
Cohesion: 0.07
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 114 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 115 - "PoiPrecomputer.java"
Cohesion: 0.06
Nodes (15): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoliticalMap (+7 more)

### Community 116 - "Countryside"
Cohesion: 0.14
Nodes (11): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+3 more)

### Community 118 - "Retinues"
Cohesion: 0.17
Nodes (4): GrandStrategy, View, Retinues, RetinuesTest

### Community 119 - "Clash"
Cohesion: 0.19
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 122 - "BattlePlan"
Cohesion: 0.04
Nodes (19): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, BattlePlan, Order, CHARGE (+11 more)

### Community 124 - "Land"
Cohesion: 0.27
Nodes (3): Land, Search, ShippingRoutes

### Community 125 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.17
Nodes (8): City wall, banners and wall guards, Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), Package layout (core, data, worldgen, sim, bridge), RafasConquest (PvE addon for Villager Recruits), Recruits faction registration (bridge package), Villager Recruits

### Community 128 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 129 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 130 - "Townsperson"
Cohesion: 0.12
Nodes (5): Walk, DistrictKey, Townsfolk, Townsperson, StreetWalksTest

### Community 132 - "MapContext"
Cohesion: 0.18
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 133 - "City"
Cohesion: 0.11
Nodes (6): City, Grid, SeatCities, CityIndex, SeatCitiesTest, World

### Community 136 - "BattleShips"
Cohesion: 0.23
Nodes (4): BattleShips, Helm, Ram, SableLink

### Community 138 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 143 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 144 - "Ranks (Soldier to Grand Marshal)"
Cohesion: 0.29
Nodes (8): Battles (prototype), Blocks of 20 soldiers, Gold church, Land grants (seven ways), Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter

### Community 146 - "Lands"
Cohesion: 0.08
Nodes (6): Goal, Lands, Stronghold, Claim, Politics, PoliticsData

### Community 148 - "Plan"
Cohesion: 0.08
Nodes (10): PlaceMusic, Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL (+2 more)

### Community 155 - ".build"
Cohesion: 0.15
Nodes (3): Frame, Harbour, CitySizesTest

### Community 156 - ".plan"
Cohesion: 0.11
Nodes (10): Centre, Demographics, Share, Faith, Bond, Kind, FRIEND, RIVAL (+2 more)

### Community 160 - "Roads"
Cohesion: 0.20
Nodes (5): Link, Place, Roads, Spur, RoadsTest

### Community 163 - "Lands"
Cohesion: 0.14
Nodes (4): Feud, Lord, Feuds, Front

### Community 164 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 167 - "Job"
Cohesion: 0.10
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 204 - "BattleType"
Cohesion: 0.17
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 206 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 217 - ".pack"
Cohesion: 0.29
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 219 - "RoadFeature"
Cohesion: 0.06
Nodes (9): CityVegetationMixin, RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin, RoadFeature, Profile, RoadNetwork, Stretch (+1 more)

### Community 223 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 226 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 232 - ".enlist"
Cohesion: 0.06
Nodes (7): WarPlan, BattleMap, GroundPlanTest, Land, Land, Land, Land

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 257 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.07
Nodes (5): CommandTableBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, MarkerBlock

### Community 262 - "MapLayer"
Cohesion: 0.19
Nodes (5): LegendEntry, MapLayer, Symbol, RoadLayer, UnitLayer

### Community 267 - "ShipKind"
Cohesion: 0.33
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 287 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.07
Nodes (20): WarHorn, Armoury, Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE (+12 more)

### Community 288 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 289 - "War"
Cohesion: 0.09
Nodes (7): Army, Event, Muster, CEREMONIAL, OFFENSIVE, PoliticsSync, War

### Community 295 - "FormationSnapshot"
Cohesion: 0.12
Nodes (12): FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN (+4 more)

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **335 isolated node(s):** `BOGFOLK`, `FROSTBORN`, `GRASSWALKERS`, `MEADOWFOLK`, `PINEKIN` (+330 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1019 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **183 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `net.minecraft.world.level.ChunkPos`, `.army`, `BattleScreen`, `net.minecraft.server.level.ServerPlayer`, `PoliticsTest`, `WarCouncil`, `rafasconquest.map.BaseMap`, `net.minecraft.world.item.ItemStack`, `PveCommands Registration`, `UnitMode`, `ConquestEvents.java`, `net.minecraft.world.phys.Vec3`, `.mapWater`, `org.jetbrains.annotations.Nullable`, `net.minecraft.core.BlockPos`, `net.minecraft.server.level.ServerLevel`, `.second`, `.passOnOrders`, `RafasConquest.java`, `Calibration`, `Doctrine`, `Phase`, `rafasconquest.battle.BattlePlan.Point`, `.enlist`, `ClientEvents`, `Trait`?**
  _High betweenness centrality (0.051) - this node is a cross-community bridge._
- **What connects `BOGFOLK`, `FROSTBORN`, `GRASSWALKERS` to the rest of the system?**
  _335 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.04678420171377918 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `MapContext`, `City`, `OrganizationScreen`, `CourtView`, `ConquestServer`, `.render`, `net.minecraft.client.gui.GuiGraphics`, `PoliticsTest`, `assertequals`, `.plan`, `WarCouncil`, `ClientPolitics`, `net.minecraft.world.item.ItemStack`, `rafasconquest.map.BaseMap`, `ConquestEvents.java`, `Realm`, `net.minecraft.server.level.ServerLevel`, `RafasConquest.java`, `MapLands`, `GrandStrategyScreen`, `RoadFeature`, `.generate`, `TitleStyle`, `Terrain`, `PoiPrecomputer.java`, `Countryside`, `.onLevelLoad`?**
  _High betweenness centrality (0.046) - this node is a cross-community bridge._
- **Should `RecruitsBattle` be split into smaller, more focused modules?**
  _Cohesion score 0.05163511187607573 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._