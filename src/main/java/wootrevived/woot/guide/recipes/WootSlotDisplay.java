package wootrevived.woot.guide.recipes;

import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.DisplayContentsFactory;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public class WootSlotDisplay implements SlotDisplay {
    private final List<ItemStack> stacks;

    public WootSlotDisplay(List<ItemStack> stacks) {
        this.stacks = stacks;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> @NotNull Stream<T> resolve(@NotNull ContextMap contextMap, @NotNull DisplayContentsFactory<T> displayContentsFactory) {
        return (Stream<T>) stacks.stream();
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public @NotNull Type<WootSlotDisplay> type() { return null; }
}
