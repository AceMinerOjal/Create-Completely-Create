package com.createcompletelycreate.compat.qol.items;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.content.kinetics.deployer.ManualApplicationRecipe;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import fr.iglee42.createqualityoflife.config.CreateQOLConfigs;
import fr.iglee42.createqualityoflife.registries.QOLDataComponents;
import fr.iglee42.createqualityoflife.utils.ItemTooltips;
import fr.iglee42.createqualityoflife.utils.QOLConfigurableItem;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PaxelItem extends TieredItem implements QOLConfigurableItem {

    public record Ability(
            net.minecraft.core.component.DataComponentType<Boolean> component,
            String langKey,
            String label,
            List<String> description,
            Supplier<Boolean> configEnabled
    ) {
    }

    private final QOLConfigurableItem.ReachType reachType;
    private final double reachModifier;
    private final List<Ability> togglableAbilities;

    public PaxelItem(Tier tier, Item.Properties properties, float attackDamage, float attackSpeed,
                     QOLConfigurableItem.ReachType reachType, double reachModifier, List<Ability> togglableAbilities) {
        super(tier, properties
                .component(DataComponents.TOOL, createPaxelTool(tier))
                .attributes(DiggerItem.createAttributes(tier, attackDamage, attackSpeed))
                .component(QOLDataComponents.ITEM_TOOLTIPS, ItemTooltips.DEFAULT));
        this.reachType = reachType;
        this.reachModifier = reachModifier;
        this.togglableAbilities = togglableAbilities;
    }

    private static Tool createPaxelTool(Tier tier) {
        return new Tool(List.of(
                Tool.Rule.deniesDrops(tier.getIncorrectBlocksForDrops()),
                Tool.Rule.minesAndDrops(BlockTags.MINEABLE_WITH_PICKAXE, tier.getSpeed()),
                Tool.Rule.minesAndDrops(BlockTags.MINEABLE_WITH_AXE, tier.getSpeed()),
                Tool.Rule.minesAndDrops(BlockTags.MINEABLE_WITH_SHOVEL, tier.getSpeed())
        ), 1.0F, 1);
    }

    public List<Ability> togglableAbilities() {
        return togglableAbilities;
    }

    public boolean hasAbility(Ability ability) {
        return togglableAbilities.contains(ability);
    }

    @Override
    public QOLConfigurableItem.Type type() {
        return Type.ITEM;
    }

    @Override
    public QOLConfigurableItem.ReachType reachType(ItemStack stack) {
        return reachType;
    }

    @Override
    public double reachModifier(ItemStack stack) {
        return reachModifier;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        invTick(stack, level, entity, slot, selected);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<Item> onBroken) {
        if (CreateQOLConfigs.server().equipments.useAir.get()
                && BacktankUtil.canAbsorbDamage(entity, amount)) {
            return 0;
        }
        return super.damageItem(stack, amount, entity, onBroken);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        if (BacktankUtil.isBarVisible(stack, stack.getMaxDamage())
                && CreateQOLConfigs.server().equipments.useAir.get()) {
            return true;
        }
        return super.isBarVisible(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if (CreateQOLConfigs.server().equipments.useAir.get()) {
            return BacktankUtil.getBarWidth(stack, stack.getMaxDamage());
        }
        return super.getBarWidth(stack);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        if (CreateQOLConfigs.server().equipments.useAir.get()) {
            return BacktankUtil.getBarColor(stack, stack.getMaxDamage());
        }
        return super.getBarColor(stack);
    }

    @Override
    public void addConfigurations(List<QOLConfigurableItem.Configuration<?>> list, ItemStack stack) {
        for (Ability ability : togglableAbilities) {
            list.add(QOLConfigurableItem.Configuration.ofBool(ability.label,
                    stack.getOrDefault(ability.component, false), ability.component, ability.description,
                    (e, oe) -> ability.configEnabled.get()));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        if (!stack.getOrDefault(QOLDataComponents.ITEM_TOOLTIPS, ItemTooltips.DEFAULT).isEnable(ItemTooltips.Tooltip.OPTIONS)) return;
        components.add(Component.translatable("createqol.ability.tool.toggle_message", Component.translatable("createqol.ability.tool.reach").getString())
                .withStyle(ChatFormatting.GOLD)
                .append(QOLConfigurableItem.chooseState(CreateQOLConfigs.server().equipments.tools.reach.get(), true, stack.getOrDefault(QOLDataComponents.REACH, true), false, true)));
        for (Ability ability : togglableAbilities) {
            components.add(Component.translatable("createqol.ability.tool.toggle_message", Component.translatable(ability.langKey).getString())
                    .withStyle(ChatFormatting.GOLD)
                    .append(QOLConfigurableItem.chooseState(ability.configEnabled.get(), true, stack.getOrDefault(ability.component, false), false, true)));
        }
        super.appendHoverText(stack, context, components, flag);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) return InteractionResultHolder.sidedSuccess(stack, true);
        if (player.isCrouching()) {
            cycleSelection(stack, player);
        } else {
            toggleSelected(stack, player);
        }
        return InteractionResultHolder.success(stack);
    }

    private int selectedAbilityIndex(ItemStack stack) {
        int idx = stack.getOrDefault(com.createcompletelycreate.register.ModDataComponents.PAXEL_ACTIVE_TOGGLE, (byte) 0) & 0xFF;
        return idx < togglableAbilities.size() ? idx : 0;
    }

    private void cycleSelection(ItemStack stack, Player player) {
        if (togglableAbilities.isEmpty()) return;
        int idx = selectedAbilityIndex(stack);
        idx = (idx + 1) % togglableAbilities.size();
        stack.set(com.createcompletelycreate.register.ModDataComponents.PAXEL_ACTIVE_TOGGLE, (byte) idx);
        Ability ability = togglableAbilities.get(idx);
        player.displayClientMessage(Component.translatable("createcompletelycreate.paxel.selected", Component.translatable(ability.langKey)).withStyle(ChatFormatting.AQUA), true);
    }

    private void toggleSelected(ItemStack stack, Player player) {
        if (togglableAbilities.isEmpty()) return;
        int idx = selectedAbilityIndex(stack);
        Ability ability = togglableAbilities.get(idx);
        if (!ability.configEnabled.get()) {
            player.displayClientMessage(Component.translatable("createqol.ability.tool.disabled", Component.translatable(ability.langKey).getString()).withStyle(ChatFormatting.RED), true);
            return;
        }
        boolean enable = !stack.getOrDefault(ability.component, false);
        stack.set(ability.component, enable);
        if (enable) {
            enforceMutualExclusion(stack, ability);
        }
        player.displayClientMessage(Component.translatable("createqol.ability.tool.toggle_message", Component.translatable(ability.langKey).getString())
                .append(QOLConfigurableItem.chooseState(true, true, enable, false, true))
                .withStyle(enable ? ChatFormatting.GREEN : ChatFormatting.RED), true);
    }

    private void enforceMutualExclusion(ItemStack stack, Ability toggled) {
        if (toggled.equals(PaxelAbilities.DIGGING)) {
            if (hasAbility(PaxelAbilities.VEIN_MINE)) stack.set(PaxelAbilities.VEIN_MINE.component, false);
        } else if (toggled.equals(PaxelAbilities.VEIN_MINE)) {
            if (hasAbility(PaxelAbilities.DIGGING)) stack.set(PaxelAbilities.DIGGING.component, false);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        ItemStack tool = ctx.getItemInHand();
        if (hasAbility(PaxelAbilities.CASINGIFIER) && tool.getOrDefault(QOLDataComponents.CASINGIFIER, false)
                && CreateQOLConfigs.server().equipments.tools.casingifier.get()) {
            return casingify(ctx);
        }
        return vanillaToolUseOn(ctx);
    }

    private InteractionResult vanillaToolUseOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        if (playerHasShieldUseIntent(ctx)) return InteractionResult.PASS;
        BlockPos pos = ctx.getClickedPos();
        BlockState state = level.getBlockState(pos);

        BlockState stripped = state.getToolModifiedState(ctx, ItemAbilities.AXE_STRIP, false);
        if (stripped != null) {
            return applyModifiedState(ctx, stripped, SoundEvents.AXE_STRIP);
        }
        if (ctx.getClickedFace() != Direction.DOWN) {
            BlockState flat = state.getToolModifiedState(ctx, ItemAbilities.SHOVEL_FLATTEN, false);
            if (flat != null && level.getBlockState(pos.above()).isAir()) {
                return applyModifiedState(ctx, flat, SoundEvents.SHOVEL_FLATTEN);
            }
            BlockState doused = state.getToolModifiedState(ctx, ItemAbilities.SHOVEL_DOUSE, false);
            if (doused != null) {
                if (!level.isClientSide) level.levelEvent(null, 1009, pos, 0);
                level.setBlock(pos, doused, 11);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, doused));
                if (player != null) ctx.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(ctx.getHand()));
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    private InteractionResult applyModifiedState(UseOnContext ctx, BlockState newState, SoundEvent sound) {
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        BlockPos pos = ctx.getClickedPos();
        level.playSound(player, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.setBlock(pos, newState, 11);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));
        if (player != null) ctx.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(ctx.getHand()));
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static boolean playerHasShieldUseIntent(UseOnContext ctx) {
        Player player = ctx.getPlayer();
        return ctx.getHand().equals(InteractionHand.MAIN_HAND) && player != null
                && player.getOffhandItem().is(Items.SHIELD) && !player.isSecondaryUseActive();
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility ability) {
        return ItemAbilities.DEFAULT_AXE_ACTIONS.contains(ability)
                || ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(ability)
                || ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(ability);
    }

    private InteractionResult casingify(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos origin = ctx.getClickedPos();
        Player player = ctx.getPlayer();
        if (player == null) return InteractionResult.PASS;
        if (playerHasShieldUseIntent(ctx)) return InteractionResult.PASS;

        ItemStack tool = ctx.getItemInHand();
        ItemStack offHandStack = player.getOffhandItem();
        int limit = CreateQOLConfigs.server().equipments.tools.casingifierMaxBlocks.get();

        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> toVisit = new ArrayDeque<>();
        toVisit.add(origin);

        int count = 0;

        while (!toVisit.isEmpty() && count < limit) {
            BlockPos pos = toVisit.poll();
            if (!visited.add(pos)) continue;
            boolean success = transformBlock(level, pos, player, ctx, offHandStack, (bs, recipe) -> {
                level.setBlock(pos, bs, 3);
                recipe.rollResults(ctx.getLevel().random).forEach(stack -> Block.popResource(level, pos, stack));

                boolean creative = player.isCreative();
                boolean unbreakable = offHandStack.has(DataComponents.UNBREAKABLE);
                boolean keepHeld = recipe.shouldKeepHeldItem() || creative;

                if (player instanceof ServerPlayer sp) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(sp, pos, tool);
                }

                tool.hurtAndBreak(1, player, LivingEntity.getSlotForHand(ctx.getHand()));

                if (!unbreakable && !keepHeld) {
                    consumeItem(player, offHandStack);
                }

                awardAdvancements(player, bs);
            });

            if (success) {
                count++;
                for (Direction dir : Direction.values()) {
                    BlockPos neighbor = pos.relative(dir);
                    if (!visited.contains(neighbor)) toVisit.add(neighbor);
                }
            }
        }

        return count > 0 ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
    }

    private void consumeItem(Player player, ItemStack reference) {
        if (reference.isEmpty()) return;
        if (reference.isDamageableItem()) {
            reference.hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
        } else {
            player.getOffhandItem().shrink(1);
        }
    }

    private boolean transformBlock(Level level, BlockPos blockpos, Player player, UseOnContext ctx, ItemStack offHandStack,
                                   BiConsumer<BlockState, ManualApplicationRecipe> onSuccess) {
        Optional<BlockState> optional = evaluateNewBlockState(level, blockpos, player, level.getBlockState(blockpos), ctx);
        if (optional.isEmpty()) return false;

        RecipeType<Recipe<RecipeWrapper>> type = AllRecipeTypes.ITEM_APPLICATION.getType();

        Optional<RecipeHolder<Recipe<RecipeWrapper>>> foundRecipe = level.getRecipeManager()
                .getAllRecipesFor(type)
                .stream()
                .filter(r -> {
                    ManualApplicationRecipe mar = (ManualApplicationRecipe) r.value();
                    return mar.testBlock(optional.get()) && mar.getIngredients().get(1).test(offHandStack);
                })
                .findFirst();

        if (foundRecipe.isEmpty()) return false;

        ManualApplicationRecipe recipe = (ManualApplicationRecipe) foundRecipe.get().value();
        level.destroyBlock(blockpos, false);

        BlockState transformedBlock = recipe.transformBlock(optional.get(), ctx.getLevel().random);
        onSuccess.accept(transformedBlock, recipe);

        return true;
    }

    private Optional<BlockState> evaluateNewBlockState(Level level, BlockPos pos, @Nullable Player player, BlockState state, UseOnContext ctx) {
        BlockState axe = state.getToolModifiedState(ctx, ItemAbilities.AXE_STRIP, false);
        if (axe != null) return Optional.of(axe);
        BlockState shovel = state.getToolModifiedState(ctx, ItemAbilities.SHOVEL_FLATTEN, false);
        if (shovel != null) return Optional.of(shovel);
        return Optional.of(state);
    }

    private static void awardAdvancements(Player player, BlockState placed) {
        CreateAdvancement advancement = null;
        if (AllBlocks.ANDESITE_CASING.has(placed)) advancement = AllAdvancements.ANDESITE_CASING;
        else if (AllBlocks.BRASS_CASING.has(placed)) advancement = AllAdvancements.BRASS_CASING;
        else if (AllBlocks.COPPER_CASING.has(placed)) advancement = AllAdvancements.COPPER_CASING;
        else if (AllBlocks.RAILWAY_CASING.has(placed)) advancement = AllAdvancements.TRAIN_CASING;
        else return;
        advancement.awardTo(player);
    }
}
