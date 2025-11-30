package wootrevived.woot.recipes.dye_liquifier;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.config.DyeLiquifierConfig;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.common.WootCodecs;
import wootrevived.woot.util.recipes.WootContainer;

import java.util.ArrayList;
import java.util.List;

public class DyeLiquifierRecipe implements Recipe<WootContainer> {
    public static final MapCodec<DyeLiquifierRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("energy").forGetter(DyeLiquifierRecipe::getEnergy),
            WootCodecs.NON_NEGATIVE_FLOAT.fieldOf("red_multiplier").forGetter(DyeLiquifierRecipe::getInternalRed),
            WootCodecs.NON_NEGATIVE_FLOAT.fieldOf("yellow_multiplier").forGetter(DyeLiquifierRecipe::getInternalYellow),
            WootCodecs.NON_NEGATIVE_FLOAT.fieldOf("blue_multiplier").forGetter(DyeLiquifierRecipe::getInternalBlue),
            WootCodecs.NON_NEGATIVE_FLOAT.fieldOf("white_multiplier").forGetter(DyeLiquifierRecipe::getInternalWhite),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(DyeLiquifierRecipe::getIngredient)
    ).apply(inst, DyeLiquifierRecipe::new));

    private final Ingredient ingredient;
    private final int energy;
    private final float red;
    private final float yellow;
    private final float blue;
    private final float white;

    public DyeLiquifierRecipe(int energy, float red, float yellow, float blue, float white, @NotNull Ingredient ingredient) {
        this.ingredient = ingredient;
        this.energy = energy;
        this.red = red;
        this.yellow = yellow;
        this.blue = blue;
        this.white = white;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipesRegistry.DYE_LIQUIFIER_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipesRegistry.DYE_LIQUIFIER_RECIPE_TYPE.get();
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getRed() {
        return Math.round(red * DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get());
    }

    public int getYellow() {
        return Math.round(yellow * DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get());
    }

    public int getBlue() {
        return Math.round(blue * DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get());
    }

    public int getWhite() {
        return Math.round(white * DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get());
    }

    public float getInternalRed() {
        return red;
    }

    public float getInternalYellow() {
        return yellow;
    }

    public float getInternalBlue() {
        return blue;
    }

    public float getInternalWhite() {
        return white;
    }

    public int getEnergy() {
        return energy;
    }

    public int ingredientCount(Item item){
        for(ItemStack stack : ingredient.getItems()){
            if(stack.is(item))
                return stack.getCount();
        }

        return 0;
    }

    @Override
    public boolean matches(WootContainer container, Level level) {
        return ingredient.test(container.getItem(0));
    }

    public static float maxMultiplier = 0;

    public static void loadRecipes(@NotNull RecipeManager manager){
        Validator.clear();
        maxMultiplier = 0;
        for(RecipeHolder<DyeLiquifierRecipe> recipeHolder : manager.getAllRecipesFor(RecipesRegistry.DYE_LIQUIFIER_RECIPE_TYPE.get())) {
            DyeLiquifierRecipe dyeLiquifierRecipe = recipeHolder.value();
            Validator.add(dyeLiquifierRecipe.getIngredient(), dyeLiquifierRecipe);
            if(maxMultiplier < dyeLiquifierRecipe.getInternalRed()) maxMultiplier = dyeLiquifierRecipe.getInternalRed();
            if(maxMultiplier < dyeLiquifierRecipe.getInternalYellow()) maxMultiplier = dyeLiquifierRecipe.getInternalYellow();
            if(maxMultiplier < dyeLiquifierRecipe.getInternalBlue()) maxMultiplier = dyeLiquifierRecipe.getInternalBlue();
            if(maxMultiplier < dyeLiquifierRecipe.getInternalWhite()) maxMultiplier = dyeLiquifierRecipe.getInternalWhite();
        }
    }

    public static class Validator {
        private static final List<Pair<Ingredient, DyeLiquifierRecipe>> validIngredients = new ArrayList<>();

        public static boolean isIngredientValid(ItemStack item, Colors colors) {
            for(Pair<Ingredient, DyeLiquifierRecipe> ingredient : validIngredients){
                if(ingredient.getFirst().test(item)){
                    if(colors == Colors.ANY)
                        return true;
                    DyeLiquifierRecipe recipe = ingredient.getSecond();
                    return (colors == Colors.RED && recipe.getInternalRed() > 0.0f) ||
                            (colors == Colors.YELLOW && recipe.getInternalYellow() > 0.0f) ||
                            (colors == Colors.BLUE && recipe.getInternalBlue() > 0.0f) ||
                            (colors == Colors.WHITE && recipe.getInternalWhite() > 0.0f);
                }
            }
            return false;
        }

        protected static void add(Ingredient ingredient, DyeLiquifierRecipe recipe){
            validIngredients.add(Pair.of(ingredient, recipe));
        }

        protected static void clear(){
            validIngredients.clear();
        }
    }

    public enum Colors {
        RED,
        YELLOW,
        BLUE,
        WHITE,
        ANY
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull WootContainer container, @NotNull RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
