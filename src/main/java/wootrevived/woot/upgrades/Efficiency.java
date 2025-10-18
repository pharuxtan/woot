package wootrevived.woot.upgrades;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.interfaces.WootGenerationProperties;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;

import javax.annotation.Nullable;
import java.util.List;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class Efficiency extends WootUpgradeItem {
    public Efficiency(int level) { super(new Properties(), level); }

    private static final float[] PERCENTAGES = new float[] { 10, 20, 30, 40, 50 };

    @Override
    public void applyGenerationProperties(WootGenerationProperties properties, CompoundTag upgradeTag) {
        int cost = properties.getVitalityFuelCost();
        float ratio = 1F - PERCENTAGES[getLevel()-1] / 100F;
        properties.setVitalityFuelCost((int)Math.ceil(cost * ratio));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("info.woot_revived.upgrade.efficiency.desc.0", PERCENTAGES[getLevel()-1]).setStyle(DESCRIPTION_STYLE));
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
    public static final DeferredHolder<Item, Efficiency> COPPER_EFFICIENCY_ITEM = ITEMS.register(COPPER_EFFICIENCY_TAG, () -> new Efficiency(1));

    public static final String IRON_EFFICIENCY_TAG = "iron_efficiency_upgrade";
    public static final DeferredHolder<Item, Efficiency> IRON_EFFICIENCY_ITEM = ITEMS.register(IRON_EFFICIENCY_TAG, () -> new Efficiency(2));

    public static final String GOLD_EFFICIENCY_TAG = "gold_efficiency_upgrade";
    public static final DeferredHolder<Item, Efficiency> GOLD_EFFICIENCY_ITEM = ITEMS.register(GOLD_EFFICIENCY_TAG, () -> new Efficiency(3));

    public static final String DIAMOND_EFFICIENCY_TAG = "diamond_efficiency_upgrade";
    public static final DeferredHolder<Item, Efficiency> DIAMOND_EFFICIENCY_ITEM = ITEMS.register(DIAMOND_EFFICIENCY_TAG, () -> new Efficiency(4));

    public static final String NETHERITE_EFFICIENCY_TAG = "netherite_efficiency_upgrade";
    public static final DeferredHolder<Item, Efficiency> NETHERITE_EFFICIENCY_ITEM = ITEMS.register(NETHERITE_EFFICIENCY_TAG, () -> new Efficiency(5));
}
