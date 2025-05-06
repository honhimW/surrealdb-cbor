package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.github.honhimw.surreal.cbor.Converter;
import io.github.honhimw.surreal.model.Geometry;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag88Converter implements Converter {

    public static final Tag88Converter INSTANCE = new Tag88Converter();

    @Override
    public Geometry.Point convert(JsonNode node) {
        ArrayNode arrayNode = node.require();
        double lon = arrayNode.at("/0").doubleValue();
        double lat = arrayNode.at("/1").doubleValue();
        return Geometry.point(lon, lat);
    }

}
