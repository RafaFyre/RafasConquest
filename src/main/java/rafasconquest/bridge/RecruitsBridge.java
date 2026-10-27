package rafasconquest.bridge;

import net.minecraft.core.BlockPos;
import rafasconquest.faction.Faction;

import java.util.Collection;

/**
 * How the simulation talks to Villager Recruits (this package is the only place that does; the debug battle,
 * {@link RecruitsBattle}, lives here too). {@link RecruitsFactions} implements it; {@link #NONE} is for tests and for
 * running without a server.
 */
public interface RecruitsBridge {
    RecruitsBridge NONE = new RecruitsBridge() {
    };

    /** A faction was discovered by the precompute. */
    default void onFactionCreated(Faction faction) {
    }

    /** Makes sure every given faction exists on the Recruits side (for factions created before the bridge did). */
    default void syncAll(Collection<Faction> factions) {
    }

    /**
     * Spawns an archer of {@code owner}'s faction standing at {@code feet}, facing {@code yaw}, holding that position.
     * Returns false if it couldn't (Recruits not ready, or the faction has no Recruits team).
     */
    default boolean spawnWallGuard(Faction owner, BlockPos feet, float yaw) {
        return false;
    }
}
