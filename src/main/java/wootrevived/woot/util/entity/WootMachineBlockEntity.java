package wootrevived.woot.util.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.blocks.dye_liquifier.DyeLiquifierBlockEntity;
import wootrevived.woot.client.render.dye_liquifier.DyeLiquifierContainerMenu;
import wootrevived.woot.network.WootMachineUpdate;
import wootrevived.woot.util.common.MachineSide;
import wootrevived.woot.util.common.MachineSideProperty;
import wootrevived.woot.util.common.RedstoneMode;
import wootrevived.woot.util.handlers.WootEnergyStorage;
import wootrevived.woot.util.handlers.WootFluidTankHandler;
import wootrevived.woot.util.handlers.WootItemStackHandler;
import wootrevived.woot.util.render.WootContainerData;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class WootMachineBlockEntity extends BlockEntity implements BlockEntityTicker<BlockEntity> {
    public static final int DATA_ENERGY = 0;
    public static final int DATA_PROGRESS = 1;
    public static final int DATA_FLUID_INPUT = 2;
    public static final int DATA_FLUID_OUTPUT = 3;
    public static final int DATA_ENERGY_TRANSFER = 4;
    public static final int DATA_LEFT_SECOND = 5;

    public WootContainerData data = new WootContainerData() {
        @Override
        public int get(int i) {
            if(i == DATA_ENERGY)
                return WootMachineBlockEntity.this.energyHandler.getEnergyStored();
            if(i == DATA_PROGRESS)
                return calculateProgress();
            if(i == DATA_ENERGY_TRANSFER)
                return getEnergyProcessTransfer();
            if(WootMachineBlockEntity.this instanceof DyeLiquifierBlockEntity be){
                if(i == DyeLiquifierContainerMenu.DATA_DYE_LIQUIFIER_RED)
                    return be.getRed();
                if(i == DyeLiquifierContainerMenu.DATA_DYE_LIQUIFIER_YELLOW)
                    return be.getYellow();
                if(i == DyeLiquifierContainerMenu.DATA_DYE_LIQUIFIER_BLUE)
                    return be.getBlue();
                if(i == DyeLiquifierContainerMenu.DATA_DYE_LIQUIFIER_WHITE)
                    return be.getWhite();
            }
            return 0;
        }

        @Override
        public float getFloat(int i) {
            if(i == DATA_LEFT_SECOND)
                return Math.max(0F, (float)processRemaining / (getEnergyProcessTransfer() * 20F));
            return 0;
        }

        @Override
        public FluidStack getFluid(int i){
            if(i == DATA_FLUID_INPUT)
                return WootMachineBlockEntity.this.inputTankHandler.getFluid();
            if(i == DATA_FLUID_OUTPUT)
                return WootMachineBlockEntity.this.outputTankHandler.getFluid();
            return FluidStack.EMPTY;
        }

        @Override
        public Direction getMachineFacing(){
            return WootMachineBlockEntity.this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        }

        @Override
        public RedstoneMode getRedstoneMode() {
            return WootMachineBlockEntity.this.redstoneMode;
        }

        @Override
        public EnumMap<MachineSide, MachineSideProperty> getMachineSideProperties(int index) {
            return WootMachineBlockEntity.this.getMachineSideProperties(index);
        }

        @Override
        public void setRedstoneMode(RedstoneMode mode) {
            WootMachineBlockEntity.this.redstoneMode = mode;
            WootMachineBlockEntity.this.sendNewState();
        }

        @Override
        public void setMachineSideProperties() {
            WootMachineBlockEntity.this.sendNewState();
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public void set(int i, int v) {

        }
    };

    public WootMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected RedstoneMode redstoneMode = RedstoneMode.ALWAYS_ON;
    public abstract EnumMap<MachineSide, MachineSideProperty> getMachineSideProperties(int index);
    public abstract List<EnumMap<MachineSide, MachineSideProperty>> getAllMachineSidesProperties();
    public abstract void setAllMachineSidesProperties(List<EnumMap<MachineSide, MachineSideProperty>> directionsProperties);

    private boolean isProcessActive = false;
    private int processMax = 0;
    private int processRemaining = 0;

    public boolean isProcessActive(){
        return this.isProcessActive;
    }

    @Override
    public void tick(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockEntity blockEntity) {
        if(level.isClientSide)
            return;

        boolean isDisabled = isDisabled();

        if(!isProcessActive) {
            if(isDisabled)
                return;

            if (!canProcess(true))
                return;

            processStart();
            processTick();
            return;
        }

        if(redstoneMode != RedstoneMode.ONCE && isDisabled){
            processOff();
            return;
        }

        processTick();

        if(canFinish()){
            processFinished();

            if(canProcess(true) && !isDisabled)
                processStart();
            else
                processOff();
            return;
        }

        if(!hasEnergy()) {
            processOff();
        }
    }

    protected abstract boolean canProcess(boolean checkEnergy);
    protected abstract void processFinished();

    protected abstract boolean hasEnergy();
    protected abstract int useEnergy();
    protected abstract void clearRecipe();
    protected abstract int getRecipeEnergy();

    private boolean lastRedstoneState = false;

    protected boolean isDisabled(){
        if(redstoneMode == RedstoneMode.ALWAYS_ON) return false;

        boolean current = level.hasNeighborSignal(getBlockPos());

        if(redstoneMode != RedstoneMode.ONCE)
            return (redstoneMode == RedstoneMode.WITH_SIGNAL) != current;

        boolean risingEdge = !lastRedstoneState && current;
        lastRedstoneState = current;
        return !risingEdge;
    }

    /**
     * If energy still needs to be consumed then do so
     * Returns the amount of energy used
     */
    private void processTick() {
        if (processRemaining <= 0)
            return;

        int energy = useEnergy();
        processRemaining -= energy;
        setChanged();
    }

    /**
     * Can finish if all the energy has been consumed and the inputs still give a valid recipe
     */
    private boolean canFinish() {
        return processRemaining <= 0 && canProcess(false);
    }

    public int calculateProgress() {
        return processMax == 0 ? 0 : 100 - (int)((100.0F / processMax) * processRemaining);
    }

    protected void processOff() {
        isProcessActive = false;
        processRemaining = 0;
        processMax = 0;
        setChanged();
        clearRecipe();
    }

    /**
     * Setup the required energy for the recipe
     */
    private void processStart() {
        isProcessActive = true;
        processMax = getRecipeEnergy();
        processRemaining = getRecipeEnergy();
    }

    public void onContentsChanged(int slot) {
        if (!isProcessActive)
            return;

        if (!canProcess(false))
            processOff();
    }

    public void dropContents(List<ItemStack> items) {
        for (ItemStack itemStack : items) {
            if (itemStack.isEmpty())
                continue;
            Containers.dropItemStack(level, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), itemStack);
        }
        setChanged();
    }

    public abstract IItemHandler getInventory();

    public final WootEnergyStorage energyHandler = createEnergy();
    private WootEnergyStorage createEnergy() {
        if(!hasEnergyCapability())
            return null;

        return new WootEnergyStorage(getEnergyCapacity(), getEnergyMaxTransfer()) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }

            @Override
            public boolean canReceive() {
                return true;
            }

            @Override
            public boolean canExtract() {
                return false;
            }
        };
    }

    public abstract int getEnergyCapacity();
    public abstract int getEnergyProcessTransfer();
    public abstract int getEnergyMaxTransfer();
    public abstract boolean hasEnergyCapability();

    public final WootFluidTankHandler inputTankHandler = createInputTank();
    private WootFluidTankHandler createInputTank() {
        if(!hasInputFluidCapability())
            return null;

        return new WootFluidTankHandler(getInputTankCapacity(), false, getInputFluidValidator()) {
            @Override
            protected void onContentsChanged() {
                setChanged();
            }
        };
    }

    public WootFluidTankHandler getInputTank() {
        return inputTankHandler;
    }

    public abstract Predicate<FluidStack> getInputFluidValidator();
    public abstract int getInputTankCapacity();
    public abstract boolean hasInputFluidCapability();

    protected void tickItem(WootItemStackHandler itemHandler, BlockPos pos, Function<Direction, MachineSideProperty> getProperty) {
        for(Direction side : Direction.values()){
            if(getProperty.apply(side) == MachineSideProperty.PUSH){
                IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(side), side.getOpposite());

                if(handler == null)
                    continue;

                ItemStack stack = itemHandler.getStackInSlot(0);
                if(stack.isEmpty())
                    return;
                ItemStack result = ItemHandlerHelper.insertItem(handler, stack, true);
                if(result.getCount() < stack.getCount()){
                    ItemStack extracted = itemHandler.extractItem(0, stack.getCount() - result.getCount(), false);
                    if(!extracted.isEmpty())
                        ItemHandlerHelper.insertItem(handler, extracted, false);
                }
            } else if(getProperty.apply(side) == MachineSideProperty.PULL && !itemHandler.isOutput()){
                IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(side), side.getOpposite());

                if(handler == null)
                    continue;

                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.getStackInSlot(i);
                    ItemStack result = itemHandler.insertItem(0, stack, true);
                    if(result.getCount() < stack.getCount()){
                        ItemStack extracted = handler.extractItem(i, stack.getCount() - result.getCount(), false);
                        if(!extracted.isEmpty()) {
                            itemHandler.insertItem(0, extracted, false);
                            return;
                        }
                    }
                }
            }
        }
    }

    protected void tickFluid(WootFluidTankHandler fluidTank, BlockPos pos, int tickRate, Function<Direction, MachineSideProperty> getProperty) {
        for(Direction side : Direction.values()){
            if(getProperty.apply(side) == MachineSideProperty.PUSH){
                IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, pos.relative(side), side.getOpposite());

                if(handler == null)
                    continue;

                FluidStack simulation = FluidUtil.tryFluidTransfer(handler, fluidTank, tickRate, false);
                if(!simulation.isEmpty())
                    FluidUtil.tryFluidTransfer(handler, fluidTank, simulation.getAmount(), true);
            } else if(getProperty.apply(side) == MachineSideProperty.PULL && !fluidTank.isOutput()){
                IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, pos.relative(side), side.getOpposite());

                if(handler == null)
                    continue;

                FluidStack simulation = FluidUtil.tryFluidTransfer(fluidTank, handler, tickRate, false);
                if(!simulation.isEmpty())
                    FluidUtil.tryFluidTransfer(fluidTank, handler, simulation.getAmount(), true);
            }
        }
    }

    public final WootFluidTankHandler outputTankHandler = createOutputTank();
    private WootFluidTankHandler createOutputTank() {
        if(!hasOutputFluidCapability())
            return null;

        return new WootFluidTankHandler(getOutputTankCapacity(), true) {
            @Override
            protected void onContentsChanged() {
                setChanged();
            }
        };
    }

    public WootFluidTankHandler getOutputTank() {
        return outputTankHandler;
    }

    public abstract int getOutputTankCapacity();
    public abstract boolean hasOutputFluidCapability();

    public static IEnergyStorage getEnergyStorageCapability(WootMachineBlockEntity blockEntity, Direction side){
        return blockEntity.energyHandler;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider){
        super.saveAdditional(tag, provider);

        long progress = ((long)processMax << 32) | (long) processRemaining;
        tag.putLong(WootTags.PROGRESS_TAG, progress);

        tag.putInt(WootTags.REDSTONE_MODE_TAG, redstoneMode.ordinal());
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider){
        super.loadAdditional(tag, provider);

        tag.getLong(WootTags.PROGRESS_TAG).ifPresent(progress -> {
            processMax = (int)(progress >> 32);
            processRemaining = progress.intValue();
        });


        if(processRemaining > 0){
            isProcessActive = true;
        }

        tag.getInt(WootTags.REDSTONE_MODE_TAG).ifPresent(mode -> redstoneMode = RedstoneMode.byIndex(mode));
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

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if(this.level == null || this.level.isClientSide) return;
        this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public void sendNewState(){
        PacketDistributor.sendToServer(new WootMachineUpdate(getBlockPos(), redstoneMode, getAllMachineSidesProperties()));
    }

    public void handleNewState(WootMachineUpdate update){
        if(update.redstoneMode() != null)
            redstoneMode = update.redstoneMode();

        if(update.listMachineProperties().size() == getAllMachineSidesProperties().size())
            setAllMachineSidesProperties(update.listMachineProperties());

        setChanged();
    }

    public boolean canPlayerAccess(ServerPlayer player) {
        return !(player.distanceToSqr(getBlockPos().getX() + 0.5,
                getBlockPos().getY() + 0.5,
                getBlockPos().getZ() + 0.5) > 64);
    }
}
