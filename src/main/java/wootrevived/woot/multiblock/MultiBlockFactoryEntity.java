package wootrevived.woot.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.api.enums.Tier;
import wootrevived.woot.util.common.WootTier;
import wootrevived.woot.util.entity.WootTags;

public abstract class MultiBlockFactoryEntity extends BlockEntity implements BlockEntityTicker<BlockEntity>  {
    public MultiBlockFactoryEntity(BlockEntityType<?> type, BlockPos pos, BlockState state){
        super(type, pos, state);
    }

    protected Tier tier = Tier.INVALID;

    public static void ticker(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity){
        if(blockEntity instanceof MultiBlockFactoryEntity multiBlockFactoryEntity){
            multiBlockFactoryEntity.tick(level, pos, state, blockEntity);
        }
    }

    public void updatePattern(Level level){
        BlockState state = getBlockState();
        if(!state.hasProperty(BlockStateProperties.ENABLED) || !state.getValue(BlockStateProperties.ENABLED))
            return;
        Direction facing = state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) ? state.getValue(BlockStateProperties.HORIZONTAL_FACING) : Direction.NORTH;
        Tier newTier = MultiBlockFactory.updatePattern(level, getBlockPos(), facing);
        if(newTier != tier){
            tier = newTier;
            setChanged();
        }
    }

    public Tier getFactoryTier(){
        return tier;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putInt(WootTags.Factory.FACTORY_TIER, tier.ordinal());
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        tier = WootTier.byIndex(tag.getInt(WootTags.Factory.FACTORY_TIER));
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(){
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
