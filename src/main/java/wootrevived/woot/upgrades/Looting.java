package wootrevived.woot.upgrades;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.interfaces.WootSpawnProperties;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.ComponentsRegistry;

import java.util.function.Consumer;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class Looting extends WootUpgradeItem {
    public Looting(String tag, int level) {
        super(new Properties().component(ComponentsRegistry.LOOTING_UPGRADE_TOOLTIP, new Tooltip(level))
                .setId(ResourceKey.create(Registries.ITEM, Woot.location(tag))), level);
    }

    @Override
    public void applySpawnProperties(WootSpawnProperties properties, CompoundTag upgradeTag) {
        ItemStack itemStack = properties.getMainHandItem();

        if(itemStack.isEnchantable()) {
            HolderLookup.Provider accessor = properties.getRegistryAccess();
            HolderLookup.RegistryLookup<Enchantment> lookup = accessor.lookupOrThrow(Registries.ENCHANTMENT);

            lookup.get(Enchantments.LOOTING).ifPresent(enchantment -> {
                itemStack.enchant(enchantment, getLevel());
            });
        }
    }

    /* Upgrade Item registration */

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Woot.MOD_ID);

    public static void register(WootUpgradeItemRegistration registration){
        ITEMS.register(registration.getWootEventBus());
        registration.register(COPPER_LOOTING_ITEM);
        registration.register(IRON_LOOTING_ITEM);
        registration.register(GOLD_LOOTING_ITEM);
        registration.register(DIAMOND_LOOTING_ITEM);
        registration.register(NETHERITE_LOOTING_ITEM);
    }

    public static final String COPPER_LOOTING_TAG = "copper_looting_upgrade";
    public static final DeferredHolder<Item, Looting> COPPER_LOOTING_ITEM = ITEMS.register(COPPER_LOOTING_TAG, () -> new Looting(COPPER_LOOTING_TAG, 1));

    public static final String IRON_LOOTING_TAG = "iron_looting_upgrade";
    public static final DeferredHolder<Item, Looting> IRON_LOOTING_ITEM = ITEMS.register(IRON_LOOTING_TAG, () -> new Looting(IRON_LOOTING_TAG, 2));

    public static final String GOLD_LOOTING_TAG = "gold_looting_upgrade";
    public static final DeferredHolder<Item, Looting> GOLD_LOOTING_ITEM = ITEMS.register(GOLD_LOOTING_TAG, () -> new Looting(GOLD_LOOTING_TAG, 3));

    public static final String DIAMOND_LOOTING_TAG = "diamond_looting_upgrade";
    public static final DeferredHolder<Item, Looting> DIAMOND_LOOTING_ITEM = ITEMS.register(DIAMOND_LOOTING_TAG, () -> new Looting(DIAMOND_LOOTING_TAG, 4));

    public static final String NETHERITE_LOOTING_TAG = "netherite_looting_upgrade";
    public static final DeferredHolder<Item, Looting> NETHERITE_LOOTING_ITEM = ITEMS.register(NETHERITE_LOOTING_TAG, () -> new Looting(NETHERITE_LOOTING_TAG, 5));

    /* Tooltip */

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(ItemStack stack, TooltipContext ctx, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag tooltipFlag){
        if(display.shows(ComponentsRegistry.LOOTING_UPGRADE_TOOLTIP.get()))
            components().get(ComponentsRegistry.LOOTING_UPGRADE_TOOLTIP.get()).addToTooltip(ctx, consumer, tooltipFlag, stack.getComponents());
    }

    public record Tooltip(int level) implements TooltipProvider {
        public static final String ID = "looting_upgrade_tooltip";

        public static final Codec<Tooltip> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        Codec.INT.fieldOf("level").forGetter(Tooltip::level)
                ).apply(inst, Tooltip::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, Tooltip> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, Tooltip::level,
                Tooltip::new
        );

        @Override
        public void addToTooltip(TooltipContext ctx, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
            consumer.accept(Component.translatable("info.woot_revived.upgrade.looting.desc.0", level).setStyle(DESCRIPTION_STYLE));
        }
    }
}
