package com.createcompletelycreate.register;

import com.createcompletelycreate.CreateCompletelyCreate;
import com.createcompletelycreate.components.extruder.andesite.ExtruderBlockEntity;
import com.createcompletelycreate.components.extruder.andesite.ExtruderRenderer;
import com.createcompletelycreate.components.extruder.andesite.ExtruderVisual;
import com.createcompletelycreate.components.extruder.brass.BrassExtruderBlockEntity;
import com.createcompletelycreate.components.extruder.brass.BrassExtruderRenderer;
import com.createcompletelycreate.components.extruder.brass.BrassExtruderVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;


public class ModBlockEntities {
    public static final BlockEntityEntry<ExtruderBlockEntity> BLOCK_EXPELLER = CreateCompletelyCreate.registrate()
            .blockEntity("block_expeller", ExtruderBlockEntity::new)
            .visual(() -> ExtruderVisual::new)
            .validBlocks(ModBlocks.BLOCK_EXPELLER)
            .renderer(() -> ExtruderRenderer::new)
            .register();

    public static final BlockEntityEntry<BrassExtruderBlockEntity> BRASS_BLOCK_EXPELLER = CreateCompletelyCreate.registrate()
            .blockEntity("brass_block_expeller", BrassExtruderBlockEntity::new)
            .visual(() -> BrassExtruderVisual::new)
            .validBlocks(ModBlocks.BRASS_BLOCK_EXPELLER)
            .renderer(() -> BrassExtruderRenderer::new)
            .register();

    public static void register() {}
}