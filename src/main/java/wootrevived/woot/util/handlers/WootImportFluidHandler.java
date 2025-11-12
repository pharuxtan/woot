package wootrevived.woot.util.handlers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class WootImportFluidHandler implements ResourceHandler<FluidResource> {
    private final Map<Integer, List<FluidStack>> importFluids = new HashMap<>();
    private final Map<Integer, List<Integer>> tanks = new HashMap<>();

    private final ImportJournal importJournal = new ImportJournal();

    private Map<Integer, List<FluidStack>> getImportFluids() {
        return importFluids;
    }

    private Map<Integer, List<Integer>> getInternalTanks(){
        return tanks;
    }

    public static final Codec<WootImportFluidHandler> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.unboundedMap(Codec.STRING, FluidStack.OPTIONAL_CODEC.listOf()).xmap(
                            m -> m.entrySet().stream().collect(Collectors.toMap(e -> Integer.parseInt(e.getKey()), e -> (List<FluidStack>)new ArrayList<>(e.getValue()))),
                            m -> m.entrySet().stream().collect(Collectors.toMap(e -> Integer.toString(e.getKey()), Map.Entry::getValue))
                    ).fieldOf("ImportTanks").forGetter(WootImportFluidHandler::getImportFluids),
                    Codec.unboundedMap(Codec.STRING, Codec.INT.listOf()).xmap(
                            m -> m.entrySet().stream().collect(Collectors.toMap(e -> Integer.parseInt(e.getKey()), e -> (List<Integer>)new ArrayList<>(e.getValue()))),
                            m -> m.entrySet().stream().collect(Collectors.toMap(e -> Integer.toString(e.getKey()), Map.Entry::getValue))
                    ).fieldOf("Tanks").forGetter(WootImportFluidHandler::getInternalTanks)
            ).apply(inst, WootImportFluidHandler::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, WootImportFluidHandler> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(
                    HashMap::new,
                    ByteBufCodecs.INT,
                    FluidStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new))
            ), WootImportFluidHandler::getImportFluids,

            ByteBufCodecs.map(
                    HashMap::new,
                    ByteBufCodecs.INT,
                    ByteBufCodecs.INT.apply(ByteBufCodecs.collection(ArrayList::new))
            ), WootImportFluidHandler::getInternalTanks,

            WootImportFluidHandler::new
    );

    @Override
    public int hashCode() {
        int h = 0;
        for(Map.Entry<Integer, List<FluidStack>> entry : importFluids.entrySet()) {
            h += entry.getKey().hashCode();
            if(entry.getValue() != null){
                for(FluidStack stack : entry.getValue()){
                    h += h * 31 + FluidStack.hashFluidAndComponents(stack);
                }
            }
        }
        return h + tanks.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof WootImportFluidHandler fluidHandler))
            return false;

        for(Map.Entry<Integer, List<FluidStack>> entry : importFluids.entrySet()) {
            if(!fluidHandler.importFluids.containsKey(entry.getKey()))
                return false;

            List<FluidStack> list = fluidHandler.importFluids.get(entry.getKey());

            if(list.size() != entry.getValue().size())
                return false;

            for(int i = 0; i < list.size(); i++){
                if(!FluidStack.isSameFluidSameComponents(list.get(i), entry.getValue().get(i)))
                    return false;
            }
        }

        return tanks.equals(fluidHandler.tanks);
    }

    public WootImportFluidHandler() {}

    private WootImportFluidHandler(Map<Integer, List<FluidStack>> importFluids, Map<Integer, List<Integer>> tanks) {
        this.importFluids.putAll(importFluids);
        this.tanks.putAll(tanks);
    }

    public void setImportFluid(int index, List<FluidStack> importFluid){
        if(importFluid == null){
            if(importFluids.containsKey(index)){
                importFluids.remove(index);
                tanks.remove(index);
            }
            return;
        }

        if(isEqual(importFluids.get(index), importFluid)) return;

        importFluids.put(index, importFluid);
        tanks.put(index, new ArrayList<>(Collections.nCopies(importFluid.size(), 0)));
    }

    private boolean isEqual(List<FluidStack> list1, List<FluidStack> list2){
        if(list1 == null) return false;
        if(list1.size() != list2.size()) return false;

        for(int i = 0; i < list1.size(); i++){
            FluidStack fluid1 = list1.get(i);
            FluidStack fluid2 = list2.get(i);

            if(fluid1.getAmount() != fluid2.getAmount()) return false;
            if(!FluidStack.isSameFluidSameComponents(fluid1, fluid2)) return false;
        }

        return true;
    }

    public boolean isImportValid(int index){
        List<FluidStack> list = importFluids.get(index);
        if(list == null)
            return true;

        List<Integer> amounts = tanks.get(index);

        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getAmount() != amounts.get(i)) return false;
        }

        return true;
    }

    public void consume(int index){
        List<Integer> amounts = tanks.get(index);
        if(amounts == null)
            return;

        Collections.fill(amounts, 0);
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public @NotNull FluidResource getResource(int index) {
        return FluidResource.EMPTY;
    }

    @Override
    public long getAmountAsLong(int index) {
        return 0;
    }

    @Override
    public long getCapacityAsLong(int index, @NotNull FluidResource resource) {
        int capacity = 0;
        for(int i = 0; i < 4; i++){
            List<FluidStack> list = importFluids.get(i);
            if(list == null)
                continue;

            for(FluidStack stack : list){
                if(capacity < stack.getAmount())
                    capacity = stack.getAmount();
            }
        }
        return capacity;
    }

    @Override
    public boolean isValid(int index, @NotNull FluidResource resource) {
        for(int i = 0; i < 4; i++){
            List<FluidStack> list = importFluids.get(i);
            if(list == null)
                continue;

            for(FluidStack s : list){
                if(FluidStack.isSameFluidSameComponents(s, resource.toStack(FluidType.BUCKET_VOLUME)))
                    return true;
            }
        }
        return false;
    }

    @Override
    public int insert(@NotNull FluidResource resource, int amount, @NotNull TransactionContext transaction) {
        if(!isValid(0, resource) || resource.isEmpty())
            return 0;

        boolean didSnapshot = false;

        int filled = 0;
        for(int i = 0; i < 4; i++){
            List<FluidStack> list = importFluids.get(i);
            if(list == null)
                continue;

            List<Integer> tank = tanks.get(i);

            for(int j = 0; j < list.size(); j++){
                FluidStack stack = list.get(j);
                if(FluidStack.isSameFluidSameComponents(stack, resource.toStack(FluidType.BUCKET_VOLUME))){
                    int tankAmount = tank.get(j);
                    int needToBeFill = stack.getAmount() - tankAmount;

                    if(!didSnapshot){
                        importJournal.updateSnapshots(transaction);
                        didSnapshot = true;
                    }

                    if(amount <= needToBeFill){
                        tank.set(j, tankAmount + amount);
                        filled += amount;
                        return filled;
                    } else {
                        tank.set(j, tankAmount + needToBeFill);
                        filled += needToBeFill;
                        amount -= needToBeFill;
                    }
                }
            }
        }
        return filled;
    }

    @Override
    public int insert(int index, @NotNull FluidResource resource, int amount, @NotNull TransactionContext transaction) {
        return insert(resource, amount, transaction);
    }

    @Override
    public int extract(int index, @NotNull FluidResource resource, int amount, @NotNull TransactionContext transaction) {
        return 0;
    }

    private class ImportJournal extends SnapshotJournal<Map<Integer, List<Integer>>> {
        @Override
        protected Map<Integer, List<Integer>> createSnapshot() {
            Map<Integer, List<Integer>> map = new HashMap<>();
            for(Map.Entry<Integer, List<Integer>> entry : tanks.entrySet()){
                map.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }
            return map;
        }

        @Override
        protected void revertToSnapshot(Map<Integer, List<Integer>> snapshot) {
            tanks.clear();
            tanks.putAll(snapshot);
        }
    }
}
