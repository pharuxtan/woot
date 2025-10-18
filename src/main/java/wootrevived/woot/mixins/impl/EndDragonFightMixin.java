package wootrevived.woot.mixins.impl;

import net.minecraft.world.level.dimension.end.EndDragonFight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EndDragonFight.class)
public interface EndDragonFightMixin {
    @Accessor(value = "dragonKilled")
    boolean woot$getDragonKilled();

    @Accessor(value = "dragonKilled")
    void woot$setDragonKilled(boolean dragonKilled);
}
