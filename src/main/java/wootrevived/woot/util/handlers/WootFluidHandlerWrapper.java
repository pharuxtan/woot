package wootrevived.woot.util.handlers;

import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.util.common.MachineSideProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class WootFluidHandlerWrapper implements ResourceHandler<FluidResource> {
    private final List<FluidHandlerWrapper> wrappers = new ArrayList<>();

    public WootFluidHandlerWrapper addHandler(WootFluidResourceHandler handler, Supplier<MachineSideProperty> property){
        this.wrappers.add(new FluidHandlerWrapper(handler, property));
        return this;
    }

    @Override
    public int size() {
        return wrappers.size();
    }

    @Override
    public @NotNull FluidResource getResource(int index) {
        if(index < 0 || index >= wrappers.size())
            return FluidResource.EMPTY;

        return wrappers.get(index).handler.getResource(0);
    }

    @Override
    public long getAmountAsLong(int index) {
        if(index < 0 || index >= wrappers.size())
            return 0;

        return wrappers.get(index).handler.getAmountAsLong(0);
    }

    @Override
    public long getCapacityAsLong(int index, @NotNull FluidResource resource) {
        if(index < 0 || index >= wrappers.size())
            return 0;

        return wrappers.get(index).handler.getCapacityAsLong(0, resource);
    }

    @Override
    public boolean isValid(int index, @NotNull FluidResource resource) {
        if(index < 0 || index >= wrappers.size())
            return false;

        return wrappers.get(index).handler.isValid(0, resource);
    }

    @Override
    public int insert(int index, @NotNull FluidResource resource, int amount, @NotNull TransactionContext transaction) {
        if(index < 0 || index >= wrappers.size())
            return 0;

        WootFluidResourceHandler handler = wrappers.get(index).handler;
        MachineSideProperty property = wrappers.get(index).property.get();

        if(handler.isOutput || !handler.isValid(0, resource) || property == MachineSideProperty.DISABLED || property == MachineSideProperty.PUSH)
            return 0;

        return handler.insert(0, resource, amount, transaction);
    }

    @Override
    public int insert(@NotNull FluidResource resource, int amount, @NotNull TransactionContext transaction) {
        int fill = 0;
        for (FluidHandlerWrapper wrapper : wrappers) {
            WootFluidResourceHandler handler = wrapper.handler;
            MachineSideProperty property = wrapper.property.get();

            if (handler.isOutput || !handler.isValid(0, resource) || property == MachineSideProperty.DISABLED || property == MachineSideProperty.PUSH)
                continue;

            int filled = handler.insert(0, resource, amount, transaction);
            amount -= filled;
            fill += filled;
        }
        return fill;
    }

    @Override
    public int extract(@NotNull FluidResource resource, int amount, @NotNull TransactionContext transaction) {
        int extract = 0;
        for(FluidHandlerWrapper wrapper : wrappers) {
            WootFluidResourceHandler handler = wrapper.handler;
            MachineSideProperty property = wrapper.property.get();

            if(handler.getAmountAsLong(0) == 0 || !resource.equals(handler.getResource(0)) || property == MachineSideProperty.DISABLED || property == MachineSideProperty.PULL)
                continue;

            int extracted = handler.extract(0, resource, amount, transaction);
            amount -= extracted;
            extract += extracted;
        }
        return extract;
    }

    @Override
    public int extract(int index, @NotNull FluidResource resource, int amount, @NotNull TransactionContext transaction) {
        if(index < 0 || index >= wrappers.size())
            return 0;

        WootFluidResourceHandler handler = wrappers.get(index).handler;
        MachineSideProperty property = wrappers.get(index).property.get();

        if(handler.getAmountAsLong(0) == 0 || !resource.equals(handler.getResource(0)) || property == MachineSideProperty.DISABLED || property == MachineSideProperty.PULL)
            return 0;

        return handler.extract(0, resource, amount, transaction);
    }

    private record FluidHandlerWrapper(
            WootFluidResourceHandler handler,
            Supplier<MachineSideProperty> property
    ) {}
}
