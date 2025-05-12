package io.github.honhimw.surreal.cbor.ser;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import io.github.honhimw.surreal.cbor.SurrealCustomTag;

import java.io.IOException;
import java.time.Duration;

/**
 * @author honhimW
 * @since 2025-04-30
 */

public class Tag14Serializer extends CborSerializer<Duration> {

    public static final Tag14Serializer INSTANCE = new Tag14Serializer();

    @Override
    public Class<Duration> handledType() {
        return Duration.class;
    }

    @Override
    public void serialize(Duration v, CBORGenerator gen, SerializerProvider serializers) throws IOException {
        long[] longs = {v.getSeconds(), v.getNano()};
        gen.writeTag(SurrealCustomTag.TAG_14.tag);
        gen.writeArray(longs, 0, 2);
    }

}
