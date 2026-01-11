package wootrevived.woot.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class UpgradesConfig {
    public static ModConfigSpec.BooleanValue MASS_REROLL_LOOT;

    public static void build(ModConfigSpec.Builder builder){
        builder.comment("Upgrades").push("upgrades");
        {
            MASS_REROLL_LOOT = builder.comment(String.format("Should the mass upgrade reroll the loot table or otherwise duplicate the already rolled items [Default: %b]", DefaultsConfig.Upgrades.MASS_REROLL_LOOT))
                    .define("massRerollLoot", DefaultsConfig.Upgrades.MASS_REROLL_LOOT);
        }
        builder.pop();
    }
}
