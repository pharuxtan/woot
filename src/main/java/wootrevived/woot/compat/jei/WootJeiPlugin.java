package wootrevived.woot.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.Woot;
import wootrevived.woot.compat.jei.categories.*;
import wootrevived.woot.config.DyeLiquifierConfig;
import wootrevived.woot.config.EnchantedLiquifierConfig;
import wootrevived.woot.events.LoadRecipes;
import wootrevived.woot.recipes.dye_liquifier.DyeLiquifierRecipe;
import wootrevived.woot.recipes.enchanted_liquifier.EnchantedLiquifierRecipe;
import wootrevived.woot.recipes.fluid_infuser.FluidInfuserRecipe;
import wootrevived.woot.recipes.item_infuser.ItemInfuserRecipe;
import wootrevived.woot.recipes.stygian_anvil.StygianAnvilRecipe;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.registries.ItemsRegistry;
import wootrevived.woot.registries.RecipesRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class WootJeiPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return Woot.location("jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new StygianAnvilRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new DyeLiquifierRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new FluidInfuserRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new ItemInfuserRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new EnchantedLiquifierRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        RecipeMap recipeMap = LoadRecipes.ClientSide.recipeMap;

        List<StygianAnvilRecipe> stygianAnvilRecipes = new ArrayList<>();
        List<DyeLiquifierRecipe> dyeLiquifierRecipes = new ArrayList<>();
        List<FluidInfuserRecipe> fluidInfuserRecipes = new ArrayList<>();
        List<ItemInfuserRecipe> itemInfuserRecipes = new ArrayList<>();

        for(RecipeHolder<StygianAnvilRecipe> recipeHolder : recipeMap.byType(RecipesRegistry.ANVIL_RECIPE_TYPE.get()))
            stygianAnvilRecipes.add(recipeHolder.value());

        for(RecipeHolder<DyeLiquifierRecipe> recipeHolder : recipeMap.byType(RecipesRegistry.DYE_LIQUIFIER_RECIPE_TYPE.get()))
            dyeLiquifierRecipes.add(recipeHolder.value());

        for(RecipeHolder<FluidInfuserRecipe> recipeHolder : recipeMap.byType(RecipesRegistry.FLUID_INFUSER_RECIPE_TYPE.get()))
            fluidInfuserRecipes.add(recipeHolder.value());

        for(RecipeHolder<ItemInfuserRecipe> recipeHolder : recipeMap.byType(RecipesRegistry.ITEM_INFUSER_RECIPE_TYPE.get()))
            itemInfuserRecipes.add(recipeHolder.value());

        registration.addRecipes(WootJeiPluginTypes.STYGIAN_ANVIL_TYPE, stygianAnvilRecipes);
        registration.addRecipes(WootJeiPluginTypes.DYE_LIQUIFIER_TYPE, dyeLiquifierRecipes);
        registration.addRecipes(WootJeiPluginTypes.FLUID_INFUSER_TYPE, fluidInfuserRecipes);
        registration.addRecipes(WootJeiPluginTypes.ITEM_INFUSER_TYPE, itemInfuserRecipes);

        List<EnchantedLiquifierRecipe> enchantedLiquifierRecipes = new ArrayList<>();

        Map<Integer, List<ItemStack>> booksMap = new HashMap<>();
        for(Enchantment enchantment : EnchantedLiquifierRecipe.enchantments){
            for(int enchantLevel = enchantment.getMinLevel(); enchantLevel <= enchantment.getMaxLevel(); ++enchantLevel) {
                ItemStack itemStack = Items.ENCHANTED_BOOK.getDefaultInstance();
                itemStack.enchant(Holder.direct(enchantment), enchantLevel);
                enchantLevel = Mth.clamp(enchantLevel, 1, EnchantedLiquifierConfig.MAX_ENCHANT_LVL.get());
                booksMap.computeIfAbsent(enchantLevel, k -> new ArrayList<>());
                booksMap.get(enchantLevel).add(itemStack);
            }
        }

        for(Integer enchantLevel : booksMap.keySet()) {
            List<ItemStack> books = booksMap.get(enchantLevel);
            int amount = enchantLevel * EnchantedLiquifierConfig.PER_ENCHANT_FLUID.get();
            int energy = enchantLevel * EnchantedLiquifierConfig.PER_ENCHANT_ENERGY.get();
            enchantedLiquifierRecipes.add(
                    new EnchantedLiquifierRecipe(
                            energy,
                            books,
                            new FluidStack(FluidsRegistry.SOURCE_ENCHANTED_FLUID.get(), amount)
                    )
            );
        }

        registration.addRecipes(WootJeiPluginTypes.ENCHANTED_LIQUIFIER_TYPE, enchantedLiquifierRecipes);

        registration.addItemStackInfo(
                List.of(
                        ItemsRegistry.IRON_SHARD_ITEM.get().getDefaultInstance(),
                        ItemsRegistry.GOLD_SHARD_ITEM.get().getDefaultInstance(),
                        ItemsRegistry.DIAMOND_SHARD_ITEM.get().getDefaultInstance(),
                        ItemsRegistry.NETHERITE_SHARD_ITEM.get().getDefaultInstance()
                ),
                Component.translatable("jei.woot_revived.shard")
        );

        registration.addItemStackInfo(
                BlocksRegistry.STYGIAN_ANVIL_BLOCK_ITEM.get().getDefaultInstance(),
                Component.translatable("jei.woot_revived.anvil.0"),
                Component.translatable("jei.woot_revived.anvil.1"),
                Component.translatable("jei.woot_revived.anvil.2"),
                Component.translatable("jei.woot_revived.anvil.3")
        );

        registration.addItemStackInfo(
                ItemsRegistry.MOB_SHARD_ITEM.get().getDefaultInstance(),
                Component.translatable("jei.woot_revived.mob_shard.0"),
                Component.translatable("jei.woot_revived.mob_shard.1"),
                Component.translatable("jei.woot_revived.mob_shard.2")
        );

        registration.addItemStackInfo(
                FluidsRegistry.PURE_DYE_FLUID_BUCKET.get().getDefaultInstance(),
                Component.translatable("jei.woot_revived.pure_dye_fluid", DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get(), DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get(), DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get(), DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get())
        );

        registration.addIngredientInfo(
                new FluidStack(FluidsRegistry.SOURCE_PURE_DYE_FLUID.get(), 1),
                NeoForgeTypes.FLUID_STACK,
                Component.translatable("jei.woot_revived.pure_dye_fluid", DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get(), DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get(), DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get(), DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get())
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(WootJeiPluginTypes.STYGIAN_ANVIL_TYPE, BlocksRegistry.STYGIAN_ANVIL_BLOCK_ITEM.get());
        registration.addCraftingStation(WootJeiPluginTypes.DYE_LIQUIFIER_TYPE, BlocksRegistry.DYE_LIQUIFIER_BLOCK_ITEM.get());
        registration.addCraftingStation(WootJeiPluginTypes.FLUID_INFUSER_TYPE, BlocksRegistry.FLUID_INFUSER_BLOCK_ITEM.get());
        registration.addCraftingStation(WootJeiPluginTypes.ITEM_INFUSER_TYPE, BlocksRegistry.ITEM_INFUSER_BLOCK_ITEM.get());
        registration.addCraftingStation(WootJeiPluginTypes.ENCHANTED_LIQUIFIER_TYPE, BlocksRegistry.ENCHANTED_LIQUIFIER_BLOCK_ITEM.get());
    }
}
