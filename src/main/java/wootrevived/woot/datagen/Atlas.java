package wootrevived.woot.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.data.SpriteSourceProvider;
import wootrevived.api.models.DynamicUpgradeItemModelUnbaked;
import wootrevived.woot.Woot;
import wootrevived.woot.client.sprite.FactoryUpgradeDynamicSpriteSource;
import wootrevived.woot.client.sprite.UpgradeItemDynamicSpriteSource;
import wootrevived.woot.registries.BlocksRegistry;

import java.util.concurrent.CompletableFuture;

public class Atlas extends SpriteSourceProvider {
    public Atlas(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider){
        super(output, lookupProvider, "minecraft");
    }

    @Override
    protected void gather() {
        atlas(AtlasIds.ITEMS)
                .addSource(new UpgradeItemDynamicSpriteSource(Woot.identifier(DynamicUpgradeItemModelUnbaked.ID)));

        atlas(AtlasIds.BLOCKS)
                .addSource(new FactoryUpgradeDynamicSpriteSource(Woot.identifier(BlocksRegistry.FACTORY_UPGRADE_TAG)));
    }
}
