package io.github.honhimw.surreal.cbor.ser;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import io.github.honhimw.surreal.cbor.SurrealCustomTag;
import io.github.honhimw.surreal.model.Id;
import io.github.honhimw.surreal.model.RecordId;

import java.io.IOException;

/**
 * @author honhimW
 * @since 2025-04-30
 */

public class Tag8Serializer extends CborSerializer<RecordId> {

    public static final Tag8Serializer INSTANCE = new Tag8Serializer();

    @Override
    public Class<RecordId> handledType() {
        return RecordId.class;
    }

    @Override
    public void serialize(RecordId v, CBORGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeTag(SurrealCustomTag.TAG_8.tag);
        gen.writeStartArray(null);
        gen.writeString(v.table.name);
        switch (v.id.kind) {
            case LONG:
                gen.writeNumber(v.id.longValue());
                break;
            case STRING:
                gen.writeString(v.id.stringValue());
                break;
            case UUID:
                serializeNested(v.id.uuidValue(), gen, serializers);
                break;
            default:
                throw new IllegalArgumentException("Unsupported id kind");
        }
        gen.writeEndArray();
    }
}
