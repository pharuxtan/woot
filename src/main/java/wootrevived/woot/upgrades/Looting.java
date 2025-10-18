package wootrevived.woot.upgrades;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.interfaces.WootSpawnProperties;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;

import javax.annotation.Nullable;
import java.util.List;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class Looting extends WootUpgradeItem {
    public Looting(int level) { super(new Properties(), level); }

    @Override
    public void applySpawnProperties(WootSpawnProperties properties, CompoundTag upgradeTag) {
        ItemStack itemStack = properties.getMainHandItem();

        if(itemStack.getItem().isEnchantable(itemStack))
            itemStack.enchant(Enchantments.MOB_LOOTING, getLevel());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("info.woot_revived.upgrade.looting.desc.0", getLevel()).setStyle(DESCRIPTION_STYLE));
    }

    /* Upgrade Item registration */

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.Keys.ITEMS, Woot.MOD_ID);

    public static void register(WootUpgradeItemRegistration registration){
        ITEMS.register(registration.getWootEventBus());
        registration.register(COPPER_LOOTING_ITEM);
        registration.register(IRON_LOOTING_ITEM);
        registration.register(GOLD_LOOTING_ITEM);
        registration.register(DIAMOND_LOOTING_ITEM);
        registration.register(NETHERITE_LOOTING_ITEM);
    }

    public static final String COPPER_LOOTING_TAG = "copper_looting_upgrade";
    public static final RegistryObject<Looting> COPPER_LOOTING_ITEM = ITEMS.register(COPPER_LOOTING_TAG, () -> new Looting(1));

    public static final String IRON_LOOTING_TAG = "iron_looting_upgrade";
    public static final RegistryObject<Looting> IRON_LOOTING_ITEM = ITEMS.register(IRON_LOOTING_TAG, () -> new Looting(2));

    public static final String GOLD_LOOTING_TAG = "gold_looting_upgrade";
    public static final RegistryObject<Looting> GOLD_LOOTING_ITEM = ITEMS.register(GOLD_LOOTING_TAG, () -> new Looting(3));

    public static final String DIAMOND_LOOTING_TAG = "diamond_looting_upgrade";
    public static final RegistryObject<Looting> DIAMOND_LOOTING_ITEM = ITEMS.register(DIAMOND_LOOTING_TAG, () -> new Looting(4));

    public static final String NETHERITE_LOOTING_TAG = "netherite_looting_upgrade";
    public static final RegistryObject<Looting> NETHERITE_LOOTING_ITEM = ITEMS.register(NETHERITE_LOOTING_TAG, () -> new Looting(5));
}
