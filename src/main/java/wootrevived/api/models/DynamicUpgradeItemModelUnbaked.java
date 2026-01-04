package wootrevived.api.models;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.item.ItemModel;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

public class DynamicUpgradeItemModelUnbaked implements ItemModel.Unbaked {
    public static String ID = "upgrade_item";

    public static final MapCodec<DynamicUpgradeItemModelUnbaked> TYPE = MapCodec.unit(new DynamicUpgradeItemModelUnbaked());

    @ApiStatus.Internal
    public static Function<ItemModel.BakingContext, ItemModel> BAKER;

    @Override
    public @NonNull MapCodec<? extends ItemModel.Unbaked> type() {
        return TYPE;
    }

    @Override
    public @NonNull ItemModel bake(ItemModel.@NonNull BakingContext bakingContext) {
        return BAKER.apply(bakingContext);
    }

    @Override
    public void resolveDependencies(@NonNull Resolver resolver) {
    }
}
