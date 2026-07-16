package com.createcompletelycreate.components.extruder.recipe;

import com.createcompletelycreate.foundation.recipe.IRecipeRequirement;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import net.createmod.catnip.data.Couple;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class ExtrudingRecipeBuilder {
    private ResourceLocation id;
    protected ExtrudingRecipe.ExtrudingRecipeParams params;

    public ExtrudingRecipeBuilder() {
        params = new ExtrudingRecipe.ExtrudingRecipeParams(null);
    }

    public ExtrudingRecipeBuilder withId(ResourceLocation id) {
        this.id = id;
        this.params.id = id;
        return this;
    }

    public ExtrudingRecipeBuilder withSingleBlockIngredient(Block ingredient) {
        return withBlockIngredients(ingredient, ingredient);
    }

    public ExtrudingRecipeBuilder withBlockIngredients(Block firstBlockIngredient, Block secondBlockIngredient) {
        return withBlockIngredients(
                Couple.create(
                        BlockPredicate.Builder.block().of(firstBlockIngredient).build(),
                        BlockPredicate.Builder.block().of(secondBlockIngredient).build()
                )
        );
    }

    public ExtrudingRecipeBuilder withBlockIngredients(BlockPredicate firstBlockIngredient, BlockPredicate secondBlockIngredient) {
        return withBlockIngredients(Couple.create(firstBlockIngredient, secondBlockIngredient));
    }

    public ExtrudingRecipeBuilder withBlockIngredients(Couple<BlockPredicate> blockIngredients) {
        params.blockPredicateIngredients = blockIngredients;
        return this;
    }

    public ExtrudingRecipeBuilder withSingleItemOutput(ItemStack output) {
        params.result = new ProcessingOutput(output, 1.0F);
        return this;
    }

    public ExtrudingRecipeBuilder withSingleItemOutput(ProcessingOutput output) {
        params.result = output;
        return this;
    }

    public ExtrudingRecipeBuilder withCatalyst(Block catalyst) {
        return withCatalyst(BlockPredicate.Builder.block().of(catalyst).build());
    }

    public ExtrudingRecipeBuilder withCatalyst(BlockPredicate catalyst) {
        params.catalyst = catalyst;
        return this;
    }

    public ExtrudingRecipeBuilder requiredBonks(int requiredBonks) {
        params.requiredBonks = requiredBonks;
        return this;
    }

    public ExtrudingRecipeBuilder isAdvanced(boolean value) {
        params.isAdvanced = value;
        return this;
    }

    public ExtrudingRecipeBuilder consumeBlocks(Couple<Boolean> pConsumeBlocks) {
        params.consumeBlocks = pConsumeBlocks;
        return this;
    }

    public ExtrudingRecipeBuilder consumeBlocksFirstBlock() {
        params.consumeBlocks.setFirst(true);
        return this;
    }

    public ExtrudingRecipeBuilder consumeBlocksSecondBlock() {
        params.consumeBlocks.setSecond(true);
        return this;
    }

    public ExtrudingRecipeBuilder withRequirements(List<IRecipeRequirement> requirements) {
        params.addRequirements(requirements);
        return this;
    }

    public ExtrudingRecipeBuilder withRequirement(IRecipeRequirement requirement) {
        params.addRequirement(requirement);
        return this;
    }

    public ExtrudingRecipe build() {
        return new ExtrudingRecipe(this.params);
    }

    public ExtrudingRecipeBuilder withSingleItemOutput(NonNullList<ProcessingOutput> results) {
        if (!results.isEmpty())
            params.result = results.getFirst();
        return this;
    }

    public void save(RecipeOutput output) {
        if (id == null) {
            throw new RuntimeException("Recipe ID not set");
        }
        output.accept(id, build(), null);
    }
}
