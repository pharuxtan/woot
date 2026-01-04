package wootrevived.woot.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.KilledTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.ItemLike;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.registries.ItemsRegistry;
import wootrevived.woot.registries.UpgradeItemsRegistry;
import wootrevived.woot.upgrades.ShardDrop;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Advancements extends AdvancementProvider {
    public Advancements(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, List.of(new Provider()));
    }

    public static final class Provider implements AdvancementSubProvider {
        private static final Identifier background = Identifier.withDefaultNamespace("textures/block/black_concrete_powder.png");

        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver) {
            AdvancementHolder wootRevived = Advancement.Builder.advancement()
                    .display(BlocksRegistry.HEART_BLOCK.get(),
                            Component.translatable("advancements.woot_revived.root.title"),
                            Component.translatable("advancements.woot_revived.root.description"),
                            background,
                            AdvancementType.TASK, false, false, false)
                    .addCriterion("killed_something", KilledTrigger.TriggerInstance.playerKilledEntity())
                    .save(saver, getNameId("root"));

            AdvancementHolder stygian_ingot = registerItem(saver, "stygian_ingot", wootRevived, ItemsRegistry.STYGIAN_INGOT_ITEM.get());
            AdvancementHolder stygian_hammer = registerItem(saver, "stygian_hammer", stygian_ingot, ItemsRegistry.STYGIAN_HAMMER_ITEM.get());
            AdvancementHolder stygian_anvil = registerItem(saver, "stygian_anvil", stygian_ingot, BlocksRegistry.STYGIAN_ANVIL_BLOCK.get());

            AdvancementHolder shard_mold = registerItem(saver, "shard_mold", stygian_anvil, ItemsRegistry.SHARD_MOLD_ITEM.get());
            AdvancementHolder dye_mold = registerItem(saver, "dye_mold", stygian_anvil, ItemsRegistry.DYE_CASING_MOLD_ITEM.get());
            AdvancementHolder plate_mold = registerItem(saver, "plate_mold", stygian_anvil, ItemsRegistry.PLATE_MOLD_ITEM.get());

            AdvancementHolder mob_shard = registerItem(saver, "mob_shard", shard_mold, ItemsRegistry.MOB_SHARD_ITEM.get());
            AdvancementHolder stygian_plate = registerItem(saver, "stygian_plate", plate_mold, ItemsRegistry.STYGIAN_PLATE_ITEM.get());

            AdvancementHolder factory_base = registerItem(saver, "factory_base", stygian_plate, BlocksRegistry.FACTORY_BASE_BLOCK.get());

            AdvancementHolder fluid_infuser = registerItem(saver, "fluid_infuser", factory_base, BlocksRegistry.FLUID_INFUSER_BLOCK.get());
            AdvancementHolder mob_tears_bucket = registerItem(saver, "mob_tears_bucket", fluid_infuser, FluidsRegistry.MOB_TEARS_FLUID_BUCKET.get());
            AdvancementHolder vitality_fuel_bucket = registerItem(saver, "vitality_fuel_bucket", mob_tears_bucket, FluidsRegistry.VITALITY_FUEL_FLUID_BUCKET.get());

            AdvancementHolder enchanted_liquifier = registerItem(saver, "enchanted_liquifier", factory_base, BlocksRegistry.ENCHANTED_LIQUIFIER_BLOCK.get());
            AdvancementHolder enchanted_bucket = registerItem(saver, "enchanted_bucket", enchanted_liquifier, FluidsRegistry.ENCHANTED_FLUID_BUCKET.get());

            AdvancementHolder item_infuser = registerItem(saver, "item_infuser", enchanted_bucket, BlocksRegistry.ITEM_INFUSER_BLOCK.get());

            AdvancementHolder enchanted_copper_plate = registerItem(saver, "enchanted_copper_plate", item_infuser, ItemsRegistry.COPPER_ENCHANTED_PLATE_ITEM.get());
            AdvancementHolder copper_cell = registerItem(saver, "copper_cell", enchanted_copper_plate, BlocksRegistry.COPPER_CELL_BLOCK.get());

            AdvancementHolder enchanted_iron_plate = registerItem(saver, "enchanted_iron_plate", item_infuser, ItemsRegistry.IRON_ENCHANTED_PLATE_ITEM.get());
            AdvancementHolder iron_cell = registerItem(saver, "iron_cell", enchanted_iron_plate, BlocksRegistry.IRON_CELL_BLOCK.get());

            AdvancementHolder enchanted_gold_plate = registerItem(saver, "enchanted_gold_plate", item_infuser, ItemsRegistry.GOLD_ENCHANTED_PLATE_ITEM.get());
            AdvancementHolder gold_cell = registerItem(saver, "gold_cell", enchanted_gold_plate, BlocksRegistry.GOLD_CELL_BLOCK.get());

            AdvancementHolder enchanted_diamond_plate = registerItem(saver, "enchanted_diamond_plate", item_infuser, ItemsRegistry.DIAMOND_ENCHANTED_PLATE_ITEM.get());
            AdvancementHolder diamond_cell = registerItem(saver, "diamond_cell", enchanted_diamond_plate, BlocksRegistry.DIAMOND_CELL_BLOCK.get());

            AdvancementHolder enchanted_netherite_plate = registerItem(saver, "enchanted_netherite_plate", item_infuser, ItemsRegistry.NETHERITE_ENCHANTED_PLATE_ITEM.get());
            AdvancementHolder netherite_cell = registerItem(saver, "netherite_cell", enchanted_netherite_plate, BlocksRegistry.NETHERITE_CELL_BLOCK.get());

            AdvancementHolder dye_liquifier = registerItem(saver, "dye_liquifier", factory_base, BlocksRegistry.DYE_LIQUIFIER_BLOCK.get());
            AdvancementHolder pure_dye = registerItem(saver, "pure_dye", dye_liquifier, FluidsRegistry.PURE_DYE_FLUID_BUCKET.get());

            AdvancementHolder prism = registerItem(saver, "prism", pure_dye, ItemsRegistry.PRISM_ITEM.get());

            AdvancementHolder fake_spawner = registerItem(saver, "fake_spawner", prism, BlocksRegistry.FAKE_SPAWNER_BLOCK.get());

            AdvancementHolder black_dye_plate = registerItem(saver, "black_dye_plate", dye_mold, ItemsRegistry.BLACK_DYE_PLATE_ITEM.get());

            AdvancementHolder upgrade_base = registerItem(saver, "upgrade_base", black_dye_plate, UpgradeItemsRegistry.UPGRADE_BASE_ITEM.get());

            AdvancementHolder copper_shard = registerItem(saver, "copper_shard", shard_mold, ItemsRegistry.COPPER_SHARD_ITEM.get());
            AdvancementHolder copper_pylon = registerItem(saver, "copper_pylon", copper_shard, BlocksRegistry.COPPER_PYLON_BLOCK.get());
            AdvancementHolder copper_plinth = registerItem(saver, "copper_plinth", copper_shard, BlocksRegistry.COPPER_PLINTH_BLOCK.get());

            AdvancementHolder iron_shard_upgrade = registerItem(saver, "iron_shard_upgrade", upgrade_base, ShardDrop.IRON_SHARD_DROP_ITEM.get());
            AdvancementHolder iron_shard = registerItem(saver, "iron_shard", iron_shard_upgrade, ItemsRegistry.IRON_SHARD_ITEM.get());
            AdvancementHolder iron_pylon = registerItem(saver, "iron_pylon", iron_shard, BlocksRegistry.IRON_PYLON_BLOCK.get());
            AdvancementHolder iron_plinth = registerItem(saver, "iron_plinth", iron_shard, BlocksRegistry.IRON_PLINTH_BLOCK.get());

            AdvancementHolder gold_shard_upgrade = registerItem(saver, "gold_shard_upgrade", iron_shard_upgrade, ShardDrop.GOLD_SHARD_DROP_ITEM.get());
            AdvancementHolder gold_shard = registerItem(saver, "gold_shard", gold_shard_upgrade, ItemsRegistry.GOLD_SHARD_ITEM.get());
            AdvancementHolder gold_pylon = registerItem(saver, "gold_pylon", gold_shard, BlocksRegistry.GOLD_PYLON_BLOCK.get());
            AdvancementHolder gold_plinth = registerItem(saver, "gold_plinth", gold_shard, BlocksRegistry.GOLD_PLINTH_BLOCK.get());

            AdvancementHolder diamond_shard_upgrade = registerItem(saver, "diamond_shard_upgrade", gold_shard_upgrade, ShardDrop.DIAMOND_SHARD_DROP_ITEM.get());
            AdvancementHolder diamond_shard = registerItem(saver, "diamond_shard", diamond_shard_upgrade, ItemsRegistry.DIAMOND_SHARD_ITEM.get());
            AdvancementHolder diamond_pylon = registerItem(saver, "diamond_pylon", diamond_shard, BlocksRegistry.DIAMOND_PYLON_BLOCK.get());
            AdvancementHolder diamond_plinth = registerItem(saver, "diamond_plinth", diamond_shard, BlocksRegistry.DIAMOND_PLINTH_BLOCK.get());

            AdvancementHolder netherite_shard_upgrade = registerItem(saver, "netherite_shard_upgrade", diamond_shard_upgrade, ShardDrop.NETHERITE_SHARD_DROP_ITEM.get());
            AdvancementHolder netherite_shard = registerItem(saver, "netherite_shard", netherite_shard_upgrade, ItemsRegistry.NETHERITE_SHARD_ITEM.get());
            AdvancementHolder netherite_pylon = registerItem(saver, "netherite_pylon", netherite_shard, BlocksRegistry.NETHERITE_PYLON_BLOCK.get());
            AdvancementHolder netherite_plinth = registerItem(saver, "netherite_plinth", netherite_shard, BlocksRegistry.NETHERITE_PLINTH_BLOCK.get());
        }

        private AdvancementHolder registerItem(Consumer<AdvancementHolder> saver, String name, AdvancementHolder parent, ItemLike item) {
            return Advancement.Builder.advancement()
                    .parent(parent)
                    .display(
                            item.asItem(),
                            Component.translatable("advancements.woot_revived." + name + ".title"),
                            Component.translatable("advancements.woot_revived." + name + ".description"),
                            background,
                            AdvancementType.TASK, false, true, false
                    )
                    .addCriterion(name, InventoryChangeTrigger.TriggerInstance.hasItems(item))
                    .save(saver, getNameId(name));
        }

        private String getNameId(String id) {
            return Woot.MOD_NAMESPACE + ":main/" + id;
        }
    }
}
