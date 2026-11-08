# Graph Report - RafasConquest  (2026-11-08)

## Corpus Check
- 437 files · ~335,170 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 115 file(s) not represented in the graph (top: .nbt 84, .toml 14, (none) 3)

## Summary
- 5879 nodes · 18699 edges · 311 communities (129 shown, 182 thin omitted)
- Extraction: 88% EXTRACTED · 12% INFERRED · 0% AMBIGUOUS · INFERRED: 2329 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `5570861a`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- net.minecraft.core.BlockPos
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsQuality.java
- .findGenerationPoint
- Duty
- RecruitsBattle
- Organizations
- War
- Peerage
- BaseMap
- .render
- Layout
- net.minecraft.network.FriendlyByteBuf
- Battle Map Web Page & Site
- Person
- arraylist
- PveCommands.java
- PoliticsTest
- net.minecraft.world.level.Level
- CityManagerScreen
- .plan
- CityGridBuilder
- .shift
- WarCouncil
- rafasconquest.map.BaseMap
- CourtView
- .send
- WorldSettings
- PveCommands Registration
- School
- CityPlan
- Land
- ConquestEvents.java
- FormationSnapshot
- GateBlockEntity
- rafasconquest.faction.Faction
- PoliticsData
- .send
- ConquestEvents.java
- Faction
- Personalities
- SeaLanes
- com.mojang.blaze3d.vertex.PoseStack
- .drawEmpire
- Countryside
- BattleSounds
- Build
- ConquestServer
- net.neoforged.neoforge.network.handling.IPayloadContext
- ConquestSavedData
- City
- Faction Map View
- .view
- SeaCalm
- Shape
- TitleMapTest
- .measure
- StyleLibrary
- OrganizationScreen
- net.minecraft.nbt.CompoundTag
- RafasConquest.java
- .render
- CityLayout
- DistantOcclusion
- Barracks
- MapLands
- RoadFeature
- PlaceMusicClient
- .findGenerationPoint
- GateScreen
- rafasconquest.battle.FormationSnapshot
- Townsperson
- Ownership & Territory
- .choose
- net.minecraft.server.level.ServerPlayer
- Nation Rows
- Arm
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- .view
- Override
- ConquestNetwork
- Arm
- SableShips
- BoardScreen
- .generate
- .postChunk
- WaterSwell
- FactionInfoScreen
- rafasconquest.battle.BattlePlan.Point
- FigureStrike
- CityStyles
- HorizontalStretch
- Realm
- ClientModEvents.java
- DistantArmyRenderer
- Swell
- Quests
- Kit
- DistantArmyRenderer.java
- DistantGeneration
- .world
- Trait
- PoiPrecomputer.java
- Phase
- net.minecraft.client.gui.GuiGraphics
- .wallSegments
- GrandStrategyScreen
- PoliticalStyle
- BattlePlan
- CourtView.java
- RafasConquest (PvE addon for Villager Recruits)
- .onLevelLoad
- org.joml.Matrix4f
- net.minecraft.world.item.DyeColor
- RafasConquest.java
- MapContext
- Ranks
- DayClockTest
- BattleShips
- .passOnOrders
- .boundsAt
- .touches
- Role
- BlockGrid
- .of
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- Lands
- .player
- net.minecraft.resources.ResourceLocation
- CLAUDE.md
- rafasconquest_world SavedData
- ShipKind
- .plan
- ItemFlags.java
- Roads
- Lands
- Clash
- .get
- Small Cluster 170
- Ranks (Soldier to Grand Marshal)
- UnitInfo
- org.jetbrains.annotations.Nullable
- CityTerrainPiece
- Kind
- .pack
- GatePlanksBlock.java
- .accept
- Characters (holders, skills, prowess, traits)
- ClientPolitics
- Politics.Front
- ClientScreens
- Wars (daily nation decisions)
- net.minecraft.core.Vec3i
- .block
- .sendBattleSpot
- net.neoforged.bus.api.SubscribeEvent
- TalkChoice
- Task
- HornOrders
- ConquestGameRules
- CityGridBuilder
- TeleportTo
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- Call
- Homeland
- net.minecraft.core.BlockPos
- Kind
- .ships
- .walks
- Strategy
- net.minecraft.world.item.ItemStack
- Ramming
- .hash
- BlockPos
- .onLogout
- Cities (grid, plots, vanilla village buildings)
- .issue
- Piece
- WarPlan
- .inCity
- UnitMode
- ClientBarks
- WarHorn
- BackBanners

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 256 edges
2. `FieldBattle` - 149 edges
3. `FieldBlock` - 139 edges
4. `ConquestNetwork` - 114 edges
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

