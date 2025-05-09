package io.github.honhimw.surreal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.github.honhimw.surreal.util.JsonUtils;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Result implements Serializable {

    public final String status;

    public final String time;

    public final JsonNode result;

    @JsonCreator
    public Result(@JsonProperty("result") JsonNode result, @JsonProperty("status") String status, @JsonProperty("time") String time) {
        this.result = result;
        this.status = status;
        this.time = time;
    }

    @JsonIgnore
    public boolean isOkay() {
        return Status.OK.validate(this.status);
    }

    @JsonIgnore
    public boolean isError() {
        return Status.ERR.validate(this.status);
    }

    public <R> R as(Class<R> type) {
        return JsonUtils.mapper().convertValue(result, type);
    }

    public <R> R as(JavaType type) {
        return JsonUtils.mapper().convertValue(result, type);
    }

    public <R> R as(TypeReference<R> type) {
        return JsonUtils.mapper().convertValue(result, type);
    }

    public <R> R as(Type type) {
        JsonMapper mapper = JsonUtils.mapper();
        return mapper.convertValue(result, mapper.constructType(type));
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }

    public String toPrettyString() {
        return JsonUtils.toPrettyJson(this);
    }


    public enum Status implements Serializable {

        OK, ERR,
        ;

        public boolean validate(String status) {
            return this.name().equals(status);
        }

    }

}
