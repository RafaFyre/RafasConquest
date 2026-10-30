package rafasconquest.client;

import com.talhanation.recruits.client.gui.faction.FactionMainScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import rafasconquest.client.map.ClientMapCache;
import rafasconquest.client.map.FactionMapScreen;
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

    /** The world map, opened from the World Map item; closing it goes back to the game. */
    public static void openWorldMap() {
        Minecraft.getInstance().setScreen(new FactionMapScreen(null));
    }

    public static void receiveFactionInfo(List<FactionInfo> factions) {
        if (Minecraft.getInstance().screen instanceof FactionInfoScreen screen) {
            screen.setFactions(factions);
        } else if (Minecraft.getInstance().screen instanceof FactionMapScreen screen) {
            screen.setFactions(factions);
        }
    }

    public static void receiveBaseMapPart(int index, int count, byte[] bytes) {
        if (ClientMapCache.accept(index, count, bytes) && Minecraft.getInstance().screen instanceof FactionMapScreen screen) {
            screen.onBaseMap();
        }
    }
}
