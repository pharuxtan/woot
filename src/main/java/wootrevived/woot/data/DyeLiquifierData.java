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

public final class DyeLiquifierData {
    public static final String ID = "dye_liquifier_data";

    public static final Codec<Component> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.INT.fieldOf(WootTags.ENERGY_TAG).forGetter(Component::energy),
                    Codec.INT.fieldOf(WootTags.DyeLiquifier.RED_TAG).forGetter(Component::red),
                    Codec.INT.fieldOf(WootTags.DyeLiquifier.YELLOW_TAG).forGetter(Component::yellow),
                    Codec.INT.fieldOf(WootTags.DyeLiquifier.BLUE_TAG).forGetter(Component::blue),
                    Codec.INT.fieldOf(WootTags.DyeLiquifier.WHITE_TAG).forGetter(Component::white),
                    FluidStack.OPTIONAL_CODEC.fieldOf(WootTags.OUTPUT_TANK_TAG).forGetter(Component::outputFluid),
                    WootCodecs.MACHINE_PROPERTIES_CODEC.fieldOf(WootTags.DirectionProperties.LIST).forGetter(Component::listMachineProperties)
            ).apply(inst, Component::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Component> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public Component decode(RegistryFriendlyByteBuf buf) {
            int energy = ByteBufCodecs.INT.decode(buf);
            int red = ByteBufCodecs.INT.decode(buf);
            int yellow = ByteBufCodecs.INT.decode(buf);
            int blue = ByteBufCodecs.INT.decode(buf);
            int white = ByteBufCodecs.INT.decode(buf);
            FluidStack outputFluid = FluidStack.OPTIONAL_STREAM_CODEC.decode(buf);
            List<EnumMap<MachineSide, MachineSideProperty>> listMachineProperties = WootCodecs.MACHINE_PROPERTIES_STREAM_CODEC.decode(buf);
            return new Component(energy, red, yellow, blue, white, outputFluid, listMachineProperties);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, Component component) {
            ByteBufCodecs.INT.encode(buf, component.energy);
            ByteBufCodecs.INT.encode(buf, component.red);
            ByteBufCodecs.INT.encode(buf, component.yellow);
            ByteBufCodecs.INT.encode(buf, component.blue);
            ByteBufCodecs.INT.encode(buf, component.white);
            FluidStack.OPTIONAL_STREAM_CODEC.encode(buf, component.outputFluid);
            WootCodecs.MACHINE_PROPERTIES_STREAM_CODEC.encode(buf, component.listMachineProperties);
        }
    };

    public record Component(
            int energy,
            int red,
            int yellow,
            int blue,
            int white,
            FluidStack outputFluid,
            List<EnumMap<MachineSide, MachineSideProperty>> listMachineProperties
    ) {
        @Override
        public int hashCode() {
            return energy + red + yellow + blue + white +
                    FluidStack.hashFluidAndComponents(outputFluid) +
                    listMachineProperties.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Component c))
                return false;

            return c.energy == energy &&
                    c.red == red &&
                    c.yellow == yellow &&
                    c.blue == blue &&
                    c.white == white &&
                    FluidStack.isSameFluidSameComponents(c.outputFluid, outputFluid) &&
                    c.listMachineProperties.equals(listMachineProperties);
        }
    }
}
