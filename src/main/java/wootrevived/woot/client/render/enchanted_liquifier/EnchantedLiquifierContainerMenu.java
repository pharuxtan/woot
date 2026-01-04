package wootrevived.woot.client.render.enchanted_liquifier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import wootrevived.woot.blocks.enchanted_liquifier.EnchantedLiquifierBlockEntity;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.util.render.WootContainerMenu;
import wootrevived.woot.util.render.WootSlot;

public class EnchantedLiquifierContainerMenu extends WootContainerMenu {
    private final Level level;
    public EnchantedLiquifierBlockEntity blockEntity;

    public EnchantedLiquifierContainerMenu(int id, Level level, BlockPos pos, Inventory playerInventory, Player player) {
        super(BlocksRegistry.ENCHANTED_LIQUIFIER_BLOCK_MENU.get(), id);
        this.level = level;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(!(blockEntity instanceof EnchantedLiquifierBlockEntity be)) return;
        this.blockEntity = be;
        this.data = be.data;

        createMachineInputSlots(be.getInventory());
        createPlayerInventory(playerInventory);
    }

    public EnchantedLiquifierContainerMenu(int windowId, Inventory inv, FriendlyByteBuf data) {
        this(windowId, inv.player.level(), data.readBlockPos(), inv, inv.player);
    }

    private void createMachineInputSlots(ItemStacksResourceHandler machineInventory){
        this.addSlot(new ResourceHandlerSlot(machineInventory, machineInventory::set, 0, 80, 40));
    }

    private void createPlayerInventory(Inventory playerInventory) {
        for(int k = 0; k < 9; k++){
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 160));
        }
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 9; j++){
                this.addSlot(new WootSlot(playerInventory, j + i * 9 + 9, 8 + j * 18, 102 + i * 18));
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // Based off Gigaherz Elements Of Power code
        Slot slot = this.slots.get(index);
        if (!slot.hasItem())
            return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack stackCopy = stack.copy();

        int startIndex;
        int endIndex;

        final int MACHINE_INV_SIZE = 1;
        final int PLAYER_INV_SIZE = 27;
        final int TOOLBAR_INV_SIZE = 9;

        if (index >= MACHINE_INV_SIZE) {
            // player slot
            if (EnchantmentHelper.hasAnyEnchantments(stack)) {
                // -> machine
                startIndex = 0;
                endIndex = MACHINE_INV_SIZE;
            } else if (index < PLAYER_INV_SIZE + MACHINE_INV_SIZE) {
                // -> toolbar
                startIndex = PLAYER_INV_SIZE + MACHINE_INV_SIZE;
                endIndex = startIndex + TOOLBAR_INV_SIZE;
            } else {
                // -> player
                startIndex = MACHINE_INV_SIZE;
                endIndex = startIndex + PLAYER_INV_SIZE;
            }
        } else {
            // machine slot
            startIndex = MACHINE_INV_SIZE;
            endIndex = startIndex + PLAYER_INV_SIZE + TOOLBAR_INV_SIZE;
        }

        if (!this.moveItemStackTo(stack, startIndex, endIndex, false))
            return ItemStack.EMPTY;

        if (stack.getCount() == 0)
            slot.set(ItemStack.EMPTY);
        else
            slot.setChanged();

        if (stack.getCount() == stackCopy.getCount())
            return ItemStack.EMPTY;

        slot.onTake(player, stack);
        return stackCopy;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, BlocksRegistry.ENCHANTED_LIQUIFIER_BLOCK.get());
    }
}