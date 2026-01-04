package wootrevived.woot.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import wootrevived.woot.Woot;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ItemTagsGen extends ItemTagsProvider {
    public ItemTagsGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(generator, lookupProvider, Woot.MOD_NAMESPACE);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addTags(HolderLookup.Provider provider) {
        addColored(tag(Tags.Items.DYES)::addTags);
    }

    // Straight from forge
    private void addColored(Consumer<TagKey<Item>> consumer)
    {
        String prefix = Tags.Items.DYES.location().getPath().toUpperCase(Locale.ENGLISH) + '_';
        for (DyeColor dyeColor : DyeColor.values()) {
            Identifier key = Woot.identifier("{color}_dye_plate".replace("{color}", dyeColor.getName()));
            TagKey<Item> iTag = getNeoForgeItemTag(prefix + dyeColor.getName());
            Item item = BuiltInRegistries.ITEM.get(key).orElseThrow().value();
            if (item == Items.AIR)
                throw new IllegalStateException("Unknown woot item: " + key);
            tag(iTag).add(item);
            consumer.accept(iTag);
        }
    }

    @SuppressWarnings("unchecked")
    private TagKey<Item> getNeoForgeItemTag(String name)
    {
        try {
            name = name.toUpperCase(Locale.ENGLISH);
            return (TagKey<Item>)Tags.Items.class.getDeclaredField(name).get(null);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new IllegalStateException(Tags.Items.class.getName() + " is missing tag name: " + name);
        }
    }
}
