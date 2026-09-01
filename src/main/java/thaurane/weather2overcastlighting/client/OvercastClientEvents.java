package thaurane.weather2overcastlighting.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class OvercastClientEvents {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null || mc.player == null) {
            OvercastState.reset();
            return;
        }

        Layer1Coverage.Result coverage = Layer1Coverage.calculate(mc.player);
        OvercastState.updateTarget(coverage);
    }
}
