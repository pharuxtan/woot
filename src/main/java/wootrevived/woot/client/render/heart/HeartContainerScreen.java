package wootrevived.woot.client.render.heart;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;
import wootrevived.woot.Woot;
import wootrevived.woot.blocks.fake_spawner.FakeSpawnerBlockEntity;
import wootrevived.woot.registries.FluidsRegistry;
import wootrevived.woot.registries.WootFactoryMobsRegistry;
import wootrevived.woot.util.common.RedstoneMode;
import wootrevived.woot.util.common.WootTier;
import wootrevived.woot.util.helper.ModNameHelper;
import wootrevived.woot.util.render.WootButton;
import wootrevived.woot.util.render.WootContainerScreen;
import wootrevived.woot.util.render.WootSlot;
import wootrevived.woot.util.render.WootSlotItemHandler;
import wootrevived.woot.util.render.buttons.WootHeartInputButton;
import wootrevived.woot.util.render.buttons.WootRedstoneButton;
import wootrevived.woot.util.render.WootEntityRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static wootrevived.woot.util.render.WootStyles.*;

public class HeartContainerScreen extends AbstractContainerScreen<HeartContainerMenu> {
    public static final ResourceLocation GUI = Woot.location("textures/gui/atlas.png");

    public static final int GUI_XSIZE = 176;
    public static final int GUI_YSIZE = 184;

    public static final int CELL_TANK_X = GUI_XSIZE - 28;
    public static final int CELL_TANK_Y = 57;

    public static final int PRIMARY_MOB_X = 8;
    public static final int PRIMARY_MOB_Y = 17;

    // Left secondary fake spawner
    public static final int SECONDARY_MOB_0_X = 130;
    public static final int SECONDARY_MOB_0_Y = 17;

    // Center secondary fake spawner
    public static final int SECONDARY_MOB_1_X = 90;
    public static final int SECONDARY_MOB_1_Y = 17;

    // Right secondary fake spawner
    public static final int SECONDARY_MOB_2_X = 50;
    public static final int SECONDARY_MOB_2_Y = 17;

    private static final double BOX_SIZE = 32D;
    private static final double BOX_PADDING = 3D;

    private static final float MAX_ENTITY_BOX_SIZE = 20F;

    public static final int UPGRADE_SLOT_0_X = 10;
    public static final int UPGRADE_SLOT_0_Y = 77;

    public static final int UPGRADE_SLOT_1_X = 28;
    public static final int UPGRADE_SLOT_1_Y = 77;

    public static final int UPGRADE_SLOT_2_X = 46;
    public static final int UPGRADE_SLOT_2_Y = 77;

    public static final int UPGRADE_SLOT_3_X = 64;
    public static final int UPGRADE_SLOT_3_Y = 77;

    public static final int UPGRADE_SLOT_TEXT_X = 10;
    public static final int UPGRADE_SLOT_TEXT_Y = 67;

    public HeartContainerScreen(HeartContainerMenu container, Inventory playerInventory, Component name) {
        super(container, playerInventory, name);
        imageWidth = GUI_XSIZE;
        imageHeight = GUI_YSIZE;
    }

    protected void renderMenuBackground(GuiGraphics gui) {
        renderEntityBoxBg(gui, PRIMARY_MOB_X, PRIMARY_MOB_Y);
        renderEntityBoxBg(gui, SECONDARY_MOB_0_X, SECONDARY_MOB_0_Y);
        renderEntityBoxBg(gui, SECONDARY_MOB_1_X, SECONDARY_MOB_1_Y);
        renderEntityBoxBg(gui, SECONDARY_MOB_2_X, SECONDARY_MOB_2_Y);
        renderFluidBg(gui, CELL_TANK_X, CELL_TANK_Y);
        WootContainerScreen.renderVanillaSlot(gui, UPGRADE_SLOT_0_X, UPGRADE_SLOT_0_Y);
        WootContainerScreen.renderVanillaSlot(gui, UPGRADE_SLOT_1_X, UPGRADE_SLOT_1_Y);
        WootContainerScreen.renderVanillaSlot(gui, UPGRADE_SLOT_2_X, UPGRADE_SLOT_2_Y);
        WootContainerScreen.renderVanillaSlot(gui, UPGRADE_SLOT_3_X, UPGRADE_SLOT_3_Y);
        gui.drawString(font, "Upgrades:", UPGRADE_SLOT_TEXT_X, UPGRADE_SLOT_TEXT_Y, 0xFF404040, false);
    }

