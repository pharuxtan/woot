package wootrevived.woot.events;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import wootrevived.woot.Woot;
import wootrevived.woot.config.GuideConfig;
import wootrevived.woot.guide.GuideBookPersistentState;
import wootrevived.woot.registries.ItemsRegistry;

@EventBusSubscriber(modid = Woot.MOD_ID)
public class GuideBook {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        MinecraftServer server = player.level().getServer();
        if(server == null)
            return;

        if(GuideConfig.GIVE_ON_SPAWN.get()){
            GuideBookPersistentState state = GuideBookPersistentState.get(player.level().getServer());
            if(!state.hasPlayerReceivedGuideBook(player) && player.getInventory().add(ItemsRegistry.GUIDE_BOOK_ITEM.get().getDefaultInstance())){
                state.addPlayerReceivedGuideBook(player);
            }
        }
    }
}
