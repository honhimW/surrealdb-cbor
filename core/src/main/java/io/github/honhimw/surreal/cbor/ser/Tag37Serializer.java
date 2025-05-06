package io.github.honhimw.surreal.cbor.ser;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import io.github.honhimw.surreal.cbor.SurrealCustomTag;
import io.github.honhimw.surreal.util.UUIDUtils;

import java.io.IOException;
import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-04-30
 */

public class Tag37Serializer extends CborSerializer<UUID> {

    public static final Tag37Serializer INSTANCE = new Tag37Serializer();

    @Override
    public Class<UUID> handledType() {
        return UUID.class;
    }

    @Override
    public void serialize(UUID v, CBORGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeTag(SurrealCustomTag.TAG_37.tag);
        gen.writeBinary(UUIDUtils.toBytes(v));
    }

}
