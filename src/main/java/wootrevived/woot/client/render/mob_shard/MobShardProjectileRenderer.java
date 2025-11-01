package wootrevived.woot.client.render.mob_shard;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import wootrevived.woot.items.mob_shard.MobShardProjectile;

@OnlyIn(Dist.CLIENT)
public class MobShardProjectileRenderer extends EntityRenderer<MobShardProjectile, MobShardProjectileRenderState> {
    private final ItemModelResolver itemModelResolver;
    private final float scale;

    public MobShardProjectileRenderer(EntityRendererProvider.Context context, float scale){
        super(context);
        this.itemModelResolver = context.getItemModelResolver();
        this.scale = scale;
    }

    public MobShardProjectileRenderer(EntityRendererProvider.Context context) { this(context, 1f); }

    @Override
    public MobShardProjectileRenderState createRenderState() {
        return new MobShardProjectileRenderState();
    }

    @Override
    public void extractRenderState(MobShardProjectile entity, MobShardProjectileRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        this.itemModelResolver.updateForNonLiving(state.item, entity.getItem(), ItemDisplayContext.GROUND, entity);
        state.xRot = entity.getXRot(partialTick);
        state.yRot = entity.getYRot(partialTick);
        state.motion = entity.getDeltaMovement();
        state.tickCount = entity.tickCount;
        state.partialTick = partialTick;
    }

    @Override
    public void render(MobShardProjectileRenderState state, PoseStack pose, MultiBufferSource buffers, int packedLight) {
        pose.pushPose();
        pose.scale(this.scale, this.scale, this.scale);

        pose.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
        pose.mulPose(Axis.ZP.rotationDegrees(state.xRot));

        float pull = (float) state.motion.length() / 2f;
        float rollAngle = (state.tickCount + state.partialTick) * (20.0f + pull * 25.0f);
        pose.mulPose(Axis.XP.rotationDegrees(rollAngle));

        pose.translate(0, 0.1, 0);
        pose.mulPose(Axis.ZP.rotationDegrees(225.0f));

        state.item.render(pose, buffers, packedLight, OverlayTexture.NO_OVERLAY);
        pose.popPose();
        super.render(state, pose, buffers, packedLight);
    }
}
