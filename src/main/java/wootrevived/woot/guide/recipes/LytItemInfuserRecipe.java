package wootrevived.woot.guide.recipes;

import guideme.document.LytRect;
import guideme.document.block.LytBox;
import guideme.document.block.LytSlot;
import guideme.layout.LayoutContext;
import guideme.render.RenderContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import wootrevived.woot.config.ItemInfuserConfig;
import wootrevived.woot.recipes.item_infuser.ItemInfuserRecipe;
import wootrevived.woot.util.render.WootContainerScreen;
import wootrevived.woot.util.render.guide.LytEnergy;
import wootrevived.woot.util.render.guide.LytFluid;

public class LytItemInfuserRecipe extends LytBox {
    private static final int GUI_WIDTH = 134;
    private static final int GUI_HEIGHT = 66;

    private static final int ENERGY_X = 5;
    private static final int ENERGY_Y = 5;

    private static final int INPUT_FLUID_X = 26;
    private static final int INPUT_FLUID_Y = 5;

    private static final int INGREDIENT_SLOT_X = 47;
    private static final int INGREDIENT_SLOT_Y = 24;

    private static final int AUGMENT_SLOT_X = 65;
    private static final int AUGMENT_SLOT_Y = 24;

    private static final int OUTPUT_SLOT_X = 111;
    private static final int OUTPUT_SLOT_Y = 24;

    private static final int PROGRESS_X = 86;
    private static final int PROGRESS_Y = 25;

    private final LytEnergy energy;
    private final LytFluid inputFluid;
    private final LytSlot ingredientSlot;
    private final LytSlot augmentSlot;
    private final LytSlot outputSlot;

    public LytItemInfuserRecipe(RecipeHolder<ItemInfuserRecipe> recipeHolder){
        ItemInfuserRecipe recipe = recipeHolder.value();
        append(energy = new LytEnergy(recipe.getEnergy(), ItemInfuserConfig.ENERGY_CAPACITY.get()));
        append(inputFluid = new LytFluid(recipe.getFluid(), ItemInfuserConfig.INPUT_TANK_CAPACITY.get()));
        append(ingredientSlot = new LytSlot(recipe.getIngredient()));
        append(augmentSlot = new LytSlot(recipe.getAugment()));
        append(outputSlot = new LytSlot(recipe.getOutput()));
    }

    @Override
    protected LytRect computeBoxLayout(LayoutContext context, int x, int y, int availableWidth) {
        energy.layout(context, x + ENERGY_X, y + ENERGY_Y, availableWidth);
        inputFluid.layout(context, x + INPUT_FLUID_X, y + INPUT_FLUID_Y, availableWidth);
        ingredientSlot.layout(context, x + INGREDIENT_SLOT_X, y + INGREDIENT_SLOT_Y, availableWidth);
        augmentSlot.layout(context, x + AUGMENT_SLOT_X, y + AUGMENT_SLOT_Y, availableWidth);
        outputSlot.layout(context, x + OUTPUT_SLOT_X, y + OUTPUT_SLOT_Y, availableWidth);

        return new LytRect(x, y, GUI_WIDTH, GUI_HEIGHT);
    }

    @Override
    public void render(RenderContext context) {
        context.renderPanel(bounds);

        WootContainerScreen.renderProgressArrowBg(context.guiGraphics(), bounds.x() + PROGRESS_X, bounds.y() + PROGRESS_Y);

        super.render(context);
    }
}
