# Graph Report - RafasConquest  (2026-11-10)

## Corpus Check
- 447 files · ~351,970 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 115 file(s) not represented in the graph (top: .nbt 84, .toml 14, (none) 3)

## Summary
- 6095 nodes · 19567 edges · 324 communities (125 shown, 199 thin omitted)
- Extraction: 88% EXTRACTED · 12% INFERRED · 0% AMBIGUOUS · INFERRED: 2427 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `29924d86`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FieldBlock
- WarCamp.java
- net.minecraft.world.level.levelgen.structure.BoundingBox
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsArmies.java
- net.minecraft.server.MinecraftServer
- SiegeWorks
- .passOnOrders
- Recruiter
- CityGridBuilder.java
- Roads
- BaseMap
- ConquestNetwork
- PoiPrecomputer.java
- BattlePlan
- Battle Map Web Page & Site
- Person
- net.minecraft.client.gui.screens.Screen
- PveCommands.java
- assertequals
- HorizontalStretch
- CityManagerScreen
- .plan
- CityLayout
- .shift
- WarCouncil
- Quests
- .say
- War
- CapitalLayout
- PveCommands Registration
- School
- CityPlan
- Land
- Override
- FormationSnapshot
- net.minecraft.core.Direction
- rafasconquest.faction.Faction
- .block
- net.minecraft.core.Vec3i
- ConquestSavedData
- net.minecraft.world.phys.Vec3
- Personalities
- SeaLanes
- CityVariant
- .drawEmpire
- .roadStart
- BattleSounds
- Centre
- Homeland
- Row
- Peerage
- SeatCities
- Faction Map View
- .of
- RecruitsBattle
- net.minecraft.world.level.block.state.BlockState
- TitleMapTest
- .measure
- StyleLibrary
- .render
- .view
- RafasConquest.java
- .render
- .build
- DistantOcclusion
- CityGridBuilder
- MapLands
- net.minecraft.server.level.ServerPlayer
- PlaceMusicClient
- .stat
- .onLevelLoad
- net.neoforged.neoforge.network.handling.IPayloadContext
- SeaCalm
- Ownership & Territory
- .choose
- .player
- PoliticsTest
- Calibration
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- Arm
- DialogueScreen
- .send
- OrganizationScreen
- net.minecraft.world.item.ItemStack
- .postChunk
- .generate
- .postChunk
- WaterSwell
- FactionInfoScreen
- ClientScreens
- .world
- CityStyles
- net.minecraft.nbt.CompoundTag
- Realm
- com.mojang.blaze3d.vertex.PoseStack
- DistantArmyRenderer
- Override
- RoadFeature
- com.talhanation.recruits.entities.AbstractRecruitEntity
- PoliticalStyle
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- MarkerScreen
- .hash
- org.jetbrains.annotations.Nullable
- GateBlockEntity
- net.minecraft.client.gui.GuiGraphics
- CityLayout
- GrandStrategyScreen
- Terrain
- ClientPolitics
- UnitOrder
- Settlement
- .fire
- RoadNetwork
- net.minecraft.world.item.DyeColor
- BattleShips
- MapContext
- RafasConquest (PvE addon for Villager Recruits)
- Role
- .readPlacements
- HarbourCut
- .boundsAt
- DayClockTest
- StatsScreen
- GroundPlan.java
- net.minecraft.world.level.block.Rotation
- net.minecraft.resources.ResourceLocation
- Lands
- Rank
- Townsperson
- CLAUDE.md
- Ranks
- Politics
- .plan
- CourtView
- ConquestContent.java
- Front
- SableLink
- City
- Small Cluster 170
- Countryside
- .inCity
- Faction
- CityTerrainPiece
- BoardScreen
- .pack
- Kit
- com.talhanation.recruits.entities.AbstractRecruitEntity
- rafasconquest.battle.BattlePlan.Point
- CityPlacement
- DistantArmyRenderer.java
- ConquestEntities.java
- .touches
- BackBanners
- net.neoforged.bus.api.SubscribeEvent
- Organizations
- UnitInfo
- .bark
- ConquestGameRules
- .issue
- Trait
- .view
- Call
- Ramming
- Duty
- Kind
- net.minecraft.network.FriendlyByteBuf
- ItemFlags.java
- DistantGeneration
- .nearest
- ConquestConfig
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- Kind
- CourtSet
- NobleDistrictTest
- BattleType
- arraylist
- Phase
- World map (CK3-style political map)
- .report
- UnitMode
- Shape
- GatePlanksBlock.java
- Piece
- CountryPiece
- ClientBarks
- BlockPos

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 265 edges
2. `FieldBlock` - 157 edges
3. `FieldBattle` - 154 edges
4. `ConquestNetwork` - 117 edges
5. `FieldArmy` - 110 edges
6. `Lands` - 89 edges
7. `Politics` - 78 edges
8. `BaseMap` - 73 edges
9. `BattleScreen` - 62 edges
10. `Realm` - 56 edges

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

