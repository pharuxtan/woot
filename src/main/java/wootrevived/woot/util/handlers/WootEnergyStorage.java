package wootrevived.woot.util.handlers;

import net.neoforged.neoforge.energy.EnergyStorage;

public class WootEnergyStorage extends EnergyStorage {
    public WootEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    protected void onEnergyChanged() {}

    public void setEnergy(int energy) {
        this.energy = energy;
        onEnergyChanged();
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int res = super.receiveEnergy(maxReceive, simulate);
        onEnergyChanged();
        return res;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int res = super.extractEnergy(maxExtract, simulate);
        onEnergyChanged();
        return res;
    }

    public int internalExtractEnergy(int toExtract, boolean simulate) {
        if (toExtract <= 0) {
            return 0;
        }

        int energyExtracted = Math.min(this.energy, Math.min(this.maxExtract, toExtract));
        if (!simulate) {
            this.energy -= energyExtracted;
            onEnergyChanged();
        }
        return energyExtracted;
    }
}
