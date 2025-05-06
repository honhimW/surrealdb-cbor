package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.github.honhimw.surreal.cbor.Converter;

import java.time.Instant;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag12Converter implements Converter {

    public static final Tag12Converter INSTANCE = new Tag12Converter();

    @Override
    public Instant convert(JsonNode node) {
        ArrayNode arrayNode = node.require();
        long seconds = arrayNode.at("/0").asLong(0);
        long nanoSeconds = arrayNode.at("/1").asLong(0);
        return Instant.ofEpochSecond(seconds, nanoSeconds);
    }
}
