package wootrevived.woot.client.render.stygian_anvil;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import wootrevived.woot.blocks.stygian_anvil.StygianAnvilBlockEntity;

public class StygianAnvilBlockEntityRenderer implements BlockEntityRenderer<StygianAnvilBlockEntity, StygianAnvilBlockEntityRenderState> {
    private final ItemModelResolver itemModelResolver;

    public StygianAnvilBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public void submit(StygianAnvilBlockEntityRenderState state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        float addX = switch(state.facing){
            case NORTH -> 0F;
            case SOUTH -> 0F;
            case WEST  -> -0.2F;
            case EAST  -> 0.2F;
            default    -> 0f;
        };

        float addZ = switch(state.facing){
            case NORTH -> -0.2F;
            case SOUTH -> 0.2F;
            case WEST  -> 0F;
            case EAST  -> 0F;
            default    -> 0f;
        };

        submitStack(state, state.base, state.isBaseABlock, pose, collector, 0.5F, 0.5F);
        submitStack(state, state.firstComplementary, state.isFirstComplementaryABlock, pose, collector, 0.5F - addX, 0.5F - addZ);
        submitStack(state, state.secondComplementary, state.isSecondComplementaryABlock, pose, collector, 0.5F + addX, 0.5F + addZ);
        submitStack(state, state.thirdComplementary, state.isThirdComplementaryABlock, pose, collector, 0.5F - addX * 2F, 0.5F - addZ * 2F);
        submitStack(state, state.fourthComplementary, state.isFourthComplementaryABlock, pose, collector, 0.5F + addX * 2F, 0.5F + addZ * 2F);
    }

    private void submitStack(StygianAnvilBlockEntityRenderState state, ItemStackRenderState item, boolean isABlock, PoseStack pose, SubmitNodeCollector collector, double x, double z) {
        float scale = 0.20F;

        pose.pushPose();
        pose.translate(x, isABlock ? 1.05F : 1.01F, z);
        pose.scale(scale, scale, scale);
        pose.mulPose(Axis.YP.rotationDegrees(switch(state.facing){
            case NORTH -> -90f;
            case SOUTH -> 90f;
            case WEST  -> 0f;
            case EAST  -> 180f;
            default    -> 0f;
        }));
        pose.mulPose(Axis.XP.rotationDegrees(90));

        item.submit(pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        pose.popPose();
    }

    @Override
    public StygianAnvilBlockEntityRenderState createRenderState() {
        return new StygianAnvilBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(StygianAnvilBlockEntity blockEntity, StygianAnvilBlockEntityRenderState renderState, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);

        renderState.facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

        ItemStack item = blockEntity.inventoryHandler.getStackInSlot(StygianAnvilBlockEntity.BASE_SLOT);
        itemModelResolver.updateForTopItem(renderState.base, item, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        renderState.isBaseABlock = item.getItem() instanceof BlockItem;

        item = blockEntity.inventoryHandler.getStackInSlot(StygianAnvilBlockEntity.FIRST_COMPLEMENTARY_SLOT);
        itemModelResolver.updateForTopItem(renderState.firstComplementary, item, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        renderState.isFirstComplementaryABlock = item.getItem() instanceof BlockItem;

        item = blockEntity.inventoryHandler.getStackInSlot(StygianAnvilBlockEntity.SECOND_COMPLEMENTARY_SLOT);
        itemModelResolver.updateForTopItem(renderState.secondComplementary, item, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        renderState.isSecondComplementaryABlock = item.getItem() instanceof BlockItem;

        item = blockEntity.inventoryHandler.getStackInSlot(StygianAnvilBlockEntity.THIRD_COMPLEMENTARY_SLOT);
        itemModelResolver.updateForTopItem(renderState.thirdComplementary, item, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        renderState.isThirdComplementaryABlock = item.getItem() instanceof BlockItem;

        item = blockEntity.inventoryHandler.getStackInSlot(StygianAnvilBlockEntity.FOURTH_COMPLEMENTARY_SLOT);
        itemModelResolver.updateForTopItem(renderState.fourthComplementary, item, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        renderState.isFourthComplementaryABlock = item.getItem() instanceof BlockItem;
    }
}
