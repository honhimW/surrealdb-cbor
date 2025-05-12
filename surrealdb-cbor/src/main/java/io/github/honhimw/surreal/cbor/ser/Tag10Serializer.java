package io.github.honhimw.surreal.cbor.ser;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import io.github.honhimw.surreal.cbor.SurrealCustomTag;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author honhimW
 * @since 2025-04-30
 */

public class Tag10Serializer extends CborSerializer<BigDecimal> {

    public static final Tag10Serializer INSTANCE = new Tag10Serializer();

    @Override
    public Class<BigDecimal> handledType() {
        return BigDecimal.class;
    }

    @Override
    public void serialize(BigDecimal v, CBORGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeTag(SurrealCustomTag.TAG_10.tag);
        gen.writeString(v.toPlainString());
    }
}
