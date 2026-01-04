package wootrevived.api.enums;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;
import wootrevived.api.interfaces.WootUpgradeEnum;

/**
 * Special variant type for upgrade items that conceptually have no variants.
 * <p>
 * This enum provides a single constant {@link #NONE} and is intended for
 * upgrades where:
 * <ul>
 *     <li>no state needs to be stored,</li>
 *     <li>the upgrade never changes mode or tier.</li>
 * </ul>
 *
 * <h2>Intended Use</h2>
 * Useful when:
 * <ul>
 *     <li>the upgrade item has only a single possible configuration,</li>
 *     <li>the upgrade does not require dynamic textures or stored modes,</li>
 *     <li>a developer wishes to avoid creating a custom enum.</li>
 * </ul>
 */
public enum UpgradeNoVariant implements WootUpgradeEnum<UpgradeNoVariant> {
    NONE;

    private static final Codec<UpgradeNoVariant> CODEC = StringRepresentable.fromEnum(UpgradeNoVariant::values);

    @Override
    public @NonNull String getSerializedName() {
        return "";
    }

    @Override
    public Codec<UpgradeNoVariant> codec() {
        return CODEC;
    }
}
