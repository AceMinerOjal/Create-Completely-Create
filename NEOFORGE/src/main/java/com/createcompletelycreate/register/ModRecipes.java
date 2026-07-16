package com.createcompletelycreate.register;

import com.createcompletelycreate.ModConstants;
import com.createcompletelycreate.components.extruder.AbstractExtruderBlockEntity;
import com.createcompletelycreate.components.extruder.recipe.ExtrudingRecipe;
import com.createcompletelycreate.components.extruder.recipe.ExtrudingRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ModConstants.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, ModConstants.MODID);


    public static final Supplier<RecipeType<ExtrudingRecipe>> EXTRUDING_TYPE =
            RECIPE_TYPES.register(
                    "extruding",
                    () -> RecipeType.<ExtrudingRecipe>simple(ModConstants.asResource("extruding"))

        );

    public static final Supplier<ExtrudingRecipeSerializer> EXTRUDING_SERIALIZER =
            SERIALIZERS.register("extruding", () -> ExtrudingRecipeSerializer.INSTANCE);

    public static void register(IEventBus eventBus) {

        SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);

    }


    public static <EXB extends AbstractExtruderBlockEntity> List<ExtrudingRecipe> findRecipesWithMatchingIngredients(EXB extruder){
        if(extruder.getLevel() == null || extruder.getLevel().isClientSide())
            return List.of();

        return extruder.getLevel().getRecipeManager().getAllRecipesFor(ModRecipes.EXTRUDING_TYPE.get())
                .stream()
                    .filter(extruder::matchesIngredients)
                    .map(RecipeHolder::value)
                    .toList();

    }

}