## Communities (311 total, 182 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.05
Nodes (8): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock, Rates

### Community 1 - "net.minecraft.core.BlockPos"
Cohesion: 0.08
Nodes (6): Saved, WarCamp, GateGeometry, CityGates, Done, GateGeometryTest

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.08
Nodes (5): CityRoadPiece, CountryPiece, FarmPiece, HarbourPiece, PlaceholderPiece

### Community 3 - ".army"
Cohesion: 0.07
Nodes (5): Point, DoctrineTest, FieldBattleTest, GroundPlanTest, Land

### Community 4 - "CityTerrain"
Cohesion: 0.05
Nodes (22): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+14 more)

### Community 5 - "BattleScreen"
Cohesion: 0.06
Nodes (19): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+11 more)

### Community 6 - "DistantHorizonsQuality.java"
Cohesion: 0.08
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - ".findGenerationPoint"
Cohesion: 0.18
Nodes (3): SlopeTile, Frame, Harbour

### Community 8 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 9 - "RecruitsBattle"
Cohesion: 0.03
Nodes (15): Outcome, Card, Enlisted, GeneralTask, Officers, RecruitsBattle, Role, BEARER (+7 more)

### Community 10 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 11 - "War"
Cohesion: 0.10
Nodes (3): Army, Goal, War

### Community 12 - "Peerage"
Cohesion: 0.10
Nodes (10): Boards, Entry, View, Merit, Peer, Peerage, Taken, PeerageSync (+2 more)

### Community 13 - "BaseMap"
Cohesion: 0.13
Nodes (6): Junction, MapPreview, BaseMap, Nation, BaseMapStore, Ready

### Community 14 - ".render"
Cohesion: 0.07
Nodes (14): CharacterScreen, Spot, ClientPeerage, Banner, Portraits, Honour, Honours, Stat (+6 more)

### Community 16 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.09
Nodes (5): BattleSnapshot, SetSetting, StatsView, Volleys, WorldStretch

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.23
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "arraylist"
Cohesion: 0.04
Nodes (5): Character page, Court 3D scene (CourtView), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), InventoryStatsButton

### Community 20 - "PveCommands.java"
Cohesion: 0.09
Nodes (30): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+22 more)

### Community 23 - "net.minecraft.world.level.Level"
Cohesion: 0.11
Nodes (4): GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, WorldMapItem

### Community 25 - ".plan"
Cohesion: 0.14
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "CityGridBuilder"
Cohesion: 0.20
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 27 - ".shift"
Cohesion: 0.20
Nodes (5): BattleSite, Part, Water, BattleSite.Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.16
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 29 - "rafasconquest.map.BaseMap"
Cohesion: 0.13
Nodes (4): FeudDuel, MusterBars, PoliticsSync, Wars

### Community 30 - "CourtView"
Cohesion: 0.09
Nodes (4): Camera, CourtView, Figure, NationScreen

### Community 31 - ".send"
Cohesion: 0.11
Nodes (7): StatsRequest, Msg, SimpleChannel, Type, CLEAR, DELIVER, SCOUT

### Community 32 - "WorldSettings"
Cohesion: 0.18
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.15
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 36 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 37 - "ConquestEvents.java"
Cohesion: 0.04
Nodes (11): CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure (+3 more)

### Community 38 - "FormationSnapshot"
Cohesion: 0.09
Nodes (13): FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN (+5 more)

### Community 39 - "GateBlockEntity"
Cohesion: 0.09
Nodes (9): GateDoors, Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING (+1 more)

