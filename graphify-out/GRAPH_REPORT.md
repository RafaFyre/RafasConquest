# Graph Report - RafasConquest  (2026-11-08)

## Corpus Check
- 443 files · ~349,728 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 115 file(s) not represented in the graph (top: .nbt 84, .toml 14, (none) 3)

## Summary
- 6058 nodes · 19428 edges · 323 communities (127 shown, 196 thin omitted)
- Extraction: 88% EXTRACTED · 12% INFERRED · 0% AMBIGUOUS · INFERRED: 2417 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `9c7a3d03`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- RoadFeature.java
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsArmies.java
- PoliticsData
- Work
- RecruitsBattle
- Townsperson
- rafasconquest.map.BaseMap
- Peerage
- BaseMap
- net.minecraft.network.FriendlyByteBuf
- Faction
- BattlePlan
- Battle Map Web Page & Site
- Person
- arraylist
- PveCommands.java
- PoliticsTest
- Centre
- CityManagerScreen
- .plan
- CityLayout
- .shift
- WarCouncil
- Quests
- .render
- .player
- CapitalLayout
- PveCommands Registration
- School
- CityPlan
- Land
- CityGridBuilder.java
- FormationSnapshot
- net.minecraft.core.Direction
- rafasconquest.faction.Faction
- net.minecraft.server.level.ServerLevel
- .saveFormation
- net.minecraft.core.BlockPos
- ConquestSavedData
- BlockGrid
- Personalities
- SeaLanes
- com.mojang.blaze3d.vertex.PoseStack
- .drawEmpire
- net.minecraft.world.entity.Entity
- BattleSounds
- Feud
- RecruitsArrowCleanerMixin.java
- Organizations
- City
- Faction Map View
- .of
- SeaCalm
- CityPlacement
- TitleMapTest
- .measure
- StyleLibrary
- CourtView
- .view
- RafasConquest.java
- .render
- .readPlacements
- DistantOcclusion
- Barracks
- MapLands
- RoadFeature
- PlaceMusicClient
- CityVariant
- net.neoforged.neoforge.network.handling.IPayloadContext
- rafasconquest.battle.FormationSnapshot
- .sample
- Ownership & Territory
- .choose
- .world
- Nation Rows
- Arm
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- Retinues
- DialogueScreen
- net.minecraft.server.level.ServerPlayer
- WallTemplates
- SableShips
- BoardScreen
- .generate
- .postChunk
- WaterSwell
- FactionInfoScreen
- ClientScreens
- Override
- CityStyles
- HorizontalStretch
- Realm
- ClientModEvents.java
- DistantArmyRenderer
- Strategy
- SimpleChannel
- net.minecraft.core.BlockPos
- DistantArmyRenderer.java
- PoliticalStyle
- MarkerScreen
- Trait
- PoiPrecomputer.java
- GateBlockEntity
- net.minecraft.client.gui.GuiGraphics
- CityLayout
- GrandStrategyScreen
- Terrain
- ClientPolitics
- BarracksPeople.java
- .touches
- .onLevelLoad
- Phase
- net.minecraft.world.item.DyeColor
- .build
- MapContext
- RafasConquest (PvE addon for Villager Recruits)
- Role
- BattleShips
- Duty
- .boundsAt
- Distribution
- StatsScreen
- GateScreen
- View
- BattleRole
- Lands
- .nearest
- net.minecraft.resources.ResourceLocation
- CLAUDE.md
- Job
- Build
- .plan
- .fire
- Roads
- Front
- Kit
- ShipKind
- Small Cluster 170
- Countryside
- Role
- Real soldiers only near players (live area, cap 300)
- CityTerrainPiece
- Kind
- .pack
- GatePlanksBlock.java
- .officers
- Engine
- UnitMode
- Override
- org.joml.Matrix4f
- OrganizationScreen
- .startAt
- net.neoforged.bus.api.SubscribeEvent
- Task
- ConquestGameRules
- CityGridBuilder
- PlayerStats
- .view
- Call
- Homeland
- net.minecraft.world.level.block.state.BlockState
- Kind
- dev.ryanhcode.sable.sublevel.ServerSubLevel
- .walks
- Piece
- net.minecraft.world.item.ItemStack
- Ramming
- Kind
- .issue
- .render
- SetSetting
- BattleSite.Water
- arraylist
- CityManager
- ItemFlags.java
- ArmouryTest
- Muster
- WarHorn
- BackBanners
- .inCity
- ProvinceNames
- BattleSnapshot
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- ClientBarks
- HornOrders
- SeatCitiesTest
- net.minecraft.world.level.biome.Biome
- .hash

