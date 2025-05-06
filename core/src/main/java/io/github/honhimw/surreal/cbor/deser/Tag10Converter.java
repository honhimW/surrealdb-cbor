package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.honhimw.surreal.cbor.Converter;

import java.math.BigDecimal;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag10Converter implements Converter {

    public static final Tag10Converter INSTANCE = new Tag10Converter();

    @Override
    public BigDecimal convert(JsonNode node) {
        String text = node.asText();
        return new BigDecimal(text);
    }

}
