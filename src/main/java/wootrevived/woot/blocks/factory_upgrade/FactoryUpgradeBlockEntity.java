package wootrevived.woot.blocks.factory_upgrade;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.interfaces.WootGenerationProperties;
import wootrevived.api.interfaces.WootSpawnProperties;
import wootrevived.woot.client.model.factory_upgrade.FactoryUpgradeBakedModel;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.UpgradeItemsRegistry;
import wootrevived.woot.util.block.FactoryBlockBaseEntity;
import wootrevived.woot.util.entity.WootTags;

public class FactoryUpgradeBlockEntity extends FactoryBlockBaseEntity {
    public FactoryUpgradeBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.FACTORY_UPGRADE_BLOCK_ENTITY.get(), pos, state);
        this.upgradeItem = null;
        this.upgradeStack = ItemStack.EMPTY;
    }

    private WootUpgradeItem upgradeItem;
    private ItemStack upgradeStack;

    public void applyGenerationProperties(WootGenerationProperties properties){
        if(upgradeItem != null) {
            CompoundTag tag = upgradeStack.getOrCreateTag();
            upgradeItem.applyGenerationProperties(properties, tag);
            upgradeStack.setTag(tag);
        }
    }

    public void applySpawnProperties(WootSpawnProperties properties){
        if(upgradeItem != null) {
            CompoundTag tag = upgradeStack.getOrCreateTag();
            upgradeItem.applySpawnProperties(properties, tag);
            upgradeStack.setTag(tag);
        }
    }

    public void modifyDrops(WootDropsProperties properties){
        if(upgradeItem != null) {
            CompoundTag tag = upgradeStack.getOrCreateTag();
            upgradeItem.modifyDrops(properties, tag);
            upgradeStack.setTag(tag);
        }
    }

    public String getUpgradeItemName() {
        return upgradeItem == null ? "" : UpgradeItemsRegistry.getNameFromItem(upgradeItem);
    }

    public ItemStack getUpgradeItemStack() {
        return upgradeStack;
    }

    public void addUpgrade(Level level, Player player, InteractionHand hand, ItemStack stack, WootUpgradeItem newUpgradeItem){
        ItemStack oldStack = upgradeStack;
        if(upgradeItem != null) {
            CompoundTag tag = upgradeStack.getOrCreateTag();
            upgradeItem.deinitItemTag(tag, level, getBlockPos());
            upgradeStack.setTag(tag);
        }

        upgradeItem = newUpgradeItem;
        upgradeStack = stack.copyWithCount(1);
        CompoundTag tag = upgradeStack.getOrCreateTag();
        newUpgradeItem.initItemTag(tag, level, getBlockPos());
        upgradeStack.setTag(tag);
        setChanged();
        player.swing(hand);

        if (!player.isCreative()){
            stack.shrink(1);
            if(!oldStack.isEmpty()){
                if(stack.isEmpty()){
                    player.setItemInHand(hand, oldStack);
                } else {
                    dropItem(level, player.getOnPos().above(), oldStack);
                }
            }
        }
    }

    public void removeUpgrade(Level level, Player player, InteractionHand hand){
        ItemStack oldStack = upgradeStack;
        if(upgradeItem != null) {
            CompoundTag tag = upgradeStack.getOrCreateTag();
            upgradeItem.deinitItemTag(tag, level, getBlockPos());
            upgradeStack.setTag(tag);
        }

        upgradeItem = null;
        upgradeStack = ItemStack.EMPTY;
        setChanged();
        player.swing(hand);

        if(!oldStack.isEmpty()){
            if(player.getItemInHand(hand).isEmpty()){
                player.setItemInHand(hand, oldStack);
            } else {
                dropItem(level, player.getOnPos().above(), oldStack);
            }
        }
    }

    public void dropItem(Level level, BlockPos pos) {
        if (upgradeStack.isEmpty())
            return;

        if(upgradeItem != null) {
            CompoundTag tag = upgradeStack.getOrCreateTag();
            upgradeItem.deinitItemTag(tag, level, getBlockPos());
            upgradeStack.setTag(tag);
        }

        dropItem(level, pos, upgradeStack);
    }

    public void dropItem(Level level, BlockPos pos, ItemStack stack){
        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag){
        super.saveAdditional(tag);
        tag.put(WootTags.Factory.UPGRADE_ITEM, StringTag.valueOf(getUpgradeItemName()));
        tag.put(WootTags.Factory.UPGRADE_ITEM_STACK, upgradeStack.save(new CompoundTag()));
    }

    @Override
    public void load(@NotNull CompoundTag tag){
        super.load(tag);

        if(tag.contains(WootTags.Factory.UPGRADE_ITEM)) {
            String item = tag.getString(WootTags.Factory.UPGRADE_ITEM);
            this.upgradeItem = !item.isEmpty() && UpgradeItemsRegistry.has(item) ? UpgradeItemsRegistry.get(item).get() : null;
        } else {
            this.upgradeItem = null;
        }

        if(this.upgradeItem == null){
            this.upgradeStack = ItemStack.EMPTY;
        } else if(tag.contains(WootTags.Factory.UPGRADE_ITEM_STACK)) {
            this.upgradeStack = ItemStack.of(tag.getCompound(WootTags.Factory.UPGRADE_ITEM_STACK));
        } else {
            this.upgradeStack = this.upgradeItem.getDefaultInstance();
        }
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag(){
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }


    @Override
    public void handleUpdateTag(CompoundTag tag){
        super.handleUpdateTag(tag);
        load(tag);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if(level == null || level.isClientSide) return;
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    @Override
    public @NotNull ModelData getModelData(){
        BlockState state = getBlockState();

        BlockRenderDispatcher renderer = Minecraft.getInstance().getBlockRenderer();
        BakedModel model = renderer.getBlockModel(state);

        return model.getModelData(level, getBlockPos(), getBlockState(), super.getModelData());
    }

    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("UnstableApiUsage")
    public void tryRequestModelDataUpdate(){
        if(level == null || level.getModelDataManager() == null)
            return;

        ModelData data = level.getModelDataManager().getAt(getBlockPos());
        if(data != null && data.has(FactoryUpgradeBakedModel.UPGRADE_PROPERTY)){
            if(upgradeItem == null && !data.get(FactoryUpgradeBakedModel.UPGRADE_PROPERTY).isEmpty()){
                requestModelDataUpdate();
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            } else if(upgradeItem != null && !data.get(FactoryUpgradeBakedModel.UPGRADE_PROPERTY).equalsIgnoreCase(UpgradeItemsRegistry.getNameFromItem(upgradeItem))){
                requestModelDataUpdate();
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
    }
}
