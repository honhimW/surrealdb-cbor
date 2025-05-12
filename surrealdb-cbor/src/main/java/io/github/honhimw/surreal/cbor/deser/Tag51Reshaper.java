package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.honhimw.surreal.cbor.Reshaper;
import io.github.honhimw.surreal.model.Range;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag51Reshaper implements Reshaper {

    public static final Tag51Reshaper INSTANCE = new Tag51Reshaper();

    @Override
    public Range.Excluded reshape(JsonNode node) {
        int value = node.intValue();
        return new Range.Excluded(value);
    }

}
