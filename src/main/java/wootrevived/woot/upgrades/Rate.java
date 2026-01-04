package wootrevived.woot.upgrades;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
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
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.enums.UpgradeDefaultVariant;
import wootrevived.api.interfaces.WootGenerationProperties;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.ComponentsRegistry;

import java.util.function.Consumer;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class Rate extends WootUpgradeItem<UpgradeDefaultVariant> {
    public Rate(String tag, UpgradeDefaultVariant variant) {
        super(new Properties()
                        .setId(ResourceKey.create(Registries.ITEM, Woot.identifier(tag)))
                        .component(ComponentsRegistry.RATE_UPGRADE_TOOLTIP, new Tooltip(variant))
                , variant);
    }

    private static final int[] PERCENTAGES = new int[] { 10, 20, 30, 50, 75 };

    @Override
    public void applyGenerationProperties(WootGenerationProperties properties, MutableDataComponentHolder dataComponentHolder) {
        int rate = properties.getSpawnRate();
        float ratio = 1F - PERCENTAGES[getVariant(dataComponentHolder).level()-1] / 100F;
        properties.setSpawnRate((int)Math.ceil(rate * ratio));
    }

    /* Upgrade Item registration */

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Woot.MOD_NAMESPACE);

    public static void register(WootUpgradeItemRegistration registration){
        ITEMS.register(registration.getWootEventBus());
        registration.register(COPPER_RATE_ITEM);
        registration.register(IRON_RATE_ITEM);
        registration.register(GOLD_RATE_ITEM);
        registration.register(DIAMOND_RATE_ITEM);
        registration.register(NETHERITE_RATE_ITEM);
    }

    public static final String COPPER_RATE_TAG = "copper_rate_upgrade";
    public static final DeferredHolder<Item, Rate> COPPER_RATE_ITEM = ITEMS.register(COPPER_RATE_TAG, () -> new Rate(COPPER_RATE_TAG, UpgradeDefaultVariant.COPPER));

    public static final String IRON_RATE_TAG = "iron_rate_upgrade";
    public static final DeferredHolder<Item, Rate> IRON_RATE_ITEM = ITEMS.register(IRON_RATE_TAG, () -> new Rate(IRON_RATE_TAG, UpgradeDefaultVariant.IRON));

    public static final String GOLD_RATE_TAG = "gold_rate_upgrade";
    public static final DeferredHolder<Item, Rate> GOLD_RATE_ITEM = ITEMS.register(GOLD_RATE_TAG, () -> new Rate(GOLD_RATE_TAG, UpgradeDefaultVariant.GOLD));

    public static final String DIAMOND_RATE_TAG = "diamond_rate_upgrade";
    public static final DeferredHolder<Item, Rate> DIAMOND_RATE_ITEM = ITEMS.register(DIAMOND_RATE_TAG, () -> new Rate(DIAMOND_RATE_TAG, UpgradeDefaultVariant.DIAMOND));

    public static final String NETHERITE_RATE_TAG = "netherite_rate_upgrade";
    public static final DeferredHolder<Item, Rate> NETHERITE_RATE_ITEM = ITEMS.register(NETHERITE_RATE_TAG, () -> new Rate(NETHERITE_RATE_TAG, UpgradeDefaultVariant.NETHERITE));

    /* Tooltip */

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(ItemStack stack, TooltipContext ctx, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag tooltipFlag){
        if(display.shows(ComponentsRegistry.RATE_UPGRADE_TOOLTIP.get()))
            components().get(ComponentsRegistry.RATE_UPGRADE_TOOLTIP.get()).addToTooltip(ctx, consumer, tooltipFlag, stack.getComponents());
    }

    public record Tooltip(UpgradeDefaultVariant variant) implements TooltipProvider {
        public static final String ID = "rate_upgrade_tooltip";

        public static final Codec<Tooltip> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        UpgradeDefaultVariant.CODEC.fieldOf("variant").forGetter(Tooltip::variant)
                ).apply(inst, Tooltip::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, Tooltip> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.fromCodec(UpgradeDefaultVariant.CODEC), Tooltip::variant,
                Tooltip::new
        );

        @Override
        public void addToTooltip(TooltipContext ctx, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
            consumer.accept(Component.translatable("info.woot_revived.upgrade.rate.desc.0", PERCENTAGES[variant.level()-1]).setStyle(DESCRIPTION_STYLE));
        }
    }
}
