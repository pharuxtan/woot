package wootrevived.woot.blocks.magmator;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.Woot;
import wootrevived.woot.blocks.stygian_anvil.StygianAnvilBlockEntity;
import wootrevived.woot.config.MagmatorConfig;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.util.common.RedstoneMode;
import wootrevived.woot.util.entity.WootTags;

public class MagmatorBlockEntity extends BlockEntity implements BlockEntityTicker<BlockEntity> {
    private final int tickRate;

    public MagmatorBlockEntity(BlockEntityType<?> entity, BlockPos pos, BlockState state) {
        super(entity, pos, state);

        if(entity == BlocksRegistry.COPPER_MAGMATOR_BLOCK_ENTITY.get()){
            tickRate = MagmatorConfig.COPPER_TICK_RATE.get();
        } else if(entity == BlocksRegistry.IRON_MAGMATOR_BLOCK_ENTITY.get()) {
            tickRate = MagmatorConfig.IRON_TICK_RATE.get();
        }  else if(entity == BlocksRegistry.GOLD_MAGMATOR_BLOCK_ENTITY.get()) {
            tickRate = MagmatorConfig.GOLD_TICK_RATE.get();
        }  else if(entity == BlocksRegistry.DIAMOND_MAGMATOR_BLOCK_ENTITY.get()) {
            tickRate = MagmatorConfig.DIAMOND_TICK_RATE.get();
        } else if(entity == BlocksRegistry.NETHERITE_MAGMATOR_BLOCK_ENTITY.get()) {
            tickRate = MagmatorConfig.NETHERITE_TICK_RATE.get();
        } else {
            tickRate = 1;
        }

        delayTick = tickRate;
    }

    public static void ticker(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity){
        if(blockEntity instanceof MagmatorBlockEntity magmatorBlockEntity){
            magmatorBlockEntity.tick(level, pos, state, blockEntity);
        }
    }

    private StygianAnvilBlockEntity anvil = null;

    private int delayTick;

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockEntity blockEntity) {
        if(level.isClientSide)
            return;

        BlockEntity upEntity = level.getBlockEntity(pos.relative(Direction.UP));
        if(!(upEntity instanceof StygianAnvilBlockEntity stygianAnvilEntity)){
            anvil = null;
            return;
        }

        anvil = stygianAnvilEntity;

        if(isDisabled())
            return;

        if(delayTick-- != 0)
            return;

        delayTick = tickRate;

        for(Direction direction : Direction.values()){
            if(direction == Direction.UP) continue;

            IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(direction), direction.getOpposite());
            if(handler == null)
                continue;

            ItemStack stack = anvil.tryCraft(null, true);
            if(stack.isEmpty())
                continue;

            ItemStack inserted = ItemHandlerHelper.insertItem(handler, stack, false);
            if(inserted.isEmpty()) {
                anvil.tryCraft(null, false);
                return;
            }
        }
    }

    private RedstoneMode redstoneMode = RedstoneMode.ALWAYS_ON;
    private boolean lastRedstoneState = false;

    private boolean isDisabled(){
        if(redstoneMode == RedstoneMode.ALWAYS_ON) return false;

        boolean current = level.hasNeighborSignal(getBlockPos());

        if(redstoneMode != RedstoneMode.ONCE)
            return (redstoneMode == RedstoneMode.WITH_SIGNAL) != current;

        boolean risingEdge = !lastRedstoneState && current;
        lastRedstoneState = current;
        return !risingEdge;
    }

    public void nextRedstoneMode(Player player){
        redstoneMode = redstoneMode.getNext();
        setChanged();
        player.displayClientMessage(redstoneMode.getComponent(), true);
    }

    private final IItemHandler handler = new AnvilHandler(this);

    public static IItemHandler getItemHandlerCapability(MagmatorBlockEntity blockEntity, Direction side) {
        return blockEntity.handler;
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output){
        super.saveAdditional(output);

        output.putInt(WootTags.REDSTONE_MODE_TAG, redstoneMode.ordinal());
    }

    @Override
    public void loadAdditional(@NotNull ValueInput input){
        super.loadAdditional(input);

        redstoneMode = input.getInt(WootTags.REDSTONE_MODE_TAG).map(RedstoneMode::byIndex).orElse(RedstoneMode.ALWAYS_ON);
    }

    private static final ProblemReporter.ScopedCollector REPORTER = Woot.reporter("MagmatorBlockEntity");

    @NotNull
    @Override
    public CompoundTag getUpdateTag(HolderLookup.@NotNull Provider provider){
        CompoundTag tag = super.getUpdateTag(provider);
        TagValueOutput output = TagValueOutput.createWithContext(REPORTER, provider);
        saveAdditional(output);
        tag.merge(output.buildResult());
        return tag;
    }

    @Override
    public void handleUpdateTag(@NotNull ValueInput input){
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

        if(this.level == null || this.level.isClientSide) return;
        this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    private static class AnvilHandler implements IItemHandler {
        MagmatorBlockEntity entity;

        private AnvilHandler(MagmatorBlockEntity entity){
            this.entity = entity;
        }

        @Override
        public int getSlots() {
            return entity.anvil == null ? 0 : entity.anvil.getInventory().getSlots();
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int i) {
            return entity.anvil == null ? ItemStack.EMPTY : entity.anvil.getInventory().getStackInSlot(i);
        }

        @Override
        public @NotNull ItemStack insertItem(int i, @NotNull ItemStack itemStack, boolean b) {
            return entity.anvil == null ? itemStack : entity.anvil.getInventory().insertItem(i, itemStack, b);
        }

        @Override
        public @NotNull ItemStack extractItem(int i, int i1, boolean b) {
            return entity.anvil == null ? ItemStack.EMPTY : entity.anvil.getInventory().extractItem(i, i1, b);
        }

        @Override
        public int getSlotLimit(int i) {
            return entity.anvil == null ? 0 : entity.anvil.getInventory().getSlotLimit(i);
        }

        @Override
        public boolean isItemValid(int i, @NotNull ItemStack itemStack) {
            return entity.anvil != null && entity.anvil.getInventory().isItemValid(i, itemStack);
        }
    }
}
