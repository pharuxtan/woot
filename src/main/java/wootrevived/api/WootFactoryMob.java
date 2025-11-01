package wootrevived.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import wootrevived.api.enums.Tier;
import wootrevived.api.interfaces.WootDropsProperties;

import java.util.List;

/**
 * Describes a mob type that can be simulated by a Woot factory.
 * <p>
 * Subclass this to customize display names, shard matching, import requirements,
 * and to modify drops produced during simulation.
 *
 * @param <T> the concrete entity type
 */
public class WootFactoryMob<T extends Entity> {
    private final Properties properties;
    protected final EntityType<T> entityType;

    public WootFactoryMob(EntityType<T> entityType, Properties properties) {
        this.entityType = entityType;
        this.properties = properties;

        if(properties.vitalityCost == -1)
            properties.vitalityCost = properties.tier.defaultVitalityCost();
    }

    /**
     * Returns the mob name to display in UI.
     * <p>
     * Override to provide a custom display name based on the mob's NBT.
     *
     * @param mobTag the mob's saved tag
     * @param registryAccess access to the current registry view
     * @return a localized display name
     */
    public MutableComponent getDisplayName(CompoundTag mobTag, RegistryAccess registryAccess) {
        return Component.translatable(entityType.getDescriptionId());
    }

    /**
     * Returns the name shown in the descriptive tooltip that instructs
     * which mob to kill on the Mob Shard.
     * <p>
     * By default, delegates to {@link #getDisplayName(CompoundTag, RegistryAccess)}.
     *
     * @param mobTag the mob's saved tag
     * @param registryAccess access to the current registry view
     * @return a localized tooltip name
     */
    public MutableComponent getTooltipKillName(CompoundTag mobTag, RegistryAccess registryAccess) {
        return getDisplayName(mobTag, registryAccess);
    }

    /**
     * Produces the NBT used by Woot to identify/modify this mob's behavior and drops.
     * <p>
     * Override to store additional fields that affect display or drop logic.
     * The default implementation copies the {@code id} from the supplied tag.
     *
     * @param mobTag the source tag from the captured entity
     * @param registryAccess access to the current registry view
     * @return a saved tag used by the factory
     */
    public CompoundTag saveTag(CompoundTag mobTag, RegistryAccess registryAccess){
        CompoundTag tag = new CompoundTag();
        tag.putString("id", mobTag.getString("id").orElse("minecraft:pig"));
        return tag;
    }

    /**
     * Compares a Mob Shard tag with a live/captured mob tag to determine if they represent
     * the same target.
     * <p>
     * Override to customize matching rules (e.g., include variant, NBT flags, etc.).
     * The default checks equality of the {@code id} field.
     *
     * @param shardTag the shard's stored tag
     * @param mobTag   the candidate mob's tag
     * @param registryAccess access to the current registry view
     * @return {@code true} if they match; otherwise {@code false}
     */
    public boolean isSame(CompoundTag shardTag, CompoundTag mobTag, RegistryAccess registryAccess){
        return shardTag.getString("id").equals(mobTag.getString("id"));
    }

    /**
     * Loads and prepares a {@link LivingEntity} instance from its serialized NBT data for use in simulation.
     * <p>
     * Override this method to alter spawn behavior, entity initialization, or to provide special handling
     * for custom entities that require additional setup during simulation.
     * <p>
     * Note: The returned entity is <em>not</em> added to the world automatically, it exists only for
     * simulated behavior and inspection.
     *
     * @param mobTag the serialized entity data (must include an {@code id})
     * @param level  the server level context to load the entity into
     * @return the reconstructed {@link LivingEntity}, or {@code null} if loading failed
     * @since 1.0.4
     */
    @SuppressWarnings({"deprecation", "OverrideOnly", "UnstableApiUsage"})
    @ApiStatus.AvailableSince("1.0.4")
    public @Nullable LivingEntity loadEntity(CompoundTag mobTag, ServerLevel level){
        if(level == null || !mobTag.contains("id"))
            return null;

        Entity entity = EntityType.loadEntityRecursive(mobTag, level, EntitySpawnReason.SPAWNER, e -> e);

        if(!(entity instanceof LivingEntity livingEntity))
            return null;

        if(entity instanceof Mob mob){
            var event = new FinalizeSpawnEvent(mob, level, 0, 0, 0, level.getCurrentDifficultyAt(BlockPos.ZERO), EntitySpawnReason.SPAWNER, null, null);
            NeoForge.EVENT_BUS.post(event);
            mob.finalizeSpawn(level, level.getCurrentDifficultyAt(BlockPos.ZERO), EntitySpawnReason.SPAWNER, null);
        }

        return livingEntity;
    }

