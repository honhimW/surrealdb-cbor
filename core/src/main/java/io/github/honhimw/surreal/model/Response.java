package io.github.honhimw.surreal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * @author honhimW
 * @since 2025-05-06
 */

public class Response implements Serializable, Iterable<Result> {

    public final List<Result> result;

    @JsonCreator
    public Response(@JsonProperty("result") List<Result> result) {
        this.result = result;
    }

    @Nonnull
    @Override
    public Iterator<Result> iterator() {
        return result.iterator();
    }

    public Result first() {
        return get(0);
    }

    public Result last() {
        return get(result.size() - 1);
    }

    public int size() {
        return result.size();
    }

    public Result get(int index) {
        return result.get(index);
    }

}
