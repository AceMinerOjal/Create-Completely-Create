package com.createcompletelycreate.components.extruder.recipe;

import com.createcompletelycreate.components.extruder.AbstractExtruderBlockEntity;
import com.createcompletelycreate.components.extruder.recipe.requirements.AdvancedExtruderRecipeRequirement;
import com.createcompletelycreate.components.extruder.recipe.requirements.BonksRecipeRequirement;
import com.createcompletelycreate.foundation.recipe.IRecipeRequirement;
import com.createcompletelycreate.foundation.recipe.RecipeRequirementType;
import com.createcompletelycreate.register.ModRecipes;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.createmod.catnip.data.Couple;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class ExtrudingRecipe implements Recipe<RecipeInput> {
    public static final BlockPredicate ANY_BLOCK = new BlockPredicate(Optional.empty(), Optional.empty(), Optional.empty());

    private ResourceLocation id;
    private ArrayList<IRecipeRequirement> recipeRequirements = new ArrayList<>();

    private Couple<BlockPredicate> blockPredicateIngredients;
    private BlockPredicate catalyst;
    private ProcessingOutput result;
    private int requiredBonks;
    private boolean isAdvanced;
    private Couple<Boolean> consumeBlocks;

    public ArrayList<IRecipeRequirement> getRecipeRequirements() {
        return recipeRequirements;
    }

    public ExtrudingRecipe(ExtrudingRecipeParams params) {
        this.id = params.id;
        this.result = params.result;
        this.blockPredicateIngredients = params.blockPredicateIngredients;
        this.catalyst = params.catalyst;
        this.requiredBonks = params.requiredBonks;
        this.isAdvanced = params.isAdvanced;
        this.consumeBlocks = params.consumeBlocks;
        this.recipeRequirements.addAll(params.getRecipeRequirements());
    }

    public boolean meetsRequirements(net.minecraft.world.level.block.entity.BlockEntity blockEntity) {
        for (IRecipeRequirement requirement : recipeRequirements) {
            if (!requirement.test(blockEntity.getLevel(), blockEntity)) {
                return false;
            }
        }
        return true;
    }

    public Optional<IRecipeRequirement> getRequirement(RecipeRequirementType type) {
        for (IRecipeRequirement requirement : recipeRequirements) {
            if (requirement.getType() == type) {
                return Optional.of(requirement);
            }
        }
        return Optional.empty();
    }

    private boolean matchIngredient(BlockPredicate blockIngredient, BlockInWorld blockInWorld) {
        if (blockIngredient.blocks().isEmpty())
            return blockIngredient.matches(blockInWorld);
        if (blockInWorld.getState().hasProperty(WATERLOGGED) && blockInWorld.getState().getValue(WATERLOGGED)) {
            boolean isWaterOnly = blockIngredient.blocks().get().size() == 1
                    && blockIngredient.blocks().get().get(0).is(Blocks.WATER.builtInRegistryHolder().key().location());
            if (isWaterOnly && blockIngredient.properties().isEmpty() && blockIngredient.nbt().isEmpty()) {
                return true;
            }
        }
        return blockIngredient.matches(blockInWorld);
    }

    public <EXB extends AbstractExtruderBlockEntity> boolean matchIngredients(EXB extruderBlockEntity, Couple<BlockPredicate> blockIngredients) {
        Couple<BlockInWorld> sideBlocks = extruderBlockEntity.getSideBlocks();

        BlockInWorld leftBlock = sideBlocks.getFirst();
        BlockInWorld rightBlock = sideBlocks.getSecond();

        BlockPredicate first = blockIngredients.getFirst();
        BlockPredicate second = blockIngredients.getSecond();

        boolean firstLeft = matchIngredient(first, leftBlock);
        boolean firstRight = matchIngredient(first, rightBlock);
        boolean secondLeft = matchIngredient(second, leftBlock);
        boolean secondRight = matchIngredient(second, rightBlock);

        return (firstLeft && secondRight) || (secondLeft && firstRight);
    }

    public <EXB extends AbstractExtruderBlockEntity> boolean match(EXB extruderBlockEntity) {
        if (Objects.requireNonNull(extruderBlockEntity.getLevel()).isClientSide)
            return false;
        FilteringBehaviour filter = extruderBlockEntity.getFilter();
        if (filter == null)
            return false;
        if (!filter.test(this.getResultItemStack()))
            return false;
        if (!matchIngredients(extruderBlockEntity, this.getBlockPredicateIngredients()))
            return false;
        if (this.catalyst.blocks().isPresent() && !this.catalyst.matches(extruderBlockEntity.getCatalystBlock()))
            return false;
        return true;
    }

    @Override
    public boolean matches(RecipeInput recipeInput, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(RecipeInput recipeInput, HolderLookup.Provider provider) {
        return getResultItem(provider);
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return getRollableResult().getStack().isEmpty() ? ItemStack.EMPTY
                : getRollableResult().getStack();
    }

    public ProcessingOutput getRollableResult() {
        return result;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ExtrudingRecipeSerializer.INSTANCE;
    }

    public ItemStack rollOutput(RandomSource source) {
        return result.rollOutput(source);
    }

    public ItemStack getResultItemStack() {
        return result.getStack();
    }

    public ProcessingOutput getResult() {
        return result;
    }

    public BlockPredicate getCatalyst() {
        return catalyst;
    }

    public int getRequiredBonks() {
        return requiredBonks;
    }

    public boolean isAdvanced() {
        return isAdvanced;
    }

    public boolean notAdvanced() {
        return !isAdvanced;
    }

    public Couple<Boolean> getConsumeBlocks() {
        return consumeBlocks;
    }

    public List<BlockPredicate> getConsumeBlocksList() {
        ArrayList<BlockPredicate> list = new ArrayList<>();
        if (consumeBlocks.getFirst())
            list.add(blockPredicateIngredients.getFirst());
        if (consumeBlocks.getSecond())
            list.add(blockPredicateIngredients.getSecond());
        return list;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipes.EXTRUDING_TYPE.get();
    }

    public static boolean hasCatalyst(net.minecraft.world.item.crafting.RecipeHolder<ExtrudingRecipe> extrudingRecipeRecipeHolder) {
        return extrudingRecipeRecipeHolder.value().catalyst.blocks().isPresent();
    }

    public ResourceLocation getId() {
        return id;
    }

    public List<IRecipeRequirement> getJeiRecipeRequirements() {
        ArrayList<IRecipeRequirement> extraRequirements = new ArrayList<>();
        if (isAdvanced())
            extraRequirements.add(new AdvancedExtruderRecipeRequirement(true));
        if (requiredBonks > 1)
            extraRequirements.add(new BonksRecipeRequirement(requiredBonks));
        return Stream.concat(extraRequirements.stream(), recipeRequirements.stream()).toList();
    }

    public List<net.neoforged.neoforge.common.conditions.ICondition> getConditions() {
        return List.of();
    }

    public static class Type implements RecipeType<ExtrudingRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "extruding";
    }

    public static class ExtrudingRecipeParams {
        protected ResourceLocation id;
        protected Couple<BlockPredicate> blockPredicateIngredients;
        protected ProcessingOutput result;
        protected BlockPredicate catalyst;
        protected int requiredBonks;
        protected boolean isAdvanced;
        protected Couple<Boolean> consumeBlocks;
        private ArrayList<IRecipeRequirement> recipeRequirements;

        protected ExtrudingRecipeParams(ResourceLocation id) {
            this.id = id;
            blockPredicateIngredients = Couple.create(ANY_BLOCK, ANY_BLOCK);
            result = ProcessingOutput.EMPTY;
            catalyst = BlockPredicate.Builder.block().build();
            requiredBonks = 1;
            isAdvanced = false;
            consumeBlocks = Couple.create(false, false);
            recipeRequirements = new ArrayList<>();
        }

        public ArrayList<IRecipeRequirement> getRecipeRequirements() {
            return recipeRequirements;
        }

        public void addRequirement(IRecipeRequirement requirement) {
            recipeRequirements.add(requirement);
        }

        public void addRequirements(List<IRecipeRequirement> requirements) {
            recipeRequirements.addAll(requirements);
        }
    }

    public Couple<BlockPredicate> getBlockPredicateIngredients() {
        return blockPredicateIngredients;
    }
}
