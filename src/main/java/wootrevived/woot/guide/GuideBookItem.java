package wootrevived.woot.guide;

import guideme.GuidesCommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.Woot;

public class GuideBookItem extends Item {
    public GuideBookItem(String tag) {
        super(new Properties().stacksTo(1)
                .setId(ResourceKey.create(Registries.ITEM, Woot.location(tag))));
    }

    @Override
    public InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if(level.isClientSide())
            GuidesCommon.openGuide(player, WootGuide.ID);

        return InteractionResult.CONSUME;
    }
}
