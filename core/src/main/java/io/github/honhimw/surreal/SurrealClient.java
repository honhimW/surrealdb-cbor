package io.github.honhimw.surreal;

import io.github.honhimw.surreal.client.SurrealClientBuilder;

/**
 * @author honhimW
 * @since 2025-04-27
 */

public interface SurrealClient {

    static SurrealClientBuilder builder() {
        return new SurrealClientBuilder();
    }

    Object sql(String sql);

    byte[] sqlBytes(String sql);

}
