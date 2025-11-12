package wootrevived.woot.util.render.guide;

import guideme.document.interaction.GuideTooltip;
import guideme.siteexport.ResourceExporter;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.ValueInput;
import wootrevived.api.WootFactoryMob;
import wootrevived.woot.util.common.WootTier;
import wootrevived.woot.util.helper.ModNameHelper;
import wootrevived.woot.util.render.WootContainerScreen;

import java.util.List;

import static wootrevived.woot.util.render.WootStyles.*;

public class EntityTooltip implements GuideTooltip {
    private final WootFactoryMob<?> mob;
    private final ValueInput input;

    public EntityTooltip(WootFactoryMob<?> mob, ValueInput input){
        this.mob = mob;
        this.input = input;
    }

    @Override
    public List<ClientTooltipComponent> getLines() {
        String modId = BuiltInRegistries.ENTITY_TYPE.getKey(mob.getEntityType()).getNamespace();

        List<Component> lines = List.of(
                mob.getDisplayName(input).append(Component.literal(": ")).setStyle(MACHINE_STYLE),
                Component.empty()
                        .append(Component.translatable("info.woot_revived.tier").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                        .append(Component.translatable(WootTier.getTranslationKey(mob.getTier()))),
                Component.empty()
                        .append(Component.translatable("info.woot_revived.rate").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                        .append(WootContainerScreen.formatInteger(mob.getSpawnTickRate()))
                        .append(Component.literal("t").setStyle(UNIT_STYLE)),
                Component.empty()
                        .append(Component.translatable("info.woot_revived.cost").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                        .append(WootContainerScreen.formatInteger(mob.getVitalityFuelCost()))
                        .append(Component.literal("mB").setStyle(UNIT_STYLE)),
                ModNameHelper.getModName(modId).setStyle(MOD_NAME_STYLE)
        );

        return lines.stream()
                .map(Component::getVisualOrderText)
                .map(ClientTooltipComponent::create)
                .toList();
    }

    @Override
    public void exportResources(ResourceExporter exporter) {
    }
}
