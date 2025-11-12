package wootrevived.woot.util.handlers;

import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class WootEnergyHandler extends SimpleEnergyHandler {
    public WootEnergyHandler(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    protected void onEnergyChanged() {}

    public void setEnergy(int energy) {
        this.energy = energy;
        onEnergyChanged();
    }

    @Override
    public int insert(int amount, TransactionContext transaction) {
        int res = super.insert(amount, transaction);
        onEnergyChanged();
        return res;
    }

    @Override
    public int extract(int amount, TransactionContext transaction) {
        int res = super.extract(amount, transaction);
        onEnergyChanged();
        return res;
    }

    public int internalExtractEnergy(int amount, TransactionContext transaction) {
        int res = super.extract(amount, transaction);
        onEnergyChanged();
        return res;
    }
}
