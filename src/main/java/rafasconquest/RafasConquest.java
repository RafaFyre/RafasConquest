package rafasconquest;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.ModContainer;
import org.slf4j.Logger;
import rafasconquest.content.ConquestContent;
import rafasconquest.net.ConquestNetwork;
import rafasconquest.worldgen.ConquestWorldgen;

@Mod(RafasConquest.MOD_ID)
public class RafasConquest {
    public static final String MOD_ID = "rafasconquest";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RafasConquest(IEventBus modBus, ModContainer container) {
        ConquestWorldgen.register(modBus);
        ConquestContent.register(modBus);
        rafasconquest.npc.ConquestEntities.register(modBus);
        modBus.addListener(ConquestNetwork::register);
        modBus.addListener(this::commonSetup);
        container.registerConfig(ModConfig.Type.COMMON, ConquestConfig.SPEC);
        LOGGER.info("RafasConquest loaded");
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ConquestGameRules::register);
        rafasconquest.battle.BattlePlan.zweihandlers = rafasconquest.gear.Armoury.available(); // they need Epic Knights' swords
    }
}
