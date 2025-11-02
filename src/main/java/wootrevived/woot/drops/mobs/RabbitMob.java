package wootrevived.woot.drops.mobs;

import com.google.common.base.CaseFormat;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class RabbitMob extends WootFactoryMob<Rabbit> {
    public RabbitMob(EntityType<Rabbit> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public MutableComponent getDisplayName(ValueInput input) {
        Rabbit.Variant variant = input.read("RabbitType", Rabbit.Variant.LEGACY_CODEC).orElse(Rabbit.Variant.DEFAULT);
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
        output.store("RabbitType", Rabbit.Variant.LEGACY_CODEC, input.read("RabbitType", Rabbit.Variant.LEGACY_CODEC).orElse(Rabbit.Variant.DEFAULT));
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new RabbitMob(EntityType.RABBIT, new Properties()));
    }
}
