package wootrevived.woot.util.handlers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class WootImportItemHandler implements ResourceHandler<ItemResource> {
    private final Map<Integer, List<ItemStack>> importItems = new HashMap<>();
    private final Map<Integer, List<Integer>> items = new HashMap<>();

    private final ImportJournal importJournal = new ImportJournal();

    private Map<Integer, List<ItemStack>> getImportItems() {
        return importItems;
    }

    private Map<Integer, List<Integer>> getItems() {
        return items;
    }

    public static final Codec<WootImportItemHandler> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.unboundedMap(Codec.STRING, ItemStack.OPTIONAL_CODEC.listOf()).xmap(
                            m -> m.entrySet().stream().collect(Collectors.toMap(e -> Integer.parseInt(e.getKey()), e -> (List<ItemStack>)new ArrayList<>(e.getValue()))),
                            m -> m.entrySet().stream().collect(Collectors.toMap(e -> Integer.toString(e.getKey()), Map.Entry::getValue))
                    ).fieldOf("ImportItems").forGetter(WootImportItemHandler::getImportItems),
                    Codec.unboundedMap(Codec.STRING, Codec.INT.listOf()).xmap(
                            m -> m.entrySet().stream().collect(Collectors.toMap(e -> Integer.parseInt(e.getKey()), e -> (List<Integer>)new ArrayList<>(e.getValue()))),
                            m -> m.entrySet().stream().collect(Collectors.toMap(e -> Integer.toString(e.getKey()), Map.Entry::getValue))
                    ).fieldOf("Items").forGetter(WootImportItemHandler::getItems)
            ).apply(inst, WootImportItemHandler::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, WootImportItemHandler> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(
                    HashMap::new,
                    ByteBufCodecs.INT,
                    ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new))
            ), WootImportItemHandler::getImportItems,

            ByteBufCodecs.map(
                    HashMap::new,
                    ByteBufCodecs.INT,
                    ByteBufCodecs.INT.apply(ByteBufCodecs.collection(ArrayList::new))
            ), WootImportItemHandler::getItems,

            WootImportItemHandler::new
    );

    @Override
    public int hashCode() {
        int h = 0;
        for(Map.Entry<Integer, List<ItemStack>> entry : importItems.entrySet()) {
            h += entry.getKey().hashCode();
            if(entry.getValue() != null){
                for(ItemStack stack : entry.getValue()){
                    h += h * 31 + ItemStack.hashItemAndComponents(stack);
                }
            }
        }
        return h + items.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof WootImportItemHandler itemHandler))
            return false;

        for(Map.Entry<Integer, List<ItemStack>> entry : importItems.entrySet()) {
            if(!itemHandler.importItems.containsKey(entry.getKey()))
                return false;

            List<ItemStack> list = itemHandler.importItems.get(entry.getKey());

            if(list.size() != entry.getValue().size())
                return false;

            for(int i = 0; i < list.size(); i++){
                if(!ItemStack.isSameItemSameComponents(list.get(i), entry.getValue().get(i)))
                    return false;
            }
        }

        return items.equals(itemHandler.items);
    }

    public WootImportItemHandler() {}

    private WootImportItemHandler(Map<Integer, List<ItemStack>> importItems, Map<Integer, List<Integer>> items) {
        this.importItems.putAll(importItems);
        this.items.putAll(items);
    }

    public void setImportItem(int index, List<ItemStack> importItem){
        if(importItem == null){
            if(importItems.containsKey(index)){
                importItems.remove(index);
                items.remove(index);
            }
            return;
        }

        if(isEqual(importItems.get(index), importItem)) return;

        importItems.put(index, importItem);
        items.put(index, new ArrayList<>(Collections.nCopies(importItem.size(), 0)));
    }

    private boolean isEqual(List<ItemStack> list1, List<ItemStack> list2){
        if(list1 == null) return false;
        if(list1.size() != list2.size()) return false;

        for(int i = 0; i < list1.size(); i++){
            ItemStack item1 = list1.get(i);
            ItemStack item2 = list2.get(i);

            if(item1.getCount() != item2.getCount()) return false;
            if(!ItemStack.isSameItemSameComponents(item1, item2)) return false;
        }

        return true;
    }

    public boolean isImportValid(int index){
        List<ItemStack> list = importItems.get(index);
        if(list == null)
            return true;

        List<Integer> counts = items.get(index);

        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getCount() != counts.get(i)) return false;
        }

        return true;
    }

    public void consume(int index){
        List<Integer> counts = items.get(index);
        if(counts == null)
            return;

        Collections.fill(counts, 0);
    }
    
    @Override
    public int size() {
        return 1;
    }

    @Override
    public @NotNull ItemResource getResource(int index) {
        return ItemResource.EMPTY;
    }

    @Override
    public long getAmountAsLong(int index) {
        return 0;
    }

    @Override
    public long getCapacityAsLong(int index, @NotNull ItemResource resource) {
        int count = 0;
        for(int i = 0; i < 4; i++){
            List<ItemStack> list = importItems.get(i);
            if(list == null)
                continue;

            for(ItemStack stack : list){
                if(count < stack.getCount())
                    count = stack.getCount();
            }
        }
        return count;
    }

    @Override
    public boolean isValid(int index, @NotNull ItemResource resource) {
        for(int i = 0; i < 4; i++){
            List<ItemStack> list = importItems.get(i);
            if(list == null)
                continue;

            for(ItemStack s : list){
                if(ItemStack.isSameItemSameComponents(s, resource.toStack()))
                    return true;
            }
        }
        return false;
    }

    @Override
    public int insert(@NotNull ItemResource resource, int amount, @NotNull TransactionContext transaction) {
        if(!isValid(0, resource) || resource.isEmpty())
            return 0;

        boolean didSnapshot = false;

        int filled = 0;
        for(int i = 0; i < 4; i++){
            List<ItemStack> list = importItems.get(i);
            if(list == null)
                continue;

            List<Integer> item = items.get(i);

            for(int j = 0; j < list.size(); j++){
                ItemStack s = list.get(j);
                if(ItemStack.isSameItemSameComponents(s, resource.toStack())){
                    int itemAmount = item.get(j);
                    int needToBeAdded = s.getCount() - itemAmount;

                    if(!didSnapshot){
                        importJournal.updateSnapshots(transaction);
                        didSnapshot = true;
                    }

                    if(amount <= needToBeAdded){
                        item.set(j, itemAmount + amount);
                        filled += amount;
                        return filled;
                    } else {
                        item.set(j, itemAmount + needToBeAdded);
                        filled += needToBeAdded;
                        amount -= needToBeAdded;
                    }
                }
            }
        }
        return filled;
    }

    @Override
    public int insert(int index, @NotNull ItemResource resource, int amount, @NotNull TransactionContext transaction) {
        return insert(resource, amount, transaction);
    }

    @Override
    public int extract(int index, @NotNull ItemResource resource, int amount, @NotNull TransactionContext transaction) {
        return 0;
    }

    private class ImportJournal extends SnapshotJournal<Map<Integer, List<Integer>>> {
        @Override
        protected Map<Integer, List<Integer>> createSnapshot() {
            Map<Integer, List<Integer>> map = new HashMap<>();
            for(Map.Entry<Integer, List<Integer>> entry : items.entrySet()){
                map.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }
            return map;
        }

        @Override
        protected void revertToSnapshot(Map<Integer, List<Integer>> snapshot) {
            items.clear();
            items.putAll(snapshot);
        }
    }
}
