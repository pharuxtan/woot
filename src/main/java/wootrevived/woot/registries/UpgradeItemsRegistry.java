package wootrevived.woot.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.interfaces.WootUpgradeEnum;
import wootrevived.api.internal.WootUpgradeComponent;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;
import wootrevived.woot.init.Registry;
import wootrevived.woot.init.WootPlugins;
import wootrevived.woot.items.basic.BasicItem;

import java.util.*;

public class UpgradeItemsRegistry extends WootUpgradeItemRegistration {
    /* Woot Items */

    private final IEventBus wootBus;

    private UpgradeItemsRegistry(IEventBus wootBus){
        this.wootBus = wootBus;
    }

    private static final Map<String, Entry<?>> REGISTRY = new HashMap<>();
    private static final Map<String, Entry<?>> ENTRIES = new HashMap<>();
    private static final Map<String, DynamicEntry<?>> DYNAMIC_ENTRIES = new HashMap<>();

    @Override
    public <T extends Enum<T> & WootUpgradeEnum<T>> void register(DeferredHolder<Item, ? extends WootUpgradeItem<T>> item) {
        register(item, null);
    }

    @Override
    public <T extends Enum<T> & WootUpgradeEnum<T>> void register(DeferredHolder<Item, ? extends WootUpgradeItem<T>> item, Class<T> dynamicVariant) {
        String name = getNameFromItem(item);

        if(dynamicVariant != null && ENTRIES.containsKey(name))
            throw new IllegalStateException("You can't register a dynamic variant item on a same single variant item");

        if(dynamicVariant == null && DYNAMIC_ENTRIES.containsKey(name))
            throw new IllegalStateException("You can't register a single variant item on a same dynamic variant item");

        REGISTRY.put(getNameFromItem(item), new Entry<>(item));

        if(dynamicVariant != null){
            DYNAMIC_ENTRIES.put(getNameFromItem(item), new DynamicEntry<>(item, dynamicVariant));
        } else {
            ENTRIES.put(getNameFromItem(item), new Entry<>(item));
            Registry.addToCreativeTab(item);
        }
    }

    @Override
    public IEventBus getWootEventBus() {
        return wootBus;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<T> & WootUpgradeEnum<T>> DeferredHolder<Item, ? extends WootUpgradeItem<T>> get(String name){
        return (DeferredHolder<Item, ? extends WootUpgradeItem<T>>) REGISTRY.get(name).item();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean has(String name){
        return REGISTRY.containsKey(name);
    }

    public static boolean isDynamic(String name){
        return DYNAMIC_ENTRIES.containsKey(name);
    }

    public static Collection<Entry<?>> getEntries(){
        return ENTRIES.values();
    }

    public static Collection<DynamicEntry<?>> getDynamicEntries(){
        return DYNAMIC_ENTRIES.values();
    }

    public static String getNameFromItem(DeferredHolder<Item, ? extends WootUpgradeItem<?>> item){
        return item.getId().toString().replaceAll("[^a-zA-Z0-9_]", "_");
    }

    public static String getNameFromItem(WootUpgradeItem<?> item){
        return BuiltInRegistries.ITEM.getKey(item).toString().replaceAll("[^a-zA-Z0-9_]", "_");
    }

    public record Entry<T extends Enum<T> & WootUpgradeEnum<T>>(
            DeferredHolder<Item, ? extends WootUpgradeItem<T>> item
    ) {}

    public record DynamicEntry<T extends Enum<T> & WootUpgradeEnum<T>>(
            DeferredHolder<Item, ? extends WootUpgradeItem<T>> item,
            Class<T> variantClass
    ) {}

    /* Forge Items */

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Woot.MOD_NAMESPACE);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        Registry.addToCreativeTab(UPGRADE_BASE_ITEM);

        WootPlugins.registerUpgradeItems(new UpgradeItemsRegistry(bus));
    }

    private static final List<ItemStack> DYNAMIC_CREATIVE_ITEMS = new ArrayList<>();

    public static void displayDynamicCreativeItems(CreativeModeTab.Output output){
        if(DYNAMIC_CREATIVE_ITEMS.isEmpty()){
            for(DynamicEntry<?> entry : DYNAMIC_ENTRIES.values())
                addDynamicCreativeItems(entry);
        }
        DYNAMIC_CREATIVE_ITEMS.forEach(output::accept);
    }

    public static <T extends Enum<T> & WootUpgradeEnum<T>> void addDynamicCreativeItems(DynamicEntry<T> entry){
        for(T constant : entry.variantClass().getEnumConstants()){
            ItemStack stack = entry.item.get().getDefaultInstance();
            stack.set(WootUpgradeComponent.type(), WootUpgradeComponent.of(constant));
            DYNAMIC_CREATIVE_ITEMS.add(stack);
        }
    }

    /* Upgrade Base */

    public static final String UPGRADE_BASE_TAG = "upgrade_base";
    public static final DeferredHolder<Item, BasicItem> UPGRADE_BASE_ITEM = ITEMS.register(UPGRADE_BASE_TAG, () -> new BasicItem(BasicItem.Type.UPGRADE_BASE, UPGRADE_BASE_TAG));
}
