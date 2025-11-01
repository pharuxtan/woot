package wootrevived.woot.util.render.buttons;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import wootrevived.woot.util.render.WootButton;
import wootrevived.woot.util.render.WootContainerScreen;

public class WootSideButton extends WootButton {
    protected final OnPress onPress;

    protected final int elemX;
    protected final int elemY;
    protected final int elemWidth;
    protected final int elemHeight;
    protected final int color;
    protected final Component name;
    public final int index;
    public final int propertiesIndex;
    public boolean isViewActive = false;

    public WootSideButton(int index, int x, int y, int elemX, int elemY, int elemWidth, int elemHeight, int color, int propertiesIndex, Component name, OnPress onPress) {
        super(x, y, 14, 14);
        this.onPress = onPress;
        this.index = index;
        this.elemX = elemX;
        this.elemY = elemY;
        this.elemWidth = elemWidth;
        this.elemHeight = elemHeight;
        this.color = color;
        this.name = name;
        this.propertiesIndex = propertiesIndex;
    }

    @Override
    protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        gui.blit(RenderType::guiTextured, WootContainerScreen.GUI, getX(), getY(), 200, 177, getWidth(), getHeight(), WootContainerScreen.ATLAS_WIDTH, WootContainerScreen.ATLAS_HEIGHT);
        gui.fill(getX() + 2, getY() + 2,  getX() + getWidth() - 2, getY() + getHeight() - 2, color);
        if(isHovered()) {
            gui.fill(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0x80FFFFFF);
            gui.renderTooltip(WootContainerScreen.getMCFont(), name, mouseX, mouseY);
        }

        if(isHovered() || isViewActive) {
            WootContainerScreen.stroke(gui, elemX, elemY, elemX + elemWidth, elemY + elemHeight, color);
        }
    }

    @Override
    public void onPress() {
        this.onPress.onPress(this);
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnPress {
        void onPress(WootSideButton button);
    }
}
