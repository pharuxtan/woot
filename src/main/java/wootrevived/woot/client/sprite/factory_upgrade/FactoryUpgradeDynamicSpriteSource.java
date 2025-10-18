package wootrevived.woot.client.sprite.factory_upgrade;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.serialization.Codec;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSourceType;
import net.minecraft.client.renderer.texture.atlas.sources.LazyLoadedImage;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootUpgradeItem;
import wootrevived.woot.Woot;
import wootrevived.woot.mixins.impl.SpriteSourcesMixin;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.UpgradeItemsRegistry;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class FactoryUpgradeDynamicSpriteSource implements SpriteSource {
    private final Atlas atlas;

    public enum Atlas {
        BLOCKS;

        private final SpriteSourceType type;

        Atlas() {
            Codec<FactoryUpgradeDynamicSpriteSource> CODEC = Codec.unit(() -> new FactoryUpgradeDynamicSpriteSource(this));
            type = SpriteSourcesMixin.woot$register(Woot.MOD_ID + ":" + BlocksRegistry.FACTORY_UPGRADE_TAG, CODEC);
        }

        private static void register() {}
    }

    public FactoryUpgradeDynamicSpriteSource(Atlas atlas) {
        this.atlas = atlas;
    }

    @Override
    public void run(@NotNull ResourceManager resourceManager, @NotNull Output output){
        Collection<RegistryObject<? extends WootUpgradeItem>> upgradeItems = UpgradeItemsRegistry.getValues();

        ResourceLocation factoryUpgradeResourceLocation = Woot.location("textures/block/" + BlocksRegistry.FACTORY_UPGRADE_TAG + ".png");
        Resource factoryResource = getResource(resourceManager, factoryUpgradeResourceLocation);
        LazyLoadedImage factoryImage = new LazyLoadedImage(factoryUpgradeResourceLocation, factoryResource, upgradeItems.size());

        for(RegistryObject<? extends WootUpgradeItem> upgradeItem : upgradeItems) {
            ResourceLocation upgradeItemResourceLocation = upgradeItem.get().getTextureLocation();
            Resource upgradeItemResource = getResource(resourceManager, upgradeItemResourceLocation);
            LazyLoadedImage upgradeItemImage = new LazyLoadedImage(upgradeItemResourceLocation, upgradeItemResource, 1);

            ResourceLocation spriteLocation = Woot.location("block/upgrade_item_" + UpgradeItemsRegistry.getNameFromItem(upgradeItem));

            output.add(spriteLocation, new UpgradeSpriteSupplier(upgradeItem.get(), factoryImage, upgradeItemImage, spriteLocation));
        }
    }

    private Resource getResource(ResourceManager resourceManager, ResourceLocation resourceLocation){
        Optional<Resource> optionalResource = resourceManager.getResource(resourceLocation);
        if(optionalResource.isPresent()){
            return optionalResource.get();
        } else {
            throw new RuntimeException("Could not find resource: " + resourceLocation);
        }
    }

    @Override
    public @NotNull SpriteSourceType type() {
        return atlas.type;
    }

    public static void register() {
        Atlas.register();
    }

    public record UpgradeSpriteSupplier(WootUpgradeItem upgradeItem, LazyLoadedImage lazyFactoryImage, LazyLoadedImage lazyUpgradeImage, ResourceLocation location) implements SpriteSource.SpriteSupplier {
        public SpriteContents get() {
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
                        AnimationMetadataSection.EMPTY,
                        null
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
