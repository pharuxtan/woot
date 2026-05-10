package wootrevived.woot.blocks.factory_upgrade;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.interfaces.WootGenerationProperties;
import wootrevived.api.interfaces.WootSpawnProperties;
import wootrevived.woot.Woot;
import wootrevived.woot.data.FactoryUpgradeData;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.UpgradeItemsRegistry;
import wootrevived.woot.util.block.FactoryBlockBaseEntity;

import java.util.Optional;

public class FactoryUpgradeBlockEntity extends FactoryBlockBaseEntity {
    public FactoryUpgradeBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.FACTORY_UPGRADE_BLOCK_ENTITY.get(), pos, state);
        this.upgradeItem = null;
        this.upgradeStack = ItemStack.EMPTY;
    }

    private WootUpgradeItem<?> upgradeItem;
    private ItemStack upgradeStack;

    public void applyGenerationProperties(WootGenerationProperties properties){
        if(upgradeItem != null) {
            upgradeItem.applyGenerationProperties(properties, upgradeStack);
            setChanged();
        }
    }

    public void applySpawnProperties(WootSpawnProperties properties){
        if(upgradeItem != null) {
            upgradeItem.applySpawnProperties(properties, upgradeStack);
            setChanged();
        }
    }

    public void modifyDrops(WootDropsProperties properties){
        if(upgradeItem != null) {
            upgradeItem.modifyDrops(properties, upgradeStack);
            setChanged();
        }
    }

    public String getUpgradeItemName() {
        return upgradeItem == null ? "" : UpgradeItemsRegistry.getNameFromItem(upgradeItem);
    }

    public WootUpgradeItem<?> getUpgradeItem() {
        return upgradeItem;
    }

    public ItemStack getUpgradeItemStack() {
        return upgradeStack;
    }

    public InteractionResult interactUpgrade(ItemStack stack, Level level, Player player, InteractionHand hand, BlockHitResult hit){
        if(upgradeItem == null)
            return InteractionResult.PASS;

        InteractionResult result = upgradeItem.interact(upgradeStack, stack, level, player, hand, hit);
        setChanged();
        return result;
    }

    public void addUpgrade(Level level, Player player, InteractionHand hand, ItemStack stack, WootUpgradeItem<?> newUpgradeItem){
        ItemStack oldStack = upgradeStack;
        if(upgradeItem != null)
            upgradeItem.deinitDataComponents(oldStack, level, getBlockPos());

        upgradeItem = newUpgradeItem;
        upgradeStack = stack.copyWithCount(1);
        newUpgradeItem.initDataComponents(upgradeStack, level, getBlockPos());
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
        if(upgradeItem != null)
            upgradeItem.deinitDataComponents(oldStack, level, getBlockPos());

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

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state){
        dropItem(level, pos);
    }

    public void dropItem(Level level, BlockPos pos) {
        if (upgradeStack.isEmpty())
            return;

        if(upgradeItem != null)
            upgradeItem.deinitDataComponents(upgradeStack, level, getBlockPos());

        dropItem(level, pos, upgradeStack);
    }

    public void dropItem(Level level, BlockPos pos, ItemStack stack){
        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
    }

    private FactoryUpgradeData.Component getComponent(){
        return new FactoryUpgradeData.Component(Optional.ofNullable(getUpgradeItemName()), Optional.of(upgradeStack));
    }

    private void setComponent(FactoryUpgradeData.Component component){
        component.upgradeItem().ifPresentOrElse(item -> {
            this.upgradeItem = !item.isEmpty() && UpgradeItemsRegistry.has(item) ? UpgradeItemsRegistry.get(item).get() : null;
        }, () -> {
            this.upgradeItem = null;
        });
        this.upgradeStack = upgradeItem == null ? ItemStack.EMPTY : component.upgradeStack().orElse(upgradeItem.getDefaultInstance());
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output){
        super.saveAdditional(output);
        output.store(FactoryUpgradeData.ID, FactoryUpgradeData.CODEC, getComponent());
    }

    @Override
    public void loadAdditional(@NotNull ValueInput input){
        super.loadAdditional(input);
        input.read(FactoryUpgradeData.ID, FactoryUpgradeData.CODEC).ifPresent(this::setComponent);
    }

    private static final ProblemReporter.ScopedCollector REPORTER = Woot.reporter("FactoryUpgradeBlockEntity");

    @NotNull
    @Override
    public CompoundTag getUpdateTag(HolderLookup.@NotNull Provider provider){
        CompoundTag tag = super.getUpdateTag(provider);
        TagValueOutput output = TagValueOutput.createWithContext(REPORTER, provider);
        saveAdditional(output);
        tag.merge(output.buildResult());
        return tag;
    }

    @Override
    public void handleUpdateTag(@NotNull ValueInput input){
        super.handleUpdateTag(input);
        loadAdditional(input);
    }

    private static final ModelProperty<String> UPGRADE_MODEL_PROPERTY = new ModelProperty<>();

    public @NotNull ModelData getModelData(){
        return super.getModelData().derive().with(UPGRADE_MODEL_PROPERTY, getUpgradeItemName()).build();
    }

        public void tryRequestModelDataUpdate(){
        if(level == null || level.getModelDataManager() == null)
            return;

        ModelData data = level.getModelDataManager().getAt(getBlockPos());
        if(data.has(UPGRADE_MODEL_PROPERTY)){
            if(upgradeItem == null && !data.get(UPGRADE_MODEL_PROPERTY).isEmpty()){
                requestModelDataUpdate();
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            } else if(upgradeItem != null && !data.get(UPGRADE_MODEL_PROPERTY).equalsIgnoreCase(UpgradeItemsRegistry.getNameFromItem(upgradeItem))){
                requestModelDataUpdate();
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
    }
}
