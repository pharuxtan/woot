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
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
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

            IItemHandler neighborItemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, blockPos, direction.getOpposite());
            if(neighborItemHandler != null){
                for (int i = 0; i < neighborItemHandler.getSlots(); i++) {
                    ItemStack stack = neighborItemHandler.getStackInSlot(i);
                    ItemStack result = itemHandler.insertItem(i, stack, true);
                    if(result.getCount() < stack.getCount()){
                        ItemStack extracted = neighborItemHandler.extractItem(i, stack.getCount() - result.getCount(), false);
                        if(!extracted.isEmpty())
                            itemHandler.insertItem(i, extracted, false);
                    }
                }
            }

            IFluidHandler neighborFluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, blockPos, direction.getOpposite());
            if(neighborFluidHandler != null){
                for (int i = 0; i < neighborFluidHandler.getTanks(); i++) {
                    FluidStack stack = neighborFluidHandler.getFluidInTank(i);
                    int filled = fluidHandler.fill(stack, IFluidHandler.FluidAction.SIMULATE);
                    if(filled > 0){
                        FluidStack drained = neighborFluidHandler.drain(new FluidStack(stack.getFluid(), filled), IFluidHandler.FluidAction.EXECUTE);
                        if(!drained.isEmpty())
                            fluidHandler.fill(drained, IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            }
        }
    }

    public static IItemHandler getItemHandlerCapability(IngredientImportBlockEntity blockEntity, Direction side){
        return blockEntity.itemHandler;
    }

    public static IFluidHandler getFluidHandlerCapability(IngredientImportBlockEntity blockEntity, Direction side){
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
    protected void saveAdditional(@NotNull ValueOutput output){
        super.saveAdditional(output);
        output.store(IngredientImportData.ID, IngredientImportData.CODEC, getComponent());
    }

    @Override
    public void loadAdditional(@NotNull ValueInput input){
        super.loadAdditional(input);
        input.read(IngredientImportData.ID, IngredientImportData.CODEC).ifPresent(this::setComponent);
    }

    private static final ProblemReporter.ScopedCollector REPORTER = Woot.reporter("IngredientImportBlockEntity");

    @NotNull
    @Override
    public CompoundTag getUpdateTag(HolderLookup.@NotNull Provider provider){
        CompoundTag tag = super.getUpdateTag(provider);
        TagValueOutput output = TagValueOutput.createWithContext(REPORTER, provider);
        saveAdditional(output);
        tag.merge(output.buildResult());
        return tag;
    }

    @Override
    public void handleUpdateTag(@NotNull ValueInput input){
        super.handleUpdateTag(input);
        loadAdditional(input);
    }
}
