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

public record MinSpeedRequirement(float minSpeed) implements IRecipeRequirement {
    public static final MapCodec<MinSpeedRequirement> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.FLOAT.fieldOf("minSpeed").forGetter(MinSpeedRequirement::minSpeed)
            ).apply(instance, MinSpeedRequirement::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, MinSpeedRequirement> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, MinSpeedRequirement::minSpeed,
            MinSpeedRequirement::new
    );

    public static MinSpeedRequirement of(float minSpeed) {
        return new MinSpeedRequirement(minSpeed);
    }

    @Override
    public boolean test(Level level, BlockEntity blockEntity) {
        if (blockEntity instanceof KineticBlockEntity kinetic) {
            return Math.abs(kinetic.getSpeed()) >= minSpeed;
        }
        return true;
    }

    @Override
    public String getIdString() {
        return "min_speed";
    }

    @Override
    public RecipeRequirementType<?> getType() {
        return ModRecipeRequirementTypes.MIN_SPEED;
    }
}
