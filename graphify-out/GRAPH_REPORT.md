# Graph Report - RafasConquest  (2026-11-07)

## Corpus Check
- 432 files · ~334,781 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 115 file(s) not represented in the graph (top: .nbt 84, .toml 14, (none) 3)

## Summary
- 5867 nodes · 18678 edges · 304 communities (117 shown, 187 thin omitted)
- Extraction: 88% EXTRACTED · 12% INFERRED · 0% AMBIGUOUS · INFERRED: 2329 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `e3a2b3ec`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- GateBlockEntity
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsQuality.java
- .build
- Duty
- RecruitsBattle
- Organizations
- DoctrineTest
- Peerage
- BaseMap
- .render
- net.minecraft.world.level.block.Rotation
- net.minecraft.network.FriendlyByteBuf
- Battle Map Web Page & Site
- Person
- arraylist
- PoliticsTest
- RoadFeature
- CityManagerScreen
- .plan
- CityGridBuilder
- .shift
- WarCouncil
- rafasconquest.map.BaseMap
- CourtView
- SimpleChannel.java
- WorldSettings
- PveCommands Registration
- School
- CityPlan
- Land
- ConquestEvents.java
- FormationSnapshot
- RoadNetwork
- rafasconquest.faction.Faction
- RafasConquest.java
- Politics
- .send
- com.talhanation.recruits.entities.AbstractRecruitEntity
- Realm
- Personalities
- SeaLanes
- com.mojang.blaze3d.vertex.PoseStack
- .drawEmpire
- Countryside
- BattleSounds
- Build
- .readPlacements
- WarCamp
- net.minecraft.server.level.ServerLevel
- City
- Faction Map View
- .view
- SeaCalm
- net.minecraft.world.phys.Vec3
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
- RecruitsArrowCleanerMixin.java
- PlaceMusicClient
- .findGenerationPoint
- GateScreen
- rafasconquest.battle.FormationSnapshot
- Townsperson
- Ownership & Territory
- .choose
- Nation Rows
- Arm
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- .view
- Override
- PoliticsView
- Arm
- SableShips
- BoardScreen
- .generate
- ProvinceMap
- WaterSwell
- FactionInfoScreen
- rafasconquest.battle.BattlePlan.Point
- ConquestNetwork
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
- .mapWater
- Trait
- PoiPrecomputer.java
- Phase
- net.minecraft.client.gui.GuiGraphics
- .render
- GrandStrategyScreen
- PoliticalStyle
- BattlePlan
- Countryside
- RafasConquest (PvE addon for Villager Recruits)
- .onLevelLoad
- org.joml.Matrix4f
- CourtSet
- Recruiter
- MapContext
- TitleStyle
- WorldStretch
- BattleShips
- .passOnOrders
- .boundsAt
- .touches
- Role
- BlockGrid
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- Lands
- net.minecraft.resources.ResourceLocation
- CLAUDE.md
- ShipKind
- .plan
- ItemFlags.java
- Roads
- Lands
- .champion
- .get
- Small Cluster 170
- ClientEvents
- org.jetbrains.annotations.Nullable
- CityTerrainPiece
- Kind
- .pack
- ClientPolitics
- Politics.Front
- ClientScreens
- BattleType
- net.minecraft.core.Vec3i
- .block
- .at
- net.neoforged.bus.api.SubscribeEvent
- Task
- net.minecraft.world.item.DyeColor
- ConquestGameRules
- CityGridBuilder
- Call
- Race
- net.minecraft.core.BlockPos
- Kind
- .ships
- .walks
- Strategy
- net.minecraft.world.item.ItemStack
- Ramming
- NobleAnnex
- BlockPos
- .onLogout
- .bark
- BattlePlan.java
- .issue
- Piece
- BattleKeepInventory
- Distribution
- .inCity
- NobleDistrictTest
- ConquestConfig
- UnitMode
- ClientBarks
- GameCharacter
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

## Communities (304 total, 187 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.05
Nodes (7): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 1 - "GateBlockEntity"
Cohesion: 0.06
Nodes (12): Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING, GateGeometry (+4 more)

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.07
Nodes (8): CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece, PlaceholderPiece

### Community 4 - "CityTerrain"
Cohesion: 0.07
Nodes (20): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+12 more)

