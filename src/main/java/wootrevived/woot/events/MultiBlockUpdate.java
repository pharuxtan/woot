package wootrevived.woot.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import wootrevived.woot.Woot;
import wootrevived.woot.multiblock.MultiBlockFactoryEntity;
import wootrevived.woot.multiblock.patterns.Patterns;

import java.util.List;

@EventBusSubscriber(modid = Woot.MOD_NAMESPACE)
public class MultiBlockUpdate {
    @SubscribeEvent
    public static void onEntityPlace(BlockEvent.EntityPlaceEvent event) {
        LevelAccessor accessor = event.getLevel();
        if(accessor instanceof ServerLevel level){
            BlockState state = event.getPlacedBlock();
            if(Patterns.getValidBlocks().contains(state.getBlock())) {
                updateMultiblocksPattern(level, List.of(event.getPos()));
            }
        }
    }

    @SubscribeEvent
    public static void onEntityBreak(BlockEvent.BreakEvent event) {
        LevelAccessor accessor = event.getLevel();
        if(accessor instanceof ServerLevel level){
            BlockState state = event.getState();
            if(Patterns.getValidBlocks().contains(state.getBlock())){
                level.getServer().execute(() -> updateMultiblocksPattern(level, List.of(event.getPos())));
            }
        }
    }

    @SubscribeEvent
    public static void onChunkSent(ChunkWatchEvent.Sent event){
        ServerLevel level = event.getLevel();
        for(BlockEntity be : event.getChunk().getBlockEntities().values()){
            if(be instanceof MultiBlockFactoryEntity entity)
                entity.updatePattern(level);
        }
    }

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event){
        if(event.getLevel() instanceof ServerLevel level){
            List<BlockPos> validBlocks = event.getAffectedBlocks().stream()
                    .filter(pos -> Patterns.getValidBlocks().contains(level.getBlockState(pos).getBlock()))
                    .toList();

            if(!validBlocks.isEmpty()){
                level.getServer().schedule(new TickTask(level.getServer().getTickCount() + 1, () -> updateMultiblocksPattern(level, validBlocks)));
            }
        }
    }

    private static void updateMultiblocksPattern(ServerLevel level, List<BlockPos> positions){
        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, minZ = Double.POSITIVE_INFINITY,
               maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;
        for(BlockPos pos : positions){
            AABB area = Patterns.getSearchAABB(pos);
            if(minX > area.minX) minX = area.minX;
            if(minY > area.minY) minY = area.minY;
            if(minZ > area.minZ) minZ = area.minZ;
            if(maxX < area.maxX) maxX = area.maxX;
            if(maxY < area.maxY) maxY = area.maxY;
            if(maxZ < area.maxZ) maxZ = area.maxZ;
        }

        int minSectionX = SectionPos.blockToSectionCoord(minX);
        int minSectionZ = SectionPos.blockToSectionCoord(minZ);
        int maxSectionX = SectionPos.blockToSectionCoord(maxX);
        int maxSectionZ = SectionPos.blockToSectionCoord(maxZ);

        for(int sectionX = minSectionX; sectionX <= maxSectionX; sectionX++){
            for(int sectionZ = minSectionZ; sectionZ <= maxSectionZ; sectionZ++){
                LevelChunk chunk = level.getChunk(sectionX, sectionZ);
                for(BlockPos entityPos : chunk.getBlockEntitiesPos()) {
                    if(minX <= entityPos.getX() && minY <= entityPos.getY() && minZ <= entityPos.getZ() &&
                            maxX >= entityPos.getX() && maxY >= entityPos.getY() && maxZ >= entityPos.getZ()) {
                        BlockEntity entity = chunk.getBlockEntity(entityPos);
                        if(entity instanceof MultiBlockFactoryEntity multiblock)
                            multiblock.updatePattern(level);
                    }
                }
            }
        }
    }
}
