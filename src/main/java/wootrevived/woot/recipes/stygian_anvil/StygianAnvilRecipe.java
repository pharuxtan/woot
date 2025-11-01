package wootrevived.woot.recipes.stygian_anvil;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.recipes.WootRecipeInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StygianAnvilRecipe implements Recipe<WootRecipeInput> {
    public static final MapCodec<StygianAnvilRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("base").forGetter(StygianAnvilRecipe::getBase),
            Ingredient.CODEC.optionalFieldOf("first_complementary").forGetter(StygianAnvilRecipe::getFirstComplementary),
            Ingredient.CODEC.optionalFieldOf("second_complementary").forGetter(StygianAnvilRecipe::getSecondComplementary),
            Ingredient.CODEC.optionalFieldOf("third_complementary").forGetter(StygianAnvilRecipe::getThirdComplementary),
            Ingredient.CODEC.optionalFieldOf("fourth_complementary").forGetter(StygianAnvilRecipe::getFourthComplementary),
            ItemStack.CODEC.fieldOf("output").forGetter(StygianAnvilRecipe::getOutput)
    ).apply(inst, StygianAnvilRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, StygianAnvilRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, StygianAnvilRecipe::getBase,
            ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC), StygianAnvilRecipe::getFirstComplementary,
            ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC), StygianAnvilRecipe::getSecondComplementary,
            ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC), StygianAnvilRecipe::getThirdComplementary,
            ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC), StygianAnvilRecipe::getFourthComplementary,
            ItemStack.STREAM_CODEC, StygianAnvilRecipe::getOutput,
            StygianAnvilRecipe::new
    );

    private final Ingredient base;
    private final Optional<Ingredient> firstComplementary;
    private final Optional<Ingredient> secondComplementary;
    private final Optional<Ingredient> thirdComplementary;
    private final Optional<Ingredient> fourthComplementary;
    private final ItemStack outputItem;

    private final int complementaryCount;

    public StygianAnvilRecipe(@NotNull Ingredient base, @NotNull Optional<Ingredient> firstComplementary, @NotNull Optional<Ingredient> secondComplementary, @NotNull Optional<Ingredient> thirdComplementary, @NotNull Optional<Ingredient> fourthComplementary, @NotNull ItemStack outputItem) {
        this.base = base;
        this.firstComplementary = firstComplementary;
        this.secondComplementary = secondComplementary;
        this.thirdComplementary = thirdComplementary;
        this.fourthComplementary = fourthComplementary;
        this.outputItem = outputItem;

        this.complementaryCount =
                Boolean.compare(firstComplementary.isPresent(), false) +
                Boolean.compare(secondComplementary.isPresent(), false) +
                Boolean.compare(thirdComplementary.isPresent(), false) +
                Boolean.compare(fourthComplementary.isPresent(), false);
    }

    @Override
    public RecipeSerializer<? extends Recipe<WootRecipeInput>> getSerializer() {
        return RecipesRegistry.ANVIL_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<WootRecipeInput>> getType() {
        return RecipesRegistry.ANVIL_RECIPE_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        List<Ingredient> ingredients = new ArrayList<>(1);
        ingredients.add(base);
        this.getFirstComplementary().ifPresent(ingredients::add);
        this.getSecondComplementary().ifPresent(ingredients::add);
        this.getThirdComplementary().ifPresent(ingredients::add);
        this.getFourthComplementary().ifPresent(ingredients::add);
        return ingredients.size() == 1 ? PlacementInfo.create(base) : PlacementInfo.create(ingredients);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipesRegistry.ANVIL_RECIPE_BOOK_CATEGORY.get();
    }

    public @NotNull Ingredient getBase(){
        return base;
    }

    public @NotNull Optional<Ingredient> getFirstComplementary(){
        return firstComplementary;
    }

    public @NotNull Optional<Ingredient> getSecondComplementary(){
        return secondComplementary;
    }

    public @NotNull Optional<Ingredient> getThirdComplementary(){
        return thirdComplementary;
    }

    public @NotNull Optional<Ingredient> getFourthComplementary(){
        return fourthComplementary;
    }

    public @NotNull ItemStack getOutput(){
        return outputItem.copy();
    }

    @Override
    public boolean matches(@NotNull WootRecipeInput input, @NotNull Level level) {
        if(!getBase().test(input.getItem(0)))
            return false;

        int count = 0;
        for(int i = 1; i < input.size(); i++){
            if(!input.getItem(i).isEmpty())
                count++;
        }

        if(complementaryCount != count)
            return false;

        List<Integer> validatedSlots = new ArrayList<>();

        return matchComplementary(input, validatedSlots, firstComplementary) &&
                matchComplementary(input, validatedSlots, secondComplementary) &&
                matchComplementary(input, validatedSlots, thirdComplementary) &&
                matchComplementary(input, validatedSlots, fourthComplementary);
    }

    private boolean matchComplementary(WootRecipeInput container, List<Integer> validatedSlots, @NotNull Optional<Ingredient> complementary){
        if(complementary.isEmpty())
            return true;

        boolean hasFound = false;

        for(int i = 1; i < container.size(); i++){
            if(!validatedSlots.contains(i) && complementary.get().test(container.getItem(i))){
                validatedSlots.add(i);
                hasFound = true;
                break;
            }
        }

        return hasFound;
    }

    public static void loadRecipes(@NotNull RecipeMap map){
        Validator.clear();
        for(RecipeHolder<StygianAnvilRecipe> recipeHolder : map.byType(RecipesRegistry.ANVIL_RECIPE_TYPE.get())) {
            Validator.add(recipeHolder.value().base, recipeHolder.value().firstComplementary, recipeHolder.value().secondComplementary, recipeHolder.value().thirdComplementary, recipeHolder.value().fourthComplementary);
        }
    }

    public static class Validator {
        private static final List<Ingredient> validBaseInputs = new ArrayList<>();
        private static final List<Ingredient> validIngredients = new ArrayList<>();

        public static boolean isBaseValid(ItemStack base){
            for(Ingredient ingredient : validBaseInputs){
                if(ingredient.test(base))
                    return true;
            }
            return false;
        }

        public static boolean isIngredientValid(ItemStack item){
            for(Ingredient ingredient : validIngredients){
                if(ingredient.test(item))
                    return true;
            }
            return false;
        }

        protected static void add(@NotNull Ingredient base, @NotNull Optional<Ingredient> firstComplementary, @NotNull Optional<Ingredient> secondComplementary, @NotNull Optional<Ingredient> thirdComplementary, @NotNull Optional<Ingredient> fourthComplementary){
            validBaseInputs.add(base);
            firstComplementary.ifPresent(validIngredients::add);
            secondComplementary.ifPresent(validIngredients::add);
            thirdComplementary.ifPresent(validIngredients::add);
            fourthComplementary.ifPresent(validIngredients::add);
        }

        protected static void clear(){
            validBaseInputs.clear();
            validIngredients.clear();
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
