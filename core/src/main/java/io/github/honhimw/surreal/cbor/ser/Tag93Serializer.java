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

public class Tag93Serializer extends CborSerializer<Geometry.MultiPolygon> {

    public static final Tag93Serializer INSTANCE = new Tag93Serializer();

    @Override
    public Class<Geometry.MultiPolygon> handledType() {
        return Geometry.MultiPolygon.class;
    }

    @Override
    public void serialize(Geometry.MultiPolygon v, CBORGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeTag(SurrealCustomTag.TAG_93.tag);
        gen.writeStartArray(null);
        for (Geometry.Polygon polygon : v.polygons) {
            serializeNested(polygon, gen, serializers);
        }
        gen.writeEndArray();
    }

}
