package wootrevived.woot.client.render.stygian_anvil;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.blocks.stygian_anvil.StygianAnvilBlockEntity;

public class StygianAnvilBlockEntityRenderer implements BlockEntityRenderer<StygianAnvilBlockEntity> {
    @Override
    public void render(StygianAnvilBlockEntity stygianAnvilBlockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay, Vec3 vec3){
        Direction facing = stygianAnvilBlockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

        IItemHandler itemHandler = stygianAnvilBlockEntity.getInventory();

        ItemStack itemStack = itemHandler.getStackInSlot(StygianAnvilBlockEntity.BASE_SLOT);
        if (!itemStack.isEmpty()) {
            renderStack(facing, itemStack, poseStack, buffer, 0.5F, 1F, 0.5F, packedLight, packedOverlay, stygianAnvilBlockEntity.getLevel());
        }

        float addX = switch(facing){
            case NORTH -> 0F;
            case SOUTH -> 0F;
            case WEST  -> -0.2F;
            case EAST  -> 0.2F;
            default    -> 0f;
        };

        float addZ = switch(facing){
            case NORTH -> -0.2F;
            case SOUTH -> 0.2F;
            case WEST  -> 0F;
            case EAST  -> 0F;
            default    -> 0f;
        };

        ItemStack ingredient_1 = itemHandler.getStackInSlot(StygianAnvilBlockEntity.INGREDIENT_1_SLOT);
        if (!ingredient_1.isEmpty())
            renderStack(facing, ingredient_1, poseStack, buffer, 0.5F - addX, 1F, 0.5F - addZ, packedLight, packedOverlay, stygianAnvilBlockEntity.getLevel());

        ItemStack ingredient_2 = itemHandler.getStackInSlot(StygianAnvilBlockEntity.INGREDIENT_2_SLOT);
        if (!ingredient_2.isEmpty())
            renderStack(facing, ingredient_2, poseStack, buffer, 0.5F + addX, 1F, 0.5F + addZ, packedLight, packedOverlay, stygianAnvilBlockEntity.getLevel());

        ItemStack ingredient_3 = itemHandler.getStackInSlot(StygianAnvilBlockEntity.INGREDIENT_3_SLOT);
        if (!ingredient_3.isEmpty())
            renderStack(facing, ingredient_3, poseStack, buffer, 0.5F - addX * 2F, 1F, 0.5F - addZ * 2F, packedLight, packedOverlay, stygianAnvilBlockEntity.getLevel());

        ItemStack ingredient_4 = itemHandler.getStackInSlot(StygianAnvilBlockEntity.INGREDIENT_4_SLOT);
        if (!ingredient_4.isEmpty())
            renderStack(facing, ingredient_4, poseStack, buffer, 0.5F + addX * 2F, 1F, 0.5F + addZ * 2F, packedLight, packedOverlay, stygianAnvilBlockEntity.getLevel());
    }

    private void renderStack(Direction facing, ItemStack itemStack, PoseStack poseStack, MultiBufferSource buffer, double x, double y, double z, int combinedLight, int combinedOverlay, Level level) {
        float scale = 0.20F;

        poseStack.pushPose();
        if(itemStack.getItem() instanceof BlockItem){
            poseStack.translate(x, y + 0.05F, z);
        } else {
            poseStack.translate(x, y + 0.01F, z);
        }
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(Axis.YP.rotationDegrees(switch(facing){
            case NORTH -> -90f;
            case SOUTH -> 90f;
            case WEST  -> 0f;
            case EAST  -> 180f;
            default    -> 0f;
        }));
        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, buffer, level, 0);
        poseStack.popPose();
    }
}
