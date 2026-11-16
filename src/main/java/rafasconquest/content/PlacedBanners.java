package rafasconquest.content;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import rafasconquest.data.SaveGuard;
import rafasconquest.net.ConquestNetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Where every battle banner and flag stands, saved with the world, so a client is told of those out to 512 blocks: its
 * own chunks (render distance) reach far less, and the server may not have theirs loaded either. Each player hears once
 * a second of those about their camera.
 */
public final class PlacedBanners extends SavedData {
    private static final String NAME = "rafasconquest_placed_banners";
    public static final double RANGE = 512;

    /** One banner: its block and the nation whose arms it shows. */
    public record Banner(BlockState state, @Nullable UUID nation) {
    }

    /** By dimension, then by packed position. */
    private final Map<String, Map<Long, Banner>> banners = new HashMap<>();
    private static int ticks;

    static PlacedBanners get(MinecraftServer server) {
        return SaveGuard.get(server, NAME, PlacedBanners::new, PlacedBanners::load);
    }

    static void put(ServerLevel level, BlockPos pos, BlockState state, @Nullable UUID nation) {
        PlacedBanners data = get(level.getServer());
        Banner banner = new Banner(state, nation);
        if (!banner.equals(data.banners.computeIfAbsent(level.dimension().location().toString(), k -> new HashMap<>()).put(pos.asLong(), banner))) {
            data.setDirty();
        }
    }

    static void remove(ServerLevel level, BlockPos pos) {
        PlacedBanners data = get(level.getServer());
        Map<Long, Banner> in = data.banners.get(level.dimension().location().toString());
        if (in != null && in.remove(pos.asLong()) != null) {
            data.setDirty();
        }
    }

    public static void tick(MinecraftServer server) {
        if (++ticks % 20 != 0) {
            return;
        }
        PlacedBanners data = get(server);
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            Vec3 camera = rafasconquest.bridge.CameraView.of(player);
            Vec3 at = camera == null ? player.position() : camera;
            List<BlockPos> where = new ArrayList<>();
            List<Banner> what = new ArrayList<>();
            data.banners.getOrDefault(player.level().dimension().location().toString(), Map.of()).forEach((pos, banner) -> {
                BlockPos p = BlockPos.of(pos);
                double dx = p.getX() - at.x;
                double dz = p.getZ() - at.z;
                if (dx * dx + dz * dz <= RANGE * RANGE) {
                    where.add(p);
                    what.add(banner);
                }
            });
            ConquestNetwork.sendFarBanners(player, where, what);
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        banners.forEach((dimension, in) -> in.forEach((pos, banner) -> {
            CompoundTag one = new CompoundTag();
            one.putString("dimension", dimension);
            one.putLong("pos", pos);
            one.put("state", NbtUtils.writeBlockState(banner.state()));
            if (banner.nation() != null) {
                one.putUUID("nation", banner.nation());
            }
            list.add(one);
        }));
        tag.put("banners", list);
        return tag;
    }

    private static PlacedBanners load(CompoundTag tag) {
        PlacedBanners data = new PlacedBanners();
        for (Tag t : tag.getList("banners", Tag.TAG_COMPOUND)) {
            CompoundTag one = (CompoundTag) t;
            BlockState state = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), one.getCompound("state"));
            if (kept(state.getBlock())) {
                data.banners.computeIfAbsent(one.getString("dimension"), k -> new HashMap<>()).put(one.getLong("pos"),
                        new Banner(state, one.hasUUID("nation") ? one.getUUID("nation") : null));
            }
        }
        return data;
    }

    /** Whether a block is one this list keeps. */
    static boolean kept(Block block) {
        return block instanceof BattleBannerBlock || block instanceof BattleWallBannerBlock;
    }
}
