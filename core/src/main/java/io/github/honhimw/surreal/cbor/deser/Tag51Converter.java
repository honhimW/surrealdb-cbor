package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.honhimw.surreal.cbor.Converter;
import io.github.honhimw.surreal.model.Range;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag51Converter implements Converter {

    public static final Tag51Converter INSTANCE = new Tag51Converter();

    @Override
    public Range.Excluded convert(JsonNode node) {
        int value = node.intValue();
        return new Range.Excluded(value);
    }

}
