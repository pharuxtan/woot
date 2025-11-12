package wootrevived.woot.util.handlers;

import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class WootCombinedItemHandler extends ItemStacksResourceHandler {
    private final ItemStacksResourceHandler[] handlers;
    private final int[] baseIndex;
    private final int sizeCache;

    public WootCombinedItemHandler(ItemStacksResourceHandler... handlers) {
        super(0);
        this.handlers = handlers;
        this.baseIndex = new int[handlers.length];
        int index = 0;
        for (int i = 0; i < handlers.length; i++) {
            this.baseIndex[i] = index;
            index += handlers[i].size();
        }
        this.sizeCache = index;
    }

    protected int getHandlerIndex(int index) {
        if (index < 0 || index >= sizeCache)
            throw new IndexOutOfBoundsException("Index " + index + " is out-of-bounds for combined handler with size " + sizeCache);

        for (int handlerIndex = 0; handlerIndex < baseIndex.length - 1; handlerIndex++) {
            if (index < baseIndex[handlerIndex + 1]) {
                return handlerIndex;
            }
        }
        // Guaranteed to be in bounds since we checked index < size above
        return baseIndex.length - 1;
    }

    protected ItemStacksResourceHandler getHandlerFromIndex(int handlerIndex) {
        return handlers[handlerIndex];
    }

    protected int getSlotFromIndex(int index, int handlerIndex) {
        return index - baseIndex[handlerIndex];
    }

    @Override
    public final int size() {
        return sizeCache;
    }

    @Override
    public void set(int index, ItemResource resource, int amount) {
        int handlerIndex = getHandlerIndex(index);
        getHandlerFromIndex(handlerIndex).set(getSlotFromIndex(index, handlerIndex), resource, amount);
    }

    @Override
    public ItemResource getResource(int index) {
        int handlerIndex = getHandlerIndex(index);
        return getHandlerFromIndex(handlerIndex).getResource(getSlotFromIndex(index, handlerIndex));
    }

    @Override
    public long getAmountAsLong(int index) {
        int handlerIndex = getHandlerIndex(index);
        return getHandlerFromIndex(handlerIndex).getAmountAsLong(getSlotFromIndex(index, handlerIndex));
    }

    @Override
    public long getCapacityAsLong(int index, ItemResource resource) {
        int handlerIndex = getHandlerIndex(index);
        return getHandlerFromIndex(handlerIndex).getCapacityAsLong(getSlotFromIndex(index, handlerIndex), resource);
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        int handlerIndex = getHandlerIndex(index);
        if (resource.isEmpty()) return true;
        return getHandlerFromIndex(handlerIndex).isValid(getSlotFromIndex(index, handlerIndex), resource);
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);

        int handlerIndex = getHandlerIndex(index);
        return getHandlerFromIndex(handlerIndex).insert(getSlotFromIndex(index, handlerIndex), resource, amount, transaction);
    }

    @Override
    public int insert(ItemResource resource, int amount, TransactionContext transaction) {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);

        int inserted = 0;
        for (ItemStacksResourceHandler resourceHandler : handlers) {
            inserted += resourceHandler.insert(resource, amount - inserted, transaction);
            if (inserted == amount) break;
        }
        return inserted;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);

        int handlerIndex = getHandlerIndex(index);
        return getHandlerFromIndex(handlerIndex).extract(getSlotFromIndex(index, handlerIndex), resource, amount, transaction);
    }

    @Override
    public int extract(ItemResource resource, int amount, TransactionContext transaction) {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);

        int extracted = 0;
        for (ItemStacksResourceHandler resourceHandler : handlers) {
            extracted += resourceHandler.extract(resource, amount - extracted, transaction);
            if (extracted == amount) break;
        }
        return extracted;
    }
}
