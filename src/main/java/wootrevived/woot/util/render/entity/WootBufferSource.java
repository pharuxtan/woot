package wootrevived.woot.util.render.entity;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectSortedMaps;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

import java.util.SequencedMap;

public class WootBufferSource extends MultiBufferSource.BufferSource {
    protected WootBufferSource(ByteBufferBuilder sharedBuffer, SequencedMap<RenderType, ByteBufferBuilder> fixedBuffers) {
        super(sharedBuffer, fixedBuffers);
    }

    static WootBufferSource immediate(ByteBufferBuilder sharedBuffer) {
        return new WootBufferSource(sharedBuffer, Object2ObjectSortedMaps.emptyMap());
    }
}
