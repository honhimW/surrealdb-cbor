package io.github.honhimw.surreal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.honhimw.surreal.cbor.SurrealCustomTag;

import java.io.Serializable;

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

}
