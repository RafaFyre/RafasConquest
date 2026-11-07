# Graph Report - RafasConquest  (2026-11-07)

## Corpus Check
- 418 files · ~328,435 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 115 file(s) not represented in the graph (top: .nbt 84, .toml 14, (none) 3)

## Summary
- 5730 nodes · 18195 edges · 300 communities (119 shown, 181 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 2310 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `54fd1854`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.jetbrains.annotations.Nullable
- net.minecraft.server.level.ServerPlayer
- net.minecraft.world.level.ChunkPos
- .army
- CityTerrain
- BattleScreen
- DistantHorizonsQuality.java
- Site
- Duty
- .save
- NationScreen
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
- ClientPolitics
- ConquestNetwork
- WorldSettings
- PveCommands Registration
- School
- CityPlan
- Land
- net.minecraft.server.level.ServerLevel
- FormationSnapshot
- Characters (holders, skills, prowess, traits)
- rafasconquest.faction.Faction
- War
- .world
- .fire
- Personalities
- SeaLanes
- MouthLayer.java
- .drawEmpire
- Countryside
- BattleSounds
- net.minecraft.world.item.DyeColor
- .of
- net.minecraft.world.phys.Vec3
- net.minecraft.server.level.ServerLevel
- City
- Faction Map View
- .player
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
- RecruitsArrowCleanerMixin.java
- PlaceMusicClient
- .allSitesInBox
- net.neoforged.neoforge.network.handling.IPayloadContext
- rafasconquest.battle.FormationSnapshot
- Townsperson
- Ownership & Territory
- .choose
- CityVariant
- Nation Rows
- Arm
- Doctrine
- Recruits PvE Addon — Design Doc (Oct 26, 2026)
- .view
- Override
- org.junit.jupiter.api.Test
- Arm
- SableShips
- BoardScreen
- .generate
- ProvinceMap
- WaterSwell
- FactionInfoScreen
- .aWoodNearAWingHidesAnAmbushThatStaysPutUntilItStrikes
- RoadNetwork
- CityStyles
- HorizontalStretch
- Realm
- ClientModEvents.java
- DistantArmyRenderer
- Swell
- Quests
- Kit
- DistantArmyRenderer.java
- MarkerScreen
- DayClockTest
- Trait
- org.jetbrains.annotations.Nullable
- Phase
- net.minecraft.client.gui.GuiGraphics
- .view
- GrandStrategyScreen
- PoliticalStyle
- BattlePlan
- Grid
- RafasConquest (PvE addon for Villager Recruits)
- .onLevelLoad
- java.io.DataInputStream
- CourtSet
- Recruiter
- MapContext
- .plan
- OrganizationScreen
- BattleShips
- .passOnOrders
- .boundsAt
- Terrain
- RoadFeature
- BlockGrid
- StatsScreen
- CityTerrainPiece
- .onLogout
- Lands
- Plan
- net.minecraft.resources.ResourceLocation
- CLAUDE.md
- .blockStart
- .build
- .plan
- ItemFlags.java
- City
- Lands
- UnitMode
- Job
- Small Cluster 170
- ClientBarks
- NobleDistrictTest
- RecruitsBattle.java
- .get
- Kind
- .pack
- Faction
- Type
- Organizations
- Politics.Front
- ClientScreens
- BattleType
- net.minecraft.core.Vec3i
- .block
- .at
- net.neoforged.bus.api.SubscribeEvent
- Task
- .candidateChunk
- ConquestGameRules
- .render
- DistantGeneration
- WarHorn
- Call
- Race
- net.minecraft.world.level.block.state.BlockState
- Kind
- .ships
- .walks
- NetworkDirection
- RoadNetworkTest.java
- net.minecraft.world.item.ItemStack
- Ramming
- .hash
- .eachSpeakersRunIsAPageWithTheChoicesOnTheLast
- .theFoamNoiseRepeatsWithoutASeamAndHasPhysicsModsSpread

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

## Communities (300 total, 181 thin omitted)

### Community 0 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.05
Nodes (8): FieldArmy, Event, FieldBattle, Order, Target, Volley, FieldBlock, Rates

### Community 1 - "net.minecraft.server.level.ServerPlayer"
Cohesion: 0.15
Nodes (4): Boards, Entry, View, CityManager

### Community 2 - "net.minecraft.world.level.ChunkPos"
Cohesion: 0.08
Nodes (7): BarracksPiece, CityRoadPiece, CountryPiece, FarmPiece, HarbourCutPiece, HarbourPiece, PlaceholderPiece

### Community 3 - ".army"
Cohesion: 0.07
Nodes (3): Point, DoctrineTest, FieldBattleTest

### Community 4 - "CityTerrain"
Cohesion: 0.05
Nodes (21): CityTerrain, Column, Edge, CARVE, FILL, VALLEY, Heights, Kind (+13 more)

### Community 5 - "BattleScreen"
Cohesion: 0.06
Nodes (20): UnitOrder, BRACE, CHARGE, FOLLOW, GO, HOLD, LEAD_CHARGE, LINE (+12 more)

### Community 6 - "DistantHorizonsQuality.java"
Cohesion: 0.08
Nodes (4): DistantHorizonsArmies, DhApiBeforeApplyShaderRenderEvent, DistantHorizonsLink, DistantHorizonsQuality

### Community 7 - "Site"
Cohesion: 0.08
Nodes (4): Site, SlopeTile, HarbourCut, HarbourCutTest

### Community 8 - "Duty"
Cohesion: 0.14
Nodes (13): Duty, BEARER, FLEEING, FRONT, GENERAL, GUARD, LINE, RALLYING (+5 more)

### Community 10 - "NationScreen"
Cohesion: 0.08
Nodes (5): Retainer, Camera, CourtView, Figure, NationScreen

### Community 11 - "ConquestSavedData"
Cohesion: 0.06
Nodes (8): ConquestSavedData, Poi, ConquestServer, StructureLink, EconomyTick, PoiPrecomputer, ConquestSavedDataTest, FactionInfoTest

### Community 12 - "Peerage"
Cohesion: 0.12
Nodes (9): Merit, Peer, Peerage, Taken, PeerageSync, Quest, Option, Talk (+1 more)

### Community 13 - "BaseMap"
Cohesion: 0.13
Nodes (5): MapPreview, BaseMap, Nation, BaseMapStore, Ready

### Community 14 - ".render"
Cohesion: 0.08
Nodes (7): CharacterScreen, Spot, ClientPeerage, Banner, Portraits, Honour, Honours

### Community 16 - "net.minecraft.network.FriendlyByteBuf"
Cohesion: 0.05
Nodes (11): BattleSnapshot, BattleSpot, BoardView, FactionInfoReply, GrandStrategyView, PeerageView, PoliticsView, StatsView (+3 more)

### Community 17 - "Battle Map Web Page & Site"
Cohesion: 0.06
Nodes (50): Battles (centre and two wings), Bigger world (horizontal stretch), Characters and faiths (sword sanctums), Land titles (counties, duchies, kingdoms, empires), Latest pre-release jar link script, Living nations, Optional mods (Epic Knights, Distant Horizons, Embeddium), Peerage (military and noble ladders) (+42 more)

### Community 18 - "Person"
Cohesion: 0.23
Nodes (6): Culture, Marriages, Setter, Wedding, Hold, Person

### Community 19 - "arraylist"
Cohesion: 0.04
Nodes (5): Character page, Court 3D scene (CourtView), Nation page, Races (Sunborn, Vinekin, Meadowfolk, ...), InventoryStatsButton

### Community 20 - "RafasConquest.java"
Cohesion: 0.10
Nodes (3): ConquestContent, ConquestEntities, RafasConquest

### Community 23 - "PveCommands.java"
Cohesion: 0.06
Nodes (38): RafasConquest Commands (guide v0.0.2), Muster bar (countdown at top of screen), /pve debug advance <days>, /pve debug battle [first [n] second [n]], /pve debug battle clear, /pve debug battle end general|surrender <0|1>, /pve debug battle livecap [soldiers], /pve debug battle simulate (+30 more)

### Community 25 - ".plan"
Cohesion: 0.17
Nodes (6): Nation, Nations, Plan, Seats, NationsTest, World

### Community 26 - "CityGridBuilder"
Cohesion: 0.14
Nodes (4): Houses, CityGridBuilder, Ground, Platform

### Community 27 - ".shift"
Cohesion: 0.20
Nodes (5): BattleSite, Part, Water, BattleSite.Water, BattleSiteTest

### Community 28 - "WarCouncil"
Cohesion: 0.18
Nodes (5): Facts, Officer, Said, Talk, WarCouncil

### Community 29 - "rafasconquest.map.BaseMap"
Cohesion: 0.10
Nodes (8): Clash, Kind, DUEL, SKIRMISH, FeudDuel, MusterBars, PoliticsSync, Wars

### Community 31 - "ConquestNetwork"
Cohesion: 0.06
Nodes (18): Bark, CityClose, CityView, ConquestNetwork, FigureStrike, HornOrders, MarkerSet, MarkerView (+10 more)

### Community 32 - "WorldSettings"
Cohesion: 0.16
Nodes (6): CapitalLayout, Land, Site, WorldSettings, CapitalRelocationTest, LayoutTest

### Community 34 - "School"
Cohesion: 0.09
Nodes (14): Dojos, Ground, School, BLOOD, FIRE, ICE, IRON, LIGHT (+6 more)

### Community 35 - "CityPlan"
Cohesion: 0.14
Nodes (3): CityPlan, Piece, Building

### Community 36 - "Land"
Cohesion: 0.14
Nodes (5): Heap, Land, Router, Search, ShippingRoutes

### Community 37 - "net.minecraft.server.level.ServerLevel"
Cohesion: 0.05
Nodes (9): CityLayout, CityDistrictStructure, CitySlopeStructure, CityStructure, ConquestWorldgen, CountryPlacement, DojoPlacement, FarmPlacement (+1 more)

### Community 38 - "FormationSnapshot"
Cohesion: 0.11
Nodes (12): FormationSnapshot, Kind, ARCHERS, BEARER, CAVALRY, GENERAL, SHIELDWALL, SWORDSMEN (+4 more)

### Community 39 - "Characters (holders, skills, prowess, traits)"
Cohesion: 0.05
Nodes (37): Ambushes from woods, Base map generated from seed, Battle phases and days, Battles (prototype), Blocks of 20 soldiers, Characters (holders, skills, prowess, traits), Cities (grid, plots, vanilla village buildings), City wall, banners and wall guards (+29 more)

### Community 42 - "War"
Cohesion: 0.08
Nodes (6): Army, Goal, Muster, CEREMONIAL, OFFENSIVE, War

### Community 44 - ".world"
Cohesion: 0.18
Nodes (4): Demographics, Share, RealmPlannerTest, World

### Community 46 - "Personalities"
Cohesion: 0.05
Nodes (36): Personality, AMBITIOUS, ARBITRARY, ARROGANT, BRAVE, CALLOUS, CALM, COMPASSIONATE (+28 more)

### Community 47 - "SeaLanes"
Cohesion: 0.15
Nodes (11): Lane, Leg, Port, SeaLanes, Ship, Size, LARGE, MEDIUM (+3 more)

### Community 50 - ".drawEmpire"
Cohesion: 0.23
Nodes (3): Hit, LabelLayer, LabelShape

### Community 51 - "Countryside"
Cohesion: 0.12
Nodes (9): Kind, CASTLE, HAMLET, RUIN, VILLAGE, WATCHTOWER, Settlement, CountryIndex (+1 more)

### Community 52 - "BattleSounds"
Cohesion: 0.14
Nodes (5): BattleSounds, ChargeSound, DinSound, Source, DistantArmies

### Community 53 - "net.minecraft.world.item.DyeColor"
Cohesion: 0.16
Nodes (5): DistantShips, Far, BattleKind, Part, ShipHulls

### Community 54 - ".of"
Cohesion: 0.20
Nodes (4): FactionBanner, Layer, FactionBannerNbt, CityBanners

### Community 55 - "net.minecraft.world.phys.Vec3"
Cohesion: 0.06
Nodes (5): Card, Enlisted, Figure, Unit, WarCamp

### Community 57 - "City"
Cohesion: 0.13
Nodes (8): City, SeatCities, Tier, COUNTY, DUCHY, EMPIRE, KINGDOM, CityIndex

### Community 59 - ".player"
Cohesion: 0.16
Nodes (7): GrandStrategy, View, Names, NobleTitle, COUNT, KNIGHT, NONE

### Community 60 - "SeaCalm"
Cohesion: 0.16
Nodes (4): Sea, Depths, SeaCalm, SwellTest

### Community 61 - "RecruitsBattle"
Cohesion: 0.04
Nodes (11): GeneralTask, Officers, RecruitsBattle, Role, BEARER, GENERAL, GUARD, LEADER (+3 more)

### Community 63 - "TitleMapTest"
Cohesion: 0.13
Nodes (4): TitleMap, TitleStyle, Land, TitleMapTest

### Community 65 - "StyleLibrary"
Cohesion: 0.11
Nodes (5): Loaded, Offsets, Placement, StyleLibrary, PlacementOffsetsTest

### Community 66 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.06
Nodes (11): Saved, Palette, WallTemplates, Build, CASTLE, HAMLET, RUIN, TOWER (+3 more)

### Community 67 - "net.minecraft.nbt.CompoundTag"
Cohesion: 0.10
Nodes (3): MarkerRenderer, MarkerBlockEntity, MarkerSpec

### Community 69 - "RafasConquest.java"
Cohesion: 0.04
Nodes (3): Battle map developer web page, Morale, rout and rally, BattleMap

### Community 71 - ".wallSegments"
Cohesion: 0.17
Nodes (7): Side, EAST, NORTH, SOUTH, WEST, WallSegment, CityWallTest

### Community 72 - "DistantOcclusion"
Cohesion: 0.15
Nodes (6): DistantOcclusion, Mode, OFF, ON, VIEW, DistantOcclusionTest

### Community 75 - "RecruitsArrowCleanerMixin.java"
Cohesion: 0.15
Nodes (4): CityVegetationMixin, RecruitsArrowCleanerMixin, SodiumShaderInterfaceMixin, SodiumShaderLoaderMixin

### Community 76 - "PlaceMusicClient"
Cohesion: 0.16
Nodes (3): PlaceMusicClient, Track, PlaceMusicClientTest

### Community 78 - "net.neoforged.neoforge.network.handling.IPayloadContext"
Cohesion: 0.11
Nodes (3): BackBanner, TalkView, Entry

### Community 79 - "rafasconquest.battle.FormationSnapshot"
Cohesion: 0.28
Nodes (6): Boxes, SoldierFigure, Stuff, CLOTH, METAL, WOOD

### Community 81 - "Townsperson"
Cohesion: 0.14
Nodes (4): Walk, DistrictKey, Townsfolk, Townsperson

### Community 82 - "Ownership & Territory"
Cohesion: 0.20
Nodes (5): Candidate, OwnershipResolver, Claim, OwnershipResolverTest, TerritoryMapTest

### Community 83 - ".choose"
Cohesion: 0.14
Nodes (10): Grant, LandGrants, Way, DEAD_VASSAL_FREE, DEAD_VASSAL_SWORN, DISLIKED_VASSAL, EMPTY_COUNTY, OWN_COUNTY (+2 more)

### Community 84 - "CityVariant"
Cohesion: 0.14
Nodes (7): PlacedWall, CityVariant, DESERT, PLAINS, SAVANNA, SNOWY, TAIGA

### Community 86 - "Arm"
Cohesion: 0.18
Nodes (8): Calibration, Fit, Striker, ARCHER_MELEE, ARROWS, CAVALRY, INFANTRY, CalibrationTest

### Community 87 - "Doctrine"
Cohesion: 0.05
Nodes (31): Distribution, EVEN, STACKED_CENTRE, STACKED_LEFT, STACKED_RIGHT, Doctrine, BAIT_AND_PINCER, CHARGE_AND_DECAPITATE (+23 more)

### Community 89 - ".view"
Cohesion: 0.10
Nodes (12): Rank, CAPTAIN, COLONEL, CORPORAL, GRAND_MARSHAL, MAJOR, MARSHAL, SERJEANT (+4 more)

### Community 91 - "org.junit.jupiter.api.Test"
Cohesion: 0.06
Nodes (8): BattlePlanTest, CitySizesTest, NobleAnnexTest, DialogueTest, FactionBannerTest, ArmouryTest, PeerageTest, RetinuesTest

### Community 92 - "Arm"
Cohesion: 0.13
Nodes (10): Arm, ARCHERS, CAVALRY, SHIELDWALL, SWORDSMEN, FieldRules, Side, FLANK (+2 more)

### Community 95 - ".generate"
Cohesion: 0.07
Nodes (14): Dojo, WorldFeatures, BaseMapGenerator, Heights, BiomePalette, Entry, Homeland, DESERT (+6 more)

### Community 96 - "ProvinceMap"
Cohesion: 0.18
Nodes (3): ProvinceMap, Terrain, ProvinceMapTest

### Community 100 - ".aWoodNearAWingHidesAnAmbushThatStaysPutUntilItStrikes"
Cohesion: 0.09
Nodes (7): Ground, GroundPlan, GroundPlanTest, Land, Land, Land, Land

### Community 101 - "RoadNetwork"
Cohesion: 0.18
Nodes (3): Profile, RoadNetwork, Stretch

### Community 103 - "HorizontalStretch"
Cohesion: 0.13
Nodes (5): ConquestWorldScreen, AbstractSliderButton, AbstractSliderButton, Choice, HorizontalStretch

### Community 105 - "Realm"
Cohesion: 0.13
Nodes (4): Hook, Realm, Succession, Taking

### Community 107 - "DistantArmyRenderer"
Cohesion: 0.09
Nodes (5): Arrow, ArrowVolleys, DistantArmyRenderer, Figure, Hull

### Community 109 - "Quests"
Cohesion: 0.13
Nodes (9): DataFiles, Dialogue, Ladder, MILITARY, NOBLE, NONE, Kind, Quests (+1 more)

### Community 110 - "Kit"
Cohesion: 0.14
Nodes (13): Kit, ARCHER_GUARD, CAPTAIN, CAVALRY, GENERAL, GREAT_NOBLE, GUARD, HEAVY_INFANTRY (+5 more)

### Community 111 - "DistantArmyRenderer.java"
Cohesion: 0.07
Nodes (4): Arrows in flight (figure volleys), Battle sounds, Seeing the whole field (distant flat figures), DistantDepth

### Community 113 - "DayClockTest"
Cohesion: 0.23
Nodes (3): DayClock, Step, DayClockTest

### Community 114 - "Trait"
Cohesion: 0.20
Nodes (5): Trait, BOLD, CAUTIOUS, CUNNING, PROUD

### Community 115 - "org.jetbrains.annotations.Nullable"
Cohesion: 0.08
Nodes (8): SeedHash, TerritoryMap, FactionType, BANDIT, CITY_STATE, EMPIRE, PoliticalMap, FactionInfo

### Community 116 - "Phase"
Cohesion: 0.22
Nodes (8): Phase, AFTERMATH, BATTLE, DEPLOYMENT, DONE, NIGHT, PULLBACK, PURSUIT

### Community 117 - "net.minecraft.client.gui.GuiGraphics"
Cohesion: 0.18
Nodes (5): BaseMapLayer, LegendEntry, MapLayer, Symbol, TerritoryLayer

### Community 118 - ".view"
Cohesion: 0.20
Nodes (3): PlayerStats, View, Retinues

### Community 122 - "BattlePlan"
Cohesion: 0.11
Nodes (7): BattlePlan, Order, CHARGE, ENGAGE, HOLD, MARCH, Slot

### Community 124 - "Grid"
Cohesion: 0.22
Nodes (5): Countryside, Plan, Grid, CountrysideTest, World

### Community 125 - "RafasConquest (PvE addon for Villager Recruits)"
Cohesion: 0.22
Nodes (5): Recruits PvE Addon Design Doc (PDF), Distant Horizons (recommended mod), Embeddium (recommended mod), RafasConquest (PvE addon for Villager Recruits), Villager Recruits

### Community 128 - "java.io.DataInputStream"
Cohesion: 0.19
Nodes (4): Bond, Kind, FRIEND, RIVAL

### Community 129 - "CourtSet"
Cohesion: 0.23
Nodes (3): CourtSet, Seat, Style

### Community 130 - "Recruiter"
Cohesion: 0.07
Nodes (9): Gold church, Peerage (military and noble ladders), Quest board and quests, Ranks (Soldier to Grand Marshal), Recruitment posts and recruiter, WorldMapItem, Barks, Ear (+1 more)

### Community 132 - "MapContext"
Cohesion: 0.18
Nodes (4): MapContext, Place, PlaceMenuLayer, PoiIconLayer

### Community 136 - "BattleShips"
Cohesion: 0.23
Nodes (4): BattleShips, Helm, Ram, SableLink

### Community 137 - ".passOnOrders"
Cohesion: 0.08
Nodes (7): Landing, Live, Shape, LINE, SQUARE, V, WEDGE

### Community 138 - ".boundsAt"
Cohesion: 0.30
Nodes (3): Bounds, DistanceScaling, DistanceScalingTest

### Community 142 - "StatsScreen"
Cohesion: 0.14
Nodes (3): SettingsScreen, StatsScreen, SetSetting

### Community 146 - "Lands"
Cohesion: 0.09
Nodes (3): Lands, Stronghold, Claim

### Community 148 - "Plan"
Cohesion: 0.09
Nodes (10): PlaceMusic, Kind, BARRACKS, CHURCH, NOBLE, PALACE, SQUARE, WALL (+2 more)

### Community 149 - "net.minecraft.resources.ResourceLocation"
Cohesion: 0.18
Nodes (3): UnitLayer, RecruiterRenderer, TownspersonRenderer

### Community 156 - ".plan"
Cohesion: 0.20
Nodes (4): Centre, Faith, RealmPlanner, Seat

### Community 160 - "City"
Cohesion: 0.13
Nodes (14): Grade, CITY, DOJO, HIGH, SEA, TOWN, Junction, Link (+6 more)

### Community 163 - "Lands"
Cohesion: 0.12
Nodes (6): Feud, Lord, Feuds, Event, Front, Politics

### Community 164 - "UnitMode"
Cohesion: 0.33
Nodes (6): UnitMode, CHARGE, FOLLOW, HOLD, MOVE, RECRUITS

### Community 167 - "Job"
Cohesion: 0.09
Nodes (12): BattleRole, BAIT, FLANKER, FRONTLINE, KILL_SQUAD, SUPPORT, Job, ARCHER (+4 more)

### Community 206 - "Kind"
Cohesion: 0.15
Nodes (12): Kind, CAPITAL, CASTLE, CHILD, CITY, DEAD, EXILE, LORD (+4 more)

### Community 217 - ".pack"
Cohesion: 0.27
Nodes (5): BlockPacker, Placed, Rect, Size, BlockPackerTest

### Community 218 - "Faction"
Cohesion: 0.10
Nodes (10): Package layout (core, data, worldgen, sim, bridge), Recruits faction registration (bridge package), RecruitsBridge, Faction, ResourceType, FOOD, PoiType, CAPITAL (+2 more)

### Community 219 - "Type"
Cohesion: 0.33
Nodes (4): Type, CLEAR, DELIVER, SCOUT

### Community 221 - "Organizations"
Cohesion: 0.29
Nodes (3): Organizations, Sanctum, OrganizationsTest

### Community 226 - "BattleType"
Cohesion: 0.17
Nodes (6): BattleType, AMPHIBIOUS, LAND, OCEAN, SIEGE, Outcome

### Community 232 - ".block"
Cohesion: 0.08
Nodes (5): ShipKind, ARCHERS, CAMP, SHOCK, SWORDSMEN

### Community 234 - "net.neoforged.bus.api.SubscribeEvent"
Cohesion: 0.05
Nodes (3): BattleKeepInventory, ClientEvents, ConquestEvents

### Community 237 - "Task"
Cohesion: 0.25
Nodes (7): Task, AMBUSH, BAIT, FLANK, KILL, NONE, SUPPORT

### Community 255 - "Call"
Cohesion: 0.22
Nodes (9): Call, ADVANCE, BAIT, CAVALRY_CHARGE, CAVALRY_RECALL, FINAL_CHARGE, GENERAL_CHARGES, PINCER (+1 more)

### Community 256 - "Race"
Cohesion: 0.17
Nodes (8): Race, BOGFOLK, FROSTBORN, GRASSWALKERS, MEADOWFOLK, PINEKIN, SUNBORN, VINEKIN

### Community 257 - "net.minecraft.world.level.block.state.BlockState"
Cohesion: 0.07
Nodes (6): CommandTableBlock, GrandStrategyBlock, QuestBoardBlock, StrategyBoardBlock, Builder, MarkerBlock

### Community 259 - "Kind"
Cohesion: 0.40
Nodes (3): Kind, FAVOUR, SECRET

### Community 277 - "NetworkDirection"
Cohesion: 0.50
Nodes (3): NetworkDirection, PLAY_TO_CLIENT, PLAY_TO_SERVER

### Community 287 - "net.minecraft.world.item.ItemStack"
Cohesion: 0.18
Nodes (4): Armoury, Outfit, Style, RankKits

### Community 288 - "Ramming"
Cohesion: 0.32
Nodes (3): Hull, Ramming, RammingTest

### Community 289 - ".hash"
Cohesion: 0.14
Nodes (4): NobleAnnex, OriginRoller, Roll, ProvinceNames

## Ambiguous Edges - Review These
- `/pve debug battle simulate` → `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`  [AMBIGUOUS]
  Recruits PvE Addon — Design Doc.pdf · relation: conceptually_related_to
- `Mustering (offensive and ceremonial)` → `Roads (high, city, town roads; bridges; signposts)`  [AMBIGUOUS]
  README.md · relation: conceptually_related_to

## Knowledge Gaps
- **335 isolated node(s):** `NORTH`, `EAST`, `SOUTH`, `WEST`, `MILITARY` (+330 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1021 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **181 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RecruitsBattle` connect `RecruitsBattle` to `org.jetbrains.annotations.Nullable`, `net.minecraft.server.level.ServerPlayer`, `net.minecraft.world.level.ChunkPos`, `.army`, `BattleScreen`, `.passOnOrders`, `Plan`, `PoliticsTest`, `.shift`, `WarCouncil`, `rafasconquest.map.BaseMap`, `net.minecraft.world.item.ItemStack`, `PveCommands Registration`, `UnitMode`, `net.minecraft.server.level.ServerLevel`, `rafasconquest.faction.Faction`, `list`, `RecruitsBattle.java`, `net.minecraft.world.item.DyeColor`, `net.minecraft.world.phys.Vec3`, `RafasConquest.java`, `net.neoforged.neoforge.network.handling.IPayloadContext`, `Doctrine`, `.aWoodNearAWingHidesAnAmbushThatStaysPutUntilItStrikes`, `.block`, `net.neoforged.bus.api.SubscribeEvent`, `Trait`, `Phase`?**
  _High betweenness centrality (0.085) - this node is a cross-community bridge._
- **What connects `NORTH`, `EAST`, `SOUTH` to the rest of the system?**
  _335 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.jetbrains.annotations.Nullable` be split into smaller, more focused modules?**
  _Cohesion score 0.05450861195542046 - nodes in this community are weakly interconnected._
- **Why does `ConquestNetwork` connect `ConquestNetwork` to `ClientScreens`, `PveCommands Registration`, `net.minecraft.world.level.block.state.BlockState`, `.aWoodNearAWingHidesAnAmbushThatStaysPutUntilItStrikes`, `BattleScreen`, `list`, `net.neoforged.neoforge.network.handling.IPayloadContext`, `StatsScreen`, `net.minecraft.network.FriendlyByteBuf`, `rafasconquest.map.BaseMap`, `arraylist`, `RafasConquest.java`, `RecruitsBattle.java`, `CityManagerScreen`, `RecruitsBattle`, `BoardScreen`, `SimpleChannel.java`?**
  _High betweenness centrality (0.039) - this node is a cross-community bridge._
- **Should `net.minecraft.world.level.ChunkPos` be split into smaller, more focused modules?**
  _Cohesion score 0.08433734939759036 - nodes in this community are weakly interconnected._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `/pve debug battle simulate` and `Open questions (R1/R2, thresholds, capture rules, resources, off-player battles)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Mustering (offensive and ceremonial)` and `Roads (high, city, town roads; bridges; signposts)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._