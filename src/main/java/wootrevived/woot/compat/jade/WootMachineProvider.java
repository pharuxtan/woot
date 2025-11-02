package wootrevived.woot.compat.jade;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.StreamServerDataProvider;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.JadeUI;
import snownee.jade.api.ui.MessageType;
import snownee.jade.api.view.ProgressView;
import wootrevived.woot.Woot;
import wootrevived.woot.util.entity.WootMachineBlockEntity;

import java.util.List;

public enum WootMachineProvider implements StreamServerDataProvider<BlockAccessor, WootMachineProvider.Data> {
    INSTANCE;

    @Override
    public Data streamData(BlockAccessor accessor) {
        WootMachineBlockEntity entity = (WootMachineBlockEntity)accessor.getBlockEntity();
        return new Data(entity.calculateProgress());
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, Data> streamCodec() {
        return Data.STREAM_CODEC;
    }

    @Override
    public ResourceLocation getUid() {
        return Woot.location("machines");
    }

    public record Data(int progress) {
        public static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Data::progress,
                Data::new
        );
    }

    public enum Client implements IBlockComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            var data = WootMachineProvider.INSTANCE.decodeFromData(accessor).orElse(null);
            if (data != null) {
                ProgressView view = new ProgressView(JadeUI.progressStyle(), BoxStyle.nestedBox());
                if (data.progress() > 0)
                    view.parts = List.of(ProgressView.Part.of((float)data.progress() / 100.0F, MessageType.INFO));
                tooltip.add(JadeUI.progress(view));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return Woot.location("machines");
        }
    }
}
