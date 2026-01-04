package wootrevived.woot.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import wootrevived.woot.util.handlers.WootImportFluidHandler;
import wootrevived.woot.util.handlers.WootImportItemHandler;

public final class IngredientImportData {
    public static final String ID = "ingredient_import_data";

    public static final Codec<Component> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    WootImportItemHandler.CODEC.fieldOf("ItemHandler").forGetter(Component::itemHandler),
                    WootImportFluidHandler.CODEC.fieldOf("FluidHandler").forGetter(Component::fluidHandler)
            ).apply(inst, Component::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Component> STREAM_CODEC = StreamCodec.composite(
            WootImportItemHandler.STREAM_CODEC, Component::itemHandler,
            WootImportFluidHandler.STREAM_CODEC, Component::fluidHandler,
            Component::new
    );

    public record Component(
            WootImportItemHandler itemHandler,
            WootImportFluidHandler fluidHandler
    ) {}
}
