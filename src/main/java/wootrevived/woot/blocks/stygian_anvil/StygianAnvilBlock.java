package wootrevived.woot.blocks.stygian_anvil;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.ItemsRegistry;

public class StygianAnvilBlock extends Block implements EntityBlock {
    private static final VoxelShape PART_BASE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);
    private static final VoxelShape PART_LOWER_X = Block.box(3.0D, 4.0D, 4.0D, 13.0D, 5.0D, 12.0D);
    private static final VoxelShape PART_MID_X = Block.box(4.0D, 5.0D, 6.0D, 12.0D, 10.0D, 10.0D);
    private static final VoxelShape PART_UPPER_X = Block.box(0.0D, 10.0D, 3.0D, 16.0D, 16.0D, 13.0D);
    private static final VoxelShape PART_LOWER_Z = Block.box(4.0D, 4.0D, 3.0D, 12.0D, 5.0D, 13.0D);
    private static final VoxelShape PART_MID_Z = Block.box(6.0D, 5.0D, 4.0D, 10.0D, 10.0D, 12.0D);
    private static final VoxelShape PART_UPPER_Z = Block.box(3.0D, 10.0D, 0.0D, 13.0D, 16.0D, 16.0D);
    private static final VoxelShape X_AXIS_AABB = Shapes.or(PART_BASE, PART_LOWER_X, PART_MID_X, PART_UPPER_X);
    private static final VoxelShape Z_AXIS_AABB = Shapes.or(PART_BASE, PART_LOWER_Z, PART_MID_Z, PART_UPPER_Z);

    public StygianAnvilBlock() {
        super(Properties.of()
                .mapColor(MapColor.METAL)
                .sound(SoundType.METAL)
                .strength(3.5F));

        final StateDefinition.Builder<Block, BlockState> stateDefinitionBuilder = new StateDefinition.Builder<>(this);
        this.createBlockStateDefinition(stateDefinitionBuilder);
        this.anvilStateDefinition = stateDefinitionBuilder.create(Block::defaultBlockState, State::new);

        registerDefaultState(getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    protected StateDefinition<Block, BlockState> anvilStateDefinition;
    @Override
    public @NotNull StateDefinition<Block, BlockState> getStateDefinition() {
        return this.anvilStateDefinition;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getClockWise());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    public boolean isAnvilHot(Level level, BlockPos pos) {
        Block block = level.getBlockState(pos.below()).getBlock();
        return block == Blocks.MAGMA_BLOCK ||
                block == BlocksRegistry.COPPER_MAGMATOR_BLOCK.get() ||
                block == BlocksRegistry.IRON_MAGMATOR_BLOCK.get() ||
                block == BlocksRegistry.GOLD_MAGMATOR_BLOCK.get() ||
                block == BlocksRegistry.DIAMOND_MAGMATOR_BLOCK.get() ||
                block == BlocksRegistry.NETHERITE_MAGMATOR_BLOCK.get();
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random){
        super.animateTick(state, level, pos, random);

        if (Minecraft.getInstance().options.particles().get() != ParticleStatus.MINIMAL && random.nextInt(10) == 0 && isAnvilHot(level, pos))
            level.addParticle(ParticleTypes.LAVA, ((float) pos.getX() + random.nextFloat()), ((float) pos.getY() + 1.1F), ((float) pos.getZ() + random.nextFloat()), 0.0D, 0.0D, 0.0D);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return BlocksRegistry.STYGIAN_ANVIL_BLOCK_ENTITY.get().create(pos, state);
    }

    public static class State extends BlockState {
        public State(Block block, ImmutableMap<Property<?>, Comparable<?>> map, MapCodec<BlockState> codec) {
            super(block, map, codec);
        }

        @Override
        public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
            if (level.isClientSide)
                return InteractionResult.SUCCESS;

            BlockEntity be = level.getBlockEntity(hit.getBlockPos());
            if (be instanceof StygianAnvilBlockEntity anvil) {
                ItemStack heldItem = player.getItemInHand(hand);

                if (player.isShiftKeyDown() && heldItem.isEmpty()) {
                    // Sneak with empty hand to empty
                    anvil.dropItem(player, hand);
                } else if (heldItem.getItem() == ItemsRegistry.STYGIAN_HAMMER_ITEM.get()) {
                    // Crafting
                    ItemStack stack = anvil.tryCraft(player, false);
                    if(!stack.isEmpty()){
                        BlockPos anvilPos = anvil.getBlockPos();
                        ItemEntity itemEntity = new ItemEntity(level,
                                anvilPos.getX(), anvilPos.getY() + 1, anvilPos.getZ(),
                                stack);
                        itemEntity.setDefaultPickUpDelay();
                        level.addFreshEntity(itemEntity);
                    }
                } else {
                    IItemHandler itemHandler = anvil.getInventory();
                    for(int slot = 0; slot < itemHandler.getSlots(); slot++) {
                        if(itemHandler.getStackInSlot(slot).isEmpty()) {
                            ItemStack item = heldItem.copy();
                            item.setCount(1);
                            ItemStack result = itemHandler.insertItem(slot, item, false);
                            if(result.equals(item))
                                break;
                            heldItem.shrink(1);
                            if (heldItem.isEmpty())
                                player.getInventory().setItem(player.getInventory().selected, ItemStack.EMPTY);
                            else
                                player.getInventory().setItem(player.getInventory().selected, heldItem);
                            player.containerMenu.broadcastChanges();
                            break;
                        }
                    }
                }
            }

            return InteractionResult.SUCCESS;
        }

        @Override
        public @NotNull VoxelShape getShape(@NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
            Direction direction = getValue(BlockStateProperties.HORIZONTAL_FACING);
            return direction.getAxis() == Direction.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
        }

        @Override
        public void onRemove(@NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
            if (getBlock() != newState.getBlock()) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof StygianAnvilBlockEntity stygianAnvilBlockEntity)
                    stygianAnvilBlockEntity.dropContents(level, pos);
                super.onRemove(level, pos, newState, isMoving);
            }
        }

        public BlockState rotate(LevelAccessor level, BlockPos pos, Rotation rotation) {
            return setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(getValue(BlockStateProperties.HORIZONTAL_FACING)));
        }

        @Override
        public @NotNull BlockState mirror(Mirror mirror) {
            return rotate(null, null, mirror.getRotation(getValue(BlockStateProperties.HORIZONTAL_FACING)));
        }
    }
}
