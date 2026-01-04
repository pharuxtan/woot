package wootrevived.api.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.storage.ValueInput;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;

/**
 * Provides access to the configurable properties of a mob simulation
 * during the generation stage.
 * <p>
 * This interface is passed into Woot API callbacks (e.g. upgrade items or plugins)
 * before the factory consumes ingredients and vitality fuel. It allows
 * inspection and modification of how the simulation will be executed.
 * <p>
 * Consumers can:
 * <ul>
 *   <li>Adjust spawn rate, vitality fuel cost, or number of simulations</li>
 *   <li>Query the factory tier and associated mob</li>
 *   <li>Access the mob's saved tag and a random source for deterministic changes</li>
 * </ul>
 * <p>
 * The implementation is provided by Woot; addon mods should not implement this
 * interface themselves.
 */
public interface WootGenerationProperties {
    int getSpawnRate();
    void setSpawnRate(int spawnRate);

    int getVitalityFuelCost();
    void setVitalityFuelCost(int vitalityFuelCost);

    int getNumberOfSimulations();
    void setNumberOfSimulations(int numberOfSimulations);

    @NonNull ServerLevel getLevel();
    @NonNull RandomSource getRandom();
    @NonNull RegistryAccess getRegistryAccess();
    @NonNull Tier getFactoryTier();
    @NonNull WootFactoryMob<?> getFactoryMob();
    @NonNull ValueInput getFactoryMobValue();

    @ApiStatus.AvailableSince("1.1.4")
    @NonNull ServerLevel getHeartLevel();

    @ApiStatus.AvailableSince("1.1.4")
    @NonNull BlockPos getHeartPos();
}
