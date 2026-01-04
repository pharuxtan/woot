package wootrevived.woot.events;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import wootrevived.api.WootFactoryMob;
import wootrevived.woot.Woot;
import wootrevived.woot.drops.simulator.DropSimulator;
import wootrevived.woot.drops.simulator.DropSimulatorDimension;
import wootrevived.woot.registries.WootFactoryMobsRegistry;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Woot.MOD_NAMESPACE)
public class InitDropSimulator {
    public static final List<Identifier> mobLocations = new ArrayList<>();

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        ServerLevel level = event.getServer().getLevel(DropSimulatorDimension.DROP_SIMULATOR_LEVEL);
        if(level != null){
            DropSimulator.init(level);

            mobLocations.clear();

            for(WootFactoryMob<?> mob : List.copyOf(WootFactoryMobsRegistry.getFactoryMobValues())){
                if(mob.isBlacklisted()) continue;

                EntityType<?> entityType = mob.getEntityType();
                if(!Language.getInstance().has(entityType.getDescriptionId())){
                    WootFactoryMobsRegistry.removeFactoryMob(entityType);
                    continue;
                }

                try {
                    Entity entity = entityType.create(level, EntitySpawnReason.SPAWNER);
                    if(!(entity instanceof LivingEntity)) {
                        WootFactoryMobsRegistry.removeFactoryMob(entityType);
                        continue;
                    }

                    Identifier location = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
                    mobLocations.add(location);
                } catch(Exception ignored){
                    WootFactoryMobsRegistry.removeFactoryMob(entityType);
                }
            }
        }
    }
}
