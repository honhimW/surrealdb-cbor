package io.github.honhimw.surreal.client;

import io.github.honhimw.surreal.SurrealClient;

/**
 * @author honhimW
 * @since 2025-04-27
 */

public class SurrealClientBuilder {

    private Protocol protocol = Protocol.HTTP;

    private String host = "127.0.0.1";

    private int port = 8000;

    private String basePath = "";

    public SurrealClientBuilder protocol(Protocol protocol) {
        this.protocol = protocol;
        return this;
    }

    public SurrealClientBuilder host(String host) {
        this.host = host;
        return this;
    }

    public SurrealClientBuilder port(int port) {
        this.port = port;
        return this;
    }

    public SurrealClientBuilder basePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    public SurrealClient build() {
        return new SurrealClientImpl(protocol, host, port, basePath);
    }

}
