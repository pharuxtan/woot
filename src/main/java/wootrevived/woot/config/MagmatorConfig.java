package wootrevived.woot.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MagmatorConfig {
    public static ForgeConfigSpec.ConfigValue<Integer> COPPER_TICK_RATE;
    public static ForgeConfigSpec.ConfigValue<Integer> IRON_TICK_RATE;
    public static ForgeConfigSpec.ConfigValue<Integer> GOLD_TICK_RATE;
    public static ForgeConfigSpec.ConfigValue<Integer> DIAMOND_TICK_RATE;
    public static ForgeConfigSpec.ConfigValue<Integer> NETHERITE_TICK_RATE;

    public static void build(ForgeConfigSpec.Builder builder){
        builder.comment("Magmator").push("magmator");
        {
            COPPER_TICK_RATE = builder.comment(String.format("The tick rate for the Copper Magmator [Default: %d]", DefaultsConfig.Magmator.COPPER_TICK_RATE))
                    .define("copperTickRate", DefaultsConfig.Magmator.COPPER_TICK_RATE);

            IRON_TICK_RATE = builder.comment(String.format("The tick rate for the Iron Magmator [Default: %d]", DefaultsConfig.Magmator.IRON_TICK_RATE))
                    .define("ironTickRate", DefaultsConfig.Magmator.IRON_TICK_RATE);

            GOLD_TICK_RATE = builder.comment(String.format("The tick rate for the Gold Magmator [Default: %d]", DefaultsConfig.Magmator.GOLD_TICK_RATE))
                    .define("goldTickRate", DefaultsConfig.Magmator.GOLD_TICK_RATE);

            DIAMOND_TICK_RATE = builder.comment(String.format("The tick rate for the Diamond Magmator [Default: %d]", DefaultsConfig.Magmator.DIAMOND_TICK_RATE))
                    .define("diamondTickRate", DefaultsConfig.Magmator.DIAMOND_TICK_RATE);

            NETHERITE_TICK_RATE = builder.comment(String.format("The tick rate for the Netherite Magmator [Default: %d]", DefaultsConfig.Magmator.NETHERITE_TICK_RATE))
                    .define("netheriteTickRate", DefaultsConfig.Magmator.NETHERITE_TICK_RATE);
        }
        builder.pop();
    }
}
