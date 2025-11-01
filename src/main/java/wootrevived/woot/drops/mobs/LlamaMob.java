package wootrevived.woot.drops.mobs;

import com.google.common.base.CaseFormat;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Llama;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class LlamaMob extends WootFactoryMob<Llama> {
    public LlamaMob(EntityType<Llama> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public MutableComponent getDisplayName(CompoundTag mobTag, RegistryAccess registryAccess) {
        Llama.Variant variant = mobTag.read("Variant", Llama.Variant.LEGACY_CODEC).orElse(Llama.Variant.DEFAULT);
        MutableComponent tip = Component.literal(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, variant.getSerializedName()).replaceAll("([a-z])([A-Z])", "$1 $2") + " ");
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
        tag.store("Variant", Llama.Variant.LEGACY_CODEC, mobTag.read("Variant", Llama.Variant.LEGACY_CODEC).orElse(Llama.Variant.DEFAULT));
        return tag;
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new LlamaMob(EntityType.LLAMA, new Properties()));
    }
}
