package wootrevived.woot.drops.mobs;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerData;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class ZombieVillagerMob extends WootFactoryMob<ZombieVillager> {
    public ZombieVillagerMob(EntityType<ZombieVillager> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public MutableComponent getDisplayName(ValueInput input) {
        MutableComponent tip = Component.empty();
        VillagerData data = input.read("VillagerData", VillagerData.CODEC).orElseGet(Villager::createDefaultVillagerData);
        if(!data.profession().is(VillagerProfession.NONE)){
            tip.append(data.profession().value().name());
            tip.append(" ");
        }
        return tip.append(super.getDisplayName(input));
    }

    @Override
    public MutableComponent getTooltipKillName(ValueInput input) {
        return super.getDisplayName(input);
    }

    @Override
    public void saveTag(ValueInput input, ValueOutput output){
        super.saveTag(input, output);
        output.store("VillagerData", VillagerData.CODEC, input.read("VillagerData", VillagerData.CODEC).orElseGet(Villager::createDefaultVillagerData));
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new ZombieVillagerMob(EntityType.ZOMBIE_VILLAGER, new Properties().tier(Tier.TIER_2)));
    }
}