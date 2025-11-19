package wootrevived.woot.upgrades;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
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
import wootrevived.api.enums.UpgradeNoVariant;
import wootrevived.api.interfaces.WootSpawnProperties;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.ComponentsRegistry;

import java.util.function.Consumer;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class Burn extends WootUpgradeItem<UpgradeNoVariant> {
    public Burn(String tag) {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, Woot.location(tag)))
                .component(ComponentsRegistry.BURN_UPGRADE_TOOLTIP, Tooltip.INSTANCE)
        , UpgradeNoVariant.NONE);
    }

    @Override
    public void applySpawnProperties(@NotNull WootSpawnProperties properties, @NotNull MutableDataComponentHolder dataComponentHolder) {
        properties.setIsInFire(true);
    }

    /* Upgrade Item registration */

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Woot.MOD_ID);

    public static void register(WootUpgradeItemRegistration registration){
        ITEMS.register(registration.getWootEventBus());
        registration.register(BURN_ITEM);
    }

    public static final String BURN_TAG = "burn_upgrade";
    public static final DeferredHolder<Item, Burn> BURN_ITEM = ITEMS.register(BURN_TAG, () -> new Burn(BURN_TAG));

    /* Tooltip */

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext ctx, @NotNull TooltipDisplay display, @NotNull Consumer<Component> consumer, @NotNull TooltipFlag tooltipFlag){
        if(display.shows(ComponentsRegistry.BURN_UPGRADE_TOOLTIP.get()))
            components().get(ComponentsRegistry.BURN_UPGRADE_TOOLTIP.get()).addToTooltip(ctx, consumer, tooltipFlag, stack.getComponents());
    }

    public record Tooltip() implements TooltipProvider {
        public static final Tooltip INSTANCE = new Tooltip();

        public static final String ID = "burn_upgrade_tooltip";
        public static final Codec<Tooltip> CODEC = Codec.unit(INSTANCE);
        public static final StreamCodec<ByteBuf, Tooltip> STREAM_CODEC = StreamCodec.unit(INSTANCE);

        @Override
        public void addToTooltip(@NotNull TooltipContext ctx, @NotNull Consumer<Component> consumer, @NotNull TooltipFlag tooltipFlag, @NotNull DataComponentGetter dataComponentGetter) {
            consumer.accept(Component.translatable("info.woot_revived.upgrade.burn.desc.0").setStyle(DESCRIPTION_STYLE));
        }
    }
}
