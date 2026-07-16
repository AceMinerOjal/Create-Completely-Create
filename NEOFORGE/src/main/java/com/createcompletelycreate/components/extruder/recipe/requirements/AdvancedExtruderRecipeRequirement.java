package com.createcompletelycreate.components.extruder.recipe.requirements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.createcompletelycreate.components.extruder.AbstractExtruderBlockEntity;
import com.createcompletelycreate.foundation.recipe.IRecipeRequirement;
import com.createcompletelycreate.foundation.recipe.RecipeRequirementType;
import com.createcompletelycreate.foundation.register.ModRecipeRequirementTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public record AdvancedExtruderRecipeRequirement(boolean advancedExtruder) implements IRecipeRequirement {
    public static final MapCodec<AdvancedExtruderRecipeRequirement> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.BOOL.fieldOf("advancedExtruder").forGetter(AdvancedExtruderRecipeRequirement::advancedExtruder)
            ).apply(instance, AdvancedExtruderRecipeRequirement::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, AdvancedExtruderRecipeRequirement> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, AdvancedExtruderRecipeRequirement::advancedExtruder,
            AdvancedExtruderRecipeRequirement::new
    );

    @Override
    public boolean test(Level level, BlockEntity blockEntity) {
        if (blockEntity instanceof AbstractExtruderBlockEntity extruder)
            return extruder.isAdvancedMachine() == advancedExtruder;
        return !advancedExtruder;
    }

    @Override
    public String getIdString() {
        return "advanced_extruder";
    }

    @Override
    public RecipeRequirementType<?> getType() {
        return ModRecipeRequirementTypes.ADVANCED_EXTRUDER;
    }
}
