package wootrevived.woot.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import wootrevived.woot.datagen.Recipes;
import wootrevived.woot.items.dye_casing.DyeCasingItem;
import wootrevived.woot.items.dye_plate.DyePlateItem;
import wootrevived.woot.recipes.item_infuser.ItemInfuserRecipeBuilder;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.registries.ItemsRegistry;

public class ItemInfuser {
    public static void registerRecipes(Recipes recipes, HolderLookup.Provider registries, RecipeOutput consumer) {
        HolderLookup.RegistryLookup<Item> itemRegistry = registries.lookupOrThrow(Registries.ITEM);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(ItemsRegistry.PRISM_ITEM.get())
                .ingredient(Ingredient.of(itemRegistry.getOrThrow(Tags.Items.GLASS_BLOCKS)))
                .fluid(FluidsRegistry.SOURCE_PURE_DYE_FLUID.get(), 1000)
                .energy(5000)
                .save(consumer);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(Items.SOUL_SOIL)
                .ingredient(Ingredient.of(Items.SOUL_SAND))
                .augment(Ingredient.of(itemRegistry.getOrThrow(Tags.Items.SANDS)))
                .fluid(FluidsRegistry.SOURCE_MOB_TEARS_FLUID.get(), 1000)
                .energy(5000)
                .save(consumer);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(Items.CRYING_OBSIDIAN)
                .ingredient(Ingredient.of(itemRegistry.getOrThrow(Tags.Items.OBSIDIANS)))
                .fluid(FluidsRegistry.SOURCE_MOB_TEARS_FLUID.get(), 1000)
                .energy(5000)
                .save(consumer);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(Items.FIRE_CHARGE, 3)
                .ingredient(Ingredient.of(Items.GUNPOWDER))
                .augment(Ingredient.of(itemRegistry.getOrThrow(ItemTags.COALS)))
                .fluid(Fluids.LAVA, 1000)
                .energy(5000)
                .save(consumer);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(Items.NETHERRACK)
                .ingredient(Ingredient.of(Items.COBBLESTONE))
                .fluid(Fluids.LAVA, 1000)
                .energy(5000)
                .save(consumer);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(Items.MAGMA_BLOCK)
                .ingredient(Ingredient.of(Items.NETHERRACK))
                .fluid(Fluids.LAVA, 1000)
                .energy(5000)
                .save(consumer);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(ItemsRegistry.COPPER_ENCHANTED_PLATE_ITEM.get())
                .ingredient(Ingredient.of(ItemsRegistry.STYGIAN_PLATE_ITEM.get()))
                .augment(Ingredient.of(ItemsRegistry.COPPER_SHARD_ITEM.get()))
                .fluid(FluidsRegistry.SOURCE_ENCHANTED_FLUID.get(), 1000)
                .energy(5000)
                .save(consumer);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(ItemsRegistry.IRON_ENCHANTED_PLATE_ITEM.get())
                .ingredient(Ingredient.of(ItemsRegistry.COPPER_ENCHANTED_PLATE_ITEM.get()))
                .augment(Ingredient.of(ItemsRegistry.IRON_SHARD_ITEM.get()))
                .fluid(FluidsRegistry.SOURCE_ENCHANTED_FLUID.get(), 2000)
                .energy(10000)
                .save(consumer);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(ItemsRegistry.GOLD_ENCHANTED_PLATE_ITEM.get())
                .ingredient(Ingredient.of(ItemsRegistry.IRON_ENCHANTED_PLATE_ITEM.get()))
                .augment(Ingredient.of(ItemsRegistry.GOLD_SHARD_ITEM.get()))
                .fluid(FluidsRegistry.SOURCE_ENCHANTED_FLUID.get(), 3000)
                .energy(15000)
                .save(consumer);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(ItemsRegistry.DIAMOND_ENCHANTED_PLATE_ITEM.get())
                .ingredient(Ingredient.of(ItemsRegistry.GOLD_ENCHANTED_PLATE_ITEM.get()))
                .augment(Ingredient.of(ItemsRegistry.DIAMOND_SHARD_ITEM.get()))
                .fluid(FluidsRegistry.SOURCE_ENCHANTED_FLUID.get(), 4000)
                .energy(20000)
                .save(consumer);

        ItemInfuserRecipeBuilder.itemInfuserRecipe(ItemsRegistry.NETHERITE_ENCHANTED_PLATE_ITEM.get())
                .ingredient(Ingredient.of(ItemsRegistry.DIAMOND_ENCHANTED_PLATE_ITEM.get()))
                .augment(Ingredient.of(ItemsRegistry.NETHERITE_SHARD_ITEM.get()))
                .fluid(FluidsRegistry.SOURCE_ENCHANTED_FLUID.get(), 5000)
                .energy(25000)
                .save(consumer);

        class Plate {
            final DeferredHolder<Item, DyeCasingItem> casing;
            final DeferredHolder<Item, DyePlateItem> plate;
            public Plate(DeferredHolder<Item, DyeCasingItem> casing, DeferredHolder<Item, DyePlateItem> plate) {
                this.casing = casing;
                this.plate = plate;
            }
        }

        Plate[] plates = {
                new Plate(ItemsRegistry.WHITE_DYE_CASING_ITEM, ItemsRegistry.WHITE_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.ORANGE_DYE_CASING_ITEM, ItemsRegistry.ORANGE_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.MAGENTA_DYE_CASING_ITEM, ItemsRegistry.MAGENTA_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.LIGHT_BLUE_DYE_CASING_ITEM, ItemsRegistry.LIGHT_BLUE_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.YELLOW_DYE_CASING_ITEM, ItemsRegistry.YELLOW_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.LIME_DYE_CASING_ITEM, ItemsRegistry.LIME_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.PINK_DYE_CASING_ITEM, ItemsRegistry.PINK_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.GRAY_DYE_CASING_ITEM, ItemsRegistry.GRAY_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.LIGHT_GRAY_DYE_CASING_ITEM, ItemsRegistry.LIGHT_GRAY_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.CYAN_DYE_CASING_ITEM, ItemsRegistry.CYAN_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.PURPLE_DYE_CASING_ITEM, ItemsRegistry.PURPLE_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.BLUE_DYE_CASING_ITEM, ItemsRegistry.BLUE_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.BROWN_DYE_CASING_ITEM, ItemsRegistry.BROWN_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.GREEN_DYE_CASING_ITEM, ItemsRegistry.GREEN_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.RED_DYE_CASING_ITEM, ItemsRegistry.RED_DYE_PLATE_ITEM),
                new Plate(ItemsRegistry.BLACK_DYE_CASING_ITEM, ItemsRegistry.BLACK_DYE_PLATE_ITEM)
        };

        for (Plate p : plates) {
            ItemInfuserRecipeBuilder.itemInfuserRecipe(p.plate.get())
                    .ingredient(Ingredient.of(p.casing.get()))
                    .fluid(FluidsRegistry.SOURCE_PURE_DYE_FLUID.get(), 500)
                    .energy(2500)
                    .save(consumer);
        }
    }
}
