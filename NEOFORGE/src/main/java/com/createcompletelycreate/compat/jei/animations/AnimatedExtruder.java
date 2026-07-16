package com.createcompletelycreate.compat.jei.animations;

import com.createcompletelycreate.components.extruder.andesite.ExtruderBlock;
import com.createcompletelycreate.register.ModBlocks;
import com.createcompletelycreate.register.ModPartials;

public class AnimatedExtruder extends AbstractAnimatedExtruder<ExtruderBlock> {
    public AnimatedExtruder() {
        super(ModBlocks.BLOCK_EXPELLER, ModPartials.BLOCK_EXPELLER_POLE, 0.44f);
    }
}
