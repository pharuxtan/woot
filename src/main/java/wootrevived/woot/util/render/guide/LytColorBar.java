package wootrevived.woot.util.render.guide;

import guideme.document.LytRect;
import guideme.document.block.LytBlock;
import guideme.document.interaction.GuideTooltip;
import guideme.document.interaction.InteractiveElement;
import guideme.layout.LayoutContext;
import guideme.render.RenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;
import wootrevived.woot.util.render.WootContainerScreen;

import java.util.Optional;

public class LytColorBar extends LytBlock implements InteractiveElement {
    private final int amount;
    private final int capacity;
    private final int color;
    private final MutableComponent colorName;

    public LytColorBar(int amount, int capacity, int color, MutableComponent colorName) {
        this.amount = amount;
        this.capacity = capacity;
        this.color = color;
        this.colorName = colorName;
    }

    @Override
    protected LytRect computeLayout(LayoutContext context, int x, int y, int availableWidth) {
        return new LytRect(x, y, 56, 11);
    }

    @Override
    protected void onLayoutMoved(int deltaX, int deltaY) {
    }

    @Override
    public void render(RenderContext context) {
        int x = bounds.x();
        int y = bounds.y();

        GuiGraphics gui = context.guiGraphics();

        WootContainerScreen.renderColorBarBg(gui, x, y, color);
        WootContainerScreen.renderColorBar(gui, x, y, amount, capacity, color);
    }

    @Override
    public Optional<GuideTooltip> getTooltip(float x, float y) {
        if(amount <= 0)
            return Optional.empty();

        return Optional.of(new ColorBarTooltip(amount, colorName));
    }
}
