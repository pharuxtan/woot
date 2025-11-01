package wootrevived.woot.events.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.locale.Language;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import wootrevived.api.WootFactoryMob;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.WootFactoryMobsRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = Woot.MOD_ID, value = { Dist.CLIENT })
public class GuideCacheLivingEntities {
    private static final Map<EntityType<?>, LivingEntity> livingEntities = new HashMap<>();

    public static Map<EntityType<?>, LivingEntity> getLivingEntities() {
        return livingEntities;
    }

    @SubscribeEvent
    public static void onClientLoggingIn(ClientPlayerNetworkEvent.LoggingIn event){
        LocalPlayer player = event.getPlayer();
        Level level = player.level();

        livingEntities.clear();

        for(WootFactoryMob<?> mob : List.copyOf(WootFactoryMobsRegistry.getFactoryMobValues())){
            if(mob.isBlacklisted()) continue;

            EntityType<?> entityType = mob.getEntityType();
            if(!Language.getInstance().has(entityType.getDescriptionId())) {
                WootFactoryMobsRegistry.removeFactoryMob(entityType);
                continue;
            }

            try {
                Entity entity = entityType.create(level, EntitySpawnReason.SPAWNER);
                if(!(entity instanceof LivingEntity livingEntity)) {
                    WootFactoryMobsRegistry.removeFactoryMob(entityType);
                    continue;
                }

                livingEntities.put(entityType, livingEntity);
            } catch(Exception ignored){
                WootFactoryMobsRegistry.removeFactoryMob(entityType);
            }
        }
    }
}
