package wootrevived.woot.compat.jei.subtypes;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootUpgradeItem;

public class UpgradeSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    public static final UpgradeSubtypeInterpreter INSTANCE = new UpgradeSubtypeInterpreter();

    @Override
    public @NotNull String apply(@NotNull ItemStack stack, @NotNull UidContext context) {
        if(!(stack.getItem() instanceof WootUpgradeItem<?> item))
            return "";

        CompoundTag itemTag = stack.getTag();
        if(itemTag == null || !itemTag.contains(WootUpgradeItem.VARIANT_TAG)) return "";
        return item.getVariant(itemTag).getSerializedName();
    }
}
