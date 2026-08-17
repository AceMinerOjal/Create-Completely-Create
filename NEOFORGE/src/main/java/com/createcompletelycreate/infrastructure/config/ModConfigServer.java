package com.createcompletelycreate.infrastructure.config;

import com.createcompletelycreate.components.extruder.ExtruderConfigs;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModConfigServer {
    public final ExtruderConfigs mechanicalExtruder;

    private final Map<String, BooleanValue> recipeToggles = new LinkedHashMap<>();
    private final Map<String, BooleanValue> extrudingRecipeToggles = new LinkedHashMap<>();

    public ModConfigServer(ModConfigSpec.Builder builder) {
        mechanicalExtruder = new ExtruderConfigs(builder);

        // ─── Recipe Categories ─────────────────────────────────────
        // Each category and sub-category can be independently toggled.
        // Disabled categories are removed from the recipe registry.
        builder.comment(
                "Recipe categories added by this mod.",
                "Each category can be independently enabled or disabled.",
                "Disabled categories are removed from the recipe registry."
        ).push("recipes");

        // Compacting
        registerToggle(builder, "compacting");
        registerToggle(builder, "compacting_slabs");
        registerToggle(builder, "compacting_stairs");

        // Crafting
        registerToggle(builder, "crafting");

        // Crushing
        registerToggle(builder, "crushing");

        // Cutting
        registerToggle(builder, "cutting");
        registerToggle(builder, "cutting_woods");

        // Extruding
        registerToggle(builder, "extruding");

        // Haunting
        registerToggle(builder, "haunting");

        // Mechanical Crafting
        registerToggle(builder, "mechanical_crafting");
        registerToggle(builder, "mechanical_crafting_paxels");
        registerToggle(builder, "mechanical_crafting_stairs");
        registerToggle(builder, "mechanical_crafting_trapdoors");

        // Milling
        registerToggle(builder, "milling");

        // Mixing
        registerToggle(builder, "mixing");

        // Pressing
        registerToggle(builder, "pressing");

        // Sequenced Assembly
        registerToggle(builder, "sequenced_assembly");

        // Splashing
        registerToggle(builder, "splashing");

        // Spout Water
        registerToggle(builder, "spout_water");
        registerToggle(builder, "spout_water_create");
        registerToggle(builder, "copper_oxidation");

        builder.pop();

        // ─── Individual Extruding Recipes ──────────────────────────
        // Per-recipe toggles. Only apply while 'recipes.extruding' is enabled.
        builder.comment(
                "Individual extruding recipe toggles.",
                "These only apply while the 'recipes.extruding' category is enabled.",
                "Disabled recipes are removed from the recipe registry."
        ).push("extruding");

        registerExtrudingToggle(builder, "cobblestone");
        registerExtrudingToggle(builder, "stone");
        registerExtrudingToggle(builder, "limestone");
        registerExtrudingToggle(builder, "scoria");
        registerExtrudingToggle(builder, "obsidian");
        registerExtrudingToggle(builder, "snow_block");
        registerExtrudingToggle(builder, "basalt");
        registerExtrudingToggle(builder, "calcite");

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

    private void registerToggle(ModConfigSpec.Builder builder, String category) {
        recipeToggles.put(category, builder
                .comment("Enable the '" + category + "' recipe category.")
                .define(category, true));
    }

    private void registerExtrudingToggle(ModConfigSpec.Builder builder, String recipeId) {
        extrudingRecipeToggles.put(recipeId, builder
                .comment("Enable the '" + recipeId + "' extruding recipe.")
                .define(recipeId, true));
    }
}
