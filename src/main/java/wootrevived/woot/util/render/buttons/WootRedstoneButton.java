package wootrevived.woot.util.render.buttons;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import wootrevived.woot.util.common.RedstoneMode;
import wootrevived.woot.util.render.WootButton;
import wootrevived.woot.util.render.WootContainerScreen;

public class WootRedstoneButton extends WootButton {
    protected final OnPress onPress;
    protected RedstoneMode mode;

    public WootRedstoneButton(int x, int y, RedstoneMode mode, OnPress onPress) {
        super(x, y, 14, 14);
        this.onPress = onPress;
        this.mode = mode;
    }

    @Override
    protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        if(active){
            int uOffset = mode == RedstoneMode.ALWAYS_ON ? 215 : mode == RedstoneMode.WITH_NO_SIGNAL ? 230 : mode == RedstoneMode.WITH_SIGNAL ? 215 : 230;
            int vOffset = mode == RedstoneMode.ALWAYS_ON ? 132 : mode == RedstoneMode.WITH_NO_SIGNAL ? 132 : mode == RedstoneMode.WITH_SIGNAL ? 147 : 147;
            gui.blit(RenderPipelines.GUI_TEXTURED, WootContainerScreen.GUI, getX(), getY(), uOffset, vOffset, getWidth(), getHeight(), WootContainerScreen.ATLAS_WIDTH, WootContainerScreen.ATLAS_HEIGHT);
            if(isHovered()) {
                gui.fill(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0x80FFFFFF);
                gui.setTooltipForNextFrame(WootContainerScreen.getMCFont(), mode.getComponent(), mouseX, mouseY);
            }
        }
    }

    public RedstoneMode nextMode(){
        mode = mode.getNext();
        return mode;
    }

    public void setMode(RedstoneMode mode){
        this.mode = mode;
    }

    @Override
    public void onPress() {
        onPress.onPress(this);
    }

        public interface OnPress {
        void onPress(WootRedstoneButton button);
    }
}
