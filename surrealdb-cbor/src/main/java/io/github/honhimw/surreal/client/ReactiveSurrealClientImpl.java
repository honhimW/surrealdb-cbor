package io.github.honhimw.surreal.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.POJONode;
import io.github.honhimw.surreal.QueryIdGenerator;
import io.github.honhimw.surreal.ReactiveSurrealClient;
import io.github.honhimw.surreal.ReactiveTypedSurrealClient;
import io.github.honhimw.surreal.SurrealClient;
import io.github.honhimw.surreal.model.*;
import io.github.honhimw.surreal.util.CborUtils;
import io.github.honhimw.surreal.util.Helpers;
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
    public SurrealClient blocking() {
        return new SurrealClientImpl(this);
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
    public Mono<Void> ping() {
        Map<String, Object> body = new LinkedHashMap<>(2);
        body.put("id", idGenerator.generate());
        body.put("method", "ping");
        return httpOps.execute(configurer -> configure(configurer, body))
            .handle(this::handleResult)
            .doOnNext(bytes -> decodeAndThrowIfError(bytes, JsonNode.class))
            .then();
    }

    @Override
    public Mono<RecordId> relate(String table, RecordId in, RecordId out, Object data) {
        Helpers.state(Helpers.isNotBlank(table), "table should not be blank");
        Map<String, Object> params = new LinkedHashMap<>(3);
        params.put("in", in);
        params.put("out", out);
        params.put("data", data);
        String sql = String.format("RELATE ONLY $in -> %s -> $out CONTENT $data RETURN id;", table);
        return sqlBytes(sql, params)
            .flatMap(bytes -> {
                JsonNode jsonNode = decodeAndThrowIfError(bytes, JsonNode.class);
                JsonNode status = jsonNode.at("/result/0/status");
                if (!Helpers.exists(status)) {
                    return Mono.error(new IllegalStateException("Unexpected result without statement result"));
                }
                JsonNode result = jsonNode.at("/result/0/result");
                if (!Result.Status.OK.validate(status.textValue())) {
                    return Mono.error(new IllegalStateException("Relate failed: " + result.asText()));
                }
                POJONode pojoNode = result.require();
                return Mono.just(((RecordId) pojoNode.getPojo()));
            });
    }

    @Override
    public <T> ReactiveTypedSurrealClient<T, String> string(String table, Class<T> type) {
        return new ReactiveTypedSurrealClientImpl<>(this, type, Table.of(table), Id.Kind.STRING);
    }

    @Override
    public <T> ReactiveTypedSurrealClient<T, Long> i64(String table, Class<T> type) {
        return new ReactiveTypedSurrealClientImpl<>(this, type, Table.of(table), Id.Kind.LONG);
    }

    @Override
    public <T> ReactiveTypedSurrealClient<T, UUID> uuid(String table, Class<T> type) {
        return new ReactiveTypedSurrealClientImpl<>(this, type, Table.of(table), Id.Kind.UUID);
    }

    @Override
    public <T> ReactiveTypedSurrealClient<T, RecordId> record(String table, Class<T> type) {
        return new ReactiveTypedSurrealClientImpl<>(this, type, Table.of(table), null);
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
