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

public class Tag50Serializer extends CborSerializer<Range.Included> {

    public static final Tag50Serializer INSTANCE = new Tag50Serializer();

    @Override
    public Class<Range.Included> handledType() {
        return Range.Included.class;
    }

    @Override
    public void serialize(Range.Included v, CBORGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeTag(SurrealCustomTag.TAG_50.tag);
        gen.writeNumber(v.value);
    }

}
