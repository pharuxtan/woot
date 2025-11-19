package wootrevived.woot.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Registry {
    public static void register(IEventBus bus){
        BlocksRegistry.register(bus);
        ItemsRegistry.register(bus);
        FluidsRegistry.register(bus);
        UpgradeItemsRegistry.register(bus);
        TABS.register(bus);

        RecipesRegistry.register(bus);
    }

    /* Creative Tab */

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Woot.MOD_ID);
    public static final List<Supplier<? extends ItemLike>> WOOT_TAB_ITEMS = new ArrayList<>();
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> WOOT_TAB = TABS.register("woot_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.woot_revived"))
                    .icon(BlocksRegistry.STYGIAN_ANVIL_BLOCK_ITEM.get()::getDefaultInstance)
                    .displayItems((displayParams, output) -> {
                        WOOT_TAB_ITEMS.forEach(itemLike -> output.accept(itemLike.get()));
                        UpgradeItemsRegistry.displayDynamicCreativeItems(output);
                    })
                    .build());

    public static void addToCreativeTab(DeferredHolder<? extends ItemLike, ? extends ItemLike> itemLike){
        WOOT_TAB_ITEMS.add(itemLike);
    }
}
