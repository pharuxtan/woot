package wootrevived.woot.drops.mobs;

import com.google.common.base.CaseFormat;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.TropicalFish;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class TropicalFishMob extends WootFactoryMob<TropicalFish> {
    public TropicalFishMob(EntityType<TropicalFish> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public MutableComponent getDisplayName(CompoundTag mobTag, RegistryAccess registryAccess) {
        TropicalFish.Variant variant = mobTag.read("Variant", TropicalFish.Variant.CODEC).orElse(TropicalFish.DEFAULT_VARIANT);
        MutableComponent tip = Component.literal(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, variant.pattern().getSerializedName()).replaceAll("([a-z])([A-Z])", "$1 $2") + " ");
        return tip.append(super.getDisplayName(mobTag, registryAccess));
    }

    @Override
    public MutableComponent getTooltipKillName(CompoundTag mobTag, RegistryAccess registryAccess) {
        return super.getDisplayName(mobTag, registryAccess);
    }

    @Override
    public CompoundTag saveTag(CompoundTag mobTag, RegistryAccess registryAccess){
        CompoundTag tag = super.saveTag(mobTag, registryAccess);
        tag.store("Variant", TropicalFish.Variant.CODEC, mobTag.read("Variant", TropicalFish.Variant.CODEC).orElse(TropicalFish.DEFAULT_VARIANT));
        return tag;
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new TropicalFishMob(EntityType.TROPICAL_FISH, new Properties()));
    }
}
