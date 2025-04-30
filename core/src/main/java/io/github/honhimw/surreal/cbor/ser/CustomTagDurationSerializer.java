package io.github.honhimw.surreal.cbor.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import com.fasterxml.jackson.datatype.jsr310.ser.DurationSerializer;
import io.github.honhimw.surreal.cbor.SurrealCustomTag;

import java.io.IOException;
import java.time.Duration;

/**
 * @author honhimW
 * @since 2025-04-30
 */

public class CustomTagDurationSerializer extends DurationSerializer {

    public static final CustomTagDurationSerializer INSTANCE = new CustomTagDurationSerializer();

    @Override
    public void serialize(Duration duration, JsonGenerator generator, SerializerProvider provider) throws IOException {
        if (generator instanceof CBORGenerator) {
            long[] longs = {duration.getSeconds(), duration.getNano()};
            ((CBORGenerator) generator).writeTag(SurrealCustomTag.TAG_14.tag);
            generator.writeArray(longs, 0, 2);
        } else {
            super.serialize(duration, generator, provider);
        }
    }
}
