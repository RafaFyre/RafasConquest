# Graph Report - RafasConquest  (2026-11-07)

## Corpus Check
- 420 files · ~329,001 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 115 file(s) not represented in the graph (top: .nbt 84, .toml 14, (none) 3)

## Summary
- 5735 nodes · 18201 edges · 332 communities (134 shown, 198 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2309 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `ac658385`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- Ranks
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsQuality.java
- .findGenerationPoint
- Duty
- .second
- OrganizationScreen
- ConquestSavedData
- Peerage
- BaseMap
- .render
- GoldChurch.java
- net.minecraft.network.FriendlyByteBuf
- Battle Map Web Page & Site
- Person
- arraylist
- RafasConquest.java
- PoliticsTest
- PveCommands.java
- CityManagerScreen
- .plan
- CityGridBuilder
- .shift
- WarCouncil
- rafasconquest.map.BaseMap
- CourtView
- net.neoforged.neoforge.network.handling.IPayloadContext
- WorldSettings
- PveCommands Registration
- School
- CityPlan
- Land
- arraylist
- FormationSnapshot
- World map (CK3-style political map)
- rafasconquest.faction.Faction
- SableShips.java
- War
- net.minecraft.server.level.ServerPlayer
- com.talhanation.recruits.entities.AbstractRecruitEntity
- Realm
- Personalities
- SeaLanes
- MouthLayer.java
- .drawEmpire
- Countryside
- BattleSounds
- net.minecraft.world.level.levelgen.structure.BoundingBox
- .of
- WarCamp
- net.minecraft.server.level.ServerLevel
- City
- Faction Map View
- .view
- SeaCalm
- RecruitsBattle
- TitleMapTest
- .measure
- StyleLibrary
- net.minecraft.nbt.CompoundTag
- net.minecraft.nbt.CompoundTag
- RafasConquest.java
- .render
- .wallSegments
- DistantOcclusion
- Barracks
- MapLands
- RoadFeature
- PlaceMusicClient
- .findGenerationPoint
- net.minecraft.world.level.Level
- rafasconquest.battle.FormationSnapshot
- Townsperson
- Ownership & Territory
- .choose
- net.minecraft.world.entity.Entity
- Nation Rows
- Arm
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- .view
- DialogueScreen
- optional
- Arm
- SableShips
- BoardScreen
- .generate
- ProvinceMap
- WaterSwell
- FactionInfoScreen
- rafasconquest.battle.BattlePlan.Point
- .sender
- CityStyles
- HorizontalStretch
- Realm
- ClientModEvents.java
- DistantArmyRenderer
- Swell
- Quests
- Kit
- DistantArmyRenderer.java
- .start
- DayClockTest
- Trait
- PoiPrecomputer.java
- Phase
- net.minecraft.client.gui.GuiGraphics
- UnitOrder
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
- .column
- Faction
- BattleShips
- .passOnOrders
- .boundsAt
- .touches
- Role
- BlockGrid
- Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)
- SimpleChannel.java
- Lands
- .roadStart
- net.minecraft.resources.ResourceLocation
- CLAUDE.md
- WorldSettings
- ShipKind
- .plan
- ItemFlags.java
- Roads
- Lands
- .champion
- Rank
- Small Cluster 170
- ClientEvents
- MarkerScreen
- org.jetbrains.annotations.Nullable
- CityTerrainPiece
- Kind
- .pack
- Grade
- FormationSnapshotTest
- Terrain
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
- .build
- SeatCitiesTest
- BattleRole
- StatsScreen
- Call
- Homeland
- net.minecraft.world.level.block.state.BlockState
- Kind
- Far
- .walks
- Strategy
- .number
- net.minecraft.world.item.ItemStack
- Ramming
- NobleAnnex
- .onLogout
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread
- Override
- .bark
- Wars (daily nation decisions)
- .issue
- .handleUnitsRequest
- Kind
- BattleKeepInventory
- Characters (holders, skills, prowess, traits)
- Ranks (Soldier to Grand Marshal)
- Distribution
- .inCity
- .handleStatsRequest
- NobleDistrictTest
- ConquestConfig
- UnitMode
- ClientBarks
- Kind
- .handleRequest
- Shape
- TalkChoice
- HornOrders
- GameCharacter
- WarHorn
- BackBanners
- BoardView
- CityManager

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

## Communities (332 total, 198 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.05
Nodes (7): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock

