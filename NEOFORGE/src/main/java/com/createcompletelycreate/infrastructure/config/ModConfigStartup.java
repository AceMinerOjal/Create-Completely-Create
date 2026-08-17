package com.createcompletelycreate.infrastructure.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;

public class ModConfigStartup {
    public final BooleanValue paxels;

    public ModConfigStartup(ModConfigSpec.Builder builder) {
        builder.comment(
                        "Whether each optional compat feature added by this mod is enabled.",
                        "Disabled features are not registered and require a game restart to take effect."
                )
                .push("compat");

        paxels = builder.comment("Enable the Create: Quality of Life paxels (multitools).")
                .define("paxels", true);

        builder.pop();
    }
}
