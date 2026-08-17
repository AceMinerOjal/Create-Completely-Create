package com.createcompletelycreate.compat.qol;

import com.createcompletelycreate.compat.qol.items.PaxelAbilities;
import com.createcompletelycreate.compat.qol.items.PaxelItem;
import com.simibubi.create.content.kinetics.saw.SawBlockEntity;
import com.simibubi.create.content.kinetics.saw.TreeCutter;
import com.simibubi.create.foundation.utility.AbstractBlockBreakQueue;
import com.simibubi.create.foundation.utility.BlockHelper;
import fr.iglee42.createqualityoflife.config.CreateQOLConfigs;
import fr.iglee42.createqualityoflife.registries.QOLDataComponents;
import fr.iglee42.createqualityoflife.utils.DestroyUtils;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

public class PaxelAbilityEvents {

    public static void register() {
        NeoForge.EVENT_BUS.addListener(BlockEvent.BreakEvent.class, PaxelAbilityEvents::onBreak);
        NeoForge.EVENT_BUS.addListener(BlockDropsEvent.class, PaxelAbilityEvents::onBlockDrops);
    }

    private static void onBreak(BlockEvent.BreakEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (event.isCanceled()) return;
        ItemStack main = event.getPlayer().getMainHandItem();
        if (!(main.getItem() instanceof PaxelItem paxel)) return;
        if (!main.isCorrectToolForDrops(event.getState())) return;

        if (paxel.hasAbility(PaxelAbilities.TREE_DECAPITATION) && main.getOrDefault(QOLDataComponents.TREE_DECAPITATION, false)) {
            treeDecapitation(event);
        }
        if (paxel.hasAbility(PaxelAbilities.DIGGING) && main.getOrDefault(QOLDataComponents.DIGGING, false)) {
            digging(event);
        }
        if (paxel.hasAbility(PaxelAbilities.VEIN_MINE) && main.getOrDefault(QOLDataComponents.VEIN_MINE, false)) {
            veinMine(event);
        }
    }

    private static void veinMine(BlockEvent.BreakEvent event) {
        int max = CreateQOLConfigs.server().equipments.tools.veinMineMaxBlocks.get();
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> toExplore = new ArrayDeque<>();

        List<BlockPos> first = getValidBlocksNextTo(event.getLevel(), event.getPos(), event.getPlayer().getMainHandItem());
        for (BlockPos pos : first) {
            if (event.getLevel().getBlockState(pos).is(event.getState().getBlock())) {
                visited.add(pos);
                toExplore.add(pos);
                if (visited.size() >= max) break;
            }
        }

        while (!toExplore.isEmpty() && visited.size() < max) {
            BlockPos current = toExplore.poll();
            List<BlockPos> neighbors = getValidBlocksNextTo(event.getLevel(), current, event.getPlayer().getMainHandItem());
            for (BlockPos neighbor : neighbors) {
                if (visited.size() >= max) break;
                if (!visited.contains(neighbor) && event.getLevel().getBlockState(neighbor).is(event.getState().getBlock())) {
                    visited.add(neighbor);
                    toExplore.add(neighbor);
                }
            }
        }

        ServerPlayer player = (ServerPlayer) event.getPlayer();
        visited.forEach(p -> DestroyUtils.destroyBlock(player.level(), player.gameMode.getGameModeForPlayer(), player, player.gameMode, p, event.getPos()));
    }

    private static List<BlockPos> getValidBlocksNextTo(LevelAccessor level, BlockPos pos, ItemStack stack) {
        List<BlockPos> poses = new ArrayList<>();
        for (Direction direction : Iterate.directions) {
            if (stack.isCorrectToolForDrops(level.getBlockState(pos.relative(direction)))) {
                poses.add(pos.relative(direction));
            }
        }
        return poses;
    }

