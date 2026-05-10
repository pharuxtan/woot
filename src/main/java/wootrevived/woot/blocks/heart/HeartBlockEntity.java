package wootrevived.woot.blocks.heart;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.enums.Tier;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.interfaces.WootGenerationProperties;
import wootrevived.api.interfaces.WootSpawnProperties;
import wootrevived.woot.blocks.cell.CellBlockEntity;
import wootrevived.woot.blocks.factory_upgrade.FactoryUpgradeBlockEntity;
import wootrevived.woot.blocks.fake_spawner.FakeSpawnerBlockEntity;
import wootrevived.woot.blocks.ingredient_import.IngredientImportBlockEntity;
import wootrevived.woot.client.render.heart.HeartContainerMenu;
import wootrevived.woot.drops.simulator.DropSimulator;
import wootrevived.woot.multiblock.MultiBlockFactoryEntity;
import wootrevived.woot.multiblock.patterns.Pattern;
import wootrevived.woot.multiblock.patterns.Patterns;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.WootFactoryMobsRegistry;
import wootrevived.woot.util.factory.*;

import java.util.*;
import java.util.function.Consumer;

public class HeartBlockEntity extends MultiBlockFactoryEntity implements MenuProvider {
    private final List<BlockPos> fakeSpawnersPos = new ArrayList<>(4);
    private final List<BlockPos> upgradesPos = new ArrayList<>(4);
    private final BlockPos importPos;
    private final BlockPos exportPos;
    private final BlockPos cellPos;

