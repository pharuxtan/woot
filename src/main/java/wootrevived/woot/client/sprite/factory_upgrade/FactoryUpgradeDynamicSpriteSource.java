package wootrevived.woot.client.sprite.factory_upgrade;

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
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootUpgradeItem;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.UpgradeItemsRegistry;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public record FactoryUpgradeDynamicSpriteSource(ResourceLocation id) implements SpriteSource {
    public static final MapCodec<FactoryUpgradeDynamicSpriteSource> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        ResourceLocation.CODEC.fieldOf("id").forGetter(FactoryUpgradeDynamicSpriteSource::id)
                ).apply(instance, FactoryUpgradeDynamicSpriteSource::new)
    );

    @Override
    public void run(@NotNull ResourceManager manager, @NotNull Output output){
        Collection<DeferredHolder<Item, ? extends WootUpgradeItem>> upgradeItems = UpgradeItemsRegistry.getValues();

        ResourceLocation factoryUpgradeResourceLocation = Woot.location("textures/block/" + BlocksRegistry.FACTORY_UPGRADE_TAG + ".png");
        Resource factoryResource = getResource(manager, factoryUpgradeResourceLocation);
        LazyLoadedImage factoryImage = new LazyLoadedImage(factoryUpgradeResourceLocation, factoryResource, upgradeItems.size());

        for(DeferredHolder<Item, ? extends WootUpgradeItem> upgradeItem : upgradeItems) {
            ResourceLocation upgradeItemResourceLocation = upgradeItem.get().getTextureLocation();
            Resource upgradeItemResource = getResource(manager, upgradeItemResourceLocation);
            LazyLoadedImage upgradeItemImage = new LazyLoadedImage(upgradeItemResourceLocation, upgradeItemResource, 1);

            ResourceLocation spriteLocation = Woot.location("block/upgrade_item_" + UpgradeItemsRegistry.getNameFromItem(upgradeItem));

            output.add(spriteLocation, new UpgradeSpriteSupplier(upgradeItem.get(), factoryImage, upgradeItemImage, spriteLocation));
        }
    }

    @Override
    public MapCodec<? extends SpriteSource> codec() {
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

    public record UpgradeSpriteSupplier(WootUpgradeItem upgradeItem, LazyLoadedImage lazyFactoryImage, LazyLoadedImage lazyUpgradeImage, ResourceLocation location) implements SpriteSource.SpriteSupplier {
        @Override
        public SpriteContents apply(SpriteResourceLoader spriteResourceLoader) {
            try {
                NativeImage factoryUpgradeImage = lazyFactoryImage.get();
                NativeImage upgradeItemImage = lazyUpgradeImage.get();
                NativeImage imageSide = new NativeImage(factoryUpgradeImage.getWidth(), factoryUpgradeImage.getHeight(), false);
                imageSide.copyFrom(factoryUpgradeImage);

                upgradeItem.applyUpgradeTexture(imageSide, upgradeItemImage);

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
