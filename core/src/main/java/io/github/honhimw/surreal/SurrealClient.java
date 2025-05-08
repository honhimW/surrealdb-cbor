package io.github.honhimw.surreal;

import io.github.honhimw.surreal.client.SurrealClientBuilder;
import io.github.honhimw.surreal.model.Response;
import jakarta.annotation.Nullable;

import java.util.Collections;
import java.util.Map;

/**
 * @author honhimW
 * @since 2025-04-27
 */

public interface SurrealClient extends AutoCloseable {

    static SurrealClientBuilder builder() {
        return new SurrealClientBuilder();
    }

    ReactiveSurrealClient reactive();

    byte[] sqlBytes(String sql, @Nullable Map<String, Object> bindings);

    Response sql(String sql, @Nullable Map<String, Object> bindings);

    default byte[] sqlBytes(String sql) {
        return sqlBytes(sql, Collections.emptyMap());
    }

    default Response sql(String sql) {
        return sql(sql, Collections.emptyMap());
    }

}
