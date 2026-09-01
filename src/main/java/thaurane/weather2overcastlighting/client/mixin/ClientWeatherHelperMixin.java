package thaurane.weather2overcastlighting.client.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaurane.weather2overcastlighting.Weather2OvercastLighting;
import thaurane.weather2overcastlighting.client.Layer1Coverage;
import thaurane.weather2overcastlighting.client.OvercastState;
import thaurane.weather2overcastlighting.config.OvercastConfig;

/**
 * Adds a client-only visual thunder floor without mutating Minecraft or Weather2
 * weather state.
 *
 * Vanilla's getThunderLevel() is multiplied by the rain level. That is why
 * calling setThunderLevel() by itself while rainLevel == 0 still produced an
 * immediate getThunderLevel() result of 0.
 *
 * We instead intercept the value returned to CLIENT callers and apply:
 *
 *     visualThunder = max(naturalThunder, layer1OvercastDarkness)
 *
 * The mixin targets Level because getThunderLevel(float) is declared there.
 * The instanceof ClientLevel guard means integrated-server ServerLevel instances
 * are left completely untouched.
 */
@Mixin(Level.class)
public abstract class ClientWeatherHelperMixin {

    private static long w2ol$lastDebugNanos = 0L;

    /**
     * 1.20.1 SRG name:
     *   m_46661_(float) = getThunderLevel(float)
     *
     * remap=false is intentional here because the production Forge runtime uses
     * the SRG name and this project currently does not use a generated mixin
     * refmap for Minecraft method-name remapping.
     */
    @Inject(
            method = "m_46661_(F)F",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    private void w2ol$applyVisualThunderFloor(
            float partialTick,
            CallbackInfoReturnable<Float> cir) {

        // Never alter ServerLevel or any other Level implementation.
        if (!((Object) this instanceof ClientLevel)) {
            return;
        }

        float naturalThunder = cir.getReturnValue();

        float overcastDarkness = Math.max(
                0.0F,
                Math.min(1.0F, OvercastState.currentDarkness()));

        float finalThunder = Math.max(naturalThunder, overcastDarkness);

        if (finalThunder != naturalThunder) {
            cir.setReturnValue(finalThunder);
        }

        w2ol$debug(naturalThunder, overcastDarkness, finalThunder);
    }

    private static void w2ol$debug(
            float naturalThunder,
            float overcastDarkness,
            float finalThunder) {

        if (!OvercastConfig.debugLogging.get()) {
            return;
        }

        long now = System.nanoTime();
        if (now - w2ol$lastDebugNanos < 5_000_000_000L) {
            return;
        }

        w2ol$lastDebugNanos = now;

        Layer1Coverage.Result coverage = OvercastState.lastCoverage();

        Weather2OvercastLighting.LOGGER.info(
                "[W2OL THUNDER GETTER DEBUG] layer1={} cloudy={} rawCoverage={} adjustedCoverage={} targetDarkness={} currentDarkness={} naturalThunder={} returnedThunder={}",
                coverage.layer1Clouds(),
                coverage.cloudyLayer1Clouds(),
                coverage.rawCoverage(),
                coverage.adjustedCoverage(),
                OvercastState.targetDarkness(),
                overcastDarkness,
                naturalThunder,
                finalThunder);
    }
}
