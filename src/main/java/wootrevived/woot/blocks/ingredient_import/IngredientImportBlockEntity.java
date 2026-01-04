package wootrevived.woot.blocks.ingredient_import;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;
import wootrevived.woot.Woot;
import wootrevived.woot.data.IngredientImportData;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.util.block.FactoryBlockBaseEntity;
import wootrevived.woot.util.handlers.WootImportFluidHandler;
import wootrevived.woot.util.handlers.WootImportItemHandler;

import java.util.List;

public class IngredientImportBlockEntity extends FactoryBlockBaseEntity {
    public IngredientImportBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.IMPORT_BLOCK_ENTITY.get(), pos, state);
    }

    private WootImportItemHandler itemHandler = new WootImportItemHandler();
    private WootImportFluidHandler fluidHandler = new WootImportFluidHandler();

    public void setImportItem(int index, List<ItemStack> importItem){
        itemHandler.setImportItem(index, importItem);
    }

    public void setImportFluid(int index, List<FluidStack> importFluid){
        fluidHandler.setImportFluid(index, importFluid);
    }

    public boolean isImportValid(int index){
        return itemHandler.isImportValid(index) && fluidHandler.isImportValid(index);
    }

    public void consumeImports(int index){
        itemHandler.consume(index);
        fluidHandler.consume(index);
    }

    public void extractNeighbors(){
        for(Direction direction : Direction.values()){
            if(direction == Direction.UP || direction == Direction.DOWN) continue;

            BlockPos blockPos = getBlockPos().relative(direction);

            ResourceHandler<ItemResource> neighborItemHandler = level.getCapability(Capabilities.Item.BLOCK, blockPos, direction.getOpposite());
            if(neighborItemHandler != null){
                for (int i = 0; i < neighborItemHandler.size(); i++) {
                    ItemResource resource = neighborItemHandler.getResource(i);
                    if(resource.isEmpty())
                        continue;

                    int sim;
                    try (Transaction tx = Transaction.openRoot()) {
                        sim = itemHandler.insert(i, resource, neighborItemHandler.getAmountAsInt(i), tx);
                    }

                    if(sim > 0){
                        try (Transaction tx = Transaction.openRoot()) {
                            int extracted = neighborItemHandler.extract(i, resource, sim, tx);
                            if (extracted > 0) {
                                itemHandler.insert(i, resource, extracted, tx);
                                tx.commit();
                            }
                        }
                    }
                }
            }

            ResourceHandler<FluidResource> neighborFluidHandler = level.getCapability(Capabilities.Fluid.BLOCK, blockPos, direction.getOpposite());
            if(neighborFluidHandler != null){
                for (int i = 0; i < neighborFluidHandler.size(); i++) {
                    FluidResource resource = neighborFluidHandler.getResource(i);
                    if(resource.isEmpty())
                        continue;

                    int sim;
                    try (Transaction tx = Transaction.openRoot()) {
                        sim = fluidHandler.insert(i, resource, neighborFluidHandler.getAmountAsInt(i), tx);
                    }

                    if(sim > 0){
                        try (Transaction tx = Transaction.openRoot()) {
                            int extracted = neighborFluidHandler.extract(i, resource, sim, tx);
                            if (extracted > 0) {
                                fluidHandler.insert(i, resource, extracted, tx);
                                tx.commit();
                            }
                        }
                    }
                }
            }
        }
    }

    public static ResourceHandler<ItemResource> getItemHandlerCapability(IngredientImportBlockEntity blockEntity, Direction side){
        return blockEntity.itemHandler;
    }

    public static ResourceHandler<FluidResource> getFluidHandlerCapability(IngredientImportBlockEntity blockEntity, Direction side){
        return blockEntity.fluidHandler;
    }

    private IngredientImportData.Component getComponent(){
        return new IngredientImportData.Component(
                itemHandler,
                fluidHandler
        );
    }

    private void setComponent(IngredientImportData.Component component){
        itemHandler = component.itemHandler();
        fluidHandler = component.fluidHandler();
    }

    @Override
    protected void saveAdditional(ValueOutput output){
        super.saveAdditional(output);
        output.store(IngredientImportData.ID, IngredientImportData.CODEC, getComponent());
    }

    @Override
    public void loadAdditional(ValueInput input){
        super.loadAdditional(input);
        input.read(IngredientImportData.ID, IngredientImportData.CODEC).ifPresent(this::setComponent);
    }

    private static final ProblemReporter.ScopedCollector REPORTER = Woot.reporter("IngredientImportBlockEntity");

    @NonNull
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider){
        CompoundTag tag = super.getUpdateTag(provider);
        TagValueOutput output = TagValueOutput.createWithContext(REPORTER, provider);
        saveAdditional(output);
        tag.merge(output.buildResult());
        return tag;
    }

    @Override
    public void handleUpdateTag(ValueInput input){
        super.handleUpdateTag(input);
        loadAdditional(input);
    }
}
