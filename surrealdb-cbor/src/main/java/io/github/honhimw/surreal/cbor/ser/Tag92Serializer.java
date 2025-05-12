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

public class Tag92Serializer extends CborSerializer<Geometry.MultiLine> {

    public static final Tag92Serializer INSTANCE = new Tag92Serializer();

    @Override
    public Class<Geometry.MultiLine> handledType() {
        return Geometry.MultiLine.class;
    }

    @Override
    public void serialize(Geometry.MultiLine v, CBORGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeTag(SurrealCustomTag.TAG_92.tag);
        gen.writeStartArray(null);
        for (Geometry.Line line : v.lines) {
            serializeNested(line, gen, serializers);
        }
        gen.writeEndArray();
    }

}