    private static void digging(BlockEvent.BreakEvent event) {
        BlockPos startPos = event.getPos();
        Direction dir = getTargetedBlockFace((ServerPlayer) event.getPlayer());
        if (dir == null) return;
        ItemStack tool = event.getPlayer().getMainHandItem();
        for (int y = 0; y <= 2; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos offsetPos = startPos.relative(dir.getOpposite(), y);
                    if (dir.getAxis().equals(Direction.Axis.Y)) {
                        offsetPos = offsetPos.offset(x, 0, z);
                    } else if (dir.getAxis().equals(Direction.Axis.Z)) {
                        offsetPos = offsetPos.offset(x, z, 0);
                    } else if (dir.getAxis().equals(Direction.Axis.X)) {
                        offsetPos = offsetPos.offset(0, x, z);
                    }
                    BlockState cState = event.getLevel().getBlockState(offsetPos);
                    if (tool.isCorrectToolForDrops(cState)) {
                        ServerPlayer player = (ServerPlayer) event.getPlayer();
                        DestroyUtils.destroyBlock(player.level(), player.gameMode.getGameModeForPlayer(), player, player.gameMode, offsetPos, event.getPos());
                    }
                }
            }
        }
    }

    private static Direction getTargetedBlockFace(ServerPlayer player) {
        Level level = player.level();
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 lookVec = player.getViewVector(1.0F);
        double reach = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);
        Vec3 reachVec = eyePos.add(lookVec.scale(reach));
        ClipContext context = new ClipContext(eyePos, reachVec, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player);
        BlockHitResult hitResult = level.clip(context);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return hitResult.getDirection();
        }
        return null;
    }

    private static void treeDecapitation(BlockEvent.BreakEvent event) {
        if (!SawBlockEntity.isSawable(event.getState())) return;
        BlockState stateToBreak = event.getState();
        BlockPos breakingPos = event.getPos();
        Level level = event.getPlayer().level();
        Optional<AbstractBlockBreakQueue> dynamicTree = TreeCutter.findDynamicTree(stateToBreak.getBlock(), breakingPos);
        if (dynamicTree.isPresent()) {
            dynamicTree.get().destroyBlocks(level, null, (pos, stack) -> dropItemFromCutTree(level, pos, stack));
            return;
        }

        Vec3 vec = VecHelper.offsetRandomly(VecHelper.getCenterOf(breakingPos), level.random, .125f);
        BlockHelper.destroyBlock(level, breakingPos, 1f, (stack) -> {
            if (stack.isEmpty()) return;
            if (!level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) return;
            if (level.restoringBlockSnapshots) return;
            ItemEntity itementity = new ItemEntity(level, vec.x, vec.y, vec.z, stack);
            itementity.setDefaultPickUpDelay();
            itementity.setDeltaMovement(Vec3.ZERO);
            level.addFreshEntity(itementity);
        });
        TreeCutter.findTree(level, breakingPos, stateToBreak)
                .destroyBlocks(level, null, (pos, stack) -> dropItemFromCutTree(level, pos, stack));
    }

    private static void dropItemFromCutTree(Level level, BlockPos pos, ItemStack stack) {
        Vec3 dropPos = VecHelper.getCenterOf(pos);
        ItemEntity entity = new ItemEntity(level, dropPos.x, dropPos.y, dropPos.z, stack);
        entity.setDeltaMovement(Vec3.ZERO);
        level.addFreshEntity(entity);
    }

    private static void onBlockDrops(BlockDropsEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (event.isCanceled()) return;
        ItemStack tool = event.getTool();
        if (!(tool.getItem() instanceof PaxelItem paxel)) return;
        if (!paxel.hasAbility(PaxelAbilities.SMELTING)) return;
        if (!tool.getOrDefault(QOLDataComponents.SMELTING, false)) return;
        if (!tool.isCorrectToolForDrops(event.getState())) return;
        if (!CreateQOLConfigs.server().equipments.tools.smelting.get()) return;

        for (ItemEntity ie : event.getDrops()) {
            ItemStack stack = ie.getItem();
            Optional<net.minecraft.world.item.crafting.RecipeHolder<net.minecraft.world.item.crafting.SmeltingRecipe>> optionalRecipe =
                    event.getLevel().getRecipeManager().getAllRecipesFor(net.minecraft.world.item.crafting.RecipeType.SMELTING).stream()
                            .filter(r -> r.value().matches(new net.minecraft.world.item.crafting.SingleRecipeInput(stack), event.getLevel()))
                            .findFirst();
            if (optionalRecipe.isEmpty()) continue;
            ItemStack result = optionalRecipe.get().value().assemble(new net.minecraft.world.item.crafting.SingleRecipeInput(stack), event.getLevel().registryAccess());
            ie.setItem(result.copyWithCount(result.getCount() * stack.getCount()));
            int amount = Mth.floor(optionalRecipe.get().value().getExperience() * stack.getCount());
            float fraction = Mth.frac(optionalRecipe.get().value().getExperience() * stack.getCount());
            if (fraction != 0.0F && Math.random() < (double) fraction) {
                amount++;
            }
            event.setDroppedExperience(event.getDroppedExperience() + amount);
        }
    }
}
