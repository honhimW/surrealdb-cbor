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

public class Tag94Serializer extends CborSerializer<Geometry.Geometries> {

    public static final Tag94Serializer INSTANCE = new Tag94Serializer();

    @Override
    public Class<Geometry.Geometries> handledType() {
        return Geometry.Geometries.class;
    }

    @Override
    public void serialize(Geometry.Geometries v, CBORGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeTag(SurrealCustomTag.TAG_94.tag);
        gen.writeStartArray(null);
        for (Geometry.Geo geo : v.geometries) {
            serializeNested(geo, gen, serializers);
        }
        gen.writeEndArray();
    }

}
