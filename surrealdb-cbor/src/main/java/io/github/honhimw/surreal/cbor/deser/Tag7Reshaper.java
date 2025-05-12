package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.honhimw.surreal.cbor.Reshaper;
import io.github.honhimw.surreal.model.Table;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag7Reshaper implements Reshaper {

    public static final Tag7Reshaper INSTANCE = new Tag7Reshaper();

    @Override
    public Table reshape(JsonNode node) {
        return Table.of(node.asText());
    }

}
