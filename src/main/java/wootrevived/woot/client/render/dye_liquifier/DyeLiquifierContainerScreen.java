package wootrevived.woot.client.render.dye_liquifier;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.blocks.dye_liquifier.DyeLiquifierBlockEntity;
import wootrevived.woot.config.DyeLiquifierConfig;
import wootrevived.woot.util.render.WootContainerScreen;

public class DyeLiquifierContainerScreen extends WootContainerScreen<DyeLiquifierContainerMenu> {
    private static final int ENERGY_X = 8;
    private static final int ENERGY_Y = 20;

    private static final int RED_INPUT_SLOT_X = 30;
    private static final int RED_INPUT_SLOT_Y = 28;
    private static final int RED_INPUT_SLOT_COLOR = 0xFFD2443f;

    private static final int YELLOW_INPUT_SLOT_X = 50;
    private static final int YELLOW_INPUT_SLOT_Y = 28;
    private static final int YELLOW_INPUT_SLOT_COLOR = 0xFFE7E72A;

    private static final int BLUE_INPUT_SLOT_X = 30;
    private static final int BLUE_INPUT_SLOT_Y = 48;
    private static final int BLUE_INPUT_SLOT_COLOR = 0xFF5A82E2;

    private static final int WHITE_INPUT_SLOT_X = 50;
    private static final int WHITE_INPUT_SLOT_Y = 48;
    private static final int WHITE_INPUT_SLOT_COLOR = 0xFF74747F;

    private static final int COLOR_BAR_X = 91;

    private static final int RED_COLOR_BAR_Y = 24;
    private static final int YELLOW_COLOR_BAR_Y = 36;
    private static final int BLUE_COLOR_BAR_Y = 48;
    private static final int WHITE_COLOR_BAR_Y = 60;

    private static final int OUTPUT_FLUID_X = GUI_XSIZE - 26;
    private static final int OUTPUT_FLUID_Y = 20;
    private static final int OUTPUT_FLUID_COLOR = 0xFF99488F;

    private static final int PROGRESS_X = 71;
    private static final int PROGRESS_Y = 26;

    public DyeLiquifierContainerScreen(DyeLiquifierContainerMenu container, Inventory playerInventory, Component name) {
        super(container, playerInventory, name);
    }

    @Override
    protected void initButtons() {
        createSlotSideButton(RED_INPUT_SLOT_X, RED_INPUT_SLOT_Y, RED_INPUT_SLOT_COLOR, DyeLiquifierBlockEntity.RED_INGREDIENT_PROPERTY, Component.translatable("info.woot_revived.dye_liquifier.red_input"));
        createSlotSideButton(YELLOW_INPUT_SLOT_X, YELLOW_INPUT_SLOT_Y, YELLOW_INPUT_SLOT_COLOR, DyeLiquifierBlockEntity.YELLOW_INGREDIENT_PROPERTY, Component.translatable("info.woot_revived.dye_liquifier.yellow_input"));
        createSlotSideButton(BLUE_INPUT_SLOT_X, BLUE_INPUT_SLOT_Y, BLUE_INPUT_SLOT_COLOR, DyeLiquifierBlockEntity.BLUE_INGREDIENT_PROPERTY, Component.translatable("info.woot_revived.dye_liquifier.blue_input"));
        createSlotSideButton(WHITE_INPUT_SLOT_X, WHITE_INPUT_SLOT_Y, WHITE_INPUT_SLOT_COLOR, DyeLiquifierBlockEntity.WHITE_INGREDIENT_PROPERTY, Component.translatable("info.woot_revived.dye_liquifier.white_input"));
        createFluidSideButton(OUTPUT_FLUID_X, OUTPUT_FLUID_Y, OUTPUT_FLUID_COLOR, DyeLiquifierBlockEntity.OUTPUT_FLUID_PROPERTY, Component.translatable("info.woot_revived.dye_liquifier.output"));
    }

    @Override
    protected void renderMenuBackground(@NotNull GuiGraphics gui) {
        renderSlot(gui, RED_INPUT_SLOT_X, RED_INPUT_SLOT_Y, RED_INPUT_SLOT_COLOR);
        renderSlot(gui, YELLOW_INPUT_SLOT_X, YELLOW_INPUT_SLOT_Y, YELLOW_INPUT_SLOT_COLOR);
        renderSlot(gui, BLUE_INPUT_SLOT_X, BLUE_INPUT_SLOT_Y, BLUE_INPUT_SLOT_COLOR);
        renderSlot(gui, WHITE_INPUT_SLOT_X, WHITE_INPUT_SLOT_Y, WHITE_INPUT_SLOT_COLOR);
        renderColorBarBg(gui, COLOR_BAR_X, RED_COLOR_BAR_Y, DyeColor.RED.getMapColor().calculateARGBColor(MapColor.Brightness.HIGH));
        renderColorBarBg(gui, COLOR_BAR_X, YELLOW_COLOR_BAR_Y, DyeColor.YELLOW.getMapColor().calculateARGBColor(MapColor.Brightness.HIGH));
        renderColorBarBg(gui, COLOR_BAR_X, BLUE_COLOR_BAR_Y, DyeColor.BLUE.getMapColor().calculateARGBColor(MapColor.Brightness.HIGH));
        renderColorBarBg(gui, COLOR_BAR_X, WHITE_COLOR_BAR_Y, DyeColor.WHITE.getMapColor().calculateARGBColor(MapColor.Brightness.HIGH));
        renderEnergyBg(gui, ENERGY_X, ENERGY_Y);
        renderFluidBg(gui, OUTPUT_FLUID_X, OUTPUT_FLUID_Y);
        renderProgressBg(gui, PROGRESS_X, PROGRESS_Y);
    }

