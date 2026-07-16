package com.createcompletelycreate.compat.jei.animations;

import com.createcompletelycreate.components.extruder.brass.BrassExtruderBlock;
import com.createcompletelycreate.register.ModBlocks;
import com.createcompletelycreate.register.ModPartials;

public class AnimatedBrassExtruder extends AbstractAnimatedExtruder<BrassExtruderBlock> {
    public AnimatedBrassExtruder() {
        super(ModBlocks.BRASS_BLOCK_EXPELLER, ModPartials.BRASS_BLOCK_EXPELLER_POLE, 0.29f);
    }
}
