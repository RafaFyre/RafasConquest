package rafasconquest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import net.minecraft.world.entity.LivingEntity;

/**
 * A speaker's mouth while they talk ({@link DialogueScreen}): a dark slit on the face that opens and closes. Skins draw
 * no mouth of their own to move, so it is drawn over the face, only while words are being said.
 */
public class MouthLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
    private static final int COLOUR = 0xFF2A1410;

    public MouthLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffers, int light, T npc, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (npc.getId() != DialogueScreen.speaking()) {
            return;
        }
        pose.pushPose();
        getParentModel().head.translateAndRotate(pose);
        // Model space, in pixels of the 8x8 head: the face is at z -4, the chin at y 0 (y grows downwards).
        // 2.2 pixels wide, centred on the face's second row from the bottom; 0.2 tall shut, 1.2 open.
        float open = 0.2F + 1.0F * Math.abs(Mth.sin(Util.getMillis() / 90F));
        float x = 1.1F / 16;
        float top = (-1.5F - open / 2) / 16;
        float bottom = (-1.5F + open / 2) / 16;
        float z = -4.02F / 16;
        Matrix4f m = pose.last().pose();
        VertexConsumer v = buffers.getBuffer(RenderType.textBackground());
        // Both windings, so it shows whichever way the type culls.
        v.addVertex(m, -x, top, z).setColor(COLOUR).setLight(light);
        v.addVertex(m, -x, bottom, z).setColor(COLOUR).setLight(light);
        v.addVertex(m, x, bottom, z).setColor(COLOUR).setLight(light);
        v.addVertex(m, x, top, z).setColor(COLOUR).setLight(light);
        v.addVertex(m, x, top, z).setColor(COLOUR).setLight(light);
        v.addVertex(m, x, bottom, z).setColor(COLOUR).setLight(light);
        v.addVertex(m, -x, bottom, z).setColor(COLOUR).setLight(light);
        v.addVertex(m, -x, top, z).setColor(COLOUR).setLight(light);
        pose.popPose();
    }
}
