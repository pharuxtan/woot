package wootrevived.woot.client.model.factory_upgrade;

import com.mojang.math.Quadrant;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import wootrevived.api.interfaces.WootUpgradeEnum;
import wootrevived.woot.Woot;
import wootrevived.woot.blocks.factory_upgrade.FactoryUpgradeBlockEntity;
import wootrevived.woot.registries.UpgradeItemsRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public record FactoryUpgradeDynamicModel(Map<String, List<BlockModelPart>> variants, TextureAtlasSprite defaultParticle) implements DynamicBlockStateModel {
    public static @NotNull FactoryUpgradeDynamicModel bake(ResolvedModel model, ModelBaker baker) {
        Map<String, List<BlockModelPart>> variants = new HashMap<>();

        TextureSlots slots = model.getTopTextureSlots();
        SpriteGetter spriteGetter = baker.sprites();

        QuadCollection bake = model.bakeTopGeometry(slots, baker, BlockModelRotation.X0_Y0);

        TextureAtlasSprite factory = spriteGetter.get(slots.getMaterial("north"), model);
        variants.put("", getBlockModelParts(model, bake, factory));

        for(UpgradeItemsRegistry.Entry<?> entry : UpgradeItemsRegistry.getEntries())
            processEntry(variants, entry, model, bake, spriteGetter);

        for(UpgradeItemsRegistry.DynamicEntry<?> entry : UpgradeItemsRegistry.getDynamicEntries())
            processDynamicEntry(variants, entry, model, bake, spriteGetter);

        return new FactoryUpgradeDynamicModel(variants, factory);
    }

    private static <T extends Enum<T> & WootUpgradeEnum<T>> void processEntry(Map<String, List<BlockModelPart>> variants, UpgradeItemsRegistry.Entry<T> entry, ResolvedModel model, QuadCollection bake, SpriteGetter spriteGetter) {
        String name = UpgradeItemsRegistry.getNameFromItem(entry.item());

        TextureAtlasSprite texture = spriteGetter.get(new Material(
                Sheets.BLOCKS_MAPPER.sheet(),
                Woot.location("block/upgrade_item_" + name)
        ), model);

        variants.put(name, getBlockModelParts(model, bake, texture));
    }

    private static <T extends Enum<T> & WootUpgradeEnum<T>> void processDynamicEntry(Map<String, List<BlockModelPart>> variants, UpgradeItemsRegistry.DynamicEntry<T> entry, ResolvedModel model, QuadCollection bake, SpriteGetter spriteGetter) {
        for (T variant : entry.variantClass().getEnumConstants()) {
            String name = variant.getSerializedName() + "_" + UpgradeItemsRegistry.getNameFromItem(entry.item());

            TextureAtlasSprite texture = spriteGetter.get(new Material(
                    Sheets.BLOCKS_MAPPER.sheet(),
                    Woot.location("block/upgrade_item_" + name)
            ), model);

            variants.put(name, getBlockModelParts(model, bake, texture));
        }
    }

    private static List<BlockModelPart> getBlockModelParts(ResolvedModel model, QuadCollection bake, TextureAtlasSprite texture) {
        QuadCollection.Builder builder = new QuadCollection.Builder();

        for(Direction direction : Direction.Plane.VERTICAL)
            builder.addCulledFace(direction, bake.getQuads(direction).getFirst());

        for(Direction direction : Direction.Plane.HORIZONTAL){
            BakedQuad quad = bake.getQuads(direction).getFirst();

            builder.addCulledFace(direction, FaceBakery.bakeQuad(
                    new Vector3f(0F, 0F, 0F),
                    new Vector3f(16F, 16F, 16F),
                    new BlockElementFace(
                            direction,
                            quad.tintIndex(),
                            texture.atlasLocation().toString(),
                            new BlockElementFace.UVs(0, 0, 16, 16),
                            Quadrant.R0
                    ),
                    texture,
                    direction,
                    BlockModelRotation.X0_Y0,
                    null,
                    quad.shade(),
                    quad.lightEmission()
            ));
        }

        return List.of(new SimpleModelWrapper(builder.build(), model.getTopAmbientOcclusion(), texture, RenderType.solid()));
    }

    @Override
    public void collectParts(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull RandomSource random, @NotNull List<BlockModelPart> parts) {
        String key = "";
        if(level.getBlockEntity(pos) instanceof FactoryUpgradeBlockEntity blockEntity)
            key = blockEntity.getUpgradeItemName();
        parts.addAll(variants.getOrDefault(key, variants.get("")));
    }

    @Override
    public @NotNull TextureAtlasSprite particleIcon(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state) {
        String key = "";
        if(level.getBlockEntity(pos) instanceof FactoryUpgradeBlockEntity blockEntity)
            key = blockEntity.getUpgradeItemName();
        return variants.getOrDefault(key, variants.get("")).getFirst().particleIcon();
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull TextureAtlasSprite particleIcon() {
        return defaultParticle;
    }
}
