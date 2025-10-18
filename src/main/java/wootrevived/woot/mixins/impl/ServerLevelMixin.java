package wootrevived.woot.mixins.impl;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import wootrevived.woot.mixins.accessors.ServerLevelMixinAccessor;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements ServerLevelMixinAccessor {
    @Shadow @Final @Mutable
    private PersistentEntitySectionManager<Entity> entityManager;

    public PersistentEntitySectionManager<Entity> woot$getEntityManager() {
        return entityManager;
    }

    public void woot$setEntityManager(PersistentEntitySectionManager<Entity> entityManager) {
        this.entityManager = entityManager;
    }
}
