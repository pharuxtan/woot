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
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.interfaces.WootSpawnProperties;
import wootrevived.api.interfaces.WootUpgradeEnum;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.ComponentsRegistry;

import java.util.function.Consumer;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class Dimension extends WootUpgradeItem<Dimension.Variant> {
    public Dimension(String tag, Variant variant){
        super(new Properties()
                        .setId(ResourceKey.create(Registries.ITEM, Woot.identifier(tag)))
                        .component(ComponentsRegistry.DIMENSION_UPGRADE_TOOLTIP, new Tooltip(variant))
                , variant);
    }

    @Override
    public void applySpawnProperties(WootSpawnProperties properties, MutableDataComponentHolder dataComponentHolder) {
        properties.setDimension(getVariant(dataComponentHolder).dimension());
    }

    public enum Variant implements WootUpgradeEnum<Variant> {
        NETHER("nether", Level.NETHER),
        END("end", Level.END);

        private static final Codec<Variant> CODEC = StringRepresentable.fromEnum(Variant::values);

        private final String name;
        private final ResourceKey<Level> dimension;

        Variant(String name, ResourceKey<Level> dimension){
            this.name = name;
            this.dimension = dimension;
        }

        public ResourceKey<Level> dimension() {
            return dimension;
        }

        @Override
        public String getSerializedName() {
            return name;
        }

        @Override
        public Codec<Variant> codec() {
            return CODEC;
        }
    }

    /* Upgrade Item registration */

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Woot.MOD_NAMESPACE);

    public static void register(WootUpgradeItemRegistration registration){
        ITEMS.register(registration.getWootEventBus());
        registration.register(NETHER_DIMENSION_ITEM);
        registration.register(END_DIMENSION_ITEM);
    }

    public static final String NETHER_DIMENSION_TAG = "nether_dimension_upgrade";
    public static final DeferredHolder<Item, Dimension> NETHER_DIMENSION_ITEM = ITEMS.register(NETHER_DIMENSION_TAG, () -> new Dimension(NETHER_DIMENSION_TAG, Variant.NETHER));

    public static final String END_DIMENSION_TAG = "end_dimension_upgrade";
    public static final DeferredHolder<Item, Dimension> END_DIMENSION_ITEM = ITEMS.register(END_DIMENSION_TAG, () -> new Dimension(END_DIMENSION_TAG, Variant.END));

    /* Tooltip */

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(ItemStack stack, TooltipContext ctx, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag tooltipFlag){
        if(display.shows(ComponentsRegistry.DIMENSION_UPGRADE_TOOLTIP.get()))
            components().get(ComponentsRegistry.DIMENSION_UPGRADE_TOOLTIP.get()).addToTooltip(ctx, consumer, tooltipFlag, stack.getComponents());
    }

    public record Tooltip(Variant variant) implements TooltipProvider {
        public static final String ID = "dimension_upgrade_tooltip";

        public static final Codec<Tooltip> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        Variant.CODEC.fieldOf("variant").forGetter(Tooltip::variant)
                ).apply(inst, Tooltip::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, Tooltip> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.fromCodec(Variant.CODEC), Tooltip::variant,
                Tooltip::new
        );

        @Override
        public void addToTooltip(TooltipContext ctx, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
            if(variant == Variant.NETHER){
                consumer.accept(Component.translatable("info.woot_revived.upgrade.dimension.desc.nether").setStyle(DESCRIPTION_STYLE));
            } else {
                consumer.accept(Component.translatable("info.woot_revived.upgrade.dimension.desc.end").setStyle(DESCRIPTION_STYLE));
            }
        }
    }
}
