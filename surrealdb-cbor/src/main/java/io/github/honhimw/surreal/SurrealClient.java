package io.github.honhimw.surreal;

import io.github.honhimw.surreal.client.SurrealClientBuilder;
import io.github.honhimw.surreal.model.RecordId;
import io.github.honhimw.surreal.model.Response;
import jakarta.annotation.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-04-27
 */

public interface SurrealClient extends AutoCloseable {

    static SurrealClientBuilder builder() {
        return new SurrealClientBuilder();
    }

    ReactiveSurrealClient reactive();

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
    byte[] sqlBytes(String sql, @Nullable Map<String, Object> bindings);

    /**
     * query [ sql, vars ]
     *
     * @param sql      sql
     * @param bindings parameters
     * @return decoded response
     * @see #sqlBytes(String, Map)
     */
    Response sql(String sql, @Nullable Map<String, Object> bindings);

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
    default byte[] sqlBytes(String sql) {
        return sqlBytes(sql, Collections.emptyMap());
    }

    /**
     * query in raw
     *
     * @param sql sql
     * @return decoded response
     * @see #sqlBytes(String)
     */
    default Response sql(String sql) {
        return sql(sql, Collections.emptyMap());
    }

    /**
     * PING
     */
    void ping();

    /**
     * RELATE `in` -> `table` -> `out` content $data RETURN id;
     *
     * @param table table name
     * @param in    in
     * @param out   out
     * @param data  relation content
     * @return recordId
     */
    RecordId relate(String table, RecordId in, RecordId out, Object data);

    /**
     * RELATE `in` -> `table` -> `out` content {} RETURN id;
     *
     * @param table table name
     * @param in    in
     * @param out   out
     * @return recordId
     */
    default RecordId relate(String table, RecordId in, RecordId out) {
        return relate(table, in, out, new HashMap<>());
    }

    /*
    =============================================================================
    Typed Client
    =============================================================================
     */

    /**
     * Typed client for single table id in string kind.
     *
     * @param table table name
     * @param type  type of table
     * @param <T>   table type
     * @return String kind id Typed client
     */
    <T> TypedSurrealClient<T, String> string(String table, Class<T> type);

    /**
     * Typed client for single table id in i64 kind.
     *
     * @param table table name
     * @param type  type of table
     * @param <T>   table type
     * @return Long kind id Typed client
     */
    <T> TypedSurrealClient<T, Long> i64(String table, Class<T> type);

    /**
     * Typed client for single table id in UUID kind.
     *
     * @param table table name
     * @param type  type of table
     * @param <T>   table type
     * @return UUID kind id Typed client
     */
    <T> TypedSurrealClient<T, UUID> uuid(String table, Class<T> type);

    /**
     * Typed client for single table id in RecordId.
     *
     * @param table table name
     * @param type  type of table
     * @param <T>   table type
     * @return RecordId Typed client
     */
    <T> TypedSurrealClient<T, RecordId> record(String table, Class<T> type);

}
