package rafasconquest;

import net.minecraft.world.level.GameRules;

public final class ConquestGameRules {
    public static GameRules.Key<GameRules.BooleanValue> PASS_INGAME_TIME_FOR_IRL_TIME;
    public static GameRules.Key<GameRules.BooleanValue> SEA_SWELL;

    private ConquestGameRules() {
    }

    /** Called once on the main thread during common setup, before any world loads. */
    static void register() {
        PASS_INGAME_TIME_FOR_IRL_TIME = GameRules.register("PassIngameTimeForIRLTime", GameRules.Category.MISC, GameRules.BooleanValue.create(false));
        // The sea's swell (ships riding it, and the water drawn moving); every client is told when it changes.
        SEA_SWELL = GameRules.register("seaSwell", GameRules.Category.MISC, GameRules.BooleanValue.create(true, (server, rule) -> {
            rafasconquest.core.Swell.calm(!rule.get());
            server.getPlayerList().getPlayers().forEach(p -> rafasconquest.net.ConquestNetwork.sendSeaSwell(p, rule.get()));
        }));
    }
}
