package wootrevived.woot.blocks.cell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import org.jetbrains.annotations.NotNull;
import wootrevived.api.enums.Tier;
import wootrevived.woot.config.CellConfig;
import wootrevived.woot.data.CellData;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.ComponentsRegistry;
import wootrevived.woot.util.block.FactoryBlockBase;
import wootrevived.woot.util.entity.WootTags;
import wootrevived.woot.util.render.WootContainerScreen;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static wootrevived.woot.util.render.WootStyles.MACHINE_STYLE;
import static wootrevived.woot.util.render.WootStyles.UNIT_STYLE;

public class CellBlock extends FactoryBlockBase {
    public CellBlock(Supplier<BlockEntityType<?>> entity, String tag) {
        super(entity, tag, Properties.of()
                .mapColor(MapColor.METAL)
                .sound(SoundType.METAL)
                .strength(3.5F));

        final StateDefinition.Builder<Block, BlockState> stateDefinitionBuilder = new StateDefinition.Builder<>(this);
        this.createBlockStateDefinition(stateDefinitionBuilder);
        this.cellStateDefinition = stateDefinitionBuilder.create(Block::defaultBlockState, State::new);

        registerDefaultState(getStateDefinition().any()
                .setValue(BlockStateProperties.ATTACHED, false)
                .setValue(BlockStateProperties.ENABLED, true));
    }

    protected StateDefinition<Block, BlockState> cellStateDefinition;
    @Override
    public @NotNull StateDefinition<Block, BlockState> getStateDefinition() {
        return this.cellStateDefinition;
    }

    @Override
    public boolean verifyItem(Item item){
        return item == BlocksRegistry.COPPER_CELL_BLOCK_ITEM.get() ||
               item == BlocksRegistry.IRON_CELL_BLOCK_ITEM.get() ||
               item == BlocksRegistry.GOLD_CELL_BLOCK_ITEM.get() ||
               item == BlocksRegistry.DIAMOND_CELL_BLOCK_ITEM.get() ||
               item == BlocksRegistry.NETHERITE_CELL_BLOCK_ITEM.get();
    }

    public record Tooltip(Tier tier) implements TooltipProvider {
        public static final String ID = "cell_block_tooltip";

        public static final Codec<Tooltip> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        StringRepresentable.fromEnum(Tier::values).fieldOf(WootTags.Factory.FACTORY_TIER).forGetter(Tooltip::tier)
                ).apply(inst, Tooltip::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, Tooltip> STREAM_CODEC = StreamCodec.composite(
                NeoForgeStreamCodecs.enumCodec(Tier.class), Tooltip::tier,
                Tooltip::new
        );

        @Override
        public void addToTooltip(Item.TooltipContext ctx, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter getter) {
            int capacity = 0;
            if(tier == Tier.TIER_1) {
                capacity = CellConfig.COPPER_CAPACITY.get();
            } else if(tier == Tier.TIER_2) {
                capacity = CellConfig.IRON_CAPACITY.get();
            } else if(tier == Tier.TIER_3) {
                capacity = CellConfig.GOLD_CAPACITY.get();
            } else if(tier == Tier.TIER_4) {
                capacity = CellConfig.DIAMOND_CAPACITY.get();
            } else if(tier == Tier.TIER_5) {
                capacity = CellConfig.NETHERITE_CAPACITY.get();
            }

            CellData.Component component = getter.get(ComponentsRegistry.CELL_DATA);
            if(component == null)
                return;

            FluidStack fluid = component.tankFluid();
            consumer.accept(
                    Component.empty()
                            .append(Component.translatable("info.woot_revived.cell.amount").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                            .append(WootContainerScreen.formatInteger(fluid.getAmount()))
                            .append(Component.literal("/").setStyle(MACHINE_STYLE))
                            .append(WootContainerScreen.formatInteger(capacity))
                            .append(Component.literal("mB").setStyle(UNIT_STYLE))
            );
        }
    }

    public static class State extends FactoryBlockBase.State {
        public State(Block block, Reference2ObjectArrayMap<Property<?>, Comparable<?>> map, MapCodec<BlockState> codec) {
            super(block, map, codec);
        }

        @Override
        public InteractionResult useItemOn(@NotNull ItemStack heldItem, @NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit){
            if(level.isClientSide())
                return InteractionResult.SUCCESS;

            if(!getValue(BlockStateProperties.ENABLED))
                return InteractionResult.PASS;

            if(!(level.getBlockEntity(hit.getBlockPos()) instanceof CellBlockEntity))
                throw new IllegalStateException("BlockEntity is missing");

            var itemAccess = ItemAccess.forPlayerInteraction(player, hand).oneByOne();
            var handHandler = itemAccess.getCapability(Capabilities.Fluid.ITEM);

            if(handHandler != null)
                return FluidUtil.interactWithFluidHandler(player, hand, level, hit.getBlockPos(), hit.getDirection()) ? InteractionResult.SUCCESS_SERVER : InteractionResult.FAIL;

            return super.useItemOn(heldItem, level, player, hand, hit);
        }
    }
}
