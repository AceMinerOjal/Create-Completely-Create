package com.createcompletelycreate.compat.jei.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.createcompletelycreate.components.extruder.AbstractExtruderBlock;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;

public abstract class AbstractAnimatedExtruder<E extends AbstractExtruderBlock<?>> extends AnimatedKinetics {
    public int offset = 5;
    private final BlockEntry<E> block;
    private final PartialModel polePartial;
    private final Float poleOffset;

    BlockEntry<E> getBlock() {
        return block;
    }

    PartialModel getPolePartial() {
        return polePartial;
    }

    Float getPoleOffset() {
        return poleOffset;
    }

    protected AbstractAnimatedExtruder(BlockEntry<E> block, PartialModel polePartial, Float poleOffset) {
        this.block = block;
        this.polePartial = polePartial;
        this.poleOffset = poleOffset;
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        PoseStack matrixStack = guiGraphics.pose();

        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0f));
        int scale = 24;

        blockElement(shaft(Direction.Axis.Z))
                .rotateBlock(0, 0, getCurrentAngle())
                .scale(scale)
                .render(guiGraphics);

        blockElement(getBlock().getDefaultState())
                .scale(scale)
                .render(guiGraphics);

        blockElement(getPolePartial())
                .atLocal(0, -getAnimatedHeadOffset() - getPoleOffset(), 0)
                .scale(scale)
                .render(guiGraphics);

        matrixStack.popPose();
    }
    private float getAnimatedHeadOffset() {
        float cycle = (AnimationTickHolder.getRenderTime() - offset * 8) % 30;
        if (cycle < 10) {
            float progress = cycle / 10;
            return -(progress * progress * progress);
        }
        if (cycle < 15)
            return -1;
        if (cycle < 20)
            return -1 + (1 - ((20 - cycle) / 5));
        return 0;
    }

}
