package wootrevived.api;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import wootrevived.api.interfaces.WootDropsProperties;
import wootrevived.api.interfaces.WootGenerationProperties;
import wootrevived.api.interfaces.WootSpawnProperties;

/**
 * Base class for Woot upgrade items.
 * <p>
 * Subclass this to influence factory behavior at three stages:
 * <ul>
 *   <li>Generation: before ingredients and vitality fuel are consumed</li>
 *   <li>Spawn: before the mob is simulated</li>
 *   <li>Drops: after simulation, when drops can be inspected/modified</li>
 * </ul>
 * The concrete Woot implementation invokes these hooks; integration mods
 * typically override one or more methods to adjust behavior.
 */
public abstract class WootUpgradeItem extends Item {
    /**
     * The current level of this {@link WootUpgradeItem upgrade item}.
     */
    protected final int level;

    public WootUpgradeItem(Properties properties, int level) {
        super(properties);
        this.level = level;
    }

    /**
     * Initializes the persistent component container for this upgrade item.
     * <p>
     * Called when the upgrade is first used by a factory. Implementations may
     * write default component values into {@code dataComponentHolder}.
     * <p>
     * The backing storage is attached to the item, so values written here
     * are preserved while the item exists, including when it is removed from
     * and reinserted into a factory or moved between inventories.
     *
     * @param dataComponentHolder container for persistent per-upgrade data on this item
     * @param level the world level containing the upgrade block
     * @param pos the position of the upgrade block using this upgrade
     */
    public void initDataComponents(MutableDataComponentHolder dataComponentHolder, Level level, BlockPos pos) {
    }

    /**
     * Clears or adjusts the persistent component data before the item is
     * removed from a factory. Implementations may remove component keys or
     * reset values stored on the item's data components.
     * <p>
     * This hook is called immediately before the upgrade item leaves the
     * upgrade block, allowing cleanup of per-installation state while
     * preserving any long‑term item data as needed.
     *
     * @param dataComponentHolder container for persistent per-upgrade data on this item
     * @param level the world level containing the upgrade block
     * @param pos the position of the upgrade block using this upgrade
     */
    public void deinitDataComponents(MutableDataComponentHolder dataComponentHolder, Level level, BlockPos pos) {
    }

    /**
     * Modifies the factory's generation-phase configuration.
     * <p>
     * Invoked before the factory consumes any ingredients or vitality fuel.
     * Implementations may mutate {@code properties} to influence cost,
     * throughput, mob selection, or other generation parameters.
     *
     * @param properties mutable generation configuration
     * @param dataComponentHolder persistent upgrade data
     */
    public void applyGenerationProperties(WootGenerationProperties properties, MutableDataComponentHolder dataComponentHolder) {
    }

    /**
     * Modifies spawn-phase configuration before the mob simulation is made.
     * <p>
     * Implementations may adjust mob attributes, environmental conditions,
     * simulation flags, or other runtime spawn parameters.
     *
     * @param properties mutable spawn configuration
     * @param dataComponentHolder persistent upgrade data
     */
    public void applySpawnProperties(WootSpawnProperties properties, MutableDataComponentHolder dataComponentHolder){
    }

    /**
     * Inspects or mutates outputs produced by the completed simulation.
     * <p>
     * Implementations may change item drops, fluid amounts, experience values,
     * or contextual metadata available via {@code properties}.
     *
     * @param properties mutable access to post-simulation drop data
     * @param dataComponentHolder persistent upgrade data
     */
    public void modifyDrops(WootDropsProperties properties, MutableDataComponentHolder dataComponentHolder) {
    }

    /**
     * Returns the upgrade level of this item.
     *
     * @return the level value
     */
    public int getLevel(){
        return this.level;
    }

    /**
     * Returns the resource location for this item's texture.
     *
     * @return the texture {@link ResourceLocation}
     */
    public ResourceLocation getTextureLocation(){
        return BuiltInRegistries.ITEM.getKey(this).withPrefix("textures/item/").withSuffix(".png");
    }

    /**
     * Called on the client when the factory block's side texture is generated on the atlas.
     * Draw your upgrade icon onto {@code upgradeSide} using pixels from {@code upgradeItem}.
     *
     * @param upgradeSide the target factory side {@link NativeImage}
     * @param upgradeItem the source upgrade item {@link NativeImage}
     */
    public void applyUpgradeTexture(NativeImage upgradeSide, NativeImage upgradeItem){
        for(int y = 2; y < 14; y++){
            for(int x = 2; x < 14; x++){
                int color = upgradeItem.getPixel(x, y);
                int alpha = color >> 24;
                if(alpha != 0)
                    upgradeSide.setPixel(x, y, color);
            }
        }
    }
}
