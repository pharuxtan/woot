package wootrevived.woot.upgrades;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.interfaces.WootSpawnProperties;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;

import javax.annotation.Nullable;
import java.util.List;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class Decapitate extends WootUpgradeItem {
    public Decapitate(int level) { super(new Properties(), level); }

    @Override
    public void applySpawnProperties(WootSpawnProperties properties, CompoundTag itemTag) {
        properties.setDoSimulateChargedCreeper(true);
    }

    Ingredient vanillaHeads = Ingredient.of(
            Items.ZOMBIE_HEAD,
            Items.CREEPER_HEAD,
            Items.PIGLIN_HEAD,
            Items.DRAGON_HEAD,
            Items.SKELETON_SKULL,
            Items.WITHER_SKELETON_SKULL
    );

    @Override
    public void modifyDrops(WootDropsProperties properties, CompoundTag itemTag) {
        List<ItemStack> drops = properties.getItemDrops();

        for(ItemStack drop : drops){
            if(vanillaHeads.test(drop))
                drop.grow(getLevel() - 1);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("info.woot_revived.upgrade.decapitate.desc.0", getLevel()).setStyle(DESCRIPTION_STYLE));
    }

    /* Upgrade Item registration */

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Woot.MOD_ID);

    public static void register(WootUpgradeItemRegistration registration){
        ITEMS.register(registration.getWootEventBus());
        registration.register(COPPER_DECAPITATE_ITEM);
        registration.register(IRON_DECAPITATE_ITEM);
        registration.register(GOLD_DECAPITATE_ITEM);
        registration.register(DIAMOND_DECAPITATE_ITEM);
        registration.register(NETHERITE_DECAPITATE_ITEM);
    }

    public static final String COPPER_DECAPITATE_TAG = "copper_decapitate_upgrade";
    public static final DeferredHolder<Item, Decapitate> COPPER_DECAPITATE_ITEM = ITEMS.register(COPPER_DECAPITATE_TAG, () -> new Decapitate(1));

    public static final String IRON_DECAPITATE_TAG = "iron_decapitate_upgrade";
    public static final DeferredHolder<Item, Decapitate> IRON_DECAPITATE_ITEM = ITEMS.register(IRON_DECAPITATE_TAG, () -> new Decapitate(2));

    public static final String GOLD_DECAPITATE_TAG = "gold_decapitate_upgrade";
    public static final DeferredHolder<Item, Decapitate> GOLD_DECAPITATE_ITEM = ITEMS.register(GOLD_DECAPITATE_TAG, () -> new Decapitate(3));

    public static final String DIAMOND_DECAPITATE_TAG = "diamond_decapitate_upgrade";
    public static final DeferredHolder<Item, Decapitate> DIAMOND_DECAPITATE_ITEM = ITEMS.register(DIAMOND_DECAPITATE_TAG, () -> new Decapitate(4));

    public static final String NETHERITE_DECAPITATE_TAG = "netherite_decapitate_upgrade";
    public static final DeferredHolder<Item, Decapitate> NETHERITE_DECAPITATE_ITEM = ITEMS.register(NETHERITE_DECAPITATE_TAG, () -> new Decapitate(5));
}
