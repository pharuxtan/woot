package wootrevived.woot.client.model.factory_upgrade;

import com.mojang.math.Quadrant;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import wootrevived.api.interfaces.WootUpgradeEnum;
import wootrevived.woot.Woot;
import wootrevived.woot.blocks.factory_upgrade.FactoryUpgradeBlockEntity;
import wootrevived.woot.registries.UpgradeItemsRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record FactoryUpgradeDynamicModel(Map<String, List<BlockModelPart>> variants, TextureAtlasSprite defaultParticle) implements DynamicBlockStateModel {
    public static FactoryUpgradeDynamicModel bake(ResolvedModel model, ModelBaker baker) {
        Map<String, List<BlockModelPart>> variants = new HashMap<>();

        TextureSlots slots = model.getTopTextureSlots();
        SpriteGetter spriteGetter = baker.sprites();

        QuadCollection bake = model.bakeTopGeometry(slots, baker, BlockModelRotation.IDENTITY);

        TextureAtlasSprite factory = spriteGetter.get(slots.getMaterial("north"), model);
        variants.put("", getBlockModelParts(model, bake, factory, baker.parts()));

        for(UpgradeItemsRegistry.Entry<?> entry : UpgradeItemsRegistry.getEntries())
            processEntry(variants, entry, model, bake, spriteGetter, baker.parts());

        for(UpgradeItemsRegistry.DynamicEntry<?> entry : UpgradeItemsRegistry.getDynamicEntries())
            processDynamicEntry(variants, entry, model, bake, spriteGetter, baker.parts());

        return new FactoryUpgradeDynamicModel(variants, factory);
    }

    private static <T extends Enum<T> & WootUpgradeEnum<T>> void processEntry(Map<String, List<BlockModelPart>> variants, UpgradeItemsRegistry.Entry<T> entry, ResolvedModel model, QuadCollection bake, SpriteGetter spriteGetter, ModelBaker.PartCache cache) {
        String name = UpgradeItemsRegistry.getNameFromItem(entry.item());

        TextureAtlasSprite texture = spriteGetter.get(new Material(
                Sheets.BLOCKS_MAPPER.sheet(),
                Woot.identifier("block/upgrade_item_" + name)
        ), model);

        variants.put(name, getBlockModelParts(model, bake, texture, cache));
    }

    private static <T extends Enum<T> & WootUpgradeEnum<T>> void processDynamicEntry(Map<String, List<BlockModelPart>> variants, UpgradeItemsRegistry.DynamicEntry<T> entry, ResolvedModel model, QuadCollection bake, SpriteGetter spriteGetter, ModelBaker.PartCache cache) {
        for (T variant : entry.variantClass().getEnumConstants()) {
            String name = variant.getSerializedName() + "_" + UpgradeItemsRegistry.getNameFromItem(entry.item());

            TextureAtlasSprite texture = spriteGetter.get(new Material(
                    Sheets.BLOCKS_MAPPER.sheet(),
                    Woot.identifier("block/upgrade_item_" + name)
            ), model);

            variants.put(name, getBlockModelParts(model, bake, texture, cache));
        }
    }

    private static List<BlockModelPart> getBlockModelParts(ResolvedModel model, QuadCollection bake, TextureAtlasSprite texture, ModelBaker.PartCache cache) {
        QuadCollection.Builder builder = new QuadCollection.Builder();

        for(Direction direction : Direction.Plane.VERTICAL)
            builder.addCulledFace(direction, bake.getQuads(direction).getFirst());

        for(Direction direction : Direction.Plane.HORIZONTAL){
            BakedQuad quad = bake.getQuads(direction).getFirst();

            builder.addCulledFace(direction, FaceBakery.bakeQuad(
                    cache,
                    cache.vector(0F, 0F, 0F),
                    cache.vector(16F, 16F, 16F),
                    new BlockElementFace(
                            direction,
                            quad.tintIndex(),
                            texture.atlasLocation().toString(),
                            new BlockElementFace.UVs(0, 0, 16, 16),
                            Quadrant.R0
                    ),
                    texture,
                    direction,
                    BlockModelRotation.IDENTITY,
                    null,
                    quad.shade(),
                    quad.lightEmission()
            ));
        }

        return List.of(new SimpleModelWrapper(builder.build(), model.getTopAmbientOcclusion(), texture, ChunkSectionLayer.SOLID));
    }

    @Override
    public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        String key = "";
        if(level.getBlockEntity(pos) instanceof FactoryUpgradeBlockEntity blockEntity)
            key = blockEntity.getUpgradeItemName();
        parts.addAll(variants.getOrDefault(key, variants.get("")));
    }

    @Override
    public TextureAtlasSprite particleIcon(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        String key = "";
        if(level.getBlockEntity(pos) instanceof FactoryUpgradeBlockEntity blockEntity)
            key = blockEntity.getUpgradeItemName();
        return variants.getOrDefault(key, variants.get("")).getFirst().particleIcon();
    }

    @Override
    @SuppressWarnings("deprecation")
    public TextureAtlasSprite particleIcon() {
        return defaultParticle;
    }
}
