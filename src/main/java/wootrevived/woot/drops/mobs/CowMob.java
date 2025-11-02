package wootrevived.woot.drops.mobs;

import com.google.common.base.CaseFormat;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.variant.VariantUtils;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class CowMob extends WootFactoryMob<Cow> {
    public CowMob(EntityType<Cow> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public MutableComponent getDisplayName(CompoundTag mobTag, RegistryAccess registryAccess) {
        MutableComponent tip = Component.empty();
        VariantUtils.readVariant(mobTag, registryAccess, Registries.COW_VARIANT).flatMap(Holder::unwrapKey).ifPresent(key -> {
            tip.append(Component.literal(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, key.location().getPath().replaceAll("([a-z])([A-Z])", "$1 $2") + " ")));
        });
        return tip.append(super.getDisplayName(mobTag, registryAccess));
    }

    @Override
    public MutableComponent getTooltipKillName(CompoundTag mobTag, RegistryAccess registryAccess) {
        return super.getDisplayName(mobTag, registryAccess);
    }

    @Override
    @SuppressWarnings("deprecation")
    public CompoundTag saveTag(CompoundTag mobTag, RegistryAccess registryAccess){
        CompoundTag tag = super.saveTag(mobTag, registryAccess);
        VariantUtils.readVariant(mobTag, registryAccess, Registries.COW_VARIANT).ifPresent(variant -> VariantUtils.writeVariant(tag, variant));
        return tag;
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new CowMob(EntityType.COW, new Properties()));
    }
}
