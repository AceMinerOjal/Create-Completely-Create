package com.createcompletelycreate.components.extruder.andesite;

import com.createcompletelycreate.components.extruder.AbstractExtruderBlock;
import com.createcompletelycreate.register.ModBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public class ExtruderBlock extends AbstractExtruderBlock<ExtruderBlockEntity> {
    public ExtruderBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected IItemHandlerModifiable getOutputInventory(ExtruderBlockEntity extruder) {
        return extruder.getOutputInventory();
    }

    @Override
    protected void sendDataInternal(ExtruderBlockEntity extruder) {
        extruder.sendData();
    }

    @Override
    public Class<ExtruderBlockEntity> getBlockEntityClass() {
        return ExtruderBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ExtruderBlockEntity> getBlockEntityType() {
        return ModBlockEntities.BLOCK_EXPELLER.get();
    }
}
