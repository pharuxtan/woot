package wootrevived.woot.drops.mobs;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.registrations.WootFactoryMobRegistration;

import java.util.List;

public class SheepMob extends WootFactoryMob<Sheep> {
    public SheepMob(EntityType<Sheep> entityType, Properties properties) {
        super(entityType, properties);
    }

    List<Item> wools = List.of(
            Items.WHITE_WOOL,
            Items.ORANGE_WOOL,
            Items.MAGENTA_WOOL,
            Items.LIGHT_BLUE_WOOL,
            Items.YELLOW_WOOL,
            Items.LIME_WOOL,
            Items.PINK_WOOL,
            Items.GRAY_WOOL,
            Items.LIGHT_GRAY_WOOL,
            Items.CYAN_WOOL,
            Items.PURPLE_WOOL,
            Items.BLUE_WOOL,
            Items.BROWN_WOOL,
            Items.GREEN_WOOL,
            Items.RED_WOOL,
            Items.BLACK_WOOL
    );

    Ingredient woolIngredient = Ingredient.of(wools.stream());

    @Override
    public void modifyDrops(Phase phase, WootDropsProperties properties){
        if(!phase.isBeforeDropCallback())
            return;

        List<ItemStack> generatedDrops = properties.getItemDrops();

        for(ItemStack stack : List.copyOf(generatedDrops)){
            if(woolIngredient.test(stack))
                generatedDrops.remove(stack);
        }

        int index = properties.getRandom().nextInt(16);
        generatedDrops.add(wools.get(index).getDefaultInstance());
    }

    public static void register(WootFactoryMobRegistration registration) {
        registration.registerFactoryMob(new SheepMob(EntityType.SHEEP, new Properties()));
    }
}
