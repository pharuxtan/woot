package wootrevived.api.interfaces;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.registrations.WootUpgradeItemRegistration;

/**
 * Represents a variant type used by a {@link WootUpgradeItem} to define its
 * logical subtypes, modes, or tier levels.
 * <p>
 * Every upgrade item declares a variant enum implementing this interface.
 * Variants are used for both logic-driven behavior and, when desired, optional
 * dynamic rendering.
 *
 * <h2>Variant Responsibilities</h2>
 * A variant enum:
 * <ul>
 *     <li>defines the possible variant values for an upgrade item,</li>
 *     <li>provides a unique serialized name via {@link #getSerializedName()},</li>
 *     <li>supplies an {@link Codec} for serialization,</li>
 *     <li>may hold additional fields or methods used by upgrade logic.</li>
 * </ul>
 *
 * <h2>Required Codec Implementation</h2>
 * Implementations <strong>must</strong> construct their codec using
 * {@link StringRepresentable#fromEnum}:
 *
 * <pre>{@code
 * private static final Codec<MyVariant> CODEC =
 *     StringRepresentable.fromEnum(MyVariant::values);
 *
 * @Override
 * public Codec<MyVariant> codec() {
 *     return CODEC;
 * }
 * }</pre>
 *
 * This ensures:
 * <ul>
 *     <li>safe round-trip serialization between strings and enum values,</li>
 *     <li>compatibility with {@link WootUpgradeItem#getVariant} and
 *         {@link WootUpgradeItem#setVariant}.</li>
 * </ul>
 *
 * <h2>Static vs. Dynamic Variant Usage</h2>
 * Variant enums themselves do not determine rendering behavior. They may be
 * used for:
 * <ul>
 *     <li><strong>static variants</strong> – separate registry items using the same enum,</li>
 *     <li><strong>logic-only variants</strong> – variants stored in item NBT but not used
 *         for rendering,</li>
 *     <li><strong>dynamic variants</strong> – a single item whose variant can change at
 *         runtime and may optionally use variant-aware rendering.</li>
 * </ul>
 *
 * Whether an upgrade item uses static or dynamic variants depends on its
 * registration in {@link WootUpgradeItemRegistration}, not on the enum itself.
 *
 * <h2>Rendering (Optional)</h2>
 * If the upgrade item is registered as dynamic, Woot's variant-rendering pipeline
 * will use the variant's serialized name to select the correct sprite and invoke
 * variant-aware texture hooks. Variant enums themselves perform no rendering.
 *
 * @param <T> the concrete enum type implementing this interface
 */
public interface WootUpgradeEnum<T extends Enum<T> & WootUpgradeEnum<T>> extends StringRepresentable {
    Codec<T> codec();
}
