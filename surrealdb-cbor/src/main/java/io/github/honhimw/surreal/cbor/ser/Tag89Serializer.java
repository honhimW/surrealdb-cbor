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

public class Tag89Serializer extends CborSerializer<Geometry.Line> {

    public static final Tag89Serializer INSTANCE = new Tag89Serializer();

    @Override
    public Class<Geometry.Line> handledType() {
        return Geometry.Line.class;
    }

    @Override
    public void serialize(Geometry.Line v, CBORGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeTag(SurrealCustomTag.TAG_89.tag);
        gen.writeStartArray(null);
        for (Geometry.Point point : v.points) {
            serializeNested(point, gen, serializers);
        }
        gen.writeEndArray();
    }

}
