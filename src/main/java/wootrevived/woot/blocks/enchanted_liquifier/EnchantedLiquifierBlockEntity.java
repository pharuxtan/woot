package wootrevived.woot.blocks.enchanted_liquifier;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.Woot;
import wootrevived.woot.client.render.enchanted_liquifier.EnchantedLiquifierContainerMenu;
import wootrevived.woot.config.EnchantedLiquifierConfig;
import wootrevived.woot.data.EnchantedLiquifierData;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.ComponentsRegistry;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.util.common.MachineSide;
import wootrevived.woot.util.common.MachineSideProperty;
import wootrevived.woot.util.entity.WootMachineBlockEntity;
import wootrevived.woot.util.entity.WootTags;
import wootrevived.woot.util.handlers.WootFluidHandlerWrapper;
import wootrevived.woot.util.handlers.WootItemHandlerWrapper;
import wootrevived.woot.util.handlers.WootItemStackHandler;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Predicate;

public class EnchantedLiquifierBlockEntity extends WootMachineBlockEntity implements MenuProvider {
    private final List<EnumMap<MachineSide, MachineSideProperty>> directionsProperties = new ArrayList<>(2);

    public static final int OUTPUT_FLUID_PROPERTY = 0;
    public static final int INGREDIENT_PROPERTY = 1;