### Community 1 - "Ranks"
Cohesion: 0.18
Nodes (3): Ranks, Offer, Recruitment

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.09
Nodes (6): CityRoadPiece, FarmPiece, GoldChurchPiece, HarbourCutPiece, HarbourPiece, PlaceholderPiece

### Community 3 - ".army"
Cohesion: 0.09
Nodes (3): Point, DoctrineTest, FieldBattleTest

### Community 4 - "CityTerrain"
Cohesion: 0.10
Nodes (19): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+11 more)

### Community 5 - "BattleScreen"
Cohesion: 0.09
Nodes (4): BattleScreen, Entry, Menu, UnitInfo

### Community 6 - "DistantHorizonsQuality.java"
Cohesion: 0.08
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - ".findGenerationPoint"
Cohesion: 0.10
Nodes (3): SlopeTile, HarbourCut, HarbourCutTest

### Community 8 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 10 - "OrganizationScreen"
Cohesion: 0.13
Nodes (5): OrganizationScreen, Organization, Organizations, Sanctum, OrganizationsTest

### Community 11 - "ConquestSavedData"
Cohesion: 0.08
Nodes (4): ConquestSavedData, EconomyTick, ConquestSavedDataTest, FactionInfoTest

### Community 12 - "Peerage"
Cohesion: 0.10
Nodes (9): Boards, Entry, View, Merit, Peer, Peerage, Taken, PeerageSync (+1 more)

### Community 13 - "BaseMap"
Cohesion: 0.11
Nodes (6): ClientMapCache, MapPreview, BaseMap, Nation, BaseMapStore, Ready

### Community 14 - ".render"
Cohesion: 0.08
Nodes (7): CharacterScreen, Spot, ClientPeerage, Banner, Portraits, Honour, Honours

### Community 16 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.07
Nodes (8): BaseMapPart, BattleSnapshot, BattleSpot, MarkerView, PeerageView, PoliticsView, Volleys, WorldStretch

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.21
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "arraylist"
Cohesion: 0.04
Nodes (6): Character page, Court 3D scene (CourtView), Demographics (population by culture and race), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), InventoryStatsButton

### Community 20 - "RafasConquest.java"
Cohesion: 0.05
Nodes (9): /pve debug citystats [samples], /pve debug cityterrain, Ambushes from woods, Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards, Deploying to the land (GroundPlan), ConquestContent, ConquestEntities (+1 more)

### Community 21 - "PoliticsTest"
Cohesion: 0.10
Nodes (3): Event, PoliticsData, PoliticsTest

### Community 23 - "PveCommands.java"
Cohesion: 0.08
Nodes (26): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+18 more)

### Community 25 - ".plan"
Cohesion: 0.13
Nodes (11): Nation, Nations, Plan, Seats, Tier, COUNTY, DUCHY, EMPIRE (+3 more)

### Community 26 - "CityGridBuilder"
Cohesion: 0.08
Nodes (9): CityGridBuilder, PlacedWall, Platform, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY (+1 more)

### Community 27 - ".shift"
Cohesion: 0.30
Nodes (4): BattleSite, Part, Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.16
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 29 - "rafasconquest.map.BaseMap"
Cohesion: 0.11
Nodes (3): MusterBars, PoliticsSync, Wars

### Community 30 - "CourtView"
Cohesion: 0.07
Nodes (7): Retainer, Camera, CourtView, Figure, NationScreen, Demographics, Share

### Community 31 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.13
Nodes (6): BackBanner, Builder, Entry, NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 32 - "WorldSettings"
Cohesion: 0.12
Nodes (7): CapitalLayout, Land, Site, OriginRoller, Roll, CapitalRelocationTest, LayoutTest

### Community 34 - "School"
Cohesion: 0.09
Nodes (15): Dojo, Dojos, Ground, School, BLOOD, FIRE, ICE, IRON (+7 more)

### Community 35 - "CityPlan"
Cohesion: 0.14
Nodes (3): CityPlan, Piece, Plan

### Community 36 - "Land"
Cohesion: 0.15
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 37 - "arraylist"
Cohesion: 0.05
Nodes (12): Countryside (hamlets, villages, watchtowers, castles), Land titles (county, duchy, kingdom, empire), Mantra schools and dojos, Roads (high, city, town roads; bridges; signposts), Seat cities, CityLayout, StreetWalks, CityDistrictStructure (+4 more)

