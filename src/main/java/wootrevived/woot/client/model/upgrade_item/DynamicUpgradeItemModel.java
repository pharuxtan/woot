package wootrevived.woot.client.model.upgrade_item;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.*;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.NeoForgeRenderTypes;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.UnbakedElementsHelper;
import org.jspecify.annotations.Nullable;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.interfaces.WootUpgradeEnum;
import wootrevived.api.models.DynamicUpgradeItemModelUnbaked;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.UpgradeItemsRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DynamicUpgradeItemModel implements ItemModel {
    private static final ModelDebugName DEBUG_NAME = () -> "DynamicUpgradeItemModel";
    private static Map<String, UpgradeItemsRegistry.DynamicEntry<?>> upgradeItems = null;

    private final BakingContext bakingContext;
    private final ItemTransforms itemTransforms;
    private final Map<String, ItemModel> cache = new HashMap<>();

    private DynamicUpgradeItemModel(BakingContext bakingContext) {
        this.bakingContext = bakingContext;
        ResolvedModel baseItemModel = bakingContext.blockModelBaker().getModel(Identifier.withDefaultNamespace("item/generated"));
        if (baseItemModel == null) {
            throw new IllegalStateException("Failed to access item/generated model");
        }
        this.itemTransforms = baseItemModel.getTopTransforms();
        if(upgradeItems == null)
            upgradeItems = UpgradeItemsRegistry.getDynamicEntries().stream().collect(Collectors.toMap(c -> c.item().getId().toString(), c -> c));
    }

    private <T extends Enum<T> & WootUpgradeEnum<T>> String getKey(UpgradeItemsRegistry.DynamicEntry<T> entry, ItemStack stack) {
        WootUpgradeItem<T> item = entry.item().get();
        T variant = item.getVariant(stack);
        String name = UpgradeItemsRegistry.getNameFromItem(entry.item());
        return variant.getSerializedName() + "_" + name;
    }

    private ItemModel bakeModel(String key) {
        SpriteGetter spriteGetter = bakingContext.blockModelBaker().sprites();

        Material material = new Material(
                Sheets.BLOCKS_MAPPER.sheet(),
                Woot.identifier("item/upgrade_item_" + key)
        );

        TextureAtlasSprite sprite = spriteGetter.get(material, DEBUG_NAME);

        var unbaked = UnbakedElementsHelper.createUnbakedItemElements(0, sprite);
        var quads = UnbakedElementsHelper.bakeElements(unbaked, $ -> sprite, BlockModelRotation.IDENTITY);

        ModelRenderProperties renderProperties = new ModelRenderProperties(false, sprite, itemTransforms);
        RenderTypeGroup renderTypeGroup = new RenderTypeGroup(ChunkSectionLayer.TRANSLUCENT, NeoForgeRenderTypes::getUnsortedTranslucent);
        Function<ItemStack, RenderType> renderType = RenderTypeHelper.detectItemModelRenderType(quads, renderTypeGroup);

        return new BlockModelWrapper(List.of(), quads, renderProperties, renderType);
    }

    @Override
    public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver modelResolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner itemOwner, int seed) {
        Identifier itemLocation = BuiltInRegistries.ITEM.getKey(stack.getItem());
        UpgradeItemsRegistry.DynamicEntry<?> entry = upgradeItems.get(itemLocation.toString());

        if(entry == null)
            throw new IllegalStateException("The upgrade item should be dynamic!");

        String name = UpgradeItemsRegistry.getNameFromItem(entry.item());
        if(!UpgradeItemsRegistry.isDynamic(name))
            throw new IllegalStateException("The upgrade item should be dynamic!");

        String variant = getKey(entry, stack);

        cache.computeIfAbsent(variant, this::bakeModel)
                .update(renderState, stack, modelResolver, displayContext, level, itemOwner, seed);
    }

    public static void register(){
        DynamicUpgradeItemModelUnbaked.BAKER = DynamicUpgradeItemModel::new;
    }
}