## Communities (324 total, 199 thin omitted)

### Community 0 - "FieldBlock"
Cohesion: 0.04
Nodes (20): BlockGrid, FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock (+12 more)

### Community 1 - "WarCamp.java"
Cohesion: 0.08
Nodes (25): Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Characters (holders, skills, prowess, traits), Cultures (seven homelands), Enlisted players, Envelopment, Epic Knights optional armour (Armoury) (+17 more)

### Community 2 - "net.minecraft.world.level.levelgen.structure.BoundingBox"
Cohesion: 0.08
Nodes (8): BarracksPiece, CityRoadPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece, PlaceholderPiece

### Community 3 - ".army"
Cohesion: 0.07
Nodes (5): Point, DoctrineTest, FieldBattleTest, GroundPlanTest, Land

### Community 4 - "CityTerrain"
Cohesion: 0.07
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.10
Nodes (3): BattleScreen, Entry, Menu

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.05
Nodes (8): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), Distant Horizons (recommended mod), DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "net.minecraft.server.MinecraftServer"
Cohesion: 0.10
Nodes (8): Clash, Kind, DUEL, SKIRMISH, FeudDuel, MusterBars, PoliticsSync, Wars

### Community 8 - "SiegeWorks"
Cohesion: 0.12
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 12 - "Roads"
Cohesion: 0.12
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 13 - "BaseMap"
Cohesion: 0.17
Nodes (4): BaseMap, Nation, BaseMapStore, Ready

### Community 14 - "ConquestNetwork"
Cohesion: 0.06
Nodes (16): Bark, CameraAt, CityClose, CityView, ConquestNetwork, FactionInfoReply, FigureStrike, Place (+8 more)

### Community 15 - "PoiPrecomputer.java"
Cohesion: 0.07
Nodes (12): FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoliticalMap, FactionInfo (+4 more)

### Community 16 - "BattlePlan"
Cohesion: 0.06
Nodes (8): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, BattlePlanTest

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.23
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "net.minecraft.client.gui.screens.Screen"
Cohesion: 0.05
Nodes (7): Character page, Court 3D scene (CourtView), Demographics (population by culture and race), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), GateScreen, InventoryStatsButton

### Community 20 - "PveCommands.java"
Cohesion: 0.08
Nodes (28): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+20 more)

### Community 23 - "HorizontalStretch"
Cohesion: 0.12
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 25 - ".plan"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "CityLayout"
Cohesion: 0.08
Nodes (5): CityLayout, District, Site, SlopeTile, CityLayoutTest

### Community 27 - ".shift"
Cohesion: 0.20
Nodes (5): BattleSite, Part, Water, BattleSite.Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.16
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 29 - "Quests"
Cohesion: 0.10
Nodes (13): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Type, CLEAR (+5 more)

### Community 32 - "CapitalLayout"
Cohesion: 0.12
Nodes (8): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, WorldSettings, CapitalRelocationTest, LayoutTest

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 36 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 37 - "Override"
Cohesion: 0.24
Nodes (3): Land, Land, Land

### Community 38 - "FormationSnapshot"
Cohesion: 0.09
Nodes (13): FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN (+5 more)

