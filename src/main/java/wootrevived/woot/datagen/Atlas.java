package wootrevived.woot.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.data.SpriteSourceProvider;
import wootrevived.woot.Woot;
import wootrevived.woot.client.sprite.factory_upgrade.FactoryUpgradeDynamicSpriteSource;
import wootrevived.woot.registries.BlocksRegistry;

import java.util.concurrent.CompletableFuture;

public class Atlas extends SpriteSourceProvider {
    public Atlas(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider){
        super(output, lookupProvider, "minecraft");
    }

    @Override
    protected void gather() {
        atlas(BLOCKS_ATLAS)
                .addSource(new FactoryUpgradeDynamicSpriteSource(Woot.location(BlocksRegistry.FACTORY_UPGRADE_TAG)));
    }
}
