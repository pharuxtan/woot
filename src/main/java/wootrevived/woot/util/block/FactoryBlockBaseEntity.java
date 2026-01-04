package wootrevived.woot.util.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import wootrevived.woot.Woot;
import wootrevived.woot.data.FactoryBlockData;

import java.util.Objects;
import java.util.Optional;

public class FactoryBlockBaseEntity extends BlockEntity {
    public FactoryBlockBaseEntity(BlockEntityType<?> entity, BlockPos pos, BlockState state) {
        super(entity, pos, state);
    }

    protected BlockPos heartPos = null;

    public boolean isHeartPosEmpty(){
        return heartPos == null;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isSameHeartPos(BlockPos blockPos){
        return Objects.equals(blockPos, heartPos);
    }

    public void setHeartPos(BlockPos blockPos){
        if(Objects.equals(blockPos, heartPos))
            return;
        heartPos = blockPos;
        setChanged();
    }

    private FactoryBlockData.Component getComponent(){
        return new FactoryBlockData.Component(
                Optional.ofNullable(heartPos)
        );
    }

    private void setComponent(FactoryBlockData.Component component){
        heartPos = component.heartPos().orElse(null);
    }

    @Override
    protected void saveAdditional(ValueOutput output){
        super.saveAdditional(output);
        output.store(FactoryBlockData.ID, FactoryBlockData.CODEC, getComponent());
    }

    @Override
    public void loadAdditional(ValueInput input){
        super.loadAdditional(input);
        input.read(FactoryBlockData.ID, FactoryBlockData.CODEC).ifPresent(this::setComponent);
    }

    private static final ProblemReporter.ScopedCollector REPORTER = Woot.reporter("FactoryBlockBaseEntity");

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
