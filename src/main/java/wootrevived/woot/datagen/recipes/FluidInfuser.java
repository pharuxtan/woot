package wootrevived.woot.datagen.recipes;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import wootrevived.woot.datagen.Recipes;
import wootrevived.woot.recipes.fluid_infuser.FluidInfuserRecipeBuilder;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.registries.ItemsRegistry;

import java.util.function.Consumer;

public class FluidInfuser {
    public static void registerRecipes(Recipes recipes, Consumer<FinishedRecipe> consumer) {
        class IngredientRecipe {
            final Ingredient ingredient;
            final int outputAmount;

            public IngredientRecipe(Ingredient ingredient, int outputAmount) {
                this.ingredient = ingredient;
                this.outputAmount = outputAmount;
            }
        }

        IngredientRecipe[] vitalityFuelIngredients = new IngredientRecipe[]{
                new IngredientRecipe(Ingredient.of(ItemsRegistry.XP_SPLINTER_ITEM.get()), 150),
                new IngredientRecipe(Ingredient.of(ItemsRegistry.XP_SHARD_ITEM.get()), 1000),
                new IngredientRecipe(Ingredient.of(Items.REDSTONE), 1000),

                new IngredientRecipe(Ingredient.of(ItemsRegistry.COPPER_SHARD_ITEM.get()), 1000),
                new IngredientRecipe(Ingredient.of(ItemsRegistry.IRON_SHARD_ITEM.get()), 2500),
                new IngredientRecipe(Ingredient.of(ItemsRegistry.GOLD_SHARD_ITEM.get()), 5000),
                new IngredientRecipe(Ingredient.of(ItemsRegistry.DIAMOND_SHARD_ITEM.get()), 7500),
                new IngredientRecipe(Ingredient.of(ItemsRegistry.NETHERITE_SHARD_ITEM.get()), 10000),

                new IngredientRecipe(Ingredient.of(ItemsRegistry.COPPER_ENCHANTED_PLATE_ITEM.get()), 2500),
                new IngredientRecipe(Ingredient.of(ItemsRegistry.IRON_ENCHANTED_PLATE_ITEM.get()), 5000),
                new IngredientRecipe(Ingredient.of(ItemsRegistry.GOLD_ENCHANTED_PLATE_ITEM.get()), 7500),
                new IngredientRecipe(Ingredient.of(ItemsRegistry.DIAMOND_ENCHANTED_PLATE_ITEM.get()), 10000),
                new IngredientRecipe(Ingredient.of(ItemsRegistry.NETHERITE_ENCHANTED_PLATE_ITEM.get()), 15000),
        };

        for (int i = 0; i < vitalityFuelIngredients.length; i++) {
            FluidInfuserRecipeBuilder.fluidInfuserRecipe(FluidsRegistry.SOURCE_VITALITY_FUEL_FLUID.get(), vitalityFuelIngredients[i].outputAmount)
                    .ingredient(vitalityFuelIngredients[i].ingredient)
                    .fluid(FluidsRegistry.SOURCE_MOB_TEARS_FLUID.get())
                    .energy(5000)
                    .save(consumer, "vitality_fuel_" + i);
        }

        IngredientRecipe[] mobTearsIngredients = new IngredientRecipe[]{
                new IngredientRecipe(Ingredient.of(
                        Items.CHICKEN,
                        Items.COD,
                        Items.MUTTON,
                        Items.PORKCHOP,
                        Items.SALMON,
                        Items.PUFFERFISH,
                        Items.RABBIT,
                        Items.RABBIT_HIDE
                ), 100),
                new IngredientRecipe(Ingredient.of(
                        Items.COOKED_CHICKEN,
                        Items.COOKED_COD,
                        Items.COOKED_MUTTON,
                        Items.COOKED_PORKCHOP,
                        Items.COOKED_SALMON,
                        Items.COOKED_RABBIT
                ), 200),
                new IngredientRecipe(Ingredient.of(
                        Items.FEATHER,
                        Items.LEATHER,
                        Items.WHITE_WOOL,
                        Items.ORANGE_WOOL,
                        Items.MAGENTA_WOOL,
                        Items.LIGHT_BLUE_WOOL,
                        Items.YELLOW_WOOL,
                        Items.LIME_WOOL,
                        Items.PINK_WOOL,
                        Items.GRAY_WOOL,
                        Items.LIGHT_GRAY_WOOL,
                        Items.CYAN_WOOL,
                        Items.PURPLE_WOOL,
                        Items.BLUE_WOOL,
                        Items.BROWN_WOOL,
                        Items.GREEN_WOOL,
                        Items.RED_WOOL,
                        Items.BLACK_WOOL,
                        Items.RABBIT_FOOT
                ), 300),
                new IngredientRecipe(Ingredient.of(
                        Items.STRING,
                        Items.GUNPOWDER,
                        Items.ROTTEN_FLESH,
                        Items.PRISMARINE_SHARD,
                        Items.PRISMARINE_CRYSTALS,
                        Items.PHANTOM_MEMBRANE,
                        Items.BONE
                ), 500),
                new IngredientRecipe(Ingredient.of(
                        Items.SPIDER_EYE,
                        Items.SLIME_BALL
                ), 1000),
                new IngredientRecipe(Ingredient.of(
                        Items.MAGMA_CREAM,
                        Items.BLAZE_ROD
                ), 1500),
                new IngredientRecipe(Ingredient.of(
                        Items.ENDER_PEARL,
                        Items.GHAST_TEAR
                ), 2500),
                new IngredientRecipe(Ingredient.of(Items.ZOMBIE_HEAD, Items.PIGLIN_HEAD, Items.SKELETON_SKULL, Items.DRAGON_HEAD, Items.WITHER_SKELETON_SKULL), 5000),
                new IngredientRecipe(Ingredient.of(
                        Items.TOTEM_OF_UNDYING,
                        Items.SCULK_CATALYST
                ), 15000),
                new IngredientRecipe(Ingredient.of(Items.NETHER_STAR), 30000),
        };

        for (int i = 0; i < mobTearsIngredients.length; i++) {
            FluidInfuserRecipeBuilder.fluidInfuserRecipe(FluidsRegistry.SOURCE_MOB_TEARS_FLUID.get(), mobTearsIngredients[i].outputAmount)
                    .ingredient(mobTearsIngredients[i].ingredient)
                    .fluid(Fluids.WATER, 500)
                    .energy(5000)
                    .save(consumer, "mob_tears_" + i);
        }
    }
}