### Community 5 - "BattleScreen"
Cohesion: 0.06
Nodes (20): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+12 more)

### Community 6 - "DistantHorizonsQuality.java"
Cohesion: 0.08
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - ".build"
Cohesion: 0.12
Nodes (4): HarbourCut, Frame, Harbour, HarbourCutTest

### Community 8 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 10 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 12 - "Peerage"
Cohesion: 0.08
Nodes (12): Advancement, Boards, Entry, View, CityManager, Merit, Peer, Peerage (+4 more)

### Community 13 - "BaseMap"
Cohesion: 0.12
Nodes (6): ClientMapCache, MapPreview, BaseMap, Nation, BaseMapStore, Ready

### Community 14 - ".render"
Cohesion: 0.08
Nodes (7): CharacterScreen, Spot, ClientPeerage, Banner, Portraits, Honour, Honours

### Community 15 - "net.minecraft.world.level.block.Rotation"
Cohesion: 0.11
Nodes (6): Gold church, Quest board and quests, Recruitment posts and recruiter, StylePiece, GoldChurch, Layout

### Community 16 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.06
Nodes (8): BattleSnapshot, BattleSpot, FactionInfoReply, PeerageView, StatsView, UnitsRequest, UnitsState, Volleys

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.24
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 25 - ".plan"
Cohesion: 0.17
Nodes (6): Nation, Nations, Plan, Seats, NationsTest, World

### Community 26 - "CityGridBuilder"
Cohesion: 0.13
Nodes (7): PlacedWall, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.15
Nodes (6): Facts, Officer, Said, Talk, WarCouncil, WarCouncilPagesTest

### Community 29 - "rafasconquest.map.BaseMap"
Cohesion: 0.08
Nodes (6): Army, Goal, MusterBars, PoliticsSync, War, Wars

### Community 30 - "CourtView"
Cohesion: 0.08
Nodes (7): Retainer, Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 31 - "SimpleChannel.java"
Cohesion: 0.06
Nodes (12): BackBanner, RequestFactionInfo, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel (+4 more)

### Community 32 - "WorldSettings"
Cohesion: 0.09
Nodes (10): CapitalLayout, Land, Site, ChunkCoord, FarmLayout, OriginRoller, Roll, WorldSettings (+2 more)

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 36 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 37 - "ConquestEvents.java"
Cohesion: 0.04
Nodes (8): CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement, FarmStructure

### Community 38 - "FormationSnapshot"
Cohesion: 0.09
Nodes (13): FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN (+5 more)

### Community 39 - "RoadNetwork"
Cohesion: 0.18
Nodes (3): RoadNetwork, Stretch, RoadNetworkTest

### Community 40 - "rafasconquest.faction.Faction"
Cohesion: 0.09
Nodes (5): Officers, RecruitsFactions, FactionBanner, Layer, FactionBannerNbt

### Community 42 - "Politics"
Cohesion: 0.08
Nodes (6): Event, Muster, CEREMONIAL, OFFENSIVE, Politics, PoliticsData

### Community 43 - ".send"
Cohesion: 0.12
Nodes (7): Bark, BoardView, CityClose, GrandStrategyView, Place, SeaSwell, TalkView

### Community 44 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.18
Nodes (7): Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER, Side

### Community 45 - "Realm"
Cohesion: 0.11
Nodes (6): Organization, Kind, FRIEND, RIVAL, Realm, CharacterMarkers

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.15
Nodes (11): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+3 more)

### Community 48 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.12
Nodes (3): BackBannerLayer, GateRenderer, MouthLayer

### Community 50 - ".drawEmpire"
Cohesion: 0.27
Nodes (3): Hit, LabelLayer, LabelShape

### Community 51 - "Countryside"
Cohesion: 0.13
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 52 - "BattleSounds"
Cohesion: 0.19
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 53 - "Build"
Cohesion: 0.25
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 54 - ".readPlacements"
Cohesion: 0.27
Nodes (3): Offsets, Placement, PlacementOffsetsTest

### Community 56 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.04
Nodes (13): RecruitsBridge, ConquestSavedData, Faction, ResourceType, FOOD, Poi, BarracksPeople, CityBanners (+5 more)

### Community 57 - "City"
Cohesion: 0.12
Nodes (6): City, Grid, SeatCities, CityIndex, SeatCitiesTest, World

