package rafasconquest.bridge;

import rafasconquest.faction.Faction;

/**
 * The only place this mod will talk to Villager Recruits. Milestone 1 makes no Recruits calls, so every hook is a
 * no-op; later milestones implement it to map factions to Recruits teams and spawn soldiers from the pool in battle.
 */
public interface RecruitsBridge {
    RecruitsBridge NONE = new RecruitsBridge() {
    };

    /** A faction was discovered by the precompute. Future: create its Recruits team. */
    default void onFactionCreated(Faction faction) {
    }
}
