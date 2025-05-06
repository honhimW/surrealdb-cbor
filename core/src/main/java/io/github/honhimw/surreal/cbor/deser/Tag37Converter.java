package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BinaryNode;
import io.github.honhimw.surreal.cbor.Converter;
import io.github.honhimw.surreal.util.UUIDUtils;

import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag37Converter implements Converter {

    public static final Tag37Converter INSTANCE = new Tag37Converter();

    @Override
    public UUID convert(JsonNode node) {
        BinaryNode binaryNode = node.require();
        byte[] bytes = binaryNode.binaryValue();
        return UUIDUtils.fromBytes(bytes);
    }
}
