package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.POJONode;
import io.github.honhimw.surreal.cbor.Reshaper;
import io.github.honhimw.surreal.model.Range;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag49Reshaper implements Reshaper {

    public static final Tag49Reshaper INSTANCE = new Tag49Reshaper();

    @Override
    public Range reshape(JsonNode node) {
        ArrayNode arrayNode = node.require();
        POJONode lowerBound = arrayNode.at("/0").require();
        POJONode upperBound = arrayNode.at("/1").require();
        Range.Value lowerValue = (Range.Value) lowerBound.getPojo();
        Range.Value upperValue = (Range.Value) upperBound.getPojo();
        return new Range(lowerValue, upperValue);
    }
}
