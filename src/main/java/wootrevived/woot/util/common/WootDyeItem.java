package wootrevived.woot.util.common;

import net.minecraft.world.item.Item;

public abstract class WootDyeItem extends Item {
    public WootDyeItem(Properties prop) { super(prop); }

    abstract public int getColor();
}
