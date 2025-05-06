package io.github.honhimw.surreal.cbor.ser;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import io.github.honhimw.surreal.cbor.SurrealCustomTag;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

/**
 * @author honhimW
 * @since 2025-04-30
 */

public class Tag12Serializer extends CborSerializer<Instant> {

    public static final Tag12Serializer INSTANCE = new Tag12Serializer();

    @Override
    public Class<Instant> handledType() {
        return Instant.class;
    }

    @Override
    public void serialize(Instant v, CBORGenerator gen, SerializerProvider serializers) throws IOException {
        long[] longs = {v.getEpochSecond(), v.getNano()};
        gen.writeTag(SurrealCustomTag.TAG_12.tag);
        gen.writeArray(longs, 0, 2);
    }

}
