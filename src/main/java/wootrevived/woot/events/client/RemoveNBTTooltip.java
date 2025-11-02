package wootrevived.woot.events.client;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.FormattedText;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import wootrevived.woot.Woot;

@EventBusSubscriber(modid = Woot.MOD_ID, value = { Dist.CLIENT })
public class RemoveNBTTooltip {
    @SubscribeEvent
    public static void onGatherTooltip(RenderTooltipEvent.GatherComponents event) {
        String namespace = BuiltInRegistries.ITEM.getKey(event.getItemStack().getItem()).getNamespace();
        if(namespace.equals("woot_revived")){
            event.getTooltipElements().removeIf(either -> either.left().map(FormattedText::getString).orElse("").equals("(+NBT)"));
        }
    }
}
