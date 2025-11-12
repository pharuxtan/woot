package wootrevived.woot.blocks.creative_tank;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.Woot;
import wootrevived.woot.data.CreativeTankData;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.ComponentsRegistry;
import wootrevived.woot.util.handlers.WootFluidResourceHandler;

public class CreativeTankBlockEntity extends BlockEntity implements BlockEntityTicker<BlockEntity> {
    public CreativeTankBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.CREATIVE_TANK_BLOCK_ENTITY.get(), pos, state);
    }

    public WootFluidResourceHandler inputTankHandler = createInputTank();

    private WootFluidResourceHandler createInputTank() {
        return new WootFluidResourceHandler(Integer.MAX_VALUE, false) {
            @Override
            protected void onContentsChanged(int i, FluidStack s) {
                if(!this.getStack().isEmpty()) stacks.set(0, this.getStack().copyWithAmount(Integer.MAX_VALUE));
                setChanged();
            }
        };
    }

    public static void ticker(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity){
        if(blockEntity instanceof CreativeTankBlockEntity creativeTankBlockEntity){
            creativeTankBlockEntity.tick(level, pos, state, blockEntity);
        }
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockEntity blockEntity) {
        if(inputTankHandler.isEmpty())
            return;

        for (Direction facing : Direction.values()) {
            ResourceHandler<FluidResource> handler = level.getCapability(Capabilities.Fluid.BLOCK, getBlockPos().relative(facing), facing.getOpposite());
            if(handler == null)
                continue;

            try (Transaction tx = Transaction.openRoot()) {
                handler.insert(FluidResource.of(inputTankHandler.getStack()), inputTankHandler.getCapacityAsInt(0, FluidResource.EMPTY), tx);
                tx.commit();
            }
        }
    }

    public void setMaxCapacity(){ inputTankHandler.setStack(inputTankHandler.getStack().copyWithAmount(Integer.MAX_VALUE)); }

    public void emptyIfDifferentFluidStack(FluidStack fluidStack){
        if(fluidStack.isEmpty())
            return;

        if(!FluidStack.isSameFluidSameComponents(inputTankHandler.getStack(), fluidStack))
            inputTankHandler.setStack(FluidStack.EMPTY);
    }

    public static ResourceHandler<FluidResource> getFluidHandlerCapability(CreativeTankBlockEntity blockEntity, Direction side){
        return blockEntity.inputTankHandler;
    }

    private CreativeTankData.Component getComponent(){
        return new CreativeTankData.Component(inputTankHandler.getStack());
    }

    private void setComponent(CreativeTankData.Component component){
        inputTankHandler.setStack(component.tankFluid());
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter getter){
        CreativeTankData.Component component = getter.get(ComponentsRegistry.CREATIVE_TANK_DATA);
        if(component == null)
            return;

        setComponent(component);
        setChanged();
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder){
        builder.set(ComponentsRegistry.CREATIVE_TANK_DATA, getComponent());
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output){
        super.saveAdditional(output);
        output.store(CreativeTankData.ID, CreativeTankData.CODEC, getComponent());
    }

    @Override
    public void loadAdditional(@NotNull ValueInput input){
        super.loadAdditional(input);
        input.read(CreativeTankData.ID, CreativeTankData.CODEC).ifPresent(this::setComponent);
    }

    private static final ProblemReporter.ScopedCollector REPORTER = Woot.reporter("CreativeTankBlockEntity");

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

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if(this.level == null || this.level.isClientSide()) return;
        this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }
}
