package wootrevived.woot.mixins.impl;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import wootrevived.woot.mixins.accessors.LevelMixinAccessor;

@Mixin(Level.class)
public abstract class LevelMixin implements LevelMixinAccessor {
    @Shadow @Final @Mutable
    private ResourceKey<Level> dimension;

    @Shadow @Final @Mutable
    private ResourceKey<DimensionType> dimensionTypeId;

    @Shadow @Final @Mutable
    private Holder<DimensionType> dimensionTypeRegistration;

    public void woot$setDimension(ResourceKey<Level> dimension, ResourceKey<DimensionType> dimensionTypeId, Holder<DimensionType> dimensionTypeRegistration) {
        this.dimension = dimension;
        this.dimensionTypeId = dimensionTypeId;
        this.dimensionTypeRegistration = dimensionTypeRegistration;
    }
}
