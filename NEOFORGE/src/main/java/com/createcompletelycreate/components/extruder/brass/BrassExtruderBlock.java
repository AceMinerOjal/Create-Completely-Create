package com.createcompletelycreate.components.extruder.brass;

import com.createcompletelycreate.components.extruder.AbstractExtruderBlock;
import com.createcompletelycreate.register.ModBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public class BrassExtruderBlock extends AbstractExtruderBlock<BrassExtruderBlockEntity> {
    public BrassExtruderBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected IItemHandlerModifiable getOutputInventory(BrassExtruderBlockEntity extruder) {
        return extruder.getOutputInventory();
    }

    @Override
    protected void sendDataInternal(BrassExtruderBlockEntity extruder) {
        extruder.sendData();
    }

    @Override
    public Class<BrassExtruderBlockEntity> getBlockEntityClass() {
        return BrassExtruderBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BrassExtruderBlockEntity> getBlockEntityType() {
        return ModBlockEntities.BRASS_BLOCK_EXPELLER.get();
    }
}
