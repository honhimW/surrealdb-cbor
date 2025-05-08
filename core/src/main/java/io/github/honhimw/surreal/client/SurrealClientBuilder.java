package io.github.honhimw.surreal.client;

import io.github.honhimw.surreal.QueryIdGenerator;
import io.github.honhimw.surreal.ReactiveSurrealClient;
import io.github.honhimw.surreal.SurrealClient;
import io.github.honhimw.surreal.util.Helpers;
import io.github.honhimw.surreal.util.ReactiveHttpUtils;

/**
 * @author honhimW
 * @since 2025-04-27
 */

public class SurrealClientBuilder {

    Protocol protocol = Protocol.HTTP;

    String host = "127.0.0.1";

    int port = 8000;

    String username;

    String password;

    String namespace;

    String database;

    String basePath = "";

    QueryIdGenerator idGenerator = new QueryIdGenerator.Default();

    ReactiveHttpUtils httpUtils = ReactiveHttpUtils.getInstance();

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

    public SurrealClientBuilder username(String username) {
        this.username = username;
        return this;
    }

    public SurrealClientBuilder password(String password) {
        this.password = password;
        return this;
    }

    public SurrealClientBuilder namespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public SurrealClientBuilder database(String database) {
        this.database = database;
        return this;
    }

    public SurrealClientBuilder basePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    public SurrealClientBuilder idGenerator(QueryIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
        return this;
    }

    public SurrealClientBuilder http(ReactiveHttpUtils http) {
        this.httpUtils = http;
        return this;
    }

    public ReactiveSurrealClient reactive() {
        Helpers.state(Helpers.isNotBlank(namespace), "namespace must not be blank.");
        Helpers.state(Helpers.isNotBlank(database), "database must not be blank.");
        return new ReactiveSurrealClientImpl(this);
    }

    public SurrealClient blocking() {
        ReactiveSurrealClient reactive = reactive();
        return new SurrealClientImpl(reactive);
    }

}
