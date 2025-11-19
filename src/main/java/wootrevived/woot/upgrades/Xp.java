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
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.enums.UpgradeDefaultVariant;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.ComponentsRegistry;
import wootrevived.woot.registries.ItemsRegistry;

import java.util.List;
import java.util.function.Consumer;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class Xp extends WootUpgradeItem<UpgradeDefaultVariant> {
    public Xp(String tag, UpgradeDefaultVariant variant) {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, Woot.location(tag)))
                .component(ComponentsRegistry.XP_UPGRADE_TOOLTIP, new Tooltip(variant))
        , variant);
    }

    private static final int[] PERCENTAGES = new int[] { 50, 75, 100, 125, 150 };

    @Override
    public void modifyDrops(@NotNull WootDropsProperties properties, @NotNull MutableDataComponentHolder dataComponentHolder) {
        List<ItemStack> drops = properties.getItemDrops();

        int experience = Math.round(properties.getExperience() * (PERCENTAGES[getVariant(dataComponentHolder).level() - 1] / 100F));
        if(experience <= 0)
            return;

        properties.setExperience(0);

        int shards = experience / 9;
        int splinters = experience % 9;

        if(shards != 0){
            ItemStack shard = ItemsRegistry.XP_SHARD_ITEM.get().getDefaultInstance();
            shard.setCount(shards);
            drops.add(shard);
        }

        if(splinters != 0){
            ItemStack splinter = ItemsRegistry.XP_SPLINTER_ITEM.get().getDefaultInstance();
            splinter.setCount(splinters);
            drops.add(splinter);
        }
    }

    /* Upgrade Item registration */

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Woot.MOD_ID);

    public static void register(WootUpgradeItemRegistration registration){
        ITEMS.register(registration.getWootEventBus());
        registration.register(COPPER_XP_ITEM);
        registration.register(IRON_XP_ITEM);
        registration.register(GOLD_XP_ITEM);
        registration.register(DIAMOND_XP_ITEM);
        registration.register(NETHERITE_XP_ITEM);
    }

    public static final String COPPER_XP_TAG = "copper_xp_upgrade";
    public static final DeferredHolder<Item, Xp> COPPER_XP_ITEM = ITEMS.register(COPPER_XP_TAG, () -> new Xp(COPPER_XP_TAG, UpgradeDefaultVariant.COPPER));

    public static final String IRON_XP_TAG = "iron_xp_upgrade";
    public static final DeferredHolder<Item, Xp> IRON_XP_ITEM = ITEMS.register(IRON_XP_TAG, () -> new Xp(IRON_XP_TAG, UpgradeDefaultVariant.IRON));

    public static final String GOLD_XP_TAG = "gold_xp_upgrade";
    public static final DeferredHolder<Item, Xp> GOLD_XP_ITEM = ITEMS.register(GOLD_XP_TAG, () -> new Xp(GOLD_XP_TAG, UpgradeDefaultVariant.GOLD));

    public static final String DIAMOND_XP_TAG = "diamond_xp_upgrade";
    public static final DeferredHolder<Item, Xp> DIAMOND_XP_ITEM = ITEMS.register(DIAMOND_XP_TAG, () -> new Xp(DIAMOND_XP_TAG, UpgradeDefaultVariant.DIAMOND));

    public static final String NETHERITE_XP_TAG = "netherite_xp_upgrade";
    public static final DeferredHolder<Item, Xp> NETHERITE_XP_ITEM = ITEMS.register(NETHERITE_XP_TAG, () -> new Xp(NETHERITE_XP_TAG, UpgradeDefaultVariant.NETHERITE));

    /* Tooltip */

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext ctx, @NotNull TooltipDisplay display, @NotNull Consumer<Component> consumer, @NotNull TooltipFlag tooltipFlag){
        if(display.shows(ComponentsRegistry.XP_UPGRADE_TOOLTIP.get()))
            components().get(ComponentsRegistry.XP_UPGRADE_TOOLTIP.get()).addToTooltip(ctx, consumer, tooltipFlag, stack.getComponents());
    }

    public record Tooltip(UpgradeDefaultVariant variant) implements TooltipProvider {
        public static final String ID = "xp_upgrade_tooltip";

        public static final Codec<Tooltip> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        UpgradeDefaultVariant.CODEC.fieldOf("level").forGetter(Tooltip::variant)
                ).apply(inst, Tooltip::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, Tooltip> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.fromCodec(UpgradeDefaultVariant.CODEC), Tooltip::variant,
                Tooltip::new
        );

        @Override
        public void addToTooltip(@NotNull TooltipContext ctx, @NotNull Consumer<Component> consumer, @NotNull TooltipFlag tooltipFlag, @NotNull DataComponentGetter dataComponentGetter) {
            consumer.accept(Component.translatable("info.woot_revived.upgrade.xp.desc.0", PERCENTAGES[variant.level() - 1]).setStyle(DESCRIPTION_STYLE));
        }
    }
}
