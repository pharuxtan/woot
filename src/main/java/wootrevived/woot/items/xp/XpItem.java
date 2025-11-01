package wootrevived.woot.items.xp;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.ComponentsRegistry;
import wootrevived.woot.registries.ItemsRegistry;
import wootrevived.woot.util.render.WootStyles;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class XpItem extends Item {
    private static final int STACK_SIZE = 64;
    public static final int SPLINTERS_IN_STACK = 9;
    private static final int SHARD_XP = 9;
    private static final int SPLINTER_XP = 1;

    final Variant variant;
    public XpItem(Variant variant, String tag) {
        super(new Item.Properties().stacksTo(STACK_SIZE)
                .setId(ResourceKey.create(Registries.ITEM, Woot.location(tag)))
                .component(ComponentsRegistry.XP_ITEM_TOOLTIP, Tooltip.INSTANCE));
        this.variant = variant;
    }

    public Variant getVariant() { return this.variant; }

    public enum Variant {
        SHARD,
        SPLINTER
    }

    public static ItemStack getItemStack(Variant variant) {
        if (variant == Variant.SHARD) {
            return ItemsRegistry.XP_SHARD_ITEM.get().getDefaultInstance();
        }
        return ItemsRegistry.XP_SPLINTER_ITEM.get().getDefaultInstance();
    }

    public static List<ItemStack> getShards(int xp) {
        List<ItemStack> shards = new ArrayList<>();

        int xpShards = xp / SPLINTERS_IN_STACK;
        int xpSplinters = xp % SPLINTERS_IN_STACK;
        int fullStacks = xpShards / STACK_SIZE;
        int leftoverShard = xpShards % STACK_SIZE;

        for(int i = 0; i < fullStacks; i++){
            ItemStack itemStack = getItemStack(Variant.SHARD);
            itemStack.setCount(STACK_SIZE);
            shards.add(itemStack);
        }

        if(leftoverShard > 0){
            ItemStack itemStack = getItemStack(Variant.SHARD);
            itemStack.setCount(leftoverShard);
            shards.add(itemStack);
        }

        if (xpSplinters > 0) {
            ItemStack itemStack = getItemStack(Variant.SPLINTER);
            itemStack.setCount(xpSplinters);
            shards.add(itemStack);
        }

        return shards;
    }

    private int getXp(ItemStack itemStack) {
        if (itemStack.getItem() == ItemsRegistry.XP_SHARD_ITEM.get())
            return SHARD_XP;
        else if (itemStack.getItem() == ItemsRegistry.XP_SPLINTER_ITEM.get())
            return SPLINTER_XP;
        return 0;
    }

    @Override
    public InteractionResult use(Level level, @NotNull Player player, @NotNull InteractionHand usedHand){
        if(level.isClientSide())
            return InteractionResult.PASS;

        ItemStack itemStack = player.getItemInHand(usedHand);
        if(itemStack.isEmpty())
            return InteractionResult.PASS;

        ItemStack advancementStack = itemStack.copy();

        level.playSound(null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.EXPERIENCE_ORB_PICKUP,
                SoundSource.PLAYERS,
                0.2F,
                0.5F * ((level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.7F + 1.8F));

        if(player instanceof FakePlayer){
            level.addFreshEntity(new ExperienceOrb(level, player.getX(), player.getY(), player.getZ(), 1));
            itemStack.shrink(1);
        } else {
            int xp = 0;
            if(player.isShiftKeyDown()){
                xp = getXp(itemStack) * itemStack.getCount();
                if(!player.isCreative())
                    itemStack.setCount(0);
            } else {
                xp = getXp(itemStack);
                if(!player.isCreative())
                    itemStack.shrink(1);
            }
            if(xp > 0){
                player.takeXpDelay = 0;
                ExperienceOrb orb = new ExperienceOrb(level, 0, 0, 0, xp);
                orb.playerTouch(player);
                player.takeXpDelay = 0;
                if(player instanceof ServerPlayer serverPlayer)
                    CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, advancementStack);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(ItemStack stack, TooltipContext ctx, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag tooltipFlag){
        if(display.shows(ComponentsRegistry.XP_ITEM_TOOLTIP.get()))
            components().get(ComponentsRegistry.XP_ITEM_TOOLTIP.get()).addToTooltip(ctx, consumer, tooltipFlag, stack.getComponents());
    }

    public record Tooltip() implements TooltipProvider {
        public static final Tooltip INSTANCE = new Tooltip();

        public static final String ID = "xp_item_tooltip";
        public static final Codec<Tooltip> CODEC = Codec.unit(INSTANCE);
        public static final StreamCodec<ByteBuf, Tooltip> STREAM_CODEC = StreamCodec.unit(INSTANCE);

        @Override
        public void addToTooltip(TooltipContext ctx, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
            consumer.accept(Component.translatable("info.woot_revived.shard.0").setStyle(WootStyles.DESCRIPTION_STYLE));
            consumer.accept(Component.translatable("info.woot_revived.shard.1").setStyle(WootStyles.DESCRIPTION_STYLE));
        }
    }
}
