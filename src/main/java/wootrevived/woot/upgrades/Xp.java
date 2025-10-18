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
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.ItemsRegistry;

import javax.annotation.Nullable;
import java.util.List;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class Xp extends WootUpgradeItem {
    public Xp(int level) { super(new Properties(), level); }

    private static final int[] PERCENTAGES = new int[] { 50, 75, 100, 125, 150 };

    @Override
    public void modifyDrops(WootDropsProperties properties, CompoundTag upgradeTag) {
        List<ItemStack> drops = properties.getItemDrops();

        int experience = Math.round(properties.getExperience() * (PERCENTAGES[getLevel() - 1] / 100F));
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

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("info.woot_revived.upgrade.xp.desc.0", PERCENTAGES[getLevel() - 1]).setStyle(DESCRIPTION_STYLE));
    }

    /* Upgrade Item registration */

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.Keys.ITEMS, Woot.MOD_ID);

    public static void register(WootUpgradeItemRegistration registration){
        ITEMS.register(registration.getWootEventBus());
        registration.register(COPPER_XP_ITEM);
        registration.register(IRON_XP_ITEM);
        registration.register(GOLD_XP_ITEM);
        registration.register(DIAMOND_XP_ITEM);
        registration.register(NETHERITE_XP_ITEM);
    }

    public static final String COPPER_XP_TAG = "copper_xp_upgrade";
    public static final RegistryObject<Xp> COPPER_XP_ITEM = ITEMS.register(COPPER_XP_TAG, () -> new Xp(1));

    public static final String IRON_XP_TAG = "iron_xp_upgrade";
    public static final RegistryObject<Xp> IRON_XP_ITEM = ITEMS.register(IRON_XP_TAG, () -> new Xp(2));

    public static final String GOLD_XP_TAG = "gold_xp_upgrade";
    public static final RegistryObject<Xp> GOLD_XP_ITEM = ITEMS.register(GOLD_XP_TAG, () -> new Xp(3));

    public static final String DIAMOND_XP_TAG = "diamond_xp_upgrade";
    public static final RegistryObject<Xp> DIAMOND_XP_ITEM = ITEMS.register(DIAMOND_XP_TAG, () -> new Xp(4));

    public static final String NETHERITE_XP_TAG = "netherite_xp_upgrade";
    public static final RegistryObject<Xp> NETHERITE_XP_ITEM = ITEMS.register(NETHERITE_XP_TAG, () -> new Xp(5));
}