### Community 39 - "net.minecraft.core.Direction"
Cohesion: 0.11
Nodes (7): Land, Result, SiegeSurvey, Wall, CityGates, Done, SiegeSurveyTest

### Community 42 - ".block"
Cohesion: 0.06
Nodes (8): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, WarPlan, GameCharacter, BattleMap

### Community 44 - "ConquestSavedData"
Cohesion: 0.07
Nodes (6): ConquestSavedData, MapPreview, Poi, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 45 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.16
Nodes (4): Camera, Card, Enlisted, Unit

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "CityVariant"
Cohesion: 0.15
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 50 - ".drawEmpire"
Cohesion: 0.21
Nodes (4): BaseMapLayer, Hit, LabelLayer, MapView

### Community 51 - ".roadStart"
Cohesion: 0.10
Nodes (4): DistrictKey, Townsfolk, CitySizesTest, StreetWalksTest

### Community 52 - "BattleSounds"
Cohesion: 0.20
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 53 - "Centre"
Cohesion: 0.15
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 54 - "Homeland"
Cohesion: 0.18
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 56 - "Peerage"
Cohesion: 0.08
Nodes (11): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+3 more)

### Community 57 - "SeatCities"
Cohesion: 0.16
Nodes (4): Grid, SeatCities, SeatCitiesTest, World

### Community 59 - ".of"
Cohesion: 0.23
Nodes (3): FactionBanner, Layer, FactionBannerNbt

### Community 60 - "RecruitsBattle"
Cohesion: 0.05
Nodes (3): Figure, Officers, RecruitsBattle

### Community 61 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (8): Saved, CommandTableBlock, GateBlock, GateBlockEntity, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, MarkerBlock

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 66 - ".render"
Cohesion: 0.11
Nodes (5): CharacterScreen, Retainer, Spot, Banner, Portraits

### Community 67 - ".view"
Cohesion: 0.20
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 69 - "RafasConquest.java"
Cohesion: 0.05
Nodes (3): Battle map developer web page, Morale, rout and rally, RafasConquest

### Community 72 - "DistantOcclusion"
Cohesion: 0.12
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 73 - "CityGridBuilder"
Cohesion: 0.13
Nodes (3): CityGridBuilder, Ground, Platform

### Community 75 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.06
Nodes (9): CameraView, ChunkMapAccess, ChunkMapCameraMixin, CityVegetationMixin, RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin, TrackedEntityCameraMixin (+1 more)

### Community 76 - "PlaceMusicClient"
Cohesion: 0.16
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 77 - ".stat"
Cohesion: 0.09
Nodes (10): ClientPeerage, Honour, Honours, Stat, DIPLOMACY, INTRIGUE, LEARNING, MARTIAL (+2 more)

### Community 79 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.14
Nodes (8): BackBanner, Builder, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel

### Community 81 - "SeaCalm"
Cohesion: 0.09
Nodes (6): Sea, Depths, SeaCalm, Ride, Swell, SwellTest

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.15
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 84 - ".player"
Cohesion: 0.13
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 86 - "Calibration"
Cohesion: 0.13
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.09
Nodes (16): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR, Strategy (+8 more)

### Community 89 - "Arm"
Cohesion: 0.12
Nodes (11): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, FieldRules, Rates, Side (+3 more)

### Community 93 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.21
Nodes (3): Armoury, Outfit, Style

### Community 94 - ".postChunk"
Cohesion: 0.11
Nodes (3): Barracks, BarracksPeople, BarracksTest

### Community 95 - ".generate"
Cohesion: 0.07
Nodes (13): WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry, Homeland, DESERT, JUNGLE (+5 more)

### Community 96 - ".postChunk"
Cohesion: 0.09
Nodes (5): CityBanners, CityGuards, ConquestServer, StructureLink, PoiPrecomputer

### Community 101 - ".world"
Cohesion: 0.16
Nodes (4): Demographics, Share, RealmPlannerTest, World

### Community 103 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.08
Nodes (5): Door, Palette, WallTemplates, MarkerBlockEntity, MarkerSpec

### Community 105 - "Realm"
Cohesion: 0.13
Nodes (4): Hook, Realm, Succession, Taking

