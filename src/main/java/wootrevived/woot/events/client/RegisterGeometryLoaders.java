package wootrevived.woot.events.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
        event.register(BlocksRegistry.FACTORY_UPGRADE_TAG, FactoryUpgradeUnbakedModel.Loader.INSTANCE);
        event.register(DynamicUpgradeItemModelBuilder.ID, DynamicUpgradeItemModel.Loader.INSTANCE);
    }
}
