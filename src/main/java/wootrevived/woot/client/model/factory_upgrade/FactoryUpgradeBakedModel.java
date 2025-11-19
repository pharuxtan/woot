package wootrevived.woot.client.model.factory_upgrade;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.RenderTypeGroup;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.api.interfaces.WootUpgradeEnum;
import wootrevived.woot.blocks.factory_upgrade.FactoryUpgradeBlockEntity;
import wootrevived.woot.registries.UpgradeItemsRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class FactoryUpgradeBakedModel extends BakedModelWrapper<SimpleBakedModel> {
    protected final Map<String, Map<Direction, List<BakedQuad>>> faces;
    protected final Map<String, TextureAtlasSprite> particles;

    public static final ModelProperty<String> UPGRADE_PROPERTY = new ModelProperty<>();

    public FactoryUpgradeBakedModel(Map<String, Map<Direction, List<BakedQuad>>> faces, boolean hasAmbientOcclusion, boolean isGui3d, boolean usesBlockLight, Map<String, TextureAtlasSprite> particles, ItemTransforms transforms, ItemOverrides overrides) {
        super(new SimpleBakedModel(new ArrayList<>(), faces.get(""), hasAmbientOcclusion, isGui3d, usesBlockLight, particles.get(""), transforms, overrides, RenderTypeGroup.EMPTY));
        this.faces = faces;
        this.particles = particles;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, @NotNull RandomSource randomSource) {
        return getQuads(blockState, direction, randomSource, ModelData.EMPTY, null);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, @NotNull RandomSource randomSource, @NotNull ModelData modelData, @Nullable RenderType renderType) {
        if(direction == null) return originalModel.getQuads(blockState, null, randomSource);
        String upgrade = "";
        if(modelData.has(UPGRADE_PROPERTY)) upgrade = modelData.get(UPGRADE_PROPERTY);
        return faces.get(upgrade).get(direction);
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        if(data.has(UPGRADE_PROPERTY)) return particles.get(data.get(UPGRADE_PROPERTY));
        return getParticleIcon();
    }

    @Override
    public @NotNull ModelData getModelData(@Nullable BlockAndTintGetter level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull ModelData modelData) {
        String upgrade = "";
        if(level instanceof BlockAndTintGetter){
            BlockEntity entity = level.getBlockEntity(blockPos);
            if(entity instanceof FactoryUpgradeBlockEntity factoryUpgradeBlockEntity){
                upgrade = factoryUpgradeBlockEntity.getUpgradeItemName();
                if(UpgradeItemsRegistry.isDynamic(upgrade)){
                    ItemStack item = factoryUpgradeBlockEntity.getUpgradeItemStack();
                    upgrade = UpgradeItemsRegistry.get(upgrade).get().getVariant(item.getTag()).getSerializedName() + "_" + upgrade;
                }
            }
        }
        return modelData.derive().with(UPGRADE_PROPERTY, upgrade).build();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Builder {
        private final Map<String, Map<Direction, List<BakedQuad>>> faces = new HashMap<>();
        private final Map<String, TextureAtlasSprite> particles = new HashMap<>();
        private final boolean hasAmbientOcclusion;
        private final boolean usesBlockLight;
        private final boolean isGui3d;
        private final ItemTransforms transforms;
        private final ItemOverrides overrides;

        public Builder(boolean hasAmbientOcclusion, boolean usesBlockLight, boolean isGui3d, ItemTransforms transforms, ItemOverrides overrides) {
            Map<Direction, List<BakedQuad>> map = Maps.newEnumMap(Direction.class);
            for(Direction direction : Direction.values()) {
                map.put(direction, new ArrayList<>());
            }
            this.faces.put("", map);

            for(UpgradeItemsRegistry.Entry<?> entry : UpgradeItemsRegistry.getEntries())
                processEntry(entry);

            for(UpgradeItemsRegistry.DynamicEntry<?> entry : UpgradeItemsRegistry.getDynamicEntries())
                processDynamicEntry(entry);

            this.hasAmbientOcclusion = hasAmbientOcclusion;
            this.usesBlockLight = usesBlockLight;
            this.isGui3d = isGui3d;
            this.transforms = transforms;
            this.overrides = overrides;
        }

        private <T extends Enum<T> & WootUpgradeEnum<T>> void processEntry(UpgradeItemsRegistry.Entry<T> entry){
            Map<Direction, List<BakedQuad>> map = Maps.newEnumMap(Direction.class);
            for(Direction direction : Direction.values()) {
                map.put(direction, new ArrayList<>());
            }
            this.faces.put(UpgradeItemsRegistry.getNameFromItem(entry.item()), map);
        }

        private <T extends Enum<T> & WootUpgradeEnum<T>> void processDynamicEntry(UpgradeItemsRegistry.DynamicEntry<T> entry){
            Map<Direction, List<BakedQuad>> map;
            for (T variant : entry.variantClass().getEnumConstants()) {
                map = Maps.newEnumMap(Direction.class);
                for(Direction direction : Direction.values()) {
                    map.put(direction, new ArrayList<>());
                }
                this.faces.put(variant.getSerializedName() + "_" + UpgradeItemsRegistry.getNameFromItem(entry.item()), map);
            }
        }

        public void addFace(Direction direction, String upgrade, BakedQuad quad) {
            this.faces.get(upgrade).get(direction).add(quad);
        }

        public Builder addParticle(String upgrade, TextureAtlasSprite particle) {
            this.particles.put(upgrade, particle);
            return this;
        }

        public BakedModel build() {
            return new FactoryUpgradeBakedModel(this.faces, this.hasAmbientOcclusion, this.usesBlockLight, this.isGui3d, this.particles, this.transforms, this.overrides);
        }
    }
}
