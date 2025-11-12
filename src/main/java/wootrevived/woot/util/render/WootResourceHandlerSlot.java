package wootrevived.woot.util.render;

import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class WootResourceHandlerSlot extends ResourceHandlerSlot {
    private final Type type;
    private boolean isActive = true;

    public WootResourceHandlerSlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, Type type, int index, int xPosition, int yPosition) {
        super(handler, slotModifier, index, xPosition, yPosition);
        this.type = type;
    }

    public void setActive(boolean isActive){
        this.isActive = isActive;
    }

    public Type getType(){
        return type;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean isHighlightable() {
        return isActive;
    }

    public enum Type {
        INVENTORY,
        HEART_INPUTS
    }
}
