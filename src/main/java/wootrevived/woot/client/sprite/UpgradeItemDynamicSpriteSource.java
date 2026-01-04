package wootrevived.woot.client.sprite;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.sources.LazyLoadedImage;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.interfaces.WootUpgradeEnum;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.UpgradeItemsRegistry;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public record UpgradeItemDynamicSpriteSource(Identifier id) implements SpriteSource {
    public static final MapCodec<UpgradeItemDynamicSpriteSource> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("id").forGetter(UpgradeItemDynamicSpriteSource::id)
            ).apply(instance, UpgradeItemDynamicSpriteSource::new)
    );

    @Override
    public void run(ResourceManager resourceManager, Output output){
        Collection<UpgradeItemsRegistry.DynamicEntry<?>> dynamicItems = UpgradeItemsRegistry.getDynamicEntries();

        for(UpgradeItemsRegistry.DynamicEntry<?> entry : dynamicItems)
            processDynamicEntry(entry, resourceManager, output);
    }

    private <T extends Enum<T> & WootUpgradeEnum<T>> void processDynamicEntry(UpgradeItemsRegistry.DynamicEntry<T> entry, ResourceManager resourceManager, Output output) {
        WootUpgradeItem<T> item = entry.item().get();
        for (T variant : entry.variantClass().getEnumConstants()) {
            Identifier upgradeItemIdentifier = item.getTextureLocation(variant);
            Resource upgradeItemResource = getResource(resourceManager, upgradeItemIdentifier);
            LazyLoadedImage upgradeItemImage = new LazyLoadedImage(upgradeItemIdentifier, upgradeItemResource, 1);

            Identifier spriteLocation = Woot.identifier("item/upgrade_item_" + variant.getSerializedName() + "_" + UpgradeItemsRegistry.getNameFromItem(entry.item()));

            output.add(spriteLocation, new UpgradeSpriteSupplier<>(entry.item().get(), variant, upgradeItemImage, spriteLocation));
        }
    }

    @Override
    public MapCodec<? extends SpriteSource> codec() {
        return CODEC;
    }

    private Resource getResource(ResourceManager resourceManager, Identifier resourceLocation){
        Optional<Resource> optionalResource = resourceManager.getResource(resourceLocation);
        if(optionalResource.isPresent()){
            return optionalResource.get();
        } else {
            throw new RuntimeException("Could not find resource: " + resourceLocation);
        }
    }

    public record UpgradeSpriteSupplier<T extends Enum<T> & WootUpgradeEnum<T>>(WootUpgradeItem<T> upgradeItem, T variant, LazyLoadedImage lazyUpgradeImage, Identifier location) implements DiscardableLoader {
        @Override
        public SpriteContents get(SpriteResourceLoader spriteResourceLoader) {
            try {
                NativeImage upgradeImage = lazyUpgradeImage.get();
                NativeImage image = new NativeImage(upgradeImage.getWidth(), upgradeImage.getHeight(), false);
                image.copyFrom(upgradeImage);

                upgradeItem.applyItemTexture(image, variant);

                return new SpriteContents(
                        location,
                        new FrameSize(image.getWidth(), image.getHeight()),
                        image
                );
            } catch (IOException ignored) {
            } finally {
                discard();
            }
            return null;
        }

        @Override
        public void discard() {
            lazyUpgradeImage.release();
        }
    }
}
