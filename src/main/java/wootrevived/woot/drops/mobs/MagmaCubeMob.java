package wootrevived.woot.drops.mobs;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.MagmaCube;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class MagmaCubeMob extends WootFactoryMob<MagmaCube> {
    public MagmaCubeMob(EntityType<MagmaCube> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public MutableComponent getDisplayName(CompoundTag mobTag, RegistryAccess registryAccess) {
        MutableComponent tip = Component.literal(
                mobTag.getInt("Size").orElse(0) > 0 ?
                        "Large " :
                        "Small "
        );
        return tip.append(super.getDisplayName(mobTag, registryAccess));
    }

    @Override
    public CompoundTag saveTag(CompoundTag mobTag, RegistryAccess registryAccess){
        CompoundTag tag = super.saveTag(mobTag, registryAccess);
        mobTag.getInt("Size").ifPresent(size -> tag.putInt("Size", size));
        return tag;
    }

    @Override
    public boolean isSame(CompoundTag shardTag, CompoundTag mobTag, RegistryAccess registryAccess){
        if(!super.isSame(shardTag, mobTag, registryAccess))
            return false;

        boolean isShardLarge = shardTag.getInt("Size").orElse(0) > 0;
        boolean isMobLarge = mobTag.getInt("Size").orElse(0) > 0;
        return isShardLarge == isMobLarge;
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new MagmaCubeMob(EntityType.MAGMA_CUBE, new Properties().tier(Tier.TIER_3)));
    }
}