### Community 43 - ".send"
Cohesion: 0.15
Nodes (3): BoardView, GrandStrategyView, TalkView

### Community 45 - "Faction"
Cohesion: 0.11
Nodes (3): RecruitsBridge, Faction, FactionInfoTest

### Community 46 - "Personalities"
Cohesion: 0.06
Nodes (29): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+21 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.10
Nodes (4): BackBannerLayer, GateRenderer, MarkerRenderer, MouthLayer

### Community 50 - ".drawEmpire"
Cohesion: 0.23
Nodes (3): Hit, LabelLayer, LabelShape

### Community 51 - "Countryside"
Cohesion: 0.11
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 52 - "BattleSounds"
Cohesion: 0.20
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 53 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 54 - "ConquestServer"
Cohesion: 0.12
Nodes (4): CityBanners, ConquestServer, StructureLink, PoiPrecomputer

### Community 55 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.13
Nodes (6): BackBanner, Builder, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 56 - "ConquestSavedData"
Cohesion: 0.08
Nodes (4): ConquestSavedData, Poi, EconomyTick, ConquestSavedDataTest

### Community 57 - "City"
Cohesion: 0.09
Nodes (8): Countryside, Plan, Grid, SeatCities, CountrysideTest, World, SeatCitiesTest, World

### Community 59 - ".view"
Cohesion: 0.18
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 60 - "SeaCalm"
Cohesion: 0.16
Nodes (4): Sea, Depths, SeaCalm, SwellTest

### Community 61 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 65 - "StyleLibrary"
Cohesion: 0.12
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 67 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.08
Nodes (4): CharacterMarkers, Placed, MarkerBlockEntity, MarkerSpec

### Community 71 - "CityLayout"
Cohesion: 0.09
Nodes (6): CityLayout, Site, ChunkCoord, FarmLayout, WorldSettings, CityLayoutTest

### Community 72 - "DistantOcclusion"
Cohesion: 0.15
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 73 - "Barracks"
Cohesion: 0.20
Nodes (3): Barracks, BarracksPiece, BarracksTest

### Community 75 - "RoadFeature"
Cohesion: 0.06
Nodes (9): CityVegetationMixin, RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin, RoadFeature, Profile, RoadNetwork, Stretch (+1 more)

### Community 76 - "PlaceMusicClient"
Cohesion: 0.19
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 79 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.28
Nodes (6): Boxes, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 81 - "Townsperson"
Cohesion: 0.07
Nodes (5): Walk, Barks, Ear, Recruiter, Townsperson

### Community 82 - "Ownership & Territory"
Cohesion: 0.20
Nodes (5): Candidate, OwnershipResolver, Claim, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.15
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 84 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.13
Nodes (5): CityManager, GrandStrategy, View, PlayerStats, View

### Community 86 - "Arm"
Cohesion: 0.19
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.16
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 89 - ".view"
Cohesion: 0.11
Nodes (11): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+3 more)

### Community 90 - "Override"
Cohesion: 0.05
Nodes (7): Cell, Terrain, DialogueScreen, Drop, MarkerScreen, SettingsScreen, StatsScreen

### Community 91 - "ConquestNetwork"
Cohesion: 0.09
Nodes (11): Bark, BaseMapPart, CityClose, ConquestNetwork, FactionInfoReply, PeerageView, Place, PoliticsView (+3 more)

### Community 92 - "Arm"
Cohesion: 0.11
Nodes (5): FieldRules, Side, FLANK, FRONT, REAR

### Community 95 - ".generate"
Cohesion: 0.08
Nodes (6): WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors

### Community 100 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.12
Nodes (5): Ground, GroundPlan, Land, Land, Land

### Community 103 - "HorizontalStretch"
Cohesion: 0.06
Nodes (8): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, ProvinceMap, Terrain, Choice, HorizontalStretch, ProvinceMapTest

### Community 105 - "Realm"
Cohesion: 0.10
Nodes (7): Hook, Kind, FRIEND, RIVAL, Realm, Succession, Taking

### Community 107 - "DistantArmyRenderer"
Cohesion: 0.08
Nodes (5): DistantArmyRenderer, Figure, Hull, DistantShips, Far

