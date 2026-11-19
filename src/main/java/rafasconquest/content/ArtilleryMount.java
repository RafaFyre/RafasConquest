package rafasconquest.content;

import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.plot.LevelPlot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
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
 * An {@link ArtilleryBlock}'s anchor. Once the block is on a Sable ship its engine takes the block's place (the block,
 * armed, is hidden), facing the block's way turned with the ship, with a siege engineer riding it; then, once a second,
 * pulls the engine back if it has left its place (each tick if it dropped through the deck), and turns it the block's
 * way while no one works it. Sable carries the engine standing on its deck (see SiegeworksShipCollisionMixin); the pull catches what that
 * misses: a ship unloaded and loaded again, a hard swing.
 */
public class ArtilleryMount extends BlockEntity {
    /** How far (squared, blocks) the engine may stray from its block's place before it is pulled back: half a block. */
    private static final double STRAY = 0.5 * 0.5;
    /**
     * How far above the deck its engine is put. Sable carries only what has stood on its ship, so a deck heeling up under
     * a new engine is otherwise already in its feet, and it drops through the hull into the sea.
     */
    private static final double LIFT = 0.25;
    /** An engine further than this below its place has dropped through the deck: it is caught each tick, not each second. */
    private static final double DROP = 0.5;
    /**
     * The tag on an engine an Artillery Block made: it belongs to its block, not to whoever works it, so it stays when
     * they go (Recruits takes any vehicle for a soldier's mount).
     */
    public static final String SHIP_GUN = "rafasconquest_ship_gun";

    @Nullable
    private UUID engine;
    /** Whether its engine was ever made: a destroyed engine is not made again. */
    private boolean made;
    /** Whether its engine's going was logged (once). */
    private boolean toldGone;
    private boolean toldNoShip;

    public ArtilleryMount(BlockPos pos, BlockState state) {
        super(ConquestContent.ARTILLERY_ENTITY.get(), pos, state);
        // Diagnostic: mounts made at all (a hull's blocks set without their block entities makes none); the first few only.
        if (CREATED.incrementAndGet() <= 20) {
            rafasconquest.RafasConquest.LOGGER.info("[guns] mount created at {} ({})", pos, state.getValue(ArtilleryBlock.WEAPON).getSerializedName());
        }
    }

    private static final java.util.concurrent.atomic.AtomicInteger CREATED = new java.util.concurrent.atomic.AtomicInteger();
    /** Whether its engine's first stray from its block was logged (diagnostic). */
    private boolean toldStray;

    void serverTick(ServerLevel level, BlockState state) {
        boolean second = level.getGameTime() % 20 == Math.floorMod(worldPosition.asLong(), 20);
        Entity e = engine == null ? null : level.getEntity(engine);
        if (!second && e == null) {
            return;
        }
        ServerSubLevel ship = ship(level, worldPosition);
        if (ship == null) {
            if (!toldNoShip) {
                toldNoShip = true;
                rafasconquest.RafasConquest.LOGGER.info("[guns] {} ticks but is on no ship", worldPosition);
            }
            return; // not on a ship: a hull being built, or a ship file being made
        }
        // The engine stands in the block's own place: the block, armed, is gone to the eye and the hand (ArtilleryBlock.ARMED).
        boolean mounted = state.getBlock() instanceof MountedCannonBlock; // its gun stands on the block, not in its place
        Vec3 spot = ship.logicalPose().transformPosition(Vec3.atBottomCenterOf(worldPosition)
                .add(0, LIFT + (mounted ? MountedCannonBlock.FOOT : 0), 0));
        if (e == null) {
            // shortcut: an engine whose chunk is unloaded looks the same as a destroyed one, so neither is made again;
            // keep a "last seen" tick if ships lose their guns after a long unload.
            if (!made) {
                make(level, state, ship, spot);
            } else if (!toldGone) {
                toldGone = true;
                rafasconquest.RafasConquest.LOGGER.info("[guns] {} on ship {}: its engine {} is gone", worldPosition, ship.getName(), engine);
                if (state.getValue(ArtilleryBlock.ARMED)) {
                    level.setBlock(worldPosition, state.setValue(ArtilleryBlock.ARMED, false), Block.UPDATE_ALL); // the block shows again
                }
            }
            return;
        }
        // Off its place, it is put back each second; dropped through the deck, each tick, before it falls far (a second's
        // fall is 14 blocks, its gunner riding it down).
        if (e.getY() < spot.y - DROP || second && e.distanceToSqr(spot) > STRAY) {
            if (!toldStray) {
                toldStray = true;
                rafasconquest.RafasConquest.LOGGER.info("[guns] {}: engine strayed {} blocks (now at {}, on ship {}); pulled back",
                        worldPosition, Math.round(Math.sqrt(e.distanceToSqr(spot))), e.position(), rafasconquest.bridge.SableLink.onShip(e));
            }
            e.teleportTo(spot.x, spot.y, spot.z);
            e.setDeltaMovement(Vec3.ZERO);
            e.resetFallDistance(); // else it lands with the whole fall in it, and hands it on to its gunner
        }
        if (mounted) {
            // Swung no further than its arc either side of the block's way, as the ship now lies; set as Siegeworks sets
            // a gun's yaw, so its synced aim agrees and its crew can't lay it outside the arc.
            float facing = yaw(state, ship);
            float off = net.minecraft.util.Mth.wrapDegrees(e.getYRot() - facing);
            if (Math.abs(off) > MountedCannonBlock.ARC) {
                float yaw = facing + Math.copySign(MountedCannonBlock.ARC, off);
                try { // Siegeworks' own: body, head and synced aim alike
                    e.getClass().getMethod("applyYaw", float.class).invoke(e, yaw);
                } catch (ReflectiveOperationException | RuntimeException ex) {
                    turn(e, yaw);
                }
            }
        }
        if (second && e.getPassengers().isEmpty()) {
            // Unworked, it points the block's way as the ship lies now: an entity on a deck isn't turned with the ship.
            turn(e, yaw(state, ship));
        }
    }

