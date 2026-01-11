package wootrevived.woot.init;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import wootrevived.woot.config.*;

public class CommonConfig {
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;

    static {
        CellConfig.build(COMMON_BUILDER);
        DyeLiquifierConfig.build(COMMON_BUILDER);
        EnchantedLiquifierConfig.build(COMMON_BUILDER);
        FluidInfuserConfig.build(COMMON_BUILDER);
        ItemInfuserConfig.build(COMMON_BUILDER);
        MagmatorConfig.build(COMMON_BUILDER);
        MobShardConfig.build(COMMON_BUILDER);
        UpgradesConfig.build(COMMON_BUILDER);
        GuideConfig.build(COMMON_BUILDER);

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static void init(){
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
    }
}
