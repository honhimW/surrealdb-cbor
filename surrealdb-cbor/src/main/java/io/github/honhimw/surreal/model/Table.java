package io.github.honhimw.surreal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.honhimw.surreal.cbor.SurrealCustomTag;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-04-30
 */

public class Table implements Serializable {

    public static final SurrealCustomTag TAG = SurrealCustomTag.TAG_7;

    public final String name;

    @JsonCreator
    public Table(@JsonProperty("name") String name) {
        this.name = name;
    }

    public static Table of(String name) {
        return new Table(name);
    }

    public RecordId id(Id id) {
        return new RecordId(this, id);
    }

    public RecordId id(String id) {
        return new RecordId(this, Id.of(id));
    }

    public RecordId id(long id) {
        return new RecordId(this, Id.of(id));
    }

    public RecordId id(UUID id) {
        return new RecordId(this, Id.of(id));
    }

}
