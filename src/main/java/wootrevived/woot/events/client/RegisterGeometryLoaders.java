package wootrevived.woot.events.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ModelEvent;
import wootrevived.api.models.DynamicUpgradeItemModelBuilder;
import wootrevived.woot.Woot;
import wootrevived.woot.client.model.factory_upgrade.FactoryUpgradeUnbakedModel;
import wootrevived.woot.client.model.upgrade_item.DynamicUpgradeItemModel;
import wootrevived.woot.registries.BlocksRegistry;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Woot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = { Dist.CLIENT })
public class RegisterGeometryLoaders {
    @SubscribeEvent
    public static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register(Woot.location(BlocksRegistry.FACTORY_UPGRADE_TAG), FactoryUpgradeUnbakedModel.Loader.INSTANCE);
        event.register(Woot.location(DynamicUpgradeItemModelBuilder.ID), DynamicUpgradeItemModel.Loader.INSTANCE);
    }
}
