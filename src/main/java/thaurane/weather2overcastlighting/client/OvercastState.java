package thaurane.weather2overcastlighting.client;

import thaurane.weather2overcastlighting.config.OvercastConfig;

public final class OvercastState {

    private static float currentDarkness = 0.0F;
    private static float targetDarkness = 0.0F;
    private static Layer1Coverage.Result lastCoverage = Layer1Coverage.Result.ZERO;

    private OvercastState() {
    }

    public static void updateTarget(Layer1Coverage.Result coverage) {
        lastCoverage = coverage;
        targetDarkness = darknessFromCoverage(coverage.adjustedCoverage());

        float step = OvercastConfig.smoothingPerTick.get().floatValue();
        currentDarkness = approach(currentDarkness, targetDarkness, step);
    }

    public static void reset() {
        currentDarkness = 0.0F;
        targetDarkness = 0.0F;
        lastCoverage = Layer1Coverage.Result.ZERO;
    }

    public static float currentDarkness() {
        return currentDarkness;
    }

    public static float targetDarkness() {
        return targetDarkness;
    }

    public static Layer1Coverage.Result lastCoverage() {
        return lastCoverage;
    }

    private static float darknessFromCoverage(float coverage) {
        float start = OvercastConfig.coverageStart.get().floatValue();

        if (coverage < start) {
            return 0.0F;
        }

        float startDarkness = OvercastConfig.darknessAtStart.get().floatValue();
        float fullDarkness = OvercastConfig.darknessAtFull.get().floatValue();

        float baseDarkness;

        if (start >= 1.0F) {
            baseDarkness = coverage >= 1.0F ? fullDarkness : 0.0F;
        } else {
            float progress = (coverage - start) / (1.0F - start);
            progress = Math.max(0.0F, Math.min(1.0F, progress));

            baseDarkness = startDarkness + progress * (fullDarkness - startDarkness);
        }

        float scale = OvercastConfig.darknessScale.get().floatValue();

        return Math.max(
                0.0F,
                Math.min(1.0F, baseDarkness * scale));
    }

    private static float approach(float current, float target, float step) {
        if (current < target) {
            return Math.min(target, current + step);
        }

        if (current > target) {
            return Math.max(target, current - step);
        }

        return current;
    }
}
