package wootrevived.api;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
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
     * write default component values into {@code itemTag}.
     * <p>
     * The backing storage is attached to the item, so values written here
     * are preserved while the item exists, including when it is removed from
     * and reinserted into a factory or moved between inventories.
     *
     * @param itemTag container for persistent per-upgrade data on this item
     * @param level the world level containing the upgrade block
     * @param pos the position of the upgrade block using this upgrade
     */
    public void initItemTag(CompoundTag itemTag, Level level, BlockPos pos) {
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
     * @param itemTag container for persistent per-upgrade data on this item
     * @param level the world level containing the upgrade block
     * @param pos the position of the upgrade block using this upgrade
     */
    public void deinitItemTag(CompoundTag itemTag, Level level, BlockPos pos) {
    }

    /**
     * Modifies the factory's generation-phase configuration.
     * <p>
     * Invoked before the factory consumes any ingredients or vitality fuel.
     * Implementations may mutate {@code properties} to influence cost,
     * throughput, mob selection, or other generation parameters.
     *
     * @param properties mutable generation properties
     * @param itemTag persistent upgrade data
     */
    public void applyGenerationProperties(WootGenerationProperties properties, CompoundTag itemTag) {
    }

    /**
     * Modifies spawn-phase configuration before the mob simulation is made.
     * <p>
     * Implementations may adjust mob attributes, environmental conditions,
     * simulation flags, or other runtime spawn parameters.
     *
     * @param properties mutable spawn properties
     * @param itemTag persistent upgrade data
     */
    public void applySpawnProperties(WootSpawnProperties properties, CompoundTag itemTag) {
    }

    /**
     * Inspects or mutates outputs produced by the completed simulation.
     * <p>
     * Implementations may change item drops, fluid amounts, experience values,
     * or contextual metadata available via {@code properties}.
     *
     * @param properties mutable access to post-simulation drop data
     * @param itemTag persistent upgrade data
     */
    public void modifyDrops(WootDropsProperties properties, CompoundTag itemTag) {
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
        return ForgeRegistries.ITEMS.getKey(this).withPrefix("textures/item/").withSuffix(".png");
    }

    /**
     * Called on the client when the factory block's side texture is generated on the atlas.
     * Draw your upgrade icon onto {@code upgradeSide} using pixels from {@code upgradeItem}.
     *
     * @param upgradeSide the target factory side {@link NativeImage}
     * @param upgradeItem the source upgrade item {@link NativeImage}
     */
    @OnlyIn(Dist.CLIENT)
    public void applyUpgradeTexture(NativeImage upgradeSide, NativeImage upgradeItem){
        for(int y = 2; y < 14; y++){
            for(int x = 2; x < 14; x++){
                int color = upgradeItem.getPixelRGBA(x, y);
                int alpha = color >> 24;
                if(alpha != 0)
                    upgradeSide.setPixelRGBA(x, y, color);
            }
        }
    }
}
