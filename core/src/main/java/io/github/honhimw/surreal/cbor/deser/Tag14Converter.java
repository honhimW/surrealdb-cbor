package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.github.honhimw.surreal.cbor.Converter;

import java.time.Duration;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag14Converter implements Converter {

    public static final Tag14Converter INSTANCE = new Tag14Converter();

    @Override
    public Duration convert(JsonNode node) {
        ArrayNode arrayNode = node.require();
        long seconds = arrayNode.at("/0").asLong(0);
        long nanoSeconds = arrayNode.at("/1").asLong(0);
        return Duration.ofSeconds(seconds, nanoSeconds);
    }
}
