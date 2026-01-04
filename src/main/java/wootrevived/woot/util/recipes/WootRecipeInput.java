package wootrevived.woot.util.recipes;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

public class WootRecipeInput implements RecipeInput {
    private final int size;
    private final NonNullList<Either<ItemStack, FluidStack>> ingredients;

    @SafeVarargs
    public WootRecipeInput(Either<ItemStack, FluidStack>... ingredients) {
        this.size = ingredients.length;
        this.ingredients = NonNullList.of(Either.left(ItemStack.EMPTY), ingredients);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot >= 0 && slot < ingredients.size() ? this.ingredients.get(slot).left().orElse(ItemStack.EMPTY) : ItemStack.EMPTY;
    }

    public FluidStack getFluid(int slot) {
        return slot >= 0 && slot < ingredients.size() ? this.ingredients.get(slot).right().orElse(FluidStack.EMPTY) : FluidStack.EMPTY;
    }

    @Override
    public int size() {
        return size;
    }
}
