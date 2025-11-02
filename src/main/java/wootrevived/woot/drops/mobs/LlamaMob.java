package wootrevived.woot.drops.mobs;

import com.google.common.base.CaseFormat;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class LlamaMob extends WootFactoryMob<Llama> {
    public LlamaMob(EntityType<Llama> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public MutableComponent getDisplayName(ValueInput input) {
        Llama.Variant variant = input.read("Variant", Llama.Variant.LEGACY_CODEC).orElse(Llama.Variant.DEFAULT);
        MutableComponent tip = Component.literal(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, variant.getSerializedName()).replaceAll("([a-z])([A-Z])", "$1 $2") + " ");
        return tip.append(super.getDisplayName(input));
    }

    @Override
    public MutableComponent getTooltipKillName(ValueInput input) {
        return super.getDisplayName(input);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void saveTag(ValueInput input, ValueOutput output){
        super.saveTag(input, output);
        output.store("Variant", Llama.Variant.LEGACY_CODEC, input.read("Variant", Llama.Variant.LEGACY_CODEC).orElse(Llama.Variant.DEFAULT));
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new LlamaMob(EntityType.LLAMA, new Properties()));
    }
}
