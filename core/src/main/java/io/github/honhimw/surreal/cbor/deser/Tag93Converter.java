package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.POJONode;
import io.github.honhimw.surreal.cbor.Converter;
import io.github.honhimw.surreal.model.Geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag93Converter implements Converter {

    public static final Tag93Converter INSTANCE = new Tag93Converter();

    @Override
    public Geometry.MultiPolygon convert(JsonNode node) {
        ArrayNode arrayNode = node.require();
        List<Geometry.Polygon> polygons = new ArrayList<>(arrayNode.size());
        for (JsonNode jsonNode : arrayNode) {
            POJONode pojoNode = jsonNode.require();
            Geometry.Polygon polygon = (Geometry.Polygon) pojoNode.getPojo();
            polygons.add(polygon);
        }
        return new Geometry.MultiPolygon(polygons);
    }

}
