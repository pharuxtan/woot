package wootrevived.woot.events;

import net.minecraft.locale.Language;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import wootrevived.api.WootFactoryMob;
import wootrevived.woot.Woot;
import wootrevived.woot.items.mob_shard.MobShardItem;
import wootrevived.woot.items.mob_shard.MobShardProjectile;
import wootrevived.woot.registries.WootFactoryMobsRegistry;
import wootrevived.woot.util.helper.SerializeEntityValueHelper;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Woot.MOD_ID)
public class MobShardKillCheck {
    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        if(!(event.getSource().getEntity() instanceof Player killer))
            return;

        if(killer instanceof FakePlayer)
            return;

        if(event.getEntity() == null)
            return;

        if(event.getSource().getDirectEntity() != null && event.getEntity().getUUID().equals(event.getSource().getDirectEntity().getUUID()))
            return;

        LivingEntity victim = event.getEntity();

        if(ignoreDeathEvent(event.getEntity()))
            return;

        if(victim instanceof Player)
            return;

        EntityType<?> entityType = victim.getType();

        if(!Language.getInstance().has(entityType.getDescriptionId()))
            return;

        if(!WootFactoryMobsRegistry.hasFactoryMob(entityType))
            return;

        WootFactoryMob<?> mob = WootFactoryMobsRegistry.getFactoryMob(entityType);
        if(mob.isBlacklisted())
            return;

        victim.addTag(MobShardProjectile.MOB_SHARD_HAS_BEEN_KILLED);
        if(victim.getTags().contains(MobShardProjectile.MOB_SHARD_KILLED_BY_PROJECTILE))
            return;

        ItemStack inHandItemStack = killer.getMainHandItem();

        if(inHandItemStack.getItem() instanceof MobShardItem mobShardItem && !MobShardItem.isProgrammed(inHandItemStack)) {
            mobShardItem.hurtEnemy(inHandItemStack, victim, killer);
        }

        MobShardItem.handleKill(killer, SerializeEntityValueHelper.serialize(victim, event.getEntity().registryAccess()));
    }

    private static final List<String> uuidList = new ArrayList<>();
    private static boolean ignoreDeathEvent(Entity entity) {
        String uuid = entity.getStringUUID();
        if (uuidList.contains(uuid))
            return true;

        uuidList.add(uuid);
        int MAX_UUID_CACHE_SIZE = 10;
        if (uuidList.size() > MAX_UUID_CACHE_SIZE)
            uuidList.remove(0);

        return false;
    }
}
