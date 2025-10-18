package wootrevived.woot.mixins.accessors;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public interface LevelMixinAccessor {
    void woot$setDimension(ResourceKey<Level> dimension, ResourceKey<DimensionType> dimensionTypeId, Holder<DimensionType> dimensionTypeRegistration);
}
