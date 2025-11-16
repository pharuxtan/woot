package wootrevived.woot.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.state.pip.GuiEntityRenderState;
import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import wootrevived.woot.events.client.GlobalClientTicker;
import wootrevived.woot.mixins.impl.GuiGraphicsMixin;

public class WootEntityRenderer {
    public static void render(@NotNull GuiGraphics gui, int x, int y, @NotNull LivingEntity entity, double size, double padding, float max_entity_size){
        EntityRenderer<? super Entity, ?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
        if(renderer == null)
            return;

        Matrix3x2fStack pose = gui.pose();

        float width = max_entity_size / entity.getBbWidth();
        float height = max_entity_size / entity.getBbHeight();
        float scale = Math.min(Math.min(width, height), max_entity_size);

        Vector3f translation = new Vector3f(0, (float)((entity.getBoundingBox().minY + entity.getBoundingBox().maxY) / 2 + 0.05), 0);

        float rotationAngle = (GlobalClientTicker.tickCounter * 4) % 360;
        Quaternionf rotation = new Quaternionf()
                .rotationY((float) Math.toRadians(rotationAngle))
                .rotateZ((float) Math.toRadians(180));

        int x0 = (int)(x + padding);
        int y0 = (int)(y + padding);
        int x1 = (int)(x0 + size);
        int y1 = (int)(y0 + size);

        ScreenRectangle bounds = PictureInPictureRenderState.getBounds(x0, y0, x1, y1, null)
                .transformMaxBounds(new Matrix3x2f(pose));

        GuiEntityRenderState renderState = new GuiEntityRenderState(
                renderer.createRenderState(entity, 0F),
                translation,
                rotation,
                null,
                bounds.left(), bounds.top(), bounds.right(), bounds.bottom(),
                scale,
                gui.peekScissorStack(),
                bounds
        );

        ((GuiGraphicsMixin) gui).woot$getGuiRenderState().submitPicturesInPictureState(renderState);
    }
}
