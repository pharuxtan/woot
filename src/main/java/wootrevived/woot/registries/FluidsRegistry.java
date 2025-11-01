package wootrevived.woot.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.Woot;
import wootrevived.woot.init.Registry;
import wootrevived.woot.util.fluid.WootFluidType;

public class FluidsRegistry {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Woot.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, Woot.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Woot.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Woot.MOD_ID);

    public static void register(IEventBus bus) {
        FLUID_TYPES.register(bus);
        FLUIDS.register(bus);
        BLOCKS.register(bus);
        ITEMS.register(bus);

        for(DeferredHolder<Item, ? extends Item> item : ITEMS.getEntries())
            Registry.addToCreativeTab(item);
    }

    /* Default Properties */

    public static final ResourceLocation UNDERWATER_OVERLAY_RL = ResourceLocation.withDefaultNamespace("misc/underwater");

    public static final FluidType.Properties defaultProperties = FluidType.Properties.create()
            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
            .canSwim(true)
            .canDrown(true)
            .canPushEntity(true)
            .supportsBoating(true);

    /* Vitality Fuel */

    public static String VITALITY_FUEL_FLUID_TAG = "vitality_fuel_fluid";
    public static final ResourceLocation VITALITY_FUEL_STILL_TEX = Woot.location("block/vitality_fuel_still");
    public static final ResourceLocation VITALITY_FUEL_FLOW_TEX = Woot.location("block/vitality_fuel_flow");
    public static DeferredHolder<FluidType, WootFluidType> VITALITY_FUEL_FLUID_TYPE = FLUID_TYPES.register(VITALITY_FUEL_FLUID_TAG,
            () -> new WootFluidType(
                    VITALITY_FUEL_STILL_TEX, VITALITY_FUEL_FLOW_TEX, UNDERWATER_OVERLAY_RL,
                    0x2E, 0xB8, 0xB8,
                    defaultProperties
            )
    );
    public static final DeferredHolder<Fluid, FlowingFluid> SOURCE_VITALITY_FUEL_FLUID = FLUIDS.register(VITALITY_FUEL_FLUID_TAG, () -> new BaseFlowingFluid.Source(FluidsRegistry.VITALITY_FUEL_FLUID_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> FLOWING_VITALITY_FUEL_FLUID = FLUIDS.register(VITALITY_FUEL_FLUID_TAG + "_flowing", () -> new BaseFlowingFluid.Flowing(FluidsRegistry.VITALITY_FUEL_FLUID_PROPERTIES));
    public static final DeferredHolder<Block, LiquidBlock> VITALITY_FUEL_FLUID_BLOCK = BLOCKS.register(VITALITY_FUEL_FLUID_TAG + "_block", () -> new LiquidBlock(SOURCE_VITALITY_FUEL_FLUID.get(), BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.WATER).noLootTable().setId(ResourceKey.create(Registries.BLOCK, Woot.location(VITALITY_FUEL_FLUID_TAG + "_block")))));
    public static final DeferredHolder<Item, BucketItem> VITALITY_FUEL_FLUID_BUCKET = ITEMS.register(VITALITY_FUEL_FLUID_TAG + "_bucket", () -> new BucketItem(SOURCE_VITALITY_FUEL_FLUID.get(), new BucketItem.Properties().craftRemainder(Items.BUCKET).stacksTo(1).setId(ResourceKey.create(Registries.ITEM, Woot.location(VITALITY_FUEL_FLUID_TAG + "_bucket")))));
    public static final BaseFlowingFluid.Properties VITALITY_FUEL_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(VITALITY_FUEL_FLUID_TYPE, SOURCE_VITALITY_FUEL_FLUID, FLOWING_VITALITY_FUEL_FLUID)
            .block(VITALITY_FUEL_FLUID_BLOCK)
            .bucket(VITALITY_FUEL_FLUID_BUCKET);

    /* Pure Dye */

    public static String PURE_DYE_FLUID_TAG = "pure_dye_fluid";
    public static final ResourceLocation PURE_DYE_STILL_TEX = Woot.location("block/pure_dye_still");
    public static final ResourceLocation PURE_DYE_FLOW_TEX = Woot.location("block/pure_dye_flow");
    public static DeferredHolder<FluidType, WootFluidType> PURE_DYE_FLUID_TYPE = FLUID_TYPES.register(PURE_DYE_FLUID_TAG,
            () -> new WootFluidType(
                    PURE_DYE_STILL_TEX, PURE_DYE_FLOW_TEX, UNDERWATER_OVERLAY_RL,
                    0xE9, 0xE9, 0xE9,
                    defaultProperties
            )
    );
    public static final DeferredHolder<Fluid, FlowingFluid> SOURCE_PURE_DYE_FLUID = FLUIDS.register(PURE_DYE_FLUID_TAG, () -> new BaseFlowingFluid.Source(FluidsRegistry.PURE_DYE_FLUID_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> FLOWING_PURE_DYE_FLUID = FLUIDS.register(PURE_DYE_FLUID_TAG + "_flowing", () -> new BaseFlowingFluid.Flowing(FluidsRegistry.PURE_DYE_FLUID_PROPERTIES));
    public static final DeferredHolder<Block, LiquidBlock> PURE_DYE_FLUID_BLOCK = BLOCKS.register(PURE_DYE_FLUID_TAG + "_block", () -> new LiquidBlock(SOURCE_PURE_DYE_FLUID.get(), BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.WATER).noLootTable().setId(ResourceKey.create(Registries.BLOCK, Woot.location(PURE_DYE_FLUID_TAG + "_block")))));
    public static final DeferredHolder<Item, BucketItem> PURE_DYE_FLUID_BUCKET = ITEMS.register(PURE_DYE_FLUID_TAG + "_bucket", () -> new BucketItem(SOURCE_PURE_DYE_FLUID.get(), new BucketItem.Properties().craftRemainder(Items.BUCKET).stacksTo(1).setId(ResourceKey.create(Registries.ITEM, Woot.location(PURE_DYE_FLUID_TAG + "_bucket")))));
    public static final BaseFlowingFluid.Properties PURE_DYE_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(PURE_DYE_FLUID_TYPE, SOURCE_PURE_DYE_FLUID, FLOWING_PURE_DYE_FLUID)
            .block(PURE_DYE_FLUID_BLOCK)
            .bucket(PURE_DYE_FLUID_BUCKET);

    /* Enchanted */

    public static String ENCHANTED_FLUID_TAG = "enchanted_fluid";
    public static final ResourceLocation ENCHANTED_STILL_TEX = Woot.location("block/enchanted_still");
    public static final ResourceLocation ENCHANTED_FLOW_TEX = Woot.location("block/enchanted_flow");
    public static DeferredHolder<FluidType, WootFluidType> ENCHANTED_FLUID_TYPE = FLUID_TYPES.register(ENCHANTED_FLUID_TAG,
            () -> new WootFluidType(
                    ENCHANTED_STILL_TEX, ENCHANTED_FLOW_TEX, UNDERWATER_OVERLAY_RL,
                    0x35, 0x4C, 0xD5,
                    defaultProperties
            )
    );
    public static final DeferredHolder<Fluid, FlowingFluid> SOURCE_ENCHANTED_FLUID = FLUIDS.register(ENCHANTED_FLUID_TAG, () -> new BaseFlowingFluid.Source(FluidsRegistry.ENCHANTED_FLUID_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> FLOWING_ENCHANTED_FLUID = FLUIDS.register(ENCHANTED_FLUID_TAG + "_flowing", () -> new BaseFlowingFluid.Flowing(FluidsRegistry.ENCHANTED_FLUID_PROPERTIES));
    public static final DeferredHolder<Block, LiquidBlock> ENCHANTED_FLUID_BLOCK = BLOCKS.register(ENCHANTED_FLUID_TAG + "_block", () -> new LiquidBlock(SOURCE_ENCHANTED_FLUID.get(), BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.WATER).noLootTable().setId(ResourceKey.create(Registries.BLOCK, Woot.location(ENCHANTED_FLUID_TAG + "_block")))));
    public static final DeferredHolder<Item, BucketItem> ENCHANTED_FLUID_BUCKET = ITEMS.register(ENCHANTED_FLUID_TAG + "_bucket", () -> new BucketItem(SOURCE_ENCHANTED_FLUID.get(), new BucketItem.Properties().craftRemainder(Items.BUCKET).stacksTo(1).setId(ResourceKey.create(Registries.ITEM, Woot.location(ENCHANTED_FLUID_TAG + "_bucket")))) {
        @Override
        public boolean isFoil(@NotNull ItemStack stack) {
            return true;
        }
    });
    public static final BaseFlowingFluid.Properties ENCHANTED_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(ENCHANTED_FLUID_TYPE, SOURCE_ENCHANTED_FLUID, FLOWING_ENCHANTED_FLUID)
            .block(ENCHANTED_FLUID_BLOCK)
            .bucket(ENCHANTED_FLUID_BUCKET);

    /* Mob Tears */

    public static String MOB_TEARS_FLUID_TAG = "mob_tears_fluid";
    public static final ResourceLocation MOB_TEARS_STILL_TEX = Woot.location("block/mob_tears_still");
    public static final ResourceLocation MOB_TEARS_FLOW_TEX = Woot.location("block/mob_tears_flow");
    public static DeferredHolder<FluidType, WootFluidType> MOB_TEARS_FLUID_TYPE = FLUID_TYPES.register(MOB_TEARS_FLUID_TAG,
            () -> new WootFluidType(
                    MOB_TEARS_STILL_TEX, MOB_TEARS_FLOW_TEX, UNDERWATER_OVERLAY_RL,
                    0x5E, 0xB8, 0x21,
                    defaultProperties
            )
    );
    public static final DeferredHolder<Fluid, FlowingFluid> SOURCE_MOB_TEARS_FLUID = FLUIDS.register(MOB_TEARS_FLUID_TAG, () -> new BaseFlowingFluid.Source(FluidsRegistry.MOB_TEARS_FLUID_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> FLOWING_MOB_TEARS_FLUID = FLUIDS.register(MOB_TEARS_FLUID_TAG + "_flowing", () -> new BaseFlowingFluid.Flowing(FluidsRegistry.MOB_TEARS_FLUID_PROPERTIES));
    public static final DeferredHolder<Block, LiquidBlock> MOB_TEARS_FLUID_BLOCK = BLOCKS.register(MOB_TEARS_FLUID_TAG + "_block", () -> new LiquidBlock(SOURCE_MOB_TEARS_FLUID.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER).noLootTable().setId(ResourceKey.create(Registries.BLOCK, Woot.location(MOB_TEARS_FLUID_TAG + "_block")))));
    public static final DeferredHolder<Item, BucketItem> MOB_TEARS_FLUID_BUCKET = ITEMS.register(MOB_TEARS_FLUID_TAG + "_bucket", () -> new BucketItem(SOURCE_MOB_TEARS_FLUID.get(), new BucketItem.Properties().craftRemainder(Items.BUCKET).stacksTo(1).setId(ResourceKey.create(Registries.ITEM, Woot.location(MOB_TEARS_FLUID_TAG + "_bucket")))));
    public static final BaseFlowingFluid.Properties MOB_TEARS_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(MOB_TEARS_FLUID_TYPE, SOURCE_MOB_TEARS_FLUID, FLOWING_MOB_TEARS_FLUID)
            .block(MOB_TEARS_FLUID_BLOCK)
            .bucket(MOB_TEARS_FLUID_BUCKET);
}
