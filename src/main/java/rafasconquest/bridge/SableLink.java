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
     * A battle ship's orders this tick: formation {@code id}'s ship sails for (toX, toZ) at up to {@code speed} blocks a
     * second, bow along its course (or along (faceX, faceZ) once there). {@code flagship}: the general's, a size up.
     */
    public record Helm(int id, double x, double z, double toX, double toZ, double faceX, double faceZ, double speed, boolean flagship) {
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
            if (!RecruitsBattle.running() && level.getGameTime() % 100 == 0) {
                BattleShips.tick(level, List.of()); // a battle's ships left from a stopped battle, or a crash, are taken away
            }
        } catch (LinkageError e) {
            enabled = false;
            RafasConquest.LOGGER.warn("Sable found, but its API doesn't match; no real ships will sail", e);
        }
    }

    /** Each tick of a battle: a ship for each of these, sailing as it says; every other battle ship is taken away. */
    public static void sailBattle(ServerLevel level, List<Helm> helms) {
        if (!enabled) {
            return;
        }
        try {
            BattleShips.tick(level, helms);
        } catch (LinkageError e) {
            enabled = false;
            RafasConquest.LOGGER.warn("Sable found, but its API doesn't match; no real ships will sail", e);
        }
    }
}
