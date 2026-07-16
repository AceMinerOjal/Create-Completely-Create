package com.createcompletelycreate.register;

import com.createcompletelycreate.ModConstants;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class ModPartials {
    public static final PartialModel BLOCK_EXPELLER_POLE = block("block_expeller/pole");
    public static final PartialModel BRASS_BLOCK_EXPELLER_POLE = block("brass_block_expeller/pole");
    private static PartialModel block(String path) {
        return PartialModel.of(ModConstants.asResource("block/" + path));
    }
    public static void init() {
        // init static fields
    }
}
