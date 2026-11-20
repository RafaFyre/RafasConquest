package rafasconquest.bridge;

import net.minecraft.server.level.ServerLevel;
import net.neoforged.fml.ModList;
import rafasconquest.RafasConquest;

import java.util.List;

/**
 * Sails real Sable ships: on the sea lanes near players ({@link SableShips}), and in battles at sea ({@link BattleShips}).
 * Sable is optional: this class never touches its API itself, so nothing of it is loaded unless the mod is installed.
 */
public final class SableLink {
    private static boolean enabled = ModList.get() != null && ModList.get().isLoaded("sable");

    /**
     * A battle ship's orders this tick: formation {@code id}'s ship (of {@code side}) sails for (toX, toZ) at up to
     * {@code speed} blocks a second, bow along its course (or along (faceX, faceZ) once there). {@code hull}: its size (the
     * general's a size up, a unit's the biggest); {@code lashed}: the formations whose
     * ships it is grappled to, which it keeps alongside; {@code hooked}: those its grapple lines are in, which haul it
     * toward its place alongside them; {@code sail}, {@code stripe}: its side's banner dyes (base, first
     * mark), which its sail is woven in.
     */
    public record Helm(int id, int side, double x, double z, double toX, double toZ, double faceX, double faceZ, double speed,
                       rafasconquest.core.SeaLanes.Size hull, java.util.Set<Integer> lashed, java.util.Set<Integer> hooked,
                       int sail, int stripe, java.util.UUID nation) {
    }

    /**
     * Formation {@code rammer}'s ship drove its bow into formation {@code struck}'s, throwing the soldiers aboard it at
     * (knockX, knockZ) blocks a tick (zero for a head-on blow).
     */
    public record Ram(int rammer, int struck, double knockX, double knockZ) {
    }


    private SableLink() {
    }

    /** Whether real ships can sail: Sable is installed and its API is the one we know. */
    public static boolean available() {
        return enabled;
    }

    /** Each overworld tick. */
    public static void tick(ServerLevel level) {
        if (!enabled) {
            return;
        }
        try {
            SableShips.tick(level);
            // Not while a battle that is over still sails its ships: they'd be taken away and rebuilt every 5 s, blinking.
            if (!RecruitsBattle.exists() && level.getGameTime() % 100 == 0) {
                BattleShips.tick(level, "", List.of()); // a battle's ships left from a stopped battle, or a crash, are taken away
            }
        } catch (LinkageError e) {
            enabled = false;
            RafasConquest.LOGGER.warn("Sable found, but its API doesn't match; no real ships will sail", e);
        }
    }

    /**
     * Each tick of a battle: a ship for each of these, sailing as it says; every other battle ship is taken away. Returns
     * the rams this tick between ships not yet grappled.
     */
    public static List<Ram> sailBattle(ServerLevel level, String battle, List<Helm> helms) {
        if (!enabled) {
            return List.of();
        }
        try {
            return BattleShips.tick(level, battle, helms);
        } catch (LinkageError e) {
            enabled = false;
            RafasConquest.LOGGER.warn("Sable found, but its API doesn't match; no real ships will sail", e);
            return List.of();
        }
    }

    /** Each tick of a Gate Block: its doors made ships, hinged and swung ({@link GateDoors}). Without Sable they stay blocks. */
    public static void gate(ServerLevel level, rafasconquest.content.GateBlockEntity gate) {
        if (!enabled) {
            return;
        }
        try {
            GateDoors.tick(level, gate);
        } catch (LinkageError e) {
            enabled = false;
            RafasConquest.LOGGER.warn("Sable found, but its API doesn't match; gate doors stay blocks", e);
        }
    }

    /** A Gate Block's new door boxes; says what was done. */
    public static String saveGate(rafasconquest.content.GateBlockEntity gate, @org.jetbrains.annotations.Nullable int[] left,
                                  @org.jetbrains.annotations.Nullable int[] right) {
        if (enabled) {
            try {
                return GateDoors.save(gate, left, right);
            } catch (LinkageError e) {
                enabled = false;
            }
        }
        gate.setBoxes(left, right);
        return "Gate doors saved (without Sable they stay blocks)";
    }

    /**
     * A blow of {@code amount} to the block at {@code pos}: if it is part of a gate door, the door takes it (and is gone at
     * 0 hit points) and true is returned, for the block not to be broken.
     */
    public static boolean hitDoor(ServerLevel level, net.minecraft.core.BlockPos pos, double amount) {
        if (!enabled) {
            return false;
        }
        try {
            return GateDoors.hit(level, pos, amount);
        } catch (LinkageError e) {
            return false;
        }
    }

    /** A door battered to nothing: its ship, if it has one, is gone. */
    public static void smashDoor(ServerLevel level, rafasconquest.content.GateBlockEntity.Door door) {
        if (!enabled) {
            return;
        }
        try {
            GateDoors.smash(level, door);
        } catch (LinkageError e) {
            // no Sable API: the door is plain blocks, knocked out by the gate itself
        }
    }

