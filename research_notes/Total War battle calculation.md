# How Total War calculates battles between units

Researched 2026-11-11. Most detail is for Warhammer II/III, the best-documented games. Rome II, Attila and Three Kingdoms use the same model with other numbers. The older games (Shogun 1, Medieval 1, Rome 1) work differently; see the end.

**How sure:** the 35% base and the "damage minus armour, AP ignores armour" split come from Creative Assembly's own Feature Focus #2 post. The 8–90% clamp, the armour roll and the leadership numbers come from the fan wiki and players. Nobody outside CA knows the exact numbers.

## 1. Combat is per soldier, not per unit

Each soldier ("entity") has its own HP, picks an enemy soldier, and swings on a timer (the *melee interval*, a few seconds). A swing that lands plays a matched animation. A unit fights only where its soldiers touch the enemy, so frontage, depth and formation matter. Every swing is resolved like this:

## 2. Does the swing land?

```
hit chance = 35% + (attacker melee attack − defender melee defence), kept between 8% and 90%
```

- Both stats are taken after buffs, traits and **fatigue** (a tired unit loses attack and defence).
- A charge adds the **charge bonus** to melee attack (see 4).
- Hits from the flank or rear cut the defender's melee defence and skip the shield, so they land more often. Same damage per hit, just more hits.
- Fan-wiki example: Temple Guard (attack 32, defence 38) against Rat Ogres (26 / 26): 35 + (32 − 26) = 41% for the Guard, 35 + (26 − 38) = 23% for the Ogres.

## 3. How much damage the hit does

A weapon has **base damage** and **armour-piercing (AP) damage**. Armour reduces only the base part:

```
damage = base × (1 − min(armour × random(0.5, 1), 100)%) + AP
```

- Armour 100 removes 50–100% of base damage on each hit, a random amount each time. Armour 200 removes all of it.
- Every weapon has some AP, so even heavily armoured units take damage.
- **Bonus vs large / bonus vs infantry** adds damage, split between base and AP in the weapon's own ratio.
- Then come Warhammer-only layers: ward save, physical, magic and fire resistance (a percent cut, capped).
- Damage beyond what kills the soldier is lost.
- Shields only stop missiles from the front (a 120° arc), as a block chance. They don't change armour.

## 4. Charges

- **Charge bonus:** a unit that charges into contact adds its charge bonus to melee attack and to weapon damage, split base/AP in the weapon's ratio. The bonus fades over about 10–15 s. That is why shock cavalry pulls out and charges again.
- **Impact (collision) damage:** comes from speed × mass when the charge hits. About 70% of it is AP, and it can knock soldiers down. In Warhammer II, CA took charge bonus out of the collision maths, leaving mass and speed. The tuning factors are in the `kv_rules` tables.
- **Bracing:** a unit with *charge defence* that stands still facing the charge cancels the charger's bonus. A charge into its flank or rear still gets the full bonus.
- Charging costs a lot of fatigue.

## 5. Missiles

- Each shot is a real projectile that flies on a path. Accuracy comes from hidden stats: **calibration distance** (the range it fires accurately at) and **calibration area** (how wide shots scatter around the aim point). Beyond the calibration distance, aim falls off fast.
- If a shot hits, the front shield gets its block roll, then armour and AP work as in 3.
- Being shot at by archers or artillery costs about −8 leadership, even if nothing hits.

## 6. Leadership (morale): what actually wins battles

Most units don't die. They **break**. Leadership is a number with many modifiers added together, and states follow from it: steady → wavering (banner flashes, the unit drops its orders) → routing → shattered (can't rally). Known modifiers:

| Modifier | Effect |
|---|---|
| Recent casualties | big; both how much it lost and **how fast** |
| Army-wide losses | Attila table: −5 at 20% lost, rising to −60 at 80%; past a point the whole army wavers |
| Attacked in flank or rear, or engaged from more than one side | large penalty |
| Flanks secured by friendly units nearby | bonus |
| Its general (lord) dies | army-wide penalty; in Rome II, units routing when it happens shatter |
| Allied units routing nearby | penalty, so one rout can set off others |
| Fear (constant nearby) / Terror (charge can force a short rout) | penalty |
| Tired or exhausted | penalty |
| Charging | +15 until the charge bonus wears off |
| Under missile or artillery fire | about −8 |
| Encouraged by the general or heroes nearby | bonus |

- A routed unit rallies once it is far enough from the enemy and its leadership comes back.
- A unit that breaks three times (or takes many penalties at once) **shatters** and won't come back.
- Undead and daemons don't rout. They lose HP instead.

