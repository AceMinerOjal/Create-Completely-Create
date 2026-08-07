package com.createcompletelycreate.components.extruder;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ExtruderConfigs {
    public final ModConfigSpec.IntValue cycleTime;
    public final ModConfigSpec.IntValue brassOutputMultiplier;

    public ExtruderConfigs(ModConfigSpec.Builder builder) {
        builder.comment("Mechanical Extruder Configs")
                .push("block_expeller.v1");

        cycleTime = builder.comment("Duration of the extruding cycle, in ticks.")
                .defineInRange("cycleTime", 240, 1, 72000);
        brassOutputMultiplier = builder.comment("Output multiplier for brass extruder")
                .defineInRange("brassOutputMultiplier", 8, 1, 64);

        builder.pop();
    }
}
