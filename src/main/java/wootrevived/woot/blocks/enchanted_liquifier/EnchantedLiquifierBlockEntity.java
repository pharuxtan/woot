package wootrevived.woot.blocks.enchanted_liquifier;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.client.render.enchanted_liquifier.EnchantedLiquifierContainerMenu;
import wootrevived.woot.config.EnchantedLiquifierConfig;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.util.common.MachineSide;
import wootrevived.woot.util.common.MachineSideProperty;
import wootrevived.woot.util.entity.WootMachineBlockEntity;
import wootrevived.woot.util.entity.WootTags;
import wootrevived.woot.util.handlers.WootFluidHandlerWrapper;
import wootrevived.woot.util.handlers.WootItemHandlerWrapper;
import wootrevived.woot.util.handlers.WootItemStackHandler;
import wootrevived.woot.util.helper.EnchantmentHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class EnchantedLiquifierBlockEntity extends WootMachineBlockEntity implements MenuProvider {
    private final List<Map<MachineSide, MachineSideProperty>> directionsProperties = new ArrayList<>(2);

    public static final int OUTPUT_FLUID_PROPERTY = 0;
    public static final int INGREDIENT_PROPERTY = 1;

    public EnchantedLiquifierBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.ENCHANTED_LIQUIFIER_BLOCK_ENTITY.get(), pos, state);
        for(int i = 0; i < 2; i++){
            Map<MachineSide, MachineSideProperty> properties = Maps.newEnumMap(MachineSide.class);
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
        tickFluid(outputTankHandler, pos, EnchantedLiquifierConfig.FLUID_TRANSFER.get(),side -> getProperties(side).getOutputFluidProperty());
    }

    public final WootItemStackHandler inventoryHandler = new WootItemStackHandler(false) {
        @Override
        protected void onContentsChanged(int slot) {
            EnchantedLiquifierBlockEntity.this.onContentsChanged(slot);
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() == Items.ENCHANTED_BOOK && EnchantmentHelper.isEnchanted(stack);
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

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @org.jetbrains.annotations.Nullable Direction side){
        if(side == null) {
            if(ForgeCapabilities.ITEM_HANDLER.equals(cap)){
                return LazyOptional.of(() -> inventoryHandler).cast();
            }

            if(ForgeCapabilities.FLUID_HANDLER.equals(cap)){
                return LazyOptional.of(() -> outputTankHandler).cast();
            }

            return super.getCapability(cap, side);
        }

        Properties properties = getProperties(side);

        if(ForgeCapabilities.ITEM_HANDLER.equals(cap)){
            WootItemHandlerWrapper wrapper = new WootItemHandlerWrapper()
                    .addHandler(inventoryHandler, properties::getIngredientProperty);

            return LazyOptional.of(() -> wrapper).cast();
        }

        if(ForgeCapabilities.FLUID_HANDLER.equals(cap)){
            WootFluidHandlerWrapper wrapper = new WootFluidHandlerWrapper()
                    .addHandler(outputTankHandler, properties::getOutputFluidProperty);

            return LazyOptional.of(() -> wrapper).cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void load(@NotNull CompoundTag tag){
        super.load(tag);

        if(tag.contains(WootTags.INPUT_INVENTORY_TAG))
            inventoryHandler.deserializeNBT(tag.getCompound(WootTags.INPUT_INVENTORY_TAG));
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag){
        super.saveAdditional(tag);

        tag.put(WootTags.INPUT_INVENTORY_TAG, inventoryHandler.serializeNBT());
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

        if (!EnchantmentHelper.isEnchanted(itemStack))
            return false;

        int amount = getEnchantAmount(itemStack);
        int filled = outputTankHandler.fill(new FluidStack(FluidsRegistry.SOURCE_ENCHANTED_FLUID.get(), amount), IFluidHandler.FluidAction.SIMULATE);

        return amount == filled;
    }

    private int getEnchantAmount(ItemStack itemStack) {
        int amount = 0;
        if (!itemStack.isEmpty() && EnchantmentHelper.isEnchanted(itemStack)) {
            ListTag listNBT;
            if (itemStack.getItem() == Items.ENCHANTED_BOOK)
                listNBT = EnchantedBookItem.getEnchantments(itemStack);
            else
                listNBT = itemStack.getEnchantmentTags();

            for (int i = 0; i < listNBT.size(); i++) {
                CompoundTag compoundNBT = listNBT.getCompound(i);
                Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(ResourceLocation.tryParse(compoundNBT.getString("id")));
                if (enchantment != null && compoundNBT.contains("lvl"))
                    amount += Mth.clamp(compoundNBT.getInt("lvl"), 1, EnchantedLiquifierConfig.MAX_ENCHANT_LVL.get()) * EnchantedLiquifierConfig.PER_ENCHANT_FLUID.get();
            }
        }
        return amount;
    }

    private int getEnchantEnergy(ItemStack itemStack) {
        int amount = 0;
        if (!itemStack.isEmpty() && EnchantmentHelper.isEnchanted(itemStack)) {
            ListTag listNBT;
            if (itemStack.getItem() == Items.ENCHANTED_BOOK)
                listNBT = EnchantedBookItem.getEnchantments(itemStack);
            else
                listNBT = itemStack.getEnchantmentTags();

            for (int i = 0; i < listNBT.size(); i++) {
                CompoundTag compoundNBT = listNBT.getCompound(i);
                Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(ResourceLocation.tryParse(compoundNBT.getString("id")));
                if (enchantment != null && compoundNBT.contains("lvl"))
                    amount += Mth.clamp(compoundNBT.getInt("lvl"), 1, EnchantedLiquifierConfig.MAX_ENCHANT_LVL.get()) * EnchantedLiquifierConfig.PER_ENCHANT_ENERGY.get();
            }
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
