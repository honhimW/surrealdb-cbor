package io.github.honhimw.surreal.cbor.ser;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import io.github.honhimw.surreal.cbor.SurrealCustomTag;
import io.github.honhimw.surreal.model.Geometry;

import java.io.IOException;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag90Serializer extends CborSerializer<Geometry.Polygon> {

    public static final Tag90Serializer INSTANCE = new Tag90Serializer();

    @Override
    public Class<Geometry.Polygon> handledType() {
        return Geometry.Polygon.class;
    }

    @Override
    public void serialize(Geometry.Polygon v, CBORGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeTag(SurrealCustomTag.TAG_90.tag);
        gen.writeStartArray(null);
        for (Geometry.Line line : v.lines) {
            serializeNested(line, gen, serializers);
        }
        gen.writeEndArray();
    }

}