## 7. Autoresolve

This is a different system from the real-time battle. CA has never published how it works. Modders say each army gets a "power" number from its unit stats (armour and ranged AP count heavily) and a few factors in database tables. Then damage is given out to all units at once, with no tactics or positions. Medieval and Rome-era autoresolve also counted difficulty, experience, ambush or flank, and charge.

## 8. Older games (Shogun 1, Medieval 1, Rome 1)

- Nearly every soldier had **1 HP**: a hit that got through killed him. Generals and bodyguards had a few HP.
- Each swing was a single kill roll using the attack − (defence + armour + shield) difference, with charge, height and flank modifiers.
- Morale was the same idea with flat modifiers. Medieval 1 had states Impetuous / Steady / Uncertain / Wavering / Routing, and nearby routing friends cost up to −12.

## What this means for RafasConquest's FieldBattle

Our model ([FieldRules.java](../src/main/java/rafasconquest/battle/FieldRules.java), [FieldBattle.java](../src/main/java/rafasconquest/battle/FieldBattle.java)) works per block and per second, not per soldier:

- **Damage:** a flat damage-per-second per arm × `meleeFactor`. There's no hit roll, no defence and no armour/AP split. Shields cut frontal melee to 0.6× and arrows to 0.4×.
- **Flanks:** `FLANK_BONUS` and `REAR_BONUS` are 1, to match Recruits. In Total War, flanking **does not** raise damage per hit. It works through (a) more hits, because of lower defence and no shield, and (b) a large **leadership** penalty. We already have (a) for shields. We don't have (b).
- **Morale:** a block routs if it loses 50% × nerve within 60 s, and a dead captain adds 0.25. That matches TW's "casualty rate" term. Total War also counts flanked or surrounded, general dead, nearby allies routing, army-wide losses and fatigue, and lets a unit shatter after it breaks several times.

The cheapest steps closer to Total War, all as morale terms and leaving the damage maths alone:
1. A flank or rear attack adds a shock, like `CAPTAIN_SHOCK`, while it lasts.
2. A friendly block routing nearby adds a shock.
3. Army-wide losses add a shock.
4. A block that has routed N times doesn't regroup.

## Design: adding the four morale shocks (built 2026-11-11, untested in game)

**Decided:**
1. Fright and army-loss shocks count everywhere, reserves included.
2. Levy is *expendable*: its rout frightens only other levy (`Arm.expendable()`).
3. A shattered block retreats to its wing's camp entrance (the centre's if its wing has none) and stays out for the day. Its soldiers count as lost for the army's 80% rout (`FieldArmy.standing()`). A new day's battle starts fresh.
4. Hovering over one of the player's own units on the battle screen for 3 s shows its state and each shock as a % of its breaking point.

