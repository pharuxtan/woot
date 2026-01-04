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
import wootrevived.woot.blocks.fake_spawner.FakeSpawnerBlockEntity;
import wootrevived.woot.util.common.RedstoneMode;

public record WootFakeSpawnerUpdate(BlockPos blockPos, RedstoneMode redstoneMode) implements CustomPacketPayload {
    public static final Identifier ID = Woot.identifier("woot_fake_spawner_update");
    public static final Type<WootFakeSpawnerUpdate> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, WootFakeSpawnerUpdate> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, WootFakeSpawnerUpdate::blockPos,
            NeoForgeStreamCodecs.enumCodec(RedstoneMode.class), WootFakeSpawnerUpdate::redstoneMode,
            WootFakeSpawnerUpdate::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handler(final WootFakeSpawnerUpdate pkt, final IPayloadContext ctx){
        ctx.enqueueWork(() -> {
            Player sender = ctx.player();
            if (!(sender instanceof ServerPlayer player)) return;
            if (!player.level().isLoaded(pkt.blockPos)) return;

            BlockEntity blockEntity = sender.level().getBlockEntity(pkt.blockPos);
            if (blockEntity instanceof FakeSpawnerBlockEntity fakeSpawnerBlockEntity && fakeSpawnerBlockEntity.canPlayerAccess(player)) {
                fakeSpawnerBlockEntity.handleNewState(pkt);
            }
        });
    }
}
