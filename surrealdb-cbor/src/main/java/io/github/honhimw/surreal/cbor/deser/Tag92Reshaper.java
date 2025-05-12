package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.POJONode;
import io.github.honhimw.surreal.cbor.Reshaper;
import io.github.honhimw.surreal.model.Geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag92Reshaper implements Reshaper {

    public static final Tag92Reshaper INSTANCE = new Tag92Reshaper();

    @Override
    public Geometry.MultiLine reshape(JsonNode node) {
        ArrayNode arrayNode = node.require();
        List<Geometry.Line> lines = new ArrayList<>(arrayNode.size());
        for (JsonNode jsonNode : arrayNode) {
            POJONode pojoNode = jsonNode.require();
            Geometry.Line line = (Geometry.Line) pojoNode.getPojo();
            lines.add(line);
        }
        return new Geometry.MultiLine(lines);
    }

}
