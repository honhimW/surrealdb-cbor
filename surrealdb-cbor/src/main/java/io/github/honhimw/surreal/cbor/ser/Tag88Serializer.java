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

public class Tag88Serializer extends CborSerializer<Geometry.Point> {

    public static final Tag88Serializer INSTANCE = new Tag88Serializer();

    @Override
    public Class<Geometry.Point> handledType() {
        return Geometry.Point.class;
    }

    @Override
    public void serialize(Geometry.Point v, CBORGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeTag(SurrealCustomTag.TAG_88.tag);
        double[] point = {v.lon, v.lat};
        gen.writeArray(point, 0, 2);
    }

}
