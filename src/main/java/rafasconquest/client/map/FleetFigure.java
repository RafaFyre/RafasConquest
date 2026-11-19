package rafasconquest.client.map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;
import rafasconquest.battle.BattlePlan;
import rafasconquest.core.SeaLanes;
import rafasconquest.faction.FactionBanner;
import rafasconquest.map.BaseMap;
import rafasconquest.peerage.GrandStrategy;
import rafasconquest.style.ShipHulls;

import java.util.List;

/**
 * A fleet's figure on the grand strategy map, as an army has its marcher: its biggest ship's battle hull, its sails in its
 * nation's banner colours as its ships sail in battle ({@link ShipHulls#battle}), drawn block by block {@link #LENGTH}
 * blocks long, so it grows and shrinks with the land as the map zooms.
 */
final class FleetFigure {
    /** How long the figure lies on the map, in blocks: about twice the marcher's height. */
    static final double LENGTH = 160;

    private FleetFigure() {
    }

    /** The hull the fleet shows: its biggest ship card's; a unit ship if it has none. */
    static SeaLanes.Size size(GrandStrategy.Army a) {
        SeaLanes.Size best = null;
        for (int u : a.units()) {
            BattlePlan.Arm arm = BattlePlan.Arm.get(u);
            SeaLanes.Size s = arm == null ? null : arm.ship();
            if (s != null && (best == null || s.length > best.length)) {
                best = s;
            }
        }
        return best != null ? best : SeaLanes.Size.unitShip();
    }

    /** The fleet's ship, built as its battle ships are, in its nation's colours (white sails for none). */
    static List<ShipHulls.Part> parts(GrandStrategy.Army a, @Nullable BaseMap base, int nation) {
        DyeColor sail = DyeColor.WHITE;
        DyeColor stripe = DyeColor.WHITE;
        if (base != null && nation >= 0 && nation < base.nations().size()) {
            FactionBanner flag = FactionBanner.of(base.nations().get(nation).id());
            sail = DyeColor.byId(flag.base());
            stripe = flag.layers().isEmpty() ? sail : DyeColor.byId(flag.layers().get(0).color());
        }
        return ShipHulls.battle(size(a), sail, stripe);
    }

    /**
     * Draws the ship at the pose's origin, keel at y 0, {@link #LENGTH} blocks long, its bow along (dx, dz): blocks of
     * its hull at the pose's scale times {@code LENGTH / size.length}.
     */
    static void draw(PoseStack pose, MultiBufferSource buffers, GrandStrategy.Army a, @Nullable BaseMap base, int nation, double dx, double dz) {
        SeaLanes.Size size = size(a);
        float k = (float) (LENGTH / Math.max(1, size.length));
        var blocks = Minecraft.getInstance().getBlockRenderer();
        pose.pushPose();
        pose.scale(k, k, k);
        if (Math.hypot(dx, dz) > 1e-6) {
            pose.mulPose(Axis.YP.rotation((float) Math.atan2(dx, dz))); // the hull's +z, its bow, along its course
        }
        for (ShipHulls.Part p : parts(a, base, nation)) {
            pose.pushPose();
            pose.translate(p.x() - 0.5, p.y(), p.z() - 0.5);
            blocks.renderSingleBlock(p.state(), pose, buffers, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
            pose.popPose();
        }
        pose.popPose();
    }
}
