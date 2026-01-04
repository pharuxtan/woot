package wootrevived.woot.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.fluids.FluidStack;
import wootrevived.woot.util.common.MachineSide;
import wootrevived.woot.util.common.MachineSideProperty;
import wootrevived.woot.util.common.WootCodecs;
import wootrevived.woot.util.entity.WootTags;

import java.util.EnumMap;
import java.util.List;

public final class ItemInfuserData {
    public static final String ID = "item_infuser_data";

    public static final Codec<Component> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.INT.fieldOf(WootTags.ENERGY_TAG).forGetter(Component::energy),
                    FluidStack.OPTIONAL_CODEC.fieldOf(WootTags.INPUT_TANK_TAG).forGetter(Component::inputFluid),
                    WootCodecs.MACHINE_PROPERTIES_CODEC.fieldOf(WootTags.DirectionProperties.LIST).forGetter(Component::listMachineProperties)
            ).apply(inst, Component::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Component> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, Component::energy,
            FluidStack.OPTIONAL_STREAM_CODEC, Component::inputFluid,
            WootCodecs.MACHINE_PROPERTIES_STREAM_CODEC, Component::listMachineProperties,
            Component::new
    );

    public record Component(
            int energy,
            FluidStack inputFluid,
            List<EnumMap<MachineSide, MachineSideProperty>> listMachineProperties
    ) {
        @Override
        public int hashCode() {
            return energy +
                    FluidStack.hashFluidAndComponents(inputFluid) +
                    listMachineProperties.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Component c))
                return false;

            return c.energy == energy &&
                    FluidStack.isSameFluidSameComponents(c.inputFluid, inputFluid) &&
                    c.listMachineProperties.equals(listMachineProperties);
        }
    }
}
