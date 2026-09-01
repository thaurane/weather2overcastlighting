package thaurane.weather2overcastlighting.client.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaurane.weather2overcastlighting.client.OvercastState;

/**
 * Adds a client-only visual thunder floor without mutating Minecraft or Weather2
 * weather state.
 *
 * Vanilla's getThunderLevel() is multiplied by the rain level. We intercept the
 * value returned to client callers and apply:
 *
 *     visualThunder = max(naturalThunder, layer1OvercastDarkness)
 *
 * The mixin targets Level because getThunderLevel(float) is declared there.
 * The instanceof ClientLevel guard means integrated-server ServerLevel instances
 * are left completely untouched.
 */
@Mixin(Level.class)
public abstract class ClientWeatherHelperMixin {

    /**
     * 1.20.1 SRG name:
     *   m_46661_(float) = getThunderLevel(float)
     *
     * remap=false is intentional because the production Forge runtime uses the
     * SRG name and this project does not use a generated mixin refmap for
     * Minecraft method-name remapping.
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
    }
}
