package wootrevived.woot.util.render;

import net.minecraft.core.Direction;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jspecify.annotations.Nullable;
import wootrevived.woot.util.common.MachineSide;
import wootrevived.woot.util.common.MachineSideProperty;
import wootrevived.woot.util.common.RedstoneMode;
import wootrevived.woot.util.entity.WootMachineBlockEntity;

import java.util.Map;

public abstract class WootContainerMenu extends AbstractContainerMenu {
    public WootContainerData data;

    protected WootContainerMenu(@Nullable MenuType<?> menuType, int containerId) { super(menuType, containerId); }

    public int getEnergy(){
        return data.get(WootMachineBlockEntity.DATA_ENERGY);
    }

    public int getProgress(){
        return data.get(WootMachineBlockEntity.DATA_PROGRESS);
    }

    public int getEnergyProcessTransfer(){
        return data.get(WootMachineBlockEntity.DATA_ENERGY_TRANSFER);
    }

    public float getLeftSeconds(){
        return data.getFloat(WootMachineBlockEntity.DATA_LEFT_SECOND);
    }

    public FluidStack getInputFluid(){
        return data.getFluid(WootMachineBlockEntity.DATA_FLUID_INPUT);
    }

    public FluidStack getOutputFluid(){
        return data.getFluid(WootMachineBlockEntity.DATA_FLUID_OUTPUT);
    }

    public Direction getMachineFacing(){
        return data.getMachineFacing();
    }

    public RedstoneMode getRedstoneMode(){
        return data.getRedstoneMode();
    }

    public void setRedstoneMode(RedstoneMode mode){
        data.setRedstoneMode(mode);
    }

    public Map<MachineSide, MachineSideProperty> getMachineSideProperties(int index) {
        return data.getMachineSideProperties(index);
    }

    public void setMachineSideProperties() {
        data.setMachineSideProperties();
    }

}
