package wootrevived.woot.events.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterSpriteSourcesEvent;
import wootrevived.api.models.DynamicUpgradeItemModelUnbaked;
import wootrevived.woot.Woot;
import wootrevived.woot.client.sprite.FactoryUpgradeDynamicSpriteSource;
import wootrevived.woot.client.sprite.UpgradeItemDynamicSpriteSource;
import wootrevived.woot.registries.BlocksRegistry;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = Woot.MOD_ID, value = { Dist.CLIENT })
public class RegisterSpriteSources {
    @SubscribeEvent
    public static void registerSpriteSourceTypes(RegisterSpriteSourcesEvent event){
        event.register(Woot.location(DynamicUpgradeItemModelUnbaked.ID), UpgradeItemDynamicSpriteSource.CODEC);
        event.register(Woot.location(BlocksRegistry.FACTORY_UPGRADE_TAG), FactoryUpgradeDynamicSpriteSource.CODEC);
    }
}
