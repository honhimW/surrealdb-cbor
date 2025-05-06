package io.github.honhimw.surreal.cbor;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author honhimW
 * @since 2025-04-30
 */

public interface Converter {

    default Object convert(JsonNode node) {
        return node;
    }

}
