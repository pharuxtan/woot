package wootrevived.woot.blocks.dye_liquifier;

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
import net.minecraft.util.Mth;
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
import wootrevived.woot.client.render.dye_liquifier.DyeLiquifierContainerMenu;
import wootrevived.woot.config.DyeLiquifierConfig;
import wootrevived.woot.data.DyeLiquifierData;
import wootrevived.woot.recipes.dye_liquifier.DyeLiquifierRecipe;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.ComponentsRegistry;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.common.MachineSide;
import wootrevived.woot.util.common.MachineSideProperty;
import wootrevived.woot.util.entity.WootMachineBlockEntity;
import wootrevived.woot.util.entity.WootTags;
import wootrevived.woot.util.handlers.WootFluidHandlerWrapper;
import wootrevived.woot.util.handlers.WootItemHandlerWrapper;
import wootrevived.woot.util.handlers.WootItemStackHandler;
import wootrevived.woot.util.recipes.WootRecipeInput;

import java.util.*;
import java.util.function.Predicate;

public class DyeLiquifierBlockEntity extends WootMachineBlockEntity implements MenuProvider {
    private int red = 0;
    private int yellow = 0;
    private int blue = 0;
    private int white = 0;

    private final List<EnumMap<MachineSide, MachineSideProperty>> directionsProperties = new ArrayList<>(5);

    public static final int OUTPUT_FLUID_PROPERTY = 0;
    public static final int RED_INGREDIENT_PROPERTY = 1;
    public static final int YELLOW_INGREDIENT_PROPERTY = 2;
    public static final int BLUE_INGREDIENT_PROPERTY = 3;
    public static final int WHITE_INGREDIENT_PROPERTY = 4;

