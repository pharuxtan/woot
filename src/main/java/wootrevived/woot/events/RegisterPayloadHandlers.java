package wootrevived.woot.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import wootrevived.woot.Woot;
import wootrevived.woot.network.WootFakeSpawnerUpdate;
import wootrevived.woot.network.WootMachineUpdate;

@EventBusSubscriber(modid = Woot.MOD_NAMESPACE)
public class RegisterPayloadHandlers {
    @SubscribeEvent
    public static void registerPayloadHandler(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(Woot.MOD_NAMESPACE).versioned("1").optional();

        registrar.playToServer(
                WootMachineUpdate.TYPE,
                WootMachineUpdate.STREAM_CODEC,
                WootMachineUpdate::handler
        );

        registrar.playToServer(
                WootFakeSpawnerUpdate.TYPE,
                WootFakeSpawnerUpdate.STREAM_CODEC,
                WootFakeSpawnerUpdate::handler
        );
    }
}
