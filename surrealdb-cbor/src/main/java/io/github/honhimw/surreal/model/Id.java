package io.github.honhimw.surreal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-04-28
 */

public class Id implements Serializable {

    public static Id of(String id) {
        return new Id(Kind.STRING, id);
    }

    public static Id of(long id) {
        return new Id(Kind.LONG, id);
    }

    public static Id of(UUID id) {
        return new Id(Kind.UUID, id);
    }

    public final Kind kind;

    public final Object value;

    @JsonCreator
    public Id(@JsonProperty("kind") Kind kind, @JsonProperty("value") Object value) {
        this.kind = kind;
        this.value = value;
    }

    @JsonIgnore
    public boolean isLong() {
        return kind == Kind.LONG;
    }

    @JsonIgnore
    public boolean isString() {
        return kind == Kind.STRING;
    }

    @JsonIgnore
    public boolean isUuid() {
        return kind == Kind.UUID;
    }

    public long longValue() {
        if (kind == Kind.LONG) {
            return (Long) value;
        }
        throw new IllegalStateException("Not a long id");
    }

    public String stringValue() {
        if (kind == Kind.STRING) {
            return (String) value;
        }
        throw new IllegalStateException("Not a String id");
    }

    public UUID uuidValue() {
        if (kind == Kind.UUID) {
            return (UUID) value;
        }
        throw new IllegalStateException("Not a UUID id");
    }

    @Override
    public String toString() {
        switch (kind) {
            case STRING:
                return "⟨" + value.toString() + "⟩";
            case UUID:
                return "u'" + value.toString() + "'";
            default:
                return String.valueOf(value);
        }
    }

    public enum Kind {
        LONG, STRING, UUID, UNKNOWN
    }

}
