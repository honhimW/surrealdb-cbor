package io.github.honhimw.surreal.cbor.deser;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.honhimw.surreal.cbor.Reshaper;
import io.github.honhimw.surreal.cbor.SurrealCustomTag;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Converters {

    public static final Map<Integer, Reshaper> CUSTOM_RESHAPER = new ConcurrentHashMap<>();

    public static Object convert(int tag, JsonNode node) {
        if (!CUSTOM_RESHAPER.isEmpty()) {
            Reshaper reshaper = CUSTOM_RESHAPER.get(tag);
            if (reshaper != null) {
                return reshaper.reshape(node);
            }
        }
        switch (tag) {
            case 6:
                return Tag6Reshaper.INSTANCE.reshape(node);
            case 7:
                return Tag7Reshaper.INSTANCE.reshape(node);
            case 8:
                return Tag8Reshaper.INSTANCE.reshape(node);
            case 10:
                return Tag10Reshaper.INSTANCE.reshape(node);
            case 12:
                return Tag12Reshaper.INSTANCE.reshape(node);
            case 14:
                return Tag14Reshaper.INSTANCE.reshape(node);
            case 37:
                return Tag37Reshaper.INSTANCE.reshape(node);
            case 49:
                return Tag49Reshaper.INSTANCE.reshape(node);
            case 50:
                return Tag50Reshaper.INSTANCE.reshape(node);
            case 51:
                return Tag51Reshaper.INSTANCE.reshape(node);
            case 88:
                return Tag88Reshaper.INSTANCE.reshape(node);
            case 89:
                return Tag89Reshaper.INSTANCE.reshape(node);
            case 90:
                return Tag90Reshaper.INSTANCE.reshape(node);
            case 91:
                return Tag91Reshaper.INSTANCE.reshape(node);
            case 92:
                return Tag92Reshaper.INSTANCE.reshape(node);
            case 93:
                return Tag93Reshaper.INSTANCE.reshape(node);
            case 94:
                return Tag94Reshaper.INSTANCE.reshape(node);
        }
        return node;
    }

}
