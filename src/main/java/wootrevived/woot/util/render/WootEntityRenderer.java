package wootrevived.woot.util.render;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import wootrevived.woot.events.client.GlobalClientTicker;
import wootrevived.woot.mixins.accessors.BufferSourceMixinAccessor;

public class WootEntityRenderer {
    public static void render(@NotNull GuiGraphics gui, int x, int y, @NotNull LivingEntity entity, double size, double padding, float max_entity_size){
        EntityRenderer<? super Entity, ?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
        if(renderer == null)
            return;

        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.translate(x, y, 0);

        Matrix4f matrix = pose.last().pose();

        Vector3f poseScale = new Vector3f();
        Vector3f pos = new Vector3f();

        matrix.getScale(poseScale);
        matrix.transformPosition(pos);

        float width = max_entity_size / entity.getBbWidth();
        float height = max_entity_size / entity.getBbHeight();
        float scale = Math.min(width, height);
        scale = Math.min(scale, max_entity_size);

        pose.translate(size / 2 + padding, (size / 2) - padding * 2 + entity.getBbHeight() * scale, 64);
        pose.scale(1F, 1F, -1F);
        pose.mulPose(Axis.YP.rotationDegrees((GlobalClientTicker.tickCounter * 4) % 360));
        pose.mulPose(Axis.ZP.rotationDegrees(180));
        pose.scale(scale, scale, scale);

        Window window = Minecraft.getInstance().getWindow();
        double windowScale = (double) window.getWidth() / (double) window.getGuiScaledWidth();

        RenderSystem.enableScissor(
                (int)((pos.x + padding * poseScale.x) * windowScale),
                (int)(window.getHeight() - (pos.y + (size + padding) * poseScale.y) * windowScale),
                (int)Math.ceil(size * windowScale * poseScale.x),
                (int)Math.ceil(size * windowScale * poseScale.y)
        );
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        BufferSourceMixinAccessor bufferSourceMixin = (BufferSourceMixinAccessor)bufferSource;
        bufferSourceMixin.woot$setActive(true);
        Lighting.setupForFlatItems();

        renderEntity(renderer, entity, pose, bufferSource, LightTexture.pack(15, 15));
        bufferSource.endLastBatch();
        bufferSourceMixin.woot$setActive(false);

        RenderSystem.disableScissor();
        Lighting.setupFor3DItems();

        pose.popPose();
    }

    private static <E extends Entity, S extends EntityRenderState> void renderEntity(EntityRenderer<? super E, S> renderer, E entity, PoseStack pose, MultiBufferSource buffer, int light) {
        renderer.render(renderer.createRenderState(entity, 0F), pose, buffer, light);
    }
}
