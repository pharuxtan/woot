package wootrevived.woot.client.render.fluid_infuser;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.blocks.fluid_infuser.FluidInfuserBlockEntity;
import wootrevived.woot.config.FluidInfuserConfig;
import wootrevived.woot.util.render.WootContainerScreen;

public class FluidInfuserContainerScreen extends WootContainerScreen<FluidInfuserContainerMenu> {
    private static final int ENERGY_X = 10;
    private static final int ENERGY_Y = 20;

    private static final int INPUT_FLUID_X = 42;
    private static final int INPUT_FLUID_Y = 20;
    private static final int INPUT_FLUID_COLOR = 0xFF253192;

    private static final int INPUT_SLOT_X = 91;
    private static final int INPUT_SLOT_Y = 24;
    private static final int INPUT_SLOT_COLOR = 0xFF4C7F99;

    private static final int OUTPUT_FLUID_X = GUI_XSIZE - 36;
    private static final int OUTPUT_FLUID_Y = 20;
    private static final int OUTPUT_FLUID_COLOR = 0xFF9658BF;

    private static final int PROGRESS_X = 68;
    private static final int PROGRESS_Y = 44;

    public FluidInfuserContainerScreen(FluidInfuserContainerMenu container, Inventory playerInventory, Component name) {
        super(container, playerInventory, name);
    }

    @Override
    protected void initButtons() {
        createFluidSideButton(INPUT_FLUID_X, INPUT_FLUID_Y, INPUT_FLUID_COLOR, FluidInfuserBlockEntity.INPUT_FLUID_PROPERTY, Component.translatable("info.woot_revived.input_fluid"));
        createSlotSideButton(INPUT_SLOT_X, INPUT_SLOT_Y, INPUT_SLOT_COLOR, FluidInfuserBlockEntity.INGREDIENT_PROPERTY, Component.translatable("info.woot_revived.input"));
        createFluidSideButton(OUTPUT_FLUID_X, OUTPUT_FLUID_Y, OUTPUT_FLUID_COLOR, FluidInfuserBlockEntity.OUTPUT_FLUID_PROPERTY, Component.translatable("info.woot_revived.output_fluid"));
    }

    @Override
    protected void renderMenuBackground(@NotNull GuiGraphics gui) {
        renderSlot(gui, INPUT_SLOT_X, INPUT_SLOT_Y, INPUT_SLOT_COLOR);
        renderEnergyBg(gui, ENERGY_X, ENERGY_Y);
        renderFluidBg(gui, INPUT_FLUID_X, INPUT_FLUID_Y);
        renderFluidBg(gui, OUTPUT_FLUID_X, OUTPUT_FLUID_Y);
        renderProgressBg(gui, PROGRESS_X, PROGRESS_Y);
    }

    @Override
    protected void renderState(@NotNull GuiGraphics gui) {
        renderEnergy(gui, ENERGY_X, ENERGY_Y, menu.getEnergy(), FluidInfuserConfig.ENERGY_CAPACITY.get());
        renderFluid(gui, INPUT_FLUID_X, INPUT_FLUID_Y, menu.getInputFluid(), FluidInfuserConfig.INPUT_TANK_CAPACITY.get());
        renderFluid(gui, OUTPUT_FLUID_X, OUTPUT_FLUID_Y, menu.getOutputFluid(), FluidInfuserConfig.OUTPUT_TANK_CAPACITY.get());
        renderProgress(gui, PROGRESS_X, PROGRESS_Y, menu.getProgress());
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY){
        renderEnergyTooltip(gui, mouseX, mouseY, ENERGY_X, ENERGY_Y, menu.getEnergy(), FluidInfuserConfig.ENERGY_CAPACITY.get());
        renderFluidTooltip(gui, mouseX, mouseY, INPUT_FLUID_X, INPUT_FLUID_Y, menu.getInputFluid(), FluidInfuserConfig.INPUT_TANK_CAPACITY.get());
        renderFluidTooltip(gui, mouseX, mouseY, OUTPUT_FLUID_X, OUTPUT_FLUID_Y, menu.getOutputFluid(), FluidInfuserConfig.OUTPUT_TANK_CAPACITY.get());
        renderProgressTooltip(gui, mouseX, mouseY, PROGRESS_X, PROGRESS_Y, menu.getProgress(), menu.getLeftSeconds(), menu.getEnergyProcessTransfer());
    }

    public static void renderProgressBg(@NotNull GuiGraphics gui, int x, int y){
        gui.blit(RenderPipelines.GUI_TEXTURED, GUI, x, y, 177, 81, 65, 24, WootContainerScreen.ATLAS_WIDTH, WootContainerScreen.ATLAS_HEIGHT);
    }

    public static void renderProgress(@NotNull GuiGraphics gui, int x, int y, int progress){
        int fillWidth = Mth.clamp(progress * 65 / 100, 0, 65);
        gui.blit(RenderPipelines.GUI_TEXTURED, GUI, x, y, 177, 106, fillWidth, 25, WootContainerScreen.ATLAS_WIDTH, WootContainerScreen.ATLAS_HEIGHT);
    }

    public void renderProgressTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY, int x, int y, int progress, float eta, int usage){
        renderProgressTooltip(gui, mouseX, mouseY, x, y, 65, 25, progress, eta, usage);
    }

    public static void _renderProgressTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY, int x, int y, int progress, float eta, int usage, boolean skipHover){
        _renderProgressTooltip(gui, mouseX, mouseY, x, y, 65, 25, progress, eta, usage, skipHover);
    }
}
