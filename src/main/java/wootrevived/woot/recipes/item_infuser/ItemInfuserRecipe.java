package wootrevived.woot.recipes.item_infuser;

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
import java.util.Optional;

public class ItemInfuserRecipe implements Recipe<WootRecipeInput> {
    public static final MapCodec<ItemInfuserRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("energy").forGetter(ItemInfuserRecipe::getEnergy),
            FluidStack.CODEC.fieldOf("fluid").forGetter(ItemInfuserRecipe::getFluid),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(ItemInfuserRecipe::getIngredient),
            Ingredient.CODEC.optionalFieldOf("augment").forGetter(ItemInfuserRecipe::getAugment),
            ItemStack.CODEC.fieldOf("output").forGetter(ItemInfuserRecipe::getOutput)
    ).apply(inst, ItemInfuserRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemInfuserRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ItemInfuserRecipe::getEnergy,
            FluidStack.STREAM_CODEC, ItemInfuserRecipe::getFluid,
            Ingredient.CONTENTS_STREAM_CODEC, ItemInfuserRecipe::getIngredient,
            ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC), ItemInfuserRecipe::getAugment,
            ItemStack.STREAM_CODEC, ItemInfuserRecipe::getOutput,
            ItemInfuserRecipe::new
    );

    private final int energy;
    private final FluidStack fluid;
    private final Ingredient ingredient;
    private final Optional<Ingredient> augment;
    private final ItemStack output;

    public ItemInfuserRecipe(int energy, @NotNull FluidStack fluid, @NotNull Ingredient ingredient, @NotNull Optional<Ingredient> augment, @NotNull ItemStack output) {
        this.energy = energy;
        this.fluid = fluid;
        this.ingredient = ingredient;
        this.augment = augment;
        this.output = output;
    }

    @Override
    public RecipeSerializer<? extends Recipe<WootRecipeInput>> getSerializer() {
        return RecipesRegistry.ITEM_INFUSER_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<WootRecipeInput>> getType() {
        return RecipesRegistry.ITEM_INFUSER_RECIPE_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return augment.map(value -> PlacementInfo.create(List.of(ingredient, value))).orElseGet(() -> PlacementInfo.create(ingredient));
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipesRegistry.ITEM_INFUSER_RECIPE_BOOK_CATEGORY.get();
    }

    public @NotNull FluidStack getFluid(){
        return this.fluid.copy();
    }

    public @NotNull Ingredient getIngredient(){
        return this.ingredient;
    }

    public @NotNull Optional<Ingredient> getAugment(){
        return this.augment;
    }

    public @NotNull ItemStack getOutput(){
        return output.copy();
    }

    public int getEnergy() {
        return energy;
    }

    public int ingredientCount(Item item){
        return ingredient.test(item.getDefaultInstance()) ? 1 : 0;
    }

    public int augmentCount(Item item){
        return augment.filter(value -> value.test(item.getDefaultInstance())).map(value -> 1).orElse(0);
    }

    @Override
    public boolean matches(@NotNull WootRecipeInput input, @NotNull Level level) {
        if(!FluidStack.isSameFluidSameComponents(getFluid(), input.getFluid(0)))
            return false;

        if(!getIngredient().test(input.getItem(1)))
            return false;

        return getAugment().isEmpty() || getAugment().get().test(input.getItem(2));
    }

    public static void loadRecipes(@NotNull RecipeMap map){
        Validator.clear();
        for(RecipeHolder<ItemInfuserRecipe> recipeHolder : map.byType(RecipesRegistry.ITEM_INFUSER_RECIPE_TYPE.get())) {
            Validator.add(recipeHolder.value().ingredient, recipeHolder.value().augment, recipeHolder.value().fluid);
        }
    }

    public static class Validator {
        private static final List<Ingredient> validIngredients = new ArrayList<>();
        private static final List<Ingredient> validAugments = new ArrayList<>();
        private static final List<FluidStack> validFluids = new ArrayList<>();

        public static boolean isIngredientValid(ItemStack item){
            for(Ingredient ingredient : validIngredients){
                if(ingredient.test(item))
                    return true;
            }
            return false;
        }

        public static boolean isAugmentValid(ItemStack item){
            for(Ingredient ingredient : validAugments){
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

        protected static void add(Ingredient ingredient, Optional<Ingredient> augment, FluidStack fluid){
            validIngredients.add(ingredient);
            augment.ifPresent(validAugments::add);
            validFluids.add(fluid);
        }

        protected static void clear(){
            validIngredients.clear();
            validAugments.clear();
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
