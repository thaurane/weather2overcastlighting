package thaurane.weather2overcastlighting.client;

import net.minecraft.world.entity.player.Player;
import thaurane.weather2overcastlighting.config.OvercastConfig;
import weather2.ClientTickHandler;
import weather2.config.ConfigMisc;
import weather2.weathersystem.storm.StormObject;
import weather2.weathersystem.storm.WeatherObject;

public final class Layer1Coverage {

    private Layer1Coverage() {
    }

    public static Result calculate(Player player) {
        if (!ConfigMisc.Cloud_Layer1_Enable || ClientTickHandler.weatherManager == null) {
            return Result.ZERO;
        }

        double sampleRadius = OvercastConfig.sampleRadius.get();
        double cloudyWeight = 0.0D;
        double totalWeight = 0.0D;
        int layer1Clouds = 0;
        int cloudyLayer1Clouds = 0;

        for (WeatherObject object : ClientTickHandler.weatherManager.getStormObjects()) {
            if (!(object instanceof StormObject storm)) {
                continue;
            }

            if (storm.isDead || storm.layer != 1 || storm.isFirenado) {
                continue;
            }

            double cloudRadius = Math.max(1.0D, storm.size);
            double dx = storm.pos.x - player.getX();
            double dz = storm.pos.z - player.getZ();
            double distance = Math.sqrt(dx * dx + dz * dz);
            double overlapLimit = cloudRadius + sampleRadius;

            if (distance > overlapLimit) {
                continue;
            }

            double weight = Math.max(0.05D, 1.0D - (distance / overlapLimit));
            totalWeight += weight;
            layer1Clouds++;

            if (!storm.isCloudlessStorm()) {
                cloudyWeight += weight;
                cloudyLayer1Clouds++;
            }
        }

        float rawCoverage = totalWeight > 0.0D
                ? (float) (cloudyWeight / totalWeight)
                : 0.0F;

        float adjustedCoverage = Math.min(
                1.0F,
                rawCoverage * OvercastConfig.coverageMultiplier.get().floatValue());

        return new Result(rawCoverage, adjustedCoverage, layer1Clouds, cloudyLayer1Clouds);
    }

    public record Result(
            float rawCoverage,
            float adjustedCoverage,
            int layer1Clouds,
            int cloudyLayer1Clouds) {

        public static final Result ZERO = new Result(0.0F, 0.0F, 0, 0);
    }
}
