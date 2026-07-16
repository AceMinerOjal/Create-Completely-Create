package com.createcompletelycreate.foundation.blockEntity.behaviour;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class CycleBehavior extends BlockEntityBehaviour {

	private final int cycleTime;
	private boolean actuateHalfcycle;
	public static final BehaviourType<CycleBehavior> TYPE = new BehaviourType<>();
	public CycleBehaviourSpecifics specifics;
	private int prevRunningTicks;
	private int runningTicks;
	private boolean running;
	private boolean finished;
	private int cycleDivider;
	private int numCycles;
	private int currentCycle;
	private int actuatedTimes;
	private boolean actuatedInCurrentCycle;

	public <T extends SmartBlockEntity & CycleBehaviourSpecifics> CycleBehavior(T te, int pCycle, boolean pActuateHalfCycle) {
		super(te);
		this.specifics = te;
		cycleTime = pCycle;
		actuateHalfcycle = pActuateHalfCycle;
		numCycles = 0;
		cycleDivider = (actuateHalfcycle) ? 2 : 1;
		currentCycle = 0;
		actuatedTimes = 0;
		actuatedInCurrentCycle = false;
	}

	@Override
	public void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
		running = compound.getBoolean("Running");
		finished = compound.getBoolean("Finished");
		prevRunningTicks = runningTicks = compound.getInt("Ticks");
		currentCycle = compound.getInt("CurrentCycle");
		numCycles = compound.getInt("NumCycles");
		actuatedTimes = compound.getInt("ActuatedTimes");
		actuatedInCurrentCycle = compound.getBoolean("Actuated");
		super.read(compound, registries, clientPacket);
	}

	@Override
	public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
		compound.putBoolean("Running", running);
		compound.putBoolean("Finished", finished);
		compound.putInt("Ticks", runningTicks);
		compound.putInt("CurrentCycle", currentCycle);
		compound.putInt("NumCycles", numCycles);
		compound.putInt("ActuatedTimes", actuatedTimes);
		compound.putBoolean("Actuated", actuatedInCurrentCycle);
		super.write(compound, registries, clientPacket);
	}

	private void resetState(boolean newRunning) {
		running = newRunning;
		finished = false;
		prevRunningTicks = 0;
		runningTicks = 0;
		currentCycle = 0;
		actuatedTimes = 0;
		actuatedInCurrentCycle = false;
		numCycles = specifics.getCycles();
		blockEntity.sendData();
	}

	public void start() {
		resetState(true);
	}

	public void stop() {
		resetState(false);
	}

	public void restartCycle() {
		running = true;
		finished = false;
		prevRunningTicks = 0;
		runningTicks = 0;
		actuatedInCurrentCycle = false;
	}

	@Override
	public BehaviourType<?> getType() {
		return TYPE;
	}

	@Override
	public void tick() {
		super.tick();

		Level level = getWorld();
		if (!running || level == null) {
			if (level != null && !level.isClientSide) {
				if (specifics.getKineticSpeed() == 0)
					return;
				if (specifics.tryProcess(true))
					start();
			}
			return;
		}

		if (level.isClientSide && runningTicks == -cycleTime) {
			prevRunningTicks = cycleTime;
			return;
		}

		if (runningTicks >= cycleTime / cycleDivider && specifics.getKineticSpeed() != 0) {
			if (!actuatedInCurrentCycle) {
				actuatedTimes++;
				apply();
				if (!level.isClientSide) {
					blockEntity.sendData();
					specifics.playActuateSound();
				}
				actuatedInCurrentCycle = true;
			}
		}

		if (!level.isClientSide && runningTicks >= cycleTime) {
			specifics.onCycleCompleted();
			currentCycle++;
			if (currentCycle == numCycles) {
				finished = true;
				running = false;
				specifics.onOperationCompleted();
				specifics.playCompletionSound();
			} else {
				if (!specifics.tryProcess(true)) {
					stop();
					return;
				}
				restartCycle();
			}
			blockEntity.sendData();
			return;
		}

		prevRunningTicks = runningTicks;
		runningTicks += getRunningTickSpeed();

		if (level.isClientSide) {
			specifics.playRunningSound();
			specifics.showParticles();
		}

		if (prevRunningTicks < cycleTime && runningTicks >= cycleTime) {
			runningTicks = cycleTime;
			if (level.isClientSide && !blockEntity.isVirtual()) {
				runningTicks = -(cycleTime);
			}
		}
	}

	protected void apply() {
		Level level = getWorld();
		if (actuatedTimes != numCycles)
			return;
		if (level.isClientSide)
			return;
		if (specifics.tryProcess(false))
			blockEntity.sendData();
	}

	public int getRunningTickSpeed() {
		float speed = specifics.getKineticSpeed();
		if (speed == 0)
			return 0;
		return (int) Mth.lerp(Mth.clamp(Math.abs(speed) / 512f, 0, 1), 1, 60);
	}

	public boolean isRunning() {
		return running;
	}

	public int getCycleTime() {
		return cycleTime;
	}

	public int getActuatedTimes() {
		return actuatedTimes;
	}

	public int getCycles() {
		return numCycles;
	}

	public int getPrevRunningTicks() {
		return prevRunningTicks;
	}

	public int getRunningTicks() {
		return runningTicks;
	}

	public interface CycleBehaviourSpecifics {
		default void onCycleCompleted() {}
		default void onOperationCompleted() {}
		default void playActuateSound() {}
		default void playRunningSound() {}
		default void showParticles() {}
		default void playCompletionSound() {}
		int getCycles();
		float getKineticSpeed();
		boolean tryProcess(boolean simulate);
	}
}
