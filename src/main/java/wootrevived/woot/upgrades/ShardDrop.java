package wootrevived.woot.upgrades;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.enums.Tier;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.ItemsRegistry;

import javax.annotation.Nullable;
import java.util.List;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class ShardDrop extends WootUpgradeItem {
    public ShardDrop(int level) { super(new Properties(), level); }

    private static final int[] PERCENTAGES = new int[] { 50, 30, 15, 5 };

    @Override
    public void modifyDrops(WootDropsProperties properties, CompoundTag upgradeTag) {
        Tier tier = properties.getFactoryTier();
        List<ItemStack> drops = properties.getItemDrops();
        RandomSource random = properties.getRandom();

        // Iron Shard
        if(!Tier.TIER_1.isFactoryTierValid(tier))
            return;

        float rand = random.nextFloat();
        if(rand <= PERCENTAGES[0] / 100F) {
            drops.add(ItemsRegistry.IRON_SHARD_ITEM.get().getDefaultInstance());
        }

        // Gold Shard
        if(getLevel() < 2 || !Tier.TIER_2.isFactoryTierValid(tier))
            return;

        rand = random.nextFloat();
        if(rand <= PERCENTAGES[1] / 100F) {
            drops.add(ItemsRegistry.GOLD_SHARD_ITEM.get().getDefaultInstance());
        }

        // Diamond Shard
        if(getLevel() < 3 || !Tier.TIER_3.isFactoryTierValid(tier))
            return;

        rand = random.nextFloat();
        if(rand <= PERCENTAGES[2] / 100F) {
            drops.add(ItemsRegistry.DIAMOND_SHARD_ITEM.get().getDefaultInstance());
        }

        // Netherite Shard
        if(getLevel() < 4 || !Tier.TIER_4.isFactoryTierValid(tier))
            return;

        rand = random.nextFloat();
        if(rand <= PERCENTAGES[3] / 100F) {
            drops.add(ItemsRegistry.NETHERITE_SHARD_ITEM.get().getDefaultInstance());
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("info.woot_revived.upgrade.shard_drop.desc.0", Component.translatable("misc.woot_revived.tier_" + (getLevel() + 1))).setStyle(DESCRIPTION_STYLE));
        tooltip.add(Component.translatable("info.woot_revived.upgrade.shard_drop.desc.1", Component.translatable("misc.woot_revived.tier_" + (getLevel()))).setStyle(DESCRIPTION_STYLE));
        tooltip.add(Component.translatable("info.woot_revived.upgrade.shard_drop.desc.2", PERCENTAGES[getLevel() - 1]).setStyle(DESCRIPTION_STYLE));
    }

    /* Upgrade Item registration */

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Woot.MOD_ID);

    public static void register(WootUpgradeItemRegistration registration){
        ITEMS.register(registration.getWootEventBus());
        registration.register(IRON_SHARD_DROP_ITEM);
        registration.register(GOLD_SHARD_DROP_ITEM);
        registration.register(DIAMOND_SHARD_DROP_ITEM);
        registration.register(NETHERITE_SHARD_DROP_ITEM);
    }

    public static final String IRON_SHARD_DROP_TAG = "iron_shard_drop_upgrade";
    public static final DeferredHolder<Item, ShardDrop> IRON_SHARD_DROP_ITEM = ITEMS.register(IRON_SHARD_DROP_TAG, () -> new ShardDrop(1));

    public static final String GOLD_SHARD_DROP_TAG = "gold_shard_drop_upgrade";
    public static final DeferredHolder<Item, ShardDrop> GOLD_SHARD_DROP_ITEM = ITEMS.register(GOLD_SHARD_DROP_TAG, () -> new ShardDrop(2));

    public static final String DIAMOND_SHARD_DROP_TAG = "diamond_shard_drop_upgrade";
    public static final DeferredHolder<Item, ShardDrop> DIAMOND_SHARD_DROP_ITEM = ITEMS.register(DIAMOND_SHARD_DROP_TAG, () -> new ShardDrop(3));

    public static final String NETHERITE_SHARD_DROP_TAG = "netherite_shard_drop_upgrade";
    public static final DeferredHolder<Item, ShardDrop> NETHERITE_SHARD_DROP_ITEM = ITEMS.register(NETHERITE_SHARD_DROP_TAG, () -> new ShardDrop(4));
}
