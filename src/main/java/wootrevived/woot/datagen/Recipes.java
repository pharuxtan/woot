package wootrevived.woot.datagen;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import wootrevived.woot.datagen.recipes.*;

import java.util.concurrent.CompletableFuture;

public class Recipes extends RecipeProvider {
    protected Recipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        Vanilla.registerRecipes(this, registries, output);
        StygianAnvil.registerRecipes(this, registries, output);
        DyeLiquifier.registerRecipes(this, registries, output);
        FluidInfuser.registerRecipes(this, registries, output);
        ItemInfuser.registerRecipes(this, registries, output);
    }

    public static class Runner extends RecipeProvider.Runner {
        protected Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> future) {
            super(packOutput, future);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
            return new Recipes(provider, recipeOutput);
        }

        @Override
        public String getName() {
            return "Recipes";
        }
    }

    public Criterion<InventoryChangeTrigger.TriggerInstance> hasItem(ItemLike item) { return has(item); }

    public Criterion<InventoryChangeTrigger.TriggerInstance> hasItem(TagKey<Item> item) { return has(item); }

    public ShapedRecipeBuilder buildShaped(RecipeCategory category, ItemLike item) {
        return super.shaped(category, item);
    }

    public ShapedRecipeBuilder buildShaped(RecipeCategory category, ItemLike item, int count) {
        return super.shaped(category, item, count);
    }

    public ShapelessRecipeBuilder buildShapeless(RecipeCategory category, ItemLike item) {
        return super.shapeless(category, item);
    }

    public ShapelessRecipeBuilder buildShapeless(RecipeCategory category, ItemLike item, int count) {
        return super.shapeless(category, item, count);
    }
}
