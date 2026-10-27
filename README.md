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

## Milestone 1 status

Built: one Empire per Capital, Farms producing food, virtual POIs, ownership, the daily food tick, offline catch-up and debug commands.
Not built (by design): battles, recruit spawning, captures, raids, City-states, Bandits, growth, resources other than food.

Debug commands (permission level 2): `/pve faction list`, `/pve faction info <name|nearest>`, `/pve poi list [radius]`,
`/pve debug advance <days>`, `/pve debug recruits`, `/pve debug citystats [samples]`.

## Recruits integration

Every faction this mod creates is also registered as a Villager Recruits faction, so it appears in the Recruits faction
menu. Registration happens as factions are discovered, plus a catch-up pass when the server starts. Each one is a
scoreboard team (Recruits factions are teams) in the faction's colour, plus a Recruits faction record holding the
faction's name and banner. These factions have no player leader. Their leader id is the faction's id, which marks them
as ours, and their leader name is the faction type ("Empire", "City-state"). Recruits saves them with the world.
All Recruits calls live in `bridge/RecruitsFactions`, behind the `RecruitsBridge` interface. `/pve debug recruits` lists
what Recruits holds.

## Cities (City-states and Empire capitals)

A city is a planned, square settlement built from vanilla village buildings, 3× as wide as a village. City-state
cities each spawn a `CITY_STATE` faction (no territory claims), tracked as a `CITY` POI. **Every Empire's capital is a
city too**, centred on its Capital site and tracked as its `CAPITAL` POI. A capital takes its style from its biome
like any city, and falls back to plains where no style matches, so every Empire always has its capital.

- **Grid:** a 483 × 483 square. 3-wide roads run every 32 blocks in both directions, with an edge road all round,
  making 15 × 15 city blocks. Each block is split into four 14 × 14 plots. The middle block holds a vanilla town centre
  (well or meeting point).
- **Buildings:** every plot gets a building from the vanilla village houses pool of the city's style, turned so its
  entrance faces a road and placed against it. Villagers, cats and other add-ons attach through the building's own
  jigsaws, as in vanilla. Zombie variants are skipped.
- **Water:** shallow water (up to 24 deep) inside the square is filled in with earth before building, so rivers and
  lakes don't leave holes. Over deeper water, roads become plank bridges and plots stay empty.
- **Cells:** the square is cut into 3 × 3 cells of 5 × 5 blocks. Each cell is its own structure start (`rafasconquest:city`
  in the middle, `rafasconquest:city_district` around it), because Minecraft only places pieces within about 128 blocks
  of a structure's start chunk. Every cell builds its share of the same grid, so they meet seamlessly and all 9 always
  generate together.
- **Wall:** a 3-wide wall stands on the outermost road band all the way round, built from 32 × 16 × 3 jigsaw segments
  (one street length each; templates `structures/city/wall/segment.nbt` and `gate.nbt`, regenerated with
  `gradlew generateWallTemplates`). Each side is 15 segments laid as a pinwheel, so the ring closes exactly. The first
  segment of each side in a cell is placed from the layout, and each next one is joined through its `wall_end` →
  `wall_start` jigsaws. Segments follow the terrain. One gate per side (3 wide, 8 high) sits where the avenue next to
  the town centre meets the wall. Material comes from the pool's processor list: weathered stone bricks, or cut sandstone for desert.
  Houses never face the wall; the 4 corner plots face the alley between plots instead.
- **Banners:** each segment carries 2 standing banners on top of its outer edge, at the quarter points. When a wall
  chunk loads (including its first load after generation), its banners are painted in the city owner's design: the
  City-state for its home city, the Empire for its capital. If a city changes hands later, the banners follow on the
  next load. Each faction's
  design is a base colour plus two pattern layers derived from its id (`faction/FactionBanner`), and
  `/pve faction info` shows it.
- **Style:** picked from the biome at the city centre via the tags `rafasconquest:city/<plains|desert|savanna|snowy|taiga>`.
- **Measured** with `/pve debug citystats 12` against vanilla villages at the same sites: every city exactly square,
  99.5% of plots built (all empty ones over deep water), **6× buildings per area, 3.14× width**, about 890 buildings
  per city.
- **Placement:** City-state cities: one per 3,000-block grid cell, at least 400 blocks inside the cell, outside the eye,
  and a full city width clear of capital cities. Capital cities sit on the Capital sites. Farms are never placed inside
  any city.
- **Old Capital structure:** the stone-keep placeholder Capital is gone. Worlds that already generated one will log an
  unknown-structure error for it on load.
- **Cost:** generating a city's area takes about 1.3× the chunk-generation time of plain terrain (force-loading a
  city's area took 28 s, the same area of plain terrain 22 s).
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
