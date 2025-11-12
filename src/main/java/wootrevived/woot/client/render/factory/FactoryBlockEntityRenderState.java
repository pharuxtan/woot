package wootrevived.woot.client.render.factory;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import org.jetbrains.annotations.NotNull;

public class FactoryBlockEntityRenderState extends BlockEntityRenderState {
    public @NotNull Runnable requestModelUpdate = () -> {};
}