### Community 38 - "FormationSnapshot"
Cohesion: 0.12
Nodes (10): FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN (+2 more)

### Community 39 - "World map (CK3-style political map)"
Cohesion: 0.33
Nodes (7): Base map generated from seed, Command Table block, Faction Info screen, World map layers, Province size setting, Provinces (ProvinceMap), World map (CK3-style political map)

### Community 43 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.12
Nodes (8): Bark, CityClose, CityView, ConquestNetwork, GrandStrategyView, Place, SeaSwell, TalkView

### Community 44 - "com.talhanation.recruits.entities.AbstractRecruitEntity"
Cohesion: 0.12
Nodes (7): Role, BEARER, GENERAL, GUARD, LEADER, SOLDIER, Side

### Community 45 - "Realm"
Cohesion: 0.14
Nodes (5): Kind, FRIEND, RIVAL, Realm, CharacterMarkers

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.15
Nodes (11): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+3 more)

### Community 50 - ".drawEmpire"
Cohesion: 0.27
Nodes (3): Hit, LabelLayer, LabelShape

### Community 51 - "Countryside"
Cohesion: 0.18
Nodes (3): Settlement, CountryIndex, CountryStructure

### Community 52 - "BattleSounds"
Cohesion: 0.20
Nodes (4): BattleSounds, ChargeSound, DinSound, Source

### Community 53 - "net.minecraft.world.level.levelgen.structure.BoundingBox"
Cohesion: 0.14
Nodes (7): Build, CASTLE, HAMLET, RUIN, TOWER, WELL, CountryPiece

### Community 54 - ".of"
Cohesion: 0.18
Nodes (4): FactionBanner, Layer, FactionBannerNbt, FactionBannerTest

### Community 56 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.09
Nodes (5): CityBanners, CityGuards, ConquestServer, StructureLink, PoiPrecomputer

### Community 57 - "City"
Cohesion: 0.14
Nodes (4): City, Grid, SeatCities, CityIndex

### Community 59 - ".view"
Cohesion: 0.19
Nodes (5): Option, Talk, View, WarCouncilPagesTest, DialogueTest

### Community 60 - "SeaCalm"
Cohesion: 0.18
Nodes (4): Sea, Depths, SeaCalm, SwellTest

### Community 61 - "RecruitsBattle"
Cohesion: 0.09
Nodes (4): Card, Enlisted, RecruitsBattle, Unit

### Community 63 - "TitleMapTest"
Cohesion: 0.14
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 65 - "StyleLibrary"
Cohesion: 0.12
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.10
Nodes (4): Palette, WallTemplates, Placed, NoblePathPiece

### Community 67 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.09
Nodes (3): MarkerRenderer, MarkerBlockEntity, MarkerSpec

### Community 69 - "RafasConquest.java"
Cohesion: 0.04
Nodes (4): Battle map developer web page, Morale, rout and rally, Nations by zone, Storm wall and war zone

### Community 71 - ".wallSegments"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 72 - "DistantOcclusion"
Cohesion: 0.14
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 73 - "Barracks"
Cohesion: 0.11
Nodes (4): Barracks, BarracksPeople, BarracksPiece, BarracksTest

### Community 75 - "RoadFeature"
Cohesion: 0.06
Nodes (9): CityVegetationMixin, RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin, RoadFeature, Profile, RoadNetwork, Stretch (+1 more)

### Community 76 - "PlaceMusicClient"
Cohesion: 0.19
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 78 - "net.minecraft.world.level.Level"
Cohesion: 0.12
Nodes (3): GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock

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
Cohesion: 0.17
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 86 - "Arm"
Cohesion: 0.11
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.16
Nodes (7): Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE, SPEAR_AND_SHIELD, Formation, GREAT_SHIELD, GREAT_SPEAR

### Community 89 - ".view"
Cohesion: 0.10
Nodes (10): GrandStrategy, View, Names, NobleTitle, COUNT, KNIGHT, NONE, PlayerStats (+2 more)

### Community 91 - "optional"
Cohesion: 0.16
Nodes (3): CountryPlacement, DojoPlacement, FarmPlacement

### Community 92 - "Arm"
Cohesion: 0.12
Nodes (11): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, FieldRules, Rates, Side (+3 more)

### Community 95 - ".generate"
Cohesion: 0.09
Nodes (6): ProvinceNames, WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry

