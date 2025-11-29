package wootrevived.woot.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wootrevived.woot.Woot;
import wootrevived.woot.blocks.cell.CellBlock;
import wootrevived.woot.blocks.cell.CellBlockEntity;
import wootrevived.woot.blocks.creative_power.CreativePowerBlock;
import wootrevived.woot.blocks.creative_power.CreativePowerBlockEntity;
import wootrevived.woot.blocks.creative_tank.CreativeTankBlock;
import wootrevived.woot.blocks.creative_tank.CreativeTankBlockEntity;
import wootrevived.woot.blocks.dye_liquifier.DyeLiquifierBlock;
import wootrevived.woot.blocks.dye_liquifier.DyeLiquifierBlockEntity;
import wootrevived.woot.blocks.enchanted_liquifier.EnchantedLiquifierBlock;
import wootrevived.woot.blocks.enchanted_liquifier.EnchantedLiquifierBlockEntity;
import wootrevived.woot.blocks.factory.FactoryBlock;
import wootrevived.woot.blocks.factory.FactoryBlockItem;
import wootrevived.woot.blocks.factory_base.FactoryBaseBlock;
import wootrevived.woot.blocks.factory_upgrade.FactoryUpgradeBlock;
import wootrevived.woot.blocks.factory_upgrade.FactoryUpgradeBlockEntity;
import wootrevived.woot.blocks.fake_spawner.FakeSpawnerBlockEntity;
import wootrevived.woot.blocks.fake_spawner.FakeSpawnerBlockItem;
import wootrevived.woot.blocks.fluid_infuser.FluidInfuserBlock;
import wootrevived.woot.blocks.fluid_infuser.FluidInfuserBlockEntity;
import wootrevived.woot.blocks.heart.HeartBlock;
import wootrevived.woot.blocks.heart.HeartBlockEntity;
import wootrevived.woot.blocks.ingredient_import.IngredientImportBlockEntity;
import wootrevived.woot.blocks.item_infuser.ItemInfuserBlock;
import wootrevived.woot.blocks.item_infuser.ItemInfuserBlockEntity;
import wootrevived.woot.blocks.layout.LayoutBlock;
import wootrevived.woot.blocks.layout.LayoutBlockEntity;
import wootrevived.woot.blocks.magmator.MagmatorBlock;
import wootrevived.woot.blocks.magmator.MagmatorBlockEntity;
import wootrevived.woot.blocks.stygian_anvil.StygianAnvilBlock;
import wootrevived.woot.blocks.stygian_anvil.StygianAnvilBlockEntity;
import wootrevived.woot.blocks.stygian_block.StygianBlock;
import wootrevived.woot.client.render.dye_liquifier.DyeLiquifierContainerMenu;
import wootrevived.woot.client.render.enchanted_liquifier.EnchantedLiquifierContainerMenu;
import wootrevived.woot.client.render.fluid_infuser.FluidInfuserContainerMenu;
import wootrevived.woot.client.render.heart.HeartContainerMenu;
import wootrevived.woot.client.render.item_infuser.ItemInfuserContainerMenu;
import wootrevived.woot.init.Registry;
import wootrevived.woot.util.block.FactoryBlockBaseEntity;

