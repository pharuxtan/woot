package wootrevived.woot.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.Woot;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ItemTagsGen extends ItemTagsProvider {
    public static final TagKey<Item> FACTORY_BLOCK = TagKey.create(Registries.ITEM, Woot.location("factory_block"));

    public ItemTagsGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> parentProvider) {
        super(generator, lookupProvider, parentProvider, Woot.MOD_ID);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        addColored(tag(Tags.Items.DYES)::addTags);
    }

    // Straight from forge
    private void addColored(Consumer<TagKey<Item>> consumer)
    {
        String prefix = Tags.Items.DYES.location().getPath().toUpperCase(Locale.ENGLISH) + '_';
        for (DyeColor dyeColor : DyeColor.values()) {
            ResourceLocation key = Woot.location("{color}_dye_plate".replace("{color}", dyeColor.getName()));
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
