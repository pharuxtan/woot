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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.enums.UpgradeDefaultVariant;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.interfaces.WootSpawnProperties;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.ComponentsRegistry;

import java.util.List;
import java.util.function.Consumer;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class Decapitate extends WootUpgradeItem<UpgradeDefaultVariant> {
    public Decapitate(String tag, UpgradeDefaultVariant variant) {
        super(new Properties()
                        .setId(ResourceKey.create(Registries.ITEM, Woot.identifier(tag)))
                        .component(ComponentsRegistry.DECAPITATE_UPGRADE_TOOLTIP, new Tooltip(variant))
                , variant);
    }

    @Override
    public void applySpawnProperties(WootSpawnProperties properties, MutableDataComponentHolder dataComponentHolder) {
        properties.setDoSimulateChargedCreeper(true);
    }

    Ingredient vanillaHeads = Ingredient.of(
            Items.ZOMBIE_HEAD,
            Items.CREEPER_HEAD,
            Items.PIGLIN_HEAD,
            Items.DRAGON_HEAD,
            Items.SKELETON_SKULL,
            Items.WITHER_SKELETON_SKULL
    );

    @Override
    public void modifyDrops(WootDropsProperties properties, MutableDataComponentHolder dataComponentHolder) {
        List<ItemStack> drops = properties.getItemDrops();

        for(ItemStack drop : drops){
            if(vanillaHeads.test(drop))
                drop.grow(getVariant(dataComponentHolder).level() - 1);
        }
    }

    /* Upgrade Item registration */

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Woot.MOD_NAMESPACE);

    public static void register(WootUpgradeItemRegistration registration){
        ITEMS.register(registration.getWootEventBus());
        registration.register(COPPER_DECAPITATE_ITEM);
        registration.register(IRON_DECAPITATE_ITEM);
        registration.register(GOLD_DECAPITATE_ITEM);
        registration.register(DIAMOND_DECAPITATE_ITEM);
        registration.register(NETHERITE_DECAPITATE_ITEM);
    }

    public static final String COPPER_DECAPITATE_TAG = "copper_decapitate_upgrade";
    public static final DeferredHolder<Item, Decapitate> COPPER_DECAPITATE_ITEM = ITEMS.register(COPPER_DECAPITATE_TAG, () -> new Decapitate(COPPER_DECAPITATE_TAG, UpgradeDefaultVariant.COPPER));

    public static final String IRON_DECAPITATE_TAG = "iron_decapitate_upgrade";
    public static final DeferredHolder<Item, Decapitate> IRON_DECAPITATE_ITEM = ITEMS.register(IRON_DECAPITATE_TAG, () -> new Decapitate(IRON_DECAPITATE_TAG, UpgradeDefaultVariant.IRON));

    public static final String GOLD_DECAPITATE_TAG = "gold_decapitate_upgrade";
    public static final DeferredHolder<Item, Decapitate> GOLD_DECAPITATE_ITEM = ITEMS.register(GOLD_DECAPITATE_TAG, () -> new Decapitate(GOLD_DECAPITATE_TAG, UpgradeDefaultVariant.GOLD));

    public static final String DIAMOND_DECAPITATE_TAG = "diamond_decapitate_upgrade";
    public static final DeferredHolder<Item, Decapitate> DIAMOND_DECAPITATE_ITEM = ITEMS.register(DIAMOND_DECAPITATE_TAG, () -> new Decapitate(DIAMOND_DECAPITATE_TAG, UpgradeDefaultVariant.DIAMOND));

    public static final String NETHERITE_DECAPITATE_TAG = "netherite_decapitate_upgrade";
    public static final DeferredHolder<Item, Decapitate> NETHERITE_DECAPITATE_ITEM = ITEMS.register(NETHERITE_DECAPITATE_TAG, () -> new Decapitate(NETHERITE_DECAPITATE_TAG, UpgradeDefaultVariant.NETHERITE));

    /* Tooltip */

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(ItemStack stack, TooltipContext ctx, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag tooltipFlag){
        if(display.shows(ComponentsRegistry.DECAPITATE_UPGRADE_TOOLTIP.get()))
            components().get(ComponentsRegistry.DECAPITATE_UPGRADE_TOOLTIP.get()).addToTooltip(ctx, consumer, tooltipFlag, stack.getComponents());
    }

    public record Tooltip(UpgradeDefaultVariant variant) implements TooltipProvider {
        public static final String ID = "decapitate_upgrade_tooltip";

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
            consumer.accept(Component.translatable("info.woot_revived.upgrade.decapitate.desc.0", variant.level()).setStyle(DESCRIPTION_STYLE));
        }
    }
}
