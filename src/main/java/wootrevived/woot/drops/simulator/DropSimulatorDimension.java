package wootrevived.woot.drops.simulator;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import wootrevived.woot.Woot;

import java.util.List;

public class DropSimulatorDimension {
    public static final String DROP_SIMULATOR_TAG = "drop_simulator";

    public static final ResourceKey<DimensionType> DROP_SIMULATOR_DIMENSION_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, Woot.identifier(DROP_SIMULATOR_TAG + "_type"));
    public static final ResourceKey<Level> DROP_SIMULATOR_LEVEL = ResourceKey.create(Registries.DIMENSION, Woot.identifier(DROP_SIMULATOR_TAG));
    public static final ResourceKey<LevelStem> DROP_SIMULATOR_LEVEL_STEM = ResourceKey.create(Registries.LEVEL_STEM, Woot.identifier(DROP_SIMULATOR_TAG));

    public static void bootstrapType(BootstrapContext<DimensionType> context) {
        context.register(DROP_SIMULATOR_DIMENSION_TYPE, new DimensionType(
                false, // hasFixedTime
                false, // hasSkylight
                false, // hasCeiling
                1.0, // coordinateScale
                -64, // minY
                384, // height
                384, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                0.0f, // ambientLight
                new DimensionType.MonsterSettings(ConstantInt.of(0), 0),
                DimensionType.Skybox.NONE,
                DimensionType.CardinalLightType.DEFAULT,
                EnvironmentAttributeMap.EMPTY,
                HolderSet.empty()
        ));
    }

    public static void bootstrapStem(BootstrapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseSettings = context.lookup(Registries.NOISE_SETTINGS);

        MultiNoiseBiomeSource biomeSource = MultiNoiseBiomeSource.createFromList(
                new Climate.ParameterList<>(List.of(
                        Pair.of(Climate.parameters(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F),
                                biomeRegistry.getOrThrow(Biomes.PLAINS))
                ))
        );

        NoiseBasedChunkGenerator overworldGenerator = new NoiseBasedChunkGenerator(
                biomeSource,
                noiseSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD)
        );

        LevelStem stem = new LevelStem(dimTypes.getOrThrow(DROP_SIMULATOR_DIMENSION_TYPE), overworldGenerator);

        context.register(DROP_SIMULATOR_LEVEL_STEM, stem);
    }
}
