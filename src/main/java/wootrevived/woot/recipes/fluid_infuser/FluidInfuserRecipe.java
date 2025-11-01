package wootrevived.woot.recipes.fluid_infuser;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.recipes.WootRecipeInput;

import java.util.ArrayList;
import java.util.List;

public class FluidInfuserRecipe implements Recipe<WootRecipeInput> {
    public static final MapCodec<FluidInfuserRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("energy").forGetter(FluidInfuserRecipe::getEnergy),
            FluidStack.CODEC.fieldOf("input_fluid").forGetter(FluidInfuserRecipe::getInputFluid),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(FluidInfuserRecipe::getIngredient),
            FluidStack.CODEC.fieldOf("output_fluid").forGetter(FluidInfuserRecipe::getOutputFluid)
    ).apply(inst, FluidInfuserRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidInfuserRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, FluidInfuserRecipe::getEnergy,
            FluidStack.STREAM_CODEC, FluidInfuserRecipe::getInputFluid,
            Ingredient.CONTENTS_STREAM_CODEC, FluidInfuserRecipe::getIngredient,
            FluidStack.STREAM_CODEC, FluidInfuserRecipe::getOutputFluid,
            FluidInfuserRecipe::new
    );

    private final int energy;
    private final FluidStack inputFluid;
    private final Ingredient ingredient;
    private final FluidStack outputFluid;

    public FluidInfuserRecipe(int energy, @NotNull FluidStack inputFluid, @NotNull Ingredient ingredient, @NotNull FluidStack outputFluid) {
        this.energy = energy;
        this.inputFluid = inputFluid;
        this.ingredient = ingredient;
        this.outputFluid = outputFluid;
    }

    @Override
    public RecipeSerializer<? extends Recipe<WootRecipeInput>> getSerializer() {
        return RecipesRegistry.FLUID_INFUSER_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<WootRecipeInput>> getType() {
        return RecipesRegistry.FLUID_INFUSER_RECIPE_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(ingredient);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipesRegistry.FLUID_INFUSER_RECIPE_BOOK_CATEGORY.get();
    }

    public FluidStack getInputFluid(){
        return this.inputFluid.copy();
    }

    public Ingredient getIngredient(){
        return this.ingredient;
    }

    public FluidStack getOutputFluid(){
        return this.outputFluid.copy();
    }

    public int getEnergy() {
        return energy;
    }

    public int ingredientCount(Item item){
        return ingredient.test(item.getDefaultInstance()) ? 1 : 0;
    }

    @Override
    public boolean matches(@NotNull WootRecipeInput input, @NotNull Level level) {
        if(!FluidStack.isSameFluidSameComponents(getInputFluid(), input.getFluid(0)))
            return false;

        return getIngredient().test(input.getItem(1));
    }

    public static void loadRecipes(@NotNull RecipeMap map){
        Validator.clear();
        for(RecipeHolder<FluidInfuserRecipe> recipeHolder : map.byType(RecipesRegistry.FLUID_INFUSER_RECIPE_TYPE.get())) {
            Validator.add(recipeHolder.value().ingredient, recipeHolder.value().inputFluid);
        }
    }

    public static class Validator {
        private static final List<Ingredient> validIngredients = new ArrayList<>();
        private static final List<FluidStack> validFluids = new ArrayList<>();

        public static boolean isCatalystValid(ItemStack item){
            for(Ingredient ingredient : validIngredients){
                if(ingredient.test(item))
                    return true;
            }
            return false;
        }

        public static boolean isFluidValid(FluidStack fluid){
            for(FluidStack fluidStack : validFluids){
                if(FluidStack.isSameFluidSameComponents(fluidStack, fluid))
                    return true;
            }
            return false;
        }

        protected static void add(Ingredient ingredient, FluidStack fluid){
            validIngredients.add(ingredient);
            validFluids.add(fluid);
        }

        protected static void clear(){
            validIngredients.clear();
            validFluids.clear();
        }
    }

    @Override
    public @NotNull ItemStack assemble(WootRecipeInput input, HolderLookup.@NotNull Provider provider){
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
