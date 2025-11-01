package wootrevived.woot.blocks.fake_spawner;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.api.WootFactoryMob;
import wootrevived.woot.blocks.factory.FactoryBlockItem;
import wootrevived.woot.data.FakeSpawnerData;
import wootrevived.woot.registries.ComponentsRegistry;
import wootrevived.woot.registries.WootFactoryMobsRegistry;
import wootrevived.woot.util.common.WootTier;
import wootrevived.woot.util.helper.ModNameHelper;

import java.util.function.Consumer;

import static wootrevived.woot.util.render.WootStyles.*;

public class FakeSpawnerBlockItem extends FactoryBlockItem {
    public FakeSpawnerBlockItem(Block block, Item.Properties properties) {
        super(block, properties.component(ComponentsRegistry.FAKE_SPAWNER_BLOCK_TOOLTIP, Tooltip.INSTANCE));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(ItemStack stack, TooltipContext ctx, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag tooltipFlag){
        if(display.shows(ComponentsRegistry.FAKE_SPAWNER_BLOCK_TOOLTIP.get()))
            components().get(ComponentsRegistry.FAKE_SPAWNER_BLOCK_TOOLTIP.get()).addToTooltip(ctx, consumer, tooltipFlag, stack.getComponents());
    }

    public record Tooltip() implements TooltipProvider {
        public static final Tooltip INSTANCE = new Tooltip();

        public static final String ID = "fake_spawner_block_tooltip";
        public static final Codec<Tooltip> CODEC = Codec.unit(INSTANCE);
        public static final StreamCodec<ByteBuf, Tooltip> STREAM_CODEC = StreamCodec.unit(INSTANCE);

        @Override
        public void addToTooltip(TooltipContext ctx, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter getter) {
            if(FMLEnvironment.dist.isDedicatedServer())
                return;

            FakeSpawnerData.Component component = getter.get(ComponentsRegistry.FAKE_SPAWNER_DATA);
            if (component == null)
                return;

            component.mobTag().ifPresent(mobTag -> {
                WootFactoryMob<?> mob = WootFactoryMobsRegistry.getFactoryMob(mobTag);

                if (mob != null) {
                    consumer.accept(mob.getDisplayName(mobTag, gatherRegistry(ctx)).setStyle(CAPTURED_STYLE));
                    String modId = BuiltInRegistries.ENTITY_TYPE.getKey(mob.getEntityType()).getNamespace();
                    consumer.accept(ModNameHelper.getModName(modId).setStyle(MOD_NAME_STYLE));
                }

                consumer.accept(Component.translatable("info.woot_revived.tier").append(Component.literal(": ")).append(Component.translatable(WootTier.getTranslationKey(mob.getTier()))).setStyle(DESCRIPTION_STYLE));
            });
        }

        @OnlyIn(Dist.CLIENT)
        public RegistryAccess gatherRegistry(TooltipContext ctx) {
            return ctx.level() == null ? Minecraft.getInstance().level.registryAccess() : ctx.level().registryAccess();
        }
    }

    @Override
    protected boolean updateCustomBlockEntityTag(@NotNull BlockPos pos, @NotNull Level level, @Nullable Player player, @NotNull ItemStack stack, @NotNull BlockState state) {
        super.updateCustomBlockEntityTag(pos, level, player, stack, state);

        FakeSpawnerData.Component component = stack.get(ComponentsRegistry.FAKE_SPAWNER_DATA);
        if(component == null)
            return true;

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity instanceof FakeSpawnerBlockEntity fakeSpawnerBlockEntity) {
            fakeSpawnerBlockEntity.setComponent(component);
            fakeSpawnerBlockEntity.setChanged();
        }

        return true;
    }
}
