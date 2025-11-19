package wootrevived.api.registrations;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import wootrevived.api.IWootPlugin;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.interfaces.WootUpgradeEnum;
import wootrevived.api.models.DynamicUpgradeItemModelBuilder;

/**
 * Registration helper for adding custom {@link WootUpgradeItem WootUpgradeItem} instances to Woot.
 * <p>
 * Instances of this class are provided to {@link IWootPlugin#registerUpgradeItems}
 * and must be used by plugin authors to integrate their upgrades.
 */
public abstract class WootUpgradeItemRegistration {
    /**
     * Registers a {@link WootUpgradeItem} whose variant is fixed for the lifetime
     * of the item instance.
     * <p>
     * Use this method when:
     * <ul>
     *     <li>the upgrade item has exactly one behavior variant, <strong>or</strong></li>
     *     <li>the upgrade item uses a variant enum, but each variant is represented as a separate registry entry,</li>
     *     <li>the variant cannot change at runtime,</li>
     *     <li>and no automatic per-variant rendering is needed.</li>
     * </ul>
     *
     * <p>
     * This registration method fully supports having multiple variants that affect
     * factory logic. Each variant simply corresponds to its own item in the registry.
     * </p>
     *
     * @param item the upgrade item to register
     * @param <T>  the variant enum type used by the item
     */
    public abstract <T extends Enum<T> & WootUpgradeEnum<T>> void register(DeferredHolder<Item, ? extends WootUpgradeItem<T>> item);

    /**
     * Registers a {@link WootUpgradeItem} that supports <strong>multiple
     * variants within the same item instance</strong>, where the active variant
     * is stored in NBT and may change at runtime.
     * <p>
     * Use this method when:
     * <ul>
     *     <li>a single item represents multiple variants,</li>
     *     <li>the variant can be changed dynamically,</li>
     *     <li>the item's logic should react to the current variant,</li>
     *     <li>and you want Woot's built-in system to handle variant tracking,
     *         block overlays, and optional per-variant item models.</li>
     * </ul>
     *
     * <p>
     * Dynamic registration activates Woot's variant-handling pipeline. This allows
     * the engine to fetch the correct model or block overlay based on the item's
     * stored variant and ensures that variant switching is reflected client-side.
     * </p>
     *
     * <p>
     * Dynamic variant rendering is <strong>optional</strong>. Developers are not
     * required to provide a model using {@link DynamicUpgradeItemModelBuilder}.
     * The dynamic pipeline exists primarily to support runtime-changeable
     * variants; the distinct visual representation is an additional convenience.
     * </p>
     *
     * <p>
     * Dynamic registration is <strong>not required</strong> for upgrades with
     * multiple logic-only variants when each variant is represented by a separate
     * item. It is required only when a single item must support multiple
     * changeable variants, with optionally distinct visual representation.
     * </p>
     *
     * @param item           the upgrade item to register
     * @param dynamicVariant the enum class defining all supported variants
     * @param <T>            the variant enum type used by the item
     */
    public abstract <T extends Enum<T> & WootUpgradeEnum<T>> void register(DeferredHolder<Item, ? extends WootUpgradeItem<T>> item, Class<T> dynamicVariant);

    /**
     * Provides access to Woot's internal event bus.
     * <p>
     * This allows external mods to register their own upgrade items in the same
     * way Woot Revived does internally. Intended for mods that extend or
     * complement Woot with additional upgrade items.
     *
     * @return the Woot event bus
     */
    public abstract IEventBus getWootEventBus();
}
