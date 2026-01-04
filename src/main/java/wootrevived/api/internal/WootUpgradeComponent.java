package wootrevived.api.internal;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.codecs.PrimitiveCodec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;
import wootrevived.api.WootUpgradeItem;
import wootrevived.api.interfaces.WootUpgradeEnum;

/**
 * Internal data component storing the active variant of a {@link WootUpgradeItem}.
 * <p>
 * This component is the underlying mechanism used by Woot to attach variant
 * information to an item stack in Minecraft's data component system.
 * It pairs:
 * <ul>
 *     <li>the enum class implementing {@link WootUpgradeEnum}, and</li>
 *     <li>the currently selected enum constant (variant)</li>
 * </ul>
 * ensuring that the correct runtime variant can always be resolved—even when
 * multiple independent upgrade items define their own variant enums.
 *
 * <h2>Internal Use</h2>
 * This class is annotated with {@link ApiStatus.Internal}
 * and is <strong>not</strong> intended for direct use by external mods.
 * Plugin authors should interact only with:
 * <ul>
 *     <li>{@link WootUpgradeItem#getVariant},</li>
 *     <li>{@link WootUpgradeItem#setVariant},</li>
 * </ul>
 * which provide the supported public API for accessing upgrade variants.
 */
@ApiStatus.Internal
public record WootUpgradeComponent(Class<? extends WootUpgradeEnum<?>> clazz, WootUpgradeEnum<?> variant) {
    public static <T extends Enum<T> & WootUpgradeEnum<T>> WootUpgradeComponent of(T variant){
        return new WootUpgradeComponent(variant.getDeclaringClass(), variant);
    }

    public static final String ID = "upgrade_variant";

    public static DataComponentType<WootUpgradeComponent> type(){
        return TYPE;
    }

    @SuppressWarnings("unchecked")
    public WootUpgradeComponent {
        if (clazz == null || variant == null)
            throw new IllegalArgumentException("clazz and variant must not be null");

        Class<? extends WootUpgradeEnum<?>> actual = ((Enum<? extends WootUpgradeEnum<?>>)variant).getDeclaringClass();

        if (!clazz.equals(actual))
            throw new IllegalArgumentException("clazz (" + clazz.getName() + ") does not match variant declaring class (" + actual.getName() + ")");
    }

    @SuppressWarnings("unchecked")
    private static final PrimitiveCodec<Class<? extends WootUpgradeEnum<?>>> CLASS_CODEC = new PrimitiveCodec<>() {
        @Override
        public <T> DataResult<Class<? extends WootUpgradeEnum<?>>> read(DynamicOps<T> ops, T input) {
            DataResult<String> className = ops.getStringValue(input);
            switch (className) {
                case DataResult.Success<String> success:
                    try {
                        Class<?> raw = Class.forName(success.value());
                        Class<? extends WootUpgradeEnum<?>> clazz = (Class<? extends WootUpgradeEnum<?>>) raw.asSubclass(WootUpgradeEnum.class);
                        return DataResult.success(clazz);
                    } catch(Exception ex){
                        return DataResult.error(ex::toString);
                    }
                case DataResult.Error<String> error:
                    return DataResult.error(error::message);
            }
        }

        @Override
        public <T> T write(DynamicOps<T> ops, Class<? extends WootUpgradeEnum<?>> value) {
            return ops.createString(value.getName());
        }

        @Override
        public String toString() {
            return "WootUpgradeEnumClass";
        }
    };

