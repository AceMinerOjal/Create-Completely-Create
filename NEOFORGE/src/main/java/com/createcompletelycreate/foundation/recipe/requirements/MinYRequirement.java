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

public record MinYRequirement(int minY) implements IRecipeRequirement {
    public static final MapCodec<MinYRequirement> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.INT.fieldOf("minY").forGetter(MinYRequirement::minY)
            ).apply(instance, MinYRequirement::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, MinYRequirement> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, MinYRequirement::minY,
            MinYRequirement::new
    );

    public static MinYRequirement of(int minY) {
        return new MinYRequirement(minY);
    }

    @Override
    public boolean test(Level level, BlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return pos.getY() >= minY;
    }

    @Override
    public String getIdString() {
        return "min_y";
    }

    @Override
    public RecipeRequirementType<?> getType() {
        return ModRecipeRequirementTypes.MIN_Y;
    }
}
