package wootrevived.woot.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.BucketResourceHandler;
import wootrevived.woot.Woot;
import wootrevived.woot.blocks.cell.CellBlockEntity;
import wootrevived.woot.blocks.creative_power.CreativePowerBlockEntity;
import wootrevived.woot.blocks.creative_tank.CreativeTankBlockEntity;
import wootrevived.woot.blocks.dye_liquifier.DyeLiquifierBlockEntity;
import wootrevived.woot.blocks.enchanted_liquifier.EnchantedLiquifierBlockEntity;
import wootrevived.woot.blocks.fluid_infuser.FluidInfuserBlockEntity;
import wootrevived.woot.blocks.ingredient_import.IngredientImportBlockEntity;
import wootrevived.woot.blocks.item_infuser.ItemInfuserBlockEntity;
import wootrevived.woot.blocks.stygian_anvil.StygianAnvilBlockEntity;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.util.entity.WootMachineBlockEntity;

@EventBusSubscriber(modid = Woot.MOD_ID)
public class RegisterCapabilities {
    @SubscribeEvent
    public static void registerCapabilites(RegisterCapabilitiesEvent event) {
        /* Stygian Anvil */
        event.registerBlockEntity(Capabilities.Item.BLOCK, BlocksRegistry.STYGIAN_ANVIL_BLOCK_ENTITY.get(), StygianAnvilBlockEntity::getItemHandlerCapability);

        /* Dye Liquifier */
        event.registerBlockEntity(Capabilities.Energy.BLOCK, BlocksRegistry.DYE_LIQUIFIER_BLOCK_ENTITY.get(), WootMachineBlockEntity::getEnergyStorageCapability);
        event.registerBlockEntity(Capabilities.Item.BLOCK, BlocksRegistry.DYE_LIQUIFIER_BLOCK_ENTITY.get(), DyeLiquifierBlockEntity::getItemHandlerCapability);
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, BlocksRegistry.DYE_LIQUIFIER_BLOCK_ENTITY.get(), DyeLiquifierBlockEntity::getFluidHandlerCapability);

        /* Enchanted Liquifier */
        event.registerBlockEntity(Capabilities.Energy.BLOCK, BlocksRegistry.ENCHANTED_LIQUIFIER_BLOCK_ENTITY.get(), WootMachineBlockEntity::getEnergyStorageCapability);
        event.registerBlockEntity(Capabilities.Item.BLOCK, BlocksRegistry.ENCHANTED_LIQUIFIER_BLOCK_ENTITY.get(), EnchantedLiquifierBlockEntity::getItemHandlerCapability);
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, BlocksRegistry.ENCHANTED_LIQUIFIER_BLOCK_ENTITY.get(), EnchantedLiquifierBlockEntity::getFluidHandlerCapability);

        /* Item Infuser */
        event.registerBlockEntity(Capabilities.Energy.BLOCK, BlocksRegistry.ITEM_INFUSER_BLOCK_ENTITY.get(), WootMachineBlockEntity::getEnergyStorageCapability);
        event.registerBlockEntity(Capabilities.Item.BLOCK, BlocksRegistry.ITEM_INFUSER_BLOCK_ENTITY.get(), ItemInfuserBlockEntity::getItemHandlerCapability);
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, BlocksRegistry.ITEM_INFUSER_BLOCK_ENTITY.get(), ItemInfuserBlockEntity::getFluidHandlerCapability);

        /* Fluid Infuser */
        event.registerBlockEntity(Capabilities.Energy.BLOCK, BlocksRegistry.FLUID_INFUSER_BLOCK_ENTITY.get(), WootMachineBlockEntity::getEnergyStorageCapability);
        event.registerBlockEntity(Capabilities.Item.BLOCK, BlocksRegistry.FLUID_INFUSER_BLOCK_ENTITY.get(), FluidInfuserBlockEntity::getItemHandlerCapability);
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, BlocksRegistry.FLUID_INFUSER_BLOCK_ENTITY.get(), FluidInfuserBlockEntity::getFluidHandlerCapability);

        /* Ingredient Importer */
        event.registerBlockEntity(Capabilities.Item.BLOCK, BlocksRegistry.IMPORT_BLOCK_ENTITY.get(), IngredientImportBlockEntity::getItemHandlerCapability);
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, BlocksRegistry.IMPORT_BLOCK_ENTITY.get(), IngredientImportBlockEntity::getFluidHandlerCapability);

        /* Creative Tank */
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, BlocksRegistry.CREATIVE_TANK_BLOCK_ENTITY.get(), CreativeTankBlockEntity::getFluidHandlerCapability);

        /* Creative Power */
        event.registerBlockEntity(Capabilities.Energy.BLOCK, BlocksRegistry.CREATIVE_POWER_BLOCK_ENTITY.get(), CreativePowerBlockEntity::getEnergyStorageCapability);

        /* Cells */
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, BlocksRegistry.COPPER_CELL_BLOCK_ENTITY.get(), CellBlockEntity::getFluidHandlerCapability);
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, BlocksRegistry.IRON_CELL_BLOCK_ENTITY.get(), CellBlockEntity::getFluidHandlerCapability);
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, BlocksRegistry.GOLD_CELL_BLOCK_ENTITY.get(), CellBlockEntity::getFluidHandlerCapability);
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, BlocksRegistry.DIAMOND_CELL_BLOCK_ENTITY.get(), CellBlockEntity::getFluidHandlerCapability);
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, BlocksRegistry.NETHERITE_CELL_BLOCK_ENTITY.get(), CellBlockEntity::getFluidHandlerCapability);

        /* Enchanted Liquid Bucket */
        event.registerItem(Capabilities.Fluid.ITEM, (stack, ctx) -> new BucketResourceHandler(ItemAccess.forStack(stack)), FluidsRegistry.ENCHANTED_FLUID_BUCKET.get());
    }
}
