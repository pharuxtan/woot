package wootrevived.woot.util.handlers;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class WootFluidResourceHandler extends FluidStacksResourceHandler {
    protected final boolean isOutput;
    protected final Predicate<FluidResource> validator;

    public WootFluidResourceHandler(int capacity, boolean isOutput) {
        this(capacity, isOutput, f -> true);
    }

    public WootFluidResourceHandler(int capacity, boolean isOutput, Predicate<FluidResource> validator) {
        super(1, capacity);
        this.isOutput = isOutput;
        this.validator = validator;
    }

    public WootFluidResourceHandler setCapacity(int capacity) {
        this.capacity = capacity;
        onContentsChanged(0, FluidStack.EMPTY);
        return this;
    }

    public FluidStack getStack(){
        return getStackFrom(getResource(0), getAmountAsInt(0));
    }

    public void setStack(FluidStack stack){
        stacks.set(0, stack.copy());
        onContentsChanged(0, stack);
    }

    public boolean isEmpty(){
        return getAmountAsInt(0) == 0;
    }

    @Override
    public boolean isValid(int index, @NotNull FluidResource resource) {
        return validator.test(resource);
    }

    public boolean isOutput(){
        return isOutput;
    }
}
