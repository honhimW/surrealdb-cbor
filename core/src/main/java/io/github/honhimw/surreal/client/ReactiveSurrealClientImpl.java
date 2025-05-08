package io.github.honhimw.surreal.client;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.honhimw.surreal.QueryIdGenerator;
import io.github.honhimw.surreal.ReactiveSurrealClient;
import io.github.honhimw.surreal.ReactiveTypedSurreal;
import io.github.honhimw.surreal.model.Id;
import io.github.honhimw.surreal.model.RecordId;
import io.github.honhimw.surreal.model.Response;
import io.github.honhimw.surreal.model.Table;
import io.github.honhimw.surreal.util.CborUtils;
import io.github.honhimw.surreal.util.Helpers;
import io.github.honhimw.surreal.util.JsonUtils;
import io.github.honhimw.surreal.util.ReactiveHttpUtils;
import io.netty.handler.codec.http.HttpHeaderNames;
import jakarta.annotation.Nullable;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author honhimW
 * @since 2025-04-27
 */

class ReactiveSurrealClientImpl implements ReactiveSurrealClient {

    final Protocol protocol;

    final String host;

    final int port;

    final String username;

    final String password;

    final String namespace;

    final String database;

    final String basePath;

    final QueryIdGenerator idGenerator;

    final String url;

    final ReactiveHttpUtils httpOps;

    final String authorization;

    ReactiveSurrealClientImpl(SurrealClientBuilder builder) {
        this.protocol = builder.protocol;
        this.host = builder.host;
        this.port = builder.port;
        this.username = builder.username;
        this.password = builder.password;
        this.namespace = builder.namespace;
        this.database = builder.database;
        this.basePath = builder.basePath;
        this.idGenerator = builder.idGenerator;
        this.httpOps = builder.httpUtils;
        this.url = String.format("%s://%s:%d%s/rpc", protocol, host, port, basePath);
        if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
            this.authorization = "Basic " + Base64.getEncoder().encodeToString(String.format("%s:%s", username, password).getBytes(StandardCharsets.UTF_8));
        } else {
            this.authorization = null;
        }
    }

    @Override
    public Mono<Response> sql(String sql, @Nullable Map<String, Object> bindings) {
        return sqlBytes(sql, bindings)
            .map(bytes -> decodeAndThrowIfError(bytes, Response.class));
    }

    @Override
    public Mono<byte[]> sqlBytes(String sql, @Nullable Map<String, Object> bindings) {
        Map<String, Object> body = new LinkedHashMap<>(3);
        List<Object> params = new ArrayList<>();
        body.put("id", idGenerator.generate());
        body.put("method", "query");
        body.put("params", params);
        params.add(sql);
        if (Helpers.isNotEmpty(bindings)) {
            params.add(bindings);
        }

        return httpOps.execute(configurer -> configure(configurer, body))
            .handle(this::handleResult);
    }

    @Override
    public <T> ReactiveTypedSurreal<T, String> string(String table, Class<T> type) {
        return new ReactiveTypedSurrealImpl<>(this, type, Table.of(table), Id.Kind.STRING);
    }

    @Override
    public <T> ReactiveTypedSurreal<T, Long> i64(String table, Class<T> type) {
        return new ReactiveTypedSurrealImpl<>(this, type, Table.of(table), Id.Kind.LONG);
    }

    @Override
    public <T> ReactiveTypedSurreal<T, UUID> uuid(String table, Class<T> type) {
        return new ReactiveTypedSurrealImpl<>(this, type, Table.of(table), Id.Kind.UUID);
    }

    @Override
    public <T> ReactiveTypedSurreal<T, RecordId> record(Class<T> type) {
        return new ReactiveTypedSurrealImpl<>(this, type, null, null);
    }

    @Override
    public void close() throws Exception {
        httpOps.close();
    }

    void configure(ReactiveHttpUtils.Configurer configurer, Object body) {
        byte[] bytes = CborUtils.encode(body);
        if (authorization != null) {
            configurer.header(HttpHeaderNames.AUTHORIZATION, authorization);
        }
        configurer
            .post()
            .url(url)
            .header(HttpHeaderNames.ACCEPT, "application/cbor")
            .header("Surreal-NS", namespace)
            .header("Surreal-DB", database)
            .body(payload -> payload.binary(binary -> binary
                .bytes(bytes, "application/cbor")
            ));
    }

    void handleResult(ReactiveHttpUtils.HttpResult httpResult, SynchronousSink<byte[]> sink) {
        if (httpResult.isOK()) {
            sink.next(httpResult.content());
        } else {
            sink.error(new IllegalStateException(httpResult.toString()));
        }
    }

    <T> T decodeAndThrowIfError(byte[] bytes, Class<T> type) {
        JsonNode jsonNode = CborUtils.readTree(bytes);
        JsonNode errorNode = jsonNode.at("/error");
        if (Helpers.exists(errorNode)) {
            throw new IllegalStateException(errorNode.toString());
        }
        try {
            return CborUtils.mapper().treeToValue(jsonNode, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
