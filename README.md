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

## Bigger world

The world creation screen has a **RafasConquest** button (top right) with the world's options: the **horizontal
stretch**, on or off, and how far (a slider, 1× to 4× wider each way; 3× by default). It stretches the overworld's
biomes (the temperature and humidity maps) and its land (continentalness, erosion and ridges: continents, mountains,
coasts), on whatever overworld generator is in use, vanilla's or a terrain mod's: when the server starts, before any
level is made, every climate noise in the overworld's noise router has its horizontal scale divided
(`worldgen/HorizontalStretch`; edited through the game's own codec). Each world keeps its choice in
`rafasconquest_world.properties` in its save folder; a world made before this, with chunks already, keeps vanilla's
scale. It is off by default when a mod it doesn't suit is installed (`HorizontalStretch.INCOMPATIBLE`: Tectonic,
Terralith), and the options say so. A dedicated server's new world takes the defaults. The log says how many climate
noises were stretched, or that none were found.

The same screen has a **province size** slider: 1, provinces 768 blocks apart and the world map out to four times the
storm wall (16,000 blocks by default); 2 (the default), provinces 1,536 blocks apart and the map out to eight times
(32,000 blocks), a pixel every 64 blocks. It is kept with the stretch in the world's properties file.
And two switches: **Enable Martial Titles** (Sword Gods, Sword Kings, Sword Saints, and the sword sanctums; off,
no one passes prowess 20 and there are no sanctums) and **Enable Mantra Schools** (an offensive magic system of ten
schools, each with a dojo; off, no dojos). Both are on by default.

With Distant Horizons, a stretched world (`client/DistantHorizonsQuality`) builds its far terrain from generated chunks
only (generator plan `CHUNKS_ONLY`): Distant Horizons' quick rough-surface pass came out coarse, wrongly coloured and
treeless there. Its horizontal quality is raised to HIGH (no higher: EXTREME over the whole horizon kept the machine
flat out). The server tells each joining client how far the world is stretched; both are set through
Distant Horizons' API as overrides, never saved over the player's own settings (quality never below theirs), and
cleared when they leave the world. LODs a world already saved keep their old look; a new world shows the change.

## Recruits integration

Every faction this mod creates is also registered as a Villager Recruits faction, so it appears in the Recruits faction
menu. Registration happens as factions are discovered, plus a catch-up pass when the server starts. Each one is a
scoreboard team (Recruits factions are teams) in the faction's colour, plus a Recruits faction record holding the
faction's name and banner. These factions have no player leader. Their leader id is the faction's id, which marks them
as ours, and their leader name is the faction type ("Empire", "City-state"). Recruits saves them with the world.
All Recruits calls live in the `bridge` package: `RecruitsFactions` behind the `RecruitsBridge` interface, and the
debug battle in `RecruitsBattle`. `/pve debug recruits` lists what Recruits holds.

## Battles (prototype)

`/pve debug battle [first [firstSoldiers] second [secondSoldiers]]` deploys two armies 45 blocks in front of you,
marching at each other across your view: for example `/pve debug battle Osford 2000 Valoia 500`.
Each side is 20 to 100,000 soldiers (rounded to whole blocks of 20), or 100 if no number is given. With no names given,
the two factions nearest you fight; quote names that contain spaces. The player is a bystander: the two factions are
made enemies in Recruits' diplomacy for the battle, so soldiers attack each other and leave you alone unless you strike
first. The old relation is restored when the battle ends.

- **Each army** is one faction, in blocks of 20 soldiers (10 wide, 2 deep), each led by a captain, plus a general and
  a standard-bearer. A fifth of the blocks are archers and a fifth cavalry (rounded); the rest is infantry, with a
  shieldwall every third file (`battle/BattlePlan.deployment`).
- **How an army deploys** (`BattlePlan.deployment`): an army of more than 300 soldiers stands as three armies side
  by side, a left wing, a centre and a right wing (see Wings below), sharing all its troops (infantry, archers and
  cavalry alike) by its general's distribution:
  - **Evenly Distributed**: a third each; **Stacked Left Wing**, **Stacked Centre**, **Stacked Right Wing**: that
    wing 35 to 45 % (drawn for each general), the other two the rest evenly. Every general draws one of the four at
    random; a battle without a general's plan is evenly distributed.
  - **The gap between the wing armies** grows with the size of the side: 3 blocks up to 10,000 soldiers, then evenly
    from 35 at 10,000 to 500 at 100,000.

  Each wing army (or a small army, all centre) stands alike, front to back:
  - **The front.** In a Great Shield, or an army of more than 1000 soldiers, each front file is 3 blocks closed up
    one behind the other, about 70 % of the wing's infantry, and always at least as wide as its archers' rows. A
    smaller plain army has a single front line at most 10 blocks wide (as wide as its archers need). The whole
    army's front is at most 40 files up to 10,000 soldiers, more beyond (about 126 at 100,000), shared between its
    wings.
  - **The archers**, in at most 3 rows right behind the front, which covers them (more rows only once the front is
    at its limit).
  - **The reserves**: the rest of the infantry, 24 blocks further back, in a few lines of one width, centred; each
    joins the file of the front block it stands behind, relieves it from there, and keeps its deployed place.
  - **The cavalry**, 10 blocks behind the reserves, centred, in rows no wider than the front (more rows rather than
    wider ones), so each wing army stands as one rectangle about as wide as its front.
  - Wedges form only later, once the front line is established, and wait behind the middle of the line, further
    back wherever the enemy has pushed in, so the general in one is never caught there. No two blocks overlap when an
    army deploys.
