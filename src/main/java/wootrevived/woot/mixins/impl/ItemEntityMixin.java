package wootrevived.woot.mixins.impl;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wootrevived.woot.items.mob_shard.MobShardItemEntity;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Inject(
            method = "hurtClient(Lnet/minecraft/world/damagesource/DamageSource;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void woot$hurtClient(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if((Object) this instanceof MobShardItemEntity){
            if(!source.is(DamageTypeTags.IS_FIRE)){
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }

    @Inject(
            method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void woot$hurtServer(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if((Object) this instanceof MobShardItemEntity){
            if(!source.is(DamageTypeTags.IS_FIRE)){
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
}
