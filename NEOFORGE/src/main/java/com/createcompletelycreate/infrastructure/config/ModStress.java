package com.createcompletelycreate.infrastructure.config;

import com.createcompletelycreate.components.extruder.ExtruderConfigs;
import com.createcompletelycreate.register.ModBlocks;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

/**
 * Registers stress impact values for this mod's kinetic blocks
 * with Create's stress API. Values are read from {@link ExtruderConfigs}
 * at runtime.
 */
public class ModStress {
    private final Map<ResourceLocation, DoubleSupplier> impacts = new HashMap<>();

    public ModStress(ExtruderConfigs configs) {
        registerImpact(ModBlocks.BLOCK_EXPELLER, configs.andesite.stressImpact::get);
        registerImpact(ModBlocks.BRASS_BLOCK_EXPELLER, configs.brass.stressImpact::get);
    }

    private void registerImpact(DeferredHolder<?, ?> holder, DoubleSupplier supplier) {
        impacts.put(holder.getKey().location(), supplier);
    }

    @Nullable
    public DoubleSupplier getImpact(Block block) {
        ResourceLocation id = RegisteredObjectsHelper.getKeyOrThrow(block);
        return impacts.get(id);
    }
}
