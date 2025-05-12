package io.github.honhimw.surreal.cbor;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author honhimW
 * @since 2025-04-30
 */

public interface Reshaper {

    default Object reshape(JsonNode node) {
        return node;
    }

}
