package io.github.honhimw.surreal.client;

import io.github.honhimw.surreal.ReactiveSurrealClient;
import io.github.honhimw.surreal.SurrealClient;
import io.github.honhimw.surreal.model.Response;
import io.github.honhimw.surreal.util.Helpers;
import jakarta.annotation.Nullable;

import java.util.Map;

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
    public void close() throws Exception {
        client.close();
    }

}