    public HeartBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.HEART_BLOCK_ENTITY.get(), pos, state);

        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

        for(Pattern.PatternBlock block : Patterns.getFakeSpawnersBlocks(facing))
            fakeSpawnersPos.add(block.getLevelBlockPos(pos));

        for(Pattern.PatternBlock block : Patterns.getUpgradeBlocks(facing))
            upgradesPos.add(block.getLevelBlockPos(pos));

        importPos = Patterns.getImportBlock(facing).getLevelBlockPos(pos);
        exportPos = Patterns.getExportBlock(facing).getLevelBlockPos(pos);
        cellPos = Patterns.getCellBlock(facing).getLevelBlockPos(pos);
    }

    private FakeSpawnerBlockEntity primaryFakeSpawner;
    private final FakeSpawnerBlockEntity[] secondaryFakeSpawners = new FakeSpawnerBlockEntity[3];
    private final FactoryUpgradeBlockEntity[] upgrades = new FactoryUpgradeBlockEntity[4];
    private CellBlockEntity cell;
    private IngredientImportBlockEntity ingredientImport;

    public FakeSpawnerBlockEntity getPrimaryFakeSpawner() {
        return primaryFakeSpawner;
    }

    public FakeSpawnerBlockEntity getSecondaryFakeSpawner(int index) {
        return secondaryFakeSpawners[index];
    }

    public FactoryUpgradeBlockEntity getUpgrade(int index){
        return upgrades[index];
    }

    public CellBlockEntity getCell(){
        return cell;
    }

    public static void ticker(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity){
        if(blockEntity instanceof HeartBlockEntity heartBlockEntity){
            heartBlockEntity.tick(level, pos, state, blockEntity);
        }
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockEntity blockEntity) {
        if(!getBlockState().getValue(BlockStateProperties.ENABLED))
            return;

        if(tier == Tier.INVALID) {
            resetFactory();
            return;
        }

        primaryFakeSpawner = getBlockEntity(primaryFakeSpawner, fakeSpawnersPos.get(0), FakeSpawnerBlockEntity.class);

        if(primaryFakeSpawner == null) {
            resetFactory();
            return;
        }

        primaryFakeSpawner.index = 0;

        cell = getBlockEntity(cell, cellPos, CellBlockEntity.class);

        if(cell == null) {
            resetFactory();
            return;
        }

        ingredientImport = getBlockEntity(ingredientImport, importPos, IngredientImportBlockEntity.class);

        if(ingredientImport == null) {
            resetFactory();
            return;
        }

        if(tier != Tier.TIER_1){
            for(int i = 0; i < secondaryFakeSpawners.length; i++){
                secondaryFakeSpawners[i] = getBlockEntity(secondaryFakeSpawners[i], fakeSpawnersPos.get(1 + i), FakeSpawnerBlockEntity.class);
                if(secondaryFakeSpawners[i] != null) {
                    secondaryFakeSpawners[i].index = 1 + i;
                }
            }
        } else {
            Arrays.fill(secondaryFakeSpawners, null);
        }

        for(int i =  0; i < upgrades.length; i++){
            if(tier == Tier.TIER_1 && i != 0){
                upgrades[i] = null;
            } else {
                upgrades[i] = getBlockEntity(upgrades[i], upgradesPos.get(i), FactoryUpgradeBlockEntity.class);
            }
        }

        if(level.isClientSide)
            return;

        List<FakeSpawnerBlockEntity> fakeSpawners = new ArrayList<>(1);
        fakeSpawners.add(primaryFakeSpawner);

        for(FakeSpawnerBlockEntity fakeSpawner : secondaryFakeSpawners){
            if(fakeSpawner != null) fakeSpawners.add(fakeSpawner);
        }

        for(FakeSpawnerBlockEntity fakeSpawner : fakeSpawners){
            if(!isTierEntityValid(fakeSpawner))
                continue;

            WootFactoryMob<?> mob = fakeSpawner.getMob();
            CompoundTag mobTag = fakeSpawner.getMobTag();

            List<ItemStack> importItemStacks = mob.getImportItems(mobTag, level.registryAccess());
            ingredientImport.setImportItem(fakeSpawner.index, importItemStacks.size() > 36 ? importItemStacks.subList(0, 36) : importItemStacks);

            List<FluidStack> importFluidStacks = mob.getImportFluids(mobTag, level.registryAccess());
            ingredientImport.setImportFluid(fakeSpawner.index, importFluidStacks.size() > 8 ?  importFluidStacks.subList(0, 8) : importFluidStacks);

            ingredientImport.extractNeighbors();

            if(!fakeSpawner.isActive() && ingredientImport.isImportValid(fakeSpawner.index)){
                WootGenerationProperties properties = getWootGenerationProperties(mob, mobTag);
                if(fakeSpawner.setActive(properties.getSpawnRate(), properties.getVitalityFuelCost(), properties.getNumberOfSimulations())){
                    ingredientImport.consumeImports(fakeSpawner.index);
                }
            } else if(fakeSpawner.tick(cell.tankHandler)){
                GenerationResult result = generateDrops(fakeSpawner);

                for(Direction direction : Direction.values()){
                    if(direction == Direction.UP || direction == Direction.DOWN) continue;

                    BlockPos blockPos = exportPos.relative(direction);

                    BlockEntity entity = level.getBlockEntity(blockPos);
                    if(entity == null) continue;

                    entity.getCapability(ForgeCapabilities.ITEM_HANDLER, direction.getOpposite()).ifPresent(handler -> {
                        for(ItemStack stack : List.copyOf(result.items)){
                            ItemStack insert = ItemHandlerHelper.insertItem(handler, stack, false);
                            if(insert.isEmpty()){
                                result.items.remove(stack);
                            } else if(insert.getCount() != stack.getCount()) {
                                stack.setCount(insert.getCount());
                            }
                        }
                    });

                    entity.getCapability(ForgeCapabilities.FLUID_HANDLER, direction.getOpposite()).ifPresent(handler -> {
                        for(FluidStack stack : List.copyOf(result.fluids)){
                            int amount = handler.fill(stack, IFluidHandler.FluidAction.EXECUTE);
                            if(stack.getAmount() == amount){
                                result.fluids.remove(stack);
                            } else if(amount > 0){
                                stack.shrink(amount);
                            }
                        }
                    });
                }
            }
        }
    }

    private @NotNull WootGenerationProperties getWootGenerationProperties(WootFactoryMob<?> mob, CompoundTag mobTag) {
        WootGenerationProperties properties = new WootFactoryGenerationProperties(tier, mob, mobTag, (ServerLevel) level, getBlockPos(), mob.getSpawnTickRate(), mob.getVitalityFuelCost());
        for(FactoryUpgradeBlockEntity upgradeBlockEntity : upgrades){
            if(upgradeBlockEntity != null)
                upgradeBlockEntity.applyGenerationProperties(properties);
        }
        return properties;
    }

    protected boolean isTierEntityValid(FakeSpawnerBlockEntity fakeSpawner){
        if(fakeSpawner == null)
            return false;

        WootFactoryMob<?> mob = fakeSpawner.getMob();
        if(mob == null)
            return false;

        return tier.isMobTierValid(mob.getTier());
    }

    protected GenerationResult generateDrops(FakeSpawnerBlockEntity fakeSpawner){
        WootFactoryMob<?> mob = fakeSpawner.getMob();
        CompoundTag mobTag = fakeSpawner.getMobTag();

        Collection<? extends WootUpgradeItem<?>> upgradesList = Arrays.stream(upgrades)
                .filter(Objects::nonNull)
                .map(FactoryUpgradeBlockEntity::getUpgradeItem)
                .filter(Objects::nonNull)
                .toList();

        List<ItemStack> unconcatItems = new ArrayList<>();
        List<FluidStack> unconcatFluids = new ArrayList<>();

        for (int i = 0; i < fakeSpawner.getNumberOfSimulations(); i++) {
            WootSpawnProperties spawnProperties = new WootFactorySpawnProperties(tier, mob, mobTag, (ServerLevel) level, getBlockPos(), upgradesList);

            for(FactoryUpgradeBlockEntity upgradeBlockEntity : upgrades){
                if(upgradeBlockEntity != null)
                    upgradeBlockEntity.applySpawnProperties(spawnProperties);
            }

            LivingEntity entity = DropSimulator.loadEntity(mob, spawnProperties.getFactoryMobTag());

            WootDropsProperties generationProperties = new WootFactoryDropsProperties(spawnProperties, entity);

            DropSimulator.patchDimension(generationProperties, false);

            if(!mob.isSimulationDisabled())
                DropSimulator.simulateDrops(generationProperties);

            mob.modifyDrops(WootFactoryMob.Phase.BEFORE_DROP_CALLBACKS, generationProperties);

            if(WootFactoryMobsRegistry.hasDropsModifier(mob.getEntityType())){
                for(Consumer<WootDropsProperties> callback : WootFactoryMobsRegistry.getDropsModifier(mob.getEntityType())){
                    callback.accept(generationProperties);
                }
            }

            for(Consumer<WootDropsProperties> callback : WootFactoryMobsRegistry.getGlobalDropsModifier()){
                callback.accept(generationProperties);
            }

            mob.modifyDrops(WootFactoryMob.Phase.AFTER_DROP_CALLBACKS, generationProperties);

            for(FactoryUpgradeBlockEntity upgradeBlockEntity : upgrades){
                if(upgradeBlockEntity != null)
                    upgradeBlockEntity.modifyDrops(generationProperties);
            }

            mob.modifyDrops(WootFactoryMob.Phase.AFTER_UPGRADES, generationProperties);

            DropSimulator.patchDimension(generationProperties, true);

            unconcatItems.addAll(generationProperties.getItemDrops());
            unconcatFluids.addAll(generationProperties.getFluidDrops());
        }

        return new GenerationResult(
                WootConcatItemStack.merge(unconcatItems),
                WootConcatFluidStack.merge(unconcatFluids)
        );
    }

    public static class GenerationResult {
        public List<ItemStack> items;
        public List<FluidStack> fluids;

        public GenerationResult(List<ItemStack> items, List<FluidStack> fluids){
            this.items = items;
            this.fluids = fluids;
        }
    }

    protected void resetFactory(){
        primaryFakeSpawner = null;
        Arrays.fill(secondaryFakeSpawners, null);
        Arrays.fill(upgrades, null);
        ingredientImport = null;
        cell = null;
    }

    protected @Nullable <T extends BlockEntity> T getBlockEntity(T oldEntity, BlockPos pos, Class<T> clazz) {
        if(oldEntity != null && !oldEntity.isRemoved())
            return oldEntity;

        BlockEntity be = level.getBlockEntity(pos);

        if(!clazz.isInstance(be))
            return null;

        return clazz.cast(be);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("gui.woot_revived.heart.name");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new HeartContainerMenu(containerId, level, getBlockPos(), playerInventory, player);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
    }
}
