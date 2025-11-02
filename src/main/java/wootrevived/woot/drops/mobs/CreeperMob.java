package wootrevived.woot.drops.mobs;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;
import wootrevived.api.registrations.WootFactoryMobRegistration;

public class CreeperMob extends WootFactoryMob<Creeper> {
    public CreeperMob(EntityType<Creeper> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public MutableComponent getDisplayName(ValueInput input) {
        MutableComponent tip = Component.empty();
        if(input.getBooleanOr("powered", false)){
            tip.append("Charged ");
        }
        return tip.append(super.getDisplayName(input));
    }

    @Override
    public void saveTag(ValueInput input, ValueOutput output){
        super.saveTag(input, output);
        output.putBoolean("powered", input.getBooleanOr("powered", false));
    }

    @Override
    public boolean isSame(ValueInput shardInput, ValueInput mobInput){
        if(!super.isSame(shardInput, mobInput))
            return false;

        boolean isShardPowered = shardInput.getBooleanOr("powered", false);
        boolean isMobPowered = mobInput.getBooleanOr("powered", false);
        return isShardPowered == isMobPowered;
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new CreeperMob(EntityType.CREEPER, new Properties().tier(Tier.TIER_2)));
    }
}
