package wootrevived.woot.items.mold;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import wootrevived.woot.Woot;

/**
 * These items are NEVER consumed in recipes
 */
public class MoldItem extends Item {

    final MoldType moldType;
    public MoldItem(MoldType moldType, String tag) {
        super(new Item.Properties().stacksTo(1)
                .setId(ResourceKey.create(Registries.ITEM, Woot.identifier(tag))));
        this.moldType = moldType;
    }

    @Override
    public ItemStack getCraftingRemainder(ItemStack itemStack) {
        return itemStack.copy();
    }

    public enum MoldType {
        PLATE,
        SHARD,
        DYE_CASING
    }
}
