package rafasconquest.client;

import com.talhanation.recruits.client.gui.faction.FactionMainScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import rafasconquest.net.FactionInfo;

import java.util.List;

/** Client-only entry points the common code reaches through DistExecutor. */
public final class ClientScreens {
    private ClientScreens() {
    }

    /** The Recruits faction menu, as Recruits' own faction key opens it. */
    public static void openFactionMenu(Player player) {
        Minecraft.getInstance().setScreen(new FactionMainScreen(player));
    }

    public static void receiveFactionInfo(List<FactionInfo> factions) {
        if (Minecraft.getInstance().screen instanceof FactionInfoScreen screen) {
            screen.setFactions(factions);
        }
    }
}
