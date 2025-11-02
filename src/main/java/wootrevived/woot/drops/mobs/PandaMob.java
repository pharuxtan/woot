package wootrevived.woot.drops.mobs;

import com.google.common.base.CaseFormat;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class PandaMob extends WootFactoryMob<Panda> {
    public PandaMob(EntityType<Panda> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public MutableComponent getDisplayName(ValueInput input) {
        MutableComponent tip = Component.empty();
        Panda.Gene mainGene = input.read("MainGene", Panda.Gene.CODEC).orElse(Panda.Gene.NORMAL);
        Panda.Gene hiddenGene = input.read("MainGene", Panda.Gene.CODEC).orElse(Panda.Gene.NORMAL);
        if(mainGene.equals(hiddenGene)){
            tip.append(Component.literal(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, mainGene.getSerializedName()).replaceAll("([a-z])([A-Z])", "$1 $2") + " "));
        } else {
            tip.append(Component.literal("Normal "));
        }
        return tip.append(super.getDisplayName(input));
    }

    @Override
    public MutableComponent getTooltipKillName(ValueInput input) {
        return super.getDisplayName(input);
    }

    @Override
    public void saveTag(ValueInput input, ValueOutput output){
        super.saveTag(input, output);
        output.store("MainGene", Panda.Gene.CODEC, input.read("MainGene", Panda.Gene.CODEC).orElse(Panda.Gene.NORMAL));
        output.store("HiddenGene", Panda.Gene.CODEC, input.read("HiddenGene", Panda.Gene.CODEC).orElse(Panda.Gene.NORMAL));
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new PandaMob(EntityType.PANDA, new Properties()));
    }
}