    protected void renderState(GuiGraphics gui) {
        renderEntityBox(gui, PRIMARY_MOB_X, PRIMARY_MOB_Y, getLivingEntity(HeartContainerMenu.PRIMARY_FAKE_SPAWNER));
        renderEntityBox(gui, SECONDARY_MOB_0_X, SECONDARY_MOB_0_Y, getLivingEntity(HeartContainerMenu.SECONDARY_FAKE_SPAWNER_0));
        renderEntityBox(gui, SECONDARY_MOB_1_X, SECONDARY_MOB_1_Y, getLivingEntity(HeartContainerMenu.SECONDARY_FAKE_SPAWNER_1));
        renderEntityBox(gui, SECONDARY_MOB_2_X, SECONDARY_MOB_2_Y, getLivingEntity(HeartContainerMenu.SECONDARY_FAKE_SPAWNER_2));
        renderFluid(gui, CELL_TANK_X, CELL_TANK_Y, menu.getCellFluid(), menu.getCellFluidCapacity());
        menu.updateUpgrades();
        updateButton(HeartContainerMenu.PRIMARY_FAKE_SPAWNER);
        updateButton(HeartContainerMenu.SECONDARY_FAKE_SPAWNER_0);
        updateButton(HeartContainerMenu.SECONDARY_FAKE_SPAWNER_1);
        updateButton(HeartContainerMenu.SECONDARY_FAKE_SPAWNER_2);
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY) {
        renderEntityTooltip(gui, mouseX, mouseY, PRIMARY_MOB_X, PRIMARY_MOB_Y, HeartContainerMenu.PRIMARY_FAKE_SPAWNER);
        renderEntityTooltip(gui, mouseX, mouseY, SECONDARY_MOB_0_X, SECONDARY_MOB_0_Y, HeartContainerMenu.SECONDARY_FAKE_SPAWNER_0);
        renderEntityTooltip(gui, mouseX, mouseY, SECONDARY_MOB_1_X, SECONDARY_MOB_1_Y, HeartContainerMenu.SECONDARY_FAKE_SPAWNER_1);
        renderEntityTooltip(gui, mouseX, mouseY, SECONDARY_MOB_2_X, SECONDARY_MOB_2_Y, HeartContainerMenu.SECONDARY_FAKE_SPAWNER_2);
        if(activeButton == -1)
            renderFluidTooltip(gui, mouseX, mouseY, CELL_TANK_X, CELL_TANK_Y, menu.getCellFluid(), menu.getCellFluidCapacity());
        else {
            List<FluidStack> stacks = menu.getFactoryMob(activeButton).getImportFluids(menu.getFactoryMobValue(activeButton));
            for(int i = 0; i < 8; i++){
                renderSmallFluidTooltip(gui, mouseX, mouseY, 9 + i * 20, 73, i >= stacks.size() ? FluidStack.EMPTY : stacks.get(i));
            }
        }
    }

    private final List<WootHeartInputButton> buttons = new ArrayList<>();
    public void addRenderableWidget(WootHeartInputButton button){
        buttons.add(button);
        addWidget(button);
    }

    /* Imports */

