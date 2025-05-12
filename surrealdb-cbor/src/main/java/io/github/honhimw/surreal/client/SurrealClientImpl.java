package io.github.honhimw.surreal.client;

import io.github.honhimw.surreal.ReactiveSurrealClient;
import io.github.honhimw.surreal.SurrealClient;
import io.github.honhimw.surreal.TypedSurrealClient;
import io.github.honhimw.surreal.model.RecordId;
import io.github.honhimw.surreal.model.Response;
import io.github.honhimw.surreal.util.Helpers;
import jakarta.annotation.Nullable;

import java.util.Map;
import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-04-27
 */

class SurrealClientImpl implements SurrealClient {

    private final ReactiveSurrealClient client;

    SurrealClientImpl(ReactiveSurrealClient client) {
        this.client = client;
    }

    @Override
    public ReactiveSurrealClient reactive() {
        return client;
    }

    @Override
    public Response sql(String sql, @Nullable Map<String, Object> bindings) {
        return Helpers.blockNonNull(client.sql(sql, bindings));
    }

    @Override
    public byte[] sqlBytes(String sql, @Nullable Map<String, Object> bindings) {
        return Helpers.blockNonNull(client.sqlBytes(sql, bindings));
    }

    @Override
    public void ping() {
        client.ping().block();
    }

    @Override
    public RecordId relate(String table, RecordId in, RecordId out, Object data) {
        return Helpers.blockNonNull(client.relate(table, in, out, data));
    }

    @Override
    public <T> TypedSurrealClient<T, String> string(String table, Class<T> type) {
        return new TypedSurrealClientImpl<>(client.string(table, type));
    }

    @Override
    public <T> TypedSurrealClient<T, Long> i64(String table, Class<T> type) {
        return new TypedSurrealClientImpl<>(client.i64(table, type));
    }

    @Override
    public <T> TypedSurrealClient<T, UUID> uuid(String table, Class<T> type) {
        return new TypedSurrealClientImpl<>(client.uuid(table, type));
    }

    @Override
    public <T> TypedSurrealClient<T, RecordId> record(String table, Class<T> type) {
        return new TypedSurrealClientImpl<>(client.record(table, type));
    }

    @Override
    public void close() throws Exception {
        client.close();
    }

}
