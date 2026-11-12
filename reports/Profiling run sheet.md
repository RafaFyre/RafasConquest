# Profiling run sheet (baseline)

Machine: 16 GB RAM, 16 CPU threads. Instance: RafasConquest1.21Neo, 4 GB heap, jar `rafasconquestPREALPHA-0.1.0.63cd2e9`.

## Before you start
1. Put **spark** for NeoForge 1.21.1 in the instance's `mods` folder (from Modrinth or CurseForge).
2. Close everything else Java: no `runClient` or `runServer`, no Gradle. Check with `jps -l`: only the game should be listed.
3. Don't change `recruits-server.toml` yet. The baseline has to be taken with 1 pathfinding thread, so the change to 4 can be measured afterwards.
4. Copy one world as `PerfBase`. For each run, copy `PerfBase` to a fresh save so every run starts from the same world.

## Each scenario
Do each scenario 3 times. Ignore the first minute of each run. Paste the spark links into the table.

| # | Scenario | What to do | Commands |
|---|---|---|---|
| 1 | Large land battle | Battle with about 300 live soldiers, sea lanes in view | `/spark profiler start --comment "s1 63cd2e9"`, wait 60 s, `/spark profiler stop`; then `/sparkc profiler start` and the same for the client |
| 2 | Siege start | Stand within 400 blocks of a capital as the siege starts | `/spark tickmonitor --threshold-tick 50` running, plus `/spark profiler start --only-ticks-over 100` |
| 3 | Coast | Stand at the sea for 2 min, then sail 500 blocks | `/sparkc profiler start --comment "s3"`; watch F3+2 for spikes |
| 4 | Map screens | Open the Faction map and the Grand Strategy map, zoomed in, pan around for 30 s | `/sparkc profiler start --comment "s4"` |
| 5 | Exploration | Fly into land you haven't been to for 2 min | `/spark tickmonitor --threshold-tick 50` |
| 6 | Day and year turn | `/time add` past a day, then past a year | `/spark tickmonitor --threshold-tick 50` |

Also write down `/spark health` (MSPT median, p95, max) after scenarios 1 and 2.

## JFR (allocations; spark can't record them on Windows)
For scenario 1 and scenario 3:
`/jfr start`, play the scenario, then `/jfr stop`. The file lands in `debug/`. Send me the path and I'll read it with `jfr view hot-methods` and `jfr view allocation-by-site`.

## Results
| # | spark server link | spark client link | MSPT median / p95 / max | Notes |
|---|---|---|---|---|
| 1 | | | | |
| 2 | | | | |
| 3 | | | | |
| 4 | | | | |
| 5 | | | | |
| 6 | | | | |