public class BlocksRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Woot.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Woot.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Woot.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, Woot.MOD_ID);

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITY_TYPES.register(bus);
        MENU_TYPES.register(bus);

        for(DeferredHolder<Item, ? extends Item> item : ITEMS.getEntries())
            Registry.addToCreativeTab(item);
    }

    /* Creative */

    public static final String CREATIVE_POWER_TAG = "creative_power";
    public static final DeferredHolder<Block, Block> CREATIVE_POWER_BLOCK = BLOCKS.register(CREATIVE_POWER_TAG, CreativePowerBlock::new);
    public static final DeferredHolder<Item, Item> CREATIVE_POWER_BLOCK_ITEM = ITEMS.register(CREATIVE_POWER_TAG, () -> new BlockItem(CREATIVE_POWER_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CreativePowerBlockEntity>> CREATIVE_POWER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(CREATIVE_POWER_TAG, () -> BlockEntityType.Builder.of(CreativePowerBlockEntity::new, CREATIVE_POWER_BLOCK.get()).build(null));

    public static final String CREATIVE_TANK_TAG = "creative_tank";
    public static final DeferredHolder<Block, Block> CREATIVE_TANK_BLOCK = BLOCKS.register(CREATIVE_TANK_TAG, CreativeTankBlock::new);
    public static final DeferredHolder<Item, Item> CREATIVE_TANK_BLOCK_ITEM = ITEMS.register(CREATIVE_TANK_TAG, () -> new BlockItem(CREATIVE_TANK_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CreativeTankBlockEntity>> CREATIVE_TANK_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(CREATIVE_TANK_TAG, () -> BlockEntityType.Builder.of(CreativeTankBlockEntity::new, CREATIVE_TANK_BLOCK.get()).build(null));

    /* Factory Base */

    public static final String FACTORY_BASE_TAG = "factory_base";
    public static final DeferredHolder<Block, Block> FACTORY_BASE_BLOCK = BLOCKS.register(FACTORY_BASE_TAG, FactoryBaseBlock::new);
    public static final DeferredHolder<Item, Item> FACTORY_BASE_BLOCK_ITEM = ITEMS.register(FACTORY_BASE_TAG, () -> new BlockItem(FACTORY_BASE_BLOCK.get(), new Item.Properties()));

    /* Machines */

    public static final String ITEM_INFUSER_TAG = "item_infuser";
    public static final DeferredHolder<Block, ItemInfuserBlock> ITEM_INFUSER_BLOCK = BLOCKS.register(ITEM_INFUSER_TAG, ItemInfuserBlock::new);
    public static final DeferredHolder<Item, Item> ITEM_INFUSER_BLOCK_ITEM = ITEMS.register(ITEM_INFUSER_TAG, () -> new BlockItem(ITEM_INFUSER_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ItemInfuserBlockEntity>> ITEM_INFUSER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(ITEM_INFUSER_TAG, () -> BlockEntityType.Builder.of(ItemInfuserBlockEntity::new, ITEM_INFUSER_BLOCK.get()).build(null));
    public static final DeferredHolder<MenuType<?>, MenuType<ItemInfuserContainerMenu>> ITEM_INFUSER_BLOCK_MENU = MENU_TYPES.register(ITEM_INFUSER_TAG, () -> IMenuTypeExtension.create(ItemInfuserContainerMenu::new));

    public static final String FLUID_INFUSER_TAG = "fluid_infuser";
    public static final DeferredHolder<Block, FluidInfuserBlock> FLUID_INFUSER_BLOCK = BLOCKS.register(FLUID_INFUSER_TAG, FluidInfuserBlock::new);
    public static final DeferredHolder<Item, Item> FLUID_INFUSER_BLOCK_ITEM = ITEMS.register(FLUID_INFUSER_TAG, () -> new BlockItem(FLUID_INFUSER_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidInfuserBlockEntity>> FLUID_INFUSER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(FLUID_INFUSER_TAG, () -> BlockEntityType.Builder.of(FluidInfuserBlockEntity::new, FLUID_INFUSER_BLOCK.get()).build(null));
    public static final DeferredHolder<MenuType<?>, MenuType<FluidInfuserContainerMenu>> FLUID_INFUSER_BLOCK_MENU = MENU_TYPES.register(FLUID_INFUSER_TAG, () -> IMenuTypeExtension.create(FluidInfuserContainerMenu::new));

    public static final String DYE_LIQUIFIER_TAG = "dye_liquifier";
    public static final DeferredHolder<Block, DyeLiquifierBlock> DYE_LIQUIFIER_BLOCK = BLOCKS.register(DYE_LIQUIFIER_TAG, DyeLiquifierBlock::new);
    public static final DeferredHolder<Item, Item> DYE_LIQUIFIER_BLOCK_ITEM = ITEMS.register(DYE_LIQUIFIER_TAG, () -> new BlockItem(DYE_LIQUIFIER_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DyeLiquifierBlockEntity>> DYE_LIQUIFIER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(DYE_LIQUIFIER_TAG, () -> BlockEntityType.Builder.of(DyeLiquifierBlockEntity::new, DYE_LIQUIFIER_BLOCK.get()).build(null));
    public static final DeferredHolder<MenuType<?>, MenuType<DyeLiquifierContainerMenu>> DYE_LIQUIFIER_BLOCK_MENU = MENU_TYPES.register(DYE_LIQUIFIER_TAG, () -> IMenuTypeExtension.create(DyeLiquifierContainerMenu::new));

    public static final String ENCHANTED_LIQUIFIER_TAG = "enchanted_liquifier";
    public static final DeferredHolder<Block, EnchantedLiquifierBlock> ENCHANTED_LIQUIFIER_BLOCK = BLOCKS.register(ENCHANTED_LIQUIFIER_TAG, EnchantedLiquifierBlock::new);
    public static final DeferredHolder<Item, Item> ENCHANTED_LIQUIFIER_BLOCK_ITEM = ITEMS.register(ENCHANTED_LIQUIFIER_TAG, () -> new BlockItem(ENCHANTED_LIQUIFIER_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnchantedLiquifierBlockEntity>> ENCHANTED_LIQUIFIER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(ENCHANTED_LIQUIFIER_TAG, () -> BlockEntityType.Builder.of(EnchantedLiquifierBlockEntity::new, ENCHANTED_LIQUIFIER_BLOCK.get()).build(null));
    public static final DeferredHolder<MenuType<?>, MenuType<EnchantedLiquifierContainerMenu>> ENCHANTED_LIQUIFIER_BLOCK_MENU = MENU_TYPES.register(ENCHANTED_LIQUIFIER_TAG, () -> IMenuTypeExtension.create(EnchantedLiquifierContainerMenu::new));

    /* Layout */

    public static final String LAYOUT_TAG = "layout";
    public static final DeferredHolder<Block, LayoutBlock> LAYOUT_BLOCK = BLOCKS.register(LAYOUT_TAG, LayoutBlock::new);
    public static final DeferredHolder<Item, Item> LAYOUT_BLOCK_ITEM = ITEMS.register(LAYOUT_TAG, () -> new BlockItem(LAYOUT_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LayoutBlockEntity>> LAYOUT_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(LAYOUT_TAG, () -> BlockEntityType.Builder.of(LayoutBlockEntity::new, LAYOUT_BLOCK.get()).build(null));

    /* Factory */

    // Main Blocks

    public static final String HEART_TAG = "heart";
    public static final DeferredHolder<Block, Block> HEART_BLOCK = BLOCKS.register(HEART_TAG, () -> new HeartBlock(BlocksRegistry.HEART_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> HEART_BLOCK_ITEM = ITEMS.register(HEART_TAG, () -> new FactoryBlockItem(HEART_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> HEART_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(HEART_TAG, () -> BlockEntityType.Builder.of(HeartBlockEntity::new, HEART_BLOCK.get()).build(null));
    public static final DeferredHolder<MenuType<?>, MenuType<HeartContainerMenu>> HEART_BLOCK_MENU = MENU_TYPES.register(HEART_TAG, () -> IMenuTypeExtension.create(HeartContainerMenu::new));

    public static final String FAKE_SPAWNER_TAG = "fake_spawner";
    public static final DeferredHolder<Block, Block> FAKE_SPAWNER_BLOCK = BLOCKS.register(FAKE_SPAWNER_TAG, () -> new FactoryBlock(BlocksRegistry.FAKE_SPAWNER_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, FakeSpawnerBlockItem> FAKE_SPAWNER_BLOCK_ITEM = ITEMS.register(FAKE_SPAWNER_TAG, () -> new FakeSpawnerBlockItem(FAKE_SPAWNER_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> FAKE_SPAWNER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(FAKE_SPAWNER_TAG, () -> BlockEntityType.Builder.of(FakeSpawnerBlockEntity::new, FAKE_SPAWNER_BLOCK.get()).build(null));

    public static final String FACTORY_CONNECT_TAG = "factory_connect";
    public static final DeferredHolder<Block, Block> FACTORY_CONNECT_BLOCK = BLOCKS.register(FACTORY_CONNECT_TAG, () -> new FactoryBlock(BlocksRegistry.FACTORY_CONNECT_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> FACTORY_CONNECT_BLOCK_ITEM = ITEMS.register(FACTORY_CONNECT_TAG, () -> new FactoryBlockItem(FACTORY_CONNECT_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> FACTORY_CONNECT_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(FACTORY_CONNECT_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new FactoryBlockBaseEntity(BlocksRegistry.FACTORY_CONNECT_BLOCK_ENTITY.get(), pos, state), FACTORY_CONNECT_BLOCK.get()).build(null));

    public static final String IMPORT_TAG = "import";
    public static final DeferredHolder<Block, Block> IMPORT_BLOCK = BLOCKS.register(IMPORT_TAG, () -> new FactoryBlock(BlocksRegistry.IMPORT_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> IMPORT_BLOCK_ITEM = ITEMS.register(IMPORT_TAG, () -> new FactoryBlockItem(IMPORT_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<IngredientImportBlockEntity>> IMPORT_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(IMPORT_TAG, () -> BlockEntityType.Builder.of(IngredientImportBlockEntity::new, IMPORT_BLOCK.get()).build(null));

    public static final String EXPORT_TAG = "export";
    public static final DeferredHolder<Block, Block> EXPORT_BLOCK = BLOCKS.register(EXPORT_TAG, () -> new FactoryBlock(BlocksRegistry.EXPORT_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> EXPORT_BLOCK_ITEM = ITEMS.register(EXPORT_TAG, () -> new FactoryBlockItem(EXPORT_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> EXPORT_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(EXPORT_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new FactoryBlockBaseEntity(BlocksRegistry.EXPORT_BLOCK_ENTITY.get(), pos, state), EXPORT_BLOCK.get()).build(null));

    public static final String FACTORY_CTR_BASE_PRI_TAG = "factory_ctr_base_pri";
    public static final DeferredHolder<Block, Block> FACTORY_CTR_BASE_PRI_BLOCK = BLOCKS.register(FACTORY_CTR_BASE_PRI_TAG, () -> new FactoryBlock(BlocksRegistry.FACTORY_CTR_BASE_PRI_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> FACTORY_CTR_BASE_PRI_BLOCK_ITEM = ITEMS.register(FACTORY_CTR_BASE_PRI_TAG, () -> new FactoryBlockItem(FACTORY_CTR_BASE_PRI_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> FACTORY_CTR_BASE_PRI_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(FACTORY_CTR_BASE_PRI_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new FactoryBlockBaseEntity(BlocksRegistry.FACTORY_CTR_BASE_PRI_BLOCK_ENTITY.get(), pos, state), FACTORY_CTR_BASE_PRI_BLOCK.get()).build(null));

    public static final String FACTORY_CTR_BASE_SEC_TAG = "factory_ctr_base_sec";
    public static final DeferredHolder<Block, Block> FACTORY_CTR_BASE_SEC_BLOCK = BLOCKS.register(FACTORY_CTR_BASE_SEC_TAG, () -> new FactoryBlock(BlocksRegistry.FACTORY_CTR_BASE_SEC_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> FACTORY_CTR_BASE_SEC_BLOCK_ITEM = ITEMS.register(FACTORY_CTR_BASE_SEC_TAG, () -> new FactoryBlockItem(FACTORY_CTR_BASE_SEC_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> FACTORY_CTR_BASE_SEC_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(FACTORY_CTR_BASE_SEC_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new FactoryBlockBaseEntity(BlocksRegistry.FACTORY_CTR_BASE_SEC_BLOCK_ENTITY.get(), pos, state), FACTORY_CTR_BASE_SEC_BLOCK.get()).build(null));

    public static final String FACTORY_UPGRADE_TAG = "factory_upgrade";
    public static final DeferredHolder<Block, Block> FACTORY_UPGRADE_BLOCK = BLOCKS.register(FACTORY_UPGRADE_TAG, () -> new FactoryUpgradeBlock(BlocksRegistry.FACTORY_UPGRADE_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> FACTORY_UPGRADE_BLOCK_ITEM = ITEMS.register(FACTORY_UPGRADE_TAG, () -> new FactoryBlockItem(FACTORY_UPGRADE_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> FACTORY_UPGRADE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(FACTORY_UPGRADE_TAG, () -> BlockEntityType.Builder.of(FactoryUpgradeBlockEntity::new, FACTORY_UPGRADE_BLOCK.get()).build(null));

    // Copper Tier Blocks

    public static final String COPPER_PYLON_TAG = "copper_pylon";
    public static final DeferredHolder<Block, Block> COPPER_PYLON_BLOCK = BLOCKS.register(COPPER_PYLON_TAG, () -> new FactoryBlock(BlocksRegistry.COPPER_PYLON_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> COPPER_PYLON_BLOCK_ITEM = ITEMS.register(COPPER_PYLON_TAG, () -> new FactoryBlockItem(COPPER_PYLON_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> COPPER_PYLON_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(COPPER_PYLON_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new FactoryBlockBaseEntity(BlocksRegistry.COPPER_PYLON_BLOCK_ENTITY.get(), pos, state), COPPER_PYLON_BLOCK.get()).build(null));

    public static final String COPPER_PLINTH_TAG = "copper_plinth";
    public static final DeferredHolder<Block, Block> COPPER_PLINTH_BLOCK = BLOCKS.register(COPPER_PLINTH_TAG, () -> new FactoryBlock(BlocksRegistry.COPPER_PLINTH_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> COPPER_PLINTH_BLOCK_ITEM = ITEMS.register(COPPER_PLINTH_TAG, () -> new FactoryBlockItem(COPPER_PLINTH_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> COPPER_PLINTH_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(COPPER_PLINTH_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new FactoryBlockBaseEntity(BlocksRegistry.COPPER_PLINTH_BLOCK_ENTITY.get(), pos, state), COPPER_PLINTH_BLOCK.get()).build(null));

    public static final String COPPER_CELL_TAG = "copper_cell";
    public static final DeferredHolder<Block, Block> COPPER_CELL_BLOCK = BLOCKS.register(COPPER_CELL_TAG, () -> new CellBlock(BlocksRegistry.COPPER_CELL_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> COPPER_CELL_BLOCK_ITEM = ITEMS.register(COPPER_CELL_TAG, () -> new FactoryBlockItem(COPPER_CELL_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CellBlockEntity>> COPPER_CELL_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(COPPER_CELL_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new CellBlockEntity(BlocksRegistry.COPPER_CELL_BLOCK_ENTITY.get(), pos, state), COPPER_CELL_BLOCK.get()).build(null));

    // Iron Tier Blocks

    public static final String IRON_PYLON_TAG = "iron_pylon";
    public static final DeferredHolder<Block, Block> IRON_PYLON_BLOCK = BLOCKS.register(IRON_PYLON_TAG, () -> new FactoryBlock(BlocksRegistry.IRON_PYLON_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> IRON_PYLON_BLOCK_ITEM = ITEMS.register(IRON_PYLON_TAG, () -> new FactoryBlockItem(IRON_PYLON_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> IRON_PYLON_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(IRON_PYLON_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new FactoryBlockBaseEntity(BlocksRegistry.IRON_PYLON_BLOCK_ENTITY.get(), pos, state), IRON_PYLON_BLOCK.get()).build(null));

    public static final String IRON_PLINTH_TAG = "iron_plinth";
    public static final DeferredHolder<Block, Block> IRON_PLINTH_BLOCK = BLOCKS.register(IRON_PLINTH_TAG, () -> new FactoryBlock(BlocksRegistry.IRON_PLINTH_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> IRON_PLINTH_BLOCK_ITEM = ITEMS.register(IRON_PLINTH_TAG, () -> new FactoryBlockItem(IRON_PLINTH_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> IRON_PLINTH_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(IRON_PLINTH_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new FactoryBlockBaseEntity(BlocksRegistry.IRON_PLINTH_BLOCK_ENTITY.get(), pos, state), IRON_PLINTH_BLOCK.get()).build(null));

    public static final String IRON_CELL_TAG = "iron_cell";
    public static final DeferredHolder<Block, Block> IRON_CELL_BLOCK = BLOCKS.register(IRON_CELL_TAG, () -> new CellBlock(BlocksRegistry.IRON_CELL_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> IRON_CELL_BLOCK_ITEM = ITEMS.register(IRON_CELL_TAG, () -> new FactoryBlockItem(IRON_CELL_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CellBlockEntity>> IRON_CELL_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(IRON_CELL_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new CellBlockEntity(BlocksRegistry.IRON_CELL_BLOCK_ENTITY.get(), pos, state), IRON_CELL_BLOCK.get()).build(null));

    // Gold Tier Blocks

    public static final String GOLD_PYLON_TAG = "gold_pylon";
    public static final DeferredHolder<Block, Block> GOLD_PYLON_BLOCK = BLOCKS.register(GOLD_PYLON_TAG, () -> new FactoryBlock(BlocksRegistry.GOLD_PYLON_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> GOLD_PYLON_BLOCK_ITEM = ITEMS.register(GOLD_PYLON_TAG, () -> new FactoryBlockItem(GOLD_PYLON_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> GOLD_PYLON_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(GOLD_PYLON_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new FactoryBlockBaseEntity(BlocksRegistry.GOLD_PYLON_BLOCK_ENTITY.get(), pos, state), GOLD_PYLON_BLOCK.get()).build(null));

    public static final String GOLD_PLINTH_TAG = "gold_plinth";
    public static final DeferredHolder<Block, Block> GOLD_PLINTH_BLOCK = BLOCKS.register(GOLD_PLINTH_TAG, () -> new FactoryBlock(BlocksRegistry.GOLD_PLINTH_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> GOLD_PLINTH_BLOCK_ITEM = ITEMS.register(GOLD_PLINTH_TAG, () -> new FactoryBlockItem(GOLD_PLINTH_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> GOLD_PLINTH_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(GOLD_PLINTH_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new FactoryBlockBaseEntity(BlocksRegistry.GOLD_PLINTH_BLOCK_ENTITY.get(), pos, state), GOLD_PLINTH_BLOCK.get()).build(null));

    public static final String GOLD_CELL_TAG = "gold_cell";
    public static final DeferredHolder<Block, Block> GOLD_CELL_BLOCK = BLOCKS.register(GOLD_CELL_TAG, () -> new CellBlock(BlocksRegistry.GOLD_CELL_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> GOLD_CELL_BLOCK_ITEM = ITEMS.register(GOLD_CELL_TAG, () -> new FactoryBlockItem(GOLD_CELL_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CellBlockEntity>> GOLD_CELL_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(GOLD_CELL_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new CellBlockEntity(BlocksRegistry.GOLD_CELL_BLOCK_ENTITY.get(), pos, state), GOLD_CELL_BLOCK.get()).build(null));

    // Diamond Tier Blocks

    public static final String DIAMOND_PYLON_TAG = "diamond_pylon";
    public static final DeferredHolder<Block, Block> DIAMOND_PYLON_BLOCK = BLOCKS.register(DIAMOND_PYLON_TAG, () -> new FactoryBlock(BlocksRegistry.DIAMOND_PYLON_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> DIAMOND_PYLON_BLOCK_ITEM = ITEMS.register(DIAMOND_PYLON_TAG, () -> new FactoryBlockItem(DIAMOND_PYLON_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> DIAMOND_PYLON_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(DIAMOND_PYLON_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new FactoryBlockBaseEntity(BlocksRegistry.DIAMOND_PYLON_BLOCK_ENTITY.get(), pos, state), DIAMOND_PYLON_BLOCK.get()).build(null));

    public static final String DIAMOND_PLINTH_TAG = "diamond_plinth";
    public static final DeferredHolder<Block, Block> DIAMOND_PLINTH_BLOCK = BLOCKS.register(DIAMOND_PLINTH_TAG, () -> new FactoryBlock(BlocksRegistry.DIAMOND_PLINTH_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> DIAMOND_PLINTH_BLOCK_ITEM = ITEMS.register(DIAMOND_PLINTH_TAG, () -> new FactoryBlockItem(DIAMOND_PLINTH_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> DIAMOND_PLINTH_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(DIAMOND_PLINTH_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new FactoryBlockBaseEntity(BlocksRegistry.DIAMOND_PLINTH_BLOCK_ENTITY.get(), pos, state), DIAMOND_PLINTH_BLOCK.get()).build(null));

    public static final String DIAMOND_CELL_TAG = "diamond_cell";
    public static final DeferredHolder<Block, Block> DIAMOND_CELL_BLOCK = BLOCKS.register(DIAMOND_CELL_TAG, () -> new CellBlock(BlocksRegistry.DIAMOND_CELL_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> DIAMOND_CELL_BLOCK_ITEM = ITEMS.register(DIAMOND_CELL_TAG, () -> new FactoryBlockItem(DIAMOND_CELL_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CellBlockEntity>> DIAMOND_CELL_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(DIAMOND_CELL_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new CellBlockEntity(BlocksRegistry.DIAMOND_CELL_BLOCK_ENTITY.get(), pos, state), DIAMOND_CELL_BLOCK.get()).build(null));

    // Netherite Tier Blocks

    public static final String NETHERITE_PYLON_TAG = "netherite_pylon";
    public static final DeferredHolder<Block, Block> NETHERITE_PYLON_BLOCK = BLOCKS.register(NETHERITE_PYLON_TAG, () -> new FactoryBlock(BlocksRegistry.NETHERITE_PYLON_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> NETHERITE_PYLON_BLOCK_ITEM = ITEMS.register(NETHERITE_PYLON_TAG, () -> new FactoryBlockItem(NETHERITE_PYLON_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> NETHERITE_PYLON_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(NETHERITE_PYLON_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new FactoryBlockBaseEntity(BlocksRegistry.NETHERITE_PYLON_BLOCK_ENTITY.get(), pos, state), NETHERITE_PYLON_BLOCK.get()).build(null));

    public static final String NETHERITE_PLINTH_TAG = "netherite_plinth";
    public static final DeferredHolder<Block, Block> NETHERITE_PLINTH_BLOCK = BLOCKS.register(NETHERITE_PLINTH_TAG, () -> new FactoryBlock(BlocksRegistry.NETHERITE_PLINTH_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> NETHERITE_PLINTH_BLOCK_ITEM = ITEMS.register(NETHERITE_PLINTH_TAG, () -> new FactoryBlockItem(NETHERITE_PLINTH_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> NETHERITE_PLINTH_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(NETHERITE_PLINTH_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new FactoryBlockBaseEntity(BlocksRegistry.NETHERITE_PLINTH_BLOCK_ENTITY.get(), pos, state), NETHERITE_PLINTH_BLOCK.get()).build(null));

    public static final String NETHERITE_CELL_TAG = "netherite_cell";
    public static final DeferredHolder<Block, Block> NETHERITE_CELL_BLOCK = BLOCKS.register(NETHERITE_CELL_TAG, () -> new CellBlock(BlocksRegistry.NETHERITE_CELL_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> NETHERITE_CELL_BLOCK_ITEM = ITEMS.register(NETHERITE_CELL_TAG, () -> new FactoryBlockItem(NETHERITE_CELL_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CellBlockEntity>> NETHERITE_CELL_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(NETHERITE_CELL_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new CellBlockEntity(BlocksRegistry.NETHERITE_CELL_BLOCK_ENTITY.get(), pos, state), NETHERITE_CELL_BLOCK.get()).build(null));

    /* Magmator */

    public static final String COPPER_MAGMATOR_TAG = "copper_magmator";
    public static final DeferredHolder<Block, MagmatorBlock> COPPER_MAGMATOR_BLOCK = BLOCKS.register(COPPER_MAGMATOR_TAG, () -> new MagmatorBlock(BlocksRegistry.COPPER_MAGMATOR_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> COPPER_MAGMATOR_BLOCK_ITEM = ITEMS.register(COPPER_MAGMATOR_TAG, () -> new BlockItem(COPPER_MAGMATOR_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MagmatorBlockEntity>> COPPER_MAGMATOR_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(COPPER_MAGMATOR_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new MagmatorBlockEntity(BlocksRegistry.COPPER_MAGMATOR_BLOCK_ENTITY.get(), pos, state), COPPER_MAGMATOR_BLOCK.get()).build(null));

    public static final String IRON_MAGMATOR_TAG = "iron_magmator";
    public static final DeferredHolder<Block, MagmatorBlock> IRON_MAGMATOR_BLOCK = BLOCKS.register(IRON_MAGMATOR_TAG, () -> new MagmatorBlock(BlocksRegistry.IRON_MAGMATOR_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> IRON_MAGMATOR_BLOCK_ITEM = ITEMS.register(IRON_MAGMATOR_TAG, () -> new BlockItem(IRON_MAGMATOR_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MagmatorBlockEntity>> IRON_MAGMATOR_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(IRON_MAGMATOR_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new MagmatorBlockEntity(BlocksRegistry.IRON_MAGMATOR_BLOCK_ENTITY.get(), pos, state), IRON_MAGMATOR_BLOCK.get()).build(null));

    public static final String GOLD_MAGMATOR_TAG = "gold_magmator";
    public static final DeferredHolder<Block, MagmatorBlock> GOLD_MAGMATOR_BLOCK = BLOCKS.register(GOLD_MAGMATOR_TAG, () -> new MagmatorBlock(BlocksRegistry.GOLD_MAGMATOR_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> GOLD_MAGMATOR_BLOCK_ITEM = ITEMS.register(GOLD_MAGMATOR_TAG, () -> new BlockItem(GOLD_MAGMATOR_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MagmatorBlockEntity>> GOLD_MAGMATOR_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(GOLD_MAGMATOR_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new MagmatorBlockEntity(BlocksRegistry.GOLD_MAGMATOR_BLOCK_ENTITY.get(), pos, state), GOLD_MAGMATOR_BLOCK.get()).build(null));

    public static final String DIAMOND_MAGMATOR_TAG = "diamond_magmator";
    public static final DeferredHolder<Block, MagmatorBlock> DIAMOND_MAGMATOR_BLOCK = BLOCKS.register(DIAMOND_MAGMATOR_TAG, () -> new MagmatorBlock(BlocksRegistry.DIAMOND_MAGMATOR_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> DIAMOND_MAGMATOR_BLOCK_ITEM = ITEMS.register(DIAMOND_MAGMATOR_TAG, () -> new BlockItem(DIAMOND_MAGMATOR_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MagmatorBlockEntity>> DIAMOND_MAGMATOR_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(DIAMOND_MAGMATOR_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new MagmatorBlockEntity(BlocksRegistry.DIAMOND_MAGMATOR_BLOCK_ENTITY.get(), pos, state), DIAMOND_MAGMATOR_BLOCK.get()).build(null));

    public static final String NETHERITE_MAGMATOR_TAG = "netherite_magmator";
    public static final DeferredHolder<Block, MagmatorBlock> NETHERITE_MAGMATOR_BLOCK = BLOCKS.register(NETHERITE_MAGMATOR_TAG, () -> new MagmatorBlock(BlocksRegistry.NETHERITE_MAGMATOR_BLOCK_ENTITY::get));
    public static final DeferredHolder<Item, Item> NETHERITE_MAGMATOR_BLOCK_ITEM = ITEMS.register(NETHERITE_MAGMATOR_TAG, () -> new BlockItem(NETHERITE_MAGMATOR_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MagmatorBlockEntity>> NETHERITE_MAGMATOR_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(NETHERITE_MAGMATOR_TAG, () -> BlockEntityType.Builder.of((pos, state) -> new MagmatorBlockEntity(BlocksRegistry.NETHERITE_MAGMATOR_BLOCK_ENTITY.get(), pos, state), NETHERITE_MAGMATOR_BLOCK.get()).build(null));

    /* Anvil */

    public static final String STYGIAN_ANVIL_TAG = "stygian_anvil";
    public static final DeferredHolder<Block, StygianAnvilBlock> STYGIAN_ANVIL_BLOCK = BLOCKS.register(STYGIAN_ANVIL_TAG, StygianAnvilBlock::new);
    public static final DeferredHolder<Item, Item> STYGIAN_ANVIL_BLOCK_ITEM = ITEMS.register(STYGIAN_ANVIL_TAG, () -> new BlockItem(STYGIAN_ANVIL_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StygianAnvilBlockEntity>> STYGIAN_ANVIL_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(STYGIAN_ANVIL_TAG, () -> BlockEntityType.Builder.of(StygianAnvilBlockEntity::new, STYGIAN_ANVIL_BLOCK.get()).build(null));

    /* Stygian Block */

    public static final String STYGIAN_BLOCK_TAG = "stygian_block";
    public static final DeferredHolder<Block, StygianBlock> STYGIAN_BLOCK = BLOCKS.register(STYGIAN_BLOCK_TAG, StygianBlock::new);
    public static final DeferredHolder<Item, Item> STYGIAN_BLOCK_ITEM = ITEMS.register(STYGIAN_BLOCK_TAG, () -> new BlockItem(STYGIAN_BLOCK.get(), new Item.Properties()));
}
