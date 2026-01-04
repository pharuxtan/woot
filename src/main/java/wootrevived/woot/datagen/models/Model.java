package wootrevived.woot.datagen.models;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;
import wootrevived.woot.Woot;

public class Model extends ModelProvider {
    public Model(PackOutput output) {
        super(output, Woot.MOD_NAMESPACE);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels){
        Blocks.registerModels(blockModels);
        Items.registerModels(itemModels);
    }
}
