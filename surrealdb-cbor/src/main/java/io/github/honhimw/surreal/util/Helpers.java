package io.github.honhimw.surreal.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author honhimW
 * @since 2025-05-07
 */

public class Helpers {

    public static void state(boolean state, String message) {
        if (!state) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * Block Mono and return non-null result.
     *
     * @param mono publisher
     * @param <R>  result type
     * @return blocking result
     */
    @Nonnull
    public static <R> R blockNonNull(@Nonnull Mono<R> mono) {
        R result = mono.block();
        if (Objects.isNull(result)) {
            throw new NoSuchElementException("blocking with an empty result.");
        }
        return result;
    }

    /**
     * Block Flux and return non-null result.
     *
     * @param flux publisher
     * @param <R>  result type
     * @return blocking result
     */
    @Nonnull
    public static <R> List<R> blockNonNull(@Nonnull Flux<R> flux) {
        List<R> result = flux.collectList().block();
        Objects.requireNonNull(result, "Flux#collectList will never be null.");
        return result;
    }

    public static boolean isEmpty(@Nullable Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof Collection) {
            return isEmpty((Collection<?>) o);
        } else if (o instanceof Map) {
            return isEmpty((Map<?, ?>) o);
        } else if (o instanceof CharSequence) {
            return isBlank((CharSequence) o);
        } else if (o.getClass().isArray()) {
            return Array.getLength(o) == 0;
        } else {
            throw new IllegalArgumentException("Unsupported type: " + o.getClass().getName());
        }
    }

    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen = cs == null ? 0 : cs.length();
        if (strLen == 0) {
            return true;
        } else {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean exists(JsonNode node) {
        return !node.isNull() && !node.isMissingNode();
    }

    public static void removeNullEntries(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objectNode = node.require();
            objectNode.forEachEntry((s, jsonNode) -> removeNullEntries(jsonNode));
            objectNode.removeNulls();
        }
    }

}
