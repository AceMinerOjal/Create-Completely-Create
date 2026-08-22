package com.createcompletelycreate.components.extruder;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ExtruderConfigs {
    public final TierConfig andesite;
    public final BrassTierConfig brass;

    public ExtruderConfigs(ModConfigSpec.Builder builder) {
        builder.comment("Mechanical Extruder settings.",
                        "Configure each tier independently.")
                .push("block_expeller");

        andesite = new TierConfig(builder, "andesite",
                "Andesite Extruder — the basic tier.",
                240, 4.0);

        brass = new BrassTierConfig(builder, "brass",
                "Brass Extruder — advanced tier with higher output.",
                240, 16.0);

        builder.pop();
    }

    public static class TierConfig {
        public final ModConfigSpec.IntValue cycleTime;
        public final ModConfigSpec.DoubleValue stressImpact;

        public TierConfig(ModConfigSpec.Builder builder, String name,
                          String comment,
                          int defaultCycle, double defaultImpact) {
            builder.comment(comment).push(name);

            cycleTime = builder
                    .comment("Cycle duration in ticks. 20 ticks = 1 second.",
                             "Default: " + defaultCycle + " (" + (defaultCycle / 20) + "s)")
                    .defineInRange("cycleTime", defaultCycle, 1, 72000);

            stressImpact = builder
                    .comment("Stress impact in Stress Units (SU).",
                             "Doubled for every speed increase the machine receives.",
                             "Default: " + defaultImpact)
                    .defineInRange("stressImpact", defaultImpact, 0.0, 1024.0);

            defineExtras(builder);

            builder.pop();
        }

        protected void defineExtras(ModConfigSpec.Builder builder) {
        }
    }

    public static class BrassTierConfig extends TierConfig {
        public ModConfigSpec.IntValue outputMultiplier;

        public BrassTierConfig(ModConfigSpec.Builder builder, String name,
                               String comment,
                               int defaultCycle, double defaultImpact) {
            super(builder, name, comment, defaultCycle, defaultImpact);
        }

        @Override
        protected void defineExtras(ModConfigSpec.Builder builder) {
            outputMultiplier = builder
                    .comment("Output item count multiplier per cycle.",
                             "Default: " + 8)
                    .defineInRange("outputMultiplier", 8, 1, 256);
        }
    }
}
