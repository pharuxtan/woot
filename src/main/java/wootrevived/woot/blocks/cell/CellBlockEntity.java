package wootrevived.woot.blocks.cell;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.config.CellConfig;
import wootrevived.woot.data.CellData;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.ComponentsRegistry;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.util.block.FactoryBlockBaseEntity;
import wootrevived.woot.util.handlers.WootFluidTankHandler;

public class CellBlockEntity extends FactoryBlockBaseEntity {
    public CellBlockEntity(BlockEntityType<?> entity, BlockPos pos, BlockState state) {
        super(entity, pos, state);
        tankHandler.setCapacity(getCapacity());
    }

    public final WootFluidTankHandler tankHandler = createTank();

    private WootFluidTankHandler createTank() {
        return new WootFluidTankHandler(1000, false, (stack) -> stack.is(FluidsRegistry.SOURCE_VITALITY_FUEL_FLUID.get())) {
            @Override
            protected void onContentsChanged() {
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

    public static IFluidHandler getFluidHandlerCapability(CellBlockEntity blockEntity, Direction side){
        if(blockEntity.getBlockState().getValue(BlockStateProperties.ENABLED))
            return blockEntity.tankHandler;
        return null;
    }

    private CellData.Component getComponent(){
        return new CellData.Component(
                tankHandler.getFluid()
        );
    }

    private void setComponent(CellData.Component component){
        tankHandler.setFluid(component.tankFluid());
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
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider){
        super.saveAdditional(tag, provider);
        CellData.CODEC.encodeStart(NbtOps.INSTANCE, getComponent()).result().ifPresent(t -> {
            if(t instanceof CompoundTag compound) tag.merge(compound);
        });
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider){
        super.loadAdditional(tag, provider);
        CellData.CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag).result().ifPresent(this::setComponent);
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag(HolderLookup.@NotNull Provider provider){
        CompoundTag tag = super.getUpdateTag(provider);
        saveAdditional(tag, provider);
        return tag;
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookupProvider){
        super.handleUpdateTag(tag, lookupProvider);
        loadAdditional(tag, lookupProvider);
    }
}
