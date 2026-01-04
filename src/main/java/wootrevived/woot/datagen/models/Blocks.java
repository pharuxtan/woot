package wootrevived.woot.datagen.models;

import com.mojang.math.Quadrant;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplate;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import wootrevived.woot.Woot;
import wootrevived.woot.client.model.factory_upgrade.FactoryUpgradeBlockBaseModel;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.util.fluid.WootFluidType;

import java.util.Optional;

public class Blocks {
    public static BlockModelGenerators models;

    public static void registerModels(BlockModelGenerators blockModels) {
        models = blockModels;

        anvil(BlocksRegistry.STYGIAN_ANVIL_BLOCK);

        cubeAll(BlocksRegistry.FACTORY_BASE_BLOCK);
        cubeAll(BlocksRegistry.STYGIAN_BLOCK);

        cubeColumn(BlocksRegistry.FAKE_SPAWNER_BLOCK);

        cubeAll(BlocksRegistry.COPPER_MAGMATOR_BLOCK);
        cubeAll(BlocksRegistry.IRON_MAGMATOR_BLOCK);
        cubeAll(BlocksRegistry.GOLD_MAGMATOR_BLOCK);
        cubeAll(BlocksRegistry.DIAMOND_MAGMATOR_BLOCK);
        cubeAll(BlocksRegistry.NETHERITE_MAGMATOR_BLOCK);

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

    public static void orientable(DeferredHolder<Block, ? extends Block> block){
        MultiVariant variant = BlockModelGenerators.plainVariant(ModelTemplates.CUBE_ORIENTABLE.create(block.get(), new TextureMapping()
                .put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block.get()))
                .put(TextureSlot.SIDE, Woot.identifier("block/factory_base"))
                .put(TextureSlot.TOP, Woot.identifier("block/factory_base")), models.modelOutput));
        models.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block.get(), variant).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
    }

    public static void cubeAll(DeferredHolder<Block, ? extends Block> block){
        MultiVariant variant = BlockModelGenerators.plainVariant(TexturedModel.CUBE.create(block.get(), models.modelOutput));
        models.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block.get(), variant));
    }

    public static void cubeColumn(DeferredHolder<Block, ? extends Block> block){
        MultiVariant variant = BlockModelGenerators.plainVariant(ModelTemplates.CUBE_COLUMN.create(block.get(), new TextureMapping()
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block.get()))
                .put(TextureSlot.END, Woot.identifier("block/factory_base")), models.modelOutput));
        models.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block.get(), variant));
    }

    public static void cubeColumnCreative(DeferredHolder<Block, ? extends Block> block){
        MultiVariant variant = BlockModelGenerators.plainVariant(ModelTemplates.CUBE_COLUMN.create(block.get(), new TextureMapping()
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block.get()))
                .put(TextureSlot.END, Woot.identifier("block/creative_base")), models.modelOutput));
        models.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block.get(), variant));
    }

    public static void fluid(DeferredHolder<Block, ? extends Block> block, DeferredHolder<FluidType, ? extends WootFluidType> fluid){
        MultiVariant variant = BlockModelGenerators.plainVariant(ModelTemplates.PARTICLE_ONLY.create(block.get(), TextureMapping.particle(fluid.get().getStillTexture()), models.modelOutput));
        models.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block.get(), variant));
    }

    public static void layout(DeferredHolder<Block, ? extends Block> block){
        TextureSlot primarySlot = TextureSlot.create("primary");
        TextureSlot secondarySlot = TextureSlot.create("secondary");
        TextureSlot[] slots = new TextureSlot[]{ TextureSlot.END, TextureSlot.PARTICLE, primarySlot, secondarySlot };
        ExtendedModelTemplate template = new ModelTemplate(Optional.of(Identifier.withDefaultNamespace("block/block")), Optional.empty(), slots)
                .extend()

                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, builder -> {
                    builder.rotation(0F, 135F, 0F);
                    builder.scale(0.4F, 0.4F, 0.4F);
                })

                .element(builder -> {
                    builder.from(0, 0, 0);
                    builder.to(16, 16, 16);
                    builder.face(Direction.DOWN, faceBuilder -> faceBuilder.texture(TextureSlot.END).rotation(Quadrant.R180).cullface(Direction.DOWN));
                    builder.face(Direction.UP, faceBuilder -> faceBuilder.texture(TextureSlot.END).cullface(Direction.UP));
                    builder.face(Direction.NORTH, faceBuilder -> faceBuilder.texture(primarySlot).cullface(Direction.NORTH));
                    builder.face(Direction.SOUTH, faceBuilder -> faceBuilder.texture(secondarySlot).cullface(Direction.SOUTH));
                    builder.face(Direction.WEST, faceBuilder -> faceBuilder.texture(secondarySlot).cullface(Direction.WEST));
                    builder.face(Direction.EAST, faceBuilder -> faceBuilder.texture(secondarySlot).cullface(Direction.EAST));
                })

                .build();

        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.END, TextureMapping.getBlockTexture(block.get()))
                .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block.get()))
                .put(primarySlot, TextureMapping.getBlockTexture(block.get(), "_primary"))
                .put(secondarySlot, TextureMapping.getBlockTexture(block.get(), "_secondary"));

        MultiVariant variant = BlockModelGenerators.plainVariant(template.create(block.get(), mapping, models.modelOutput));
        models.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block.get(), variant).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
    }

    public static void anvil(DeferredHolder<Block, ? extends Block> block){
        TextureSlot baseSlot = TextureSlot.create("base");
        TextureSlot bodySlot = TextureSlot.create("body");
        TextureSlot[] slots = new TextureSlot[]{ TextureSlot.PARTICLE, baseSlot, bodySlot, TextureSlot.TOP };

        ExtendedModelTemplate template = new ModelTemplate(Optional.of(Identifier.withDefaultNamespace("block/block")), Optional.empty(), slots)
                .extend()

                .transform(ItemDisplayContext.FIXED, builder -> {
                    builder.rotation(0F, 90F, 0F);
                    builder.scale(0.5F, 0.5F, 0.5F);
                })

                .element(builder -> {
                    builder.from(2, 0, 2);
                    builder.to(14, 4, 14);
                    builder.face(Direction.DOWN, faceBuilder -> faceBuilder.uvs(2, 2, 14, 14).texture(baseSlot).rotation(Quadrant.R180).cullface(Direction.DOWN));
                    builder.face(Direction.UP, faceBuilder -> faceBuilder.uvs(2, 2, 14, 14).texture(baseSlot).rotation(Quadrant.R180));
                    builder.face(Direction.NORTH, faceBuilder -> faceBuilder.uvs(2, 12, 14, 16).texture(baseSlot));
                    builder.face(Direction.SOUTH, faceBuilder -> faceBuilder.uvs(2, 12, 14, 16).texture(baseSlot));
                    builder.face(Direction.WEST, faceBuilder -> faceBuilder.uvs(0, 2, 4, 14).texture(baseSlot).rotation(Quadrant.R90));
                    builder.face(Direction.EAST, faceBuilder -> faceBuilder.uvs(4, 2, 0, 14).texture(baseSlot).rotation(Quadrant.R270));
                })

                .element(builder -> {
                    builder.from(4, 4, 3);
                    builder.to(12, 5, 13);
                    builder.face(Direction.UP, faceBuilder -> faceBuilder.uvs(4, 3, 12, 13).texture(bodySlot).rotation(Quadrant.R180));
                    builder.face(Direction.NORTH, faceBuilder -> faceBuilder.uvs(4, 11, 12, 12).texture(bodySlot));
                    builder.face(Direction.SOUTH, faceBuilder -> faceBuilder.uvs(4, 11, 12, 12).texture(bodySlot));
                    builder.face(Direction.WEST, faceBuilder -> faceBuilder.uvs(4, 3, 5, 13).texture(bodySlot).rotation(Quadrant.R90));
                    builder.face(Direction.EAST, faceBuilder -> faceBuilder.uvs(5, 3, 4, 13).texture(bodySlot).rotation(Quadrant.R270));
                })

                .element(builder -> {
                    builder.from(6, 5, 4);
                    builder.to(10, 10, 12);
                    builder.face(Direction.NORTH, faceBuilder -> faceBuilder.uvs(6, 6, 10, 11).texture(bodySlot));
                    builder.face(Direction.SOUTH, faceBuilder -> faceBuilder.uvs(6, 6, 10, 11).texture(bodySlot));
                    builder.face(Direction.WEST, faceBuilder -> faceBuilder.uvs(5, 4, 10, 12).texture(bodySlot).rotation(Quadrant.R90));
                    builder.face(Direction.EAST, faceBuilder -> faceBuilder.uvs(10, 4, 5, 12).texture(bodySlot).rotation(Quadrant.R270));
                })

                .element(builder -> {
                    builder.from(3, 10, 0);
                    builder.to(13, 16, 16);
                    builder.face(Direction.DOWN, faceBuilder -> faceBuilder.uvs(3, 0, 13, 16).texture(bodySlot).rotation(Quadrant.R180));
                    builder.face(Direction.UP, faceBuilder -> faceBuilder.uvs(3, 0, 13, 16).texture(TextureSlot.TOP).rotation(Quadrant.R180));
                    builder.face(Direction.NORTH, faceBuilder -> faceBuilder.uvs(3, 0, 13, 6).texture(bodySlot));
                    builder.face(Direction.SOUTH, faceBuilder -> faceBuilder.uvs(3, 0, 13, 6).texture(bodySlot));
                    builder.face(Direction.WEST, faceBuilder -> faceBuilder.uvs(10, 0, 16, 16).texture(bodySlot).rotation(Quadrant.R90));
                    builder.face(Direction.EAST, faceBuilder -> faceBuilder.uvs(16, 0, 10, 16).texture(bodySlot).rotation(Quadrant.R270));
                })

                .build();

        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block.get()))
                .put(bodySlot, TextureMapping.getBlockTexture(block.get()))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block.get(), "_top"))
                .put(baseSlot, Identifier.withDefaultNamespace("block/crying_obsidian"));

        MultiVariant variant = BlockModelGenerators.plainVariant(template.create(block.get(), mapping, models.modelOutput));
        models.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block.get(), variant).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
    }

    public static void upgrade(DeferredHolder<Block, ? extends Block> block){
        ModelTemplates.CUBE_COLUMN.create(block.get(), new TextureMapping()
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block.get()))
                .put(TextureSlot.END, Woot.identifier("block/factory_base")), models.modelOutput);

        models.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block.get(), MultiVariant.of(new FactoryUpgradeBlockBaseModel.Builder())));
    }
}
