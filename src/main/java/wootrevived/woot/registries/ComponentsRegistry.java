package wootrevived.woot.registries;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wootrevived.woot.Woot;
import wootrevived.woot.blocks.cell.CellBlock;
import wootrevived.woot.blocks.dye_liquifier.DyeLiquifierBlock;
import wootrevived.woot.blocks.enchanted_liquifier.EnchantedLiquifierBlock;
import wootrevived.woot.blocks.fake_spawner.FakeSpawnerBlockItem;
import wootrevived.woot.blocks.fluid_infuser.FluidInfuserBlock;
import wootrevived.woot.blocks.item_infuser.ItemInfuserBlock;
import wootrevived.woot.data.*;
import wootrevived.woot.items.mob_shard.MobShardItem;
import wootrevived.woot.items.xp.XpItem;
import wootrevived.woot.upgrades.*;

public class ComponentsRegistry {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Woot.MOD_ID);

    public static void register(IEventBus bus) {
        DATA_COMPONENTS.register(bus);
    }

    // Components

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CellData.Component>> CELL_DATA =
            DATA_COMPONENTS.registerComponentType(
                    CellData.ID,
                    builder -> builder
                            .persistent(CellData.CODEC)
                            .networkSynchronized(CellData.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CreativeTankData.Component>> CREATIVE_TANK_DATA =
            DATA_COMPONENTS.registerComponentType(
                    CreativeTankData.ID,
                    builder -> builder
                            .persistent(CreativeTankData.CODEC)
                            .networkSynchronized(CreativeTankData.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FactoryUpgradeData.Component>> FACTORY_UPGRADE_DATA =
            DATA_COMPONENTS.registerComponentType(
                    FactoryUpgradeData.ID,
                    builder -> builder
                            .persistent(FactoryUpgradeData.CODEC)
                            .networkSynchronized(FactoryUpgradeData.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FakeSpawnerData.Component>> FAKE_SPAWNER_DATA =
            DATA_COMPONENTS.registerComponentType(
                    FakeSpawnerData.ID,
                    builder -> builder
                            .persistent(FakeSpawnerData.CODEC)
                            .networkSynchronized(FakeSpawnerData.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MultiBlockFactoryData.Component>> MULTI_BLOCK_FACTORY_DATA =
            DATA_COMPONENTS.registerComponentType(
                    MultiBlockFactoryData.ID,
                    builder -> builder
                            .persistent(MultiBlockFactoryData.CODEC)
                            .networkSynchronized(MultiBlockFactoryData.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<IngredientImportData.Component>> INGREDIENT_IMPORT_DATA =
            DATA_COMPONENTS.registerComponentType(
                    IngredientImportData.ID,
                    builder -> builder
                            .persistent(IngredientImportData.CODEC)
                            .networkSynchronized(IngredientImportData.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<LayoutData.Component>> LAYOUT_DATA =
            DATA_COMPONENTS.registerComponentType(
                    LayoutData.ID,
                    builder -> builder
                            .persistent(LayoutData.CODEC)
                            .networkSynchronized(LayoutData.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FactoryBlockData.Component>> FACTORY_BLOCK_DATA =
            DATA_COMPONENTS.registerComponentType(
                    FactoryBlockData.ID,
                    builder -> builder
                            .persistent(FactoryBlockData.CODEC)
                            .networkSynchronized(FactoryBlockData.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<DyeLiquifierData.Component>> DYE_LIQUIFIER_DATA =
            DATA_COMPONENTS.registerComponentType(
                    DyeLiquifierData.ID,
                    builder -> builder
                            .persistent(DyeLiquifierData.CODEC)
                            .networkSynchronized(DyeLiquifierData.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EnchantedLiquifierData.Component>> ENCHANTED_LIQUIFIER_DATA =
            DATA_COMPONENTS.registerComponentType(
                    EnchantedLiquifierData.ID,
                    builder -> builder
                            .persistent(EnchantedLiquifierData.CODEC)
                            .networkSynchronized(EnchantedLiquifierData.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemInfuserData.Component>> ITEM_INFUSER_DATA =
            DATA_COMPONENTS.registerComponentType(
                    ItemInfuserData.ID,
                    builder -> builder
                            .persistent(ItemInfuserData.CODEC)
                            .networkSynchronized(ItemInfuserData.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FluidInfuserData.Component>> FLUID_INFUSER_DATA =
            DATA_COMPONENTS.registerComponentType(
                    FluidInfuserData.ID,
                    builder -> builder
                            .persistent(FluidInfuserData.CODEC)
                            .networkSynchronized(FluidInfuserData.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MobShardData.Component>> MOB_SHARD_DATA =
            DATA_COMPONENTS.registerComponentType(
                    MobShardData.ID,
                    builder -> builder
                            .persistent(MobShardData.CODEC)
                            .networkSynchronized(MobShardData.STREAM_CODEC)
            );

    // Tooltips

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MobShardItem.Tooltip>> MOB_SHARD_ITEM_TOOLTIP =
            DATA_COMPONENTS.registerComponentType(
                    MobShardItem.Tooltip.ID,
                    builder -> builder
                            .persistent(MobShardItem.Tooltip.CODEC)
                            .networkSynchronized(MobShardItem.Tooltip.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<XpItem.Tooltip>> XP_ITEM_TOOLTIP =
            DATA_COMPONENTS.registerComponentType(
                    XpItem.Tooltip.ID,
                    builder -> builder
                            .persistent(XpItem.Tooltip.CODEC)
                            .networkSynchronized(XpItem.Tooltip.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Burn.Tooltip>> BURN_UPGRADE_TOOLTIP =
            DATA_COMPONENTS.registerComponentType(
                    Burn.Tooltip.ID,
                    builder -> builder
                            .persistent(Burn.Tooltip.CODEC)
                            .networkSynchronized(Burn.Tooltip.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Decapitate.Tooltip>> DECAPITATE_UPGRADE_TOOLTIP =
            DATA_COMPONENTS.registerComponentType(
                    Decapitate.Tooltip.ID,
                    builder -> builder
                            .persistent(Decapitate.Tooltip.CODEC)
                            .networkSynchronized(Decapitate.Tooltip.STREAM_CODEC)
                            .cacheEncoding()
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Dimension.Tooltip>> DIMENSION_UPGRADE_TOOLTIP =
            DATA_COMPONENTS.registerComponentType(
                    Dimension.Tooltip.ID,
                    builder -> builder
                            .persistent(Dimension.Tooltip.CODEC)
                            .networkSynchronized(Dimension.Tooltip.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Efficiency.Tooltip>> EFFICIENCY_UPGRADE_TOOLTIP =
            DATA_COMPONENTS.registerComponentType(
                    Efficiency.Tooltip.ID,
                    builder -> builder
                            .persistent(Efficiency.Tooltip.CODEC)
                            .networkSynchronized(Efficiency.Tooltip.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Looting.Tooltip>> LOOTING_UPGRADE_TOOLTIP =
            DATA_COMPONENTS.registerComponentType(
                    Looting.Tooltip.ID,
                    builder -> builder
                            .persistent(Looting.Tooltip.CODEC)
                            .networkSynchronized(Looting.Tooltip.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Mass.Tooltip>> MASS_UPGRADE_TOOLTIP =
            DATA_COMPONENTS.registerComponentType(
                    Mass.Tooltip.ID,
                    builder -> builder
                            .persistent(Mass.Tooltip.CODEC)
                            .networkSynchronized(Mass.Tooltip.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Rate.Tooltip>> RATE_UPGRADE_TOOLTIP =
            DATA_COMPONENTS.registerComponentType(
                    Rate.Tooltip.ID,
                    builder -> builder
                            .persistent(Rate.Tooltip.CODEC)
                            .networkSynchronized(Rate.Tooltip.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ShardDrop.Tooltip>> SHARD_DROP_UPGRADE_TOOLTIP =
            DATA_COMPONENTS.registerComponentType(
                    ShardDrop.Tooltip.ID,
                    builder -> builder
                            .persistent(ShardDrop.Tooltip.CODEC)
                            .networkSynchronized(ShardDrop.Tooltip.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Xp.Tooltip>> XP_UPGRADE_TOOLTIP =
            DATA_COMPONENTS.registerComponentType(
                    Xp.Tooltip.ID,
                    builder -> builder
                            .persistent(Xp.Tooltip.CODEC)
                            .networkSynchronized(Xp.Tooltip.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CellBlock.Tooltip>> CELL_BLOCK_TOOLTIP =
            DATA_COMPONENTS.registerComponentType(
                    CellBlock.Tooltip.ID,
                    builder -> builder
                            .persistent(CellBlock.Tooltip.CODEC)
                            .networkSynchronized(CellBlock.Tooltip.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<DyeLiquifierBlock.Tooltip>> DYE_LIQUIFIER_BLOCK_TOOLTIP =
            DATA_COMPONENTS.registerComponentType(
                    DyeLiquifierBlock.Tooltip.ID,
                    builder -> builder
                            .persistent(DyeLiquifierBlock.Tooltip.CODEC)
                            .networkSynchronized(DyeLiquifierBlock.Tooltip.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EnchantedLiquifierBlock.Tooltip>> ENCHANTED_LIQUIFIER_BLOCK_TOOLTIP =
            DATA_COMPONENTS.registerComponentType(
                    EnchantedLiquifierBlock.Tooltip.ID,
                    builder -> builder
                            .persistent(EnchantedLiquifierBlock.Tooltip.CODEC)
                            .networkSynchronized(EnchantedLiquifierBlock.Tooltip.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FakeSpawnerBlockItem.Tooltip>> FAKE_SPAWNER_BLOCK_TOOLTIP =
            DATA_COMPONENTS.registerComponentType(
                    FakeSpawnerBlockItem.Tooltip.ID,
                    builder -> builder
                            .persistent(FakeSpawnerBlockItem.Tooltip.CODEC)
                            .networkSynchronized(FakeSpawnerBlockItem.Tooltip.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FluidInfuserBlock.Tooltip>> FLUID_INFUSER_BLOCK_TOOLTIP =
            DATA_COMPONENTS.registerComponentType(
                    FluidInfuserBlock.Tooltip.ID,
                    builder -> builder
                            .persistent(FluidInfuserBlock.Tooltip.CODEC)
                            .networkSynchronized(FluidInfuserBlock.Tooltip.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemInfuserBlock.Tooltip>> ITEM_INFUSER_BLOCK_TOOLTIP =
            DATA_COMPONENTS.registerComponentType(
                    ItemInfuserBlock.Tooltip.ID,
                    builder -> builder
                            .persistent(ItemInfuserBlock.Tooltip.CODEC)
                            .networkSynchronized(ItemInfuserBlock.Tooltip.STREAM_CODEC)
            );
}
