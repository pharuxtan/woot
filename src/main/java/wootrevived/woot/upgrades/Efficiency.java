package wootrevived.woot.upgrades;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.interfaces.WootGenerationProperties;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.ComponentsRegistry;

import java.util.function.Consumer;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class Efficiency extends WootUpgradeItem {
    public Efficiency(String tag, int level) {
        super(new Properties().component(ComponentsRegistry.EFFICIENCY_UPGRADE_TOOLTIP, new Tooltip(level))
                .setId(ResourceKey.create(Registries.ITEM, Woot.location(tag))), level);
    }

    private static final float[] PERCENTAGES = new float[] { 10, 20, 30, 40, 50 };

    @Override
    public void applyGenerationProperties(WootGenerationProperties properties, CompoundTag upgradeTag) {
        int cost = properties.getVitalityFuelCost();
        float ratio = 1F - PERCENTAGES[getLevel()-1] / 100F;
        properties.setVitalityFuelCost((int)Math.ceil(cost * ratio));
    }

    /* Upgrade Item registration */

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Woot.MOD_ID);

    public static void register(WootUpgradeItemRegistration registration){
        ITEMS.register(registration.getWootEventBus());
        registration.register(COPPER_EFFICIENCY_ITEM);
        registration.register(IRON_EFFICIENCY_ITEM);
        registration.register(GOLD_EFFICIENCY_ITEM);
        registration.register(DIAMOND_EFFICIENCY_ITEM);
        registration.register(NETHERITE_EFFICIENCY_ITEM);
    }

    public static final String COPPER_EFFICIENCY_TAG = "copper_efficiency_upgrade";
    public static final DeferredHolder<Item, Efficiency> COPPER_EFFICIENCY_ITEM = ITEMS.register(COPPER_EFFICIENCY_TAG, () -> new Efficiency(COPPER_EFFICIENCY_TAG, 1));

    public static final String IRON_EFFICIENCY_TAG = "iron_efficiency_upgrade";
    public static final DeferredHolder<Item, Efficiency> IRON_EFFICIENCY_ITEM = ITEMS.register(IRON_EFFICIENCY_TAG, () -> new Efficiency(IRON_EFFICIENCY_TAG, 2));

    public static final String GOLD_EFFICIENCY_TAG = "gold_efficiency_upgrade";
    public static final DeferredHolder<Item, Efficiency> GOLD_EFFICIENCY_ITEM = ITEMS.register(GOLD_EFFICIENCY_TAG, () -> new Efficiency(GOLD_EFFICIENCY_TAG, 3));

    public static final String DIAMOND_EFFICIENCY_TAG = "diamond_efficiency_upgrade";
    public static final DeferredHolder<Item, Efficiency> DIAMOND_EFFICIENCY_ITEM = ITEMS.register(DIAMOND_EFFICIENCY_TAG, () -> new Efficiency(DIAMOND_EFFICIENCY_TAG, 4));

    public static final String NETHERITE_EFFICIENCY_TAG = "netherite_efficiency_upgrade";
    public static final DeferredHolder<Item, Efficiency> NETHERITE_EFFICIENCY_ITEM = ITEMS.register(NETHERITE_EFFICIENCY_TAG, () -> new Efficiency(NETHERITE_EFFICIENCY_TAG, 5));

    /* Tooltip */

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(ItemStack stack, TooltipContext ctx, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag tooltipFlag){
        if(display.shows(ComponentsRegistry.EFFICIENCY_UPGRADE_TOOLTIP.get()))
            components().get(ComponentsRegistry.EFFICIENCY_UPGRADE_TOOLTIP.get()).addToTooltip(ctx, consumer, tooltipFlag, stack.getComponents());
    }

    public record Tooltip(int level) implements TooltipProvider {
        public static final String ID = "efficiency_upgrade_tooltip";

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
            consumer.accept(Component.translatable("info.woot_revived.upgrade.efficiency.desc.0", PERCENTAGES[level-1]).setStyle(DESCRIPTION_STYLE));
        }
    }
}
