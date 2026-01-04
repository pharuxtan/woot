package wootrevived.woot.events.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import wootrevived.woot.Woot;

@EventBusSubscriber(modid = Woot.MOD_NAMESPACE, value = { Dist.CLIENT })
public class GlobalClientTicker {
    public static int tickCounter = 0;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        tickCounter++;
    }
}
