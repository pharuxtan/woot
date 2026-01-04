package wootrevived.woot.blocks.stygian_anvil;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import wootrevived.woot.Woot;
import wootrevived.woot.blocks.fake_spawner.FakeSpawnerBlockEntity;
import wootrevived.woot.items.mob_shard.MobShardItem;
import wootrevived.woot.recipes.stygian_anvil.StygianAnvilRecipe;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.entity.WootTags;
import wootrevived.woot.util.handlers.WootItemResourceHandler;
import wootrevived.woot.util.recipes.WootRecipeInput;

public class StygianAnvilBlockEntity extends BlockEntity {
    public StygianAnvilBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.STYGIAN_ANVIL_BLOCK_ENTITY.get(), pos, state);
    }

    public final WootItemResourceHandler inventoryHandler = new WootItemResourceHandler(5, false) {
        @Override
        protected void onContentsChanged(int slot, ItemStack stack) {
            setChanged();
        }

        @Override
        public boolean isValid(int slot, ItemResource stack) {
            if(slot == BASE_SLOT){
                if(stack.getItem() instanceof MobShardItem)
                    return MobShardItem.isFullyProgrammed(stack.toStack());

                return StygianAnvilRecipe.Validator.isBaseValid(stack.toStack());
            }
            return StygianAnvilRecipe.Validator.isIngredientValid(stack.toStack());
        }

        @Override
        public int getCapacity(int index, ItemResource resource){
            return 1;
        }
    };

    public static int BASE_SLOT = 0;
    public static int FIRST_COMPLEMENTARY_SLOT = 1;
    public static int SECOND_COMPLEMENTARY_SLOT = 2;
    public static int THIRD_COMPLEMENTARY_SLOT = 3;
    public static int FOURTH_COMPLEMENTARY_SLOT = 4;
    public ItemStacksResourceHandler getInventory() { return inventoryHandler; }

    public static ResourceHandler<ItemResource> getItemHandlerCapability(StygianAnvilBlockEntity blockEntity, Direction side) {
        return blockEntity.getInventory();
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state){
        dropContents(level, pos);
    }

    public void dropContents(Level level, BlockPos pos) {
        for(int slot = 0; slot < inventoryHandler.size(); slot++) {
            ItemStack stack = inventoryHandler.getResource(slot).toStack();
            if(!stack.isEmpty()){
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
        }

        setChanged();
    }

    public void dropItem(Player player, InteractionHand hand) {
        ItemStack itemStack = ItemStack.EMPTY;

        for(int slot = inventoryHandler.size() - 1; slot >= 0; slot--) {
            ItemResource resource = inventoryHandler.getResource(slot);
            ItemStack stack = resource.toStack();
            if(!stack.isEmpty()){
                itemStack = stack;
                inventoryHandler.set(slot, ItemResource.EMPTY, 0);
                break;
            }
        }

        if (!itemStack.isEmpty()) {
            setChanged();
            player.setItemInHand(hand, itemStack);
        }
    }

    public ItemStack tryCraft(Player playerEntity, boolean simulate) {
        if(!(level instanceof ServerLevel serverLevel))
            return ItemStack.EMPTY;

        if (!BlocksRegistry.STYGIAN_ANVIL_BLOCK.get().isAnvilHot(level, getBlockPos())) {
            if(playerEntity != null)
                playerEntity.displayClientMessage(Component.translatable("chat.woot_revived.anvil.cold"), true);
            return ItemStack.EMPTY;
        }

        RecipeHolder<StygianAnvilRecipe> recipeHolder = serverLevel.recipeAccess().getRecipeFor(RecipesRegistry.ANVIL_RECIPE_TYPE.get(),
                new WootRecipeInput(
                        Either.left(inventoryHandler.getResource(BASE_SLOT).toStack()),
                        Either.left(inventoryHandler.getResource(FIRST_COMPLEMENTARY_SLOT).toStack()),
                        Either.left(inventoryHandler.getResource(SECOND_COMPLEMENTARY_SLOT).toStack()),
                        Either.left(inventoryHandler.getResource(THIRD_COMPLEMENTARY_SLOT).toStack()),
                        Either.left(inventoryHandler.getResource(FOURTH_COMPLEMENTARY_SLOT).toStack())
                ),
                level).orElse(null);

        if (recipeHolder == null)
            return ItemStack.EMPTY;

        ItemStack output = recipeHolder.value().getOutput();
        ItemStack baseStack = inventoryHandler.getResource(BASE_SLOT).toStack();

        if (baseStack.getItem() instanceof MobShardItem) {
            if(!MobShardItem.isFullyProgrammed(baseStack))
                return ItemStack.EMPTY;

            CompoundTag mobTag = MobShardItem.getProgrammedMobTag(baseStack);
            if(mobTag == null)
                return ItemStack.EMPTY;

            output = FakeSpawnerBlockEntity.getItemStack(mobTag);
        }

        if(!simulate){
            for(int slot = 0; slot < inventoryHandler.size(); slot++) {
                ItemStack stack = inventoryHandler.getResource(slot).toStack().getCraftingRemainder();
                inventoryHandler.set(slot, ItemResource.of(stack), stack.getCount());
            }

            setChanged();
        }

        return output;
    }

    @Override
    protected void saveAdditional(ValueOutput output){
        super.saveAdditional(output);
        inventoryHandler.serialize(output.child(WootTags.INPUT_INVENTORY_TAG));
    }

    @Override
    public void loadAdditional(ValueInput input){
        super.loadAdditional(input);
        input.child(WootTags.INPUT_INVENTORY_TAG).ifPresent(inventoryHandler::deserialize);
    }

    private static final ProblemReporter.ScopedCollector REPORTER = Woot.reporter("StygianAnvilBlockEntity");

    @NonNull
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider){
        CompoundTag tag = super.getUpdateTag(provider);
        TagValueOutput output = TagValueOutput.createWithContext(REPORTER, provider);
        saveAdditional(output);
        tag.merge(output.buildResult());
        return tag;
    }

    @Override
    public void handleUpdateTag(ValueInput input){
        super.handleUpdateTag(input);
        loadAdditional(input);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if(this.level == null || this.level.isClientSide()) return;
        this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }
}
