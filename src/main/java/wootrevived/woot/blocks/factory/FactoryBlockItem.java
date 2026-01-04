package wootrevived.woot.blocks.factory;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import wootrevived.woot.util.block.FactoryBlockBase;

public class FactoryBlockItem extends BlockItem {
    public FactoryBlockItem(Block block, Properties properties){
        super(block, properties);
    }

    public InteractionResult useOn(UseOnContext context){
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        Direction face = context.getClickedFace();
        ItemStack heldItem = context.getItemInHand();

        if(player == null)
            return super.useOn(context);

        if(!player.getAbilities().mayBuild)
            return super.useOn(context);

        BlockState blockState = level.getBlockState(pos);
        if (tryRePlaceBlock(context, level, player, pos, heldItem, blockState)) return InteractionResult.CONSUME;

        switch(face){
            case EAST  -> pos = pos.offset(+1, +0, +0);
            case WEST  -> pos = pos.offset(-1, +0, +0);
            case UP    -> pos = pos.offset(+0, +1, +0);
            case DOWN  -> pos = pos.offset(+0, -1, +0);
            case SOUTH -> pos = pos.offset(+0, +0, +1);
            case NORTH -> pos = pos.offset(+0, +0, -1);
        }

        blockState = level.getBlockState(pos);
        if (tryRePlaceBlock(context, level, player, pos, heldItem, blockState)) return InteractionResult.CONSUME;

        return super.useOn(context);
    }

    private boolean tryRePlaceBlock(UseOnContext context, Level level, Player player, BlockPos pos, ItemStack heldItem, BlockState blockState) {
        if(blockState.getBlock() instanceof FactoryBlockBase block &&
                heldItem.getItem() instanceof FactoryBlockItem item &&
                block.verifyItem(heldItem.getItem()) &&
                !blockState.getValue(BlockStateProperties.ENABLED)
        ){
            BlockState state = item.getBlock().defaultBlockState();
            if(state.hasProperty(BlockStateProperties.HORIZONTAL_FACING))
                state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING));

            level.setBlock(pos, state, Block.UPDATE_ALL_IMMEDIATE);
            updateCustomBlockEntityTag(pos, level, player, heldItem, state);
            level.updateNeighborsAt(pos, state.getBlock());
            block.setPlacedBy(level, pos, state, player, heldItem);
            level.playSound(null, pos, state.getSoundType(level, pos, player).getPlaceSound(), SoundSource.BLOCKS, 1.0F, 0.8F);
            level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, state));
            player.swing(context.getHand());

            if(!player.getAbilities().instabuild){
                heldItem.shrink(1);
            }

            return true;
        }
        return false;
    }
}
