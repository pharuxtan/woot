package wootrevived.woot.guide.recipes;

import guideme.document.LytRect;
import guideme.document.block.LytBox;
import guideme.document.block.LytSlot;
import guideme.layout.LayoutContext;
import guideme.render.RenderContext;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.fluids.FluidStack;
import wootrevived.woot.config.EnchantedLiquifierConfig;
import wootrevived.woot.recipes.enchanted_liquifier.EnchantedLiquifierRecipe;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.util.render.WootContainerScreen;
import wootrevived.woot.util.render.guide.LytEnergy;
import wootrevived.woot.util.render.guide.LytFluid;

import java.util.ArrayList;
import java.util.List;

public class LytEnchantedLiquifierRecipe extends LytBox {
    private static final int GUI_WIDTH = 95;
    private static final int GUI_HEIGHT = 66;

    private static final int ENERGY_X = 5;
    private static final int ENERGY_Y = 5;

    private static final int INPUT_SLOT_X = 26;
    private static final int INPUT_SLOT_Y = 24;

    private static final int OUTPUT_FLUID_X = 72;
    private static final int OUTPUT_FLUID_Y = 5;

    private static final int PROGRESS_X = 47;
    private static final int PROGRESS_Y = 25;

    private final LytEnergy energy;
    private final LytSlot inputSlot;
    private final LytFluid outputFluid;

    public LytEnchantedLiquifierRecipe(int enchantLevel){
        List<ItemStack> items = new ArrayList<>();
        for(Enchantment enchantment : EnchantedLiquifierRecipe.enchantments){
            if(enchantLevel > enchantment.getMaxLevel() || enchantLevel < enchantment.getMinLevel())
                continue;

            ItemStack itemStack = Items.ENCHANTED_BOOK.getDefaultInstance();
            itemStack.enchant(Holder.direct(enchantment), enchantLevel);
            items.add(itemStack);
        }

        int energyAmount = enchantLevel * EnchantedLiquifierConfig.PER_ENCHANT_ENERGY.get();
        int fluidAmount = enchantLevel * EnchantedLiquifierConfig.PER_ENCHANT_FLUID.get();

        append(energy = new LytEnergy(energyAmount, EnchantedLiquifierConfig.ENERGY_CAPACITY.get()));
        append(inputSlot = new LytSlot(new WootSlotDisplay(items)));
        append(outputFluid = new LytFluid(new FluidStack(FluidsRegistry.SOURCE_ENCHANTED_FLUID.get(), fluidAmount), EnchantedLiquifierConfig.OUTPUT_TANK_CAPACITY.get()));
    }

    @Override
    protected LytRect computeBoxLayout(LayoutContext context, int x, int y, int availableWidth) {
        energy.layout(context, x + ENERGY_X, y + ENERGY_Y, availableWidth);
        inputSlot.layout(context, x + INPUT_SLOT_X, y + INPUT_SLOT_Y, availableWidth);
        outputFluid.layout(context, x + OUTPUT_FLUID_X, y + OUTPUT_FLUID_Y, availableWidth);

        return new LytRect(x, y, GUI_WIDTH, GUI_HEIGHT);
    }

    @Override
    public void render(RenderContext context) {
        context.renderPanel(bounds);

        WootContainerScreen.renderProgressArrowBg(context.guiGraphics(), bounds.x() + PROGRESS_X, bounds.y() + PROGRESS_Y);

        super.render(context);
    }
}
