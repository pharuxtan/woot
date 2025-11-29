package wootrevived.woot.blocks.magmator;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class MagmatorBlock extends Block implements EntityBlock {
    protected final Supplier<BlockEntityType<?>> entity;

    public MagmatorBlock(Supplier<BlockEntityType<?>> entity) {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .sound(SoundType.METAL)
                .strength(3.5F));

        this.entity = entity;

        final StateDefinition.Builder<Block, BlockState> stateDefinitionBuilder = new StateDefinition.Builder<>(this);
        this.createBlockStateDefinition(stateDefinitionBuilder);
        this.magmatorStateDefinition = stateDefinitionBuilder.create(Block::defaultBlockState, State::new);

        registerDefaultState(getStateDefinition().any());
    }

    protected StateDefinition<Block, BlockState> magmatorStateDefinition;
    @Override
    public @NotNull StateDefinition<Block, BlockState> getStateDefinition() {
        return this.magmatorStateDefinition;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return entity.get().create(pos, state);
    }

    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) return null;
        return MagmatorBlockEntity::ticker;
    }

    public static class State extends BlockState {
        public State(Block block, ImmutableMap<Property<?>, Comparable<?>> map, MapCodec<BlockState> codec) {
            super(block, map, codec);
        }

        @Override
        public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
            BlockEntity be = level.getBlockEntity(hit.getBlockPos());
            if(player.isShiftKeyDown() && be instanceof MagmatorBlockEntity entity){
                entity.nextRedstoneMode(player);
                return InteractionResult.SUCCESS;
            }

            return InteractionResult.PASS;
        }
    }
}
