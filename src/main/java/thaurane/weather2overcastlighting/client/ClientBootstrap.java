package thaurane.weather2overcastlighting.client;

import net.minecraftforge.common.MinecraftForge;
import thaurane.weather2overcastlighting.Weather2OvercastLighting;

public final class ClientBootstrap {

    private ClientBootstrap() {
    }

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new OvercastClientEvents());
        Weather2OvercastLighting.LOGGER.info(
                "[W2OL] Client event handlers registered on MinecraftForge.EVENT_BUS");
    }
}
