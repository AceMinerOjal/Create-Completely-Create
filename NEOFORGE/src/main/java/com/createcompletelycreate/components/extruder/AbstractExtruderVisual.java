package com.createcompletelycreate.components.extruder;

import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.core.Direction;
import org.joml.Quaternionf;

import java.util.function.Consumer;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public abstract class AbstractExtruderVisual<EX extends AbstractExtruderBlockEntity> extends KineticBlockEntityVisual<EX> implements SimpleDynamicVisual {
    private final OrientedInstance extruderPole;
    protected final RotatingInstance shaftBack;
    protected final RotatingInstance shaftFront;
    final Direction direction;
    private final Direction opposite;
    private final PartialModel poleModel;

    protected PartialModel getPoleModel() {
        return poleModel;
    }

    public AbstractExtruderVisual(VisualizationContext context, EX blockEntity, float partialTick, PartialModel poleModel) {
        super(context, blockEntity, partialTick);
        this.poleModel = poleModel;
        direction = blockState.getValue(HORIZONTAL_FACING);

        opposite = direction.getOpposite();

        shaftBack = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
                .createInstance();
        shaftBack.setup(blockEntity)
                .setPosition(getVisualPosition())
                .rotateToFace(Direction.SOUTH, opposite)
                .setChanged();

        shaftFront = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
                .createInstance();
        shaftFront.setup(blockEntity)
                .setPosition(getVisualPosition())
                .rotateToFace(Direction.SOUTH, direction)
                .setChanged();

        extruderPole = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(getPoleModel()))
                .createInstance();

        Quaternionf q = Axis.YP
                .rotationDegrees(AngleHelper.horizontalAngle(blockState.getValue(HORIZONTAL_FACING)));

        extruderPole.rotation(q);

        transformModels(partialTick);
    }

    private void transformModels(float pt) {
        float renderedHeadOffset = getRenderedHeadOffset(pt);

        extruderPole.position(getVisualPosition())
                .translatePosition(0, -renderedHeadOffset, 0)
                .setChanged();
    }

    private float getRenderedHeadOffset(float pt) {
        return blockEntity.getRenderedPoleOffset(pt) * blockEntity.headOffset;
    }

    @Override
    public void beginFrame(Context ctx) {
        shaftBack.setup(blockEntity).setChanged();
        shaftFront.setup(blockEntity).setChanged();
        transformModels(ctx.partialTick());
    }

    @Override
    public void updateLight(float partialTick) {
        relight(extruderPole, shaftBack, shaftFront);
    }

    @Override
    protected void _delete() {
        extruderPole.delete();
        shaftBack.delete();
        shaftFront.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(extruderPole);
        consumer.accept(shaftBack);
        consumer.accept(shaftFront);
    }
}
