package wootrevived.woot.events.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterBlockStateModels;
import net.neoforged.neoforge.client.event.RegisterItemModelsEvent;
import wootrevived.api.models.DynamicUpgradeItemModelUnbaked;
import wootrevived.woot.Woot;
import wootrevived.woot.client.model.factory_upgrade.FactoryUpgradeBlockBaseModel;

@EventBusSubscriber(modid = Woot.MOD_ID, value = { Dist.CLIENT })
public class RegisterModelLoaders {
    @SubscribeEvent
    public static void registerBlockStateModels(RegisterBlockStateModels event) {
        event.registerModel(FactoryUpgradeBlockBaseModel.ID, FactoryUpgradeBlockBaseModel.CODEC);
    }

    @SubscribeEvent
    static void registerItemModels(RegisterItemModelsEvent event) {
        event.register(Woot.location(DynamicUpgradeItemModelUnbaked.ID), DynamicUpgradeItemModelUnbaked.TYPE);
    }
}
