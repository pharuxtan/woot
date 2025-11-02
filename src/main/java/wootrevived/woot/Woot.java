package wootrevived.woot;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ProblemReporter;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import wootrevived.woot.guide.WootGuide;
import wootrevived.woot.init.CommonConfig;
import wootrevived.woot.init.Registry;
import wootrevived.woot.init.WootPlugins;

@Mod(Woot.MOD_ID)
public class Woot
{
    public static final String MOD_ID = "woot_revived";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Woot(ModContainer container, IEventBus bus)
    {
        CommonConfig.init(container);

        WootPlugins.registerPlugins();

        Registry.register(bus);

        WootGuide.init();
    }

    public static @NotNull ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(Woot.MOD_ID, path);
    }

    public static ProblemReporter.ScopedCollector reporter(String path){
        return new ProblemReporter.ScopedCollector(new WootPathElement(path), LOGGER);
    }

    record WootPathElement(String path) implements ProblemReporter.PathElement {
        public String get() {
            return path;
        }
    }
}