## God Nodes (most connected - your core abstractions)
1. `RecruitsBattle` - 261 edges
2. `FieldBlock` - 188 edges
3. `FieldBattle` - 163 edges
4. `ConquestNetwork` - 113 edges
5. `FieldArmy` - 110 edges
6. `Lands` - 89 edges
7. `Politics` - 78 edges
8. `BaseMap` - 73 edges
9. `BattleScreen` - 60 edges
10. `City` - 56 edges

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

## Communities (323 total, 196 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.04
Nodes (15): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock, Rates (+7 more)

### Community 1 - "RoadFeature.java"
Cohesion: 0.05
Nodes (39): Base map generated from seed, Battles (prototype), Blocks of 20 soldiers, Character page, Characters (holders, skills, prowess, traits), City wall, banners and wall guards, Command Table block, Countryside (hamlets, villages, watchtowers, castles) (+31 more)

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.07
Nodes (9): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, NoblePathPiece (+1 more)

### Community 3 - ".army"
Cohesion: 0.07
Nodes (3): Point, DoctrineTest, FieldBattleTest

### Community 4 - "CityTerrain"
Cohesion: 0.06
Nodes (21): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+13 more)

### Community 5 - "BattleScreen"
Cohesion: 0.06
Nodes (20): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+12 more)

### Community 6 - "DistantHorizonsArmies.java"
Cohesion: 0.08
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 8 - "Work"
Cohesion: 0.12
Nodes (6): State, CARRIED, RAISING, UP, Work, SiegeWorks

### Community 9 - "RecruitsBattle"
Cohesion: 0.03
Nodes (14): Card, Enlisted, Figure, GeneralTask, Landing, Live, RecruitsBattle, Shape (+6 more)

### Community 10 - "Townsperson"
Cohesion: 0.06
Nodes (8): Walk, Barks, Ear, Recruiter, DistrictKey, Townsfolk, Townsperson, StreetWalksTest

### Community 11 - "rafasconquest.map.BaseMap"
Cohesion: 0.08
Nodes (6): Army, Goal, MusterBars, PoliticsSync, War, Wars

### Community 12 - "Peerage"
Cohesion: 0.08
Nodes (11): Advancement, Boards, Entry, View, Merit, Peer, Peerage, Taken (+3 more)

### Community 13 - "BaseMap"
Cohesion: 0.18
Nodes (4): BaseMap, Nation, BaseMapStore, Ready

### Community 14 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.05
Nodes (11): BaseMapPart, BattleSpot, FigureStrike, GateView, PeerageView, PoliticsView, TalkChoice, TeleportTo (+3 more)

### Community 15 - "Faction"
Cohesion: 0.10
Nodes (8): Faction, ResourceType, FOOD, Poi, PoiType, CAPITAL, CITY, FARM

### Community 16 - "BattlePlan"
Cohesion: 0.05
Nodes (14): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, BattlePlan, Order, CHARGE (+6 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.25
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 20 - "PveCommands.java"
Cohesion: 0.03
Nodes (39): RafasConquest Commands (guide v0.0.2), /council <topic>, Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers] (+31 more)

### Community 23 - "Centre"
Cohesion: 0.16
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 25 - ".plan"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "CityLayout"
Cohesion: 0.08
Nodes (6): CityLayout, District, Site, SlopeTile, WorldSettings, CityLayoutTest

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.17
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 29 - "Quests"
Cohesion: 0.12
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 30 - ".render"
Cohesion: 0.08
Nodes (8): CharacterScreen, Retainer, Spot, ClientPeerage, Banner, Portraits, Honour, Honours

