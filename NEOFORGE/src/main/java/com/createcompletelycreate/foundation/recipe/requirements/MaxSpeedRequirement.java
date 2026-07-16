package com.createcompletelycreate.foundation.recipe.requirements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.createcompletelycreate.foundation.recipe.IRecipeRequirement;
import com.createcompletelycreate.foundation.recipe.RecipeRequirementType;
import com.createcompletelycreate.foundation.register.ModRecipeRequirementTypes;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public record MaxSpeedRequirement(float maxSpeed) implements IRecipeRequirement {
    public static final MapCodec<MaxSpeedRequirement> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.FLOAT.fieldOf("maxSpeed").forGetter(MaxSpeedRequirement::maxSpeed)
            ).apply(instance, MaxSpeedRequirement::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, MaxSpeedRequirement> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, MaxSpeedRequirement::maxSpeed,
            MaxSpeedRequirement::new
    );

    public static MaxSpeedRequirement of(float maxSpeed) {
        return new MaxSpeedRequirement(maxSpeed);
    }

    @Override
    public boolean test(Level level, BlockEntity blockEntity) {
        if (blockEntity instanceof KineticBlockEntity kinetic) {
            return Math.abs(kinetic.getSpeed()) <= maxSpeed;
        }
        return true;
    }

    @Override
    public String getIdString() {
        return "max_speed";
    }

    @Override
    public RecipeRequirementType<?> getType() {
        return ModRecipeRequirementTypes.MAX_SPEED;
    }
}
