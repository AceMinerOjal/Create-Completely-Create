package com.createcompletelycreate.infrastructure.config;

import com.createcompletelycreate.components.extruder.ExtruderConfigs;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModConfigServer {
    public final ExtruderConfigs mechanicalExtruder;
    public final ModStress stressValues;

    private final Map<String, BooleanValue> recipeToggles = new LinkedHashMap<>();
    private final Map<String, BooleanValue> extrudingRecipeToggles = new LinkedHashMap<>();

    public ModConfigServer(ModConfigSpec.Builder builder) {
        mechanicalExtruder = new ExtruderConfigs(builder);
        stressValues = new ModStress(builder);

        builder.comment(
                        "Whether each recipe category added by this mod is enabled.",
                        "Disabled categories are removed from the recipe registry and can no longer be crafted."
                )
                .push("recipes");

        registerRecipeToggle(builder, "compacting");
        registerRecipeToggle(builder, "crafting");
        registerRecipeToggle(builder, "crushing");
        registerRecipeToggle(builder, "cutting");
        registerRecipeToggle(builder, "extruding");
        registerRecipeToggle(builder, "haunting");
        registerRecipeToggle(builder, "mechanical_crafting");
        registerRecipeToggle(builder, "milling");
        registerRecipeToggle(builder, "mixing");
        registerRecipeToggle(builder, "pressing");
        registerRecipeToggle(builder, "splashing");
        registerRecipeToggle(builder, "spout-water");

        builder.pop();

        builder.comment(
                        "Whether each individual extruding recipe is enabled.",
                        "These only apply while the 'recipes.extruding' category toggle is enabled.",
                        "Disabled recipes are removed from the recipe registry and can no longer be extruded."
                )
                .push("extruding");

        registerExtrudingRecipeToggle(builder, "cobblestone");
        registerExtrudingRecipeToggle(builder, "stone");
        registerExtrudingRecipeToggle(builder, "limestone");
        registerExtrudingRecipeToggle(builder, "scoria");
        registerExtrudingRecipeToggle(builder, "obsidian");
        registerExtrudingRecipeToggle(builder, "snow_block");
        registerExtrudingRecipeToggle(builder, "basalt");
        registerExtrudingRecipeToggle(builder, "calcite");

        builder.pop();
    }

    public boolean recipeEnabled(String category) {
        BooleanValue toggle = recipeToggles.get(category);
        return toggle == null || toggle.get();
    }

    public boolean extrudingRecipeEnabled(String recipeId) {
        BooleanValue toggle = extrudingRecipeToggles.get(recipeId);
        return toggle == null || toggle.get();
    }

    private void registerRecipeToggle(ModConfigSpec.Builder builder, String category) {
        recipeToggles.put(category, builder.comment("Enable the '" + category + "' recipe category.")
                .define(category, true));
    }

    private void registerExtrudingRecipeToggle(ModConfigSpec.Builder builder, String recipeId) {
        extrudingRecipeToggles.put(recipeId, builder.comment("Enable the '" + recipeId + "' extruding recipe.")
                .define(recipeId, true));
    }
}
