package wootrevived.woot.drops.mobs;

import com.google.common.base.CaseFormat;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class FoxMob extends WootFactoryMob<Fox> {
    public FoxMob(EntityType<Fox> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public MutableComponent getDisplayName(ValueInput input) {
        Fox.Variant variant = input.read("Type", Fox.Variant.CODEC).orElse(Fox.Variant.DEFAULT);
        MutableComponent tip = Component.literal(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, variant.getSerializedName()).replaceAll("([a-z])([A-Z])", "$1 $2") + " ");
        return tip.append(super.getDisplayName(input));
    }

    @Override
    public MutableComponent getTooltipKillName(ValueInput input) {
        return super.getDisplayName(input);
    }

    @Override
    public void saveTag(ValueInput input, ValueOutput output){
        super.saveTag(input, output);
        output.store("Type", Fox.Variant.CODEC, input.read("Type", Fox.Variant.CODEC).orElse(Fox.Variant.DEFAULT));
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new FoxMob(EntityType.FOX, new Properties()));
    }
}
