package rafasconquest;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import rafasconquest.worldgen.ConquestWorldgen;

@Mod(RafasConquest.MOD_ID)
public class RafasConquest {
    public static final String MOD_ID = "rafasconquest";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RafasConquest(FMLJavaModLoadingContext context) {
        IEventBus modBus = context.getModEventBus();
        ConquestWorldgen.register(modBus);
        modBus.addListener(this::commonSetup);
        context.registerConfig(ModConfig.Type.COMMON, ConquestConfig.SPEC);
        LOGGER.info("RafasConquest loaded");
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ConquestGameRules::register);
    }
}
