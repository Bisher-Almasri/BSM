package com.bisheralmasri.bsm.blockentity;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class ControllerEnergyStorage implements IEnergyStorage {
    private static final int CAPACITY = 20_000;
    private static final int MAX_RECEIVE = 1_500;
    private static final int MAX_EXTRACT = 1_000;

    private int energy;
    private final Runnable onChanged;

    public ControllerEnergyStorage(Runnable onChanged) {
        this.onChanged = onChanged;
    }

    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        int received = Math.min(Math.min(toReceive, MAX_RECEIVE), CAPACITY - energy);

        if (received > 0 && !simulate) {
            energy += received;
            onChanged.run();
        }

        return received;
    }

    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        int extracted = Math.min(
                Math.min(toExtract, MAX_EXTRACT),
                energy
        );

        if (extracted > 0 && !simulate) {
            energy -= extracted;
            onChanged.run();
        }

        return extracted;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return CAPACITY;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Energy", energy);
        return tag;
    }

    public void load(CompoundTag tag) {
        energy = Math.clamp(tag.getInt("Energy"), 0, CAPACITY);
    }
}
