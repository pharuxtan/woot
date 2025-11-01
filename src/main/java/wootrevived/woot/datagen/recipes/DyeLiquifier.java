package wootrevived.woot.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import wootrevived.woot.datagen.Recipes;
import wootrevived.woot.recipes.dye_liquifier.DyeLiquifierRecipeBuilder;
import wootrevived.woot.util.common.DyeMakeup;

import java.util.Locale;

public class DyeLiquifier {
    public static void registerRecipes(Recipes recipes, HolderLookup.Provider registries, RecipeOutput output){
        HolderLookup.RegistryLookup<Item> itemRegistry = registries.lookupOrThrow(Registries.ITEM);

        for (DyeMakeup d : DyeMakeup.values()) {
            DyeLiquifierRecipeBuilder.dyeLiquifierRecipe()
                    .ingredient(Ingredient.of(itemRegistry.getOrThrow(d.getTag())))
                    .energy(500)
                    .red(d.getRed())
                    .yellow(d.getYellow())
                    .blue(d.getBlue())
                    .white(d.getWhite())
                    .save(output, d.name().toLowerCase(Locale.ROOT));
        }

        class VanillaDyes {
            final Item item;
            final DyeMakeup dyeMakeup;
            final float multiply;
            final String name;
            public VanillaDyes(Item item, DyeMakeup dyeMakeup, float multiply, String name) {
                this.item = item;
                this.dyeMakeup = dyeMakeup;
                this.multiply = multiply;
                this.name = name;
            }
        }

        VanillaDyes[] dyes = {
                new VanillaDyes(Items.BONE_MEAL, DyeMakeup.WHITE, 1F, "bone_meal"),
                new VanillaDyes(Items.LILY_OF_THE_VALLEY, DyeMakeup.WHITE, 1F, "lily_of_the_valley"),
                new VanillaDyes(Items.OXEYE_DAISY, DyeMakeup.LIGHT_GRAY, 1F, "oxeye_daisy"),
                new VanillaDyes(Items.AZURE_BLUET, DyeMakeup.LIGHT_GRAY, 1F, "azure_bluet"),
                new VanillaDyes(Items.WHITE_TULIP, DyeMakeup.LIGHT_GRAY, 1F, "white_tulip"),
                new VanillaDyes(Items.INK_SAC, DyeMakeup.BLACK, 1F, "ink_sac"),
                new VanillaDyes(Items.WITHER_ROSE, DyeMakeup.BLACK, 1F, "wither_rose"),
                new VanillaDyes(Items.COCOA_BEANS, DyeMakeup.BROWN, 1F, "cocoa_beans"),
                new VanillaDyes(Items.RED_TULIP, DyeMakeup.RED, 1F, "red_tulip"),
                new VanillaDyes(Items.BEETROOT, DyeMakeup.RED, 1F, "beetroot"),
                new VanillaDyes(Items.POPPY, DyeMakeup.RED, 1F, "poppy"),
                new VanillaDyes(Items.ROSE_BUSH, DyeMakeup.RED, 2F, "rose_bush"),
                new VanillaDyes(Items.TORCHFLOWER, DyeMakeup.ORANGE, 1F, "torchflower"),
                new VanillaDyes(Items.ORANGE_TULIP, DyeMakeup.ORANGE, 1F, "orange_tulip"),
                new VanillaDyes(Items.DANDELION, DyeMakeup.YELLOW, 1F, "dandelion"),
                new VanillaDyes(Items.SUNFLOWER, DyeMakeup.YELLOW, 2F, "sunflower"),
                new VanillaDyes(Items.SEA_PICKLE, DyeMakeup.LIME, 1F, "sea_pickle"),
                new VanillaDyes(Items.CACTUS, DyeMakeup.GREEN, 1F, "cactus"),
                new VanillaDyes(Items.PITCHER_PLANT, DyeMakeup.CYAN, 2F, "pitcher_plant"),
                new VanillaDyes(Items.BLUE_ORCHID, DyeMakeup.LIGHT_BLUE, 1F, "blue_orchid"),
                new VanillaDyes(Items.CORNFLOWER, DyeMakeup.BLUE, 1F, "cornflower"),
                new VanillaDyes(Items.ALLIUM, DyeMakeup.MAGENTA, 1F, "allium"),
                new VanillaDyes(Items.LILAC, DyeMakeup.MAGENTA, 2F, "lilac"),
                new VanillaDyes(Items.PINK_PETALS, DyeMakeup.PINK, 1F, "pink_petals"),
                new VanillaDyes(Items.PINK_TULIP, DyeMakeup.PINK, 1F, "pink_tulip"),
                new VanillaDyes(Items.PEONY, DyeMakeup.PINK, 2F, "peony"),
        };

        for (VanillaDyes d : dyes) {
            DyeLiquifierRecipeBuilder.dyeLiquifierRecipe()
                    .ingredient(Ingredient.of(d.item))
                    .energy(500)
                    .red(d.dyeMakeup.getRed())
                    .yellow(d.dyeMakeup.getYellow())
                    .blue(d.dyeMakeup.getBlue())
                    .white(d.dyeMakeup.getWhite())
                    .multiply(d.multiply)
                    .save(output, d.name);
        }
    }
}
