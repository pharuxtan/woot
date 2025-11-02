package wootrevived.woot.events.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterSpriteSourcesEvent;
import wootrevived.woot.Woot;
import wootrevived.woot.client.sprite.factory_upgrade.FactoryUpgradeDynamicSpriteSource;
import wootrevived.woot.registries.BlocksRegistry;

@EventBusSubscriber(modid = Woot.MOD_ID, value = { Dist.CLIENT })
public class RegisterSpriteSources {
    @SubscribeEvent
    public static void registerSpriteSourceTypes(RegisterSpriteSourcesEvent event){
        event.register(Woot.location(BlocksRegistry.FACTORY_UPGRADE_TAG), FactoryUpgradeDynamicSpriteSource.CODEC);
    }
}
