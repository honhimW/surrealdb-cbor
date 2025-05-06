package io.github.honhimw.surreal.cbor.ser;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import io.github.honhimw.surreal.cbor.SurrealCustomTag;
import io.github.honhimw.surreal.model.Table;

import java.io.IOException;

/**
 * @author honhimW
 * @since 2025-04-30
 */

public class Tag7Serializer extends CborSerializer<Table> {

    public static final Tag7Serializer INSTANCE = new Tag7Serializer();

    @Override
    public Class<Table> handledType() {
        return Table.class;
    }

    @Override
    public void serialize(Table v, CBORGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeTag(SurrealCustomTag.TAG_7.tag);
        gen.writeString(v.name);
    }
}
