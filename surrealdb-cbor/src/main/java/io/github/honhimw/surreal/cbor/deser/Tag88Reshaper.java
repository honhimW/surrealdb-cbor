package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.github.honhimw.surreal.cbor.Reshaper;
import io.github.honhimw.surreal.model.Geometry;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag88Reshaper implements Reshaper {

    public static final Tag88Reshaper INSTANCE = new Tag88Reshaper();

    @Override
    public Geometry.Point reshape(JsonNode node) {
        ArrayNode arrayNode = node.require();
        double lon = arrayNode.at("/0").doubleValue();
        double lat = arrayNode.at("/1").doubleValue();
        return Geometry.point(lon, lat);
    }

}
