package rafasconquest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import rafasconquest.content.CannonAim;
import rafasconquest.content.MediumCannonBlock;
import rafasconquest.content.MediumCannonEntity;
import rafasconquest.net.ConquestNetwork;

/**
 * The player working a Medium Cannon: its barrel follows where they look, held within its arc; the attack button fires
 * it; sneaking, or stepping away, lets it go. The lay is shown here at once and sent to the server as it changes.
 */
public final class CannonControl {
    /** Further than this (blocks) from the gun, the player has left it. */
    private static final double REACH = 4;
    /** A lay that moved less than this (degrees) isn't sent. */
    private static final float SEND_STEP = 0.25F;

    @Nullable
    private static BlockPos working;
    private static float sentYaw = Float.NaN;
    private static float sentPitch = Float.NaN;

    private CannonControl() {
    }

    /** The server gave the player the cannon at {@code pos}. */
    public static void take(BlockPos pos) {
        working = pos;
        sentYaw = Float.NaN;
        sentPitch = Float.NaN;
    }

    /** Each client tick: lays the gun where the player looks, or lets it go. */
    static void tick() {
        Minecraft mc = Minecraft.getInstance();
        if (working == null) {
            return;
        }
        MediumCannonEntity gun = mc.level == null || mc.player == null ? null
                : mc.level.getBlockEntity(working) instanceof MediumCannonEntity g ? g : null;
        Vec3 base = gun == null ? null : rafasconquest.bridge.SableLink.outOfPlot(mc.level, Vec3.atBottomCenterOf(working));
        if (gun == null || !mc.player.isAlive() || mc.player.isShiftKeyDown() || mc.player.distanceToSqr(base) > REACH * REACH) {
            if (gun != null) {
                ConquestNetwork.sendCannonLeave(working);
            }
            working = null;
            return;
        }
        Vec3 look = mc.player.getViewVector(1);
        Quaterniond ship = rafasconquest.bridge.SableLink.turnAt(mc.level, working);
        double[] lay = CannonAim.layFor(new Vector3d(look.x, look.y, look.z), ship,
                CannonAim.facing(gun.getBlockState().getValue(MediumCannonBlock.FACING)));
        gun.yaw = (float) lay[0];
        gun.pitch = (float) lay[1];
        if (!(Math.abs(gun.yaw - sentYaw) < SEND_STEP && Math.abs(gun.pitch - sentPitch) < SEND_STEP)) {
            sentYaw = gun.yaw;
            sentPitch = gun.pitch;
            ConquestNetwork.sendCannonLay(working, gun.yaw, gun.pitch);
        }
    }

    /** The attack button fires the gun the player works, instead of swinging. */
    static boolean attack() {
        if (working == null) {
            return false;
        }
        ConquestNetwork.sendCannonFire(working);
        return true;
    }
}
