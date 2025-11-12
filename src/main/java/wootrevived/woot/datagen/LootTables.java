package wootrevived.woot.datagen;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.ComponentsRegistry;

import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class LootTables extends BlockLootSubProvider {
    public LootTables(HolderLookup.Provider provider){
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
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

        copyComponentWithEnable(BlocksRegistry.FAKE_SPAWNER_BLOCK, ComponentsRegistry.FAKE_SPAWNER_DATA);

        dropSelfWithEnable(BlocksRegistry.COPPER_PLINTH_BLOCK);
        dropSelfWithEnable(BlocksRegistry.COPPER_PYLON_BLOCK);
        copyComponentWithEnable(BlocksRegistry.COPPER_CELL_BLOCK, ComponentsRegistry.CELL_DATA);

        dropSelfWithEnable(BlocksRegistry.IRON_PLINTH_BLOCK);
        dropSelfWithEnable(BlocksRegistry.IRON_PYLON_BLOCK);
        copyComponentWithEnable(BlocksRegistry.IRON_CELL_BLOCK, ComponentsRegistry.CELL_DATA);

        dropSelfWithEnable(BlocksRegistry.GOLD_PLINTH_BLOCK);
        dropSelfWithEnable(BlocksRegistry.GOLD_PYLON_BLOCK);
        copyComponentWithEnable(BlocksRegistry.GOLD_CELL_BLOCK, ComponentsRegistry.CELL_DATA);

        dropSelfWithEnable(BlocksRegistry.DIAMOND_PLINTH_BLOCK);
        dropSelfWithEnable(BlocksRegistry.DIAMOND_PYLON_BLOCK);
        copyComponentWithEnable(BlocksRegistry.DIAMOND_CELL_BLOCK, ComponentsRegistry.CELL_DATA);

        dropSelfWithEnable(BlocksRegistry.NETHERITE_PLINTH_BLOCK);
        dropSelfWithEnable(BlocksRegistry.NETHERITE_PYLON_BLOCK);
        copyComponentWithEnable(BlocksRegistry.NETHERITE_CELL_BLOCK, ComponentsRegistry.CELL_DATA);

        dropSelfWithEnable(BlocksRegistry.HEART_BLOCK);
        dropSelfWithEnable(BlocksRegistry.FACTORY_UPGRADE_BLOCK);
        dropSelfWithEnable(BlocksRegistry.FACTORY_CONNECT_BLOCK);
        dropSelfWithEnable(BlocksRegistry.FACTORY_CTR_BASE_PRI_BLOCK);
        dropSelfWithEnable(BlocksRegistry.FACTORY_CTR_BASE_SEC_BLOCK);
        dropSelfWithEnable(BlocksRegistry.IMPORT_BLOCK);
        dropSelfWithEnable(BlocksRegistry.EXPORT_BLOCK);

        dropSelf(BlocksRegistry.LAYOUT_BLOCK);

        dropSelf(BlocksRegistry.CREATIVE_POWER_BLOCK);
        copyComponent(BlocksRegistry.CREATIVE_TANK_BLOCK, ComponentsRegistry.CREATIVE_TANK_DATA);
        copyComponent(BlocksRegistry.FLUID_INFUSER_BLOCK, ComponentsRegistry.FLUID_INFUSER_DATA);
        copyComponent(BlocksRegistry.ITEM_INFUSER_BLOCK, ComponentsRegistry.ITEM_INFUSER_DATA);
        copyComponent(BlocksRegistry.ENCHANTED_LIQUIFIER_BLOCK, ComponentsRegistry.ENCHANTED_LIQUIFIER_DATA);
        copyComponent(BlocksRegistry.DYE_LIQUIFIER_BLOCK, ComponentsRegistry.DYE_LIQUIFIER_DATA);
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

    public void copyComponent(DeferredHolder<Block, ? extends Block> block, Supplier<? extends DataComponentType<?>> component){
        add(block.get(), noDrop()
                .withPool(LootPool.lootPool()
                        .name(getBlockResource(block.get()).toString())
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(
                                LootItem.lootTableItem((block.get()).asItem())
                                        .apply(
                                                CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
                                                        .include(component.get())
                                        )
                        )
                ));
    }

    public void copyComponentWithEnable(DeferredHolder<Block, ? extends Block> block, Supplier<? extends DataComponentType<?>> component){
        LootItemCondition.Builder stateBuilder = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.ENABLED, true));
        add(block.get(), noDrop()
            .withPool(LootPool.lootPool()
                    .name(getBlockResource(block.get()).toString())
                    .setRolls(ConstantValue.exactly(1.0F))
                    .when(stateBuilder)
                    .add(
                            LootItem.lootTableItem((block.get()).asItem())
                                    .apply(
                                            CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
                                                    .include(component.get())
                                    )
                    )
            ));
    }
}
