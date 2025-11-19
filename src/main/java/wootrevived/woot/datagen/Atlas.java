package wootrevived.woot.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;
import wootrevived.api.models.DynamicUpgradeItemModelBuilder;
import wootrevived.woot.Woot;
import wootrevived.woot.client.sprite.FactoryUpgradeDynamicSpriteSource;
import wootrevived.woot.client.sprite.UpgradeItemDynamicSpriteSource;
import wootrevived.woot.registries.BlocksRegistry;

import java.util.concurrent.CompletableFuture;

public class Atlas extends SpriteSourceProvider {
    public Atlas(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper){
        super(output, lookupProvider, "minecraft", existingFileHelper);
    }

    @Override
    protected void gather() {
        atlas(BLOCKS_ATLAS)
                .addSource(new UpgradeItemDynamicSpriteSource(Woot.location(DynamicUpgradeItemModelBuilder.ID)))
                .addSource(new FactoryUpgradeDynamicSpriteSource(Woot.location(BlocksRegistry.FACTORY_UPGRADE_TAG)));
    }
}
