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

public class Tag91Serializer extends CborSerializer<Geometry.MultiPoint> {

    public static final Tag91Serializer INSTANCE = new Tag91Serializer();

    @Override
    public Class<Geometry.MultiPoint> handledType() {
        return Geometry.MultiPoint.class;
    }

    @Override
    public void serialize(Geometry.MultiPoint v, CBORGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeTag(SurrealCustomTag.TAG_91.tag);
        gen.writeStartArray(null);
        for (Geometry.Point point : v.points) {
            serializeNested(point, gen, serializers);
        }
        gen.writeEndArray();
    }

}