- **Real soldiers only near players** (`bridge/RecruitsBattle`): the whole battle is fought as numbers
  (`battle/FieldBattle`, below), and soldiers within 96 blocks of a player become real Recruits soldiers one by
  one, each at its place in its block's ranks with the health it has left; beyond 128 each goes back to numbers,
  keeping its health. At most 300 soldiers are real at once (a rider counts as two), the nearest first, and at most
  160 become real each second. A real soldier stays real until 6 blocks further out, so the edge doesn't flicker.
  So a block can be part real, part figures: its real soldiers keep their own places in its ranks among the figures,
  its position is the middle of all its soldiers (from where the real ones stand and their places in the ranks), and
  where it meets a real enemy, Recruits fights between the real soldiers while its figures fight on as numbers. The
  numbers' blows on it fall on real soldiers and figures alike.
  - While a block is real, its soldiers walk and fight as Recruits soldiers; each second they report where they stand
    and their health, the numbers give the block its orders, and those are passed on (stand in line here, charge,
    withdraw without fighting).
  - Real soldiers fighting real soldiers fight it out themselves. Where real soldiers fight a block that is only
    numbers, the numbers work the fight out, and the damage is dealt to the real soldiers in hits of 4.
  - `/pve debug battle watch <x> <z>` treats a spot like a player standing there (and `watch clear` stops), to test
    the live area without one.
  - Measured on the dev server, 2000 against 2000: about 3 ms a tick with nobody near (all numbers), 16 to 50 ms with
    about 600 real soldiers near a watch point, 20 TPS throughout; a cavalry wing near the watch point cost more
    (68 ms), so riders now count double.
  - 10,000 against 10,000 (dev server): 4 to 10 ms a tick with nobody near, 15 to 24 ms with a watch point and its 400
    real soldiers, 20 TPS throughout. The numbers take about half a millisecond a tick even at this size: a cheap
    distance check lets each block's search for the nearest enemy skip blocks that can't be nearer. The ground height
    for drawing blocks that are only numbers is asked of the world generator once per chunk, at most 40 a second
    (a block keeps its last height meanwhile), since those lookups had cost up to 280 ms a second. Reports break the
    battle's share down into the numbers, switching blocks between real and numbers, and snapshots.
  - `/pve debug battle livecap <soldiers>` changes the cap (kept in `config/rafasconquest/livecap.txt`); without a
    number it shows it. With a player connected (client
    and server on one 8-core machine), about 1200 real soldiers ran at 10 to 15 TPS, and even about 450 at 13 to 16
    TPS once fighting began, so the cap was 400; it is now 300.
  - **TickWeave** (an experimental mod that ticks entities on several threads; optional, not a dependency) was tried on
    a regular Forge server (it can't run in the dev setup: its mixins don't survive the dev remapping). With nobody
    connected and 1500 real soldiers it held 20 TPS at 33 to 47 ms a tick, against 56 to 98 ms without it; with a
    player connected and about 1200 real soldiers, 46 to 51 ms against 68 to 96 ms. It helps, but doesn't make large
    real crowds smooth with a player on the same machine.
- **Uniforms:** everyone wears full leather dyed the faction's main colour (its banner's base colour), and cavalry
  horses wear dyed leather horse armour. If both factions share a colour, the second side wears a darker shade, or a
  lighter one if the colour is already very dark. The head shows rank:
  - **Ranks:** a dyed leather cap.
  - **Captain** (one per block, cavalry and archers included): the faction banner, worn on the head.
  - **General:** a gold helmet and the faction banner on the back, like a sashimono (`client/BackBannerLayer`; the server
    tells each client which soldiers wear one).
  - **Standard-bearer:** a dyed cap and the faction banner carried in hand. They stay at the general's side and don't fight.
- **The conveyor:** each front file stands 3 blocks deep in an army of more than 1000 soldiers (or a Great Shield), 2
  deep in one of more than 300, and 1 deep (no conveyor) in a smaller one. A front block that has fought for 2 minutes
  swaps with the block behind it and goes to the back of its file's stack, so the front turns like a slow conveyor
  belt; a badly worn block is still relieved from the reserves.
- **Morale:** an infantry block that loses half its soldiers within a minute **routs**: it stops fighting, runs 30
  blocks behind its army's reserves, regroups for 2 minutes and rejoins the back of its file. The next block of its
  file takes its place at once. Its captain's fall shakes it, counting as a quarter more lost for a minute. The enemy
  blocks fighting a block that routs or loses its captain **rally**: their losses are forgotten and for 45 s they
  can't rout. The battle map outlines routed blocks purple and rallied ones green.
- **Phases and days** (by Minecraft's clock): *deployment* (the armies form up before their camps; the war council
  meets) → *battle* (from the charge until sunset, time 12000, at least a minute) → *evening pullback* (the fighting
  stops; every block marches back 25 blocks, re-forming just short of its camp, before nightfall at 13000) (the din of
  battle and the hoofbeats of charges fade out over 3 s) →
  *night* in camp, nobody fighting → at dawn (23000) the survivors regroup into fresh blocks of 20 (the wounded
  recovered), in camp, under the same generals and plans, and march out to their places: a smaller deployment
  than the first. Enlisted players wake in camp (each has a tent with a bed; the ring points to it in the evening
  and at night), are enlisted anew, and lead their unit out: their soldiers follow them until they are within 5
  blocks of their ring, then form their square. Unless the general gives them another task for the day (60 % of
  mornings when there is a reason: hunt an enemy general who leads their own charge, support when outnumbered,
  flank a thin wing, bait for a cunning general, lead the charge for a bold one): then a messenger waits at their
  tent and gives them the orders, a line at a time, when they come near (or right-click). The charge comes once both
  armies and every enlisted player are in position, at least 30 s and at most 4 minutes after dawn. Day 1 waits for
  the Start button (or `/pve debug battle start`). It repeats until the battle is won.
