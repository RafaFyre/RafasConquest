package rafasconquest.content;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import rafasconquest.map.BaseMap;
import rafasconquest.worldgen.ConquestRuntime;

import java.util.UUID;

/**
 * A battle banner's nation, whose arms it shows. A player's banner takes the placer's nation; one a structure brought
 * takes the nation that built the place (the province's first holder) the first time it loads.
 */
public class BattleBannerBlockEntity extends BlockEntity {
    @Nullable
    private UUID nation;
    /** Whether its nation is settled: by a player placing it, or by the place it was generated in. */
    private boolean settled;

    public BattleBannerBlockEntity(BlockPos pos, BlockState state) {
        super(ConquestContent.BATTLE_BANNER_ENTITY.get(), pos, state);
    }

    @Nullable
    public UUID nation() {
        return nation;
    }

    /** Settles the banner on a nation (null: none), and tells the clients. */
    public void claim(@Nullable UUID nation) {
        this.nation = nation;
        this.settled = true;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
        listed();
    }

    /** Puts it on {@link PlacedBanners}' list, so clients far off are told of it. */
    private void listed() {
        if (level instanceof net.minecraft.server.level.ServerLevel server && PlacedBanners.kept(getBlockState().getBlock())) {
            PlacedBanners.put(server, worldPosition, getBlockState(), nation);
        }
    }

    /** Its chunk is going: the banner is still there, so it stays listed. */
    private boolean unloading;

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        unloading = true;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (!unloading && level instanceof net.minecraft.server.level.ServerLevel server) {
            PlacedBanners.remove(server, worldPosition);
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        unloading = false;
        BaseMap map = ConquestRuntime.map();
        if (settled || level == null || level.isClientSide || map == null) {
            listed();
            return; // without a map yet, it tries again next load
        }
        int province = map.provinceIndexAt(worldPosition.getX(), worldPosition.getZ());
        int[] founders = ConquestRuntime.founders();
        BaseMap.Nation builder = founders != null && province >= 0 && province < founders.length && founders[province] >= 0
                ? map.nations().get(founders[province]) : map.holderOf(province);
        nation = builder == null ? null : builder.id();
        settled = true;
        setChanged();
        listed();
    }

    /** The nation a player serves (their team is the nation's), or null. */
    @Nullable
    public static UUID nationOf(ServerPlayer player) {
        rafasconquest.faction.Faction nation = rafasconquest.gear.RankKits.nationOf(player, ConquestRuntime.map());
        return nation == null ? null : nation.id();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (nation != null) {
            tag.putUUID("nation", nation);
        }
        tag.putBoolean("settled", settled);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        nation = tag.hasUUID("nation") ? tag.getUUID("nation") : null;
        settled = tag.getBoolean("settled");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