### Community 59 - ".view"
Cohesion: 0.15
Nodes (6): Offer, Recruitment, Option, Talk, View, DialogueTest

### Community 60 - "SeaCalm"
Cohesion: 0.19
Nodes (4): Sea, Depths, SeaCalm, SwellTest

### Community 61 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.12
Nodes (8): Card, Enlisted, Shape, LINE, SQUARE, V, WEDGE, Unit

### Community 63 - "TitleMapTest"
Cohesion: 0.12
Nodes (8): Tier, COUNTY, DUCHY, EMPIRE, KINGDOM, TitleMap, Land, TitleMapTest

### Community 67 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.07
Nodes (6): MarkerRenderer, Palette, WallTemplates, Placed, MarkerBlockEntity, MarkerSpec

### Community 71 - "CityLayout"
Cohesion: 0.08
Nodes (10): CityLayout, Side, EAST, NORTH, SOUTH, WEST, Site, SlopeTile (+2 more)

### Community 72 - "DistantOcclusion"
Cohesion: 0.15
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 73 - "Barracks"
Cohesion: 0.23
Nodes (3): Barracks, BarracksPiece, BarracksTest

### Community 75 - "RecruitsArrowCleanerMixin.java"
Cohesion: 0.17
Nodes (4): CityVegetationMixin, RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 76 - "PlaceMusicClient"
Cohesion: 0.19
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 79 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.28
Nodes (6): Boxes, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 81 - "Townsperson"
Cohesion: 0.12
Nodes (4): Walk, DistrictKey, Townsfolk, Townsperson

### Community 82 - "Ownership & Territory"
Cohesion: 0.18
Nodes (6): Candidate, OwnershipResolver, Claim, TerritoryMap, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.15
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 86 - "Arm"
Cohesion: 0.19
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.16
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.05
Nodes (27): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+19 more)

### Community 89 - ".view"
Cohesion: 0.05
Nodes (21): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+13 more)

### Community 90 - "Override"
Cohesion: 0.05
Nodes (7): Cell, Terrain, DialogueScreen, Drop, MarkerScreen, SettingsScreen, StatsScreen

### Community 92 - "Arm"
Cohesion: 0.12
Nodes (11): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, FieldRules, Rates, Side (+3 more)

### Community 95 - ".generate"
Cohesion: 0.06
Nodes (15): ProvinceNames, WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry, MapColors, Homeland (+7 more)

### Community 96 - "ProvinceMap"
Cohesion: 0.14
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 99 - "FactionInfoScreen"
Cohesion: 0.15
Nodes (3): FactionInfoScreen, Line, FactionInfoTest

### Community 100 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.08
Nodes (8): Point, Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 101 - "ConquestNetwork"
Cohesion: 0.09
Nodes (10): CityView, ConquestNetwork, FigureStrike, HornOrders, MarkerSet, RequestBaseMap, SetSetting, StatsRequest (+2 more)

### Community 103 - "HorizontalStretch"
Cohesion: 0.12
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 105 - "Realm"
Cohesion: 0.10
Nodes (5): Hook, Succession, Taking, RealmPlannerTest, World

### Community 107 - "DistantArmyRenderer"
Cohesion: 0.11
Nodes (3): DistantArmyRenderer, Figure, Hull

### Community 109 - "Quests"
Cohesion: 0.12
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 110 - "Kit"
Cohesion: 0.14
Nodes (13): Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD, HEAVY_INFANTRY (+5 more)

### Community 111 - "DistantArmyRenderer.java"
Cohesion: 0.07
Nodes (4): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), DistantDepth

### Community 114 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 115 - "PoiPrecomputer.java"
Cohesion: 0.06
Nodes (15): /pve debug citystats [samples], /pve debug cityterrain, /pve politics, /pve politics war <attacker> <defender> [generals], SeedHash, FactionType, BANDIT, CITY_STATE (+7 more)

### Community 116 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 117 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.18
Nodes (5): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer

### Community 122 - "BattlePlan"
Cohesion: 0.05
Nodes (14): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, WarPlan (+6 more)

### Community 124 - "Countryside"
Cohesion: 0.20
Nodes (4): Countryside, Plan, CountrysideTest, World

