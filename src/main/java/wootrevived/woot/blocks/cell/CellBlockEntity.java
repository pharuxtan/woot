package wootrevived.woot.blocks.cell;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.Woot;
import wootrevived.woot.config.CellConfig;
import wootrevived.woot.data.CellData;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.ComponentsRegistry;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.util.block.FactoryBlockBaseEntity;
import wootrevived.woot.util.handlers.WootFluidResourceHandler;

public class CellBlockEntity extends FactoryBlockBaseEntity {
    public CellBlockEntity(BlockEntityType<?> entity, BlockPos pos, BlockState state) {
        super(entity, pos, state);
        tankHandler.setCapacity(getCapacity());
    }

    public final WootFluidResourceHandler tankHandler = createTank();

    private WootFluidResourceHandler createTank() {
        return new WootFluidResourceHandler(1000, false, (stack) -> stack.is(FluidsRegistry.SOURCE_VITALITY_FUEL_FLUID.get())) {
            @Override
            protected void onContentsChanged(int i, @NotNull FluidStack s) {
                setChanged();
            }
        };
    }

    private int getCapacity(){
        if(getType() == BlocksRegistry.COPPER_CELL_BLOCK_ENTITY.get())
            return CellConfig.COPPER_CAPACITY.get();
        if(getType() == BlocksRegistry.IRON_CELL_BLOCK_ENTITY.get())
            return CellConfig.IRON_CAPACITY.get();
        if(getType() == BlocksRegistry.GOLD_CELL_BLOCK_ENTITY.get())
            return CellConfig.GOLD_CAPACITY.get();
        if(getType() == BlocksRegistry.DIAMOND_CELL_BLOCK_ENTITY.get())
            return CellConfig.DIAMOND_CAPACITY.get();
        if(getType() == BlocksRegistry.NETHERITE_CELL_BLOCK_ENTITY.get())
            return CellConfig.NETHERITE_CAPACITY.get();
        return 0;
    }

    public static ResourceHandler<FluidResource> getFluidHandlerCapability(CellBlockEntity blockEntity, Direction side){
        if(blockEntity.getBlockState().getValue(BlockStateProperties.ENABLED))
            return blockEntity.tankHandler;
        return null;
    }

    private CellData.Component getComponent(){
        return new CellData.Component(
                tankHandler.getStack()
        );
    }

    private void setComponent(CellData.Component component){
        tankHandler.setStack(component.tankFluid());
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter getter){
        CellData.Component component = getter.get(ComponentsRegistry.CELL_DATA);
        if(component == null)
            return;

        setComponent(component);
        setChanged();
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder){
        builder.set(ComponentsRegistry.CELL_DATA, getComponent());
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output){
        super.saveAdditional(output);
        output.store(CellData.ID, CellData.CODEC, getComponent());
    }

    @Override
    public void loadAdditional(@NotNull ValueInput input){
        super.loadAdditional(input);
        input.read(CellData.ID, CellData.CODEC).ifPresent(this::setComponent);
    }

    private static final ProblemReporter.ScopedCollector REPORTER = Woot.reporter("CellBlockEntity");

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
