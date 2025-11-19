package wootrevived.woot.client.sprite;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.sources.LazyLoadedImage;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.interfaces.WootUpgradeEnum;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.UpgradeItemsRegistry;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public record FactoryUpgradeDynamicSpriteSource(ResourceLocation id) implements SpriteSource {
    public static final MapCodec<FactoryUpgradeDynamicSpriteSource> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        ResourceLocation.CODEC.fieldOf("id").forGetter(FactoryUpgradeDynamicSpriteSource::id)
                ).apply(instance, FactoryUpgradeDynamicSpriteSource::new)
    );

    @Override
    public void run(@NotNull ResourceManager resourceManager, @NotNull Output output){
        Collection<UpgradeItemsRegistry.DynamicEntry<?>> dynamicItems = UpgradeItemsRegistry.getDynamicEntries();

        for(UpgradeItemsRegistry.DynamicEntry<?> entry : dynamicItems)
            processDynamicEntry(entry, resourceManager, output);

        Collection<UpgradeItemsRegistry.Entry<?>> items = UpgradeItemsRegistry.getEntries();

        ResourceLocation factoryUpgradeResourceLocation = Woot.location("textures/block/" + BlocksRegistry.FACTORY_UPGRADE_TAG + ".png");
        Resource factoryResource = getResource(resourceManager, factoryUpgradeResourceLocation);
        LazyLoadedImage factoryImage = new LazyLoadedImage(factoryUpgradeResourceLocation, factoryResource, items.size());

        for(UpgradeItemsRegistry.Entry<?> entry : items)
            processEntry(entry, factoryImage, resourceManager, output);
    }

    private <T extends Enum<T> & WootUpgradeEnum<T>> void processEntry(UpgradeItemsRegistry.Entry<T> entry, LazyLoadedImage factoryImage, ResourceManager resourceManager, Output output) {
        WootUpgradeItem<T> item = entry.item().get();
        T variant = item.getVariant(null);

        ResourceLocation upgradeItemResourceLocation = item.getTextureLocation(variant);
        Resource upgradeItemResource = getResource(resourceManager, upgradeItemResourceLocation);
        LazyLoadedImage upgradeItemImage = new LazyLoadedImage(upgradeItemResourceLocation, upgradeItemResource, 1);

        ResourceLocation spriteLocation = Woot.location("block/upgrade_item_" + UpgradeItemsRegistry.getNameFromItem(entry.item()));

        output.add(spriteLocation, new UpgradeSpriteSupplier<>(item, variant, factoryImage, upgradeItemImage, spriteLocation));
    }

    private <T extends Enum<T> & WootUpgradeEnum<T>> void processDynamicEntry(UpgradeItemsRegistry.DynamicEntry<T> entry, ResourceManager resourceManager, Output output) {
        T[] variants = entry.variantClass().getEnumConstants();

        ResourceLocation factoryUpgradeResourceLocation = Woot.location("textures/block/" + BlocksRegistry.FACTORY_UPGRADE_TAG + ".png");
        Resource factoryResource = getResource(resourceManager, factoryUpgradeResourceLocation);
        LazyLoadedImage factoryImage = new LazyLoadedImage(factoryUpgradeResourceLocation, factoryResource, variants.length);

        WootUpgradeItem<T> item = entry.item().get();
        for (T variant : variants) {
            ResourceLocation upgradeItemResourceLocation = item.getTextureLocation(variant);
            Resource upgradeItemResource = getResource(resourceManager, upgradeItemResourceLocation);
            LazyLoadedImage upgradeItemImage = new LazyLoadedImage(upgradeItemResourceLocation, upgradeItemResource, 1);

            ResourceLocation spriteLocation = Woot.location("block/upgrade_item_" + variant.getSerializedName() + "_" + UpgradeItemsRegistry.getNameFromItem(entry.item()));

            output.add(spriteLocation, new UpgradeSpriteSupplier<>(entry.item().get(), variant, factoryImage, upgradeItemImage, spriteLocation));
        }
    }

    @Override
    public @NotNull MapCodec<? extends SpriteSource> codec() {
        return CODEC;
    }

    private Resource getResource(ResourceManager resourceManager, ResourceLocation resourceLocation){
        Optional<Resource> optionalResource = resourceManager.getResource(resourceLocation);
        if(optionalResource.isPresent()){
            return optionalResource.get();
        } else {
            throw new RuntimeException("Could not find resource: " + resourceLocation);
        }
    }

    public record UpgradeSpriteSupplier<T extends Enum<T> & WootUpgradeEnum<T>>(WootUpgradeItem<T> upgradeItem, T variant, LazyLoadedImage lazyFactoryImage, LazyLoadedImage lazyUpgradeImage, ResourceLocation location) implements SpriteSupplier {
        @Override
        public SpriteContents apply(SpriteResourceLoader spriteResourceLoader) {
            try {
                NativeImage factoryUpgradeImage = lazyFactoryImage.get();
                NativeImage imageSide = new NativeImage(factoryUpgradeImage.getWidth(), factoryUpgradeImage.getHeight(), false);
                imageSide.copyFrom(factoryUpgradeImage);

                NativeImage upgradeItemImage = lazyUpgradeImage.get();
                upgradeItem.applyItemTexture(upgradeItemImage, variant);
                upgradeItem.applyUpgradeTexture(imageSide, upgradeItemImage, variant);

                return new SpriteContents(
                        location,
                        new FrameSize(imageSide.getWidth(), imageSide.getHeight()),
                        imageSide,
                        ResourceMetadata.EMPTY
                );
            } catch (IOException ignored) {
            } finally {
                discard();
            }
            return null;
        }

        @Override
        public void discard() {
            lazyFactoryImage.release();
            lazyUpgradeImage.release();
        }
    }
}
