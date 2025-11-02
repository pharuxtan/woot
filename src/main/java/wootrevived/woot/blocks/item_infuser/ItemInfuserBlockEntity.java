package wootrevived.woot.blocks.item_infuser;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.Woot;
import wootrevived.woot.client.render.item_infuser.ItemInfuserContainerMenu;
import wootrevived.woot.config.ItemInfuserConfig;
import wootrevived.woot.data.ItemInfuserData;
import wootrevived.woot.recipes.item_infuser.ItemInfuserRecipe;
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

public class ItemInfuserBlockEntity extends WootMachineBlockEntity implements MenuProvider {
    private final List<EnumMap<MachineSide, MachineSideProperty>> directionsProperties = new ArrayList<>(4);

    public static final int INPUT_FLUID_PROPERTY = 0;
    public static final int INGREDIENT_PROPERTY = 1;
    public static final int AUGMENT_PROPERTY = 2;
    public static final int OUTPUT_PROPERTY = 3;

    public ItemInfuserBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.ITEM_INFUSER_BLOCK_ENTITY.get(), pos, state);
        for(int i = 0; i < 4; i++){
            EnumMap<MachineSide, MachineSideProperty> properties = Maps.newEnumMap(MachineSide.class);
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
        tickFluid(inputTankHandler, pos, ItemInfuserConfig.FLUID_TRANSFER.get(), side -> getProperties(side).getInputFluidProperty());
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

    private ItemInfuserData.Component getComponent(){
        return new ItemInfuserData.Component(
                energyHandler.getEnergyStored(),
                getInputTank().getFluid(),
                getAllMachineSidesProperties()
        );
    }

    private void setComponent(ItemInfuserData.Component component){
        energyHandler.setEnergy(component.energy());
        getInputTank().setFluid(component.inputFluid());
        setAllMachineSidesProperties(component.listMachineProperties());
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter getter){
        ItemInfuserData.Component component = getter.get(ComponentsRegistry.ITEM_INFUSER_DATA);
        if(component == null)
            return;

        setComponent(component);
        setChanged();
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder){
        builder.set(ComponentsRegistry.ITEM_INFUSER_DATA, getComponent());
    }

    private static final ProblemReporter.ScopedCollector REPORTER = Woot.reporter("ItemInfuserBlockEntity");

    protected ProblemReporter.ScopedCollector getReporter() {
        return REPORTER;
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output){
        super.saveAdditional(output);

        inputSlotHandler.serialize(output.child(WootTags.INPUT_INVENTORY_TAG));
        augmentSlotHandler.serialize(output.child(WootTags.AUGMENT_INVENTORY_TAG));
        outputSlotHandler.serialize(output.child(WootTags.OUTPUT_INVENTORY_TAG));

        output.store(ItemInfuserData.ID, ItemInfuserData.CODEC, getComponent());
    }

    @Override
    public void loadAdditional(@NotNull ValueInput input){
        super.loadAdditional(input);

        input.child(WootTags.INPUT_INVENTORY_TAG).ifPresent(inputSlotHandler::deserialize);
        input.child(WootTags.AUGMENT_INVENTORY_TAG).ifPresent(augmentSlotHandler::deserialize);
        input.child(WootTags.OUTPUT_INVENTORY_TAG).ifPresent(outputSlotHandler::deserialize);

        input.read(ItemInfuserData.ID, ItemInfuserData.CODEC).ifPresent(this::setComponent);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state){
        dropContents(level, pos);
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
        int ingredientAmount = recipe.ingredientCount(item.getItem());
        for(int i = 0; i < ingredientAmount; i++){
            ItemStack remainder = item.getCraftingRemainder();
            if(!remainder.isEmpty())
                item = remainder;
            else
                item.shrink(1);
        }
        inputSlotHandler.setStackInSlot(INPUT_SLOT, item);

        if (recipe.getAugment().isPresent()){
            ItemStack augment = augmentSlotHandler.getStackInSlot(AUGMENT_SLOT);
            int augmentAmount = recipe.augmentCount(item.getItem());
            for(int i = 0; i < augmentAmount; i++){
                ItemStack remainder = augment.getCraftingRemainder();
                if(!remainder.isEmpty())
                    augment = remainder;
                else
                    augment.shrink(1);
            }
            augmentSlotHandler.setStackInSlot(AUGMENT_SLOT, augment);
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
        if(!(level instanceof ServerLevel serverLevel))
            return;

        if (inputTankHandler.isEmpty() || inputTankHandler.getFluid().getFluid().getBucket() == null) {
            clearRecipe();
            return;
        }

        RecipeHolder<ItemInfuserRecipe> recipeHolder = serverLevel.recipeAccess().getRecipeFor(
                RecipesRegistry.ITEM_INFUSER_RECIPE_TYPE.get(),
                new WootRecipeInput(
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
