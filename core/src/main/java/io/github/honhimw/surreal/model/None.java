package io.github.honhimw.surreal.model;

import com.fasterxml.jackson.core.TreeNode;
import io.github.honhimw.surreal.cbor.SurrealCustomTag;

/**
 * @author honhimW
 * @since 2025-04-30
 */

public class None implements CustomType<None> {

    public static final SurrealCustomTag TAG = SurrealCustomTag.TAG_6;

    public static final None $ = new None();

    @Override
    public None from(TreeNode treeNode) {
        return $;
    }
}