    @SuppressWarnings("unchecked")
    private static final Codec<WootUpgradeComponent> CODEC = new Codec<>() {
        @Override
        public <T> DataResult<T> encode(WootUpgradeComponent input, DynamicOps<T> ops, T prefix) {
            DataResult<T> clazzEncoded = CLASS_CODEC.encodeStart(ops, input.clazz());
            switch (clazzEncoded) {
                case DataResult.Success<T> clazz:
                    WootUpgradeEnum<?> v = input.variant();
                    Codec<WootUpgradeEnum<?>> variantCodec = (Codec<WootUpgradeEnum<?>>) v.codec();

                    DataResult<T> variantEncoded = variantCodec.encodeStart(ops, v);
                    switch (variantEncoded) {
                        case DataResult.Success<T> variant:
                            RecordBuilder<T> builder = ops.mapBuilder();
                            builder.add(ops.createString("clazz"), clazz.value());
                            builder.add(ops.createString("variant"), variant.value());
                            return builder.build(prefix);
                        case DataResult.Error<T> error:
                            return DataResult.error(error::message);
                    }
                case DataResult.Error<T> error:
                    return DataResult.error(error::message);
            }
        }

        @Override
        public <T> DataResult<Pair<WootUpgradeComponent, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getMap(input).flatMap(map -> {
                T clazzNode = map.get(ops.createString("clazz"));
                T variantNode = map.get(ops.createString("variant"));

                if(clazzNode == null)
                    return DataResult.error(() -> "Missing field: clazz");

                if(variantNode == null)
                    return DataResult.error(() -> "Missing field: variant");

                DataResult<Class<? extends WootUpgradeEnum<?>>> clazzResult = CLASS_CODEC.read(ops, clazzNode);
                switch (clazzResult) {
                    case DataResult.Success<Class<? extends WootUpgradeEnum<?>>> clazzSuccess:
                        Class<? extends WootUpgradeEnum<?>> clazz = clazzSuccess.value();
                        Codec<? extends WootUpgradeEnum<?>> rawVariantCodec;
                        try {
                            rawVariantCodec = variantCodecFor(clazz);
                        } catch (Exception ex) {
                            return DataResult.error(ex::toString);
                        }

                        Codec<WootUpgradeEnum<?>> variantCodec = (Codec<WootUpgradeEnum<?>>) rawVariantCodec;
                        DataResult<Pair<WootUpgradeEnum<?>, T>> variantResult = variantCodec.decode(ops, variantNode);
                        switch (variantResult) {
                            case DataResult.Success<Pair<WootUpgradeEnum<?>, T>> variantSuccess:
                                WootUpgradeEnum<?> variant = variantSuccess.value().getFirst();
                                WootUpgradeComponent component = new WootUpgradeComponent(clazz, variant);
                                return DataResult.success(Pair.of(component, input));
                            case DataResult.Error<Pair<WootUpgradeEnum<?>, T>> error:
                                return DataResult.error(error::message);
                        }
                    case DataResult.Error<Class<? extends WootUpgradeEnum<?>>> error:
                        return DataResult.error(error::message);
                }
            });
        }
    };

    @SuppressWarnings("unchecked")
    private static final StreamCodec<RegistryFriendlyByteBuf, WootUpgradeComponent> STREAM_CODEC = new StreamCodec<>(){
        @Override
        public void encode(@NonNull RegistryFriendlyByteBuf buf, @NonNull WootUpgradeComponent comp) {
            buf.writeUtf(comp.clazz().getName());

            Codec<WootUpgradeEnum<?>> codec = (Codec<WootUpgradeEnum<?>>) comp.variant().codec();

            DataResult<Tag> variantEncoded = codec.encodeStart(NbtOps.INSTANCE, comp.variant());
            switch (variantEncoded) {
                case DataResult.Success<Tag> variant:
                    ByteBufCodecs.TAG.encode(buf, variant.value());
                    return;
                case DataResult.Error<Tag> error:
                    throw new IllegalStateException("Failed to encode upgrade variant: " + error.message());
            }
        }

        @Override
        public @NonNull WootUpgradeComponent decode(@NonNull RegistryFriendlyByteBuf buf) {
            String className = buf.readUtf();
            Class<? extends WootUpgradeEnum<?>> clazz;
            try {
                Class<?> raw = Class.forName(className);
                clazz = (Class<? extends WootUpgradeEnum<?>>) raw.asSubclass(WootUpgradeEnum.class);
            } catch (Exception ex) {
                throw new IllegalStateException("Invalid upgrade enum class: " + className, ex);
            }

            Codec<? extends WootUpgradeEnum<?>> rawCodec = variantCodecFor(clazz);
            Codec<WootUpgradeEnum<?>> variantCodec = (Codec<WootUpgradeEnum<?>>) rawCodec;
            var dynamic = ByteBufCodecs.TAG.decode(buf);
            DataResult<WootUpgradeEnum<?>> variantResult = variantCodec.parse(NbtOps.INSTANCE, dynamic);
            return switch (variantResult) {
                case DataResult.Success<WootUpgradeEnum<?>> variant -> new WootUpgradeComponent(clazz, variant.value());
                case DataResult.Error<WootUpgradeEnum<?>> error ->
                        throw new IllegalStateException("Failed to decode upgrade variant: " + error.message());
            };
        }
    };

    private static Codec<? extends WootUpgradeEnum<?>> variantCodecFor(Class<? extends WootUpgradeEnum<?>> clazz){
        if (!clazz.isEnum())
            throw new IllegalStateException("Not an enum: " + clazz);

        Object[] constants = clazz.getEnumConstants();

        if (constants == null || constants.length == 0)
            throw new IllegalStateException("Empty enum: " + clazz);

        WootUpgradeEnum<?> any = (WootUpgradeEnum<?>) constants[0];
        return any.codec();
    }

    @Override
    public int hashCode() {
        int result = clazz.getName().hashCode();
        result = 31 * result + variant.getSerializedName().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;

        if(!(obj instanceof WootUpgradeComponent c))
            return false;

        return clazz.getName().equals(c.clazz.getName()) && variant.getSerializedName().equals(c.variant.getSerializedName());
    }

    private static final DataComponentType<WootUpgradeComponent> TYPE = DataComponentType.<WootUpgradeComponent>builder()
            .persistent(CODEC)
            .networkSynchronized(STREAM_CODEC)
            .build();
}
