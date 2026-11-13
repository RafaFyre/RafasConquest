package rafasconquest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecruitFiguresTest {
    @Test
    void theFiguresHeadSitsOnItsShouldersInEveryModel() {
        for (HumanoidModel<?> m : RecruitFigures.models()) {
            float[] head = span(m.head), body = span(m.body), leg = span(m.rightLeg);
            // y down, in blocks: head above the neck (0), body down to the hips (0.75), legs to the feet (1.5)
            assertEquals(-0.25, (head[0] + head[1]) / 2, 0.05, "head");
            assertEquals(0.375, (body[0] + body[1]) / 2, 0.05, "body");
            assertEquals(1.125, (leg[0] + leg[1]) / 2, 0.05, "leg");
        }
    }

    @Test
    void aHorsesFramesStandOnTheGroundMoveAndRepeatAFrameThatDrewOtherParts() {
        ModelPart root = net.minecraft.client.model.geom.builders.LayerDefinition.create(
                net.minecraft.client.model.HorseModel.createBodyMesh(net.minecraft.client.model.geom.builders.CubeDeformation.NONE), 64, 64).bakeRoot();
        for (String foal : new String[]{"left_hind_baby_leg", "right_hind_baby_leg", "left_front_baby_leg", "right_front_baby_leg"}) {
            root.getChild(foal).visible = false; // a grown horse's renderer hides these
        }
        RecruitFigures.Capture still = horse(root);
        root.getChild("right_front_leg").xRot = 0.8F;
        RecruitFigures.Capture striding = horse(root);
        RecruitFigures.Capture odd = horse(root);
        odd.box(0, 0, 0, 1, 1, 1); // a part the others don't draw
        float[] packed = RecruitFigures.pack(still, java.util.List.of(still, striding, odd));
        int corners = still.triangles().length / 12;
        assertEquals(3 * corners * 8, packed.length);
        float lowest = -Float.MAX_VALUE, moved = 0;
        for (int v = 0; v < corners; v++) {
            lowest = Math.max(lowest, packed[v * 8 + 1]);
            moved = Math.max(moved, Math.abs(packed[(corners + v) * 8 + 2] - packed[v * 8 + 2]));
            assertEquals(packed[(corners + v) * 8 + 2], packed[(2 * corners + v) * 8 + 2], "the odd frame repeats the one before");
        }
        assertEquals(1.5, lowest, 0.1, "hooves on the ground: y down, feet at 1.5");
        assertTrue(moved > 0.2, "the striding leg moved");
    }

    @Test
    void aStrideIsWhereItWasSetAtTheSameSpeedAtAnyPartialTick() {
        net.minecraft.world.entity.WalkAnimationState walk = new net.minecraft.world.entity.WalkAnimationState();
        RecruitFigures.stride(walk, 5F, 0.3F);
        RecruitFigures.stride(walk, 7.5F, 0.75F);
        assertEquals(7.5F, walk.position(), 1e-5);
        assertEquals(0.75F, walk.speed(0F), 1e-6, "Entity Model Features reads it at the game's partial tick");
        assertEquals(0.75F, walk.speed(1F), 1e-6);
    }

    /** A horse as LivingEntityRenderer places it facing south, taken in the model's frame. */
    private static RecruitFigures.Capture horse(ModelPart root) {
        RecruitFigures.Capture c = new RecruitFigures.Capture();
        c.entityFrame = true;
        c.bone(RecruitFigures.HORSE, 0, 0, 0);
        PoseStack pose = new PoseStack();
        pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(180));
        pose.scale(1.1F, 1.1F, 1.1F);
        pose.scale(-1, -1, 1);
        pose.translate(0, -1.501F, 0);
        root.render(pose, c, 0, 0);
        return c;
    }

    /** The lowest and highest y a part draws at rest. */
    private static float[] span(ModelPart part) {
        RecruitFigures.Capture c = new RecruitFigures.Capture();
        part.resetPose();
        part.render(new PoseStack(), c, 0, 0);
        float lo = Float.MAX_VALUE, hi = -Float.MAX_VALUE;
        for (int v = 0; v < c.size; v += 12) {
            lo = Math.min(lo, c.v[v + 1]);
            hi = Math.max(hi, c.v[v + 1]);
        }
        return new float[]{lo, hi};
    }
}
