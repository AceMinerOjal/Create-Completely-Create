package com.createcompletelycreate.foundation.recipe.requirements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.createcompletelycreate.foundation.recipe.IRecipeRequirement;
import com.createcompletelycreate.foundation.recipe.RecipeRequirementType;
import com.createcompletelycreate.foundation.register.ModRecipeRequirementTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;

public record BiomeTagRequirement(TagKey<Biome> biomeTag) implements IRecipeRequirement {
    public static final Codec<TagKey<Biome>> BIOME_TAG_CODEC = TagKey.codec(Registries.BIOME);
    public static final StreamCodec<RegistryFriendlyByteBuf, TagKey<Biome>> BIOME_TAG_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public TagKey<Biome> decode(RegistryFriendlyByteBuf buf) {
            ResourceLocation loc = ResourceLocation.STREAM_CODEC.decode(buf);
            return TagKey.create(Registries.BIOME, loc);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, TagKey<Biome> value) {
            ResourceLocation.STREAM_CODEC.encode(buf, value.location());
        }
    };

    public static final MapCodec<BiomeTagRequirement> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    BIOME_TAG_CODEC.fieldOf("biomeTag").forGetter(BiomeTagRequirement::biomeTag)
            ).apply(instance, BiomeTagRequirement::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, BiomeTagRequirement> STREAM_CODEC = StreamCodec.composite(
            BIOME_TAG_STREAM_CODEC, BiomeTagRequirement::biomeTag,
            BiomeTagRequirement::new
    );

    public static BiomeTagRequirement of(TagKey<Biome> biomeTag) {
        return new BiomeTagRequirement(biomeTag);
    }

    @Override
    public boolean test(Level level, BlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        Holder<Biome> biomeHolder = level.getBiome(pos);
        return biomeHolder.is(biomeTag);
    }

    @Override
    public String getIdString() {
        return "biome_tag";
    }

    @Override
    public RecipeRequirementType<?> getType() {
        return ModRecipeRequirementTypes.BIOME_TAG;
    }
}
