package wootrevived.woot.recipes.stygian_anvil;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.BlocksRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StygianAnvilRecipeBuilder {
    private Ingredient base;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final ItemStack output;

    protected StygianAnvilRecipeBuilder(ItemLike output, int count) {
        this.output = output.asItem().getDefaultInstance();
        this.output.setCount(count);
    }

    public static StygianAnvilRecipeBuilder anvilRecipe(ItemLike output, int count) {
        return new StygianAnvilRecipeBuilder(output, count);
    }

    public static StygianAnvilRecipeBuilder anvilRecipe(ItemLike output){
        return new StygianAnvilRecipeBuilder(output, 1);
    }

    public StygianAnvilRecipeBuilder base(Ingredient base){
        this.base = base;
        return this;
    }

    public StygianAnvilRecipeBuilder ingredient(Ingredient ingredient){
        ingredients.add(ingredient);
        return this;
    }

    public void save(RecipeOutput recipeOutput){
        save(recipeOutput, BuiltInRegistries.ITEM.getKey(output.getItem()).getPath());
    }

    public void save(RecipeOutput recipeOutput, String path){
        List<Ingredient> itemInputs = new ArrayList<>(1 + ingredients.size());
        itemInputs.add(base);
        itemInputs.addAll(ingredients);
        recipeOutput.accept(
                ResourceKey.create(Registries.RECIPE, Woot.identifier(BlocksRegistry.STYGIAN_ANVIL_TAG + "/" + path)),
                new StygianAnvilRecipe(
                        base,
                        ingredients.isEmpty() ? Optional.empty() : Optional.of(ingredients.get(0)),
                        ingredients.size() <= 1 ? Optional.empty() : Optional.of(ingredients.get(1)),
                        ingredients.size() <= 2 ? Optional.empty() : Optional.of(ingredients.get(2)),
                        ingredients.size() <= 3 ? Optional.empty() : Optional.of(ingredients.get(3)),
                        output
                ),
                null
        );
    }
}
