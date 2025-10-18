package wootrevived.woot.mixins.impl;

import net.minecraft.world.level.entity.EntityPersistentStorage;
import net.minecraft.world.level.entity.LevelCallback;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PersistentEntitySectionManager.class)
public interface PersistentEntitySectionManagerMixin {
    @Accessor(value = "callbacks")
    <T> LevelCallback<T> woot$getCallbacks();

    @Accessor(value = "permanentStorage")
    <T> EntityPersistentStorage<T> woot$getPermanentStorage();
}
