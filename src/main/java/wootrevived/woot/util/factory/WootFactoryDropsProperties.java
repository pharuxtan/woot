package wootrevived.woot.util.factory;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jspecify.annotations.Nullable;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.interfaces.WootSpawnProperties;
import wootrevived.woot.drops.simulator.DropSimulator;

import java.util.ArrayList;
import java.util.List;

public class WootFactoryDropsProperties implements WootDropsProperties {
    private final WootSpawnProperties wootSpawnProperties;
    private final List<ItemStack> itemDrops = new ArrayList<>();
    private final List<FluidStack> fluidDrops = new ArrayList<>();
    private final LivingEntity entity;
    private int experience = 0;

    public WootFactoryDropsProperties(WootSpawnProperties properties, LivingEntity entity) {
        wootSpawnProperties = properties;
        this.entity = entity;
    }

    @Override
    public ItemStack getMainHandItem() {
        return wootSpawnProperties.getMainHandItem().copy();
    }

    @Override
    public ItemStack getOffHandItem() {
        return wootSpawnProperties.getOffHandItem().copy();
    }

    @Override
    public float getLuck() {
        return wootSpawnProperties.getLuck();
    }

    @Override
    public boolean doSimulateChargedCreeper() {
        return wootSpawnProperties.doSimulateChargedCreeper();
    }

    @Override
    public boolean isEnderDragonAlreadyKilled() {
        return wootSpawnProperties.isEnderDragonAlreadyKilled();
    }

    @Override
    public boolean isInFire() {
        return wootSpawnProperties.isInFire();
    }

    @Override
    public List<ItemStack> getItemDrops() {
        return itemDrops;
    }

    @Override
    public List<FluidStack> getFluidDrops() {
        return fluidDrops;
    }

    @Override
    public int getExperience() {
        return experience;
    }

    @Override
    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public ServerLevel getLevel() {
        return DropSimulator.getLevel();
    }

    @Override
    public RandomSource getRandom() {
        return wootSpawnProperties.getRandom();
    }

    @Override
    public RegistryAccess getRegistryAccess() {
        return DropSimulator.getRegistryAccess();
    }

    @Override
    public Tier getFactoryTier() {
        return wootSpawnProperties.getFactoryTier();
    }

    @Override
    public WootFactoryMob<?> getFactoryMob() {
        return wootSpawnProperties.getFactoryMob();
    }

    @Override
    public ValueInput getFactoryMobValue() {
        return wootSpawnProperties.getFactoryMobValue();
    }

    @Override
    public @Nullable LivingEntity getEntity() {
        return entity;
    }

    @Override
    public ResourceKey<Level> getDimension() {
        return wootSpawnProperties.getDimension();
    }

    @Override
    public ServerLevel getHeartLevel() {
        return wootSpawnProperties.getHeartLevel();
    }

    @Override
    public BlockPos getHeartPos() {
        return wootSpawnProperties.getHeartPos();
    }
}
