package wootrevived.woot.drops.mobs;

import com.google.common.base.CaseFormat;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class TropicalFishMob extends WootFactoryMob<TropicalFish> {
    public TropicalFishMob(EntityType<TropicalFish> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public MutableComponent getDisplayName(ValueInput input) {
        TropicalFish.Variant variant = input.read("Variant", TropicalFish.Variant.CODEC).orElse(TropicalFish.DEFAULT_VARIANT);
        MutableComponent tip = Component.literal(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, variant.pattern().getSerializedName()).replaceAll("([a-z])([A-Z])", "$1 $2") + " ");
        return tip.append(super.getDisplayName(input));
    }

    @Override
    public MutableComponent getTooltipKillName(ValueInput input) {
        return super.getDisplayName(input);
    }

    @Override
    public void saveTag(ValueInput input, ValueOutput output){
        super.saveTag(input, output);
        output.store("Variant", TropicalFish.Variant.CODEC, input.read("Variant", TropicalFish.Variant.CODEC).orElse(TropicalFish.DEFAULT_VARIANT));
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new TropicalFishMob(EntityType.TROPICAL_FISH, new Properties()));
    }
}
