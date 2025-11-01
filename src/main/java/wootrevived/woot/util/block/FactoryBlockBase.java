package wootrevived.woot.util.block;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.Woot;
import wootrevived.woot.util.render.WootShapes;

import java.util.function.Supplier;

public abstract class FactoryBlockBase extends Block implements EntityBlock {
    protected final Supplier<BlockEntityType<?>> entity;

    public FactoryBlockBase(Supplier<BlockEntityType<?>> entity, String tag, Properties properties) {
        super(properties.setId(ResourceKey.create(Registries.BLOCK, Woot.location(tag))));
        this.entity = entity;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.ATTACHED);
        builder.add(BlockStateProperties.ENABLED);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, BlockState state, @NotNull Player player){
        if(state.getValue(BlockStateProperties.ENABLED))
            return super.playerWillDestroy(level, pos, state, player);
        return state;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return entity.get().create(pos, state);
    }

    public boolean verifyItem(Item item){
        return item == asItem();
    }

    public static class State extends BlockState {
        public State(Block block, Reference2ObjectArrayMap<Property<?>, Comparable<?>> map, MapCodec<BlockState> codec) {
            super(block, map, codec);
        }

        @Override
        public @NotNull VoxelShape getShape(@NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
            if (!getValue(BlockStateProperties.ENABLED)) {
                return WootShapes.disabledShape;
            } else if (getValue(BlockStateProperties.ATTACHED)) {
                return Shapes.block();
            } else {
                return WootShapes.nonAttachedShape;
            }
        }

        @Override
        public @NotNull RenderShape getRenderShape() {
            if(getValue(BlockStateProperties.ENABLED) && getValue(BlockStateProperties.ATTACHED))
                return RenderShape.MODEL;
            return RenderShape.INVISIBLE;
        }

        @Override
        public float getDestroyProgress(@NotNull Player player, @NotNull BlockGetter level, @NotNull BlockPos pos){
            if(getValue(BlockStateProperties.ENABLED))
                return super.getDestroyProgress(player, level, pos);
            return 0.0F;
        }

        @Override
        public boolean canSurvive(@NotNull LevelReader level, @NotNull BlockPos pos) {
            if(getValue(BlockStateProperties.ENABLED))
                return super.canSurvive(level, pos);
            return true;
        }

        @Override
        public @NotNull PushReaction getPistonPushReaction() {
            if(getValue(BlockStateProperties.ENABLED))
                return super.getPistonPushReaction();
            return PushReaction.BLOCK;
        }

        @Override
        public @NotNull VoxelShape getCollisionShape(@NotNull BlockGetter level, @NotNull BlockPos pos){
            if(getValue(BlockStateProperties.ENABLED))
                return super.getCollisionShape(level, pos);
            return Shapes.empty();
        }

        @Override
        public @NotNull VoxelShape getCollisionShape(@NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context){
            if(getValue(BlockStateProperties.ENABLED))
                return super.getCollisionShape(level, pos, context);
            return Shapes.empty();
        }

        @Override
        public boolean isRedstoneConductor(@NotNull BlockGetter level, @NotNull BlockPos pos) {
            return hasProperty(BlockStateProperties.ATTACHED) && getValue(BlockStateProperties.ATTACHED);
        }

        @Override
        public boolean isSuffocating(@NotNull BlockGetter level, @NotNull BlockPos pos) {
            if(getValue(BlockStateProperties.ENABLED))
                return super.isSuffocating(level, pos);
            return false;
        }

        @Override
        public boolean isViewBlocking(@NotNull BlockGetter level, @NotNull BlockPos pos) {
            if(getValue(BlockStateProperties.ENABLED))
                return super.isViewBlocking(level, pos);
            return false;
        }
    }
}
