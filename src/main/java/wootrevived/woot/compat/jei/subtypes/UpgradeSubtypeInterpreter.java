package wootrevived.woot.compat.jei.subtypes;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import wootrevived.api.internal.WootUpgradeComponent;

public class UpgradeSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    public static final UpgradeSubtypeInterpreter INSTANCE = new UpgradeSubtypeInterpreter();

    @Override
    public @Nullable Object getSubtypeData(ItemStack stack, UidContext context) {
        WootUpgradeComponent component = stack.get(WootUpgradeComponent.type());
        if(component == null) return null;
        return component.variant().getSerializedName();
    }
}
