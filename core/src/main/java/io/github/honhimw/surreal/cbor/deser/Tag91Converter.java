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

public class Tag91Converter implements Converter {

    public static final Tag91Converter INSTANCE = new Tag91Converter();

    @Override
    public Geometry.MultiPoint convert(JsonNode node) {
        ArrayNode arrayNode = node.require();
        List<Geometry.Point> points = new ArrayList<>(arrayNode.size());
        for (JsonNode jsonNode : arrayNode) {
            POJONode pojoNode = jsonNode.require();
            Geometry.Point point = (Geometry.Point) pojoNode.getPojo();
            points.add(point);
        }
        return new Geometry.MultiPoint(points);
    }

}
