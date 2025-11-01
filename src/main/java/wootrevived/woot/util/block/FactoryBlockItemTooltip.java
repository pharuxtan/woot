package wootrevived.woot.util.block;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.block.Block;
import wootrevived.woot.blocks.factory.FactoryBlockItem;

import java.util.function.Consumer;

public class FactoryBlockItemTooltip<T extends TooltipProvider> extends FactoryBlockItem {
    private final DataComponentType<T> type;

    public FactoryBlockItemTooltip(Block block, DataComponentType<T> type, Properties properties) {
        super(block, properties);
        this.type = type;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(ItemStack stack, TooltipContext ctx, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag tooltipFlag){
        if(display.shows(type))
            components().get(type).addToTooltip(ctx, consumer, tooltipFlag, stack.getComponents());
    }
}
