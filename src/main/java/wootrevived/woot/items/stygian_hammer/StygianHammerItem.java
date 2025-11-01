package wootrevived.woot.items.stygian_hammer;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import wootrevived.woot.Woot;

public class StygianHammerItem extends Item {
    public StygianHammerItem(String tag) {
        super(new Item.Properties().stacksTo(1)
                .setId(ResourceKey.create(Registries.ITEM, Woot.location(tag))));
    }

    @Override
    public ItemStack getCraftingRemainder(ItemStack itemStack) {
        return itemStack.copy();
    }
}
