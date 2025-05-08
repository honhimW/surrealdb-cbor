package io.github.honhimw.surreal;

import io.github.honhimw.surreal.client.SurrealClientBuilder;
import io.github.honhimw.surreal.model.RecordId;
import io.github.honhimw.surreal.model.Response;
import jakarta.annotation.Nullable;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-04-27
 */

public interface ReactiveSurrealClient extends AutoCloseable {

    static SurrealClientBuilder builder() {
        return new SurrealClientBuilder();
    }

    /**
     * query [ sql, vars ]
     * <pre>
     * {
     *     "id": 1,
     *     "method": "query",
     *     "params": [
     *         "CREATE person SET name = 'John'; SELECT * FROM type::table($tb);",
     *         {
     *             "tb": "person"
     *         }
     *     ]
     * }
     * </pre>
     *
     * @param sql      sql
     * @param bindings parameters
     * @return response content in cbor
     */
    Mono<byte[]> sqlBytes(String sql, @Nullable Map<String, Object> bindings);

    /**
     * query [ sql, vars ]
     *
     * @param sql      sql
     * @param bindings parameters
     * @return decoded response
     * @see #sqlBytes(String, Map)
     */
    Mono<Response> sql(String sql, @Nullable Map<String, Object> bindings);

    /**
     * query in raw
     * # <pre>
     * {
     *     "id": 1,
     *     "method": "query",
     *     "params": [
     *         "CREATE person SET name = 'John'; SELECT * FROM type::table('person');"
     *     ]
     * }
     * </pre>
     *
     * @param sql sql
     * @return response content in cbor
     * @see #sqlBytes(String, Map)
     */
    default Mono<byte[]> sqlBytes(String sql) {
        return sqlBytes(sql, Collections.emptyMap());
    }

    /**
     * query in raw
     *
     * @param sql sql
     * @return decoded response
     * @see #sqlBytes(String)
     */
    default Mono<Response> sql(String sql) {
        return sql(sql, Collections.emptyMap());
    }

    /*
    =============================================================================
    Typed Client
    =============================================================================
     */

    /**
     * Typed client for single table id in string kind.
     * @param table table name
     * @param type  type of table
     * @return String kind id Typed client
     * @param <T> table type
     */
    <T> ReactiveTypedSurreal<T, String> string(String table, Class<T> type);

    /**
     * Typed client for single table id in i64 kind.
     * @param table table name
     * @param type  type of table
     * @return Long kind id Typed client
     * @param <T> table type
     */
    <T> ReactiveTypedSurreal<T, Long> i64(String table, Class<T> type);

    /**
     * Typed client for single table id in UUID kind.
     * @param table table name
     * @param type  type of table
     * @return UUID kind id Typed client
     * @param <T> table type
     */
    <T> ReactiveTypedSurreal<T, UUID> uuid(String table, Class<T> type);

    /**
     * Typed client for single table id in RecordId.
     * @param table table name
     * @param type  type of table
     * @return RecordId Typed client
     * @param <T> table type
     */
    <T> ReactiveTypedSurreal<T, RecordId> record(Class<T> type);

}