### Community 109 - "Quests"
Cohesion: 0.12
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 110 - "Kit"
Cohesion: 0.14
Nodes (13): Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD, HEAVY_INFANTRY (+5 more)

### Community 111 - "DistantArmyRenderer.java"
Cohesion: 0.07
Nodes (4): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), DistantDepth

### Community 113 - ".world"
Cohesion: 0.15
Nodes (3): Retainer, RealmPlannerTest, World

### Community 114 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 115 - "PoiPrecomputer.java"
Cohesion: 0.08
Nodes (13): SeedHash, TerritoryMap, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD (+5 more)

### Community 116 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 117 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.14
Nodes (6): BaseMapLayer, LegendEntry, MapLayer, Symbol, RoadLayer, TerritoryLayer

### Community 118 - ".wallSegments"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 122 - "BattlePlan"
Cohesion: 0.05
Nodes (18): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, BattlePlan, Order, CHARGE (+10 more)

### Community 125 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 129 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.17
Nodes (6): CourtSet, Seat, Style, BattleKind, Part, ShipHulls

### Community 130 - "RafasConquest.java"
Cohesion: 0.07
Nodes (4): ConquestContent, PoliticalMap, ConquestEntities, RafasConquest

### Community 132 - "MapContext"
Cohesion: 0.17
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 133 - "Ranks"
Cohesion: 0.12
Nodes (3): Ranks, Advancement, PeerageTest

### Community 134 - "DayClockTest"
Cohesion: 0.25
Nodes (3): DayClock, Step, DayClockTest

### Community 136 - "BattleShips"
Cohesion: 0.19
Nodes (4): BattleShips, Helm, Ram, SableLink

### Community 137 - ".passOnOrders"
Cohesion: 0.07
Nodes (3): Figure, Landing, Live

### Community 138 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 140 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 142 - ".of"
Cohesion: 0.22
Nodes (3): FactionBanner, Layer, FactionBannerTest

### Community 143 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.15
Nodes (9): Battle phases and days, Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Envelopment, The general and guard, Battle roles (frontline, bait, support, flanker, kill squad), Victory, pursuit and surrender, War camps (+1 more)

### Community 146 - "Lands"
Cohesion: 0.08
Nodes (7): Lands, Stronghold, Claim, Muster, CEREMONIAL, OFFENSIVE, Politics

### Community 148 - ".player"
Cohesion: 0.22
Nodes (5): Names, NobleTitle, COUNT, KNIGHT, NONE

### Community 149 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.22
Nodes (3): UnitLayer, RecruiterRenderer, TownspersonRenderer

### Community 155 - "ShipKind"
Cohesion: 0.33
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 156 - ".plan"
Cohesion: 0.13
Nodes (7): Centre, Demographics, Share, Faith, Bond, RealmPlanner, Seat

### Community 160 - "Roads"
Cohesion: 0.09
Nodes (15): Grade, CITY, DOJO, HIGH, SEA, TOWN, Link, Place (+7 more)

### Community 163 - "Lands"
Cohesion: 0.13
Nodes (4): Feud, Lord, Feuds, Front

### Community 164 - "Clash"
Cohesion: 0.28
Nodes (4): Clash, Kind, DUEL, SKIRMISH

### Community 167 - ".get"
Cohesion: 0.10
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 174 - "Ranks (Soldier to Grand Marshal)"
Cohesion: 0.22
Nodes (9): Enlisted players, Gold church, Jobs (cavalry, footman, archer, shieldman, heavy infantry), Land grants (seven ways), Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter (+1 more)

### Community 178 - "UnitInfo"
Cohesion: 0.22
Nodes (3): UnitInfo, UnitsRequest, UnitsState

### Community 206 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 217 - ".pack"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 221 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.29
Nodes (7): Characters (holders, skills, prowess, traits), Cultures (seven homelands), Epic Knights optional armour (Armoury), Faiths and cults, Martial titles (Sword Saint, Sword King, Sword God), Organizations, Sword sanctums

