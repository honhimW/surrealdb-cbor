package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.honhimw.surreal.cbor.Reshaper;
import io.github.honhimw.surreal.model.Option;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Tag6Reshaper implements Reshaper {

    public static final Tag6Reshaper INSTANCE = new Tag6Reshaper();

    @Override
    public Option reshape(JsonNode node) {
        return Option.NONE;
    }

}
