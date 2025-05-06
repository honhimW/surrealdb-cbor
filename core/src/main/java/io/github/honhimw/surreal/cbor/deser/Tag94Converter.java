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

public class Tag94Converter implements Converter {

    public static final Tag94Converter INSTANCE = new Tag94Converter();

    @Override
    public Geometry.Geometries convert(JsonNode node) {
        ArrayNode arrayNode = node.require();
        List<Geometry.Geo> geometries = new ArrayList<>(arrayNode.size());
        for (JsonNode jsonNode : arrayNode) {
            POJONode pojoNode = jsonNode.require();
            Geometry.Geo geo = (Geometry.Geo) pojoNode.getPojo();
            geometries.add(geo);
        }
        return new Geometry.Geometries(geometries);
    }

}
