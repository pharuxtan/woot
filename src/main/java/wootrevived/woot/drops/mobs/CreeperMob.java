package wootrevived.woot.drops.mobs;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class CreeperMob extends WootFactoryMob<Creeper> {
    public CreeperMob(EntityType<Creeper> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public MutableComponent getDisplayName(CompoundTag mobTag, RegistryAccess registryAccess) {
        MutableComponent tip = Component.empty();
        mobTag.getBoolean("powered").ifPresent(powered -> {
            tip.append("Charged ");
        });
        return tip.append(super.getDisplayName(mobTag, registryAccess));
    }

    @Override
    public CompoundTag saveTag(CompoundTag mobTag, RegistryAccess registryAccess){
        CompoundTag tag = super.saveTag(mobTag, registryAccess);
        mobTag.getBoolean("powered").ifPresent(powered -> tag.putBoolean("powered", powered));
        return tag;
    }

    @Override
    public boolean isSame(CompoundTag shardTag, CompoundTag mobTag, RegistryAccess registryAccess){
        if(!super.isSame(shardTag, mobTag, registryAccess))
            return false;

        boolean isShardPowered = shardTag.getBoolean("powered").orElse(false);
        boolean isMobPowered = mobTag.getBoolean("powered").orElse(false);
        return isShardPowered == isMobPowered;
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new CreeperMob(EntityType.CREEPER, new Properties().tier(Tier.TIER_2)));
    }
}
