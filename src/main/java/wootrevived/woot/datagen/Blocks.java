package wootrevived.woot.datagen;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import wootrevived.woot.Woot;
import wootrevived.woot.client.model.factory_upgrade.FactoryUpgradeModelBuilder;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.util.fluid.WootFluidType;

import java.util.Objects;

public class Blocks extends BlockStateProvider {
    public Blocks(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Woot.MOD_ID, exFileHelper);
    }

    private final ResourceLocation FACTORY_BASE = Woot.location("block/factory_base");
    private final ResourceLocation CREATIVE_BASE = Woot.location("block/creative_base");

    @Override
    protected void registerStatesAndModels() {
        anvil(BlocksRegistry.STYGIAN_ANVIL_BLOCK);

        cubeAll(BlocksRegistry.FACTORY_BASE_BLOCK);
        cubeAll(BlocksRegistry.STYGIAN_BLOCK);

        cubeAll(BlocksRegistry.COPPER_MAGMATOR_BLOCK);
        cubeAll(BlocksRegistry.IRON_MAGMATOR_BLOCK);
        cubeAll(BlocksRegistry.GOLD_MAGMATOR_BLOCK);
        cubeAll(BlocksRegistry.DIAMOND_MAGMATOR_BLOCK);
        cubeAll(BlocksRegistry.NETHERITE_MAGMATOR_BLOCK);

        cubeColumn(BlocksRegistry.FAKE_SPAWNER_BLOCK);

        cubeAll(BlocksRegistry.COPPER_CELL_BLOCK);
        cubeColumn(BlocksRegistry.COPPER_PLINTH_BLOCK);
        cubeColumn(BlocksRegistry.COPPER_PYLON_BLOCK);

        cubeAll(BlocksRegistry.IRON_CELL_BLOCK);
        cubeColumn(BlocksRegistry.IRON_PLINTH_BLOCK);
        cubeColumn(BlocksRegistry.IRON_PYLON_BLOCK);

        cubeAll(BlocksRegistry.GOLD_CELL_BLOCK);
        cubeColumn(BlocksRegistry.GOLD_PLINTH_BLOCK);
        cubeColumn(BlocksRegistry.GOLD_PYLON_BLOCK);

        cubeAll(BlocksRegistry.DIAMOND_CELL_BLOCK);
        cubeColumn(BlocksRegistry.DIAMOND_PLINTH_BLOCK);
        cubeColumn(BlocksRegistry.DIAMOND_PYLON_BLOCK);

        cubeAll(BlocksRegistry.NETHERITE_CELL_BLOCK);
        cubeColumn(BlocksRegistry.NETHERITE_PLINTH_BLOCK);
        cubeColumn(BlocksRegistry.NETHERITE_PYLON_BLOCK);

        cubeColumn(BlocksRegistry.FACTORY_CONNECT_BLOCK);
        cubeColumn(BlocksRegistry.FACTORY_CTR_BASE_PRI_BLOCK);
        cubeColumn(BlocksRegistry.FACTORY_CTR_BASE_SEC_BLOCK);
        cubeColumn(BlocksRegistry.IMPORT_BLOCK);
        cubeColumn(BlocksRegistry.EXPORT_BLOCK);

        upgrade(BlocksRegistry.FACTORY_UPGRADE_BLOCK);

        cubeColumnCreative(BlocksRegistry.CREATIVE_TANK_BLOCK);
        cubeColumnCreative(BlocksRegistry.CREATIVE_POWER_BLOCK);

        orientable(BlocksRegistry.HEART_BLOCK);
        orientable(BlocksRegistry.FLUID_INFUSER_BLOCK);
        orientable(BlocksRegistry.ITEM_INFUSER_BLOCK);
        orientable(BlocksRegistry.DYE_LIQUIFIER_BLOCK);
        orientable(BlocksRegistry.ENCHANTED_LIQUIFIER_BLOCK);
        layout(BlocksRegistry.LAYOUT_BLOCK);

        fluid(FluidsRegistry.VITALITY_FUEL_FLUID_BLOCK, FluidsRegistry.VITALITY_FUEL_FLUID_TYPE);
        fluid(FluidsRegistry.ENCHANTED_FLUID_BLOCK, FluidsRegistry.ENCHANTED_FLUID_TYPE);
        fluid(FluidsRegistry.MOB_TEARS_FLUID_BLOCK, FluidsRegistry.MOB_TEARS_FLUID_TYPE);
        fluid(FluidsRegistry.PURE_DYE_FLUID_BLOCK, FluidsRegistry.PURE_DYE_FLUID_TYPE);
    }

    public ResourceLocation getBlockResource(Block block){
        return Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block));
    }

    public void fluid(DeferredHolder<Block, ? extends Block> block, DeferredHolder<FluidType, ? extends FluidType> fluid){
        ResourceLocation blockResource = getBlockResource(block.get());
        WootFluidType fluidType = (WootFluidType) fluid.get();
        ModelFile model = models().getBuilder(blockResource.getPath())
                        .texture("particle", fluidType.getStillTexture());
        VariantBlockStateBuilder builder = getVariantBuilder(block.get());
        builder.partialState().modelForState().modelFile(model).addModel();
    }

    public void orientable(DeferredHolder<Block, ? extends Block> block){
        ResourceLocation blockResource = getBlockResource(block.get());
        horizontalBlock(block.get(), FACTORY_BASE, blockResource.withPrefix("block/"), FACTORY_BASE);
    }

    public void cubeAll(DeferredHolder<Block, ? extends Block> block){
        ResourceLocation blockResource = getBlockResource(block.get());
        ModelFile model = models().cubeAll(blockResource.getPath(), blockResource.withPrefix("block/"));
        VariantBlockStateBuilder builder = getVariantBuilder(block.get());
        builder.partialState().modelForState().modelFile(model).addModel();
    }

    public void cubeColumn(DeferredHolder<Block, ? extends Block> block){
        ResourceLocation blockResource = getBlockResource(block.get());
        ModelFile model = models().cubeColumn(blockResource.getPath(), blockResource.withPrefix("block/"), FACTORY_BASE);
        VariantBlockStateBuilder builder = getVariantBuilder(block.get());
        builder.partialState().modelForState().modelFile(model).addModel();
    }

    public void cubeColumnCreative(DeferredHolder<Block, ? extends Block> block){
        ResourceLocation blockResource = getBlockResource(block.get());
        ModelFile model = models().cubeColumn(blockResource.getPath(), blockResource.withPrefix("block/"), CREATIVE_BASE);
        VariantBlockStateBuilder builder = getVariantBuilder(block.get());
        builder.partialState().modelForState().modelFile(model).addModel();
    }

    public void upgrade(DeferredHolder<Block, ? extends Block> block){
        ResourceLocation blockResource = getBlockResource(block.get());
        ModelFile model = models().cubeColumn(blockResource.getPath(), blockResource.withPrefix("block/"), FACTORY_BASE)
                .customLoader(FactoryUpgradeModelBuilder::new)
                .end();
        VariantBlockStateBuilder builder = getVariantBuilder(block.get());
        builder.partialState().modelForState().modelFile(model).addModel();
    }

    public void layout(DeferredHolder<Block, ? extends Block>block){
        ResourceLocation blockResource = getBlockResource(block.get());
        ModelFile model = models().withExistingParent(blockResource.getPath(), ResourceLocation.tryBuild("minecraft", "block/block"))
                .texture("particle", blockResource.withPrefix("block/"))
                .texture("end", blockResource.withPrefix("block/"))
                .texture("primary", blockResource.withPrefix("block/").withSuffix("_primary"))
                .texture("secondary", blockResource.withPrefix("block/").withSuffix("_secondary"))

                .transforms()
                    .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                        .rotation(0F, 135F, 0F)
                        .scale(0.4F, 0.4F, 0.4F)
                    .end()
                .end()

                .element()
                    .from(0, 0, 0)
                    .to(16, 16, 16)
                    .face(Direction.DOWN).texture("#end").rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).cullface(Direction.DOWN).end()
                    .face(Direction.UP).texture("#end").cullface(Direction.UP).end()
                    .face(Direction.NORTH).texture("#primary").cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).texture("#secondary").cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).texture("#secondary").cullface(Direction.WEST).end()
                    .face(Direction.EAST).texture("#secondary").cullface(Direction.EAST).end()
                .end();
        horizontalBlock(block.get(), model);
    }

    public void anvil(DeferredHolder<Block, ? extends Block> block){
        ResourceLocation blockResource = getBlockResource(block.get());
        ModelFile model = models().withExistingParent(blockResource.getPath(), ResourceLocation.tryBuild("minecraft", "block/block"))
                .texture("particle", blockResource.withPrefix("block/"))
                .texture("base", ResourceLocation.tryBuild("minecraft", "block/crying_obsidian"))
                .texture("body", blockResource.withPrefix("block/"))
                .texture("top", blockResource.withPrefix("block/").withSuffix("_top"))

                .transforms()
                    .transform(ItemDisplayContext.FIXED)
                        .rotation(0F, 90F, 0F)
                        .scale(0.5F, 0.5F, 0.5F)
                    .end()
                .end()

                .element()
                    .from(2, 0, 2)
                    .to(14, 4, 14)
                    .face(Direction.DOWN).uvs(2, 2, 14, 14).texture("#base").rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(2, 2, 14, 14).texture("#base").rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).end()
                    .face(Direction.NORTH).uvs(2, 12, 14, 16).texture("#base").end()
                    .face(Direction.SOUTH).uvs(2, 12, 14, 16).texture("#base").end()
                    .face(Direction.WEST).uvs(0, 2, 4, 14).texture("#base").rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.EAST).uvs(4, 2, 0, 14).texture("#base").rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).end()
                .end()

                .element()
                    .from(4, 4, 3)
                    .to(12, 5, 13)
                    .face(Direction.UP).uvs(4, 3, 12, 13).texture("#body").rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).end()
                    .face(Direction.NORTH).uvs(4, 11, 12, 12).texture("#body").end()
                    .face(Direction.SOUTH).uvs(4, 11, 12, 12).texture("#body").end()
                    .face(Direction.WEST).uvs(4, 3, 5, 13).texture("#body").rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.EAST).uvs(5, 3, 4, 13).texture("#body").rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).end()
                .end()

                .element()
                    .from(6, 5, 4)
                    .to(10, 10, 12)
                    .face(Direction.NORTH).uvs(6, 6, 10, 11).texture("#body").end()
                    .face(Direction.SOUTH).uvs(6, 6, 10, 11).texture("#body").end()
                    .face(Direction.WEST).uvs(5, 4, 10, 12).texture("#body").rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.EAST).uvs(10, 4, 5, 12).texture("#body").rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).end()
                .end()

                .element()
                    .from(3, 10, 0)
                    .to(13, 16, 16)
                    .face(Direction.DOWN).uvs(3, 0, 13, 16).texture("#body").rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).end()
                    .face(Direction.UP).uvs(3, 0, 13, 16).texture("#top").rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).end()
                    .face(Direction.NORTH).uvs(3, 0, 13, 6).texture("#body").end()
                    .face(Direction.SOUTH).uvs(3, 0, 13, 6).texture("#body").end()
                    .face(Direction.WEST).uvs(10, 0, 16, 16).texture("#body").rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
                    .face(Direction.EAST).uvs(16, 0, 10, 16).texture("#body").rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).end()
                .end();
        horizontalBlock(block.get(), model);
    }
}
