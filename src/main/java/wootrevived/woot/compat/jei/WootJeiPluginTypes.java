package wootrevived.woot.compat.jei;

import mezz.jei.api.recipe.types.IRecipeType;
import wootrevived.woot.Woot;
import wootrevived.woot.recipes.dye_liquifier.DyeLiquifierRecipe;
import wootrevived.woot.recipes.enchanted_liquifier.EnchantedLiquifierRecipe;
import wootrevived.woot.recipes.fluid_infuser.FluidInfuserRecipe;
import wootrevived.woot.recipes.item_infuser.ItemInfuserRecipe;
import wootrevived.woot.recipes.stygian_anvil.StygianAnvilRecipe;
import wootrevived.woot.registries.BlocksRegistry;

public class WootJeiPluginTypes {
    public static final IRecipeType<StygianAnvilRecipe> STYGIAN_ANVIL_TYPE = IRecipeType.create(Woot.MOD_NAMESPACE, BlocksRegistry.STYGIAN_ANVIL_TAG, StygianAnvilRecipe.class);
    public static final IRecipeType<DyeLiquifierRecipe> DYE_LIQUIFIER_TYPE = IRecipeType.create(Woot.MOD_NAMESPACE, BlocksRegistry.DYE_LIQUIFIER_TAG, DyeLiquifierRecipe.class);
    public static final IRecipeType<FluidInfuserRecipe> FLUID_INFUSER_TYPE = IRecipeType.create(Woot.MOD_NAMESPACE, BlocksRegistry.FLUID_INFUSER_TAG, FluidInfuserRecipe.class);
    public static final IRecipeType<ItemInfuserRecipe> ITEM_INFUSER_TYPE = IRecipeType.create(Woot.MOD_NAMESPACE, BlocksRegistry.ITEM_INFUSER_TAG, ItemInfuserRecipe.class);
    public static final IRecipeType<EnchantedLiquifierRecipe> ENCHANTED_LIQUIFIER_TYPE = IRecipeType.create(Woot.MOD_NAMESPACE, BlocksRegistry.ENCHANTED_LIQUIFIER_TAG, EnchantedLiquifierRecipe.class);
}
