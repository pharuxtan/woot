package wootrevived.woot.upgrades;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.enums.UpgradeDefaultVariant;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.interfaces.WootGenerationProperties;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;
import wootrevived.woot.config.UpgradesConfig;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class Mass extends WootUpgradeItem<UpgradeDefaultVariant> {
    public Mass(UpgradeDefaultVariant variant) {
        super(new Properties(), variant);
    }

    @Override
    public void applyGenerationProperties(@NotNull WootGenerationProperties properties, @NotNull CompoundTag itemTag) {
        if(UpgradesConfig.MASS_REROLL_LOOT.get())
            properties.setNumberOfSimulations(2 * getVariant(itemTag).level());
    }

    @Override
    public void modifyDrops(@NotNull WootDropsProperties properties, @NotNull CompoundTag itemTag) {
        if(!UpgradesConfig.MASS_REROLL_LOOT.get() && !properties.getSpawnContextData().contains("woot_revived_mass_upgrade_applied")){
            properties.getSpawnContextData().putBoolean("woot_revived_mass_upgrade_applied", true);

            int multiplier = 2 * getVariant(itemTag).level();

            for(ItemStack stack : properties.getItemDrops())
                stack.setCount(stack.getCount() * multiplier);

            for(FluidStack stack : properties.getFluidDrops())
                stack.setAmount(stack.getAmount() * multiplier);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("info.woot_revived.upgrade.mass.desc.0", 2 * getVariant(stack.getTag()).level()).setStyle(DESCRIPTION_STYLE));
    }

    /* Upgrade Item registration */

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.Keys.ITEMS, Woot.MOD_ID);

    public static void register(WootUpgradeItemRegistration registration){
        ITEMS.register(registration.getWootEventBus());
        registration.register(COPPER_MASS_ITEM);
        registration.register(IRON_MASS_ITEM);
        registration.register(GOLD_MASS_ITEM);
        registration.register(DIAMOND_MASS_ITEM);
        registration.register(NETHERITE_MASS_ITEM);
    }

    public static final String COPPER_MASS_TAG = "copper_mass_upgrade";
    public static final RegistryObject<Mass> COPPER_MASS_ITEM = ITEMS.register(COPPER_MASS_TAG, () -> new Mass(UpgradeDefaultVariant.COPPER));

    public static final String IRON_MASS_TAG = "iron_mass_upgrade";
    public static final RegistryObject<Mass> IRON_MASS_ITEM = ITEMS.register(IRON_MASS_TAG, () -> new Mass(UpgradeDefaultVariant.IRON));

    public static final String GOLD_MASS_TAG = "gold_mass_upgrade";
    public static final RegistryObject<Mass> GOLD_MASS_ITEM = ITEMS.register(GOLD_MASS_TAG, () -> new Mass(UpgradeDefaultVariant.GOLD));

    public static final String DIAMOND_MASS_TAG = "diamond_mass_upgrade";
    public static final RegistryObject<Mass> DIAMOND_MASS_ITEM = ITEMS.register(DIAMOND_MASS_TAG, () -> new Mass(UpgradeDefaultVariant.DIAMOND));

    public static final String NETHERITE_MASS_TAG = "netherite_mass_upgrade";
    public static final RegistryObject<Mass> NETHERITE_MASS_ITEM = ITEMS.register(NETHERITE_MASS_TAG, () -> new Mass(UpgradeDefaultVariant.NETHERITE));
}