### Community 96 - "ProvinceMap"
Cohesion: 0.18
Nodes (4): Centre, ProvinceMap, Terrain, ProvinceMapTest

### Community 100 - "rafasconquest.battle.BattlePlan.Point"
Cohesion: 0.10
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 101 - ".sender"
Cohesion: 0.08
Nodes (5): BoardAction, FigureStrike, RequestBaseMap, SetSetting, TeleportTo

### Community 102 - "CityStyles"
Cohesion: 0.20
Nodes (3): Building, CityStyles, Houses

### Community 103 - "HorizontalStretch"
Cohesion: 0.13
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 105 - "Realm"
Cohesion: 0.10
Nodes (5): Hook, Succession, Taking, RealmPlannerTest, World

### Community 107 - "DistantArmyRenderer"
Cohesion: 0.11
Nodes (3): DistantArmyRenderer, Figure, Hull

### Community 109 - "Quests"
Cohesion: 0.08
Nodes (14): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Quest, Type (+6 more)

### Community 110 - "Kit"
Cohesion: 0.14
Nodes (13): Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD, HEAVY_INFANTRY (+5 more)

### Community 111 - "DistantArmyRenderer.java"
Cohesion: 0.05
Nodes (3): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures)

### Community 112 - ".start"
Cohesion: 0.09
Nodes (4): Api, DistantGeneration, Officers, BattleSite.Water

### Community 113 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 114 - "Trait"
Cohesion: 0.22
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 115 - "PoiPrecomputer.java"
Cohesion: 0.09
Nodes (14): SeedHash, FactionType, BANDIT, CITY_STATE, EMPIRE, ResourceType, FOOD, PoliticalMap (+6 more)

### Community 116 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 117 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.17
Nodes (5): LegendEntry, MapLayer, Symbol, RoadLayer, TerritoryLayer

### Community 118 - "UnitOrder"
Cohesion: 0.12
Nodes (16): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+8 more)

### Community 122 - "BattlePlan"
Cohesion: 0.06
Nodes (11): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot, WarPlan (+3 more)

### Community 124 - "Countryside"
Cohesion: 0.19
Nodes (4): Countryside, Plan, CountrysideTest, World

### Community 125 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 128 - "org.joml.Matrix4f"
Cohesion: 0.20
Nodes (3): DistantDepth, Arrow, ArrowVolleys

### Community 129 - "CourtSet"
Cohesion: 0.35
Nodes (3): CourtSet, Seat, Style

### Community 132 - "MapContext"
Cohesion: 0.17
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 134 - "Faction"
Cohesion: 0.15
Nodes (4): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction

### Community 136 - "BattleShips"
Cohesion: 0.23
Nodes (4): BattleShips, Helm, Ram, SableLink

### Community 138 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 140 - "Role"
Cohesion: 0.25
Nodes (8): Role, ADVISOR, GENERAL, KING, MERCHANT, POLITICIAN, TACTICIAN, WARRIOR

### Community 143 - "Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer)"
Cohesion: 0.17
Nodes (9): Battle phases and days, Braced walls against cavalry, Doctrines (Spear and Shield, Charge and Decapitate, Bait and Pincer), Enlisted players, The general and guard, Battle roles (frontline, bait, support, flanker, kill squad), Victory, pursuit and surrender, War council (+1 more)

### Community 144 - "SimpleChannel.java"
Cohesion: 0.15
Nodes (3): MarkerSet, Msg, SimpleChannel

### Community 146 - "Lands"
Cohesion: 0.07
Nodes (8): Goal, Lands, Stronghold, Claim, Muster, CEREMONIAL, OFFENSIVE, Politics

### Community 149 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.22
Nodes (3): UnitLayer, RecruiterRenderer, TownspersonRenderer

### Community 152 - "WorldSettings"
Cohesion: 0.10
Nodes (5): Site, ChunkCoord, FarmLayout, WorldSettings, CityLayoutTest

### Community 155 - "ShipKind"
Cohesion: 0.33
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 156 - ".plan"
Cohesion: 0.16
Nodes (4): Faith, Bond, RealmPlanner, Seat

### Community 160 - "Roads"
Cohesion: 0.23
Nodes (5): Link, Place, Roads, Spur, RoadsTest

### Community 163 - "Lands"
Cohesion: 0.14
Nodes (4): Feud, Lord, Feuds, Front

### Community 164 - ".champion"
Cohesion: 0.18
Nodes (5): Clash, Kind, DUEL, SKIRMISH, FeudDuel

