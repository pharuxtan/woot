package wootrevived.woot.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.util.entity.WootTags;

import java.util.Optional;

public final class FactoryUpgradeData {
    public static final String ID = "factory_upgrade_data";

    public static final Codec<Component> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.STRING.optionalFieldOf(WootTags.Factory.UPGRADE_ITEM).forGetter(Component::upgradeItem),
                    ItemStack.OPTIONAL_CODEC.optionalFieldOf(WootTags.Factory.UPGRADE_ITEM_STACK).forGetter(Component::upgradeStack)
            ).apply(inst, Component::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Component> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), Component::upgradeItem,
            ByteBufCodecs.optional(ItemStack.OPTIONAL_STREAM_CODEC), Component::upgradeStack,
            Component::new
    );

    public record Component(
            @NotNull Optional<String> upgradeItem,
            @NotNull Optional<ItemStack> upgradeStack
    ) {
        @Override
        public int hashCode() {
            return upgradeItem.hashCode() + upgradeStack.map(ItemStack::hashItemAndComponents).orElse(0);
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Component(Optional<String> item, Optional<ItemStack> stack)))
                return false;

            return upgradeItem.equals(item) && upgradeStack.map(s -> stack.filter(itemStack -> ItemStack.isSameItemSameComponents(s, itemStack)).isPresent()).orElse(stack.isEmpty());
        }
    }
}
