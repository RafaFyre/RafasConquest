package rafasconquest.peerage;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.levelgen.Heightmap;
import rafasconquest.core.SeatCities;
import rafasconquest.map.BaseMap;
import rafasconquest.worldgen.ConquestRuntime;

/**
 * The city manager: while a player looks over a city they own, the server keeps them there as an unseen spectator, high
 * above its middle, so the city's chunks reach their client; leaving puts them back where and as they were. Where they
 * were is kept on the player, so a logout (or a crash) mid-view still puts them back.
 */
public final class CityManager {
    private static final String AWAY = "rafasconquestCityView";
    /** How high above the city's ground the player is held. */
    private static final int ABOVE = 40;

    private CityManager() {
    }

    /** The player asks to look over the city seating {@code province}: only their own. */
    public static void open(ServerPlayer player, int province) {
        BaseMap map = ConquestRuntime.map();
        if (map == null || player.level() != player.server.overworld() || Peerage.get(player.server).of(player.getUUID()).hold != province) {
            return;
        }
        SeatCities.City city = map.cities().stream().filter(c -> c.province() == province).findFirst().orElse(null);
        if (city == null) {
            return;
        }
        if (!player.getPersistentData().contains(AWAY)) { // switching cities keeps the first place they left
            CompoundTag away = new CompoundTag();
            away.putString("dim", player.level().dimension().location().toString());
            away.putDouble("x", player.getX());
            away.putDouble("y", player.getY());
            away.putDouble("z", player.getZ());
            away.putFloat("yaw", player.getYRot());
            away.putFloat("pitch", player.getXRot());
            away.putInt("mode", player.gameMode.getGameModeForPlayer().getId());
            player.getPersistentData().put(AWAY, away);
        }
        ServerLevel level = player.server.overworld();
        int y = level.getChunk(city.x() >> 4, city.z() >> 4).getHeight(Heightmap.Types.MOTION_BLOCKING, city.x() & 15, city.z() & 15);
        player.setGameMode(GameType.SPECTATOR);
        player.teleportTo(level, city.x() + 0.5, y + ABOVE, city.z() + 0.5, player.getYRot(), player.getXRot());
    }

    /** Leaving the city manager (or the game): back where and as they were. Nothing if they were not away. */
    public static void close(ServerPlayer player) {
        CompoundTag away = player.getPersistentData().getCompound(AWAY);
        if (!player.getPersistentData().contains(AWAY)) {
            return;
        }
        player.getPersistentData().remove(AWAY);
        ResourceLocation dim = ResourceLocation.tryParse(away.getString("dim"));
        ServerLevel level = dim == null ? null : player.server.getLevel(ResourceKey.create(Registries.DIMENSION, dim));
        player.teleportTo(level == null ? player.server.overworld() : level, away.getDouble("x"), away.getDouble("y"), away.getDouble("z"),
                away.getFloat("yaw"), away.getFloat("pitch"));
        player.setGameMode(GameType.byId(away.getInt("mode")));
    }

    /** A battle muster calls the player away: out of the city manager, and its screen closed. */
    public static void muster(ServerPlayer player) {
        if (player.getPersistentData().contains(AWAY)) {
            close(player);
            rafasconquest.net.ConquestNetwork.closeCityView(player);
        }
    }
}
