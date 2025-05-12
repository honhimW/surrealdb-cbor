package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.honhimw.surreal.cbor.Reshaper;
import io.github.honhimw.surreal.model.Range;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag50Reshaper implements Reshaper {

    public static final Tag50Reshaper INSTANCE = new Tag50Reshaper();

    @Override
    public Range.Included reshape(JsonNode node) {
        int value = node.intValue();
        return new Range.Included(value);
    }

}
