package wootrevived.woot.mixins.impl;

import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Creeper.class)
public interface CreeperMixin {
    @Accessor(value = "droppedSkulls")
    void woot$setDroppedSkulls(int droppedSkulls);
}