### Community 226 - "Wars (daily nation decisions)"
Cohesion: 0.33
Nodes (5): Battles (prototype), Blocks of 20 soldiers, Heralds (daily news), Mustering (offensive and ceremonial), Wars (daily nation decisions)

### Community 234 - "net.neoforged.bus.api.SubscribeEvent"
Cohesion: 0.05
Nodes (3): BattleKeepInventory, ClientEvents, ConquestEvents

### Community 237 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 249 - "CityGridBuilder"
Cohesion: 0.11
Nodes (4): Houses, CityGridBuilder, PlacedWall, Platform

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Homeland"
Cohesion: 0.09
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 257 - "net.minecraft.core.BlockPos"
Cohesion: 0.09
Nodes (3): CommandTableBlock, GateBlock, MarkerBlock

### Community 259 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 267 - ".walks"
Cohesion: 0.11
Nodes (4): DistrictKey, Townsfolk, CitySizesTest, StreetWalksTest

### Community 277 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 287 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.18
Nodes (4): Armoury, Outfit, Style, RankKits

### Community 288 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 289 - ".hash"
Cohesion: 0.13
Nodes (5): NobleAnnex, OriginRoller, Roll, ProvinceNames, NobleAnnexTest

### Community 291 - "BlockPos"
Cohesion: 0.12
Nodes (5): BoardAction, GateSet, GateView, MarkerSet, MarkerView

### Community 300 - "Cities (grid, plots, vanilla village buildings)"
Cohesion: 0.07
Nodes (22): Ambushes from woods, Base map generated from seed, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Command Table block, Countryside (hamlets, villages, watchtowers, castles), Demographics (population by culture and race), Faction Info screen (+14 more)

### Community 303 - "Piece"
Cohesion: 0.12
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 309 - "WarPlan"
Cohesion: 0.18
Nodes (7): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, WarPlan, GameCharacter

### Community 315 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

## Ambiguous Edges - Review These
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to

## Knowledge Gaps
- **339 isolated node(s):** `CLOSED`, `OPENING`, `OPEN`, `CLOSING`, `MILITARY` (+334 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1034 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **182 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `org.jetbrains.annotations.Nullable`, `net.minecraft.world.item.DyeColor`, `net.minecraft.world.level.ChunkPos`, `.army`, `net.minecraft.core.BlockPos`, `BattleScreen`, `.passOnOrders`, `.shift`, `WarCouncil`, `rafasconquest.map.BaseMap`, `net.minecraft.world.item.ItemStack`, `PveCommands Registration`, `ConquestEvents.java`, `rafasconquest.faction.Faction`, `org.jetbrains.annotations.Nullable`, `.inCity`, `UnitMode`, `Shape`, `RafasConquest.java`, `net.minecraft.server.level.ServerPlayer`, `Arm`, `.generate`, `.block`, `net.neoforged.bus.api.SubscribeEvent`, `Trait`, `Phase`?**
  _High betweenness centrality (0.069) - this node is a cross-community bridge._
- **What connects `CLOSED`, `OPENING`, `OPEN` to the rest of the system?**
  _339 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.05244231345519928 - nodes in this community are weakly interconnected._
- **Why does `BaseMap` connect `BaseMap` to `Homeland`, `RafasConquest.java`, `MapContext`, `.render`, `arraylist`, `assertequals`, `.plan`, `WarCouncil`, `CourtView`, `net.minecraft.world.item.ItemStack`, `Roads`, `rafasconquest.map.BaseMap`, `School`, `ConquestEvents.java`, `Countryside`, `ConquestServer`, `City`, `TitleMapTest`, `OrganizationScreen`, `RafasConquest.java`, `MapLands`, `RoadFeature`, `Override`, `ClientPolitics`, `.generate`, `ClientScreens`, `.postChunk`, `Realm`, `Kit`, `PoiPrecomputer.java`, `GrandStrategyScreen`, `PoliticalStyle`, `.onLevelLoad`?**
  _High betweenness centrality (0.043) - this node is a cross-community bridge._
- **Should `net.minecraft.core.BlockPos` be split into smaller, more focused modules?**
  _Cohesion score 0.08055152394775036 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._