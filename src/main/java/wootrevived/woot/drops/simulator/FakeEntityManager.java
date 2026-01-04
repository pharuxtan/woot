package wootrevived.woot.drops.simulator;

import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntityPersistentStorage;
import net.minecraft.world.level.entity.LevelCallback;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FakeEntityManager<T extends EntityAccess> extends PersistentEntitySectionManager<T> {
    private final List<T> entityList = new ArrayList<>();

    public FakeEntityManager(Class<T> entityClass, LevelCallback<T> callbacks, EntityPersistentStorage<T> permanentStorage) {
        super(entityClass, callbacks, permanentStorage);
    }

    public void clearEntityList(){
        entityList.clear();
    }

    public List<T> getEntityList(){
        return entityList;
    }

    @Override
    public boolean addNewEntity(T entity) {
        entityList.add(entity);
        return false;
    }

    @Override
    public boolean addNewEntityWithoutEvent(T entity) {
        entityList.add(entity);
        return false;
    }

    @Override
    public void addLegacyChunkEntities(Stream<T> entities) {
        entities.forEach(entityList::add);
    }

    @Override
    public void addWorldGenChunkEntities(Stream<T> entities) {
        entities.forEach(entityList::add);
    }
}
