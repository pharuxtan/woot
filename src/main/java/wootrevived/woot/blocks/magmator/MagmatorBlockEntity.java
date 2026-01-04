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
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if(level.isClientSide())
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

            ResourceHandler<ItemResource> handler = level.getCapability(Capabilities.Item.BLOCK, pos.relative(direction), direction.getOpposite());
            if(handler == null)
                continue;

            ItemStack stack = anvil.tryCraft(null, true);
            if(stack.isEmpty())
                continue;

            ItemStack inserted = ItemUtil.insertItemReturnRemaining(handler, stack, false, null);
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

    private final ResourceHandler<ItemResource> handler = new AnvilHandler(this);

    public static ResourceHandler<ItemResource> getItemHandlerCapability(MagmatorBlockEntity blockEntity, Direction side) {
        return blockEntity.handler;
    }

    @Override
    protected void saveAdditional(ValueOutput output){
        super.saveAdditional(output);

        output.putInt(WootTags.REDSTONE_MODE_TAG, redstoneMode.ordinal());
    }

    @Override
    public void loadAdditional(ValueInput input){
        super.loadAdditional(input);

        redstoneMode = input.getInt(WootTags.REDSTONE_MODE_TAG).map(RedstoneMode::byIndex).orElse(RedstoneMode.ALWAYS_ON);
    }

    private static final ProblemReporter.ScopedCollector REPORTER = Woot.reporter("MagmatorBlockEntity");

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

    private static class AnvilHandler implements ResourceHandler<ItemResource> {
        MagmatorBlockEntity entity;

        private AnvilHandler(MagmatorBlockEntity entity){
            this.entity = entity;
        }

        @Override
        public int size() {
            return entity.anvil == null ? 0 : entity.anvil.getInventory().size();
        }

        @Override
        public ItemResource getResource(int index) {
            return entity.anvil == null ? ItemResource.EMPTY : entity.anvil.getInventory().getResource(index);
        }

        @Override
        public long getAmountAsLong(int index) {
            return entity.anvil == null ? 0 : entity.anvil.getInventory().getAmountAsLong(index);
        }

        @Override
        public long getCapacityAsLong(int index, ItemResource resource) {
            return entity.anvil == null ? 0 : entity.anvil.getInventory().getCapacityAsLong(index, resource);
        }

        @Override
        public boolean isValid(int index, ItemResource resource) {
            return entity.anvil != null && entity.anvil.getInventory().isValid(index, resource);
        }

        @Override
        public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
            return entity.anvil == null ? 0 : entity.anvil.getInventory().insert(index, resource, amount, transaction);
        }

        @Override
        public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
            return entity.anvil == null ? 0 : entity.anvil.getInventory().extract(index, resource, amount, transaction);
        }
    }
}
