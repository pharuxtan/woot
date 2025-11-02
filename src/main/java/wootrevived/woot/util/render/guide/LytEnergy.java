package wootrevived.woot.util.render.guide;

import guideme.document.LytRect;
import guideme.document.block.LytBlock;
import guideme.document.interaction.GuideTooltip;
import guideme.document.interaction.InteractiveElement;
import guideme.layout.LayoutContext;
import guideme.render.RenderContext;
import net.minecraft.client.gui.GuiGraphics;
import wootrevived.woot.util.render.WootContainerScreen;

import java.util.Optional;

public class LytEnergy extends LytBlock implements InteractiveElement {
    private final int amount;
    private final int capacity;

    public LytEnergy(int amount, int capacity) {
        this.amount = amount;
        this.capacity = capacity;
    }

    @Override
    protected LytRect computeLayout(LayoutContext context, int x, int y, int availableWidth) {
        return new LytRect(x, y, 18, 56);
    }

    @Override
    protected void onLayoutMoved(int deltaX, int deltaY) {
    }

    @Override
    public void render(RenderContext context) {
        int x = bounds.x();
        int y = bounds.y();

        GuiGraphics gui = context.guiGraphics();

        WootContainerScreen.renderEnergyBg(gui, x, y);
        WootContainerScreen.renderEnergy(gui, x, y, amount, capacity);
    }

    @Override
    public Optional<GuideTooltip> getTooltip(float x, float y) {
        if(amount <= 0)
            return Optional.empty();

        return Optional.of(new EnergyTooltip(amount));
    }
}
