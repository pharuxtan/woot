package wootrevived.woot.mixins.impl;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wootrevived.woot.mixins.accessors.BufferSourceMixinAccessor;

@OnlyIn(Dist.CLIENT)
@Mixin(MultiBufferSource.BufferSource.class)
public abstract class BufferSourceMixin implements BufferSourceMixinAccessor {
    @Unique
    private GpuTexture woot$sampler1;

    @Unique
    private GpuTexture woot$sampler2;

    @Unique
    private boolean woot$isActive = false;

    public void woot$setActive(boolean active){
        woot$isActive = active;
    }

    @Inject(
            method = "endBatch(Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/vertex/BufferBuilder;)V",
            at = @At("HEAD")
    )
    private void woot$endBatchHead(RenderType renderType, BufferBuilder builder, CallbackInfo ci){
        if(woot$isActive) {
            GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;
            RenderPipeline pipeline = renderType.getRenderPipeline();

            woot$sampler1 = RenderSystem.getShaderTexture(1);
            woot$sampler2 = RenderSystem.getShaderTexture(2);

            if(pipeline.getSamplers().contains("Sampler1"))
                gameRenderer.overlayTexture().setupOverlayColor();
            else
                RenderSystem.setShaderTexture(1, null);

            if(pipeline.getSamplers().contains("Sampler2"))
                gameRenderer.lightTexture().turnOnLightLayer();
            else
                RenderSystem.setShaderTexture(2, null);
        }
    }

    @Inject(
            method = "endBatch(Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/vertex/BufferBuilder;)V",
            at = @At("RETURN")
    )
    private void woot$endBatchReturn(RenderType renderType, BufferBuilder builder, CallbackInfo ci){
        if(woot$isActive) {
            RenderSystem.setShaderTexture(1, woot$sampler1);
            RenderSystem.setShaderTexture(2, woot$sampler2);

            woot$sampler1 = null;
            woot$sampler2 = null;
        }
    }
}
