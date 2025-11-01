package wootrevived.woot.registries;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.registrations.WootFactoryMobRegistration;
import wootrevived.woot.init.WootPlugins;

import java.util.*;
import java.util.function.Consumer;

public class WootFactoryMobsRegistry extends WootFactoryMobRegistration {
    public static void register(){
        WootPlugins.registerFactoryMobs(new WootFactoryMobsRegistry());

        for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
            if (hasFactoryMob(entityType))
                continue;

            if (!entityType.canSerialize())
                continue;

            FACTORY_MOB_REGISTRY.put(entityType, new WootFactoryMob<>(entityType, new WootFactoryMob.Properties()));
        }
    }

    private static final Map<EntityType<?>, WootFactoryMob<?>> FACTORY_MOB_REGISTRY = new HashMap<>();

    @Override
    public void registerFactoryMob(WootFactoryMob<?> mob){
        FACTORY_MOB_REGISTRY.put(mob.getEntityType(), mob);
    }

    public static WootFactoryMob<?> getFactoryMob(CompoundTag mobTag){
        return getFactoryMob(mobTag.getString("id").orElse("minecraft:pig"));
    }

    public static WootFactoryMob<?> getFactoryMob(String mobId){
        Holder.Reference<EntityType<?>> entityType = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.tryParse(mobId)).orElse(null);
        if(entityType == null) return null;
        return getFactoryMob(entityType.getDelegate().value());
    }

    public static WootFactoryMob<?> getFactoryMob(EntityType<?> entityType){
        return FACTORY_MOB_REGISTRY.get(entityType);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasFactoryMob(CompoundTag mobTag){
        return hasFactoryMob(mobTag.getString("id").orElse("minecraft:pig"));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasFactoryMob(String mobId){
        Holder.Reference<EntityType<?>> entityType = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.tryParse(mobId)).orElse(null);
        if(entityType == null) return false;
        return hasFactoryMob(entityType.getDelegate().value());
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasFactoryMob(EntityType<?> entityType){
        return FACTORY_MOB_REGISTRY.containsKey(entityType);
    }

    public static void removeFactoryMob(EntityType<?> entityType){
        FACTORY_MOB_REGISTRY.remove(entityType);
    }

    public static Collection<WootFactoryMob<?>> getFactoryMobValues(){
        return FACTORY_MOB_REGISTRY.values();
    }

    private static final Map<EntityType<?>, List<Consumer<WootDropsProperties>>> ITEM_DROPS_REGISTRY = new HashMap<>();

    @Override
    public void registerDropsModifier(EntityType<?> entityType, Consumer<WootDropsProperties> callback) {
        if(!ITEM_DROPS_REGISTRY.containsKey(entityType)) ITEM_DROPS_REGISTRY.put(entityType, new ArrayList<>());
        ITEM_DROPS_REGISTRY.get(entityType).add(callback);
    }

    public static List<Consumer<WootDropsProperties>> getDropsModifier(CompoundTag mobTag){
        return getDropsModifier(mobTag.getString("id").orElse("minecraft:pig"));
    }

    public static List<Consumer<WootDropsProperties>> getDropsModifier(String mobId){
        Holder.Reference<EntityType<?>> entityType = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.tryParse(mobId)).orElse(null);
        if(entityType == null) return List.of();
        return getDropsModifier(entityType.getDelegate().value());
    }

    public static List<Consumer<WootDropsProperties>> getDropsModifier(EntityType<?> entityType){
        if(!hasDropsModifier(entityType)) return List.of();
        return ITEM_DROPS_REGISTRY.get(entityType);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasDropsModifier(CompoundTag mobTag){
        return hasDropsModifier(mobTag.getString("id").orElse("minecraft:pig"));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasDropsModifier(String mobId){
        Holder.Reference<EntityType<?>> entityType = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.tryParse(mobId)).orElse(null);
        if(entityType == null) return false;
        return hasDropsModifier(entityType.getDelegate().value());
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasDropsModifier(EntityType<?> entityType){
        return ITEM_DROPS_REGISTRY.containsKey(entityType);
    }

    private static final List<Consumer<WootDropsProperties>> GLOBAL_ITEM_DROPS_REGISTRY = new ArrayList<>();

    @Override
    public void registerGlobalDropsModifier(Consumer<WootDropsProperties> callback) {
        GLOBAL_ITEM_DROPS_REGISTRY.add(callback);
    }

    public static List<Consumer<WootDropsProperties>> getGlobalDropsModifier(){
        return GLOBAL_ITEM_DROPS_REGISTRY;
    }
}
