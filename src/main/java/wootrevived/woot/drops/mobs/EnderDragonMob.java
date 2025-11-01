package wootrevived.woot.drops.mobs;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.registrations.WootFactoryMobRegistration;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EnderDragonMob extends WootFactoryMob<EnderDragon> {
    public EnderDragonMob(EntityType<EnderDragon> entityType, Properties properties) {
        super(entityType, properties);
    }

    @Override
    public void modifyDrops(Phase phase, WootDropsProperties properties){
        if(!phase.isBeforeDropCallback())
            return;

        List<ItemStack> generatedDrops = properties.getItemDrops();

        generatedDrops.add(Items.DRAGON_EGG.getDefaultInstance());

        AtomicInteger looting = new AtomicInteger();
        ItemStack stack = properties.getMainHandItem();
        if(EnchantmentHelper.hasAnyEnchantments(stack)){
            HolderLookup.Provider accessor = properties.getRegistryAccess();
            HolderLookup.RegistryLookup<Enchantment> lookup = accessor.lookupOrThrow(Registries.ENCHANTMENT);

            lookup.get(Enchantments.LOOTING).ifPresent(enchantment -> {
                looting.set(stack.getEnchantmentLevel(enchantment));
            });
        }

        ItemStack dragonBreath = Items.DRAGON_BREATH.getDefaultInstance();
        dragonBreath.setCount(16 * (looting.get() + 1));
        generatedDrops.add(dragonBreath);

        if(properties.doSimulateChargedCreeper()){
            generatedDrops.add(Items.DRAGON_HEAD.getDefaultInstance());
        }
    }

    @Override
    public List<ItemStack> getImportItems(CompoundTag mobTag, RegistryAccess registryAccess){
        ItemStack endCrystal = Items.END_CRYSTAL.getDefaultInstance();
        endCrystal.setCount(4);
        return List.of(endCrystal);
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new EnderDragonMob(EntityType.ENDER_DRAGON,
                new Properties()
                        .tier(Tier.TIER_5)
        ));
    }
}
