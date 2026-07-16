package com.createcompletelycreate.components.extruder.andesite;

import com.createcompletelycreate.components.extruder.AbstractExtruderVisual;
import com.createcompletelycreate.register.ModPartials;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;

public class ExtruderVisual extends AbstractExtruderVisual<ExtruderBlockEntity> {
    public ExtruderVisual(VisualizationContext context, ExtruderBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, ModPartials.BLOCK_EXPELLER_POLE);
    }
}
