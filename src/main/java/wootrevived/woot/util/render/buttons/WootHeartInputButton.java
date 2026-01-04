package wootrevived.woot.util.render.buttons;

import net.minecraft.client.gui.GuiGraphics;
import wootrevived.woot.util.render.WootButton;

public class WootHeartInputButton extends WootButton {
    protected final OnPress onPress;
    public final int index;
    public boolean isViewActive = false;

    public WootHeartInputButton(int index, int x, int y, OnPress onPress) {
        super(x, y, 38, 38);
        this.onPress = onPress;
        this.index = index;
    }

    @Override
    protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        if(isViewActive || (active && isHovered())) {
            gui.fill(getX() + 3, getY() + 3, getX() + getWidth() - 3, getY() + getHeight() - 3, 0x80FFFFFF);
        }
    }

    public int getFakeSpawnerIndex(){
        return index;
    }

    @Override
    public void onPress() {
        this.onPress.onPress(this);
    }

        public interface OnPress {
        void onPress(WootHeartInputButton button);
    }
}
