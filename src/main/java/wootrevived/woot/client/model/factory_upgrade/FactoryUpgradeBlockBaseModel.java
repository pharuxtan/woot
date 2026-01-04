package wootrevived.woot.client.model.factory_upgrade;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import net.neoforged.neoforge.client.model.generators.blockstate.CustomBlockStateModelBuilder;
import net.neoforged.neoforge.client.model.generators.blockstate.UnbakedMutator;
import wootrevived.woot.Woot;
import wootrevived.woot.registries.BlocksRegistry;

public class FactoryUpgradeBlockBaseModel implements CustomUnbakedBlockStateModel {
    public static final MapCodec<FactoryUpgradeBlockBaseModel> CODEC = MapCodec.unit(FactoryUpgradeBlockBaseModel::new);

    public static final Identifier ID = Woot.identifier(BlocksRegistry.FACTORY_UPGRADE_TAG);

    @Override
    public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
        return CODEC;
    }

    @Override
    public BlockStateModel bake(ModelBaker modelBaker) {
        ResolvedModel parent = modelBaker.getModel(Woot.identifier("block/factory_upgrade"));
        return FactoryUpgradeDynamicModel.bake(parent, modelBaker);
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        resolver.markDependency(Woot.identifier("block/factory_upgrade"));
    }

    public static class Builder extends CustomBlockStateModelBuilder {
        public Builder(){}

        @Override
        public CustomBlockStateModelBuilder with(VariantMutator variantMutator) {
            return this;
        }

        @Override
        public CustomBlockStateModelBuilder with(UnbakedMutator unbakedMutator) {
            return this;
        }

        @Override
        public CustomUnbakedBlockStateModel toUnbaked() {
            return new FactoryUpgradeBlockBaseModel();
        }
    }
}
