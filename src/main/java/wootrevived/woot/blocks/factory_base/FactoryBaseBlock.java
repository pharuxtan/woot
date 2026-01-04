package wootrevived.woot.blocks.factory_base;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import wootrevived.woot.Woot;

public class FactoryBaseBlock extends Block {
    public FactoryBaseBlock(String tag) {
        super(Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, Woot.identifier(tag)))
                .mapColor(MapColor.METAL)
                .sound(SoundType.METAL)
                .strength(3.5F));

        final StateDefinition.Builder<Block, BlockState> stateDefinitionBuilder = new StateDefinition.Builder<>(this);
        this.createBlockStateDefinition(stateDefinitionBuilder);
        this.baseStateDefinition = stateDefinitionBuilder.create(Block::defaultBlockState, State::new);

        registerDefaultState(baseStateDefinition.any());
    }

    protected StateDefinition<Block, BlockState> baseStateDefinition;
    @Override
    public StateDefinition<Block, BlockState> getStateDefinition() {
        return this.baseStateDefinition;
    }

    public static class State extends BlockState {
        public State(Block block, Reference2ObjectArrayMap<Property<?>, Comparable<?>> map, MapCodec<BlockState> codec) {
            super(block, map, codec);
        }
    }
}
