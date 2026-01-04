package wootrevived.woot.blocks.creative_tank;

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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import org.jspecify.annotations.Nullable;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.BlocksRegistry;

public class CreativeTankBlock extends Block implements EntityBlock {
    public CreativeTankBlock(String tag) {
        super(Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, Woot.identifier(tag)))
                .mapColor(MapColor.METAL)
                .sound(SoundType.METAL));

        final StateDefinition.Builder<Block, BlockState> stateDefinitionBuilder = new StateDefinition.Builder<>(this);
        this.createBlockStateDefinition(stateDefinitionBuilder);
        this.creativeTankStateDefinition = stateDefinitionBuilder.create(Block::defaultBlockState, State::new);

        registerDefaultState(getStateDefinition().any());
    }

    protected StateDefinition<Block, BlockState> creativeTankStateDefinition;
    @Override
    public StateDefinition<Block, BlockState> getStateDefinition() {
        return this.creativeTankStateDefinition;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlocksRegistry.CREATIVE_TANK_BLOCK_ENTITY.get().create(pos, state);
    }

    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) return null;
        return blockEntityType == BlocksRegistry.CREATIVE_TANK_BLOCK_ENTITY.get() ? CreativeTankBlockEntity::ticker : null;
    }

    public static class State extends BlockState {
        public State(Block block, Reference2ObjectArrayMap<Property<?>, Comparable<?>> map, MapCodec<BlockState> codec) {
            super(block, map, codec);
        }

        @Override
        public InteractionResult useItemOn(ItemStack heldItem, Level level, Player player, InteractionHand hand, BlockHitResult hit) {
            if (level.isClientSide())
                return InteractionResult.SUCCESS;

            if (!(level.getBlockEntity(hit.getBlockPos()) instanceof CreativeTankBlockEntity createTankBlockEntity))
                throw new IllegalStateException("BlockEntity is missing");

            var itemAccess = ItemAccess.forPlayerInteraction(player, hand).oneByOne();
            var handHandler = itemAccess.getCapability(Capabilities.Fluid.ITEM);

            if (handHandler != null) {
                FluidStack stack = FluidUtil.getFirstStackContained(heldItem);
                createTankBlockEntity.emptyIfDifferentFluidStack(stack);
                if (FluidUtil.interactWithFluidHandler(player, hand, level, hit.getBlockPos(), hit.getDirection())) {
                    createTankBlockEntity.setMaxCapacity();
                    return InteractionResult.SUCCESS_SERVER;
                } else {
                    return InteractionResult.FAIL;
                }
            }

            return InteractionResult.SUCCESS_SERVER;
        }
    }
}