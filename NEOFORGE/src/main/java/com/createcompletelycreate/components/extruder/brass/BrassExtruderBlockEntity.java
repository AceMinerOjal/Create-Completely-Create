package com.createcompletelycreate.components.extruder.brass;

import com.createcompletelycreate.components.extruder.AbstractExtruderBlockEntity;
import com.createcompletelycreate.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class BrassExtruderBlockEntity extends AbstractExtruderBlockEntity {
    @Override
    public boolean isAdvancedMachine() {
        return true;
    }

    public BrassExtruderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.headOffset = 0.29f;
    }
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.BRASS_BLOCK_EXPELLER.get(),
                (be, context) -> context == Direction.DOWN ? be.getItemHandler() : null
        );

    }
}
