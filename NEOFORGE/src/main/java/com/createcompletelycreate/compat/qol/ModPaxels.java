package com.createcompletelycreate.compat.qol;

import com.createcompletelycreate.compat.qol.items.PaxelAbilities;
import com.createcompletelycreate.compat.qol.items.PaxelItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import fr.iglee42.createqualityoflife.registries.QOLTiers;
import fr.iglee42.createqualityoflife.utils.QOLConfigurableItem;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

import static com.createcompletelycreate.CreateCompletelyCreate.REGISTRATE;

public class ModPaxels {

    private record TripledUsesTier(Tier delegate) implements Tier {
        @Override
        public int getUses() {
            return delegate.getUses() * 3;
        }

        @Override
        public float getSpeed() {
            return delegate.getSpeed();
        }

        @Override
        public float getAttackDamageBonus() {
            return delegate.getAttackDamageBonus();
        }

        @Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return delegate.getIncorrectBlocksForDrops();
        }

        @Override
        public int getEnchantmentValue() {
            return delegate.getEnchantmentValue();
        }

        @Override
        public Ingredient getRepairIngredient() {
            return delegate.getRepairIngredient();
        }
    }

    public static final ItemEntry<PaxelItem> SHADOW_STEEL_PAXEL = REGISTRATE.item("shadow_steel_paxel",
                    p -> new PaxelItem(new TripledUsesTier(QOLTiers.SHADOW_STEEL), p, 5.0F, -3.0F,
                            QOLConfigurableItem.ReachType.BOTH, 0.5,
                            List.of(PaxelAbilities.DIGGING, PaxelAbilities.TREE_DECAPITATION)))
            .properties(p -> p.rarity(Rarity.RARE).fireResistant())
            .model((c, p) -> p.handheld(c))
            .tag(ItemTags.PICKAXES, ItemTags.AXES, ItemTags.SHOVELS, ItemTags.CLUSTER_MAX_HARVESTABLES, Tags.Items.MELEE_WEAPON_TOOLS)
            .register();

    public static final ItemEntry<PaxelItem> REFINED_RADIANCE_PAXEL = REGISTRATE.item("refined_radiance_paxel",
                    p -> new PaxelItem(new TripledUsesTier(QOLTiers.REFINED_RADIANCE), p, 5.0F, -3.0F,
                            QOLConfigurableItem.ReachType.BOTH, 0.5,
                            List.of(PaxelAbilities.VEIN_MINE, PaxelAbilities.CASINGIFIER, PaxelAbilities.SMELTING)))
            .properties(p -> p.rarity(Rarity.RARE).fireResistant())
            .model((c, p) -> p.handheld(c))
            .tag(ItemTags.PICKAXES, ItemTags.AXES, ItemTags.SHOVELS, ItemTags.CLUSTER_MAX_HARVESTABLES, Tags.Items.MELEE_WEAPON_TOOLS)
            .register();

    public static final ItemEntry<PaxelItem> SHADOW_RADIANCE_PAXEL = REGISTRATE.item("shadow_radiance_paxel",
                    p -> new PaxelItem(new TripledUsesTier(QOLTiers.SHADOW_RADIANCE), p, 5.0F, -3.0F,
                            QOLConfigurableItem.ReachType.BOTH, 1.0,
                            List.of(PaxelAbilities.DIGGING, PaxelAbilities.VEIN_MINE, PaxelAbilities.CASINGIFIER,
                                    PaxelAbilities.TREE_DECAPITATION, PaxelAbilities.SMELTING)))
            .properties(p -> p.rarity(Rarity.EPIC).fireResistant())
            .model((c, p) -> p.handheld(c))
            .tag(ItemTags.PICKAXES, ItemTags.AXES, ItemTags.SHOVELS, ItemTags.CLUSTER_MAX_HARVESTABLES, Tags.Items.MELEE_WEAPON_TOOLS)
            .register();

    public static void register() {
    }
}
