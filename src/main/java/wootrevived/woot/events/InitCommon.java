package wootrevived.woot.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.WootFactoryMobsRegistry;

@EventBusSubscriber(modid = Woot.MOD_NAMESPACE)
public class InitCommon {
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event){
        WootFactoryMobsRegistry.register();
    }
}
