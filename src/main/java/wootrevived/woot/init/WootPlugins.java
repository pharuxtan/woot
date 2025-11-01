package wootrevived.woot.init;

import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;
import wootrevived.api.IWootPlugin;
import wootrevived.api.WootPlugin;
import wootrevived.api.registrations.WootFactoryMobRegistration;
import wootrevived.api.registrations.WootUpgradeItemRegistration;

import java.lang.reflect.Constructor;
import java.util.*;

public class WootPlugins {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final WootRevivedPlugin wootInternalPlugin = new WootRevivedPlugin();

    private static List<IWootPlugin> plugins;

    public static void registerPlugins(){
        plugins = getInstances(WootPlugin.class, IWootPlugin.class);
    }

    public static void registerUpgradeItems(WootUpgradeItemRegistration registration){
        wootInternalPlugin.registerUpgradeItems(registration);
        for(IWootPlugin plugin : plugins){
            plugin.registerUpgradeItems(registration);
        }
    }

    public static void registerFactoryMobs(WootFactoryMobRegistration registration){
        wootInternalPlugin.registerFactoryMobs(registration);
        for(IWootPlugin plugin : plugins){
            plugin.registerFactoryMobs(registration);
        }
    }

    private static <T> List<T> getInstances(Class<?> annotationClass, Class<T> instanceClass){
        Type annotationType = Type.getType(annotationClass);
        List<ModFileScanData> allScanData = ModList.get().getAllScanData();
        Set<String> pluginClassNames = new LinkedHashSet<>();
        for(ModFileScanData scanData : allScanData){
            Iterable<ModFileScanData.AnnotationData> annotations = scanData.getAnnotations();
            for(ModFileScanData.AnnotationData annotation : annotations){
                if(Objects.equals(annotation.annotationType(), annotationType)){
                    String memberName = annotation.memberName();
                    pluginClassNames.add(memberName);
                }
            }
        }

        List<T> instances = new ArrayList<>();
        for(String pluginClassName : pluginClassNames){
            try {
                Class<?> asmClass = Class.forName(pluginClassName);
                Class<? extends T> asmInstanceClass = asmClass.asSubclass(instanceClass);
                Constructor<? extends T> constructor = asmInstanceClass.getDeclaredConstructor();
                T instance = constructor.newInstance();
                instances.add(instance);
            } catch(ReflectiveOperationException | LinkageError e) {
                LOGGER.error("Failed to load: {}", pluginClassName, e);
            }
        }
        return instances;
    }
}
