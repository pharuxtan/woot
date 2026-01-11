package wootrevived.woot.config;

public class DefaultsConfig {
    public static final int BUCKET_CAPACITY = 1000;

    public static class Cell {
        public static final int COPPER_CAPACITY = BUCKET_CAPACITY * 30;
        public static final int IRON_CAPACITY = BUCKET_CAPACITY * 100;
        public static final int GOLD_CAPACITY = BUCKET_CAPACITY * 250;
        public static final int DIAMOND_CAPACITY = BUCKET_CAPACITY * 500;
        public static final int NETHERITE_CAPACITY = BUCKET_CAPACITY * 1000;
    }

    public static class DyeLiquifier {
        public static final int ENERGY_CAPACITY = 25000;
        public static final int ENERGY_MAX_TRANSFER = 1000;
        public static final int ENERGY_PROCESS_TRANSFER = 50;
        public static final int FLUID_TRANSFER = 500;
        public static final int OUTPUT_TANK_CAPACITY = BUCKET_CAPACITY * 25;

        public static final int COLOR_PRODUCE_AMOUNT = 125;
        public static final int PURE_DYE_PRODUCE_AMOUNT = COLOR_PRODUCE_AMOUNT * 4;

        public static final int RED_TANK_CAPACITY = COLOR_PRODUCE_AMOUNT * 100;
        public static final int YELLOW_TANK_CAPACITY = COLOR_PRODUCE_AMOUNT * 100;
        public static final int BLUE_TANK_CAPACITY = COLOR_PRODUCE_AMOUNT * 100;
        public static final int WHITE_TANK_CAPACITY = COLOR_PRODUCE_AMOUNT * 100;
    }

    public static class EnchantedLiquifier {
        public static final int ENERGY_CAPACITY = 100000;
        public static final int ENERGY_MAX_TRANSFER = 1000;
        public static final int ENERGY_PROCESS_TRANSFER = 100;
        public static final int FLUID_TRANSFER = 500;
        public static final int OUTPUT_TANK_CAPACITY = BUCKET_CAPACITY * 50;

        public static final int PER_ENCHANT_FLUID = BUCKET_CAPACITY * 5 / 2;
        public static final int PER_ENCHANT_ENERGY = 5000;
        public static final int MAX_ENCHANT_LVL = 10;
    }

    public static class FluidInfuser {
        public static final int ENERGY_CAPACITY = 50000;
        public static final int ENERGY_MAX_TRANSFER = 1000;
        public static final int ENERGY_PROCESS_TRANSFER = 250;
        public static final int FLUID_TRANSFER = 50000;
        public static final int INPUT_TANK_CAPACITY = BUCKET_CAPACITY * 50;
        public static final int OUTPUT_TANK_CAPACITY = BUCKET_CAPACITY * 50;
    }

    public static class ItemInfuser {
        public static final int ENERGY_CAPACITY = 50000;
        public static final int ENERGY_MAX_TRANSFER = 1000;
        public static final int ENERGY_PROCESS_TRANSFER = 250;
        public static final int FLUID_TRANSFER = 500;
        public static final int INPUT_TANK_CAPACITY = BUCKET_CAPACITY * 25;
    }

    public static class Guide {
        public static final boolean GIVE_ON_SPAWN = true;
    }

    public static class MobShard {
        public static final int NUM_OF_KILLS = 5;
    }

    public static class Magmator {
        public static final int COPPER_TICK_RATE = 50;
        public static final int IRON_TICK_RATE = 20;
        public static final int GOLD_TICK_RATE = 10;
        public static final int DIAMOND_TICK_RATE = 5;
        public static final int NETHERITE_TICK_RATE = 1;
    }

    public static class Upgrades {
        public static final boolean MASS_REROLL_LOOT = true;
    }
}
