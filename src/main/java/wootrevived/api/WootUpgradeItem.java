package wootrevived.api;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.interfaces.WootGenerationProperties;
import wootrevived.api.interfaces.WootSpawnProperties;
import wootrevived.api.interfaces.WootUpgradeEnum;
import wootrevived.api.models.DynamicUpgradeItemModelBuilder;
import wootrevived.api.registrations.WootUpgradeItemRegistration;

import java.util.Optional;

/**
 * Base class for all Woot upgrade items.
 * <p>
 * An upgrade item defines additional behavior for a Woot factory. Each upgrade
 * is parameterized by an enum type {@code T} that implements
 * {@link WootUpgradeEnum}. This enables one item to represent one or more
 * logical variants that may affect behavior, stored state, or appearance.
 *
 * <h2>Registration Requirements</h2>
 * All upgrade items <strong>must</strong> be registered using
 * {@link WootUpgradeItemRegistration}.
 * Registration enables Woot to:
 * <ul>
 *     <li>include the upgrade in the factory logic pipeline,</li>
 *     <li>load and manage variant definitions,</li>
 *     <li>expose upgrades to the Woot creative tab,</li>
 *     <li>enable dynamic-variant behavior when needed.</li>
 * </ul>
 *
 * <h2>Static vs. Dynamic Upgrade Items</h2>
 * Woot supports two patterns for variant usage:
 *
 * <h3>1. Static (logic-only or tiered) variants</h3>
 * Each variant is represented by its <strong>own item</strong>.
 * Variant differences may affect upgrade logic (e.g., the Decapitate upgrade's
 * tier levels).
 * Use
 * {@link WootUpgradeItemRegistration#register(RegistryObject)}
 * when:
 * <ul>
 *     <li>each variant is a separate registry object,</li>
 *     <li>the variant never changes at runtime,</li>
 *     <li>distinct per-variant models are <em>not</em> required,</li>
 *     <li>variants primarily affect logic, not rendering.</li>
 * </ul>
 * This is the recommended pattern for tiered or level-based upgrades.
 *
 * <h3>2. Dynamic (runtime-changeable) variants</h3>
 * A single item may represent multiple variants whose value is stored in NBT
 * and may change dynamically (e.g., through interaction or custom rules).
 * <br>
 * Use
 * {@link WootUpgradeItemRegistration#register(RegistryObject, Class)}
 * when:
 * <ul>
 *     <li>a single item contains multiple possible variants,</li>
 *     <li>the variant may change during gameplay,</li>
 *     <li>the variant influences factory logic,</li>
 *     <li>optional automatic per-variant item/block rendering is desired.</li>
 * </ul>
 *
 * <p><strong>Important:</strong> Dynamic registration enables Woot's dynamic
 * variant-handling pipeline, but <strong>per-variant rendering is optional</strong>.
 * Developers may choose not to use {@link DynamicUpgradeItemModelBuilder}.
 * </p>
 *
 * <h2>Variant Storage</h2>
 * For dynamically registered items, the current variant is stored in the
 * item's NBT under {@link #VARIANT_TAG}.
 * Static items may ignore this and rely entirely on the variant passed into
 * the constructor.
 *
 * <h2>Upgrade Interaction & Logic</h2>
 * Subclasses may override:
 * <ul>
 *     <li>{@link #applyGenerationProperties}</li>
 *     <li>{@link #applySpawnProperties}</li>
 *     <li>{@link #modifyDrops}</li>
 *     <li>{@link #interact}</li>
 *     <li>{@link #initItemTag}</li>
 *     <li>{@link #deinitItemTag}</li>
 * </ul>
 * to define their behavior at different points in the factory lifecycle.
 *
 * <h2>Rendering Support (Optional)</h2>
 * Two client-side hooks allow an upgrade to customize its appearance:
 * <ul>
 *     <li>{@link #applyItemTexture}</li>
 *     <li>{@link #applyUpgradeTexture}</li>
 * </ul>
 * These hooks receive mutable images and allow variant-aware visual adjustments
 * without supplying separate PNG files.
 * <br>
 * If dynamic visual representation is desired, data generators may use
 * {@link DynamicUpgradeItemModelBuilder}.
 *
 * <h2>Implementing a new upgrade</h2>
 * <ol>
 *     <li>Create an enum implementing {@link WootUpgradeEnum}.</li>
 *     <li>Subclass {@code WootUpgradeItem<T>}.</li>
 *     <li>Register the item using the appropriate registration method:
 *         <ul>
 *             <li>use {@code register(item)} for static or logic-only variants,</li>
 *             <li>use {@code register(item, variantClass)} for runtime-changeable variants.</li>
 *         </ul>
 *     </li>
 *     <li>(Optional) Use {@link DynamicUpgradeItemModelBuilder} for variant-specific item models.</li>
 * </ol>
 *
 * @param <T> the variant enum type for this upgrade, implementing {@link WootUpgradeEnum}
 */
public abstract class WootUpgradeItem<T extends Enum<T> & WootUpgradeEnum<T>> extends Item {
    public static String VARIANT_TAG = "UpgradeItemVariant";

    protected final T defaultVariant;
    private final Codec<T> variantCodec;

    /**
     * Creates a new upgrade item using the given default variant.
     * <p>
     * Static upgrades receive their variant from the constructor and never change.
     * Dynamic upgrades use this value when no variant is stored in NBT.
     *
     * @param properties     standard item properties
     * @param defaultVariant the variant to fall back to when no NBT value is present
     */
    public WootUpgradeItem(@NotNull Properties properties, @NotNull T defaultVariant) {
        super(properties);
        this.variantCodec = defaultVariant.codec();
        this.defaultVariant = defaultVariant;
    }

