package wootrevived.api.enums;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.interfaces.WootUpgradeEnum;

/**
 * Default variant set used by many built-in Woot upgrades.
 * <p>
 * This enum provides a simple tiered structure (Copper -> Netherite) that may
 * be used to express increasing strength, effectiveness, or value. It is
 * suitable for both:
 * <ul>
 *     <li><strong>static variants</strong> - each tier registered as a separate upgrade item,</li>
 *     <li><strong>dynamic variants</strong> - a single item whose tier can change at runtime.</li>
 * </ul>
 *
 * <h2>Fields</h2>
 * Each variant provides:
 * <ul>
 *     <li>a human-readable <strong>serialized name</strong>,</li>
 *     <li>a numeric <strong>level</strong> representing upgrade strength.</li>
 * </ul>
 */
public enum UpgradeDefaultVariant implements WootUpgradeEnum<UpgradeDefaultVariant> {
    COPPER("copper", 1),
    IRON("iron", 2),
    GOLD("gold", 3),
    DIAMOND("diamond", 4),
    NETHERITE("netherite", 5);

    private static final Codec<UpgradeDefaultVariant> CODEC = StringRepresentable.fromEnum(UpgradeDefaultVariant::values);

    private final int level;
    private final String name;

    UpgradeDefaultVariant(String name, int level){
        this.level = level;
        this.name = name;
    }

    public int level() {
        return level;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }

    @Override
    public Codec<UpgradeDefaultVariant> codec() {
        return CODEC;
    }
}
