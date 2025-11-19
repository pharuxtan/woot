package wootrevived.woot.client.sprite;

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
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.interfaces.WootUpgradeEnum;
import wootrevived.api.models.DynamicUpgradeItemModelBuilder;
import wootrevived.woot.Woot;
import wootrevived.woot.mixins.impl.SpriteSourcesMixin;
import wootrevived.woot.registries.UpgradeItemsRegistry;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class UpgradeItemDynamicSpriteSource implements SpriteSource {
    private final Atlas atlas;

    public enum Atlas {
        ITEMS;

        private final SpriteSourceType type;

        Atlas() {
            Codec<UpgradeItemDynamicSpriteSource> CODEC = Codec.unit(() -> new UpgradeItemDynamicSpriteSource(this));
            type = SpriteSourcesMixin.woot$register(Woot.MOD_ID + ":" + DynamicUpgradeItemModelBuilder.ID, CODEC);
        }

        private static void register() {}
    }

    public UpgradeItemDynamicSpriteSource(Atlas atlas) {
        this.atlas = atlas;
    }

    @Override
    public void run(@NotNull ResourceManager resourceManager, @NotNull Output output){
        Collection<UpgradeItemsRegistry.DynamicEntry<?>> dynamicItems = UpgradeItemsRegistry.getDynamicEntries();

        for(UpgradeItemsRegistry.DynamicEntry<?> entry : dynamicItems)
            processDynamicEntry(entry, resourceManager, output);
    }

    private <T extends Enum<T> & WootUpgradeEnum<T>> void processDynamicEntry(UpgradeItemsRegistry.DynamicEntry<T> entry, ResourceManager resourceManager, Output output) {
        WootUpgradeItem<T> item = entry.item().get();
        for (T variant : entry.variantClass().getEnumConstants()) {
            ResourceLocation upgradeItemResourceLocation = item.getTextureLocation(variant);
            Resource upgradeItemResource = getResource(resourceManager, upgradeItemResourceLocation);
            LazyLoadedImage upgradeItemImage = new LazyLoadedImage(upgradeItemResourceLocation, upgradeItemResource, 1);

            ResourceLocation spriteLocation = Woot.location("item/upgrade_item_" + variant.getSerializedName() + "_" + UpgradeItemsRegistry.getNameFromItem(entry.item()));

            output.add(spriteLocation, new UpgradeSpriteSupplier<>(entry.item().get(), variant, upgradeItemImage, spriteLocation));
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

    public record UpgradeSpriteSupplier<T extends Enum<T> & WootUpgradeEnum<T>>(WootUpgradeItem<T> upgradeItem, T variant, LazyLoadedImage lazyUpgradeImage, ResourceLocation location) implements SpriteSupplier {
        public SpriteContents get() {
            try {
                NativeImage upgradeImage = lazyUpgradeImage.get();
                NativeImage image = new NativeImage(upgradeImage.getWidth(), upgradeImage.getHeight(), false);
                image.copyFrom(upgradeImage);

                upgradeItem.applyItemTexture(image, variant);

                return new SpriteContents(
                        location,
                        new FrameSize(image.getWidth(), image.getHeight()),
                        image,
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
            lazyUpgradeImage.release();
        }
    }
}
