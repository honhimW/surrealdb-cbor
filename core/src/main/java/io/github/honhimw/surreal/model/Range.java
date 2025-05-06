package io.github.honhimw.surreal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author honhimW
 * @since 2025-04-28
 */

public class Range implements Serializable {

    public final Value lower;

    public final Value upper;

    @JsonCreator
    public Range(@JsonProperty("lower") Value lower, @JsonProperty("upperBound") Value upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public static class Value implements Serializable {

        public final int value;

        public Value(int value) {
            this.value = value;
        }

        public boolean isIncluded() {
            return false;
        }

        public boolean isExcluded() {
            return false;
        }

    }

    public static class Included extends Value {

        public Included(int value) {
            super(value);
        }

        public boolean isIncluded() {
            return true;
        }

    }

    public static class Excluded extends Value {

        public Excluded(int value) {
            super(value);
        }

        public boolean isExcluded() {
            return true;
        }

    }

}
