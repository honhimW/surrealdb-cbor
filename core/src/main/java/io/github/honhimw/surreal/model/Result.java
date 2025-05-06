package io.github.honhimw.surreal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import io.github.honhimw.surreal.util.CborUtils;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Result implements Serializable {

    public final Object result;

    public final String status;

    public final String time;

    @JsonCreator
    public Result(@JsonProperty("result") Object result, @JsonProperty("status") String status, @JsonProperty("time") String time) {
        this.result = result;
        this.status = status;
        this.time = time;
    }

    public <R> R as(Class<R> type) {
        return CborUtils.mapper().convertValue(result, type);
    }

    public <R> R as(JavaType type) {
        return CborUtils.mapper().convertValue(result, type);
    }

    public <R> R as(TypeReference<R> type) {
        return CborUtils.mapper().convertValue(result, type);
    }

    public <R> R as(Type type) {
        CBORMapper mapper = CborUtils.mapper();
        return CborUtils.mapper().convertValue(result, mapper.constructType(type));
    }

}
