package io.github.honhimw.surreal.model;

import com.fasterxml.jackson.core.TreeNode;

import java.io.Serializable;

/**
 * @author honhimW
 * @since 2025-04-30
 * @param <C> class
 */

public interface CustomType<C extends CustomType<C>> extends Serializable {

    C from(TreeNode treeNode);

}
