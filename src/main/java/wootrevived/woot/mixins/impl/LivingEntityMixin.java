package wootrevived.woot.mixins.impl;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityMixin {
    @Invoker("dropFromLootTable")
    void woot$dropFromLootTable(DamageSource damageSource, boolean hitByPlayer);

    @Invoker("dropCustomDeathLoot")
    void woot$dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit);

    @Invoker("dropEquipment")
    void woot$dropEquipment();

    @Invoker("dropExperience")
    void woot$dropExperience();
}
