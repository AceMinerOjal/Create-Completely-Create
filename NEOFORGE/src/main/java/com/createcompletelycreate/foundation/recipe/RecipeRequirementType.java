package com.createcompletelycreate.foundation.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RecipeRequirementType<T extends IRecipeRequirement> {
    private static final Map<String, RecipeRequirementType<?>> TYPES = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static final Codec<IRecipeRequirement> REQUIREMENT_CODEC = Codec.STRING.dispatch(
            (Function<IRecipeRequirement, String>) r -> r.getType().getId(),
            (Function<String, MapCodec<? extends IRecipeRequirement>>) id -> {
                RecipeRequirementType<?> type = TYPES.get(id);
                if (type == null) {
                    throw new RuntimeException("Unknown requirement type: " + id);
                }
                return (MapCodec<? extends IRecipeRequirement>) type.getCodec();
            }
    );

    @SuppressWarnings("unchecked")
    public static final StreamCodec<RegistryFriendlyByteBuf, IRecipeRequirement> REQUIREMENT_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public IRecipeRequirement decode(RegistryFriendlyByteBuf buf) {
            String id = ByteBufCodecs.STRING_UTF8.decode(buf);
            RecipeRequirementType<?> type = TYPES.get(id);
            if (type == null) {
                throw new RuntimeException("Unknown requirement type: " + id);
            }
            StreamCodec<RegistryFriendlyByteBuf, IRecipeRequirement> castCodec =
                    (StreamCodec<RegistryFriendlyByteBuf, IRecipeRequirement>) (StreamCodec<RegistryFriendlyByteBuf, ?>) type.getStreamCodec();
            return castCodec.decode(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, IRecipeRequirement value) {
            ByteBufCodecs.STRING_UTF8.encode(buf, value.getType().getId());
            StreamCodec<RegistryFriendlyByteBuf, IRecipeRequirement> castCodec =
                    (StreamCodec<RegistryFriendlyByteBuf, IRecipeRequirement>) (StreamCodec<RegistryFriendlyByteBuf, ?>) value.getType().getStreamCodec();
            castCodec.encode(buf, value);
        }
    };

    private final String id;
    private final MapCodec<T> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

    public RecipeRequirementType(String id, MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        this.id = id;
        this.codec = codec;
        this.streamCodec = streamCodec;
        TYPES.put(id, this);
    }

    public String getId() {
        return id;
    }

    public MapCodec<T> getCodec() {
        return codec;
    }

    public StreamCodec<RegistryFriendlyByteBuf, T> getStreamCodec() {
        return streamCodec;
    }
}
