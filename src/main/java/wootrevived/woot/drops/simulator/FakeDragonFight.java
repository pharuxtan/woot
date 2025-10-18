package wootrevived.woot.drops.simulator;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.mixins.impl.EndDragonFightMixin;

public class FakeDragonFight extends EndDragonFight {
    public FakeDragonFight(ServerLevel level, long seed, Data data) {
        super(level, seed, data);
    }

    @Override
    public void setDragonKilled(@NotNull EnderDragon dragon) {
        ((EndDragonFightMixin) this).woot$setDragonKilled(true);
    }
}
