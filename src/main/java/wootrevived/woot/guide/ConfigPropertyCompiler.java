package wootrevived.woot.guide;

import guideme.compiler.PageCompiler;
import guideme.compiler.tags.FlowTagCompiler;
import guideme.document.flow.LytFlowParent;
import guideme.libs.mdast.mdx.model.MdxJsxElementFields;
import wootrevived.woot.config.*;
import wootrevived.woot.util.render.WootContainerScreen;

import java.util.Set;

public class ConfigPropertyCompiler extends FlowTagCompiler {
    @Override
    public Set<String> getTagNames(){
        return Set.of("WootConfig");
    }

    @Override
    protected void compile(PageCompiler compiler, LytFlowParent parent, MdxJsxElementFields el){
        String key = el.getAttributeString("key", "");

        String value = WootContainerScreen.formatInteger(switch(key){
            // CellConfig
            case "cell.copper_capacity" -> CellConfig.COPPER_CAPACITY.get();
            case "cell.iron_capacity" -> CellConfig.IRON_CAPACITY.get();
            case "cell.gold_capacity" -> CellConfig.GOLD_CAPACITY.get();
            case "cell.diamond_capacity" -> CellConfig.DIAMOND_CAPACITY.get();
            case "cell.netherite_capacity" -> CellConfig.NETHERITE_CAPACITY.get();

            // Dye Liquifier
            case "dye_liquifier.energy_capacity" -> DyeLiquifierConfig.ENERGY_CAPACITY.get();
            case "dye_liquifier.energy_max_transfer" -> DyeLiquifierConfig.ENERGY_MAX_TRANSFER.get();
            case "dye_liquifier.energy_process_transfer" -> DyeLiquifierConfig.ENERGY_PROCESS_TRANSFER.get();
            case "dye_liquifier.output_tank_capacity" -> DyeLiquifierConfig.OUTPUT_TANK_CAPACITY.get();
            case "dye_liquifier.color_produce_amount" -> DyeLiquifierConfig.COLOR_PRODUCE_AMOUNT.get();
            case "dye_liquifier.pure_dye_produce_amount" -> DyeLiquifierConfig.PURE_DYE_PRODUCE_AMOUNT.get();
            case "dye_liquifier.red_tank_capacity" -> DyeLiquifierConfig.RED_TANK_CAPACITY.get();
            case "dye_liquifier.yellow_tank_capacity" -> DyeLiquifierConfig.YELLOW_TANK_CAPACITY.get();
            case "dye_liquifier.blue_tank_capacity" -> DyeLiquifierConfig.BLUE_TANK_CAPACITY.get();
            case "dye_liquifier.white_tank_capacity" -> DyeLiquifierConfig.WHITE_TANK_CAPACITY.get();

            // Enchanted Liquifier
            case "enchanted_liquifier.energy_capacity" -> EnchantedLiquifierConfig.ENERGY_CAPACITY.get();
            case "enchanted_liquifier.energy_max_transfer" -> EnchantedLiquifierConfig.ENERGY_MAX_TRANSFER.get();
            case "enchanted_liquifier.energy_process_transfer" -> EnchantedLiquifierConfig.ENERGY_PROCESS_TRANSFER.get();
            case "enchanted_liquifier.output_tank_capacity" -> EnchantedLiquifierConfig.OUTPUT_TANK_CAPACITY.get();
            case "enchanted_liquifier.per_enchant_fluid" -> EnchantedLiquifierConfig.PER_ENCHANT_FLUID.get();
            case "enchanted_liquifier.per_enchant_energy" -> EnchantedLiquifierConfig.PER_ENCHANT_ENERGY.get();
            case "enchanted_liquifier.max_enchant_lvl" -> EnchantedLiquifierConfig.MAX_ENCHANT_LVL.get();

            // Fluid Infuser
            case "fluid_infuser.energy_capacity" -> FluidInfuserConfig.ENERGY_CAPACITY.get();
            case "fluid_infuser.energy_max_transfer" -> FluidInfuserConfig.ENERGY_MAX_TRANSFER.get();
            case "fluid_infuser.energy_process_transfer" -> FluidInfuserConfig.ENERGY_PROCESS_TRANSFER.get();
            case "fluid_infuser.input_tank_capacity" -> FluidInfuserConfig.INPUT_TANK_CAPACITY.get();
            case "fluid_infuser.output_tank_capacity" -> FluidInfuserConfig.OUTPUT_TANK_CAPACITY.get();

            // Item Infuser
            case "item_infuser.energy_capacity" -> ItemInfuserConfig.ENERGY_CAPACITY.get();
            case "item_infuser.energy_max_transfer" -> ItemInfuserConfig.ENERGY_MAX_TRANSFER.get();
            case "item_infuser.energy_process_transfer" -> ItemInfuserConfig.ENERGY_PROCESS_TRANSFER.get();
            case "item_infuser.input_tank_capacity" -> ItemInfuserConfig.INPUT_TANK_CAPACITY.get();

            // Magmator
            case "magmator.copper_tick_rate" -> MagmatorConfig.COPPER_TICK_RATE.get();
            case "magmator.iron_tick_rate" -> MagmatorConfig.IRON_TICK_RATE.get();
            case "magmator.gold_tick_rate" -> MagmatorConfig.GOLD_TICK_RATE.get();
            case "magmator.diamond_tick_rate" -> MagmatorConfig.DIAMOND_TICK_RATE.get();
            case "magmator.netherite_tick_rate" -> MagmatorConfig.NETHERITE_TICK_RATE.get();

            default -> 0;
        });

        parent.appendText(value);
    }
}
