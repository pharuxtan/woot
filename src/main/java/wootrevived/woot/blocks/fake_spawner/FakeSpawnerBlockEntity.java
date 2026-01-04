package wootrevived.woot.blocks.fake_spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import wootrevived.api.WootFactoryMob;
import wootrevived.api.enums.Tier;
import wootrevived.woot.Woot;
import wootrevived.woot.data.FakeSpawnerData;
import wootrevived.woot.network.WootFakeSpawnerUpdate;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.ComponentsRegistry;
import wootrevived.woot.registries.WootFactoryMobsRegistry;
import wootrevived.woot.util.block.FactoryBlockBaseEntity;
import wootrevived.woot.util.common.RedstoneMode;
import wootrevived.woot.util.entity.WootTags;
import wootrevived.woot.util.handlers.WootFluidResourceHandler;
import wootrevived.woot.util.helper.SerializeEntityValueHelper;

import java.util.Optional;

public class FakeSpawnerBlockEntity extends FactoryBlockBaseEntity {
    public FakeSpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.FAKE_SPAWNER_BLOCK_ENTITY.get(), pos, state);
    }

    private CompoundTag mobTag = null;

    public @Nullable CompoundTag getMobTag() {
        return mobTag;
    }

    public @Nullable ValueInput getMobValue() {
        return mobTag == null ? null : TagValueInput.create(SerializeEntityValueHelper.REPORTER, level.registryAccess(), mobTag);
    }

    public @Nullable WootFactoryMob<?> getMob() {
        if(mobTag == null)
            return null;
        return WootFactoryMobsRegistry.getFactoryMob(mobTag);
    }

    public int index;

    private int vitalityCost = 0;
    private int totalDrained = 0;
    private double perTickRatio = 0;
    private double accumulator = 0;
    private int numOfSim = 0;

    public int getNumberOfSimulations(){
        return numOfSim;
    }

    public int getVitalityCost(){
        return vitalityCost;
    }

    public int getTotalDrained(){
        return totalDrained;
    }

    public float getETA(){
        return (float)((vitalityCost - totalDrained - accumulator) / (perTickRatio * 20));
    }

    public float getRate(){
        return (float)perTickRatio;
    }

    public boolean setActive(int rate, int cost, int numOfSim){
        if(isDisabled())
            return false;

        this.perTickRatio = ((double)cost) / ((double)rate);
        this.accumulator = 0;
        this.vitalityCost = cost;
        this.totalDrained = 0;
        this.numOfSim = numOfSim;
        setChanged();
        return true;
    }

    public boolean isActive(){
        return totalDrained < vitalityCost;
    }

    public boolean tick(WootFluidResourceHandler tank){
        if(getMob() == null || getMob().isBlacklisted() || !isActive())
            return false;

        if(redstoneMode != RedstoneMode.ONCE && isDisabled())
            return false;

        accumulator += perTickRatio;
        int drainAmount = (int) accumulator;
        accumulator -= drainAmount;

        if(drainAmount <= 0)
            return false;

        int drained;
        try (Transaction tx = Transaction.openRoot()){
            if(tank.getResource(0).isEmpty()){
                accumulator += (double)drainAmount - perTickRatio;
                setChanged();
                return false;
            }

            drained = tank.extract(tank.getResource(0), drainAmount, tx);

            if(drained < drainAmount){
                accumulator += (double)drainAmount - perTickRatio;
                setChanged();
                return false;
            }

            tx.commit();
        }

        totalDrained += drained;

        if(totalDrained >= vitalityCost){
            this.vitalityCost = 0;
            this.totalDrained = 0;
            setChanged();
            return true;
        }

        setChanged();
        return false;
    }

    public Tier getTier() {
        if (mobTag == null)
            return Tier.INVALID;

        if(!WootFactoryMobsRegistry.hasFactoryMob(mobTag))
            return Tier.INVALID;

        WootFactoryMob<?> mob = WootFactoryMobsRegistry.getFactoryMob(mobTag);

        return mob.getTier();
    }

    public static ItemStack getItemStack(CompoundTag tag) {
        ItemStack itemStack = BlocksRegistry.FAKE_SPAWNER_BLOCK.get().asItem().getDefaultInstance();

        itemStack.applyComponents(DataComponentPatch.builder().set(ComponentsRegistry.FAKE_SPAWNER_DATA.get(), new FakeSpawnerData.Component(
                Optional.ofNullable(tag)
        )).build());

        return itemStack;
    }

    protected RedstoneMode redstoneMode = RedstoneMode.ALWAYS_ON;

    public RedstoneMode getRedstoneMode() {
        return redstoneMode;
    }

    public void setRedstoneMode(RedstoneMode mode) {
        redstoneMode = mode;
    }

    private boolean lastRedstoneState = false;
    protected boolean isDisabled(){
        if(redstoneMode == RedstoneMode.ALWAYS_ON) return false;

        boolean current = level.hasNeighborSignal(getBlockPos());

        if(redstoneMode != RedstoneMode.ONCE)
            return (redstoneMode == RedstoneMode.WITH_SIGNAL) != current;

        boolean risingEdge = !lastRedstoneState && current;
        lastRedstoneState = current;
        return !risingEdge;
    }

    private FakeSpawnerData.Component getComponent(){
        return new FakeSpawnerData.Component(
                Optional.ofNullable(getMobTag())
        );
    }

    public void setComponent(FakeSpawnerData.Component component){
        mobTag = component.mobTag().orElse(null);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter getter){
        FakeSpawnerData.Component component = getter.get(ComponentsRegistry.FAKE_SPAWNER_DATA);
        if(component == null)
            return;

        setComponent(component);
        setChanged();
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder){
        builder.set(ComponentsRegistry.FAKE_SPAWNER_DATA, getComponent());
    }

    @Override
    protected void saveAdditional(ValueOutput output){
        super.saveAdditional(output);

        output.putInt(WootTags.REDSTONE_MODE_TAG, redstoneMode.ordinal());
        output.putInt(WootTags.Factory.NUMBER_OF_SIMULATIONS, numOfSim);
        output.putInt(WootTags.Factory.VITALITY_COST, vitalityCost);
        output.putInt(WootTags.Factory.TOTAL_DRAINED, totalDrained);
        output.putDouble(WootTags.Factory.PER_TICK_RATIO, perTickRatio);
        output.putDouble(WootTags.Factory.ACCUMULATOR, accumulator);

        output.store(FakeSpawnerData.ID, FakeSpawnerData.CODEC, getComponent());
    }

    @Override
    public void loadAdditional(ValueInput input){
        super.loadAdditional(input);

        input.getInt(WootTags.REDSTONE_MODE_TAG).ifPresent(mode -> redstoneMode = RedstoneMode.byIndex(mode));
        input.getInt(WootTags.Factory.NUMBER_OF_SIMULATIONS).ifPresent(num -> numOfSim = num);
        input.getInt(WootTags.Factory.VITALITY_COST).ifPresent(cost -> vitalityCost = cost);
        input.getInt(WootTags.Factory.TOTAL_DRAINED).ifPresent(drained -> totalDrained = drained);
        perTickRatio = input.getDoubleOr(WootTags.Factory.PER_TICK_RATIO, 0);
        accumulator = input.getDoubleOr(WootTags.Factory.ACCUMULATOR, 0);

        input.read(FakeSpawnerData.ID, FakeSpawnerData.CODEC).ifPresent(this::setComponent);
    }

    private static final ProblemReporter.ScopedCollector REPORTER = Woot.reporter("FakeSpawnerBlockEntity");

    @NonNull
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider){
        CompoundTag tag = super.getUpdateTag(provider);
        TagValueOutput output = TagValueOutput.createWithContext(REPORTER, provider);
        saveAdditional(output);
        tag.merge(output.buildResult());
        return tag;
    }

    public void sendNewState(){
        ClientPacketDistributor.sendToServer(new WootFakeSpawnerUpdate(getBlockPos(), redstoneMode));
    }

    public void handleNewState(WootFakeSpawnerUpdate update){
        if(update.redstoneMode() != null)
            redstoneMode = update.redstoneMode();

        setChanged();
    }

    public boolean canPlayerAccess(ServerPlayer player) {
        return !(player.distanceToSqr(getBlockPos().getX() + 0.5,
                getBlockPos().getY() + 0.5,
                getBlockPos().getZ() + 0.5) > 64);
    }
}
