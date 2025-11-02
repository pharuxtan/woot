package wootrevived.woot.util.render.guide;

import guideme.document.LytRect;
import guideme.document.block.LytBlock;
import guideme.document.interaction.GuideTooltip;
import guideme.document.interaction.InteractiveElement;
import guideme.layout.LayoutContext;
import guideme.render.RenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.fluids.FluidStack;
import wootrevived.woot.util.render.WootContainerScreen;

import java.util.Optional;

public class LytFluid extends LytBlock implements InteractiveElement {
    private final FluidStack stack;
    private final int capacity;

    public LytFluid(FluidStack stack, int capacity) {
        this.stack = stack;
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

        WootContainerScreen.renderFluidBg(gui, x, y);
        WootContainerScreen.renderFluid(gui, x, y, stack, capacity);
    }

    @Override
    public Optional<GuideTooltip> getTooltip(float x, float y) {
        if(stack.isEmpty())
            return Optional.empty();

        return Optional.of(new FluidTooltip(stack));
    }
}
