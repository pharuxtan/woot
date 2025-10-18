package wootrevived.woot.mixins.accessors;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;

public interface ServerLevelMixinAccessor {
    PersistentEntitySectionManager<Entity> woot$getEntityManager();
    void woot$setEntityManager(PersistentEntitySectionManager<Entity> entityManager);
}
