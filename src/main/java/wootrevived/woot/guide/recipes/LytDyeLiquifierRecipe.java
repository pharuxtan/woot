package wootrevived.woot.guide.recipes;

import guideme.document.LytRect;
import guideme.document.block.LytBox;
import guideme.document.block.LytSlot;
import guideme.layout.LayoutContext;
import guideme.render.RenderContext;
import guideme.scene.level.GuidebookLevel;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import wootrevived.woot.client.render.dye_liquifier.DyeLiquifierContainerScreen;
import wootrevived.woot.config.DyeLiquifierConfig;
import wootrevived.woot.recipes.dye_liquifier.DyeLiquifierRecipe;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.util.render.guide.LytColorBar;
import wootrevived.woot.util.render.guide.LytEnergy;
import wootrevived.woot.util.render.guide.LytFluid;

public class LytDyeLiquifierRecipe extends LytBox {
    private static final int GUI_WIDTH = 150;
    private static final int GUI_HEIGHT = 66;

    private static final int ENERGY_X = 5;
    private static final int ENERGY_Y = 5;

    private static final int INPUT_SLOT_X = 26;
    private static final int INPUT_SLOT_Y = 24;

    private static final int COLOR_BAR_X = 68;

    private static final int RED_COLOR_BAR_Y = 10;
    private static final int YELLOW_COLOR_BAR_Y = 22;
    private static final int BLUE_COLOR_BAR_Y = 34;
    private static final int WHITE_COLOR_BAR_Y = 46;

    private static final int OUTPUT_FLUID_X = 127;
    private static final int OUTPUT_FLUID_Y = 5;

    private static final int PROGRESS_X = 47;
    private static final int PROGRESS_Y = 12;

    private final LytEnergy energy;
    private final LytSlot inputSlot;
    private final LytColorBar redColorBar;
    private final LytColorBar yellowColorBar;
    private final LytColorBar blueColorBar;
    private final LytColorBar whiteColorBar;
    private final LytFluid outputFluid;

    public LytDyeLiquifierRecipe(RecipeHolder<DyeLiquifierRecipe> recipeHolder){
        Level level = new GuidebookLevel();
        HolderSet<Item> dyes = level.registryAccess().lookupOrThrow(Registries.ITEM).getOrThrow(Tags.Items.DYES);
        DyeLiquifierRecipe recipe = recipeHolder.value();
        append(energy = new LytEnergy(recipe.getEnergy(), DyeLiquifierConfig.ENERGY_CAPACITY.get()));
        append(inputSlot = new LytSlot(Ingredient.of(dyes)));
        append(redColorBar = new LytColorBar(DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get(), Math.round(DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get() * DyeLiquifierRecipe.maxMultiplier), DyeColor.RED.getMapColor().calculateARGBColor(MapColor.Brightness.HIGH), Component.translatable("info.woot_revived.dye.red")));
        append(yellowColorBar = new LytColorBar(DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get(), Math.round(DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get() * DyeLiquifierRecipe.maxMultiplier), DyeColor.YELLOW.getMapColor().calculateARGBColor(MapColor.Brightness.HIGH), Component.translatable("info.woot_revived.dye.yellow")));
        append(blueColorBar = new LytColorBar(DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get(), Math.round(DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get() * DyeLiquifierRecipe.maxMultiplier), DyeColor.BLUE.getMapColor().calculateARGBColor(MapColor.Brightness.HIGH), Component.translatable("info.woot_revived.dye.blue")));
        append(whiteColorBar = new LytColorBar(DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get(), Math.round(DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get() * DyeLiquifierRecipe.maxMultiplier), DyeColor.WHITE.getMapColor().calculateARGBColor(MapColor.Brightness.HIGH), Component.translatable("info.woot_revived.dye.white")));
        append(outputFluid = new LytFluid(new FluidStack(FluidsRegistry.SOURCE_PURE_DYE_FLUID.get(), DyeLiquifierConfig.PURE_DYE_PRODUCE_AMOUNT.get()), DyeLiquifierConfig.OUTPUT_TANK_CAPACITY.get()));
    }

    @Override
    protected LytRect computeBoxLayout(LayoutContext context, int x, int y, int availableWidth) {
        energy.layout(context, x + ENERGY_X, y + ENERGY_Y, availableWidth);
        inputSlot.layout(context, x + INPUT_SLOT_X, y + INPUT_SLOT_Y, availableWidth);
        redColorBar.layout(context, x + COLOR_BAR_X, y + RED_COLOR_BAR_Y, availableWidth);
        yellowColorBar.layout(context, x + COLOR_BAR_X, y + YELLOW_COLOR_BAR_Y, availableWidth);
        blueColorBar.layout(context, x + COLOR_BAR_X, y + BLUE_COLOR_BAR_Y, availableWidth);
        whiteColorBar.layout(context, x + COLOR_BAR_X, y + WHITE_COLOR_BAR_Y, availableWidth);
        outputFluid.layout(context, x + OUTPUT_FLUID_X, y + OUTPUT_FLUID_Y, availableWidth);
        return new LytRect(x, y, GUI_WIDTH, GUI_HEIGHT);
    }

    @Override
    public void render(RenderContext context) {
        context.renderPanel(bounds);

        DyeLiquifierContainerScreen.renderProgressBg(context.guiGraphics(), bounds.x() + PROGRESS_X, bounds.y() + PROGRESS_Y);

        super.render(context);
    }
}
