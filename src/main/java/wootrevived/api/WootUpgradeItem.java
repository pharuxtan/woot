package wootrevived.api;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.interfaces.WootGenerationProperties;
import wootrevived.api.interfaces.WootSpawnProperties;
import wootrevived.api.interfaces.WootUpgradeEnum;
import wootrevived.api.internal.WootUpgradeComponent;
import wootrevived.api.models.DynamicUpgradeItemModelUnbaked;
import wootrevived.api.registrations.WootUpgradeItemRegistration;

/**
 * Base class for all Woot upgrade items.
 * <p>
 * An upgrade item augments the behavior of a Woot factory. Each upgrade is
 * parameterized by an enum type {@code T} implementing {@link WootUpgradeEnum},
 * which serves as the upgrade's variant type. Variants may influence upgrade
 * logic, store state, or optionally control rendering behavior.
 *
 * <h2>Registration Requirements</h2>
 * All upgrade items <strong>must</strong> be registered through
 * {@link WootUpgradeItemRegistration}. Registration ensures that Woot:
 * <ul>
 *     <li>exposes the upgrade in the correct creative tab,</li>
 *     <li>loads and manages variant semantics,</li>
 *     <li>integrates the upgrade in the factory processing lifecycle,</li>
 *     <li>enables optional dynamic variant handling and rendering.</li>
 * </ul>
 *
 * <h2>Variant Models</h2>
 * Woot supports two distinct variant usage patterns:
 *
 * <h3>1. Static (logic-only or fixed) variants</h3>
 * Each variant corresponds to a <strong>distinct item</strong>. The variant does
 * not change at runtime. Use:
 * <pre>
 *     register(item)
 * </pre>
 * when:
 * <ul>
 *     <li>each variant is a separate {@link DeferredHolder},</li>
 *     <li>no runtime switching is required,</li>
 *     <li>variant differences primarily affect factory behavior,</li>
 *     <li>separate models per variant are not required.</li>
 * </ul>
 *
 * <h3>2. Dynamic (runtime-changeable) variants</h3>
 * A single item holds multiple possible variants, stored as a data component
 * ({@link WootUpgradeComponent}) and optionally changed during gameplay.
 * Use:
 * <pre>
 *     register(item, variantClass)
 * </pre>
 * when:
 * <ul>
 *     <li>one item must support multiple modes,</li>
 *     <li>the variant may be changed through interactions or logic,</li>
 *     <li>the variant influences factory logic and/or visuals,</li>
 *     <li>optional automatic variant-aware rendering is desired.</li>
 * </ul>
 *
 * <p><strong>Note:</strong> Dynamic registration enables the runtime variant
 * system but <strong>does not require</strong> using
 * {@link DynamicUpgradeItemModelUnbaked}. Variant-specific rendering remains
 * optional.</p>
 *
 * <h2>Variant Storage</h2>
 * Runtime-changeable variants are stored using
 * {@link WootUpgradeComponent}, attached to the item through the
 * {@code MutableDataComponentHolder}. Static upgrades simply use the constructor
 * default and ignore stored values.
 *
 * <h2>Lifecycle Hooks</h2>
 * Subclasses may override the following methods to influence factory behavior:
 * <ul>
 *     <li>{@link #initDataComponents}</li>
 *     <li>{@link #deinitDataComponents}</li>
 *     <li>{@link #applyGenerationProperties}</li>
 *     <li>{@link #applySpawnProperties}</li>
 *     <li>{@link #modifyDrops}</li>
 *     <li>{@link #interact}</li>
 * </ul>
 *
 * <h2>Client-Side Rendering (Optional)</h2>
 * Upgrades may optionally provide variant-dependent visual customization using:
 * <ul>
 *     <li>{@link #applyItemTexture}</li>
 *     <li>{@link #applyUpgradeTexture}</li>
 * </ul>
 * These hooks supply raw {@link NativeImage} buffers that the upgrade may modify
 * without requiring separate texture files.
 * For data generators, {@link DynamicUpgradeItemModelUnbaked} enables
 * variant-aware item models.
 *
 * <h2>Implementing a new upgrade</h2>
 * <ol>
 *     <li>Define an enum implementing {@link WootUpgradeEnum}.</li>
 *     <li>Subclass {@code WootUpgradeItem<T>} using your enum.</li>
 *     <li>Register the item:
 *         <ul>
 *             <li>use {@code register(item)} for fixed/tiered variants,</li>
 *             <li>use {@code register(item, variantClass)} for runtime-changeable variants.</li>
 *         </ul>
 *     </li>
 *     <li>(Optional) Attach {@code DynamicUpgradeItemModelBuilder} for
 *         variant-aware models.</li>
 * </ol>
 *
 * @param <T> the variant enum type for this upgrade, implementing {@link WootUpgradeEnum}
 */
public abstract class WootUpgradeItem<T extends Enum<T> & WootUpgradeEnum<T>> extends Item {
    protected final WootUpgradeComponent defaultVariant;

    /**
     * Creates a new upgrade item using the given default variant.
     * <p>
     * Static upgrades rely entirely on this value.
     * Dynamic upgrades fall back to this variant when no variant is stored in
     * the item's {@link WootUpgradeComponent}.
     *
     * @param properties     standard item properties
     * @param defaultVariant the variant to fall back to when no component value is present
     */
    public WootUpgradeItem(@NonNull Properties properties, @NonNull T defaultVariant) {
        super(properties.component(WootUpgradeComponent.type(), WootUpgradeComponent.of(defaultVariant)));
        this.defaultVariant = WootUpgradeComponent.of(defaultVariant);
    }

