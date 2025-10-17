package wootrevived.woot.drops.mobs;

import net.minecraft.world.entity.EntityType;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class VanillaMobs {
    public static void register(WootFactoryMobRegistration registration) {
        /* Tier 2 */

        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.CAVE_SPIDER, new WootFactoryMob.Properties().tier(Tier.TIER_2)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.SPIDER, new WootFactoryMob.Properties().tier(Tier.TIER_2)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.STRAY, new WootFactoryMob.Properties().tier(Tier.TIER_2)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.ZOMBIE, new WootFactoryMob.Properties().tier(Tier.TIER_2)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.GIANT, new WootFactoryMob.Properties().tier(Tier.TIER_2)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.SKELETON, new WootFactoryMob.Properties().tier(Tier.TIER_2)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.DROWNED, new WootFactoryMob.Properties().tier(Tier.TIER_2)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.ZOGLIN, new WootFactoryMob.Properties().tier(Tier.TIER_2)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.ZOMBIFIED_PIGLIN, new WootFactoryMob.Properties().tier(Tier.TIER_2)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.ELDER_GUARDIAN, new WootFactoryMob.Properties().tier(Tier.TIER_2)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.HUSK, new WootFactoryMob.Properties().tier(Tier.TIER_2)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.SNOW_GOLEM, new WootFactoryMob.Properties().tier(Tier.TIER_2)));

        /* Tier 3 */

        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.RAVAGER, new WootFactoryMob.Properties().tier(Tier.TIER_3)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.HOGLIN, new WootFactoryMob.Properties().tier(Tier.TIER_3)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.STRIDER, new WootFactoryMob.Properties().tier(Tier.TIER_3)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.PIGLIN, new WootFactoryMob.Properties().tier(Tier.TIER_3)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.PIGLIN_BRUTE, new WootFactoryMob.Properties().tier(Tier.TIER_3)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.BLAZE, new WootFactoryMob.Properties().tier(Tier.TIER_3)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.IRON_GOLEM, new WootFactoryMob.Properties().tier(Tier.TIER_3)));

        /* Tier 4 */

        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.GHAST, new WootFactoryMob.Properties().tier(Tier.TIER_4)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.SHULKER, new WootFactoryMob.Properties().tier(Tier.TIER_4)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.ENDERMAN, new WootFactoryMob.Properties().tier(Tier.TIER_4)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.WITHER_SKELETON, new WootFactoryMob.Properties().tier(Tier.TIER_4)));

        /* Tier 5 */

        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.EVOKER, new WootFactoryMob.Properties().tier(Tier.TIER_5)));
        registration.registerFactoryMob(new WootFactoryMob<>(EntityType.WARDEN, new WootFactoryMob.Properties().tier(Tier.TIER_5)));
    }
}
