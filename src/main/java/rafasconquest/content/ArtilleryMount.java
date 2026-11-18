package rafasconquest.content;

import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.plot.LevelPlot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import rafasconquest.battle.Machine;

import java.util.UUID;

/**
 * An {@link ArtilleryBlock}'s anchor. Once the block is on a Sable ship it puts its engine on top, facing the block's way
 * turned with the ship, with a siege engineer riding it; then, once a second, pulls the engine back if it has left its
 * block. Sable itself carries a living entity (Siegeworks engines are) standing on a moving ship; the pull catches what
 * that misses: a ship unloaded and loaded again, a hard swing.
 */
public class ArtilleryMount extends BlockEntity {
    /** How far (squared, blocks) the engine may stray from its block before it is pulled back. */
    private static final double STRAY = 2 * 2;

    @Nullable
    private UUID engine;
    /** Whether its engine was ever made: a destroyed engine is not made again. */
    private boolean made;

    public ArtilleryMount(BlockPos pos, BlockState state) {
        super(ConquestContent.ARTILLERY_ENTITY.get(), pos, state);
    }

    void serverTick(ServerLevel level, BlockState state) {
        if (level.getGameTime() % 20 != Math.floorMod(worldPosition.asLong(), 20)) {
            return;
        }
        ServerSubLevel ship = ship(level, worldPosition);
        if (ship == null) {
            return; // not on a ship: a hull being built, or a ship file being made
        }
        Vec3 top = ship.logicalPose().transformPosition(Vec3.atBottomCenterOf(worldPosition.above()));
        Entity e = engine == null ? null : level.getEntity(engine);
        if (e == null) {
            // shortcut: an engine whose chunk is unloaded looks the same as a destroyed one, so neither is made again;
            // keep a "last seen" tick if ships lose their guns after a long unload.
            if (!made) {
                make(level, state, ship, top);
            }
            return;
        }
        if (e.distanceToSqr(top) > STRAY) {
            e.teleportTo(top.x, top.y, top.z);
        }
    }

    private void make(ServerLevel level, BlockState state, ServerSubLevel ship, Vec3 top) {
        Machine weapon = state.getValue(ArtilleryBlock.WEAPON).machine();
        Entity e = BuiltInRegistries.ENTITY_TYPE.getOptional(ResourceLocation.parse(weapon.entity()))
                .map(t -> t.create(level)).orElse(null);
        made = true;
        setChanged();
        if (e == null) {
            return; // Siegeworks is not installed, or renamed it
        }
        Direction facing = state.getValue(ArtilleryBlock.FACING);
        Vector3d d = ship.logicalPose().orientation().transform(new Vector3d(facing.getStepX(), 0, facing.getStepZ()));
        float yaw = (float) Math.toDegrees(Math.atan2(-d.x, d.z));
        e.moveTo(top.x, top.y, top.z, yaw, 0);
        level.addFreshEntity(e);
        engine = e.getUUID();
        if (rafasconquest.bridge.SableLink.battleShip(ship)) {
            return; // a battle gunship: its gunners are the battle's soldiers at its Character Markers (RecruitsBattle.manGun)
        }
        var crew = com.talhanation.recruits.init.ModEntityTypes.SIEGE_ENGINEER.get().create(level);
        if (crew != null) {
            crew.moveTo(top.x, top.y, top.z, yaw, 0);
            // Recruits' engineer finalizeSpawn crashes (see RecruitsBattle.dressed); this is the rest of what it does.
            crew.setRandomSpawnBonus();
            crew.initSpawn();
            crew.setPersistenceRequired();
            level.addFreshEntity(crew);
            rafasconquest.bridge.RecruitsBattle.man(crew, e, weapon);
        }
    }

    /** Removes its engine, if it is in the world. */
    void dismantle(ServerLevel level) {
        Entity e = engine == null ? null : level.getEntity(engine);
        if (e != null) {
            e.discard();
        }
    }

    /** The Sable ship whose plot holds {@code pos}, or null. */
    @Nullable
    private static ServerSubLevel ship(ServerLevel level, BlockPos pos) {
        ServerSubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container == null || !container.inBounds(pos)) {
            return null;
        }
        LevelPlot plot = container.getPlot(new ChunkPos(pos));
        return plot != null && plot.getSubLevel() instanceof ServerSubLevel sub && !sub.isRemoved() ? sub : null;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (engine != null) {
            tag.putUUID("engine", engine);
        }
        tag.putBoolean("made", made);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        engine = tag.hasUUID("engine") ? tag.getUUID("engine") : null;
        made = tag.getBoolean("made");
    }
}