    /** The way the block faces as its ship now lies, as an entity's yaw (degrees). */
    private static float yaw(BlockState state, ServerSubLevel ship) {
        Direction facing = state.getValue(ArtilleryBlock.FACING);
        Vector3d d = ship.logicalPose().orientation().transform(new Vector3d(facing.getStepX(), 0, facing.getStepZ()));
        return (float) Math.toDegrees(Math.atan2(-d.x, d.z));
    }

    private static void turn(Entity e, float yaw) {
        e.setYRot(yaw);
        e.yRotO = yaw;
        e.setYHeadRot(yaw);
        if (e instanceof net.minecraft.world.entity.LivingEntity living) {
            living.setYBodyRot(yaw);
            living.yBodyRotO = yaw;
        }
    }

    private void make(ServerLevel level, BlockState state, ServerSubLevel ship, Vec3 top) {
        Machine weapon = state.getValue(ArtilleryBlock.WEAPON).machine();
        Entity e = BuiltInRegistries.ENTITY_TYPE.getOptional(ResourceLocation.parse(weapon.entity()))
                .map(t -> t.create(level)).orElse(null);
        made = true;
        setChanged();
        if (e == null) {
            rafasconquest.RafasConquest.LOGGER.warn("[guns] {} on ship {}: no entity {} to make", worldPosition, ship.getName(), weapon.entity());
            return; // Siegeworks is not installed, or renamed it
        }
        float yaw = yaw(state, ship);
        level.setBlock(worldPosition, state.setValue(ArtilleryBlock.ARMED, true), Block.UPDATE_ALL); // first, so the engine isn't pushed out of it
        e.moveTo(top.x, top.y, top.z, yaw, 0);
        turn(e, yaw);
        e.addTag(SHIP_GUN);
        boolean added = level.addFreshEntity(e);
        rafasconquest.RafasConquest.LOGGER.info("[guns] {} on ship {}: made {} at {} (added {}, chunk loaded {}, on ship {})", worldPosition,
                ship.getName(), weapon.entity(), top, added, level.hasChunkAt(BlockPos.containing(top)), rafasconquest.bridge.SableLink.onShip(e));
        engine = e.getUUID();
        if (state.getBlock() instanceof MountedCannonBlock) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS); // clients learn its gun is mounted
        }
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
        if (engine != null && getBlockState().getBlock() instanceof MountedCannonBlock) {
            MountedCannonBlock.MOUNTED.add(engine);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (engine != null) {
            MountedCannonBlock.MOUNTED.remove(engine);
        }
    }

    /** Sent to clients with its engine, so a Mounted Light Cannon's culverin is drawn barrel only. */
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getUpdatePacket() {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
    }
}
