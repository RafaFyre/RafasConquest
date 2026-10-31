package rafasconquest.client;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import rafasconquest.npc.Recruiter;
import rafasconquest.npc.Townsperson;

/** A recruiter: a player-shaped figure in one of Recruits' human skins, like the townsfolk. */
public class RecruiterRenderer extends HumanoidMobRenderer<Recruiter, PlayerModel<Recruiter>> {
    private static final ResourceLocation[] SKINS = new ResourceLocation[Townsperson.SKINS];

    static {
        for (int i = 0; i < SKINS.length; i++) {
            SKINS[i] = new ResourceLocation("recruits", "textures/entity/human/human_" + i + ".png");
        }
    }

    public RecruiterRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
        // What they wear: a noble's crown and dress, an officer's harness.
        addLayer(new net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer<>(this,
                new net.minecraft.client.model.HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                new net.minecraft.client.model.HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
    }

    @Override
    public ResourceLocation getTextureLocation(Recruiter recruiter) {
        return SKINS[recruiter.skin()];
    }
}
