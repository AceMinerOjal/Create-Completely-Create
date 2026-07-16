package com.createcompletelycreate.components.extruder.andesite;

import com.createcompletelycreate.components.extruder.AbstractExtruderBlockEntity;
import com.createcompletelycreate.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class ExtruderBlockEntity extends AbstractExtruderBlockEntity {
    @Override
    public boolean isAdvancedMachine() {
        return false;
    }

    public ExtruderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.BLOCK_EXPELLER.get(),
                (be, context) -> context == Direction.DOWN ? be.getItemHandler() : null
        );

    }
}
