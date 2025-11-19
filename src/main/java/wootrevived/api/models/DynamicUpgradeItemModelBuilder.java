package wootrevived.api.models;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import wootrevived.api.WootUpgradeItem;

/**
 * Data-generator helper for declaring that a {@link WootUpgradeItem} uses
 * Woot's variant-aware dynamic item model.
 * <p>
 * This builder does <strong>not</strong> define variants or upgrade behavior.
 * Instead, it attaches a special model loader to the generated JSON so that
 * Woot can render the item using per-variant sprites when appropriate.
 *
 * <h2>When to Use</h2>
 * Use this model builder only when the upgrade item:
 * <ul>
 *     <li>is registered as a <em>dynamic</em> upgrade item (one item with
 *     multiple runtime-changeable variants), and</li>
 *     <li>you want the item's appearance to reflect the current variant.</li>
 * </ul>
 *
 * Dynamic registration alone does <em>not</em> require custom rendering.
 * This builder is optional and only needed if the item's icon should visually
 * change based on its active variant.
 *
 * <h2>Usage in Data Generators</h2>
 * Attach this builder to a generated item model:
 *
 * <pre>{@code
 * getBuilder("my_dynamic_upgrade")
 *     .customLoader(DynamicUpgradeItemModelBuilder::begin)
 *     .end();
 * }</pre>
 *
 * This instructs Woot to use its variant-aware renderer for the item.
 *
 * <h2>What This Enables</h2>
 * Once this loader is attached, Woot's rendering pipeline:
 * <ul>
 *     <li>selects the correct sprite based on the active variant,</li>
 *     <li>applies {@link WootUpgradeItem#applyItemTexture} when building the icon,</li>
 *     <li>renders each variant with its own sprite on the item layer,</li>
 *     <li>avoids the need for separate JSON model files per variant.</li>
 * </ul>
 */
public class DynamicUpgradeItemModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {
    public static String ID = "upgrade_item";

    public static <T extends ModelBuilder<T>> DynamicUpgradeItemModelBuilder<T> begin(T parent, ExistingFileHelper existingFileHelper) {
        return new DynamicUpgradeItemModelBuilder<>(parent, existingFileHelper);
    }

    protected DynamicUpgradeItemModelBuilder(T parent, ExistingFileHelper existingFileHelper) {
        super(ResourceLocation.tryBuild("woot_revived", ID), parent, existingFileHelper, false);
    }
}
