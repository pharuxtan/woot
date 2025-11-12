package wootrevived.woot.util.handlers;

import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.util.common.MachineSideProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class WootItemHandlerWrapper implements ResourceHandler<ItemResource> {
    private final List<ItemHandlerWrapper> wrappers = new ArrayList<>();

    public WootItemHandlerWrapper addHandler(WootItemResourceHandler handler, Supplier<MachineSideProperty> property) {
        this.wrappers.add(new ItemHandlerWrapper(handler, property));
        return this;
    }

    @Override
    public int size() {
        int size = 0;

        for(ItemHandlerWrapper wrapper : wrappers)
            size += wrapper.handler.size();

        return size;
    }

    @Override
    public @NotNull ItemResource getResource(int index) {
        int size = 0;
        for(ItemHandlerWrapper wrapper : wrappers){
            WootItemResourceHandler handler = wrapper.handler;
            if(index >= size + handler.size()){
                size += handler.size();
                continue;
            }

            return handler.getResource(index - size);
        }
        return ItemResource.EMPTY;
    }

    @Override
    public long getAmountAsLong(int index) {
        int size = 0;
        for(ItemHandlerWrapper wrapper : wrappers){
            WootItemResourceHandler handler = wrapper.handler;
            if(index >= size + handler.size()){
                size += handler.size();
                continue;
            }

            return handler.getAmountAsLong(index - size);
        }
        return 0;
    }

    @Override
    public long getCapacityAsLong(int index, @NotNull ItemResource resource) {
        int size = 0;
        for(ItemHandlerWrapper wrapper : wrappers){
            WootItemResourceHandler handler = wrapper.handler;
            if(index >= size + handler.size()){
                size += handler.size();
                continue;
            }

            return handler.getCapacityAsLong(index - size, resource);
        }
        return 0;
    }

    @Override
    public boolean isValid(int index, @NotNull ItemResource resource) {
        int size = 0;
        for(ItemHandlerWrapper wrapper : wrappers){
            WootItemResourceHandler handler = wrapper.handler;
            if(index >= size + handler.size()){
                size += handler.size();
                continue;
            }

            return handler.isValid(index - size, resource);
        }
        return false;
    }

    @Override
    public int insert(int index, @NotNull ItemResource resource, int amount, @NotNull TransactionContext transaction) {
        int size = 0;
        for(ItemHandlerWrapper wrapper : wrappers){
            WootItemResourceHandler handler = wrapper.handler;
            if(index >= size + handler.size()){
                size += handler.size();
                continue;
            }

            MachineSideProperty property = wrapper.property.get();
            if(handler.isOutput || !handler.isValid(index - size, resource) || property == MachineSideProperty.DISABLED || property == MachineSideProperty.PUSH)
                break;

            return handler.insert(index - size, resource, amount, transaction);
        }
        return 0;
    }

    @Override
    public int extract(int index, @NotNull ItemResource resource, int amount, @NotNull TransactionContext transaction) {
        int size = 0;
        for(ItemHandlerWrapper wrapper : wrappers){
            WootItemResourceHandler handler = wrapper.handler;
            if(index >= size + handler.size()){
                size += handler.size();
                continue;
            }

            MachineSideProperty property = wrapper.property.get();
            if(property == MachineSideProperty.DISABLED || property == MachineSideProperty.PULL)
                break;

            return handler.extract(index - size, resource, amount, transaction);
        }
        return 0;
    }

    private record ItemHandlerWrapper(
            WootItemResourceHandler handler,
            Supplier<MachineSideProperty> property
    ) {}
}