    public EnchantedLiquifierBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.ENCHANTED_LIQUIFIER_BLOCK_ENTITY.get(), pos, state);
        for(int i = 0; i < 2; i++){
            EnumMap<MachineSide, MachineSideProperty> properties = Maps.newEnumMap(MachineSide.class);
            for(MachineSide side : MachineSide.values()){
                properties.put(side, MachineSideProperty.ENABLED);
            }
            directionsProperties.add(properties);
        }
    }

    public static void ticker(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity){
        if(blockEntity instanceof EnchantedLiquifierBlockEntity enchantedLiquifierBlockEntity){
            enchantedLiquifierBlockEntity.tick(level, pos, state, blockEntity);
        }
    }

    @Override
    public void tick(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);

        if(level.isClientSide)
            return;

        tickItem(inventoryHandler, pos, side -> getProperties(side).getIngredientProperty());
        tickFluid(outputTankHandler, pos, EnchantedLiquifierConfig.FLUID_TRANSFER.get(), side -> getProperties(side).getOutputFluidProperty());
    }

    public final WootItemStackHandler inventoryHandler = new WootItemStackHandler(false) {
        @Override
        protected void onContentsChanged(int slot) {
            EnchantedLiquifierBlockEntity.this.onContentsChanged(slot);
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() == Items.ENCHANTED_BOOK && EnchantmentHelper.hasAnyEnchantments(stack);
        }
    };

    public static int INPUT_SLOT = 0;
    public IItemHandler getInventory() { return inventoryHandler; }

    public record Properties(EnchantedLiquifierBlockEntity entity, MachineSide machineSide){
        public MachineSideProperty getIngredientProperty(){
            return entity.directionsProperties.get(INGREDIENT_PROPERTY).get(machineSide);
        }

        public MachineSideProperty getOutputFluidProperty(){
            return entity.directionsProperties.get(OUTPUT_FLUID_PROPERTY).get(machineSide);
        }
    }

    private Properties getProperties(Direction side){
        Direction facing = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        return new Properties(this, MachineSide.getMachineSide(facing, side));
    }

    public static IItemHandler getItemHandlerCapability(EnchantedLiquifierBlockEntity blockEntity, @Nullable Direction side){
        if(side == null)
            return blockEntity.inventoryHandler;

        Properties properties = blockEntity.getProperties(side);

        return new WootItemHandlerWrapper()
                .addHandler(blockEntity.inventoryHandler, properties::getIngredientProperty);
    }

    public static IFluidHandler getFluidHandlerCapability(EnchantedLiquifierBlockEntity blockEntity, @Nullable Direction side){
        if(side == null)
            return blockEntity.outputTankHandler;

        Properties properties = blockEntity.getProperties(side);

        return new WootFluidHandlerWrapper()
                .addHandler(blockEntity.outputTankHandler, properties::getOutputFluidProperty);
    }

    private EnchantedLiquifierData.Component getComponent(){
        return new EnchantedLiquifierData.Component(
                energyHandler.getEnergyStored(),
                getOutputTank().getFluid(),
                getAllMachineSidesProperties()
        );
    }

    private void setComponent(EnchantedLiquifierData.Component component){
        energyHandler.setEnergy(component.energy());
        getOutputTank().setFluid(component.outputFluid());
        setAllMachineSidesProperties(component.listMachineProperties());
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter getter){
        EnchantedLiquifierData.Component component = getter.get(ComponentsRegistry.ENCHANTED_LIQUIFIER_DATA);
        if(component == null)
            return;

        setComponent(component);
        setChanged();
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder){
        builder.set(ComponentsRegistry.ENCHANTED_LIQUIFIER_DATA, getComponent());
    }

    private static final ProblemReporter.ScopedCollector REPORTER = Woot.reporter("EnchantedLiquifierBlockEntity");

    protected ProblemReporter.ScopedCollector getReporter() {
        return REPORTER;
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output){
        super.saveAdditional(output);

        inventoryHandler.serialize(output.child(WootTags.INPUT_INVENTORY_TAG));

        output.store(EnchantedLiquifierData.ID, EnchantedLiquifierData.CODEC, getComponent());
    }

    @Override
    public void loadAdditional(@NotNull ValueInput input){
        super.loadAdditional(input);

        input.child(WootTags.INPUT_INVENTORY_TAG).ifPresent(inventoryHandler::deserialize);

        input.read(EnchantedLiquifierData.ID, EnchantedLiquifierData.CODEC).ifPresent(this::setComponent);
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
        return Component.translatable("gui.woot_revived.enchanted_liquifier.name");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new EnchantedLiquifierContainerMenu(containerId, level, getBlockPos(), playerInventory, player);
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

    @Override
    protected boolean hasEnergy() { return energyHandler.getEnergyStored() > 0; }

    @Override
    protected int useEnergy(){
        return energyHandler.internalExtractEnergy(getEnergyProcessTransfer(), false);
    }

    @Override
    protected void clearRecipe() { }

    @Override
    protected int getRecipeEnergy() {
        ItemStack itemStack = inventoryHandler.getStackInSlot(INPUT_SLOT);
        if (itemStack.isEmpty())
            return 0;

        return getEnchantEnergy(itemStack);
    }

    @Override
    protected void processFinished() {
        ItemStack itemStack = inventoryHandler.getStackInSlot(INPUT_SLOT);
        if (itemStack.isEmpty())
            return;

        inventoryHandler.extractItem(INPUT_SLOT, 1, false);

        int amount = getEnchantAmount(itemStack);
        outputTankHandler.fill(new FluidStack(FluidsRegistry.SOURCE_ENCHANTED_FLUID.get(), amount), IFluidHandler.FluidAction.EXECUTE);

        setChanged();
    }

    @Override
    protected boolean canProcess(boolean checkEnergy) {
        if (checkEnergy && energyHandler.getEnergyStored() <= 0)
            return false;

        ItemStack itemStack = inventoryHandler.getStackInSlot(INPUT_SLOT);
        if (itemStack.isEmpty())
            return false;

        if (!EnchantmentHelper.hasAnyEnchantments(itemStack))
            return false;

        int amount = getEnchantAmount(itemStack);
        int filled = outputTankHandler.fill(new FluidStack(FluidsRegistry.SOURCE_ENCHANTED_FLUID.get(), amount), IFluidHandler.FluidAction.SIMULATE);

        return amount == filled;
    }

    private int getEnchantAmount(ItemStack itemStack) {
        int amount = 0;
        if (!itemStack.isEmpty() && EnchantmentHelper.hasAnyEnchantments(itemStack)) {
            ItemEnchantments enchantments = itemStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);

            for(Holder<Enchantment> enchantmentHolder : enchantments.keySet())
                amount += Mth.clamp(enchantments.getLevel(enchantmentHolder), 1, EnchantedLiquifierConfig.MAX_ENCHANT_LVL.get()) * EnchantedLiquifierConfig.PER_ENCHANT_FLUID.get();
        }
        return amount;
    }

    private int getEnchantEnergy(ItemStack itemStack) {
        int amount = 0;
        if (!itemStack.isEmpty() && EnchantmentHelper.hasAnyEnchantments(itemStack)) {
            ItemEnchantments enchantments = itemStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);

            for(Holder<Enchantment> enchantmentHolder : enchantments.keySet())
                amount += Mth.clamp(enchantments.getLevel(enchantmentHolder), 1, EnchantedLiquifierConfig.MAX_ENCHANT_LVL.get()) * EnchantedLiquifierConfig.PER_ENCHANT_ENERGY.get();
        }
        return amount;
    }

    public int getEnergyCapacity(){
        return EnchantedLiquifierConfig.ENERGY_CAPACITY.get();
    }

    public int getEnergyMaxTransfer(){
        return EnchantedLiquifierConfig.ENERGY_MAX_TRANSFER.get();
    }

    public int getEnergyProcessTransfer(){
        return EnchantedLiquifierConfig.ENERGY_PROCESS_TRANSFER.get();
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
        return EnchantedLiquifierConfig.OUTPUT_TANK_CAPACITY.get();
    }

    public boolean hasOutputFluidCapability() {
        return true;
    }
}
