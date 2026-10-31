package rafasconquest.gear;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import rafasconquest.character.Rank;
import rafasconquest.faction.Faction;
import rafasconquest.map.BaseMap;
import rafasconquest.sim.ConquestServer;
import rafasconquest.worldgen.ConquestRuntime;

/**
 * The arms an enlisted player is issued, in their nation's style ({@link Armoury#rankKit}), on enlisting and on each
 * promotion into a better grade of harness: the levy's (Soldier, Corporal), the heavy infantry's (Serjeant, Captain), the
 * cavalry's (Major, Colonel) and the officers' (Marshal, Grand Marshal). Into their inventory, or at their feet if it is
 * full. Nothing without Epic Knights.
 */
public final class RankKits {
    private RankKits() {
    }

    /** A rank's grade of harness, 0 to 3. */
    static int grade(Rank rank) {
        return (rank.number() - 1) / 2;
    }

    /** Issues the kit of {@code to}, if it is a better grade than {@code from} (null: just enlisted). */
    public static void issue(ServerPlayer player, @Nullable Rank from, Rank to) {
        if (!Armoury.available() || from != null && grade(to) <= grade(from)) {
            return;
        }
        BaseMap map = ConquestRuntime.map();
        Faction nation = nationOf(player, map);
        if (map == null || nation == null) {
            return;
        }
        Armoury.Outfit kit = Armoury.rankKit(to.number(), Armoury.homeland(map, nation.id()), Armoury.colour(nation.id()),
                Armoury.banner(player.level().registryAccess(), nation.id()));
        if (kit.isEmpty()) {
            return;
        }
        for (ItemStack stack : kit.slots().values()) {
            if (!player.getInventory().add(stack.copy())) {
                player.drop(stack.copy(), false);
            }
        }
        player.sendSystemMessage(Component.literal("The quartermaster issues you the arms of a " + to.title() + ", in "
                + nation.name() + "'s colours.").withStyle(ChatFormatting.GOLD));
    }

    /** The nation a player serves, or null. */
    @Nullable
    private static Faction nationOf(ServerPlayer player, @Nullable BaseMap map) {
        ConquestServer server = ConquestServer.get();
        if (map == null || server == null) {
            return null;
        }
        for (BaseMap.Nation n : map.nations()) {
            Faction f = server.data().faction(n.id());
            if (f != null && rafasconquest.peerage.Talk.serves(player, f)) {
                return f;
            }
        }
        return null;
    }
}
