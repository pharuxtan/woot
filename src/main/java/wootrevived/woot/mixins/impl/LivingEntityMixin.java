package wootrevived.woot.mixins.impl;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityMixin {
    @Invoker("dropFromLootTable")
    void woot$dropFromLootTable(ServerLevel source, DamageSource damageSource, boolean hitByPlayer);

    @Invoker("dropCustomDeathLoot")
    void woot$dropCustomDeathLoot(ServerLevel source, DamageSource damageSource, boolean recentlyHit);

    @Invoker("dropEquipment")
    void woot$dropEquipment(ServerLevel source);

    @Invoker("dropExperience")
    void woot$dropExperience(ServerLevel source, Entity entity);
}
