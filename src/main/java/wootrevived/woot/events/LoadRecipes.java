package wootrevived.woot.events;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import wootrevived.woot.Woot;
import wootrevived.woot.recipes.dye_liquifier.DyeLiquifierRecipe;
import wootrevived.woot.recipes.enchanted_liquifier.EnchantedLiquifierRecipe;
import wootrevived.woot.recipes.fluid_infuser.FluidInfuserRecipe;
import wootrevived.woot.recipes.item_infuser.ItemInfuserRecipe;
import wootrevived.woot.recipes.stygian_anvil.StygianAnvilRecipe;
import wootrevived.woot.registries.RecipesRegistry;

@EventBusSubscriber(modid = Woot.MOD_ID)
public class LoadRecipes {
    private static MinecraftServer server;

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        server = event.getServer();
        loadRecipes(server.getRecipeManager().recipeMap(), server.overworld());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDatapackSyncEvent(OnDatapackSyncEvent event) {
        loadRecipes(server.getRecipeManager().recipeMap(), server.overworld());
        event.sendRecipes(
                RecipesRegistry.ANVIL_RECIPE_TYPE.get(),
                RecipesRegistry.DYE_LIQUIFIER_RECIPE_TYPE.get(),
                RecipesRegistry.FLUID_INFUSER_RECIPE_TYPE.get(),
                RecipesRegistry.ITEM_INFUSER_RECIPE_TYPE.get()
        );
    }

    @EventBusSubscriber(modid = Woot.MOD_ID, value = { Dist.CLIENT })
    public static class ClientSide {
        public static RecipeMap recipeMap = null;

        @SubscribeEvent
        public static void onRecipesUpdated(RecipesReceivedEvent event) {
            loadRecipes(recipeMap = event.getRecipeMap(), Minecraft.getInstance().level);
        }

        @SubscribeEvent
        public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
            recipeMap = null;
        }
    }

    private static void loadRecipes(RecipeMap recipeMap, Level level) {
        StygianAnvilRecipe.loadRecipes(recipeMap);
        DyeLiquifierRecipe.loadRecipes(recipeMap);
        FluidInfuserRecipe.loadRecipes(recipeMap);
        ItemInfuserRecipe.loadRecipes(recipeMap);
        EnchantedLiquifierRecipe.loadRecipes(level);
    }
}
