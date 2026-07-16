package com.createcompletelycreate.register;

import com.createcompletelycreate.ModConstants;
import com.createcompletelycreate.components.extruder.andesite.ExtruderBlock;
import com.createcompletelycreate.components.extruder.brass.BrassExtruderBlock;
import com.createcompletelycreate.infrastructure.config.ModStress;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeBuilder;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.Tags;

import static com.createcompletelycreate.CreateCompletelyCreate.REGISTRATE;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class ModBlocks {
    public static final BlockEntry<ExtruderBlock> BLOCK_EXPELLER = REGISTRATE.block("block_expeller", ExtruderBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.METAL).noOcclusion())
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .transform(ModStress.setImpact(4.0))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get())
                    .define('S', AllBlocks.SHAFT)
                    .define('A', AllBlocks.ANDESITE_CASING)
                    .define('G', Tags.Items.GLASS_BLOCKS)
                    .pattern(" S ")
                    .pattern("GAG")
                    .pattern(" G ")
                    .unlockedBy("has_andesite_casing", RegistrateRecipeProvider.has(AllTags.AllItemTags.CASING.tag))
                    .save(p, ModConstants.asResource("crafting/" + c.getName())))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<BrassExtruderBlock> BRASS_BLOCK_EXPELLER = REGISTRATE.block("brass_block_expeller", BrassExtruderBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.METAL).noOcclusion())
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .transform(ModStress.setImpact(16.0))
            .recipe((ctx, provider) ->
                    MechanicalCraftingRecipeBuilder.shapedRecipe(ctx.get())
                            .key('G', Ingredient.of(AllBlocks.METAL_GIRDER))
                            .key('T', Ingredient.of(AllBlocks.FRAMED_GLASS_TRAPDOOR))
                            .key('C', Ingredient.of(AllBlocks.BRASS_CASING))
                            .key('P', Ingredient.of(net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM, net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("c", "plates/brass"))))
                            .patternLine(" G ")
                            .patternLine("PGP")
                            .patternLine("TCT")
                            .patternLine("PTP")
                            .build(provider)
                    )
            .item()
            .transform(customItemModel())

            .register();

    public static void register() {}

}
