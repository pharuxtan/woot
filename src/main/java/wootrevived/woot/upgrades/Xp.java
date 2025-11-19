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
import wootrevived.api.enums.UpgradeDefaultVariant;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.ItemsRegistry;

import javax.annotation.Nullable;
import java.util.List;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class Xp extends WootUpgradeItem<UpgradeDefaultVariant> {
    public Xp(UpgradeDefaultVariant variant) {
        super(new Properties(), variant);
    }

    private static final int[] PERCENTAGES = new int[] { 50, 75, 100, 125, 150 };

    @Override
    public void modifyDrops(@NotNull WootDropsProperties properties, @NotNull CompoundTag itemTag) {
        List<ItemStack> drops = properties.getItemDrops();

        int experience = Math.round(properties.getExperience() * (PERCENTAGES[getVariant(itemTag).level() - 1] / 100F));
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
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("info.woot_revived.upgrade.xp.desc.0", PERCENTAGES[getVariant(stack.getTag()).level() - 1]).setStyle(DESCRIPTION_STYLE));
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
    public static final DeferredHolder<Item, Xp> COPPER_XP_ITEM = ITEMS.register(COPPER_XP_TAG, () -> new Xp(UpgradeDefaultVariant.COPPER));

    public static final String IRON_XP_TAG = "iron_xp_upgrade";
    public static final DeferredHolder<Item, Xp> IRON_XP_ITEM = ITEMS.register(IRON_XP_TAG, () -> new Xp(UpgradeDefaultVariant.IRON));

    public static final String GOLD_XP_TAG = "gold_xp_upgrade";
    public static final DeferredHolder<Item, Xp> GOLD_XP_ITEM = ITEMS.register(GOLD_XP_TAG, () -> new Xp(UpgradeDefaultVariant.GOLD));

    public static final String DIAMOND_XP_TAG = "diamond_xp_upgrade";
    public static final DeferredHolder<Item, Xp> DIAMOND_XP_ITEM = ITEMS.register(DIAMOND_XP_TAG, () -> new Xp(UpgradeDefaultVariant.DIAMOND));

    public static final String NETHERITE_XP_TAG = "netherite_xp_upgrade";
    public static final DeferredHolder<Item, Xp> NETHERITE_XP_ITEM = ITEMS.register(NETHERITE_XP_TAG, () -> new Xp(UpgradeDefaultVariant.NETHERITE));
}
