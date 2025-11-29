package wootrevived.woot.blocks.stygian_anvil;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.blocks.fake_spawner.FakeSpawnerBlockEntity;
import wootrevived.woot.items.mob_shard.MobShardItem;
import wootrevived.woot.recipes.stygian_anvil.StygianAnvilRecipe;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.entity.WootTags;
import wootrevived.woot.util.recipes.WootContainer;

public class StygianAnvilBlockEntity extends BlockEntity {
    public StygianAnvilBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.STYGIAN_ANVIL_BLOCK_ENTITY.get(), pos, state);
    }

    public final ItemStackHandler inventoryHandler = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if(slot == BASE_SLOT){
                if(stack.getItem() instanceof MobShardItem)
                    return MobShardItem.isFullyProgrammed(stack);

                return StygianAnvilRecipe.Validator.isBaseValid(stack);
            }
            return StygianAnvilRecipe.Validator.isIngredientValid(stack);
        }

        @Override
        public int getSlotLimit(int slot){
            return 1;
        }
    };

    public static int BASE_SLOT = 0;
    public static int INGREDIENT_1_SLOT = 1;
    public static int INGREDIENT_2_SLOT = 2;
    public static int INGREDIENT_3_SLOT = 3;
    public static int INGREDIENT_4_SLOT = 4;
    public IItemHandler getInventory() { return inventoryHandler; }

    public static IItemHandler getItemHandlerCapability(StygianAnvilBlockEntity blockEntity, Direction side) {
        return blockEntity.getInventory();
    }

    public ItemStack[] getIngredients() {
        return new ItemStack[] {
                inventoryHandler.getStackInSlot(INGREDIENT_1_SLOT),
                inventoryHandler.getStackInSlot(INGREDIENT_2_SLOT),
                inventoryHandler.getStackInSlot(INGREDIENT_3_SLOT),
                inventoryHandler.getStackInSlot(INGREDIENT_4_SLOT),
        };
    }

    public void dropContents(Level level, BlockPos pos) {
        for(int slot = 0; slot < inventoryHandler.getSlots(); slot++) {
            ItemStack stack = inventoryHandler.getStackInSlot(slot);
            if(!stack.isEmpty()){
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
        }

        setChanged();
    }

    public void dropItem(Player player, InteractionHand hand) {
        ItemStack itemStack = ItemStack.EMPTY;

        for(int slot = inventoryHandler.getSlots() - 1; slot >= 0; slot--) {
            ItemStack stack = inventoryHandler.getStackInSlot(slot);
            if(!stack.isEmpty()){
                itemStack = stack;
                inventoryHandler.setStackInSlot(slot, ItemStack.EMPTY);
                break;
            }
        }

        if (!itemStack.isEmpty()) {
            setChanged();
            player.setItemInHand(hand, itemStack);
        }
    }

    public ItemStack tryCraft(Player playerEntity, boolean simulate) {
        if (!BlocksRegistry.STYGIAN_ANVIL_BLOCK.get().isAnvilHot(level, getBlockPos())) {
            if(playerEntity != null)
                playerEntity.displayClientMessage(Component.translatable("chat.woot_revived.anvil.cold"), true);
            return ItemStack.EMPTY;
        }

        RecipeHolder<StygianAnvilRecipe> recipeHolder = level.getRecipeManager().getRecipeFor(RecipesRegistry.ANVIL_RECIPE_TYPE.get(),
                new WootContainer(
                        Either.left(inventoryHandler.getStackInSlot(BASE_SLOT)),
                        Either.left(inventoryHandler.getStackInSlot(INGREDIENT_1_SLOT)),
                        Either.left(inventoryHandler.getStackInSlot(INGREDIENT_2_SLOT)),
                        Either.left(inventoryHandler.getStackInSlot(INGREDIENT_3_SLOT)),
                        Either.left(inventoryHandler.getStackInSlot(INGREDIENT_4_SLOT))
                ),
                level).orElse(null);
                
        if (recipeHolder == null)
            return ItemStack.EMPTY;

        ItemStack output = recipeHolder.value().getOutput();
        ItemStack baseStack = inventoryHandler.getStackInSlot(BASE_SLOT);

        if (baseStack.getItem() instanceof MobShardItem) {
            if(!MobShardItem.isFullyProgrammed(baseStack))
                return ItemStack.EMPTY;

            CompoundTag mobTag = MobShardItem.getProgrammedMob(baseStack);
            if(mobTag == null)
                return ItemStack.EMPTY;

            output = FakeSpawnerBlockEntity.getItemStack(mobTag);
        }

        if(!simulate) {
            for (int slot = 0; slot < inventoryHandler.getSlots(); slot++) {
                ItemStack item = inventoryHandler.getStackInSlot(slot);
                if (item.getItem().hasCraftingRemainingItem(item)) {
                    inventoryHandler.setStackInSlot(slot, item.getItem().getCraftingRemainingItem(item));
                } else {
                    inventoryHandler.setStackInSlot(slot, ItemStack.EMPTY);
                }
            }

            setChanged();
        }

        return output;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag){
        super.saveAdditional(tag);

        tag.put(WootTags.INPUT_INVENTORY_TAG, inventoryHandler.serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag tag){
        super.load(tag);

        if(tag.contains(WootTags.INPUT_INVENTORY_TAG))
            inventoryHandler.deserializeNBT(tag.getCompound(WootTags.INPUT_INVENTORY_TAG));
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag(){
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag){
        super.handleUpdateTag(tag);
        load(tag);
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
}
