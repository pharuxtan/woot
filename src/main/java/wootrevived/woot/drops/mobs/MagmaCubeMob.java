package wootrevived.woot.drops.mobs;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class MagmaCubeMob extends WootFactoryMob<MagmaCube> {
    public MagmaCubeMob(EntityType<MagmaCube> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public MutableComponent getDisplayName(ValueInput input) {
        MutableComponent tip = Component.literal(
                input.getInt("Size").orElse(0) > 0 ?
                        "Large " :
                        "Small "
        );
        return tip.append(super.getDisplayName(input));
    }

    @Override
    public void saveTag(ValueInput input, ValueOutput output){
        super.saveTag(input, output);
        input.getInt("Size").ifPresent(size -> output.putInt("Size", size));
    }

    @Override
    public boolean isSame(ValueInput shardInput, ValueInput mobInput){
        if(!super.isSame(shardInput, mobInput))
            return false;

        boolean isShardLarge = shardInput.getInt("Size").orElse(0) > 0;
        boolean isMobLarge = mobInput.getInt("Size").orElse(0) > 0;
        return isShardLarge == isMobLarge;
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new MagmaCubeMob(EntityType.MAGMA_CUBE, new Properties().tier(Tier.TIER_3)));
    }
}
