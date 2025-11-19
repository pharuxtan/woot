package wootrevived.woot.client.model.factory_upgrade;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.QuadTransformers;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.neoforged.neoforge.client.model.geometry.UnbakedGeometryHelper;
import wootrevived.api.interfaces.WootUpgradeEnum;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.UpgradeItemsRegistry;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class FactoryUpgradeUnbakedModel implements IUnbakedGeometry<FactoryUpgradeUnbakedModel> {
    private final ResourceLocation parentLocation;

    private BlockModel blockModel;

    public FactoryUpgradeUnbakedModel(ResourceLocation parentLocation) {
        this.parentLocation = parentLocation;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation){
        TextureAtlasSprite factory = spriteGetter.apply(context.getMaterial("north"));

        FactoryUpgradeBakedModel.Builder builder = new FactoryUpgradeBakedModel.Builder(context.useAmbientOcclusion(), context.useBlockLight(), context.isGui3d(), context.getTransforms(), overrides)
                .addParticle("", factory);

        addQuads(context, builder, spriteGetter, modelState, modelLocation, factory, "");

        for(UpgradeItemsRegistry.Entry<?> entry : UpgradeItemsRegistry.getEntries())
            processEntry(builder, entry, context, spriteGetter, modelState, modelLocation);

        for(UpgradeItemsRegistry.DynamicEntry<?> entry : UpgradeItemsRegistry.getDynamicEntries())
            processDynamicEntry(builder, entry, context, spriteGetter, modelState, modelLocation);

        return builder.build();
    }

    private <T extends Enum<T> & WootUpgradeEnum<T>> void processEntry(FactoryUpgradeBakedModel.Builder builder, UpgradeItemsRegistry.Entry<T> entry, IGeometryBakingContext context, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation) {
        String name = UpgradeItemsRegistry.getNameFromItem(entry.item());

        TextureAtlasSprite texture = spriteGetter.apply(new Material(
                InventoryMenu.BLOCK_ATLAS,
                Woot.location("block/upgrade_item_" + name)
        ));

        builder.addParticle(name, texture);

        addQuads(context, builder, spriteGetter, modelState, modelLocation, texture, name);
    }

    private <T extends Enum<T> & WootUpgradeEnum<T>> void processDynamicEntry(FactoryUpgradeBakedModel.Builder builder, UpgradeItemsRegistry.DynamicEntry<T> entry, IGeometryBakingContext context, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation) {
        for (T variant : entry.variantClass().getEnumConstants()) {
            String name = variant.getSerializedName() + "_" + UpgradeItemsRegistry.getNameFromItem(entry.item());

            TextureAtlasSprite texture = spriteGetter.apply(new Material(
                    InventoryMenu.BLOCK_ATLAS,
                    Woot.location("block/upgrade_item_" + name)
            ));

            builder.addParticle(name, texture);

            addQuads(context, builder, spriteGetter, modelState, modelLocation, texture, name);
        }
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context)
    {
        UnbakedModel unbakedModel = modelGetter.apply(parentLocation);
        unbakedModel.resolveParents(modelGetter);
        if(!(unbakedModel instanceof BlockModel model))
            throw new RuntimeException("Baking factory_upgrade parent not a block");
        this.blockModel = model;
    }

    @SuppressWarnings("deprecation")
    protected void addQuads(IGeometryBakingContext context, FactoryUpgradeBakedModel.Builder modelBuilder, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation, TextureAtlasSprite customSprite, String upgrade)
    {
        var postTransform = QuadTransformers.empty();
        var rootTransform = context.getRootTransform();
        if (!rootTransform.isIdentity())
            postTransform = UnbakedGeometryHelper.applyRootTransform(modelState, rootTransform);

        for (BlockElement element : blockModel.getElements())
        {
            for (Direction direction : element.faces.keySet())
            {
                var face = element.faces.get(direction);
                var sprite = direction != Direction.DOWN && direction != Direction.UP ? customSprite : spriteGetter.apply(context.getMaterial(face.texture));
                var quad = BlockModel.bakeFace(element, face, sprite, direction, modelState, modelLocation);
                postTransform.processInPlace(quad);

                modelBuilder.addFace(modelState.getRotation().rotateTransform(face.cullForDirection), upgrade, quad);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Loader implements IGeometryLoader<FactoryUpgradeUnbakedModel> {
        public static final Loader INSTANCE = new Loader();

        @Override
        public FactoryUpgradeUnbakedModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext){
            if(!jsonObject.has("parent"))
                throw new RuntimeException("Model factory_upgrade don't have parent");

            return new FactoryUpgradeUnbakedModel(ResourceLocation.tryParse(jsonObject.get("parent").getAsString()));
        }
    }
}
