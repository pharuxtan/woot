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

public final class MobShardData {
    public static final String ID = "mob_shard_data";

    public static final Codec<Component> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    CompoundTag.CODEC.optionalFieldOf(WootTags.MOB_TAG).forGetter(Component::mobTag),
                    Codec.INT.fieldOf(WootTags.KILLS_TAG).forGetter(Component::killCount),
                    Codec.BOOL.fieldOf("JEIShard").forGetter(Component::jeiShard)
            ).apply(inst, Component::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Component> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ByteBufCodecs.compoundTagCodec(NbtAccounter::unlimitedHeap)), Component::mobTag,
            ByteBufCodecs.INT, Component::killCount,
            ByteBufCodecs.BOOL, Component::jeiShard,
            Component::new
    );

    public record Component(
            Optional<CompoundTag> mobTag,
            int killCount,
            boolean jeiShard
    ) {}
}
