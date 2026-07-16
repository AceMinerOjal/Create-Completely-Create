package com.createcompletelycreate.components.extruder.andesite;

import com.createcompletelycreate.components.extruder.AbstractExtruderRenderer;
import com.createcompletelycreate.register.ModPartials;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class ExtruderRenderer extends AbstractExtruderRenderer<ExtruderBlockEntity> {
    public ExtruderRenderer(BlockEntityRendererProvider.Context context) {
        super(context, ModPartials.BLOCK_EXPELLER_POLE);
    }
}
