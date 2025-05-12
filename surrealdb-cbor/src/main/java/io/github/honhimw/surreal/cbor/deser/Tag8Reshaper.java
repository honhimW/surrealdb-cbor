package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.POJONode;
import io.github.honhimw.surreal.cbor.Reshaper;
import io.github.honhimw.surreal.model.RecordId;

import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag8Reshaper implements Reshaper {

    public static final Tag8Reshaper INSTANCE = new Tag8Reshaper();

    @Override
    public RecordId reshape(JsonNode node) {
        String table = node.at("/0").asText();
        JsonNode id = node.at("/1");
        if (id.isNumber()) {
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
