package wootrevived.woot.blocks.magmator;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;
import wootrevived.woot.Woot;

import java.util.function.Supplier;

public class MagmatorBlock extends Block implements EntityBlock {
    protected final Supplier<BlockEntityType<?>> entity;

    public MagmatorBlock(Supplier<BlockEntityType<?>> entity, String tag) {
        super(Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, Woot.identifier(tag)))
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
    public StateDefinition<Block, BlockState> getStateDefinition() {
        return this.magmatorStateDefinition;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return entity.get().create(pos, state);
    }

    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) return null;
        return MagmatorBlockEntity::ticker;
    }

    public static class State extends BlockState {
        public State(Block block, Reference2ObjectArrayMap<Property<?>, Comparable<?>> map, MapCodec<BlockState> codec) {
            super(block, map, codec);
        }

        @Override
        public InteractionResult useItemOn(ItemStack heldItem, Level level, Player player, InteractionHand hand, BlockHitResult hit) {
            BlockEntity be = level.getBlockEntity(hit.getBlockPos());
            if(player.isShiftKeyDown() && be instanceof MagmatorBlockEntity entity){
                entity.nextRedstoneMode(player);
                return InteractionResult.SUCCESS;
            }

            return InteractionResult.PASS;
        }

        @Override
        public InteractionResult useWithoutItem(Level level, Player player, BlockHitResult hit){
            return useItemOn(ItemStack.EMPTY, level, player, InteractionHand.MAIN_HAND, hit);
        }
    }
}
