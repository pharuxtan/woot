package wootrevived.woot.drops.mobs;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.ValueInput;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.registrations.WootFactoryMobRegistration;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WitherMob extends WootFactoryMob<WitherBoss> {
    public WitherMob(EntityType<WitherBoss> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public void modifyDrops(Phase phase, WootDropsProperties properties){
        if(!phase.isBeforeDropCallback())
            return;

        List<ItemStack> generatedDrops = properties.getItemDrops();

        AtomicInteger looting = new AtomicInteger();
        ItemStack handStack = properties.getMainHandItem();
        if(EnchantmentHelper.hasAnyEnchantments(handStack)){
            HolderLookup.Provider accessor = properties.getRegistryAccess();
            HolderLookup.RegistryLookup<Enchantment> lookup = accessor.lookupOrThrow(Registries.ENCHANTMENT);

            lookup.get(Enchantments.LOOTING).ifPresent(enchantment -> {
                looting.set(handStack.getEnchantmentLevel(enchantment));
            });
        }

        ItemStack witherRose = Items.WITHER_ROSE.getDefaultInstance();
        witherRose.setCount(1 + looting.get());
        generatedDrops.add(witherRose);

        if(properties.doSimulateChargedCreeper()){
            for(ItemStack stack : List.copyOf(generatedDrops)){
                if(stack.is(Items.NETHER_STAR)){
                    generatedDrops.remove(stack);
                    break;
                }
            }
        }
    }

    @Override
    public List<ItemStack> getImportItems(ValueInput input){
        ItemStack witherSkeletonSkull = Items.WITHER_SKELETON_SKULL.getDefaultInstance();
        witherSkeletonSkull.setCount(3);

        ItemStack soulSand = Items.SOUL_SAND.getDefaultInstance();
        soulSand.setCount(4);

        return List.of(witherSkeletonSkull, soulSand);
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new WitherMob(EntityType.WITHER, new Properties().tier(Tier.TIER_5)));
    }
}
