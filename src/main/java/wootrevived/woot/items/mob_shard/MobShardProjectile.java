package wootrevived.woot.items.mob_shard;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.registries.ItemsRegistry;

public class MobShardProjectile extends ThrowableItemProjectile {
    public static String MOB_SHARD_KILLED_BY_PROJECTILE = "MOB_SHARD_KILLED_BY_PROJECTILE";
    public static String MOB_SHARD_HAS_BEEN_KILLED = "MOB_SHARD_HAS_BEEN_KILLED";

    public MobShardProjectile(EntityType<MobShardProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public MobShardProjectile(LivingEntity owner, Level level, ItemStack item) {
        super(ItemsRegistry.MOB_SHARD_PROJECTILE.get(), owner, level, item);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ItemsRegistry.MOB_SHARD_ITEM.get();
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 vec3 = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = vec3.horizontalDistance();
            this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        if(this.isInLava()){
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result){
        if(!this.level().isClientSide()) {
            this.remove(RemovalReason.DISCARDED);

            BlockPos pos = result.getBlockPos();
            this.dropItem(this.getItem(), pos);
        }

        super.onHitBlock(result);
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);

        if(this.level() instanceof ServerLevel serverLevel) {
            Entity target = result.getEntity();
            Entity shooter = this.getOwner();
            ItemStack itemStack = this.getItem();

            if(shooter instanceof Player player){
                Entity realTarget = target;

                while(true){
                    if(realTarget instanceof PartEntity<?> partEntity){
                        realTarget = partEntity.getParent();
                    } else break;
                }

                DamageSources sources = new DamageSources(this.level().registryAccess());
                realTarget.addTag(MOB_SHARD_KILLED_BY_PROJECTILE);
                target.hurtServer(serverLevel, sources.playerAttack(player), 1f);

                if(realTarget instanceof Mob mob){
                    Item item = itemStack.getItem();
                    item.hurtEnemy(itemStack, mob, player);

                    if(realTarget.getTags().contains(MOB_SHARD_HAS_BEEN_KILLED)){
                        realTarget.removeTag(MOB_SHARD_HAS_BEEN_KILLED);
                        MobShardItem.incrementKills(itemStack, 1);
                    }
                }
                realTarget.removeTag(MOB_SHARD_KILLED_BY_PROJECTILE);
            }

            this.remove(RemovalReason.DISCARDED);

            BlockPos pos = result.getEntity().getOnPos();
            this.dropItem(itemStack, pos);
        }
    }

    private void dropItem(ItemStack itemStack, BlockPos pos){
        Level level = this.level();

        if(level.isClientSide()) return;

        if(level.getBlockState(pos.above()).isAir()) {
            pos = pos.above();
        } else if(level.getBlockState(pos.below()).isAir()) {
            pos = pos.below();
        } else if(level.getBlockState(pos.east()).isAir()) {
            pos = pos.east();
        } else if(level.getBlockState(pos.west()).isAir()) {
            pos = pos.west();
        } else if(level.getBlockState(pos.north()).isAir()) {
            pos = pos.north();
        } else if(level.getBlockState(pos.south()).isAir()) {
            pos = pos.south();
        }

        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();

        double d0 = EntityType.ITEM.getWidth();
        double d1 = 1.0D - d0;
        double d2 = d0 / 2.0D;
        double d3 = Math.floor(x) + level.random.nextDouble() * d1 + d2;
        double d4 = Math.floor(y) + level.random.nextDouble() * d1;
        double d5 = Math.floor(z) + level.random.nextDouble() * d1 + d2;

        while(!itemStack.isEmpty()) {
            ItemEntity itementity = new MobShardItemEntity(level, d3, d4, d5, itemStack.split(level.random.nextInt(21) + 10));
            itementity.setDeltaMovement(level.random.triangle(0.0D, 0.11485000171139836D), level.random.triangle(0.2D, 0.11485000171139836D), level.random.triangle(0.0D, 0.11485000171139836D));
            level.addFreshEntity(itementity);
        }
    }
}