### Community 106 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.09
Nodes (4): BackBannerLayer, GateRenderer, MarkerRenderer, MouthLayer

### Community 107 - "DistantArmyRenderer"
Cohesion: 0.12
Nodes (9): DistantArmyRenderer, Figure, Hull, Boxes, SoldierFigure, Stuff, CLOTH, METAL (+1 more)

### Community 114 - ".hash"
Cohesion: 0.11
Nodes (6): NobleAnnex, OriginRoller, Roll, ProvinceNames, SeedHash, NobleAnnexTest

### Community 115 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.04
Nodes (3): /council <topic>, /pve debug recruits, StreetWalks

### Community 116 - "GateBlockEntity"
Cohesion: 0.09
Nodes (8): Swing, CLOSED, CLOSING, OPEN, OPENING, GateGeometry, Hinge, GateGeometryTest

### Community 117 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.15
Nodes (6): LegendEntry, MapLayer, Symbol, RoadLayer, TerritoryLayer, UnitLayer

### Community 118 - "CityLayout"
Cohesion: 0.18
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 124 - "UnitOrder"
Cohesion: 0.12
Nodes (17): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+9 more)

### Community 125 - "Settlement"
Cohesion: 0.09
Nodes (10): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+2 more)

### Community 128 - "RoadNetwork"
Cohesion: 0.18
Nodes (3): RoadNetwork, Stretch, RoadNetworkTest

### Community 129 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.14
Nodes (6): DistantShips, Far, SableSeen, BattleKind, Part, ShipHulls

### Community 130 - "BattleShips"
Cohesion: 0.17
Nodes (5): BattleShips, Frame, Helm, Ram, SableLink

### Community 132 - "MapContext"
Cohesion: 0.19
Nodes (3): MapContext, PoiIconLayer, LabelShape

### Community 133 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (6): Recruits PvE Addon Design Doc (PDF), Embeddium (recommended mod), Package layout (core, data, worldgen, sim, bridge), RafasConquest (PvE addon for Villager Recruits), Recruits faction registration (bridge package), Villager Recruits

### Community 134 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 136 - ".readPlacements"
Cohesion: 0.27
Nodes (3): Offsets, Placement, PlacementOffsetsTest

### Community 138 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 139 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 141 - "GroundPlan.java"
Cohesion: 0.18
Nodes (5): Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), TickWeave experiment

### Community 142 - "net.minecraft.world.level.block.Rotation"
Cohesion: 0.16
Nodes (3): PlacedWall, GoldChurch, Layout

### Community 146 - "Lands"
Cohesion: 0.08
Nodes (4): Goal, Lands, Stronghold, Claim

### Community 148 - "Rank"
Cohesion: 0.12
Nodes (11): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+3 more)

### Community 152 - "Ranks"
Cohesion: 0.08
Nodes (13): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+5 more)

### Community 155 - "Politics"
Cohesion: 0.12
Nodes (6): Event, Muster, CEREMONIAL, OFFENSIVE, Politics, PoliticsData

### Community 156 - ".plan"
Cohesion: 0.12
Nodes (7): Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 157 - "CourtView"
Cohesion: 0.09
Nodes (5): Camera, CourtView, Figure, NationScreen, Organization

### Community 163 - "Front"
Cohesion: 0.12
Nodes (4): Feud, Lord, Feuds, Front

### Community 174 - "Countryside"
Cohesion: 0.19
Nodes (4): Countryside, Plan, CountrysideTest, World

### Community 217 - ".pack"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 218 - "Kit"
Cohesion: 0.15
Nodes (13): Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD, HEAVY_INFANTRY (+5 more)

### Community 219 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.18
Nodes (7): Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER, Side

### Community 221 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.04
Nodes (21): Ground, GroundPlan, Engine, LADDERS, RAMS, TOWERS, Gate, GateAction (+13 more)

### Community 224 - "DistantArmyRenderer.java"
Cohesion: 0.06
Nodes (4): DistantDepth, Arrow, ArrowVolleys, BattleSpotRing

