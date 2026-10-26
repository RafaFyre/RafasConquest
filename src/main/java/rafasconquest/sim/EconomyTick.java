package rafasconquest.sim;

import rafasconquest.data.ConquestSavedData;
import rafasconquest.faction.Faction;
import rafasconquest.faction.FactionType;
import rafasconquest.faction.ResourceType;
import rafasconquest.poi.Poi;

/**
 * One simulated day. The design's loop is produce → upkeep → decide → resolve; Milestone 1 implements produce only.
 */
public final class EconomyTick {
    private EconomyTick() {
    }

    public static void runDay(ConquestSavedData data) {
        produce(data);
    }

    /** Every owned POI adds its daily output to its owner's stockpile. Bandits grow from raids instead. */
    static void produce(ConquestSavedData data) {
        for (Poi poi : data.pois()) {
            ResourceType resource = poi.type().produces();
            if (resource == null || poi.dailyOutput() <= 0) {
                continue;
            }
            Faction owner = data.faction(poi.owner());
            if (owner != null && owner.type() != FactionType.BANDIT) {
                owner.addStock(resource, poi.dailyOutput());
            }
        }
    }
}