    @Override
    protected void renderState(@NotNull GuiGraphics gui) {
        renderEnergy(gui, ENERGY_X, ENERGY_Y, menu.getEnergy(), DyeLiquifierConfig.ENERGY_CAPACITY.get());
        renderColorBar(gui, COLOR_BAR_X, RED_COLOR_BAR_Y, menu.getRedDyeAmount(), DyeLiquifierConfig.RED_TANK_CAPACITY.get(), DyeColor.RED.getMapColor().calculateARGBColor(MapColor.Brightness.HIGH));
        renderColorBar(gui, COLOR_BAR_X, YELLOW_COLOR_BAR_Y, menu.getYellowDyeAmount(), DyeLiquifierConfig.YELLOW_TANK_CAPACITY.get(), DyeColor.YELLOW.getMapColor().calculateARGBColor(MapColor.Brightness.HIGH));
        renderColorBar(gui, COLOR_BAR_X, BLUE_COLOR_BAR_Y, menu.getBlueDyeAmount(), DyeLiquifierConfig.BLUE_TANK_CAPACITY.get(), DyeColor.BLUE.getMapColor().calculateARGBColor(MapColor.Brightness.HIGH));
        renderColorBar(gui, COLOR_BAR_X, WHITE_COLOR_BAR_Y, menu.getWhiteDyeAmount(), DyeLiquifierConfig.WHITE_TANK_CAPACITY.get(), DyeColor.WHITE.getMapColor().calculateARGBColor(MapColor.Brightness.HIGH));
        renderFluid(gui, OUTPUT_FLUID_X, OUTPUT_FLUID_Y, menu.getOutputFluid(), DyeLiquifierConfig.OUTPUT_TANK_CAPACITY.get());
        renderProgress(gui, PROGRESS_X, PROGRESS_Y, menu.getProgress());
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY){
        renderEnergyTooltip(gui, mouseX, mouseY, ENERGY_X, ENERGY_Y, menu.getEnergy(), DyeLiquifierConfig.ENERGY_CAPACITY.get());
        renderColorBarTooltip(gui, mouseX, mouseY, COLOR_BAR_X, RED_COLOR_BAR_Y, menu.getRedDyeAmount(), DyeLiquifierConfig.RED_TANK_CAPACITY.get(), Component.translatable("info.woot_revived.dye.red"));
        renderColorBarTooltip(gui, mouseX, mouseY, COLOR_BAR_X, YELLOW_COLOR_BAR_Y, menu.getYellowDyeAmount(), DyeLiquifierConfig.YELLOW_TANK_CAPACITY.get(), Component.translatable("info.woot_revived.dye.yellow"));
        renderColorBarTooltip(gui, mouseX, mouseY, COLOR_BAR_X, BLUE_COLOR_BAR_Y, menu.getBlueDyeAmount(), DyeLiquifierConfig.BLUE_TANK_CAPACITY.get(), Component.translatable("info.woot_revived.dye.blue"));
        renderColorBarTooltip(gui, mouseX, mouseY, COLOR_BAR_X, WHITE_COLOR_BAR_Y, menu.getWhiteDyeAmount(), DyeLiquifierConfig.WHITE_TANK_CAPACITY.get(), Component.translatable("info.woot_revived.dye.white"));
        renderFluidTooltip(gui, mouseX, mouseY, OUTPUT_FLUID_X, OUTPUT_FLUID_Y, menu.getOutputFluid(), DyeLiquifierConfig.OUTPUT_TANK_CAPACITY.get());
        renderProgressTooltip(gui, mouseX, mouseY, PROGRESS_X, PROGRESS_Y, menu.getProgress(), menu.getLeftSeconds(), menu.getEnergyProcessTransfer());
    }

    public static void renderProgressBg(@NotNull GuiGraphics gui, int x, int y){
        gui.blit(RenderPipelines.GUI_TEXTURED, GUI, x, y, 177, 132, 18, 43, WootContainerScreen.ATLAS_WIDTH, WootContainerScreen.ATLAS_HEIGHT);
    }

    public static void renderProgress(@NotNull GuiGraphics gui, int x, int y, int progress){
        int fillWidth = Mth.clamp(progress * 18 / 100, 0, 18);
        gui.blit(RenderPipelines.GUI_TEXTURED, GUI, x, y, 196, 132, fillWidth, 44, WootContainerScreen.ATLAS_WIDTH, WootContainerScreen.ATLAS_HEIGHT);
    }

    public void renderProgressTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY, int x, int y, int progress, float eta, int usage){
        renderProgressTooltip(gui, mouseX, mouseY, x, y, 18, 44, progress, eta, usage);
    }

    public static void _renderProgressTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY, int x, int y, int progress, float eta, int usage, boolean skipHover){
        _renderProgressTooltip(gui, mouseX, mouseY, x, y, 18, 44, progress, eta, usage, skipHover);
    }
}
