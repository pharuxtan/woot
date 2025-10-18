package wootrevived.woot.util.render;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import wootrevived.woot.Woot;
import wootrevived.woot.mixins.impl.GuiGraphicsMixin;
import wootrevived.woot.util.common.MachineSide;
import wootrevived.woot.util.common.MachineSideProperty;
import wootrevived.woot.util.common.RedstoneMode;
import wootrevived.woot.util.render.buttons.WootRedstoneButton;
import wootrevived.woot.util.render.buttons.WootSideButton;
import wootrevived.woot.util.render.buttons.WootSidePropertyButton;

import java.util.*;

import static wootrevived.woot.util.render.WootStyles.MACHINE_STYLE;
import static wootrevived.woot.util.render.WootStyles.UNIT_STYLE;

/*
 * All Woot GUIs are inspired by InnovativeOnlineIndustries Mods and retake some GUI assets from their Titanium mod
 * https://github.com/InnovativeOnlineIndustries
 * https://github.com/InnovativeOnlineIndustries/Titanium/blob/1.20/LICENSE.md - LGPLv3
 */

public abstract class WootContainerScreen<T extends WootContainerMenu> extends AbstractContainerScreen<T> {
    public static final ResourceLocation GUI = Woot.location("textures/gui/atlas.png");

    protected static final int GUI_XSIZE = 176;
    protected static final int GUI_YSIZE = 184;

    private int xSides = 8;
    private final int ySides = 84;
    private final List<WootSideButton> sideButtons = new ArrayList<>();
    private int activeSideButton = -1;
    private final Direction machineFacing;

    private final List<WootButton> buttons = new ArrayList<>();
    public void addRenderableWidget(WootButton button){
        buttons.add(button);
        addWidget(button);
    }

    private final Map<MachineSide, WootSidePropertyButton> propertyButtons = Maps.newEnumMap(MachineSide.class);

    public WootContainerScreen(T container, Inventory inventory, Component title) {
        super(container, inventory, title);
        imageWidth = GUI_XSIZE;
        imageHeight = GUI_YSIZE;
        machineFacing = container.getMachineFacing();
    }

    @Override
    protected void init(){
        super.init();

        buttons.clear();

        addRenderableWidget(new WootRedstoneButton(leftPos + 154, topPos + 84, menu.getRedstoneMode(), button -> {
            RedstoneMode mode = button.nextMode();
            menu.setRedstoneMode(mode);
        }));

        xSides = 8;

        sideButtons.clear();

        initButtons();

        if(activeSideButton != -1)
            sideButtons.get(activeSideButton).isViewActive = true;

        createPropertyButton(81, 105, MachineSide.TOP);
        createPropertyButton(65, 121, MachineSide.LEFT);
        createPropertyButton(81, 121, MachineSide.FRONT);
        createPropertyButton(97, 121, MachineSide.RIGHT);
        createPropertyButton(81, 137, MachineSide.BOTTOM);
        createPropertyButton(97, 137, MachineSide.BACK);
    }

    protected void createPropertyButton(int x, int y, MachineSide side){
        MachineSideProperty property = MachineSideProperty.ENABLED;

        if(activeSideButton != -1)
            property = menu.getMachineSideProperties(sideButtons.get(activeSideButton).propertiesIndex).get(side);

        WootSidePropertyButton button = new WootSidePropertyButton(leftPos + x, topPos + y, side, property, machineFacing, btn -> {
            Map<MachineSide, MachineSideProperty> properties = menu.getMachineSideProperties(sideButtons.get(activeSideButton).propertiesIndex);
            properties.put(side, btn.nextProperty());
            menu.setMachineSideProperties();
        });

        button.active = activeSideButton != -1;

        propertyButtons.put(side, button);
        addWidget(button);
    }

    protected abstract void initButtons();

