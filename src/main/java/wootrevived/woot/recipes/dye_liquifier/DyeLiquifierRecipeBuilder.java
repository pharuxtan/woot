package wootrevived.woot.recipes.dye_liquifier;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.BlocksRegistry;

public class DyeLiquifierRecipeBuilder {
    private int energy;
    private float red;
    private float yellow;
    private float blue;
    private float white;
    private float multiply;
    private Ingredient ingredient;

    protected DyeLiquifierRecipeBuilder() {
        this.multiply = 1;
    }

    public static DyeLiquifierRecipeBuilder dyeLiquifierRecipe(){
        return new DyeLiquifierRecipeBuilder();
    }

    public DyeLiquifierRecipeBuilder energy(int energy){
        this.energy = energy;
        return this;
    }

    public DyeLiquifierRecipeBuilder red(float red){
        this.red = red;
        return this;
    }

    public DyeLiquifierRecipeBuilder yellow(float yellow){
        this.yellow = yellow;
        return this;
    }

    public DyeLiquifierRecipeBuilder blue(float blue){
        this.blue = blue;
        return this;
    }

    public DyeLiquifierRecipeBuilder white(float white){
        this.white = white;
        return this;
    }

    public DyeLiquifierRecipeBuilder multiply(float multiply){
        this.multiply = multiply;
        return this;
    }

    public DyeLiquifierRecipeBuilder ingredient(Ingredient ingredient){
        this.ingredient = ingredient;
        return this;
    }

    public void save(RecipeOutput recipeOutput, String path){
        recipeOutput.accept(
                ResourceKey.create(Registries.RECIPE, Woot.identifier(BlocksRegistry.DYE_LIQUIFIER_TAG + "/" + path)),
                new DyeLiquifierRecipe(energy, red * multiply, yellow * multiply, blue * multiply, white * multiply, ingredient),
                null
        );
    }
}
