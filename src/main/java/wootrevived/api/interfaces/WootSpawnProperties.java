package wootrevived.api.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;

import java.util.function.Consumer;

/**
 * Provides access to the spawn properties of a mob before it is simulated
 * by a Woot factory.
 * <p>
 * This interface is passed into Woot API callbacks (e.g. upgrade items or plugins)
 * during the spawn stage. It allows inspection and modification of the mob's
 * equipment, status flags, and contextual data before the simulation begins.
 * <p>
 * Consumers can:
 * <ul>
 *   <li>Change the mob's main hand or off-hand items</li>
 *   <li>Adjust attributes such as luck or environmental flags (e.g. charged creeper, in fire, dragon killed)</li>
 *   <li>Access the factory tier, associated mob, saved NBT, server level, and random source</li>
 * </ul>
 * <p>
 * The implementation is provided by Woot; addon mods should not implement this
 * interface themselves.
 */
public interface WootSpawnProperties {
    @NonNull ItemStack getMainHandItem();
    void setMainHandItem(@NonNull ItemStack itemStack);

    @NonNull ItemStack getOffHandItem();
    void setOffHandItem(@NonNull ItemStack itemStack);

    float getLuck();
    void setLuck(float luck);

    boolean doSimulateChargedCreeper();
    void setDoSimulateChargedCreeper(boolean doSimulateChargedCreeper);

    boolean isEnderDragonAlreadyKilled();
    void setEnderDragonAlreadyKilled(boolean enderDragonAlreadyKilled);

    boolean isInFire();
    void setIsInFire(boolean isInFire);

    @NonNull ServerLevel getLevel();
    @NonNull RandomSource getRandom();
    @NonNull RegistryAccess getRegistryAccess();
    @NonNull Tier getFactoryTier();
    @NonNull WootFactoryMob<?> getFactoryMob();
    @NonNull ValueInput getFactoryMobValue();
    void setFactoryMobValue(Consumer<ValueOutput> consumer);

    @ApiStatus.AvailableSince("1.0.6")
    @NonNull ResourceKey<Level> getDimension();
    @ApiStatus.AvailableSince("1.0.6")
    void setDimension(@NonNull ResourceKey<Level> dimension);

    @ApiStatus.AvailableSince("1.1.4")
    @NonNull ServerLevel getHeartLevel();

    @ApiStatus.AvailableSince("1.1.4")
    @NonNull BlockPos getHeartPos();
}
