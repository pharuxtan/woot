package wootrevived.woot.blocks.heart;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.multiblock.MultiBlockFactory;
import wootrevived.woot.util.render.WootShapes;

import java.util.function.Supplier;

public class HeartBlock extends MultiBlockFactory {
    public HeartBlock(Supplier<BlockEntityType<?>> entity, String tag) {
        super(entity, tag, Block.Properties.of()
                .mapColor(MapColor.METAL)
                .sound(SoundType.METAL)
                .strength(3.5F));

        final StateDefinition.Builder<Block, BlockState> stateDefinitionBuilder = new StateDefinition.Builder<>(this);
        this.createBlockStateDefinition(stateDefinitionBuilder);
        this.heartStateDefinition = stateDefinitionBuilder.create(Block::defaultBlockState, State::new);

        registerDefaultState(getStateDefinition().any()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(BlockStateProperties.ENABLED, true));
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return HeartBlockEntity::ticker;
    }

    protected StateDefinition<Block, BlockState> heartStateDefinition;
    @Override
    public @NotNull StateDefinition<Block, BlockState> getStateDefinition() {
        return this.heartStateDefinition;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        builder.add(BlockStateProperties.ENABLED);
    }

    public static class State extends MultiBlockFactory.State {
        public State(Block block, Reference2ObjectArrayMap<Property<?>, Comparable<?>> map, MapCodec<BlockState> codec) {
            super(block, map, codec);
        }

        @Override
        public InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
            if (level.isClientSide)
                return InteractionResult.SUCCESS;

            if(!getValue(BlockStateProperties.ENABLED))
                return InteractionResult.PASS;

            if (!(level.getBlockEntity(hit.getBlockPos()) instanceof HeartBlockEntity heart))
                throw new IllegalStateException("BlockEntity is missing");

            player.openMenu(heart, buf -> buf.writeBlockPos(hit.getBlockPos()));

            return InteractionResult.SUCCESS_SERVER;
        }

        @Override
        public @NotNull InteractionResult useWithoutItem(@NotNull Level level, @NotNull Player player, @NotNull BlockHitResult hit){
            return useItemOn(ItemStack.EMPTY, level, player, InteractionHand.MAIN_HAND, hit);
        }

        public BlockState rotate(LevelAccessor level, BlockPos pos, Rotation rotation) {
            return setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(getValue(BlockStateProperties.HORIZONTAL_FACING)));
        }

        @Override
        public @NotNull BlockState mirror(Mirror mirror) {
            return rotate(null, null, mirror.getRotation(getValue(BlockStateProperties.HORIZONTAL_FACING)));
        }

        @Override
        public @NotNull VoxelShape getShape(@NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
            if(!getValue(BlockStateProperties.ENABLED)) {
                return WootShapes.disabledShape;
            } else {
                return Shapes.block();
            }
        }

        @Override
        public @NotNull RenderShape getRenderShape() {
            if(getValue(BlockStateProperties.ENABLED))
                return RenderShape.MODEL;
            return RenderShape.INVISIBLE;
        }

        @Override
        public void onPlace(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
            if(getValue(BlockStateProperties.ENABLED))
                super.onPlace(level, pos, newState, isMoving);
        }

        @Override
        public void affectNeighborsAfterRemoval(@NotNull ServerLevel level, @NotNull BlockPos pos, boolean movedByPiston) {
            if(getValue(BlockStateProperties.ENABLED))
                super.affectNeighborsAfterRemoval(level, pos, movedByPiston);
        }
    }
}
