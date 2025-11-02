package wootrevived.woot.compat.jade;

import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import wootrevived.woot.blocks.dye_liquifier.DyeLiquifierBlock;
import wootrevived.woot.blocks.enchanted_liquifier.EnchantedLiquifierBlock;
import wootrevived.woot.blocks.fluid_infuser.FluidInfuserBlock;
import wootrevived.woot.blocks.item_infuser.ItemInfuserBlock;
import wootrevived.woot.util.entity.WootMachineBlockEntity;

@WailaPlugin
public class WootJadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(WootMachineProvider.INSTANCE, WootMachineBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(WootMachineProvider.Client.INSTANCE, ItemInfuserBlock.class);
        registration.registerBlockComponent(WootMachineProvider.Client.INSTANCE, FluidInfuserBlock.class);
        registration.registerBlockComponent(WootMachineProvider.Client.INSTANCE, DyeLiquifierBlock.class);
        registration.registerBlockComponent(WootMachineProvider.Client.INSTANCE, EnchantedLiquifierBlock.class);
    }
}
