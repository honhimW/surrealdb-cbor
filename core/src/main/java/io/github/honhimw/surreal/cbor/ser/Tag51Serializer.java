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

public class Tag51Serializer extends CborSerializer<Range.Excluded> {

    public static final Tag51Serializer INSTANCE = new Tag51Serializer();

    @Override
    public Class<Range.Excluded> handledType() {
        return Range.Excluded.class;
    }

    @Override
    public void serialize(Range.Excluded v, CBORGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeTag(SurrealCustomTag.TAG_51.tag);
        gen.writeNumber(v.value);
    }

}
