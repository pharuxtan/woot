package wootrevived.woot.util.helper;

import net.minecraft.core.HolderLookup;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import wootrevived.woot.Woot;

public class SerializeEntityValueHelper {
    public static final ProblemReporter.ScopedCollector REPORTER = Woot.reporter("WootEntitySerialization");

    public static ValueInput serialize(Entity entity, HolderLookup.Provider provider){
        TagValueOutput output = TagValueOutput.createWithContext(REPORTER, provider);
        output.putString("id", entity.getEncodeId());
        try {
            entity.saveWithoutId(output);
        } catch(Exception ignored){}
        return TagValueInput.create(REPORTER, provider, output.buildResult());
    }
}
