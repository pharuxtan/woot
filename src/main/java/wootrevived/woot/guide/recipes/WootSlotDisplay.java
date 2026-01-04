package wootrevived.woot.guide.recipes;

import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.DisplayContentsFactory;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;
import java.util.stream.Stream;

public class WootSlotDisplay implements SlotDisplay {
    private final List<ItemStack> stacks;

    public WootSlotDisplay(List<ItemStack> stacks) {
        this.stacks = stacks;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Stream<T> resolve(ContextMap contextMap, DisplayContentsFactory<T> displayContentsFactory) {
        return (Stream<T>) stacks.stream();
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public Type<WootSlotDisplay> type() { return null; }
}
