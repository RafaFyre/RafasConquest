package rafasconquest;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(RafasConquest.MOD_ID)
public class RafasConquest {
    public static final String MOD_ID = "rafasconquest";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RafasConquest() {
        LOGGER.info("RafasConquest loaded");
    }
}