    /** Whether the block at {@code pos} is part of a gate door (which can't be mined). */
    public static boolean isDoor(ServerLevel level, net.minecraft.core.BlockPos pos) {
        if (!enabled) {
            return false;
        }
        try {
            return GateDoors.doorAt(level, pos) != null;
        } catch (LinkageError e) {
            return false;
        }
    }

    /** Where formation {@code id}'s battle ship is, which way its bow points and its deck's height: {x, z, dirX, dirZ, deckY}; null with none sailing. */
    @org.jetbrains.annotations.Nullable
    public static double[] shipAt(int id) {
        if (!enabled) {
            return null;
        }
        try {
            return BattleShips.where(id);
        } catch (LinkageError e) {
            return null;
        }
    }

    /** Formation {@code id}'s battle ship's Sable id, the same on its clients; null with none sailing. */
    @org.jetbrains.annotations.Nullable
    public static java.util.UUID shipUuid(int id) {
        if (!enabled) {
            return null;
        }
        try {
            return BattleShips.id(id);
        } catch (LinkageError e) {
            return null;
        }
    }

    /** Whether Sable ship {@code sub} (a ServerSubLevel, typed loosely so this class loads without Sable) is a battle's. */
    public static boolean battleShip(Object sub) {
        return enabled && BattleShips.battleShip((dev.ryanhcode.sable.sublevel.ServerSubLevel) sub);
    }

    /**
     * Whether {@code e} stands on (or last stood on) a Sable ship.
     * shortcut: an engine that once rode a ship counts as on one after, so ashore it collides as plain Minecraft does;
     * check distance to that ship if engines landed off ships should get Siegeworks' terrain collision back.
     */
    public static boolean onShip(net.minecraft.world.entity.Entity e) {
        if (!enabled) {
            return false;
        }
        try {
            return dev.ryanhcode.sable.Sable.HELPER.getTrackingSubLevel(e) != null
                    || dev.ryanhcode.sable.Sable.HELPER.getLastTrackingSubLevel(e) != null;
        } catch (LinkageError err) {
            return false;
        }
    }

    /**
     * Whether {@code e} stands within a ship's world box. Unlike {@link #onShip} it holds on a client from an entity's
     * first tick: Sable tracks an entity as aboard only once it has stood there, and an entity's tags aren't sent.
     */
    public static boolean overShip(net.minecraft.world.entity.Entity e) {
        if (!enabled) {
            return false;
        }
        try {
            dev.ryanhcode.sable.api.sublevel.SubLevelContainer container =
                    dev.ryanhcode.sable.api.sublevel.SubLevelContainer.getContainer(e.level());
            if (container == null) {
                return false;
            }
            for (dev.ryanhcode.sable.sublevel.SubLevel sub : container.getAllSubLevels()) {
                var box = sub.boundingBox();
                if (!sub.isRemoved() && e.getX() >= box.minX() && e.getX() <= box.maxX() && e.getZ() >= box.minZ() && e.getZ() <= box.maxZ()
                        && e.getY() >= box.minY() - 2 && e.getY() <= box.maxY() + 2) {
                    return true;
                }
            }
            return false;
        } catch (LinkageError err) {
            return false;
        }
    }

    /**
     * Where {@code at} lies in the world: a point in a ship's plot (Sable's far-off home for a ship's blocks) as that ship
     * lies now; any other point is {@code at} itself, the same object.
     */
    public static net.minecraft.world.phys.Vec3 outOfPlot(net.minecraft.world.level.Level level, net.minecraft.world.phys.Vec3 at) {
        if (!enabled) {
            return at;
        }
        try {
            dev.ryanhcode.sable.sublevel.SubLevel sub = dev.ryanhcode.sable.Sable.HELPER.getContaining(level, at);
            return sub == null ? at : sub.logicalPose().transformPosition(at);
        } catch (LinkageError err) {
            return at;
        }
    }

    /** How the ship whose plot holds {@code pos} is turned now (its logical pose); null on land. */
    @org.jetbrains.annotations.Nullable
    public static org.joml.Quaterniond turnAt(net.minecraft.world.level.Level level, net.minecraft.core.BlockPos pos) {
        if (!enabled) {
            return null;
        }
        try {
            dev.ryanhcode.sable.sublevel.SubLevel sub = dev.ryanhcode.sable.Sable.HELPER.getContaining(level, pos);
            return sub == null || sub.isRemoved() ? null : new org.joml.Quaterniond(sub.logicalPose().orientation());
        } catch (LinkageError err) {
            return null;
        }
    }

    /** A spot on formation {@code id}'s deck in the world, {right, back} of its hull's middle: {x, y, z}; null with none sailing. */
    @org.jetbrains.annotations.Nullable
    public static double[] onDeck(int id, double right, double back) {
        if (!enabled) {
            return null;
        }
        try {
            return BattleShips.onDeck(id, right, back);
        } catch (LinkageError e) {
            return null;
        }
    }
}
