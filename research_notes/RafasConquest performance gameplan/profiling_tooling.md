# Profiling, measuring and benchmarking a NeoForge 1.21.1 mod (RafasConquest)

Scope: tooling and method for server (TPS/MSPT), client (FPS, frame time, stutter, GPU) and memory, for Minecraft 1.21.1 / NeoForge 21.1.x / Java 21 on a memory-constrained Windows 10 PC. Researched 2026-11-10. Anything Fabric-only or version-specific is flagged.

## 1. spark on NeoForge 1.21.1: modes, flags, health, memory, viewer, client vs server

### Takeaway
spark (1.10.x for NeoForge 1.21.1) is the first tool to reach for: `/spark profiler` for the integrated or dedicated server, `/sparkc profiler` for the client, `--only-ticks-over` plus `/spark tickmonitor` for spikes, and the viewer's Sources view for a per-mod breakdown. On Windows spark cannot use its async-profiler engine, so it falls back to the Java sampler, which has safepoint bias. Its allocation mode (`--alloc`) depends on async-profiler, so it is very likely unavailable on Windows. Use JFR for allocations there.

### Cited Findings
**Versions**
- spark builds for NeoForge 1.21.1 include 1.10.97, 1.10.109 and 1.10.124. The 1.10.124 build is described as "a backport release to bring some bug fixes to 1.21.1". The full changelog is at spark.lucko.me/changelog. — [Modrinth spark 1.10.124](https://modrinth.com/project/l6YH9Als/version/v5qtqRQi); [Modrinth spark 1.10.109](https://modrinth.com/project/l6YH9Als/version/jg1XqG66); [boxtoplay listing](https://www.boxtoplay.com/en/minecraft-hosting/minecraft-server/modrinth-mods/spark-46?page=5)

**Profiler commands** (permission `spark` or `spark.profiler`)
- `/spark profiler start`, `/spark profiler stop`, `/spark profiler open` (opens the viewer without stopping), `/spark profiler cancel` (stops without uploading), `/spark profiler info`. — [spark Command Usage](https://spark.lucko.me/docs/Command-Usage)
- Start flags:
  - `--timeout <seconds>`
  - `--thread *` (all threads), `--thread <thread name>`, `--thread <pattern> --regex`
  - `--alloc`: profiles memory allocations instead of CPU. Add `--alloc-live-only` to keep only objects still alive at the end. With `--alloc`, `--interval <bytes>` sets the sampling rate (default 524287, about 512 KB).
  - `--interval <milliseconds>`: sampling interval, default 4 ms.
  - `--only-ticks-over <milliseconds>`
  - `--combine-all`: groups all threads under one root.
  - `--not-combined`: turns off thread-pool grouping.
  - `--ignore-sleeping`
  - `--force-java-sampler`

  — [spark Command Usage](https://spark.lucko.me/docs/Command-Usage)
- Stop flags: `--comment <comment>` (shown in the viewer) and `--save-to-file` (saves to the config directory instead of uploading). — [spark Command Usage](https://spark.lucko.me/docs/Command-Usage)
- `/sparkc` replaces `/spark` on Forge and Fabric **client** installations. — [spark Command Usage](https://spark.lucko.me/docs/Command-Usage)

**Engines and the Windows limitation**
- spark has two engines: a Java engine based on WarmRoast and an async-profiler engine. The async-profiler engine is "more accurate because it avoids safe-point sampling bias" and is used automatically when the system supports it. It is supported only on "a Linux or macOS operating system using x86_64 or aarch64". Windows is not listed. — [spark: Using async-profiler](https://spark.lucko.me/docs/misc/Using-async-profiler)
- The allocation profiler mode needs HotSpot debug symbols, which Java 11+ includes by default. — [spark: Using async-profiler](https://spark.lucko.me/docs/misc/Using-async-profiler)
- A Minecraft hosting forum post reports spark printing that the async profiler only works on Linux. — summarised in search results; the original post was not opened, so treat it as weak corroboration.

**Spike hunting**
- `/spark tickmonitor` learns the average tick, then reports ticks over the threshold in chat. The default threshold is 100% over the average. `--threshold-tick 50` sets an absolute threshold in ms; the guide calls 50 ms "the point at which the server has to start catching up, or lagging". Then run `/spark profiler --only-ticks-over <ms>`, using a value below the spike duration. The guide suggests 50–100 and used 150 for 300 ms spikes. Normal profiles average away spikes. — [spark: Finding lag spikes](https://spark.lucko.me/docs/guides/Finding-lag-spikes)
- Other tickmonitor flags: `--threshold <percent>`, `--threshold-tick <ms>`, `--without-gc`. — [spark Command Usage](https://spark.lucko.me/docs/Command-Usage)

**Health and TPS/MSPT**
- `/spark health`: live dashboard refreshed every 10 s. `/spark health upload` uploads a static report. `/spark health show [--memory] [--network]` prints to the console. `/spark tps` shows TPS and CPU. `/spark ping [--player <name>]`. — [spark Command Usage](https://spark.lucko.me/docs/Command-Usage)
- spark reports TPS and MSPT, with MSPT given as min, median, 95th percentile and max. Target 20 TPS, which needs an average of 50 ms per tick or less. Output is coloured green, amber or red. — [spark: TPS and MSPT](https://spark.lucko.me/docs/guides/TPS-and-MSPT)
- Metric sources:
  - TPS and MSPT come from spark's TickHook and TickReporter.
  - CPU comes from `OperatingSystemMXBean`.
  - GC comes from `GarbageCollectorMXBean`.
  - Memory and network partly read `/proc`, which is Linux only.
  - CPU and OS name come from `wmic` on Windows.
  - The metrics page does not mention client FPS or frame-time metrics.

  — [spark: About spark metrics](https://spark.lucko.me/docs/misc/About-spark-metrics)

**Memory commands**
- `/spark gc`: GC history.
- `/spark gcmonitor`: toggles GC notifications.
- `/spark heapsummary`: uploads a class histogram to the viewer. `--run-gc-before` is deprecated.
- `/spark heapdump`: writes an `.hprof`. `--compress gzip|xz|lzma` compresses it. `--include-non-live` and `--run-gc-before` are deprecated.

— [spark Command Usage](https://spark.lucko.me/docs/Command-Usage)

**Viewer**
- Call tree: the Server thread is usually the one to check. Nodes show their percentage, with milliseconds on hover, and ⓘ info points explain well-known frames. — [spark: Using the viewer](https://spark.lucko.me/docs/Using-the-viewer)
- Flat view: "the slowest 250 method calls are listed at the top level". It has Top Down and Bottom Up modes, sorted by Total Time or Self Time. — [spark: Using the viewer](https://spark.lucko.me/docs/Using-the-viewer)
- Sources view: "a separate profiler tree will be shown for each plugin/mod", filtered to that source's outgoing calls. It has Merge and Separate modes. — [spark: Using the viewer](https://spark.lucko.me/docs/Using-the-viewer)
- Deobfuscation mappings are detected automatically, with a manual dropdown as fallback. — [spark: Using the viewer](https://spark.lucko.me/docs/Using-the-viewer)
- Bookmarks (alt+click) are encoded in the URL, which makes them useful when sharing before and after links. There is also a flame-graph mode. — [spark: Using the viewer](https://spark.lucko.me/docs/Using-the-viewer)

### Inferences
- **Which command profiles what:**
  - In singleplayer, `/spark ...` targets the integrated server, which is where battles, worldgen and simulation tick.
  - `/sparkc ...` targets the client JVM: Render thread, map screens, sea swell, DH integration.
  - Since both live in one JVM, `/sparkc profiler start --thread *` captures client and integrated-server threads in a single profile.
  - Name threads explicitly to cut noise. Vanilla thread names are "Server thread", "Render thread" and "Worker-Main-N".

  These thread names come from general Minecraft knowledge, not a fetched source.
- **Windows limits:**
  - On Windows the Java sampler's safepoint bias can misattribute time inside tight loops, such as pathfinding or physics.
  - Treat small percentages with caution, and confirm hot spots with JFR, which samples without safepoint bias.
  - For allocations on Windows, use JFR's allocation events rather than `spark --alloc`.
- **Mod-specific checks:**
  - Use `--interval 1` or `2` only for short captures, because a finer interval inflates profile size and overhead.
  - Thread pools your mod creates (city or road generation, the living-world simulation) will show up merged unless you pass `--not-combined`.
  - Give them distinctive thread names so `--thread <name>` and the Sources view isolate them.

### Gaps
- No spark doc page was found stating explicitly what `--alloc` does on Windows (whether it errors or falls back). Test it in game.
- The spark changelog was not opened. I did not verify what changed between 1.10.97, 1.10.109 and 1.10.124, or whether newer viewer features such as the time-range or graph widgets apply.
- The Viewer docs fetched did not describe the viewer's TPS/MSPT/CPU/memory graph widgets or time-range selection. Check them in the live viewer.
- I could not confirm the 5s/10s/1m/5m/15m windows of `/spark tps` from the fetched pages.

## 2. Vanilla and NeoForge built-ins (debug charts, F3+L, /debug, /perf, /jfr, /tick, /neoforge)

### Takeaway
1.21.1 has everything needed for cheap first-pass numbers without extra mods:
- F3+2 for frame-time and tick-time charts. The tick chart is stacked by phase.
- F3+L for a 10 s profile in singleplayer (client and server).
- `/tick query` for MSPT percentiles and `/tick sprint` for throughput benchmarks.
- `/jfr start|stop` for a JFR recording with Minecraft-specific events.
- `/neoforge track` and `/neoforge entity list` for per-entity and per-block-entity cost and counts.
- `/neoforge generate start` for built-in pregeneration.

`/perf` only works on a dedicated server.

### Cited Findings
**Debug charts**
- F3+1 opens the profiler pie chart. Press a number key to enter a category and 0 to go back; categories numbered above 9 can't be reached. — [Minecraft Wiki: Debug screen](https://minecraft.wiki/w/Debug_screen)
- F3+2 shows the FPS graph (left, last 240 frames) and the TPS graph (right, last 240 ticks). The TPS graph appears only in singleplayer or for operators. — [Minecraft Wiki: Debug screen](https://minecraft.wiki/w/Debug_screen)
- F3+3 shows the bandwidth graph (last 240 packets, KiB) and the ping graph. — [Minecraft Wiki: Debug screen](https://minecraft.wiki/w/Debug_screen)
- The frame-time chart shows min, avg and max, with bars on a green to yellow to red gradient. — [Minecraft Wiki: Debug screen](https://minecraft.wiki/w/Debug_screen)
- The tick-time chart is stacked. From top to bottom:
  - green/yellow/red: idle, waiting for the next tick
  - purple: miscellaneous
  - light brown: scheduled tasks
  - dark red: main server tick logic

  — [Minecraft Wiki: Debug screen](https://minecraft.wiki/w/Debug_screen)
- Version history: the hotkeys moved to F3+1/2/3 in 23w33a (1.20.2). The TPS graph was updated in 24w04a (1.20.5), so both apply to 1.21.1. A debug-text configuration menu arrived only in 25w31a (1.21.9), so it is not in 1.21.1. — [Minecraft Wiki: Debug screen](https://minecraft.wiki/w/Debug_screen)

**F3+L profiler**
- F3+L starts the profiler. It stops after 10 s or on a second press.
- The zip goes to `debug/profiling` in the run directory, named `yyyy-mm-dd_hh_mi_ss-WorldName-VersionNumber.zip`.
- The `client` and `server` folders each contain a `profiling.txt`. The header gives the duration in ms and the tick count.
- Lines read as depth, name, % of parent and % of whole tick, e.g. `[03] |   |   |   entities - 47.72%/45.72%`.
- Entities and BlockEntities are profiled out of the box.
- Custom sections: call `ProfilerFiller#push(name)` before the code and `ProfilerFiller#pop()` after it, getting the ProfilerFiller from `Level`, `MinecraftServer` or `Minecraft`.

— [NeoForge docs 1.21.1: Debug Profiler](https://docs.neoforged.net/docs/1.21.1/misc/debugprofiler)

**/perf** (dedicated server only)
- `/perf start` records for 10 s and `/perf stop` ends early. Permission level 4. "can only be executed on a dedicated server"; use F3+L locally.
- The zip goes to `debug/profiling`. It contains:
  - `system.txt` and `profiling.txt`
  - `server/metrics/` with `cpu.csv`, `jvm.csv` and `ticking.csv`
  - per-dimension entity, block-entity and chunk CSVs
  - `threads.txt`, `classpath.txt`, `gamerules.txt` and more

— [Minecraft Wiki: /perf](https://minecraft.wiki/w/Commands/perf)

**/debug**
- `/debug start` and `/debug stop` run a time profiler; stop returns the average TPS. `/debug function <name>` writes a `debug-trace-*.txt`. Permission level 3, and it works in singleplayer.
- The wiki lists `profile-results-*.txt` output only under removed content. "/debug report" was replaced by F3+L and `/perf`.

— [Minecraft Wiki: /debug](https://minecraft.wiki/w/Commands/debug)

**/jfr**
- `/jfr start` and `/jfr stop`, permission level 4, available since 1.18 (21w37a).
- Writes a `.jfr` file and a summary JSON to `.minecraft/debug`, and the summary also goes to the log.
- Custom events:
  - Network Packet Received, Network Packet Sent and Network Summary
  - Region File Read and Region File Write
  - Server Tick Time, sampled at 1 s
  - Chunk Generation, timed per stage
  - Create/Load World

— [Minecraft Wiki: /jfr](https://minecraft.wiki/w/Commands/jfr)

**/tick** (Java 1.20.3+, permission level 3, not usable from command blocks)
- `/tick query` reports status and target rate, "including average time per tick and percentiles of time per tick".
- `/tick rate <1.0–10000.0>`
- `/tick freeze` and `/tick unfreeze`
- `/tick step [<time>]`
- `/tick sprint [<time>]` runs as fast as possible for the given ticks, then "shows performance information about the sprint's tick times". `/tick sprint stop` ends it early.

— [Minecraft Wiki: /tick](https://minecraft.wiki/w/Commands/tick)

**/neoforge** (NeoForge 1.21.1)
- The root registers ten subcommands: TPSCommand, TrackCommand, EntityCommand, GenerateCommand, DimensionsCommand, ModListCommand, TagsCommand, DumpCommand, TimeSpeedCommand and DataComponentCommand. — [NeoForge 1.21.1 NeoForgeCommand.java](https://github.com/neoforged/NeoForge/blob/1.21.1/src/main/java/net/neoforged/neoforge/server/command/NeoForgeCommand.java)
- **track** (permission 2 for start and reset): `/neoforge track start entity <duration>`, `/neoforge track start blockentity <duration>`, `/neoforge track reset entity|blockentity`, then `/neoforge track entity` or `/neoforge track blockentity`. Results list the top 10 by average time with type ID, dimension, x/y/z and average. Values over 1000 µs are shown in ms, smaller ones in µs. — [NeoForge 1.21.1 TrackCommand.java](https://github.com/neoforged/NeoForge/blob/1.21.1/src/main/java/net/neoforged/neoforge/server/command/TrackCommand.java)
- **entity list** (permission 2): `/neoforge entity list [filter] [dim]`. The filter takes `*` and `?` wildcards on type IDs, e.g. `recruits:*`.
  - With one matching type, it prints per-chunk counts as `<count>: <chunkX>, <chunkZ>`, top 10.
  - With several types, it prints per-type counts.

  — [NeoForge 1.21.1 EntityCommand.java](https://github.com/neoforged/NeoForge/blob/1.21.1/src/main/java/net/neoforged/neoforge/server/command/EntityCommand.java)
- **generate** (permission 4): `/neoforge generate start <pos> <chunkRadius 1–2500> [progressBar]`, plus `/neoforge generate stop`, `status` and `help`. It has no dimension argument and runs in the command source's level. — [NeoForge 1.21.1 GenerateCommand.java](https://github.com/neoforged/NeoForge/blob/1.21.1/src/main/java/net/neoforged/neoforge/server/command/GenerateCommand.java)

**GL debug output**
- `options.txt` has `glDebugVerbosity` with values 0–4 (none, high, medium, low, notification), default 1. Messages appear in the log as "OpenGL debug message" entries with id, source, type and severity. — [Minecraft Wiki options.txt mirror](https://breezewiki.discard.no/minecraft/wiki/Options.txt); [MC-133140](https://bugs.mojang.com/browse/MC-133140)

### Inferences
- **Benchmarks with /tick sprint:**
  - `/tick sprint` is the cleanest built-in server throughput benchmark. Set up a fixed scenario, e.g. 300 Recruits mid-battle, run `/tick sprint 2000`, and compare the reported ms per tick between builds.
  - It removes the 50 ms wait, so it measures pure work.
  - It needs cheats or op permission in singleplayer.
- **Steady-state MSPT:** run `/tick query` right after a timed window of normal play.
- **TPS chart colours:** if the dark-red band grows during battles, it is tick logic. If light brown grows, it is scheduled tasks, i.e. work queued onto the server thread, such as packet handlers or `server.execute` calls from other threads.
- **Section names:** wrap the mod's big systems in `ProfilerFiller.push("rafasconquest:battle")` and similar. Candidates are battle AI, sea lanes, the living-world simulation, road and city placement, and the depth merge or swell render passes. They then appear by name in F3+L output, the F3+1 pie chart and `/perf`. NeoForge documents only the API; the naming is my suggestion.
- **Cheap triage of Recruits load:** `/neoforge track start entity 30` during a battle, then `/neoforge track entity`, gives per-entity cost with coordinates. `/neoforge entity list recruits:*` gives the count.
- **NeoForge TPS output:** `/neoforge tps [dimension]` prints mean tick time and TPS per dimension. This comes from general NeoForge knowledge; I did not open the TPSCommand source.
- **GPU line on F3:** I believe vanilla F3 shows a "GPU: N%" utilisation line from GL timer queries in recent versions. This was not verified against a fetched source.

### Gaps
- The exact `/tick query` and `/tick sprint` output format (which percentiles) was not shown on the wiki page. Check in game.
- The `/neoforge tps` literal and output were not verified from source (TPSCommand.java was not opened).
- No source confirms a client launch argument that auto-starts JFR (e.g. `--jfrProfile`). Unverified.

## 3. JFR + JMC, async-profiler, VisualVM, Eclipse MAT: when each beats spark

### Takeaway
- **JFR** (built into JDK 21) is the best Windows complement to spark. It samples without safepoint bias and records allocations, GC pauses, locks, thread states and Minecraft's own chunk-gen, tick and packet events. It can be started from inside the game with `/jfr start` or attached with `jcmd`, with no extra long-running JVM. Read it with `jfr view` (new in JDK 21) or JMC.
- **async-profiler** has no official Windows build, so it is out for this PC.
- **Eclipse MAT** is the tool for heap dumps and leak hunting: dominator tree, retained size, path to GC roots.
- **VisualVM** is unnecessary given the tools above.

### Cited Findings
**JFR syntax**
- At JVM start:
  - `-XX:StartFlightRecording.delay=20s,duration=60s,name=myrecording,filename=myrecording.jfr,settings=profile`. The standard templates are `default` and `profile`.
  - Continuous: `-XX:StartFlightRecording.disk=true,maxage=6h,settings=default`.

  — [Oracle JDK 21 Troubleshooting: Diagnostic Tools](https://docs.oracle.com/en/java/javase/21/troubleshoot/diagnostic-tools.html)
- On a running JVM, with `jcmd` from the JDK `bin` folder:
  - `jcmd <pid> JFR.start name=MyRecording settings=profile delay=20s duration=2m filename=C:\TEMP\myrecording.jfr`
  - `jcmd <pid> JFR.check`, `jcmd <pid> JFR.stop` and `jcmd <pid> JFR.dump name=MyRecording filename=...`
  - Also `jcmd <pid> GC.heap_dump <file>`, `jcmd <pid> GC.class_histogram` and `jcmd <pid> Thread.print -l`.
  - `jcmd` is "the recommended tool" over `jmap`.

  — [Oracle JDK 21 Troubleshooting: Diagnostic Tools](https://docs.oracle.com/en/java/javase/21/troubleshoot/diagnostic-tools.html)
- The JDK 21 `jfr` tool:
  - `jfr print [--json] [--events <filters>] [--stack-depth <n>] <file>`
  - `jfr summary <file>`
  - `jfr view [--verbose] [--width N] [--truncate beginning|end] [--cell-height N] <view> <file>`, e.g. `jfr view --width 120 --truncate beginning hot-methods recording.jfr`
  - `jfr configure ...` builds custom `.jfc` settings
  - `jfr scrub`, `assemble`, `disassemble`

  — [Oracle JDK 21: The jfr Command](https://docs.oracle.com/en/java/javase/21/docs/specs/man/jfr.html)
- JDK 21 ships about 70 predefined views, including `hot-methods`, `gc-pauses`, `allocation-by-class`, `allocation-by-site`, `memory-leaks-by-class` and `pinned-threads`. Live use: `jcmd <pid> JFR.view hot-methods`, or with limits such as `jcmd <pid> JFR.view maxage=1h maxsize=2000MB gc-pauses`. Run `jfr view` with no view name to list them. — [inside.java: New JFR view command](https://inside.java/2023/05/31/jfr-view-command/); [Erik Gahlin: JFR views](https://egahlin.github.io/2023/05/30/views.html); [Baeldung: JFR view](https://www.baeldung.com/java-flight-recorder-view)
- In JMC, the Method Profiling page needs a time-fixed recording made with profiling settings. — [Oracle JDK 21 Troubleshooting: Diagnostic Tools](https://docs.oracle.com/en/java/javase/21/troubleshoot/diagnostic-tools.html)

**async-profiler**
- The README lists Linux (x64, arm64 and others) and macOS (x64, arm64). Windows is not listed. The current stable release is 4.6. Quick start: `asprof -d 30 -f flamegraph.html <PID>`. It profiles CPU, heap allocations and native memory. — [async-profiler GitHub](https://github.com/async-profiler/async-profiler)
- One README mirror reportedly lists Windows as a "community supported build", while Sentry's tracker says there is no open-source Windows version. The official README omits Windows. — search summary of [Sentry issue #4779](https://github.com/getsentry/sentry-java/issues/4779) and an async-profiler v2.10 README mirror. These conflict, so treat Windows as unsupported.

**Eclipse MAT**
- Leak Suspects report.
- Dominator tree: the biggest objects, where children would be freed if the parent's references were removed. Can be grouped by class loader or package.
- Histogram with shallow and retained size, grouped by class loader, package or superclass.
- "Path to GC roots": "the main tool for finding leaks where unused objects are still referenced".

— [Eclipse MAT Basic Tutorial](https://help.eclipse.org/latest/topic/org.eclipse.mat.ui.help/gettingstarted/basictutorial.html)

### Inferences
- **When each tool wins:**

  | Tool | Use it for |
  |---|---|
  | spark | First look, per-mod Sources view, spike filtering, shareable links |
  | JFR | Windows-accurate CPU sampling, allocation pressure (`allocation-by-site` will point at per-tick allocations in battle AI, sea swell meshes and pathfinding), GC pauses that line up with stutter, lock contention, chunk-gen stage timings via Minecraft's own JFR events |
  | MAT | Leaks across world reloads, e.g. static maps keyed by Level, ships or holds that outlive their world |
- **Low-memory JFR workflow:**
  1. Start the CurseForge instance.
  2. Run `jcmd` (a short-lived process) or `/jfr start` in game.
  3. Close the game, then run `jfr view hot-methods file.jfr` and `jfr view allocation-by-site file.jfr` in the terminal.
  4. Open JMC only if needed, and only after the game is closed. JMC is a heavyweight Eclipse RCP app and a second big JVM.

  MAT likewise needs its own heap, roughly the size of the dump, so take smaller heap dumps by reproducing on a smaller world, and analyse after closing the game.
- **Heap dumps:** `jcmd <pid> GC.heap_dump` and `/spark heapdump` both produce `.hprof` files for MAT. Use `--compress gzip` on spark to save disk; MAT needs it decompressed first.
- **VisualVM** adds little over JFR, JMC and spark here, and it is another resident JVM. Skip it on this PC.

### Gaps
- The JMC version current for 2025–2026 and its minimum JDK were not checked.
- VisualVM's current version was not researched, as it is low priority.
- Nothing was found on how much overhead the JFR `profile` template adds in Minecraft specifically. The often-cited figure is about 1–2%, but no source was opened for it.

## 4. GPU and render profiling (RenderDoc, Nsight, apitrace, GL debug, sync stalls)

### Takeaway
- Use RenderDoc for per-draw and per-pass inspection, and Nsight Graphics' GPU Trace for per-pass GPU timing on NVIDIA.
- With CurseForge, launch the launcher under RenderDoc with "Capture Child Processes" on and attach to `javaw.exe`.
- In a NeoForge dev setup, inject into the paused JVM instead. ModDevGradle can generate launch scripts for external tools.
- A CPU profiler shows GPU sync stalls as Render-thread time inside GL calls. Look for `glReadPixels`, `glGetQueryObject*`, `glFinish` and buffer mapping, then confirm in Nsight.

### Cited Findings
**RenderDoc with a launcher**
1. In RenderDoc, open "Launch Application" and set Executable Path to the launcher. A third-party launcher such as Prism is recommended.
2. Enable "Capture Child Processes" and click Launch.
3. In the launcher tab, double-click `javaw.exe` under Child Processes.
4. In game, press F12 to capture. The game may freeze briefly.
5. Use the Event Browser, Pipeline State, Texture Viewer and Mesh Viewer.

Saved captures keep only the draw calls and recompute outputs on load. With Iris, `glMulti*` calls are generally G-buffers and `glDrawElements(6)` calls are composites.

— [ShaderLABS: Attaching Graphics Debuggers to Minecraft](https://shaderlabs.org/wiki/Attaching_Graphics_Debuggers_to_Minecraft)

**Nsight Graphics**
- Frame debugger: Start Activity, set the launcher as the executable, choose Frame Debugger then OpenGL, and capture with F11.
- Performance: run Nsight as administrator and choose the "GPU Trace Profiler" activity, then Launch GPU Trace, F11 in game, and Open.

— [ShaderLABS: Attaching Graphics Debuggers to Minecraft](https://shaderlabs.org/wiki/Attaching_Graphics_Debuggers_to_Minecraft)

**Dev environment and other tools**
- Manual injection for a dev environment: enable process injection in RenderDoc, pause the JVM at the client main method with a breakpoint, then File > Inject into process by PID and resume. The page is from 2022 and calls itself a workaround. — [Flywheel wiki: Using RenderDoc in a Development Environment](https://git.grimmauld.de/mirrors/Flywheel/wiki/Using-RenderDoc-in-a-Development-Environment)
- ModDevGradle 2.0.7-beta added generated launch scripts "for use by external tools (RenderDoc, Nsight Graphics, perf)". — summarised from the [ModDevGradle changelogs](https://maven.neoforged.net/releases/net/neoforged/moddev-gradle/2.0.46-beta/moddev-gradle-2.0.46-beta-changelog.txt). The 2.0.7 entry itself was not opened.
- The MDG docs page has no RenderDoc section. Runs accept `jvmArguments`, `systemProperty`, `environment` and `programArguments`. — [NeoForged MDG docs](https://docs.neoforged.net/toolchain/docs/plugins/mdg/)
- GFX-Debuggers injects RenderDoc or Nsight via `-Ddebugger=renderdoc`, with `-Drenderdoc.path` or `RENDERDOC_PATH`. It is **Fabric only** and not usable on NeoForge. — [GFX-Debuggers on Modrinth](https://modrinth.com/project/7eo1rzX8); [modpackindex](https://www.modpackindex.com/mod/111134/gfx-debuggers-nsight-and-renderdoc-injector)
- GL debug output via `glDebugVerbosity` in `options.txt` (see section 2). The Khronos wiki notes that debug output generates no messages unless enabled, and that it starts enabled in debug contexts. — [Khronos: Debug Output](https://wikis.khronos.org/opengl/Debug_Output)

### Inferences
- **RafasConquest render passes:**
  - Capture one frame each with the custom passes on and off: DH+MC depth merge, sea swell/foam, map screen.
  - In Nsight GPU Trace, compare the GPU time of the depth merge and foam passes against the vanilla terrain pass.
  - Watch for full-screen copies or blits that run every frame even when nothing changed.
- **Sync stalls (from OpenGL semantics, not a fetched source):**
  - `glReadPixels` into client memory, `glGetTexImage`, `glGetBufferSubData`, `glGetQueryObject*(GL_QUERY_RESULT)` without first checking `GL_QUERY_RESULT_AVAILABLE`, `glFinish`, and mapping a buffer the GPU still uses all block the CPU until the GPU catches up.
  - In spark or JFR these show as Render-thread time inside `org.lwjgl.opengl.GL*` native frames.
  - Fixes: read back into a PBO and map it a frame or two later, use `glFenceSync` with `glClientWaitSync` and a 0 timeout, and poll queries for availability.
  - Depth merging with `glBlitFramebuffer`, `glCopyImageSubData` or a shader pass stays GPU-side and does not stall.
- **Frame pacing:** Nsight's GPU Trace and the F3+2 frame chart together separate "GPU-bound" (frame time high, Render thread idle-waiting in swap) from "CPU-bound" (Render thread busy in Java).
- **Sodium, Iris and DH:** these change the rendering path. Profile once with and without each, especially Iris shader packs, since the user's own memory says shader-pack compatibility is still open.
- **apitrace:** found no Minecraft-specific guidance. RenderDoc and Nsight cover the need.

### Gaps
- The Khronos Synchronization and PBO wiki pages could not be fetched (403 and connection refused), so the sync-stall list above is from general knowledge.
- No 2025–2026 guide was found specifically for RenderDoc with NeoForge 1.21.1 plus Sodium or Iris. I also did not confirm whether RenderDoc's OpenGL capture copes with the GL extensions Sodium or DH use.
- Neither the AMD tools (Radeon GPU Profiler does not support OpenGL) nor the user's GPU vendor was checked. Nsight is NVIDIA-only.
- No Distant Horizons-specific profiling documentation was researched, e.g. its debug overlays or LOD build-thread metrics.

## 5. Reproducible benchmarks (seed worlds, pregeneration, scripted scenarios, GameTest, chunk/s, percentiles, warm-up, JMH)

### Takeaway
- Build benchmarks around a pregenerated, copied world folder. Pregenerate with `/neoforge generate start` or Chunky on NeoForge 1.21.1.
- Drive a fixed scenario, e.g. spawn N Recruits and start a battle, by command or by a GameTest.
- Measure with `/tick sprint` or `/tick query`, spark health MSPT percentiles, and the JFR Chunk Generation events.
- Use JMH (`me.champeau.jmh`) only for pure-Java hot code, such as noise or road-planning math. Minecraft classes are awkward to load there.

### Cited Findings
- **Chunky:**
  - `chunky world`, `chunky center <x> <z>`, `chunky radius <r>`, `chunky shape square|circle`, `chunky pattern concentric|loop`, `chunky start`, `chunky pause`, `chunky continue`, `chunky cancel`, `chunky progress`, `chunky silent`, `chunky quiet <seconds>`, `chunky trim ...`, `chunky selection`.
  - Progress is normally printed to the server console.

  — [Chunky wiki: Commands](https://github.com/pop4959/Chunky/wiki/Commands)
- A NeoForge build of Chunky exists (1.21.11 was seen). Other NeoForge 1.21.1 pregenerators include World Pre Generator (`/pregen gen ...`) and Chunksmith. — summarised from search results ([World Pre Generator](https://www.curseforge.com/minecraft/mc-mods/world-pre-generator), [Chunksmith](https://modrinth.com/mod/chunksmith), [Chunky listing](https://skinmc.net/project/chunky)). I did not verify that Chunky has a NeoForge 1.21.1 file.
- `/neoforge generate start <pos> <chunkRadius> [progressBar]` is built into NeoForge 1.21.1 and needs permission 4. — [GenerateCommand.java](https://github.com/neoforged/NeoForge/blob/1.21.1/src/main/java/net/neoforged/neoforge/server/command/GenerateCommand.java)
- The JFR "Chunk Generation" event times individual chunk stages. — [Minecraft Wiki: /jfr](https://minecraft.wiki/w/Commands/jfr)
- **GameTest (NeoForge 1.21.1):**
  - Test methods: `public static void test(GameTestHelper helper)` with `@GameTest(template=..., templateNamespace=MODID, setupTicks=..., batch=..., required=..., timeoutTicks=...)`.
  - Registration: `@GameTestHolder(MODID)`, or `RegisterGameTestsEvent` with `event.register(Class)`. `@PrefixGameTestTemplate(false)` stops the class name being prepended.
  - Hooks: `@BeforeBatch` and `@AfterBatch`.
  - In-game: `/test run <name>`, `/test runall`, `/test runclosest`, `/test runthese`, `/test runfailed`.
  - Headless run: `gradlew runGameTestServer`. It exits with the number of failed required tests.
  - Build properties: `neoforge.enabledGameTestNamespaces` (comma-separated, no spaces) and `neoforge.enableGameTest` for other runs.

  — [NeoForge docs 1.21.1: Game Tests](https://docs.neoforged.net/docs/1.21.1/misc/gametest)
- In ModDevGradle, define a run with `type = "gameTestServer"` inside `neoForge { runs { ... } }`. Unit tests use `neoForge { unitTest { enable(); testedMod = mods.<name> } }`. The `net.neoforged:testframework` dependency with `@ExtendWith(EphemeralTestServerProvider.class)` runs JUnit tests in a server context. — [NeoForged MDG docs](https://docs.neoforged.net/toolchain/docs/plugins/mdg/)
- **JMH:**
  - Plugin `id "me.champeau.jmh" version "0.7.3"`, with sources in `src/jmh/java`.
  - Run with `gradle jmh`; `jmhJar` builds the jar.
  - The `jmh { }` block takes `iterations`, `warmupIterations`, `fork` (0 disables forking), `includes`, `jvmArgs` and `resultFormat` (CSV, JSON, ...).
  - Gradle 8 needs plugin 0.7.0 or newer.

  — [jmh-gradle-plugin GitHub](https://github.com/melix/jmh-gradle-plugin)

### Inferences
- **A credible scenario harness for RafasConquest:**
  1. Keep a pregenerated copy of a fixed-seed world with a known city and coast.
  2. Copy the world fresh for every run, so saved entity and simulation state does not drift.
  3. Use one command function or debug command to teleport to fixed coordinates, spawn N Recruits per side, and start the battle.
  4. Freeze with `/tick freeze`, then measure with `/tick sprint 1200` and record its ms/tick.
  5. Repeat at least 3 times and report the median.
- **Steady-state runs:** for client FPS and server MSPT, use a fixed-duration run of about 2–5 minutes. Discard the first 30–60 s as JIT warm-up and chunk loading. Record `/spark health` MSPT (median, p95, max) and the F3+2 frame-time min/avg/max.
- **Worldgen rate:**
  - Time `/neoforge generate start` or Chunky over a fixed radius in a new world: chunks divided by wall time.
  - Add JFR Chunk Generation events to see which stage (structures, features, surface) holds the city and road time.
  - Compare against the same radius with RafasConquest structures turned off, to isolate the mod's share.
- **GameTest headless:** `runGameTestServer` is one JVM with no client, so it can run nightly or on demand without the client open. On this PC, do not run it while the client or a dev server is up.
- **JMH:** `fork` defaults to forking JVMs. On this PC use `fork = 1` with small heaps, and never run it alongside the game.

### Gaps
- No write-ups from performance-mod authors (CaffeineMC/Lithium, ModernFix, FerriteCore, C2ME) on their benchmarking methodology were fetched, for lack of time.
- Not confirmed whether GameTest timing (ticks) can be read back as a perf metric, or whether any mod or library offers "perf GameTests". I found no such source.
- The exact output of `/tick sprint` (average ms/tick and TPS) was not verified.

## 6. JVM settings for Java 21 / 1.21.1 on a low-RAM PC, and keeping JVM count down

### Takeaway
- On Java 21, G1 (the default) is still the safe choice for a small heap.
- Generational ZGC (`-XX:+UseZGC -XX:+ZGenerational`) cuts pause spikes but needs more memory, roughly 10–20% overhead per community reports. It mainly pays off on large heaps of about 12–16 GB or more, which this PC likely can't spare.
- Aikar's flags target Paper servers with 6–10 GB or more and are not client advice.
- For profiling, run exactly one game JVM, the CurseForge instance, with no Gradle daemon alive. Attach short-lived tools (`jcmd`, `/jfr`, spark) to it and analyse after it closes.

### Cited Findings
**ZGC across JDK versions**
- JEP 439 delivered Generational ZGC in JDK 21 as opt-in: `java -XX:+UseZGC -XX:+ZGenerational`. Without the flag, JDK 21's ZGC is non-generational. — [JEP 439](https://openjdk.org/jeps/439); [inside.java: Gen ZGC explainer](https://inside.java/2023/11/28/gen-zgc-explainer/)
- JEP 474 later made generational mode the default and deprecated the flag. JEP 490 removed non-generational mode. — [JDK-8326958 (JEP 474)](https://bugs.openjdk.org/browse/JDK-8326958); [JDK-8341693 (JEP 490)](https://bugs.openjdk.org/browse/JDK-8341693)
- Release numbers: one source says JDK 23 made it the default and JDK 24 removed the old mode; another says Java 25. — [Cybrancee](https://cybrancee.com/blog/?p=8157); contradicted by [MineGuard](https://mineguard.pro/en/blog/zgc-vs-g1gc-minecraft-java-21-benchmark). Irrelevant on JDK 21, where the flag is needed.

**G1 vs ZGC in community guides**
- G1 is still recommended on small heaps (8 GB or less). — [CraftDex server flags](https://craftdex.net/pt/tools/server-flags)
- ZGC flattens p99 latency while G1 has a slight average-throughput edge on small heaps. — [MineGuard](https://mineguard.pro/en/blog/zgc-vs-g1gc-minecraft-java-21-benchmark)
- ZGC costs about 10–20% memory overhead compared with G1. — [Space-node](https://space-node.net/blog/zgc-vs-g1gc-minecraft-server-2026)
- These are community blogs, not primary benchmarks.

**Aikar's flags**
- The PaperMC page gives a fixed G1 flag set: `-Xms10G -Xmx10G -XX:+UseG1GC ...`. It recommends Xms equal to Xmx, "at least 6-10GB", and leaving 1000–1500 MB headroom.
- Never "allocate all of your available memory on a shared host".
- GC logging: `-Xlog:gc*` on Java 11+, cheap enough to leave on.
- No ZGC mention and no heap-size thresholds.

— [PaperMC: Aikar's flags](https://docs.papermc.io/paper/aikars-flags)

**Gradle daemon behaviour**
- `--no-daemon` or `org.gradle.daemon=false` disables the daemon. If the client JVM's args don't match `org.gradle.jvmargs`, Gradle still starts a "single-used Daemon (disposable JVM)" and stops it after the build. If `JAVA_OPTS`/`GRADLE_OPTS` match `org.gradle.jvmargs`, the build runs in the client JVM. — [Gradle: The Daemon](https://docs.gradle.org/current/userguide/gradle_daemon.html)
- Without a configured max heap, the daemon uses up to 512 MB. Idle daemons stop after 3 hours, or sooner when memory is low. — [Gradle: The Daemon](https://docs.gradle.org/current/userguide/gradle_daemon.html)
- A new daemon is spawned whenever Java home, JVM args, immutable properties (`-Xmx`, `-ea`) or the Gradle version differ. Changing `org.gradle.jvmargs` therefore spawns another daemon. — [Gradle: The Daemon](https://docs.gradle.org/current/userguide/gradle_daemon.html)
- `gradle --status` lists daemons. `gradle --stop` kills all daemons of that Gradle version. Use `jps` to see `GradleDaemon` processes from any version. — [Gradle: The Daemon](https://docs.gradle.org/current/userguide/gradle_daemon.html)

### Inferences
- **What to run on this PC:**
  - Profile in the CurseForge instance, which is one JVM, rather than `runClient`. `runClient` is a Gradle daemon plus a forked client JVM, so at least two big JVMs.
  - Build the jar with `gradlew build`, then let the daemon expire or stop it with `gradlew --stop` before launching the instance.
  - The user's memory notes say never to run `--stop` while a `runClient` client is running from Gradle.
  - Use `jps -l` to check that only one Minecraft JVM is alive before measuring. This also keeps background JVMs from distorting the measurement.
- **GC choice:**
  - Keep G1 with a fixed heap (`-Xms` = `-Xmx`, e.g. 6–8 GB if the PC has 16 GB).
  - Try Generational ZGC only as an A/B test, judged on stutter (frame-time p99, GC pauses in `jfr view gc-pauses`), not FPS average.
  - On a memory-constrained PC, ZGC's extra footprint may push the system into paging, which is worse than any GC pause.
- **GC logging:** add `-Xlog:gc*:file=gc.log` during perf sessions. It is low cost and lets GC pauses be lined up with spark tick-monitor spikes.
- **Gradle memory:** set `org.gradle.jvmargs=-Xmx2g` explicitly, and keep it identical everywhere so only one daemon kind exists. Run tests and GameTests sequentially, never alongside the client.

### Gaps
- Not verified: where CurseForge sets per-instance JVM args (Settings > Minecraft > Java Settings "Additional Arguments" and memory slider, per instance profile options) and whether it overrides Xms.
- No primary-source benchmark of G1 vs Generational ZGC for a Minecraft **client** on a small heap was found. Only community blogs.
- The user's actual RAM size is unknown to me. Heap advice should be scaled to it.

## 7. Attributing lag to a specific mod in a modpack, and credible before/after comparisons

### Takeaway
- Attribute with spark's Sources view, which builds a per-mod tree, and per-object tools: `/neoforge track` for entities and block entities, and Observable or Tickwise for an in-world overlay of laggy entities and chunks.
- Confirm by toggling: the same scenario with the suspect mod removed, or with the RafasConquest feature disabled by config.
- Compare builds with fixed-scenario runs, several repeats each, reporting median and p95/p99 MSPT or frame time.
- Keep the spark links with `--comment` tags and bookmarks as evidence.

### Cited Findings
- spark's Sources view shows "a separate profiler tree … for each plugin/mod", for finding poorly performing mods. Bookmarks are encoded in the shared URL. — [spark: Using the viewer](https://spark.lucko.me/docs/Using-the-viewer)
- `--comment <comment>` on `/spark profiler stop` labels a profile in the viewer. — [spark Command Usage](https://spark.lucko.me/docs/Command-Usage)
- `/neoforge track start entity|blockentity <duration>` then `/neoforge track entity|blockentity` gives the top 10 objects by average tick time, with type ID and coordinates. — [TrackCommand.java](https://github.com/neoforged/NeoForge/blob/1.21.1/src/main/java/net/neoforged/neoforge/server/command/TrackCommand.java)
- **Observable** profiles block entities and entities, showing which objects use the most tick time and where, with an in-game overlay. A NeoForge 1.21.1 build is listed as 5.4.4. "Observable – Remake" is a separate project targeting a much newer version. — [mado-hosting listing](https://www.mado-hosting.com/en/minecraft-mods/observable); [modpackindex Observable](https://www.modpackindex.com/mod/27493/observable); [modpackindex Observable Remake](https://www.modpackindex.com/mod/215107/observable-remake). These are aggregator listings; check the CurseForge or Modrinth page.
- **Tickwise** is a NeoForge mod listed for MC 1.21.1 that tracks entity and block-entity costs. — [moddex: Tickwise](https://moddex.gg/mod/tickwise)
- F3+L output per section gives % of parent and % of tick, and custom `ProfilerFiller.push/pop` sections show up by name. — [NeoForge docs 1.21.1: Debug Profiler](https://docs.neoforged.net/docs/1.21.1/misc/debugprofiler)

### Inferences
- **Attribution flow for this mod:**
  1. Use F3+2 to tell server-bound lag (tick chart) from client-bound lag (frame chart).
  2. Server-bound: `/spark tickmonitor --threshold-tick 50`, then `/spark profiler start --only-ticks-over 60 --timeout 120`. In the viewer, open the Sources view and expand `rafasconquest`, recruits, sable and distanthorizons.
  3. Client-bound: `/sparkc profiler start --thread "Render thread" --timeout 60` (thread name assumed from vanilla), then the Sources view. If the Render thread mostly waits in swap or GL calls, move to Nsight GPU Trace.
  4. Memory or stutter: `/spark gcmonitor`, or JFR `gc-pauses` and `allocation-by-site`.
- **Mixin and bridge attribution:**
  - Time spent in mixin-injected code is attributed to the target class's frames. spark's Sources view maps by class origin, so injected handler methods from `rafasconquest.mixin.*` may appear under vanilla frames.
  - Search the call tree for `rafasconquest` handler names, as in mixin-generated `handler$...` methods.
  - The new ChunkMap/TrackedEntity camera mixins in the working tree are worth checking this way.
- **Cross-mod cost:** Villager Recruits units are the recruits mod's entities, but RafasConquest's battle code drives them. The cost will be split between the two mod trees. Use `--not-combined` and compare the same battle size with RafasConquest's battle controller off.
- **Credible before/after:**
  - Same world copy, seed, coordinates, render distance, simulation distance, window size, modlist and JVM args.
  - Same spark version and engine. On Windows it is always the Java sampler, so it is at least consistent.
  - At least 3 runs per build, with warm-up discarded. Report median MSPT, p95 and max, or for the client frame-time avg, p99 and max.
  - Report the change only if it exceeds the run-to-run spread.
  - Save the spark links (`--comment "build abc123, 300 recruits"`) and JFR files alongside the commit hash.

### Gaps
- spark's documentation does not explain exactly how it maps frames to "sources" on NeoForge: by jar or by mod-file class ownership, and how it treats mixin handlers. This was not confirmed.
- Observable's and Tickwise's exact NeoForge 1.21.1 compatibility and commands were not verified on their primary pages.
- I found no NeoForge-specific "mod blame" tool beyond track, entity list and the profiler sections.
