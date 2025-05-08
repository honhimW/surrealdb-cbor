package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.honhimw.surreal.cbor.Converter;
import io.github.honhimw.surreal.model.Table;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag7Converter implements Converter {

    public static final Tag7Converter INSTANCE = new Tag7Converter();

    @Override
    public Table convert(JsonNode node) {
        return Table.of(node.asText());
    }

}
