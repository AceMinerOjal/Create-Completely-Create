package com.createcompletelycreate.components.extruder;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.core.Direction;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public abstract class AbstractExtruderRenderer<EXB extends AbstractExtruderBlockEntity> extends KineticBlockEntityRenderer<EXB> {
    private final PartialModel poleModel;

    public AbstractExtruderRenderer(BlockEntityRendererProvider.Context context, PartialModel poleModel) {
        super(context);
        this.poleModel = poleModel;
    }

    protected PartialModel getPoleModel() {
        return poleModel;
    }

    @Override
    public boolean shouldRenderOffScreen(EXB be) {
        return true;
    }
    @Override
    protected void renderSafe(EXB be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {

        FilteringRenderer.renderOnBlockEntity(be, partialTicks, ms, buffer, light, overlay);

        if (VisualizationManager.supportsVisualization(be.getLevel()))
            return;

        VertexConsumer vb = buffer.getBuffer(RenderType.cutout());

        BlockState blockState = be.getBlockState();

        float renderedHeadOffset =
                be.getRenderedPoleOffset(partialTicks) * be.getHeadOffset();

        SuperByteBuffer poleRender = CachedBuffers.partialFacing(getPoleModel(), blockState,
                blockState.getValue(HORIZONTAL_FACING));
        poleRender.translate(0, -renderedHeadOffset, 0)
                .light(light)
                .renderInto(ms, vb);

        Direction facing = blockState.getValue(HORIZONTAL_FACING);
        SuperByteBuffer shaftBack = CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, blockState, facing.getOpposite());
        standardKineticRotationTransform(shaftBack, be, light).renderInto(ms, vb);
        SuperByteBuffer shaftFront = CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, blockState, facing);
        standardKineticRotationTransform(shaftFront, be, light).renderInto(ms, vb);
    }
    @Override
    protected SuperByteBuffer getRotatedModel(EXB be, BlockState state) {
        return CachedBuffers.partial(AllPartialModels.SHAFT_HALF, state);
    }
}