    protected void renderFakeSpawner(GuiGraphics gui) {
        /* Background */
        gui.fill(4, 56, 172, 159, 0xFFC6C6C6);
        gui.blit(RenderPipelines.GUI_TEXTURED, GUI, 7, 105, 7, 101, 162, 54, WootContainerScreen.ATLAS_WIDTH, WootContainerScreen.ATLAS_HEIGHT);
        for(int i = 0; i < 8; i++){
            gui.blit(RenderPipelines.GUI_TEXTURED, GUI, 9 + i * 20, 73, 58, 185, 18, 19, WootContainerScreen.ATLAS_WIDTH, WootContainerScreen.ATLAS_HEIGHT);
        }
        gui.drawString(font, "Needed Fluid Imports", 9, 63, 0xFF404040, false);
        gui.drawString(font, "Needed Item Imports", 7, 95, 0xFF404040, false);

        /* State */
        WootFactoryMob<?> mob = menu.getFactoryMob(activeButton);
        ValueInput input = menu.getFactoryMobValue(activeButton);
        menu.updateImports(mob.getImportItems(input));

        List<FluidStack> stacks = mob.getImportFluids(input);
        for(int i = 0; i < 8; i++){
            renderSmallFluid(gui, 9 + i * 20, 73, i >= stacks.size() ? FluidStack.EMPTY : stacks.get(i));
        }
    }

    /* Buttons */

    private int activeButton = -1;

    private WootRedstoneButton redstoneButton;

    @Override
    protected void init(){
        super.init();

        buttons.clear();

        addRenderableWidget(redstoneButton = new WootRedstoneButton(leftPos + 152, topPos + 57, RedstoneMode.ALWAYS_ON, button -> {
            RedstoneMode mode = button.nextMode();
            FakeSpawnerBlockEntity entity = menu.getFakeSpawner(buttons.get(activeButton).getFakeSpawnerIndex());
            entity.setRedstoneMode(mode);
            entity.sendNewState();
        }));

        redstoneButton.active = false;

        createButton(PRIMARY_MOB_X, PRIMARY_MOB_Y, HeartContainerMenu.PRIMARY_FAKE_SPAWNER);
        createButton(SECONDARY_MOB_0_X, SECONDARY_MOB_0_Y, HeartContainerMenu.SECONDARY_FAKE_SPAWNER_0);
        createButton(SECONDARY_MOB_1_X, SECONDARY_MOB_1_Y, HeartContainerMenu.SECONDARY_FAKE_SPAWNER_1);
        createButton(SECONDARY_MOB_2_X, SECONDARY_MOB_2_Y, HeartContainerMenu.SECONDARY_FAKE_SPAWNER_2);

        if(activeButton != -1)
            buttons.get(activeButton).isViewActive = true;
    }

    protected void createButton(int x, int y, int fakeSpawnerIndex){
        addRenderableWidget(new WootHeartInputButton(fakeSpawnerIndex, leftPos + x, topPos + y, this::setButtonActive));
    }

    public void setButtonActive(WootHeartInputButton button){
        if(activeButton == button.index){
            activeButton = -1;
            button.isViewActive = false;
            redstoneButton.active = false;
        } else {
            if(activeButton != -1)
                buttons.get(activeButton).isViewActive = false;

            activeButton = button.index;
            button.isViewActive = true;
            redstoneButton.active = true;
            redstoneButton.setMode(menu.getFakeSpawner(buttons.get(activeButton).getFakeSpawnerIndex()).getRedstoneMode());
        }
    }

    protected void updateButton(int fakeSpawnerIndex){
        WootFactoryMob<?> mob = menu.getFactoryMob(fakeSpawnerIndex);
        buttons.get(fakeSpawnerIndex).active = mob != null && isTierEntityValid(mob);
    }

