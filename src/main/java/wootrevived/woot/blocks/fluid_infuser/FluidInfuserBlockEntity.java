package wootrevived.woot.blocks.fluid_infuser;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.client.render.fluid_infuser.FluidInfuserContainerMenu;
import wootrevived.woot.config.FluidInfuserConfig;
import wootrevived.woot.data.FluidInfuserData;
import wootrevived.woot.recipes.fluid_infuser.FluidInfuserRecipe;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.ComponentsRegistry;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.common.MachineSide;
import wootrevived.woot.util.common.MachineSideProperty;
import wootrevived.woot.util.entity.WootMachineBlockEntity;
import wootrevived.woot.util.entity.WootTags;
import wootrevived.woot.util.handlers.WootFluidHandlerWrapper;
import wootrevived.woot.util.handlers.WootItemHandlerWrapper;
import wootrevived.woot.util.handlers.WootItemStackHandler;
import wootrevived.woot.util.recipes.WootRecipeInput;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Predicate;

public class FluidInfuserBlockEntity extends WootMachineBlockEntity implements MenuProvider {
    private final List<EnumMap<MachineSide, MachineSideProperty>> directionsProperties = new ArrayList<>(3);

    public static final int INPUT_FLUID_PROPERTY = 0;
    public static final int OUTPUT_FLUID_PROPERTY = 1;
    public static final int INGREDIENT_PROPERTY = 2;

