package wootrevived.woot.recipes.enchanted_liquifier;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import wootrevived.woot.registries.RecipesRegistry;
import wootrevived.woot.util.recipes.WootRecipeInput;

import java.util.ArrayList;
import java.util.List;

// Recipe class for JEI
public class EnchantedLiquifierRecipe implements Recipe<WootRecipeInput> {
    private final int energy;
    private final List<ItemStack> ingredients;
    private final FluidStack output;

    public EnchantedLiquifierRecipe(int energy, List<ItemStack> ingredients, FluidStack output) {
        this.energy = energy;
        this.ingredients = ingredients;
        this.output = output;
    }

    public List<ItemStack> getIngredients(){
        return ingredients;
    }

    public FluidStack getOutput(){
        return this.output;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public RecipeSerializer<? extends Recipe<WootRecipeInput>> getSerializer() {
        throw new IllegalStateException("Enchanted Serializer shouldn't exist");
    }

    @Override
    public RecipeType<? extends Recipe<WootRecipeInput>> getType() {
        return RecipesRegistry.ENCHANTED_LIQUIFIER_RECIPE_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(Ingredient.of(ingredients.stream().map(ItemStack::getItem)));
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipesRegistry.ENCHANTED_LIQUIFIER_RECIPE_BOOK_CATEGORY.get();
    }

    @Override
    public boolean matches(WootRecipeInput input, Level level) {
        return false;
    }

    public static List<Holder<Enchantment>> enchantments = new ArrayList<>();

    public static void loadRecipes(Level level){
        RegistryAccess accessor = level.registryAccess();
        HolderLookup.RegistryLookup<Enchantment> lookup = accessor.lookupOrThrow(Registries.ENCHANTMENT);

        enchantments.clear();

        lookup.listElements().forEach(enchantment -> {
            enchantments.add(enchantment);
        });
    }

    @Override
    public ItemStack assemble(WootRecipeInput input, HolderLookup.Provider provider){
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
