package wootrevived.woot.upgrades;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.interfaces.WootSpawnProperties;
import wootrevived.api.registrations.WootUpgradeItemRegistration;
import wootrevived.woot.Woot;

import javax.annotation.Nullable;
import java.util.List;

import static wootrevived.woot.util.render.WootStyles.DESCRIPTION_STYLE;

public class Dimension extends WootUpgradeItem {
    private final ResourceKey<Level> dimension;

    public Dimension(ResourceKey<Level> dimension){
        super(new Properties(), 1);
        this.dimension = dimension;
    }

    @Override
    public void applySpawnProperties(WootSpawnProperties properties, CompoundTag itemTag) {
        properties.setDimension(dimension);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag flag) {
        if(dimension == Level.NETHER){
            tooltip.add(Component.translatable("info.woot_revived.upgrade.dimension.desc.nether").setStyle(DESCRIPTION_STYLE));
        } else {
            tooltip.add(Component.translatable("info.woot_revived.upgrade.dimension.desc.end").setStyle(DESCRIPTION_STYLE));
        }
    }

    /* Upgrade Item registration */

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.Keys.ITEMS, Woot.MOD_ID);

    public static void register(WootUpgradeItemRegistration registration){
        ITEMS.register(registration.getWootEventBus());
        registration.register(NETHER_DIMENSION_ITEM);
        registration.register(END_DIMENSION_ITEM);
    }

    public static final String NETHER_DIMENSION_TAG = "nether_dimension_upgrade";
    public static final RegistryObject<Dimension> NETHER_DIMENSION_ITEM = ITEMS.register(NETHER_DIMENSION_TAG, () -> new Dimension(Level.NETHER));

    public static final String END_DIMENSION_TAG = "end_dimension_upgrade";
    public static final RegistryObject<Dimension> END_DIMENSION_ITEM = ITEMS.register(END_DIMENSION_TAG, () -> new Dimension(Level.END));
}
