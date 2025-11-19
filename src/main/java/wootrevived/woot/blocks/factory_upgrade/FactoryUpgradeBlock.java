package wootrevived.woot.blocks.factory_upgrade;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootUpgradeItem;
import wootrevived.woot.util.block.FactoryBlockBase;

import java.util.function.Supplier;

public class FactoryUpgradeBlock extends FactoryBlockBase {
    public FactoryUpgradeBlock(Supplier<BlockEntityType<?>> entity) {
        super(entity, BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .sound(SoundType.STONE)
                .strength(3.5F));

        final StateDefinition.Builder<Block, BlockState> stateDefinitionBuilder = new StateDefinition.Builder<>(this);
        this.createBlockStateDefinition(stateDefinitionBuilder);
        this.upgradeStateDefinition = stateDefinitionBuilder.create(Block::defaultBlockState, State::new);

        registerDefaultState(getStateDefinition().any()
                .setValue(BlockStateProperties.ATTACHED, false)
                .setValue(BlockStateProperties.ENABLED, true));
    }

    protected StateDefinition<Block, BlockState> upgradeStateDefinition;
    @Override
    public @NotNull StateDefinition<Block, BlockState> getStateDefinition() {
        return this.upgradeStateDefinition;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }

    public static class State extends FactoryBlockBase.State {
        public State(Block block, ImmutableMap<Property<?>, Comparable<?>> map, MapCodec<BlockState> codec) {
            super(block, map, codec);
        }

        @Override
        public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
            if(!getValue(BlockStateProperties.ENABLED))
                return super.use(level, player, hand, hit);

            BlockEntity blockEntity = level.getBlockEntity(hit.getBlockPos());
            if (!level.isClientSide && blockEntity instanceof FactoryUpgradeBlockEntity factoryUpgradeBlockEntity) {
                ItemStack stack = player.getItemInHand(hand);
                if(stack.isEmpty() && player.isShiftKeyDown()){
                    factoryUpgradeBlockEntity.removeUpgrade(level, player, hand);
                    return InteractionResult.SUCCESS;
                } else if (!stack.isEmpty() && stack.getItem() instanceof WootUpgradeItem<?> upgradeItem) {
                    factoryUpgradeBlockEntity.addUpgrade(level, player, hand, stack, upgradeItem);
                    return InteractionResult.SUCCESS;
                } else {
                    return factoryUpgradeBlockEntity.interactUpgrade(level, player, hand, hit);
                }
            }

            return super.use(level, player, hand, hit);
        }

        @Override
        public void onRemove(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
            if(getValue(BlockStateProperties.ENABLED) && getBlock() != newState.getBlock()) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if(blockEntity instanceof FactoryUpgradeBlockEntity factoryUpgradeBlockEntity) {
                    factoryUpgradeBlockEntity.dropItem(level, pos);
                }
            }
            super.onRemove(level, pos, newState, isMoving);
        }
    }
}
