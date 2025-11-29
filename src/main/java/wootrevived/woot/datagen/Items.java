package wootrevived.woot.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wootrevived.woot.Woot;
import wootrevived.api.models.DynamicUpgradeItemModelBuilder;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.registries.ItemsRegistry;
import wootrevived.woot.registries.UpgradeItemsRegistry;
import wootrevived.woot.upgrades.*;

import java.util.Objects;

public class Items extends ItemModelProvider {
    public Items(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, Woot.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        itemHandheld(ItemsRegistry.STYGIAN_HAMMER_ITEM);
        itemGenerated(ItemsRegistry.PLATE_MOLD_ITEM);
        itemGenerated(ItemsRegistry.SHARD_MOLD_ITEM);
        itemGenerated(ItemsRegistry.DYE_CASING_MOLD_ITEM);
        parentedBlock(BlocksRegistry.STYGIAN_ANVIL_BLOCK_ITEM, BlocksRegistry.STYGIAN_ANVIL_BLOCK);

        parentedBlock(BlocksRegistry.FACTORY_BASE_BLOCK_ITEM, BlocksRegistry.FACTORY_BASE_BLOCK);
        parentedBlock(BlocksRegistry.STYGIAN_BLOCK_ITEM, BlocksRegistry.STYGIAN_BLOCK);

        parentedBlock(BlocksRegistry.COPPER_MAGMATOR_BLOCK_ITEM, BlocksRegistry.COPPER_MAGMATOR_BLOCK);
        parentedBlock(BlocksRegistry.IRON_MAGMATOR_BLOCK_ITEM, BlocksRegistry.IRON_MAGMATOR_BLOCK);
        parentedBlock(BlocksRegistry.GOLD_MAGMATOR_BLOCK_ITEM, BlocksRegistry.GOLD_MAGMATOR_BLOCK);
        parentedBlock(BlocksRegistry.DIAMOND_MAGMATOR_BLOCK_ITEM, BlocksRegistry.DIAMOND_MAGMATOR_BLOCK);
        parentedBlock(BlocksRegistry.NETHERITE_MAGMATOR_BLOCK_ITEM, BlocksRegistry.NETHERITE_MAGMATOR_BLOCK);

        parentedBlock(BlocksRegistry.HEART_BLOCK_ITEM, BlocksRegistry.HEART_BLOCK);
        parentedBlock(BlocksRegistry.FAKE_SPAWNER_BLOCK_ITEM, BlocksRegistry.FAKE_SPAWNER_BLOCK);

        parentedBlock(BlocksRegistry.COPPER_CELL_BLOCK_ITEM, BlocksRegistry.COPPER_CELL_BLOCK);
        parentedBlock(BlocksRegistry.COPPER_PYLON_BLOCK_ITEM, BlocksRegistry.COPPER_PYLON_BLOCK);
        parentedBlock(BlocksRegistry.COPPER_PLINTH_BLOCK_ITEM, BlocksRegistry.COPPER_PLINTH_BLOCK);
        itemGenerated(ItemsRegistry.COPPER_ENCHANTED_PLATE_ITEM);
        itemGenerated(ItemsRegistry.COPPER_SHARD_ITEM);

        parentedBlock(BlocksRegistry.IRON_CELL_BLOCK_ITEM, BlocksRegistry.IRON_CELL_BLOCK);
        parentedBlock(BlocksRegistry.IRON_PYLON_BLOCK_ITEM, BlocksRegistry.IRON_PYLON_BLOCK);
        parentedBlock(BlocksRegistry.IRON_PLINTH_BLOCK_ITEM, BlocksRegistry.IRON_PLINTH_BLOCK);
        itemGenerated(ItemsRegistry.IRON_ENCHANTED_PLATE_ITEM);
        itemGenerated(ItemsRegistry.IRON_SHARD_ITEM);

        parentedBlock(BlocksRegistry.GOLD_CELL_BLOCK_ITEM, BlocksRegistry.GOLD_CELL_BLOCK);
        parentedBlock(BlocksRegistry.GOLD_PYLON_BLOCK_ITEM, BlocksRegistry.GOLD_PYLON_BLOCK);
        parentedBlock(BlocksRegistry.GOLD_PLINTH_BLOCK_ITEM, BlocksRegistry.GOLD_PLINTH_BLOCK);
        itemGenerated(ItemsRegistry.GOLD_ENCHANTED_PLATE_ITEM);
        itemGenerated(ItemsRegistry.GOLD_SHARD_ITEM);

        parentedBlock(BlocksRegistry.DIAMOND_CELL_BLOCK_ITEM, BlocksRegistry.DIAMOND_CELL_BLOCK);
        parentedBlock(BlocksRegistry.DIAMOND_PYLON_BLOCK_ITEM, BlocksRegistry.DIAMOND_PYLON_BLOCK);
        parentedBlock(BlocksRegistry.DIAMOND_PLINTH_BLOCK_ITEM, BlocksRegistry.DIAMOND_PLINTH_BLOCK);
        itemGenerated(ItemsRegistry.DIAMOND_ENCHANTED_PLATE_ITEM);
        itemGenerated(ItemsRegistry.DIAMOND_SHARD_ITEM);

        parentedBlock(BlocksRegistry.NETHERITE_CELL_BLOCK_ITEM, BlocksRegistry.NETHERITE_CELL_BLOCK);
        parentedBlock(BlocksRegistry.NETHERITE_PYLON_BLOCK_ITEM, BlocksRegistry.NETHERITE_PYLON_BLOCK);
        parentedBlock(BlocksRegistry.NETHERITE_PLINTH_BLOCK_ITEM, BlocksRegistry.NETHERITE_PLINTH_BLOCK);
        itemGenerated(ItemsRegistry.NETHERITE_ENCHANTED_PLATE_ITEM);
        itemGenerated(ItemsRegistry.NETHERITE_SHARD_ITEM);

        parentedBlock(BlocksRegistry.FACTORY_CONNECT_BLOCK_ITEM, BlocksRegistry.FACTORY_CONNECT_BLOCK);
        parentedBlock(BlocksRegistry.FACTORY_CTR_BASE_PRI_BLOCK_ITEM, BlocksRegistry.FACTORY_CTR_BASE_PRI_BLOCK);
        parentedBlock(BlocksRegistry.FACTORY_CTR_BASE_SEC_BLOCK_ITEM,  BlocksRegistry.FACTORY_CTR_BASE_SEC_BLOCK);
        parentedBlock(BlocksRegistry.IMPORT_BLOCK_ITEM, BlocksRegistry.IMPORT_BLOCK);
        parentedBlock(BlocksRegistry.EXPORT_BLOCK_ITEM, BlocksRegistry.EXPORT_BLOCK);
        parentedBlock(BlocksRegistry.FACTORY_UPGRADE_BLOCK_ITEM, BlocksRegistry.FACTORY_UPGRADE_BLOCK);

        parentedBlock(BlocksRegistry.FLUID_INFUSER_BLOCK_ITEM, BlocksRegistry.FLUID_INFUSER_BLOCK);
        parentedBlock(BlocksRegistry.ITEM_INFUSER_BLOCK_ITEM,  BlocksRegistry.ITEM_INFUSER_BLOCK);
        parentedBlock(BlocksRegistry.LAYOUT_BLOCK_ITEM, BlocksRegistry.LAYOUT_BLOCK);
        parentedBlock(BlocksRegistry.DYE_LIQUIFIER_BLOCK_ITEM, BlocksRegistry.DYE_LIQUIFIER_BLOCK);
        parentedBlock(BlocksRegistry.ENCHANTED_LIQUIFIER_BLOCK_ITEM, BlocksRegistry.ENCHANTED_LIQUIFIER_BLOCK);

        parentedBlock(BlocksRegistry.CREATIVE_POWER_BLOCK_ITEM, BlocksRegistry.CREATIVE_POWER_BLOCK);
        parentedBlock(BlocksRegistry.CREATIVE_TANK_BLOCK_ITEM, BlocksRegistry.CREATIVE_TANK_BLOCK);

        itemGenerated(ItemsRegistry.WHITE_DYE_PLATE_ITEM, Woot.location("item/dye_plate"));
        itemGenerated(ItemsRegistry.ORANGE_DYE_PLATE_ITEM, Woot.location("item/dye_plate"));
        itemGenerated(ItemsRegistry.MAGENTA_DYE_PLATE_ITEM, Woot.location("item/dye_plate"));
        itemGenerated(ItemsRegistry.LIGHT_BLUE_DYE_PLATE_ITEM, Woot.location("item/dye_plate"));
        itemGenerated(ItemsRegistry.YELLOW_DYE_PLATE_ITEM, Woot.location("item/dye_plate"));
        itemGenerated(ItemsRegistry.LIME_DYE_PLATE_ITEM, Woot.location("item/dye_plate"));
        itemGenerated(ItemsRegistry.PINK_DYE_PLATE_ITEM, Woot.location("item/dye_plate"));
        itemGenerated(ItemsRegistry.GRAY_DYE_PLATE_ITEM, Woot.location("item/dye_plate"));
        itemGenerated(ItemsRegistry.LIGHT_GRAY_DYE_PLATE_ITEM, Woot.location("item/dye_plate"));
        itemGenerated(ItemsRegistry.CYAN_DYE_PLATE_ITEM, Woot.location("item/dye_plate"));
        itemGenerated(ItemsRegistry.PURPLE_DYE_PLATE_ITEM, Woot.location("item/dye_plate"));
        itemGenerated(ItemsRegistry.BLUE_DYE_PLATE_ITEM, Woot.location("item/dye_plate"));
        itemGenerated(ItemsRegistry.BROWN_DYE_PLATE_ITEM, Woot.location("item/dye_plate"));
        itemGenerated(ItemsRegistry.GREEN_DYE_PLATE_ITEM, Woot.location("item/dye_plate"));
        itemGenerated(ItemsRegistry.RED_DYE_PLATE_ITEM, Woot.location("item/dye_plate"));
        itemGenerated(ItemsRegistry.BLACK_DYE_PLATE_ITEM, Woot.location("item/dye_plate"));

        itemGenerated(ItemsRegistry.WHITE_DYE_CASING_ITEM, Woot.location("item/dye_casing"));
        itemGenerated(ItemsRegistry.ORANGE_DYE_CASING_ITEM, Woot.location("item/dye_casing"));
        itemGenerated(ItemsRegistry.MAGENTA_DYE_CASING_ITEM, Woot.location("item/dye_casing"));
        itemGenerated(ItemsRegistry.LIGHT_BLUE_DYE_CASING_ITEM, Woot.location("item/dye_casing"));
        itemGenerated(ItemsRegistry.YELLOW_DYE_CASING_ITEM, Woot.location("item/dye_casing"));
        itemGenerated(ItemsRegistry.LIME_DYE_CASING_ITEM, Woot.location("item/dye_casing"));
        itemGenerated(ItemsRegistry.PINK_DYE_CASING_ITEM, Woot.location("item/dye_casing"));
        itemGenerated(ItemsRegistry.GRAY_DYE_CASING_ITEM, Woot.location("item/dye_casing"));
        itemGenerated(ItemsRegistry.LIGHT_GRAY_DYE_CASING_ITEM, Woot.location("item/dye_casing"));
        itemGenerated(ItemsRegistry.CYAN_DYE_CASING_ITEM, Woot.location("item/dye_casing"));
        itemGenerated(ItemsRegistry.PURPLE_DYE_CASING_ITEM, Woot.location("item/dye_casing"));
        itemGenerated(ItemsRegistry.BLUE_DYE_CASING_ITEM, Woot.location("item/dye_casing"));
        itemGenerated(ItemsRegistry.BROWN_DYE_CASING_ITEM, Woot.location("item/dye_casing"));
        itemGenerated(ItemsRegistry.GREEN_DYE_CASING_ITEM, Woot.location("item/dye_casing"));
        itemGenerated(ItemsRegistry.RED_DYE_CASING_ITEM, Woot.location("item/dye_casing"));
        itemGenerated(ItemsRegistry.BLACK_DYE_CASING_ITEM, Woot.location("item/dye_casing"));

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

        itemBucket(FluidsRegistry.VITALITY_FUEL_FLUID_BUCKET, FluidsRegistry.SOURCE_VITALITY_FUEL_FLUID);
        itemBucket(FluidsRegistry.PURE_DYE_FLUID_BUCKET, FluidsRegistry.SOURCE_PURE_DYE_FLUID);
        itemBucket(FluidsRegistry.ENCHANTED_FLUID_BUCKET, FluidsRegistry.SOURCE_ENCHANTED_FLUID);
        itemBucket(FluidsRegistry.MOB_TEARS_FLUID_BUCKET, FluidsRegistry.SOURCE_MOB_TEARS_FLUID);
    }

