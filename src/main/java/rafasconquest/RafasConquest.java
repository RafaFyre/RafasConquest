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
        rafasconquest.content.GrappleHooks.register(modBus);
        modBus.addListener(ConquestNetwork::register);
        modBus.addListener(this::commonSetup);
        container.registerConfig(ModConfig.Type.COMMON, ConquestConfig.SPEC);
        LOGGER.info("RafasConquest loaded");
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ConquestGameRules::register);
        // Vanilla fills each rotation's table on first use, unguarded: two worldgen threads racing it get a null and
        // drop a whole city cell (JigsawBlock.rotate NPE). Fill them all once, here, on one thread.
        for (com.mojang.math.OctahedralGroup g : com.mojang.math.OctahedralGroup.values()) {
            g.rotate(net.minecraft.core.Direction.UP);
        }
        rafasconquest.battle.BattlePlan.zweihandlers = rafasconquest.gear.Armoury.available(); // they need Epic Knights' swords
        rafasconquest.battle.BattlePlan.muskets = net.neoforged.fml.ModList.get().isLoaded("musketmod"); // Recruits' crossbowmen fire its muskets
        rafasconquest.battle.BattlePlan.siegeworks = net.neoforged.fml.ModList.get().isLoaded("siegeworks"); // the siege train's engines
        rafasconquest.battle.BattlePlan.mods = id -> net.neoforged.fml.ModList.get().isLoaded(id); // for cards that need a mod
        rafasconquest.battle.UnitCards.load(net.neoforged.fml.loading.FMLPaths.GAMEDIR.get().resolve("mods").resolve("RafasConquest").resolve("units"));
        rafasconquest.style.ShipHulls.load(net.neoforged.fml.loading.FMLPaths.GAMEDIR.get().resolve("mods").resolve("RafasConquest").resolve("units").resolve("ships"));
    }
}
