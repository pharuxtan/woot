package wootrevived.woot.util.factory;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.storage.ValueInput;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;
import wootrevived.api.interfaces.WootGenerationProperties;
import wootrevived.woot.drops.simulator.DropSimulator;

public class WootFactoryGenerationProperties implements WootGenerationProperties {
    private final Tier factoryTier;
    private final WootFactoryMob<?> factoryMob;
    private final ValueInput factoryMobValue;
    private final ServerLevel heartLevel;
    private final BlockPos heartPos;

    private int spawnRate;
    private int vitalityFuelCost;
    private int numberOfSimulations = 1;

    public WootFactoryGenerationProperties(Tier factoryTier, WootFactoryMob<?> factoryMob, ValueInput factoryMobValue, ServerLevel heartLevel, BlockPos heartPos, int spawnRate, int vitalityFuelCost){
        this.factoryTier = factoryTier;
        this.factoryMob = factoryMob;
        this.factoryMobValue = factoryMobValue;
        this.heartLevel = heartLevel;
        this.heartPos = heartPos;

        this.spawnRate = spawnRate;
        this.vitalityFuelCost = vitalityFuelCost;
    }

    @Override
    public int getSpawnRate() {
        return spawnRate;
    }

    @Override
    public void setSpawnRate(int spawnRate) {
        this.spawnRate = Math.max(0, spawnRate);
    }

    @Override
    public int getVitalityFuelCost() {
        return vitalityFuelCost;
    }

    @Override
    public void setVitalityFuelCost(int vitalityFuelCost) {
        this.vitalityFuelCost = Math.max(0, vitalityFuelCost);
    }

    @Override
    public int getNumberOfSimulations() {
        return numberOfSimulations;
    }

    @Override
    public void setNumberOfSimulations(int numberOfSimulations) {
        this.numberOfSimulations = Math.max(1, numberOfSimulations);
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
    public @NotNull RegistryAccess getRegistryAccess() {
        return DropSimulator.getRegistryAccess();
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
    public @NotNull ValueInput getFactoryMobValue() {
        return factoryMobValue;
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