    /**
     * Initializes the persistent per-item data container the first time the upgrade
     * is used in a factory. Static and dynamic upgrades may store long-lived state
     * here.
     *
     * @param itemTag container for persistent per-upgrade data on this item
     * @param level the world level containing the upgrade block
     * @param pos the position of the upgrade block using this upgrade
     */
    public void initItemTag(@NotNull CompoundTag itemTag, @NotNull Level level, @NotNull BlockPos pos) {
    }

    /**
     * Called when the item is removed from a factory. Allows removal or reset of
     * installation-specific state while preserving long-term item data.
     *
     * @param itemTag container for persistent per-upgrade data on this item
     * @param level the world level containing the upgrade block
     * @param pos the position of the upgrade block using this upgrade
     */
    public void deinitItemTag(@NotNull CompoundTag itemTag, @NotNull Level level, @NotNull BlockPos pos) {
    }

    /**
     * Applies generation-phase configuration. Implementations may adjust behavior
     * based on the current variant or stored state.
     *
     * @param properties mutable generation properties
     * @param itemTag persistent upgrade data
     */
    public void applyGenerationProperties(@NotNull WootGenerationProperties properties, @NotNull CompoundTag itemTag) {
    }

    /**
     * Applies spawn-phase configuration before simulated mob spawning begins.
     * Variant values may be used to alter behavior.
     *
     * @param properties mutable spawn properties
     * @param itemTag persistent upgrade data
     */
    public void applySpawnProperties(@NotNull WootSpawnProperties properties, @NotNull CompoundTag itemTag) {
    }

    /**
     * Modifies the drops generated by the factory simulation. The variant may
     * influence how the produced items are processed.
     *
     * @param properties mutable access to post-simulation drop data
     * @param itemTag persistent upgrade data
     */
    public void modifyDrops(@NotNull WootDropsProperties properties, @NotNull CompoundTag itemTag) {
    }

    /**
     * Called when a player interacts with the upgrade block containing this item.
     * <p>
     * Dynamic upgrades may use this to cycle variants or change stored runtime
     * state. Static upgrades may use it for configuration or UI triggers.
     *
     * @param itemTag persistent upgrade data
     * @param level   the world level containing the upgrade block
     * @param player  the interacting player
     * @param hand    the hand used for the interaction
     * @param hit     raycast result describing the hit position and side
     *
     * @return an {@link InteractionResult} indicating whether the action was handled
     */
    public @NotNull InteractionResult interact(@NotNull CompoundTag itemTag, @NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit){
        return InteractionResult.PASS;
    }

    /**
     * Returns the current variant of this upgrade item. Dynamic upgrades read from
     * NBT; static upgrades simply return the default variant.
     *
     * @param itemTag the persistent component container for this item, or {@code null}
     * @return the resolved variant value
     */
    public final @NotNull T getVariant(@Nullable CompoundTag itemTag){
        if(itemTag == null)
            return defaultVariant;

        Tag tag = itemTag.get(VARIANT_TAG);
        if(tag == null)
            return defaultVariant;

        Optional<Pair<T, Tag>> variant = variantCodec.decode(NbtOps.INSTANCE, tag).result();
        return variant.map(Pair::getFirst).orElse(defaultVariant);
    }

    /**
     * Stores a new variant in the item's NBT (dynamic upgrades). Static upgrades
     * typically never call this method.
     *
     * @param itemTag the persistent component container for this item
     * @param variant the variant to store, or {@code null} to clear it
     */
    public final void setVariant(@NotNull CompoundTag itemTag, @Nullable T variant){
        if(variant != null) {
            Optional<Tag> tag = variantCodec.encodeStart(NbtOps.INSTANCE, variant).result();
            if(tag.isPresent()) {
                itemTag.put(VARIANT_TAG, tag.get());
                return;
            }
        }

        if(itemTag.contains(VARIANT_TAG))
            itemTag.remove(VARIANT_TAG);
    }

    /**
     * Returns the texture resource location for the given variant of this item.
     * <p>
     * By default, variants share the same texture, but subclasses may override this
     * to supply variant-specific icons.
     *
     * @param variant the upgrade variant whose icon is being requested
     * @return a {@link ResourceLocation} pointing to the item texture
     */
    public @NotNull ResourceLocation getTextureLocation(@NotNull T variant){
        return ForgeRegistries.ITEMS.getKey(this).withPrefix("textures/item/").withSuffix(".png");
    }

    /**
     * Optional client-side hook for variant-aware icon customization. Used only
     * when dynamic or custom rendering is desired.
     *
     * @param upgradeItem mutable pixel buffer containing the upgrade item's icon
     * @param variant      the active variant for which the icon is being prepared
     */
    @OnlyIn(Dist.CLIENT)
    public void applyItemTexture(@NotNull NativeImage upgradeItem, @NotNull T variant){
    }

    /**
     * Optional client-side hook for drawing the processed upgrade icon onto larger
     * composite textures (e.g., factory upgrade block faces).
     *
     * @param upgradeSide  the mutable target texture onto which the icon should be drawn
     * @param upgradeItem  the previously processed upgrade-item icon (after applyItemTexture)
     * @param variant      the active variant for which this composite texture is being produced
     */
    @OnlyIn(Dist.CLIENT)
    public void applyUpgradeTexture(@NotNull NativeImage upgradeSide, @NotNull NativeImage upgradeItem, @NotNull T variant){
        for(int y = 2; y < 14; y++){
            for(int x = 2; x < 14; x++){
                int color = upgradeItem.getPixelRGBA(x, y);
                int alpha = color >> 24;
                if(alpha != 0)
                    upgradeSide.setPixelRGBA(x, y, color);
            }
        }
    }
}
