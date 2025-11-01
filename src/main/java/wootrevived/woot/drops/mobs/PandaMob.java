package wootrevived.woot.drops.mobs;

import com.google.common.base.CaseFormat;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Panda;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class PandaMob extends WootFactoryMob<Panda> {
    public PandaMob(EntityType<Panda> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public MutableComponent getDisplayName(CompoundTag mobTag, RegistryAccess registryAccess) {
        MutableComponent tip = Component.empty();
        Panda.Gene mainGene = mobTag.read("MainGene", Panda.Gene.CODEC).orElse(Panda.Gene.NORMAL);
        Panda.Gene hiddenGene = mobTag.read("MainGene", Panda.Gene.CODEC).orElse(Panda.Gene.NORMAL);
        if(mainGene.equals(hiddenGene)){
            tip.append(Component.literal(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, mainGene.getSerializedName()).replaceAll("([a-z])([A-Z])", "$1 $2") + " "));
        } else {
            tip.append(Component.literal("Normal "));
        }
        return tip.append(super.getDisplayName(mobTag, registryAccess));
    }

    @Override
    public MutableComponent getTooltipKillName(CompoundTag mobTag, RegistryAccess registryAccess) {
        return super.getDisplayName(mobTag, registryAccess);
    }

    @Override
    public CompoundTag saveTag(CompoundTag mobTag, RegistryAccess registryAccess){
        CompoundTag tag = super.saveTag(mobTag, registryAccess);
        tag.store("MainGene", Panda.Gene.CODEC, mobTag.read("MainGene", Panda.Gene.CODEC).orElse(Panda.Gene.NORMAL));
        tag.store("HiddenGene", Panda.Gene.CODEC, mobTag.read("HiddenGene", Panda.Gene.CODEC).orElse(Panda.Gene.NORMAL));
        return tag;
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new PandaMob(EntityType.PANDA, new Properties()));
    }
}
