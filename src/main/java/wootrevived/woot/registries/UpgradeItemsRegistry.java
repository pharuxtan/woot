package wootrevived.woot.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;
import wootrevived.woot.init.Registry;
import wootrevived.woot.init.WootPlugins;
import wootrevived.woot.items.basic.BasicItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UpgradeItemsRegistry extends WootUpgradeItemRegistration {
    /* Woot Items */

    private final IEventBus wootBus;

    private UpgradeItemsRegistry(IEventBus wootBus){
        this.wootBus = wootBus;
    }

    private static final Map<String, DeferredHolder<Item, ? extends WootUpgradeItem>> REGISTRY = new HashMap<>();

    @Override
    public void register(DeferredHolder<Item, ? extends WootUpgradeItem> item) {
        REGISTRY.put(getNameFromItem(item), item);
        Registry.addToCreativeTab(item);
    }

    @Override
    public IEventBus getWootEventBus() {
        return wootBus;
    }

    public static DeferredHolder<Item, ? extends WootUpgradeItem> get(String name){
        return REGISTRY.get(name);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean has(String name){
        return REGISTRY.containsKey(name);
    }

    public static Collection<DeferredHolder<Item, ? extends WootUpgradeItem>> getValues(){
        return REGISTRY.values();
    }

    public static String getNameFromItem(DeferredHolder<Item, ? extends WootUpgradeItem> item){
        return item.getId().toString().replaceAll("[^a-zA-Z0-9_]", "_");
    }

    public static String getNameFromItem(WootUpgradeItem item){
        return BuiltInRegistries.ITEM.getKey(item).toString().replaceAll("[^a-zA-Z0-9_]", "_");
    }

    /* Forge Items */

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Woot.MOD_ID);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        Registry.addToCreativeTab(UPGRADE_BASE_ITEM);

        WootPlugins.registerUpgradeItems(new UpgradeItemsRegistry(bus));
    }

    /* Upgrade Base */

    public static final String UPGRADE_BASE_TAG = "upgrade_base";
    public static final DeferredHolder<Item, BasicItem> UPGRADE_BASE_ITEM = ITEMS.register(UPGRADE_BASE_TAG, () -> new BasicItem(BasicItem.Type.UPGRADE_BASE, UPGRADE_BASE_TAG));
}
