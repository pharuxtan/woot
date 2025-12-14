package wootrevived.woot.drops.simulator;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.entity.EntityPersistentStorage;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.woot.Woot;
import wootrevived.woot.mixins.accessors.LevelMixinAccessor;
import wootrevived.woot.mixins.accessors.ServerLevelMixinAccessor;
import wootrevived.woot.mixins.impl.*;

import java.util.*;

public class DropSimulator {
    private static final DropSimulator INSTANCE = new DropSimulator();

    private final GameProfile gameProfile = new GameProfile(UUID.nameUUIDFromBytes(Woot.MOD_ID.getBytes()), Woot.MOD_ID);
    private ServerLevel dimensionLevel = null;
    private ResourceKey<Level> dimension;
    private Holder<DimensionType> dimensionTypeRegistration;
    private FakeServerPlayer fakePlayer = null;
    private FakeEntityManager<Entity> fakeEntityManager = null;
    private Creeper chargedCreeper = null;
    private DamageSource playerSource = null;
    private DamageSource chargedCreeperSource = null;

    public static void simulateDrops(WootDropsProperties properties) {
        INSTANCE.simulate(properties);
    }

    public static @NotNull ServerLevel getLevel() {
        return INSTANCE.dimensionLevel;
    }

    public static @NotNull RandomSource getRandom() {
        return INSTANCE.dimensionLevel.getRandom();
    }

    public static @NotNull RegistryAccess getRegistryAccess() {
        return INSTANCE.dimensionLevel.registryAccess();
    }

    public static @Nullable LivingEntity loadEntity(WootFactoryMob<?> entity, CompoundTag mobTag) {
        return entity.loadEntity(mobTag, INSTANCE.dimensionLevel);
    }

    public static void patchDimension(WootDropsProperties properties, boolean restore){
        ServerLevel level = INSTANCE.dimensionLevel, dropsLevel;
        MinecraftServer server = level.getServer();
        if(!restore && (dropsLevel = server.getLevel(properties.getDimension())) != null){
            ((LevelMixinAccessor) level).woot$setDimension(
                    dropsLevel.dimension(),
                    dropsLevel.dimensionTypeRegistration()
            );
        } else {
            ((LevelMixinAccessor) level).woot$setDimension(
                    INSTANCE.dimension,
                    INSTANCE.dimensionTypeRegistration
            );
        }
    }

    private void simulate(WootDropsProperties properties){
        LivingEntity livingEntity = properties.getEntity();

        if(livingEntity == null)
            return;

        ItemStack mainHand = properties.getMainHandItem();

        if(!livingEntity.fireImmune() && properties.isInFire()){
            livingEntity.setRemainingFireTicks(20);
            livingEntity.setSharedFlagOnFire(true);
        }

        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, mainHand);
        fakePlayer.setItemInHand(InteractionHand.OFF_HAND, properties.getOffHandItem());

        fakeEntityManager.clearEntityList();

        livingEntity.tickCount = 100;
        livingEntity.setLastHurtByPlayer(fakePlayer, 100);
        Holder<Attribute> luckAttributeHolder = Attributes.LUCK;
        RangedAttribute luckAttribute = (RangedAttribute)luckAttributeHolder.value();
        fakePlayer.getAttribute(luckAttributeHolder).setBaseValue(Mth.clamp(properties.getLuck(), luckAttribute.getMinValue(), luckAttribute.getMaxValue()));

        if(livingEntity instanceof EnderDragon enderDragon){
            simulateEnderdragon(enderDragon, properties);
            return;
        }

        livingEntity.captureDrops(new java.util.ArrayList<>());

        LivingEntityMixin mixin = (LivingEntityMixin)livingEntity;
        mixin.woot$dropFromLootTable(dimensionLevel, playerSource, true);
        mixin.woot$dropCustomDeathLoot(dimensionLevel, playerSource, true);
        mixin.woot$dropEquipment(dimensionLevel);
        mixin.woot$dropExperience(dimensionLevel, fakePlayer);

        Collection<ItemEntity> eventDrops = livingEntity.captureDrops(null);
        CommonHooks.onLivingDrops(livingEntity, playerSource, eventDrops, true);
        eventDrops.forEach(e -> dimensionLevel.addFreshEntity(e));

        List<ItemStack> drops = properties.getItemDrops();
        int experience = properties.getExperience();

        for(Entity droppedEntity : fakeEntityManager.getEntityList()){
            if(droppedEntity instanceof ItemEntity itemEntity){
                drops.add(itemEntity.getItem());
            } else if(droppedEntity instanceof ExperienceOrb experienceOrb){
                experience += experienceOrb.getValue();
            }
        }

        properties.setExperience(experience);

        fakeEntityManager.clearEntityList();

