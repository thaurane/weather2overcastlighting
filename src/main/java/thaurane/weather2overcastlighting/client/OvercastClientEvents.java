package thaurane.weather2overcastlighting.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import thaurane.weather2overcastlighting.Weather2OvercastLighting;
import thaurane.weather2overcastlighting.config.OvercastConfig;

public final class OvercastClientEvents {

    private long lastCoverageDebugNanos = 0L;

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

        debugCoverageIfNeeded(coverage);
    }

    private void debugCoverageIfNeeded(Layer1Coverage.Result coverage) {
        if (!OvercastConfig.debugLogging.get()) {
            return;
        }

        long now = System.nanoTime();
        if (now - lastCoverageDebugNanos < 5_000_000_000L) {
            return;
        }

        lastCoverageDebugNanos = now;

        Weather2OvercastLighting.LOGGER.info(
                "[W2OL COVERAGE DEBUG] layer1={} cloudy={} rawCoverage={} adjustedCoverage={} targetDarkness={} currentDarkness={}",
                coverage.layer1Clouds(),
                coverage.cloudyLayer1Clouds(),
                coverage.rawCoverage(),
                coverage.adjustedCoverage(),
                OvercastState.targetDarkness(),
                OvercastState.currentDarkness());
    }
}
