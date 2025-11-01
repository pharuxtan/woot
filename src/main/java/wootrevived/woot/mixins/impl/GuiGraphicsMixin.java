package wootrevived.woot.mixins.impl;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Function;

@Mixin(GuiGraphics.class)
public interface GuiGraphicsMixin {
    @Invoker("blitTiledSprite")
    void woot$blitTiledSprite(Function<ResourceLocation, RenderType> renderTypeGetter, TextureAtlasSprite sprite, int x, int y, int width, int height, int uPosition, int vPosition, int spriteWidth, int spriteHeight, int nineSliceWidth, int nineSliceHeight, int blitOffset);

    @Invoker("innerBlit")
    void woot$innerBlit(Function<ResourceLocation, RenderType> renderTypeGetter, ResourceLocation atlasLocation, int x1, int x2, int y1, int y2, float minU, float maxU, float minV, float maxV, int color);
}