### Community 31 - ".player"
Cohesion: 0.14
Nodes (6): Names, NobleTitle, COUNT, KNIGHT, NONE, PeerageTest

### Community 32 - "CapitalLayout"
Cohesion: 0.18
Nodes (5): CapitalLayout, Land, Site, CapitalRelocationTest, LayoutTest

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.16
Nodes (3): CityPlan, Building, NobleDistrictTest

### Community 36 - "Land"
Cohesion: 0.14
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 37 - "CityGridBuilder.java"
Cohesion: 0.05
Nodes (11): /pve debug citystats [samples], /pve debug cityterrain, Cities (grid, plots, vanilla village buildings), CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, CountryPlacement (+3 more)

### Community 38 - "FormationSnapshot"
Cohesion: 0.09
Nodes (13): FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN (+5 more)

### Community 39 - "net.minecraft.core.Direction"
Cohesion: 0.13
Nodes (7): Land, Result, SiegeSurvey, Wall, CityGates, Done, SiegeSurveyTest

### Community 43 - "net.minecraft.core.BlockPos"
Cohesion: 0.10
Nodes (3): StylePiece, GoldChurch, Layout

### Community 44 - "ConquestSavedData"
Cohesion: 0.06
Nodes (7): ConquestSavedData, CityBanners, ConquestServer, StructureLink, EconomyTick, PoiPrecomputer, ConquestSavedDataTest

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.11
Nodes (12): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+4 more)

### Community 48 - "com.mojang.blaze3d.vertex.PoseStack"
Cohesion: 0.10
Nodes (4): BackBannerLayer, GateRenderer, MarkerRenderer, MouthLayer

### Community 51 - "net.minecraft.world.entity.Entity"
Cohesion: 0.09
Nodes (5): FieldRules, Side, FLANK, FRONT, REAR

### Community 52 - "BattleSounds"
Cohesion: 0.20
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 54 - "Feud"
Cohesion: 0.13
Nodes (6): Clash, Feud, Kind, DUEL, SKIRMISH, FeudDuel

### Community 55 - "RecruitsArrowCleanerMixin.java"
Cohesion: 0.17
Nodes (4): CityVegetationMixin, RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 56 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 57 - "City"
Cohesion: 0.12
Nodes (6): City, Grid, SeatCities, CityIndex, CountrysideTest, World

### Community 59 - ".of"
Cohesion: 0.20
Nodes (3): FactionBanner, Layer, FactionBannerTest

### Community 60 - "SeaCalm"
Cohesion: 0.18
Nodes (4): Sea, Depths, SeaCalm, SwellTest

### Community 63 - "TitleMapTest"
Cohesion: 0.13
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 66 - "CourtView"
Cohesion: 0.08
Nodes (6): Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 67 - ".view"
Cohesion: 0.18
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 71 - ".readPlacements"
Cohesion: 0.27
Nodes (3): Offsets, Placement, PlacementOffsetsTest

### Community 72 - "DistantOcclusion"
Cohesion: 0.14
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 75 - "RoadFeature"
Cohesion: 0.09
Nodes (5): RoadFeature, Profile, RoadNetwork, Stretch, RoadNetworkTest

### Community 76 - "PlaceMusicClient"
Cohesion: 0.19
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 77 - "CityVariant"
Cohesion: 0.13
Nodes (6): CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 78 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.13
Nodes (3): FactionInfoReply, StatsView, UnitsState

### Community 79 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.28
Nodes (6): Boxes, SoldierFigure, Stuff, CLOTH, METAL, WOOD

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
Cohesion: 0.18
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 88 - "Recruits PvE Addon — Design Doc (Oct 26, 2026)"
Cohesion: 0.07
Nodes (4): Recruits PvE Addon — Design Doc (Oct 26, 2026), DayClock, Step, DayClockTest

### Community 89 - "Retinues"
Cohesion: 0.16
Nodes (4): GrandStrategy, View, Retinues, RetinuesTest

