package wootrevived.woot.client.render.factory;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import wootrevived.woot.blocks.factory_upgrade.FactoryUpgradeBlockEntity;

public class FactoryBlockEntityRenderer implements BlockEntityRenderer<BlockEntity, FactoryBlockEntityRenderState> {
    @Override
    public void submit(FactoryBlockEntityRenderState state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.blockState.getRenderShape() == RenderShape.MODEL) {
            state.requestModelUpdate.run();
            return;
        }

        boolean isNonAttached = state.blockState.hasProperty(BlockStateProperties.ATTACHED) && !state.blockState.getValue(BlockStateProperties.ATTACHED);
        boolean isDisabled = state.blockState.hasProperty(BlockStateProperties.ENABLED) && !state.blockState.getValue(BlockStateProperties.ENABLED);

        float scale;
        if (isDisabled) {
            scale = 0.5f;
        } else if (isNonAttached) {
            scale = 0.75f;
        } else { // Should never happen
            scale = 1.0f;
        }

        pose.pushPose();

        pose.translate((1.0f - scale) / 2f, (1.0f - scale) / 2f, (1.0f - scale) / 2f);
        pose.scale(scale, scale, scale);

        collector.submitMovingBlock(pose, state.movingBlock);

        pose.popPose();
    }

    @Override
    public FactoryBlockEntityRenderState createRenderState() {
        return new FactoryBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(BlockEntity blockEntity, FactoryBlockEntityRenderState renderState, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.movingBlock.level = blockEntity.getLevel();
        renderState.movingBlock.blockPos = blockEntity.getBlockPos();
        renderState.movingBlock.blockState = blockEntity.getBlockState();
        if(blockEntity instanceof FactoryUpgradeBlockEntity factoryUpgradeBlockEntity)
            renderState.requestModelUpdate = factoryUpgradeBlockEntity::tryRequestModelDataUpdate;
    }
}