        if(properties.doSimulateChargedCreeper())
            drops.addAll(simulateChargedCreeper(livingEntity));
    }

    private @NotNull List<ItemStack> simulateChargedCreeper(@NotNull LivingEntity livingEntity){
        fakeEntityManager.clearEntityList();

        livingEntity.tickCount = 0;
        livingEntity.setLastHurtByPlayer((Player) null, 100);

        ((CreeperMixin) chargedCreeper).woot$setDroppedSkulls(0);

        livingEntity.captureDrops(null);

        LivingEntityMixin mixin = (LivingEntityMixin)livingEntity;
        mixin.woot$dropCustomDeathLoot(dimensionLevel, chargedCreeperSource, false);

        List<ItemStack> drops = new ArrayList<>();

        for(Entity droppedEntity : fakeEntityManager.getEntityList()){
            if(droppedEntity instanceof ItemEntity itemEntity){
                drops.add(itemEntity.getItem());
            }
        }

        fakeEntityManager.clearEntityList();

        return drops;
    }

    private void simulateEnderdragon(@NotNull EnderDragon enderDragon, WootDropsProperties properties){
        fakeEntityManager.clearEntityList();

        EndDragonFight.Data data = new EndDragonFight.Data(false, false, properties.isEnderDragonAlreadyKilled(), false, Optional.empty(), Optional.empty(), Optional.empty());

        enderDragon.setDragonFight(new FakeDragonFight(dimensionLevel, dimensionLevel.getSeed(), data));
        enderDragon.setSilent(true);

        EnderDragonMixin dragonMixin = (EnderDragonMixin)enderDragon;
        dragonMixin.woot$setUnlimitedLastHurtByPlayer(new EntityReference<>(fakePlayer));

        for(enderDragon.dragonDeathTime = 0; !((EndDragonFightMixin) enderDragon.getDragonFight()).woot$getDragonKilled();){
            dragonMixin.woot$tickDeath();
        }

        enderDragon.captureDrops(new java.util.ArrayList<>());

        LivingEntityMixin mixin = (LivingEntityMixin)enderDragon;
        mixin.woot$dropFromLootTable(dimensionLevel, playerSource, true);
        mixin.woot$dropCustomDeathLoot(dimensionLevel, playerSource, true);
        mixin.woot$dropEquipment(dimensionLevel);
        mixin.woot$dropExperience(dimensionLevel, fakePlayer);

        Collection<ItemEntity> eventDrops = enderDragon.captureDrops(null);
        CommonHooks.onLivingDrops(enderDragon, playerSource, eventDrops, true);
        eventDrops.forEach(e -> dimensionLevel.addFreshEntity(e));

        List<ItemStack> drops = properties.getItemDrops();
        int experience = properties.getExperience();

        for(Entity droppedEntity : fakeEntityManager.getEntityList()){
            if(droppedEntity instanceof ItemEntity itemEntity){
                drops.add(itemEntity.getItem());
            } else if(droppedEntity instanceof ExperienceOrb experienceOrb){
                experience += experienceOrb.getValue();
            }
        }

        properties.setExperience(experience);

        fakeEntityManager.clearEntityList();
    }

    public static void init(ServerLevel dimensionLevel){
        INSTANCE.setup(dimensionLevel);
    }

    private void setup(ServerLevel dimensionLevel){
        setDimensionLevel(dimensionLevel);
        patchDimensionLevel();
        initFakePlayer();
        initDamageSources();
    }

    private void setDimensionLevel(ServerLevel level){
        dimensionLevel = level;
        dimension = level.dimension();
        dimensionTypeRegistration = level.dimensionTypeRegistration();
    }

    private void initFakePlayer(){
        fakePlayer = new FakeServerPlayer(dimensionLevel, gameProfile);
    }

    private void initDamageSources(){
        DamageSources sources = dimensionLevel.damageSources();

        CompoundTag creeper = new CompoundTag();
        creeper.putString("id", "minecraft:creeper");
        creeper.putBoolean("powered", true);
        chargedCreeper = (Creeper)EntityType.loadEntityRecursive(creeper, dimensionLevel, EntitySpawnReason.SPAWNER, e -> e);

        playerSource = sources.playerAttack(fakePlayer);
        chargedCreeperSource = sources.explosion(chargedCreeper, chargedCreeper);
    }

    private void patchDimensionLevel(){
        ServerLevelMixinAccessor level = (ServerLevelMixinAccessor)dimensionLevel;

        PersistentEntitySectionManager<Entity> persistentEntitySectionManager = level.woot$getEntityManager();
        PersistentEntitySectionManagerMixin entitySectionMixin = (PersistentEntitySectionManagerMixin)persistentEntitySectionManager;
        EntityPersistentStorage<Entity> entityPersistentStorage = entitySectionMixin.woot$getPermanentStorage();

        fakeEntityManager = new FakeEntityManager<>(Entity.class, entitySectionMixin.woot$getCallbacks(), entityPersistentStorage);

        level.woot$setEntityManager(fakeEntityManager);
    }
}
