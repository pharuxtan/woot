package wootrevived.woot.drops.mobs;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class VillagerMob extends WootFactoryMob<Villager> {
    public VillagerMob(EntityType<Villager> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public MutableComponent getDisplayName(CompoundTag mobTag, RegistryAccess registryAccess) {
        MutableComponent tip = Component.empty();
        VillagerData data = mobTag.read("VillagerData", VillagerData.CODEC).orElseGet(Villager::createDefaultVillagerData);
        if(!data.profession().is(VillagerProfession.NONE)){
            tip.append(data.profession().value().name());
            tip.append(" ");
        }
        return tip.append(super.getDisplayName(mobTag, registryAccess));
    }

    @Override
    public MutableComponent getTooltipKillName(CompoundTag mobTag, RegistryAccess registryAccess) {
        return super.getDisplayName(mobTag, registryAccess);
    }

    @Override
    public CompoundTag saveTag(CompoundTag mobTag, RegistryAccess registryAccess){
        CompoundTag tag = super.saveTag(mobTag, registryAccess);
        tag.store("VillagerData", VillagerData.CODEC, mobTag.read("VillagerData", VillagerData.CODEC).orElseGet(Villager::createDefaultVillagerData));
        return tag;
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new VillagerMob(EntityType.VILLAGER, new Properties()));
    }
}