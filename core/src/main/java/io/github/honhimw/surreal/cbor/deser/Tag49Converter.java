package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.POJONode;
import io.github.honhimw.surreal.cbor.Converter;
import io.github.honhimw.surreal.model.Range;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag49Converter implements Converter {

    public static final Tag49Converter INSTANCE = new Tag49Converter();

    @Override
    public Range convert(JsonNode node) {
        ArrayNode arrayNode = node.require();
        POJONode lowerBound = arrayNode.at("/0").require();
        POJONode upperBound = arrayNode.at("/1").require();
        Range.Value lowerValue = (Range.Value) lowerBound.getPojo();
        Range.Value upperValue = (Range.Value) upperBound.getPojo();
        return new Range(lowerValue, upperValue);
    }
}
