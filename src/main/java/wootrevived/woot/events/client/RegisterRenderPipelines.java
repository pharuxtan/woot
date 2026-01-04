package wootrevived.woot.events.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import wootrevived.woot.Woot;
import wootrevived.woot.util.render.WootContainerScreen;

@EventBusSubscriber(modid = Woot.MOD_NAMESPACE, value = { Dist.CLIENT })
public class RegisterRenderPipelines {
    @SubscribeEvent
    public static void registerRenderPipelines(RegisterRenderPipelinesEvent event){
        event.registerPipeline(WootContainerScreen.GUI_TEXTURED_OVERLAY);
    }
}
