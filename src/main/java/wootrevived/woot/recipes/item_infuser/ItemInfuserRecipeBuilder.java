package wootrevived.woot.recipes.item_infuser;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.BlocksRegistry;

import java.util.Optional;

public class ItemInfuserRecipeBuilder {
    private FluidStack fluid;
    private Ingredient ingredient;
    private Optional<Ingredient> augment;
    private final ItemStack output;
    private int energy;

    protected ItemInfuserRecipeBuilder(ItemLike output, int count) {
        this.output = output.asItem().getDefaultInstance();
        this.output.setCount(count);
        this.augment = Optional.empty();
    }

    public static ItemInfuserRecipeBuilder itemInfuserRecipe(ItemLike output, int count) {
        return new ItemInfuserRecipeBuilder(output, count);
    }

    public static ItemInfuserRecipeBuilder itemInfuserRecipe(ItemLike output){
        return new ItemInfuserRecipeBuilder(output, 1);
    }

    public ItemInfuserRecipeBuilder fluid(Fluid fluid, int amount){
        this.fluid = new FluidStack(fluid, amount);
        return this;
    }

    public ItemInfuserRecipeBuilder fluid(Fluid fluid){
        this.fluid = new FluidStack(fluid, 1000);
        return this;
    }

    public ItemInfuserRecipeBuilder fluid(FluidStack fluidStack){
        this.fluid = fluidStack;
        return this;
    }

    public ItemInfuserRecipeBuilder ingredient(Ingredient ingredient){
        this.ingredient = ingredient;
        return this;
    }

    public ItemInfuserRecipeBuilder augment(Ingredient augment){
        this.augment = Optional.of(augment);
        return this;
    }

    public ItemInfuserRecipeBuilder energy(int energy){
        this.energy = energy;
        return this;
    }

    public void save(RecipeOutput recipeOutput){
        save(recipeOutput, BuiltInRegistries.ITEM.getKey(output.getItem()).getPath());
    }

    public void save(RecipeOutput recipeOutput, String path){
        recipeOutput.accept(
                ResourceKey.create(Registries.RECIPE, Woot.location(BlocksRegistry.ITEM_INFUSER_TAG + "/" + path)),
                new ItemInfuserRecipe(energy, fluid, ingredient, augment, output),
                null
        );
    }
}
