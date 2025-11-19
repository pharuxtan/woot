package wootrevived.woot.client.model.upgrade_item;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeRenderTypes;
import net.minecraftforge.client.RenderTypeGroup;
import net.minecraftforge.client.model.CompositeModel;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import net.minecraftforge.client.model.geometry.UnbakedGeometryHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.interfaces.WootUpgradeEnum;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.UpgradeItemsRegistry;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DynamicUpgradeItemModel implements IUnbakedGeometry<DynamicUpgradeItemModel> {
    private static Map<String, UpgradeItemsRegistry.DynamicEntry<?>> upgradeItems = null;

    private DynamicUpgradeItemModel()
    {
        if(upgradeItems == null)
            upgradeItems = UpgradeItemsRegistry.getDynamicEntries().stream().collect(Collectors.toMap(c -> c.item().getId().toString(), c -> c));
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
        UpgradeItemsRegistry.DynamicEntry<?> entry = upgradeItems.get(modelLocation.getNamespace() + ":" + modelLocation.getPath());

        if(entry == null)
            throw new IllegalStateException("The upgrade item should be dynamic!");

        String name = UpgradeItemsRegistry.getNameFromItem(entry.item());
        if(!UpgradeItemsRegistry.isDynamic(name))
            throw new IllegalStateException("The upgrade item should be dynamic!");

        return bake(entry, context, baker, spriteGetter, modelState, overrides, modelLocation);
    }

    public <T extends Enum<T> & WootUpgradeEnum<T>> BakedModel bake(UpgradeItemsRegistry.DynamicEntry<T> entry, IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
        WootUpgradeItem<T> item = entry.item().get();
        String name = UpgradeItemsRegistry.getNameFromItem(entry.item());

        EnumMap<T, BakedModel> cache = new EnumMap<>(entry.variantClass());
        ItemOverrides upgradeOverrides = new UpgradeItemOverrideHandler<>(cache, item, overrides);

        for(T variant : entry.variantClass().getEnumConstants()) {
            Material material = new Material(
                    InventoryMenu.BLOCK_ATLAS,
                    Woot.location("item/upgrade_item_" + variant.getSerializedName() + "_" + name)
            );
            TextureAtlasSprite sprite = spriteGetter.apply(material);

            CompositeModel.Baked.Builder modelBuilder = CompositeModel.Baked.builder(context, sprite, upgradeOverrides, context.getTransforms());

            var normalRenderTypes = new RenderTypeGroup(RenderType.translucent(), ForgeRenderTypes.ITEM_UNSORTED_TRANSLUCENT.get());
            var unbaked = UnbakedGeometryHelper.createUnbakedItemElements(0, sprite.contents());
            var quads = UnbakedGeometryHelper.bakeElements(unbaked, $ -> sprite, modelState, modelLocation);
            modelBuilder.addQuads(normalRenderTypes, quads);

            cache.put(variant, modelBuilder.build());
        }

        return cache.get(item.getVariant(null));
    }

    private static final class UpgradeItemOverrideHandler<T extends Enum<T> & WootUpgradeEnum<T>> extends ItemOverrides
    {
        private final ItemOverrides nested;
        private final WootUpgradeItem<T> item;
        private final EnumMap<T, BakedModel> cache;

        private UpgradeItemOverrideHandler(EnumMap<T, BakedModel> cache, WootUpgradeItem<T> item, ItemOverrides nested)
        {
            this.nested = nested;
            this.item = item;
            this.cache = cache;
        }

        @Override
        public BakedModel resolve(@NotNull BakedModel originalModel, @NotNull ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed)
        {
            BakedModel overridden = nested.resolve(originalModel, stack, level, entity, seed);
            if (overridden != originalModel) return overridden;

            T variant = item.getVariant(stack.getTag());
            BakedModel model = cache.get(variant);
            return model != null ? model : originalModel;
        }
    }

    public static final class Loader implements IGeometryLoader<DynamicUpgradeItemModel>
    {
        public static final DynamicUpgradeItemModel.Loader INSTANCE = new DynamicUpgradeItemModel.Loader();

        private Loader()
        {
        }

        @Override
        public DynamicUpgradeItemModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext){
            return new DynamicUpgradeItemModel();
        }
    }
}
