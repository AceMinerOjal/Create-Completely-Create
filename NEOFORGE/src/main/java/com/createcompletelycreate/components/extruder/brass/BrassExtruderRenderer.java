package com.createcompletelycreate.components.extruder.brass;

import com.createcompletelycreate.components.extruder.AbstractExtruderRenderer;
import com.createcompletelycreate.register.ModPartials;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class BrassExtruderRenderer extends AbstractExtruderRenderer<BrassExtruderBlockEntity> {
    public BrassExtruderRenderer(BlockEntityRendererProvider.Context context) {
        super(context, ModPartials.BRASS_BLOCK_EXPELLER_POLE);
    }
}
