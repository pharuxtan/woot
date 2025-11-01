package wootrevived.woot.datagen.languages;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.registries.ItemsRegistry;
import wootrevived.woot.registries.UpgradeItemsRegistry;
import wootrevived.woot.upgrades.*;

public class English extends LanguageProvider {
    public English(PackOutput output){
        super(output, Woot.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.woot_revived", "Woot Revived");
        add("config.jade.plugin_woot_revived.machines", "Woot Machines Plugin");

        addBlock(BlocksRegistry.STYGIAN_ANVIL_BLOCK.get(), "Stygian Anvil");
        add(ItemsRegistry.STYGIAN_HAMMER_ITEM.get(), "Stygian Hammer");
        add(ItemsRegistry.PLATE_MOLD_ITEM.get(), "Plate Mold");
        add(ItemsRegistry.SHARD_MOLD_ITEM.get(), "Shard Mold");
        add(ItemsRegistry.DYE_CASING_MOLD_ITEM.get(), "Dye Casing Mold");

        addBlock(BlocksRegistry.CREATIVE_POWER_BLOCK.get(), "Creative Power");
        addBlock(BlocksRegistry.CREATIVE_TANK_BLOCK.get(), "Creative Tank");

        addBlock(BlocksRegistry.FACTORY_BASE_BLOCK.get(), "Factory Base");

        addBlock(BlocksRegistry.HEART_BLOCK.get(), "Factory Heart");
        addBlock(BlocksRegistry.FAKE_SPAWNER_BLOCK.get(), "Fake Spawner");

        addBlock(BlocksRegistry.COPPER_CELL_BLOCK.get(), "Copper Vitality Cell");
        addBlock(BlocksRegistry.COPPER_PYLON_BLOCK.get(), "Copper Pylon");
        addBlock(BlocksRegistry.COPPER_PLINTH_BLOCK.get(), "Copper Plinth");
        add(ItemsRegistry.COPPER_ENCHANTED_PLATE_ITEM.get(), "Enchanted Copper Plate");
        add(ItemsRegistry.COPPER_SHARD_ITEM.get(), "Copper Shard");

        addBlock(BlocksRegistry.IRON_CELL_BLOCK.get(), "Iron Vitality Cell");
        addBlock(BlocksRegistry.IRON_PYLON_BLOCK.get(), "Iron Pylon");
        addBlock(BlocksRegistry.IRON_PLINTH_BLOCK.get(), "Iron Plinth");
        add(ItemsRegistry.IRON_ENCHANTED_PLATE_ITEM.get(), "Enchanted Iron Plate");
        add(ItemsRegistry.IRON_SHARD_ITEM.get(), "Iron Shard");

        addBlock(BlocksRegistry.GOLD_CELL_BLOCK.get(), "Gold Vitality Cell");
        addBlock(BlocksRegistry.GOLD_PYLON_BLOCK.get(), "Gold Pylon");
        addBlock(BlocksRegistry.GOLD_PLINTH_BLOCK.get(), "Gold Plinth");
        add(ItemsRegistry.GOLD_ENCHANTED_PLATE_ITEM.get(), "Enchanted Gold Plate");
        add(ItemsRegistry.GOLD_SHARD_ITEM.get(), "Gold Shard");

        addBlock(BlocksRegistry.DIAMOND_CELL_BLOCK.get(), "Diamond Vitality Cell");
        addBlock(BlocksRegistry.DIAMOND_PYLON_BLOCK.get(), "Diamond Pylon");
        addBlock(BlocksRegistry.DIAMOND_PLINTH_BLOCK.get(), "Diamond Plinth");
        add(ItemsRegistry.DIAMOND_ENCHANTED_PLATE_ITEM.get(), "Enchanted Diamond Plate");
        add(ItemsRegistry.DIAMOND_SHARD_ITEM.get(), "Diamond Shard");

        addBlock(BlocksRegistry.NETHERITE_CELL_BLOCK.get(), "Netherite Vitality Cell");
        addBlock(BlocksRegistry.NETHERITE_PYLON_BLOCK.get(), "Netherite Pylon");
        addBlock(BlocksRegistry.NETHERITE_PLINTH_BLOCK.get(), "Netherite Plinth");
        add(ItemsRegistry.NETHERITE_ENCHANTED_PLATE_ITEM.get(), "Enchanted Netherite Plate");
        add(ItemsRegistry.NETHERITE_SHARD_ITEM.get(), "Netherite Shard");

        addBlock(BlocksRegistry.FACTORY_CONNECT_BLOCK.get(), "Factory Connector");
        addBlock(BlocksRegistry.FACTORY_CTR_BASE_PRI_BLOCK.get(), "Primary Base");
        addBlock(BlocksRegistry.FACTORY_CTR_BASE_SEC_BLOCK.get(), "Secondary Base");

        addBlock(BlocksRegistry.IMPORT_BLOCK.get(), "Ingredient Importer");
        addBlock(BlocksRegistry.EXPORT_BLOCK.get(), "Loot Exporter");

        addBlock(BlocksRegistry.FACTORY_UPGRADE_BLOCK.get(), "Upgrade Slot");

        add(Efficiency.COPPER_EFFICIENCY_ITEM.get(), "Efficiency I Upgrade");
        add(Efficiency.IRON_EFFICIENCY_ITEM.get(), "Efficiency II Upgrade");
        add(Efficiency.GOLD_EFFICIENCY_ITEM.get(), "Efficiency III Upgrade");
        add(Efficiency.DIAMOND_EFFICIENCY_ITEM.get(), "Efficiency IV Upgrade");
        add(Efficiency.NETHERITE_EFFICIENCY_ITEM.get(), "Efficiency V Upgrade");
        add("info.woot_revived.upgrade.efficiency.desc.0", "Reduce vitality fuel cost by %d%%");

        add(Looting.COPPER_LOOTING_ITEM.get(), "Looting I Upgrade");
        add(Looting.IRON_LOOTING_ITEM.get(), "Looting II Upgrade");
        add(Looting.GOLD_LOOTING_ITEM.get(), "Looting III Upgrade");
        add(Looting.DIAMOND_LOOTING_ITEM.get(), "Looting IV Upgrade");
        add(Looting.NETHERITE_LOOTING_ITEM.get(), "Looting V Upgrade");
        add("info.woot_revived.upgrade.looting.desc.0", "Apply looting %d effect");

        add(Mass.COPPER_MASS_ITEM.get(), "Mass I Upgrade");
        add(Mass.IRON_MASS_ITEM.get(), "Mass II Upgrade");
        add(Mass.GOLD_MASS_ITEM.get(), "Mass III Upgrade");
        add(Mass.DIAMOND_MASS_ITEM.get(), "Mass IV Upgrade");
        add(Mass.NETHERITE_MASS_ITEM.get(), "Mass V Upgrade");
        add("info.woot_revived.upgrade.mass.desc.0", "Kill %d mobs");

        add(Rate.COPPER_RATE_ITEM.get(), "Rate I Upgrade");
        add(Rate.IRON_RATE_ITEM.get(), "Rate II Upgrade");
        add(Rate.GOLD_RATE_ITEM.get(), "Rate III Upgrade");
        add(Rate.DIAMOND_RATE_ITEM.get(), "Rate IV Upgrade");
        add(Rate.NETHERITE_RATE_ITEM.get(), "Rate V Upgrade");
        add("info.woot_revived.upgrade.rate.desc.0", "Reduce rate by %d%%");

        add(Decapitate.COPPER_DECAPITATE_ITEM.get(), "Decapitate I Upgrade");
        add(Decapitate.IRON_DECAPITATE_ITEM.get(), "Decapitate II Upgrade");
        add(Decapitate.GOLD_DECAPITATE_ITEM.get(), "Decapitate III Upgrade");
        add(Decapitate.DIAMOND_DECAPITATE_ITEM.get(), "Decapitate IV Upgrade");
        add(Decapitate.NETHERITE_DECAPITATE_ITEM.get(), "Decapitate V Upgrade");
        add("info.woot_revived.upgrade.decapitate.desc.0", "Drop %dx mob head");

        add(Xp.COPPER_XP_ITEM.get(), "XP I Upgrade");
        add(Xp.IRON_XP_ITEM.get(), "XP II Upgrade");
        add(Xp.GOLD_XP_ITEM.get(), "XP III Upgrade");
        add(Xp.DIAMOND_XP_ITEM.get(), "XP IV Upgrade");
        add(Xp.NETHERITE_XP_ITEM.get(), "XP V Upgrade");
        add("info.woot_revived.upgrade.xp.desc.0", "Drop %d%% XP in shard form");

        add(ShardDrop.IRON_SHARD_DROP_ITEM.get(), "Shard Drop I Upgrade");
        add(ShardDrop.GOLD_SHARD_DROP_ITEM.get(), "Shard Drop II Upgrade");
        add(ShardDrop.DIAMOND_SHARD_DROP_ITEM.get(), "Shard Drop III Upgrade");
        add(ShardDrop.NETHERITE_SHARD_DROP_ITEM.get(), "Shard Drop IV Upgrade");
        add("info.woot_revived.upgrade.shard_drop.desc.0", "Drop %s Shard and all the precedent");
        add("info.woot_revived.upgrade.shard_drop.desc.1", "Apply at least at the %s tier");
        add("info.woot_revived.upgrade.shard_drop.desc.2", "Shard has %d%% drop chance");

        add(Burn.BURN_ITEM.get(), "Burn Upgrade");
        add("info.woot_revived.upgrade.burn.desc.0", "Apply fire to the simulated mob");

        add(Dimension.NETHER_DIMENSION_ITEM.get(), "Nether Dimension Upgrade");
        add(Dimension.END_DIMENSION_ITEM.get(), "End Dimension Upgrade");
        add("info.woot_revived.upgrade.dimension.desc.nether", "Kill the simulated mob in the nether");
        add("info.woot_revived.upgrade.dimension.desc.end", "Kill the simulated mob in the end");

        add(ItemsRegistry.MOB_SHARD_ITEM.get(), "Mob Shard");
        add(ItemsRegistry.MOB_SHARD_PROJECTILE.get(), "Mob Shard");

        add(ItemsRegistry.XP_SHARD_ITEM.get(), "Experience Shard");
        add(ItemsRegistry.XP_SPLINTER_ITEM.get(), "Experience Splinter");

        addBlock(BlocksRegistry.FLUID_INFUSER_BLOCK.get(), "Fluid Infuser");

        add(ItemsRegistry.STYGIAN_INGOT_ITEM.get(), "Stygian Ingot");
        add(ItemsRegistry.STYGIAN_DUST_ITEM.get(), "Stygian Dust");
        add(ItemsRegistry.STYGIAN_PLATE_ITEM.get(), "Stygian Plate");
        addBlock(BlocksRegistry.STYGIAN_BLOCK.get(), "Stygian Block");
        add(ItemsRegistry.PRISM_ITEM.get(), "Prism");
        add(UpgradeItemsRegistry.UPGRADE_BASE_ITEM.get(), "Upgrade Base");

        addBlock(BlocksRegistry.ITEM_INFUSER_BLOCK.get(), "Item Infuser");
        add(ItemsRegistry.WHITE_DYE_CASING_ITEM.get(), "White Dye Casing");
        add(ItemsRegistry.WHITE_DYE_PLATE_ITEM.get(), "White Dye Plate");
        add(ItemsRegistry.ORANGE_DYE_CASING_ITEM.get(), "Orange Dye Casing");
        add(ItemsRegistry.ORANGE_DYE_PLATE_ITEM.get(), "Orange Dye Plate");
        add(ItemsRegistry.MAGENTA_DYE_CASING_ITEM.get(), "Magenta Dye Casing");
        add(ItemsRegistry.MAGENTA_DYE_PLATE_ITEM.get(), "Magenta Dye Plate");
        add(ItemsRegistry.LIGHT_BLUE_DYE_CASING_ITEM.get(), "Light Blue Dye Casing");
        add(ItemsRegistry.LIGHT_BLUE_DYE_PLATE_ITEM.get(), "Light Blue Dye Plate");
        add(ItemsRegistry.YELLOW_DYE_CASING_ITEM.get(), "Yellow Dye Casing");
        add(ItemsRegistry.YELLOW_DYE_PLATE_ITEM.get(), "Yellow Dye Plate");
        add(ItemsRegistry.LIME_DYE_CASING_ITEM.get(), "Lime Dye Casing");
        add(ItemsRegistry.LIME_DYE_PLATE_ITEM.get(), "Lime Dye Plate");
        add(ItemsRegistry.PINK_DYE_CASING_ITEM.get(), "Pink Dye Casing");
        add(ItemsRegistry.PINK_DYE_PLATE_ITEM.get(), "Pink Dye Plate");
        add(ItemsRegistry.GRAY_DYE_CASING_ITEM.get(), "Gray Dye Casing");
        add(ItemsRegistry.GRAY_DYE_PLATE_ITEM.get(), "Gray Dye Plate");
        add(ItemsRegistry.LIGHT_GRAY_DYE_CASING_ITEM.get(), "Light Gray Dye Casing");
        add(ItemsRegistry.LIGHT_GRAY_DYE_PLATE_ITEM.get(), "Light Gray Dye Plate");
        add(ItemsRegistry.CYAN_DYE_CASING_ITEM.get(), "Cyan Dye Casing");
        add(ItemsRegistry.CYAN_DYE_PLATE_ITEM.get(), "Cyan Dye Plate");
        add(ItemsRegistry.PURPLE_DYE_CASING_ITEM.get(), "Purple Dye Casing");
        add(ItemsRegistry.PURPLE_DYE_PLATE_ITEM.get(), "Purple Dye Plate");
        add(ItemsRegistry.BLUE_DYE_CASING_ITEM.get(), "Blue Dye Casing");
        add(ItemsRegistry.BLUE_DYE_PLATE_ITEM.get(), "Blue Dye Plate");
        add(ItemsRegistry.BROWN_DYE_CASING_ITEM.get(), "Brown Dye Casing");
        add(ItemsRegistry.BROWN_DYE_PLATE_ITEM.get(), "Brown Dye Plate");
        add(ItemsRegistry.GREEN_DYE_CASING_ITEM.get(), "Green Dye Casing");
        add(ItemsRegistry.GREEN_DYE_PLATE_ITEM.get(), "Green Dye Plate");
        add(ItemsRegistry.RED_DYE_CASING_ITEM.get(), "Red Dye Casing");
        add(ItemsRegistry.RED_DYE_PLATE_ITEM.get(), "Red Dye Plate");
        add(ItemsRegistry.BLACK_DYE_CASING_ITEM.get(), "Black Dye Casing");
        add(ItemsRegistry.BLACK_DYE_PLATE_ITEM.get(), "Black Dye Plate");

        add(ItemsRegistry.GUIDE_BOOK_ITEM.get(), "Woot Guide Book");

        addBlock(BlocksRegistry.LAYOUT_BLOCK.get(), "Factory Layout");

        addBlock(BlocksRegistry.DYE_LIQUIFIER_BLOCK.get(), "Dye Liquifier");
        addBlock(BlocksRegistry.ENCHANTED_LIQUIFIER_BLOCK.get(), "Enchanted Liquifier");

        add(FluidsRegistry.VITALITY_FUEL_FLUID_BLOCK.get(), "Vitality Fuel Fluid");
        add(FluidsRegistry.VITALITY_FUEL_FLUID_BUCKET.get(), "Vitality Fuel Bucket");
        add("fluid_type.woot_revived." + FluidsRegistry.VITALITY_FUEL_FLUID_TAG, "Vitality Fuel Fluid");

        add(FluidsRegistry.ENCHANTED_FLUID_BLOCK.get(), "Enchanted Fluid");
        add(FluidsRegistry.ENCHANTED_FLUID_BUCKET.get(), "Enchanted Bucket");
        add("fluid_type.woot_revived." + FluidsRegistry.ENCHANTED_FLUID_TAG, "Enchanted Fluid");

        add(FluidsRegistry.MOB_TEARS_FLUID_BLOCK.get(), "Mob Tears Fluid");
        add(FluidsRegistry.MOB_TEARS_FLUID_BUCKET.get(), "Mob Tears Bucket");
        add("fluid_type.woot_revived." + FluidsRegistry.MOB_TEARS_FLUID_TAG, "Mob Tears Fluid");

        add(FluidsRegistry.PURE_DYE_FLUID_BLOCK.get(), "Pure Dye Fluid");
        add(FluidsRegistry.PURE_DYE_FLUID_BUCKET.get(), "Pure Dye Bucket");
        add("fluid_type.woot_revived." + FluidsRegistry.PURE_DYE_FLUID_TAG, "Pure Dye Fluid");

        add("info.woot_revived.mobshard.unprogrammed.desc", "Capture by attacking or throwing the shard at enemies");
        add("info.woot_revived.mobshard.unprogrammed", "Unprogrammed");
        add("info.woot_revived.mobshard.remaining.desc", "Kill %s to program the shard");
        add("info.woot_revived.mobshard.remaining.desc_no_entity", "Kill the enemy to program the shard");
        add("info.woot_revived.mobshard.remaining", "Partially Programmed (%d/%d Kills)");
        add("info.woot_revived.mobshard.programmed", "Fully Programmed");
        add("info.woot_revived.shard.0", "Right click to gain experience");
        add("info.woot_revived.shard.1", "Sneak right click to consume full stack");

        add("info.woot_revived.cell.amount", "Vitality Fuel Amount");
        add("info.woot_revived.tier", "Tier");
        add("info.woot_revived.power", "Power");
        add("info.woot_revived.output_fluid", "Output Fluid");
        add("info.woot_revived.output_amount", "Output Amount");
        add("info.woot_revived.output", "Output");
        add("info.woot_revived.input", "Input");
        add("info.woot_revived.augment_input", "Augment Input");
        add("info.woot_revived.input_fluid", "Input Fluid");
        add("info.woot_revived.input_amount", "Input Amount");
        add("info.woot_revived.dye.red", "Red");
        add("info.woot_revived.dye.yellow", "Yellow");
        add("info.woot_revived.dye.blue", "Blue");
        add("info.woot_revived.dye.white", "White");
        add("info.woot_revived.base_item", "Base Item");
        add("info.woot_revived.fluid", "Fluid");
        add("info.woot_revived.amount", "Amount");
        add("info.woot_revived.progress", "Progress");
        add("info.woot_revived.drained", "Drained");
        add("info.woot_revived.eta", "ETA");
        add("info.woot_revived.usage", "Usage");
        add("info.woot_revived.cost", "Cost");
        add("info.woot_revived.rate", "Rate");
        add("info.woot_revived.direction", "Direction");
        add("info.woot_revived.action", "Action");
        add("info.woot_revived.empty", "Empty");

        add("info.woot_revived.enchanted_liquifier.input", "Enchanted Book Input");
        add("info.woot_revived.enchanted_liquifier.output", "Enchanted Fluid Output");
        add("info.woot_revived.dye_liquifier.red_input", "Red Dye Input");
        add("info.woot_revived.dye_liquifier.yellow_input", "Yellow Dye Input");
        add("info.woot_revived.dye_liquifier.blue_input", "Blue Dye Input");
        add("info.woot_revived.dye_liquifier.white_input", "White Dye Input");
        add("info.woot_revived.dye_liquifier.output", "Pure Dye Output");

        add("info.woot_revived.factory.invalid", "INVALID");
        add("info.woot_revived.factory.empty", "EMPTY");

        add("chat.woot_revived.anvil.cold", "Anvil must be sitting on a Magma Block");
        add("chat.woot_revived.anvil.nobase", "Place valid base item first");

        add("misc.woot_revived.tier_1", "Copper");
        add("misc.woot_revived.tier_2", "Iron");
        add("misc.woot_revived.tier_3", "Gold");
        add("misc.woot_revived.tier_4", "Diamond");
        add("misc.woot_revived.tier_5", "Netherite");

        add("gui.woot_revived.anvil.name", "Stygian Anvil");
        add("gui.woot_revived.enchanted_liquifier.name", "Enchanted Liquifier");
        add("gui.woot_revived.fluid_infuser.name", "Fluid Infuser");
        add("gui.woot_revived.item_infuser.name", "Item Infuser");
        add("gui.woot_revived.dye_liquifier.name", "Dye Liquifier");
        add("gui.woot_revived.redstone.always_on", "Redstone Mode: Ignored");
        add("gui.woot_revived.redstone.with_no_signal", "Redstone Mode: Run with no redstone signal");
        add("gui.woot_revived.redstone.with_signal", "Redstone Mode: Run with redstone signal");
        add("gui.woot_revived.redstone.once", "Redstone Mode: Run on Pulse");
        add("gui.woot_revived.heart.name", "Factory Heart");
        add("gui.woot_revived.heart.no_tier", "You need to build the factory");
        add("gui.woot_revived.heart.insufficient_tier", "You need the %s tier");
        add("gui.woot_revived.heart.no_secondary", "No secondary fake spawner placed");
        add("gui.woot_revived.heart.vitality_cell_missing", "Vitality Cell is missing");
        add("gui.woot_revived.heart.vitality_cell", "Vitality Cell");

        add("jei.woot_revived.shard", "Generated from the factory with the Shard Drop upgrade installed.");
        add("jei.woot_revived.anvil.0", "Add item by right clicking on the anvil.");
        add("jei.woot_revived.anvil.1", "Use the Stygian Hammer to craft.");
        add("jei.woot_revived.anvil.2", "The Stygian Anvil must be placed on a Magma Block.");
        add("jei.woot_revived.anvil.3", "You need a fully programmed Mob Shard to get a Fake Spawner block.");
        add("jei.woot_revived.mob_shard.0", "Hit the mob with the shard or throw the shard on the mob to start programming it.");
        add("jei.woot_revived.mob_shard.1", "You need to kill the same mob you killed a number of times to get the shard fully programmed.");
        add("jei.woot_revived.mob_shard.2", "Once fully programmed it can be turned into a Fake Spawner with the Stygian Anvil.");
        add("jei.woot_revived.pure_dye_fluid", "Generated using the Dye Liquifier with %dmB of Blue, %dmB of Yellow, %dmB of Red and %dmB of White.");

        add("advancements.woot_revived.root.title", "Woot Revived");
        add("advancements.woot_revived.root.description", "You missed this loot mod right?");
        add("advancements.woot_revived.stygian_ingot.title", "How much for one ingot!?");
        add("advancements.woot_revived.stygian_ingot.description", "Get your first Stygian Ingot");
        add("advancements.woot_revived.stygian_hammer.title", "The only hammer you'll ever need!");
        add("advancements.woot_revived.stygian_hammer.description", "Craft your first Stygian Hammer");
        add("advancements.woot_revived.stygian_anvil.title", "Shape your factory from mobs suffer!");
        add("advancements.woot_revived.stygian_anvil.description", "Craft your first Stygian Anvil");
        add("advancements.woot_revived.shard_mold.title", "Your favorite mold!");
        add("advancements.woot_revived.shard_mold.description", "Craft a Shard Mold on the Stygian Anvil");
        add("advancements.woot_revived.dye_mold.title", "Your most hated mold!");
        add("advancements.woot_revived.dye_mold.description", "Craft a Dye Mold on the Stygian Anvil");
        add("advancements.woot_revived.plate_mold.title", "Another plate mold?");
        add("advancements.woot_revived.plate_mold.description", "Craft a Plate Mold on the Stygian Anvil");
        add("advancements.woot_revived.mob_shard.title", "Absolutely no mobs were armed! Side-eye...");
        add("advancements.woot_revived.mob_shard.description", "Craft a Mob Shard with the Shard Mold on the Stygian Anvil");
        add("advancements.woot_revived.stygian_plate.title", "Served on a Stygian platter");
        add("advancements.woot_revived.stygian_plate.description", "Craft a Stygian Plate with the Plate Mold on the Stygian Anvil");
        add("advancements.woot_revived.factory_base.title", "The origin of all your problems");
        add("advancements.woot_revived.factory_base.description", "Craft a Factory Base");
        add("advancements.woot_revived.fluid_infuser.title", "The origin of all your problems");
        add("advancements.woot_revived.fluid_infuser.description", "Craft a Factory Base");
        add("advancements.woot_revived.mob_tears_bucket.title", "All these tears, so satisfying!");
        add("advancements.woot_revived.mob_tears_bucket.description", "Collect a bucket of Mob Tears Fluid");
        add("advancements.woot_revived.vitality_fuel_bucket.title", "We can get vitality fuel from their tears???");
        add("advancements.woot_revived.vitality_fuel_bucket.description", "Collect a bucket of Vitality Fuel Fluid");
        add("advancements.woot_revived.enchanted_copper_plate.title", "Shiny Copper Plate meh...");
        add("advancements.woot_revived.enchanted_copper_plate.description", "Craft an Enchanted Copper Plate");
        add("advancements.woot_revived.copper_cell.title", "This tank is so small...");
        add("advancements.woot_revived.copper_cell.description", "Craft a Copper Cell");
        add("advancements.woot_revived.enchanted_iron_plate.title", "Shiny Iron Plate alright.");
        add("advancements.woot_revived.enchanted_iron_plate.description", "Craft an Enchanted Iron Plate");
        add("advancements.woot_revived.iron_cell.title", "This tank start to be pretty good.");
        add("advancements.woot_revived.iron_cell.description", "Craft an Iron Cell");
        add("advancements.woot_revived.enchanted_gold_plate.title", "Shiny Gold Plate woah!");
        add("advancements.woot_revived.enchanted_gold_plate.description", "Craft an Enchanted Gold Plate");
        add("advancements.woot_revived.gold_cell.title", "This tank bright so well!");
        add("advancements.woot_revived.gold_cell.description", "Craft a Gold Cell");
        add("advancements.woot_revived.enchanted_diamond_plate.title", "Shiny Diamond Plate so rich!");
        add("advancements.woot_revived.enchanted_diamond_plate.description", "Craft an Enchanted Diamond Plate");
        add("advancements.woot_revived.diamond_cell.title", "This tank seems so luxurious!");
        add("advancements.woot_revived.diamond_cell.description", "Craft a Diamond Cell");
        add("advancements.woot_revived.enchanted_netherite_plate.title", "Shiny Netherite Plate so FIRE!!!");
        add("advancements.woot_revived.enchanted_netherite_plate.description", "Craft an Enchanted Netherite Plate");
        add("advancements.woot_revived.netherite_cell.title", "This tank literally comes from HELL!!!");
        add("advancements.woot_revived.netherite_cell.description", "Craft a Netherite Cell");
        add("advancements.woot_revived.enchanted_liquifier.title", "You want those shiny things right?");
        add("advancements.woot_revived.enchanted_liquifier.description", "Craft an Enchanted Liquifier");
        add("advancements.woot_revived.enchanted_bucket.title", "You got this shiny bucket!");
        add("advancements.woot_revived.enchanted_bucket.description", "Collect a bucket of Enchanted Fluid");
        add("advancements.woot_revived.item_infuser.title", "You'll infuse those items!");
        add("advancements.woot_revived.item_infuser.description", "Craft an Item Infuser");
        add("advancements.woot_revived.dye_liquifier.title", "Shouldn't it be bad to mix all the colors?");
        add("advancements.woot_revived.dye_liquifier.description", "Craft a Dye Liquifier");
        add("advancements.woot_revived.pure_dye.title", "The result is rainbow!!!");
        add("advancements.woot_revived.pure_dye.description", "Collect a bucket of Pure Dye Fluid");
        add("advancements.woot_revived.prism.title", "This glass reflects all the colors!");
        add("advancements.woot_revived.prism.description", "Craft a Prism using the Item Infuser with Glass and Pure Dye Fluid");
        add("advancements.woot_revived.fake_spawner.title", "Does this spawner really exist?");
        add("advancements.woot_revived.fake_spawner.description", "Craft a Fake Spawner");
        add("advancements.woot_revived.black_dye_plate.title", "Start of upgrades adventure!");
        add("advancements.woot_revived.black_dye_plate.description", "Craft a Black Dye Plate");
        add("advancements.woot_revived.upgrade_base.title", "Where are your upgrades belong!");
        add("advancements.woot_revived.upgrade_base.description", "Craft a Black Dye Plate");
        add("advancements.woot_revived.copper_shard.title", "Rusty shard!");
        add("advancements.woot_revived.copper_shard.description", "Craft a Copper Shard on the Stygian Anvil");
        add("advancements.woot_revived.copper_pylon.title", "Your first pylon!");
        add("advancements.woot_revived.copper_pylon.description", "Craft a Copper Pylon");
        add("advancements.woot_revived.copper_plinth.title", "Your first plinth!");
        add("advancements.woot_revived.copper_plinth.description", "Craft a Copper Plinth");
        add("advancements.woot_revived.iron_shard_upgrade.title", "Metal shard upgrade!");
        add("advancements.woot_revived.iron_shard_upgrade.description", "Craft the Iron Shard Upgrade");
        add("advancements.woot_revived.iron_shard.title", "Metal shard!");
        add("advancements.woot_revived.iron_shard.description", "Get an Iron Shard from the factory");
        add("advancements.woot_revived.iron_pylon.title", "Your second pylon!");
        add("advancements.woot_revived.iron_pylon.description", "Craft an Iron Pylon");
        add("advancements.woot_revived.iron_plinth.title", "Your second plinth!");
        add("advancements.woot_revived.iron_plinth.description", "Craft an Iron Plinth");
        add("advancements.woot_revived.gold_shard_upgrade.title", "Shiny shard upgrade!");
        add("advancements.woot_revived.gold_shard_upgrade.description", "Craft the Gold Shard Upgrade");
        add("advancements.woot_revived.gold_shard.title", "Shiny shard!");
        add("advancements.woot_revived.gold_shard.description", "Get a Gold Shard from the factory");
        add("advancements.woot_revived.gold_pylon.title", "Your third pylon!");
        add("advancements.woot_revived.gold_pylon.description", "Craft a Gold Pylon");
        add("advancements.woot_revived.gold_plinth.title", "Your third plinth!");
        add("advancements.woot_revived.gold_plinth.description", "Craft a Gold Plinth");
        add("advancements.woot_revived.diamond_shard_upgrade.title", "Rich shard upgrade!");
        add("advancements.woot_revived.diamond_shard_upgrade.description", "Craft the Diamond Shard Upgrade");
        add("advancements.woot_revived.diamond_shard.title", "Rich shard!");
        add("advancements.woot_revived.diamond_shard.description", "Get a Diamond Shard from the factory");
        add("advancements.woot_revived.diamond_pylon.title", "Your fourth pylon!");
        add("advancements.woot_revived.diamond_pylon.description", "Craft a Diamond Pylon");
        add("advancements.woot_revived.diamond_plinth.title", "Your fourth plinth!");
        add("advancements.woot_revived.diamond_plinth.description", "Craft a Diamond Plinth");
        add("advancements.woot_revived.netherite_shard_upgrade.title", "Hell shard upgrade!");
        add("advancements.woot_revived.netherite_shard_upgrade.description", "Craft the Netherite Shard Upgrade");
        add("advancements.woot_revived.netherite_shard.title", "Hell shard!");
        add("advancements.woot_revived.netherite_shard.description", "Get a Netherite Shard from the factory");
        add("advancements.woot_revived.netherite_pylon.title", "Your fifth and last pylon!");
        add("advancements.woot_revived.netherite_pylon.description", "Craft a Netherite Pylon");
        add("advancements.woot_revived.netherite_plinth.title", "Your fifth and last plinth!");
        add("advancements.woot_revived.netherite_plinth.description", "Craft a Netherite Plinth");
    }

    protected void addBlock(Block block, String translation){
        add(block, translation);
        add(block.asItem(), translation);
    }
}
