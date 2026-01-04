package wootrevived.woot.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import wootrevived.woot.Woot;
import wootrevived.woot.util.common.MachineSide;
import wootrevived.woot.util.common.MachineSideProperty;
import wootrevived.woot.util.common.RedstoneMode;
import wootrevived.woot.util.common.WootCodecs;
import wootrevived.woot.util.entity.WootMachineBlockEntity;

import java.util.EnumMap;
import java.util.List;

public record WootMachineUpdate(BlockPos blockPos, RedstoneMode redstoneMode,
                                List<EnumMap<MachineSide, MachineSideProperty>> listMachineProperties) implements CustomPacketPayload {
    public static final Identifier ID = Woot.identifier("woot_machine_update");
    public static final Type<WootMachineUpdate> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, WootMachineUpdate> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, WootMachineUpdate::blockPos,
            NeoForgeStreamCodecs.enumCodec(RedstoneMode.class), WootMachineUpdate::redstoneMode,
            WootCodecs.MACHINE_PROPERTIES_STREAM_CODEC, WootMachineUpdate::listMachineProperties,
            WootMachineUpdate::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handler(final WootMachineUpdate pkt, final IPayloadContext ctx){
        ctx.enqueueWork(() -> {
            Player sender = ctx.player();
            if (!(sender instanceof ServerPlayer player)) return;
            if (!player.level().isLoaded(pkt.blockPos)) return;

            BlockEntity blockEntity = player.level().getBlockEntity(pkt.blockPos);
            if (blockEntity instanceof WootMachineBlockEntity wootMachineBlockEntity && wootMachineBlockEntity.canPlayerAccess(player)) {
                wootMachineBlockEntity.handleNewState(pkt);
            }
        });
    }
}
