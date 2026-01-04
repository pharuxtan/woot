package wootrevived.woot.events;

import net.minecraft.commands.Commands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import wootrevived.woot.Woot;
import wootrevived.woot.commands.GiveCommand;

@EventBusSubscriber(modid = Woot.MOD_NAMESPACE)
public class CommandsRegister {
    @SubscribeEvent
    public static void onRegisterCommandsEvent(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("woot")
                        .then(GiveCommand.register())
        );
    }
}
