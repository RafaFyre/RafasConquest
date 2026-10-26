package rafasconquest;

import net.minecraft.world.level.GameRules;

public final class ConquestGameRules {
    public static GameRules.Key<GameRules.BooleanValue> PASS_INGAME_TIME_FOR_IRL_TIME;

    private ConquestGameRules() {
    }

    /** Called once on the main thread during common setup, before any world loads. */
    static void register() {
        PASS_INGAME_TIME_FOR_IRL_TIME = GameRules.register("PassIngameTimeForIRLTime", GameRules.Category.MISC, GameRules.BooleanValue.create(false));
    }
}
