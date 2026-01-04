package wootrevived.woot.blocks.creative_power;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.util.handlers.WootEnergyHandler;

public class CreativePowerBlockEntity extends BlockEntity implements BlockEntityTicker<BlockEntity> {
    public CreativePowerBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.CREATIVE_POWER_BLOCK_ENTITY.get(), pos, state);
    }

    public static void ticker(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity){
        if(blockEntity instanceof CreativePowerBlockEntity creativePowerBlockEntity){
            creativePowerBlockEntity.tick(level, pos, state, blockEntity);
        }
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (level.isClientSide())
            return;

        for (Direction facing : Direction.values()) {
            EnergyHandler storage = level.getCapability(Capabilities.Energy.BLOCK, getBlockPos().relative(facing), facing.getOpposite());
            if(storage == null)
                continue;

            try (Transaction tx = Transaction.openRoot()) {
                storage.insert(1000, tx);
                tx.commit();
            }
        }
    }

    private final EnergyHandler energyHandler = createEnergy();
    private EnergyHandler createEnergy() {
        EnergyHandler es = new WootEnergyHandler(Integer.MAX_VALUE, Integer.MAX_VALUE);

        try (Transaction tx = Transaction.openRoot()) {
            es.insert(Integer.MAX_VALUE, tx);
            tx.commit();
        }

        return es;
    }

    public static EnergyHandler getEnergyStorageCapability(CreativePowerBlockEntity blockEntity, Direction side){
        return blockEntity.energyHandler;
    }
}
