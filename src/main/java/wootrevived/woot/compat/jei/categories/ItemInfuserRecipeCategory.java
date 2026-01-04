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
import wootrevived.woot.compat.jei.WootJeiCustomFluidRenderer;
import wootrevived.woot.compat.jei.WootJeiPluginTypes;
import wootrevived.woot.config.ItemInfuserConfig;
import wootrevived.woot.events.client.GlobalClientTicker;
import wootrevived.woot.recipes.item_infuser.ItemInfuserRecipe;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.util.render.WootContainerScreen;

public class ItemInfuserRecipeCategory implements IRecipeCategory<ItemInfuserRecipe> {
    private static IDrawable icon;

    private static final int GUI_WIDTH = 124;
    private static final int GUI_HEIGHT = 56;

    private static final int ENERGY_X = 0;
    private static final int ENERGY_Y = 0;

    private static final int INPUT_FLUID_X = 21;
    private static final int INPUT_FLUID_Y = 0;

    private static final int INGREDIENT_SLOT_X = 42;
    private static final int INGREDIENT_SLOT_Y = 19;

    private static final int AUGMENT_SLOT_X = 60;
    private static final int AUGMENT_SLOT_Y = 19;

    private static final int OUTPUT_SLOT_X = 106;
    private static final int OUTPUT_SLOT_Y = 19;

    private static final int PROGRESS_X = 81;
    private static final int PROGRESS_Y = 20;

    public ItemInfuserRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(BlocksRegistry.ITEM_INFUSER_BLOCK_ITEM.get().getDefaultInstance());
    }

    @Override
    public void draw(ItemInfuserRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics gui, double mouseX, double mouseY) {
        int totalProgressTick = recipe.getEnergy() / ItemInfuserConfig.ENERGY_PROCESS_TRANSFER.get();
        int progress = (GlobalClientTicker.tickCounter % totalProgressTick) * 100 / totalProgressTick;

        WootContainerScreen.renderVanillaSlot(gui, INGREDIENT_SLOT_X, INGREDIENT_SLOT_Y);
        WootContainerScreen.renderVanillaSlot(gui, AUGMENT_SLOT_X, AUGMENT_SLOT_Y);
        WootContainerScreen.renderVanillaSlot(gui, OUTPUT_SLOT_X, OUTPUT_SLOT_Y);
        WootContainerScreen.renderEnergyBg(gui, ENERGY_X, ENERGY_Y);
        WootContainerScreen.renderFluidBg(gui, INPUT_FLUID_X, INPUT_FLUID_Y);
        WootContainerScreen.renderProgressArrowBg(gui, PROGRESS_X, PROGRESS_Y);

        WootContainerScreen.renderEnergy(gui, ENERGY_X, ENERGY_Y, recipe.getEnergy(), ItemInfuserConfig.ENERGY_CAPACITY.get());
        WootContainerScreen.renderProgressArrow(gui, PROGRESS_X, PROGRESS_Y, progress);

        WootContainerScreen._renderEnergyTooltip(gui, (int)mouseX, (int)mouseY, ENERGY_X, ENERGY_Y, recipe.getEnergy(), ItemInfuserConfig.ENERGY_CAPACITY.get(), false, false);
        WootContainerScreen._renderProgressArrowTooltip(gui, (int)mouseX, (int)mouseY, PROGRESS_X, PROGRESS_Y, progress, Math.max(0F, totalProgressTick/ 20F), ItemInfuserConfig.ENERGY_PROCESS_TRANSFER.get(), false);
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
    public IRecipeType<ItemInfuserRecipe> getRecipeType() {
        return WootJeiPluginTypes.ITEM_INFUSER_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.woot_revived.item_infuser.name");
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ItemInfuserRecipe recipe, IFocusGroup focuses) {
        FluidStack inputFluid = recipe.getFluid();

        builder.addSlot(RecipeIngredientRole.INPUT, INPUT_FLUID_X + 3, INPUT_FLUID_Y + 3)
                .add(inputFluid.getFluid(), inputFluid.getAmount())
                .setCustomRenderer(NeoForgeTypes.FLUID_STACK, new WootJeiCustomFluidRenderer(ItemInfuserConfig.INPUT_TANK_CAPACITY.get()));

        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT)
               .add(inputFluid.getFluid().getBucket().getDefaultInstance());

        builder.addSlot(RecipeIngredientRole.INPUT, INGREDIENT_SLOT_X + 1, INGREDIENT_SLOT_Y + 1)
               .add(recipe.getIngredient());

        if(recipe.getAugment().isPresent()) {
            builder.addSlot(RecipeIngredientRole.INPUT, AUGMENT_SLOT_X + 1, AUGMENT_SLOT_Y + 1)
                   .add(recipe.getAugment().get());
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_SLOT_X + 1, OUTPUT_SLOT_Y + 1)
               .add(recipe.getOutput());
    }
}
