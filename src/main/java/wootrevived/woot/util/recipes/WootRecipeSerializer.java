package wootrevived.woot.util.recipes;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class WootRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {
    protected final MapCodec<T> mapCodec;
    protected final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

    public WootRecipeSerializer(MapCodec<T> mapCodec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        this.mapCodec = mapCodec;
        this.streamCodec = streamCodec;
    }

    @Override
    public MapCodec<T> codec() {
        return mapCodec;
    }

    @Override
    @SuppressWarnings("deprecation")
    public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
        return streamCodec;
    }
}
