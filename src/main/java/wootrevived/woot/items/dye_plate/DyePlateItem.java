package wootrevived.woot.items.dye_plate;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.MapColor;
import wootrevived.woot.Woot;
import wootrevived.woot.util.common.WootDyeItem;

public class DyePlateItem extends WootDyeItem {
    final DyeColor color;

    public DyePlateItem(DyeColor color, String tag) {
        super(new Item.Properties().stacksTo(64)
                .setId(ResourceKey.create(Registries.ITEM, Woot.identifier(tag))));
        this.color = color;
    }

    public int getColor() {
        return color.getMapColor().calculateARGBColor(MapColor.Brightness.HIGH);
    }
}
