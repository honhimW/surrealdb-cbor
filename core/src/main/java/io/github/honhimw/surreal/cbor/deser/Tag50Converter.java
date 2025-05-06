package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.honhimw.surreal.cbor.Converter;
import io.github.honhimw.surreal.model.Range;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag50Converter implements Converter {

    public static final Tag50Converter INSTANCE = new Tag50Converter();

    @Override
    public Range.Included convert(JsonNode node) {
        int value = node.intValue();
        return new Range.Included(value);
    }

}
