package wootrevived.woot.guide;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import wootrevived.woot.Woot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GuideBookPersistentState extends SavedData {
    private final Set<String> receivedPlayers;

    public static final SavedDataType<GuideBookPersistentState> TYPE = new SavedDataType<>(
            Woot.MOD_NAMESPACE + "_guidebook",
            GuideBookPersistentState::new,
            level -> RecordCodecBuilder.create(inst -> inst.group(
                    Codec.STRING.listOf().xmap(Set::copyOf, List::copyOf).fieldOf("receivedPlayers").forGetter(state -> state.receivedPlayers)
            ).apply(inst, GuideBookPersistentState::new))
    );

    private GuideBookPersistentState(ServerLevel level) {
        this(new HashSet<>());
    }

    private GuideBookPersistentState(Set<String> receivedPlayers) {
        this.receivedPlayers = receivedPlayers;
    }

    public boolean hasPlayerReceivedGuideBook(Player player) {
        return receivedPlayers.contains(player.getStringUUID());
    }

    public void addPlayerReceivedGuideBook(Player player) {
        receivedPlayers.add(player.getStringUUID());
        setDirty();
    }

    public static GuideBookPersistentState get(MinecraftServer server){
        ServerLevel level = server.getLevel(ServerLevel.OVERWORLD);
        return level.getDataStorage().computeIfAbsent(TYPE);
    }
}
