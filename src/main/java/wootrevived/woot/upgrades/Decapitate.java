package wootrevived.woot.upgrades;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.enums.UpgradeDefaultVariant;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.interfaces.WootSpawnProperties;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;

import javax.annotation.Nullable;
import java.util.List;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class Decapitate extends WootUpgradeItem<UpgradeDefaultVariant> {
    public Decapitate(UpgradeDefaultVariant variant) {
        super(new Properties(), variant);
    }

    @Override
    public void applySpawnProperties(@NotNull WootSpawnProperties properties, @NotNull CompoundTag itemTag) {
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
    public void modifyDrops(@NotNull WootDropsProperties properties, @NotNull CompoundTag itemTag) {
        List<ItemStack> drops = properties.getItemDrops();

        for(ItemStack drop : drops){
            if(vanillaHeads.test(drop))
                drop.grow(getVariant(itemTag).level() - 1);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("info.woot_revived.upgrade.decapitate.desc.0", getVariant(stack.getTag()).level()).setStyle(DESCRIPTION_STYLE));
    }

    /* Upgrade Item registration */

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.Keys.ITEMS, Woot.MOD_ID);

    public static void register(WootUpgradeItemRegistration registration){
        ITEMS.register(registration.getWootEventBus());
        registration.register(COPPER_DECAPITATE_ITEM);
        registration.register(IRON_DECAPITATE_ITEM);
        registration.register(GOLD_DECAPITATE_ITEM);
        registration.register(DIAMOND_DECAPITATE_ITEM);
        registration.register(NETHERITE_DECAPITATE_ITEM);
    }

    public static final String COPPER_DECAPITATE_TAG = "copper_decapitate_upgrade";
    public static final RegistryObject<Decapitate> COPPER_DECAPITATE_ITEM = ITEMS.register(COPPER_DECAPITATE_TAG, () -> new Decapitate(UpgradeDefaultVariant.COPPER));

    public static final String IRON_DECAPITATE_TAG = "iron_decapitate_upgrade";
    public static final RegistryObject<Decapitate> IRON_DECAPITATE_ITEM = ITEMS.register(IRON_DECAPITATE_TAG, () -> new Decapitate(UpgradeDefaultVariant.IRON));

    public static final String GOLD_DECAPITATE_TAG = "gold_decapitate_upgrade";
    public static final RegistryObject<Decapitate> GOLD_DECAPITATE_ITEM = ITEMS.register(GOLD_DECAPITATE_TAG, () -> new Decapitate(UpgradeDefaultVariant.GOLD));

    public static final String DIAMOND_DECAPITATE_TAG = "diamond_decapitate_upgrade";
    public static final RegistryObject<Decapitate> DIAMOND_DECAPITATE_ITEM = ITEMS.register(DIAMOND_DECAPITATE_TAG, () -> new Decapitate(UpgradeDefaultVariant.DIAMOND));

    public static final String NETHERITE_DECAPITATE_TAG = "netherite_decapitate_upgrade";
    public static final RegistryObject<Decapitate> NETHERITE_DECAPITATE_ITEM = ITEMS.register(NETHERITE_DECAPITATE_TAG, () -> new Decapitate(UpgradeDefaultVariant.NETHERITE));
}
