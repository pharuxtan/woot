package wootrevived.api;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.ApiStatus;
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
     * Initialize the per-upgrade persistent data container.
     * <p>
     * This method is called when the upgrade is installed into a factory.
     * Addons can use the provided {@code upgradeTag} to store any default keys
     * or values they need to persist while the upgrade remains installed.
     * <p>
     * The same {@code upgradeTag} instance is passed to later hooks so stored data
     * can be read or updated across lifecycle stages.
     * <p>
     * Note that the tag is <strong>reset each time the player removes and re-adds
     * the upgrade</strong> to the factory. It should therefore only be used
     * for runtime or per-installation state, not for long-term persistence.
     *
     * @param upgradeTag persistent data for this upgrade instance
     * @param registryAccess access to the current registry view
     */
    public void initUpgradeTag(CompoundTag upgradeTag, RegistryAccess registryAccess) {
    }

    /**
     * Allows upgrades to adjust factory generation properties
     * before any ingredients or vitality fuel are consumed.
     * <p>
     * @param properties mutable generation properties
     * @param upgradeTag persistent data for this upgrade across lifecycle stages
     */
    public void applyGenerationProperties(WootGenerationProperties properties, CompoundTag upgradeTag) {
    }

    /**
     * Allows upgrades to adjust the mob's spawn properties
     * before the simulation begins.
     * <p>
     * @param properties mutable spawn properties
     * @param upgradeTag persistent data for this upgrade across lifecycle stages
     */
    public void applySpawnProperties(WootSpawnProperties properties, CompoundTag upgradeTag){
    }

    /**
     * Allows upgrades to inspect and modify drops produced by the simulation.
     * <p>
     * @param properties mutable access to item/fluids/XP drops and context
     * @param upgradeTag persistent data for this upgrade across lifecycle stages
     */
    public void modifyDrops(WootDropsProperties properties, CompoundTag upgradeTag) {
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
    @OnlyIn(Dist.CLIENT)
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
