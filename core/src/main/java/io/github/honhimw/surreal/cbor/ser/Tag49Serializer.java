package io.github.honhimw.surreal.cbor.ser;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import io.github.honhimw.surreal.cbor.SurrealCustomTag;
import io.github.honhimw.surreal.model.Range;

import java.io.IOException;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag49Serializer extends CborSerializer<Range> {

    public static final Tag49Serializer INSTANCE = new Tag49Serializer();

    @Override
    public Class<Range> handledType() {
        return Range.class;
    }

    @Override
    public void serialize(Range v, CBORGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeTag(SurrealCustomTag.TAG_49.tag);
        gen.writeStartArray(null);
        serializeNested(v.lowerBound, gen, serializers);
        serializeNested(v.upperBound, gen, serializers);
        gen.writeEndArray();
    }

}
