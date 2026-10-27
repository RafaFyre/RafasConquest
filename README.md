# RafasConquest

A PvE addon for [Villager Recruits](https://modrinth.com/mod/villager-recruits): AI-run factions that grow their armies from territory.
Forge 1.20.1 (47.4.28), Java 17. The design doc is `Recruits PvE Addon — Design Doc.pdf` in this folder.

## Build and run

```
gradlew build          # compiles, runs unit tests, writes build/libs/rafasconquest-<version>.jar
gradlew test           # unit tests only
gradlew runClient      # dev client with Recruits loaded
gradlew runServer      # dev server (needs eula=true in run/eula.txt)
```

Recruits is pulled from Modrinth's maven, pinned by version ID in `gradle.properties` (`recruits_version`; currently 1.15.2).

## Recommended mods

Not required (they aren't in `mods.toml`), but recommended for everyone, and loaded in dev runs:

- [Embeddium](https://modrinth.com/mod/embeddium) (0.3.31+mc1.20.1): rendering performance.
- [Distant Horizons](https://modrinth.com/mod/distanthorizons) (3.3.3-1.20.1): far-away terrain, so cities and their
  walls can be seen from a distance. Its distant generation runs normal world generation, cities included, for far
  chunks, so expect background CPU use while it fills in.

They are `runtimeOnly` dependencies in `build.gradle`, pinned in `gradle.properties` (`embeddium_version`,
`distant_horizons_version`).

## Milestone 1 status

Built: one Empire per Capital, Farms producing food, virtual POIs, ownership, the daily food tick, offline catch-up and debug commands.
Not built (by design): battles (beyond the prototype below), recruit spawning, captures, raids, City-states, Bandits, growth, resources other than food.

Debug commands (permission level 2): `/pve faction list`, `/pve faction info <name|nearest>`, `/pve poi list [radius]`,
`/pve debug advance <days>`, `/pve debug recruits`, `/pve debug citystats [samples]`, `/pve debug cityterrain`,
`/pve debug battle [first second]`, `/pve debug battle clear`.

## Recruits integration

Every faction this mod creates is also registered as a Villager Recruits faction, so it appears in the Recruits faction
menu. Registration happens as factions are discovered, plus a catch-up pass when the server starts. Each one is a
scoreboard team (Recruits factions are teams) in the faction's colour, plus a Recruits faction record holding the
faction's name and banner. These factions have no player leader. Their leader id is the faction's id, which marks them
as ours, and their leader name is the faction type ("Empire", "City-state"). Recruits saves them with the world.
All Recruits calls live in the `bridge` package: `RecruitsFactions` behind the `RecruitsBridge` interface, and the
debug battle in `RecruitsBattle`. `/pve debug recruits` lists what Recruits holds.

## Battles (prototype)

`/pve debug battle [first [firstSoldiers] second [secondSoldiers]]` deploys two armies of Recruits soldiers 45 blocks
in front of you, marching at each other across your view: for example `/pve debug battle Osford 2000 Valoia 500`.
Each side is 20 to 2000 soldiers (rounded to whole blocks of 20), or 100 if no number is given. With no names given,
the two factions nearest you fight; quote names that contain spaces. The player is a bystander: the two factions are
made enemies in Recruits' diplomacy for the battle, so soldiers attack each other and leave you alone unless you strike
first. The old relation is restored when the battle ends.

- **Each army** is one faction, in blocks of 20 soldiers (10 wide, 2 deep), each led by a captain, plus a general and
  a standard-bearer. A fifth of the blocks are archers and a fifth cavalry (rounded); the rest is infantry, with a
  shieldwall every third file (`battle/BattlePlan.deployment`).
- **Lines:** the infantry stands in a front line at most 10 blocks wide, with relief lines 16 blocks apart behind it
  (at least two lines once there are more than 4 infantry blocks), so 2000 soldiers stand about 6 lines deep. Archers
  stand in rows just behind the front line, and the cavalry on both wings, in columns of blocks. The ground is loaded
  before anyone is spawned.
- **Uniforms:** everyone wears full leather dyed the faction's main colour (its banner's base colour), and cavalry
  horses wear dyed leather horse armour. If both factions share a colour, the second side wears a darker shade, or a
  lighter one if the colour is already very dark. The head shows rank:
  - **Ranks:** a dyed leather cap.
  - **Captain** (one per block, cavalry and archers included): the faction banner, worn on the head.
  - **General:** a gold helmet and the faction banner on the back, like a sashimono (`client/BackBannerLayer`; the server
    tells each client which soldiers wear one).
  - **Standard-bearer:** a dyed cap and the faction banner carried in hand. They stay at the general's side and don't fight.
- **Battle soldiers can't pick up items, and drop nothing when they die**, so nobody swaps the uniform for loot and
  the field stays clear.
- **Line fighting:** front blocks march in line until an enemy is within 10 blocks, then stand and fight in line.
  Recruits soldiers holding a place take on whoever comes within reach and return to it, so the line holds instead of
  dissolving into a melee. A block whose enemy in front is gone presses on.
- **Relief:** a front block down to half its soldiers is relieved by the first fit block (at least 60 % strong)
  waiting in its file, or the nearest fit block from another file. When the relief arrives (or after 20 s) it takes
  over, and the worn block withdraws behind the last block of its file, passive on the way. It stays there, fighting
  only if the enemy reaches it. This rotation keeps fresh soldiers at the front and makes a big battle last; with no
  fit block left, a front block fights on.
- **The general** is rolled 50/50 for each army, and gives every block an order every second
  (`battle/BattlePlan.order`).
  - **Warrior:** a Recruits soldier at the top level (20 by Recruits' default config, about 80 health) with an iron
    sword. They stand just behind the front line and charge in once it is fighting. Simple orders:
    - After 5 s in formation, the front line marches 2 blocks a second, then fights in line.
    - Archers march until an enemy is within 30 blocks, then halt and shoot.
    - Cavalry waits on the wings until its front line is fighting, or an enemy comes within 20 blocks, then rides for
      the enemy archers.
  - **Tactician:** stays behind all the lines, out of the fight, and commands better:
    - **Patience:** the infantry holds for 20 s while the archers advance and shoot, unless an enemy comes within
      25 blocks first.
    - **Cavalry flanking:** once the lines lock, each cavalry block rides wide past its end of the enemy line, then
      round behind it, and charges from the rear. A ride that takes more than 40 s charges anyway.
    - **A reserve:** a fifth of the infantry waits behind the last line. It is committed, joining the files with the
      fewest blocks waiting, when a front block needs relief and no fit block is left, or the army is down to 60 %.
  - Cavalry that has charged, and a warrior general, send any soldier with no target after the nearest enemy left.
- **Archers never march past their own front line**, so they don't walk out alone while the infantry waits.
- **No open files:** a front file whose block has fallen, with no fit block left to send, is filled by any block still
  in the lines (worn or not), else the nearest resting block.
- **The end:** an army routs at 20 % strength; its survivors turn passive and run back the way they came. If nobody
  has fallen for 30 s once fighting has begun, the lines have stalled and everyone left charges. After 30 minutes the
  battle is called a draw.
- **Reports:** every 10 s the chat and server log get the soldiers left on each side, the blocks relieved so far, the
  server tick time, and how much of it the battle's own orders take. The start and end name each general's type, and
  the end says whether each general and standard survived. `/pve debug battle clear` removes every soldier and horse
  the battle spawned. Only one battle runs at a time.
- **Battles as numbers** (`battle/FieldBattle`, the first step toward battles too big for live soldiers): the same
  battle fought block by block, one second at a time, with no entities. The generals give the same orders as in a
  live battle (`BattlePlan`); blocks walk to where they are ordered (3 blocks a second on foot, 6 mounted) and fight
  whoever is within reach (`battle/FieldRules`):
  - A block holding its place reaches 12 blocks (its soldiers step out and return); one that has broken formation
    must close to 2.5.
  - Each fighting soldier does 0.12 damage a second (cavalry 0.36, archers in melee 0.088), dealt in hits of 4 to
    random soldiers, so each soldier's health is kept. Archers shoot 44 blocks, an arrow a second doing 0.19 on
    average, until their 64 arrows each run out.
  - A cavalry charge does half as much again for its first 3 s; a shieldwall takes 60 % from the front, and 40 % of
    arrows. A warrior general fights like 4 soldiers.
  - **Calibration** (`battle/Calibration`): every live battle measures, each second, the health each block lost and
    who was striking it (by the numeric battle's own reach rules), how fast blocks walk and how much health soldiers
    start with, and fits a damage rate per kind of soldier. At its end it reports the fit (for the battle and all
    battles since the server started) and, beside it, how the same battle came out fought as numbers. Over 12 live
    battles of 100 to 500 a side the fits settled near: infantry 0.15 to 0.2 damage a second each, cavalry about 0.65,
    archers in melee about 0.2, arrows about 0.18 an archer a second (Recruits bowmen shoot about once a second, out to
    44 blocks), starting health 21. Recruits deals a blow the same from any side, so the numeric battle has no flank
    or rear bonus any more. Walking speeds aren't measured well yet.
  - **How well it matches:** a battle's length and its winner are in the right range, but not reliably. At 300 a side
    live battles lasted 340 to 770 s against about 310 s as numbers; at 100 a side about 190 s against about 355 s.
    As numbers a warrior general beats a tactician almost every time, while live the warrior won 3 of 5 at 300 a side
    and the tactician won all 3 at 100. Live battles also vary much more.

  `/pve debug battle <first> <soldiers> <second> <soldiers> [warrior|tactician] [warrior|tactician]` picks each
  army's general, for calibration.

  `/pve debug battle simulate <first> <soldiers> <second> <soldiers>` fights one at once and reports the outcome.
  2000 against 2000 lasts several minutes of battle, with dozens of blocks relieved on each side, and takes about
  half a second to work out.
- **Seeing the whole field:** the game stops drawing a soldier about 67 blocks away (a horse at about 94, both scaled
  by the Entity Distance video setting). Beyond that, each soldier is drawn as a flat figure turned to face you, in
  the uniform's colour: leaders with a flag in the banner's colour, the general with a gold helmet and a flag on the
  back, the standard-bearer with a flag on a pole, cavalry on a horse in the army's colour
  (`client/DistantArmyRenderer`). Once a second the server sends each player a summary of every formation (where it
  stands, which way it faces, how wide and deep it is, its soldiers' entity ids; `battle/FormationSnapshot`). A
  soldier the client still has (out to about its render distance) is drawn exactly where it stands, so nothing jumps
  when the real soldier gives way to its figure. Further out, the client lays the soldiers out in ranks across the
  formation's space (scattered a little once it is fighting) and glides each formation to its newest place over the
  next second.
- **The handover:** with Distant Horizons, the game's world fades into its terrain near the render distance, and it
  would fade real soldiers with it (from 1.5 times its near clip distance plus a chunk; about 45 blocks at 6 chunks).
  So real soldiers hand over to figures where that fade starts, if it is nearer than 67 blocks, and past the handover
  the real soldiers and their horses are hidden. The figures are drawn after the fade, so they stay solid. The camera
  sees only 4 render distances; figures further away are pulled in along the line of sight and shrunk to match, so
  they look the same on screen (`battle/DistantDepth`). Without Distant Horizons there is no terrain beyond the render
  distance, so figures out there stand over empty sky.
- **The battle keeps its own ground loaded:** the chunks around every living soldier stay loaded and running, the way
  `/forceload` does, while the battle lasts (not saved with the world). So a player can fly far off to watch, and the
  battle carries on instead of unloading, which would freeze its soldiers and count them as fallen. A server with no
  players at all still stops running entities after 15 s, as vanilla does.
- **With Distant Horizons** (optional, recommended): soldiers beyond the render distance are drawn inside its terrain
  instead, as little figures of coloured boxes (uniform, face and cap, the leaders' and general's flags, the general's
  gold helmet, cavalry on armoured horses), so its far hills hide them and they stand on its ground. They are rebuilt
  ten times a second through its generic rendering API (`client/DistantHorizonsArmies`, the only class that touches
  it). Nearer soldiers keep the flat figures. If Distant Horizons is missing, its rendering is off, or its "generic
  rendering" option is off, the flat figures cover the whole field as before.
- **Measured** on the dev server with no client connected, 200 soldiers on flat ground: about 10 ms per tick on
  average, worst ticks 35–50 ms, and a steady 20 TPS. Battles ended in a rout after 60–100 s. On a small island,
  cavalry that charged into the sea got stuck, so fight on open land.

## Faction Info and the Command Table

- **Faction Info button:** Recruits' faction menu (its faction key, or the Command Table) has a third button,
  **Faction Info** (`client/ClientEvents`). It opens a screen showing one faction at a time, nearest first. Use
  ◀ ▶ to switch faction and ✕ to go back. Each faction shows its banner and name, then a scrolling box with:
  - type, strength (claim radius, 1–10; City-states claim none) and military strength (soldier pool, of 400,000);
  - capital (or a City-state's home city) and how far away it is;
  - structures owned, with a count per type (capitals, cities, farms) and every one's location.
  The screen asks the server for the data when it opens (`net/ConquestNetwork`, `net/FactionInfo`; at most the 64
  nearest known factions). It shows factions in precomputed regions, so the far outer world fills in as players
  explore.
- **Command Table** (`rafasconquest:command_table`): a lectern with an open book. Using it opens Recruits' faction menu,
  Faction Info button included. It faces whoever placed it, breaks with an axe, and drops itself. It has no crafting
  recipe yet; it is in the mod's own creative tab, **Rafa's Conquest**.

## World map

Recruits' faction menu has a **World Map** button beside Faction Info (`client/map/FactionMapScreen`). It is a
political map in the style of Crusader Kings 3, out to twice the storm wall's radius (8,000 blocks at default settings),
all known from the start. Drag to pan and scroll to zoom. The legend is bottom left and the cursor's coordinates are
bottom right. Double-click a faction's name to open its Faction Info, and ✕ (or Esc) goes back.

It is drawn as layers, bottom to top, each its own class, listed in order in `FactionMapScreen.layers`:

1. **Base map** (`BaseMapLayer`): the world, washed to parchment with its relief and sea depth still faint. Beyond the
   known world is unexplored.
2. **Realms** (`TerritoryLayer`): each realm fills the provinces it holds in its own colour.
   - Thin lines run between the provinces.
   - A dark line runs round the realm, coasts included, with a soft shadow inside it.
   - Unclaimed provinces get faint lines only.
   - The storm wall is a dashed ring drawn on top.
3. **Cities and POIs** (`PoiIconLayer`): a capital is its banner in a gold frame, a City-state its banner in a silver
   frame, and a Farm a wheat sheaf once zoomed in.
4. **Paths and roads** (`RoadLayer`): empty for now; it holds the place for roads.
5. **Armies and moving things** (`UnitLayer`): you as an arrow, and other loaded players.
6. **Names and labels** (`LabelLayer`): an Empire's name in capitals across its realm.
   - The name is turned along the way the realm runs (up to 60°) and letter-spaced to stretch along it.
   - Letters never grow past 1.2× normal text size.
   - A City-state's name sits small under its city.

Each layer adds its own rows to the legend.

**Provinces** (`core/ProvinceMap`) split the land like CK3's counties:
- **Centres:** they sit on a seeded, jittered grid every 384 blocks. Deserts, badlands, snow and peaks drop half of
  theirs, so provinces are larger there. Every city (City-state home or Empire capital) is a centre itself.
- **Shapes:** each land pixel joins the centre that is cheapest to reach in a straight line. The sea costs a lot (coasts
  become borders, and islands get their own provinces). A ridge rising above both ends of the path costs (mountain
  ranges become borders). Crossing a biome edge costs a little (borders lean onto rivers and biome edges).
- **Smoothing:** two passes give each pixel the province most of its neighbours have, which clears ragged edges.
- **Tiles:** everything a pixel looks at is within 1,152 blocks of it, so any area computed with that margin matches
  the whole map. That allows tiled provinces beyond the map later.

**Who holds a province** (`map/PoliticalMap`) is whoever holds its centre:
- the City-state, within 400 blocks of its city;
- otherwise the Empire that would own a Farm there (`core/TerritoryMap`, using `OwnershipResolver`).

**Farms belong to their province's holder** (`PoiPrecomputer.farmOwner`), so the map and the economy always agree, and
a City-state can hold Farms in its provinces:
- **Before the map is ready** (the first ~20 s of a new world), and for Farms off the map's edge, the Farm's own
  position stands in for its province's centre.
- **Once the map is ready**, every Farm is re-judged by its province. On seed 12345, 14 of 317 Farms changed hands,
  and none change after that.
- `gradlew mapPreview` reports any Farm whose owner disagrees with the map (0 on seed 12345).

**Colours:** a realm's colour starts from its banner's main colour, softened to a map tone and varied a little per
faction. Where two neighbouring realms would look alike, one takes a lighter, darker or hue-shifted shade instead
(`map/PoliticalStyle`).

The **base map and provinces** are drawn by the server from the seed alone (`map/BaseMapGenerator`), so nothing is
generated or loaded:
- Terrain height comes from the noise generator every 64 blocks, smoothed between samples.
- Colour comes from the surface biome (`map/BiomePalette`).
- At default settings it is 1,000 × 1,000 pixels of 16 blocks with about 880 provinces, drawn in about 21 s on a
  background thread when the server first starts.
- It is kept in `data/rafasconquest_basemap.bin` in the world folder (about 1 MB) and sent to clients compressed, in
  parts.

The client builds the political layers from it and the faction data. `gradlew mapPreview -Pworld=run/<world>
-Pout=<png>` renders the same political map for a dev world, without the game.

## Cities (City-states and Empire capitals)

A city is a planned, square settlement built from vanilla village buildings, 3× as wide as a village. City-state
cities each spawn a `CITY_STATE` faction (no territory claims), tracked as a `CITY` POI. **Every Empire's capital is a
city too**, centred on its Capital site and tracked as its `CAPITAL` POI. A capital takes its style from its biome
like any city, and falls back to plains where no style matches.

**No cities or farms on ocean:** no part of a city's flattened box, and no part of a farm's plot, may lie on a biome
in the tag `rafasconquest:no_settlement` (all oceans; packs can add more). Biomes are sampled at sea level, every 16
blocks over a city and at the corners and centre of a farm (`worldgen/SettlementBiomes`). A City-state site that fails
this has no City-state.

**Capitals move to land:** a Capital site whose city would touch ocean moves to the nearest spot, searched every 32
blocks up to 1,024 away, where the whole city fits on land. The new spot also stays out of the eye and keeps a city's
width clear of City-state sites and other Capitals (`CapitalLayout.relocated`). Everything that looks Capitals up
(placement, walls, banners, guards, farm spacing, ownership) gets the moved site. City-states keep their distance from
the seeded sites, so the two layouts don't depend on each other. The search needs the world's biomes, published when
the overworld loads, and runs once per site, on first use. On seed 12345 it adds about 4.5 s to the first startup, and
all 6 storm-wall Empires keep their capitals (4 of them moved). Only a site with no land within 1,024 blocks loses its
Empire.

- **Grid:** a 483 × 483 square. 3-wide roads run every 32 blocks in both directions, with an edge road all round,
  making 15 × 15 city blocks. Each block is split into four 14 × 14 plots. The middle block holds a vanilla town centre
  (well or meeting point).
- **Buildings:** every plot gets a building from the vanilla village houses pool of the city's style, turned so its
  entrance faces a road and placed against it. Villagers, cats and other add-ons attach through the building's own
  jigsaws, as in vanilla. Zombie variants are skipped.
- **Terrain:** before anything is built, the land is reshaped (`core/CityTerrain`, `worldgen/CityTerrainPiece`), all
  during world generation:
  - The **box** is the city square plus the wall's 2-block outset plus 3 blocks past the wall. The **plate** is the median ground height over the box,
    never below sea level. Inside the box everything above the plate is cleared and everything below is filled (valleys,
    rivers and lakes included), with grass or sand on top and dirt or sandstone beneath.
  - Outside the box, out to 42 blocks (the furthest a cell's structure start can reach), each 32-block stretch of the
    edge is one of:
    - **Carve** (land level or rising): land above a slope rising from the plate is removed. The slope is 1 up per 2
      out, steepened where needed so it meets the land at the end of its reach, so the carve never ends in a wall. How
      steep blends smoothly along the edge; corners follow the steeper side. The carve only removes blocks; dips on a
      rising stretch are left natural rather than built up.
    - **Fill** (land falling away within reach): a 1-up-2-out slope of stone, topped with the biome's own surface
      block, eases the plate down into the lower land. Bumps above the slope are carved.
    - **Valley** (land falling away beyond reach): no fill and no ledge. The wall stands on the edge, plain stone runs
      under it down to the natural ground, and the cliff below is left as it is.
  - The plan is computed once per city from the original terrain (about 2 s, on a worldgen thread) and logged.
    `/pve debug cityterrain` prints the plan of the nearest city: one letter per edge stretch (C carve, F fill,
    V valley) and how far above the plate each stretch's carve reaches. `/pve debug column x z` prints the ground
    height at a column.
- **Lighting:** lamp posts from the style's vanilla village decor stand in every city block's alleys: where each alley
  meets the road (lighting the streets about every 15 blocks) and where the alleys cross, about 1,100–1,300 per city.
  A style's own lamp is used where it fits without touching a building, otherwise the 1-wide taiga lamp post.
- **No hostile spawns:** the city structures' `spawn_overrides` give hostile mobs an empty spawn list inside every city
  piece: the whole city and its slopes, from 16 blocks below the plate upward. Caves deeper under the city keep their
  mobs.
  - Roads, houses, the town centre and the walls all sit on the plate; the walls are rigid rather than terrain-following.
- **Cells:** the square is cut into 3 × 3 cells of 5 × 5 blocks. Each cell is its own structure start (`rafasconquest:city`
  in the middle, `rafasconquest:city_district` around it), because Minecraft only places pieces within about 128 blocks
  of a structure's start chunk. Every cell builds its share of the same grid, so they meet seamlessly and all 9 always
  generate together.
- **Wall:** a 5-wide wall runs all the way round on the outermost road band and the 2 blocks outside it, so the city
  inside is unchanged. It is built from 32 × 8 × 5 jigsaw segments (one street length each; templates
  `structures/city/wall/segment.nbt`, `gate.nbt` and `corner.nbt`, regenerated with `gradlew generateWallTemplates`).
  Each side is 15 segments laid as a pinwheel. The first segment of each side is the 34-long corner, which also covers
  the corner the wall makes past the city square, so the ring closes exactly. The first segment of each side in a cell
  is placed from the layout, and each next one is joined through its `wall_end` → `wall_start` jigsaws. Segments stand
  rigid on the plate. One gate per side (3 wide, 4 high, through the full width) sits where the avenue next to the
  town centre meets the wall. Material comes from the pool's processor list: weathered stone bricks, or cut sandstone
  for desert. Some south and west segments reach 3 blocks into the next cell, so a cell's terrain never removes wall
  blocks, whichever is laid first.
  Houses never face the wall; the 4 corner plots face the alley between plots instead.
- **Banners:** each segment carries 2 standing banners on top of its outer edge, at the quarter points. When a wall
  chunk loads (including its first load after generation), its banners are painted in the city owner's design: the
  City-state for its home city, the Empire for its capital. If a city changes hands later, the banners follow on the
  next load. Each faction's
  design is a base colour plus two pattern layers derived from its id (`faction/FactionBanner`), and
  `/pve faction info` shows it.
- **Wall guards:** every wall segment has 3 Recruits bowmen on its walkway (180 per city), evenly spread and clear of
  the banners, facing out and told to hold their positions. It belongs to the city owner's Recruits faction (team and colour) and is set up the
  way Recruits sets up its own patrol bowmen (chainmail, a bow, 64 arrows, never despawns). Guards spawn on the server
  thread the first time a wall chunk loads, since joining a faction's team can't happen during world generation. Each
  post is recorded in save data, so each guard spawns once: reloading doesn't duplicate it, and a guard that dies isn't
  replaced. They aren't drawn from the faction's soldier pool (strength).
- **Style:** picked from the biome at the city centre via the tags `rafasconquest:city/<plains|desert|savanna|snowy|taiga>`.
- **Measured** with `/pve debug citystats 12` against vanilla villages at the same sites: every city exactly square,
  99.5% of plots built (all empty ones over deep water), **6× buildings per area, 3.14× width**, about 890 buildings
  per city.
- **Placement:** City-state cities: one per 3,000-block grid cell, at least 400 blocks inside the cell, outside the eye,
  and a full city width clear of capital cities. Capital cities sit on the Capital sites. Farms are never placed inside
  any city.
- **Old Capital structure:** the stone-keep placeholder Capital is gone. Worlds that already generated one will log an
  unknown-structure error for it on load.
- **Cost:** force-loading a capital city with its slopes (580 × 580 blocks) took 34 s; before flattening, the city alone
  (488 × 488) took 28 s and the same area of plain terrain 22 s.
- **Villagers:** some villagers suffocate standing on beds or job blocks in vanilla's low snowy houses. Vanilla snowy
  villages do the same: 10 of them lost 12 of 122 villagers in 3 minutes, against 21 of 428 in a snowy capital city.
- **Older worlds:** worlds created before cities existed load with cities disabled, since their settings are frozen.

Grid sizes are constants in `core/CityLayout`; the builder is `worldgen/CityGridBuilder`. `/pve debug citystats [samples]`
runs in the background and writes its results to the server log.

## Decisions the design doc left open

- **"Spawn" means the world origin (0, 0).** The Forge `LevelEvent.Load` that freezes settings fires before vanilla picks the
  spawn point, and `/setworldspawn` can move spawn later. The origin keeps every zone deterministic from the seed alone.
- **Placement reads the frozen settings, not JSON numbers.** Farms and Capitals use custom placement types
  (`rafasconquest:farm_spread`, `rafasconquest:capital_sites`) that read the world's frozen `WorldSettings`. The precompute uses the
  same math and calls the structure's own `findValidGenerationPoint` (height + biome check), so predicted and real
  positions agree by construction. `/locate` does not support these placement types; use `/pve poi list`.
- **Outer-world Capitals** sit at a seeded spot inside their 8,000-block cell, at least `outerCellMargin` (1,000) blocks from
  the cell edges, and are dropped if that spot is within R2.
- **`/pve faction list` shows every known faction**, not just the storm wall. Farms near the 8,000-block precompute edge need every
  Capital within 10,000 blocks, so outer-world Empires are discovered too. Each line is labelled with its zone.
- **Offline catch-up** is read when the overworld loads (before anything can save). If the server stops mid catch-up, the
  remaining days are dropped, because the Milestone 1 data model has no field for them.
- **`/time set` backwards** resyncs the day counter without paying. The next sunrise after that is a new crossing and pays once.
- **City-states** roll strength 300–800 (config `cities.strengthMin/Max`) and their city produces 0 food per day
  (config `cities.dailyOutput`), pending the design doc's open questions on thresholds and per-POI output.

## Layout

- `core/`: pure logic (seed hashing, layouts, distance scaling, ownership, day math), unit tested.
- `data/`, `faction/`, `poi/`: the `rafasconquest_world` SavedData and its records.
- `worldgen/`: structure and placement types, placeholder pieces, settings published to worldgen threads.
- `sim/`: precompute, daily economy tick, server tick loop.
- `bridge/RecruitsBridge`: the only place future Recruits API calls will go (no-op in Milestone 1).
