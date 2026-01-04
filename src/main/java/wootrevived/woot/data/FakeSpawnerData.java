package wootrevived.woot.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import wootrevived.woot.util.entity.WootTags;

import java.util.Optional;

public final class FakeSpawnerData {
    public static final String ID = "fake_spawner_data";

    public static final Codec<Component> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    CompoundTag.CODEC.optionalFieldOf(WootTags.MOB_TAG).forGetter(Component::mobTag)
            ).apply(inst, Component::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Component> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ByteBufCodecs.compoundTagCodec(NbtAccounter::unlimitedHeap)), Component::mobTag,
            Component::new
    );

    public record Component(
             Optional<CompoundTag> mobTag
    ) {}
}
