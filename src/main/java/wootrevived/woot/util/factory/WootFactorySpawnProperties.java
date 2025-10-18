package wootrevived.woot.util.factory;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;
import wootrevived.api.interfaces.WootSpawnProperties;
import wootrevived.woot.drops.simulator.DropSimulator;

public class WootFactorySpawnProperties implements WootSpawnProperties {
    private final Tier factoryTier;
    private final WootFactoryMob<?> factoryMob;
    private CompoundTag factoryMobTag;
    private final ServerLevel heartLevel;
    private final BlockPos heartPos;

    private ItemStack mainHandItem = Items.NETHERITE_SWORD.getDefaultInstance();
    private ItemStack offHandItem = ItemStack.EMPTY;
    private float luck = 0;
    private boolean isEnderDragonAlreadyKilled = true;
    private boolean isInFire = false;
    private boolean doSimulateChargedCreeper = false;
    private ResourceKey<Level> dimension = Level.OVERWORLD;

    public WootFactorySpawnProperties(Tier factoryTier, WootFactoryMob<?> factoryMob, CompoundTag factoryMobTag, ServerLevel heartLevel, BlockPos heartPos) {
        this.factoryTier = factoryTier;
        this.factoryMob = factoryMob;
        this.factoryMobTag = factoryMobTag;
        this.heartLevel = heartLevel;
        this.heartPos = heartPos;
    }

    @Override
    public @NotNull ItemStack getMainHandItem() {
        return mainHandItem;
    }

    @Override
    public void setMainHandItem(@NotNull ItemStack itemStack) {
        mainHandItem = itemStack;
    }

    @Override
    public @NotNull ItemStack getOffHandItem() {
        return offHandItem;
    }

    @Override
    public void setOffHandItem(@NotNull ItemStack itemStack) {
        offHandItem = itemStack;
    }

    @Override
    public float getLuck() {
        return luck;
    }

    @Override
    public void setLuck(float luck) {
        this.luck = luck;
    }

    @Override
    public boolean doSimulateChargedCreeper() {
        return doSimulateChargedCreeper;
    }

    @Override
    public void setDoSimulateChargedCreeper(boolean doSimulateChargedCreeper) {
        this.doSimulateChargedCreeper = doSimulateChargedCreeper;
    }

    @Override
    public boolean isEnderDragonAlreadyKilled() {
        return isEnderDragonAlreadyKilled;
    }

    @Override
    public void setEnderDragonAlreadyKilled(boolean isEnderDragonAlreadyKilled) {
        this.isEnderDragonAlreadyKilled = isEnderDragonAlreadyKilled;
    }

    @Override
    public boolean isInFire() {
        return isInFire;
    }

    @Override
    public void setIsInFire(boolean isInFire) {
        this.isInFire = isInFire;
    }

    @Override
    public @NotNull ServerLevel getLevel() {
        return DropSimulator.getLevel();
    }

    @Override
    public @NotNull RandomSource getRandom() {
        return DropSimulator.getRandom();
    }

    @Override
    public @NotNull HolderLookup.Provider getLookupProvider() {
        return DropSimulator.getLookupProvider();
    }

    @Override
    public @NotNull Tier getFactoryTier() {
        return factoryTier;
    }

    @Override
    public @NotNull WootFactoryMob<?> getFactoryMob() {
        return factoryMob;
    }

    @Override
    public @NotNull CompoundTag getFactoryMobTag() {
        return factoryMobTag.copy();
    }

    @Override
    public void setFactoryMobTag(CompoundTag tag) {
        if(!tag.getString("id").equals(factoryMobTag.getString("id")))
            return;
        factoryMobTag = tag;
    }

    @Override
    public @NotNull ResourceKey<Level> getDimension() {
        return dimension;
    }

    @Override
    public void setDimension(@NotNull ResourceKey<Level> dimension) {
        this.dimension = dimension;
    }

    @Override
    public @NotNull ServerLevel getHeartLevel() {
        return heartLevel;
    }

    @Override
    public @NotNull BlockPos getHeartPos() {
        return heartPos;
    }
}
