package com.createcompletelycreate.foundation.register;

import com.createcompletelycreate.components.extruder.recipe.requirements.AdvancedExtruderRecipeRequirement;
import com.createcompletelycreate.components.extruder.recipe.requirements.BonksRecipeRequirement;
import com.createcompletelycreate.foundation.recipe.RecipeRequirementType;
import com.createcompletelycreate.foundation.recipe.requirements.BiomeTagRequirement;
import com.createcompletelycreate.foundation.recipe.requirements.MaxSpeedRequirement;
import com.createcompletelycreate.foundation.recipe.requirements.MaxYRequirement;
import com.createcompletelycreate.foundation.recipe.requirements.MinSpeedRequirement;
import com.createcompletelycreate.foundation.recipe.requirements.MinYRequirement;

public class ModRecipeRequirementTypes {
    public static final RecipeRequirementType<MinYRequirement> MIN_Y =
            new RecipeRequirementType<>("min_y", MinYRequirement.MAP_CODEC, MinYRequirement.STREAM_CODEC);
    public static final RecipeRequirementType<MaxYRequirement> MAX_Y =
            new RecipeRequirementType<>("max_y", MaxYRequirement.MAP_CODEC, MaxYRequirement.STREAM_CODEC);
    public static final RecipeRequirementType<MaxSpeedRequirement> MAX_SPEED =
            new RecipeRequirementType<>("max_speed", MaxSpeedRequirement.MAP_CODEC, MaxSpeedRequirement.STREAM_CODEC);
    public static final RecipeRequirementType<MinSpeedRequirement> MIN_SPEED =
            new RecipeRequirementType<>("min_speed", MinSpeedRequirement.MAP_CODEC, MinSpeedRequirement.STREAM_CODEC);
    public static final RecipeRequirementType<BiomeTagRequirement> BIOME_TAG =
            new RecipeRequirementType<>("biome_tag", BiomeTagRequirement.MAP_CODEC, BiomeTagRequirement.STREAM_CODEC);
    public static final RecipeRequirementType<AdvancedExtruderRecipeRequirement> ADVANCED_EXTRUDER =
            new RecipeRequirementType<>("advanced_extruder", AdvancedExtruderRecipeRequirement.MAP_CODEC, AdvancedExtruderRecipeRequirement.STREAM_CODEC);
    public static final RecipeRequirementType<BonksRecipeRequirement> BONKS =
            new RecipeRequirementType<>("bonks", BonksRecipeRequirement.MAP_CODEC, BonksRecipeRequirement.STREAM_CODEC);

    public static void init() {
        // force static init
    }
}
