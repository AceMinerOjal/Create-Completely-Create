package com.createcompletelycreate.foundation.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public interface IRecipeRequirement {
    Codec<IRecipeRequirement> CODEC = RecipeRequirementType.REQUIREMENT_CODEC;
    StreamCodec<RegistryFriendlyByteBuf, IRecipeRequirement> STREAM_CODEC = RecipeRequirementType.REQUIREMENT_STREAM_CODEC;
    Codec<List<IRecipeRequirement>> LIST_CODEC = CODEC.listOf();

    static StreamCodec<RegistryFriendlyByteBuf, List<IRecipeRequirement>> listStreamCodec() {
        return new StreamCodec<>() {
            @Override
            public List<IRecipeRequirement> decode(RegistryFriendlyByteBuf buf) {
                int size = ByteBufCodecs.VAR_INT.decode(buf);
                List<IRecipeRequirement> list = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    list.add(STREAM_CODEC.decode(buf));
                }
                return list;
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, List<IRecipeRequirement> value) {
                ByteBufCodecs.VAR_INT.encode(buf, value.size());
                for (IRecipeRequirement req : value) {
                    STREAM_CODEC.encode(buf, req);
                }
            }
        };
    }

    StreamCodec<RegistryFriendlyByteBuf, List<IRecipeRequirement>> LIST_STREAM_CODEC = listStreamCodec();

    boolean test(Level level, BlockEntity blockEntity);

    String getIdString();

    RecipeRequirementType<?> getType();
}
