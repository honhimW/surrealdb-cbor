package io.github.honhimw.surreal.model;

import io.github.honhimw.surreal.cbor.SurrealCustomTag;

import java.io.Serializable;

/**
 * @author honhimW
 * @since 2025-04-30
 */

public enum Option implements Serializable {

    NONE;

    public static final SurrealCustomTag TAG = SurrealCustomTag.TAG_6;

}
