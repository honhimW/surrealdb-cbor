package io.github.honhimw.surreal.cbor.ser;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import io.github.honhimw.surreal.cbor.SurrealCustomTag;
import io.github.honhimw.surreal.model.Option;

import java.io.IOException;

/**
 * @author honhimW
 * @since 2025-04-30
 */

public class Tag6Serializer extends CborSerializer<Option> {

    public static final Tag6Serializer INSTANCE = new Tag6Serializer();

    @Override
    public Class<Option> handledType() {
        return Option.class;
    }

    @Override
    public void serialize(Option v, CBORGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeTag(SurrealCustomTag.TAG_6.tag);
        gen.writeNull();
    }
}
