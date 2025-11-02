package wootrevived.woot.drops.mobs;

import com.google.common.base.CaseFormat;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.variant.VariantUtils;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class PigMob extends WootFactoryMob<Pig> {
    public PigMob(EntityType<Pig> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public MutableComponent getDisplayName(ValueInput input) {
        MutableComponent tip = Component.empty();
        VariantUtils.readVariant(input, Registries.PIG_VARIANT).flatMap(Holder::unwrapKey).ifPresent(key -> {
            tip.append(Component.literal(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, key.location().getPath().replaceAll("([a-z])([A-Z])", "$1 $2") + " ")));
        });
        return tip.append(super.getDisplayName(input));
    }

    @Override
    public MutableComponent getTooltipKillName(ValueInput input) {
        return super.getDisplayName(input);
    }

    @Override
    public void saveTag(ValueInput input, ValueOutput output){
        super.saveTag(input, output);
        VariantUtils.readVariant(input, Registries.PIG_VARIANT).ifPresent(variant -> VariantUtils.writeVariant(output, variant));
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new PigMob(EntityType.PIG, new Properties()));
    }
}
