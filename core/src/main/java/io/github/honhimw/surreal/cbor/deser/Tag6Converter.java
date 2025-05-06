package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.honhimw.surreal.cbor.Converter;
import io.github.honhimw.surreal.model.Option;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag6Converter implements Converter {

    public static final Tag6Converter INSTANCE = new Tag6Converter();

    @Override
    public Option convert(JsonNode node) {
        return Option.NONE;
    }

}
