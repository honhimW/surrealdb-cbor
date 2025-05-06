package io.github.honhimw.surreal.model;

import io.github.honhimw.surreal.cbor.SurrealCustomTag;

import java.io.Serializable;

/**
 * @author honhimW
 * @since 2025-04-30
 */

public class Table implements Serializable {

    public static final SurrealCustomTag TAG = SurrealCustomTag.TAG_7;

    public final String name;

    public Table(String name) {
        this.name = name;
    }

    public static Table of(String name) {
        return new Table(name);
    }

}
