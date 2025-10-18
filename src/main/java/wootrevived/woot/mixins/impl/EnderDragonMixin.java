package wootrevived.woot.mixins.impl;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EnderDragon.class)
public interface EnderDragonMixin {
    @Accessor(value = "unlimitedLastHurtByPlayer", remap = false)
    void woot$setUnlimitedLastHurtByPlayer(Player unlimitedLastHurtByPlayer);

    @Invoker("tickDeath")
    void woot$tickDeath();
}
