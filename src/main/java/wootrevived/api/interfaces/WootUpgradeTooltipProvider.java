package wootrevived.api.interfaces;

import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TooltipProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.api.internal.WootUpgradeComponent;

/**
 * Tooltip helper interface for upgrade items that display information based on
 * their active variant.
 * <p>
 * Implementing this interface allows an item to easily read the current
 * {@link WootUpgradeEnum variant} stored in its
 * {@link WootUpgradeComponent} and use it while generating tooltips.
 *
 * <h2>Purpose</h2>
 * Many upgrade items provide descriptive tooltips that vary depending on the
 * current selected variant.
 * This interface provides utility methods for extracting the variant from the
 * item's data components without repeating boilerplate code.
 *
 * <h2>How It Works</h2>
 * The variant is retrieved from the
 * {@linkplain WootUpgradeComponent upgrade variant component}
 * stored on the {@link ItemStack}.
 * Tooltip generation may call:
 * <ul>
 *     <li>{@link #getVariant(DataComponentGetter)} – returns the component
 *     value or {@code null} if none is stored,</li>
 *     <li>{@link #getVariant(DataComponentGetter, Enum)} – returns the
 *     component value or a supplied fallback variant.</li>
 * </ul>
 *
 * @param <T> the upgrade item's variant enum type, implementing {@link WootUpgradeEnum}
 */
public interface WootUpgradeTooltipProvider<T extends Enum<T> & WootUpgradeEnum<T>> extends TooltipProvider {
    /**
     * Retrieves the active upgrade variant from the item's component data.
     *
     * @param dataComponentGetter a getter exposing the item's stored components
     * @return the variant value, or {@code null} if not present or unreadable
     */
    @SuppressWarnings("unchecked")
    default @Nullable T getVariant(@NotNull DataComponentGetter dataComponentGetter) {
        try {
            WootUpgradeComponent component = dataComponentGetter.get(WootUpgradeComponent.type());
            if(component == null)
                return null;
            return (T) component.variant();
        } catch(Exception ex) {
            return null;
        }
    }

    /**
     * Retrieves the active upgrade variant from the item's component data,
     * falling back to a provided default when no component value exists.
     *
     * @param dataComponentGetter the component accessor
     * @param defaultVariant the fallback value to return when no variant is stored
     * @return the stored variant, or {@code defaultVariant} if none exists
     */
    default @NotNull T getVariant(@NotNull DataComponentGetter dataComponentGetter, @NotNull T defaultVariant) {
        T variant = getVariant(dataComponentGetter);
        return variant == null ? defaultVariant : variant;
    }
}
