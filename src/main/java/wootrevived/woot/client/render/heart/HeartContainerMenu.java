package wootrevived.woot.client.render.heart;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;
import wootrevived.woot.blocks.cell.CellBlockEntity;
import wootrevived.woot.blocks.factory_upgrade.FactoryUpgradeBlockEntity;
import wootrevived.woot.blocks.fake_spawner.FakeSpawnerBlockEntity;
import wootrevived.woot.blocks.heart.HeartBlockEntity;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.util.render.WootSlot;
import wootrevived.woot.util.render.WootSlotItemHandler;

import java.util.List;

public class HeartContainerMenu extends AbstractContainerMenu {
    private final Level level;
    private HeartBlockEntity blockEntity;

    private final ItemStackHandler upgrades = new ItemStackHandler(4) {
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate){
            return stack;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate){
            return ItemStack.EMPTY;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack)
        {
            return false;
        }
    };

    private final ItemStackHandler imports = new ItemStackHandler(36) {
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate){
            return stack;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate){
            return ItemStack.EMPTY;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack)
        {
            return false;
        }
    };

    public HeartContainerMenu(int id, Level level, BlockPos pos, Inventory playerInventory, Player player) {
        super(BlocksRegistry.HEART_BLOCK_MENU.get(), id);
        this.level = level;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(!(blockEntity instanceof HeartBlockEntity be)) return;
        this.blockEntity = be;

        createImportSlots();
        createUpgradeSlots();
        createPlayerInventory(playerInventory);
    }

    public HeartContainerMenu(int windowId, Inventory inv, FriendlyByteBuf data) {
        this(windowId, inv.player.level(), data.readBlockPos(), inv, inv.player);
    }

    private void createImportSlots(){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 9; j++){
                this.addSlot(new WootSlotItemHandler(imports, WootSlotItemHandler.Type.HEART_INPUTS, j + i * 9, 8 + j * 18, 106 + i * 18));
            }
        }
    }

    private void createUpgradeSlots(){
        this.addSlot(new WootSlotItemHandler(upgrades, WootSlotItemHandler.Type.INVENTORY, 0, 11, 78));
        this.addSlot(new WootSlotItemHandler(upgrades, WootSlotItemHandler.Type.INVENTORY, 1, 29, 78));
        this.addSlot(new WootSlotItemHandler(upgrades, WootSlotItemHandler.Type.INVENTORY, 2, 47, 78));
        this.addSlot(new WootSlotItemHandler(upgrades, WootSlotItemHandler.Type.INVENTORY, 3, 65, 78));
    }

    private void createPlayerInventory(Inventory playerInventory) {
        for(int k = 0; k < 9; k++){
            this.addSlot(new WootSlot(playerInventory, k, 8 + k * 18, 160));
        }
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 9; j++){
                this.addSlot(new WootSlot(playerInventory, j + i * 9 + 9, 8 + j * 18, 102 + i * 18));
            }
        }
    }

    public void updateImports(List<ItemStack> stacks){
        for(int i = 0; i < 36; i++){
            imports.setStackInSlot(i, i >= stacks.size() ? ItemStack.EMPTY : stacks.get(i));
        }
    }

    public void updateUpgrades(){
        upgrades.setStackInSlot(0, getUpgrade(0));
        upgrades.setStackInSlot(1, getUpgrade(3));
        upgrades.setStackInSlot(2, getUpgrade(2));
        upgrades.setStackInSlot(3, getUpgrade(1));
    }

    public ItemStack getUpgrade(int index){
        FactoryUpgradeBlockEntity upgrade = blockEntity.getUpgrade(index);

        if(upgrade == null)
            return ItemStack.EMPTY;

        return upgrade.getUpgradeItemStack();
    }

    public FluidStack getCellFluid() {
        CellBlockEntity cell = blockEntity.getCell();
        if(cell == null)
            return FluidStack.EMPTY;
        return cell.tankHandler.getFluid();
    }

    public int getCellFluidCapacity() {
        CellBlockEntity cell = blockEntity.getCell();
        if(cell == null)
            return 0;
        return cell.tankHandler.getCapacity();
    }

    public Tier getFactoryTier(){
        return blockEntity.getFactoryTier();
    }

    public Level getLevel(){
        return level;
    }

    public static final int PRIMARY_FAKE_SPAWNER = 0;
    public static final int SECONDARY_FAKE_SPAWNER_0 = 1;
    public static final int SECONDARY_FAKE_SPAWNER_1 = 2;
    public static final int SECONDARY_FAKE_SPAWNER_2 = 3;

    public @Nullable FakeSpawnerBlockEntity getFakeSpawner(int index){
        FakeSpawnerBlockEntity fakeSpawner;
        if(index == PRIMARY_FAKE_SPAWNER){
            fakeSpawner = blockEntity.getPrimaryFakeSpawner();
        } else {
            fakeSpawner = blockEntity.getSecondaryFakeSpawner(index - 1);
        }
        return fakeSpawner;
    }

    public @Nullable ValueInput getFactoryMobValue(int index){
        FakeSpawnerBlockEntity fakeSpawner = getFakeSpawner(index);

        if(fakeSpawner == null || fakeSpawner.isRemoved())
            return null;

        return fakeSpawner.getMobValue();
    }

    public @Nullable WootFactoryMob<?> getFactoryMob(int index){
        FakeSpawnerBlockEntity fakeSpawner = getFakeSpawner(index);

        if(fakeSpawner == null || fakeSpawner.isRemoved())
            return null;

        return fakeSpawner.getMob();
    }

    public int getFactoryVitalityAmount(int index){
        FakeSpawnerBlockEntity fakeSpawner = getFakeSpawner(index);

        if(fakeSpawner == null || fakeSpawner.isRemoved())
            return 0;

        return fakeSpawner.getVitalityCost();
    }

    public int getFactoryVitalityDrained(int index){
        FakeSpawnerBlockEntity fakeSpawner = getFakeSpawner(index);

        if(fakeSpawner == null || fakeSpawner.isRemoved())
            return 0;

        return fakeSpawner.getTotalDrained();
    }

    public boolean getFactoryIsActive(int index){
        FakeSpawnerBlockEntity fakeSpawner = getFakeSpawner(index);

        if(fakeSpawner == null || fakeSpawner.isRemoved())
            return false;

        return fakeSpawner.isActive();
    }

    public float getFactoryETA(int index){
        FakeSpawnerBlockEntity fakeSpawner = getFakeSpawner(index);

        if(fakeSpawner == null || fakeSpawner.isRemoved())
            return 0;

        return fakeSpawner.getETA();
    }

    public float getFactoryRate(int index){
        FakeSpawnerBlockEntity fakeSpawner = getFakeSpawner(index);

        if(fakeSpawner == null || fakeSpawner.isRemoved())
            return 0;

        return fakeSpawner.getRate();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, BlocksRegistry.HEART_BLOCK.get());
    }
}
