package wootrevived.woot.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import wootrevived.woot.Woot;
import wootrevived.woot.drops.simulator.DropSimulatorDimension;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DropSimulatorDim extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, DropSimulatorDimension::bootstrapType)
            .add(Registries.LEVEL_STEM, DropSimulatorDimension::bootstrapStem);

    public DropSimulatorDim(PackOutput output, CompletableFuture<HolderLookup.Provider> registries){
        super(output, registries, BUILDER, Set.of(Woot.MOD_NAMESPACE));
    }
}
