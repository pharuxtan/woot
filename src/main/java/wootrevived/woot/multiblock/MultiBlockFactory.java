package wootrevived.woot.multiblock;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.api.enums.Tier;
import wootrevived.woot.multiblock.patterns.Patterns;
import wootrevived.woot.util.block.FactoryBlockBase;

import java.util.function.Supplier;

public abstract class MultiBlockFactory extends FactoryBlockBase implements EntityBlock {
    public MultiBlockFactory(Supplier<BlockEntityType<?>> entity, String tag, Properties properties) {
        super(entity, tag, properties);
    }

    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) return null;
        return MultiBlockFactoryEntity::ticker;
    }

    public static Tier updatePattern(@NotNull Level level, @NotNull BlockPos pos, @NotNull Direction facing){
        Tier tier = Tier.INVALID;

        boolean firstTierIsValid = Patterns.TIER_1.isPatternValid(level, pos, facing);
        Patterns.TIER_1.setAttached(firstTierIsValid, level, pos, facing);
        if(firstTierIsValid)
            tier = Tier.TIER_1;

        boolean secondTierIsValid = firstTierIsValid && Patterns.TIER_2.isPatternValid(level, pos, facing);
        Patterns.TIER_2.setAttached(secondTierIsValid, level, pos, facing);
        if(secondTierIsValid)
            tier = Tier.TIER_2;

        boolean thirdTierIsValid = secondTierIsValid && Patterns.TIER_3.isPatternValid(level, pos, facing);
        Patterns.TIER_3.setAttached(thirdTierIsValid, level, pos, facing);
        if(thirdTierIsValid)
            tier = Tier.TIER_3;

        boolean fourthTierIsValid = thirdTierIsValid && Patterns.TIER_4.isPatternValid(level, pos, facing);
        Patterns.TIER_4.setAttached(fourthTierIsValid, level, pos, facing);
        if(fourthTierIsValid)
            tier = Tier.TIER_4;

        boolean fifthTierIsValid = fourthTierIsValid && Patterns.TIER_5.isPatternValid(level, pos, facing);
        Patterns.TIER_5.setAttached(fifthTierIsValid, level, pos, facing);
        if(fifthTierIsValid)
            tier = Tier.TIER_5;

        return tier;
    }

    public static void unattachPattern(@NotNull Level level, @NotNull BlockPos pos, @NotNull Direction facing){
        Patterns.TIER_1.setAttached(false, level, pos, facing);
        Patterns.TIER_2.setAttached(false, level, pos, facing);
        Patterns.TIER_3.setAttached(false, level, pos, facing);
        Patterns.TIER_4.setAttached(false, level, pos, facing);
        Patterns.TIER_5.setAttached(false, level, pos, facing);
    }

    public static class State extends FactoryBlockBase.State {
        public State(Block block, Reference2ObjectArrayMap<Property<?>, Comparable<?>> map, MapCodec<BlockState> codec) {
            super(block, map, codec);
        }

        @Override
        public void affectNeighborsAfterRemoval(@NotNull ServerLevel level, @NotNull BlockPos pos, boolean movedByPiston) {
            super.affectNeighborsAfterRemoval(level, pos, movedByPiston);

            if (level.isClientSide())
                return;

            Direction direction = getValue(BlockStateProperties.HORIZONTAL_FACING);
            MultiBlockFactory.unattachPattern(level, pos, direction);
        }

        @Override
        public void onPlace(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
            super.onPlace(level, pos, newState, isMoving);

            if(level.isClientSide())
                return;

            Direction direction = getValue(BlockStateProperties.HORIZONTAL_FACING);
            MultiBlockFactory.updatePattern(level, pos, direction);
        }
    }
}
