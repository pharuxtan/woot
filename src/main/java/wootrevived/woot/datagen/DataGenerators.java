package wootrevived.woot.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import wootrevived.woot.Woot;
import wootrevived.woot.datagen.models.Model;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Woot.MOD_ID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherDataClient(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        PackOutput packOutput = generator.getPackOutput();

        generator.addProvider(true, new Model(packOutput));
        generator.addProvider(true, new Atlas(packOutput, lookupProvider));
        generator.addProvider(true, new Languages(packOutput));
    }

    @SubscribeEvent
    public static void gatherDataServer(GatherDataEvent.Server event) {
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        PackOutput packOutput = generator.getPackOutput();

        generator.addProvider(true, new Recipes.Runner(packOutput, lookupProvider));
        BlockTagsProvider blockTagsProvider = new BlockTagsGen(packOutput, lookupProvider);
        generator.addProvider(true, blockTagsProvider);
        generator.addProvider(true, new ItemTagsGen(packOutput, lookupProvider, blockTagsProvider.contentsGetter()));
        generator.addProvider(true, new DropSimulatorDim(packOutput, lookupProvider));
        generator.addProvider(true, new Advancements(packOutput, lookupProvider));
        generator.addProvider(true, new LootTableProvider(packOutput, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(LootTables::new, LootContextParamSets.BLOCK)
        ), lookupProvider));
    }
}
