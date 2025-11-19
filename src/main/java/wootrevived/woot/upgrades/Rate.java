package wootrevived.woot.upgrades;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.enums.UpgradeDefaultVariant;
import wootrevived.api.interfaces.WootGenerationProperties;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;

import javax.annotation.Nullable;
import java.util.List;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class Rate extends WootUpgradeItem<UpgradeDefaultVariant> {
    public Rate(UpgradeDefaultVariant variant) {
        super(new Properties(), variant);
    }

    private static final int[] PERCENTAGES = new int[] { 10, 20, 30, 50, 75 };

    @Override
    public void applyGenerationProperties(@NotNull WootGenerationProperties properties, @NotNull CompoundTag itemTag) {
        int rate = properties.getSpawnRate();
        float ratio = 1F - PERCENTAGES[getVariant(itemTag).level()-1] / 100F;
        properties.setSpawnRate((int)Math.ceil(rate * ratio));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("info.woot_revived.upgrade.rate.desc.0", PERCENTAGES[getVariant(stack.getTag()).level()-1]).setStyle(DESCRIPTION_STYLE));
    }

    /* Upgrade Item registration */

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.Keys.ITEMS, Woot.MOD_ID);

    public static void register(WootUpgradeItemRegistration registration){
        ITEMS.register(registration.getWootEventBus());
        registration.register(COPPER_RATE_ITEM);
        registration.register(IRON_RATE_ITEM);
        registration.register(GOLD_RATE_ITEM);
        registration.register(DIAMOND_RATE_ITEM);
        registration.register(NETHERITE_RATE_ITEM);
    }

    public static final String COPPER_RATE_TAG = "copper_rate_upgrade";
    public static final RegistryObject<Rate> COPPER_RATE_ITEM = ITEMS.register(COPPER_RATE_TAG, () -> new Rate(UpgradeDefaultVariant.COPPER));

    public static final String IRON_RATE_TAG = "iron_rate_upgrade";
    public static final RegistryObject<Rate> IRON_RATE_ITEM = ITEMS.register(IRON_RATE_TAG, () -> new Rate(UpgradeDefaultVariant.IRON));

    public static final String GOLD_RATE_TAG = "gold_rate_upgrade";
    public static final RegistryObject<Rate> GOLD_RATE_ITEM = ITEMS.register(GOLD_RATE_TAG, () -> new Rate(UpgradeDefaultVariant.GOLD));

    public static final String DIAMOND_RATE_TAG = "diamond_rate_upgrade";
    public static final RegistryObject<Rate> DIAMOND_RATE_ITEM = ITEMS.register(DIAMOND_RATE_TAG, () -> new Rate(UpgradeDefaultVariant.DIAMOND));

    public static final String NETHERITE_RATE_TAG = "netherite_rate_upgrade";
    public static final RegistryObject<Rate> NETHERITE_RATE_ITEM = ITEMS.register(NETHERITE_RATE_TAG, () -> new Rate(UpgradeDefaultVariant.NETHERITE));
}