### Community 125 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 129 - "CourtSet"
Cohesion: 0.25
Nodes (3): CourtSet, Seat, Style

### Community 130 - "Recruiter"
Cohesion: 0.06
Nodes (4): ConquestContent, WorldMapItem, ConquestEntities, Recruiter

### Community 132 - "MapContext"
Cohesion: 0.17
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 136 - "BattleShips"
Cohesion: 0.21
Nodes (4): BattleShips, Helm, Ram, SableLink

### Community 137 - ".passOnOrders"
Cohesion: 0.08
Nodes (3): Figure, Landing, Live

### Community 138 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 140 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 143 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.33
Nodes (4): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender

### Community 146 - "Lands"
Cohesion: 0.11
Nodes (3): Lands, Stronghold, Claim

### Community 149 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.22
Nodes (3): UnitLayer, RecruiterRenderer, TownspersonRenderer

### Community 155 - "ShipKind"
Cohesion: 0.33
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 156 - ".plan"
Cohesion: 0.15
Nodes (5): Centre, Faith, Bond, RealmPlanner, Seat

### Community 160 - "Roads"
Cohesion: 0.12
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 163 - "Lands"
Cohesion: 0.10
Nodes (4): Feud, Lord, Feuds, Front

### Community 164 - ".champion"
Cohesion: 0.16
Nodes (5): Clash, Kind, DUEL, SKIRMISH, FeudDuel

### Community 167 - ".get"
Cohesion: 0.08
Nodes (13): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+5 more)

### Community 206 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 217 - ".pack"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 226 - "BattleType"
Cohesion: 0.25
Nodes (5): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE

### Community 237 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 240 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.18
Nodes (5): DistantShips, Far, BattleKind, Part, ShipHulls

### Community 249 - "CityGridBuilder"
Cohesion: 0.15
Nodes (3): CityGridBuilder, Ground, Platform

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Race"
Cohesion: 0.17
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 257 - "net.minecraft.core.BlockPos"
Cohesion: 0.06
Nodes (8): Saved, CommandTableBlock, GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, Builder, MarkerBlock

### Community 259 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 267 - ".walks"
Cohesion: 0.09
Nodes (3): CityLayoutTest, CitySizesTest, StreetWalksTest

### Community 277 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 287 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.20
Nodes (3): Armoury, Outfit, Style

### Community 288 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 291 - "BlockPos"
Cohesion: 0.21
Nodes (3): GateSet, GateView, MarkerView

### Community 300 - "BattlePlan.java"
Cohesion: 0.04
Nodes (49): Ambushes from woods, Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Character page, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings) (+41 more)

### Community 303 - "Piece"
Cohesion: 0.13
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 309 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 315 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **339 isolated node(s):** `CLOSED`, `OPENING`, `OPEN`, `CLOSING`, `TOWN` (+334 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1028 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **187 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `org.jetbrains.annotations.Nullable`, `net.minecraft.world.level.ChunkPos`, `.army`, `BattleScreen`, `.passOnOrders`, `Peerage`, `.onRenderLiving`, `WarCouncil`, `net.minecraft.world.item.ItemStack`, `SimpleChannel.java`, `PveCommands Registration`, `.champion`, `ConquestEvents.java`, `rafasconquest.faction.Faction`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `org.jetbrains.annotations.Nullable`, `.inCity`, `net.minecraft.server.level.ServerLevel`, `WarCamp`, `UnitMode`, `net.minecraft.world.phys.Vec3`, `GameCharacter`, `RafasConquest.java`, `net.minecraft.world.entity.Entity`, `.block`, `.mapWater`, `Phase`?**
  _High betweenness centrality (0.047) - this node is a cross-community bridge._
- **What connects `CLOSED`, `OPENING`, `OPEN` to the rest of the system?**
  _339 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.05268168835290384 - nodes in this community are weakly interconnected._
- **Why does `Realm` connect `Realm` to `Realm`, `Organizations`, `MapLands`, `BaseMap`, `Person`, `arraylist`, `Lands`, `org.jetbrains.annotations.Nullable`, `assertequals`, `.plan`, `CourtView`, `.generate`?**
  _High betweenness centrality (0.045) - this node is a cross-community bridge._
- **Should `GateBlockEntity` be split into smaller, more focused modules?**
  _Cohesion score 0.06428571428571428 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._