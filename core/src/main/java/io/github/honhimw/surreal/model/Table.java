package io.github.honhimw.surreal.model;

import com.fasterxml.jackson.core.TreeNode;
import io.github.honhimw.surreal.cbor.SurrealCustomTag;

/**
 * @author honhimW
 * @since 2025-04-30
 */

public class Table implements CustomType<Table> {

    public static final SurrealCustomTag TAG = SurrealCustomTag.TAG_7;
    
    private final String name;

    public Table(String name) {
        this.name = name;
    }

    public static Table of(String name) {
        return new Table(name);
    }

    @Override
    public Table from(TreeNode treeNode) {
        return null;
    }
}
