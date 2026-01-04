package wootrevived.woot.client.render.enchanted_liquifier;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import wootrevived.woot.blocks.enchanted_liquifier.EnchantedLiquifierBlockEntity;
import wootrevived.woot.config.EnchantedLiquifierConfig;
import wootrevived.woot.util.render.WootContainerScreen;

public class EnchantedLiquifierContainerScreen extends WootContainerScreen<EnchantedLiquifierContainerMenu> {
    private static final int ENERGY_X = 10;
    private static final int ENERGY_Y = 20;

    private static final int INPUT_SLOT_X = 79;
    private static final int INPUT_SLOT_Y = 39;
    private static final int INPUT_SLOT_COLOR = 0xFF9D1B37;

    private static final int OUTPUT_FLUID_X = GUI_XSIZE - 28;
    private static final int OUTPUT_FLUID_Y = 20;
    private static final int OUTPUT_FLUID_COLOR = 0xFF440090;

    private static final int PROGRESS_X = 112;
    private static final int PROGRESS_Y = 40;

    public EnchantedLiquifierContainerScreen(EnchantedLiquifierContainerMenu container, Inventory playerInventory, Component name) {
        super(container, playerInventory, name);
    }

    @Override
    protected void initButtons(){
        createSlotSideButton(INPUT_SLOT_X, INPUT_SLOT_Y, INPUT_SLOT_COLOR, EnchantedLiquifierBlockEntity.INGREDIENT_PROPERTY, Component.translatable("info.woot_revived.enchanted_liquifier.input"));
        createFluidSideButton(OUTPUT_FLUID_X, OUTPUT_FLUID_Y, OUTPUT_FLUID_COLOR, EnchantedLiquifierBlockEntity.OUTPUT_FLUID_PROPERTY, Component.translatable("info.woot_revived.enchanted_liquifier.output"));
    }

    @Override
    protected void renderMenuBackground(GuiGraphics gui) {
        renderSlot(gui, INPUT_SLOT_X, INPUT_SLOT_Y, INPUT_SLOT_COLOR);
        renderEnergyBg(gui, ENERGY_X, ENERGY_Y);
        renderFluidBg(gui, OUTPUT_FLUID_X, OUTPUT_FLUID_Y);
        renderProgressArrowBg(gui, PROGRESS_X, PROGRESS_Y);
    }

    @Override
    protected void renderState(GuiGraphics gui){
        renderEnergy(gui, ENERGY_X, ENERGY_Y, menu.getEnergy(), EnchantedLiquifierConfig.ENERGY_CAPACITY.get());
        renderFluid(gui, OUTPUT_FLUID_X, OUTPUT_FLUID_Y, menu.getOutputFluid(), EnchantedLiquifierConfig.OUTPUT_TANK_CAPACITY.get());
        renderProgressArrow(gui, PROGRESS_X, PROGRESS_Y, menu.getProgress());
    }

    @Override
    protected void renderTooltip(GuiGraphics gui, int mouseX, int mouseY){
        renderEnergyTooltip(gui, mouseX, mouseY, ENERGY_X, ENERGY_Y, menu.getEnergy(), EnchantedLiquifierConfig.ENERGY_CAPACITY.get());
        renderFluidTooltip(gui, mouseX, mouseY, OUTPUT_FLUID_X, OUTPUT_FLUID_Y, menu.getOutputFluid(), EnchantedLiquifierConfig.OUTPUT_TANK_CAPACITY.get());
        renderProgressArrowTooltip(gui, mouseX, mouseY, PROGRESS_X, PROGRESS_Y, menu.getProgress(), menu.getLeftSeconds(), menu.getEnergyProcessTransfer());
    }
}