    /**
     * Allows modification of drops produced by the simulation.
     * <p>
     * This is invoked at well-defined points in the drop pipeline (see {@link Phase}).
     * Use the provided {@code properties} to inspect context and add/replace drops.
     *
     * @param phase      current phase of the drop pipeline
     * @param properties mutable access to generated drops and context
     */
    public void modifyDrops(Phase phase, WootDropsProperties properties) {
    }

    /**
     * Specifies item inputs required to simulate this mob.
     * <p>
     * Called before simulation. The list size is limited to 36 stacks.
     *
     * @param mobTag the mob's saved tag
     * @param registryAccess access to the current registry view
     * @return a list of required item stacks (may be empty)
     */
    public List<ItemStack> getImportItems(CompoundTag mobTag, RegistryAccess registryAccess){
        return List.of();
    }

    /**
     * Specifies fluid inputs required to simulate this mob.
     * <p>
     * Called before simulation. The list size is limited to 8 stacks.
     *
     * @param mobTag the mob's saved tag
     * @param registryAccess access to the current registry view
     * @return a list of required fluid stacks (may be empty)
     */
    public List<FluidStack> getImportFluids(CompoundTag mobTag, RegistryAccess registryAccess){
        return List.of();
    }

    /**
     * @return the underlying {@link EntityType}
     */
    public final EntityType<T> getEntityType() {
        return entityType;
    }

    /**
     * Phases within the drop-generation pipeline where {@link #modifyDrops} may be called.
     */
    public enum Phase {
        /**
         * Invoked immediately before custom drop callbacks execute.
         */
        BEFORE_DROP_CALLBACKS,

        /**
         * Invoked immediately after custom drop callbacks complete.
         */
        AFTER_DROP_CALLBACKS,

        /**
         * Invoked after upgrade modules have applied their drop modifications.
         */
        AFTER_UPGRADES;

        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        public boolean isBeforeDropCallback(){
            return this == BEFORE_DROP_CALLBACKS;
        }

        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        public boolean isAfterDropCallback(){
            return this == AFTER_DROP_CALLBACKS;
        }

        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        public boolean isAfterUpgrades(){
            return this == AFTER_UPGRADES;
        }
    }

    /**
     * Mutable configuration used to constrain or annotate simulation behavior
     * for this mob type (blacklisting, timing, tier, and fuel cost).
     * <p>
     * Use the fluent setters to adjust behavior; values are validated/sanitized
     * where applicable.
     */
    public static final class Properties {
        boolean blacklist = false;
        boolean disabledSimulation = false;
        int rate = 300; // 15 sec
        int vitalityCost = -1; // mB
        Tier tier = Tier.TIER_1;

        /**
         * Whether this mob is banned from capture/simulation.
         *
         * @param isBlacklisted true to blacklist, false otherwise
         */
        public Properties blacklist(boolean isBlacklisted) {
            this.blacklist = isBlacklisted;
            return this;
        }

        /**
         * Whether the factory should skip vanilla drop simulation for this mob.
         *
         * @param isSimulationDisabled true to disable vanilla drops
         */
        public Properties disabledSimulation(boolean isSimulationDisabled) {
            this.disabledSimulation = isSimulationDisabled;
            return this;
        }

        /**
         * Sets the number of ticks between simulations.
         *
         * @param spawnTickRate non-negative tick interval
         */
        public Properties rate(int spawnTickRate){
            this.rate = Math.max(0, spawnTickRate);
            return this;
        }

        /**
         * Sets the vitality fuel cost (mB) required to simulate this mob.
         *
         * @param vitalityCost non-negative cost in millibuckets
         */
        public Properties vitalityCost(int vitalityCost){
            this.vitalityCost = Math.max(0, vitalityCost);
            return this;
        }

        /**
         * Sets the minimum factory tier required to simulate this mob.
         * {@link Tier#INVALID} is ignored.
         *
         * @param tier minimum required tier
         */
        public Properties tier(Tier tier) {
            if(tier == Tier.INVALID) return this;
            this.tier = tier;
            return this;
        }
    }

    public final boolean isBlacklisted(){
        return this.properties.blacklist;
    }

    public final boolean isSimulationDisabled(){
        return this.properties.disabledSimulation;
    }

    public final int getSpawnTickRate(){
        return this.properties.rate;
    }

    public final int getVitalityFuelCost(){
        return this.properties.vitalityCost;
    }

    public final Tier getTier(){
        return this.properties.tier;
    }
}