### Community 167 - "Rank"
Cohesion: 0.08
Nodes (15): Job, ARCHER, CAVALRY, FOOTMAN, HEAVY_INFANTRY, SHIELDMAN, Rank, CAPTAIN (+7 more)

### Community 181 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.04
Nodes (4): /council <topic>, /pve debug recruits, /pve politics war <attacker> <defender> [generals], TickWeave experiment

### Community 206 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 217 - ".pack"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 218 - "Grade"
Cohesion: 0.16
Nodes (9): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Plan (+1 more)

### Community 219 - "FormationSnapshotTest"
Cohesion: 0.27
Nodes (3): Positions, Sprite, FormationSnapshotTest

### Community 226 - "BattleType"
Cohesion: 0.15
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 237 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 240 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.30
Nodes (3): BattleKind, Part, ShipHulls

### Community 252 - "BattleRole"
Cohesion: 0.18
Nodes (6): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Homeland"
Cohesion: 0.10
Nodes (16): Homeland, DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA (+8 more)

### Community 257 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.09
Nodes (3): Saved, CommandTableBlock, MarkerBlock

### Community 259 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 262 - "Far"
Cohesion: 0.21
Nodes (3): DistantShips, Far, SableSeen

### Community 277 - "Strategy"
Cohesion: 0.18
Nodes (9): Strategy, BAIT, CAVALRY_FLANK, DECAPITATE, DECAPITATING_CHARGE, MASS_CASUALTIES, PINCER, PRECISION_STRIKES (+1 more)

### Community 287 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.22
Nodes (3): Armoury, Outfit, Style

### Community 288 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 300 - "Wars (daily nation decisions)"
Cohesion: 0.22
Nodes (7): Battles (prototype), Blocks of 20 soldiers, Envelopment, Heralds (daily news), Mustering (offensive and ceremonial), War camps, Wars (daily nation decisions)

### Community 303 - "Kind"
Cohesion: 0.22
Nodes (7): Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL

### Community 306 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.33
Nodes (7): Characters (holders, skills, prowess, traits), Cultures (seven homelands), Epic Knights optional armour (Armoury), Faiths and cults, Martial titles (Sword Saint, Sword King, Sword God), Organizations, Sword sanctums

### Community 308 - "Ranks (Soldier to Grand Marshal)"
Cohesion: 0.33
Nodes (6): Gold church, Land grants (seven ways), Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter

### Community 309 - "Distribution"
Cohesion: 0.29
Nodes (5): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT

### Community 315 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 317 - "Kind"
Cohesion: 0.33
Nodes (6): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER

### Community 319 - "Shape"
Cohesion: 0.40
Nodes (5): Shape, LINE, SQUARE, V, WEDGE

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **335 isolated node(s):** `MILITARY`, `NOBLE`, `NONE`, `BANDIT`, `CITY_STATE` (+330 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1023 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **198 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `net.minecraft.world.level.ChunkPos`, `.army`, `.passOnOrders`, `.second`, `Peerage`, `RafasConquest.java`, `WarCouncil`, `rafasconquest.map.BaseMap`, `net.minecraft.world.item.ItemStack`, `PveCommands Registration`, `arraylist`, `rafasconquest.faction.Faction`, `net.minecraft.server.level.ServerPlayer`, `com.talhanation.recruits.entities.AbstractRecruitEntity`, `ClientEvents`, `org.jetbrains.annotations.Nullable`, `WarCamp`, `.inCity`, `UnitMode`, `Shape`, `GameCharacter`, `RafasConquest.java`, `net.minecraft.world.entity.Entity`, `Arm`, `rafasconquest.battle.BattlePlan.Point`, `.block`, `.start`, `net.minecraft.world.item.DyeColor`, `Phase`, `UnitOrder`?**
  _High betweenness centrality (0.072) - this node is a cross-community bridge._
- **What connects `MILITARY`, `NOBLE`, `NONE` to the rest of the system?**
  _335 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.05032679738562092 - nodes in this community are weakly interconnected._
- **Why does `Building style (style/<name>, style.json homelands)` connect `Battle Map Web Page & Site` to `RafasConquest.java`?**
  _High betweenness centrality (0.045) - this node is a cross-community bridge._
- **Should `net.minecraft.world.level.ChunkPos` be split into smaller, more focused modules?**
  _Cohesion score 0.08611670020120725 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._