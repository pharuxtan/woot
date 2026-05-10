package wootrevived.woot.upgrades;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.enums.UpgradeDefaultVariant;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.interfaces.WootGenerationProperties;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;
import wootrevived.woot.config.UpgradesConfig;

import java.util.List;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class Mass extends WootUpgradeItem<UpgradeDefaultVariant> {
    public Mass(UpgradeDefaultVariant variant) {
        super(new Properties(), variant);
    }

    @Override
    public void applyGenerationProperties(@NotNull WootGenerationProperties properties, @NotNull MutableDataComponentHolder dataComponentHolder) {
        if(UpgradesConfig.MASS_REROLL_LOOT.get())
            properties.setNumberOfSimulations(2 * getVariant(dataComponentHolder).level());
    }

    @Override
    public void modifyDrops(@NotNull WootDropsProperties properties, @NotNull MutableDataComponentHolder dataComponentHolder) {
        if(!UpgradesConfig.MASS_REROLL_LOOT.get() && !properties.getSpawnContextData().contains("woot_revived_mass_upgrade_applied")){
            properties.getSpawnContextData().putBoolean("woot_revived_mass_upgrade_applied", true);

            int multiplier = 2 * getVariant(dataComponentHolder).level();

            for(ItemStack stack : properties.getItemDrops())
                stack.setCount(stack.getCount() * multiplier);

            for(FluidStack stack : properties.getFluidDrops())
                stack.setAmount(stack.getAmount() * multiplier);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext ctx, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("info.woot_revived.upgrade.mass.desc.0", 2 * getVariant(stack).level()).setStyle(DESCRIPTION_STYLE));
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
    public static final DeferredHolder<Item, Mass> COPPER_MASS_ITEM = ITEMS.register(COPPER_MASS_TAG, () -> new Mass(UpgradeDefaultVariant.COPPER));

    public static final String IRON_MASS_TAG = "iron_mass_upgrade";
    public static final DeferredHolder<Item, Mass> IRON_MASS_ITEM = ITEMS.register(IRON_MASS_TAG, () -> new Mass(UpgradeDefaultVariant.IRON));

    public static final String GOLD_MASS_TAG = "gold_mass_upgrade";
    public static final DeferredHolder<Item, Mass> GOLD_MASS_ITEM = ITEMS.register(GOLD_MASS_TAG, () -> new Mass(UpgradeDefaultVariant.GOLD));

    public static final String DIAMOND_MASS_TAG = "diamond_mass_upgrade";
    public static final DeferredHolder<Item, Mass> DIAMOND_MASS_ITEM = ITEMS.register(DIAMOND_MASS_TAG, () -> new Mass(UpgradeDefaultVariant.DIAMOND));

    public static final String NETHERITE_MASS_TAG = "netherite_mass_upgrade";
    public static final DeferredHolder<Item, Mass> NETHERITE_MASS_ITEM = ITEMS.register(NETHERITE_MASS_TAG, () -> new Mass(UpgradeDefaultVariant.NETHERITE));
}
