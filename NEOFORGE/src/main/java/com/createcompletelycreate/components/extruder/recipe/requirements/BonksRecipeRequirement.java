package com.createcompletelycreate.components.extruder.recipe.requirements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.createcompletelycreate.foundation.recipe.IRecipeRequirement;
import com.createcompletelycreate.foundation.recipe.RecipeRequirementType;
import com.createcompletelycreate.foundation.blockEntity.behaviour.CycleBehavior;
import com.createcompletelycreate.foundation.register.ModRecipeRequirementTypes;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public record BonksRecipeRequirement(int bonks) implements IRecipeRequirement {
    public static final MapCodec<BonksRecipeRequirement> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.INT.fieldOf("bonks").forGetter(BonksRecipeRequirement::bonks)
            ).apply(instance, BonksRecipeRequirement::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, BonksRecipeRequirement> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, BonksRecipeRequirement::bonks,
            BonksRecipeRequirement::new
    );

    @Override
    public boolean test(Level level, BlockEntity blockEntity) {
        if (blockEntity instanceof SmartBlockEntity smart) {
            CycleBehavior cycle = smart.getBehaviour(CycleBehavior.TYPE);
            if (cycle != null)
                return cycle.getActuatedTimes() >= bonks;
        }
        return true;
    }

    @Override
    public String getIdString() {
        return "bonks";
    }

    @Override
    public RecipeRequirementType<?> getType() {
        return ModRecipeRequirementTypes.BONKS;
    }

    @Override
    public String toString() {
        return String.valueOf(bonks);
    }
}
