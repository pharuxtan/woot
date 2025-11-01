package wootrevived.woot.datagen.models;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.registries.ItemsRegistry;
import wootrevived.woot.registries.UpgradeItemsRegistry;
import wootrevived.woot.upgrades.*;
import wootrevived.woot.util.common.WootDyeItem;
import wootrevived.woot.util.fluid.WootFluidType;

import java.util.Optional;

public class Items {
    public static ItemModelGenerators models;

    public static void registerModels(ItemModelGenerators itemModels) {
        models = itemModels;

        itemHandheld(ItemsRegistry.STYGIAN_HAMMER_ITEM);
        itemGenerated(ItemsRegistry.PLATE_MOLD_ITEM);
        itemGenerated(ItemsRegistry.SHARD_MOLD_ITEM);
        itemGenerated(ItemsRegistry.DYE_CASING_MOLD_ITEM);

        itemGenerated(ItemsRegistry.COPPER_ENCHANTED_PLATE_ITEM);
        itemGenerated(ItemsRegistry.COPPER_SHARD_ITEM);

        itemGenerated(ItemsRegistry.IRON_ENCHANTED_PLATE_ITEM);
        itemGenerated(ItemsRegistry.IRON_SHARD_ITEM);

        itemGenerated(ItemsRegistry.GOLD_ENCHANTED_PLATE_ITEM);
        itemGenerated(ItemsRegistry.GOLD_SHARD_ITEM);

        itemGenerated(ItemsRegistry.DIAMOND_ENCHANTED_PLATE_ITEM);
        itemGenerated(ItemsRegistry.DIAMOND_SHARD_ITEM);

        itemGenerated(ItemsRegistry.NETHERITE_ENCHANTED_PLATE_ITEM);
        itemGenerated(ItemsRegistry.NETHERITE_SHARD_ITEM);

        itemGeneratedDye(ItemsRegistry.WHITE_DYE_PLATE_ITEM, "dye_plate");
        itemGeneratedDye(ItemsRegistry.ORANGE_DYE_PLATE_ITEM, "dye_plate");
        itemGeneratedDye(ItemsRegistry.MAGENTA_DYE_PLATE_ITEM, "dye_plate");
        itemGeneratedDye(ItemsRegistry.LIGHT_BLUE_DYE_PLATE_ITEM, "dye_plate");
        itemGeneratedDye(ItemsRegistry.YELLOW_DYE_PLATE_ITEM, "dye_plate");
        itemGeneratedDye(ItemsRegistry.LIME_DYE_PLATE_ITEM, "dye_plate");
        itemGeneratedDye(ItemsRegistry.PINK_DYE_PLATE_ITEM, "dye_plate");
        itemGeneratedDye(ItemsRegistry.GRAY_DYE_PLATE_ITEM, "dye_plate");
        itemGeneratedDye(ItemsRegistry.LIGHT_GRAY_DYE_PLATE_ITEM, "dye_plate");
        itemGeneratedDye(ItemsRegistry.CYAN_DYE_PLATE_ITEM, "dye_plate");
        itemGeneratedDye(ItemsRegistry.PURPLE_DYE_PLATE_ITEM, "dye_plate");
        itemGeneratedDye(ItemsRegistry.BLUE_DYE_PLATE_ITEM, "dye_plate");
        itemGeneratedDye(ItemsRegistry.BROWN_DYE_PLATE_ITEM, "dye_plate");
        itemGeneratedDye(ItemsRegistry.GREEN_DYE_PLATE_ITEM, "dye_plate");
        itemGeneratedDye(ItemsRegistry.RED_DYE_PLATE_ITEM, "dye_plate");
        itemGeneratedDye(ItemsRegistry.BLACK_DYE_PLATE_ITEM, "dye_plate");

        itemGeneratedDye(ItemsRegistry.WHITE_DYE_CASING_ITEM, "dye_casing");
        itemGeneratedDye(ItemsRegistry.ORANGE_DYE_CASING_ITEM, "dye_casing");
        itemGeneratedDye(ItemsRegistry.MAGENTA_DYE_CASING_ITEM, "dye_casing");
        itemGeneratedDye(ItemsRegistry.LIGHT_BLUE_DYE_CASING_ITEM, "dye_casing");
        itemGeneratedDye(ItemsRegistry.YELLOW_DYE_CASING_ITEM, "dye_casing");
        itemGeneratedDye(ItemsRegistry.LIME_DYE_CASING_ITEM, "dye_casing");
        itemGeneratedDye(ItemsRegistry.PINK_DYE_CASING_ITEM, "dye_casing");
        itemGeneratedDye(ItemsRegistry.GRAY_DYE_CASING_ITEM, "dye_casing");
        itemGeneratedDye(ItemsRegistry.LIGHT_GRAY_DYE_CASING_ITEM, "dye_casing");
        itemGeneratedDye(ItemsRegistry.CYAN_DYE_CASING_ITEM, "dye_casing");
        itemGeneratedDye(ItemsRegistry.PURPLE_DYE_CASING_ITEM, "dye_casing");
        itemGeneratedDye(ItemsRegistry.BLUE_DYE_CASING_ITEM, "dye_casing");
        itemGeneratedDye(ItemsRegistry.BROWN_DYE_CASING_ITEM, "dye_casing");
        itemGeneratedDye(ItemsRegistry.GREEN_DYE_CASING_ITEM, "dye_casing");
        itemGeneratedDye(ItemsRegistry.RED_DYE_CASING_ITEM, "dye_casing");
        itemGeneratedDye(ItemsRegistry.BLACK_DYE_CASING_ITEM, "dye_casing");

        itemGenerated(ItemsRegistry.STYGIAN_INGOT_ITEM);
        itemGenerated(ItemsRegistry.STYGIAN_DUST_ITEM);
        itemGenerated(ItemsRegistry.STYGIAN_PLATE_ITEM);
        itemGenerated(ItemsRegistry.PRISM_ITEM);
        itemGenerated(UpgradeItemsRegistry.UPGRADE_BASE_ITEM);

        itemGenerated(Efficiency.COPPER_EFFICIENCY_ITEM);
        itemGenerated(Efficiency.IRON_EFFICIENCY_ITEM);
        itemGenerated(Efficiency.GOLD_EFFICIENCY_ITEM);
        itemGenerated(Efficiency.DIAMOND_EFFICIENCY_ITEM);
        itemGenerated(Efficiency.NETHERITE_EFFICIENCY_ITEM);

        itemGenerated(Looting.COPPER_LOOTING_ITEM);
        itemGenerated(Looting.IRON_LOOTING_ITEM);
        itemGenerated(Looting.GOLD_LOOTING_ITEM);
        itemGenerated(Looting.DIAMOND_LOOTING_ITEM);
        itemGenerated(Looting.NETHERITE_LOOTING_ITEM);

        itemGenerated(Mass.COPPER_MASS_ITEM);
        itemGenerated(Mass.IRON_MASS_ITEM);
        itemGenerated(Mass.GOLD_MASS_ITEM);
        itemGenerated(Mass.DIAMOND_MASS_ITEM);
        itemGenerated(Mass.NETHERITE_MASS_ITEM);

        itemGenerated(Rate.COPPER_RATE_ITEM);
        itemGenerated(Rate.IRON_RATE_ITEM);
        itemGenerated(Rate.GOLD_RATE_ITEM);
        itemGenerated(Rate.DIAMOND_RATE_ITEM);
        itemGenerated(Rate.NETHERITE_RATE_ITEM);

        itemGenerated(Decapitate.COPPER_DECAPITATE_ITEM);
        itemGenerated(Decapitate.IRON_DECAPITATE_ITEM);
        itemGenerated(Decapitate.GOLD_DECAPITATE_ITEM);
        itemGenerated(Decapitate.DIAMOND_DECAPITATE_ITEM);
        itemGenerated(Decapitate.NETHERITE_DECAPITATE_ITEM);

        itemGenerated(Xp.COPPER_XP_ITEM);
        itemGenerated(Xp.IRON_XP_ITEM);
        itemGenerated(Xp.GOLD_XP_ITEM);
        itemGenerated(Xp.DIAMOND_XP_ITEM);
        itemGenerated(Xp.NETHERITE_XP_ITEM);

        itemGenerated(ShardDrop.IRON_SHARD_DROP_ITEM);
        itemGenerated(ShardDrop.GOLD_SHARD_DROP_ITEM);
        itemGenerated(ShardDrop.DIAMOND_SHARD_DROP_ITEM);
        itemGenerated(ShardDrop.NETHERITE_SHARD_DROP_ITEM);

        itemGenerated(Burn.BURN_ITEM);

        itemGenerated(Dimension.NETHER_DIMENSION_ITEM);
        itemGenerated(Dimension.END_DIMENSION_ITEM);

        itemGenerated(ItemsRegistry.MOB_SHARD_ITEM);
        itemGenerated(ItemsRegistry.XP_SHARD_ITEM);
        itemGenerated(ItemsRegistry.XP_SPLINTER_ITEM);

        itemGenerated(ItemsRegistry.GUIDE_BOOK_ITEM);

        itemBucket(FluidsRegistry.VITALITY_FUEL_FLUID_BUCKET, FluidsRegistry.SOURCE_VITALITY_FUEL_FLUID, FluidsRegistry.VITALITY_FUEL_FLUID_TYPE);
        itemBucket(FluidsRegistry.PURE_DYE_FLUID_BUCKET, FluidsRegistry.SOURCE_PURE_DYE_FLUID, FluidsRegistry.PURE_DYE_FLUID_TYPE);
        itemBucket(FluidsRegistry.ENCHANTED_FLUID_BUCKET, FluidsRegistry.SOURCE_ENCHANTED_FLUID, FluidsRegistry.ENCHANTED_FLUID_TYPE);
        itemBucket(FluidsRegistry.MOB_TEARS_FLUID_BUCKET, FluidsRegistry.SOURCE_MOB_TEARS_FLUID, FluidsRegistry.MOB_TEARS_FLUID_TYPE);
    }

