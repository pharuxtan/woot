package wootrevived.woot.datagen;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.util.entity.WootTags;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class LootTables extends BlockLootSubProvider {
    public LootTables(){
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BlocksRegistry.BLOCKS.getEntries().stream().map(DeferredHolder::get).collect(Collectors.toList());
    }

    @Override
    protected void generate() {
        dropSelf(BlocksRegistry.STYGIAN_ANVIL_BLOCK);
        dropSelf(BlocksRegistry.FACTORY_BASE_BLOCK);
        dropSelf(BlocksRegistry.STYGIAN_BLOCK);

        copyTagWithEnable(BlocksRegistry.FAKE_SPAWNER_BLOCK, CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                .copy(WootTags.MOB_TAG, "BlockEntityTag." + WootTags.MOB_TAG, CopyNbtFunction.MergeStrategy.REPLACE));

        dropSelf(BlocksRegistry.COPPER_MAGMATOR_BLOCK);
        dropSelf(BlocksRegistry.IRON_MAGMATOR_BLOCK);
        dropSelf(BlocksRegistry.GOLD_MAGMATOR_BLOCK);
        dropSelf(BlocksRegistry.DIAMOND_MAGMATOR_BLOCK);
        dropSelf(BlocksRegistry.NETHERITE_MAGMATOR_BLOCK);

        dropSelfWithEnable(BlocksRegistry.COPPER_PLINTH_BLOCK);
        dropSelfWithEnable(BlocksRegistry.COPPER_PYLON_BLOCK);
        copyTagWithEnable(BlocksRegistry.COPPER_CELL_BLOCK, CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                .copy(WootTags.INPUT_TANK_TAG, "BlockEntityTag." + WootTags.INPUT_TANK_TAG, CopyNbtFunction.MergeStrategy.REPLACE));

        dropSelfWithEnable(BlocksRegistry.IRON_PLINTH_BLOCK);
        dropSelfWithEnable(BlocksRegistry.IRON_PYLON_BLOCK);
        copyTagWithEnable(BlocksRegistry.IRON_CELL_BLOCK, CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                .copy(WootTags.INPUT_TANK_TAG, "BlockEntityTag." + WootTags.INPUT_TANK_TAG, CopyNbtFunction.MergeStrategy.REPLACE));

        dropSelfWithEnable(BlocksRegistry.GOLD_PLINTH_BLOCK);
        dropSelfWithEnable(BlocksRegistry.GOLD_PYLON_BLOCK);
        copyTagWithEnable(BlocksRegistry.GOLD_CELL_BLOCK, CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                .copy(WootTags.INPUT_TANK_TAG, "BlockEntityTag." + WootTags.INPUT_TANK_TAG, CopyNbtFunction.MergeStrategy.REPLACE));

        dropSelfWithEnable(BlocksRegistry.DIAMOND_PLINTH_BLOCK);
        dropSelfWithEnable(BlocksRegistry.DIAMOND_PYLON_BLOCK);
        copyTagWithEnable(BlocksRegistry.DIAMOND_CELL_BLOCK, CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                .copy(WootTags.INPUT_TANK_TAG, "BlockEntityTag." + WootTags.INPUT_TANK_TAG, CopyNbtFunction.MergeStrategy.REPLACE));

        dropSelfWithEnable(BlocksRegistry.NETHERITE_PLINTH_BLOCK);
        dropSelfWithEnable(BlocksRegistry.NETHERITE_PYLON_BLOCK);
        copyTagWithEnable(BlocksRegistry.NETHERITE_CELL_BLOCK, CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                .copy(WootTags.INPUT_TANK_TAG, "BlockEntityTag." + WootTags.INPUT_TANK_TAG, CopyNbtFunction.MergeStrategy.REPLACE));

        dropSelfWithEnable(BlocksRegistry.HEART_BLOCK);
        dropSelfWithEnable(BlocksRegistry.FACTORY_UPGRADE_BLOCK);
        dropSelfWithEnable(BlocksRegistry.FACTORY_CONNECT_BLOCK);
        dropSelfWithEnable(BlocksRegistry.FACTORY_CTR_BASE_PRI_BLOCK);
        dropSelfWithEnable(BlocksRegistry.FACTORY_CTR_BASE_SEC_BLOCK);
        dropSelfWithEnable(BlocksRegistry.IMPORT_BLOCK);
        dropSelfWithEnable(BlocksRegistry.EXPORT_BLOCK);

        dropSelf(BlocksRegistry.LAYOUT_BLOCK);

        dropSelf(BlocksRegistry.CREATIVE_POWER_BLOCK);
        copyTag(BlocksRegistry.CREATIVE_TANK_BLOCK, CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                .copy(WootTags.INPUT_TANK_TAG, "BlockEntityTag." + WootTags.INPUT_TANK_TAG, CopyNbtFunction.MergeStrategy.REPLACE));

        copyTag(BlocksRegistry.FLUID_INFUSER_BLOCK, CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                .copy(WootTags.ENERGY_TAG, "BlockEntityTag." + WootTags.ENERGY_TAG, CopyNbtFunction.MergeStrategy.REPLACE)
                .copy(WootTags.INPUT_TANK_TAG, "BlockEntityTag." + WootTags.INPUT_TANK_TAG, CopyNbtFunction.MergeStrategy.REPLACE)
                .copy(WootTags.OUTPUT_TANK_TAG, "BlockEntityTag." + WootTags.OUTPUT_TANK_TAG, CopyNbtFunction.MergeStrategy.REPLACE)
                .copy(WootTags.REDSTONE_MODE_TAG, "BlockEntityTag." + WootTags.REDSTONE_MODE_TAG, CopyNbtFunction.MergeStrategy.REPLACE)
                .copy(WootTags.DirectionProperties.LIST, "BlockEntityTag." + WootTags.DirectionProperties.LIST, CopyNbtFunction.MergeStrategy.REPLACE));

        copyTag(BlocksRegistry.ITEM_INFUSER_BLOCK, CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                .copy(WootTags.ENERGY_TAG, "BlockEntityTag." + WootTags.ENERGY_TAG, CopyNbtFunction.MergeStrategy.REPLACE)
                .copy(WootTags.INPUT_TANK_TAG, "BlockEntityTag." + WootTags.INPUT_TANK_TAG, CopyNbtFunction.MergeStrategy.REPLACE)
                .copy(WootTags.REDSTONE_MODE_TAG, "BlockEntityTag." + WootTags.REDSTONE_MODE_TAG, CopyNbtFunction.MergeStrategy.REPLACE)
                .copy(WootTags.DirectionProperties.LIST, "BlockEntityTag." + WootTags.DirectionProperties.LIST, CopyNbtFunction.MergeStrategy.REPLACE));

        copyTag(BlocksRegistry.ENCHANTED_LIQUIFIER_BLOCK, CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                .copy(WootTags.ENERGY_TAG, "BlockEntityTag." + WootTags.ENERGY_TAG, CopyNbtFunction.MergeStrategy.REPLACE)
                .copy(WootTags.OUTPUT_TANK_TAG, "BlockEntityTag." + WootTags.OUTPUT_TANK_TAG, CopyNbtFunction.MergeStrategy.REPLACE)
                .copy(WootTags.REDSTONE_MODE_TAG, "BlockEntityTag." + WootTags.REDSTONE_MODE_TAG, CopyNbtFunction.MergeStrategy.REPLACE)
                .copy(WootTags.DirectionProperties.LIST, "BlockEntityTag." + WootTags.DirectionProperties.LIST, CopyNbtFunction.MergeStrategy.REPLACE));

        copyTag(BlocksRegistry.DYE_LIQUIFIER_BLOCK, CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                .copy(WootTags.ENERGY_TAG, "BlockEntityTag." + WootTags.ENERGY_TAG, CopyNbtFunction.MergeStrategy.REPLACE)
                .copy(WootTags.OUTPUT_TANK_TAG, "BlockEntityTag." + WootTags.OUTPUT_TANK_TAG, CopyNbtFunction.MergeStrategy.REPLACE)
                .copy(WootTags.REDSTONE_MODE_TAG, "BlockEntityTag." + WootTags.REDSTONE_MODE_TAG, CopyNbtFunction.MergeStrategy.REPLACE)
                .copy(WootTags.DirectionProperties.LIST, "BlockEntityTag." + WootTags.DirectionProperties.LIST, CopyNbtFunction.MergeStrategy.REPLACE)
                .copy(WootTags.DyeLiquifier.INTERNAL_DYE_TANKS_TAG, "BlockEntityTag." + WootTags.DyeLiquifier.INTERNAL_DYE_TANKS_TAG, CopyNbtFunction.MergeStrategy.REPLACE));
    }

    public ResourceLocation getBlockResource(Block block){
        return Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block));
    }

    public void dropSelf(DeferredHolder<Block, ? extends Block> block){
        add(block.get(), noDrop().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem((Block)block.get())).unwrap()));
    }

    public void dropSelfWithEnable(DeferredHolder<Block, ? extends Block> block){
        LootItemCondition.Builder builder = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.ENABLED, true));
        add(block.get(), noDrop().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem((Block)block.get()).when(builder))));
    }

    public void copyTag(DeferredHolder<Block, ? extends Block> block, LootItemFunction.Builder builder){
        add(block.get(), noDrop()
                .withPool(LootPool.lootPool()
                        .name(getBlockResource(block.get()).toString())
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(
                                LootItem.lootTableItem((block.get()).asItem())
                                        .apply(builder)
                        )
                ));
    }

    public void copyTagWithEnable(DeferredHolder<Block, ? extends Block> block, LootItemFunction.Builder builder){
        LootItemCondition.Builder stateBuilder = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.ENABLED, true));
        add(block.get(), noDrop()
            .withPool(LootPool.lootPool()
                    .name(getBlockResource(block.get()).toString())
                    .setRolls(ConstantValue.exactly(1.0F))
                    .when(stateBuilder)
                    .add(
                            LootItem.lootTableItem((block.get()).asItem())
                                    .apply(builder)
                    )
            ));
    }
}
