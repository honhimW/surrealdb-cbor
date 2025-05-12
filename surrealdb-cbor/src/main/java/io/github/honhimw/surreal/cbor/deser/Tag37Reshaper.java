package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BinaryNode;
import io.github.honhimw.surreal.cbor.Reshaper;
import io.github.honhimw.surreal.util.UUIDUtils;

import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag37Reshaper implements Reshaper {

    public static final Tag37Reshaper INSTANCE = new Tag37Reshaper();

    @Override
    public UUID reshape(JsonNode node) {
        BinaryNode binaryNode = node.require();
        byte[] bytes = binaryNode.binaryValue();
        return UUIDUtils.fromBytes(bytes);
    }
}
