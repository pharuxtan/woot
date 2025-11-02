package wootrevived.woot.drops.mobs;

import com.google.common.base.CaseFormat;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.variant.VariantUtils;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class PigMob extends WootFactoryMob<Pig> {
    public PigMob(EntityType<Pig> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public MutableComponent getDisplayName(CompoundTag mobTag, RegistryAccess registryAccess) {
        MutableComponent tip = Component.empty();
        VariantUtils.readVariant(mobTag, registryAccess, Registries.PIG_VARIANT).flatMap(Holder::unwrapKey).ifPresent(key -> {
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
        VariantUtils.readVariant(mobTag, registryAccess, Registries.PIG_VARIANT).ifPresent(variant -> VariantUtils.writeVariant(tag, variant));
        return tag;
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new PigMob(EntityType.PIG, new Properties()));
    }
}
