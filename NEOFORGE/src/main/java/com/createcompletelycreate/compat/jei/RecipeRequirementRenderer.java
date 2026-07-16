package com.createcompletelycreate.compat.jei;

import com.createcompletelycreate.components.extruder.recipe.ExtrudingRecipe;
import com.createcompletelycreate.components.extruder.recipe.requirements.BonksRecipeRequirement;
import com.createcompletelycreate.foundation.recipe.IRecipeRequirement;
import com.createcompletelycreate.ModLang;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public class RecipeRequirementRenderer {

    public static void drawRequirements(ExtrudingRecipe recipe, GuiGraphics graphics, int x, int y) {
        List<IRecipeRequirement> requirements = recipe.getJeiRecipeRequirements();
        var pose = graphics.pose();
        pose.pushPose();
        pose.scale(0.75f, 0.75f, 1.0f);
        int offsetY = 0;
        for (IRecipeRequirement requirement : requirements) {
            Component text;
            if (requirement instanceof BonksRecipeRequirement bonks) {
                text = ModLang.translate("ui.recipe_requirement.bonks.tooltip.title")
                        .add(Component.literal(" ").append(Component.literal(String.valueOf(bonks.bonks())).withStyle(ChatFormatting.AQUA)))
                        .component()
                        .withStyle(ChatFormatting.BLACK);
            } else {
                String key = "ui.recipe_requirement." + requirement.getType().getId() + ".tooltip.title";
                text = ModLang.translate(key).component().withStyle(ChatFormatting.BLACK);
            }
            graphics.drawString(Minecraft.getInstance().font,
                    text, (int)(x / 0.75f), (int)((y + offsetY) / 0.75f), 0xFF000000, false);
            offsetY += 10;
        }
        pose.popPose();
    }
}
