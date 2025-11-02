package wootrevived.woot.init;

import wootrevived.api.IWootPlugin;
import wootrevived.api.registrations.WootFactoryMobRegistration;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.drops.mobs.*;
import wootrevived.woot.upgrades.*;

public class WootRevivedPlugin implements IWootPlugin {
    @Override
    public void registerUpgradeItems(WootUpgradeItemRegistration registration) {
        Burn.register(registration);
        Decapitate.register(registration);
        Dimension.register(registration);
        Efficiency.register(registration);
        Looting.register(registration);
        Mass.register(registration);
        Rate.register(registration);
        ShardDrop.register(registration);
        Xp.register(registration);
    }

    @Override
    public void registerFactoryMobs(WootFactoryMobRegistration registration) {
        AxolotlMob.register(registration);
        BlacklistedMobs.register(registration);
        CatMob.register(registration);
        ChickenMob.register(registration);
        CowMob.register(registration);
        CreeperMob.register(registration);
        EnderDragonMob.register(registration);
        FoxMob.register(registration);
        FrogMob.register(registration);
        HorseMob.register(registration);
        LlamaMob.register(registration);
        MagmaCubeMob.register(registration);
        MushroomCowMob.register(registration);
        PandaMob.register(registration);
        ParrotMob.register(registration);
        PigMob.register(registration);
        RabbitMob.register(registration);
        SheepMob.register(registration);
        SlimeMob.register(registration);
        SnifferMob.register(registration);
        TropicalFishMob.register(registration);
        VanillaMobs.register(registration);
        VillagerMob.register(registration);
        WitherMob.register(registration);
        ZombieVillagerMob.register(registration);
    }
}
