package rafasconquest.bridge;

import rafasconquest.faction.Faction;

import java.util.Collection;

/**
 * The only place this mod talks to Villager Recruits. {@link RecruitsFactions} implements it; {@link #NONE} is for
 * tests and for running without a server.
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
}