    public void setButtonActive(WootSideButton button){
        if(activeSideButton == button.index){
            activeSideButton = -1;
            button.isViewActive = false;

            for(MachineSide side : MachineSide.values()){
                propertyButtons.get(side).active = false;
            }
        } else {
            if(activeSideButton != -1)
                sideButtons.get(activeSideButton).isViewActive = false;

            activeSideButton = button.index;
            button.isViewActive = true;

            Map<MachineSide, MachineSideProperty> properties = menu.getMachineSideProperties(button.propertiesIndex);
            for(MachineSide side : MachineSide.values()){
                propertyButtons.get(side).active = true;
                propertyButtons.get(side).property = properties.get(side);
            }
        }
    }

    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks){
        for(Slot slot : menu.slots){
            if(slot instanceof WootSlot wootSlot){
                wootSlot.setActive(activeSideButton == -1);
            }
        }

        super.render(gui, mouseX, mouseY, partialTicks);
        super.renderTooltip(gui, mouseX, mouseY);
        renderTooltip(gui, mouseX, mouseY);

        for(WootButton button : buttons){
            button.render(gui, mouseX, mouseY, partialTicks);
        }

        if(activeSideButton != -1){
            renderInventoryHoverBox(gui, 7, 101, 169, 155);
            for(MachineSide side : MachineSide.values()){
                WootSidePropertyButton button = propertyButtons.get(side);
                button.render(gui, mouseX, mouseY, partialTicks);
            }
        }
    }

    private void renderInventoryHoverBox(@NotNull GuiGraphics gui, int minX, int minY, int maxX, int maxY){
        minX += leftPos;
        maxX += leftPos;
        minY += topPos;
        maxY += topPos;
        gui.fill(minX + 1, minY + 1, maxX - 1, maxY - 1, 0xFFDBDBDB);
        gui.fill(minX, minY, maxX - 1, minY + 1, 0xFF363637);
        gui.fill(minX, minY, minX + 1, maxY - 1, 0xFF363637);
        gui.fill(minX + 1, maxY - 1, maxX, maxY, 0xFFFFFFFF);
        gui.fill(maxX - 1, minY + 1, maxX, maxY, 0xFFFFFFFF);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics gui, int mouseX, int mouseY){
        int titleX = 1 + (imageWidth - font.width(title)) / 2;
        gui.drawString(font, title, titleX, 6, 0x404040, false);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics gui, float partialTicks, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.translate(x, y, 0);
        gui.blit(GUI, 0, 0, 0, 0, imageWidth, imageHeight);

        renderMenuBackground(gui);
        renderState(gui);

        pose.popPose();
    }

    protected abstract void renderMenuBackground(@NotNull GuiGraphics gui);
    protected abstract void renderState(@NotNull GuiGraphics gui);

    public static void renderSlot(@NotNull GuiGraphics gui, int x, int y, int color){
        stroke(gui, x - 1, y - 1, x + 18, y + 18, color & 0x2F_FFFFFF);
        gui.blit(GUI, x, y, 228, 0, 18, 18);
        gui.fill(x + 1, y + 1, x + 17, y + 17, color & 0x4F_FFFFFF);
    }

    public static void renderVanillaSlot(@NotNull GuiGraphics gui, int x, int y){
        gui.blit(GUI, x, y, 7, 101, 18, 18);
    }

    public void createSlotSideButton(int x, int y, int color, int propertiesIndex, Component name){
        WootSideButton button = new WootSideButton(sideButtons.size(), leftPos + xSides, topPos + ySides, leftPos + x - 2, topPos + y - 2, 21, 21, color, propertiesIndex, name, this::setButtonActive);
        sideButtons.add(button);
        addRenderableWidget(button);
        xSides += 18;
    }

    public static void renderEnergyBg(@NotNull GuiGraphics gui, int x, int y){
        gui.blit(GUI, x, y, 177, 0, 18, 56);
    }

    public static void renderEnergy(@NotNull GuiGraphics gui, int x, int y, int fill, int capacity){
        int fillHeight = Mth.clamp(fill * 50 / capacity, 0, 50);
        int fillY = 50 - fillHeight;
        gui.blit(GUI, x + 3, y + fillY + 3, 196, 3 + fillY, 12, fillHeight);
    }

    public void renderEnergyTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY, int x, int y, int fill, int capacity) {
        if(isHovering(x, y, 18, 56, mouseX, mouseY)){
            _renderEnergyTooltip(gui, mouseX, mouseY, x, y, fill, capacity, true, true);
        }
    }

    public static void _renderEnergyTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY, int x, int y, int fill, int capacity, boolean skipHover, boolean showCapacity){
        if(skipHover || _isHovering(x, y, 18, 56, mouseX, mouseY)){
            List<Component> tooltip;
            if(showCapacity){
                tooltip = List.of(
                        Component.translatable("info.woot_revived.power").append(Component.literal(":")).setStyle(MACHINE_STYLE),
                        Component.literal(formatInteger(fill))
                                .append(Component.literal("/").setStyle(MACHINE_STYLE))
                                .append(formatInteger(capacity))
                                .append(Component.literal(" FE").setStyle(UNIT_STYLE))
                );
            } else {
                tooltip = List.of(
                        Component.translatable("info.woot_revived.power").append(Component.literal(":")).setStyle(MACHINE_STYLE),
                        Component.literal(formatInteger(fill))
                                .append(Component.literal(" FE").setStyle(UNIT_STYLE))
                );
            }
            gui.renderTooltip(getFont(), tooltip, Optional.empty(), mouseX, mouseY);
        }
    }

    public static void renderFluidBg(@NotNull GuiGraphics gui, int x, int y){
        gui.blit(GUI, x, y, 209, 0, 18, 56);
    }

    public static void renderFluid(@NotNull GuiGraphics gui, int x, int y, FluidStack fluid, int capacity){
        if(fluid == null || fluid.isEmpty())
            return;

        int fillHeight = Mth.clamp(fluid.getAmount() * 50 / capacity, 0, 50);
        int fillY = 50 - fillHeight;

        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluid.getFluid().getFluidType());
        TextureAtlasSprite texture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidTypeExtensions.getStillTexture());
        renderTiledFluidTextureAtlas(gui, texture, x + 3, y + fillY + 3, 12, fillHeight, fluidTypeExtensions.getTintColor(), false);
        gui.blit(GUI, x + 3, y + 3, 212, 3, 12, 50);
    }

    public void createFluidSideButton(int x, int y, int color, int propertiesIndex, Component name){
        WootSideButton button = new WootSideButton(sideButtons.size(),leftPos + xSides, topPos + ySides, leftPos + x - 2, topPos + y - 2, 21, 59, color, propertiesIndex, name, this::setButtonActive);
        sideButtons.add(button);
        addRenderableWidget(button);
        xSides += 18;
    }

    public void renderFluidTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY, int x, int y, FluidStack fluid, int capacity) {
        if(isHovering(x, y, 18, 56, mouseX, mouseY)) {
            _renderFluidTooltip(gui, mouseX, mouseY, x, y, fluid, capacity, true, true);
        }
    }

    public static void _renderFluidTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY, int x, int y, FluidStack fluid, int capacity, boolean skipHover, boolean showCapacity){
        if(skipHover || _isHovering(x, y, 18, 56, mouseX, mouseY)){
            List<Component> tooltip;
            if(showCapacity){
                tooltip = List.of(
                        Component.empty()
                                .append(Component.translatable("info.woot_revived.fluid").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                                .append(fluid != null && !fluid.isEmpty() ? fluid.getDisplayName() : Component.translatable("info.woot_revived.empty")),
                        Component.empty()
                                .append(Component.translatable("info.woot_revived.amount").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                                .append(formatInteger(fluid.getAmount()))
                                .append(Component.literal("/").setStyle(MACHINE_STYLE))
                                .append(formatInteger(capacity))
                                .append(Component.literal("mB").setStyle(UNIT_STYLE))
                );
            } else {
                tooltip = List.of(
                        Component.empty()
                                .append(Component.translatable("info.woot_revived.fluid").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                                .append(fluid != null && !fluid.isEmpty() ? fluid.getDisplayName() : Component.translatable("info.woot_revived.empty")),
                        Component.empty()
                                .append(Component.translatable("info.woot_revived.amount").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                                .append(formatInteger(fluid.getAmount()))
                                .append(Component.literal("mB").setStyle(UNIT_STYLE))
                );
            }
            gui.renderTooltip(getFont(), tooltip, Optional.empty(), mouseX, mouseY);
        }
    }

    public static void renderTiledFluidTextureAtlas(@NotNull GuiGraphics guiGraphics, TextureAtlasSprite sprite, int x, int y, int width, int height, int color, boolean enableBlend) {
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, sprite.atlasLocation());

        if(enableBlend) RenderSystem.enableBlend();
        else RenderSystem.disableBlend();

        int spriteWidth = sprite.contents().width();
        int spriteHeight = sprite.contents().height();

        for(int i = 0; i < width; i += spriteWidth){
            for(int j = 0; j < height; j += spriteHeight){
                int drawWidth = Math.min(width - i, spriteWidth);
                int drawHeight = Math.min(height - j, spriteHeight);
                renderScaledTexturedModelRectFromIcon(guiGraphics, sprite, x + i, y + j, drawWidth, drawHeight, color);
            }
        }

        if(enableBlend) RenderSystem.disableBlend();
    }

    public static void renderScaledTexturedModelRectFromIcon(@NotNull GuiGraphics guiGraphics, TextureAtlasSprite icon, int x, int y, int width, int height, int color) {
        if (icon == null)
            return;

        float minU = icon.getU0();
        float maxU = icon.getU1();
        float minV = icon.getV0();
        float maxV = icon.getV1();

        float red   = (color >> 16 & 0xFF) / 255.0f;
        float green = (color >>  8 & 0xFF) / 255.0f;
        float blue  = (color       & 0xFF) / 255.0f;

        BufferBuilder builder = Tesselator.getInstance().getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        Matrix4f matrix = guiGraphics.pose().last().pose();
        builder.vertex(matrix, x, y + height, 0).uv(minU, minV + (maxV - minV) * height / 16F).color(red, green, blue, 1).endVertex();
        builder.vertex(matrix, x + width, y + height, 0).uv(minU + (maxU - minU) * width / 16F, minV + (maxV - minV) * height / 16F).color(red, green, blue, 1).endVertex();
        builder.vertex(matrix, x + width, y, 0).uv(minU + (maxU - minU) * width / 16F, minV).color(red, green, blue, 1).endVertex();
        builder.vertex(matrix, x, y, 0).uv(minU, minV).color(red, green, blue, 1).endVertex();

        BufferUploader.drawWithShader(builder.end());
    }

    public static void renderProgressArrowBg(@NotNull GuiGraphics gui, int x, int y){
        gui.blit(GUI, x, y, 228, 19, 22, 15);
    }

    public static void renderProgressArrow(@NotNull GuiGraphics gui, int x, int y, int progress){
        int fillWidth = Mth.clamp(progress * 22 / 100, 0, 22);
        gui.blit(GUI, x, y, 228, 35, fillWidth, 16);
    }

    public void renderProgressArrowTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY, int x, int y, int progress, float eta, int usage){
        renderProgressTooltip(gui, mouseX, mouseY, x, y, 22, 16, progress, eta, usage);
    }

    public static void _renderProgressArrowTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY, int x, int y, int progress, float eta, int usage, boolean skipHover){
        _renderProgressTooltip(gui, mouseX, mouseY, x, y, 22, 16, progress, eta, usage, skipHover);
    }

    public void renderProgressTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY, int x, int y, int width, int height, int progress, float eta, int usage) {
        if(isHovering(x, y, width, height, mouseX, mouseY)) {
            _renderProgressTooltip(gui, mouseX, mouseY, x, y, width, height, progress, eta, usage, true);
        }
    }

    public static void _renderProgressTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY, int x, int y, int width, int height, int progress, float eta, int usage, boolean skipHover){
        if(skipHover || _isHovering(x, y, width, height, mouseX, mouseY)){
            List<Component> tooltip = List.of(
                    Component.empty()
                            .append(Component.translatable("info.woot_revived.progress").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                            .append(formatInteger(progress))
                            .append(Component.literal("/").setStyle(MACHINE_STYLE))
                            .append(Component.literal("100")),
                    Component.empty()
                            .append(Component.translatable("info.woot_revived.eta").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                            .append(String.format("%.1f", eta))
                            .append(Component.literal("s").setStyle(UNIT_STYLE)),
                    Component.empty()
                            .append(Component.translatable("info.woot_revived.usage").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                            .append(formatInteger(usage))
                            .append(Component.literal(" FE").setStyle(UNIT_STYLE))
                            .append(Component.literal("/").setStyle(MACHINE_STYLE))
                            .append(Component.literal("t").setStyle(UNIT_STYLE))
            );
            gui.renderTooltip(getFont(), tooltip, Optional.empty(), mouseX, mouseY);
        }
    }

    public static void renderColorBarBg(@NotNull GuiGraphics gui, int x, int y, float[] color){
        gui.blit(GUI, x, y, 177, 57, 56, 11);
        ((GuiGraphicsMixin) gui).woot$innerBlit(GUI, x + 3, x + 53, y + 3, y + 8, 0, 180F / 256F, 230F / 256F, 69F / 256F, 74F / 256F, color[0], color[1], color[2], 1F);
    }

    public static void renderColorBar(@NotNull GuiGraphics gui, int x, int y, int fill, int capacity, float[] color){
        int fillWidth = Mth.clamp(fill * 50 / capacity, 0, 50);
        ((GuiGraphicsMixin) gui).woot$innerBlit(GUI, x + 3, x + 3 + fillWidth, y + 3, y + 8, 0, 180F / 256F, (180F + (float)fillWidth) / 256F, 75F / 256F, 80F / 256F, color[0], color[1], color[2], 1F);
    }

    public void renderColorBarTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY, int x, int y, int fill, int capacity, MutableComponent colorName) {
        if(isHovering(x + 1, y + 1, 54, 9, mouseX, mouseY)) {
            _renderColorBarTooltip(gui, mouseX, mouseY, x, y, fill, capacity, colorName, true, true);
        }
    }

    public static void _renderColorBarTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY, int x, int y, int fill, int capacity, MutableComponent colorName, boolean skipHover, boolean showCapacity){
        if(skipHover || _isHovering(x + 1, y + 1, 54, 9, mouseX, mouseY)){
            List<Component> tooltip;
            if(showCapacity){
                tooltip = List.of(
                        colorName.append(Component.literal(": ")).setStyle(MACHINE_STYLE),
                        Component.literal(formatInteger(fill))
                                .append(Component.literal("/").setStyle(MACHINE_STYLE))
                                .append(formatInteger(capacity))
                                .append(Component.literal("mB").setStyle(UNIT_STYLE))
                );
            } else {
                tooltip = List.of(
                        colorName.append(Component.literal(": ")).setStyle(MACHINE_STYLE),
                        Component.literal(formatInteger(fill))
                                .append(Component.literal("mB").setStyle(UNIT_STYLE))
                );
            }
            gui.renderTooltip(getFont(), tooltip, Optional.empty(), mouseX, mouseY);
        }
    }

    public static void stroke(@NotNull GuiGraphics gui, int minX, int minY, int maxX, int maxY, int color){
        gui.hLine(minX, maxX, minY, color);
        gui.vLine(maxX, minY, maxY, color);
        gui.hLine(maxX, minX, maxY, color);
        gui.vLine(minX, maxY, minY, color);
    }

    public static String formatInteger(int val){
        return String.format(Locale.US, "%,d", val).replace(",", " ");
    }

    public static String formatFloat(float val){
        return String.format(Locale.US, "%,.1f", val).replace(",", " ");
    }

    public static Font getFont(){
        return Minecraft.getInstance().font;
    }
    
    /* JEI */

    public static boolean _isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
        return mouseX >= (double)(x - 1) && mouseX < (double)(x + width + 1) && mouseY >= (double)(y - 1) && mouseY < (double)(y + height + 1);
    }

    public static void renderPlus(@NotNull GuiGraphics gui, int x, int y){
        gui.blit(GUI, x, y, 234, 57, 13, 14);
    }
}

