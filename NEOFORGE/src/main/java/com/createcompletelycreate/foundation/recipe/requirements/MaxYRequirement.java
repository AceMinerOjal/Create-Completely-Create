package com.createcompletelycreate.foundation.recipe.requirements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.createcompletelycreate.foundation.recipe.IRecipeRequirement;
import com.createcompletelycreate.foundation.recipe.RecipeRequirementType;
import com.createcompletelycreate.foundation.register.ModRecipeRequirementTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public record MaxYRequirement(int maxY) implements IRecipeRequirement {
    public static final MapCodec<MaxYRequirement> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.INT.fieldOf("maxY").forGetter(MaxYRequirement::maxY)
            ).apply(instance, MaxYRequirement::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, MaxYRequirement> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, MaxYRequirement::maxY,
            MaxYRequirement::new
    );

    public static MaxYRequirement of(int maxY) {
        return new MaxYRequirement(maxY);
    }

    @Override
    public boolean test(Level level, BlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return pos.getY() <= maxY;
    }

    @Override
    public String getIdString() {
        return "max_y";
    }

    @Override
    public RecipeRequirementType<?> getType() {
        return ModRecipeRequirementTypes.MAX_Y;
    }
}