    /**
     * Initializes long-lived data components the first time this upgrade is used
     * in a factory. Both static and dynamic upgrades may store state here.
     *
     * @param dataComponentHolder component storage attached to the item
     * @param level the world level containing the upgrade block
     * @param pos the block position of the upgrade block
     */
    public void initDataComponents(@NonNull MutableDataComponentHolder dataComponentHolder, @NonNull Level level, @NonNull BlockPos pos) {
    }

    /**
     * Called when the item is removed from a factory. Allows cleaning up
     * installation-specific state while preserving any long-term data.
     *
     * @param dataComponentHolder component storage attached to the item
     * @param level the world level containing the upgrade block
     * @param pos the block position of the upgrade block
     */
    public void deinitDataComponents(@NonNull MutableDataComponentHolder dataComponentHolder, @NonNull Level level, @NonNull BlockPos pos) {
    }

    /**
     * Applies generation-phase configuration. Implementations may alter factory
     * generation settings based on the variant or other stored component data.
     *
     * @param properties mutable generation configuration
     * @param dataComponentHolder persistent upgrade data
     */
    public void applyGenerationProperties(@NonNull WootGenerationProperties properties, @NonNull MutableDataComponentHolder dataComponentHolder) {
    }

    /**
     * Applies spawn-phase configuration before simulated mob spawning begins.
     * Variant values may be used to alter behavior.
     *
     * @param properties mutable spawn configuration
     * @param dataComponentHolder persistent upgrade data
     */
    public void applySpawnProperties(@NonNull WootSpawnProperties properties, @NonNull MutableDataComponentHolder dataComponentHolder){
    }

    /**
     * Modifies the drops produced by the simulation. The active variant may
     * influence what item stacks are changed or produced.
     *
     * @param properties mutable access to post-simulation drop data
     * @param dataComponentHolder persistent upgrade data
     */
    public void modifyDrops(@NonNull WootDropsProperties properties, @NonNull MutableDataComponentHolder dataComponentHolder) {
    }

    /**
     * Called when a player interacts with the upgrade block containing this item.
     * <p>
     * Dynamic upgrades may use this to cycle variants or change stored runtime
     * state. Static upgrades may use it for configuration or UI triggers.
     *
     * @param dataComponentHolder persistent upgrade data
     * @param stack               the stack used for the interaction
     * @param level               the world level containing the upgrade block
     * @param player              the interacting player
     * @param hand                the hand used for the interaction
     * @param hit                 raycast result describing the hit position and side
     *
     * @return an {@link InteractionResult} indicating whether the action was handled
     */
    public @NonNull InteractionResult interact(@NonNull MutableDataComponentHolder dataComponentHolder, @NonNull ItemStack stack, @NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand, @NonNull BlockHitResult hit){
        return InteractionResult.PASS;
    }

    /**
     * Returns the active variant for this upgrade item.
     * <p>
     * For dynamic upgrades, this value is read from the
     * {@link WootUpgradeComponent}.
     * For static upgrades, the constructor-provided default is always returned.
     *
     * @param dataComponentHolder the persistent component container for this item, or {@code null}
     * @return the resolved variant value
     */
    @SuppressWarnings("unchecked")
    public final @NonNull T getVariant(@Nullable MutableDataComponentHolder dataComponentHolder){
        if(dataComponentHolder == null)
            return (T) defaultVariant.variant();

        try {
            return (T) dataComponentHolder.getOrDefault(WootUpgradeComponent.type(), defaultVariant).variant();
        } catch(Exception ex) {
            dataComponentHolder.remove(WootUpgradeComponent.type());
            return (T) defaultVariant.variant();
        }
    }

    /**
     * Updates the stored variant in the item's component container.
     * Static upgrades typically never call this method.
     *
     * @param dataComponentHolder the persistent component container for this item
     * @param variant new variant value, or {@code null} to remove the stored component
     */
    public final void setVariant(@NonNull MutableDataComponentHolder dataComponentHolder, @Nullable T variant){
        if(variant != null)
            dataComponentHolder.set(WootUpgradeComponent.type(), WootUpgradeComponent.of(variant));
        else
            dataComponentHolder.remove(WootUpgradeComponent.type());
    }

    /**
     * Returns the texture resource location for the given variant of this item.
     * <p>
     * By default, variants share the same texture, but subclasses may override this
     * to supply variant-specific icons.
     *
     * @param variant the upgrade variant whose icon is being requested
     * @return a {@link Identifier} pointing to the item texture
     */
    public Identifier getTextureLocation(@NonNull T variant){
        return BuiltInRegistries.ITEM.getKey(this).withPrefix("textures/item/").withSuffix(".png");
    }

    /**
     * Optional client-side hook for variant-aware icon customization. Used only
     * when dynamic or custom rendering is desired.
     *
     * @param upgradeItem mutable pixel buffer containing the upgrade item's icon
     * @param variant      the active variant for which the icon is being prepared
     */
    public void applyItemTexture(@NonNull NativeImage upgradeItem, @NonNull T variant){
    }

    /**
     * Optional client-side hook for drawing the processed upgrade icon onto larger
     * composite textures (e.g., factory upgrade block faces).
     *
     * @param upgradeSide  the mutable target texture onto which the icon should be drawn
     * @param upgradeItem  the previously processed upgrade-item icon (after applyItemTexture)
     * @param variant      the active variant for which this composite texture is being produced
     */
    public void applyUpgradeTexture(@NonNull NativeImage upgradeSide, @NonNull NativeImage upgradeItem, @NonNull T variant){
        for(int y = 2; y < 14; y++){
            for(int x = 2; x < 14; x++){
                int color = upgradeItem.getPixel(x, y);
                int alpha = color >> 24;
                if(alpha != 0)
                    upgradeSide.setPixel(x, y, color);
            }
        }
    }
}
