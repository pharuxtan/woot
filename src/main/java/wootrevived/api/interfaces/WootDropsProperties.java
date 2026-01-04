package wootrevived.api.interfaces;

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
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;

import java.util.List;

/**
 * Provides access to the outcome of a mob simulation after it has completed.
 * <p>
 * This interface is passed into Woot API callbacks (e.g. upgrade items or plugins)
 * to allow inspection and modification of generated drops.
 * <p>
 * Consumers can:
 * <ul>
 *   <li>Query the mob's spawn context (equipment, flags, tier, etc.)</li>
 *   <li>Inspect the generated drops (items, fluids, and experience)</li>
 *   <li>Adjust the experience value or mutate the drop lists before they are finalized</li>
 * </ul>
 * <p>
 * The implementation is provided by Woot; addon mods should not implement this
 * interface themselves.
 */
public interface WootDropsProperties {
    @NonNull ItemStack getMainHandItem();
    @NonNull ItemStack getOffHandItem();
    float getLuck();
    boolean doSimulateChargedCreeper();
    boolean isEnderDragonAlreadyKilled();
    boolean isInFire();

    List<ItemStack> getItemDrops();

    List<FluidStack> getFluidDrops();

    int getExperience();
    void setExperience(int experience);

    @NonNull ServerLevel getLevel();
    @NonNull RandomSource getRandom();
    @NonNull RegistryAccess getRegistryAccess();
    @NonNull Tier getFactoryTier();
    @NonNull WootFactoryMob<?> getFactoryMob();
    @NonNull ValueInput getFactoryMobValue();

    @ApiStatus.AvailableSince("1.0.4")
    @Nullable LivingEntity getEntity();

    @ApiStatus.AvailableSince("1.0.6")
    @NonNull ResourceKey<Level> getDimension();

    @ApiStatus.AvailableSince("1.1.4")
    @NonNull ServerLevel getHeartLevel();

    @ApiStatus.AvailableSince("1.1.4")
    @NonNull BlockPos getHeartPos();
}
