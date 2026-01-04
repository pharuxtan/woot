package wootrevived.woot.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import wootrevived.api.enums.Tier;
import wootrevived.woot.util.entity.WootTags;

public final class MultiBlockFactoryData {
    public static final String ID = "multi_block_factory_data";

    public static final Codec<Component> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    StringRepresentable.fromEnum(Tier::values).fieldOf(WootTags.Factory.FACTORY_TIER).forGetter(Component::tier)
            ).apply(inst, Component::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Component> STREAM_CODEC = StreamCodec.composite(
            NeoForgeStreamCodecs.enumCodec(Tier.class), Component::tier,
            Component::new
    );

    public record Component(
            Tier tier
    ) {}
}
