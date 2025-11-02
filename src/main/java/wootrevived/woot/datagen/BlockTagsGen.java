package wootrevived.woot.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.BlocksRegistry;

import java.util.concurrent.CompletableFuture;

public class BlockTagsGen extends BlockTagsProvider {
    public BlockTagsGen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider, Woot.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        TagAppender<Block, Block> tagAppender = tag(BlockTags.MINEABLE_WITH_PICKAXE);

        for(DeferredHolder<Block, ? extends Block> block : BlocksRegistry.BLOCKS.getEntries())
            tagAppender.add(block.get());
    }
}
