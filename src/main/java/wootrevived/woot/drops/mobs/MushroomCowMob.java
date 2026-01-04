package wootrevived.woot.drops.mobs;

import com.google.common.base.CaseFormat;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class MushroomCowMob extends WootFactoryMob<MushroomCow> {
    public MushroomCowMob(EntityType<MushroomCow> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public MutableComponent getDisplayName(ValueInput input) {
        MushroomCow.Variant variant = input.read("Type", MushroomCow.Variant.CODEC).orElse(MushroomCow.Variant.DEFAULT);
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
        output.store("Type", MushroomCow.Variant.CODEC, input.read("Type", MushroomCow.Variant.CODEC).orElse(MushroomCow.Variant.DEFAULT));
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new MushroomCowMob(EntityType.MOOSHROOM, new Properties()));
    }
}
