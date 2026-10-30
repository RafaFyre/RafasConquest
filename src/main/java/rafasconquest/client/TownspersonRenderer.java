package rafasconquest.client;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import rafasconquest.npc.Townsperson;

/** A townsperson: a player-shaped figure in one of Recruits' human skins. */
public class TownspersonRenderer extends HumanoidMobRenderer<Townsperson, PlayerModel<Townsperson>> {
    private static final ResourceLocation[] SKINS = new ResourceLocation[Townsperson.SKINS];

    static {
        for (int i = 0; i < SKINS.length; i++) {
            SKINS[i] = new ResourceLocation("recruits", "textures/entity/human/human_" + i + ".png");
        }
    }

    public TownspersonRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(Townsperson townsperson) {
        return SKINS[townsperson.skin()];
    }
}