### Community 91 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.11
Nodes (11): Bark, CityClose, ConquestNetwork, GateSet, MarkerSet, MarkerView, Place, RequestBaseMap (+3 more)

### Community 95 - ".generate"
Cohesion: 0.15
Nodes (3): WorldFeatures, BaseMapGenerator, Heights

### Community 96 - ".postChunk"
Cohesion: 0.13
Nodes (3): RecruitsBridge, BarracksPeople, CityGuards

### Community 98 - "WaterSwell"
Cohesion: 0.11
Nodes (3): WaterSwell, Swell, SeaFoamTest

### Community 101 - "Override"
Cohesion: 0.24
Nodes (3): Land, Land, Land

### Community 103 - "HorizontalStretch"
Cohesion: 0.13
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 105 - "Realm"
Cohesion: 0.12
Nodes (4): Hook, Realm, Succession, Taking

### Community 107 - "DistantArmyRenderer"
Cohesion: 0.12
Nodes (3): DistantArmyRenderer, Figure, Hull

### Community 108 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 109 - "SimpleChannel"
Cohesion: 0.07
Nodes (12): BackBanner, Builder, Entry, Msg, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER, SimpleChannel (+4 more)

### Community 110 - "net.minecraft.core.BlockPos"
Cohesion: 0.11
Nodes (5): Saved, WarCamp, CourtSet, Seat, Style

### Community 111 - "DistantArmyRenderer.java"
Cohesion: 0.06
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 112 - "PoliticalStyle"
Cohesion: 0.15
Nodes (4): MapColors, PoliticalMap, LabelShape, PoliticalStyle

### Community 114 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 115 - "PoiPrecomputer.java"
Cohesion: 0.07
Nodes (7): SeedHash, FactionBannerNbt, FactionType, BANDIT, CITY_STATE, EMPIRE, FactionInfo

### Community 116 - "GateBlockEntity"
Cohesion: 0.07
Nodes (10): Door, GateBlockEntity, Swing, CLOSED, CLOSING, OPEN, OPENING, GateGeometry (+2 more)

### Community 117 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.18
Nodes (5): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer

### Community 118 - "CityLayout"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 124 - "BarracksPeople.java"
Cohesion: 0.11
Nodes (3): ConquestContent, WorldMapItem, ConquestEntities

### Community 128 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 129 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.14
Nodes (6): DistantShips, Far, SableSeen, BattleKind, Part, ShipHulls

### Community 133 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 134 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 136 - "BattleShips"
Cohesion: 0.27
Nodes (3): BattleShips, Helm, Ram

### Community 137 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 138 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 139 - "Distribution"
Cohesion: 0.22
Nodes (6): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, WarPlan

### Community 142 - "View"
Cohesion: 0.15
Nodes (3): BoardView, GrandStrategyView, TalkView

### Community 143 - "BattleRole"
Cohesion: 0.25
Nodes (6): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT

### Community 146 - "Lands"
Cohesion: 0.08
Nodes (3): Lands, Stronghold, Claim

### Community 149 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.22
Nodes (3): UnitLayer, RecruiterRenderer, TownspersonRenderer

### Community 152 - "Job"
Cohesion: 0.22
Nodes (6): Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY, SHIELDMAN

### Community 155 - "Build"
Cohesion: 0.29
Nodes (6): Build, CASTLE, HAMLET, RUIN, TOWER, WELL

### Community 156 - ".plan"
Cohesion: 0.12
Nodes (7): Faith, Bond, Kind, FRIEND, RIVAL, RealmPlanner, Seat

### Community 157 - ".fire"
Cohesion: 0.07
Nodes (4): CharacterMarkers, Placed, MarkerBlockEntity, MarkerSpec

### Community 160 - "Roads"
Cohesion: 0.12
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 163 - "Front"
Cohesion: 0.13
Nodes (4): Lord, Feuds, Front, Politics

### Community 164 - "Kit"
Cohesion: 0.14
Nodes (13): Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD, HEAVY_INFANTRY (+5 more)

