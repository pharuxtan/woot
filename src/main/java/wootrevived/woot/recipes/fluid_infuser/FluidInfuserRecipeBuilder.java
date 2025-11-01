package wootrevived.woot.recipes.fluid_infuser;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.BlocksRegistry;

public class FluidInfuserRecipeBuilder {
    private FluidStack inputFluid;
    private Ingredient ingredient;
    private final FluidStack outputFluid;
    private int energy;

    protected FluidInfuserRecipeBuilder(FluidStack outputFluid) {
        this.outputFluid = outputFluid;
    }

    public static FluidInfuserRecipeBuilder fluidInfuserRecipe(Fluid outputFluid, int amount) {
        return new FluidInfuserRecipeBuilder(new FluidStack(outputFluid, amount));
    }

    public static FluidInfuserRecipeBuilder fluidInfuserRecipe(Fluid outputFluid){
        return new FluidInfuserRecipeBuilder(new FluidStack(outputFluid, 1000));
    }

    public static FluidInfuserRecipeBuilder fluidInfuserRecipe(FluidStack outputFluid){
        return new FluidInfuserRecipeBuilder(outputFluid);
    }

    public FluidInfuserRecipeBuilder fluid(Fluid fluid, int amount){
        this.inputFluid = new FluidStack(fluid, amount);
        return this;
    }

    public FluidInfuserRecipeBuilder fluid(Fluid fluid){
        this.inputFluid = new FluidStack(fluid, 1000);
        return this;
    }

    public FluidInfuserRecipeBuilder fluid(FluidStack fluidStack){
        this.inputFluid = fluidStack;
        return this;
    }

    public FluidInfuserRecipeBuilder ingredient(Ingredient ingredient){
        this.ingredient = ingredient;
        return this;
    }

    public FluidInfuserRecipeBuilder energy(int energy){
        this.energy = energy;
        return this;
    }

    public void save(RecipeOutput recipeOutput, String path){
        recipeOutput.accept(
                ResourceKey.create(Registries.RECIPE, Woot.location(BlocksRegistry.FLUID_INFUSER_TAG + "/" + path)),
                new FluidInfuserRecipe(energy, inputFluid, ingredient, outputFluid),
                null
        );
    }
}
