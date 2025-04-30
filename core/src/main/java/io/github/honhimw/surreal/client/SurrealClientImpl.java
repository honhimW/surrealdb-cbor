package io.github.honhimw.surreal.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import io.github.honhimw.surreal.SurrealClient;
import io.github.honhimw.surreal.util.ReactiveHttpUtils;
import io.netty.handler.codec.http.HttpHeaderNames;

import java.util.*;

/**
 * @author honhimW
 * @since 2025-04-27
 */

class SurrealClientImpl implements SurrealClient {

    private final Protocol protocol;

    private final String host;

    private final int port;

    private final String basePath;

    private final CBORMapper cborMapper = new CBORMapper();

    private final String url;

    SurrealClientImpl(Protocol protocol, String host, int port, String basePath) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.basePath = basePath;
        this.url = String.format("%s://%s:%d%s/rpc", protocol, host, port, basePath);
    }

    @Override
    public Object sql(String sql) {
        try {
            byte[] bytes = sqlBytes(sql);
            return cborMapper.readValue(bytes, Object.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] sqlBytes(String sql) {
        try {
            Map<String, Object> body = new LinkedHashMap<>();
            List<Object> params = new ArrayList<>();
            body.put("id", 1);
            body.put("method", "query");
            params.add(sql);
            body.put("params", params);
            byte[] bytes = cborMapper.writeValueAsBytes(body);
            ReactiveHttpUtils.HttpResult httpResult = ReactiveHttpUtils.getInstance().post(url, configurer -> configurer
                .header(HttpHeaderNames.ACCEPT, "application/cbor")
                .header("Surreal-NS", "surrealdb")
                .header("Surreal-DB", "surrealdb")
                .header(HttpHeaderNames.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString("root:root".getBytes()))
                .body(payload -> payload.binary(binary -> binary
                    .bytes(bytes, "application/cbor")
                ))
            );
            return httpResult.content();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
