package wootrevived.woot.blocks.item_infuser;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.client.render.item_infuser.ItemInfuserContainerMenu;
import wootrevived.woot.config.ItemInfuserConfig;
import wootrevived.woot.recipes.item_infuser.ItemInfuserRecipe;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.common.MachineSide;
import wootrevived.woot.util.common.MachineSideProperty;
import wootrevived.woot.util.entity.WootMachineBlockEntity;
import wootrevived.woot.util.entity.WootTags;
import wootrevived.woot.util.handlers.WootFluidHandlerWrapper;
import wootrevived.woot.util.handlers.WootItemHandlerWrapper;
import wootrevived.woot.util.handlers.WootItemStackHandler;
import wootrevived.woot.util.recipes.WootContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ItemInfuserBlockEntity extends WootMachineBlockEntity implements MenuProvider {
    private final List<Map<MachineSide, MachineSideProperty>> directionsProperties = new ArrayList<>(4);

    public static final int INPUT_FLUID_PROPERTY = 0;
    public static final int INGREDIENT_PROPERTY = 1;
    public static final int AUGMENT_PROPERTY = 2;
    public static final int OUTPUT_PROPERTY = 3;

    public ItemInfuserBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.ITEM_INFUSER_BLOCK_ENTITY.get(), pos, state);
        for(int i = 0; i < 4; i++){
            Map<MachineSide, MachineSideProperty> properties = Maps.newEnumMap(MachineSide.class);
            for(MachineSide side : MachineSide.values()){
                properties.put(side, MachineSideProperty.ENABLED);
            }
            directionsProperties.add(properties);
        }
    }

    public static void ticker(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity){
        if(blockEntity instanceof ItemInfuserBlockEntity itemInfuserBlockEntity){
            itemInfuserBlockEntity.tick(level, pos, state, blockEntity);
        }
    }

    @Override
    public void tick(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);

        if(level.isClientSide)
            return;

        tickItem(inputSlotHandler, pos, side -> getProperties(side).getIngredientProperty());
        tickItem(augmentSlotHandler, pos, side -> getProperties(side).getAugmentProperty());
        tickItem(outputSlotHandler, pos, side -> getProperties(side).getOutputProperty());
        tickFluid(inputTankHandler, pos, ItemInfuserConfig.FLUID_TRANSFER.get(),side -> getProperties(side).getInputFluidProperty());
    }

    public static final int INPUT_SLOT = 0;
    public static final int AUGMENT_SLOT = 0;
    public static final int OUTPUT_SLOT = 0;
    private final WootItemStackHandler inputSlotHandler = new WootItemStackHandler(false) {
        @Override
        protected void onContentsChanged(int slot) {
            ItemInfuserBlockEntity.this.onContentsChanged(slot);
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return ItemInfuserRecipe.Validator.isIngredientValid(stack);
        }
    };
    private final WootItemStackHandler augmentSlotHandler = new WootItemStackHandler(false) {
        @Override
        protected void onContentsChanged(int slot) {
            ItemInfuserBlockEntity.this.onContentsChanged(slot);
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return ItemInfuserRecipe.Validator.isAugmentValid(stack);
        }
    };
    private final WootItemStackHandler outputSlotHandler = new WootItemStackHandler(true) {
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate){
            return stack;
        }
    };
    private final IItemHandler allSlotsHandler = new CombinedInvWrapper(inputSlotHandler, augmentSlotHandler, outputSlotHandler);

    public IItemHandler getInventory() { return allSlotsHandler; }

    public record Properties(ItemInfuserBlockEntity entity, MachineSide machineSide){
        public MachineSideProperty getInputFluidProperty() {
            return entity.directionsProperties.get(INPUT_FLUID_PROPERTY).get(machineSide);
        }

        public MachineSideProperty getIngredientProperty(){
            return entity.directionsProperties.get(INGREDIENT_PROPERTY).get(machineSide);
        }

        public MachineSideProperty getAugmentProperty(){
            return entity.directionsProperties.get(AUGMENT_PROPERTY).get(machineSide);
        }

        public MachineSideProperty getOutputProperty(){
            return entity.directionsProperties.get(OUTPUT_PROPERTY).get(machineSide);
        }
    }

    private Properties getProperties(Direction side){
        Direction facing = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        return new Properties(this, MachineSide.getMachineSide(facing, side));
    }

    public static IItemHandler getItemHandlerCapability(ItemInfuserBlockEntity blockEntity, @Nullable Direction side){
        if(side == null)
            return blockEntity.allSlotsHandler;

        Properties properties = blockEntity.getProperties(side);

        return new WootItemHandlerWrapper()
                .addHandler(blockEntity.outputSlotHandler, properties::getOutputProperty)
                .addHandler(blockEntity.augmentSlotHandler, properties::getAugmentProperty)
                .addHandler(blockEntity.inputSlotHandler, properties::getIngredientProperty);
    }

    public static IFluidHandler getFluidHandlerCapability(ItemInfuserBlockEntity blockEntity, @Nullable Direction side){
        if(side == null)
            return blockEntity.inputTankHandler;

        Properties properties = blockEntity.getProperties(side);

        return new WootFluidHandlerWrapper()
                .addHandler(blockEntity.inputTankHandler, properties::getInputFluidProperty);
    }

    @Override
    public void load(@NotNull CompoundTag tag){
        super.load(tag);

        if(tag.contains(WootTags.INPUT_INVENTORY_TAG))
            inputSlotHandler.deserializeNBT(tag.getCompound(WootTags.INPUT_INVENTORY_TAG));

        if(tag.contains(WootTags.AUGMENT_INVENTORY_TAG))
            augmentSlotHandler.deserializeNBT(tag.getCompound(WootTags.AUGMENT_INVENTORY_TAG));

        if(tag.contains(WootTags.OUTPUT_INVENTORY_TAG))
            outputSlotHandler.deserializeNBT(tag.getCompound(WootTags.OUTPUT_INVENTORY_TAG));
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag){
        super.saveAdditional(tag);

        tag.put(WootTags.INPUT_INVENTORY_TAG, inputSlotHandler.serializeNBT());
        tag.put(WootTags.AUGMENT_INVENTORY_TAG, augmentSlotHandler.serializeNBT());

        tag.put(WootTags.OUTPUT_INVENTORY_TAG, outputSlotHandler.serializeNBT());
    }

    public void dropContents(Level level, BlockPos pos) {
        List<ItemStack> drops = new ArrayList<>();
        ItemStack itemStack = inputSlotHandler.getStackInSlot(INPUT_SLOT);
        if (!itemStack.isEmpty()) {
            drops.add(itemStack);
            inputSlotHandler.setStackInSlot(INPUT_SLOT, ItemStack.EMPTY);
        }

        itemStack = augmentSlotHandler.getStackInSlot(AUGMENT_SLOT);
        if (!itemStack.isEmpty()) {
            drops.add(itemStack);
            augmentSlotHandler.setStackInSlot(AUGMENT_SLOT, ItemStack.EMPTY);
        }

        itemStack = outputSlotHandler.getStackInSlot(OUTPUT_SLOT);
        if (!itemStack.isEmpty()) {
            drops.add(itemStack);
            outputSlotHandler.setStackInSlot(OUTPUT_SLOT, ItemStack.EMPTY);
        }
        super.dropContents(drops);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("gui.woot_revived.item_infuser.name");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new ItemInfuserContainerMenu(containerId, level, getBlockPos(), playerInventory, player);
    }

    @Override
    public Map<MachineSide, MachineSideProperty> getMachineSideProperties(int index) {
        return directionsProperties.get(index);
    }

    @Override
    public List<Map<MachineSide, MachineSideProperty>> getAllMachineSidesProperties() {
        return directionsProperties;
    }

    @Override
    public void setAllMachineSidesProperties(List<Map<MachineSide, MachineSideProperty>> directionsProperties){
        for(int i = 0; i < directionsProperties.size(); i++){
            this.directionsProperties.set(i, directionsProperties.get(i));
        }
    }

    private ItemInfuserRecipe recipe = null;

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

        ItemInfuserRecipe recipe = this.recipe;

        ItemStack item = inputSlotHandler.getStackInSlot(INPUT_SLOT);
        if(item.getItem().hasCraftingRemainingItem(item)){
            inputSlotHandler.setStackInSlot(INPUT_SLOT, item.getItem().getCraftingRemainingItem(item));
        } else {
            int inputSize = recipe.ingredientCount(inputSlotHandler.getStackInSlot(INPUT_SLOT).getItem());
            inputSlotHandler.extractItem(INPUT_SLOT, inputSize, false);
        }

        if (recipe.getAugment().isPresent()){
            ItemStack augment = augmentSlotHandler.getStackInSlot(AUGMENT_SLOT);
            if(augment.getItem().hasCraftingRemainingItem(augment)){
                augmentSlotHandler.setStackInSlot(AUGMENT_SLOT, augment.getItem().getCraftingRemainingItem(augment));
            } else {
                int augmentSize = recipe.augmentCount(augmentSlotHandler.getStackInSlot(AUGMENT_SLOT).getItem());
                augmentSlotHandler.extractItem(AUGMENT_SLOT, augmentSize, false);
            }
        }

        ItemStack itemStack = recipe.getOutput();
        ItemStack output = outputSlotHandler.getStackInSlot(OUTPUT_SLOT);
        if(!output.isEmpty()){
            output.grow(itemStack.getCount());
        } else {
            outputSlotHandler.setStackInSlot(OUTPUT_SLOT, itemStack);
        }

        inputTankHandler.drain(recipe.getFluid().getAmount(), IFluidHandler.FluidAction.EXECUTE);
        setChanged();
    }

    @Override
    protected boolean canProcess(boolean checkEnergy) {
        if (checkEnergy && energyHandler.getEnergyStored() <= 0)
            return false;

        getRecipe();
        if (recipe == null)
            return false;

        ItemStack output = outputSlotHandler.getStackInSlot(OUTPUT_SLOT);
        if(!output.isEmpty()){
            if(output.getItem() != recipe.getOutput().getItem())
                return false;

            if(output.getCount() + recipe.getOutput().getCount() > output.getMaxStackSize())
                return false;
        }

        return inputTankHandler.getFluid().getAmount() >= recipe.getFluid().getAmount();
    }
    //endregion

    private void getRecipe() {
        if (inputTankHandler.isEmpty() || inputTankHandler.getFluid().getFluid().getBucket() == null) {
            clearRecipe();
            return;
        }

        RecipeHolder<ItemInfuserRecipe> recipeHolder = level.getRecipeManager().getRecipeFor(
                RecipesRegistry.ITEM_INFUSER_RECIPE_TYPE.get(),
                new WootContainer(
                        Either.right(inputTankHandler.getFluid()),
                        Either.left(inputSlotHandler.getStackInSlot(INPUT_SLOT)),
                        Either.left(augmentSlotHandler.getStackInSlot(AUGMENT_SLOT))),
                level).orElse(null);

        recipe = recipeHolder == null ? null : recipeHolder.value();
    }

    public int getEnergyCapacity(){
        return ItemInfuserConfig.ENERGY_CAPACITY.get();
    }

    public int getEnergyMaxTransfer(){
        return ItemInfuserConfig.ENERGY_MAX_TRANSFER.get();
    }

    public int getEnergyProcessTransfer(){
        return ItemInfuserConfig.ENERGY_PROCESS_TRANSFER.get();
    }

    public boolean hasEnergyCapability() {
        return true;
    }

    public int getInputTankCapacity() {
        return ItemInfuserConfig.INPUT_TANK_CAPACITY.get();
    }

    public boolean hasInputFluidCapability() {
        return true;
    }

    public Predicate<FluidStack> getInputFluidValidator() {
        return ItemInfuserRecipe.Validator::isFluidValid;
    }

    public int getOutputTankCapacity() {
        return 0;
    }

    public boolean hasOutputFluidCapability() {
        return false;
    }
}
