package wootrevived.woot.util.render.guide;

import guideme.document.LytRect;
import guideme.document.block.LytBlock;
import guideme.document.interaction.GuideTooltip;
import guideme.document.interaction.InteractiveElement;
import guideme.layout.LayoutContext;
import guideme.render.RenderContext;
import guideme.scene.level.GuidebookLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import wootrevived.api.WootFactoryMob;
import wootrevived.woot.util.helper.SerializeEntityNBTHelper;
import wootrevived.woot.util.render.WootEntityRenderer;

import java.util.Optional;

public class LytEntity extends LytBlock implements InteractiveElement {
    private static final double BOX_SIZE = 36D;
    private static final double BOX_PADDING = 3D;

    private static final float MAX_ENTITY_BOX_SIZE = 20F;

    private final WootFactoryMob<?> mob;
    private final LivingEntity entity;
    private final GuidebookLevel level;

    public LytEntity(WootFactoryMob<?> mob, LivingEntity entity, GuidebookLevel level) {
        this.mob = mob;
        this.entity = entity;
        this.level = level;
    }

    @Override
    protected LytRect computeLayout(LayoutContext context, int x, int y, int availableWidth) {
        return new LytRect(x, y, 42, 42);
    }

    @Override
    protected void onLayoutMoved(int deltaX, int deltaY) {
    }

    @Override
    public void renderBatch(RenderContext renderContext, MultiBufferSource multiBufferSource) {
    }

    @Override
    public void render(RenderContext renderContext) {
        renderContext.renderPanel(bounds);

        WootEntityRenderer.render(renderContext.guiGraphics(), bounds.x(), bounds.y(), entity, BOX_SIZE, BOX_PADDING, MAX_ENTITY_BOX_SIZE);
    }

    @Override
    public Optional<GuideTooltip> getTooltip(float x, float y) {
        return Optional.of(new EntityTooltip(mob, SerializeEntityNBTHelper.serialize(entity), level));
    }
}
