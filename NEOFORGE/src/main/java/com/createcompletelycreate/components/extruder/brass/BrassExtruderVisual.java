package com.createcompletelycreate.components.extruder.brass;

import com.createcompletelycreate.components.extruder.AbstractExtruderVisual;
import com.createcompletelycreate.register.ModPartials;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;

public class BrassExtruderVisual extends AbstractExtruderVisual<BrassExtruderBlockEntity> {
    public BrassExtruderVisual(VisualizationContext context, BrassExtruderBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, ModPartials.BRASS_BLOCK_EXPELLER_POLE);
    }
}