    public ResourceLocation getItemResource(Item item){
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).withPrefix("item/");
    }

    public ResourceLocation getBlockResource(Block block){
        return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).withPrefix("block/");
    }

    public void itemBucket(RegistryObject<?> item, RegistryObject<?> fluid){
        ResourceLocation itemResource = getItemResource((Item) item.get());
        getBuilder(itemResource.getPath())
                .parent(getExistingFile(ResourceLocation.tryBuild("forge", "item/bucket")))
                .customLoader(DynamicFluidContainerModelBuilder::begin)
                .fluid((Fluid)fluid.get());
    }

    public void parentedBlock(RegistryObject<?> item, RegistryObject<?> block){
        ResourceLocation itemResource = getItemResource((Item) item.get());
        ResourceLocation blockResource = getBlockResource((Block) block.get());
        getBuilder(itemResource.getPath())
                .parent(getExistingFile(blockResource));
    }

    public void itemGenerated(RegistryObject<?> item){
        ResourceLocation itemResource = getItemResource((Item) item.get());
        getBuilder(itemResource.getPath())
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", itemResource);
    }

    public void itemGenerated(RegistryObject<?> item, ResourceLocation texture){
        ResourceLocation itemResource = getItemResource((Item) item.get());
        getBuilder(itemResource.getPath())
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", texture);
    }

    public void itemHandheld(RegistryObject<?> item){
        ResourceLocation itemResource = getItemResource((Item) item.get());
        getBuilder(itemResource.getPath())
                .parent(getExistingFile(mcLoc("item/handheld")))
                .texture("layer0", itemResource);
    }

    public void itemUpgrade(RegistryObject<?> item){
        ResourceLocation itemResource = getItemResource((Item) item.get());
        getBuilder(itemResource.getPath())
                .parent(getExistingFile(mcLoc("item/generated")))
                .customLoader(DynamicUpgradeItemModelBuilder::begin);
    }
}
