package wootrevived.woot.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import wootrevived.woot.util.entity.WootTags;

import java.util.Optional;

public final class FactoryBlockData {
    public static final String ID = "factory_block_data";

    public static final Codec<Component> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    BlockPos.CODEC.optionalFieldOf(WootTags.Factory.HEART_POS).forGetter(Component::heartPos)
            ).apply(inst, Component::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Component> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(BlockPos.STREAM_CODEC), Component::heartPos,
            Component::new
    );

    public record Component(
            Optional<BlockPos> heartPos
    ) {}
}
