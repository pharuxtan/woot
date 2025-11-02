package wootrevived.woot.client.render.factory;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.RenderTypeHelper;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.blocks.factory_upgrade.FactoryUpgradeBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class FactoryBlockEntityRenderer implements BlockEntityRenderer<BlockEntity> {
    @Override
    public void render(BlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffers, int packedLight, int packedOverlay, Vec3 vec3) {
        BlockState state = blockEntity.getBlockState();

        if (state.getRenderShape() == RenderShape.MODEL) {
            if (blockEntity instanceof FactoryUpgradeBlockEntity factoryUpgradeBlockEntity)
                factoryUpgradeBlockEntity.tryRequestModelDataUpdate();
            return;
        }

        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();

        boolean isNonAttached = state.hasProperty(BlockStateProperties.ATTACHED) && !state.getValue(BlockStateProperties.ATTACHED);
        boolean isDisabled = state.hasProperty(BlockStateProperties.ENABLED) && !state.getValue(BlockStateProperties.ENABLED);

        float scale;
        if (isDisabled) {
            scale = 0.5f;
        } else if (isNonAttached) {
            scale = 0.75f;
        } else { // Should never happen
            scale = 1.0f;
        }

        poseStack.pushPose();

        poseStack.translate((1.0f - scale) / 2f, (1.0f - scale) / 2f, (1.0f - scale) / 2f);
        poseStack.scale(scale, scale, scale);

        BlockAndTintGetter level = blockEntity.getLevel();
        BlockPos pos = blockEntity.getBlockPos();

        BlockStateModel model = blockRenderer.getBlockModel(state);
        List<BlockModelPart> parts = new ArrayList<>();

        model.collectParts(level, pos, state, RandomSource.create(state.getSeed(pos)), parts);
        blockRenderer.renderBatched(state, pos, level, poseStack, layer -> buffers.getBuffer(RenderTypeHelper.getMovingBlockRenderType(layer)), false, parts);

        poseStack.popPose();
    }
}
