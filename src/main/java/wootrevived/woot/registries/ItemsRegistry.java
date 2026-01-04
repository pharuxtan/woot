package wootrevived.woot.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wootrevived.woot.Woot;
import wootrevived.woot.guide.GuideBookItem;
import wootrevived.woot.init.Registry;
import wootrevived.woot.items.basic.BasicItem;
import wootrevived.woot.items.dye_casing.DyeCasingItem;
import wootrevived.woot.items.dye_plate.DyePlateItem;
import wootrevived.woot.items.mob_shard.MobShardItem;
import wootrevived.woot.items.mob_shard.MobShardProjectile;
import wootrevived.woot.items.mold.MoldItem;
import wootrevived.woot.items.stygian_hammer.StygianHammerItem;
import wootrevived.woot.items.xp.XpItem;

public class ItemsRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Woot.MOD_NAMESPACE);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Woot.MOD_NAMESPACE);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        ENTITY_TYPES.register(bus);

        for(DeferredHolder<Item, ? extends Item> item : ITEMS.getEntries())
            Registry.addToCreativeTab(item);
    }

    /* Stygian Items */

    public static final String STYGIAN_INGOT_TAG = "stygian_ingot";
    public static final DeferredHolder<Item, BasicItem> STYGIAN_INGOT_ITEM = ITEMS.register(STYGIAN_INGOT_TAG, () -> new BasicItem(BasicItem.Type.STYGIAN_PLATE, STYGIAN_INGOT_TAG));

    public static final String STYGIAN_DUST_TAG = "stygian_dust";
    public static final DeferredHolder<Item, BasicItem> STYGIAN_DUST_ITEM = ITEMS.register(STYGIAN_DUST_TAG, () -> new BasicItem(BasicItem.Type.STYGIAN_DUST, STYGIAN_DUST_TAG));

    public static final String STYGIAN_PLATE_TAG = "stygian_plate";
    public static final DeferredHolder<Item, BasicItem> STYGIAN_PLATE_ITEM = ITEMS.register(STYGIAN_PLATE_TAG, () -> new BasicItem(BasicItem.Type.STYGIAN_PLATE, STYGIAN_PLATE_TAG));

    public static final String STYGIAN_HAMMER_TAG = "stygian_hammer";
    public static final DeferredHolder<Item, StygianHammerItem> STYGIAN_HAMMER_ITEM = ITEMS.register(STYGIAN_HAMMER_TAG, () -> new StygianHammerItem(STYGIAN_HAMMER_TAG));

    /* Molds */

    public static final String PLATE_MOLD_TAG = "plate_mold";
    public static final DeferredHolder<Item, MoldItem> PLATE_MOLD_ITEM = ITEMS.register(PLATE_MOLD_TAG, () -> new MoldItem(MoldItem.MoldType.PLATE, PLATE_MOLD_TAG));

    public static final String SHARD_MOLD_TAG = "shard_mold";
    public static final DeferredHolder<Item, MoldItem> SHARD_MOLD_ITEM = ITEMS.register(SHARD_MOLD_TAG, () -> new MoldItem(MoldItem.MoldType.SHARD, SHARD_MOLD_TAG));

    public static final String DYE_CASING_MOLD_TAG = "dye_casing_mold";
    public static final DeferredHolder<Item, MoldItem> DYE_CASING_MOLD_ITEM = ITEMS.register(DYE_CASING_MOLD_TAG, () -> new MoldItem(MoldItem.MoldType.DYE_CASING, DYE_CASING_MOLD_TAG));

    /* Prism */

    public static final String PRISM_TAG = "prism";
    public static final DeferredHolder<Item, BasicItem> PRISM_ITEM = ITEMS.register(PRISM_TAG, () -> new BasicItem(BasicItem.Type.PRISM, PRISM_TAG));

    /* Mob Shard */

    public static final String MOB_SHARD_TAG = "mob_shard";
    public static final DeferredHolder<Item, MobShardItem> MOB_SHARD_ITEM = ITEMS.register(MOB_SHARD_TAG, () -> new MobShardItem(MOB_SHARD_TAG));
    public static final DeferredHolder<EntityType<?>, EntityType<MobShardProjectile>> MOB_SHARD_PROJECTILE = ENTITY_TYPES.register(MOB_SHARD_TAG, () -> EntityType.Builder.<MobShardProjectile>of(MobShardProjectile::new, MobCategory.MISC).sized(0.5f, 0.5f).build(ResourceKey.create(Registries.ENTITY_TYPE, Woot.identifier(MOB_SHARD_TAG))));

    /* Xp Shard & Splinter */

    public static final String XP_SHARD_TAG = "xp_shard";
    public static final DeferredHolder<Item, XpItem> XP_SHARD_ITEM = ITEMS.register(XP_SHARD_TAG, () -> new XpItem(XpItem.Variant.SHARD, XP_SHARD_TAG));

    public static final String XP_SPLINTER_TAG = "xp_splinter";
    public static final DeferredHolder<Item, XpItem> XP_SPLINTER_ITEM = ITEMS.register(XP_SPLINTER_TAG, () -> new XpItem(XpItem.Variant.SPLINTER, XP_SPLINTER_TAG));

    /* Tier Shards */

    public static final String COPPER_SHARD_TAG = "copper_shard";
    public static final DeferredHolder<Item, BasicItem> COPPER_SHARD_ITEM = ITEMS.register(COPPER_SHARD_TAG, () -> new BasicItem(BasicItem.Type.COPPER_SHARD, COPPER_SHARD_TAG));

    public static final String IRON_SHARD_TAG = "iron_shard";
    public static final DeferredHolder<Item, BasicItem> IRON_SHARD_ITEM = ITEMS.register(IRON_SHARD_TAG, () -> new BasicItem(BasicItem.Type.IRON_SHARD, IRON_SHARD_TAG));

    public static final String GOLD_SHARD_TAG = "gold_shard";
    public static final DeferredHolder<Item, BasicItem> GOLD_SHARD_ITEM = ITEMS.register(GOLD_SHARD_TAG, () -> new BasicItem(BasicItem.Type.GOLD_SHARD, GOLD_SHARD_TAG));

    public static final String DIAMOND_SHARD_TAG = "diamond_shard";
    public static final DeferredHolder<Item, BasicItem> DIAMOND_SHARD_ITEM = ITEMS.register(DIAMOND_SHARD_TAG, () -> new BasicItem(BasicItem.Type.DIAMOND_SHARD, DIAMOND_SHARD_TAG));

    public static final String NETHERITE_SHARD_TAG = "netherite_shard";
    public static final DeferredHolder<Item, BasicItem> NETHERITE_SHARD_ITEM = ITEMS.register(NETHERITE_SHARD_TAG, () -> new BasicItem(BasicItem.Type.NETHERITE_SHARD, NETHERITE_SHARD_TAG));

    /* Tier Enchanted Plates */

    public static final String COPPER_ENCHANTED_PLATE_TAG = "copper_enchanted_plate";
    public static final DeferredHolder<Item, BasicItem> COPPER_ENCHANTED_PLATE_ITEM = ITEMS.register(COPPER_ENCHANTED_PLATE_TAG, () -> new BasicItem(BasicItem.Type.COPPER_ENCHANTED_PLATE, COPPER_ENCHANTED_PLATE_TAG));

    public static final String IRON_ENCHANTED_PLATE_TAG = "iron_enchanted_plate";
    public static final DeferredHolder<Item, BasicItem> IRON_ENCHANTED_PLATE_ITEM = ITEMS.register(IRON_ENCHANTED_PLATE_TAG, () -> new BasicItem(BasicItem.Type.IRON_ENCHANTED_PLATE, IRON_ENCHANTED_PLATE_TAG));

    public static final String GOLD_ENCHANTED_PLATE_TAG = "gold_enchanted_plate";
    public static final DeferredHolder<Item, BasicItem> GOLD_ENCHANTED_PLATE_ITEM = ITEMS.register(GOLD_ENCHANTED_PLATE_TAG, () -> new BasicItem(BasicItem.Type.GOLD_ENCHANTED_PLATE, GOLD_ENCHANTED_PLATE_TAG));

    public static final String DIAMOND_ENCHANTED_PLATE_TAG = "diamond_enchanted_plate";
    public static final DeferredHolder<Item, BasicItem> DIAMOND_ENCHANTED_PLATE_ITEM = ITEMS.register(DIAMOND_ENCHANTED_PLATE_TAG, () -> new BasicItem(BasicItem.Type.DIAMOND_ENCHANTED_PLATE, DIAMOND_ENCHANTED_PLATE_TAG));

    public static final String NETHERITE_ENCHANTED_PLATE_TAG = "netherite_enchanted_plate";
    public static final DeferredHolder<Item, BasicItem> NETHERITE_ENCHANTED_PLATE_ITEM = ITEMS.register(NETHERITE_ENCHANTED_PLATE_TAG, () -> new BasicItem(BasicItem.Type.NETHERITE_ENCHANTED_PLATE, NETHERITE_ENCHANTED_PLATE_TAG));

    /* Dye Plate */

    public static final DeferredHolder<Item, DyePlateItem> WHITE_DYE_PLATE_ITEM = registerDyePlateItem(DyeColor.WHITE);
    public static final DeferredHolder<Item, DyePlateItem> ORANGE_DYE_PLATE_ITEM = registerDyePlateItem(DyeColor.ORANGE);
    public static final DeferredHolder<Item, DyePlateItem> MAGENTA_DYE_PLATE_ITEM = registerDyePlateItem(DyeColor.MAGENTA);
    public static final DeferredHolder<Item, DyePlateItem> LIGHT_BLUE_DYE_PLATE_ITEM = registerDyePlateItem(DyeColor.LIGHT_BLUE);
    public static final DeferredHolder<Item, DyePlateItem> YELLOW_DYE_PLATE_ITEM = registerDyePlateItem(DyeColor.YELLOW);
    public static final DeferredHolder<Item, DyePlateItem> LIME_DYE_PLATE_ITEM = registerDyePlateItem(DyeColor.LIME);
    public static final DeferredHolder<Item, DyePlateItem> PINK_DYE_PLATE_ITEM = registerDyePlateItem(DyeColor.PINK);
    public static final DeferredHolder<Item, DyePlateItem> GRAY_DYE_PLATE_ITEM = registerDyePlateItem(DyeColor.GRAY);
    public static final DeferredHolder<Item, DyePlateItem> LIGHT_GRAY_DYE_PLATE_ITEM = registerDyePlateItem(DyeColor.LIGHT_GRAY);
    public static final DeferredHolder<Item, DyePlateItem> CYAN_DYE_PLATE_ITEM = registerDyePlateItem(DyeColor.CYAN);
    public static final DeferredHolder<Item, DyePlateItem> PURPLE_DYE_PLATE_ITEM = registerDyePlateItem(DyeColor.PURPLE);
    public static final DeferredHolder<Item, DyePlateItem> BLUE_DYE_PLATE_ITEM = registerDyePlateItem(DyeColor.BLUE);
    public static final DeferredHolder<Item, DyePlateItem> BROWN_DYE_PLATE_ITEM = registerDyePlateItem(DyeColor.BROWN);
    public static final DeferredHolder<Item, DyePlateItem> GREEN_DYE_PLATE_ITEM = registerDyePlateItem(DyeColor.GREEN);
    public static final DeferredHolder<Item, DyePlateItem> RED_DYE_PLATE_ITEM = registerDyePlateItem(DyeColor.RED);
    public static final DeferredHolder<Item, DyePlateItem> BLACK_DYE_PLATE_ITEM = registerDyePlateItem(DyeColor.BLACK);

    private static DeferredHolder<Item, DyePlateItem> registerDyePlateItem(DyeColor color) {
        return ItemsRegistry.ITEMS.register(color.getName() + "_dye_plate", () -> new DyePlateItem(color, color.getName() + "_dye_plate"));
    }

    /* Dye Casing */

    public static final DeferredHolder<Item, DyeCasingItem> WHITE_DYE_CASING_ITEM = registerDyeCasingItem(DyeColor.WHITE);
    public static final DeferredHolder<Item, DyeCasingItem> ORANGE_DYE_CASING_ITEM = registerDyeCasingItem(DyeColor.ORANGE);
    public static final DeferredHolder<Item, DyeCasingItem> MAGENTA_DYE_CASING_ITEM = registerDyeCasingItem(DyeColor.MAGENTA);
    public static final DeferredHolder<Item, DyeCasingItem> LIGHT_BLUE_DYE_CASING_ITEM = registerDyeCasingItem(DyeColor.LIGHT_BLUE);
    public static final DeferredHolder<Item, DyeCasingItem> YELLOW_DYE_CASING_ITEM = registerDyeCasingItem(DyeColor.YELLOW);
    public static final DeferredHolder<Item, DyeCasingItem> LIME_DYE_CASING_ITEM = registerDyeCasingItem(DyeColor.LIME);
    public static final DeferredHolder<Item, DyeCasingItem> PINK_DYE_CASING_ITEM = registerDyeCasingItem(DyeColor.PINK);
    public static final DeferredHolder<Item, DyeCasingItem> GRAY_DYE_CASING_ITEM = registerDyeCasingItem(DyeColor.GRAY);
    public static final DeferredHolder<Item, DyeCasingItem> LIGHT_GRAY_DYE_CASING_ITEM = registerDyeCasingItem(DyeColor.LIGHT_GRAY);
    public static final DeferredHolder<Item, DyeCasingItem> CYAN_DYE_CASING_ITEM = registerDyeCasingItem(DyeColor.CYAN);
    public static final DeferredHolder<Item, DyeCasingItem> PURPLE_DYE_CASING_ITEM = registerDyeCasingItem(DyeColor.PURPLE);
    public static final DeferredHolder<Item, DyeCasingItem> BLUE_DYE_CASING_ITEM = registerDyeCasingItem(DyeColor.BLUE);
    public static final DeferredHolder<Item, DyeCasingItem> BROWN_DYE_CASING_ITEM = registerDyeCasingItem(DyeColor.BROWN);
    public static final DeferredHolder<Item, DyeCasingItem> GREEN_DYE_CASING_ITEM = registerDyeCasingItem(DyeColor.GREEN);
    public static final DeferredHolder<Item, DyeCasingItem> RED_DYE_CASING_ITEM = registerDyeCasingItem(DyeColor.RED);
    public static final DeferredHolder<Item, DyeCasingItem> BLACK_DYE_CASING_ITEM = registerDyeCasingItem(DyeColor.BLACK);

    private static DeferredHolder<Item, DyeCasingItem> registerDyeCasingItem(DyeColor color) {
        return ItemsRegistry.ITEMS.register(color.getName() + "_dye_casing", () -> new DyeCasingItem(color, color.getName() + "_dye_casing"));
    }

    /* Guide Book */

    public static final String GUIDE_BOOK_TAG = "guide";
    public static final DeferredHolder<Item, GuideBookItem> GUIDE_BOOK_ITEM = ITEMS.register(GUIDE_BOOK_TAG, () -> new GuideBookItem(GUIDE_BOOK_TAG));
}
