package com.createcompletelycreate.components.extruder;

import com.createcompletelycreate.ModLang;
import com.createcompletelycreate.components.extruder.recipe.ExtrudingRecipe;
import com.createcompletelycreate.foundation.blockEntity.behaviour.CycleBehavior;
import com.createcompletelycreate.foundation.register.ModRecipeRequirementTypes;
import com.createcompletelycreate.infrastructure.config.ModConfigs;
import com.createcompletelycreate.register.ModRecipes;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.createmod.catnip.data.Couple;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public abstract class AbstractExtruderBlockEntity extends KineticBlockEntity implements CycleBehavior.CycleBehaviourSpecifics {

    private CycleBehavior extrudingBehaviour;
    private FilteringBehaviour filtering;
    // Vertical offset for the extruder head (pole) in model-space units.
    // Andesite variant default: 0.44f (roughly halfway down the block)
    protected float headOffset = 0.44f;

    public abstract boolean isAdvancedMachine();

    // Output inventory that only accepts items produced internally by the extruder.
    // insertItem is overridden to reject all external insertion - extraction is allowed.
    private final ItemStackHandler outputInventory = new ItemStackHandler(1) {
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null)
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    };

    public AbstractExtruderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public @Nullable IItemHandler getItemHandler() {
        return outputInventory;
    }

    public ItemStackHandler getOutputInventory() {
        return outputInventory;
    }

    @Override
    public boolean isSpeedRequirementFulfilled() {
        Optional<ExtrudingRecipe> recipeOpt = getRecipe();
        if (recipeOpt.isEmpty()) {
            return super.isSpeedRequirementFulfilled();
        }
        ExtrudingRecipe recipe = recipeOpt.get();
        Optional<com.createcompletelycreate.foundation.recipe.IRecipeRequirement> minSpeed = recipe.getRequirement(ModRecipeRequirementTypes.MIN_SPEED);
        Optional<com.createcompletelycreate.foundation.recipe.IRecipeRequirement> maxSpeed = recipe.getRequirement(ModRecipeRequirementTypes.MAX_SPEED);

        boolean fulfilled = true;
        if (minSpeed.isPresent()) {
            fulfilled = minSpeed.get().test(level, this);
        }
        if (maxSpeed.isPresent()) {
            fulfilled = fulfilled && maxSpeed.get().test(level, this);
        }
        if (minSpeed.isEmpty() && maxSpeed.isEmpty()) {
            return super.isSpeedRequirementFulfilled();
        }
        return fulfilled;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        filtering = new FilteringBehaviour(this, new ExtruderFilterSlotPositioning())
                .forRecipes();
        behaviours.add(filtering);

        int cycleTime = ModConfigs.server().mechanicalExtruder.cycleTime.get();

        extrudingBehaviour = new CycleBehavior(this, cycleTime, true);
        behaviours.add(extrudingBehaviour);
    }

    public CycleBehavior getExtrudingBehaviour() {
        return extrudingBehaviour;
    }

    public float getHeadOffset() {
        return headOffset;
    }

    public float getKineticSpeed() {
        return getSpeed();
    }

    @Override
    public boolean tryProcess(boolean simulate) {
        Optional<ExtrudingRecipe> recipe = getRecipe();
        if (recipe.isEmpty()) {
            return false;
        }
        ExtrudingRecipe extrudingRecipe = recipe.get();

        if (!hasEnoughOutputSpace(extrudingRecipe))
            return false;

        if (simulate)
            return true;

        ItemStack output = extrudingRecipe.rollOutput(level.random);

        if (!output.isEmpty()) {
            if (isAdvancedMachine()) {
                int multiplier = ModConfigs.server().mechanicalExtruder.brassOutputMultiplier.get();
                output.setCount(output.getCount() * multiplier);
            }

            if (outputInventory.getStackInSlot(0).isEmpty()) {
                outputInventory.setStackInSlot(0, output);
            } else if (outputInventory.getStackInSlot(0).is(output.getItem())) {
                int newCount = outputInventory.getStackInSlot(0).getCount() + output.getCount();
                ItemStack merged = output.copy();
                merged.setCount(Math.min(newCount, outputInventory.getStackInSlot(0).getMaxStackSize()));
                outputInventory.setStackInSlot(0, merged);
            } else {
                return false;
            }
        }

        if (isAdvancedMachine()) {
            for (BlockPredicate blockPredicate : extrudingRecipe.getConsumeBlocksList()) {
                if (blockPredicate.matches(getLeftBlockInWorld()))
                    consumeBlock(getLeftBlockInWorld());
                if (blockPredicate.matches(getRightBlockInWorld()))
                    consumeBlock(getRightBlockInWorld());
            }
        }

        return true;
    }

    private void consumeBlock(BlockInWorld blockInWorld) {
        var level = Objects.requireNonNull(this.getLevel());
        if (level.isClientSide)
            return;
        level.setBlock(blockInWorld.getPos(), Blocks.AIR.defaultBlockState(), 3);
    }

    @Override
    public void playActuateSound() {
        AllSoundEvents.MECHANICAL_PRESS_ACTIVATION_ON_BELT.playOnServer(level, worldPosition);
    }

    @Override
    public void playCompletionSound() {
    }

    @Override
    public void onCycleCompleted() {
    }

    @Override
    public void onOperationCompleted() {
    }

    @Override
    public void playRunningSound() {
    }

    @Override
    public void showParticles() {
    }

    @Override
    public int getCycles() {
        Optional<ExtrudingRecipe> extrudingRecipe = getRecipe();
        return extrudingRecipe.map(ExtrudingRecipe::getRequiredBonks).orElse(1);
    }

    public float getRenderedPoleOffset(float partialTicks) {
        if (!extrudingBehaviour.isRunning())
            return 0;
        int runningTicks = Math.abs(extrudingBehaviour.getRunningTicks());
        float prevTicks = Math.abs(extrudingBehaviour.getPrevRunningTicks());
        float ticks = Mth.lerp(partialTicks, prevTicks, runningTicks);
        float cycleTime = extrudingBehaviour.getCycleTime();
        if (ticks < cycleTime * 2.0f / 3)
            return (float) Mth.clamp(Math.pow(ticks / (cycleTime * 2.0f / 3), 3), 0, 1);
        return Mth.clamp((cycleTime - ticks) / (cycleTime / 3.0f), 0, 1);
    }

    public Optional<ExtrudingRecipe> getRecipe() {
        List<ExtrudingRecipe> matchingRecipes = ModRecipes.findRecipesWithMatchingIngredients(this);
        if (matchingRecipes.isEmpty())
            return Optional.empty();

        if (!isAdvancedMachine())
            matchingRecipes = matchingRecipes.stream().filter(ExtrudingRecipe::notAdvanced).toList();

        for (ExtrudingRecipe recipe : matchingRecipes) {
            if (recipe.meetsRequirements(this))
                return Optional.of(recipe);
        }
        return Optional.empty();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        invalidateCapabilities();
    }

    public FilteringBehaviour getFilter() {
        return filtering;
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.put("OutputInventory", outputInventory.serializeNBT(registries));
        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        outputInventory.deserializeNBT(registries, compound.getCompound("OutputInventory"));
        super.read(compound, registries, clientPacket);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = super.addToGoggleTooltip(tooltip, isPlayerSneaking);

        int currentBonks = extrudingBehaviour.getActuatedTimes();
        if (currentBonks > 0 && extrudingBehaviour.getCycles() > 1) {
            ModLang.translate("goggles.bonks", currentBonks)
                    .forGoggles(tooltip, 1);
            added = true;
        }

        return added;
    }

    public BlockInWorld getLeftBlockInWorld() {
        Direction facing = this.getBlockState().getValue(HORIZONTAL_FACING);
        return new BlockInWorld(Objects.requireNonNull(this.level), this.getBlockPos().relative(facing.getCounterClockWise()), false);
    }

    public BlockInWorld getRightBlockInWorld() {
        Direction facing = this.getBlockState().getValue(HORIZONTAL_FACING);
        return new BlockInWorld(Objects.requireNonNull(this.level), this.getBlockPos().relative(facing.getClockWise()), false);
    }

    public BlockInWorld getCatalystBlock() {
        return new BlockInWorld(Objects.requireNonNull(this.level), this.getBlockPos().above(), false);
    }

    public boolean hasEnoughOutputSpace(ExtrudingRecipe recipe) {
        ItemStack stack = outputInventory.getStackInSlot(0);
        if (stack.isEmpty())
            return true;
        if (!stack.is(recipe.getResult().getStack().getItem()))
            return false;
        int outputCount = recipe.getResult().getStack().getCount();
        if (isAdvancedMachine()) {
            outputCount *= ModConfigs.server().mechanicalExtruder.brassOutputMultiplier.get();
        }
        return stack.getCount() + outputCount <= stack.getMaxStackSize();
    }

    public boolean matchesIngredients(ExtrudingRecipe extrudingRecipe) {
        return extrudingRecipe.match(this);
    }

    public boolean matchesIngredients(RecipeHolder<ExtrudingRecipe> extrudingRecipeRecipeHolder) {
        return matchesIngredients(extrudingRecipeRecipeHolder.value());
    }

    public Couple<BlockInWorld> getSideBlocks() {
        return Couple.create(
                this.getLeftBlockInWorld(),
                this.getRightBlockInWorld()
        );
    }
}