    /* UTILS */

    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks){
        for(Slot slot : menu.slots){
            if(slot instanceof WootSlot wootSlot){
                wootSlot.setActive(activeButton == -1);
            } else if(slot instanceof WootSlotItemHandler wootSlotItemHandler){
                if(wootSlotItemHandler.getType() == WootSlotItemHandler.Type.INVENTORY){
                    wootSlotItemHandler.setActive(activeButton == -1);
                } else {
                    wootSlotItemHandler.setActive(activeButton != -1);
                }
            }
        }

        super.render(gui, mouseX, mouseY, partialTicks);
        super.renderTooltip(gui, mouseX, mouseY);
        renderTooltip(gui, mouseX, mouseY);

        for(WootButton button : buttons){
            button.render(gui, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics gui, float partialTicks, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        Matrix3x2fStack pose = gui.pose();
        pose.pushMatrix();
        pose.translate(x, y);
        gui.blit(RenderPipelines.GUI_TEXTURED, GUI, 0, 0, 0, 0, imageWidth, imageHeight, WootContainerScreen.ATLAS_WIDTH, WootContainerScreen.ATLAS_HEIGHT);

        renderMenuBackground(gui);
        renderState(gui);

        if(activeButton != -1 && menu.getFactoryMob(activeButton) == null){
            buttons.get(activeButton).isViewActive = false;
            activeButton = -1;
        }

        if(activeButton != -1)
            renderFakeSpawner(gui);

        pose.popMatrix();
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics gui, int mouseX, int mouseY){
        int titleX = 1 + (imageWidth - font.width(title)) / 2;
        gui.drawString(font, title, titleX, 6, 0xFF404040, false);
    }

    protected void renderFluidBg(@NotNull GuiGraphics gui, int x, int y){
        gui.blit(RenderPipelines.GUI_TEXTURED, GUI, x, y, 39, 185, 18, 41, WootContainerScreen.ATLAS_WIDTH, WootContainerScreen.ATLAS_HEIGHT);
    }

    protected void renderFluid(@NotNull GuiGraphics gui, int x, int y, FluidStack fluid, int capacity){
        if(fluid == null || fluid.isEmpty())
            return;

        int fillHeight = Mth.clamp(fluid.getAmount() * 35 / capacity, 0, 35);
        int fillY = 35 - fillHeight;

        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluid.getFluid().getFluidType());
        TextureAtlasSprite texture = Minecraft.getInstance().getTextureAtlas(Sheets.BLOCKS_MAPPER.sheet()).apply(fluidTypeExtensions.getStillTexture());
        WootContainerScreen.renderTiledFluidTextureAtlas(gui, texture, x + 3, y + fillY + 3, 12, fillHeight, fluidTypeExtensions.getTintColor(), false);
        gui.blit(RenderPipelines.GUI_TEXTURED, GUI, x + 3, y + 3, 42, 188, 12, 35, WootContainerScreen.ATLAS_WIDTH, WootContainerScreen.ATLAS_HEIGHT);
    }

    protected void renderFluidTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY, int x, int y, FluidStack fluid, int capacity){
        if(isHovering(x, y, 18, 41, mouseX, mouseY)){
            List<Component> tooltip;
            if(capacity == 0){
                tooltip = List.of(
                        Component.translatable("gui.woot_revived.heart.vitality_cell_missing").setStyle(MACHINE_STYLE)
                );
            } else {
                tooltip = List.of(
                        Component.translatable("gui.woot_revived.heart.vitality_cell")
                                .append(Component.literal(": ")).setStyle(MACHINE_STYLE),
                        Component.empty()
                                .append(Component.translatable("info.woot_revived.amount").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                                .append(WootContainerScreen.formatInteger(fluid.getAmount()))
                                .append(Component.literal("/").setStyle(MACHINE_STYLE))
                                .append(WootContainerScreen.formatInteger(capacity))
                                .append(Component.literal("mB").setStyle(UNIT_STYLE))
                );
            }
            gui.setTooltipForNextFrame(font, tooltip, Optional.empty(), mouseX, mouseY);
        }
    }

    protected void renderSmallFluid(@NotNull GuiGraphics gui, int x, int y, FluidStack fluid){
        if(fluid == null || fluid.isEmpty())
            return;

        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluid.getFluid().getFluidType());
        TextureAtlasSprite texture = Minecraft.getInstance().getTextureAtlas(Sheets.BLOCKS_MAPPER.sheet()).apply(fluidTypeExtensions.getStillTexture());
        WootContainerScreen.renderTiledFluidTextureAtlas(gui, texture, x + 3, y + 3, 12, 13, fluidTypeExtensions.getTintColor(), false);
        gui.blit(RenderPipelines.GUI_TEXTURED, GUI, x + 3, y + 3, 61, 188, 12, 13, WootContainerScreen.ATLAS_WIDTH, WootContainerScreen.ATLAS_HEIGHT);
    }

    protected void renderSmallFluidTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY, int x, int y, FluidStack fluid){
        if(isHovering(x + 1, y + 1, 16, 17, mouseX, mouseY)){
            List<Component> tooltip = new ArrayList<>(List.of(
                    Component.empty()
                            .append(Component.translatable("info.woot_revived.fluid").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                            .append(fluid != null && !fluid.isEmpty() ? fluid.getHoverName() : Component.translatable("info.woot_revived.empty")),
                    Component.empty()
                            .append(Component.translatable("info.woot_revived.amount").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                            .append(WootContainerScreen.formatInteger(fluid.getAmount()))
                            .append(Component.literal("mB").setStyle(UNIT_STYLE))
            ));

            if(!fluid.isEmpty()){
                String modId = BuiltInRegistries.FLUID.getKey(fluid.getFluid()).getNamespace();
                tooltip.add(ModNameHelper.getModName(modId).setStyle(MOD_NAME_STYLE));
            }

            gui.setTooltipForNextFrame(font, tooltip, Optional.empty(), mouseX, mouseY);
        }
    }

    protected @Nullable LivingEntity getLivingEntity(int fakeSpawnerIndex) {
        ValueInput input = menu.getFactoryMobValue(fakeSpawnerIndex);

        if(input == null)
            return null;

        Entity entity = EntityType.loadEntityRecursive(input, menu.getLevel(), EntitySpawnReason.SPAWNER, e -> e);

        if(!(entity instanceof LivingEntity livingEntity))
            return null;

        return livingEntity;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean isTierEntityValid(@NotNull LivingEntity entity){
        Tier tier = menu.getFactoryTier();
        WootFactoryMob<?> mob = WootFactoryMobsRegistry.getFactoryMob(entity.getEncodeId());

        return tier.isMobTierValid(mob.getTier());
    }

    protected boolean isTierEntityValid(@NotNull WootFactoryMob<?> mob){
        Tier tier = menu.getFactoryTier();

        return tier.isMobTierValid(mob.getTier());
    }

    protected void renderEntityBoxBg(@NotNull GuiGraphics gui, int x, int y){
        gui.blit(RenderPipelines.GUI_TEXTURED, GUI, x, y, 0, 185, 38, 38, WootContainerScreen.ATLAS_WIDTH, WootContainerScreen.ATLAS_HEIGHT);
    }

    protected void renderEntityBox(@NotNull GuiGraphics gui, int x, int y, @Nullable LivingEntity entity){
        if(entity == null || !isTierEntityValid(entity)){
            Component invalid = Component.translatable("info.woot_revived.factory.invalid");
            Component empty = Component.translatable("info.woot_revived.factory.empty");

            Tier tier = menu.getFactoryTier();

            float scale = 0.65F;

            Matrix3x2fStack pose = gui.pose();
            pose.pushMatrix();
            pose.scale(scale, scale);

            float drawY = ((int)BOX_SIZE - font.lineHeight * scale) / 2;
            if(tier == Tier.INVALID || tier == Tier.TIER_1 || (entity != null && !isTierEntityValid(entity))){
                float drawX = ((int)BOX_SIZE - font.width(invalid) * scale) / 2;
                pose.translate((x + drawX + 3.25F) / scale, (y + drawY + 3.25F) / scale);

                gui.drawString(font, invalid, 0, 0, 0xFF404040, false);
            } else {
                float drawX = ((int)BOX_SIZE - font.width(empty) * scale) / 2;
                pose.translate((x + drawX + 3.25F) / scale, (y + drawY + 3.25F) / scale);

                gui.drawString(font, empty, 0, 0, 0xFF404040, false);
            }

            pose.popMatrix();
            return;
        }

        WootEntityRenderer.render(gui, x, y, entity, BOX_SIZE, BOX_PADDING, MAX_ENTITY_BOX_SIZE);

        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(FluidsRegistry.VITALITY_FUEL_FLUID_TYPE.get());
        TextureAtlasSprite texture = Minecraft.getInstance().getTextureAtlas(Sheets.BLOCKS_MAPPER.sheet()).apply(fluidTypeExtensions.getStillTexture());
        WootContainerScreen.renderTiledFluidTextureAtlas(gui, texture, x + (int)BOX_PADDING, y + (int)BOX_PADDING, (int)BOX_SIZE, (int)BOX_SIZE, fluidTypeExtensions.getTintColor(), true);
    }

    protected void renderEntityTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY, int x, int y, int fakeSpawnerIndex){
        if(isHovering(x + 1, y + 1, 36, 36, mouseX, mouseY)){
            WootFactoryMob<?> mob = menu.getFactoryMob(fakeSpawnerIndex);
            @Nullable ValueInput input = menu.getFactoryMobValue(fakeSpawnerIndex);
            List<Component> tooltip;
            Tier tier = menu.getFactoryTier();
            if(mob == null || !isTierEntityValid(mob)){
                if(tier == Tier.INVALID){
                    tooltip = List.of(Component.translatable("gui.woot_revived.heart.no_tier"));
                } else if(tier == Tier.TIER_1 && mob == null){
                    tooltip = List.of(Component.translatable("gui.woot_revived.heart.insufficient_tier", Component.translatable(WootTier.getTranslationKey(Tier.TIER_2))));
                } else if(mob == null) {
                    tooltip = List.of(Component.translatable("gui.woot_revived.heart.no_secondary"));
                } else {
                    tooltip = List.of(Component.translatable("gui.woot_revived.heart.insufficient_tier", Component.translatable(WootTier.getTranslationKey(mob.getTier()))));
                }
            } else {
                tooltip = new ArrayList<>();
                tooltip.add(mob.getDisplayName(input).append(Component.literal(": ")).setStyle(MACHINE_STYLE));
                tooltip.add(
                        Component.empty()
                                .append(Component.translatable("info.woot_revived.tier").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                                .append(Component.translatable(WootTier.getTranslationKey(mob.getTier())))
                );
                tooltip.add(
                        Component.empty()
                                .append(Component.translatable("info.woot_revived.rate").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                                .append(WootContainerScreen.formatInteger(mob.getSpawnTickRate()))
                                .append(Component.literal("t").setStyle(UNIT_STYLE))
                );
                tooltip.add(
                        Component.empty()
                                .append(Component.translatable("info.woot_revived.cost").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                                .append(WootContainerScreen.formatInteger(mob.getVitalityFuelCost()))
                                .append(Component.literal("mB").setStyle(UNIT_STYLE))
                );

                String modId = BuiltInRegistries.ENTITY_TYPE.getKey(mob.getEntityType()).getNamespace();
                tooltip.add(ModNameHelper.getModName(modId).setStyle(MOD_NAME_STYLE));

                if(menu.getFactoryIsActive(fakeSpawnerIndex)){
                    tooltip.add(Component.empty());

                    tooltip.add(Component.empty()
                            .append(Component.translatable("info.woot_revived.drained").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                            .append(WootContainerScreen.formatInteger(menu.getFactoryVitalityDrained(fakeSpawnerIndex)))
                            .append(Component.literal("/").setStyle(MACHINE_STYLE))
                            .append(WootContainerScreen.formatInteger(menu.getFactoryVitalityAmount(fakeSpawnerIndex)))
                            .append(Component.literal("mB").setStyle(UNIT_STYLE))
                    );

                    tooltip.add(Component.empty()
                            .append(Component.translatable("info.woot_revived.eta").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                            .append(String.format("%.1f", menu.getFactoryETA(fakeSpawnerIndex)))
                            .append(Component.literal("s").setStyle(UNIT_STYLE))
                    );

                    tooltip.add(Component.empty()
                            .append(Component.translatable("info.woot_revived.usage").append(Component.literal(": ")).setStyle(MACHINE_STYLE))
                            .append(WootContainerScreen.formatFloat(menu.getFactoryRate(fakeSpawnerIndex)))
                            .append(Component.literal(" mB").setStyle(UNIT_STYLE))
                            .append(Component.literal("/").setStyle(MACHINE_STYLE))
                            .append(Component.literal("t").setStyle(UNIT_STYLE))
                    );
                }
            }
            gui.setTooltipForNextFrame(font, tooltip, Optional.empty(), mouseX, mouseY);
        }
    }
}
