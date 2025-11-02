package wootrevived.woot.util.handlers;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.util.common.MachineSideProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class WootFluidHandlerWrapper implements IFluidHandler {
    private final List<FluidWrapper> fluidWrappers = new ArrayList<>();

    public WootFluidHandlerWrapper addHandler(WootFluidTankHandler tank, Supplier<MachineSideProperty> property){
        fluidWrappers.add(new FluidWrapper(tank, property));
        return this;
    }

    @Override
    public int getTanks() {
        return fluidWrappers.size();
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        if(tank < 0 || tank >= fluidWrappers.size())
            return FluidStack.EMPTY;
        return fluidWrappers.get(tank).tank.getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        if(tank < 0 || tank >= fluidWrappers.size())
            return 0;
        return fluidWrappers.get(tank).tank.getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        if(tank < 0 || tank >= fluidWrappers.size())
            return false;
        return fluidWrappers.get(tank).tank.isFluidValid(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        resource = resource.copy();
        int fill = 0;
        for(int i = 0; i < getTanks(); i++){
            WootFluidTankHandler tank = fluidWrappers.get(i).tank;
            MachineSideProperty property = fluidWrappers.get(i).property().get();

            if(tank.isOutput || !tank.isFluidValid(resource) || property == MachineSideProperty.DISABLED || property == MachineSideProperty.PUSH)
                continue;

            int filled = tank.fill(resource, action);
            resource.shrink(filled);
            fill += filled;
        }
        return fill;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        for(int i = 0; i < getTanks(); i++){
            WootFluidTankHandler tank = fluidWrappers.get(i).tank;
            if(tank.isEmpty() || !FluidStack.isSameFluidSameComponents(tank.getFluid(), resource))
                continue;

            MachineSideProperty property = fluidWrappers.get(i).property().get();
            if(property == MachineSideProperty.DISABLED || property == MachineSideProperty.PULL)
                continue;

            FluidStack drained = tank.drain(resource, action);
            if(!drained.isEmpty())
                return drained;
        }
        return FluidStack.EMPTY;
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        for(int i = 0; i < getTanks(); i++){
            WootFluidTankHandler tank = fluidWrappers.get(i).tank;
            if(tank.isEmpty())
                continue;

            MachineSideProperty property = fluidWrappers.get(i).property().get();
            if(property == MachineSideProperty.DISABLED || property == MachineSideProperty.PULL)
                continue;

            return tank.drain(maxDrain, action);
        }
        return FluidStack.EMPTY;
    }

    private record FluidWrapper(
            WootFluidTankHandler tank,
            Supplier<MachineSideProperty> property
    ) {}
}