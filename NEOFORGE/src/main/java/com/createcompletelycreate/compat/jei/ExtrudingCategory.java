package com.createcompletelycreate.compat.jei;

import com.createcompletelycreate.ModConstants;
import com.createcompletelycreate.ModLang;
import com.createcompletelycreate.compat.jei.animations.AnimatedBrassExtruder;
import com.createcompletelycreate.compat.jei.animations.AnimatedExtruder;
import com.createcompletelycreate.components.extruder.recipe.ExtrudingRecipe;
import com.createcompletelycreate.register.ModBlocks;
import com.createcompletelycreate.register.ModRecipes;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.simibubi.create.compat.jei.ItemIcon;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ExtrudingCategory extends CreateRecipeCategory<ExtrudingRecipe> {
    public static final BlockPredicate ANY = new BlockPredicate(Optional.empty(), Optional.empty(), Optional.empty());

    private AnimatedExtruder extruder = new AnimatedExtruder();
    private AnimatedBrassExtruder brassExtruder = new AnimatedBrassExtruder();

    @SuppressWarnings("unchecked")
    public final static CreateRecipeCategory<ExtrudingRecipe> INFO = new ExtrudingCategory(
            new Info<>(
                    (mezz.jei.api.recipe.RecipeType<net.minecraft.world.item.crafting.RecipeHolder<ExtrudingRecipe>>)
                            (mezz.jei.api.recipe.RecipeType<?>)
                            new mezz.jei.api.recipe.RecipeType<>(ModConstants.asResource("extruding"), ExtrudingRecipe.class),
                    Component.translatable(ModConstants.MODID + ".recipe.extruding"),
                    new com.simibubi.create.compat.jei.EmptyBackground(177, 85),
                    new ItemIcon(() -> new ItemStack(ModBlocks.BLOCK_EXPELLER.asItem())),
                    () -> ExtrudingCategory.getAllHolders(),
                    List.of(
                            () -> new ItemStack(ModBlocks.BLOCK_EXPELLER.get()),
                            () -> new ItemStack(ModBlocks.BRASS_BLOCK_EXPELLER.get())
                    )
            )
    );

    public ExtrudingCategory(Info<ExtrudingRecipe> info) {
        super(info);
    }

    public static List<net.minecraft.world.item.crafting.RecipeHolder<ExtrudingRecipe>> getAllHolders() {
        var connection = net.minecraft.client.Minecraft.getInstance().getConnection();
        if (connection == null)
            return List.of();
        return connection.getRecipeManager()
                .getAllRecipesFor(ModRecipes.EXTRUDING_TYPE.get());
    }

    public static boolean isAny(BlockPredicate predicate) {
        if (predicate == ANY) {
            return true;
        }
        return predicate.blocks().isEmpty() && predicate.properties().isEmpty() && predicate.nbt().isEmpty();
    }

    public static Set<Block> matchedBlocks(BlockPredicate predicate) {
        if (isAny(predicate)) {
            return Set.of();
        }
        final var blocks = Lists.<Holder<Block>>newArrayList();
        if (predicate.blocks().isPresent()) {
            Iterables.addAll(
                    blocks,
                    predicate.blocks().get().unwrap().map(BuiltInRegistries.BLOCK::getOrCreateTag, Function.identity()));
        }
        return blocks.stream().map(Holder::value).collect(Collectors.toSet());
    }

    public static Set<Fluid> matchedFluids(BlockPredicate predicate) {
        return matchedBlocks(predicate).stream()
                .filter(LiquidBlock.class::isInstance)
                .map(it -> it.defaultBlockState().getFluidState())
                .filter(Predicate.not(FluidState::isEmpty))
                .map(FluidState::getType)
                .collect(Collectors.toSet());
    }

    public static Set<FluidStack> matchedFluidStacks(BlockPredicate predicate) {
        return matchedFluids(predicate).stream()
                .map(fluid -> new FluidStack(fluid, 1000))
                .collect(Collectors.toSet());
    }

    public static List<ItemStack> matchedItemStacks(BlockPredicate predicate) {
        if (isAny(predicate)) {
            return List.of();
        }
        return matchedBlocks(predicate).stream()
                .map(Block::asItem)
                .filter(Predicate.not(Items.AIR::equals))
                .distinct()
                .map(Item::getDefaultInstance)
                .toList();
    }

    @Override
    protected void setRecipe(IRecipeLayoutBuilder builder, ExtrudingRecipe recipe, IFocusGroup focuses) {
        int slotIndex = 0;
        int initX = 1;
        int initY = 30;
        int distance = 42;
        for (int index = 0; index < 2; index++) {
            List<ItemStack> itemStacks = matchedItemStacks(recipe.getBlockPredicateIngredients().get(index == 0));
            if (!itemStacks.isEmpty()) {
                builder.addSlot(RecipeIngredientRole.INPUT, initX + distance * slotIndex, initY).setBackground(getRenderedSlot(), -1, -1)
                        .addItemStacks(itemStacks)
                        .addRichTooltipCallback(addConsumeBlockTooltip(recipe.getConsumeBlocks().get(index == 0)));
                slotIndex++;
            }
            Set<FluidStack> fluidIngredients = matchedFluidStacks(recipe.getBlockPredicateIngredients().get(index == 0));
            if (!fluidIngredients.isEmpty()) {
                builder.addSlot(RecipeIngredientRole.INPUT, initX + distance * slotIndex, initY).setBackground(getRenderedSlot(), -1, -1)
                        .addIngredients(NeoForgeTypes.FLUID_STACK, fluidIngredients.stream().toList())
                        .addRichTooltipCallback(addConsumeBlockTooltip(recipe.getConsumeBlocks().get(index == 0)));
                slotIndex++;
            }
        }

        if (recipe.getCatalyst().blocks().isPresent()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 21, 5)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addItemStacks(matchedItemStacks(recipe.getCatalyst()));
        }

        ProcessingOutput output = recipe.getResult();
        builder.addSlot(RecipeIngredientRole.OUTPUT, 44, 67)
                .setBackground(getRenderedSlot(), -1, -1)
                .addRichTooltipCallback(addStochasticTooltip(output))
                .addItemStack(recipe.getResultItemStack());
    }

    private static IRecipeSlotRichTooltipCallback addConsumeBlockTooltip(boolean consume) {
        return (view, tooltip) -> {
            if (consume)
                tooltip.add(ModLang.translate("ui.recipe.extruding.consumes_block").component()
                        .withStyle(ChatFormatting.RED));
        };
    }

    @Override
    protected void draw(ExtrudingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        if (recipe.isAdvanced())
            brassExtruder.draw(graphics, 42, 55);
        else
            extruder.draw(graphics, 42, 55);

        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 42, 50);
        RecipeRequirementRenderer.drawRequirements(recipe, graphics, 63, 4);
    }
}
