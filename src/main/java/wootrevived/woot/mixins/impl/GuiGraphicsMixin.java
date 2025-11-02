package wootrevived.woot.mixins.impl;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiGraphics.class)
public interface GuiGraphicsMixin {
    @Invoker("blitTiledSprite")
    void woot$blitTiledSprite(RenderPipeline renderTypeGetter, TextureAtlasSprite sprite, int x, int y, int width, int height, int uPosition, int vPosition, int spriteWidth, int spriteHeight, int nineSliceWidth, int nineSliceHeight, int blitOffset);

    @Invoker("innerBlit")
    void woot$innerBlit(RenderPipeline renderTypeGetter, ResourceLocation atlasLocation, int x1, int x2, int y1, int y2, float minU, float maxU, float minV, float maxV, int color);

    @Accessor("guiRenderState")
    GuiRenderState woot$getGuiRenderState();
}
