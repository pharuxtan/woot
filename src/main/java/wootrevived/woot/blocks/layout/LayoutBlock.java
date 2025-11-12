package wootrevived.woot.blocks.layout;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.Woot;
import wootrevived.woot.multiblock.patterns.Pattern;
import wootrevived.woot.multiblock.patterns.Patterns;
import wootrevived.woot.registries.BlocksRegistry;

public class LayoutBlock extends Block implements EntityBlock {
    public LayoutBlock(String tag) {
        super(BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, Woot.location(tag)))
                .mapColor(MapColor.METAL)
                .sound(SoundType.METAL)
                .strength(3.5F));

        final StateDefinition.Builder<Block, BlockState> stateDefinitionBuilder = new StateDefinition.Builder<>(this);
        this.createBlockStateDefinition(stateDefinitionBuilder);
        this.layoutStateDefinition = stateDefinitionBuilder.create(Block::defaultBlockState, State::new);

        registerDefaultState(getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    protected StateDefinition<Block, BlockState> layoutStateDefinition;
    @Override
    public @NotNull StateDefinition<Block, BlockState> getStateDefinition() {
        return this.layoutStateDefinition;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return BlocksRegistry.LAYOUT_BLOCK_ENTITY.get().create(pos, state);
    }

    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) return null;
        return LayoutBlockEntity::ticker;
    }

    public static class State extends BlockState {
        public State(Block block, Reference2ObjectArrayMap<Property<?>, Comparable<?>> map, MapCodec<BlockState> codec) {
            super(block, map, codec);
        }

        public BlockState rotate(LevelAccessor level, BlockPos pos, Rotation rotation) {
            return setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(getValue(BlockStateProperties.HORIZONTAL_FACING)));
        }

        @Override
        public InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
            if(level.isClientSide() || hand == InteractionHand.OFF_HAND)
                return super.useItemOn(stack, level, player, hand, hit);

            if(!player.getMainHandItem().isEmpty())
                return InteractionResult.FAIL;

            BlockEntity blockEntity = level.getBlockEntity(hit.getBlockPos());
            if (blockEntity instanceof LayoutBlockEntity layoutBlockEntity) {
                layoutBlockEntity.setNextTier();
            }

            return InteractionResult.SUCCESS_SERVER;
        }

        @Override
        public @NotNull InteractionResult useWithoutItem(@NotNull Level level, @NotNull Player player, @NotNull BlockHitResult hit){
            return useItemOn(ItemStack.EMPTY, level, player, InteractionHand.MAIN_HAND, hit);
        }

        @Override
        public void affectNeighborsAfterRemoval(@NotNull ServerLevel level, @NotNull BlockPos pos, boolean movedByPiston) {
            if(!level.isClientSide()) {
                Direction facing = getValue(BlockStateProperties.HORIZONTAL_FACING);

                BlockPos layoutPos = switch(facing){
                    case NORTH -> pos.offset(0, 10, -1);
                    case SOUTH -> pos.offset(0, 10, 1);
                    case EAST -> pos.offset(1, 10, 0);
                    case WEST -> pos.offset(-1, 10, 0);
                    default -> pos;
                };

                for (Pattern.PatternBlock patternBlock : Patterns.TIER_1.patterns.get(facing))
                    LayoutBlockEntity.removePatternBlock(level, layoutPos, patternBlock);

                for (Pattern.PatternBlock patternBlock : Patterns.TIER_2.patterns.get(facing))
                    LayoutBlockEntity.removePatternBlock(level, layoutPos, patternBlock);

                for (Pattern.PatternBlock patternBlock : Patterns.TIER_3.patterns.get(facing))
                    LayoutBlockEntity.removePatternBlock(level, layoutPos, patternBlock);

                for (Pattern.PatternBlock patternBlock : Patterns.TIER_4.patterns.get(facing))
                    LayoutBlockEntity.removePatternBlock(level, layoutPos, patternBlock);

                for (Pattern.PatternBlock patternBlock : Patterns.TIER_5.patterns.get(facing))
                    LayoutBlockEntity.removePatternBlock(level, layoutPos, patternBlock);
            }

            super.affectNeighborsAfterRemoval(level, pos, movedByPiston);
        }
    }
}
