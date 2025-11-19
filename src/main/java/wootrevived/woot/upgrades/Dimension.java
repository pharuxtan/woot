package wootrevived.woot.upgrades;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.interfaces.WootSpawnProperties;
import wootrevived.api.interfaces.WootUpgradeEnum;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;

import javax.annotation.Nullable;
import java.util.List;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class Dimension extends WootUpgradeItem<Dimension.Variant> {
    public Dimension(Variant variant){
        super(new Properties(), variant);
    }

    @Override
    public void applySpawnProperties(@NotNull WootSpawnProperties properties, @NotNull CompoundTag itemTag) {
        properties.setDimension(getVariant(itemTag).dimension());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        if(getVariant(stack.getTag()) == Variant.NETHER){
            tooltip.add(Component.translatable("info.woot_revived.upgrade.dimension.desc.nether").setStyle(DESCRIPTION_STYLE));
        } else {
            tooltip.add(Component.translatable("info.woot_revived.upgrade.dimension.desc.end").setStyle(DESCRIPTION_STYLE));
        }
    }

    public enum Variant implements WootUpgradeEnum<Variant> {
        NETHER("nether", Level.NETHER),
        END("end", Level.END);

        private static final Codec<Variant> CODEC = StringRepresentable.fromEnum(Variant::values);

        private final String name;
        private final ResourceKey<Level> dimension;

        Variant(String name, ResourceKey<Level> dimension){
            this.name = name;
            this.dimension = dimension;
        }

        public ResourceKey<Level> dimension() {
            return dimension;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }

        @Override
        public Codec<Variant> codec() {
            return CODEC;
        }
    }

    /* Upgrade Item registration */

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Woot.MOD_ID);

    public static void register(WootUpgradeItemRegistration registration){
        ITEMS.register(registration.getWootEventBus());
        registration.register(NETHER_DIMENSION_ITEM);
        registration.register(END_DIMENSION_ITEM);
    }

    public static final String NETHER_DIMENSION_TAG = "nether_dimension_upgrade";
    public static final DeferredHolder<Item, Dimension> NETHER_DIMENSION_ITEM = ITEMS.register(NETHER_DIMENSION_TAG, () -> new Dimension(Variant.NETHER));

    public static final String END_DIMENSION_TAG = "end_dimension_upgrade";
    public static final DeferredHolder<Item, Dimension> END_DIMENSION_ITEM = ITEMS.register(END_DIMENSION_TAG, () -> new Dimension(Variant.END));
}
