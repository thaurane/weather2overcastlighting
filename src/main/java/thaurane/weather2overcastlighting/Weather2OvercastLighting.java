package thaurane.weather2overcastlighting;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;
import thaurane.weather2overcastlighting.client.ClientBootstrap;
import thaurane.weather2overcastlighting.config.OvercastConfig;

@Mod(Weather2OvercastLighting.MODID)
public final class Weather2OvercastLighting {

    public static final String MODID = "weather2overcastlighting";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Weather2OvercastLighting() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, OvercastConfig.SPEC);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientBootstrap::init);
    }
}