### Community 234 - "net.neoforged.bus.api.SubscribeEvent"
Cohesion: 0.04
Nodes (4): BattleKeepInventory, ClientEvents, ClientModEvents, ConquestEvents

### Community 235 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 237 - "UnitInfo"
Cohesion: 0.22
Nodes (3): UnitInfo, UnitsRequest, UnitsState

### Community 252 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 254 - ".view"
Cohesion: 0.12
Nodes (6): GrandStrategy, View, PlayerStats, View, Retinues, RetinuesTest

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 257 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 259 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 263 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.05
Nodes (10): BaseMapPart, BattleSnapshot, BattleSpot, BoardView, GrandStrategyView, PeerageView, PoliticsView, TalkView (+2 more)

### Community 287 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.33
Nodes (4): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender

### Community 289 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 291 - "CourtSet"
Cohesion: 0.25
Nodes (3): CourtSet, Seat, Style

### Community 298 - "BattleType"
Cohesion: 0.15
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 300 - "arraylist"
Cohesion: 0.05
Nodes (14): Countryside (hamlets, villages, watchtowers, castles), Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, Nations by zone, Roads (high, city, town roads; bridges; signposts), Seat cities, Storm wall and war zone, CityDistrictStructure (+6 more)

### Community 301 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 303 - "World map (CK3-style political map)"
Cohesion: 0.33
Nodes (7): Base map generated from seed, Command Table block, Faction Info screen, World map layers, Province size setting, Provinces (ProvinceMap), World map (CK3-style political map)

### Community 306 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 308 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 312 - "Piece"
Cohesion: 0.13
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 324 - "CountryPiece"
Cohesion: 0.16
Nodes (7): Build, CASTLE, HAMLET, RUIN, TOWER, WELL, CountryPiece

### Community 336 - "BlockPos"
Cohesion: 0.16
Nodes (4): GateSet, GateView, MarkerSet, MarkerView

## Ambiguous Edges - Review These
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **352 isolated node(s):** `SOLDIER`, `LEADER`, `GENERAL`, `BEARER`, `GUARD` (+347 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1060 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **199 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `net.minecraft.world.item.DyeColor`, `net.minecraft.world.level.levelgen.structure.BoundingBox`, `.army`, `net.minecraft.server.MinecraftServer`, `SiegeWorks`, `.passOnOrders`, `.shift`, `WarCouncil`, `.say`, `PveCommands Registration`, `net.minecraft.core.Direction`, `rafasconquest.faction.Faction`, `.block`, `net.minecraft.world.phys.Vec3`, `Phase`, `.report`, `UnitMode`, `Shape`, `Peerage`, `RafasConquest.java`, `net.minecraft.server.level.ServerPlayer`, `Calibration`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `rafasconquest.battle.BattlePlan.Point`, `net.minecraft.world.item.ItemStack`, `net.neoforged.bus.api.SubscribeEvent`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `UnitOrder`, `org.jetbrains.annotations.Nullable`, `Trait`?**
  _High betweenness centrality (0.069) - this node is a cross-community bridge._
- **What connects `SOLDIER`, `LEADER`, `GENERAL` to the rest of the system?**
  _352 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `FieldBlock` be split into smaller, more focused modules?**
  _Cohesion score 0.039248467478703926 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `MapContext`, `net.minecraft.server.MinecraftServer`, `PoiPrecomputer.java`, `net.minecraft.client.gui.screens.Screen`, `assertequals`, `.plan`, `WarCouncil`, `CourtView`, `School`, `City`, `arraylist`, `ConquestSavedData`, `Centre`, `TitleMapTest`, `.render`, `RafasConquest.java`, `.onLevelLoad`, `OrganizationScreen`, `net.minecraft.world.item.ItemStack`, `.postChunk`, `.generate`, `.postChunk`, `ClientScreens`, `Realm`, `RoadFeature`, `PoliticalStyle`, `GrandStrategyScreen`, `Terrain`, `ClientPolitics`, `Settlement`?**
  _High betweenness centrality (0.031) - this node is a cross-community bridge._
- **Should `WarCamp.java` be split into smaller, more focused modules?**
  _Cohesion score 0.0812807881773399 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._