### Community 167 - "ShipKind"
Cohesion: 0.33
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 174 - "Countryside"
Cohesion: 0.10
Nodes (11): Countryside, Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Plan (+3 more)

### Community 178 - "Role"
Cohesion: 0.33
Nodes (6): Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER

### Community 206 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 217 - ".pack"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 221 - "Engine"
Cohesion: 0.13
Nodes (8): Engine, LADDERS, RAMS, TOWERS, Gate, GateState, Plan, SiegeTest

### Community 223 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 226 - "org.joml.Matrix4f"
Cohesion: 0.10
Nodes (4): DistantDepth, Arrow, ArrowVolleys, BattleSpotRing

### Community 232 - ".startAt"
Cohesion: 0.05
Nodes (12): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Ground, GroundPlan, Api (+4 more)

### Community 234 - "net.neoforged.bus.api.SubscribeEvent"
Cohesion: 0.04
Nodes (3): BattleKeepInventory, ClientEvents, ConquestEvents

### Community 237 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 249 - "CityGridBuilder"
Cohesion: 0.12
Nodes (4): CityGridBuilder, Ground, PlacedWall, Platform

### Community 254 - ".view"
Cohesion: 0.08
Nodes (12): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+4 more)

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Homeland"
Cohesion: 0.08
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 257 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.06
Nodes (6): CommandTableBlock, GateBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, MarkerBlock

### Community 259 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 262 - "dev.ryanhcode.sable.sublevel.ServerSubLevel"
Cohesion: 0.14
Nodes (4): GateDoors, Frame, SableLink, SiegeFrames

### Community 267 - ".walks"
Cohesion: 0.09
Nodes (4): Frame, Harbour, CitySizesTest, PlaceMusicTest

### Community 277 - "Piece"
Cohesion: 0.13
Nodes (9): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL, Piece (+1 more)

### Community 287 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.35
Nodes (3): Armoury, Outfit, Style

### Community 288 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 289 - "Kind"
Cohesion: 0.50
Nodes (4): Kind, CLOSE, OPEN, RAM

### Community 305 - "Muster"
Cohesion: 0.67
Nodes (3): Muster, CEREMONIAL, OFFENSIVE

### Community 319 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.33
Nodes (4): Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), The general and guard, Victory, pursuit and surrender

### Community 332 - ".hash"
Cohesion: 0.11
Nodes (5): ChunkCoord, FarmLayout, NobleAnnex, OriginRoller, Roll

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **351 isolated node(s):** `CITY`, `DOJO`, `HIGH`, `SEA`, `TOWN` (+346 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1051 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **196 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `org.jetbrains.annotations.Nullable`, `Phase`, `net.minecraft.world.level.ChunkPos`, `.army`, `BattleScreen`, `Work`, `Peerage`, `PveCommands.java`, `WarCouncil`, `net.minecraft.world.item.ItemStack`, `PveCommands Registration`, `CityGridBuilder.java`, `net.minecraft.core.Direction`, `rafasconquest.faction.Faction`, `net.minecraft.server.level.ServerLevel`, `.saveFormation`, `Role`, `net.minecraft.world.entity.Entity`, `Feud`, `.inCity`, `net.minecraft.world.level.biome.Biome`, `net.minecraft.server.level.ServerPlayer`, `.officers`, `UnitMode`, `.startAt`, `net.neoforged.bus.api.SubscribeEvent`, `net.minecraft.core.BlockPos`?**
  _High betweenness centrality (0.049) - this node is a cross-community bridge._
- **What connects `CITY`, `DOJO`, `HIGH` to the rest of the system?**
  _351 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.039738826972869526 - nodes in this community are weakly interconnected._
- **Why does `FieldBattle` connect `org.jetbrains.annotations.Nullable` to `.army`, `.startAt`, `RecruitsBattle`, `.saveFormation`, `arraylist`, `BattlePlan`, `PveCommands.java`, `Engine`, `Call`?**
  _High betweenness centrality (0.040) - this node is a cross-community bridge._
- **Should `RoadFeature.java` be split into smaller, more focused modules?**
  _Cohesion score 0.050241545893719805 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._