    public FluidInfuserBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.FLUID_INFUSER_BLOCK_ENTITY.get(), pos, state);
        for(int i = 0; i < 3; i++){
            EnumMap<MachineSide, MachineSideProperty> properties = Maps.newEnumMap(MachineSide.class);
            for(MachineSide side : MachineSide.values()){
                properties.put(side, MachineSideProperty.ENABLED);
            }
            directionsProperties.add(properties);
        }
    }

    public static void ticker(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity){
        if(blockEntity instanceof FluidInfuserBlockEntity fluidInfuserBlockEntity){
            fluidInfuserBlockEntity.tick(level, pos, state, blockEntity);
        }
    }

    @Override
    public void tick(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);

        if(level.isClientSide)
            return;

        tickItem(inventoryHandler, pos, side -> getProperties(side).getIngredientProperty());
        tickFluid(inputTankHandler, pos, FluidInfuserConfig.FLUID_TRANSFER.get(), side -> getProperties(side).getInputFluidProperty());
        tickFluid(outputTankHandler, pos, FluidInfuserConfig.FLUID_TRANSFER.get(), side -> getProperties(side).getOutputFluidProperty());
    }

    public final WootItemStackHandler inventoryHandler = new WootItemStackHandler(false) {
        @Override
        protected void onContentsChanged(int slot) {
            FluidInfuserBlockEntity.this.onContentsChanged(slot);
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return FluidInfuserRecipe.Validator.isCatalystValid(stack);
        }
    };

    public static final int INPUT_SLOT = 0;
    public IItemHandler getInventory() { return inventoryHandler; }

    public record Properties(FluidInfuserBlockEntity entity, MachineSide machineSide){
        public MachineSideProperty getIngredientProperty(){
            return entity.directionsProperties.get(INGREDIENT_PROPERTY).get(machineSide);
        }

        public MachineSideProperty getInputFluidProperty(){
            return entity.directionsProperties.get(INPUT_FLUID_PROPERTY).get(machineSide);
        }

        public MachineSideProperty getOutputFluidProperty(){
            return entity.directionsProperties.get(OUTPUT_FLUID_PROPERTY).get(machineSide);
        }
    }

    private Properties getProperties(Direction side){
        Direction facing = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        return new Properties(this, MachineSide.getMachineSide(facing, side));
    }

    public static IItemHandler getItemHandlerCapability(FluidInfuserBlockEntity blockEntity, @Nullable Direction side){
        if(side == null)
            return blockEntity.inventoryHandler;

        Properties properties = blockEntity.getProperties(side);

        return new WootItemHandlerWrapper()
                .addHandler(blockEntity.inventoryHandler, properties::getIngredientProperty);
    }

    public static IFluidHandler getFluidHandlerCapability(FluidInfuserBlockEntity blockEntity, @Nullable Direction side){
        if(side == null)
            return new WootFluidHandlerWrapper()
                    .addHandler(blockEntity.inputTankHandler, () -> MachineSideProperty.ENABLED)
                    .addHandler(blockEntity.outputTankHandler, () -> MachineSideProperty.ENABLED);

        Properties properties = blockEntity.getProperties(side);

        return new WootFluidHandlerWrapper()
                .addHandler(blockEntity.outputTankHandler, properties::getOutputFluidProperty)
                .addHandler(blockEntity.inputTankHandler, properties::getInputFluidProperty);
    }

    private FluidInfuserData.Component getComponent(){
        return new FluidInfuserData.Component(
                energyHandler.getEnergyStored(),
                getInputTank().getFluid(),
                getOutputTank().getFluid(),
                getAllMachineSidesProperties()
        );
    }

    private void setComponent(FluidInfuserData.Component component){
        energyHandler.setEnergy(component.energy());
        getInputTank().setFluid(component.inputFluid());
        getOutputTank().setFluid(component.outputFluid());
        setAllMachineSidesProperties(component.listMachineProperties());
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter getter){
        FluidInfuserData.Component component = getter.get(ComponentsRegistry.FLUID_INFUSER_DATA);
        if(component == null)
            return;

        setComponent(component);
        setChanged();
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder){
        builder.set(ComponentsRegistry.FLUID_INFUSER_DATA, getComponent());
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider){
        super.saveAdditional(tag, provider);

        tag.put(WootTags.INPUT_INVENTORY_TAG, inventoryHandler.serializeNBT(provider));

        FluidInfuserData.CODEC.encodeStart(NbtOps.INSTANCE, getComponent()).result().ifPresent(t -> {
            if(t instanceof CompoundTag compound) tag.merge(compound);
        });
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider){
        super.loadAdditional(tag, provider);

        tag.getCompound(WootTags.INPUT_INVENTORY_TAG).ifPresent(handler -> inventoryHandler.deserializeNBT(provider, handler));

        FluidInfuserData.CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag).result().ifPresent(this::setComponent);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state){
        dropContents(level, pos);
    }

    public void dropContents(Level level, BlockPos pos) {
        List<ItemStack> drops = new ArrayList<>();
        ItemStack itemStack = inventoryHandler.getStackInSlot(INPUT_SLOT);
        if (!itemStack.isEmpty()) {
            drops.add(itemStack);
            inventoryHandler.insertItem(INPUT_SLOT, ItemStack.EMPTY, false);
        }
        super.dropContents(drops);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("gui.woot_revived.fluid_infuser.name");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new FluidInfuserContainerMenu(containerId, level, getBlockPos(), playerInventory, player);
    }

    @Override
    public EnumMap<MachineSide, MachineSideProperty> getMachineSideProperties(int index) {
        return directionsProperties.get(index);
    }

    @Override
    public List<EnumMap<MachineSide, MachineSideProperty>> getAllMachineSidesProperties() {
        return directionsProperties;
    }

    @Override
    public void setAllMachineSidesProperties(List<EnumMap<MachineSide, MachineSideProperty>> directionsProperties){
        for(int i = 0; i < directionsProperties.size(); i++){
            this.directionsProperties.set(i, directionsProperties.get(i));
        }
    }

    private FluidInfuserRecipe recipe = null;

    @Override
    protected boolean hasEnergy() { return energyHandler.getEnergyStored() > 0; }

    @Override
    protected int useEnergy(){
        return energyHandler.internalExtractEnergy(getEnergyProcessTransfer(), false);
    }

    @Override
    protected void clearRecipe() {
        recipe = null;
    }

    @Override
    protected int getRecipeEnergy() {
        return recipe != null ? recipe.getEnergy() : 0;
    }

    @Override
    protected void processFinished() {
        if (recipe == null)
            getRecipe();

        if (recipe == null) {
            processOff();
            return;
        }

        FluidInfuserRecipe recipe = this.recipe;

        ItemStack item = inventoryHandler.getStackInSlot(INPUT_SLOT);
        int ingredientAmount = recipe.ingredientCount(item.getItem());
        for(int i = 0; i < ingredientAmount; i++){
            ItemStack remainder = item.getCraftingRemainder();
            if(!remainder.isEmpty())
                item = remainder;
            else
                item.shrink(1);
        }
        inventoryHandler.setStackInSlot(INPUT_SLOT, item);

        inputTankHandler.drain(recipe.getInputFluid().getAmount(), IFluidHandler.FluidAction.EXECUTE);

        outputTankHandler.fill(recipe.getOutputFluid(), IFluidHandler.FluidAction.EXECUTE);
        setChanged();
    }

    @Override
    protected boolean canProcess(boolean checkEnergy) {
        if (checkEnergy && energyHandler.getEnergyStored() <= 0)
            return false;

        getRecipe();
        if (recipe == null)
            return false;

        if(!outputTankHandler.isEmpty() && outputTankHandler.getFluid().getFluid() != recipe.getOutputFluid().getFluid())
            return false;

        if(outputTankHandler.getFluidAmount() + recipe.getOutputFluid().getAmount() > outputTankHandler.getCapacity())
            return false;

        return inputTankHandler.getFluid().getAmount() >= recipe.getInputFluid().getAmount();
    }
    //endregion

    private void getRecipe() {
        if(!(level instanceof ServerLevel serverLevel))
            return;

        clearRecipe();

        FluidStack inFluid = inputTankHandler.getFluid();
        if (inFluid.isEmpty()) {
            clearRecipe();
            return;
        }

        ItemStack catalyst = inventoryHandler.getStackInSlot(INPUT_SLOT);
        if (catalyst.isEmpty()) {
            clearRecipe();
            return;
        }

        RecipeHolder<FluidInfuserRecipe> recipeHolder = serverLevel.recipeAccess().getRecipeFor(
                RecipesRegistry.FLUID_INFUSER_RECIPE_TYPE.get(),
                new WootRecipeInput(
                        Either.right(inputTankHandler.getFluid()),
                        Either.left(inventoryHandler.getStackInSlot(INPUT_SLOT))
                ), level).orElse(null);

        recipe = recipeHolder == null ? null : recipeHolder.value();
    }

    public int getEnergyCapacity(){
        return FluidInfuserConfig.ENERGY_CAPACITY.get();
    }

    public int getEnergyMaxTransfer(){
        return FluidInfuserConfig.ENERGY_MAX_TRANSFER.get();
    }

    public int getEnergyProcessTransfer(){
        return FluidInfuserConfig.ENERGY_PROCESS_TRANSFER.get();
    }

    public boolean hasEnergyCapability() {
        return true;
    }

    public int getInputTankCapacity() {
        return FluidInfuserConfig.INPUT_TANK_CAPACITY.get();
    }

    public boolean hasInputFluidCapability() {
        return true;
    }

    public Predicate<FluidStack> getInputFluidValidator() {
        return FluidInfuserRecipe.Validator::isFluidValid;
    }

    public int getOutputTankCapacity() {
        return FluidInfuserConfig.OUTPUT_TANK_CAPACITY.get();
    }

    public boolean hasOutputFluidCapability() {
        return true;
    }
}
