package wootrevived.woot.blocks.factory_upgrade;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
        this.upgradeNBT = new CompoundTag();
    }

    private WootUpgradeItem upgradeItem;
    private CompoundTag upgradeNBT;

    public void applyGenerationProperties(WootGenerationProperties properties){
        if(upgradeItem != null)
            upgradeItem.applyGenerationProperties(properties, upgradeNBT);
    }

    public void applySpawnProperties(WootSpawnProperties properties){
        if(upgradeItem != null)
            upgradeItem.applySpawnProperties(properties, upgradeNBT);
    }

    public void modifyDrops(WootDropsProperties properties){
        if(upgradeItem != null)
            upgradeItem.modifyDrops(properties, upgradeNBT);
    }

    public String getUpgradeItemName() {
        return upgradeItem == null ? "" : UpgradeItemsRegistry.getNameFromItem(upgradeItem);
    }

    public ItemStack getUpgradeItemStack() {
        return upgradeItem == null ? ItemStack.EMPTY : upgradeItem.getDefaultInstance();
    }

    public void addUpgrade(Level level, Player player, InteractionHand hand, ItemStack stack, WootUpgradeItem newUpgradeItem){
        if(upgradeItem == newUpgradeItem)
            return;

        WootUpgradeItem oldUpgradeItem = upgradeItem;
        upgradeItem = newUpgradeItem;
        newUpgradeItem.initUpgradeTag(upgradeNBT = new CompoundTag(), level.registryAccess());
        setChanged();
        player.swing(hand);

        if (!player.isCreative()){
            stack.shrink(1);
            if(oldUpgradeItem != null){
                if(stack.isEmpty()){
                    player.setItemInHand(hand, oldUpgradeItem.getDefaultInstance());
                } else {
                    dropItem(level, player.getOnPos().above(), oldUpgradeItem);
                }
            }
        }
    }

    public void removeUpgrade(Level level, Player player, InteractionHand hand){
        WootUpgradeItem oldUpgradeItem = upgradeItem;
        upgradeItem = null;
        upgradeNBT = new CompoundTag();
        setChanged();
        player.swing(hand);

        if(oldUpgradeItem != null){
            if(player.getItemInHand(hand).isEmpty()){
                player.setItemInHand(hand, oldUpgradeItem.getDefaultInstance());
            } else {
                dropItem(level, player.getOnPos().above(), oldUpgradeItem);
            }
        }
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state){
        dropItem(level, pos);
    }

    public void dropItem(Level level, BlockPos pos) {
        if (upgradeItem == null)
            return;

        dropItem(level, pos, upgradeItem);
    }

    public void dropItem(Level level, BlockPos pos, WootUpgradeItem upgradeItem){
        ItemStack stack = upgradeItem.getDefaultInstance();
        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
    }

    private FactoryUpgradeData.Component getComponent(){
        return new FactoryUpgradeData.Component(Optional.ofNullable(getUpgradeItemName()), Optional.of(upgradeNBT));
    }

    private void setComponent(FactoryUpgradeData.Component component){
        component.upgradeItem().ifPresentOrElse(item -> {
            this.upgradeItem = !item.isEmpty() && UpgradeItemsRegistry.has(item) ? UpgradeItemsRegistry.get(item).get() : null;
        }, () -> {
            this.upgradeItem = null;
        });
        upgradeNBT = component.upgradeTag().orElse(new CompoundTag());
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
