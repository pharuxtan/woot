package wootrevived.woot.util.handlers;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class WootItemResourceHandler extends ItemStacksResourceHandler {
    protected final boolean isOutput;

    public WootItemResourceHandler(int size, boolean isOutput) {
        super(size);
        this.isOutput = isOutput;
    }

    public WootItemResourceHandler(boolean isOutput) {
        super(1);
        this.isOutput = isOutput;
    }

    public boolean isOutput(){
        return this.isOutput;
    }

    public ItemStack getStackInSlot(int slot) {
        return getStackFrom(getResource(slot), getAmountAsInt(slot));
    }

    public void setStackInSlot(int slot, ItemStack stack) {
        set(slot, ItemResource.of(stack), stack.getCount());
    }
}
