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

Debug commands (permission level 2): `/pve faction list`, `/pve faction info <name|nearest>`, `/pve poi list [radius]`, `/pve debug advance <days>`.

## Cities (City-state origins)

A city is a vanilla-village-style settlement 3× as wide as a village and about 2× as dense. Each one spawns a
`CITY_STATE` faction (no territory claims) and is tracked as a `CITY` POI.

- **Layout:** a 3 × 3 grid of 160-block cells (480 blocks across). The middle cell is the town centre
  (`rafasconquest:city`); each of the 8 outer cells is a district (`rafasconquest:city_district`). Each cell is its own
  structure start, because Minecraft only places pieces within about 128 blocks of a structure's start chunk.
  Districts over ocean, river or beach are skipped.
- **Style:** picked from the biome at the city centre via the tags `rafasconquest:city/<plains|desert|savanna|snowy|taiga>`.
- **Density:** the city pools (`data/rafasconquest/worldgen/template_pool/city/`) reuse the vanilla village templates.
  Custom pool elements (`rafasconquest:city_legacy_single`, `rafasconquest:city_single`) redirect their jigsaws to the city's
  street and house pools. City pools drop vanilla's "empty" house picks and zombie variants and triple crossroad and
  square weights. Measured with `/pve debug citystats 12` against vanilla villages at the same sites:
  **2.13× buildings per area, 3.14× width**.
- **Placement:** one city per 3,000-block grid cell, at least 400 blocks inside the cell, outside the eye, and clear of
  Capitals. Farms are never placed inside a city.
- **Cost:** generating a city's area takes about 2× the chunk-generation time of plain terrain (measured by force-loading
  a 480 × 480 area with and without a city).
- **Older worlds:** worlds created before cities existed load with cities disabled, since their settings are frozen.

To change city pool weights, edit `dev/PoolExporter`, run `/pve debug exportpools` on a dev server, copy
`run/rafasconquest-export/city/data` into `src/main/resources/data`, then check the result with `/pve debug citystats`
(it runs in the background and writes results to the server log).

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
