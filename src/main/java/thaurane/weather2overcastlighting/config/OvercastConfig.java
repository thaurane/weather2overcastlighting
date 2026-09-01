package thaurane.weather2overcastlighting.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class OvercastConfig {

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.DoubleValue sampleRadius;
    public static final ForgeConfigSpec.DoubleValue coverageMultiplier;
    public static final ForgeConfigSpec.DoubleValue coverageStart;
    public static final ForgeConfigSpec.DoubleValue darknessAtStart;
    public static final ForgeConfigSpec.DoubleValue darknessAtFull;
    public static final ForgeConfigSpec.DoubleValue darknessScale;
    public static final ForgeConfigSpec.DoubleValue smoothingPerTick;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("overcast");

        sampleRadius = builder
                .comment("Radius in blocks used to evaluate local Weather2 layer 1 cloud coverage.")
                .defineInRange("sampleRadius", 128.0D, 16.0D, 1024.0D);

        coverageMultiplier = builder
                .comment("Multiplier applied to measured local layer 1 cloud coverage.")
                .defineInRange("coverageMultiplier", 2.25D, 0.0D, 10.0D);

        coverageStart = builder
                .comment("Adjusted cloud coverage where darkening begins. 0.70 = 70%.")
                .defineInRange("coverageStart", 0.70D, 0.0D, 1.0D);

        darknessAtStart = builder
                .comment("Base weather-style darkness at coverageStart before darknessScale is applied.")
                .defineInRange("darknessAtStart", 0.40D, 0.0D, 1.0D);

        darknessAtFull = builder
                .comment("Base weather-style darkness at 100% adjusted coverage before darknessScale is applied.")
                .defineInRange("darknessAtFull", 0.70D, 0.0D, 1.0D);

        darknessScale = builder
                .comment(
                        "Final multiplier applied to the layer-1 overcast darkness.",
                        "0.50 halves the previous darkening strength.",
                        "With the defaults this makes the effective range about 20% to 35% darkness.")
                .defineInRange("darknessScale", 0.50D, 0.0D, 2.0D);

        smoothingPerTick = builder
                .comment("Maximum amount darkness can change per client tick.")
                .defineInRange("smoothingPerTick", 0.0025D, 0.0001D, 1.0D);

        builder.pop();
        SPEC = builder.build();
    }

    private OvercastConfig() {
    }
}
