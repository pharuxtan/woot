package wootrevived.woot.events.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.FluidsRegistry;

@EventBusSubscriber(modid = Woot.MOD_NAMESPACE, value = { Dist.CLIENT })
public class RegisterFluids {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        ItemBlockRenderTypes.setRenderLayer(FluidsRegistry.SOURCE_VITALITY_FUEL_FLUID.get(), ChunkSectionLayer.TRANSLUCENT);
        ItemBlockRenderTypes.setRenderLayer(FluidsRegistry.FLOWING_VITALITY_FUEL_FLUID.get(), ChunkSectionLayer.TRANSLUCENT);

        ItemBlockRenderTypes.setRenderLayer(FluidsRegistry.SOURCE_PURE_DYE_FLUID.get(), ChunkSectionLayer.TRANSLUCENT);
        ItemBlockRenderTypes.setRenderLayer(FluidsRegistry.FLOWING_PURE_DYE_FLUID.get(), ChunkSectionLayer.TRANSLUCENT);

        ItemBlockRenderTypes.setRenderLayer(FluidsRegistry.SOURCE_ENCHANTED_FLUID.get(), ChunkSectionLayer.TRANSLUCENT);
        ItemBlockRenderTypes.setRenderLayer(FluidsRegistry.FLOWING_ENCHANTED_FLUID.get(), ChunkSectionLayer.TRANSLUCENT);

        ItemBlockRenderTypes.setRenderLayer(FluidsRegistry.SOURCE_MOB_TEARS_FLUID.get(), ChunkSectionLayer.TRANSLUCENT);
        ItemBlockRenderTypes.setRenderLayer(FluidsRegistry.FLOWING_MOB_TEARS_FLUID.get(), ChunkSectionLayer.TRANSLUCENT);
    }

    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(FluidsRegistry.VITALITY_FUEL_FLUID_TYPE.get().EXTENSION, FluidsRegistry.VITALITY_FUEL_FLUID_TYPE.get());
        event.registerFluidType(FluidsRegistry.PURE_DYE_FLUID_TYPE.get().EXTENSION, FluidsRegistry.PURE_DYE_FLUID_TYPE.get());
        event.registerFluidType(FluidsRegistry.ENCHANTED_FLUID_TYPE.get().EXTENSION, FluidsRegistry.ENCHANTED_FLUID_TYPE.get());
        event.registerFluidType(FluidsRegistry.MOB_TEARS_FLUID_TYPE.get().EXTENSION, FluidsRegistry.MOB_TEARS_FLUID_TYPE.get());
    }
}
