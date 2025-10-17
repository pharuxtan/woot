package wootrevived.woot.datagen;

import net.minecraft.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import wootrevived.woot.datagen.languages.English;
import wootrevived.woot.datagen.languages.SimplifiedChinese;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Languages implements DataProvider {
    private final List<? extends LanguageProvider> languages;

    public Languages(PackOutput packOutput) {
        languages = List.of(
                new English(packOutput),
                new SimplifiedChinese(packOutput)
        );
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for(LanguageProvider language : languages)
            futures.add(language.run(cache));

        return CompletableFuture.runAsync(() -> {
            for(CompletableFuture<?> future : futures)
                future.join();
        }, Util.backgroundExecutor());
    }

    @Override
    public String getName() {
        return "Languages";
    }
}