- **War camps** (`bridge/WarCamp`): each army's camp is built of blocks 40 behind its rearmost block, facing the
  enemy: a command tent 13 × 13 inside and 5 tall in the wool nearest the army's uniform colour, with a plank floor,
  lanterns, the nation's banners on three walls inside and either side of the entrance, and a bedrock block in the
  middle (the Battle Command Table, for now); behind it, rows of small ridge tents (one per 100 soldiers, 6 to 36)
  with campfires. Ten heavy infantry (iron armour, axes, the nation's shields) stand guard at the entrance, five
  either side of the way in. Everything a camp replaces is recorded and put back when it is struck.
- **Deploying to the land** (`battle/GroundPlan`, read by `bridge/Terrain` on a grid of 4 to 8 blocks without
  loading or generating anything: the world where a chunk is loaded, else the world generator; a wood is a canopy of
  leaves, or a forest, taiga or jungle biome): before the battle and every morning, each block moves from its place
  in the template, up to 40 blocks, to the best ground near it. Armies keep out of water entirely (nothing of a
  block's front or its middle may stand in it), love high ground (archers most of all, then infantry; cavalry little),
  keep together (each block near its place, 8 blocks clear of the others), and go no more than 8 blocks further toward
  the enemy. The front chooses first, then the archers (searching as far as the front, for the high ground), then
  the rest, each following its file's front and choosing within 20 blocks of where that puts it.
- **Ambushes:** where a wood lies within 60 blocks of a wing's outer end, up to three of that wing's reserve blocks
  hide in it. They stay where they hide, whatever their file does, until an enemy block is fighting within 50 blocks;
  then they strike it in the flank (or the rear, if others strike it already), and the general's chat says an ambush
  was sprung. When their quarry is gone they wait for the next.
- **A camp per wing:** a winged army has three camps, each behind its wing: the general's in the centre, with the
  command tent and war council, and one behind each wing with its Marshal's tent (9 by 9 inside, 4 tall, six guards,
  the nation's banners) and that wing's own rows of tents. A camp is put on the nearest dry, fairly level ground
  within 48 blocks of where it should be (never on water or over a drop), and a tent is pitched only on dry ground
  within 6 blocks of its command tent's floor in height. Players sleep in their wing's camp.
- **Battle names:** a battle is named after the province its middle lies in (provinces are named the way factions
  are, from their centre and the world seed) and the land there, from the biome: "The Battle of Shukai Plains" (Plains,
  Woods, Pines, Hills, Mountains, Desert, Savanna, Jungle, Marsh, Snows, Meadows, River, Shore, Coast, Badlands). Off
  the map or before it is drawn: "The Battle of the Plains". The name opens the battle, heads the deployment
  countdown, and closes it.
- **Orders to save breaking blocks:** once a second in the battle, a player with no such order (and none for the last
  45 s) is sent to a block of their army within 150 blocks that is 60 % of the way to routing (by its losses over the
  last minute and a fallen captain's shock), not one of their own. An enlisted player is told to support that block;
  an officer is told by the wing's Marshal to save the wing's line there: that block and those within 60 of it about
  to break too. The order stands until they steady ("The line holds. Well done."), break ("Too late: they broke."),
  or 2 minutes pass.
- **Beacons:** a beacon beam shows an enlisted player the way to where an order sends them: their place while the
  armies deploy (whenever they are more than 10 blocks from it), the blocks they were sent to save, the block their
  own is sent against ("Target ...!"), and their tent in the evening pullback. For an order, the beacon shows if they
  are more than 10 blocks off when it is given, goes once they get there, and shows again if they wander more than 45
  blocks away while it stands. The ring stays at their place (or tent).
- **Easing the camp's ground:** a camp's site is chosen for flatness across the whole camp, tents included (each block
  of rise and fall costing as much as 6 blocks further away), with its floor at the ground's middle height under the
  command tent. For the battle the command tent's ground (and 2 blocks around) and a patch under each small tent are
  levelled to that floor, cut down or filled with dirt under grass, and cleared of plants and trees above; all of it is
  put back when the camp is struck.
- **War council** (`bridge/WarCouncil`): while the armies deploy, the general and the Marshals of the left and right
  wings stand around the command table. An officer of the army (rank 5 and up; others are turned away) right-clicks
  the general to be briefed, a line every 2.5 s: the numbers on each side, the plan, how the troops stand, the enemy,
  what the scouts say of them, a word from each wing's Marshal, and the player's own part. Then clickable choices
  (`/council <topic>`, open to everyone): ask about the enemy general (their habits: leading their own charge, a
  great wedge, baits, a shield wall, hunting generals), ask where they are weakest (a thin wing, few horse or
  archers, no shield wall), and offers that depend on the officers' traits (bold, cautious, cunning, proud) and on
  what was asked: join a bold Marshal's flank attack (flanker), back a cautious Marshal's wing (support), volunteer
  as bait for a cunning general, hunt the enemy general once asked about them (if they lead their own charge, or the
  general is bold), propose striking their weak side (a proud general won't have the plan questioned), or lead the
  first charge (frontline). A choice sets the player's role for the charge.
- **Victory:** if a general falls, their army retreats, and the winner may pursue it (a bold general, or one who
  leads charges or hunts generals, does; a cautious one doesn't; else a coin toss): the winner's blocks make for the
  nearest of the enemy as it runs back the way it came, for 5 minutes at most. Once none of the beaten army has been
  harmed by the winner for 30 s, it fades away (a few soldiers a tick, in a puff) and its camp is struck. Without a
  pursuit it simply runs, and fades 30 s after the last blow. An army worn down with its general alive surrenders:
  the fighting stops where it stands, and after 30 s of milling about it fades and its camp is struck. The factions'
  old relations come back then. The winner's army and camp stay until `/pve debug battle clear`.
  A beaten army runs a quarter faster than its pursuers march, so foot outruns foot and only horse rides it down.
  For testing, `/pve debug battle end general <side>` fells a side's general and `/pve debug battle end surrender
  <side>` makes a side surrender. (Minecraft 1.20.1 has no `/tick` command to speed time up.)
- **Deployment countdown:** a bar at the top of the screen counts the deployment down, in the nation's colour for
  players of each army (neutral for others): 4 minutes the first day, 1 minute every morning after. At 0 the battle
  begins (`/pve debug battle start` still starts it at once). Players on a side are enlisted as soon as the battle is
  set up, and while the armies deploy a beacon beam rises over their ring when they are more than 10 blocks from it.
- **A player's units** (one per block they were given, each its own group in Recruits' command screen, so each
  forms its own formation): they stand side by side behind the player in formation and move as the player moves:
  each soldier is walked straight to its place every couple of ticks rather than pathing after the player, so they
  keep up and stop when the player stops. They turn as the player turns, except within 4 s of the player fighting.
  A soldier fighting an enemy at hand is left to it. An order the player gives in Recruits' own screen takes that
  unit over until the war horn orders it again.
- **The war horn** (every enlisted player gets one, in their nation's call): right-clicking it opens its screen: a
  map of the field around the player, north up, with their units and every formation of the battle. Click a unit to
  select it (shift adds), click the map to send the selected units there, or order them: follow me, hold, charge
  (let loose on the nearest real enemy within 40 blocks), square, line (10 wide), shields up or down. Nothing is
  limited to Recruits' 99. Orders wait until the screen closes; then, if any were given, the horn blows, heard far
  across the field, and they are carried out.
- **Blows on figures:** soldiers that are only numbers (figures) can be hit. A player's swing that meets no entity
  but a figure within reach (3.5 blocks, 5 riding) strikes it as hard as the player hits (with their charge, for a
  cavalry player at a gallop); a galloping cavalry player tramples the figures their horse runs into; and a player's
  soldiers with nobody real at hand strike a figure within 2.5 blocks once a second. The blows count in the numbers.
- **Pursuits** last 5 minutes at most, and a winner outnumbered by 20 % or more by the beaten army doesn't pursue.
- **Ranks** (`character/Rank`, saved per player with the world in `data/Ranks`): Soldier (1, leads 1), Corporal (2,
  5), Serjeant (3, 20), Captain (4, 100), Major (5, 500), Colonel (6, 1000), Marshal (7, 3000), Grand Marshal (8,
  10,000). Ranks 1 to 4 are enlisted; 5 to 8 are officers, whose commands come later. `/pve rank` shows yours;
  `/pve rank set <player> <1-8>` (operators) sets one. Every army of 10,000 or more is led by a Grand Marshal, others
  by a General, and an army fighting in wings names a Marshal for each wing.
- **Enlisted players:** a player who has joined one side's team (`/team join`) when the battle starts is enlisted
  with it. They are teleported to the front of the front block nearest the middle of the line (the first to charge)
  and given that block's soldiers, then the blocks beside it, as many as their rank leads (a Captain's 100 at most),
  in a group of theirs in Recruits' command screen; the battle gives those soldiers no orders. Until the player orders
  them otherwise they stand in a square behind the player's ring, facing the enemy, moving with it as the block
  advances; the command screen's formation is set to square. A white ring on the ground marks their place in the
  block, shown when they are more than 5 blocks from it. The other side's front rows go for them as for any foe.
  Above the hotbar, in the faction's colour, they're told what their block is doing: **Charge!**, **Pullback!**,
  **Rotate!**, **Retreat!**. In the middle of the screen come the Marshals' and general's orders: **ALL TROOPS
  CHARGE!** when the army advances, **Hold the left wing!** when their wing holds for a bait, **Target ...!** when
  their block is sent at an enemy block, **Kill General ...!** when it goes for the enemy general. Officers'
  orders come at most one every 3 seconds; a newer one waiting replaces the older.
- **Charges hit at impact:** a real rider's charge bonus (from its block's momentum, or a cavalry player's or their
  riders') counts only while its horse is at a gallop (0.15 blocks a tick or more); a rider standing in the melee hits
  like anyone else.
- **Jobs** (`character/Job`, `/pve job [cavalry|footman|archer|shieldman|heavy_infantry]`, saved with the rank;
  heavy infantry by default): what an enlisted player and their soldiers fight as. Each soldier given to the player
  is remade as the job's kind where it stands, with the health it had, and the player gets the job's kit, taken back
  when the battle ends:
  - *Cavalry:* horsemen; the player rides a saddled horse in the uniform's colour. Cavalry momentum works for the
    player's horse and, as one, for their riders as for cavalry blocks: gathered riding (full after 20 blocks), it
    makes their blows up to three times as hard; each second in melee spends 0.3 of it (0.6 against cavalry), and
    spent in melee their horses slow to half pace.
  - *Footman:* swordsmen with iron swords; the player gets an iron sword with Sweeping Edge III.
  - *Archer:* bowmen; the player gets a bow with Infinity and Unbreaking II, and an arrow.
  - *Shieldman:* shieldmen with the nation's banner on their shields; the player gets such a shield.
  - *Heavy infantry:* shieldmen in iron armour (the captain keeps the banner on the head) with iron axes and the
    nation's shields; the player gets the same.
  A soldier of a player's unit with its shield up (Recruits' Shields command; the battle no longer raises them
  itself) is braced like a wall against riders striking its front: it takes 40 % of the blow and the rider's charge
  is spent, and it hits riders in front of it 2.5 times as hard. A player's own soldiers stay real however far off.
- **Roles** (`character/BattleRole`, `/pve role [frontline|bait|support|flanker|kill_squad]`; frontline by default):
  where the player's unit starts and what the numbers have it do (`FieldBlock.Task`); the general leaves its blocks
  to it (no relief duty, no pincers or envelopments):
  - *Frontline:* the front block nearest the middle, the first to charge.
  - *Bait:* that front block becomes its wing's bait, whatever the general's plan: it runs ahead of the line and,
    once fighting, falls back to draw the enemy on.
  - *Support:* a reserve block near the middle; once the lines have formed it goes for the enemy fighting the
    nearest block of its wing that is losing (under 60 % strong, or outnumbered by its foe).
  - *Flanker:* a reserve block on a wing (the outermost); it strikes the nearest enemy infantry in a fight (within
    200 blocks) from the side nearer it, or from behind if others strike it already.
  - *Kill squad:* a reserve block near the middle; it goes for the enemy general within 200 blocks, chasing them,
    else the nearest enemy block whose captain stands, those fighting first. Its player hears "Kill General ...!".
  Blocks sent this way strike as the general's do: to the target's flank or rear, facing it, and fighting there;
  the player's square follows the ring there, and the player hears "Charge!" once they're fighting.
- **Shields:** a soldier with a shield raises it within 50 blocks of enemy archers, or with an enemy within 12 blocks
  that it isn't striking itself, so long as it is near its place (a raised shield slows it to half pace or less). A
  charged or passive soldier lowers it. Charged soldiers go after real enemies within 80 blocks (was 40).
- **Facing the enemy:** real soldiers spawn facing their block's enemy (the block it fights, else the nearest), and
  those standing in their places with nobody to fight turn to face it each second.
- **Battle soldiers can't be hired:** right-clicking a battle soldier, or its horse, does nothing (Recruits would
  otherwise open its hire menu).
- **Battle soldiers can't pick up items, and drop nothing when they die**, so nobody swaps the uniform for loot and
  the field stays clear.
- **Line fighting:** front blocks march in line until an enemy is within 10 blocks, then engage: they keep pressing on,
  still in their ranks, until their front rank is 2 blocks from the enemy's, so the lines actually meet. Recruits
  soldiers holding a place take on whoever comes within reach and return to it, so the line holds instead of
  dissolving into a melee. A block whose enemy in front is gone marches on: straight ahead while the nearest enemy is
  ahead of it, else toward that enemy, so a line that has broken through turns on what is left instead of walking
  off the field.
- **Front rows fight:** where two blocks of real soldiers meet, only the front row of each fights (to spare the
  server): the two ranks nearest the enemy (20 soldiers; 10 in a wall) each go after the nearest enemy within 12 blocks
  of their place, breaking ranks to reach them, and return to it when there is none. The enemies at hand are those of
  the block it fights and of any real enemy block within 24 blocks, so blocks fight what is near them even before
  the numbers have them in contact. The ranks behind hold their places, passive, until the front thins and they
  become it, but fight any enemy that comes within 4 blocks of them. The numbers leave these fights to Recruits; the reports
  count the blocks fighting front to front. For the battle the two factions are enemies in Recruits' diplomacy; when
  it ends or is cleared, their old relations come back.
- **Relief:** a front block down to half its soldiers is relieved by the first fit block (at least 60 % strong)
  waiting in its file, or the nearest fit block from another file. When the relief arrives (or after 20 s) it takes
  over, and the worn block withdraws behind the last block of its file, passive on the way. It stays there, fighting
  only if the enemy reaches it. This rotation keeps fresh soldiers at the front and makes a big battle last; with no
  fit block left, a front block fights on.
- **The general** gives every block an order every second (`battle/BattlePlan.order`). They are a mounted Recruits
  soldier at the top level (20 by Recruits' default config, about 80 health) in iron under a gold helmet, with an iron
  sword, who rides 20 blocks behind the middle of the front, out of the enemy line's reach. **If the general falls,
  their army retreats at once.**
  - **The guard:** 20 riders in full iron, their horses in iron too, ride just behind the general. They ride out at
    any enemy within 15 blocks of the general, and at cavalry charging the general from 40, and reform at the
    general's back. Cavalry charging the general sends them to shelter behind the nearest of their own infantry.
    The guard shields the general wherever it is: anything charging or riding at the general within 40 blocks is
    met by the guard first, even if the guard is in the middle of a charge itself.
  - After 5 s in formation, the front line marches 2 blocks a second, then fights in line.
  - Archers march until an enemy is within 30 blocks, then halt and shoot.
  - **Wings:** an army of more than 300 soldiers fights as a left wing, a centre and a right wing: the left and right
    quarters of its front files and the middle half. Each wing's cavalry acts on that wing's fighting, and each wing has its
    own bait and pincer, so several actions go on at once across the field. A smaller army is all centre. The chat
    says which wing did what.
  - Cavalry waits in its wing's rear reserves until its own wing's front is fighting, or an enemy comes within 20
    blocks, then rides for the enemy archers.
  - **Wedges form once the front line is established:** a wedge (Great Spear, Unrelenting Charge) doesn't stand at the
    start; its blocks wait in the reserves (the general and guard in their usual places) until both armies' front
    infantry have met and fought for 5 s. Then they ride to their places in the wedge, far behind the middle of the
    front, and only then does the wedge charge.
  - Cavalry that has charged sends any soldier with no target after the nearest enemy left. Charging cavalry that
    runs into an enemy block on the way to its target is caught fighting it, and can't ride through.
  - **Cycle charge:** a charge builds momentum over a 20-block run-up, and a rider hits up to 3 times as hard with full
    momentum. Each second in contact loses some (0.3 against infantry, 0.6 against cavalry; all of it against a braced
    wall from the front), and spent riders' horses slow to as little as half pace, so cavalry can't ride through a
    line. Once below 0.15 after 2 s of contact the cavalry rides back to its wing's backline (behind the last infantry
    line; at most 20 s) and charges again once 5 s have passed. Cavalry that has charged never stands still: it
    rides back and forth along its wing's backline, 12 blocks each way, keeping its momentum up until it goes in
    again. (A wedge rides 35 blocks straight back instead, and waits in its wedge.)
  - **Infantry against stopped cavalry:** enemy cavalry that moved less than a block in the last second (caught in a
    melee, or waiting) is set on by up to two idle infantry blocks within 25 blocks (front blocks not fighting, or
    blocks waiting in the files), which close in on its flanks. They let it go once it rides on at 3 blocks a second
    or more, or leads them more than 50 blocks off.
  - **Envelopment:** once the army is fighting, a front block with no enemy ahead of it and not fighting anyone (its
    wing reaches past the end of the enemy's line) wheels onto the nearest enemy infantry beside it, within 60
    blocks: the first block strikes its flank, a second its rear. When that block falls it turns on the next, so an
    overlapping wing rolls up the enemy's exposed flank; the blocks waiting in its file follow it round.
  - **A moving front:** a front block with fewer than 70 % of the soldiers of the block it fights gives ground, half
    a block a second, while the stronger side presses on, so the line bends where one side is winning.
  - **3 deep:** an army of more than 1000 soldiers stands 3 lines deep (as far as a front of at most 40 blocks
    allows), so each wing has a broad front of its own.
  - **The general keeps clear:** the general stands 20 blocks behind the middle of the blocks holding the line,
    and falls further back wherever the enemy has pushed in, keeping at least 25 blocks from any enemy.
  - **Walls:** an infantry block that charged enemy cavalry is heading for (within 30 blocks), or that cavalry comes
    within 10 blocks of, turns to face it and closes up 5 wide at 1.3 spacing. A braced wall takes 40 % of cavalry's
    damage and deals it 2.5 times as much; it stands down 5 s after the last threat. The wall is one-sided: struck
    from behind or the flank it gets no bonus.
  - **Horns:** every faction has one of Minecraft's eight goat horn calls. The general sounds it for each big order
    (the advance, a cavalry charge or recall, a wall, the general charging, the final charge), heard 256 blocks
    around, at most once every 5 s; a fallen general sounds none.
- **Characters and doctrines** (`character/GameCharacter`, `battle/Doctrine`, `battle/WarPlan`): a character is a
  king, merchant, warrior, politician, tactician, advisor or general; only generals act yet. Each faction follows
  doctrines (saved with it; all three for now), and each battle's general is made for it with a name and two of
  their faction's doctrines, chosen at random. They fight with every formation and strategy of both. The start of a
  battle names each general and their plan, and the chat says when a strategy acts. The order itself is shown in the
  general's nation's colour.
  - **Spear and Shield**
    - *Great Spear* (formation): once the front line is established, all the cavalry forms one wedge of blocks at
      least 45 blocks behind the front, behind the reserves. It charges as one, in formation, and breaks up on
      striking. Its riders gather up to twice the momentum (5 times the damage at full), over a 40-block run-up.
    - *Great Shield* (formation): every infantry block bears shields and re-forms 4 wide and 5 deep. Each front file
      is 3 blocks closed up one behind the other, 15 ranks; relief comes from the block right behind. The front is
      narrower for it. Blocks waiting in a file reach no further than a broken formation, so only the front block
      fights.
    - *Decapitate*: the wedge rides at the enemy general once the lines are fighting; charging cavalry within 60
      blocks of the enemy general goes for them.
    - *Mass Casualties*: soldiers who break formation, and the wedge, go for the most enemy soldiers within reach for
      the distance; archers shoot the enemy block with the most soldiers in range.
    - *Cavalry Flank*: cavalry charging infantry first rides in formation out to a point 15 blocks beyond the side of
      its target's line, then turns in, so it strikes the flank, where no wall stands. A wall gives its bonus only
      against cavalry striking its front, and a block fighting to its front can't turn to brace.
  - **Charge and Decapitate**
    - *Unrelenting Charge*: once the front line is established, the general, their guard and half the cavalry (the
      centre's reserves first, then the left's and right's in turn) form a wedge far behind the middle of the lines,
      the guard at its tip and the general straight behind it; the rest of the cavalry stays in the left and right
      wings' reserves, fighting for its wing. The wedge's first half, which the general leads with their guard, then
      charges. The general never rides on without the guard
      ahead: if the guard turns off to meet a threat, or falls, the general drops out of the charge, and in the
      fight the general keeps right behind the guard and falls back when it does. When the first half is spent and
      riding back, the second half goes in, then the first again, and so on. While the general
      leads, their own soldiers within 15 blocks take 70 % of the harm done to them. The general and their guard
      spend momentum at half the rate of other cavalry (half of it, not all, against a wall's front), so they cut
      deeper before falling back.
    - *Precision Strikes*: cavalry strikes the most exposed enemy block, even before the lines meet: one 6 blocks or
      more ahead of its own line, fighting with a flank open (an end of the line, or a fallen neighbour), or worn to
      half its soldiers.
    - *Decapitate*: once the enemy general stands exposed (none of their army but the standard within 15 blocks),
      the unrelenting charge rides at them.
  - **Bait and Pincer**
    - *Bait*: small baits, one in each wing with at least 3 front files: that wing's middle front block runs ahead
      of the wing's line, up to 12 blocks in front (the files behind it stay). Once fighting, it falls back a step a
      second, still fighting, to 10 blocks behind the line. The rest of the wing holds its ground meanwhile, fighting
      only what comes within reach.
    - *Pincer*: in each wing, an enemy block drawn on by its bait to within 20 blocks of it, and 6 or more ahead of
      its own line, is struck with what that wing has to hand: in the centre the wedge, on a wing that wing's
      cavalry, charges it if ready; else three of the wing's idle infantry blocks surround it (both flanks and the
      rear), else one strikes its flank. A pincer gives up after 40 s.
- **The sound of the battle** (`client/BattleSounds`), from the formations the client is told about, so blocks that are
  only numbers are heard too: a cavalry charge from the 8 nearest cavalry blocks riding at a gallop (each sound rides
  along with its block and fades out when it slows), and the din of a medieval battle from the 12 nearest stretches of
  front of up to 5 infantry blocks, about every 100 soldiers. Both are louder the more soldiers there are. They are
  recordings cut into mono 10 s clips that fade in and out (`assets/rafasconquest/sounds/battle`); each source starts a
  new random clip before the last ends, so it never falls silent and no two sound alike.
- **Archers never march past their own front line**, so they don't walk out alone while the infantry waits.
- **No open files:** a front file whose block has fallen, with no fit block left to send, is filled by any block still
  in the lines (worn or not), else the nearest resting block.
- **The end:** an army whose general falls retreats; one worn to 20 % strength routs. Either way its survivors turn
  passive and run back the way they came. If nobody has fallen for 30 s once fighting has begun, the lines have
  stalled and everyone left charges, except the generals, their guards and the standards. After 30 minutes the
  battle is called a draw.
- **Reports:** every 10 s the chat and server log get the soldiers left on each side, the blocks relieved so far, the
  server tick time, and how much of it the battle's own orders take. The end says whether each general and standard survived. `/pve debug battle clear` removes every soldier and horse
  the battle spawned. Only one battle runs at a time.
- **Preparing, then starting:** `/pve debug battle ...` sets the armies up and spawns them, but nobody moves or
  fights until the battle is started, with the battle map's Start battle button or `/pve debug battle start`.
- **The battle map** (`dev/BattleMap`, page `assets/rafasconquest/web/battle_map.html`): a developer's web page at
  http://localhost:8099/ (this machine only), linked from the chat when a debug battle is set up. It shows every
  block of the battle as it stands, a few times a second: the top army green along the top half, the bottom army red
  along the bottom, each block its real width and depth, turned the way it faces (its front edge bright), labelled
  with its kind (INF, SHD, ARC, CAV, GEN, GRD, STD) and soldiers left, darker as it loses them. Dotted lines run to
  where each block is ordered to stand; red arrows to whom charging cavalry is going for, orange to whom a block was
  sent to strike (envelopment, pincer, stopped cavalry). Blocks fighting are outlined red, braced walls blue.
  - Click a block for its details (its name, kind, duty, wing, soldiers, where it is and is ordered to, what it is
    doing and going for); Shift+click adds to the selection, dragging on empty ground box-selects, Escape clears.
    Right- or middle-drag pans, the wheel zooms, Fit view fits the battle.
  - **Placing blocks:** before the battle starts, drag the selected blocks to place them. A placed block keeps its
    place when the battle begins (`FieldBattle.place`): a front block marches from there, a block waiting in a file
    keeps its place behind its file's front block, the wedge, general, guard and standard theirs relative to the
    front and the general, other cavalry waits there and patrols the backline behind it.
  - **Groups:** select blocks of one army, name them and save; groups are listed (hover to highlight, click to
    select, × to delete) and saved with formations, so they can be referred to when working out tactics.
  - **Formations:** save where each of an army's blocks stands, with its groups, under a name; load one into
    either army before the battle starts. They are JSON files in `config/rafasconquest/formations/`: each block by
    name (its kind and number, e.g. `CAVALRY#3`, so it fits any army of the same make-up) with its arm, duty, wing,
    file, width and place, `right` and `back` of where its army's front centre deployed, in the army's own frame.
  - The page also lists both armies (faction, general, plan, soldiers) and the battle's chat log.
- **Battles as numbers** (`battle/FieldBattle`, the first step toward battles too big for live soldiers): the same
  battle fought block by block, one second at a time, with no entities. The generals give the same orders as in a
  live battle (`BattlePlan`); blocks walk to where they are ordered (3 blocks a second on foot, 6 mounted) and fight
  whoever is within reach (`battle/FieldRules`):
  - A block holding its place reaches 12 blocks (its soldiers step out and return); one that has broken formation
    must close to 2.5.
  - Each fighting soldier does 0.12 damage a second (cavalry 0.36, archers in melee 0.088), dealt in hits of 4 to
    random soldiers, so each soldier's health is kept. Archers shoot 44 blocks, an arrow a second doing 0.19 on
    average, until their 64 arrows each run out.
  - Cavalry momentum and braced walls work as in a live battle; a shieldwall takes 60 % from the front, and 40 % of
    arrows. The general fights like 4 soldiers.
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
    Live battles also vary much more. (Those were measured with two kinds of general and lines that stopped short of
    each other; the rules have changed since.)

  `/pve debug battle simulate <first> <soldiers> <second> <soldiers>` fights one at once and reports the outcome.
  2000 against 2000 lasts several minutes of battle, with dozens of blocks relieved on each side, and takes a fifth of
  a second to work out. Searches for the nearest enemy look only at the blocks filed near it (`battle/BlockGrid`), and
  only as far as matters: 10,000 against 10,000 costs about 6 ms per battle-second, 50,000 about 28, 100,000 about
  68 (at worst 100). Battles over 2000 a side aren't also fought through as
  numbers at the start for comparing (it would hold the server up).
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
- **Arrows in flight:** archers that are only figures shoot arrows you can see. Each second the server tells clients
  which formation shot at which and how many arrows (only the figures' share of a block that is part real; its real
  archers shoot real arrows). Up to 24 arrows of each volley leave the archers' figures over the second, arc up to
  18 % of the distance (at most 25 blocks) and come down across the target formation, at 30 blocks a second
  (`client/ArrowVolleys`). They're drawn as streaks of head, shaft and fletching along their flight, at least about
  1.5 % of their distance long so volleys show from far off, and pulled in like the figures beyond what the camera
  sees.
- **The handover:** with Distant Horizons, the game's world fades into its terrain near the render distance, and it
  would fade real soldiers with it (from 1.5 times its near clip distance plus a chunk; about 45 blocks at 6 chunks).
  So real soldiers hand over to figures where that fade starts, if it is nearer than 67 blocks, and past the handover
  the real soldiers and their horses are hidden. The figures are drawn after the fade, so they stay solid. The camera
  sees only 4 render distances; figures further away are pulled in along the line of sight and shrunk to match, so
  they look the same on screen (`battle/DistantDepth`). Without Distant Horizons there is no terrain beyond the render
  distance, so figures out there stand over empty sky.
- **Real soldiers keep their ground loaded:** the chunks around every real soldier stay loaded and running, the way
  `/forceload` does, while they stand there (not saved with the world), and a block's ground is loaded before its
  soldiers are spawned. Blocks that are only numbers need no ground; they are drawn at the ground's height from the
  world where it is loaded, else from the world generator (as Distant Horizons draws it), without loading anything.
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
political map in the style of Crusader Kings 3, out to four or eight times the storm wall's radius by province size (16,000 or 32,000 blocks at default settings, a pixel every 32 or 64 blocks),
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
- **Centres:** they sit on a seeded, jittered grid every 768 or 1,536 blocks (province size 1 or 2). Deserts, badlands, snow and peaks drop half of
  theirs, so provinces are larger there. Every city (City-state home or Empire capital) is a centre itself.
- **Shapes:** each land pixel joins the centre that is cheapest to reach in a straight line. The sea costs a lot (coasts
  become borders, and islands get their own provinces). A ridge rising above both ends of the path costs (mountain
  ranges become borders). Crossing a biome edge costs a little (borders lean onto rivers and biome edges).
- **Smoothing:** two passes give each pixel the province most of its neighbours have, which clears ragged edges.
- **Tiles:** everything a pixel looks at is within three times that, so any area computed with that margin matches
  the whole map. That allows tiled provinces beyond the map later.

**Who holds a province** (`map/PoliticalMap`) is the nation that took up its title on the base map (see **Nations**
under **Land titles**).

**Farms belong to their province's holder** (`PoiPrecomputer.farmOwner`), so the map and the economy always agree, and
a City-state can hold Farms in its provinces:
- A Farm off the map's edge, or in a province nobody holds, has no owner.
- `gradlew mapPreview` reports any Farm whose owner disagrees with the map.

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

## Land titles

The land is divided into titles once, when the world map is drawn, from its provinces alone (`core/TitleMap`); nations
take them up afterwards, not the other way round. Provinces group into **counties** (2 to 6 neighbouring provinces),
counties into **duchies** (3 to 6), duchies into **kingdoms** (2 to 4) and kingdoms into **empires** (3 to 6). Each group
grows from a random start through its neighbours to a size drawn in its range; a group left too small joins its
smallest neighbour, and one with no neighbour (an island) stands alone. Every title has a **seat**: a county's is one of
its provinces, a duchy's one of its counties' seats, a kingdom's one of its duchies', an empire's one of its kingdoms',
so an empire's seat is also the seat of its kingdom, duchy and county. Seats are drawn at random, provinces on the
shore three times as likely. A title is named after its seat ("Duchy of Shukai"); provinces are named from their centre
and the world seed in the style of the factions' names (`core/ProvinceNames`). The titles and names are saved and sent
with the world map (`map/BaseMap`).

On the world map (`client/map/TitleLayer`, `map/TitleStyle`) a faint line runs between counties, a darker one between
duchies, a heavy double line between kingdoms and a dark red one between empires. Each seat is marked, larger and
brighter the higher its title (county seats once zoomed in); hovering over one names every title seated there.

**Vanilla villages are off:** the mod overrides vanilla's village placement (`data/minecraft/worldgen/structure_set/
villages.json`) with no structures in it; the mod's own settlements will take their place.

**Seat cities** (`core/SeatCities`) are planned with the titles: N × N districts of 160 blocks (a district is 5 × 5 city
blocks), by the highest title a city seats: a county's Town 2 × 2; a duchy's 3 × 3, or 4 × 4 with 5 or 6 counties; a
kingdom's 5 × 5, one in four 4 × 4; an empire's 7 × 7, 8 × 8 with 5 kingdoms, a 9 × 9 metropolis with 6. Cities are placed
highest title first, each as near its seat province's centre as it can while standing wholly on land and 64 blocks
clear of the others, so a seat by the shore draws up to the water's edge. A seat with no room moves to another province
the title could seat (one of its lower titles' seats), so seats stay nested; a title with no room anywhere has no city.
A big city spreads over the provinces around its own, which have it as their structure. The cities are saved with the
map and drawn on it as squares in the colour of their highest title, with the seat marks on them.

World generation needs the cities before the first chunk is made, so a world's map is now **loaded or drawn when the
overworld loads** (`BaseMapStore.loadOrDrawNow`), not in the background: a new world takes about 20 s longer to open the
first time. Provinces are drawn from the land alone now, no longer centred on the old city sites.

**The seat cities are the cities built.** World generation builds each one where it was planned: N × N cells of 5 × 5
city blocks, each cell its own structure start (the one holding the middle block has the town centre), every road,
plot, lamp and wall segment, and the terrain (plate, carve, fill, valley) sized to the city. A city takes the style of
its centre's biome, or the plains style where none matches; one whose box would touch ocean is not built. A city
belongs to the nation holding its province (its banners painted in its colours, its walls guarded); one nobody holds
stands unowned.

**The countryside** (`core/Countryside`, built by `worldgen/CountryStructure` and `CountryPiece`, all in code): every
province with no city on it has one settlement half the time (the other half is wilderness), planned with the cities and saved with the map. Most are a **hamlet**: a
5 × 5 shack (plank walls on log corners, a slab roof, a door, a barrel and a crafting table) and a fenced sheep pen beside
it with hay and 2 to 4 sheep. Some are a **small village**: 3 to 6 hamlets in a ring 18 blocks about a well, their pens
toward it; a province is one 30 % of the time unless a neighbour already is, so villages never neighbour, and villages
turn back into hamlets until hamlets outnumber them at least two to one. About one hamlet in eight is a **watchtower**
instead (one in four on a kingdom's edge, one in twelve inside): a 5 × 5 stone shaft 12 high with a door, a ladder and a
crenellated lookout. About one county Town in four is a **castle** instead of a city (two in five on a duchy's edge,
one in eight inside): a curtain wall 41 blocks square with a gate, a tower at each corner and a keep with an upper
floor; the land the Town would have spread over gets settlements of its own. Each settlement stands as near its
province's centre as it fits on land, clear of cities, and each building on ground levelled to its own height (filled
up to 12 below, cleared above). On the world map, zoomed in, each has a mark, named on hover with its province.

**Nations** (`core/Nations`) take up the titles once they are drawn, by zone, judged from each title's seat:
- **The eye** (within the eye radius of spawn): nobody's.
- **The war zone** (out to its outer edge): a kingdom is held whole by a **small nation** 40 % of the time; otherwise
  each of its counties is a **City-state** 35 % of the time, and the rest is nobody's.
- **The outer world:** an empire is held whole by one great nation 35 % of the time, which takes "Empire" into its
  name ("Branundun Empire"); otherwise each of its kingdoms is a **large nation** 70 % of the time.
- **Mini-eyes:** about one outer kingdom in four is the centre of a calm as big as the eye, at least four eye radii from
  the next and clear of the war zone, where nobody holds anything.

A nation holds its title and every province under it, outside the calms; its capital is its title's seat city (a
City-state's, its county's Town). The nations are planned and named with the map, saved in it (with which nation holds
each province) and made factions when the server starts (`PoiPrecomputer.ensureNations`), replacing the old ring of
Empire capitals and seeded City-state sites. Land, Farms and cities belong to their province's holder.

**Unowned seats:** a seat city in a province nobody holds is a **ruin** 60 % of the time (no city, just a single
empty shack where it would have stood: no torch, no pen, nobody in it), is **not there at all** 25 % of the time, and
otherwise still has people living in it, with no nation over them.

## Characters, cultures and faiths

Every hold (a capital, city, town or castle) has a **holder**, planned with the world map from the seed
(`people/RealmPlanner`), kept and sent with it (`people/Realm`). Holders only for now: no families or courts.

- **Who:** a name in their culture's sounds, sex, age (18 to 70), the hold they keep, and the nation they serve. A
  nation's ruler holds its capital: Emperor, King, Duke or Count by the nation's rank. Others are titled by their hold:
  a city's a Duke, a town's a Count, a castle's a Lord (Duchess, Countess and Lady for women).
- **Skills** (`people/Stat`): Diplomacy, Martial, Stewardship, Intrigue and Learning, 0 to 20, around 6 with one they
  were schooled in higher. **Prowess** runs 0 to 45: ordinary people stay within 20, but about one in twenty is
  superhuman, each step rarer, and their prowess grants a title: 21, **Sword Saint** (they have achieved a miracle
  in their prowess); 30, **Sword King** (their sword possesses the might of a nation); 40, **Sword God** (their sword's
  power is biblical). These are martial titles, and the world has only so many of each: seven Sword Gods, three
  times as many Sword Kings (21) and three times as many again Sword Saints (63). Someone rising into a full title
  stays at the top of the one below.
- **Traits** (`people/Personality`): three personality traits, from opposed pairs (Brave or Craven, Calm or Wrathful,
  Content or Ambitious, Honest or Deceitful, Zealous or Cynical, and so on; never both of a pair). Each bends a few
  skills.
- **Lieges:** a holder answers to the holder of the next higher seat above them in the same nation (a county's town to
  its duchy's city, up through kingdom and empire seats), else to the ruler. Holds no nation keeps answer to no one.
- **Friends and rivals:** among each holder's nearest neighbours and those serving the same liege, at most two of each;
  shared traits make friends likelier, opposed ones rivals.
- **Opinion** of one character for another, -100 to 100: shared traits please and opposed ones grate; a shared faith
  pleases (a Zealous character doubly, a Cynical one not at all) and another faith grates; so, less, with culture; a
  vassal thinks well of their liege; friends and rivals most of all.

**Sword sanctums:** three in each world, in the hearts of the mini-eyes (a kingdom's seat), or at other kingdoms' seats
in the outer world where there are too few eyes. Each is a City-state, the "Sword Sanctum of ...", holding the county
round it, calm or not; its city is 4 × 4 districts; and it is held by a Sword God, styled by that title, who is away
from it about one time in three.

**Mantra schools and their dojos** (`core/Dojos`): ten schools of an offensive magic system, each with one dojo,
placed with the world map on dry land clear of the cities and the eye of spawn, at least 7,000 blocks from each other
and from the sword sanctums. Seven are known (Fire, Water, Wind, Lightning, Ice, Stone, Iron): their dojos stand on
the surface (a pillager outpost's buildings, without pillagers, for now) and are marked on the world map. Three are
hidden (Blood, Shadow, Light): their dojos are underground (a stronghold, for now) and on no map. A world too small
for all ten loses hidden dojos before known ones. The schools teach nothing yet.

**Cultures:** one for the lands of each empire title, with its own name and the sounds its names are made of. A nation
has the culture of its seat's lands, and every one of its characters has it; a hold no nation keeps has its lands'.

**Faiths:** 3 to 5 broad faiths, each held over the lands nearest where it began, and 4 cults, each from a home in one
empire's lands. A nation keeps its seat's faith with a piety of its own (worldly to devout). Its characters mostly
share it, between 70 % (worldly) and 95 % (devout) of them; the rest keep a cult (their lands' own, if there is one)
or another faith.

**On the world map**, hovering over a seat or a castle names its holder; a double-click opens their **character page**
(`client/CharacterScreen`): their skills (prowess with its rank), traits, culture and faith (marked if not the
nation's), and their liege, vassals, friends and rivals, each with what this character thinks of them. Any of those
names opens that character's page.

**In battle**, an army's general and its three Marshals are its nation's own holders, the best Martial commanding and
the next three leading the wings, each officer's war-council trait from their personality (Deceitful or Paranoid:
cunning; Arrogant: proud; Brave, Wrathful or Impatient: bold; Craven, Patient or Calm: cautious). A nation with too
few holders, or a faction not on the map, makes up the rest as before.

## Cities (City-states and Empire capitals)

> Since the land titles, the cities built are the titles' seat cities, N × N cells by title (see **Land titles**); what
> follows describes how any city is laid out and built, with a 3 × 3 city's numbers. Where City-states and capitals are
> placed now only decides where their factions start.

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