    public static void itemBucket(DeferredHolder<Item, ? extends Item> item, DeferredHolder<Fluid, ? extends Fluid> fluid, DeferredHolder<FluidType, ? extends WootFluidType> type){
        DynamicFluidContainerModel.Unbaked model = new DynamicFluidContainerModel.Unbaked(
                new DynamicFluidContainerModel.Textures(
                        Optional.of(ResourceLocation.withDefaultNamespace("item/bucket")),
                        Optional.of(ResourceLocation.withDefaultNamespace("item/bucket")),
                        Optional.of(ResourceLocation.fromNamespaceAndPath("neoforge", "item/mask/bucket_fluid")),
                        Optional.of(ResourceLocation.fromNamespaceAndPath("neoforge", "item/mask/bucket_fluid_cover"))
                ),
                fluid.get(),
                true,
                true,
                false
        );

        models.itemModelOutput.accept(item.get(), model);
    }

    public static void itemGenerated(DeferredHolder<Item, ? extends Item> item){
        ItemModel.Unbaked model = ItemModelUtils.plainModel(models.createFlatItemModel(item.get(), ModelTemplates.FLAT_ITEM));
        models.itemModelOutput.accept(item.get(), model);
    }

    public static void itemGeneratedDye(DeferredHolder<Item, ? extends WootDyeItem> item, String texture){
        ItemModel.Unbaked model = ItemModelUtils.tintedModel(ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item.get()), TextureMapping.layer0(Woot.location("item/"+texture)), models.modelOutput), ItemModelUtils.constantTint(item.get().getColor()));
        models.itemModelOutput.accept(item.get(), model);
    }

    public static void itemHandheld(DeferredHolder<Item, ? extends Item> item){
        ItemModel.Unbaked model = ItemModelUtils.plainModel(models.createFlatItemModel(item.get(), ModelTemplates.FLAT_HANDHELD_ITEM));
        models.itemModelOutput.accept(item.get(), model);
    }
}
