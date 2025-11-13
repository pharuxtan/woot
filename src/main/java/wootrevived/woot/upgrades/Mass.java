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
import wootrevived.api.interfaces.WootGenerationProperties;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.ComponentsRegistry;

import java.util.function.Consumer;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class Mass extends WootUpgradeItem {
    public Mass(String tag, int level) {
        super(new Properties().component(ComponentsRegistry.MASS_UPGRADE_TOOLTIP, new Tooltip(level))
                .setId(ResourceKey.create(Registries.ITEM, Woot.location(tag))), level);
    }

    @Override
    public void applyGenerationProperties(WootGenerationProperties properties, MutableDataComponentHolder dataComponentHolder) {
        properties.setNumberOfSimulations(2 * getLevel());
    }

    /* Upgrade Item registration */

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Woot.MOD_ID);

    public static void register(WootUpgradeItemRegistration registration){
        ITEMS.register(registration.getWootEventBus());
        registration.register(COPPER_MASS_ITEM);
        registration.register(IRON_MASS_ITEM);
        registration.register(GOLD_MASS_ITEM);
        registration.register(DIAMOND_MASS_ITEM);
        registration.register(NETHERITE_MASS_ITEM);
    }

    public static final String COPPER_MASS_TAG = "copper_mass_upgrade";
    public static final DeferredHolder<Item, Mass> COPPER_MASS_ITEM = ITEMS.register(COPPER_MASS_TAG, () -> new Mass(COPPER_MASS_TAG, 1));

    public static final String IRON_MASS_TAG = "iron_mass_upgrade";
    public static final DeferredHolder<Item, Mass> IRON_MASS_ITEM = ITEMS.register(IRON_MASS_TAG, () -> new Mass(IRON_MASS_TAG, 2));

    public static final String GOLD_MASS_TAG = "gold_mass_upgrade";
    public static final DeferredHolder<Item, Mass> GOLD_MASS_ITEM = ITEMS.register(GOLD_MASS_TAG, () -> new Mass(GOLD_MASS_TAG, 3));

    public static final String DIAMOND_MASS_TAG = "diamond_mass_upgrade";
    public static final DeferredHolder<Item, Mass> DIAMOND_MASS_ITEM = ITEMS.register(DIAMOND_MASS_TAG, () -> new Mass(DIAMOND_MASS_TAG, 4));

    public static final String NETHERITE_MASS_TAG = "netherite_mass_upgrade";
    public static final DeferredHolder<Item, Mass> NETHERITE_MASS_ITEM = ITEMS.register(NETHERITE_MASS_TAG, () -> new Mass(NETHERITE_MASS_TAG, 5));

    /* Tooltip */

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(ItemStack stack, TooltipContext ctx, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag tooltipFlag){
        if(display.shows(ComponentsRegistry.MASS_UPGRADE_TOOLTIP.get()))
            components().get(ComponentsRegistry.MASS_UPGRADE_TOOLTIP.get()).addToTooltip(ctx, consumer, tooltipFlag, stack.getComponents());
    }

    public record Tooltip(int level) implements TooltipProvider {
        public static final String ID = "mass_upgrade_tooltip";

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
            consumer.accept(Component.translatable("info.woot_revived.upgrade.mass.desc.0", 2 * level).setStyle(DESCRIPTION_STYLE));
        }
    }
}
