package com.createcompletelycreate.infrastructure.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ModConfigCommon {
    public ModConfigCommon(ModConfigSpec.Builder builder) {
        builder.comment("Common settings for Create: Completely Create.")
                .push("general");
        builder.pop();
    }
}
