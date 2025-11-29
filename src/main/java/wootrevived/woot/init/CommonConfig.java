package wootrevived.woot.init;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import wootrevived.woot.config.*;

public class CommonConfig {
    private static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();

    public static ModConfigSpec COMMON_CONFIG;

    static {
        CellConfig.build(COMMON_BUILDER);
        DyeLiquifierConfig.build(COMMON_BUILDER);
        EnchantedLiquifierConfig.build(COMMON_BUILDER);
        FluidInfuserConfig.build(COMMON_BUILDER);
        ItemInfuserConfig.build(COMMON_BUILDER);
        MagmatorConfig.build(COMMON_BUILDER);
        MobShardConfig.build(COMMON_BUILDER);
        GuideConfig.build(COMMON_BUILDER);

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static void init(){
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
    }
}
