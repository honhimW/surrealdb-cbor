package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.POJONode;
import io.github.honhimw.surreal.cbor.Converter;
import io.github.honhimw.surreal.model.RecordId;

import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag8Converter implements Converter {

    public static final Tag8Converter INSTANCE = new Tag8Converter();

    @Override
    public RecordId convert(JsonNode node) {
        String table = node.at("/0").asText();
        JsonNode id = node.at("/1");
        if (id.isLong()) {
            return RecordId.of(table, id.longValue());
        } else if (id.isTextual()) {
            return RecordId.of(table, id.textValue());
        } else if (id.isPojo()) {
            Object pojo = ((POJONode) id).getPojo();
            if (pojo instanceof UUID) {
                return RecordId.of(table, (UUID) pojo);
            }
        }
        throw new IllegalArgumentException("Unsupported id kind.");
    }
}
