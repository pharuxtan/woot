package wootrevived.woot.items.basic;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import wootrevived.woot.Woot;

public class BasicItem extends Item {
    final Type itemType;

    public BasicItem(Type itemType, String tag, int stackSize) {
        super(new Properties().stacksTo(stackSize)
                .setId(ResourceKey.create(Registries.ITEM, Woot.identifier(tag))));
        this.itemType = itemType;
    }

    public BasicItem(Type itemType, String tag) { this(itemType, tag, 64); }

    @Override
    public boolean isFoil(ItemStack stack) {
        return itemType == Type.COPPER_ENCHANTED_PLATE ||
               itemType == Type.IRON_ENCHANTED_PLATE ||
               itemType == Type.GOLD_ENCHANTED_PLATE ||
               itemType == Type.DIAMOND_ENCHANTED_PLATE ||
               itemType == Type.NETHERITE_ENCHANTED_PLATE;
    }

    public enum Type {
        STYGIAN_INGOT,
        STYGIAN_DUST,
        STYGIAN_PLATE,
        PRISM,
        COPPER_ENCHANTED_PLATE,
        IRON_ENCHANTED_PLATE,
        GOLD_ENCHANTED_PLATE,
        DIAMOND_ENCHANTED_PLATE,
        NETHERITE_ENCHANTED_PLATE,
        COPPER_SHARD,
        IRON_SHARD,
        GOLD_SHARD,
        DIAMOND_SHARD,
        NETHERITE_SHARD,
        UPGRADE_BASE
    }
}
