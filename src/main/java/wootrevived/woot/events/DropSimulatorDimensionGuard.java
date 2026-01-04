package wootrevived.woot.events;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import wootrevived.woot.Woot;
import wootrevived.woot.drops.simulator.DropSimulatorDimension;

import java.util.Set;

@EventBusSubscriber(modid = Woot.MOD_NAMESPACE)
public class DropSimulatorDimensionGuard {
    @SubscribeEvent
    public static void onDimensionChange(EntityTravelToDimensionEvent event){
        ResourceKey<Level> dimension = event.getDimension();
        if(DropSimulatorDimension.DROP_SIMULATOR_LEVEL == dimension){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event){
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        if(DropSimulatorDimension.DROP_SIMULATOR_LEVEL == player.level().dimension()){
            ServerLevel overworld = player.level().getServer().overworld();
            BlockPos respawnPos = overworld.getRespawnData().pos();
            player.teleportTo(overworld, respawnPos.getX(), respawnPos.getY(), respawnPos.getZ(), Set.of(), player.getYRot(), player.getXRot(), true);
        }
    }
}
