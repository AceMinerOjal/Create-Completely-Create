package com.createcompletelycreate.infrastructure.data;

import com.createcompletelycreate.components.extruder.recipe.ExtrudingRecipe;
import com.createcompletelycreate.components.extruder.recipe.ExtrudingRecipeBuilder;
import net.createmod.catnip.data.Couple;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

import static com.createcompletelycreate.ModConstants.MODID;

public class ExtrudingRecipeGen extends RecipeProvider {

    public ExtrudingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    protected ExtrudingRecipeBuilder create(String id) {
        ExtrudingRecipeBuilder builder = new ExtrudingRecipeBuilder();
        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(MODID, ExtrudingRecipe.Type.ID + "/" + id);
        return builder.withId(recipeId);
    }

    private ExtrudingRecipeBuilder create(String id, Item output) {
        return create(id)
                .withSingleItemOutput(new com.simibubi.create.content.processing.recipe.ProcessingOutput(new ItemStack(output), 1));
    }

    private ExtrudingRecipeBuilder createAdvanced(String id, Item output) {
        return create(id)
                .withSingleItemOutput(new com.simibubi.create.content.processing.recipe.ProcessingOutput(new ItemStack(output), 1))
                .isAdvanced(true);
    }

    private static BlockPredicate tagPredicate(String tagPath) {
        TagKey<Block> tag = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MODID, tagPath));
        return BlockPredicate.Builder.block().of(tag).build();
    }

    private static final BlockPredicate WATER = tagPredicate("extruding/water_equivalents");
    private static final BlockPredicate LAVA = tagPredicate("extruding/lava_equivalents");
    private static final BlockPredicate ICE = tagPredicate("extruding/ice_equivalents");

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        create("cobblestone", Items.COBBLESTONE)
                .withBlockIngredients(WATER, LAVA)
                .save(recipeOutput);

        create("stone", Items.STONE)
                .withBlockIngredients(WATER, LAVA)
                .save(recipeOutput);

        create("limestone")
                .withSingleItemOutput(new com.simibubi.create.content.processing.recipe.ProcessingOutput(new ItemStack(block("create:limestone")), 1))
                .withBlockIngredients(
                        BlockPredicate.Builder.block().of(block("create:honey")).build(),
                        LAVA
                )
                .save(recipeOutput);

        create("scoria")
                .withSingleItemOutput(new com.simibubi.create.content.processing.recipe.ProcessingOutput(new ItemStack(block("create:scoria")), 1))
                .withBlockIngredients(
                        LAVA,
                        BlockPredicate.Builder.block().of(block("create:chocolate")).build()
                )
                .save(recipeOutput);

        createAdvanced("obsidian", Items.OBSIDIAN)
                .withBlockIngredients(WATER, BlockPredicate.Builder.block().of(Blocks.LAVA).build())
                .consumeBlocksSecondBlock()
                .withCatalyst(BlockPredicate.Builder.block().of(Blocks.OBSIDIAN).build())
                .save(recipeOutput);

        createAdvanced("snow_block", Items.SNOW_BLOCK)
                .withBlockIngredients(WATER, BlockPredicate.Builder.block().of(Blocks.WATER).build())
                .consumeBlocksSecondBlock()
                .withCatalyst(ICE)
                .save(recipeOutput);

        create("basalt", Items.BASALT)
                .withBlockIngredients(LAVA, BlockPredicate.Builder.block().of(Blocks.BLUE_ICE).build())
                .withCatalyst(BlockPredicate.Builder.block().of(Blocks.SOUL_SOIL).build())
                .save(recipeOutput);
    }

    private Block block(String resourceLocationString) {
        return block(ResourceLocation.parse(resourceLocationString));
    }

    private Block block(ResourceLocation resourceLocation) {
        return BuiltInRegistries.BLOCK.get(resourceLocation);
    }
}
