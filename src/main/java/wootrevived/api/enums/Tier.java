package wootrevived.api.enums;

import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

import java.util.EnumSet;
import java.util.Locale;

/**
 * Factory tier levels used by Woot.
 * <p>
 * Each tier represents both the multiblock structure required
 * and the mobs that can be simulated within it:
 * <ul>
 *   <li>{@link #TIER_1} – Copper</li>
 *   <li>{@link #TIER_2} – Iron</li>
 *   <li>{@link #TIER_3} – Gold</li>
 *   <li>{@link #TIER_4} – Diamond</li>
 *   <li>{@link #TIER_5} – Netherite</li>
 * </ul>
 * {@link #INVALID} is used as a sentinel value.
 */
public enum Tier implements StringRepresentable {
    INVALID,
    TIER_1,
    TIER_2,
    TIER_3,
    TIER_4,
    TIER_5;

    /**
     * Returns the default vitality cost (in mB) required
     * for this tier's simulation.
     *
     * @return the default vitality cost, or {@code 0} for {@link #INVALID}
     */
    public int defaultVitalityCost(){
        return switch(this){
            case TIER_1 -> 1500;
            case TIER_2 -> 3500;
            case TIER_3 -> 7500;
            case TIER_4 -> 15000;
            case TIER_5 -> 30000;
            default -> 0;
        };
    }

    private static final EnumSet<Tier> MOB_VALID_FOR_TIER_1 = EnumSet.of(TIER_1);
    private static final EnumSet<Tier> MOB_VALID_FOR_TIER_2 = EnumSet.range(TIER_1, TIER_2);
    private static final EnumSet<Tier> MOB_VALID_FOR_TIER_3 = EnumSet.range(TIER_1, TIER_3);
    private static final EnumSet<Tier> MOB_VALID_FOR_TIER_4 = EnumSet.range(TIER_1, TIER_4);
    private static final EnumSet<Tier> MOB_VALID_FOR_TIER_5 = EnumSet.range(TIER_1, TIER_5);

    /**
     * Checks whether a mob of the given tier can be simulated
     * inside a factory of this tier.
     *
     * @param tier the mob's tier
     * @return {@code true} if this factory tier supports that mob tier
     */
    public boolean isMobTierValid(Tier tier) {
        return switch(this){
            case TIER_1 -> MOB_VALID_FOR_TIER_1.contains(tier);
            case TIER_2 -> MOB_VALID_FOR_TIER_2.contains(tier);
            case TIER_3 -> MOB_VALID_FOR_TIER_3.contains(tier);
            case TIER_4 -> MOB_VALID_FOR_TIER_4.contains(tier);
            case TIER_5 -> MOB_VALID_FOR_TIER_5.contains(tier);
            default -> false;
        };
    }

    private static final EnumSet<Tier> FACTORY_VALID_FOR_TIER_1 = EnumSet.range(Tier.TIER_1, Tier.TIER_5);
    private static final EnumSet<Tier> FACTORY_VALID_FOR_TIER_2 = EnumSet.range(Tier.TIER_2, Tier.TIER_5);
    private static final EnumSet<Tier> FACTORY_VALID_FOR_TIER_3 = EnumSet.range(Tier.TIER_3, Tier.TIER_5);
    private static final EnumSet<Tier> FACTORY_VALID_FOR_TIER_4 = EnumSet.range(Tier.TIER_4, Tier.TIER_5);
    private static final EnumSet<Tier> FACTORY_VALID_FOR_TIER_5 = EnumSet.of(Tier.TIER_5);

    /**
     * Checks whether this mob tier is valid for simulation
     * in the given factory tier.
     *
     * @param tier the factory tier
     * @return {@code true} if the mob tier can run inside that factory
     */
    public boolean isFactoryTierValid(Tier tier) {
        return switch(this){
            case TIER_1 -> FACTORY_VALID_FOR_TIER_1.contains(tier);
            case TIER_2 -> FACTORY_VALID_FOR_TIER_2.contains(tier);
            case TIER_3 -> FACTORY_VALID_FOR_TIER_3.contains(tier);
            case TIER_4 -> FACTORY_VALID_FOR_TIER_4.contains(tier);
            case TIER_5 -> FACTORY_VALID_FOR_TIER_5.contains(tier);
            default -> false;
        };
    }

    /**
     * Returns the serialized name of this tier for use in registries,
     * commands, JSON, etc. This is based on the enum constant's name
     * in lowercase.
     *
     * @return the lowercase identifier for this tier
     */
    @Override
    public @NonNull String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
