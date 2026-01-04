package wootrevived.woot.events.client;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.jetbrains.annotations.NotNull;
import wootrevived.woot.Woot;
import wootrevived.woot.blocks.stygian_anvil.StygianAnvilBlockEntity;
import wootrevived.woot.client.render.factory.FactoryBlockEntityRenderState;
import wootrevived.woot.client.render.factory.FactoryBlockEntityRenderer;
import wootrevived.woot.client.render.mob_shard.MobShardProjectileRenderer;
import wootrevived.woot.client.render.stygian_anvil.StygianAnvilBlockEntityRenderState;
import wootrevived.woot.client.render.stygian_anvil.StygianAnvilBlockEntityRenderer;
import wootrevived.woot.registries.BlocksRegistry;
import wootrevived.woot.registries.ItemsRegistry;

@EventBusSubscriber(modid = Woot.MOD_ID, value = { Dist.CLIENT })
public class RegisterEntityRenderers {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ItemsRegistry.MOB_SHARD_PROJECTILE.get(), MobShardProjectileRenderer::new);

        event.registerBlockEntityRenderer(BlocksRegistry.STYGIAN_ANVIL_BLOCK_ENTITY.get(), new BlockEntityRendererProvider<StygianAnvilBlockEntity, StygianAnvilBlockEntityRenderState>() {
            @Override
            public BlockEntityRenderer<StygianAnvilBlockEntity, StygianAnvilBlockEntityRenderState> create(@NotNull Context context) {
                return new StygianAnvilBlockEntityRenderer(context);
            }
        });

        BlockEntityRendererProvider<BlockEntity, FactoryBlockEntityRenderState> factoryProvider = new BlockEntityRendererProvider<BlockEntity, FactoryBlockEntityRenderState>() {
            @Override
            public @NotNull BlockEntityRenderer<BlockEntity, FactoryBlockEntityRenderState> create(@NotNull Context context) {
                return new FactoryBlockEntityRenderer();
            }
        };

        event.registerBlockEntityRenderer(BlocksRegistry.HEART_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.FAKE_SPAWNER_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.COPPER_PYLON_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.IRON_PYLON_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.GOLD_PYLON_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.DIAMOND_PYLON_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.NETHERITE_PYLON_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.COPPER_PLINTH_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.IRON_PLINTH_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.GOLD_PLINTH_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.DIAMOND_PLINTH_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.NETHERITE_PLINTH_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.FACTORY_CONNECT_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.FACTORY_CTR_BASE_PRI_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.FACTORY_CTR_BASE_SEC_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.IMPORT_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.EXPORT_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.FACTORY_UPGRADE_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.COPPER_CELL_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.IRON_CELL_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.GOLD_CELL_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.DIAMOND_CELL_BLOCK_ENTITY.get(), factoryProvider);
        event.registerBlockEntityRenderer(BlocksRegistry.NETHERITE_CELL_BLOCK_ENTITY.get(), factoryProvider);
    }
}
