package wootrevived.woot.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import wootrevived.woot.datagen.Recipes;
import wootrevived.woot.items.dye_casing.DyeCasingItem;
import wootrevived.woot.recipes.stygian_anvil.StygianAnvilRecipeBuilder;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.ItemsRegistry;

public class StygianAnvil {
    public static void registerRecipes(Recipes recipes, HolderLookup.Provider registries, RecipeOutput output){
        HolderLookup.RegistryLookup<Item> itemRegistry = registries.lookupOrThrow(Registries.ITEM);

        StygianAnvilRecipeBuilder.anvilRecipe(ItemsRegistry.PLATE_MOLD_ITEM.get())
                .base(Ingredient.of(Items.IRON_TRAPDOOR))
                .ingredient(Ingredient.of(Items.QUARTZ))
                .ingredient(Ingredient.of(ItemsRegistry.STYGIAN_INGOT_ITEM.get()))
                .save(output);

        StygianAnvilRecipeBuilder.anvilRecipe(ItemsRegistry.SHARD_MOLD_ITEM.get())
                .base(Ingredient.of(Items.PRISMARINE_SHARD))
                .ingredient(Ingredient.of(Items.QUARTZ))
                .ingredient(Ingredient.of(ItemsRegistry.STYGIAN_INGOT_ITEM.get()))
                .save(output, ItemsRegistry.SHARD_MOLD_TAG);

        StygianAnvilRecipeBuilder.anvilRecipe(ItemsRegistry.DYE_CASING_MOLD_ITEM.get())
                .base(Ingredient.of(itemRegistry.getOrThrow(Tags.Items.DYES)))
                .ingredient(Ingredient.of(Items.QUARTZ))
                .ingredient(Ingredient.of(ItemsRegistry.STYGIAN_INGOT_ITEM.get()))
                .save(output, ItemsRegistry.DYE_CASING_MOLD_TAG);

        StygianAnvilRecipeBuilder.anvilRecipe(ItemsRegistry.STYGIAN_PLATE_ITEM.get())
                .base(Ingredient.of(ItemsRegistry.PLATE_MOLD_ITEM.get()))
                .ingredient(Ingredient.of(ItemsRegistry.STYGIAN_INGOT_ITEM.get()))
                .save(output, ItemsRegistry.STYGIAN_PLATE_TAG);

        StygianAnvilRecipeBuilder.anvilRecipe(ItemsRegistry.MOB_SHARD_ITEM.get())
                .base(Ingredient.of(ItemsRegistry.SHARD_MOLD_ITEM.get()))
                .ingredient(Ingredient.of(ItemsRegistry.STYGIAN_INGOT_ITEM.get()))
                .ingredient(Ingredient.of(ItemsRegistry.STYGIAN_INGOT_ITEM.get()))
                .save(output, ItemsRegistry.MOB_SHARD_TAG);

        StygianAnvilRecipeBuilder.anvilRecipe(ItemsRegistry.COPPER_SHARD_ITEM.get())
                .base(Ingredient.of(ItemsRegistry.SHARD_MOLD_ITEM.get()))
                .ingredient(Ingredient.of(Items.COPPER_INGOT))
                .ingredient(Ingredient.of(Items.COPPER_INGOT))
                .ingredient(Ingredient.of(Items.COPPER_INGOT))
                .ingredient(Ingredient.of(Items.COPPER_INGOT))
                .save(output, ItemsRegistry.COPPER_SHARD_TAG);

        StygianAnvilRecipeBuilder.anvilRecipe(BlocksRegistry.FAKE_SPAWNER_BLOCK.get())
                .base(Ingredient.of(ItemsRegistry.MOB_SHARD_ITEM.get()))
                .ingredient(Ingredient.of(ItemsRegistry.PRISM_ITEM.get()))
                .ingredient(Ingredient.of(BlocksRegistry.FACTORY_BASE_BLOCK.get()))
                .save(output, BlocksRegistry.FAKE_SPAWNER_TAG);

        class Casing {
            final DeferredHolder<Item, DyeCasingItem> casing;
            final String name;
            final TagKey<Item> tag;

            public Casing(DeferredHolder<Item, DyeCasingItem> casing, TagKey<Item> tag) {
                this.casing = casing;
                this.name = BuiltInRegistries.ITEM.getKey(casing.get()).getPath();
                this.tag = tag;
            }
        }

        Casing[] casings = {
                new Casing(ItemsRegistry.WHITE_DYE_CASING_ITEM, Tags.Items.DYES_WHITE),
                new Casing(ItemsRegistry.ORANGE_DYE_CASING_ITEM, Tags.Items.DYES_ORANGE),
                new Casing(ItemsRegistry.MAGENTA_DYE_CASING_ITEM, Tags.Items.DYES_MAGENTA),
                new Casing(ItemsRegistry.LIGHT_BLUE_DYE_CASING_ITEM, Tags.Items.DYES_LIGHT_BLUE),
                new Casing(ItemsRegistry.YELLOW_DYE_CASING_ITEM, Tags.Items.DYES_YELLOW),
                new Casing(ItemsRegistry.LIME_DYE_CASING_ITEM, Tags.Items.DYES_LIME),
                new Casing(ItemsRegistry.PINK_DYE_CASING_ITEM, Tags.Items.DYES_PINK),
                new Casing(ItemsRegistry.GRAY_DYE_CASING_ITEM, Tags.Items.DYES_GRAY),
                new Casing(ItemsRegistry.LIGHT_GRAY_DYE_CASING_ITEM, Tags.Items.DYES_LIGHT_GRAY),
                new Casing(ItemsRegistry.CYAN_DYE_CASING_ITEM, Tags.Items.DYES_CYAN),
                new Casing(ItemsRegistry.PURPLE_DYE_CASING_ITEM, Tags.Items.DYES_PURPLE),
                new Casing(ItemsRegistry.BLUE_DYE_CASING_ITEM, Tags.Items.DYES_BLUE),
                new Casing(ItemsRegistry.BROWN_DYE_CASING_ITEM, Tags.Items.DYES_BROWN),
                new Casing(ItemsRegistry.GREEN_DYE_CASING_ITEM, Tags.Items.DYES_GREEN),
                new Casing(ItemsRegistry.RED_DYE_CASING_ITEM, Tags.Items.DYES_RED),
                new Casing(ItemsRegistry.BLACK_DYE_CASING_ITEM, Tags.Items.DYES_BLACK)
        };

        for(Casing c : casings) {
            StygianAnvilRecipeBuilder.anvilRecipe(c.casing.get())
                    .base(Ingredient.of(ItemsRegistry.DYE_CASING_MOLD_ITEM.get()))
                    .ingredient(Ingredient.of(itemRegistry.getOrThrow(c.tag)))
                    .save(output, c.name);
        }
    }
}
