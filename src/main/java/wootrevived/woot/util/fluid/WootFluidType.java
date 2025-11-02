package wootrevived.woot.util.fluid;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

public class WootFluidType extends FluidType {
    private final ResourceLocation stillTexture;
    private final ResourceLocation flowingTexture;
    private final ResourceLocation overlayTexture;
    private final int fogColorR;
    private final int fogColorG;
    private final int fogColorB;
    private final Vector4f fogColor;

    public WootFluidType(final ResourceLocation stillTexture, final ResourceLocation flowingTexture, final ResourceLocation overlayTexture, final int fogColorR, int fogColorG, int fogColorB, final Properties properties) {
        super(properties);
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.overlayTexture = overlayTexture;
        this.fogColorR = fogColorR;
        this.fogColorG = fogColorG;
        this.fogColorB = fogColorB;
        this.fogColor = new Vector4f((float)fogColorR / 255f, (float)fogColorG / 255f, (float)fogColorB / 255f, 1.0f);
    }

    public final IClientFluidTypeExtensions EXTENSION = new IClientFluidTypeExtensions() {
        @Override
        public ResourceLocation getStillTexture() {
            return stillTexture;
        }

        @Override
        public ResourceLocation getFlowingTexture() {
            return flowingTexture;
        }

        @Override
        public ResourceLocation getOverlayTexture() {
            return overlayTexture;
        }

        @Override
        public @NotNull Vector4f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector4f fluidFogColor){
            return fogColor;
        }

        @Override
        public void modifyFogRender(Camera camera, @Nullable FogEnvironment environment, float renderDistance, float partialTick, FogData fogData){
            fogData.environmentalStart = 1F;
            fogData.renderDistanceStart = 1F;
            fogData.environmentalEnd = 6F;
            fogData.renderDistanceEnd = 6F;
            fogData.skyEnd = 7.5F;
            fogData.cloudEnd = 5.4F;
        }
    };

    public ResourceLocation getStillTexture() {
        return stillTexture;
    }

    public ResourceLocation getFlowingTexture() {
        return flowingTexture;
    }

    public ResourceLocation getOverlayTexture() {
        return overlayTexture;
    }

    public int getColor(){
        return fogColorR << 16 | fogColorG << 8 | fogColorB;
    }

    public int getTintColor(){
        return getColor();
    }

    public Vector4f getFogColor(){
        return this.fogColor;
    }
}
