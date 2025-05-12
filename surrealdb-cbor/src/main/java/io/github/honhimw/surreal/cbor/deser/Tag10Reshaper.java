package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.honhimw.surreal.cbor.Reshaper;

import java.math.BigDecimal;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag10Reshaper implements Reshaper {

    public static final Tag10Reshaper INSTANCE = new Tag10Reshaper();

    @Override
    public BigDecimal reshape(JsonNode node) {
        String text = node.asText();
        return new BigDecimal(text);
    }

}
