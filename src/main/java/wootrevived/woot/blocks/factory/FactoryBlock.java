package wootrevived.woot.blocks.factory;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import wootrevived.woot.util.block.FactoryBlockBase;

import java.util.function.Supplier;

public class FactoryBlock extends FactoryBlockBase {
    public FactoryBlock(Supplier<BlockEntityType<?>> entity, String tag) {
        super(entity, tag, BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .sound(SoundType.STONE)
                .strength(3.5F));

        final StateDefinition.Builder<Block, BlockState> stateDefinitionBuilder = new StateDefinition.Builder<>(this);
        this.createBlockStateDefinition(stateDefinitionBuilder);
        this.factoryStateDefinition = stateDefinitionBuilder.create(Block::defaultBlockState, State::new);

        registerDefaultState(getStateDefinition().any()
                .setValue(BlockStateProperties.ATTACHED, false)
                .setValue(BlockStateProperties.ENABLED, true));
    }

    protected StateDefinition<Block, BlockState> factoryStateDefinition;
    @Override
    public StateDefinition<Block, BlockState> getStateDefinition() {
        return this.factoryStateDefinition;
    }

    public static class State extends FactoryBlockBase.State {
        public State(Block block, Reference2ObjectArrayMap<Property<?>, Comparable<?>> map, MapCodec<BlockState> codec) {
            super(block, map, codec);
        }
    }
}
