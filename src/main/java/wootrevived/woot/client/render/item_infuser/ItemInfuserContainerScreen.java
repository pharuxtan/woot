package wootrevived.woot.client.render.item_infuser;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import wootrevived.woot.blocks.item_infuser.ItemInfuserBlockEntity;
import wootrevived.woot.config.ItemInfuserConfig;
import wootrevived.woot.util.render.WootContainerScreen;

public class ItemInfuserContainerScreen extends WootContainerScreen<ItemInfuserContainerMenu> {
    private static final int ENERGY_X = 10;
    private static final int ENERGY_Y = 20;

    private static final int INPUT_FLUID_X = 31;
    private static final int INPUT_FLUID_Y = 20;
    private static final int INPUT_FLUID_COLOR = 0xFF253192;

    private static final int INGREDIENT_SLOT_X = 64;
    private static final int INGREDIENT_SLOT_Y = 39;
    private static final int INGREDIENT_SLOT_COLOR = 0xFF4C7F99;

    private static final int AUGMENT_SLOT_X = 84;
    private static final int AUGMENT_SLOT_Y = 39;
    private static final int AUGMENT_SLOT_COLOR = 0xFF993333;

    private static final int OUTPUT_SLOT_X = 140;
    private static final int OUTPUT_SLOT_Y = 39;
    private static final int OUTPUT_SLOT_COLOR = 0xFF7FCC19;

    private static final int PROGRESS_X = 110;
    private static final int PROGRESS_Y = 40;

    public ItemInfuserContainerScreen(ItemInfuserContainerMenu container, Inventory playerInventory, Component name) {
        super(container, playerInventory, name);
    }

    @Override
    protected void initButtons() {
        createFluidSideButton(INPUT_FLUID_X, INPUT_FLUID_Y, INPUT_FLUID_COLOR, ItemInfuserBlockEntity.INPUT_FLUID_PROPERTY, Component.translatable("info.woot_revived.input_fluid"));
        createSlotSideButton(INGREDIENT_SLOT_X, INGREDIENT_SLOT_Y, INGREDIENT_SLOT_COLOR, ItemInfuserBlockEntity.INGREDIENT_PROPERTY, Component.translatable("info.woot_revived.input"));
        createSlotSideButton(AUGMENT_SLOT_X, AUGMENT_SLOT_Y, AUGMENT_SLOT_COLOR, ItemInfuserBlockEntity.AUGMENT_PROPERTY, Component.translatable("info.woot_revived.augment_input"));
        createSlotSideButton(OUTPUT_SLOT_X, OUTPUT_SLOT_Y, OUTPUT_SLOT_COLOR, ItemInfuserBlockEntity.OUTPUT_PROPERTY, Component.translatable("info.woot_revived.output"));
    }

    @Override
    protected void renderMenuBackground(GuiGraphics gui) {
        renderSlot(gui, INGREDIENT_SLOT_X, INGREDIENT_SLOT_Y, INGREDIENT_SLOT_COLOR);
        renderSlot(gui, AUGMENT_SLOT_X, AUGMENT_SLOT_Y, AUGMENT_SLOT_COLOR);
        renderSlot(gui, OUTPUT_SLOT_X, OUTPUT_SLOT_Y, OUTPUT_SLOT_COLOR);
        renderEnergyBg(gui, ENERGY_X, ENERGY_Y);
        renderFluidBg(gui, INPUT_FLUID_X, INPUT_FLUID_Y);
        renderProgressArrowBg(gui, PROGRESS_X, PROGRESS_Y);
    }

    @Override
    protected void renderState(GuiGraphics gui) {
        renderEnergy(gui, ENERGY_X, ENERGY_Y, menu.getEnergy(), ItemInfuserConfig.ENERGY_CAPACITY.get());
        renderFluid(gui, INPUT_FLUID_X, INPUT_FLUID_Y, menu.getInputFluid(), ItemInfuserConfig.INPUT_TANK_CAPACITY.get());
        renderProgressArrow(gui, PROGRESS_X, PROGRESS_Y, menu.getProgress());
    }

    @Override
    protected void renderTooltip(GuiGraphics gui, int mouseX, int mouseY){
        renderEnergyTooltip(gui, mouseX, mouseY, ENERGY_X, ENERGY_Y, menu.getEnergy(), ItemInfuserConfig.ENERGY_CAPACITY.get());
        renderFluidTooltip(gui, mouseX, mouseY, INPUT_FLUID_X, INPUT_FLUID_Y, menu.getInputFluid(), ItemInfuserConfig.INPUT_TANK_CAPACITY.get());
        renderProgressArrowTooltip(gui, mouseX, mouseY, PROGRESS_X, PROGRESS_Y, menu.getProgress(), menu.getLeftSeconds(), menu.getEnergyProcessTransfer());
    }
}
