package wootrevived.woot.util.render.buttons;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import wootrevived.woot.util.common.MachineSide;
import wootrevived.woot.util.common.MachineSideProperty;
import wootrevived.woot.util.render.WootButton;
import wootrevived.woot.util.render.WootContainerScreen;

import java.util.List;
import java.util.Optional;

import static wootrevived.woot.util.render.WootStyles.MACHINE_STYLE;

public class WootSidePropertyButton extends WootButton {
    protected final OnPress onPress;

    public final MachineSide side;
    public final Direction facing;
    public MachineSideProperty property;

    public WootSidePropertyButton(int x, int y, MachineSide side, MachineSideProperty property, Direction facing, OnPress onPress) {
        super(x, y, 14, 14);
        this.onPress = onPress;
        this.side = side;
        this.property = property;
        this.facing = facing;
    }

    @Override
    protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        int uOffset = property == MachineSideProperty.ENABLED ? 215 : property == MachineSideProperty.DISABLED ? 230 : property == MachineSideProperty.PULL ? 215 : 230;
        int vOffset = property == MachineSideProperty.ENABLED ? 162 : property == MachineSideProperty.DISABLED ? 162 : property == MachineSideProperty.PULL ? 177 : 177;
        gui.blit(RenderType::guiTextured, WootContainerScreen.GUI, getX(), getY(), uOffset, vOffset, getWidth(), getHeight(), WootContainerScreen.ATLAS_WIDTH, WootContainerScreen.ATLAS_HEIGHT);
        if(isHovered()) {
            gui.fill(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, 0x80FFFFFF);
            List<Component> tooltip = List.of(
                    Component.empty()
                            .append(Component.translatable("info.woot_revived.direction").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                            .append(side.getComponent())
                            .append(" ")
                            .append(side.getDirectionComponent(facing)),
                    Component.empty()
                            .append(Component.translatable("info.woot_revived.action").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                            .append(property.getComponent())
            );
            gui.renderTooltip(WootContainerScreen.getMCFont(), tooltip, Optional.empty(), mouseX, mouseY);
        }
    }

    public MachineSideProperty nextProperty(){
        property = property.getNext();
        return property;
    }

    @Override
    public void onPress() {
        this.onPress.onPress(this);
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnPress {
        void onPress(WootSidePropertyButton button);
    }
}