    public DyeLiquifierBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.DYE_LIQUIFIER_BLOCK_ENTITY.get(), pos, state);
        for(int i = 0; i < 5; i++){
            EnumMap<MachineSide, MachineSideProperty> properties = Maps.newEnumMap(MachineSide.class);
            for(MachineSide side : MachineSide.values()){
                properties.put(side, MachineSideProperty.ENABLED);
            }
            directionsProperties.add(properties);
        }
    }

    public static void ticker(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity){
        if(blockEntity instanceof DyeLiquifierBlockEntity dyeLiquifierBlockEntity){
            dyeLiquifierBlockEntity.tick(level, pos, state, blockEntity);
        }
    }

    @Override
    public void tick(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);

        if(level.isClientSide)
            return;

        generatePureFluid();
        tickFluid(outputTankHandler, pos, DyeLiquifierConfig.FLUID_TRANSFER.get(), side -> getProperties(side).getOutputFluidProperty());
    }

    public final WootItemStackHandler redInventoryHandler = new WootItemStackHandler(false) {
        @Override
        protected void onContentsChanged(int slot) {
            DyeLiquifierBlockEntity.this.onContentsChanged(slot);
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return DyeLiquifierRecipe.Validator.isIngredientValid(stack, DyeLiquifierRecipe.Colors.RED);
        }
    };

    public final WootItemStackHandler yellowInventoryHandler = new WootItemStackHandler(false) {
        @Override
        protected void onContentsChanged(int slot) {
            DyeLiquifierBlockEntity.this.onContentsChanged(slot);
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return DyeLiquifierRecipe.Validator.isIngredientValid(stack, DyeLiquifierRecipe.Colors.YELLOW);
        }
    };

    public final WootItemStackHandler blueInventoryHandler = new WootItemStackHandler(false) {
        @Override
        protected void onContentsChanged(int slot) {
            DyeLiquifierBlockEntity.this.onContentsChanged(slot);
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return DyeLiquifierRecipe.Validator.isIngredientValid(stack, DyeLiquifierRecipe.Colors.BLUE);
        }
    };

    public final WootItemStackHandler whiteInventoryHandler = new WootItemStackHandler(false) {
        @Override
        protected void onContentsChanged(int slot) {
            DyeLiquifierBlockEntity.this.onContentsChanged(slot);
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return DyeLiquifierRecipe.Validator.isIngredientValid(stack, DyeLiquifierRecipe.Colors.WHITE);
        }
    };

    public static int INPUT_SLOT = 0;
    private final IItemHandler allSlotsHandler = new CombinedInvWrapper(redInventoryHandler, yellowInventoryHandler, blueInventoryHandler, whiteInventoryHandler);
    public IItemHandler getInventory() { return allSlotsHandler; }

    public record Properties(DyeLiquifierBlockEntity entity, MachineSide machineSide){
        public MachineSideProperty getRedIngredientProperty(){
            return entity.directionsProperties.get(RED_INGREDIENT_PROPERTY).get(machineSide);
        }

        public MachineSideProperty getYellowIngredientProperty(){
            return entity.directionsProperties.get(YELLOW_INGREDIENT_PROPERTY).get(machineSide);
        }

        public MachineSideProperty getBlueIngredientProperty(){
            return entity.directionsProperties.get(BLUE_INGREDIENT_PROPERTY).get(machineSide);
        }

        public MachineSideProperty getWhiteIngredientProperty(){
            return entity.directionsProperties.get(WHITE_INGREDIENT_PROPERTY).get(machineSide);
        }

        public MachineSideProperty getOutputFluidProperty(){
            return entity.directionsProperties.get(OUTPUT_FLUID_PROPERTY).get(machineSide);
        }
    }

    private Properties getProperties(Direction side){
        Direction facing = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        return new Properties(this, MachineSide.getMachineSide(facing, side));
    }

    public static IItemHandler getItemHandlerCapability(DyeLiquifierBlockEntity blockEntity, Direction side){
        Properties properties = blockEntity.getProperties(side);

        return new WootItemHandlerWrapper()
                .addHandler(blockEntity.redInventoryHandler, properties::getRedIngredientProperty)
                .addHandler(blockEntity.blueInventoryHandler, properties::getBlueIngredientProperty)
                .addHandler(blockEntity.yellowInventoryHandler, properties::getYellowIngredientProperty)
                .addHandler(blockEntity.whiteInventoryHandler, properties::getWhiteIngredientProperty);
    }

    public static IFluidHandler getFluidHandlerCapability(DyeLiquifierBlockEntity blockEntity, Direction side){
        Properties properties = blockEntity.getProperties(side);

        return new WootFluidHandlerWrapper()
                .addHandler(blockEntity.outputTankHandler, properties::getOutputFluidProperty);
    }

    private DyeLiquifierData.Component getComponent(){
        return new DyeLiquifierData.Component(
                energyHandler.getEnergyStored(),
                getRed(),
                getYellow(),
                getBlue(),
                getWhite(),
                getOutputTank().getFluid(),
                getAllMachineSidesProperties()
        );
    }

    private void setComponent(DyeLiquifierData.Component component){
        energyHandler.setEnergy(component.energy());
        red = component.red();
        yellow = component.yellow();
        blue = component.blue();
        white = component.white();
        getOutputTank().setFluid(component.outputFluid());
        // Make properties loading compatible with the previous only 2 properties stored info
        List<EnumMap<MachineSide, MachineSideProperty>> propertiesList = component.listMachineProperties();
        for(int i = 0; i < propertiesList.size(); i++){
            Map<MachineSide, MachineSideProperty> properties = directionsProperties.get(i);
            Map<MachineSide, MachineSideProperty> componentProperties = propertiesList.get(i);
            for(MachineSide side : MachineSide.values())
                properties.put(side, componentProperties.get(side));
        }
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter getter){
        DyeLiquifierData.Component component = getter.get(ComponentsRegistry.DYE_LIQUIFIER_DATA);
        if(component == null)
            return;

        setComponent(component);
        setChanged();
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder){
        builder.set(ComponentsRegistry.DYE_LIQUIFIER_DATA, getComponent());
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider){
        super.saveAdditional(tag, provider);

        tag.put(WootTags.DyeLiquifier.RED_INVENTORY_TAG, redInventoryHandler.serializeNBT(provider));
        tag.put(WootTags.DyeLiquifier.YELLOW_INVENTORY_TAG, yellowInventoryHandler.serializeNBT(provider));
        tag.put(WootTags.DyeLiquifier.BLUE_INVENTORY_TAG, blueInventoryHandler.serializeNBT(provider));
        tag.put(WootTags.DyeLiquifier.WHITE_INVENTORY_TAG, whiteInventoryHandler.serializeNBT(provider));

        DyeLiquifierData.CODEC.encodeStart(NbtOps.INSTANCE, getComponent()).result().ifPresent(t -> {
            if(t instanceof CompoundTag compound) tag.merge(compound);
        });
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider){
        super.loadAdditional(tag, provider);

        tag.getCompound(WootTags.DyeLiquifier.RED_INVENTORY_TAG).ifPresent(handler -> redInventoryHandler.deserializeNBT(provider, handler));
        tag.getCompound(WootTags.DyeLiquifier.YELLOW_INVENTORY_TAG).ifPresent(handler -> yellowInventoryHandler.deserializeNBT(provider, handler));
        tag.getCompound(WootTags.DyeLiquifier.BLUE_INVENTORY_TAG).ifPresent(handler -> blueInventoryHandler.deserializeNBT(provider, handler));
        tag.getCompound(WootTags.DyeLiquifier.WHITE_INVENTORY_TAG).ifPresent(handler -> whiteInventoryHandler.deserializeNBT(provider, handler));

        DyeLiquifierData.CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag).result().ifPresent(this::setComponent);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state){
        dropContents(level, pos);
    }

    public void dropContents(Level level, BlockPos pos) {
        List<ItemStack> drops = new ArrayList<>();

        ItemStack itemStack = redInventoryHandler.getStackInSlot(INPUT_SLOT).copy();
        if (!itemStack.isEmpty()) {
            drops.add(itemStack);
            redInventoryHandler.insertItem(INPUT_SLOT, ItemStack.EMPTY, false);
        }

        itemStack = yellowInventoryHandler.getStackInSlot(INPUT_SLOT).copy();
        if (!itemStack.isEmpty()) {
            drops.add(itemStack);
            yellowInventoryHandler.insertItem(INPUT_SLOT, ItemStack.EMPTY, false);
        }

        itemStack = blueInventoryHandler.getStackInSlot(INPUT_SLOT).copy();
        if (!itemStack.isEmpty()) {
            drops.add(itemStack);
            blueInventoryHandler.insertItem(INPUT_SLOT, ItemStack.EMPTY, false);
        }

        itemStack = whiteInventoryHandler.getStackInSlot(INPUT_SLOT).copy();
        if (!itemStack.isEmpty()) {
            drops.add(itemStack);
            whiteInventoryHandler.insertItem(INPUT_SLOT, ItemStack.EMPTY, false);
        }

        super.dropContents(drops);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("gui.woot_revived.dye_liquifier.name");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new DyeLiquifierContainerMenu(containerId, level, getBlockPos(), playerInventory, player);
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

    private final Map<Integer, DyeLiquifierRecipe> recipes = new HashMap<>();

    @Override
    protected boolean hasEnergy() { return energyHandler.getEnergyStored() > 0; }

    @Override
    protected int useEnergy(){
        return energyHandler.internalExtractEnergy(getEnergyProcessTransfer(), false);
    }

    @Override
    protected void clearRecipe() {
        recipes.clear();
    }

    @Override
    protected int getRecipeEnergy() {
        return recipes.values().stream().mapToInt(DyeLiquifierRecipe::getEnergy).sum();
    }

    private void generatePureFluid() {
        while (canCreateOutput() && canStoreOutput()) {
            outputTankHandler.fill(new FluidStack(FluidsRegistry.SOURCE_PURE_DYE_FLUID.get(), DyeLiquifierConfig.PURE_DYE_PRODUCE_AMOUNT.get()), IFluidHandler.FluidAction.EXECUTE);
            red -= DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get();
            yellow -= DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get();
            blue -= DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get();
            white -= DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get();
            setChanged();
        }
    }

    @Override
    protected void processFinished() {
        if(recipes.values().stream().allMatch(Objects::isNull))
            getRecipe();
        if (recipes.values().stream().allMatch(Objects::isNull)) {
            processOff();
            return;
        }

        for(DyeLiquifierRecipe recipe : this.recipes.values()){
            red += recipe.getRed();
            yellow += recipe.getYellow();
            blue += recipe.getBlue();
            white += recipe.getWhite();
        }

        red = Mth.clamp(red, 0, DyeLiquifierConfig.RED_TANK_CAPACITY.get());
        yellow = Mth.clamp(yellow, 0, DyeLiquifierConfig.YELLOW_TANK_CAPACITY.get());
        blue = Mth.clamp(blue, 0, DyeLiquifierConfig.BLUE_TANK_CAPACITY.get());
        white = Mth.clamp(white, 0, DyeLiquifierConfig.WHITE_TANK_CAPACITY.get());

        if(this.recipes.containsKey(0)){
            ItemStack item = redInventoryHandler.getStackInSlot(INPUT_SLOT);
            int ingredientAmount = recipes.get(0).ingredientCount(item.getItem());
            for(int i = 0; i < ingredientAmount; i++){
                ItemStack remainder = item.getCraftingRemainder();
                if(!remainder.isEmpty())
                    item = remainder;
                else
                    item.shrink(1);
            }
            redInventoryHandler.setStackInSlot(INPUT_SLOT, item);
        }

        if(this.recipes.containsKey(1)){
            ItemStack item = yellowInventoryHandler.getStackInSlot(INPUT_SLOT);
            int ingredientAmount = recipes.get(1).ingredientCount(item.getItem());
            for (int i = 0; i < ingredientAmount; i++) {
                ItemStack remainder = item.getCraftingRemainder();
                if(!remainder.isEmpty())
                    item = remainder;
                else
                    item.shrink(1);
            }
            yellowInventoryHandler.setStackInSlot(INPUT_SLOT, item);
        }

        if(this.recipes.containsKey(2)){
            ItemStack item = blueInventoryHandler.getStackInSlot(INPUT_SLOT);
            int ingredientAmount = recipes.get(2).ingredientCount(item.getItem());
            for (int i = 0; i < ingredientAmount; i++) {
                ItemStack remainder = item.getCraftingRemainder();
                if(!remainder.isEmpty())
                    item = remainder;
                else
                    item.shrink(1);
            }
            blueInventoryHandler.setStackInSlot(INPUT_SLOT, item);
        }

        if(this.recipes.containsKey(3)){
            ItemStack item = whiteInventoryHandler.getStackInSlot(INPUT_SLOT);
            int ingredientAmount = recipes.get(3).ingredientCount(item.getItem());
            for (int i = 0; i < ingredientAmount; i++) {
                ItemStack remainder = item.getCraftingRemainder();
                if(!remainder.isEmpty())
                    item = remainder;
                else
                    item.shrink(1);
            }
            whiteInventoryHandler.setStackInSlot(INPUT_SLOT, item);
        }

        setChanged();
    }

    @Override
    protected boolean canProcess(boolean checkEnergy) {
        if (checkEnergy && energyHandler.getEnergyStored() <= 0)
            return false;

        getRecipe();
        if(recipes.values().stream().allMatch(Objects::isNull))
            return false;

        int red = 0, yellow = 0, blue = 0, white = 0;
        for(DyeLiquifierRecipe recipe : this.recipes.values()){
            red += recipe.getRed();
            yellow += recipe.getYellow();
            blue += recipe.getBlue();
            white += recipe.getWhite();
        }

        return canStoreInternal(red, yellow, blue, white);
    }
    //endregion

    private void getRecipe() {
        if(!(level instanceof ServerLevel serverLevel))
            return;

        RecipeHolder<DyeLiquifierRecipe> recipeHolder = serverLevel.recipeAccess().getRecipeFor(RecipesRegistry.DYE_LIQUIFIER_RECIPE_TYPE.get(),
                new WootRecipeInput(Either.left(redInventoryHandler.getStackInSlot(INPUT_SLOT))),
                level).orElse(null);

        if(recipeHolder != null) recipes.put(0, recipeHolder.value());
        else recipes.remove(0);

        recipeHolder = serverLevel.recipeAccess().getRecipeFor(RecipesRegistry.DYE_LIQUIFIER_RECIPE_TYPE.get(),
                new WootRecipeInput(Either.left(yellowInventoryHandler.getStackInSlot(INPUT_SLOT))),
                level).orElse(null);

        if(recipeHolder != null) recipes.put(1, recipeHolder.value());
        else recipes.remove(1);

        recipeHolder = serverLevel.recipeAccess().getRecipeFor(RecipesRegistry.DYE_LIQUIFIER_RECIPE_TYPE.get(),
                new WootRecipeInput(Either.left(blueInventoryHandler.getStackInSlot(INPUT_SLOT))),
                level).orElse(null);

        if(recipeHolder != null) recipes.put(2, recipeHolder.value());
        else recipes.remove(2);

        recipeHolder = serverLevel.recipeAccess().getRecipeFor(RecipesRegistry.DYE_LIQUIFIER_RECIPE_TYPE.get(),
                new WootRecipeInput(Either.left(whiteInventoryHandler.getStackInSlot(INPUT_SLOT))),
                level).orElse(null);

        if(recipeHolder != null) recipes.put(3, recipeHolder.value());
        else recipes.remove(3);
    }

    public int getRed() { return this.red; }
    public int getYellow() { return this.yellow; }
    public int getBlue() { return this.blue; }
    public int getWhite() { return this.white; }

    private boolean canStoreInternal(int recipeRed, int recipeYellow, int recipeBlue, int recipeWhite) {
        boolean redHasSpace = recipeRed + red <= DyeLiquifierConfig.RED_TANK_CAPACITY.get();
        boolean yellowHasSpace = recipeYellow + yellow <= DyeLiquifierConfig.YELLOW_TANK_CAPACITY.get();
        boolean blueHasSpace = recipeBlue + blue <= DyeLiquifierConfig.BLUE_TANK_CAPACITY.get();
        boolean whiteHasSpace = recipeWhite + white <= DyeLiquifierConfig.WHITE_TANK_CAPACITY.get();

        return recipeRed > 0 && redHasSpace ||
                recipeYellow > 0 && yellowHasSpace ||
                recipeBlue > 0 && blueHasSpace ||
                recipeWhite > 0 && whiteHasSpace;
    }

    private boolean canCreateOutput() {
        return red >= DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get() &&
                yellow >= DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get() &&
                blue >= DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get() &&
                white >= DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get();
    }
    private boolean canStoreOutput() { return outputTankHandler.fill(new FluidStack(FluidsRegistry.SOURCE_PURE_DYE_FLUID.get(), DyeLiquifierConfig.PURE_DYE_PRODUCE_AMOUNT.get()), IFluidHandler.FluidAction.SIMULATE ) == DyeLiquifierConfig.PURE_DYE_PRODUCE_AMOUNT.get(); }

    public int getEnergyCapacity(){
        return DyeLiquifierConfig.ENERGY_CAPACITY.get();
    }

    public int getEnergyMaxTransfer(){
        return DyeLiquifierConfig.ENERGY_MAX_TRANSFER.get();
    }

    public int getEnergyProcessTransfer(){
        return DyeLiquifierConfig.ENERGY_PROCESS_TRANSFER.get();
    }

    public boolean hasEnergyCapability() {
        return true;
    }

    public int getInputTankCapacity() {
        return 0;
    }

    public boolean hasInputFluidCapability() {
        return false;
    }

    public Predicate<FluidStack> getInputFluidValidator() {
        return null;
    }

    public int getOutputTankCapacity() {
        return DyeLiquifierConfig.OUTPUT_TANK_CAPACITY.get();
    }

    public boolean hasOutputFluidCapability() {
        return true;
    }
}
