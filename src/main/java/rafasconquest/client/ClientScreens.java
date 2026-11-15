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

    /**
     * A conversation (a recruiter, a noble, the war council): what follows in one already open, or a new one, unless the
     * player is busy in another screen (they came near while in their inventory, say).
     */
    public static void openTalk(List<rafasconquest.peerage.Talk.View> pages) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof DialogueScreen talk) {
            talk.show(pages);
        } else if (mc.screen == null) {
            mc.setScreen(new DialogueScreen(pages));
        }
    }

    /** The player's stats, over the screen they were asked from (the inventory), which closing them goes back to. */
    public static void openStats(rafasconquest.peerage.PlayerStats.View view) {
        Minecraft.getInstance().setScreen(new StatsScreen(Minecraft.getInstance().screen, view));
    }

    /** A Gate Block's door boxes, to edit. */
    public static void openGate(net.minecraft.core.BlockPos pos, int[] left, int[] right) {
        Minecraft.getInstance().setScreen(new GateScreen(pos, left, right));
    }

    /** An Artillery Block's weapon, to pick. */
    public static void openArtillery(net.minecraft.core.BlockPos pos, rafasconquest.battle.Machine weapon) {
        Minecraft.getInstance().setScreen(new ArtilleryScreen(pos, weapon));
    }

    /** A Character Marker's settings, to edit. */
    public static void openMarker(net.minecraft.core.BlockPos pos, rafasconquest.style.MarkerSpec spec) {
        Minecraft.getInstance().setScreen(new MarkerScreen(pos, spec));
    }

    /** A quest board's notices. */
    public static void openBoard(rafasconquest.peerage.Boards.View view) {
        Minecraft.getInstance().setScreen(new BoardScreen(view));
    }

    /** The world map, opened from the World Map item; closing it goes back to the game. */
    public static void openWorldMap() {
        Minecraft.getInstance().setScreen(new FactionMapScreen(null));
    }

    /** The grand strategy screen: the world map with the player's fief over it. */
    public static void openGrandStrategy(rafasconquest.peerage.GrandStrategy.View view) {
        rafasconquest.client.map.GrandStrategyScreen open = rafasconquest.client.map.GrandStrategyScreen.shown();
        if (open != null) {
            open.refresh(view); // an army order's answer: keep the camera where it is (and any menu over the map)
            return;
        }
        Minecraft.getInstance().setScreen(new rafasconquest.client.map.GrandStrategyScreen(view));
    }

    /** The server called the player out of the city manager (a battle muster). */
    public static void closeCityManager() {
        if (Minecraft.getInstance().screen instanceof CityManagerScreen) {
            Minecraft.getInstance().setScreen(null);
        }
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
