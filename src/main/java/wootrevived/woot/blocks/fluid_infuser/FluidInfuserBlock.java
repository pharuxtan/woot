package wootrevived.woot.blocks.fluid_infuser;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import org.jspecify.annotations.Nullable;
import wootrevived.woot.Woot;
import wootrevived.woot.config.FluidInfuserConfig;
import wootrevived.woot.data.FluidInfuserData;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.ComponentsRegistry;
import wootrevived.woot.util.render.WootContainerScreen;

import java.util.function.Consumer;

import static wootrevived.woot.util.render.WootStyles.MACHINE_STYLE;
import static wootrevived.woot.util.render.WootStyles.UNIT_STYLE;

public class FluidInfuserBlock extends Block implements EntityBlock {
    public FluidInfuserBlock(String tag) {
        super(Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, Woot.identifier(tag)))
                .mapColor(MapColor.METAL)
                .sound(SoundType.METAL)
                .strength(3.5F));

        final StateDefinition.Builder<Block, BlockState> stateDefinitionBuilder = new StateDefinition.Builder<>(this);
        this.createBlockStateDefinition(stateDefinitionBuilder);
        this.fluidConvertorStateDefinition = stateDefinitionBuilder.create(Block::defaultBlockState, State::new);

        registerDefaultState(getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    protected StateDefinition<Block, BlockState> fluidConvertorStateDefinition;
    @Override
    public StateDefinition<Block, BlockState> getStateDefinition() {
        return this.fluidConvertorStateDefinition;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlocksRegistry.FLUID_INFUSER_BLOCK_ENTITY.get().create(pos, state);
    }

    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) return null;
        return blockEntityType == BlocksRegistry.FLUID_INFUSER_BLOCK_ENTITY.get() ? FluidInfuserBlockEntity::ticker : null;
    }

    public record Tooltip() implements TooltipProvider {
        public static final Tooltip INSTANCE = new Tooltip();

        public static final String ID = "fluid_infuser_block_tooltip";
        public static final Codec<Tooltip> CODEC = MapCodec.unit(() -> INSTANCE).codec();
        public static final StreamCodec<ByteBuf, Tooltip> STREAM_CODEC = StreamCodec.unit(INSTANCE);

        @Override
        public void addToTooltip(Item.TooltipContext ctx, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter getter) {
            FluidInfuserData.Component component = getter.get(ComponentsRegistry.FLUID_INFUSER_DATA);
            if(component == null)
                return;

            consumer.accept(
                    Component.empty()
                            .append(Component.translatable("info.woot_revived.power").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                            .append(Component.literal(WootContainerScreen.formatInteger(component.energy())))
                            .append(Component.literal("/").setStyle(MACHINE_STYLE))
                            .append(WootContainerScreen.formatInteger(FluidInfuserConfig.ENERGY_CAPACITY.get()))
                            .append(Component.literal(" FE").setStyle(UNIT_STYLE))
            );

            FluidStack inputFluid = component.inputFluid();

            consumer.accept(
                    Component.empty()
                            .append(Component.translatable("info.woot_revived.input_fluid").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                            .append(!inputFluid.isEmpty() ? inputFluid.getHoverName() : Component.translatable("info.woot_revived.empty"))
            );

            consumer.accept(
                    Component.empty()
                            .append(Component.translatable("info.woot_revived.input_amount").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                            .append(WootContainerScreen.formatInteger(inputFluid.getAmount()))
                            .append(Component.literal("/").setStyle(MACHINE_STYLE))
                            .append(WootContainerScreen.formatInteger(FluidInfuserConfig.INPUT_TANK_CAPACITY.get()))
                            .append(Component.literal("mB").setStyle(UNIT_STYLE))
            );

            FluidStack outputFluid = component.outputFluid();

            consumer.accept(
                    Component.empty()
                            .append(Component.translatable("info.woot_revived.output_fluid").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                            .append(!outputFluid.isEmpty() ? outputFluid.getHoverName() : Component.translatable("info.woot_revived.empty"))
            );

            consumer.accept(
                    Component.empty()
                            .append(Component.translatable("info.woot_revived.output_amount").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                            .append(WootContainerScreen.formatInteger(outputFluid.getAmount()))
                            .append(Component.literal("/").setStyle(MACHINE_STYLE))
                            .append(WootContainerScreen.formatInteger(FluidInfuserConfig.OUTPUT_TANK_CAPACITY.get()))
                            .append(Component.literal("mB").setStyle(UNIT_STYLE))
            );
        }
    }

    public static class State extends BlockState {
        public State(Block block, Reference2ObjectArrayMap<Property<?>, Comparable<?>> map, MapCodec<BlockState> codec) {
            super(block, map, codec);
        }

        @Override
        public InteractionResult useItemOn(ItemStack heldItem, Level level, Player player, InteractionHand hand, BlockHitResult hit) {
            if (level.isClientSide())
                return InteractionResult.SUCCESS;

            var itemAccess = ItemAccess.forPlayerInteraction(player, hand).oneByOne();
            var handHandler = itemAccess.getCapability(Capabilities.Fluid.ITEM);

            if(handHandler != null)
                return FluidUtil.interactWithFluidHandler(player, hand, level, hit.getBlockPos(), hit.getDirection()) ? InteractionResult.SUCCESS_SERVER : InteractionResult.FAIL;

            if (!(level.getBlockEntity(hit.getBlockPos()) instanceof FluidInfuserBlockEntity blockEntity))
                throw new IllegalStateException("BlockEntity is missing");

            player.openMenu(blockEntity, buf -> buf.writeBlockPos(hit.getBlockPos()));

            return InteractionResult.SUCCESS_SERVER;
        }

        @Override
        public InteractionResult useWithoutItem(Level level, Player player, BlockHitResult hit){
            return useItemOn(ItemStack.EMPTY, level, player, InteractionHand.MAIN_HAND, hit);
        }

        public BlockState rotate(LevelAccessor level, BlockPos pos, Rotation rotation) {
            return setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(getValue(BlockStateProperties.HORIZONTAL_FACING)));
        }

        @Override
        public BlockState mirror(Mirror mirror) {
            return rotate(null, null, mirror.getRotation(getValue(BlockStateProperties.HORIZONTAL_FACING)));
        }
    }
}