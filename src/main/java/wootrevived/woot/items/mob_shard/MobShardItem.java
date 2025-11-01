package wootrevived.woot.items.mob_shard;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.WootFactoryMob;
import wootrevived.woot.Woot;
import wootrevived.woot.config.MobShardConfig;
import wootrevived.woot.data.MobShardData;
import wootrevived.woot.mixins.impl.InventoryMixin;
import wootrevived.woot.registries.ComponentsRegistry;
import wootrevived.woot.registries.ItemsRegistry;
import wootrevived.woot.registries.WootFactoryMobsRegistry;
import wootrevived.woot.util.helper.ModNameHelper;
import wootrevived.woot.util.helper.SerializeEntityNBTHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static wootrevived.woot.util.render.WootStyles.*;

public class MobShardItem extends Item {
    public MobShardItem(String tag) {
        super(new Properties().stacksTo(1)
                .setId(ResourceKey.create(Registries.ITEM, Woot.location(tag)))
                .component(ComponentsRegistry.MOB_SHARD_ITEM_TOOLTIP, Tooltip.INSTANCE)
                .component(ComponentsRegistry.MOB_SHARD_DATA, new MobShardData.Component(Optional.empty(), 0, false)));
    }

    @Override
    public void hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, LivingEntity tmpAttacker) {
        if (tmpAttacker.getCommandSenderWorld().isClientSide() || !(tmpAttacker instanceof Player))
            return;

        if(!WootFactoryMobsRegistry.hasFactoryMob(target.getType()))
            return;

        WootFactoryMob<?> mob = WootFactoryMobsRegistry.getFactoryMob(target.getType());
        if(mob.isBlacklisted())
            return;

        if (isProgrammed(stack))
            return;

        setProgrammedMob(stack, mob.saveTag(SerializeEntityNBTHelper.serialize(target), target.level().registryAccess()));
    }

    public static boolean isProgrammed(DataComponentGetter getter) {
        MobShardData.Component component = getter.get(ComponentsRegistry.MOB_SHARD_DATA);
        if(component == null)
            return false;

        return component.mobTag().isPresent();
    }

    public static CompoundTag getProgrammedMob(DataComponentGetter getter) {
        MobShardData.Component component = getter.get(ComponentsRegistry.MOB_SHARD_DATA);
        if(component == null)
            return null;

        return component.mobTag().orElse(null);
    }

    private boolean setProgrammedMob(ItemStack itemStack, CompoundTag mobTag) {
        if(!WootFactoryMobsRegistry.hasFactoryMob(mobTag))
            return false;

        itemStack.set(ComponentsRegistry.MOB_SHARD_DATA, new MobShardData.Component(
                Optional.ofNullable(mobTag),
                0,
                false
        ));

        return true;
    }

    private static boolean isMatchingMob(ItemStack itemStack, CompoundTag mobTag, Level level) {
        if(itemStack.getItem() != ItemsRegistry.MOB_SHARD_ITEM.get())
            return false;

        if(!isProgrammed(itemStack))
            return false;

        CompoundTag programmedMob = getProgrammedMob(itemStack);
        if(programmedMob == null)
            return false;

        WootFactoryMob<?> mob = WootFactoryMobsRegistry.getFactoryMob(programmedMob);

        return mob.isSame(programmedMob, mobTag, level.registryAccess());
    }

    public static void handleKill(Player player, CompoundTag mobTag) {
        ItemStack foundStack = ItemStack.EMPTY;

        ItemStack inHandItemStack = player.getMainHandItem();

        if(!inHandItemStack.isEmpty() && isMatchingMob(inHandItemStack, mobTag, player.level()) && !isFullyProgrammed(inHandItemStack)){
            foundStack = inHandItemStack;
        } else {
            List<ItemStack> inventoryItems = new ArrayList<>();
            inventoryItems.add(player.getOffhandItem());
            inventoryItems.addAll(((InventoryMixin) player.getInventory()).woot$getItems());

            for(ItemStack itemStack : inventoryItems) {
                if(inHandItemStack.equals(itemStack)) continue;
                if(!itemStack.isEmpty() && isMatchingMob(itemStack, mobTag, player.level()) && !isFullyProgrammed(itemStack)){
                    foundStack = itemStack;
                    break;
                }
            }
        }

        if(!foundStack.isEmpty())
            incrementKills(foundStack, 1);
    }

    public static void incrementKills(ItemStack itemStack, int amount) {
        if(itemStack.getItem() != ItemsRegistry.MOB_SHARD_ITEM.get())
            return;

        MobShardData.Component component = itemStack.get(ComponentsRegistry.MOB_SHARD_DATA);
        if(component == null)
            return;

        int killCount = component.killCount();
        if(!isFull(itemStack)){
            killCount += amount;
            itemStack.set(ComponentsRegistry.MOB_SHARD_DATA, new MobShardData.Component(component.mobTag(), killCount, false));
        }
    }

    private static boolean isFull(DataComponentGetter getter) {
        MobShardData.Component component = getter.get(ComponentsRegistry.MOB_SHARD_DATA);
        if(component == null)
            return false;

        if(component.mobTag().isEmpty())
            return false;

        return component.killCount() >= MobShardConfig.NUM_OF_KILLS.get();
    }

    public static void setJEIShard(ItemStack itemStack) {
        itemStack.set(ComponentsRegistry.MOB_SHARD_DATA, new MobShardData.Component(
                Optional.empty(),
                0,
                true
        ));
    }

    public static boolean isJEIShard(DataComponentGetter getter) {
        MobShardData.Component component = getter.get(ComponentsRegistry.MOB_SHARD_DATA);
        if(component == null)
            return false;

        return component.jeiShard();
    }

    public static boolean isFullyProgrammed(ItemStack itemStack) {
        return isProgrammed(itemStack) && isFull(itemStack);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemStack) {
        if (isFullyProgrammed(itemStack)) return true;
        return isJEIShard(itemStack);
    }

    @Override
    public InteractionResult use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand){
        ItemStack itemStack = player.getItemInHand(usedHand);

        if(isProgrammed(itemStack))
            return InteractionResult.FAIL;

        player.startUsingItem(usedHand);
        return InteractionResult.CONSUME;
    }

    @Override
    public @NotNull ItemUseAnimation getUseAnimation(@NotNull ItemStack stack) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public boolean releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, int timeLeft){
        if(!(entity instanceof Player player)) return false;
        int used = this.getUseDuration(stack, entity) - timeLeft;
        float pull = Math.min(used / 20f, 1f);

        MobShardProjectile proj = new MobShardProjectile(player, level, stack);
        proj.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, pull * 2f, 1f - pull * 0.5f);
        if(!level.isClientSide) level.addFreshEntity(proj);
        stack.shrink(1);
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(ItemStack stack, TooltipContext ctx, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag tooltipFlag){
        if(display.shows(ComponentsRegistry.MOB_SHARD_ITEM_TOOLTIP.get()))
            components().get(ComponentsRegistry.MOB_SHARD_ITEM_TOOLTIP.get()).addToTooltip(ctx, consumer, tooltipFlag, stack.getComponents());
    }

    public record Tooltip() implements TooltipProvider {
        public static final Tooltip INSTANCE = new Tooltip();

        public static final String ID = "mob_shard_item_tooltip";
        public static final Codec<Tooltip> CODEC = Codec.unit(INSTANCE);
        public static final StreamCodec<ByteBuf, Tooltip> STREAM_CODEC = StreamCodec.unit(INSTANCE);

        @Override
        public void addToTooltip(TooltipContext ctx, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
            if(FMLEnvironment.dist.isDedicatedServer())
                return;

            if(isJEIShard(dataComponentGetter)) {
                consumer.accept(Component.translatable("info.woot_revived.mobshard.programmed").setStyle(SHARD_PROGRAM_STYLE));
                return;
            }

            CompoundTag mobTag = getProgrammedMob(dataComponentGetter);
            if(mobTag == null){
                consumer.accept(Component.translatable("info.woot_revived.mobshard.unprogrammed").setStyle(SHARD_PROGRAM_STYLE));
                consumer.accept(Component.translatable("info.woot_revived.mobshard.unprogrammed.desc").setStyle(DESCRIPTION_STYLE));
                return;
            }

            WootFactoryMob<?> mob = WootFactoryMobsRegistry.getFactoryMob(mobTag);
            if(mob != null) {
                consumer.accept(mob.getDisplayName(mobTag, gatherRegistry(ctx)).setStyle(CAPTURED_STYLE));
                String modId = BuiltInRegistries.ENTITY_TYPE.getKey(mob.getEntityType()).getNamespace();
                consumer.accept(ModNameHelper.getModName(modId).setStyle(MOD_NAME_STYLE));
            }

            int killCount = 0;
            MobShardData.Component component = dataComponentGetter.get(ComponentsRegistry.MOB_SHARD_DATA);
            if(component != null) killCount = component.killCount();

            if(isFull(dataComponentGetter)){
                consumer.accept(Component.translatable("info.woot_revived.mobshard.programmed").setStyle(SHARD_PROGRAM_STYLE));
            } else {
                consumer.accept(Component.translatable("info.woot_revived.mobshard.remaining", killCount, MobShardConfig.NUM_OF_KILLS.get()).setStyle(SHARD_PROGRAM_STYLE));
                if(mob != null) {
                    consumer.accept(Component.translatable("info.woot_revived.mobshard.remaining.desc", mob.getTooltipKillName(mobTag, gatherRegistry(ctx)).setStyle(DESCRIPTION_STYLE)).setStyle(DESCRIPTION_STYLE));
                } else {
                    consumer.accept(Component.translatable("info.woot_revived.mobshard.remaining.desc_no_entity").setStyle(DESCRIPTION_STYLE));
                }
            }
        }

        @OnlyIn(Dist.CLIENT)
        public RegistryAccess gatherRegistry(TooltipContext ctx) {
            return ctx.level() == null ? Minecraft.getInstance().level.registryAccess() : ctx.level().registryAccess();
        }
    }
}
