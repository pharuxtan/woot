package wootrevived.woot.compat.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.compat.jei.WootJeiCustomFluidRenderer;
import wootrevived.woot.compat.jei.WootJeiPluginTypes;
import wootrevived.woot.config.EnchantedLiquifierConfig;
import wootrevived.woot.events.client.GlobalClientTicker;
import wootrevived.woot.recipes.enchanted_liquifier.EnchantedLiquifierRecipe;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.util.render.WootContainerScreen;

public class EnchantedLiquifierRecipeCategory implements IRecipeCategory<EnchantedLiquifierRecipe> {
    private static IDrawable icon;

    private static final int GUI_WIDTH = 85;
    private static final int GUI_HEIGHT = 56;

    private static final int ENERGY_X = 0;
    private static final int ENERGY_Y = 0;

    private static final int INPUT_SLOT_X = 21;
    private static final int INPUT_SLOT_Y = 19;

    private static final int OUTPUT_FLUID_X = 67;
    private static final int OUTPUT_FLUID_Y = 0;

    private static final int PROGRESS_X = 42;
    private static final int PROGRESS_Y = 20;

    public EnchantedLiquifierRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(BlocksRegistry.ENCHANTED_LIQUIFIER_BLOCK_ITEM.get().getDefaultInstance());
    }

    @Override
    public void draw(EnchantedLiquifierRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics gui, double mouseX, double mouseY) {
        int totalProgressTick = recipe.getEnergy() / EnchantedLiquifierConfig.ENERGY_PROCESS_TRANSFER.get();
        int progress = (GlobalClientTicker.tickCounter % totalProgressTick) * 100 / totalProgressTick;

        WootContainerScreen.renderVanillaSlot(gui, INPUT_SLOT_X, INPUT_SLOT_Y);
        WootContainerScreen.renderEnergyBg(gui, ENERGY_X, ENERGY_Y);
        WootContainerScreen.renderFluidBg(gui, OUTPUT_FLUID_X, OUTPUT_FLUID_Y);
        WootContainerScreen.renderProgressArrowBg(gui, PROGRESS_X, PROGRESS_Y);

        WootContainerScreen.renderEnergy(gui, ENERGY_X, ENERGY_Y, recipe.getEnergy(), EnchantedLiquifierConfig.ENERGY_CAPACITY.get());
        WootContainerScreen.renderProgressArrow(gui, PROGRESS_X, PROGRESS_Y, progress);

        WootContainerScreen._renderEnergyTooltip(gui, (int)mouseX, (int)mouseY, ENERGY_X, ENERGY_Y, recipe.getEnergy(), EnchantedLiquifierConfig.ENERGY_CAPACITY.get(), false, false);
        WootContainerScreen._renderProgressArrowTooltip(gui, (int)mouseX, (int)mouseY, PROGRESS_X, PROGRESS_Y, progress, Math.max(0F, totalProgressTick / 20F), EnchantedLiquifierConfig.ENERGY_PROCESS_TRANSFER.get(), false);
    }

    @Override
    public int getWidth() {
        return GUI_WIDTH;
    }

    @Override
    public int getHeight() {
        return GUI_HEIGHT;
    }

    @Override
    public @NotNull IRecipeType<EnchantedLiquifierRecipe> getRecipeType() {
        return WootJeiPluginTypes.ENCHANTED_LIQUIFIER_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("gui.woot_revived.enchanted_liquifier.name");
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, EnchantedLiquifierRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, INPUT_SLOT_X + 1, INPUT_SLOT_Y + 1)
                .addItemStacks(recipe.getIngredients());

        FluidStack outputFluid = recipe.getOutput();
        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_FLUID_X + 3, OUTPUT_FLUID_Y + 3)
                .add(outputFluid.getFluid(), outputFluid.getAmount())
                .setCustomRenderer(NeoForgeTypes.FLUID_STACK, new WootJeiCustomFluidRenderer(EnchantedLiquifierConfig.OUTPUT_TANK_CAPACITY.get()));

        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT)
                .add(outputFluid.getFluid().getBucket().getDefaultInstance());
    }
}