5. All units have morale: foot, cavalry (the general's guard included) and archers. Only lone figures (the general, the standard-bearer) and a besieged garrison don't. Routed cavalry and archers regroup to their own duty, not into an infantry file, and a routed rider sits out the wedge charge.

**Correction:** there was no risk meter on the battle screen. `routRisk` is only used by RecruitsBattle's support helpers.

### The core change: morale becomes a sum

Today, `FieldBattle.morale()` and `routRisk()` each work out the same thing on their own: the share lost in the last 60 s, plus `CAPTAIN_SHOCK` while the block is shaken. Both get replaced by one helper:

```java
/** How hard a block is pressed: its recent losses plus every shock on it now. It routs at ROUT_LOSS × nerve. */
double pressure(FieldBlock b, double armyShock) {
    double p = recentLossShare(b);                               // as today
    if (seconds < b.shakenUntil)    p += CAPTAIN_SHOCK;          // as today
    if (seconds < b.flankedUntil)   p += FLANK_SHOCK;            // new 1
    if (seconds < b.rearedUntil)    p += REAR_SHOCK;             // new 1
    if (seconds < b.frightUntil)    p += FRIGHT_SHOCK;           // new 2
    return p + armyShock;                                        // new 3
}
```

The rule stays "rout when pressure ≥ `ROUT_LOSS` × nerve, unless rallied". `routRisk` becomes `pressure / (ROUT_LOSS × nerve)`, so the battle screen's risk meter and RecruitsBattle's support helpers (`SUPPORT_RISK`, around L4114/L4132) see the new shocks for free. Being rallied (`rallyAgainst`, `rally()`) should also clear `frightUntil`, the same way it clears `shakenUntil` today.

Every shock is an "until second N" int on `FieldBlock`, like `shakenUntil`, so it never stacks with itself. A second flank attack just keeps the timer going.

### 1. Flank and rear shock

- **Where:** the melee loop in FieldBattle (around L2990), straight after `block.contactFoe = foe`. It has to go before the `if (figures <= 0) continue`, or blocks of real Recruits soldiers would never register being flanked.
- **What:** `side = sideOf(foe, block)`. If FLANK, set `foe.flankedUntil = seconds + FLANK_LINGER`. If REAR, set `foe.rearedUntil = seconds + FLANK_LINGER`.
- **Proposed numbers:** `FLANK_SHOCK = 0.15`, `REAR_SHOCK = 0.25`, `FLANK_LINGER = 5`. Hit in front and flank together: losses + 0.15. Front and rear together: losses + 0.25. All three: losses + 0.40, so such a block breaks after losing only about 10% (× nerve) instead of 50%. Pincers become worth doing.
- **Melee only.** In Total War, arrows don't flank anyone.
- `FLANK_BONUS` and `REAR_BONUS` stay at 1. The damage maths is left alone.

### 2. Nearby rout (fright)

- **Where:** in `rout(army, block)`, after the existing code.
- **What:** each block of the same army that is alive, not ROUTED, FLEEING or WITHDRAWING, and within `FRIGHT_REACH` of the routing block gets `frightUntil = seconds + FRIGHT_SECONDS`.
- **Proposed numbers:** `FRIGHT_SHOCK = 0.15`, `FRIGHT_REACH = 20`, `FRIGHT_SECONDS = 30`.
- **Chain routs** are the point, but they have to stay bounded. The fright timer doesn't stack, and 0.15 alone never routs anyone. A neighbour only breaks if it was already near breaking.

### 3. Army-wide losses

- **Where:** once per `morale(army, ...)` call: `lostShare = 1 − army.alive() / army.started`, then `armyShock = ARMY_SHOCK × lostShare`, passed into `pressure`.
- **Proposed number:** `ARMY_SHOCK = 0.3`. That gives +0.15 at half the army lost and +0.24 at 80% lost. The army itself already routs at 80% lost (`BattlePlan.ROUT_SHARE`), so this only makes the last stretch crumble faster.
- It's a sum, not a scaling of `ROUT_LOSS`, so it shows on the risk meter like the other shocks.

### 4. Shattering

- **Where:** new field `int routs` on FieldBlock, increased in `rout()`. In the ROUTED case of the duty switch (around L1198), when the regroup time is up, a block with `routs >= SHATTER_ROUTS` doesn't rejoin its file.
- **Proposed number:** `SHATTER_ROUTS = 3`, as in Total War.
- **Not yet decided:** what a shattered block does (see the questions below).

### Tests (FieldBattleTest)

- Two identical fights, one hit in the flank: the flanked block routs sooner.
- A block routs: a friendly block within 20 gets `frightUntil`, one 40 away doesn't.
- `routRisk` rises with army losses while the block's own losses stay the same.
- A block that has routed three times stays ROUTED after `REGROUP_SECONDS`.

All the numbers are first guesses to tune in game, the same as `CAPTAIN_SHOCK`.

## Sources

- [CA Feature Focus #2: Damage](https://totalwar.com/?p=19917) (official; the 35% base and the damage/AP split)
- [TW:WH wiki: Combat](https://totalwarwarhammer.fandom.com/wiki/Combat), [Melee Attack](https://totalwarwarhammer.fandom.com/wiki/Melee_Attack), [Charge Bonus](https://totalwarwarhammer.fandom.com/wiki/Charge_Bonus), [Leadership](https://totalwarwarhammer.fandom.com/wiki/Leadership)
- [Rome II manual: Morale](https://r2enc.totalwar.com/en/manual/single-player/0087_enc_page_battle_play_phase_conflict_morale)
- [TW modding wiki: Missiles and You](https://tw-modding.com/wiki/Tutorial:Missiles_and_You)
- [Steam: WH3 shield block discussion](https://steamcommunity.com/app/1142710/discussions/0/3948028800072759169)
- [PCGamesN: WH2 cavalry overhaul](https://www.pcgamesn.com/total-war-warhammer-2/cavalry-overhaul-beta)
- [MTW Morale (org wiki)](https://forums.totalwar.org/wiki/index.php/MTW_Morale), [MTW Melee Combat](https://forums.totalwar.org/wiki/index.php/MTW_Melee_Combat)
- [Three Kingdoms morale guide](https://gameplay.tips/guides/4225-total-war-three-kingdoms.html)
- [Steam: WH3 autoresolve discussion](https://steamcommunity.com/app/1142710/discussions/0/3832045251561220120)
