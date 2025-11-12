package wootrevived.woot.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public abstract class WootButton extends AbstractWidget {
    public WootButton(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
    }

    @Override
    protected abstract void renderWidget(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTick);

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {}

    public abstract void onPress();

    @Override
    public void onClick(MouseButtonEvent event, boolean isDoubleClick) {
        if (this.active && this.visible && event.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT &&
                event.x() >= this.getX() && event.y() >= this.getY() &&
                event.x() < this.getX() + this.width && event.y() < this.getY() + this.height) {
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            this.onPress();
        }
    }
}
